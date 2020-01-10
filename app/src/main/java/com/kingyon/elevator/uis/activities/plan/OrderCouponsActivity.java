package com.kingyon.elevator.uis.activities.plan;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.blankj.utilcode.util.LogUtils;
import com.kingyon.elevator.R;
import com.kingyon.elevator.constants.Constants;
import com.kingyon.elevator.entities.CouponItemEntity;
import com.kingyon.elevator.nets.CustomApiCallback;
import com.kingyon.elevator.nets.NetService;
import com.kingyon.elevator.uis.adapters.CouponsAdapter;
import com.kingyon.elevator.utils.CommonUtil;
import com.kingyon.elevator.utils.MyToastUtils;
import com.leo.afbaselibrary.nets.exceptions.ApiException;
import com.leo.afbaselibrary.uis.activities.BaseStateRefreshingLoadingActivity;
import com.leo.afbaselibrary.uis.adapters.MultiItemTypeAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Created by GongLi on 2019/1/3.
 * Email：lc824767150@163.com
 */

public class OrderCouponsActivity extends BaseStateRefreshingLoadingActivity<Object> {

    @BindView(R.id.tv_total_price)
    TextView tvTotalPrice;
    @BindView(R.id.tv_discounts)
    TextView tvDiscounts;
    @BindView(R.id.tv_order_price)
    TextView tvOrderPrice;

    private float totalPrice;
    private String type;

    private boolean choosedInit;
    private ArrayList<CouponItemEntity> choosedCoupons;
    private Boolean isMaxOrderMoney = false;//是否达到最大优惠金额

    @Override
    protected String getTitleText() {
        totalPrice = getIntent().getFloatExtra(CommonUtil.KEY_VALUE_1, 0);
        type = getIntent().getStringExtra(CommonUtil.KEY_VALUE_2);
        choosedCoupons = getIntent().getParcelableArrayListExtra(CommonUtil.KEY_VALUE_3);
        return "优惠券";
    }

    @Override
    public int getContentViewId() {
        return R.layout.activity_order_coupons;
    }

    @Override
    protected void initViews(Bundle savedInstanceState) {
        super.initViews(savedInstanceState);
        updatePriceUI(totalPrice, 0);
    }

    @Override
    protected MultiItemTypeAdapter<Object> getAdapter() {
        return new CouponsAdapter(this, mItems);
    }

    @Override
    protected void loadData(final int page) {
        if (choosedCoupons == null) {
            String choosedCouponType = getChoosedCouponType();
            if (TextUtils.equals(Constants.CouponType.VOUCHER, choosedCouponType)) {
                choosedCoupons = new ArrayList<>();
                choosedCoupons.addAll(getChoosedVoucherCoupons());
            } else if (TextUtils.equals(Constants.CouponType.DISCOUNT, choosedCouponType)) {
                choosedCoupons = new ArrayList<>();
                choosedCoupons.add(getChoosedDiscountCoupon());
            }
        }
        NetService.getInstance().getMeetCoupons(totalPrice, type)
                .compose(this.<List<CouponItemEntity>>bindLifeCycle())
                .subscribe(new CustomApiCallback<List<CouponItemEntity>>() {
                    @Override
                    protected void onResultError(ApiException ex) {
                        showToast(ex.getDisplayMessage());
                        loadingComplete(false, 1);
                    }

                    @Override
                    public void onNext(List<CouponItemEntity> couponItemEntities) {
                        if (FIRST_PAGE == page) {
                            mItems.clear();
                        }
                        if (couponItemEntities != null) {
                            List<CouponItemEntity> discountCoupons = new ArrayList<>();
                            List<CouponItemEntity> voucherCoupons = new ArrayList<>();
                            for (CouponItemEntity item : couponItemEntities) {
                                if (choosedCoupons != null) {
                                    for (CouponItemEntity choosedCache : choosedCoupons) {
                                        if (item.getObjctId() == choosedCache.getObjctId()) {
                                            item.setChoosed(true);
                                        }
                                    }
                                }
                                if (TextUtils.equals(Constants.CouponType.VOUCHER, item.getCoupontype())) {
                                    voucherCoupons.add(item);
                                } else {
                                    discountCoupons.add(item);
                                }
                            }
                            choosedInit = true;
                            choosedCoupons = null;
                            if (discountCoupons.size() > 0) {
                                mItems.add(Constants.CouponType.DISCOUNT);
                                mItems.addAll(discountCoupons);
                            }
                            if (voucherCoupons.size() > 0) {
                                mItems.add(Constants.CouponType.VOUCHER);
                                mItems.addAll(voucherCoupons);
                            }
                        }
                        loadingComplete(true, 1);
                        calculateCouponPrice();
                    }
                });
    }

    @Override
    public void onItemClick(View view, RecyclerView.ViewHolder holder, Object item, int position) {
        super.onItemClick(view, holder, item, position);
        if (item != null && item instanceof CouponItemEntity) {
            CouponItemEntity entity = (CouponItemEntity) item;
            if (entity.isChoosed()) {
                entity.setChoosed(false);
            } else {
                if (isMaxOrderMoney) {
                    MyToastUtils.showShort("已达到最大优惠金额");
                    return;
                }
                setCouponChoosed(entity);
            }
            mAdapter.notifyDataSetChanged();
            calculateCouponPrice(entity);
        }
    }

    private void setCouponChoosed(CouponItemEntity entity) {
        String choosedCouponType = getChoosedCouponType();
        if (TextUtils.isEmpty(choosedCouponType) || TextUtils.equals(choosedCouponType, entity.getCoupontype())) {
            if (TextUtils.equals(Constants.CouponType.VOUCHER, choosedCouponType)) {
                List<CouponItemEntity> choosedVoucherCoupons = getChoosedVoucherCoupons();
                float voucherSum = 0;
                for (CouponItemEntity item : choosedVoucherCoupons) {
                    voucherSum += item.getMoney();
                }
                float beLeftMoney = totalPrice - getChoosedVoucherCouponConditionPrice();
                LogUtils.d("剩余的金额：", beLeftMoney, totalPrice,
                        getChoosedVoucherCouponConditionPrice(), entity.getCouponCondition());
                if (beLeftMoney < entity.getCouponCondition()) {
                    showToast("剩余金额没有达到使用门槛");
                } else {
                    if (getChoosedVoucherCouponConditionPrice() + entity.getCouponCondition() <= totalPrice) {
                        entity.setChoosed(true);
                    } else {
                        showToast("总金额没有达到使用门槛");
                    }
                }
            } else if (TextUtils.equals(Constants.CouponType.DISCOUNT, choosedCouponType)) {
                showToast("只能使用一张折扣券");
            } else {
                if (entity.getCouponCondition() <= totalPrice) {
                    entity.setChoosed(true);
                } else {
                    showToast("总金额没有达到使用门槛");
                }
            }
        } else {
            showToast("只能选择一种类型的优惠券");
        }
    }

    private void calculateCouponPrice(CouponItemEntity entity) {
        String choosedCouponType = getChoosedCouponType();
        if (TextUtils.equals(Constants.CouponType.VOUCHER, choosedCouponType)) {
            List<CouponItemEntity> choosedVoucherCoupons = getChoosedVoucherCoupons();
            float voucherSum = 0;
            for (CouponItemEntity item : choosedVoucherCoupons) {
                voucherSum += item.getMoney();
            }
            updatePriceUI(totalPrice, voucherSum, entity);
        } else if (TextUtils.equals(Constants.CouponType.DISCOUNT, choosedCouponType)) {
            CouponItemEntity choosedDiscountCoupon = getChoosedDiscountCoupon();
            updatePriceUI(totalPrice, (1 - choosedDiscountCoupon.getDiscount() / 10) * totalPrice, entity);
        } else {
            updatePriceUI(totalPrice, 0, entity);
        }
    }


    private void calculateCouponPrice() {
        String choosedCouponType = getChoosedCouponType();
        if (TextUtils.equals(Constants.CouponType.VOUCHER, choosedCouponType)) {
            List<CouponItemEntity> choosedVoucherCoupons = getChoosedVoucherCoupons();
            float voucherSum = 0;
            for (CouponItemEntity item : choosedVoucherCoupons) {
                voucherSum += item.getMoney();
            }
            updatePriceUI(totalPrice, voucherSum);
        } else if (TextUtils.equals(Constants.CouponType.DISCOUNT, choosedCouponType)) {
            CouponItemEntity choosedDiscountCoupon = getChoosedDiscountCoupon();
            updatePriceUI(totalPrice, (1 - choosedDiscountCoupon.getDiscount() / 10) * totalPrice);
        } else {
            updatePriceUI(totalPrice, 0);
        }
    }

    private String getChoosedCouponType() {
        String result = null;
        for (Object obj : mItems) {
            if (obj instanceof CouponItemEntity) {
                CouponItemEntity item = (CouponItemEntity) obj;
                if (item.isChoosed()) {
                    result = item.getCoupontype();
                    break;
                }
            }
        }
        return result;
    }

    private CouponItemEntity getChoosedDiscountCoupon() {
        CouponItemEntity result = null;
        for (Object obj : mItems) {
            if (obj instanceof CouponItemEntity) {
                CouponItemEntity item = (CouponItemEntity) obj;
                if (item.isChoosed()) {
                    result = item;
                    break;
                }
            }
        }
        return result;
    }

    private List<CouponItemEntity> getChoosedVoucherCoupons() {
        List<CouponItemEntity> result = new ArrayList<>();
        for (Object obj : mItems) {
            if (obj instanceof CouponItemEntity) {
                CouponItemEntity item = (CouponItemEntity) obj;
                if (item.isChoosed()) {
                    result.add(item);
                }
            }
        }
        return result;
    }

    private float getChoosedVoucherCouponConditionPrice() {
        float result = 0;
        for (Object obj : mItems) {
            if (obj instanceof CouponItemEntity) {
                CouponItemEntity item = (CouponItemEntity) obj;
                if (item.isChoosed()) {
                    result += item.getCouponCondition();
                }
            }
        }
        return result;
    }

    private void updatePriceUI(float totalPrice, float discount, CouponItemEntity entity) {
        tvTotalPrice.setText(String.format("总金额：¥%s", CommonUtil.getMayTwoFloat(totalPrice)));
        float resultPrice = totalPrice - discount;
        if (resultPrice > 0) {
            isMaxOrderMoney = false;
            tvDiscounts.setText(String.format("优惠金额：%s¥", CommonUtil.getMayTwoFloat(discount)));
            tvOrderPrice.setText(String.format("订单金额：%s¥", CommonUtil.getMayTwoFloat(resultPrice)));
        } else {
            isMaxOrderMoney = true;
            tvDiscounts.setText(String.format("优惠金额：%s¥", CommonUtil.getMayTwoFloat(totalPrice)));
            tvOrderPrice.setText(String.format("订单金额：%s¥", CommonUtil.getMayTwoFloat(0)));
        }
    }

    private void updatePriceUI(float totalPrice, float discount) {
        tvTotalPrice.setText(String.format("总金额：¥%s", CommonUtil.getMayTwoFloat(totalPrice)));
        float resultPrice = totalPrice - discount;
        if (resultPrice > 0) {
            isMaxOrderMoney = false;
            tvDiscounts.setText(String.format("优惠金额：%s¥", CommonUtil.getMayTwoFloat(discount)));
            tvOrderPrice.setText(String.format("订单金额：%s¥", CommonUtil.getMayTwoFloat(resultPrice)));
        } else {
            isMaxOrderMoney = true;
            tvDiscounts.setText(String.format("优惠金额：%s¥", CommonUtil.getMayTwoFloat(totalPrice)));
            tvOrderPrice.setText(String.format("订单金额：%s¥", CommonUtil.getMayTwoFloat(0)));
        }
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent();
        String choosedCouponType = getChoosedCouponType();
        ArrayList<CouponItemEntity> datas = new ArrayList<>();
        if (!choosedInit && choosedCoupons != null) {
            datas.addAll(choosedCoupons);
        } else {
            if (TextUtils.equals(Constants.CouponType.VOUCHER, choosedCouponType)) {
                datas.addAll(getChoosedVoucherCoupons());
            } else if (TextUtils.equals(Constants.CouponType.DISCOUNT, choosedCouponType)) {
                datas.add(getChoosedDiscountCoupon());
            }
        }
        intent.putParcelableArrayListExtra(CommonUtil.KEY_VALUE_1, datas);
        setResult(RESULT_OK, intent);
        super.onBackPressed();
    }

    @NonNull
    @Override
    protected String getEmptyTip() {
        return "暂无可用优惠券";
    }

    @Override
    protected boolean isShowDivider() {
        return false;
    }
}

package com.kingyon.elevator.uis.activities.user;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;

import com.kingyon.elevator.R;
import com.kingyon.elevator.application.AppContent;
import com.kingyon.elevator.constants.Constants;
import com.kingyon.elevator.entities.CouponItemEntity;
import com.kingyon.elevator.entities.entities.ConentEntity;
import com.kingyon.elevator.nets.CustomApiCallback;
import com.kingyon.elevator.nets.NetService;
import com.kingyon.elevator.uis.widgets.EditCountViewIntUpDown;
import com.kingyon.elevator.utils.CommonUtil;
import com.kingyon.elevator.utils.RoleUtils;
import com.leo.afbaselibrary.nets.exceptions.ApiException;
import com.leo.afbaselibrary.nets.exceptions.ResultException;
import com.leo.afbaselibrary.uis.activities.BaseStateRefreshingLoadingActivity;
import com.leo.afbaselibrary.uis.adapters.BaseAdapter;
import com.leo.afbaselibrary.uis.adapters.MultiItemTypeAdapter;
import com.leo.afbaselibrary.uis.adapters.holders.CommonHolder;
import com.leo.afbaselibrary.utils.TimeUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.OnClick;

/**
 * Created by GongLi on 2019/1/10.
 * Email：lc824767150@163.com
 */

public class DonateChooseActivity extends BaseStateRefreshingLoadingActivity<CouponItemEntity> {

    private ArrayList<CouponItemEntity> choosedCoupons;
    private boolean notFirstIn;

    @Override
    protected String getTitleText() {
        return "选择";
    }

    @Override
    public int getContentViewId() {
        return R.layout.activity_donate_choose;
    }

    @Override
    protected MultiItemTypeAdapter<CouponItemEntity> getAdapter() {
        return new BaseAdapter<CouponItemEntity>(this, R.layout.activity_donate_choose_item_new, mItems) {
            @Override
            protected void convert(CommonHolder holder, CouponItemEntity item, int position) {
//                holder.setSelected(R.id.ll_bg, item.isChoosed());

                boolean discount = TextUtils.equals(Constants.CouponType.DISCOUNT, item.getCoupontype());
                holder.setTextNotHide(R.id.tv_discounts_1, discount ? CommonUtil.getMayTwoFloat(item.getDiscount()) : "￥");
                holder.setTextNotHide(R.id.tv_discounts_2, discount ? "折" : CommonUtil.getMayTwoFloat(item.getMoney()));
                holder.setTextSize(R.id.tv_discounts_1, discount ? 32 : 14);
                holder.setTextSize(R.id.tv_discounts_2, discount ? 14 : 32);
                holder.setTextNotHide(R.id.tv_condition, String.format("满%s可用", CommonUtil.getMayTwoFloat(item.getCouponCondition())));
                holder.setTextNotHide(R.id.tv_name, discount ? "折扣券" : "代金券");
                holder.setBackgroundRes(R.id.ll_juan, discount ? R.mipmap.bg_wallet_discount:R.mipmap.bg_wallet_voucher);


                EditCountViewIntUpDown editCountView = holder.getView(R.id.ecv_count);
                editCountView.removeOnNumberChange();
                editCountView.setOnNumberChange(new EditCountViewIntUpDown.OnNumberChange() {
                    @Override
                    public void onChange(int num, final int position, EditText text) {
                        if (position >= 0 && position < mItems.size()) {
                            CouponItemEntity couponItemEntity = mItems.get(position);
                            couponItemEntity.setCacheCount(num);
                            mItems.get(position).setCacheCount(num);
                        }
                    }
                }, position);
                editCountView.setCanEdit(true);
                editCountView.setMaxCount(item.getCouponsCount());
                editCountView.setMinCount(0);
                editCountView.setCurrentCount(item.getCacheCount());
//                holder.setTextNotHide(R.id.tv_number, String.format("×%s", item.getCouponsCount()));
//                holder.setVisible(R.id.tv_number, RoleUtils.getInstance().roleBeTarget(Constants.RoleType.PARTNER, AppContent.getInstance().getMyUserRole()));

                String adType = item.getAdType() != null ? item.getAdType() : "";
                boolean business = adType.contains(Constants.PLAN_TYPE.BUSINESS);
                boolean diy = adType.contains(Constants.PLAN_TYPE.DIY);
                boolean info = adType.contains(Constants.PLAN_TYPE.INFORMATION);
                String result;
                if (business && diy && info) {
                    result = "全部";
                } else if (business && diy && !info) {
                    result = "商业广告,DIY";
                } else if (business && !diy && info) {
                    result = "商业广告,便民服务";
                } else if (!business && diy && info) {
                    result = "DIY,便民服务";
                } else if (business && !diy && !info) {
                    result = "仅商业广告可用";
                } else if (!business && diy && !info) {
                    result = "仅DIY可用";
                } else if (!business && !diy && info) {
                    result = "仅便民服务可用";
                } else {
                    result = "全部";
                }
                holder.setTextNotHide(R.id.tv_range, String.format("适用：%s", result));
                holder.setTextNotHide(R.id.tv_expier_time, String.format("过期时间：%s", TimeUtil.getYMdTime(item.getExpiredDate())));
            }
        };
    }

    @Override
    public void onItemClick(View view, RecyclerView.ViewHolder holder, CouponItemEntity item, int position) {
        super.onItemClick(view, holder, item, position);
//        if (item != null) {
////            item.setChoosed(!item.isChoosed());
////            mAdapter.notifyItemChanged(position);
//            for (CouponItemEntity entity : mItems) {
//                if (entity == item)
//                    entity.setChoosed(!entity.isChoosed());
//            }
//            mAdapter.notifyDataSetChanged();
//        }
    }

    @Override
    protected void loadData(final int page) {
        if (FIRST_PAGE == page) {
            choosedCoupons = new ArrayList<>();
            choosedCoupons.addAll(getChoosedCoupons());
        } else {
            choosedCoupons = null;
        }
        NetService.getInstance().getCoupons(Constants.CouponStatus.NORMAL, page)
                .compose(this.<ConentEntity<CouponItemEntity>>bindLifeCycle())
                .subscribe(new CustomApiCallback<ConentEntity<CouponItemEntity>>() {
                    @Override
                    protected void onResultError(ApiException ex) {
                        showToast(ex.getDisplayMessage());
                        loadingComplete(false, 100000);
                    }

                    @Override
                    public void onNext(ConentEntity<CouponItemEntity> couponItemEntityPageListEntity) {
                        if (couponItemEntityPageListEntity == null) {
                            throw new ResultException(9001, "返回参数异常");
                        }
                        List<CouponItemEntity> datas = couponItemEntityPageListEntity.getContent() != null ? couponItemEntityPageListEntity.getContent() : new ArrayList<CouponItemEntity>();
                        if (FIRST_PAGE == page) {
                            mItems.clear();
                        }
                        for (CouponItemEntity item : datas) {
                            if (choosedCoupons != null) {
                                for (CouponItemEntity choosedCache : choosedCoupons) {
                                    if (item.getObjctId() == choosedCache.getObjctId()) {
                                        item.setChoosed(true);
                                    }
                                }
                            }
                        }
                        choosedCoupons = null;
                        mItems.addAll(datas);
                        loadingComplete(true, couponItemEntityPageListEntity.getTotalPages());
                    }
                });
    }

    public ArrayList<CouponItemEntity> getChoosedCoupons() {
        ArrayList<CouponItemEntity> chooseds = new ArrayList<>();
        for (Object object : mItems) {
            if (object instanceof CouponItemEntity) {
                CouponItemEntity coupon = (CouponItemEntity) object;
                if (coupon.getCacheCount() != 0) {
                    chooseds.add(coupon);
                }
            }
        }
        return chooseds;
    }

    public CouponItemEntity getChoosedCoupon() {
        CouponItemEntity choosed = null;
        for (Object object : mItems) {
            if (object instanceof CouponItemEntity) {
                CouponItemEntity coupon = (CouponItemEntity) object;
                if (coupon.isChoosed()) {
                    choosed = coupon;
                }
            }
        }
        return choosed;
    }

    @OnClick(R.id.tv_ensure)
    public void onViewClicked() {
        ArrayList<CouponItemEntity> choosedCoupons = getChoosedCoupons();
        if (choosedCoupons.size() < 1) {
            showToast("还没有选择任何优惠券");
            return;
        }
        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList(CommonUtil.KEY_VALUE_1, choosedCoupons);
        startActivityForResult(DonateCouponsActivity.class, CommonUtil.REQ_CODE_1, bundle);
    }

    @Override
    protected void onResume() {
//        if (notFirstIn) {
//            autoRefresh();
//        } else {
//            notFirstIn = true;
//        }
        super.onResume();
        if (!RoleUtils.getInstance().roleBeTarget(Constants.RoleType.PARTNER, AppContent.getInstance().getMyUserRole())) {
            showToast("合伙人才能赠送优惠券");
            finish();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (RESULT_OK == resultCode && CommonUtil.REQ_CODE_1 == requestCode) {
            setResult(RESULT_OK);
            finish();
        }
    }

    @Override
    protected boolean isShowDivider() {
        return false;
    }
}

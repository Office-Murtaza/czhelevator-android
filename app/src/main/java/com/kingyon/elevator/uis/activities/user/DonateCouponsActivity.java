package com.kingyon.elevator.uis.activities.user;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.widget.EditText;
import android.widget.TextView;

import com.kingyon.elevator.R;
import com.kingyon.elevator.application.AppContent;
import com.kingyon.elevator.constants.Constants;
import com.kingyon.elevator.data.DataSharedPreferences;
import com.kingyon.elevator.entities.CouponItemEntity;
import com.kingyon.elevator.entities.UserEntity;
import com.kingyon.elevator.nets.CustomApiCallback;
import com.kingyon.elevator.nets.NetService;
import com.kingyon.elevator.utils.CommonUtil;
import com.kingyon.elevator.utils.KeyBoardUtils;
import com.kingyon.elevator.utils.RoleUtils;
import com.leo.afbaselibrary.nets.exceptions.ApiException;
import com.leo.afbaselibrary.uis.activities.BaseStateRefreshingLoadingActivity;
import com.leo.afbaselibrary.uis.adapters.BaseAdapter;
import com.leo.afbaselibrary.uis.adapters.MultiItemTypeAdapter;
import com.leo.afbaselibrary.uis.adapters.holders.CommonHolder;
import com.leo.afbaselibrary.utils.TimeUtil;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by GongLi on 2019/1/10.
 * Email：lc824767150@163.com
 */

public class DonateCouponsActivity extends BaseStateRefreshingLoadingActivity<CouponItemEntity> {

    @BindView(R.id.et_phone)
    EditText etPhone;
    @BindView(R.id.et_number)
    EditText etNumber;
    @BindView(R.id.tv_ensure)
    TextView tvEnsure;

    private List<CouponItemEntity> coupons;
    private int maxCount;

    @Override
    protected String getTitleText() {
        coupons = getIntent().getParcelableArrayListExtra(CommonUtil.KEY_VALUE_1);
//        if (coupons != null) {
//            for (CouponItemEntity entity : coupons) {
//                entity.setCacheCount(entity.getCouponsCount());
//            }
//        }
        return "赠送";
    }

    @Override
    public int getContentViewId() {
        return R.layout.activity_donate_coupons;
    }

    @Override
    protected void initViews(Bundle savedInstanceState) {
        super.initViews(savedInstanceState);
        mLayoutRefresh.setEnabled(false);
        maxCount = (coupons != null && coupons.size() > 0) ? coupons.get(0).getCouponsCount() : 0;
        etNumber.setHint(String.format("最多可赠送%s张", maxCount));
    }

    @Override
    protected MultiItemTypeAdapter<CouponItemEntity> getAdapter() {
        return new BaseAdapter<CouponItemEntity>(this, R.layout.activity_donate_coupons_item, mItems) {
            @Override
            protected void convert(CommonHolder holder, CouponItemEntity item, int position) {
                holder.setSelected(R.id.ll_bg, item.isChoosed());

                boolean discount = TextUtils.equals(Constants.CouponType.DISCOUNT, item.getCoupontype());
                holder.setTextNotHide(R.id.tv_discounts_1, discount ? CommonUtil.getMayTwoFloat(item.getDiscount()) : "￥");
                holder.setTextNotHide(R.id.tv_discounts_2, discount ? "折" : CommonUtil.getMayTwoFloat(item.getMoney()));
                holder.setTextSize(R.id.tv_discounts_1, discount ? 32 : 14);
                holder.setTextSize(R.id.tv_discounts_2, discount ? 14 : 32);
                holder.setTextNotHide(R.id.tv_condition, String.format("满%s可用", CommonUtil.getMayTwoFloat(item.getCouponCondition())));
                holder.setTextNotHide(R.id.tv_name, discount ? "折扣券" : "代金券");
                holder.setBackgroundRes(R.id.ll_juan,discount ? R.mipmap.bg_wallet_discount:R.mipmap.bg_wallet_voucher);

//                EditCountViewIntUpDown editCountView = holder.getView(R.id.ecv_count);
//                editCountView.removeOnNumberChange();
//                editCountView.setOnNumberChange(new EditCountViewInList.OnNumberChange() {
//                    @Override
//                    public void onChange(int num, final int position, EditText text) {
//                        if (position >= 0 && position < mItems.size()) {
//                            CouponItemEntity couponItemEntity = mItems.get(position);
//                            couponItemEntity.setCacheCount(num);
//                        }
//                    }
//                }, position);
//                editCountView.setCanEdit(false);
//                editCountView.setMaxCount(item.getCouponsCount());
//                editCountView.setMinCount(1);
//                editCountView.setCurrentCount(item.getCacheCount());
                holder.setTextNotHide(R.id.tv_number, String.format("×%s", item.getCacheCount()));
                holder.setVisible(R.id.tv_number, RoleUtils.getInstance().roleBeTarget(Constants.RoleType.PARTNER, AppContent.getInstance().getMyUserRole()));

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
    protected void loadData(int page) {
        mItems.clear();
        mItems.addAll(coupons);
        loadingComplete(true, 1);
    }

    @OnClick(R.id.tv_ensure)
    public void onViewClicked() {
        if (TextUtils.isEmpty(CommonUtil.getEditText(etPhone))) {
            showToast("请输入赠送用户手机号");
            return;
        }
        UserEntity userBean = DataSharedPreferences.getUserBean();
        if (userBean != null && TextUtils.equals(CommonUtil.getEditText(etPhone), userBean.getPhone())) {
            showToast("不能赠送给自己优惠券");
            return;
        }
//        if (TextUtils.isEmpty(CommonUtil.getEditText(etNumber))) {
//            showToast("请输入赠送数量");
//            return;
//        }
        int number = 0;
//        try {
//            number = Integer.parseInt(CommonUtil.getEditText(etNumber));
//        } catch (NumberFormatException e) {
//            e.printStackTrace();
//            number = 0;
//        }
//        if (number <= 0) {
//            showToast("请正确输入赠送数量");
//            return;
//        }
//        if (number > maxCount) {
//            showToast(String.format("最多只能赠送%s张", maxCount));
//            return;
//        }

        String couponsParam;
        String couponsCount;
        StringBuilder ids = new StringBuilder();
        StringBuilder counts = new StringBuilder();
        if (coupons != null && coupons.size() > 0) {
            for (CouponItemEntity item : coupons) {
                ids.append(item.getObjctId()).append(",");
                counts.append(item.getCacheCount()).append(",");
                number += item.getCacheCount();
            }
        }
        if (ids.length() > 1) {
            couponsParam = ids.substring(0, ids.length() - 1);
        } else {
            couponsParam = "";
        }
        if (counts.length() > 1) {
            couponsCount = counts.substring(0, counts.length() - 1);
        } else {
            couponsCount = "";
        }
        if (TextUtils.isEmpty(couponsParam)) {
            showToast("请至少选择一张优惠券");
            return;
        }
        KeyBoardUtils.closeKeybord(this);
        showEnsureDialog(etPhone.getText().toString(), number, couponsCount, couponsParam);
    }

    private void showEnsureDialog(final String phone, final int number, final String couponCounts, final String couponsParam) {
        new AlertDialog.Builder(this)
                .setTitle("提示")
                .setMessage(String.format("确认要赠送%s张该优惠券给%s?", number, phone))
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        requestDonate(phone, couponCounts, couponsParam);
                    }
                })
                .setNegativeButton("取消", null)
                .show();
    }

    private void requestDonate(String phone, String couponCounts, String couponsParam) {
        tvEnsure.setEnabled(false);
        showProgressDialog(getString(R.string.wait),true);
        NetService.getInstance().donateCoupons(phone, couponCounts, couponsParam)
                .compose(this.<String>bindLifeCycle())
                .subscribe(new CustomApiCallback<String>() {
                    @Override
                    protected void onResultError(ApiException ex) {
                        showToast(ex.getDisplayMessage());
                        tvEnsure.setEnabled(true);
                        hideProgress();
                    }

                    @Override
                    public void onNext(String s) {
                        showToast("赠送成功");
                        tvEnsure.setEnabled(true);
                        hideProgress();
                        setResult(RESULT_OK);
                        finish();
                    }
                });
    }

    @Override
    protected boolean isShowDivider() {
        return false;
    }


}

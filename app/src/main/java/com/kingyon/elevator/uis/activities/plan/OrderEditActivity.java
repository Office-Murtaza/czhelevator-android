package com.kingyon.elevator.uis.activities.plan;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v4.widget.NestedScrollView;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.AbsoluteSizeSpan;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.kingyon.elevator.R;
import com.kingyon.elevator.application.AppContent;
import com.kingyon.elevator.constants.Constants;
import com.kingyon.elevator.entities.ADEntity;
import com.kingyon.elevator.entities.CellItemEntity;
import com.kingyon.elevator.entities.CommitOrderEntiy;
import com.kingyon.elevator.entities.CouponItemEntity;
import com.kingyon.elevator.entities.DeviceParamCache;
import com.kingyon.elevator.entities.IndustryEntity;
import com.kingyon.elevator.entities.OrderDetailsEntity;
import com.kingyon.elevator.entities.OrderIdentityEntity;
import com.kingyon.elevator.entities.PointItemEntity;
import com.kingyon.elevator.nets.CustomApiCallback;
import com.kingyon.elevator.nets.NetService;
import com.kingyon.elevator.uis.activities.AgreementActivity;
import com.kingyon.elevator.uis.activities.advertising.InfomationAdvertisingActivity;
import com.kingyon.elevator.uis.activities.order.OrderPayActivity;
import com.kingyon.elevator.uis.activities.order.PaySuccessActivity;
import com.kingyon.elevator.uis.activities.user.IdentityInfoActivity;
import com.kingyon.elevator.uis.activities.user.MyAdActivity;
import com.kingyon.elevator.uis.dialogs.OrderIdentityDialog;
import com.kingyon.elevator.uis.widgets.ProportionFrameLayout;
import com.kingyon.elevator.utils.CommonUtil;
import com.kingyon.elevator.utils.FormatUtils;
import com.kingyon.elevator.utils.JumpUtils;
import com.kingyon.elevator.utils.StatusBarUtil;
import com.leo.afbaselibrary.nets.exceptions.ApiException;
import com.leo.afbaselibrary.nets.exceptions.ResultException;
import com.leo.afbaselibrary.uis.activities.BaseStateLoadingActivity;
import com.leo.afbaselibrary.utils.ScreenUtil;
import com.leo.afbaselibrary.utils.TimeUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by GongLi on 2019/1/3.
 * Email：lc824767150@163.com
 */

public class OrderEditActivity extends BaseStateLoadingActivity {

    @BindView(R.id.tv_agreement_check)
    TextView tvAgreementCheck;
    @BindView(R.id.pfl_head)
    ProportionFrameLayout pflHead;
    @BindView(R.id.tv_auth_name)
    TextView tvAuthName;
    @BindView(R.id.tv_type)
    TextView tvType;
    @BindView(R.id.tv_start_time)
    TextView tvStartTime;
    @BindView(R.id.tv_end_time)
    TextView tvEndTime;
    @BindView(R.id.tv_industry)
    TextView tvIndustry;
    @BindView(R.id.tv_advertising)
    TextView tvAdvertising;
    @BindView(R.id.tv_devices)
    TextView tvDevices;
    @BindView(R.id.tv_screen_count)
    TextView tvScreenCount;
    @BindView(R.id.tv_release_days)
    TextView tvReleaseDays;
    @BindView(R.id.tv_total_price)
    TextView tvTotalPrice;
    @BindView(R.id.tv_coupons)
    TextView tvCoupons;
    @BindView(R.id.nsv)
    NestedScrollView nsv;
    @BindView(R.id.v_line)
    View vLine;
    @BindView(R.id.ll_title)
    LinearLayout llTitle;
    @BindView(R.id.tv_coupons_discounts)
    TextView tvCouponsDiscount;
    @BindView(R.id.tv_price)
    TextView tvPrice;
    @BindView(R.id.tv_ensure)
    TextView tvEnsure;
    @BindView(R.id.tv_ad_preview)
    TextView tvAdPreview;
    @BindView(R.id.ll_ad_preview)
    LinearLayout llAdPreview;

    private boolean notFirstIn;

    private String type;
    private long startTime;
    private long endTime;
    private ArrayList<CellItemEntity> cells;
    private float limitedY;
    private ArrayList<CouponItemEntity> coupons;
    private OrderIdentityDialog identityDialog;

    @Override
    protected String getTitleText() {
        type = getIntent().getStringExtra(CommonUtil.KEY_VALUE_1);
        startTime = getIntent().getLongExtra(CommonUtil.KEY_VALUE_2, 0);
        endTime = getIntent().getLongExtra(CommonUtil.KEY_VALUE_3, 0);
        cells = getIntent().getParcelableArrayListExtra(CommonUtil.KEY_VALUE_4);
        if (cells != null) {
            for (CellItemEntity cell : cells) {
                ArrayList<PointItemEntity> cellPoints = cell.getPoints();
                if (cellPoints != null && cellPoints.size() > cell.getChoosedScreenNum()) {
                    ArrayList<PointItemEntity> entities = new ArrayList<>();
                    entities.addAll(cellPoints.subList(0, cell.getChoosedScreenNum()));
                    cell.setPoints(entities);
                }
            }
        }
        return "确认订单";
    }

    @Override
    public int getContentViewId() {
        StatusBarUtil.setTransparent(this, false);
        return R.layout.activity_edit_order;
    }

    @Override
    protected void initViews(Bundle savedInstanceState) {
        StatusBarUtil.setHeadViewPadding(this, llTitle);
        ((ImageView) vBack).setImageDrawable(getBackDrawable(0xFFFFFFFF));
        initListener();
        tvType.setText(FormatUtils.getInstance().getPlanType(type));
        tvStartTime.setText(TimeUtil.getYMdTime(startTime));
        tvEndTime.setText(TimeUtil.getYMdTime(endTime));
        updatePriceInfo();
        if (TextUtils.equals(Constants.PLAN_TYPE.INFORMATION, type)) {
            tvAdvertising.setHint("请输入");
        } else {
            tvAdvertising.setHint("请选择");
        }
    }

    private void initListener() {
        nsv.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
            @Override
            public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                if (limitedY == 0) {
                    limitedY = ScreenUtil.getScreenWidth(OrderEditActivity.this) / pflHead.getProporty() - ScreenUtil.dp2px(56) - StatusBarUtil.getStatusBarHeight(OrderEditActivity.this);
                }
                if (scrollY == 0) {
                    llTitle.setBackgroundColor(0x00FFFFFF);
                    tvTitle.setTextColor(0xFFFFFFFF);
                    ((ImageView) vBack).setImageDrawable(getBackDrawable(0xFFFFFFFF));
                    vLine.setBackgroundColor(0x00EEEEEE);
                } else if (scrollY < limitedY) {
                    float percent = (limitedY - scrollY) / limitedY;
                    int alpha = (int) (255 - percent * 255);
                    llTitle.setBackgroundColor(Color.argb(alpha, 255, 255, 255));
                    int blackValue = (int) (0x33 + (0xFF - 0x33) * percent);
                    tvTitle.setTextColor(Color.argb(0xFF, blackValue, blackValue, blackValue));
                    ((ImageView) vBack).setImageDrawable(getBackDrawable(Color.argb(0xFF, blackValue, blackValue, blackValue)));
                    vLine.setBackgroundColor(Color.argb(alpha, 238, 238, 238));
                } else {
                    llTitle.setBackgroundColor(0xFFFFFFFF);
                    tvTitle.setTextColor(0xFF333333);
                    ((ImageView) vBack).setImageDrawable(getBackDrawable(0xFF333333));
                    vLine.setBackgroundColor(0xFFEEEEEE);
                }
            }
        });
    }

    private Drawable getBackDrawable(int color) {
        Drawable up = ContextCompat.getDrawable(this, R.drawable.ic_back_gray_tint);
        Drawable drawableUp = null;
        if (up != null) {
            drawableUp = DrawableCompat.wrap(up);
            DrawableCompat.setTint(drawableUp, color);
        }
        Drawable up1 = ContextCompat.getDrawable(this, R.drawable.ic_back_gray_tint);
        if (up1 != null) {
            Drawable drawableUp1 = DrawableCompat.unwrap(up1);
            DrawableCompat.setTintList(drawableUp1, null);
        }
        return drawableUp;
    }

    @Override
    protected void loadData() {
        NetService.getInstance().orderIdentityInfo()
                .compose(this.<OrderIdentityEntity>bindLifeCycle())
                .subscribe(new CustomApiCallback<OrderIdentityEntity>() {
                    @Override
                    protected void onResultError(ApiException ex) {
                        showToast(ex.getDisplayMessage());
                        loadingComplete(STATE_ERROR);
                    }

                    @Override
                    public void onNext(OrderIdentityEntity orderIdentityEntity) {
                        if (orderIdentityEntity == null) {
                            throw new ResultException(9001, "返回参数异常");
                        }
                        updateUI(orderIdentityEntity);
                        loadingComplete(STATE_CONTENT);
                    }
                });
    }

    @Override
    protected void onResume() {
        if (notFirstIn) {
            loadData();
        } else {
            notFirstIn = true;
        }
        super.onResume();
    }

    private void updateUI(OrderIdentityEntity identityInfo) {
        String authStatus = identityInfo.getState();
        switch (authStatus) {
            case Constants.IDENTITY_STATUS.NO_AUTH:
                tvAuthName.setText("未认证");
                tvAuthName.setTextColor(0xFFFF7E12);
                tvAuthName.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_homepage_right, 0);
                tvAuthName.setEnabled(true);
                break;
            case Constants.IDENTITY_STATUS.AUTHING:
                tvAuthName.setText("认证中");
                tvAuthName.setTextColor(0xFFFF7E12);
                tvAuthName.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_homepage_right, 0);
                tvAuthName.setEnabled(false);
                break;
            case Constants.IDENTITY_STATUS.AUTHED:
                tvAuthName.setText(TextUtils.equals("PERSONAL", identityInfo.getTypeCertification()) ? identityInfo.getName() : identityInfo.getCompanyName());
                tvAuthName.setTextColor(0xFF333333);
                tvAuthName.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                tvAuthName.setEnabled(false);
                break;
            case Constants.IDENTITY_STATUS.FAILD:
                tvAuthName.setText("认证失败");
                tvAuthName.setTextColor(0xFFFF7E12);
                tvAuthName.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_homepage_right, 0);
                tvAuthName.setEnabled(true);
                break;
            default:
                tvAuthName.setText("认证状态未知");
                tvAuthName.setTextColor(0xFFFF7E12);
                tvAuthName.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_homepage_right, 0);
                tvAuthName.setEnabled(true);
                break;
        }
    }

    @OnClick({R.id.tv_agreement_check, R.id.tv_agreement, R.id.tv_auth_name, R.id.ll_industry, R.id.ll_advertising, R.id.ll_devices, R.id.ll_coupons, R.id.tv_ensure, R.id.ll_ad_preview})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_agreement_check:
                tvAgreementCheck.setSelected(!tvAgreementCheck.isSelected());
                tvEnsure.setEnabled(tvAgreementCheck.isSelected());
                break;
            case R.id.tv_agreement:
                AgreementActivity.start(this, "屏多多使用须知", Constants.AgreementType.AD_TERMS.getValue());
                break;
            case R.id.tv_auth_name:
                jumpToIdentify();
                break;
            case R.id.ll_industry:
                Bundle industryBundle = new Bundle();
                IndustryEntity industry = (IndustryEntity) tvIndustry.getTag();
                if (industry != null) {
                    industryBundle.putParcelable(CommonUtil.KEY_VALUE_1, industry);
                }
                startActivityForResult(IndustryActivity.class, CommonUtil.REQ_CODE_1, industryBundle);
                break;
            case R.id.ll_advertising:
                jumpToAdvertising();
                break;
            case R.id.ll_devices:
                Bundle cellsBundle = new Bundle();
                cellsBundle.putParcelableArrayList(CommonUtil.KEY_VALUE_1, cells);
                cellsBundle.putString(CommonUtil.KEY_VALUE_2, type);
                cellsBundle.putLong(CommonUtil.KEY_VALUE_3, startTime);
                cellsBundle.putLong(CommonUtil.KEY_VALUE_4, endTime);
                startActivity(OrderCellsActivity.class, cellsBundle);
                break;
            case R.id.ll_coupons:
                Bundle couponsBundle = new Bundle();
                couponsBundle.putFloat(CommonUtil.KEY_VALUE_1, updatePriceInfo());
                couponsBundle.putString(CommonUtil.KEY_VALUE_2, type);
                if (coupons != null) {
                    couponsBundle.putParcelableArrayList(CommonUtil.KEY_VALUE_3, coupons);
                }
                startActivityForResult(OrderCouponsActivity.class, CommonUtil.REQ_CODE_2, couponsBundle);
                break;
            case R.id.tv_ensure:
                commitOrder();
                break;
            case R.id.ll_ad_preview:
                ADEntity adEntity = (ADEntity) tvAdvertising.getTag();
                if (adEntity != null) {
                    JumpUtils.getInstance().jumpToAdPreview(this, adEntity,"ad");
                }
                break;
        }
    }

    private void jumpToIdentify() {
        startActivity(IdentityInfoActivity.class);
    }

    private void commitOrder() {
        if (!tvAgreementCheck.isSelected()) {
            showToast("请阅读并同意广告投放须知");
            return;
        }
        if (tvAuthName.isEnabled()) {
            showOrderIdentityDialog();
            return;
        }
        IndustryEntity industry = (IndustryEntity) tvIndustry.getTag();
        if (industry == null) {
            showToast("请选择广告所属行业");
            return;
        }
        ADEntity adEntity = (ADEntity) tvAdvertising.getTag();
        if (adEntity == null) {
            showToast(TextUtils.equals(Constants.PLAN_TYPE.INFORMATION, type) ? "请填写广告内容" : "请选择广告内容");
            return;
        }
        List<DeviceParamCache> deviceParams = new ArrayList<>();
        if (cells != null) {
            for (CellItemEntity item : cells) {
                ArrayList<PointItemEntity> points = item.getPoints();
                if (points == null || points.size() > 0) {
                    DeviceParamCache cache = new DeviceParamCache();
                    cache.setCellId(item.getObjctId());
                    cache.setCount(item.getChoosedScreenNum());
                    if (points != null) {
                        StringBuilder stringBuilder = new StringBuilder();
                        for (PointItemEntity point : points) {
                            stringBuilder.append(point.getObjectId()).append(",");
                        }
                        cache.setDeviceIds(stringBuilder.length() > 1 ? stringBuilder.substring(0, stringBuilder.length() - 1) : "");
                    } else {
                        cache.setDeviceIds(null);
                    }
                    deviceParams.add(cache);
                }
            }
        }
        if (deviceParams.size() < 1) {
            showToast("投放数量不能为0");
            return;
        }
        String couponIds = null;
        if (coupons != null) {
            StringBuilder stringBuilder = new StringBuilder();
            for (CouponItemEntity item : coupons) {
                stringBuilder.append(item.getObjctId()).append(",");
            }
            couponIds = stringBuilder.length() > 1 ? stringBuilder.substring(0, stringBuilder.length() - 1) : "";
        }
        tvEnsure.setEnabled(false);
        showProgressDialog(getString(R.string.wait),true);
        NetService.getInstance().commitOrder(type, startTime, endTime, Long.valueOf(adEntity.getObjctId())
                , AppContent.getInstance().getGson().toJson(deviceParams), couponIds, industry.getObjectId())
                .compose(this.<CommitOrderEntiy>bindLifeCycle())
                .subscribe(new CustomApiCallback<CommitOrderEntiy>() {
                    @Override
                    protected void onResultError(ApiException ex) {
                        showToast(ex.getDisplayMessage());
                        hideProgress();
                        tvEnsure.setEnabled(true);
                    }

                    @Override
                    public void onNext(CommitOrderEntiy orderEntiy) {
                        hideProgress();
                        Bundle bundle = new Bundle();
                        if (orderEntiy.getPayMoney() > 0) {
                            bundle.putString(CommonUtil.KEY_VALUE_1, orderEntiy.getOrderId());
                            startActivity(OrderPayActivity.class, bundle);
                        } else {
                            OrderDetailsEntity detailsEntity = new OrderDetailsEntity();
                            detailsEntity.setObjctId(orderEntiy.getOrderId());
                            detailsEntity.setCouponPrice(updatePriceInfo());
                            detailsEntity.setRealPrice(orderEntiy.getPayMoney());
                            detailsEntity.setPayTime(System.currentTimeMillis());
                            bundle.putParcelable(CommonUtil.KEY_VALUE_1, detailsEntity);
                            bundle.putString(CommonUtil.KEY_VALUE_2, Constants.PayType.FREE);
                            startActivity(PaySuccessActivity.class, bundle);
                        }
                        tvEnsure.setEnabled(true);
                        Intent intent = new Intent();
                        intent.putExtra(CommonUtil.KEY_VALUE_2, type);
                        setResult(RESULT_OK, intent);
                        finish();
                    }
                });
    }

    private void showOrderIdentityDialog() {
        if (identityDialog == null) {
            identityDialog = new OrderIdentityDialog(this);
            identityDialog.setOnIdentityClickListener(new OrderIdentityDialog.OnIdentityClickListener() {
                @Override
                public void onIdentityClick() {
                    jumpToIdentify();
                }
            });
        }
        identityDialog.show("根据法律规定，您当前还未成功进行认证，不能进行广告发布");
    }

    private void jumpToAdvertising() {
        Bundle bundle = new Bundle();
        switch (type) {
            case Constants.PLAN_TYPE.BUSINESS:
            case Constants.PLAN_TYPE.DIY:
                bundle.putBoolean(CommonUtil.KEY_VALUE_1, true);
                bundle.putString(CommonUtil.KEY_VALUE_2, type);
                startActivityForResult(MyAdActivity.class, 9000, bundle);
                break;
            case Constants.PLAN_TYPE.INFORMATION:
                ADEntity adEntity = (ADEntity) tvAdvertising.getTag();
                if (adEntity != null) {
                    bundle.putParcelable(CommonUtil.KEY_VALUE_1, adEntity);
                }
                startActivityForResult(InfomationAdvertisingActivity.class, 9000, bundle);
                break;
        }
    }

    private float updatePriceInfo() {
        int cellNum = 0;
        int screenNum = 0;
        float totalPrice = 0;
        if (cells != null) {
            for (CellItemEntity item : cells) {
                cellNum++;

                ArrayList<PointItemEntity> points = item.getPoints();
                int cellScreenNum = item.getChoosedScreenNum();
                screenNum += cellScreenNum;

                float cellScreenPrice = 0;
                switch (type) {
                    case Constants.PLAN_TYPE.BUSINESS:
                        cellScreenPrice = item.getBusinessAdPrice() * cellScreenNum;
                        break;
                    case Constants.PLAN_TYPE.DIY:
                        cellScreenPrice = item.getDiyAdPrice() * cellScreenNum;
                        break;
                    case Constants.PLAN_TYPE.INFORMATION:
                        cellScreenPrice = item.getInformationAdPrice() * cellScreenNum;
                        break;
                }
                totalPrice += cellScreenPrice;
            }
        }
        tvDevices.setText(String.format("共%s个小区 %s面屏", cellNum, screenNum));
        tvScreenCount.setText(String.format("%s面", screenNum));

        int days = FormatUtils.getInstance().getTimeDays(startTime, endTime);
        tvReleaseDays.setText(String.format("%s天", days));
        totalPrice = totalPrice * days;

        tvTotalPrice.setTag(totalPrice);
        tvTotalPrice.setText(String.format("￥%s", CommonUtil.getTwoFloat(totalPrice)));

        float couponsSum = 0;
        if (coupons != null && coupons.size() > 0) {
            tvCoupons.setText(String.format("已选%s张", coupons.size()));
            String choosedCouponType = coupons.get(0).getCoupontype();
            if (TextUtils.equals(Constants.CouponType.VOUCHER, choosedCouponType)) {
                for (CouponItemEntity item : coupons) {
                    couponsSum += item.getMoney();
                }
            } else if (TextUtils.equals(Constants.CouponType.DISCOUNT, choosedCouponType)) {
                couponsSum = totalPrice * (1 - coupons.get(0).getDiscount() / 10);
            }
        } else {
            tvCoupons.setText("");
        }
        float resultPrice = totalPrice - couponsSum;
        if (resultPrice > 0) {
            tvCouponsDiscount.setText(String.format("优惠:￥%s", CommonUtil.getTwoFloat(couponsSum)));
            tvPrice.setText(getPriceSpan(CommonUtil.getTwoFloat(resultPrice)));
        } else {
            tvCouponsDiscount.setText(String.format("优惠:￥%s", CommonUtil.getTwoFloat(totalPrice)));
            tvPrice.setText(getPriceSpan(CommonUtil.getTwoFloat(0)));
        }
        return totalPrice;
    }

    private CharSequence getPriceSpan(String price) {
        SpannableString spannableString = new SpannableString(price);
        int index = price.indexOf(".");
        spannableString.setSpan(new AbsoluteSizeSpan(18, true), index, index + 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        spannableString.setSpan(new AbsoluteSizeSpan(13, true), index + 1, price.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        return spannableString;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (RESULT_OK == resultCode && data != null) {
            switch (requestCode) {
                case CommonUtil.REQ_CODE_1:
                    IndustryEntity industryEntity = data.getParcelableExtra(CommonUtil.KEY_VALUE_1);
                    if (industryEntity != null) {
                        tvIndustry.setTag(industryEntity);
                        tvIndustry.setText(industryEntity.getName());
                    } else {
                        tvIndustry.setTag(null);
                        tvIndustry.setText("");
                    }
                    break;
                case 9000:
                    ADEntity adEntity = data.getParcelableExtra(CommonUtil.KEY_VALUE_1);
                    if (adEntity != null) {
                        tvAdvertising.setTag(adEntity);
                        tvAdvertising.setText(adEntity.getTitle());
                        if (TextUtils.equals(Constants.PLAN_TYPE.INFORMATION, type)) {
                            llAdPreview.setVisibility(View.GONE);
                        } else {
                            llAdPreview.setVisibility(View.VISIBLE);
                        }
                    } else {
                        tvAdvertising.setTag(null);
                        tvAdvertising.setText("");
                        llAdPreview.setVisibility(View.GONE);
                    }
                    break;
                case CommonUtil.REQ_CODE_2:
                    coupons = data.getParcelableArrayListExtra(CommonUtil.KEY_VALUE_1);
                    updatePriceInfo();
                    break;
            }
        }
    }

    @Override
    protected void onDestroy() {
        identityDialog = null;
        super.onDestroy();
    }
}

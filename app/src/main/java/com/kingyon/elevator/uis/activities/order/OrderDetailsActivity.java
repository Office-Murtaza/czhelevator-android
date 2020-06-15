package com.kingyon.elevator.uis.activities.order;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v4.widget.NestedScrollView;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.AbsoluteSizeSpan;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.LogUtils;
import com.kingyon.elevator.R;
import com.kingyon.elevator.constants.Constants;
import com.kingyon.elevator.entities.ADEntity;
import com.kingyon.elevator.entities.OrderDetailsEntity;
import com.kingyon.elevator.entities.OrderIdentityEntity;
import com.kingyon.elevator.nets.CustomApiCallback;
import com.kingyon.elevator.nets.NetService;
import com.kingyon.elevator.others.OnClickWithObjectListener;
import com.kingyon.elevator.uis.activities.advertising.AdEditActivity;
import com.kingyon.elevator.uis.activities.advertising.InfomationAdvertisingActivity;
import com.kingyon.elevator.uis.adapters.adaptertwo.order.OrderCommunityAdapter;
import com.kingyon.elevator.uis.widgets.ProportionFrameLayout;
import com.kingyon.elevator.utils.CommonUtil;
import com.kingyon.elevator.utils.FormatUtils;
import com.kingyon.elevator.utils.StatusBarUtil;
import com.leo.afbaselibrary.nets.exceptions.ApiException;
import com.leo.afbaselibrary.nets.exceptions.ResultException;
import com.leo.afbaselibrary.uis.activities.BaseStateRefreshingActivity;
import com.leo.afbaselibrary.utils.AFUtil;
import com.leo.afbaselibrary.utils.ScreenUtil;
import com.leo.afbaselibrary.utils.TimeUtil;
import com.leo.afbaselibrary.widgets.StateLayout;

import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by GongLi on 2019/1/4.
 * Email：lc824767150@163.com
 */

public class OrderDetailsActivity extends BaseStateRefreshingActivity {
    @BindView(R.id.img_head)
    ImageView imgHead;
    @BindView(R.id.tv_waitpay_remain_time)
    TextView tvWaitpayRemainTime;
    @BindView(R.id.ll_order_head_waitpay)
    LinearLayout llOrderHeadWaitpay;
    @BindView(R.id.tv_cancel_reason)
    TextView tvCancelReason;
    @BindView(R.id.ll_cancel_reason)
    LinearLayout llCancelReason;
    @BindView(R.id.tv_cancel_time)
    TextView tvCancelTime;
    @BindView(R.id.ll_order_head_cancel)
    LinearLayout llOrderHeadCancel;
    @BindView(R.id.ll_order_head_authing)
    LinearLayout llOrderHeadAuthing;
    @BindView(R.id.tv_authfailed_reason)
    TextView tvAuthfailedReason;
    @BindView(R.id.tv_authfailed_modify)
    TextView tvAuthfailedModify;
    @BindView(R.id.ll_order_head_authfailed)
    LinearLayout llOrderHeadAuthfailed;
    @BindView(R.id.ll_order_head_waitrelease)
    LinearLayout llOrderHeadWaitrelease;
    @BindView(R.id.tv_release_info)
    TextView tvReleaseInfo;
    @BindView(R.id.ll_order_head_releasing)
    LinearLayout llOrderHeadReleasing;
    @BindView(R.id.tv_order_completed_title)
    TextView tvOrderCompletedTitle;
    @BindView(R.id.tv_completed_monit)
    TextView tvCompletedMonit;
    @BindView(R.id.ll_order_head_completed)
    LinearLayout llOrderHeadCompleted;
    @BindView(R.id.tv_failed_title)
    TextView tvFailedTitle;
    @BindView(R.id.tv_failed_reason)
    TextView tvFailedReason;
    @BindView(R.id.ll_order_head_failed)
    LinearLayout llOrderHeadFailed;
    @BindView(R.id.pfl_head)
    ProportionFrameLayout pflHead;
    @BindView(R.id.tv_auth_name)
    TextView tvAuthName;
    @BindView(R.id.rcv_list)
    RecyclerView rcvList;
    @BindView(R.id.ll_sq)
    LinearLayout llSq;
    @BindView(R.id.tv_devices)
    TextView tvDevices;
    @BindView(R.id.ll_devices)
    TextView llDevices;
    @BindView(R.id.tv_total)
    TextView tvTotal;
    @BindView(R.id.tv_discount)
    TextView tvDiscount;
    @BindView(R.id.tv_waitpay_money)
    TextView tvWaitpayMoney;
    @BindView(R.id.tv_order_type)
    TextView tvOrderType;
    @BindView(R.id.tv_ad_name)
    TextView tvAdName;
    @BindView(R.id.ll_name)
    LinearLayout llName;
    @BindView(R.id.tv_duration)
    TextView tvDuration;
    @BindView(R.id.ll_duration)
    LinearLayout llDuration;
    @BindView(R.id.tv_pay_time)
    TextView tvPayTime;
    @BindView(R.id.ll_pay_time)
    LinearLayout llPayTime;
    @BindView(R.id.tv_sn)
    TextView tvSn;
    @BindView(R.id.tv_copy_sn)
    TextView tvCopySn;
    @BindView(R.id.ll_sn)
    LinearLayout llSn;
    @BindView(R.id.tv_address)
    TextView tvAddress;
    @BindView(R.id.ll_city)
    LinearLayout llCity;
    @BindView(R.id.tv_pay_way)
    TextView tvPayWay;
    @BindView(R.id.ll_pay_way)
    LinearLayout llPayWay;
    @BindView(R.id.nsv)
    NestedScrollView nsv;
    @BindView(R.id.pre_v_back)
    ImageView preVBack;
    @BindView(R.id.pre_tv_title)
    TextView preTvTitle;
    @BindView(R.id.v_line)
    View vLine;
    @BindView(R.id.ll_title)
    LinearLayout llTitle;
    @BindView(R.id.pre_refresh)
    SwipeRefreshLayout preRefresh;
    @BindView(R.id.tv_waitpay_cancel)
    TextView tvWaitpayCancel;
    @BindView(R.id.tv_waitpay_pay)
    TextView tvWaitpayPay;
    @BindView(R.id.ll_order_operate_waitpay)
    LinearLayout llOrderOperateWaitpay;
    @BindView(R.id.tv_release_down)
    TextView tvReleaseDown;
    @BindView(R.id.tv_release_monit)
    TextView tvReleaseMonit;
    @BindView(R.id.ll_order_operate_release)
    LinearLayout llOrderOperateRelease;
    @BindView(R.id.ll_operate)
    LinearLayout llOperate;
    @BindView(R.id.stateLayout)
    StateLayout stateLayout;
    @BindView(R.id.tv_sq)
    TextView tvSq;
    @BindView(R.id.img_sq)
    ImageView imgSq;
    private String orderId;
    private boolean isSq = true;

    private OrderDetailsEntity orderDetails;
    private View[] headViews;
    private View[] operateViews;
    private Subscription countDownSubscribe;
    private long countDownTime;
    private float limitedY;
    private boolean notFirstIn;

    @Override
    protected String getTitleText() {
        orderId = getIntent().getStringExtra(CommonUtil.KEY_VALUE_1);
        return "订单详情";
    }

    @Override
    public int getContentViewId() {
        StatusBarUtil.setTransparent(this, false);
        return R.layout.activity_order_detailstwo;
    }

    @Override
    public void init(Bundle savedInstanceState) {
        super.init(savedInstanceState);
        headViews = new View[]{llOrderHeadWaitpay, llOrderHeadCancel, llOrderHeadAuthing, llOrderHeadAuthfailed, llOrderHeadWaitrelease, llOrderHeadReleasing, llOrderHeadCompleted, llOrderHeadFailed};
        operateViews = new View[]{llOrderOperateWaitpay, llOrderOperateRelease};
        StatusBarUtil.setHeadViewPadding(this, llTitle);
        ((ImageView) vBack).setImageDrawable(getBackDrawable(0xFFFFFFFF));
        nsv.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
            @Override
            public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                if (limitedY == 0) {
                    limitedY = ScreenUtil.getScreenWidth(OrderDetailsActivity.this) / pflHead.getProporty() - ScreenUtil.dp2px(48) - StatusBarUtil.getStatusBarHeight(OrderDetailsActivity.this);
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

    @Override
    public void onRefresh() {
        LogUtils.e(orderId);
        NetService.getInstance().orderDetatils(orderId)
                .compose(this.<OrderDetailsEntity>bindLifeCycle())
                .subscribe(new CustomApiCallback<OrderDetailsEntity>() {
                    @Override
                    protected void onResultError(ApiException ex) {
                        showToast(ex.getDisplayMessage());
                        loadingComplete(STATE_ERROR);
                    }

                    @Override
                    public void onNext(OrderDetailsEntity order) {
                        LogUtils.e(order.toString());
                        LogUtils.e(order.getLstHousingBean().size(), order.getLstHousingBean().toString());
                        if (order == null) {
                            throw new ResultException(9001, "返回参数异常");
                        }
                        orderDetails = order;

                        updateHead(order);
                        updateOperate(order);

                        OrderIdentityEntity identity = order.getContract();
                        tvAuthName.setText("投放人：" + order.getCreator().getNikeName());
                        tvSq.setText("展开");
                        imgSq.setImageResource(R.mipmap.ic_arrow_down);
                        OrderCommunityAdapter orderCommunityAdapter = new OrderCommunityAdapter(OrderDetailsActivity.this, order.getLstHousingBean(), 1);
                        rcvList.setAdapter(orderCommunityAdapter);
                        rcvList.setLayoutManager(new GridLayoutManager(OrderDetailsActivity.this, 1, GridLayoutManager.VERTICAL, false));
                        orderCommunityAdapter.notifyDataSetChanged();
                        tvOrderType.setText(FormatUtils.getInstance().getPlanType(order.getOrderType()));
                        tvAdName.setText((order.getAdvertising() == null || order.getAdvertising().getTitle() == null) ? "" : order.getAdvertising().getTitle());
                        tvDevices.setText(String.format("%s面", order.getTotalScreen()));
                        tvDuration.setText(String.format("%s-%s", TimeUtil.getAllTimeDuration(order.getAdStartTime()), TimeUtil.getAllTimeDuration(order.getAdEndTime())));

                        long startDealTime = TimeUtil.getDayStartTimeMilliseconds(order.getAdStartTime());
                        long endDealTime = TimeUtil.getDayEndTimeMilliseconds(order.getAdEndTime());
                        tvAddress.setText(String.format("%s（%s天）", order.getAddress(), FormatUtils.getInstance().getTimeDays(startDealTime, endDealTime)));

                        tvTotal.setText(String.format("￥%s", CommonUtil.getTwoFloat(order.getGoodsPrice())));
                        tvDiscount.setText(String.format("-￥%s", CommonUtil.getTwoFloat(Math.abs(order.getCouponPrice()))));
                        tvSn.setText(order.getOrderSn());

                        boolean payInvalid = TextUtils.equals(Constants.OrderStatus.WAIT_PAY, order.getOrderStatus()) || TextUtils.equals(Constants.OrderStatus.CANCEL, order.getOrderStatus());

                        tvPayTime.setText(TimeUtil.getAllTimeNoSecond(order.getPayTime()));
                        llPayTime.setVisibility((order.getPayTime() != 0 && !payInvalid) ? View.VISIBLE : View.GONE);
                        tvPayWay.setText(FormatUtils.getInstance().getPayWay(order.getPayWay()));
                        llPayWay.setVisibility((payInvalid || TextUtils.isEmpty(order.getPayWay())) ? View.GONE : View.VISIBLE);
                        tvWaitpayMoney.setText("￥"+CommonUtil.getTwoFloat(order.getRealPrice()));
                        llSq.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if (isSq){
                                    isSq = false;
                                    OrderCommunityAdapter orderCommunityAdapter = new OrderCommunityAdapter(OrderDetailsActivity.this, order.getLstHousingBean(), order.getLstHousingBean().size());
                                    rcvList.setAdapter(orderCommunityAdapter);
                                    rcvList.setLayoutManager(new GridLayoutManager(OrderDetailsActivity.this, 1, GridLayoutManager.VERTICAL, false));
                                    orderCommunityAdapter.notifyDataSetChanged();
                                    tvSq.setText("收起");
                                    imgSq.setImageResource(R.mipmap.ic_arrow_small_up);
                                }else {
                                    isSq = true;
                                    OrderCommunityAdapter orderCommunityAdapter = new OrderCommunityAdapter(OrderDetailsActivity.this, order.getLstHousingBean(), 1);
                                    rcvList.setAdapter(orderCommunityAdapter);
                                    rcvList.setLayoutManager(new GridLayoutManager(OrderDetailsActivity.this, 1, GridLayoutManager.VERTICAL, false));
                                    orderCommunityAdapter.notifyDataSetChanged();
                                    tvSq.setText("展开");
                                    imgSq.setImageResource(R.mipmap.ic_arrow_down);
                                }

                            }
                        });

                        loadingComplete(STATE_CONTENT);
                    }
                });
    }

    private void updateOperate(OrderDetailsEntity order) {
        switch (order.getOrderStatus()) {
            case Constants.OrderStatus.WAIT_PAY:
                updateOperateViewVisiable(R.id.ll_order_operate_waitpay);
                tvWaitpayMoney.setText(getPriceSpan(CommonUtil.getTwoFloat(order.getRealPrice())));
                break;
            case Constants.OrderStatus.RELEASEING:
                updateOperateViewVisiable(R.id.ll_order_operate_release);
                tvReleaseMonit.setVisibility(View.VISIBLE);
                break;
            case Constants.OrderStatus.WAIT_RELEASE:
                updateOperateViewVisiable(R.id.ll_order_operate_release);
                tvReleaseMonit.setVisibility(View.GONE);
                break;
            default:
                updateOperateViewVisiable(0);
                break;
        }
    }

    private void updateHead(OrderDetailsEntity order) {
        switch (order.getOrderStatus()) {
            case Constants.OrderStatus.WAIT_PAY:
                startOrderCountDown(order.getRemainTime());
                updateHeadViewVisiable(R.id.ll_order_head_waitpay);
//                imgHead.setImageResource(R.drawable.bg_order_wait_pay);
                break;
            case Constants.OrderStatus.CANCEL:
                closeOrderCountDown();
                updateHeadViewVisiable(R.id.ll_order_head_cancel);
//                imgHead.setImageResource(R.drawable.bg_order_cancel);
                tvCancelReason.setText(!TextUtils.isEmpty(order.getCancelReason()) ? order.getCancelReason() : "无");
                tvCancelTime.setText(TimeUtil.getAllTime(order.getCancelTime()));
                break;
            case Constants.OrderStatus.OVERDUE:
                closeOrderCountDown();
                updateHeadViewVisiable(R.id.ll_order_head_failed);
//                imgHead.setImageResource(R.drawable.bg_order_failed);
                tvFailedTitle.setText("已过期");
                tvFailedReason.setText(!TextUtils.isEmpty(order.getFailedReason()) ? order.getFailedReason() : "超时未支付");
                break;
            case Constants.OrderStatus.WAIT_RELEASE:
                closeOrderCountDown();
                updateHeadWaitRelease(order);
                break;
            case Constants.OrderStatus.RELEASEING:
                closeOrderCountDown();
                updateHeadViewVisiable(R.id.ll_order_head_releasing);
                tvReleaseInfo.setText(String.format("当前设备投放百分比  %s%%", CommonUtil.getMayTwoFloat(order.getReleaseingPercent() * 100)));
//                imgHead.setImageResource(R.drawable.bg_order_releasing);
                break;
            case Constants.OrderStatus.COMPLETE:
                closeOrderCountDown();
                updateHeadViewVisiable(R.id.ll_order_head_completed);
                tvOrderCompletedTitle.setText(order.isAuditLate() ? "广告审核已过有效期" : "当前广告推广已完成");
//                imgHead.setImageResource(R.drawable.bg_order_completed);
                break;
            case Constants.OrderStatus.FAILED:
                closeOrderCountDown();
                updateHeadViewVisiable(R.id.ll_order_head_failed);
//                imgHead.setImageResource(R.drawable.bg_order_failed);
                tvFailedTitle.setText("投放失败");
                tvFailedReason.setText(!TextUtils.isEmpty(order.getFailedReason()) ? order.getFailedReason() : "无");
                break;
            case Constants.OrderStatus.SOWING:
                closeOrderCountDown();
                updateHeadViewVisiable(R.id.ll_order_head_failed);
//                imgHead.setImageResource(R.drawable.bg_order_failed);
                tvFailedTitle.setText("已下播");
                tvFailedReason.setText(!TextUtils.isEmpty(order.getFailedReason()) ? order.getFailedReason() : "用户主动申请下播");
                break;
            default:
                closeOrderCountDown();
                updateHeadViewVisiable(0);
//                imgHead.setImageResource(R.drawable.bg_order_cancel);
                break;
        }
    }

    private void updateHeadWaitRelease(OrderDetailsEntity order) {
        ADEntity advertising = order.getAdvertising();
        String adStatus = advertising != null && advertising.getAdStatus() != null ? advertising.getAdStatus() : "";
        switch (adStatus) {
            case Constants.AD_STATUS.REVIEW_FAILED:
                updateHeadViewVisiable(R.id.ll_order_head_authfailed);
                tvAuthfailedReason.setText((advertising != null && advertising.getFaildReason() != null) ? advertising.getFaildReason() : "无");
//                imgHead.setImageResource(R.drawable.bg_order_authfailed);
                break;
            case Constants.AD_STATUS.WAIT_REVIEW:
                updateHeadViewVisiable(R.id.ll_order_head_authing);
//                imgHead.setImageResource(R.drawable.bg_order_authing);
                break;
            default:
                updateHeadViewVisiable(R.id.ll_order_head_waitrelease);
//                imgHead.setImageResource(R.drawable.bg_order_waitrelease);
                break;
        }
    }

    private void startOrderCountDown(long duration) {
        closeOrderCountDown();
        this.countDownTime = duration;
        countDownSubscribe = Observable.interval(0, 1000, TimeUnit.MILLISECONDS)
                .compose(this.<Long>bindLifeCycle())
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new CustomApiCallback<Long>() {
                    @Override
                    protected void onResultError(ApiException ex) {

                    }

                    @Override
                    public void onNext(Long aLong) {
                        tvWaitpayRemainTime.setText(TimeUtil.getRemainingTimeSecond(countDownTime));
                        if (countDownTime <= 0) {
                            closeOrderCountDown();
                            autoRefresh();
                        }
                        countDownTime -= 1000;
                    }
                });
    }

    protected void closeOrderCountDown() {
        if (countDownSubscribe != null && !countDownSubscribe.isUnsubscribed()) {
            countDownSubscribe.unsubscribe();
            countDownSubscribe = null;
        }
    }

    private void updateHeadViewVisiable(int viewId) {
        if (headViews == null) {
            return;
        }
        for (View headView : headViews) {
            headView.setVisibility(headView.getId() == viewId ? View.VISIBLE : View.GONE);
        }
    }

    private void updateOperateViewVisiable(int viewId) {
        if (operateViews == null) {
            return;
        }
        for (View operateView : operateViews) {
            operateView.setVisibility(operateView.getId() == viewId ? View.VISIBLE : View.GONE);
        }
        llOperate.setVisibility(viewId == 0 ? View.GONE : View.VISIBLE);
    }

    @OnClick({R.id.ll_devices, R.id.tv_authfailed_modify, R.id.tv_completed_monit, R.id.tv_copy_sn, R.id.tv_waitpay_cancel, R.id.tv_waitpay_pay, R.id.tv_release_down, R.id.tv_release_monit})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ll_devices:
                LogUtils.e(orderDetails.getOrderSn());
                Bundle bundle3 = new Bundle();
                bundle3.putString(CommonUtil.KEY_VALUE_1, orderDetails.getOrderSn());
                startActivity(OrderDevicesActivity.class, bundle3);
                break;
            case R.id.tv_authfailed_modify:
                jumpToAdvertising(orderDetails);
                break;
            case R.id.tv_completed_monit:
                if (FormatUtils.getInstance().beInfomationPlan(orderDetails.getOrderType())) {
                    showToast("便民服务广告不能查看监播表");
                    return;
                }
                Bundle bundle5 = new Bundle();
                bundle5.putString(CommonUtil.KEY_VALUE_1, orderDetails.getOrderSn());
                bundle5.putBoolean(CommonUtil.KEY_VALUE_2, true);
                startActivity(OrderMonitActivity.class, bundle5);
                break;
            case R.id.tv_copy_sn:
                if (orderDetails != null && !TextUtils.isEmpty(orderDetails.getOrderSn())) {
                    ClipboardManager cm = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                    ClipData mClipData = ClipData.newPlainText(AFUtil.getAppProcessName(this), orderDetails.getOrderSn());
                    if (cm != null) {
                        cm.setPrimaryClip(mClipData);
                        showToast("已成功复制订单编号");
                    }
                }
                break;
            case R.id.tv_waitpay_cancel:
                cancelOrder(orderDetails);
                break;
            case R.id.tv_waitpay_pay:
                Bundle bundle1 = new Bundle();
                bundle1.putString(CommonUtil.KEY_VALUE_1, orderDetails.getOrderSn());
                startActivity(OrderPayActivity.class, bundle1);
                break;
            case R.id.tv_release_down:
                Bundle bundle2 = new Bundle();
                bundle2.putString(CommonUtil.KEY_VALUE_1, orderDetails.getOrderSn());
                startActivityForResult(OrderDownActivity.class, CommonUtil.REQ_CODE_2, bundle2);
                break;
            case R.id.tv_release_monit:
                if (FormatUtils.getInstance().beInfomationPlan(orderDetails.getOrderType())) {
                    showToast("便民服务广告不能查看视频监控");
                    return;
                }
                Bundle bundle4 = new Bundle();
                bundle4.putString(CommonUtil.KEY_VALUE_1, orderDetails.getOrderSn());
                bundle4.putBoolean(CommonUtil.KEY_VALUE_2, false);
                startActivity(OrderMonitActivity.class, bundle4);
                break;
        }
    }

    private void jumpToAdvertising(OrderDetailsEntity orderDetails) {
        ADEntity advertising = orderDetails.getAdvertising();
        if (advertising == null) {
            showToast("没有找到关联的广告");
            return;
        }
        Bundle bundle = new Bundle();
        LogUtils.e(advertising.toString());
        switch (advertising.getPlanType()) {
            case Constants.PLAN_TYPE.BUSINESS:
                bundle.putBoolean(CommonUtil.KEY_VALUE_1, true);
                bundle.putParcelable(CommonUtil.KEY_VALUE_2, advertising);
                startActivityForResult(AdEditActivity.class, CommonUtil.REQ_CODE_1, bundle);
                break;
            case Constants.PLAN_TYPE.DIY:
                bundle.putBoolean(CommonUtil.KEY_VALUE_1, true);
                bundle.putParcelable(CommonUtil.KEY_VALUE_2, advertising);
                startActivityForResult(AdEditActivity.class, CommonUtil.REQ_CODE_1, bundle);
                break;
            case Constants.PLAN_TYPE.INFORMATION:
                bundle.putParcelable(CommonUtil.KEY_VALUE_1, advertising);
                startActivityForResult(InfomationAdvertisingActivity.class, CommonUtil.REQ_CODE_1, bundle);
                break;
        }
    }

    @Override
    protected void onResume() {
        if (notFirstIn) {
            autoRefresh();
        } else {
            notFirstIn = true;
        }
        super.onResume();
    }

//    @Override
//    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        if (RESULT_OK == resultCode) {
//            switch (requestCode) {
//                case CommonUtil.REQ_CODE_1:
//                case CommonUtil.REQ_CODE_2:
//                    autoRefresh();
//                    break;
//            }
//        }
//    }

    private void deleteOrder(OrderDetailsEntity itemEntity) {
        showTipDialog("确定要删除本订单吗？", new OnClickWithObjectListener<OrderDetailsEntity>(itemEntity) {
            @Override
            public void onClick(DialogInterface dialog, int which, OrderDetailsEntity entity) {
                requestDeleteOrder(entity);
            }
        });
    }

    private void requestDeleteOrder(final OrderDetailsEntity itemEntity) {
        NetService.getInstance().orderDelete(itemEntity.getObjctId())
                .compose(this.<String>bindLifeCycle())
                .subscribe(new CustomApiCallback<String>() {
                    @Override
                    protected void onResultError(ApiException ex) {
                        showToast(ex.getDisplayMessage());
                    }

                    @Override
                    public void onNext(String s) {
                        showToast("删除成功");
                        finish();
                    }
                });
    }

    private void cancelOrder(OrderDetailsEntity itemEntity) {
        showTipDialog("确定要取消本订单吗？", new OnClickWithObjectListener<OrderDetailsEntity>(itemEntity) {
            @Override
            public void onClick(DialogInterface dialog, int which, OrderDetailsEntity entity) {
                requestCancelOrder(entity);
            }
        });
    }

    private void requestCancelOrder(final OrderDetailsEntity itemEntity) {
        NetService.getInstance().orderCancel(itemEntity.getObjctId())
                .compose(this.<String>bindLifeCycle())
                .subscribe(new CustomApiCallback<String>() {
                    @Override
                    protected void onResultError(ApiException ex) {
                        showToast(ex.getDisplayMessage());
                    }

                    @Override
                    public void onNext(String s) {
                        showToast("取消成功");
                        autoRefresh();
                    }
                });
    }

    private void showTipDialog(String tipContent, DialogInterface.OnClickListener onClickListener) {
        new AlertDialog.Builder(this)
                .setTitle("提示")
                .setMessage(tipContent)
                .setPositiveButton("确定", onClickListener)
                .setNegativeButton("取消", null)
                .show();
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

    private CharSequence getPriceSpan(String price) {
        SpannableString spannableString = new SpannableString(price);
        int index = price.indexOf(".");
        spannableString.setSpan(new AbsoluteSizeSpan(24, true), index, index + 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        spannableString.setSpan(new AbsoluteSizeSpan(20, true), index + 1, price.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        return spannableString;
    }

    @Override
    protected void onDestroy() {
        closeOrderCountDown();
        super.onDestroy();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }
}

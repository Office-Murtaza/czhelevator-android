package com.kingyon.elevator.uis.fragments.main;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.chanven.lib.cptr.loadmore.SwipeRefreshHelper;
import com.jakewharton.rxbinding.widget.RxTextView;
import com.kingyon.elevator.R;
import com.kingyon.elevator.constants.Constants;
import com.kingyon.elevator.entities.ADEntity;
import com.kingyon.elevator.entities.FreshOrderEntity;
import com.kingyon.elevator.entities.NormalParamEntity;
import com.kingyon.elevator.entities.OrderDetailsEntity;
import com.kingyon.elevator.nets.CustomApiCallback;
import com.kingyon.elevator.nets.Net;
import com.kingyon.elevator.nets.NetService;
import com.kingyon.elevator.others.OnClickWithObjectListener;
import com.kingyon.elevator.uis.actiivty2.login.LoginActiivty;
import com.kingyon.elevator.uis.activities.order.OrderDetailsActivity;
import com.kingyon.elevator.uis.activities.order.OrderPayActivity;
import com.kingyon.elevator.uis.pops.OrderStatusWindow;
import com.kingyon.elevator.utils.CommonUtil;
import com.kingyon.elevator.utils.FormatUtils;
import com.kingyon.elevator.utils.LeakCanaryUtils;
import com.kingyon.elevator.utils.StatusBarUtil;
import com.leo.afbaselibrary.listeners.OnClickWithObjects;
import com.leo.afbaselibrary.nets.entities.PageListEntity;
import com.leo.afbaselibrary.nets.exceptions.ApiException;
import com.leo.afbaselibrary.nets.exceptions.ResultException;
import com.leo.afbaselibrary.uis.adapters.BaseAdapter;
import com.leo.afbaselibrary.uis.adapters.MultiItemTypeAdapter;
import com.leo.afbaselibrary.uis.adapters.holders.CommonHolder;
import com.leo.afbaselibrary.uis.fragments.BaseStateRefreshLoadingFragment;
import com.leo.afbaselibrary.utils.ScreenUtil;
import com.leo.afbaselibrary.utils.TimeUtil;
import com.leo.afbaselibrary.widgets.emptyprovider.FadeViewAnimProvider;
import com.yqritc.recyclerviewflexibledivider.HorizontalDividerItemDecoration;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.OnClick;
import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by GongLi on 2018/12/24.
 * Email：lc824767150@163.com
 */

public class OrderFragment extends BaseStateRefreshLoadingFragment<OrderDetailsEntity> {

    @BindView(R.id.pre_tv_title)
    TextView preTvTitle;
    @BindView(R.id.fl_title)
    FrameLayout flTitle;

    @BindView(R.id.back_close)
    ImageView back_close;

    private String status;
    private Subscription countDownSubscribe;
    private OrderStatusWindow orderStatusWindow;

    public static OrderFragment newInstance(NormalParamEntity entity) {
        Bundle args = new Bundle();
        if (entity != null) {
            args.putParcelable(CommonUtil.KEY_VALUE_1, entity);
        }
        OrderFragment fragment = new OrderFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public int getContentViewId() {
        return R.layout.fragment_order;
    }

    @Override
    public void init(Bundle savedInstanceState) {
        super.init(savedInstanceState);
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
        NormalParamEntity entity = null;
        if (getArguments() != null) {
            entity = getArguments().getParcelable(CommonUtil.KEY_VALUE_1);
        }
        status = entity == null ? "" : entity.getType();
        preTvTitle.setText(entity == null ? "全部订单" : entity.getName());
        //StatusBarUtil.setHeadViewPadding(getActivity(), flTitle);
        RxTextView.textChanges(preTvTitle)
                .debounce(600, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new CustomApiCallback<CharSequence>() {
                    @Override
                    protected void onResultError(ApiException ex) {
                    }

                    @Override
                    public void onNext(CharSequence charSequence) {
                        autoRefresh();
                    }
                });
        back_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().finish();
            }
        });
    }

    protected void setupRefreshAndLoadMore() {
        mSwipeRefreshHelper = new SwipeRefreshHelper(mLayoutRefresh);
        mSwipeRefreshHelper.setOnSwipeRefreshListener(this);
        mSwipeRefreshHelper.setOnLoadMoreListener(this);
    }

    @Override
    protected MultiItemTypeAdapter<OrderDetailsEntity> getAdapter() {
        return new BaseAdapter<OrderDetailsEntity>(getContext(), R.layout.fragment_order_item, mItems) {
            @Override
            protected void convert(CommonHolder holder, OrderDetailsEntity item, int position) {
                boolean waitPay = TextUtils.equals(Constants.OrderStatus.WAIT_PAY, item.getOrderStatus());
                holder.setTextNotHide(R.id.tv_time, String.format("下单时间：%s", TimeUtil.getAllTime(item.getCreateTime())));
                holder.setTextNotHide(R.id.tv_status, FormatUtils.getInstance().getOrderStatusStr(item.getOrderStatus()));
                String statusStr = FormatUtils.getInstance().getOrderStatusStr(item);
//                holder.setTextNotHide(R.id.tv_status, statusStr);
//                holder.setTextColor(R.id.tv_status, TextUtils.equals(statusStr, "未通过") ? 0xFFFF4800 : 0xff333333);
                holder.setVisible(R.id.img_not_pass, TextUtils.equals(statusStr, "未通过"));
                holder.setTextNotHide(R.id.tv_sn, String.format("订单编号：%s", item.getOrderSn()));
                holder.setText(R.id.tv_operate, FormatUtils.getInstance().getOrderOperateStr(item.getOrderStatus()));
                holder.setBackgroundRes(R.id.tv_operate, waitPay ? R.drawable.bg_six_btn : R.drawable.bg_six_red_btn);
                holder.setOnClickListener(R.id.tv_operate, new OnClickWithObjects(item) {
                    @Override
                    public void onClick(View view, Object[] objects) {
                        onOperateClick((OrderDetailsEntity) objects[0]);
                    }
                });

                ADEntity advertising = item.getAdvertising() != null ? item.getAdvertising() : new ADEntity();
                if (TextUtils.isEmpty(advertising.getVideoUrl())) {
                    holder.setImage(R.id.img_cover, advertising.getImageUrl());
                } else {
                    holder.setVideoImage(R.id.img_cover, advertising.getVideoUrl());
                }
                holder.setTextNotHide(R.id.tv_info_content, String.format("便民信息内容：%s", advertising.getTitle()));

                holder.setTextNotHide(R.id.tv_name, item.getName());
                holder.setTextNotHide(R.id.tv_screen_num, String.valueOf(item.getTotalScreen()));
                holder.setTextNotHide(R.id.tv_order_type, FormatUtils.getInstance().getPlanType(item.getOrderType()));
                holder.setTextNotHide(R.id.tv_price, CommonUtil.getTwoFloat(item.getRealPrice()));

                boolean beInfomationOrder = FormatUtils.getInstance().beInfomationPlan(item.getOrderType());
                holder.setVisible(R.id.cv_img, !beInfomationOrder);
                holder.setVisible(R.id.v_holder, !beInfomationOrder);
                holder.setVisible(R.id.tv_info_content, beInfomationOrder);


                holder.setVisible(R.id.v_line, waitPay);
                holder.setVisible(R.id.ll_remain_time, waitPay);
                if (waitPay) {
                    holder.setText(R.id.tv_remain_time, TimeUtil.getRemainingTimeSecond(item.getRemainTime()));
                }
            }
        };
    }

    @Override
    public void onItemClick(View view, RecyclerView.ViewHolder holder, OrderDetailsEntity item, int position) {
        super.onItemClick(view, holder, item, position);
        if (item != null) {
            Bundle bundle = new Bundle();
            bundle.putString(CommonUtil.KEY_VALUE_1, item.getObjctId());
            startActivity(OrderDetailsActivity.class, bundle);
        }
    }

    private void onOperateClick(OrderDetailsEntity entity) {
        switch (entity.getOrderStatus()) {
            case Constants.OrderStatus.WAIT_PAY:
                Bundle bundle = new Bundle();
                bundle.putString(CommonUtil.KEY_VALUE_1, entity.getObjctId());
                startActivity(OrderPayActivity.class, bundle);
//                cancelOrder(entity);
                break;
            case Constants.OrderStatus.CANCEL:
            case Constants.OrderStatus.COMPLETE:
            case Constants.OrderStatus.FAILED:
            case Constants.OrderStatus.OVERDUE:
            case Constants.OrderStatus.SOWING:
                deleteOrder(entity);
                break;
        }
    }

    @OnClick(R.id.pre_tv_title)
    public void onViewClicked() {
        if (orderStatusWindow == null) {
            orderStatusWindow = new OrderStatusWindow(getContext(), new OrderStatusWindow.OnStatusChangeListener() {
                @Override
                public void onStatusChange(NormalParamEntity entity) {
                    onStatusModify(entity);
                }
            });
        }
        orderStatusWindow.showAsDropDown(preTvTitle, status);
    }

    public void onStatusModify(NormalParamEntity entity) {
        if (entity == null) {
            return;
        }
        if (!TextUtils.equals(status, entity.getType())) {
            status = entity.getType();
            preTvTitle.setText(entity.getName());
        }
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            onfresh();
        }
    }

    @Override
    protected void loadData(final int page) {
        if (!TextUtils.isEmpty(Net.getInstance().getToken())) {
            NetService.getInstance().orderList(status,"", page)
                    .compose(this.<PageListEntity<OrderDetailsEntity>>bindLifeCycle())
                    .subscribe(new CustomApiCallback<PageListEntity<OrderDetailsEntity>>() {
                        @Override
                        protected void onResultError(ApiException ex) {
                            showToast(ex.getDisplayMessage());
                            loadingComplete(false, 100000);
                        }

                        @Override
                        public void onNext(PageListEntity<OrderDetailsEntity> orderDetailsEntityPageListEntity) {
                            if (orderDetailsEntityPageListEntity == null || orderDetailsEntityPageListEntity.getContent() == null) {
                                throw new ResultException(9001, "返回参数异常");
                            }
                            if (FIRST_PAGE == page) {
                                mItems.clear();
                            }
                            mItems.addAll(orderDetailsEntityPageListEntity.getContent());
                            loadingComplete(true, orderDetailsEntityPageListEntity.getTotalPages());
                            countDownFresh();
                        }
                    });
        } else {
            mCurrPage = FIRST_PAGE;
            mItems.clear();
            loadingComplete(true, 1);
        }
    }

    public void countDownFresh() {
        if (countDownSubscribe == null || countDownSubscribe.isUnsubscribed()) {
            countDownSubscribe = Observable.interval(1000, 1000, TimeUnit.MILLISECONDS)
                    .compose(this.<Long>bindLifeCycle())
                    .subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new CustomApiCallback<Long>() {
                        @Override
                        protected void onResultError(ApiException ex) {
                        }

                        @Override
                        public void onNext(Long aLong) {
                            boolean needFresh = false;
                            for (OrderDetailsEntity entity : mItems) {
                                if (TextUtils.equals(Constants.OrderStatus.WAIT_PAY, entity.getOrderStatus())) {
                                    entity.setRemainTime(entity.getRemainTime() - 1000);
                                    if (entity.getRemainTime() < 0) {
                                        needFresh = true;
                                    }
                                }
                            }
                            mAdapter.notifyDataSetChanged();
                            if (needFresh) {
                                onfresh();
                            }
                        }
                    });
        }
    }

    protected void closeCountDown() {
        if (countDownSubscribe != null && !countDownSubscribe.isUnsubscribed()) {
            countDownSubscribe.unsubscribe();
            countDownSubscribe = null;
        }
    }

    @Override
    protected void setDivider() {
        if (getContext() != null) {
            mRecyclerView.addItemDecoration(new HorizontalDividerItemDecoration.Builder(getContext())
                    .colorResId(R.color.background)
                    .size(ScreenUtil.dp2px(10))
                    .build());
        }
    }

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
                        if (mItems.contains(itemEntity)) {
                            mItems.remove(itemEntity);
                        }
                        mAdapter.notifyDataSetChanged();
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
                        itemEntity.setOrderStatus(Constants.OrderStatus.CANCEL);
                        mAdapter.notifyDataSetChanged();
                    }
                });
    }

    private void showTipDialog(String tipContent, DialogInterface.OnClickListener onClickListener) {
        if (getContext() != null) {
            new AlertDialog.Builder(getContext())
                    .setTitle("提示")
                    .setMessage(tipContent)
                    .setPositiveButton("确定", onClickListener)
                    .setNegativeButton("取消", null)
                    .show();
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onReceivedRefresh(FreshOrderEntity entity) {
        autoRefresh();
    }

    @Override
    public void onDestroy() {
        closeCountDown();
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
        super.onDestroy();
    }

    @Override
    protected String getEmptyTip() {
        boolean notLogin = TextUtils.isEmpty(Net.getInstance().getToken());
        String tip = notLogin ? "请先登录/注册" : "暂无数据";
        if (notLogin) {
            stateLayout.setEmptyViewTip("点击登录/注册");
        } else {
            stateLayout.setEmptyViewTip("");
        }
        return tip;
    }

    protected void initStateLayout() {
        stateLayout.setViewSwitchAnimProvider(new FadeViewAnimProvider());
        stateLayout.setErrorAndEmptyAction(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(Net.getInstance().getToken())) {
                    startActivity(LoginActiivty.class);
                } else {
                    autoLoading();
                }
            }
        });
        stateLayout.showProgressView(getString(com.leo.afbaselibrary.R.string.loading));
    }

    @Override
    protected void dealLeackCanary() {
        LeakCanaryUtils.watchLeakCanary(this);
    }

}

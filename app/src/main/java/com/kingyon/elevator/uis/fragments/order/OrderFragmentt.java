package com.kingyon.elevator.uis.fragments.order;

import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.blankj.utilcode.util.LogUtils;
import com.czh.myversiontwo.activity.ActivityUtils;
import com.kingyon.elevator.R;
import com.kingyon.elevator.entities.OrderDetailsEntity;
import com.kingyon.elevator.entities.entities.ConentEntity;
import com.kingyon.elevator.entities.entities.OrderComeEntiy;
import com.kingyon.elevator.nets.CustomApiCallback;
import com.kingyon.elevator.nets.NetService;
import com.kingyon.elevator.uis.activities.order.OrderDetailsActivity;
import com.kingyon.elevator.uis.dialogs.popwindow.MassageDelePopWindow;
import com.kingyon.elevator.uis.fragments.main.PlanNewFragment;
import com.kingyon.elevator.utils.CommonUtil;
import com.kingyon.elevator.utils.FormatUtils;
import com.kingyon.elevator.utils.utilstwo.AdUtils;
import com.kingyon.elevator.utils.utilstwo.IsSuccess;
import com.kingyon.elevator.utils.utilstwo.OrdinaryActivity;
import com.leo.afbaselibrary.nets.exceptions.ApiException;
import com.leo.afbaselibrary.nets.exceptions.ResultException;
import com.leo.afbaselibrary.uis.adapters.BaseAdapter;
import com.leo.afbaselibrary.uis.adapters.MultiItemTypeAdapter;
import com.leo.afbaselibrary.uis.adapters.holders.CommonHolder;
import com.leo.afbaselibrary.uis.fragments.BaseStateRefreshLoadingFragment;
import com.leo.afbaselibrary.utils.GlideUtils;
import com.leo.afbaselibrary.utils.TimeUtil;
import com.leo.afbaselibrary.utils.ToastUtils;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

import static com.czh.myversiontwo.utils.CodeType.OEDER_COMPLETE;
import static com.czh.myversiontwo.utils.CodeType.OEDER_RELEASEING;
import static com.czh.myversiontwo.utils.CodeType.OEDER_SOWING;
import static com.czh.myversiontwo.utils.CodeType.OEDER_WAITRELEASE;
import static com.czh.myversiontwo.utils.CodeType.REVIEWFAILD;
import static com.czh.myversiontwo.utils.CodeType.WAITREVIEW;
import static com.czh.myversiontwo.utils.StringContent.ORADER_NUMBER;
import static com.kingyon.elevator.utils.utilstwo.TokenUtils.isToken;

/**
 * @Created By Admin  on 2020/6/11
 * @Email : 163235610@qq.com
 * @Author:Mrczh
 * @Instructions:
 */
public class OrderFragmentt extends BaseStateRefreshLoadingFragment<OrderDetailsEntity> {

    Unbinder unbinder;
    String type, type1;

    public OrderFragmentt setIndex(String type, String type1) {
            this.type = type;
            this.type1 = type1;
            return (this);
    }

    @Override
    protected MultiItemTypeAdapter<OrderDetailsEntity> getAdapter() {
        return new BaseAdapter<OrderDetailsEntity>(getContext(), R.layout.itme_order_lits, mItems) {
            @Override
            protected void convert(CommonHolder holder, OrderDetailsEntity item, int position) {
                holder.setText(R.id.tv_order_time, String.format("下单时间：%s", TimeUtil.getAllTime(item.getCreateTime())));
                holder.setText(R.id.tv_title, item.getName());
                holder.setText(R.id.tv_point, "点位：共" + item.getTotalScreen() + "面屏");
                holder.setText(R.id.tv_type, "类型：" + FormatUtils.getInstance().getPlanType(item.getOrderType()));
                holder.setText(R.id.tv_number, String.format(ORADER_NUMBER, item.getTotalScreen(), CommonUtil.getTwoFloat(item.getRealPrice())));
                if (item.getLstHousingBean().size() > 0) {
                    GlideUtils.loadRoundCornersImage(getContext(), item.getLstHousingBean().get(0).housePic, holder.getView(R.id.img_img), 20);
                }
                holder.setVisible(R.id.tv_status, true);
                holder.setVisible(R.id.img_status, false);
                switch (item.getOrderStatus()) {
                    case OEDER_WAITRELEASE:
                        holder.setText(R.id.tv_status, "待发布");
                        holder.setVisible(R.id.tv_again, false);
                        holder.setVisible(R.id.img_delete, false);
                        LogUtils.e(item.getAuditState());
                        switch (item.getAuditState()) {
                            case WAITREVIEW:
                                /*审核中*/
                                holder.setVisible(R.id.img_status, true);
                                holder.setImageResource(R.id.img_status, R.mipmap.im_order_audit_wait);
                                break;
                            case REVIEWFAILD:
                                /*未通过*/
                                holder.setVisible(R.id.img_status, true);
                                holder.setImageResource(R.id.img_status, R.mipmap.im_order_audit_fail);
                                break;
                            default:
                                holder.setVisible(R.id.img_status, false);
                        }
                        break;
                    case OEDER_COMPLETE:
                        holder.setText(R.id.tv_status, "已完成");
                        holder.setVisible(R.id.tv_again, true);
                        holder.setVisible(R.id.img_delete, true);
                        holder.setText(R.id.tv_again, "再来一单");
                        switch (item.getAuditState()) {
                            case WAITREVIEW:
                                /*审核中*/
                                holder.setVisible(R.id.img_status, true);
                                holder.setImageResource(R.id.img_status, R.mipmap.im_order_audit_wait);
                                break;
                            case REVIEWFAILD:
                                /*未通过*/
                                holder.setVisible(R.id.img_status, true);
                                holder.setImageResource(R.id.img_status, R.mipmap.im_order_audit_fail);
                                break;
                            default:
                                holder.setVisible(R.id.img_status, false);
                        }
                        break;
                    case OEDER_SOWING:
                        holder.setText(R.id.tv_status, "已完成");
                        holder.setVisible(R.id.tv_again, true);
                        holder.setVisible(R.id.img_delete, true);
                        holder.setText(R.id.tv_again, "重新投放");
                        break;
                    case OEDER_RELEASEING:
                        holder.setVisible(R.id.tv_again, false);
                        holder.setVisible(R.id.img_delete, false);
                        holder.setText(R.id.tv_status, "发布中");
                        break;
                    default:

                }
                holder.setOnClickListener(R.id.tv_again, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        if (isToken(getActivity())) {
                            AdUtils.type = item.getOrderType();
                            httpOrderAgain(item.getOrderSn());
                        }else {
                            ActivityUtils.setLoginActivity();
                        }
                        LogUtils.e("再来一单");
                    }
                });
                holder.setOnClickListener(R.id.img_delete, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        MassageDelePopWindow window =  new MassageDelePopWindow(getActivity());
                        window.showAtBottom(holder.getView(R.id.img_delete));
                        window.onClick(new IsSuccess() {
                            @Override
                            public void isSuccess(boolean success) {
                                window.dismiss();
                                showProgressDialog(getString(R.string.wait));
                                NetService.getInstance().orderDelete(item.getOrderSn())
                                        .subscribe(new CustomApiCallback<String>() {
                                            @Override
                                            protected void onResultError(ApiException ex) {
                                                hideProgress();
                                                showToast(ex.getDisplayMessage());
                                            }
                                            @Override
                                            public void onNext(String s) {
                                                hideProgress();
                                                showToast("删除成功");
                                                autoRefresh();
                                            }
                                        });

                            }
                        });
                    }
                });
            }
        };
    }

    private void httpOrderAgain(String orderSn) {
        AdUtils.orderSn = orderSn;
        NetService.getInstance().orderAgain(orderSn)
                .compose(this.bindLifeCycle())
                .subscribe(new CustomApiCallback<List<OrderComeEntiy>>() {
                    @Override
                    protected void onResultError(ApiException ex) {
                        LogUtils.e(ex.getDisplayMessage(),ex.getCode());
                    }
                    @Override
                    public void onNext(List<OrderComeEntiy> orderComeEntiys) {
                        Bundle bundle = new Bundle();
                        bundle.putString("type","order");
                        startActivity(PlanNewFragment.class,bundle);
                    }
                });
    }

    @Override
    public void onItemClick(View view, RecyclerView.ViewHolder holder, OrderDetailsEntity item, int position) {
        super.onItemClick(view, holder, item, position);
        Bundle bundle = new Bundle();
        bundle.putString(CommonUtil.KEY_VALUE_1, item.getOrderSn());
        startActivity(OrderDetailsActivity.class, bundle);
    }

    @Override
    protected void loadData(int page) {
        NetService.getInstance().orderList(type, type1, page)
                .compose(this.bindLifeCycle())
                .subscribe(new CustomApiCallback<ConentEntity<OrderDetailsEntity>>() {
                    @Override
                    protected void onResultError(ApiException ex) {
                        showToast(ex.getDisplayMessage());
                        loadingComplete(false, 100000);
                    }

                    @Override
                    public void onNext(ConentEntity<OrderDetailsEntity> orderDetailsEntityPageListEntity) {
                        if (orderDetailsEntityPageListEntity == null || orderDetailsEntityPageListEntity.getContent() == null) {
                            throw new ResultException(9001, "返回参数异常");
                        }
                        if (FIRST_PAGE == page) {
                            mItems.clear();
                        }
                        mItems.addAll(orderDetailsEntityPageListEntity.getContent());
                        if (page > 1 && orderDetailsEntityPageListEntity.getContent().size() <= 0) {
                            showToast("已经没有了");
                        }
                        loadingComplete(true, orderDetailsEntityPageListEntity.getTotalPages());
                    }
                });
    }

    @Override
    public int getContentViewId() {
        return R.layout.fragment_ordertwot;
    }

    @Override
    protected void dealLeackCanary() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // TODO: inflate a fragment view
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        unbinder = ButterKnife.bind(this, rootView);
        return rootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

}

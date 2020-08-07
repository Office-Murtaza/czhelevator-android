package com.kingyon.elevator.uis.activities.cooperation;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.czh.myversiontwo.activity.ActivityUtils;
import com.kingyon.elevator.R;
import com.kingyon.elevator.date.DateUtils;
import com.kingyon.elevator.entities.entities.ConentTxEntity;
import com.kingyon.elevator.entities.entities.EarningsYesterdayEnity;
import com.kingyon.elevator.entities.entities.StatisticalEnity;
import com.kingyon.elevator.nets.CustomApiCallback;
import com.kingyon.elevator.nets.NetService;
import com.kingyon.elevator.uis.adapters.adapterone.IncomeAdapter;
import com.leo.afbaselibrary.nets.exceptions.ApiException;
import com.leo.afbaselibrary.uis.activities.BaseActivity;
import com.leo.afbaselibrary.utils.ToastUtils;
import com.qiniu.android.utils.StringUtils;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.czh.myversiontwo.utils.Constance.ACTIVITY_EARNINGS_YESTERDAY;
import static com.czh.myversiontwo.utils.Constance.ACTIVITY_HAVE_WITHDRAWAL;

/**
 * @Created By Admin  on 2020/7/2
 * @Email : 163235610@qq.com
 * @Author:Mrczh
 * @Instructions:已提现
 */
@Route(path = ACTIVITY_HAVE_WITHDRAWAL)
public class HaveWithdrawalActivity extends BaseActivity {
    @BindView(R.id.img_top_back)
    ImageView imgTopBack;
    @BindView(R.id.tv_top_title)
    TextView tvTopTitle;
    @BindView(R.id.tv_right)
    TextView tvRight;
    @BindView(R.id.tv_time)
    TextView tvTime;
    @BindView(R.id.tv_money)
    TextView tvMoney;
    @BindView(R.id.rv_attention_list)
    RecyclerView rvAttentionList;
    @BindView(R.id.rl_error)
    RelativeLayout rlError;
    @BindView(R.id.rl_null)
    RelativeLayout rlNull;
    @BindView(R.id.rl_notlogin)
    RelativeLayout rlNotlogin;
    int page = 1;
    List<EarningsYesterdayEnity> list = new ArrayList<>();
    IncomeAdapter incomeAdapter;
    @BindView(R.id.smart_refresh_layout)
    SmartRefreshLayout smartRefreshLayout;

    @Override
    public int getContentViewId() {
        return R.layout.activity_earnings_yesterday;
    }

    @Override
    public void init(Bundle savedInstanceState) {

        getYesterdayIncomeData(page);

        tvTopTitle.setText("已提现");
        smartRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                list.clear();
                page = 1;
                getYesterdayIncomeData(page);
                smartRefreshLayout.finishRefresh();

            }
        });
        smartRefreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                page++;
                getYesterdayIncomeData(page);
                smartRefreshLayout.finishLoadMore();
            }
        });
    }

    /**
     * 获取昨日收益数据
     */
    public void getYesterdayIncomeData(int page) {
        showProgressDialog(getString(R.string.wait),true);
        NetService.getInstance().getCashedList(page)
                .compose(this.bindLifeCycle())
                .subscribe(new CustomApiCallback<ConentTxEntity<StatisticalEnity<EarningsYesterdayEnity>>>() {
                    @Override
                    protected void onResultError(ApiException ex) {
                        hideProgress();
                        if (ex.getCode() == -102) {
                            if (page > 1) {
                                ToastUtils.showToast(HaveWithdrawalActivity.this, ex.getDisplayMessage(), 1000);
                            } else {
                                rvAttentionList.setVisibility(View.GONE);
                                rlError.setVisibility(View.GONE);
                                rlNull.setVisibility(View.VISIBLE);
                                rlNotlogin.setVisibility(View.GONE);
                            }

                        } else if (ex.getCode() == 100200) {
                            rvAttentionList.setVisibility(View.GONE);
                            rlError.setVisibility(View.GONE);
                            rlNull.setVisibility(View.GONE);
                            rlNotlogin.setVisibility(View.VISIBLE);
                        } else {
                            rvAttentionList.setVisibility(View.GONE);
                            rlError.setVisibility(View.VISIBLE);
                            rlNull.setVisibility(View.GONE);
                            rlNotlogin.setVisibility(View.GONE);
                        }
                    }

                    @Override
                    public void onNext(ConentTxEntity<StatisticalEnity<EarningsYesterdayEnity>> incomeDetailsEntities) {
                        hideProgress();
                        tvMoney.setText("￥" + incomeDetailsEntities.pageContent.subtotal + "");
                        tvTime.setText(DateUtils.getCurrentTime());
                        dataAdd(incomeDetailsEntities);
                        rvAttentionList.setVisibility(View.VISIBLE);
                        rlError.setVisibility(View.GONE);
                        rlNull.setVisibility(View.GONE);
                        rlNotlogin.setVisibility(View.GONE);
                    }
                });
    }

    private void dataAdd(ConentTxEntity<StatisticalEnity<EarningsYesterdayEnity>> incomeDetailsEntities) {
        for (int i = 0; i < incomeDetailsEntities.pageContent.lstResponse.size(); i++) {
            EarningsYesterdayEnity queryRecommendEntity = new EarningsYesterdayEnity();
            queryRecommendEntity = incomeDetailsEntities.pageContent.lstResponse.get(i);
            list.add(queryRecommendEntity);
        }
        if (incomeAdapter == null || page == 1) {
            incomeAdapter = new IncomeAdapter(HaveWithdrawalActivity.this, list, "2");
            incomeAdapter.reflashData(list);
            rvAttentionList.setAdapter(incomeAdapter);
            rvAttentionList.setLayoutManager(new GridLayoutManager(this, 1, GridLayoutManager.VERTICAL, false));
        } else {
            incomeAdapter.reflashData(list);
            incomeAdapter.notifyDataSetChanged();
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }

    @OnClick({R.id.img_top_back, R.id.rl_error, R.id.rl_notlogin})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.img_top_back:
                finish();
                break;
            case R.id.rl_error:
                getYesterdayIncomeData(1);
                break;
            case R.id.rl_notlogin:
                ActivityUtils.setLoginActivity();
                break;
        }
    }
}

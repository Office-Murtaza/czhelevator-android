package com.kingyon.elevator.uis.activities.cooperation;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.kingyon.elevator.R;
import com.kingyon.elevator.constants.Constants;
import com.kingyon.elevator.entities.IncomeStatisticsEntity;
import com.kingyon.elevator.entities.NormalParamEntity;
import com.kingyon.elevator.nets.CustomApiCallback;
import com.kingyon.elevator.nets.NetService;
import com.kingyon.elevator.uis.pops.IncomeFilterWindow;
import com.kingyon.elevator.uis.widgets.CooperationIncomeStickyDecoration;
import com.kingyon.elevator.utils.CommonUtil;
import com.kingyon.elevator.utils.TimeUtil;
import com.leo.afbaselibrary.nets.entities.PageListEntity;
import com.leo.afbaselibrary.nets.exceptions.ApiException;
import com.leo.afbaselibrary.nets.exceptions.ResultException;
import com.leo.afbaselibrary.uis.activities.BaseStateRefreshingLoadingActivity;
import com.leo.afbaselibrary.uis.adapters.BaseAdapter;
import com.leo.afbaselibrary.uis.adapters.MultiItemTypeAdapter;
import com.leo.afbaselibrary.uis.adapters.holders.CommonHolder;
import com.yqritc.recyclerviewflexibledivider.HorizontalDividerItemDecoration;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by GongLi on 2019/1/14.
 * Email：lc824767150@163.com
 */

public class CooperationIncomeActivity extends BaseStateRefreshingLoadingActivity<IncomeStatisticsEntity> implements CooperationIncomeStickyDecoration.GroupStickyListener {
    @BindView(R.id.pre_v_right)
    TextView preVRight;
    @BindView(R.id.head_root)
    View headRoot;

    protected String filter;
    private boolean beYear;
    private boolean beMonth;
    private boolean beDay;

    protected IncomeFilterWindow incomeFilterWindow;
    protected double totalMoney;

    @Override
    public int getContentViewId() {
        return R.layout.activity_cooperation_income;
    }

    @Override
    protected String getTitleText() {
        filter = getIntent().getStringExtra(CommonUtil.KEY_VALUE_1);
        String result;
        if (TextUtils.isEmpty(filter)) {
            filter = Constants.INCOME_FILTER.DAY;
            result = "收益明细";
        } else {
            switch (filter) {
                case Constants.INCOME_FILTER.YEAR:
                    result = "本年收益";
                    break;
                case Constants.INCOME_FILTER.DAY:
                    result = "今日收益";
                    break;
                default:
                    result = "本月收益";
                    break;
            }
        }
        updateBoolean();
        return result;
    }

    private void updateBoolean() {
        beYear = TextUtils.equals(Constants.INCOME_FILTER.YEAR, filter);
        beMonth = TextUtils.equals(Constants.INCOME_FILTER.MONTH, filter);
        beDay = TextUtils.equals(Constants.INCOME_FILTER.DAY, filter);
    }

    @Override
    protected void initViews(Bundle savedInstanceState) {
        super.initViews(savedInstanceState);
        mRecyclerView.addItemDecoration(new CooperationIncomeStickyDecoration(this, this, true));
        preVRight.setText("筛选");
        preVRight.setVisibility(TextUtils.equals("收益明细", tvTitle.getText()) ? View.VISIBLE : View.GONE);
    }

    @Override
    protected MultiItemTypeAdapter<IncomeStatisticsEntity> getAdapter() {
        return new BaseAdapter<IncomeStatisticsEntity>(this, R.layout.activity_cooperation_income_item, mItems) {
            @Override
            protected void convert(CommonHolder holder, IncomeStatisticsEntity item, int position) {
                if (beYear) {
                    holder.setTextNotHide(R.id.tv_time, TimeUtil.getYmCh(item.getTime()));
                } else if (beDay) {
                    holder.setTextNotHide(R.id.tv_time, com.leo.afbaselibrary.utils.TimeUtil.getAllTimeNoSecond(item.getTime()));
                } else {
                    holder.setTextNotHide(R.id.tv_time, com.leo.afbaselibrary.utils.TimeUtil.getYMdTime(item.getTime()));
                }
                float sum = item.getSum();
                holder.setTextNotHide(R.id.tv_sum, sum > 0 ? String.format("+%s", CommonUtil.getTwoFloat(sum)) : CommonUtil.getTwoFloat(sum));
            }
        };
    }

    @Override
    protected void loadData(final int page) {
        NetService.getInstance().partnerIncomeStatistics(filter, page)
                .compose(this.<PageListEntity<IncomeStatisticsEntity>>bindLifeCycle())
                .subscribe(new CustomApiCallback<PageListEntity<IncomeStatisticsEntity>>() {
                    @Override
                    protected void onResultError(ApiException ex) {
                        showToast(ex.getDisplayMessage());
                        loadingComplete(false, 100000);
                    }

                    @Override
                    public void onNext(PageListEntity<IncomeStatisticsEntity> incomeStatisticsEntityPageListEntity) {
                        if (incomeStatisticsEntityPageListEntity == null) {
                            throw new ResultException(9001, "返回参数异常");
                        }
                        totalMoney = incomeStatisticsEntityPageListEntity.getTotalAmount();
                        if (FIRST_PAGE == page) {
                            mItems.clear();
                        }
                        if (incomeStatisticsEntityPageListEntity.getContent() != null) {
                            mItems.addAll(incomeStatisticsEntityPageListEntity.getContent());
                        }
                        loadingComplete(true, incomeStatisticsEntityPageListEntity.getTotalPages());
                    }
                });
    }

    @Override
    protected void setDivider() {
        mRecyclerView.addItemDecoration(new HorizontalDividerItemDecoration.Builder(this)
                .drawable(R.drawable.white_margin_sixteen_divider)
                .sizeResId(R.dimen.spacing_divider)
                .build());
    }

    @Override
    public boolean isFirstInGroup(int pos) {
        boolean result;
//        if (beYear) {
//            result = false;
//        } else {
        if (pos == 0) {
            result = true;
        } else {
            String prevGroupId = getGroupName(pos - 1);
            String groupId = getGroupName(pos);
            result = !TextUtils.equals(prevGroupId, groupId);
        }
//        }
        return result;
    }

    @Override
    public String getGroupName(int position) {
        String result = null;
//        if (!beYear) {
//            if (position >= 0 && position < mItems.size()) {
//                IncomeStatisticsEntity item = mItems.get(position);
//                result = beDay ? TimeUtil.getYmCh(item.getTime()) : TimeUtil.getYCh(item.getTime());
//            }
//        }
        if (position >= 0 && position < mItems.size()) {
            IncomeStatisticsEntity item = mItems.get(position);
            if (beYear) {
                result = TimeUtil.getYCh(item.getTime());
            } else if (beDay) {
                result = TimeUtil.getYmdCh(item.getTime());
            } else {
                result = TimeUtil.getYmCh(item.getTime());
            }
        }
        return result;
    }

    @Override
    public double getTotal() {
        return totalMoney;
    }

    @OnClick(R.id.pre_v_right)
    public void onViewClicked() {
        if (incomeFilterWindow == null) {
            incomeFilterWindow = new IncomeFilterWindow(this, new IncomeFilterWindow.OnFilterChangeListener() {
                @Override
                public void onFilterChage(NormalParamEntity entity) {
                    onFilterModify(entity);
                }
            });
        }
        incomeFilterWindow.showAsDropDown(headRoot, filter);
    }

    public void onFilterModify(NormalParamEntity entity) {
        if (entity == null) {
            return;
        }
        if (!TextUtils.equals(filter, entity.getType())) {
            filter = entity.getType();
            updateBoolean();
            autoRefresh();
        }
    }
}

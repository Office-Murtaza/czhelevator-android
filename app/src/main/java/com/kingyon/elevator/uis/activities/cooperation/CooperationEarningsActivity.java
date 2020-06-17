package com.kingyon.elevator.uis.activities.cooperation;

import android.os.Bundle;
import android.widget.TextView;

import com.kingyon.elevator.R;
import com.kingyon.elevator.entities.IncomeStatisticsEntity;
import com.kingyon.elevator.nets.CustomApiCallback;
import com.kingyon.elevator.nets.NetService;
import com.kingyon.elevator.utils.CommonUtil;
import com.leo.afbaselibrary.nets.entities.PageListEntity;
import com.leo.afbaselibrary.nets.exceptions.ApiException;
import com.leo.afbaselibrary.nets.exceptions.ResultException;
import com.leo.afbaselibrary.uis.activities.BaseStateRefreshingLoadingActivity;
import com.leo.afbaselibrary.uis.adapters.BaseAdapter;
import com.leo.afbaselibrary.uis.adapters.MultiItemTypeAdapter;
import com.leo.afbaselibrary.uis.adapters.holders.CommonHolder;
import com.leo.afbaselibrary.utils.TimeUtil;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import java.util.Calendar;

import butterknife.BindView;
import butterknife.OnClick;
import rx.Subscription;

public class CooperationEarningsActivity extends BaseStateRefreshingLoadingActivity<IncomeStatisticsEntity> {
    @BindView(R.id.tv_start_time)
    TextView tvStartTime;
    @BindView(R.id.tv_end_time)
    TextView tvEndTime;
    @BindView(R.id.tv_total)
    TextView tvTotal;

    protected long startTime;
    protected long endTime;

    private DatePickerDialog startDialog;
    private DatePickerDialog endDialog;
    private Long startTimeCache;

    protected Subscription subscription;

    @Override
    protected String getTitleText() {
        return "收益明细";
    }

    @Override
    public int getContentViewId() {
        return R.layout.activity_cooperation_earnings;
    }

    @Override
    public void init(Bundle savedInstanceState) {
        super.init(savedInstanceState);
        long curTime = System.currentTimeMillis();
        startTime = TimeUtil.getDayStartTimeMilliseconds(curTime);
        endTime = TimeUtil.getDayEndTimeMilliseconds(curTime);
        updateTimeUI();
    }

    @Override
    protected MultiItemTypeAdapter<IncomeStatisticsEntity> getAdapter() {
        return new BaseAdapter<IncomeStatisticsEntity>(this, R.layout.activity_cooperation_earnings_item, mItems) {
            @Override
            protected void convert(CommonHolder holder, IncomeStatisticsEntity item, int position) {
                holder.setTextNotHide(R.id.tv_time, TimeUtil.getAllTimeNoSecond(item.getTime()));
                float sum = item.getSum();
                holder.setTextNotHide(R.id.tv_sum, sum > 0 ? String.format("+%s", CommonUtil.getTwoFloat(sum)) : CommonUtil.getTwoFloat(sum));
            }
        };
    }

    @Override
    protected void loadData(int page) {
        if (subscription != null && !subscription.isUnsubscribed()) {
            subscription.unsubscribe();
        }
        subscription = NetService.getInstance().partnerEarningsDetails(startTime, endTime, page)
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
                        if (FIRST_PAGE == page) {
                            updateTotal(incomeStatisticsEntityPageListEntity.getTotalAmount());
                            mItems.clear();
                        }
                        if (incomeStatisticsEntityPageListEntity.getContent() != null) {
                            mItems.addAll(incomeStatisticsEntityPageListEntity.getContent());
                        }
                        loadingComplete(true, incomeStatisticsEntityPageListEntity.getTotalPages());
                    }
                });
    }

    @OnClick(R.id.ll_time)
    public void onViewClicked() {
        showStartPicker();
    }

    private void showStartPicker() {
        Calendar now = Calendar.getInstance();
        Calendar min = Calendar.getInstance();
        min.add(Calendar.MONTH, -6);
        if (startDialog == null) {
            startDialog = DatePickerDialog.newInstance(
                    new DatePickerDialog.OnDateSetListener() {
                        @Override
                        public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
                            long startResult = TimeUtil.getDayStartTimeMilliseconds(com.kingyon.elevator.utils.TimeUtil.getLongTime(year + "-" + (monthOfYear + 1) + "-" + dayOfMonth));
                            startTimeCache = startResult;
                            showEndPicker();
                        }
                    },
                    now.get(Calendar.YEAR),
                    now.get(Calendar.MONTH),
                    now.get(Calendar.DAY_OF_MONTH)
            );
            startDialog.setTitle("请选择开始时间");
        }
        startDialog.setMaxDate(now);
        startDialog.setMinDate(min);
        startDialog.show(getFragmentManager(), "Datepickerdialog_start");
    }

    private void showEndPicker() {
        Calendar now = Calendar.getInstance();
        Calendar start = Calendar.getInstance();
        start.setTimeInMillis(startTimeCache);
        if (endDialog == null) {
            endDialog = DatePickerDialog.newInstance(
                    new DatePickerDialog.OnDateSetListener() {
                        @Override
                        public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
                            long endResult = TimeUtil.getDayEndTimeMilliseconds(com.kingyon.elevator.utils.TimeUtil.getLongTime(year + "-" + (monthOfYear + 1) + "-" + dayOfMonth));
                            if (endResult > startTimeCache) {
                                startTime = startTimeCache;
                                endTime = endResult;
                                updateTimeUI();
                                autoRefresh();
                            } else {
                                showToast("结束时间不能大于开始时间");
                            }
                        }
                    },
                    now.get(Calendar.YEAR),
                    now.get(Calendar.MONTH),
                    now.get(Calendar.DAY_OF_MONTH)
            );
            endDialog.setTitle("请选择结束时间");
        }
        endDialog.setMinDate(start);
        endDialog.setMaxDate(now);
        endDialog.show(getFragmentManager(), "Datepickerdialog_end");
    }

    private void updateTimeUI() {
        tvStartTime.setText(TimeUtil.getYMdTime(startTime));
        tvEndTime.setText(TimeUtil.getYMdTime(endTime));
    }

    protected void updateTotal(double total) {
        tvTotal.setText(CommonUtil.getMayTwoFloat(total));
    }
}

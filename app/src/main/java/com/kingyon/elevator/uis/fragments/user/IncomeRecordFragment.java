package com.kingyon.elevator.uis.fragments.user;


import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.LogUtils;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.kingyon.elevator.R;
import com.kingyon.elevator.constants.FragmentConstants;
import com.kingyon.elevator.customview.MyMarkView;
import com.kingyon.elevator.customview.MyTextItemView;
import com.kingyon.elevator.date.DateUtils;
import com.kingyon.elevator.entities.ChartSelectParameterEntity;
import com.kingyon.elevator.entities.IncomeOrPayEntity;
import com.kingyon.elevator.entities.MonthOrDayIncomeOrPayEntity;
import com.kingyon.elevator.entities.entities.EarningsTopEntity;
import com.kingyon.elevator.entities.entities.EarningsTwoYearlistEntity;
import com.kingyon.elevator.entities.entities.chartentily;
import com.kingyon.elevator.mvpbase.MvpBaseFragment;
import com.kingyon.elevator.presenter.IncomeRecordPresenter;
import com.kingyon.elevator.utils.CommonUtil;
import com.kingyon.elevator.utils.DensityUtil;
import com.kingyon.elevator.utils.DialogUtils;
import com.kingyon.elevator.utils.MyActivityUtils;
import com.kingyon.elevator.utils.RuntimeUtils;
import com.kingyon.elevator.view.IncomeRecordView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 收益记录
 */
public class IncomeRecordFragment extends MvpBaseFragment<IncomeRecordPresenter> implements IncomeRecordView, OnChartValueSelectedListener {

    @BindView(R.id.line_chart_view)
    LineChart line_chart_view;
    @BindView(R.id.chart_container)
    RelativeLayout chart_container;
    @BindView(R.id.tv_select_time)
    TextView tv_select_time;
    @BindView(R.id.user_pay_money)
    MyTextItemView user_pay_money;
    @BindView(R.id.user_income)
    MyTextItemView user_income;
    @BindView(R.id.tv_user_all_income)
    MyTextItemView tv_user_all_income;
    @BindView(R.id.tv_current_tips)
    TextView tv_current_tips;
    @BindView(R.id.tv_no_chart_data)
    TextView tv_no_chart_data;
    Button catIncomeBtn;//覆盖在markerview上的按钮，为了点击跳转到详情
    private Boolean isAddButton = false;
    private int markerViewWidth = 100;//单位为dp
    private int markerViewHeight = 35;//单位为dp
    private int selectCatType = 1;//当前按年查看  1表示为按月查看

    private int currentSelectYear = 2019;//当前选择的年份
    private int currentSelectMonth = 1;//当前选择的月份
    private int currentSelectDay = 1;//当前选择日期
    private int selectIncomeOrPay = 0;//选择的是收入还是支出  0收入 1支出
    MyMarkView myMarkView;
    YAxis yAxis;
    XAxis xAxis;
    private Highlight lastHighLight;

    @Override
    public IncomeRecordPresenter initPresenter() {
        return new IncomeRecordPresenter(getActivity());
    }

    @Override
    public int getContentViewId() {
        return R.layout.fragment_income_record;
    }

    @Override
    public void initView(Bundle savedInstanceState) {
        ButterKnife.bind(this, getContentView());
//        setStateLayout();
        currentSelectYear = DateUtils.getCurrentYear();
        currentSelectMonth = DateUtils.getCurrentMonth();
        currentSelectDay = DateUtils.getCurrentDay();
        tv_select_time.setText(currentSelectYear + "年" + currentSelectMonth + "月");
        selectCatType = 1;
        selectIncomeOrPay = 0;
        creatButton();
        initChartView();
        LogUtils.e(selectCatType, currentSelectYear, currentSelectMonth);
        presenter.getIncomeAndPayData(selectCatType, currentSelectYear, currentSelectMonth);
//        presenter.getIncomePayDataPerDay(selectIncomeOrPay, selectCatType, currentSelectYear, currentSelectMonth);
    }


    private void initChartView() {
        line_chart_view.getDescription().setEnabled(false);
        // enable touch gestures
        line_chart_view.setTouchEnabled(true);
        // set listeners
        line_chart_view.setOnChartValueSelectedListener(this);
        line_chart_view.setDrawGridBackground(false);
        line_chart_view.getLegend().setEnabled(false);
        line_chart_view.setScaleXEnabled(false);
        line_chart_view.setScaleYEnabled(false);
        myMarkView = new MyMarkView(getActivity(), R.layout.my_marke_view_layout);
        // Set the marker to the chart
        myMarkView.setShowType(selectIncomeOrPay, selectCatType, currentSelectYear, currentSelectMonth);
        myMarkView.setChartView(line_chart_view);
        line_chart_view.setMarker(myMarkView);
        xAxis = line_chart_view.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setDrawGridLines(false);


        yAxis = line_chart_view.getAxisLeft();
        line_chart_view.getAxisRight().setEnabled(false);
        yAxis.enableGridDashedLine(10f, 0f, 0f);
        yAxis.setTextColor(Color.parseColor("#00000000"));
        yAxis.setAxisMinimum(0);
        yAxis.setLabelCount(5);
        yAxis.setDrawLabels(false);
    }


    /**
     * 按年展示数据
     */
    private void showYeardata(MonthOrDayIncomeOrPayEntity monthOrDayIncomeOrPayEntity) {
        xAxis.setAxisMinimum(0f);
        xAxis.setGranularity(1.0f);
        xAxis.setAxisMaximum(monthOrDayIncomeOrPayEntity.getList().size());
        xAxis.setLabelCount(monthOrDayIncomeOrPayEntity.getList().size());
        yAxis.setAxisMaximum((float) monthOrDayIncomeOrPayEntity.getMaxValue() * 2);
        yAxis.setAxisMinimum(0f);
        yAxis.setLabelCount(5);
        line_chart_view.getViewPortHandler().setMaximumScaleX(1.0f);
        line_chart_view.getViewPortHandler().setMinimumScaleX(1.0f);
        line_chart_view.setScaleMinima(1f, 1f);
        setYearData(monthOrDayIncomeOrPayEntity);
    }


    private void showMonthData(MonthOrDayIncomeOrPayEntity monthOrDayIncomeOrPayEntity) {
        xAxis.setAxisMinimum(0f);
        xAxis.setAxisMaximum(monthOrDayIncomeOrPayEntity.getList().size());
        xAxis.setGranularity(1.0f);
        xAxis.setLabelCount(monthOrDayIncomeOrPayEntity.getList().size());
        line_chart_view.getViewPortHandler().setMaximumScaleX(2.0f);
        line_chart_view.setScaleMinima(2f, 1f);
        yAxis.setAxisMaximum((float) monthOrDayIncomeOrPayEntity.getMaxValue() * 2);
        yAxis.setAxisMinimum(0f);
        yAxis.setLabelCount(5);
        setData(monthOrDayIncomeOrPayEntity);
    }


    private void setYearData(MonthOrDayIncomeOrPayEntity monthOrDayIncomeOrPayEntity) {
        ArrayList<Entry> values = new ArrayList<>();
        for (int i = 0; i < monthOrDayIncomeOrPayEntity.getList().size(); i++) {
            values.add(new Entry(i + 1, (float) monthOrDayIncomeOrPayEntity.getList().get(i).getValue(), null));
        }
        LogUtils.d("图表数据集大小：" + values.size());
        LineDataSet set1;
        if (line_chart_view.getData() != null) {
            line_chart_view.clearValues();
        }
        set1 = new LineDataSet(values, "提示信息");
        set1.setDrawIcons(false);
        set1.setColor(Color.parseColor("#74749B"));
        set1.setCircleColor(Color.parseColor("#569EBE"));
        set1.setLineWidth(1f);
        set1.setCircleRadius(2f);
        set1.setDrawCircleHole(false);
        set1.setFormLineWidth(1f);
        set1.setFormSize(15.f);
        set1.setDrawValues(false);
        // text size of values
        set1.setValueTextSize(9f);
        set1.setDrawHorizontalHighlightIndicator(false);
        // draw selection line as dashed
        set1.enableDashedHighlightLine(10f, 0f, 0f);
        // set the filled area
        set1.setDrawFilled(false);
        set1.setFillFormatter((dataSet, dataProvider) -> line_chart_view.getAxisLeft().getAxisMinimum());
        set1.setHighLightColor(Color.parseColor("#74749B"));
        set1.setFillColor(Color.parseColor("#00000000"));
        ArrayList<ILineDataSet> dataSets = new ArrayList<>();
        dataSets.add(set1);
        LineData data = new LineData(dataSets);
        line_chart_view.setData(data);
        line_chart_view.invalidate();
        line_chart_view.highlightValue(currentSelectMonth, 0, true);
    }


    @OnClick({R.id.tv_select_time, R.id.user_income, R.id.user_pay_money, R.id.tv_no_chart_data})
    public void OnClick(View view) {
        switch (view.getId()) {
            case R.id.tv_select_time:
                DialogUtils.getInstance().showSelectDateDialog(getActivity(), (type, year, month) -> {
                    currentSelectYear = year;
                    currentSelectMonth = month;
                    selectCatType = type;
                    if (selectCatType == 0) {
                        //按年查看数据
                        tv_select_time.setText(year + "年");
                    } else {
                        tv_select_time.setText(currentSelectYear + "年" + currentSelectMonth + "月");
                    }
                    myMarkView.setShowType(selectIncomeOrPay, selectCatType, currentSelectYear, currentSelectMonth);
                    presenter.filterIncomeAndPayData(selectCatType, currentSelectYear, currentSelectMonth);
                    presenter.getIncomePayDataPerDay(selectIncomeOrPay, selectCatType, currentSelectYear, currentSelectMonth);
                    if (selectIncomeOrPay == 0) {
                        setIncomeDesc();
                    } else {
                        setPayDesc();
                    }
                });
                break;
            case R.id.user_income:
                /*收入*/
                selectIncomeOrPay = 0;
                user_income.setMainTitleColor(Color.parseColor("#DD575F"));
                user_pay_money.setMainTitleColor(Color.parseColor("#474747"));
                setIncomeDesc();
                myMarkView.setShowType(selectIncomeOrPay, selectCatType, currentSelectYear, currentSelectMonth);
                presenter.getIncomePayDataPerDay(selectIncomeOrPay, selectCatType, currentSelectYear, currentSelectMonth);
                break;
            case R.id.user_pay_money:
                /*支出*/
                selectIncomeOrPay = 1;
                user_income.setMainTitleColor(Color.parseColor("#474747"));
                user_pay_money.setMainTitleColor(Color.parseColor("#DD575F"));
                setPayDesc();
                myMarkView.setShowType(selectIncomeOrPay, selectCatType, currentSelectYear, currentSelectMonth);
                presenter.getIncomePayDataPerDay(selectIncomeOrPay, selectCatType, currentSelectYear, currentSelectMonth);
                break;
            case R.id.tv_no_chart_data:
                LogUtils.e(selectCatType, currentSelectYear, currentSelectMonth);
                presenter.getIncomeAndPayData(selectCatType, currentSelectYear, currentSelectMonth);
                presenter.getIncomePayDataPerDay(selectIncomeOrPay, selectCatType, currentSelectYear, currentSelectMonth);
                break;
        }
    }

    private void setIncomeDesc() {
        if (selectCatType == 0) {
            tv_current_tips.setText("每月收入");
        } else {
            tv_current_tips.setText("每日收入");
        }
    }


    private void setPayDesc() {
        if (selectCatType == 0) {
            tv_current_tips.setText("每月支出");
        } else {
            tv_current_tips.setText("每日支出");
        }
    }


    public static IncomeRecordFragment newInstance() {
        Bundle args = new Bundle();
        IncomeRecordFragment fragment = new IncomeRecordFragment();
        fragment.setArguments(args);
        return fragment;
    }


    private void creatButton() {
        catIncomeBtn = new Button(getActivity());
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(DensityUtil.dip2px(markerViewWidth), DensityUtil.dip2px(markerViewHeight));
        layoutParams.addRule(RelativeLayout.ALIGN_TOP);
        catIncomeBtn.setBackgroundColor(Color.parseColor("#00000000"));
        catIncomeBtn.setLayoutParams(layoutParams);
    }

    /**
     * 添加一个marker点击按钮
     */
    private void addButton(Entry e, Highlight h) {
        if (catIncomeBtn != null) {
            if (isAddButton) {
                chart_container.removeView(catIncomeBtn);
                isAddButton = false;
            }
            if (line_chart_view.getWidth() - h.getXPx() < DensityUtil.dip2px(markerViewWidth) / 2) {
                catIncomeBtn.setX(line_chart_view.getWidth() - DensityUtil.dip2px(markerViewWidth));
            } else {
                catIncomeBtn.setX(h.getXPx() - DensityUtil.dip2px(markerViewWidth) / 2);
            }
            catIncomeBtn.setY(0);
            catIncomeBtn.setOnClickListener(v -> {
                RuntimeUtils.chartSelectParameterEntity = new ChartSelectParameterEntity(selectCatType, currentSelectYear, currentSelectMonth, selectIncomeOrPay);
                RuntimeUtils.chartSelectParameterEntity.setCurrentSelectDay((int) e.getX());
                MyActivityUtils.goFragmentContainerActivity(getActivity(), FragmentConstants.IncomeWithYearOrMonth);
            });
            isAddButton = true;
            chart_container.addView(catIncomeBtn);
        }
    }


    @Override
    public void onValueSelected(Entry e, Highlight h) {
        //LogUtils.d("图表已设置高亮值-----------------------",GsonUtils.toJson(h));
        addButton(e, h);
        lastHighLight = h;
    }

    @Override
    public void onNothingSelected() {
        if (isAddButton) {
            chart_container.removeView(catIncomeBtn);
            isAddButton = false;
            if (lastHighLight!=null) {
                line_chart_view.highlightValue(lastHighLight);
            }
        }
    }

    private void setData(MonthOrDayIncomeOrPayEntity monthOrDayIncomeOrPayEntity) {

        ArrayList<Entry> values = new ArrayList<>();

        for (int i = 0; i < monthOrDayIncomeOrPayEntity.getList().size(); i++) {
            values.add(new Entry(i + 1, (float) monthOrDayIncomeOrPayEntity.getList().get(i).getValue(), null));
        }
        LineDataSet set1;

        if (line_chart_view.getData() != null) {
            line_chart_view.clearValues();
            line_chart_view.invalidate();
        }
        set1 = new LineDataSet(values, "提示信息");
        set1.setDrawIcons(false);
//        set1.setColor(Color.RED);
        set1.setColor(Color.parseColor("#74749B"));
        set1.setCircleColor(Color.parseColor("#45C7D6"));
        set1.setLineWidth(1f);
        set1.setCircleRadius(2f);
        set1.setDrawCircleHole(false);
        set1.setFormLineWidth(1f);
        set1.setFormSize(15.f);
        set1.setDrawValues(false);
        // text size of values
        set1.setValueTextSize(9f);
        set1.setDrawHorizontalHighlightIndicator(false);
        set1.enableDashedHighlightLine(10f, 0f, 0f);
        // set the filled area
        set1.setDrawFilled(false);
        set1.setFillFormatter((dataSet, dataProvider) -> line_chart_view.getAxisLeft().getAxisMinimum());
//        set1.setHighLightColor(Color.RED);
        set1.setHighLightColor(Color.parseColor("#74749B"));
        set1.setFillColor(Color.parseColor("#00000000"));
        ArrayList<ILineDataSet> dataSets = new ArrayList<>();
        dataSets.add(set1);
        LineData data = new LineData(dataSets);
        line_chart_view.setData(data);
        line_chart_view.invalidate();
        line_chart_view.highlightValue(currentSelectDay, 0, true);
        // }
    }



    @Override
    public void showIncomeOrPayData(EarningsTopEntity<EarningsTwoYearlistEntity> incomeOrPayEntity) {
        LogUtils.e(incomeOrPayEntity.toString());
        if (incomeOrPayEntity != null) {
            tv_user_all_income.setMainTitleText(CommonUtil.getMayTwoFloat(incomeOrPayEntity.totalEarning));
            user_income.setMainTitleText(CommonUtil.getMayTwoFloat(incomeOrPayEntity.totalIncome));
            user_pay_money.setMainTitleText(CommonUtil.getMayTwoFloat(incomeOrPayEntity.totalPay));
            if (incomeOrPayEntity.lstDayItem!=null&&incomeOrPayEntity.lstDayItem.size()>0){
                /*月*/
                MonthOrDayIncomeOrPayEntity monthOrDayIncomeOrPayEntity = new MonthOrDayIncomeOrPayEntity();
                monthOrDayIncomeOrPayEntity.setMaxValue(incomeOrPayEntity.totalIncome);
                List<MonthOrDayIncomeOrPayEntity.ListBean> list = new ArrayList<>();
                list.clear();
                if (selectIncomeOrPay==0){
                    /*收入*/
                    for (int i= 0;i<incomeOrPayEntity.lstDayItem.size();i++){
                                MonthOrDayIncomeOrPayEntity.ListBean listBean = new MonthOrDayIncomeOrPayEntity.ListBean();
                                listBean.setStep(incomeOrPayEntity.lstDayItem.get(i).day);
                                listBean.setValue(Math.abs(incomeOrPayEntity.lstDayItem.get(i).totalIncomeDay));
                                listBean.setTitle(incomeOrPayEntity.lstDayItem.get(i).day + "");
                                list.add(listBean);
                    }
                }else {
                    /*支出*/
                    for (int i= 0;i<incomeOrPayEntity.lstDayItem.size();i++){
                        MonthOrDayIncomeOrPayEntity.ListBean listBean = new MonthOrDayIncomeOrPayEntity.ListBean();
                        listBean.setStep(incomeOrPayEntity.lstDayItem.get(i).day);
                        listBean.setValue(Math.abs(incomeOrPayEntity.lstDayItem.get(i).totalPayDay));
                        listBean.setTitle(incomeOrPayEntity.lstDayItem.get(i).day + "");
                        list.add(listBean);
                    }
                }
                monthOrDayIncomeOrPayEntity.setList(list);
                showMonthData(monthOrDayIncomeOrPayEntity);

            }
            if (incomeOrPayEntity.lstMonthItem!=null&&incomeOrPayEntity.lstMonthItem.size()>0){
                /*年*/
                MonthOrDayIncomeOrPayEntity monthOrDayIncomeOrPayEntity = new MonthOrDayIncomeOrPayEntity();
                monthOrDayIncomeOrPayEntity.setMaxValue(incomeOrPayEntity.totalIncome);
                List<MonthOrDayIncomeOrPayEntity.ListBean> list = new ArrayList<>();
                list.clear();
                if (selectIncomeOrPay==0) {
                    for (int i = 0; i < incomeOrPayEntity.lstMonthItem.size(); i++) {
                        MonthOrDayIncomeOrPayEntity.ListBean listBean = new MonthOrDayIncomeOrPayEntity.ListBean();
                        listBean.setStep(incomeOrPayEntity.lstMonthItem.get(i).month);
                        listBean.setValue(Math.abs(incomeOrPayEntity.lstMonthItem.get(i).totalIncomeMonth));
                        listBean.setTitle(incomeOrPayEntity.lstMonthItem.get(i).month + "");
                        list.add(listBean);
                    }
                }else {
                    for (int i = 0; i < incomeOrPayEntity.lstMonthItem.size(); i++) {
                        MonthOrDayIncomeOrPayEntity.ListBean listBean = new MonthOrDayIncomeOrPayEntity.ListBean();
                        listBean.setStep(incomeOrPayEntity.lstMonthItem.get(i).month);
                        listBean.setValue(Math.abs(incomeOrPayEntity.lstMonthItem.get(i).totalPayMonth));
                        listBean.setTitle(incomeOrPayEntity.lstMonthItem.get(i).month + "");
                        list.add(listBean);
                    }
                }
                monthOrDayIncomeOrPayEntity.setList(list);
                showYeardata(monthOrDayIncomeOrPayEntity);
            }
        }
    }

    @Override
    public void showChartData(MonthOrDayIncomeOrPayEntity monthOrDayIncomeOrPayEntity) {
        tv_no_chart_data.setVisibility(View.GONE);
    }

    @Override
    public void showChartLoadingTips(String tips) {
        tv_no_chart_data.setText(tips);
        tv_no_chart_data.setVisibility(View.GONE);
    }

    @Override
    public void hideChartLoadingTips() {
        tv_no_chart_data.setVisibility(View.GONE);
    }


}

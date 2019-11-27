package com.kingyon.elevator.uis.fragments.user;


import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.GsonUtils;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IFillFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.interfaces.dataprovider.LineDataProvider;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.Utils;
import com.google.gson.Gson;
import com.kingyon.elevator.R;
import com.kingyon.elevator.constants.FragmentConstants;
import com.kingyon.elevator.customview.MyMarkView;
import com.kingyon.elevator.customview.MyTextItemView;
import com.kingyon.elevator.date.DatePickerListener;
import com.kingyon.elevator.date.DateUtils;
import com.kingyon.elevator.mvpbase.MvpBaseFragment;
import com.kingyon.elevator.presenter.IncomeRecordPresenter;
import com.kingyon.elevator.utils.DensityUtil;
import com.kingyon.elevator.utils.DialogUtils;
import com.kingyon.elevator.utils.MyActivityUtils;
import com.kingyon.elevator.view.IncomeRecordView;

import java.util.ArrayList;

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
    @BindView(R.id.tv_current_tips)
    TextView tv_current_tips;


    Button catIncomeBtn;//覆盖在markerview上的按钮，为了点击跳转到详情
    private Boolean isAddButton = false;
    private int markerViewWidth = 100;//单位为dp
    private int markerViewHeight = 35;//单位为dp
    private int selectCatType = 0;//当前按年查看  1表示为按月查看

    private int currentSelectYear = 0;//当前选择的年份
    private int currentSelectMonth = 0;//当前选择的月份
    private int selectIncomeOrPay = 0;//选择的是收入还是支出  0收入 1支出

    YAxis yAxis;
    XAxis xAxis;

    @Override
    public IncomeRecordPresenter initPresenter() {
        return new IncomeRecordPresenter(getActivity());
    }


    @Override
    public void initView(Bundle savedInstanceState) {
        ButterKnife.bind(this, getContentView());
        currentSelectYear = DateUtils.getCurrentYear();
        currentSelectMonth = DateUtils.getCurrentMonth();
        selectCatType = 0;
        selectIncomeOrPay = 0;
        creatButton();
        initChartView();
        showYeardata();
        //setData(12, 100);
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
        MyMarkView mv = new MyMarkView(getActivity(), R.layout.my_marke_view_layout);
        // Set the marker to the chart
        mv.setChartView(line_chart_view);
        line_chart_view.setMarker(mv);
        xAxis = line_chart_view.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setDrawGridLines(false);


        yAxis = line_chart_view.getAxisLeft();
        line_chart_view.getAxisRight().setEnabled(false);
        yAxis.enableGridDashedLine(10f, 0f, 0f);
        yAxis.setTextColor(Color.parseColor("#00000000"));
        yAxis.setAxisMinimum(0);
        yAxis.setDrawLabels(false);
    }


    /**
     * 按年展示数据
     */
    private void showYeardata() {
        xAxis.setAxisMinimum(0f);
        xAxis.setGranularity(1.0f);
        xAxis.setLabelCount(12);
        xAxis.setAxisMaximum(12f);
        yAxis.setAxisMaximum(110f);
        yAxis.setAxisMinimum(0f);
        line_chart_view.setScaleMinima(1f, 1f);
        setYearData();
    }


    private void showMonthData() {
        xAxis.setAxisMinimum(0f);
        xAxis.setAxisMaximum(31f);
        xAxis.setGranularity(1.0f);
        xAxis.setLabelCount(31);
        line_chart_view.setScaleMinima(2f, 1f);
        yAxis.setAxisMaximum(110f);
        yAxis.setAxisMinimum(0f);
        setData(31, 100);
    }


    private void setYearData() {
        ArrayList<Entry> values = new ArrayList<>();

        for (int i = 0; i < 12; i++) {

            float val = (float) (Math.random() * 100);
            values.add(new Entry(i + 1, val, null));
        }

        LineDataSet set1;

        if (line_chart_view.getData() != null) {
            line_chart_view.clearValues();
        }

//        if (line_chart_view.getData() != null &&
//                line_chart_view.getData().getDataSetCount() > 0) {
//            set1 = (LineDataSet) line_chart_view.getData().getDataSetByIndex(0);
//            set1.setValues(values);
//            set1.notifyDataSetChanged();
//            line_chart_view.getData().notifyDataChanged();
//            line_chart_view.notifyDataSetChanged();
//        } else {
        // create a dataset and give it a type
        set1 = new LineDataSet(values, "提示信息");

        set1.setDrawIcons(false);

        // draw dashed line
        //set1.enableDashedLine(10f, 5f, 0f);

        // black lines and points
        set1.setColor(Color.RED);
        set1.setCircleColor(Color.parseColor("#569EBE"));

        // line thickness and point size
        set1.setLineWidth(1f);
        set1.setCircleRadius(2f);

        // draw points as solid circles
        set1.setDrawCircleHole(false);

        // customize legend entry
        set1.setFormLineWidth(1f);
        //set1.setFormLineDashEffect(new DashPathEffect(new float[]{10f, 5f}, 0f));
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
        set1.setHighLightColor(Color.RED);
        set1.setFillColor(Color.parseColor("#00000000"));
        ArrayList<ILineDataSet> dataSets = new ArrayList<>();
        dataSets.add(set1);
        LineData data = new LineData(dataSets);
        line_chart_view.setData(data);
        //}
    }


    @Override
    public int getContentViewId() {
        return R.layout.fragment_income_record;
    }


    @OnClick({R.id.tv_select_time, R.id.user_income, R.id.user_pay_money})
    public void OnClick(View view) {
        switch (view.getId()) {
            case R.id.tv_select_time:
                DialogUtils.getInstance().showSelectDateDialog(getActivity(), (type, year, month) -> {
                    selectCatType = type;
                    if (type == 0) {
                        //按年查看数据
                        tv_select_time.setText(year + "年");
                        showYeardata();
                    } else {
                        tv_select_time.setText(year + "年" + month + "月");
                        showMonthData();
                    }
                    if (selectIncomeOrPay == 0) {
                        setIncomeDesc();
                    } else {
                        setPayDesc();
                    }
                });
                break;
            case R.id.user_income:
                selectIncomeOrPay = 0;
                user_income.setMainTitleColor(Color.parseColor("#DD575F"));
                user_pay_money.setMainTitleColor(Color.parseColor("#474747"));
                setIncomeDesc();
                break;
            case R.id.user_pay_money:
                selectIncomeOrPay = 1;
                user_income.setMainTitleColor(Color.parseColor("#474747"));
                user_pay_money.setMainTitleColor(Color.parseColor("#DD575F"));
                setPayDesc();
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
            catIncomeBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    LogUtils.e("点击查看事件--------------------------");
                    showShortToast("月份：" + e.getX() + "--收入：" + e.getY());
                    MyActivityUtils.goFragmentContainerActivity(getActivity(), FragmentConstants.IncomeWithMonth);
                }
            });
            isAddButton = true;
            chart_container.addView(catIncomeBtn);
        }
    }


    @Override
    public void onValueSelected(Entry e, Highlight h) {
        LogUtils.e("选中列表值了：", GsonUtils.toJson(h));
        LogUtils.e("列表宽高值了：", line_chart_view.getWidth(), line_chart_view.getHeight());
        addButton(e, h);
    }

    @Override
    public void onNothingSelected() {
        LogUtils.e("什么值都没选中-----------------");
        if (isAddButton) {
            chart_container.removeView(catIncomeBtn);
            isAddButton = false;
        }
    }

    private void setData(int count, float range) {

        ArrayList<Entry> values = new ArrayList<>();

        for (int i = 0; i < count; i++) {
            float val = (float) (Math.random() * range);
            values.add(new Entry(i + 1, val, null));
        }

        LineDataSet set1;

        if (line_chart_view.getData() != null) {
            line_chart_view.clearValues();
        }
//        if (line_chart_view.getData() != null &&
//                line_chart_view.getData().getDataSetCount() > 0) {
//            set1 = (LineDataSet) line_chart_view.getData().getDataSetByIndex(0);
//            set1.setValues(values);
//            set1.notifyDataSetChanged();
//            line_chart_view.getData().notifyDataChanged();
//            line_chart_view.notifyDataSetChanged();
//        } else {
        // create a dataset and give it a type
        set1 = new LineDataSet(values, "提示信息");

        set1.setDrawIcons(false);

        // draw dashed line
        //set1.enableDashedLine(10f, 5f, 0f);

        // black lines and points
        set1.setColor(Color.RED);
        set1.setCircleColor(Color.parseColor("#569EBE"));

        // line thickness and point size
        set1.setLineWidth(1f);
        set1.setCircleRadius(2f);

        // draw points as solid circles
        set1.setDrawCircleHole(false);

        // customize legend entry
        set1.setFormLineWidth(1f);
        //set1.setFormLineDashEffect(new DashPathEffect(new float[]{10f, 5f}, 0f));
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
        set1.setHighLightColor(Color.RED);
        set1.setFillColor(Color.parseColor("#00000000"));
        ArrayList<ILineDataSet> dataSets = new ArrayList<>();
        dataSets.add(set1);
        LineData data = new LineData(dataSets);
        line_chart_view.setData(data);
        // }
    }
}

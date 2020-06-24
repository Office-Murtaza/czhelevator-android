package com.kingyon.elevator.uis.activities.cooperation;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.highlight.Highlight;
import com.kingyon.elevator.R;
import com.kingyon.elevator.customview.MyMarkView;
import com.kingyon.elevator.date.DateUtils;
import com.kingyon.elevator.utils.DialogUtils;
import com.leo.afbaselibrary.uis.activities.BaseActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * @Created By Admin  on 2020/6/24
 * @Email : 163235610@qq.com
 * @Author:Mrczh
 * @Instructions:
 */
public class EarningsRecordActivity extends BaseActivity {
    @BindView(R.id.img_top_back)
    ImageView imgTopBack;
    @BindView(R.id.tv_top_title)
    TextView tvTopTitle;
    @BindView(R.id.tv_right)
    TextView tvRight;
    @BindView(R.id.tv_select_time)
    TextView tvSelectTime;
    @BindView(R.id.tv_user_all_income)
    TextView tvUserAllIncome;
    @BindView(R.id.tv_sr)
    TextView tvSr;
    @BindView(R.id.ll_sr)
    LinearLayout llSr;
    @BindView(R.id.user_pay_money)
    TextView userPayMoney;
    @BindView(R.id.ll_zc)
    LinearLayout llZc;
    @BindView(R.id.tv_current_tips)
    TextView tvCurrentTips;
    @BindView(R.id.line_chart_view)
    LineChart lineChartView;
    @BindView(R.id.tv_no_chart_data)
    TextView tvNoChartData;
    @BindView(R.id.chart_container)
    RelativeLayout chartContainer;
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
    public int getContentViewId() {
        return R.layout.activity_earnings_record;
    }

    @Override
    public void init(Bundle savedInstanceState) {
        currentSelectYear = DateUtils.getCurrentYear();
        currentSelectMonth = DateUtils.getCurrentMonth();
        currentSelectDay = DateUtils.getCurrentDay();
        tvSelectTime.setText(currentSelectYear + "年" + currentSelectMonth + "月");
        selectCatType = 1;
        selectIncomeOrPay = 0;
//        creatButton();
//        initChartView();

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }

    @OnClick({R.id.img_top_back, R.id.tv_select_time, R.id.tv_sr, R.id.ll_sr, R.id.ll_zc})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.img_top_back:
                finish();
                break;
            case R.id.tv_select_time:
                DialogUtils.getInstance().showSelectDateDialog(this, (type, year, month) -> {
                    currentSelectYear = year;
                    currentSelectMonth = month;
                    selectCatType = type;
                    if (selectCatType == 0) {
                        //按年查看数据
                        tvSelectTime.setText(year + "年");
                    } else {
                        tvSelectTime.setText(currentSelectYear + "年" + currentSelectMonth + "月");
                    }
                    myMarkView.setShowType(selectIncomeOrPay, selectCatType, currentSelectYear, currentSelectMonth);

//                    presenter.filterIncomeAndPayData(selectCatType, currentSelectYear, currentSelectMonth);
//                    presenter.getIncomePayDataPerDay(selectIncomeOrPay, selectCatType, currentSelectYear, currentSelectMonth);
//                    if (selectIncomeOrPay == 0) {
//                        setIncomeDesc();
//                    } else {
//                        setPayDesc();
//                    }
                });

                break;
            case R.id.tv_sr:
                break;
            case R.id.ll_sr:
                break;
            case R.id.ll_zc:
                break;
        }
    }
}

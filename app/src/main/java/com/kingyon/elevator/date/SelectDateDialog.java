package com.kingyon.elevator.date;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.kingyon.elevator.R;
import com.kingyon.elevator.utils.DialogUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created By SongPeng  on 2019/11/26
 * Email : 1531603384@qq.com
 * 选择日期对话框
 */
public class SelectDateDialog extends Dialog {

    @BindView(R.id.year_picker_view)
    DateWheelPicker year_picker_view;
    @BindView(R.id.month_select_container)
    LinearLayout month_select_container;
    @BindView(R.id.year_month_picker_view)
    DateWheelPicker year_month_picker_view;
    @BindView(R.id.month_picker_view)
    DateWheelPicker month_picker_view;
    @BindView(R.id.tv_annian)
    TextView tv_annian;
    @BindView(R.id.tv_anyue)
    TextView tv_anyue;
    DatePickerListener datePickerListener;
    private List<String> yearData;
    private List<String> monthData;
    private int yearSelectIndex = 2;
    private int monthSelectIndex = 0;
    private int selectType = 0;//默认是按年选择  1表示按月
    private int selectYear,selectMonth=0;


    public SelectDateDialog(@NonNull Context context, DatePickerListener datePickerListener) {
        super(context, R.style.MyDialog);
        this.datePickerListener = datePickerListener;
        yearData = new ArrayList<>();
        monthData = new ArrayList<>();
        for (int i = 2017; i <= 2030; i++) {
            if (DateUtils.getCurrentYear() == i) {
                yearSelectIndex = i - 2017;
                selectYear = i;
            }
            yearData.add(i+"年");
        }
        for (int i = 1; i <= 12; i++) {
            if (DateUtils.getCurrentMonth() == i) {
                monthSelectIndex = i - 1;
                selectMonth = i;
            }
            monthData.add(i+"月");
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.select_date_dialog_layout);
        ButterKnife.bind(this);
        year_picker_view.setDataList(yearData);
        year_picker_view.setCurrentPosition(yearSelectIndex);
        year_month_picker_view.setDataList(yearData);
        month_picker_view.setDataList(monthData);
        year_month_picker_view.setCurrentPosition(yearSelectIndex);
        month_picker_view.setCurrentPosition(monthSelectIndex);
        month_picker_view.setOnWheelChangeListener((item, position) -> {
            selectMonth = Integer.parseInt(item.replaceAll("月", ""));
        });
        year_month_picker_view.setOnWheelChangeListener((item, position) -> {
            selectYear = Integer.parseInt(item.replaceAll("年", ""));
        });
        year_picker_view.setOnWheelChangeListener((item, position) -> {
            selectYear = Integer.parseInt(item.replaceAll("年", ""));
        });
    }


    @OnClick({R.id.tv_cancel, R.id.tv_confirm, R.id.tv_annian, R.id.tv_anyue})
    public void OnClick(View view) {
        switch (view.getId()) {
            case R.id.tv_cancel:
                DialogUtils.getInstance().hideSelectDateDialog();
                break;
            case R.id.tv_confirm:
                DialogUtils.getInstance().hideSelectDateDialog();
                datePickerListener.selectDate(selectType, selectYear, selectMonth);
                break;
            case R.id.tv_annian:
                selectType = 0;
                tv_annian.setTextColor(Color.parseColor("#ffffff"));
                tv_annian.setBackgroundResource(R.drawable.shape_bg_date_dialog_tab_select);
                tv_anyue.setTextColor(Color.parseColor("#FF2659"));
                tv_anyue.setBackgroundResource(R.drawable.shape_bg_date_dialog_tab_unselect);
                year_picker_view.setVisibility(View.VISIBLE);
                month_select_container.setVisibility(View.INVISIBLE);
                break;
            case R.id.tv_anyue:
                selectType = 1;
                tv_annian.setTextColor(Color.parseColor("#FF2659"));
                tv_anyue.setTextColor(Color.parseColor("#ffffff"));
                tv_annian.setBackgroundResource(R.drawable.shape_bg_date_dialog_tab_unselect1);
                tv_anyue.setBackgroundResource(R.drawable.shape_bg_date_dialog_tab_select1);
                year_picker_view.setVisibility(View.INVISIBLE);
                month_select_container.setVisibility(View.VISIBLE);
                break;
        }
    }

    @Override
    public void show() {
        super.show();
        WindowManager.LayoutParams params = getWindow().getAttributes();
        //设置窗口宽度为充满全屏
        params.width = WindowManager.LayoutParams.MATCH_PARENT;
        //设置窗口高度为包裹内容
        params.height = WindowManager.LayoutParams.WRAP_CONTENT;
        params.gravity = Gravity.BOTTOM;
        getWindow().setAttributes(params);
    }
}

package com.kingyon.elevator.utils;

import android.content.Context;
import android.view.ViewGroup;

import com.bigkoo.pickerview.TimePickerView;
import com.bigkoo.pickerview.listener.CustomListener;
import com.kingyon.elevator.R;

import java.util.Calendar;

/**
 * Created by gongli on 2017/6/1 17:03
 * email: lc824767150@163.com
 */

public class TimePickerUtils {

    public static TimePickerView getNormalTimePickerView(Context context, TimePickerView.OnTimeSelectListener onTimeSelectListener) {
        long startTime = System.currentTimeMillis();
        Calendar selectedDate = Calendar.getInstance();
        selectedDate.setTimeInMillis(startTime);
        return new TimePickerView.Builder(context, onTimeSelectListener)
                .setType(new boolean[]{true, true, true, false, false, false})
                .isCyclic(false)
                .setDate(selectedDate)
                .build();
    }

    public static TimePickerView getBeforeTimePickerView(long time, Context context, String title, TimePickerView.OnTimeSelectListener onTimeSelectListener) {
        Calendar selectedDate = Calendar.getInstance();
        selectedDate.setTimeInMillis(time);
        selectedDate.add(Calendar.DATE, -1);
        return new TimePickerView.Builder(context, onTimeSelectListener)
                .setType(new boolean[]{true, true, true, false, false, false})
                .isCyclic(false)
                .setDate(selectedDate)
                .setRangDate(null, selectedDate)
                .setTitleText(title)
                .build();
    }

    public static TimePickerView getBeforeTimePickerView(Context context, String title, TimePickerView.OnTimeSelectListener onTimeSelectListener) {
        long startTime = System.currentTimeMillis();
        Calendar selectedDate = Calendar.getInstance();
        selectedDate.setTimeInMillis(startTime);
        selectedDate.add(Calendar.DATE, -1);
        return new TimePickerView.Builder(context, onTimeSelectListener)
                .setType(new boolean[]{true, true, true, false, false, false})
                .isCyclic(false)
                .setDate(selectedDate)
                .setRangDate(null, selectedDate)
                .setTitleText(title)
                .build();
    }

    public static TimePickerView getPlanTimePickerView(Context context, String title, TimePickerView.OnTimeSelectListener onTimeSelectListener, ViewGroup viewGroup) {
        long startTime = System.currentTimeMillis();
        Calendar selectedDate = Calendar.getInstance();
        selectedDate.setTimeInMillis(startTime);
//            selectedDate.add(Calendar.DATE, 1);
        return new TimePickerView.Builder(context, onTimeSelectListener)
                .setType(new boolean[]{true, true, true, false, false, false})
                .isCyclic(false)
                .setDate(selectedDate)
                .setRangDate(selectedDate, null)
                .setTitleText(title)
                .setDecorView(viewGroup)
                .build();
    }

    public static TimePickerView getPlanTimePickerView(Context context, String title, TimePickerView.OnTimeSelectListener onTimeSelectListener) {
        long startTime = System.currentTimeMillis();
        Calendar selectedDate = Calendar.getInstance();
        selectedDate.setTimeInMillis(startTime);
//            selectedDate.add(Calendar.DATE, 1);
        return new TimePickerView.Builder(context, onTimeSelectListener)
                .setType(new boolean[]{true, true, true, false, false, false})
                .isCyclic(false)
                .setDate(selectedDate)
                .setRangDate(selectedDate, null)
                .setTitleText(title)
                .build();
    }

    public static TimePickerView getDayPicker(Context context, TimePickerView.OnTimeSelectListener onTimeSelectListener) {
        long startTime = System.currentTimeMillis();
        Calendar selectedDate = Calendar.getInstance();
        selectedDate.setTimeInMillis(startTime);
        return new TimePickerView.Builder(context, onTimeSelectListener)
                .setType(new boolean[]{true, true, true, false, false, false})
                .isCyclic(false)
                .setDate(selectedDate)
                .build();
    }

    public static TimePickerView getMonthPicker(Context context, TimePickerView.OnTimeSelectListener onTimeSelectListener, CustomListener customListener) {
        long startTime = System.currentTimeMillis();
        Calendar selectedDate = Calendar.getInstance();
        selectedDate.setTimeInMillis(startTime);
        return new TimePickerView.Builder(context, onTimeSelectListener)
                .setType(new boolean[]{true, true, false, false, false, false})
                .isCyclic(false)
                .setLayoutRes(R.layout.layout_pick_time_month, customListener)
                .setDate(selectedDate)
                .build();
    }

    public static TimePickerView getYearPicker(Context context, TimePickerView.OnTimeSelectListener onTimeSelectListener, CustomListener customListener) {
        long startTime = System.currentTimeMillis();
        Calendar selectedDate = Calendar.getInstance();
        selectedDate.setTimeInMillis(startTime);
        return new TimePickerView.Builder(context, onTimeSelectListener)
                .setType(new boolean[]{true, false, false, false, false, false})
                .isCyclic(false)
                .setLayoutRes(R.layout.layout_pick_time_year, customListener)
                .setDate(selectedDate)
                .build();
    }

    public static TimePickerView getAfterYearPicker(Context context, TimePickerView.OnTimeSelectListener onTimeSelectListener, CustomListener customListener) {
        long startTime = System.currentTimeMillis();
        Calendar selectedDate = Calendar.getInstance();
        selectedDate.setTimeInMillis(startTime);
        return new TimePickerView.Builder(context, onTimeSelectListener)
                .setType(new boolean[]{true, false, false, false, false, false})
                .isCyclic(false)
                .setLayoutRes(R.layout.layout_pick_time_year, customListener)
                .setDate(selectedDate)
                .setRangDate(selectedDate, null)
                .build();
    }
}

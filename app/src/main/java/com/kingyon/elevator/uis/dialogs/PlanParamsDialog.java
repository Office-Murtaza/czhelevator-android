package com.kingyon.elevator.uis.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.kingyon.elevator.R;
import com.kingyon.elevator.constants.Constants;
import com.kingyon.elevator.entities.NormalParamEntity;
import com.kingyon.elevator.uis.adapters.PlanTypeAdapter;
import com.kingyon.elevator.uis.pops.PlanParamsWindow;
import com.kingyon.elevator.utils.GridSpacingItemDecoration;
import com.leo.afbaselibrary.uis.activities.BaseActivity;
import com.leo.afbaselibrary.utils.ScreenUtil;
import com.leo.afbaselibrary.utils.TimeUtil;
import com.leo.afbaselibrary.utils.ToastUtils;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by GongLi on 2019/1/31.
 * Email：lc824767150@163.com
 */

public class PlanParamsDialog extends Dialog {
    @BindView(R.id.rv_cell_types)
    RecyclerView rvCellTypes;
    @BindView(R.id.tv_start_time)
    TextView tvStartTime;
    @BindView(R.id.tv_end_time)
    TextView tvEndTime;
    @BindView(R.id.fl_root)
    FrameLayout flRoot;

    private Context mContext;
    private BaseActivity mActivity;
    private PlanTypeAdapter planTypeAdapter;

    private PlanParamsWindow.OnResultListener onResultListener;
    private DatePickerDialog startDialog;
    private DatePickerDialog endDialog;

    public PlanParamsDialog(@NonNull BaseActivity baseActivity, @NonNull Context context, PlanParamsWindow.OnResultListener onResultListener) {
        super(context, R.style.normal_dialog_white);
        mActivity = baseActivity;
        mContext = context;
        this.onResultListener = onResultListener;
        setContentView(R.layout.window_plan_params);
//        DisplayMetrics metrics = getContext().getResources().getDisplayMetrics();
//        int screenWidth = (int) (metrics.widthPixels * 0.92);
        Window window = getWindow();
        if (window != null) {
            window.setWindowAnimations(R.style.dialog_show_anim);
            window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            window.setGravity(Gravity.BOTTOM);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
        rvCellTypes.setLayoutManager(new GridLayoutManager(mContext, 3));
        rvCellTypes.addItemDecoration(new GridSpacingItemDecoration(3, ScreenUtil.dp2px(20), false));
        planTypeAdapter = new PlanTypeAdapter(mContext);
        rvCellTypes.setAdapter(planTypeAdapter);
        List<NormalParamEntity> entities = new ArrayList<>();
        entities.add(new NormalParamEntity(Constants.PLAN_TYPE.BUSINESS, "商业"));
        entities.add(new NormalParamEntity(Constants.PLAN_TYPE.DIY, "DIY"));
        entities.add(new NormalParamEntity(Constants.PLAN_TYPE.INFORMATION, "便民服务"));
        planTypeAdapter.refreshDatas(entities);
    }

    @OnClick({R.id.tv_start_time, R.id.tv_end_time, R.id.tv_reset, R.id.tv_ensure})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_start_time:
                showStartPicker();
                break;
            case R.id.tv_end_time:
                showEndPicker();
                break;
            case R.id.tv_reset:
                planTypeAdapter.setChoosedType("");
                tvEndTime.setTag(null);
                tvEndTime.setText("");
                tvStartTime.setTag(null);
                tvStartTime.setText("");
//                if (onResultListener != null) {
//                    onResultListener.onResultReturn(null, null, null);
//                }
//                dismiss();
                break;
            case R.id.tv_ensure:
                NormalParamEntity choosedEntity = planTypeAdapter.getChoosedEntity();
                Long startTime = (Long) tvStartTime.getTag();
                Long endTime = (Long) tvEndTime.getTag();
//                if (choosedEntity == null) {
//                    ToastUtils.toast(mContext, "请选择类型");
//                    return;
//                }
//                if (startTime == null) {
//                    ToastUtils.toast(mContext, "请选择开始时间");
//                    return;
//                }
//                if (endTime == null) {
//                    ToastUtils.toast(mContext, "请选择结束时间");
//                    return;
//                }
                if (startTime == null && endTime != null) {
                    ToastUtils.toast(mContext, "请选择开始时间");
                    return;
                }
                if (startTime != null && endTime == null) {
                    ToastUtils.toast(mContext, "请选择结束时间");
                    return;
                }
                if (onResultListener != null) {
                    onResultListener.onResultReturn(choosedEntity, startTime, endTime);
                }
                dismiss();
                break;
        }
    }

    private void showEndPicker() {
        Long startTime = (Long) tvStartTime.getTag();
        if (startTime == null) {
            ToastUtils.toast(mContext, "请先选择开始时间");
        } else {
//            if (endPicker == null) {
//                endPicker = TimePickerUtils.getPlanTimePickerView(mContext, "请选择结束时间", new TimePickerView.OnTimeSelectListener() {
//                    @Override
//                    public void onTimeSelect(Date date, View v) {
//                        if (date != null) {
//                            long endTime = TimeUtil.getDayEndTimeMilliseconds(date.getTime());
//                            Long startTime = (Long) tvStartTime.getTag();
//                            if (startTime == null) {
//                                ToastUtils.toast(mContext, "请先选择开始时间");
//                            } else if (startTime > endTime) {
//                                ToastUtils.toast(mContext, "结束时间必须大于开始时间");
//                            } else {
//                                tvEndTime.setTag(endTime);
//                                tvEndTime.setText(TimeUtil.getYMdTime(endTime));
//                            }
//                        }
//                        if (endPicker != null && endPicker.isShowing()) {
//                            endPicker.dismiss();
//                        }
//                    }
//                }, flRoot);
//            }
//            endPicker.show();

            if (endDialog == null) {
                Calendar now = Calendar.getInstance();
                endDialog = DatePickerDialog.newInstance(
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
                                long endTime = TimeUtil.getDayEndTimeMilliseconds(com.kingyon.elevator.utils.TimeUtil.getLongTime(year + "-" + (monthOfYear + 1) + "-" + dayOfMonth));
                                Long startTime = (Long) tvStartTime.getTag();
                                if (startTime == null) {
                                    ToastUtils.toast(mContext, "请先选择开始时间");
                                } else if (startTime > endTime) {
                                    ToastUtils.toast(mContext, "结束时间必须大于开始时间");
                                } else {
                                    tvEndTime.setTag(endTime);
                                    tvEndTime.setText(TimeUtil.getYMdTime(endTime));
                                }
                            }
                        },
                        now.get(Calendar.YEAR),
                        now.get(Calendar.MONTH),
                        now.get(Calendar.DAY_OF_MONTH)
                );
                endDialog.setMinDate(now);
            }
            endDialog.show(mActivity.getFragmentManager(), "Datepickerdialog_end");
        }
    }

    private void showStartPicker() {
//        if (startPicker == null) {
//            startPicker = TimePickerUtils.getPlanTimePickerView(mContext, "请选择开始时间", new TimePickerView.OnTimeSelectListener() {
//                @Override
//                public void onTimeSelect(Date date, View v) {
//                    if (date != null) {
//                        long startTime = TimeUtil.getDayStartTimeMilliseconds(date.getTime());
//                        tvStartTime.setTag(startTime);
//                        tvStartTime.setText(TimeUtil.getYMdTime(startTime));
//                        Long endTime = (Long) tvEndTime.getTag();
//                        if (endTime != null && endTime < startTime) {
//                            tvEndTime.setTag(null);
//                            tvEndTime.setText("");
//                        }
//                    }
//                    if (startPicker != null && startPicker.isShowing()) {
//                        startPicker.dismiss();
//                    }
//                }
//            }, flRoot);
//        }
//        startPicker.show();

        if (startDialog == null) {
            Calendar now = Calendar.getInstance();
            startDialog = DatePickerDialog.newInstance(
                    new DatePickerDialog.OnDateSetListener() {
                        @Override
                        public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
                            long startTime = TimeUtil.getDayStartTimeMilliseconds(com.kingyon.elevator.utils.TimeUtil.getLongTime(year + "-" + (monthOfYear + 1) + "-" + dayOfMonth));
                            tvStartTime.setTag(startTime);
                            tvStartTime.setText(TimeUtil.getYMdTime(startTime));
                            Long endTime = (Long) tvEndTime.getTag();
                            if (endTime != null && endTime < startTime) {
                                tvEndTime.setTag(null);
                                tvEndTime.setText("");
                            }
                        }
                    },
                    now.get(Calendar.YEAR),
                    now.get(Calendar.MONTH),
                    now.get(Calendar.DAY_OF_MONTH)
            );
            startDialog.setMinDate(now);
        }
        startDialog.show(mActivity.getFragmentManager(), "Datepickerdialog_start");
    }

    public void show(String type, Long startTime, Long endTime) {
        show();
        planTypeAdapter.setChoosedType(type);
        tvStartTime.setTag(startTime);
        tvStartTime.setText(startTime != null ? TimeUtil.getYMdTime(startTime) : "");
        tvEndTime.setTag(endTime);
        tvEndTime.setText(endTime != null ? TimeUtil.getYMdTime(endTime) : "");
    }
}

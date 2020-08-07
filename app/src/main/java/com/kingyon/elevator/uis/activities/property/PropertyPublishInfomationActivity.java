package com.kingyon.elevator.uis.activities.property;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.kingyon.elevator.R;
import com.kingyon.elevator.entities.PointItemEntity;
import com.kingyon.elevator.nets.CustomApiCallback;
import com.kingyon.elevator.nets.NetService;
import com.kingyon.elevator.utils.CommonUtil;
import com.kingyon.elevator.utils.KeyBoardUtils;
import com.leo.afbaselibrary.nets.exceptions.ApiException;
import com.leo.afbaselibrary.uis.activities.BaseSwipeBackActivity;
import com.leo.afbaselibrary.utils.TimeUtil;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import java.util.ArrayList;
import java.util.Calendar;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by GongLi on 2019/1/15.
 * Email：lc824767150@163.com
 */

public class PropertyPublishInfomationActivity extends BaseSwipeBackActivity {
    @BindView(R.id.tv_devices)
    TextView tvDevices;
    @BindView(R.id.tv_start_time)
    TextView tvStartTime;
    @BindView(R.id.tv_end_time)
    TextView tvEndTime;
    @BindView(R.id.et_content)
    EditText etContent;
    @BindView(R.id.tv_length)
    TextView tvLength;
    @BindView(R.id.tv_create)
    TextView tvCreate;

    //    private TimePickerView startPicker;
    //    private TimePickerView endPicker;
    private DatePickerDialog startDialog;
    private DatePickerDialog endDialog;

    @Override
    protected String getTitleText() {
        return "新增便民信息";
    }

    @Override
    public int getContentViewId() {
        return R.layout.activity_property_publish_infomation;
    }

    @Override
    protected void initViews(Bundle savedInstanceState) {
        etContent.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                tvLength.setText(String.format("%s/100", s.length()));
            }
        });
    }

    @OnClick({R.id.ll_devices, R.id.ll_start_time, R.id.ll_end_time, R.id.tv_create})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ll_devices:
                Bundle bundle = new Bundle();
                ArrayList<PointItemEntity> datas = (ArrayList<PointItemEntity>) tvDevices.getTag();
                if (datas != null) {
                    bundle.putParcelableArrayList(CommonUtil.KEY_VALUE_1, datas);
                }
                startActivityForResult(PropertyDeviceChooseActivity.class, CommonUtil.REQ_CODE_1, bundle);
                break;
            case R.id.ll_start_time:
                showStartPicker();
                break;
            case R.id.ll_end_time:
                showEndPicker();
                break;
            case R.id.tv_create:
                onCreateClick();
                break;
        }
    }

    private void onCreateClick() {
        String devicesParams = getDevicesParams();
        if (TextUtils.isEmpty(devicesParams)) {
            showToast("请选择发布位置");
            return;
        }
        Long startTime = (Long) tvStartTime.getTag();
        if (startTime == null) {
            showToast("请选择开始时间");
            return;
        }
        Long endTime = (Long) tvEndTime.getTag();
        if (endTime == null) {
            showToast("请选择结束时间");
            return;
        }
        if (TextUtils.isEmpty(CommonUtil.getEditText(etContent))) {
            showToast("请输入发布内容");
            return;
        }
        tvCreate.setEnabled(false);
        showProgressDialog(getString(R.string.wait),true);
        NetService.getInstance().createPropertyInfomation(etContent.getText().toString(), devicesParams, startTime, endTime)
                .compose(this.<String>bindLifeCycle())
                .subscribe(new CustomApiCallback<String>() {
                    @Override
                    protected void onResultError(ApiException ex) {
                        showToast(ex.getDisplayMessage());
                        hideProgress();
                        tvCreate.setEnabled(true);
                    }

                    @Override
                    public void onNext(String s) {
                        tvCreate.setEnabled(true);
                        showToast("发布成功");
                        hideProgress();
                        setResult(RESULT_OK);
                        finish();
                    }
                });
    }

    private String getDevicesParams() {
        ArrayList<PointItemEntity> devices = (ArrayList<PointItemEntity>) tvDevices.getTag();
        StringBuilder stringBuilder = new StringBuilder();
        if (devices != null) {
            for (PointItemEntity item : devices) {
                stringBuilder.append(item.getObjectId()).append(",");
            }
        }
        return stringBuilder.length() > 1 ? stringBuilder.substring(0, stringBuilder.length() - 1) : "";
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (RESULT_OK == resultCode && CommonUtil.REQ_CODE_1 == requestCode && data != null) {
            ArrayList<PointItemEntity> devices = data.getParcelableArrayListExtra(CommonUtil.KEY_VALUE_1);
            if (devices == null) {
                devices = new ArrayList<>();
            }
            tvDevices.setTag(devices);
            tvDevices.setText(devices.size() > 0 ? String.format("%s个设备", devices.size()) : "");
        }
    }

    private void showEndPicker() {
        Long startTime = (Long) tvStartTime.getTag();
        if (startTime == null) {
            showToast("请先选择开始时间");
        } else {
//            if (endPicker == null) {
//                endPicker = TimePickerUtils.getPlanTimePickerView(this, "请选择结束时间", new TimePickerView.OnTimeSelectListener() {
//                    @Override
//                    public void onTimeSelect(Date date, View v) {
//                        if (date != null) {
//                            long endTime = TimeUtil.getDayEndTimeMilliseconds(date.getTime());
//                            Long startTime = (Long) tvStartTime.getTag();
//                            if (startTime == null) {
//                                showToast("请先选择开始时间");
//                            } else if (startTime > endTime) {
//                                showToast("结束时间必须大于开始时间");
//                            } else {
//                                tvEndTime.setTag(endTime);
//                                tvEndTime.setText(TimeUtil.getYMdTime(endTime));
//                            }
//                        }
//                        if (endPicker != null && endPicker.isShowing()) {
//                            endPicker.dismiss();
//                        }
//                    }
//                });
//            }
//            KeyBoardUtils.closeKeybord(this);
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
                                    showToast("请先选择开始时间");
                                } else if (startTime > endTime) {
                                    showToast("结束时间必须大于开始时间");
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
            KeyBoardUtils.closeKeybord(this);
            endDialog.show(getFragmentManager(), "Datepickerdialog_end");
        }
    }

    private void showStartPicker() {
//        if (startPicker == null) {
//            startPicker = TimePickerUtils.getPlanTimePickerView(this, "请选择开始时间", new TimePickerView.OnTimeSelectListener() {
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
//            });
//        }
//        KeyBoardUtils.closeKeybord(this);
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
        KeyBoardUtils.closeKeybord(this);
        startDialog.show(getFragmentManager(), "Datepickerdialog_start");
    }
}

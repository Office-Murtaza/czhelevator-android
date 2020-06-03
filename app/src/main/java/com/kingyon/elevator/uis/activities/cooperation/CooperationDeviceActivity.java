package com.kingyon.elevator.uis.activities.cooperation;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;

import com.acker.simplezxing.activity.CaptureActivity;
import com.kingyon.elevator.R;
import com.kingyon.elevator.entities.CellDeviceNumberEntity;
import com.kingyon.elevator.entities.DeviceNumberEntity;
import com.kingyon.elevator.entities.DeviceNumberInfo;
import com.kingyon.elevator.nets.CustomApiCallback;
import com.kingyon.elevator.nets.NetService;
import com.kingyon.elevator.uis.activities.devices.DeviceEditActivity;
import com.kingyon.elevator.uis.adapters.adapterone.CooperationDeviceNumberAdapter;
import com.kingyon.elevator.utils.CommonUtil;
import com.kingyon.elevator.utils.FormatUtils;
import com.leo.afbaselibrary.nets.exceptions.ApiException;
import com.leo.afbaselibrary.nets.exceptions.ResultException;
import com.leo.afbaselibrary.uis.activities.BaseActivity;
import com.leo.afbaselibrary.uis.activities.BaseStateRefreshingLoadingActivity;
import com.leo.afbaselibrary.uis.adapters.MultiItemTypeAdapter;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by GongLi on 2019/1/14.
 * Email：lc824767150@163.com
 */

public class CooperationDeviceActivity extends BaseStateRefreshingLoadingActivity<Object> {
    @BindView(R.id.pre_v_right)
    public ImageView preVRight;

    protected String role;

    @Override
    protected String getTitleText() {
        role = getIntent().getStringExtra(CommonUtil.KEY_VALUE_1);
        return "设备管理";
    }

    @Override
    public int getContentViewId() {
        return R.layout.activity_cooperation_device;
    }

    @Override
    protected void initViews(Bundle savedInstanceState) {
        super.initViews(savedInstanceState);
        preVRight.setImageResource(R.drawable.ic_capture_code);
    }

    @Override
    protected MultiItemTypeAdapter<Object> getAdapter() {
        return new CooperationDeviceNumberAdapter(this, mItems);
    }

    @Override
    public void onItemClick(View view, RecyclerView.ViewHolder holder, Object item, int position) {
        super.onItemClick(view, holder, item, position);
        if (item != null && item instanceof CellDeviceNumberEntity) {
            Bundle bundle = new Bundle();
            bundle.putLong(CommonUtil.KEY_VALUE_1, ((CellDeviceNumberEntity) item).getCellId());
            bundle.putString(CommonUtil.KEY_VALUE_2, role);
            startActivity(CooperationCellDevicesActivity.class, bundle);
        }
    }

    @Override
    protected void loadData(final int page) {
        NetService.getInstance().deviceNumber()
                .compose(this.<DeviceNumberInfo>bindLifeCycle())
                .subscribe(new CustomApiCallback<DeviceNumberInfo>() {
                    @Override
                    protected void onResultError(ApiException ex) {
                        showToast(ex.getDisplayMessage());
                        loadingComplete(false, 1);
                    }

                    @Override
                    public void onNext(DeviceNumberInfo deviceNumberInfo) {
                        DeviceNumberEntity deviceNumber = deviceNumberInfo.getDeviceNumber();
                        List<CellDeviceNumberEntity> cellDeviceNumbers = deviceNumberInfo.getCellDeviceNumbers();
                        if (deviceNumber == null || cellDeviceNumbers == null) {
                            throw new ResultException(9001, "返回参数异常");
                        }
                        if (FIRST_PAGE == page) {
                            mItems.clear();
                            mItems.add(deviceNumber);
                            if (cellDeviceNumbers.size() > 0) {
                                mItems.add("tip");
                            }
                        }
                        mItems.addAll(cellDeviceNumbers);
                        loadingComplete(true, 1);
                    }
                });
    }

    @Override
    protected boolean isShowDivider() {
        return false;
    }

    @OnClick(R.id.pre_v_right)
    public void onViewClicked() {
        String[] perms = {Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA};
        checkPermission(new BaseActivity.CheckPermListener() {
            @Override
            public void agreeAllPermission() {
                jumpToQrcode();
            }


        }, "扫描二维码需要以下权限", perms);

//        Bundle bundle = new Bundle();
//        bundle.putBoolean(CommonUtil.KEY_VALUE_4, true);
//        startActivityForResult(DeviceEditActivity.class, CommonUtil.REQ_CODE_2, bundle);
    }

    private void jumpToQrcode() {
        Bundle bundle = new Bundle();
        bundle.putBoolean(CaptureActivity.KEY_NEED_BEEP, CaptureActivity.VALUE_BEEP);
        bundle.putBoolean(CaptureActivity.KEY_NEED_VIBRATION, CaptureActivity.VALUE_VIBRATION);
        bundle.putBoolean(CaptureActivity.KEY_NEED_EXPOSURE, CaptureActivity.VALUE_NO_EXPOSURE);
        bundle.putByte(CaptureActivity.KEY_FLASHLIGHT_MODE, CaptureActivity.VALUE_FLASHLIGHT_AUTO);
        bundle.putByte(CaptureActivity.KEY_ORIENTATION_MODE, CaptureActivity.VALUE_ORIENTATION_AUTO);
        bundle.putBoolean(CaptureActivity.KEY_SCAN_AREA_FULL_SCREEN, CaptureActivity.VALUE_SCAN_AREA_FULL_SCREEN);
        bundle.putBoolean(CaptureActivity.KEY_NEED_SCAN_HINT_TEXT, CaptureActivity.VALUE_SCAN_HINT_TEXT);
        startActivityForResult(CaptureActivity.class, CommonUtil.REQ_CODE_1, bundle);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (RESULT_OK == resultCode) {
            switch (requestCode) {
                case CommonUtil.REQ_CODE_1:
                    if (data != null) {
                        String deviceStr = data.getStringExtra(CaptureActivity.EXTRA_SCAN_RESULT);
                        String deviceNo = FormatUtils.getInstance().getDeviceNo(deviceStr);
                        if (!TextUtils.isEmpty(deviceNo)) {
                            Bundle bundle = new Bundle();
                            bundle.putString(CommonUtil.KEY_VALUE_3, deviceNo);
                            bundle.putBoolean(CommonUtil.KEY_VALUE_4, true);
                            startActivityForResult(DeviceEditActivity.class, CommonUtil.REQ_CODE_2, bundle);
                        } else {
                            showToast("没有获取到设备特征码");
                        }
                    }
                    break;
                case CommonUtil.REQ_CODE_2:
                    autoRefresh();
                    break;
            }
        }
    }
}

package com.kingyon.elevator.uis.activities.devices;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.acker.simplezxing.activity.CaptureActivity;
import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationListener;
import com.bigkoo.pickerview.OptionsPickerView;
import com.kingyon.elevator.R;
import com.kingyon.elevator.constants.Constants;
import com.kingyon.elevator.entities.CameraBrandEntity;
import com.kingyon.elevator.entities.CameraEntity;
import com.kingyon.elevator.entities.CellItemEntity;
import com.kingyon.elevator.entities.LiftElemEntity;
import com.kingyon.elevator.entities.NormalElemEntity;
import com.kingyon.elevator.entities.NormalParamEntity;
import com.kingyon.elevator.entities.PointItemEntity;
import com.kingyon.elevator.entities.entities.ConentEntity;
import com.kingyon.elevator.nets.CustomApiCallback;
import com.kingyon.elevator.nets.NetService;
import com.kingyon.elevator.uis.dialogs.ScreenPositionDialog;
import com.kingyon.elevator.uis.dialogs.ScreenTypeDialog;
import com.kingyon.elevator.utils.CommonUtil;
import com.kingyon.elevator.utils.FormatUtils;
import com.kingyon.elevator.utils.LocationUtils;
import com.leo.afbaselibrary.nets.entities.PageListEntity;
import com.leo.afbaselibrary.nets.exceptions.ApiException;
import com.leo.afbaselibrary.uis.activities.BaseSwipeBackActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import rx.Observable;
import rx.functions.Func1;

/**
 * Created by GongLi on 2019/1/17.
 * Email：lc824767150@163.com
 * 2。0安装管理-添加设备
 */

public class DeviceEditActivity extends BaseSwipeBackActivity implements AMapLocationListener {

    @BindView(R.id.tv_location)
    TextView tvLocation;
    @BindView(R.id.fl_location)
    LinearLayout flLocation;
    @BindView(R.id.tv_device_no)
    TextView tvDeviceNo;
    @BindView(R.id.tv_cell)
    TextView tvCell;
    @BindView(R.id.tv_build)
    TextView tvBuild;
    @BindView(R.id.tv_unit)
    TextView tvUnit;
    @BindView(R.id.tv_lift)
    TextView tvLift;
    //    @BindView(R.id.img_oritation)
//    ImageView imgOritation;
    @BindView(R.id.tv_camera_info)
    TextView tvCameraInfo;
    @BindView(R.id.ll_camera_info)
    LinearLayout llCameraInfo;
    @BindView(R.id.et_camera_ip)
    EditText etCameraIp;
    @BindView(R.id.ll_camera)
    LinearLayout llCamera;
    @BindView(R.id.tv_oritation)
    TextView tvOritation;
    @BindView(R.id.tv_type)
    TextView tvType;
    @BindView(R.id.tv_submit)
    TextView tvSubmit;

    private boolean edit;
    private PointItemEntity device;
    private boolean fromCooperation;

    private Double longitude;
    private Double latitude;

    private OptionsPickerView oritationPicker;
    private List<NormalParamEntity> oritationOptions;
    private OptionsPickerView typePicker;
    private List<NormalParamEntity> typeOptions;
    private String regionName;

    @Override
    protected String getTitleText() {
        edit = getIntent().getBooleanExtra(CommonUtil.KEY_VALUE_1, false);
        device = getIntent().getParcelableExtra(CommonUtil.KEY_VALUE_2);
        fromCooperation = getIntent().getBooleanExtra(CommonUtil.KEY_VALUE_4, false);

        return edit ? "编辑设备" : "添加设备";
    }

    @Override
    public int getContentViewId() {
        return R.layout.activity_device_edit;
    }

    @Override
    protected void initViews(Bundle savedInstanceState) {
        checkLocation();
        if (edit) {
            if (device != null) {
                tvDeviceNo.setText(device.getDeviceNo());
                tvCell.setTag(device.getCellId());
                tvCell.setText(device.getCellName());
                tvBuild.setTag(device.getBuildId());
                tvBuild.setText(device.getBuild());
//                tvUnit.setTag(device.getUnitId());
                tvUnit.setText(device.getUnit());
                tvLift.setTag(device.getLiftId());
                tvLift.setText(device.getLift());
                switch (device.getDevice()) {
                    case Constants.DEVICE_PLACE.LEFT:
                        tvOritation.setTag(Constants.DEVICE_PLACE.LEFT);
                        tvOritation.setText("左屏");
                        tvOritation.setCompoundDrawablesWithIntrinsicBounds(R.mipmap.ic_screen_left, 0, R.drawable.ic_user_right, 0);
//                        imgOritation.setImageResource(R.drawable.ic_screen_left);
                        break;
                    case Constants.DEVICE_PLACE.CENTER:
                        tvOritation.setTag(Constants.DEVICE_PLACE.CENTER);
                        tvOritation.setText("中屏");
                        tvOritation.setCompoundDrawablesWithIntrinsicBounds(R.mipmap.ic_screen_middle, 0, R.drawable.ic_user_right, 0);
//                        imgOritation.setImageResource(R.drawable.ic_screen_center);
                        break;
                    case Constants.DEVICE_PLACE.RIGHT:
                        tvOritation.setTag(Constants.DEVICE_PLACE.RIGHT);
                        tvOritation.setText("右屏");
                        tvOritation.setCompoundDrawablesWithIntrinsicBounds(R.mipmap.ic_screen_right, 0, R.drawable.ic_user_right, 0);
//                        imgOritation.setImageResource(R.drawable.ic_screen_right);
                        break;
                    default:
                        tvOritation.setTag(null);
                        tvOritation.setText("");
                        tvOritation.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_user_right, 0);
//                        imgOritation.setImageDrawable(null);
                        break;
                }
            }
        } else {
            String deviceNo = getIntent().getStringExtra(CommonUtil.KEY_VALUE_3);
            tvDeviceNo.setText(deviceNo != null ? deviceNo : "");
        }
    }

    @OnClick({R.id.fl_location, R.id.ll_device_no, R.id.ll_cell, R.id.ll_build, R.id.ll_unit, R.id.ll_lift, R.id.ll_camera_info, R.id.ll_oritation, R.id.ll_type, R.id.tv_submit})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.fl_location:
                if (tvLocation.getTag() != null) {
                    CellItemEntity entity = (CellItemEntity) tvLocation.getTag();
                    if (tvCell.getTag() == null || ((long) tvCell.getTag()) != entity.getObjctId()) {
                        tvCell.setTag(entity.getObjctId());
                        tvCell.setText(entity.getCellName());
                        clearBelowCell();
                    }
                }
                break;
            case R.id.ll_device_no:
                Intent intent = new Intent(this, CaptureActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivityForResult(intent, CommonUtil.REQ_CODE_1);
                break;
            case R.id.ll_cell:
                Bundle bundle1 = new Bundle();
                if (longitude != null && latitude != null) {
                    bundle1.putDouble(CommonUtil.KEY_VALUE_1, longitude);
                    bundle1.putDouble(CommonUtil.KEY_VALUE_2, latitude);
                }
                startActivityForResult(CellChooseActivity.class, CommonUtil.REQ_CODE_2, bundle1);
                break;
            case R.id.ll_build:
                Long cellId = (Long) tvCell.getTag();
                if (cellId != null) {
                    Bundle bundle = new Bundle();
                    bundle.putLong(CommonUtil.KEY_VALUE_1, cellId);
                    bundle.putString(CommonUtil.KEY_VALUE_2, tvCell.getText().toString());
                    bundle.putString(CommonUtil.KEY_VALUE_3,regionName);
                    startActivityForResult(BuildChooseActivity.class, CommonUtil.REQ_CODE_3, bundle);
                } else {
                    showToast("请先选择小区");
                }
                break;
            case R.id.ll_unit:
                Long buildId = (Long) tvBuild.getTag();
                if (buildId != null) {
                    Bundle bundle = new Bundle();
                    bundle.putLong(CommonUtil.KEY_VALUE_1, buildId);
                    bundle.putString(CommonUtil.KEY_VALUE_2, String.format("%s-%s", tvCell.getText().toString(), tvBuild.getText().toString()));
                    startActivityForResult(UnitChooseActivity.class, CommonUtil.REQ_CODE_4, bundle);
                } else {
                    showToast("请先选择楼栋");
                }
                break;
            case R.id.ll_lift:
                Long unitId = (Long) tvUnit.getTag();
                if (unitId != null) {
                    Bundle bundle = new Bundle();
                    bundle.putLong(CommonUtil.KEY_VALUE_1, unitId);
                    bundle.putString(CommonUtil.KEY_VALUE_2, String.format("%s-%s-%s", tvCell.getText().toString(), tvBuild.getText().toString(), tvUnit.getText().toString()));
                    startActivityForResult(LiftChooseActivity.class, CommonUtil.REQ_CODE_5, bundle);
                } else {
                    showToast("请先选择单元");
                }
                break;
            case R.id.ll_camera_info:
                Long liftId = (Long) tvLift.getTag();
                if (liftId != null) {
                    startActivityForResult(CameraBrandActivity.class, CommonUtil.REQ_CODE_6);
                } else {
                    showToast("请先选择电梯");
                }
                break;
            case R.id.ll_oritation:
                showScreenLocationPicker();
                break;
            case R.id.ll_type:
                showScreenTypePicker();
                break;
            case R.id.tv_submit:
                onSubmitClick();
                break;
        }
    }

    private void onSubmitClick() {
        if (TextUtils.isEmpty(CommonUtil.getEditText(tvDeviceNo))) {
            showToast("请先扫描设备特征码");
            return;
        }
        String deviceNo = tvDeviceNo.getText().toString();
        Long cellId = (Long) tvCell.getTag();
        if (cellId == null) {
            showToast("请先选择小区");
            return;
        }
        Long buildId = (Long) tvBuild.getTag();
        if (buildId == null) {
            showToast("请先选择楼栋");
            return;
        }
        Long unitId = (Long) tvUnit.getTag();
        if (unitId == null) {
            showToast("请先选择单元");
            return;
        }
        Long liftId = (Long) tvLift.getTag();
        if (liftId == null) {
            showToast("请先选择电梯");
            return;
        }

        Long cameraBrand = null;
        String cameraIp = null;
        if (llCamera.getVisibility() == View.VISIBLE) {
            cameraBrand = (Long) tvCameraInfo.getTag();
            if (cameraBrand == null) {
                showToast("请选择摄像头品牌");
                return;
            }
            cameraIp = CommonUtil.getEditText(etCameraIp);
            if (TextUtils.isEmpty(cameraIp)) {
                showToast("请填写摄像头IP");
                return;
            }
        }
        String deviceLocation = (String) tvOritation.getTag();
        if (TextUtils.isEmpty(deviceLocation)) {
            showToast("请选择屏幕位置");
            return;
        }
        String deviceType = (String) tvType.getTag();
        if (TextUtils.isEmpty(deviceType)) {
            showToast("请选择屏幕类型");
            return;
        }
        tvSubmit.setEnabled(false);
        showProgressDialog(getString(R.string.wait));
        NetService.getInstance().addDevice(edit ? (device != null ? device.getObjectId() : 0) : null
                , deviceNo, deviceLocation, deviceType, cellId, buildId, unitId, liftId, cameraBrand, cameraIp)
                .compose(this.<String>bindLifeCycle())
                .subscribe(new CustomApiCallback<String>() {
                    @Override
                    protected void onResultError(ApiException ex) {
                        tvSubmit.setEnabled(true);
                        hideProgress();
                        showToast(ex.getDisplayMessage());
                    }

                    @Override
                    public void onNext(String s) {
                        showToast(edit ? "编辑成功" : "添加成功");
                        tvSubmit.setEnabled(true);
                        hideProgress();
                        setResult(RESULT_OK);
                        finish();
                    }
                });
    }

    private void showScreenTypePicker() {
        ScreenTypeDialog screenTypeDialog = new ScreenTypeDialog(this, new ScreenTypeDialog.OnWayString() {
            @Override
            public void onWay(String str, String type) {
                    tvType.setTag(type);
                    tvType.setText(str);
            }
        });
        screenTypeDialog.show();
    }

    private void showScreenLocationPicker() {
        ScreenPositionDialog screenPositionDialog = new ScreenPositionDialog(this, new ScreenPositionDialog.OnWayString() {
            @Override
            public void onWay(String str, String type) {
                switch (str){
                    case "左屏":
                        tvOritation.setTag(Constants.DEVICE_PLACE.LEFT);
                        tvOritation.setText("左屏");
                        tvOritation.setCompoundDrawablesWithIntrinsicBounds(R.mipmap.ic_screen_left, 0, R.drawable.ic_user_right, 0);
                        break;
                    case "中屏":
                        tvOritation.setTag(Constants.DEVICE_PLACE.CENTER);
                        tvOritation.setText("中屏");
                        tvOritation.setCompoundDrawablesWithIntrinsicBounds(R.mipmap.ic_screen_middle, 0, R.drawable.ic_user_right, 0);
                        break;
                    case "右屏" :
                        tvOritation.setTag(Constants.DEVICE_PLACE.RIGHT);
                        tvOritation.setText("右屏");
                        tvOritation.setCompoundDrawablesWithIntrinsicBounds(R.mipmap.ic_screen_right, 0, R.drawable.ic_user_right, 0);
                        break;
                }

            }
        });
        screenPositionDialog.show();
    }

    public void checkLocation() {
        String[] locationPermission = new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION};
        LocationUtils.getInstance(this).register(this, this);
        checkPermission(new CheckPermListener() {
            @Override
            public void agreeAllPermission() {
                flLocation.post(new Runnable() {
                    @Override
                    public void run() {
                        LocationUtils.getInstance(DeviceEditActivity.this).startLocation();
                    }
                });
            }
        }, "需要允许定位相关权限用于获取地区资讯", locationPermission);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (RESULT_OK == resultCode && data != null) {
            switch (requestCode) {
                case CommonUtil.REQ_CODE_1:
                    String deviceStr = data.getStringExtra(CaptureActivity.EXTRA_SCAN_RESULT);
                    String deviceNo = FormatUtils.getInstance().getDeviceNo(deviceStr);
                    if (!TextUtils.isEmpty(deviceNo)) {
                        tvDeviceNo.setText(deviceNo);
                    } else {
                        showToast("没有获取到设备特征码");
                    }
                    break;
                case CommonUtil.REQ_CODE_2:
                    CellItemEntity cell = data.getParcelableExtra(CommonUtil.KEY_VALUE_1);
                    if (cell != null) {
                        if (tvCell.getTag() == null || (long) tvCell.getTag() != cell.getObjctId()) {
                            tvCell.setTag(cell.getObjctId());
                            clearBelowCell();
                        }
                        regionName = cell.getRegionName();
                        tvCell.setText(cell.getCellName());
                    }
                    break;
                case CommonUtil.REQ_CODE_3:
                    NormalElemEntity build = data.getParcelableExtra(CommonUtil.KEY_VALUE_1);
                    if (build != null) {
                        if (tvBuild.getTag() == null || (long) tvBuild.getTag() != build.getObjectId()) {
                            tvBuild.setTag(build.getObjectId());
                            clearBelowBuild();
                        }
                        tvBuild.setText(build.getName());
                    }
                    break;
                case CommonUtil.REQ_CODE_4:
                    NormalElemEntity unit = data.getParcelableExtra(CommonUtil.KEY_VALUE_1);
                    if (unit != null) {
                        if (tvUnit.getTag() == null || (long) tvUnit.getTag() != unit.getObjectId()) {
                            tvUnit.setTag(unit.getObjectId());
                            clearBelowUnit();
                        }
                        tvUnit.setText(unit.getName());
                    }
                    break;
                case CommonUtil.REQ_CODE_5:
                    LiftElemEntity lift = data.getParcelableExtra(CommonUtil.KEY_VALUE_1);
                    if (lift != null) {
                        if (tvLift.getTag() == null || (long) tvLift.getTag() != lift.getObjectId()) {
                            tvLift.setTag(lift.getObjectId());
                        }
                        CameraEntity camera = lift.getCamera();
                        llCamera.setVisibility(View.VISIBLE);
                        if (camera == null) {
                            llCamera.setEnabled(true);
                            llCameraInfo.setEnabled(true);
                            tvCameraInfo.setEnabled(true);
                            etCameraIp.setEnabled(true);
                            tvCameraInfo.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_user_right, 0);
                            tvCameraInfo.setText("");
                            tvCameraInfo.setTag(null);
                            etCameraIp.setText("");
                            etCameraIp.setSelection(etCameraIp.getText().length());
                        } else {
                            llCamera.setEnabled(false);
                            llCameraInfo.setEnabled(false);
                            tvCameraInfo.setEnabled(false);
                            etCameraIp.setEnabled(false);
                            tvCameraInfo.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                            tvCameraInfo.setText(camera.getBrand() != null ? camera.getBrand().getName() : "");
                            tvCameraInfo.setTag(camera.getBrand() != null ? camera.getBrand().getObjectId() : 0);
                            etCameraIp.setText(camera.getIpAddress());
                            etCameraIp.setSelection(etCameraIp.getText().length());
                        }
                        tvLift.setText(lift.getLiftNo());
                    }
                    break;
                case CommonUtil.REQ_CODE_6:
                    CameraBrandEntity brandEntity = data.getParcelableExtra(CommonUtil.KEY_VALUE_1);
                    if (brandEntity != null) {
                        tvCameraInfo.setText(brandEntity.getName());
                        tvCameraInfo.setTag(brandEntity.getObjectId());
                    }
                    break;
            }
        }
    }

    private void clearBelowUnit() {
        tvLift.setTag(null);
        tvLift.setText("");
        llCamera.setVisibility(View.GONE);
    }

    private void clearBelowBuild() {
        tvUnit.setTag(null);
        tvUnit.setText("");
        tvLift.setTag(null);
        tvLift.setText("");
        llCamera.setVisibility(View.GONE);
    }

    private void clearBelowCell() {
        tvBuild.setTag(null);
        tvBuild.setText("");
        tvUnit.setTag(null);
        tvUnit.setText("");
        tvLift.setTag(null);
        tvLift.setText("");
        llCamera.setVisibility(View.GONE);
    }

    @Override
    public void onLocationChanged(AMapLocation aMapLocation) {
        if (aMapLocation != null) {
            if (aMapLocation.getErrorCode() == 0) {
                longitude = aMapLocation.getLongitude();
                latitude = aMapLocation.getLatitude();
                requestCell(longitude, latitude);
            } else {
                flLocation.setVisibility(View.GONE);
                showToast("定位失败");
            }
        } else {
            flLocation.setVisibility(View.GONE);
            showToast("定位失败");
        }
        LocationUtils.getInstance(this).stopLocation();
        LocationUtils.getInstance(this).unregister(this);
    }

    @Override
    protected void onDestroy() {
        LocationUtils.getInstance(this).unregister(this);
        super.onDestroy();
    }

    private void requestCell(double longitude, double latitude) {
        NetService.getInstance().partnerCellList(longitude, latitude, 1)
                .flatMap(new Func1<ConentEntity<CellItemEntity>, Observable<CellItemEntity>>() {
                    @Override
                    public Observable<CellItemEntity> call(ConentEntity<CellItemEntity> pageListEntity) {
                        return Observable.just((pageListEntity != null && pageListEntity.getContent() != null && pageListEntity.getContent().size() > 0) ? pageListEntity.getContent().get(0) : null);
                    }
                })
                .compose(this.<CellItemEntity>bindLifeCycle())
                .subscribe(new CustomApiCallback<CellItemEntity>() {
                    @Override
                    protected void onResultError(ApiException ex) {
                        showToast(ex.getDisplayMessage());
                        flLocation.setVisibility(View.GONE);
                        showToast("没有获取到附近小区");
                    }

                    @Override
                    public void onNext(CellItemEntity entity) {
                        tvLocation.setTag(entity);
//                        tvLocation.setText(String.format("当前定位：%s", entity.getCellName()));
                        tvLocation.setText(entity.getCellName());
                        if (tvCell.getTag() == null) {
                            tvCell.setTag(entity.getObjctId());
                            tvCell.setText(entity.getCellName());
                            clearBelowCell();
                        }
                        flLocation.setVisibility(View.VISIBLE);
                    }
                });
    }
}
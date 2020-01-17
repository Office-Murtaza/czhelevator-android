package com.kingyon.elevator.uis.activities.installer;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;

import com.acker.simplezxing.activity.CaptureActivity;
import com.blankj.utilcode.util.LogUtils;
import com.gerry.scaledelete.ScreenUtil;
import com.kingyon.elevator.R;
import com.kingyon.elevator.entities.PointItemEntity;
import com.kingyon.elevator.nets.CustomApiCallback;
import com.kingyon.elevator.nets.NetService;
import com.kingyon.elevator.uis.activities.devices.DeviceEditActivity;
import com.kingyon.elevator.utils.CommonUtil;
import com.kingyon.elevator.utils.FormatUtils;
import com.kingyon.elevator.utils.TimeUtil;
import com.leo.afbaselibrary.nets.entities.PageListEntity;
import com.leo.afbaselibrary.nets.exceptions.ApiException;
import com.leo.afbaselibrary.nets.exceptions.ResultException;
import com.leo.afbaselibrary.uis.activities.BaseActivity;
import com.leo.afbaselibrary.uis.activities.BaseStateRefreshingLoadingActivity;
import com.leo.afbaselibrary.uis.adapters.BaseAdapter;
import com.leo.afbaselibrary.uis.adapters.MultiItemTypeAdapter;
import com.leo.afbaselibrary.uis.adapters.holders.CommonHolder;
import com.yqritc.recyclerviewflexibledivider.HorizontalDividerItemDecoration;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by GongLi on 2019/1/21.
 * Email：lc824767150@163.com
 */

public class InstallerActivity extends BaseStateRefreshingLoadingActivity<PointItemEntity> {
    @BindView(R.id.pre_v_right)
    ImageView preVRight;

    @Override
    protected String getTitleText() {
        return "设备管理";
    }

    @Override
    public int getContentViewId() {
        return R.layout.activity_installer;
    }

    @Override
    protected void initViews(Bundle savedInstanceState) {
        super.initViews(savedInstanceState);
        preVRight.setImageResource(R.drawable.ic_capture_code);
    }

    @Override
    protected MultiItemTypeAdapter<PointItemEntity> getAdapter() {
        return new BaseAdapter<PointItemEntity>(this, R.layout.activity_installer_item, mItems) {
            @Override
            protected void convert(CommonHolder holder, PointItemEntity item, int position) {

                holder.setTextNotHide(R.id.tv_cell, item.getCellName());

                String location = String.format("%s%s%s", item.getBuild() != null ? item.getBuild() : "", item.getUnit() != null ? item.getUnit() : "", item.getLift() != null ? item.getLift() : "");
                String deviceOritation = FormatUtils.getInstance().getDeviceOritation(item.getDevice());

                holder.setTextNotHide(R.id.tv_location, location);
                holder.setVisible(R.id.tv_interval, !TextUtils.isEmpty(location) && !TextUtils.isEmpty(deviceOritation));
                holder.setTextNotHide(R.id.tv_oritation, deviceOritation);
                holder.setImageDrawable(R.id.img_oritation, FormatUtils.getInstance().getDeviceOritationDrawable(mContext, item.getDevice()));

                holder.setTextNotHide(R.id.tv_sn, String.format("设备特征码：%s", item.getDeviceNo()));
                holder.setTextNotHide(R.id.tv_device_id, String.format("设备编号：%s", CommonUtil.getDeviceId(item.getObjectId())));
                holder.setTextNotHide(R.id.tv_time, String.format("创建时间：%s", TimeUtil.getAllTimeNoSecond(item.getInstallTime())));
            }
        };
    }

    @Override
    public void onItemClick(View view, RecyclerView.ViewHolder holder, PointItemEntity item, int position) {
        super.onItemClick(view, holder, item, position);
        if (item != null) {
            Bundle bundle = new Bundle();
            bundle.putLong(CommonUtil.KEY_VALUE_1, item.getObjectId());
            startActivity(InstallerDeviceDetailsActivity.class, bundle);
        }
    }

    @Override
    protected void loadData(final int page) {
        NetService.getInstance().installerDeviceList(page)
                .compose(this.<PageListEntity<PointItemEntity>>bindLifeCycle())
                .subscribe(new CustomApiCallback<PageListEntity<PointItemEntity>>() {
                    @Override
                    protected void onResultError(ApiException ex) {
                        showToast(ex.getDisplayMessage());
                        loadingComplete(false, 100000);
                    }

                    @Override
                    public void onNext(PageListEntity<PointItemEntity> pointItemEntityPageListEntity) {
                        if (pointItemEntityPageListEntity == null) {
                            throw new ResultException(9001, "返回参数异常");
                        }
                        if (FIRST_PAGE == page) {
                            mItems.clear();
                        }
                        if (pointItemEntityPageListEntity.getContent() != null) {
                            mItems.addAll(pointItemEntityPageListEntity.getContent());
                        }
                        loadingComplete(true, pointItemEntityPageListEntity.getTotalPages());
                    }
                });
    }

    @Override
    protected void setDivider() {
        mRecyclerView.addItemDecoration(new HorizontalDividerItemDecoration.Builder(this)
                .drawable(R.drawable.white_margin_sixteen_divider)
                .sizeResId(R.dimen.spacing_divider)
                .build());
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

        //        Bundle bundle = new Bundle();
//        bundle.putBoolean(CommonUtil.KEY_VALUE_4, true);
//        startActivityForResult(DeviceEditActivity.class, CommonUtil.REQ_CODE_2, bundle);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (RESULT_OK == resultCode) {
            switch (requestCode) {
                case CommonUtil.REQ_CODE_1:
                    if (data != null) {
                        String deviceStr = data.getStringExtra(CaptureActivity.EXTRA_SCAN_RESULT);
                        LogUtils.d("返回的设备字符串："+deviceStr);
                        String deviceNo = FormatUtils.getInstance().getDeviceNo(deviceStr);
                        if (!TextUtils.isEmpty(deviceNo)) {
                            Bundle bundle = new Bundle();
                            bundle.putString(CommonUtil.KEY_VALUE_3, deviceNo);
                            bundle.putBoolean(CommonUtil.KEY_VALUE_4, false);
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

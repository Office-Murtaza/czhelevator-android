package com.kingyon.elevator.uis.activities.cooperation;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.TextView;

import com.kingyon.elevator.R;
import com.kingyon.elevator.entities.DeviceDetailsInfo;
import com.kingyon.elevator.entities.IncomeRecordEntity;
import com.kingyon.elevator.entities.PointItemEntity;
import com.kingyon.elevator.nets.CustomApiCallback;
import com.kingyon.elevator.nets.NetService;
import com.kingyon.elevator.uis.adapters.adapterone.CooperationDeviceDetailsAdapter;
import com.kingyon.elevator.uis.widgets.CooperationDeviceDetailsDecoration;
import com.kingyon.elevator.utils.CommonUtil;
import com.kingyon.elevator.utils.TimeUtil;
import com.leo.afbaselibrary.nets.entities.PageListEntity;
import com.leo.afbaselibrary.nets.exceptions.ApiException;
import com.leo.afbaselibrary.nets.exceptions.ResultException;
import com.leo.afbaselibrary.uis.activities.BaseStateRefreshingLoadingActivity;
import com.leo.afbaselibrary.uis.adapters.MultiItemTypeAdapter;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by GongLi on 2019/1/15.
 * Email：lc824767150@163.com
 * 设备详情
 */

public class CooperationDevicesDetailsActivity extends BaseStateRefreshingLoadingActivity<Object> implements CooperationDeviceDetailsDecoration.GroupStickyListener {
    @BindView(R.id.pre_v_right)
    public TextView preVRight;

    protected long deviceId;
    private String role;

    @Override
    protected String getTitleText() {
        deviceId = getIntent().getLongExtra(CommonUtil.KEY_VALUE_1, 0);
        role = getIntent().getStringExtra(CommonUtil.KEY_VALUE_2);
        return "设备详情";
    }

    @Override
    public int getContentViewId() {
        return R.layout.activity_cooperation_devices_details;
    }

    @Override
    protected void initViews(Bundle savedInstanceState) {
        super.initViews(savedInstanceState);
        preVRight.setText("设备报修");
        mRecyclerView.addItemDecoration(new CooperationDeviceDetailsDecoration(this, this));
    }

    @Override
    protected MultiItemTypeAdapter<Object> getAdapter() {
        return new CooperationDeviceDetailsAdapter(this, mItems);
    }

    @Override
    protected void loadData(final int page) {
        NetService.getInstance().deviceDetails(deviceId, role, page)
                .compose(this.<DeviceDetailsInfo>bindLifeCycle())
                .subscribe(new CustomApiCallback<DeviceDetailsInfo>() {
                    @Override
                    protected void onResultError(ApiException ex) {
                        showToast(ex.getDisplayMessage());
                        loadingComplete(false, 100000);
                    }

                    @Override
                    public void onNext(DeviceDetailsInfo deviceDetailsInfo) {
                        PageListEntity<IncomeRecordEntity> incomePage = deviceDetailsInfo.getIncomePage();
                        if (incomePage == null) {
                            throw new ResultException(9001, "返回参数异常");
                        }
                        List<IncomeRecordEntity> content = incomePage.getContent();
                        if (FIRST_PAGE == page) {
                            mItems.clear();
                            PointItemEntity device = deviceDetailsInfo.getDevice();
                            if (device == null) {
                                throw new ResultException(9001, "返回参数异常");
                            }
                            mItems.add(device);
                            if (content != null && content.size() > 0) {
                                mItems.add("tip");
                            }
                        }
                        if (content != null) {
                            mItems.addAll(content);
                        }
                        loadingComplete(true, incomePage.getTotalPages());
                    }
                });
    }

    @OnClick(R.id.pre_v_right)
    public void onViewClicked() {
        onReportClick();
    }

    protected void onReportClick() {
        Bundle bundle = new Bundle();
        bundle.putLong(CommonUtil.KEY_VALUE_1, deviceId);
        startActivityForResult(CooperationDeviceReportActivity.class, CommonUtil.REQ_CODE_1, bundle);
    }

    @Override
    public boolean isFirstInGroup(int pos) {
        boolean result;
        if (pos >= 0 && pos < mItems.size()) {
            Object o = mItems.get(pos);
            if (o instanceof IncomeRecordEntity) {
                String prevGroupId = getGroupName(pos - 1);
                String groupId = getGroupName(pos);
                result = !TextUtils.equals(prevGroupId, groupId);
            } else {
                result = false;
            }
        } else {
            result = false;
        }
        return result;
    }

    @Override
    public String getGroupName(int position) {
        String result;
        if (position >= 0 && position < mItems.size()) {
            Object o = mItems.get(position);
            if (o instanceof IncomeRecordEntity) {
                result = TimeUtil.getYmCh(((IncomeRecordEntity) o).getCreateTime());
            } else {
                result = null;
            }
        } else {
            result = null;
        }
        return result;
    }

    @Override
    protected boolean isShowDivider() {
        return false;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (RESULT_OK == resultCode && CommonUtil.REQ_CODE_1 == requestCode) {
            autoRefresh();
        }
    }
}

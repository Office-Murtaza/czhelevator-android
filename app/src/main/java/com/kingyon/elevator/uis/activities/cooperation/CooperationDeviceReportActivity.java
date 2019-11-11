package com.kingyon.elevator.uis.activities.cooperation;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bigkoo.pickerview.OptionsPickerView;
import com.kingyon.elevator.R;
import com.kingyon.elevator.entities.NormalElemEntity;
import com.kingyon.elevator.nets.CustomApiCallback;
import com.kingyon.elevator.nets.NetService;
import com.kingyon.elevator.nets.NetUpload;
import com.kingyon.elevator.uis.adapters.BaseAdapterWithHF;
import com.kingyon.elevator.uis.adapters.UploadImageAdapter;
import com.kingyon.elevator.uis.widgets.FullyGridLayoutManager;
import com.kingyon.elevator.utils.CommonUtil;
import com.kingyon.elevator.utils.DealScrollRecyclerView;
import com.kingyon.elevator.utils.GridSpacingItemDecoration;
import com.kingyon.elevator.utils.KeyBoardUtils;
import com.kingyon.elevator.utils.PictureSelectorUtil;
import com.leo.afbaselibrary.nets.exceptions.ApiException;
import com.leo.afbaselibrary.nets.exceptions.ResultException;
import com.leo.afbaselibrary.uis.activities.BaseSwipeBackActivity;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import me.nereo.multi_image_selector.MultiImageSelector;

/**
 * Created by GongLi on 2019/1/15.
 * Email：lc824767150@163.com
 */

public class CooperationDeviceReportActivity extends BaseSwipeBackActivity implements BaseAdapterWithHF.OnItemClickListener<Object>, NetUpload.OnUploadCompletedListener {

    @BindView(R.id.tv_reason)
    TextView tvReason;
    @BindView(R.id.ll_reason)
    LinearLayout llReason;
    @BindView(R.id.et_remark)
    EditText etRemark;
    @BindView(R.id.tv_length)
    TextView tvLength;
    @BindView(R.id.rv_images)
    RecyclerView rvImages;
    @BindView(R.id.tv_create)
    TextView tvCreate;

    private long deviceId;
    private UploadImageAdapter uploadAdapter;
    private OptionsPickerView reasonPicker;
    private ArrayList<NormalElemEntity> reasonOptions;

    @Override
    protected String getTitleText() {
        deviceId = getIntent().getLongExtra(CommonUtil.KEY_VALUE_1, 0);
        return "设备报修";
    }

    @Override
    public int getContentViewId() {
        return R.layout.activity_cooperation_device_report;
    }

    @Override
    protected void initViews(Bundle savedInstanceState) {
        rvImages.addItemDecoration(new GridSpacingItemDecoration(3, getResources().getDimensionPixelSize(R.dimen.spacing_small), false));
        uploadAdapter = new UploadImageAdapter(this);
        uploadAdapter.setMaxCount(9);
        DealScrollRecyclerView.getInstance().dealAdapter(uploadAdapter, rvImages, new FullyGridLayoutManager(this, 3));
        uploadAdapter.setOnItemClickListener(this);
        etRemark.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                tvLength.setText(String.format("%s/200", s.length()));
            }
        });
    }

    @Override
    public void onItemClick(View view, int position, Object entity, BaseAdapterWithHF<Object> baseAdaper) {
        if (view.getId() == R.id.img_delete) {
            uploadAdapter.deleteItemData(entity);
        } else {
            if (position >= uploadAdapter.getItemCount() - uploadAdapter.getFooterCount()) {
                PictureSelectorUtil.showPictureSelectorNoCrop(this, CommonUtil.REQ_CODE_1, uploadAdapter.getMaxCount() - uploadAdapter.getItemRealCount());
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (RESULT_OK == resultCode && CommonUtil.REQ_CODE_1 == requestCode && data != null) {
            ArrayList<String> mSelectPath = data.getStringArrayListExtra(MultiImageSelector.EXTRA_RESULT);
            if (mSelectPath != null && mSelectPath.size() > 0) {
                uploadAdapter.addDatas(mSelectPath);
            }
        }
    }

    @OnClick({R.id.ll_reason, R.id.tv_create})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ll_reason:
                showReasonPicker();
                break;
            case R.id.tv_create:
                onCreateClick();
                break;
        }
    }

    private void showReasonPicker() {
        if (reasonPicker == null) {
            llReason.setEnabled(false);
            NetService.getInstance().repairReasons()
                    .compose(this.<List<NormalElemEntity>>bindLifeCycle())
                    .subscribe(new CustomApiCallback<List<NormalElemEntity>>() {
                        @Override
                        protected void onResultError(ApiException ex) {
                            showToast(ex.getDisplayMessage());
                            llReason.setEnabled(true);
                        }

                        @Override
                        public void onNext(List<NormalElemEntity> entities) {
                            if (entities == null || entities.size() < 1) {
                                throw new ResultException(9001, "返回参数异常");
                            }
                            reasonOptions = new ArrayList<>();
                            reasonOptions.addAll(entities);
                            reasonPicker = new OptionsPickerView.Builder(CooperationDeviceReportActivity.this, new OptionsPickerView.OnOptionsSelectListener() {
                                @Override
                                public void onOptionsSelect(int options1, int options2, int options3, View v) {
                                    if (reasonOptions == null || reasonOptions.size() <= options1) {
                                        return;
                                    }
                                    NormalElemEntity entity = reasonOptions.get(options1);
                                    tvReason.setTag(entity.getObjectId());
                                    tvReason.setText(entity.getName());
                                }
                            }).setCyclic(false, false, false).build();
                            reasonPicker.setPicker(reasonOptions);
                            KeyBoardUtils.closeKeybord(CooperationDeviceReportActivity.this);
                            reasonPicker.show();
                            llReason.setEnabled(true);
                        }
                    });
        } else {
            KeyBoardUtils.closeKeybord(CooperationDeviceReportActivity.this);
            reasonPicker.show();
            llReason.setEnabled(true);
        }
    }

    private void onCreateClick() {
        Long reasonId = (Long) tvReason.getTag();
        if (reasonId == null) {
            showToast("请选择原因");
            return;
        }
        if (TextUtils.isEmpty(CommonUtil.getEditText(etRemark))) {
            showToast("请输入具体问题");
            return;
        }
        if (uploadAdapter.getItemRealCount() > 0) {
            List<File> uploadFiles = uploadAdapter.getUploadDatas();
            if (uploadFiles.size() > 0) {
                showProgressDialog(getString(R.string.wait));
                NetService.getInstance().uploadFiles(this, uploadFiles, this);
            } else {
                publishRequest(NetService.getInstance().getUploadResultString(uploadAdapter.getAllDatas()));
            }
        } else {
            publishRequest(null);
        }
    }

    private void publishRequest(String pictures) {
        showProgressDialog(getString(R.string.wait));
        tvCreate.setEnabled(false);
        NetService.getInstance().repairDevice(deviceId, (Long) tvReason.getTag(), etRemark.getText().toString(), pictures)
                .compose(this.<String>bindLifeCycle())
                .subscribe(new CustomApiCallback<String>() {
                    @Override
                    protected void onResultError(ApiException ex) {
                        tvCreate.setEnabled(true);
                        hideProgress();
                        showToast(ex.getDisplayMessage());
                    }

                    @Override
                    public void onNext(String s) {
                        tvCreate.setEnabled(true);
                        showToast("提交成功");
                        hideProgress();
                        setResult(RESULT_OK);
                        finish();
                    }
                });
    }

    @Override
    public void uploadSuccess(final List<String> images) {
        List<String> result = new ArrayList<>();
        int index = 0;
        for (Object object : uploadAdapter.getDatas()) {
            String link = object.toString();
            if (!TextUtils.isEmpty(link) && !link.startsWith("http")) {
                result.add(images.get(index++));
            } else {
                result.add(link);
            }
        }
        publishRequest(NetService.getInstance().getUploadResultString(result));
    }

    @Override
    public void uploadFailed(ApiException ex) {
        hideProgress();
        showToast(ex.getDisplayMessage());
    }
}

package com.kingyon.elevator.uis.activities.user;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.LogUtils;
import com.kingyon.elevator.R;
import com.kingyon.elevator.constants.Constants;
import com.kingyon.elevator.entities.IdentityInfoEntity;
import com.kingyon.elevator.nets.CustomApiCallback;
import com.kingyon.elevator.nets.NetService;
import com.kingyon.elevator.nets.NetUpload;
import com.kingyon.elevator.utils.CommonUtil;
import com.kingyon.elevator.utils.PictureSelectorUtil;
import com.leo.afbaselibrary.nets.exceptions.ApiException;
import com.leo.afbaselibrary.nets.exceptions.ResultException;
import com.leo.afbaselibrary.uis.activities.BaseStateLoadingActivity;
import com.leo.afbaselibrary.utils.GlideUtils;

import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import me.nereo.multi_image_selector.MultiImageSelector;

/**
 * Created by GongLi on 2019/1/9.
 * Email：lc824767150@163.com
 */

public class IdentityCompanyActivity extends BaseStateLoadingActivity {
    @BindView(R.id.tv_reason)
    TextView tvReason;
    @BindView(R.id.ll_failed)
    LinearLayout llFailed;
    @BindView(R.id.et_name)
    EditText etName;
    @BindView(R.id.img_cert)
    ImageView imgCert;
    @BindView(R.id.fl_cert)
    FrameLayout flCert;
    @BindView(R.id.tv_ensure)
    TextView tvEnsure;

    private boolean jumping;

    @Override
    protected String getTitleText() {
        return "企业认证";
    }

    @Override
    public int getContentViewId() {
        return R.layout.activity_identity_company;
    }

    @Override
    protected void initViews(Bundle savedInstanceState) {

    }

    @Override
    protected void loadData() {
        NetService.getInstance().getIdentityInformation()
                .compose(this.<IdentityInfoEntity>bindLifeCycle())
                .subscribe(new CustomApiCallback<IdentityInfoEntity>() {
                    @Override
                    protected void onResultError(ApiException ex) {
                        showToast(ex.getDisplayMessage());
                        loadingComplete(STATE_EMPTY);
                        switch (ex.getCode()) {
                            case 9002:
                                if (!jumping) {
                                    jumping = true;
                                    stateLayout.postDelayed(new Runnable() {
                                        @Override
                                        public void run() {
                                            finish();
                                        }
                                    }, 1000);
                                }
                                break;
                            case 9003:
                                if (!jumping) {
                                    jumping = true;
                                    stateLayout.postDelayed(new Runnable() {
                                        @Override
                                        public void run() {
                                            Bundle bundle = new Bundle();
                                            bundle.putString("type",Constants.IDENTITY_STATUS.AUTHED);
                                            startActivity(IdentitySuccessActivity.class,bundle);
                                            finish();
                                        }
                                    }, 1000);
                                }
                                break;
                            case 9004:
                                if (!jumping) {
                                    jumping = true;
                                    stateLayout.postDelayed(new Runnable() {
                                        @Override
                                        public void run() {
                                            Bundle bundle = new Bundle();
                                            bundle.putString("type",Constants.IDENTITY_STATUS.FAILD);
                                            startActivity(IdentitySuccessActivity.class,bundle);
                                            finish();
                                        }
                                    }, 1000);
                                }
                                break;
                        }
                    }

                    @Override
                    public void onNext(IdentityInfoEntity identityInfoEntity) {
                        if (identityInfoEntity == null) {
                            throw new ResultException(9001, "返回参数异常");
                        }
                        if (TextUtils.equals(Constants.IDENTITY_STATUS.AUTHING, identityInfoEntity.getStatus())) {
                            throw new ResultException(9002, "新的认证资料审核中，即将跳转");
                        }
                        if (TextUtils.equals(Constants.IDENTITY_STATUS.AUTHED, identityInfoEntity.getStatus())) {
                            throw new ResultException(9003, "新的认证资料已通过，即将跳转");
                        }
                        if (TextUtils.equals(Constants.IDENTITY_STATUS.FAILD, identityInfoEntity.getStatus())) {
                            throw new ResultException(9004, "新的认证失败，即将跳转");
                        }

                        tvReason.setText(identityInfoEntity.getFaildReason() != null ? identityInfoEntity.getFaildReason() : "");
                        boolean failed = TextUtils.equals(Constants.IDENTITY_STATUS.FAILD, identityInfoEntity.getStatus());
                        llFailed.setVisibility(failed ? View.VISIBLE : View.GONE);
                        etName.setText(identityInfoEntity.getCompanyName() != null ? identityInfoEntity.getCompanyName() : "");
                        etName.setSelection(etName.getText().length());
                        String certUrl = identityInfoEntity.getBusinessCert();
                        if (!TextUtils.isEmpty(certUrl)) {
                            flCert.setTag(certUrl);
                            GlideUtils.loadImage(IdentityCompanyActivity.this, certUrl, imgCert);
                        }
                        loadingComplete(STATE_CONTENT);
                    }
                });
    }

    @Override
    protected String getEmptyTip() {
        stateLayout.setEmptyViewTip("  ");
        return "  ";
    }

    @OnClick({R.id.fl_cert, R.id.tv_ensure})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.fl_cert:
                PictureSelectorUtil.showPictureSelectorNoCrop(this, CommonUtil.REQ_CODE_1);
                break;
            case R.id.tv_ensure:
                onEnsureClick();
                break;
        }
    }

    public void onEnsureClick() {
        if (TextUtils.isEmpty(CommonUtil.getEditText(etName))) {
            showToast("请填写企业名称");
            return;
        }
        String certUrl = (String) flCert.getTag();
        if (TextUtils.isEmpty(certUrl)) {
            showToast("请上传营业执照图片");
            return;
        }
        showProgressDialog(getString(R.string.wait));
        tvEnsure.setEnabled(false);
        NetService.getInstance().identityAuth(Constants.IDENTITY_TYPE.COMPANY
                , null, null, null, null
                , etName.getText().toString(), certUrl)
                .compose(this.<String>bindLifeCycle())
                .subscribe(new CustomApiCallback<String>() {
                    @Override
                    protected void onResultError(ApiException ex) {
                        showToast(ex.getDisplayMessage());
                        hideProgress();
                        tvEnsure.setEnabled(false);
                    }

                    @Override
                    public void onNext(String s) {
                        showToast("保存成功");
                        hideProgress();
                        tvEnsure.setEnabled(true);
                        setResult(RESULT_OK);
                        finish();
                    }
                });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (RESULT_OK == resultCode && CommonUtil.REQ_CODE_1 == requestCode && data != null) {
            ArrayList<String> facePath = data.getStringArrayListExtra(MultiImageSelector.EXTRA_RESULT);
            if (facePath != null && facePath.size() > 0) {
                showProgressDialog(getString(R.string.wait));
                NetService.getInstance().uploadFile(this, new File(facePath.get(0)), new NetUpload.OnUploadCompletedListener() {
                    @Override
                    public void uploadSuccess(List<String> images,List<String> hash, JSONObject response) {
                        LogUtils.e(images,hash,response);
                        if (images != null && images.size() > 0) {
                            String certUrl = images.get(0);
                            flCert.setTag(certUrl);
                            GlideUtils.loadImage(IdentityCompanyActivity.this, certUrl, imgCert);
                            hideProgress();
                        } else {
                            showToast("上传头像出错");
                            hideProgress();
                        }
                    }

                    @Override
                    public void uploadFailed(ApiException ex) {
                        showToast(ex.getDisplayMessage());
                        hideProgress();
                    }
                });
            }
        }
    }
}

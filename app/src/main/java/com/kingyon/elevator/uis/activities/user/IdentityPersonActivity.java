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

import com.alibaba.android.arouter.facade.annotation.Route;
import com.baidu.ocr.sdk.OCR;
import com.baidu.ocr.sdk.OnResultListener;
import com.baidu.ocr.sdk.exception.OCRError;
import com.baidu.ocr.sdk.model.IDCardParams;
import com.baidu.ocr.sdk.model.IDCardResult;
import com.baidu.ocr.ui.camera.CameraActivity;
import com.baidu.ocr.ui.camera.CameraNativeHelper;
import com.baidu.ocr.ui.camera.CameraView;
import com.blankj.utilcode.util.LogUtils;
import com.kingyon.elevator.R;
import com.kingyon.elevator.constants.Constants;
import com.kingyon.elevator.entities.IdentityInfoEntity;
import com.kingyon.elevator.nets.CustomApiCallback;
import com.kingyon.elevator.nets.NetService;
import com.kingyon.elevator.nets.NetUpload;
import com.kingyon.elevator.utils.CommonUtil;
import com.kingyon.elevator.utils.OCRUtil;
import com.kingyon.elevator.utils.PictureSelectorUtil;
import com.leo.afbaselibrary.nets.exceptions.ApiException;
import com.leo.afbaselibrary.nets.exceptions.ResultException;
import com.leo.afbaselibrary.uis.activities.BaseStateLoadingActivity;
import com.leo.afbaselibrary.utils.GlideUtils;
import com.orhanobut.logger.Logger;

import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import me.nereo.multi_image_selector.MultiImageSelector;

import static com.czh.myversiontwo.utils.Constance.ACTIVITY_IDENTITY_INFO;

/**
 * Created by GongLi on 2019/1/9.
 * Email：lc824767150@163.com
 */
@Route(path = ACTIVITY_IDENTITY_INFO)
public class IdentityPersonActivity extends BaseStateLoadingActivity {
    @BindView(R.id.tv_reason)
    TextView tvReason;
    @BindView(R.id.ll_failed)
    LinearLayout llFailed;
    @BindView(R.id.et_name)
    EditText etName;
    @BindView(R.id.et_number)
    EditText etNumber;
    @BindView(R.id.img_face)
    ImageView imgFace;
    @BindView(R.id.fl_face)
    FrameLayout flFace;
    @BindView(R.id.img_back)
    ImageView imgBack;
    @BindView(R.id.fl_back)
    FrameLayout flBack;
    @BindView(R.id.tv_ensure)
    TextView tvEnsure;

    private boolean jumping;

    @Override
    protected String getTitleText() {
        return "个人认证";
    }

    @Override
    public int getContentViewId() {
        return R.layout.activity_identity_person;
    }

    @Override
    protected void initViews(Bundle savedInstanceState) {
        //  初始化本地质量控制模型,释放代码在onDestory中
        //  调用身份证扫描必须加上 intent.putExtra(CameraActivity.KEY_NATIVE_MANUAL, true); 关闭自动初始化和释放本地模型
        CameraNativeHelper.init(this, OCR.getInstance(this).getLicense(),
                new CameraNativeHelper.CameraNativeInitCallback() {
                    @Override
                    public void onError(int errorCode, Throwable e) {
                        String msg;
                        switch (errorCode) {
                            case CameraView.NATIVE_SOLOAD_FAIL:
                                msg = "加载so失败，请确保apk中存在ui部分的so";
                                break;
                            case CameraView.NATIVE_AUTH_FAIL:
                                msg = "授权本地质量控制token获取失败";
                                break;
                            case CameraView.NATIVE_INIT_FAIL:
                                msg = "本地质量控制";
                                break;
                            default:
                                msg = String.valueOf(errorCode);
                        }
                        showToast(msg);
                        Logger.e(String.format("本地质量控制初始化错误，错误原因：%s!", msg));
                        finish();
                    }
                });
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
                                            startActivity(IdentitySuccessActivity.class);
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
                        tvReason.setText(identityInfoEntity.getFaildReason() != null ? identityInfoEntity.getFaildReason() : "");
                        boolean failed = TextUtils.equals(Constants.IDENTITY_STATUS.FAILD, identityInfoEntity.getStatus());
                        llFailed.setVisibility(failed ? View.VISIBLE : View.GONE);
                        etNumber.setText(identityInfoEntity.getIdNum() != null ? identityInfoEntity.getIdNum() : "");
                        etNumber.setSelection(etNumber.getText().length());
                        etName.setText(identityInfoEntity.getPersonName() != null ? identityInfoEntity.getPersonName() : "");
                        etName.setSelection(etName.getText().length());
                        String faceUrl = identityInfoEntity.getIdFace();
                        if (!TextUtils.isEmpty(faceUrl)) {
                            flFace.setTag(faceUrl);
                            GlideUtils.loadImage(IdentityPersonActivity.this, faceUrl, imgFace);
                        }
                        String backUrl = identityInfoEntity.getIdBack();
                        if (!TextUtils.isEmpty(backUrl)) {
                            flBack.setTag(backUrl);
                            GlideUtils.loadImage(IdentityPersonActivity.this, backUrl, imgBack);
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

    @OnClick({R.id.fl_face, R.id.fl_back, R.id.tv_ensure})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.fl_face:
//                PictureSelectorUtil.showPictureSelectorNoCrop(this, CommonUtil.REQ_CODE_1);
                PictureSelectorUtil.showCameraIdCardFace(this, CommonUtil.REQ_CODE_3);
                break;
            case R.id.fl_back:
//                PictureSelectorUtil.showPictureSelectorNoCrop(this, CommonUtil.REQ_CODE_2);
                PictureSelectorUtil.showCameraIdCardBack(this, CommonUtil.REQ_CODE_3);
                break;
            case R.id.tv_ensure:
                onEnsureClick();
                break;
        }
    }

    public void onEnsureClick() {
        if (TextUtils.isEmpty(CommonUtil.getEditText(etName))) {
            showToast("请填写你真实姓名");
            return;
        }
        if (TextUtils.isEmpty(CommonUtil.getEditText(etNumber))) {
            showToast("请填写你的身份证号");
            return;
        }
        String faceUrl = (String) flFace.getTag();
        if (TextUtils.isEmpty(faceUrl)) {
            showToast("请上传身份证正面照片");
            return;
        }
        String backUrl = (String) flBack.getTag();
        if (TextUtils.isEmpty(backUrl)) {
            showToast("请上传身份证反面照片");
            return;
        }
        showProgressDialog(getString(R.string.wait),true);
        tvEnsure.setEnabled(false);
        NetService.getInstance().identityAuth(Constants.IDENTITY_TYPE.PERSON
                , etName.getText().toString(), etNumber.getText().toString()
                , faceUrl, backUrl, null, null)
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
        if (RESULT_OK == resultCode && data != null) {
            switch (requestCode) {
                case CommonUtil.REQ_CODE_1:
                    ArrayList<String> facePath = data.getStringArrayListExtra(MultiImageSelector.EXTRA_RESULT);
                    if (facePath != null && facePath.size() > 0) {
                        uploadFace(facePath.get(0));
                    }
                    break;
                case CommonUtil.REQ_CODE_2:
                    ArrayList<String> backPath = data.getStringArrayListExtra(MultiImageSelector.EXTRA_RESULT);
                    if (backPath != null && backPath.size() > 0) {
                        uploadBack(backPath.get(0));
                    }
                    break;
                case CommonUtil.REQ_CODE_3:
                    String contentType = data.getStringExtra(CameraActivity.KEY_CONTENT_TYPE);
                    if (!TextUtils.isEmpty(contentType)) {
                        if (CameraActivity.CONTENT_TYPE_ID_CARD_FRONT.equals(contentType)) {
                            uploadFace(OCRUtil.getInstance().getFaceFile(this).getAbsolutePath());
                        } else if (CameraActivity.CONTENT_TYPE_ID_CARD_BACK.equals(contentType)) {
                            uploadBack(OCRUtil.getInstance().getBackFile(this).getAbsolutePath());
                        }
                    }
                    break;
            }
        }
    }

    private void recIDCard(String idCardSide, String filePath) {
        IDCardParams param = new IDCardParams();
        param.setImageFile(new File(filePath));
        // 设置身份证正反面
        param.setIdCardSide(idCardSide);
        // 设置方向检测
        param.setDetectDirection(true);
        // 设置图像参数压缩质量0-100, 越大图像质量越好但是请求时间越长。 不设置则默认值为20
        param.setImageQuality(20);

        OCR.getInstance(this).recognizeIDCard(param, new OnResultListener<IDCardResult>() {
            @Override
            public void onResult(IDCardResult result) {
                onRecResult(result);
            }

            @Override
            public void onError(OCRError error) {
            }
        });
    }

    private void onRecResult(IDCardResult result) {
        try {
            if (result != null && !IdentityPersonActivity.this.isFinishing()) {
                Logger.i(String.format("身份证识别结果：%s", result.toString()));
                if (tvEnsure != null) {
                    tvEnsure.post(new Runnable() {
                        @Override
                        public void run() {
                            updateRecContent(result);
                        }
                    });
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void updateRecContent(IDCardResult result) {
        if (!IdentityPersonActivity.this.isFinishing()) {
            if (etNumber != null && result.getIdNumber() != null && !TextUtils.isEmpty(result.getIdNumber().getWords())) {
                etNumber.setText(result.getIdNumber().getWords());
                etNumber.setSelection(etNumber.getText().length());
            }
            if (etName != null && result.getName() != null && !TextUtils.isEmpty(result.getName().getWords())) {
                etName.setText(result.getName().getWords());
                etName.setSelection(etName.getText().length());
            }
        }
    }

    private void uploadBack(String backPath) {
        showProgressDialog(getString(R.string.wait),true);
        NetService.getInstance().uploadFile(this, new File(backPath), new NetUpload.OnUploadCompletedListener() {
            @Override
            public void uploadSuccess(List<String> images, List<String> hash,JSONObject response) {
                LogUtils.e(images,hash,response);
                if (images != null && images.size() > 0) {
                    String backUrl = images.get(0);
                    flBack.setTag(backUrl);
                    GlideUtils.loadImage(IdentityPersonActivity.this, backUrl, imgBack);
//                    recIDCard(IDCardParams.ID_CARD_SIDE_BACK, backPath);
                    hideProgress();
                } else {
                    showToast("上传图片出错");
                    hideProgress();
                }
            }

            @Override
            public void uploadFailed(ApiException ex) {
                showToast(ex.getDisplayMessage());
                hideProgress();
            }
        }, false);
    }

    private void uploadFace(String facePath) {
        showProgressDialog(getString(R.string.wait),true);
        recIDCard(IDCardParams.ID_CARD_SIDE_FRONT, facePath);
        NetService.getInstance().uploadFile(this, new File(facePath), new NetUpload.OnUploadCompletedListener() {
            @Override
            public void uploadSuccess(List<String> images,List<String> hash,JSONObject response) {
                LogUtils.e(images,hash,response);
                if (images != null && images.size() > 0) {
                    String faceUrl = images.get(0);
                    flFace.setTag(faceUrl);
                    GlideUtils.loadImage(IdentityPersonActivity.this, faceUrl, imgFace);
                    hideProgress();
                } else {
                    showToast("上传图片出错");
                    hideProgress();
                }
            }

            @Override
            public void uploadFailed(ApiException ex) {
                showToast(ex.getDisplayMessage());
                hideProgress();
            }
        }, false);
    }

    @Override
    protected void onDestroy() {
        // 释放本地质量控制模型
        CameraNativeHelper.release();
        super.onDestroy();
    }
}

package com.kingyon.elevator.uis.actiivty2.user;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.blankj.utilcode.util.LogUtils;
import com.czh.myversiontwo.utils.EditTextUtils;
import com.kingyon.elevator.R;
import com.kingyon.elevator.entities.entities.CertifiCationEntiy;
import com.kingyon.elevator.nets.CustomApiCallback;
import com.kingyon.elevator.nets.NetService;
import com.kingyon.elevator.nets.NetUpload;
import com.kingyon.elevator.uis.actiivty2.activityutils.CameraViewActivit;
import com.leo.afbaselibrary.nets.exceptions.ApiException;
import com.leo.afbaselibrary.uis.activities.BaseActivity;
import com.leo.afbaselibrary.utils.GlideUtils;
import com.leo.afbaselibrary.utils.ToastUtils;
import com.zhihu.matisse.Matisse;


import org.json.JSONObject;

import java.io.File;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.czh.myversiontwo.utils.Constance.ACTIVITY_IDENTITY_CERTIFICATION;
import static com.kingyon.elevator.uis.actiivty2.user.CertificationActivity.certificationActivity;
import static com.kingyon.elevator.utils.utilstwo.TokenUtils.isToken;

/**
 * @Created By Admin  on 2020/6/11
 * @Email : 163235610@qq.com
 * @Author:Mrczh
 * @Instructions: 提交身份信息
 */
@Route(path = ACTIVITY_IDENTITY_CERTIFICATION)
public class IdentityCertificationActivity extends BaseActivity {
    @BindView(R.id.img_top_back)
    ImageView imgTopBack;
    @BindView(R.id.tv_top_title)
    TextView tvTopTitle;
    @BindView(R.id.tv_right)
    TextView tvRight;
    @BindView(R.id.et_name)
    EditText etName;
    @BindView(R.id.et_number)
    EditText etNumber;
    @BindView(R.id.img_idcard)
    ImageView imgIdcard;
    @BindView(R.id.tv_rz)
    TextView tvRz;
    String idCardPic = "";
    Uri mUri;
    @Override
    public int getContentViewId() {
        return R.layout.activity_identity_certification;
    }

    @Override
    public void init(Bundle savedInstanceState) {
        EditTextUtils.setEditTextInhibitInputSpace(etName);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }

    @OnClick({R.id.img_top_back, R.id.img_idcard, R.id.tv_rz})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.img_top_back:
                finish();
                break;
            case R.id.img_idcard:
                /*打开相机拍照*/
                startActivityForResult(CameraViewActivit.class, 101);
                break;
            case R.id.tv_rz:
                httpSubmit();
                break;
        }
    }
    private void httpSubmit() {
        if (etName.getText().toString().isEmpty()||etNumber.getText().toString().isEmpty()){
            ToastUtils.showToast(this,"姓名身份证号不能为空",1000);
        }else if (idCardPic.isEmpty()){
            ToastUtils.showToast(this,"请上传手持身份照",1000);
        }else {
            tvRz.setClickable(false);
            showProgressDialog(getString(R.string.wait),true);
            NetService.getInstance().setIdentyAuth(etName.getText().toString(),etNumber.getText().toString(),idCardPic,"CUSTOMER")
                    .compose(this.bindLifeCycle())
                    .subscribe(new CustomApiCallback<CertifiCationEntiy>() {
                        @Override
                        protected void onResultError(ApiException ex) {
                            hideProgress();
                            LogUtils.e(ex.getDisplayMessage(), ex.getCode());
                            ToastUtils.showToast(IdentityCertificationActivity.this,ex.getDisplayMessage(),1000);
                            tvRz.setClickable(true);
                        }
                        @Override
                        public void onNext(CertifiCationEntiy s) {
                            tvRz.setClickable(true);
                            hideProgress();

                            ToastUtils.showToast(IdentityCertificationActivity.this,"提交成功",1000);
                            finish();
                            certificationActivity.finish();
                        }
                    });
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //判断返回码不等于0
        if (requestCode != RESULT_CANCELED){
            switch (requestCode){
                case 101:
                    try {
                        String path =data.getStringExtra("path");
                        showProgressDialog(getString(R.string.wait),true);
                        NetService.getInstance().uploadFile(this, new File(path), new NetUpload.OnUploadCompletedListener() {
                            @Override
                            public void uploadSuccess(List<String> images,List<String> hash,JSONObject response) {
                                hideProgress();
                                if (images != null && images.size() > 0) {
                                    idCardPic = images.get(0);
                                    GlideUtils.loadImage(IdentityCertificationActivity.this,idCardPic,imgIdcard);
                                } else {
                                    hideProgress();
                                    showToast("上传失败");
                                }
                            }
                            @Override
                            public void uploadFailed(ApiException ex) {
                                hideProgress();
                                showToast("上传图片失败");
                            }
                        }, false);
                    }catch (Exception e){
                        LogUtils.e(e.toString());
                    }
                    break;
            }
        }
    }

}

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
import com.kingyon.elevator.R;
import com.kingyon.elevator.nets.CustomApiCallback;
import com.kingyon.elevator.nets.NetService;
import com.kingyon.elevator.uis.actiivty2.activityutils.CameraViewActivit;
import com.leo.afbaselibrary.nets.exceptions.ApiException;
import com.leo.afbaselibrary.uis.activities.BaseActivity;
import com.leo.afbaselibrary.utils.ToastUtils;


import java.io.File;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.czh.myversiontwo.utils.Constance.ACTIVITY_IDENTITY_CERTIFICATION;

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
    String idCardPic;
    Uri mUri;
    @Override
    public int getContentViewId() {
        return R.layout.activity_identity_certification;
    }

    @Override
    public void init(Bundle savedInstanceState) {


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
//                takePhoto();
                startActivityForResult(CameraViewActivit.class, 101);
                break;
            case R.id.tv_rz:
                httpSubmit();
                break;
        }
    }
    private void takePhoto() {
        // 步骤一：创建存储照片的文件
        String path = getFilesDir() + File.separator + "images" + File.separator;
        File file = new File(path, "test.jpg");
        if(!file.getParentFile().exists())
            file.getParentFile().mkdirs();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            //步骤二：Android 7.0及以上获取文件 Uri
             mUri = FileProvider.getUriForFile(IdentityCertificationActivity.this, "com.kingyon.elevator.provider", file);
        } else {
            //步骤三：获取文件Uri
            mUri = Uri.fromFile(file);
        }
        //步骤四：调取系统拍照
        Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
        intent.putExtra(MediaStore.EXTRA_OUTPUT, mUri);
        startActivityForResult(intent, 101);
    }
    private void httpSubmit() {
        if (etName.getText().toString().isEmpty()||etNumber.getText().toString().isEmpty()){
            ToastUtils.showToast(this,"姓名身份证号不能为空",1000);
        }else {
            NetService.getInstance().setIdentyAuth(etName.getText().toString(),etNumber.getText().toString(),"","")
                    .compose(this.bindLifeCycle())
                    .subscribe(new CustomApiCallback<String>() {
                        @Override
                        protected void onResultError(ApiException ex) {
                            LogUtils.e(ex.getDisplayMessage(), ex.getCode());
                        }

                        @Override
                        public void onNext(String s) {
                            ToastUtils.showToast(IdentityCertificationActivity.this,"提交成功",1000);
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
//                    File tempFile = new File(Environment.getExternalStorageDirectory(),"fileImg.jpg");
                    String path =data.getDataString().concat("path");
                    LogUtils.e(path);
                    break;
            }
        }
    }

}

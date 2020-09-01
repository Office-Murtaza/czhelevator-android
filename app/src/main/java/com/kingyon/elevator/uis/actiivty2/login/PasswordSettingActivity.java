package com.kingyon.elevator.uis.actiivty2.login;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.acker.simplezxing.activity.CaptureActivity;
import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.blankj.utilcode.util.LogUtils;
import com.kingyon.elevator.R;
import com.kingyon.elevator.utils.CommonUtil;
import com.kingyon.elevator.utils.FormatUtils;
import com.kingyon.elevator.utils.utilstwo.OrdinaryActivity;
import com.kingyon.elevator.utils.utilstwo.TokenUtils;
import com.leo.afbaselibrary.uis.activities.BaseActivity;
import com.leo.afbaselibrary.utils.ToastUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.czh.myversiontwo.utils.Constance.ACTIVITY_MAIN2_PASSSWORD_SETTING;

/**
 * Created By Admin  on 2020/4/9
 * Email : 163235610@qq.com
 * Author:Mrczh
 * Instructions: 密码设置
 */
@Route(path = ACTIVITY_MAIN2_PASSSWORD_SETTING)
public class PasswordSettingActivity extends BaseActivity {
    @BindView(R.id.img_top_back)
    ImageView imgTopBack;
    @BindView(R.id.tv_top_title)
    TextView tvTopTitle;
    @BindView(R.id.tv_right)
    TextView tvRight;
    @BindView(R.id.tv_dl)
    TextView tvDl;
    @BindView(R.id.ed_password)
    EditText edPassword;
    @BindView(R.id.img_password)
    ImageView imgPassword;
    @BindView(R.id.ll_sjh)
    LinearLayout llSjh;
    @BindView(R.id.ed_recommended_code)
    EditText edRecommendedCode;
    @BindView(R.id.img_sweep)
    ImageView imgSweep;
    @BindView(R.id.tv_login_next)
    TextView tvLoginNext;
    boolean isdisplay = false;

    @Autowired
    String phone;

    String thisPhone;

    @Override
    public int getContentViewId() {
        return R.layout.activity_passwordsetting;
    }

    @Override
    public void init(Bundle savedInstanceState) {
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        ARouter.getInstance().inject(this);
        thisPhone = phone;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);

    }

    @OnClick({R.id.img_top_back, R.id.img_password, R.id.img_sweep, R.id.tv_login_next})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.img_top_back:
                finish();
                break;
            case R.id.img_password:
                if (isdisplay){
                    isdisplay = false;
                    edPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    imgPassword.setImageResource(R.mipmap.ic_login_password_on);
                    edPassword.setSelection(edPassword.getText().toString().length());
                }else {
                    isdisplay = true;
                    edPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    imgPassword.setImageResource(R.mipmap.ic_login_pasword_off);
                    edPassword.setSelection(edPassword.getText().toString().length());
                }
                break;
            case R.id.img_sweep:
                String[] perms = {Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA};
                checkPermission(new BaseActivity.CheckPermListener() {
                    @Override
                    public void agreeAllPermission() {
                        Intent intent = new Intent(PasswordSettingActivity.this, CaptureActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivityForResult(intent, CommonUtil.REQ_CODE_7);
                    }
                }, "扫描二维码需要以下权限", perms);

                break;
            case R.id.tv_login_next:
                httpPasswordSetting(thisPhone,edPassword.getText().toString(),edRecommendedCode.getText().toString());
                break;
        }
    }

    private void httpPasswordSetting(String thisPhone,String password,String recommend) {
        if (!password.isEmpty()) {
            if (password.length() < 6) {
                ToastUtils.showToast(this, "密码最少六位", 1000);
            } else {
                OrdinaryActivity.httpPasswordSetting(this, thisPhone, password, recommend);
            }
        }else {
            ToastUtils.showToast(this, "请输入密码", 1000);
        }

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (RESULT_OK == resultCode && data != null) {
            switch (requestCode) {
                case CommonUtil.REQ_CODE_7:
                    edRecommendedCode.setText("");
                    String deviceStr = data.getStringExtra(CaptureActivity.EXTRA_SCAN_RESULT);
                    String deviceNo = FormatUtils.getInstance().getDeviceNo(deviceStr);
                    LogUtils.e(deviceStr,deviceNo);
                    if (!TextUtils.isEmpty(deviceStr)) {
                        if (isNumeric(deviceStr)&&deviceStr.length()==6){
                            edRecommendedCode.setText(deviceStr);
                            showToast("获取推荐码成功");
                        }else {
                            showToast("请检查推荐码是否正确");
                        }
                    } else {
                        showToast("没有获取到推荐码");
                    }
                    break;
            }
        }
    }
    public boolean isNumeric(String str){
        Pattern pattern = Pattern.compile("[0-9]*");
        Matcher isNum = pattern.matcher(str);
        if( !isNum.matches() ){
            return false;
        }
        return true;
    }
}

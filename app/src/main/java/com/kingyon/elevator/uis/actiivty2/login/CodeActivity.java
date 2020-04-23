package com.kingyon.elevator.uis.actiivty2.login;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.blankj.utilcode.util.LogUtils;
import com.kingyon.elevator.R;
import com.leo.afbaselibrary.uis.activities.BaseActivity;
import com.luozm.captcha.Captcha;
import com.zhaoss.weixinrecorded.view.PhoneCode;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created By Admin  on 2020/4/10
 * Email : 163235610@qq.com
 * Author:Mrczh
 * Instructions:验证码
 */
public class CodeActivity extends BaseActivity {
    @BindView(R.id.img_top_back)
    ImageView imgTopBack;
    @BindView(R.id.tv_top_title)
    TextView tvTopTitle;
    @BindView(R.id.tv_dl)
    TextView tvDl;
    @BindView(R.id.tv_code_phone)
    TextView tvCodePhone;
    @BindView(R.id.tv_sendcode)
    TextView tvSendcode;
    @BindView(R.id.ll_sjh)
    LinearLayout llSjh;
    @BindView(R.id.tv_number1)
    TextView tvNumber1;
    @BindView(R.id.tv_number2)
    TextView tvNumber2;
    @BindView(R.id.tv_number3)
    TextView tvNumber3;
    @BindView(R.id.tv_number4)
    TextView tvNumber4;
    @BindView(R.id.tv_number5)
    TextView tvNumber5;
    @BindView(R.id.tv_number6)
    TextView tvNumber6;
    @BindView(R.id.tv_code_next)
    TextView tvCodeNext;
    @BindView(R.id.pc_1)
    PhoneCode pc1;
    @BindView(R.id.captCha)
    Captcha captcha;

    @Override
    public int getContentViewId() {
        return R.layout.activity_code;
    }

    @Override
    public void init(Bundle savedInstanceState) {
        pc1.setOnInputListener(new PhoneCode.OnInputListener() {
            @Override
            public void onSucess(String code) {
                LogUtils.e(code);
            }

            @Override
            public void onInput() {
                LogUtils.e("未完成");
            }
        });

        captcha.setBitmap(R.mipmap.ic_launcher);
        captcha.setCaptchaListener(new Captcha.CaptchaListener() {
            @Override
            public String onAccess(long time) {
                Toast.makeText(CodeActivity.this, "验证成功", Toast.LENGTH_SHORT).show();
                return "验证通过,耗时" + time + "毫秒";
            }

            @Override
            public String onFailed(int failedCount) {
                Toast.makeText(CodeActivity.this, "验证失败", Toast.LENGTH_SHORT).show();
                captcha.reset(true);
                return "验证失败,已失败" + failedCount + "次";
            }

            @Override
            public String onMaxFailed() {
                Toast.makeText(CodeActivity.this, "验证超过次数，你的帐号被封锁", Toast.LENGTH_SHORT).show();
                return "验证失败,帐号已封锁";
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }

    @OnClick({R.id.img_top_back, R.id.tv_sendcode, R.id.tv_code_next})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.img_top_back:
                break;
            case R.id.tv_sendcode:
                break;
            case R.id.tv_code_next:
                String phoneCode = pc1.getPhoneCode();
                LogUtils.e(phoneCode);
                break;
        }
    }
}

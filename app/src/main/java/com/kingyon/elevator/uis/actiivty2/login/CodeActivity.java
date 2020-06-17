package com.kingyon.elevator.uis.actiivty2.login;

import android.app.ActionBar;
import android.app.AlertDialog;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.kingyon.elevator.R;
import com.kingyon.elevator.utils.utilstwo.CountDownTimerUtils;
import com.kingyon.elevator.utils.utilstwo.OrdinaryActivity;
import com.leo.afbaselibrary.uis.activities.BaseActivity;
import com.luozm.captcha.Captcha;
import com.zhaoss.weixinrecorded.view.PhoneCode;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.czh.myversiontwo.utils.CodeType.CODE;
import static com.czh.myversiontwo.utils.CodeType.REGISTER;
import static com.czh.myversiontwo.utils.Constance.ACTIVITY_MAIN2_CODE;

/**
 * Created By Admin  on 2020/4/10
 * Email : 163235610@qq.com
 * Author:Mrczh
 * Instructions:验证码
 */
@Route(path = ACTIVITY_MAIN2_CODE)
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
    @Autowired
    String phone;
    @Autowired
    String unique;
    @Autowired
    String avatar;
    @Autowired
    String nickName;
    @Autowired
    String isbinding;
    @Autowired
    String loginType;


    @Override
    public int getContentViewId() {
        return R.layout.activity_code;
    }

    @Override
    public void init(Bundle savedInstanceState) {
        ARouter.getInstance().inject(this);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        CountDownTimerUtils mCountDownTimerUtils = new CountDownTimerUtils(tvSendcode, 60000, 1000);
        mCountDownTimerUtils.start();

        tvCodePhone.setText("+86  "+phone);

        pc1.setOnInputListener(new PhoneCode.OnInputListener() {
            @Override
            public void onSucess(String code) {
                tvCodeNext.setBackgroundResource(R.mipmap.btn_common_normal);
                tvCodeNext.setClickable(true);
            }

            @Override
            public void onInput() {
                tvCodeNext.setBackgroundResource(R.mipmap.btn_common_dislabled);
                tvCodeNext.setClickable(false);
            }
        });


    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
        OrdinaryActivity.codeActivity = this;
    }

    @OnClick({R.id.img_top_back, R.id.tv_sendcode, R.id.tv_code_next})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.img_top_back:
                finish();
                break;
            case R.id.tv_sendcode:
                httpCode();
                break;
            case R.id.tv_code_next:
                String phoneCode = pc1.getPhoneCode();
                if (isbinding.equals("1")) {
                    OrdinaryActivity.httpLogin(this, phone, phoneCode, CODE, unique, avatar, nickName, null, null);
                }else {
                    OrdinaryActivity.httpCheckVerifyCode(this,phone,phoneCode, unique, avatar, nickName,loginType);
                }
                break;
        }
    }

    private void httpCode() {
        final AlertDialog alertDialog = new AlertDialog.Builder(this).create();
        alertDialog.show();
        /* 添加对话框自定义布局 */
        alertDialog.setContentView(R.layout.layout_captcha);
        /* 获取对话框窗口 */
        Window window = alertDialog.getWindow();
        /* 设置显示窗口的宽高 */
        window.setLayout(ActionBar.LayoutParams.MATCH_PARENT, ActionBar.LayoutParams.WRAP_CONTENT);
        /* 设置窗口显示位置 */
        window.setGravity(Gravity.CENTER);
        Captcha captcha  = window.findViewById(R.id.captCha);
        captcha.setBitmap(R.mipmap.ic_launcher);
        captcha.setCaptchaListener(new Captcha.CaptchaListener() {
            @Override
            public String onAccess(long time) {
//                Toast.makeText(CodeActivity.this, "验证成功", Toast.LENGTH_SHORT).show();
                alertDialog.dismiss();
                OrdinaryActivity.CodeTextviewActivity(CodeActivity.this,REGISTER,phone,tvSendcode);
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
}

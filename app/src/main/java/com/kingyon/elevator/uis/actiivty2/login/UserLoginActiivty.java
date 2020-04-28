package com.kingyon.elevator.uis.actiivty2.login;

import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.kingyon.elevator.R;
import com.kingyon.elevator.utils.utilstwo.OrdinaryActivity;
import com.leo.afbaselibrary.uis.activities.BaseActivity;
import com.leo.afbaselibrary.utils.ToastUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.czh.myversiontwo.utils.CodeType.CODE;
import static com.czh.myversiontwo.utils.CodeType.NOR;
import static com.czh.myversiontwo.utils.Constance.ACTIVITY_MAIN2_CODE;
import static com.czh.myversiontwo.utils.Constance.ACTIVITY_MAIN2_FORGET_PASSWORD;
import static com.czh.myversiontwo.utils.Constance.ACTIVITY_MAIN2_USER_LOGIN;

/**
 * Created By Admin  on 2020/4/9
 * Email : 163235610@qq.com
 * Author:Mrczh
 * Instructions:账户登录
 */
@Route(path = ACTIVITY_MAIN2_USER_LOGIN)
public class UserLoginActiivty extends BaseActivity {
    @BindView(R.id.img_top_back)
    ImageView imgTopBack;
    @BindView(R.id.tv_top_title)
    TextView tvTopTitle;
    @BindView(R.id.tv_right)
    TextView tvRight;
    @BindView(R.id.tv_dl)
    TextView tvDl;
    @BindView(R.id.ed_phon)
    EditText edPhon;
    @BindView(R.id.ll_sjh)
    LinearLayout llSjh;
    @BindView(R.id.ed_password)
    EditText edPassword;
    @BindView(R.id.tv_porgot_password)
    TextView tvPorgotPassword;
    @BindView(R.id.tv_login_next)
    TextView tvLoginNext;

    @Override
    public int getContentViewId() {
        return R.layout.activity_userlogin;
    }

    @Override
    public void init(Bundle savedInstanceState) {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        ButterKnife.bind(this);
        OrdinaryActivity.userLoginActiivty = this;
    }

    @OnClick({R.id.img_top_back, R.id.tv_login_next,R.id.tv_porgot_password})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.img_top_back:
                finish();
                break;
            case R.id.tv_login_next:
                httpUserLogin( edPhon.getText().toString(),
                        edPassword.getText().toString());

                break;
            case R.id.tv_porgot_password:
                ARouter.getInstance().build(ACTIVITY_MAIN2_FORGET_PASSWORD).navigation();
                break;
        }
    }

    private void httpUserLogin(String phon, String password) {
        if (phon.length()<11){
            ToastUtils.showToast(this,"手机号错误",1000);
        }else if (password.length()<6){
            ToastUtils.showToast(this,"密码需要大于6位",1000);
        }else {
            OrdinaryActivity.httpLogin(UserLoginActiivty.this, phon, password, NOR, "","","",null,null);
        }

    }
}

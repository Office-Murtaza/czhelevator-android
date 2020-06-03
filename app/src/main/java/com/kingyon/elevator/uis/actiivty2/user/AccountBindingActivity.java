package com.kingyon.elevator.uis.actiivty2.user;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.kingyon.elevator.R;
import com.leo.afbaselibrary.uis.activities.BaseActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.czh.myversiontwo.utils.Constance.ACTIVITY_ACCOUNT_BINDING;

/**
 * @Created By Admin  on 2020/6/3
 * @Email : 163235610@qq.com
 * @Author:Mrczh
 * @Instructions:绑定账户
 */
@Route(path = ACTIVITY_ACCOUNT_BINDING)
public class AccountBindingActivity extends BaseActivity {


    @BindView(R.id.img_top_back)
    ImageView imgTopBack;
    @BindView(R.id.tv_top_title)
    TextView tvTopTitle;
    @BindView(R.id.tv_right)
    TextView tvRight;
    @BindView(R.id.tv_phone)
    TextView tvPhone;
    @BindView(R.id.ll_phone)
    LinearLayout llPhone;
    @BindView(R.id.tv_wx)
    TextView tvWx;
    @BindView(R.id.ll_wx)
    LinearLayout llWx;
    @BindView(R.id.tv_pay)
    TextView tvPay;
    @BindView(R.id.ll_pay)
    LinearLayout llPay;
    @BindView(R.id.tv_qq)
    TextView tvQq;
    @BindView(R.id.ll_qq)
    LinearLayout llQq;

    @Override
    public int getContentViewId() {
        return R.layout.activity_account_binding;
    }

    @Override
    public void init(Bundle savedInstanceState) {
        tvTopTitle.setText("账户绑定");
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }

    @OnClick({R.id.img_top_back, R.id.ll_phone, R.id.ll_wx, R.id.ll_pay, R.id.ll_qq})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.img_top_back:
                finish();
                break;
            case R.id.ll_phone:
                break;
            case R.id.ll_wx:
                break;
            case R.id.ll_pay:
                break;
            case R.id.ll_qq:
                break;
        }
    }
}

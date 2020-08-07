package com.kingyon.elevator.uis.activities.user;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.czh.myversiontwo.activity.ActivityUtils;
import com.kingyon.elevator.R;
import com.kingyon.elevator.constants.Constants;
import com.kingyon.elevator.entities.entities.AuthStatusEntily;
import com.kingyon.elevator.nets.CustomApiCallback;
import com.kingyon.elevator.nets.NetService;
import com.leo.afbaselibrary.nets.exceptions.ApiException;
import com.leo.afbaselibrary.uis.activities.BaseActivity;
import com.leo.afbaselibrary.utils.ToastUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.czh.myversiontwo.utils.Constance.ACTIVITY_CERTIFICATION;
import static com.czh.myversiontwo.utils.Constance.IDENTITY_SUCCESS_ACTIVITY;

/**
 * Created by GongLi on 2019/1/9.
 * Email：lc824767150@163.com
 */
@Route(path = IDENTITY_SUCCESS_ACTIVITY)
public class IdentitySuccessActivity extends BaseActivity {
    @BindView(R.id.img_top_back)
    ImageView imgTopBack;
    @BindView(R.id.tv_top_title)
    TextView tvTopTitle;
    @BindView(R.id.tv_right)
    TextView tvRight;
    @BindView(R.id.img_icon)
    ImageView imgIcon;
    @BindView(R.id.tv_top)
    TextView tvTop;
    @BindView(R.id.tv_was)
    TextView tvWas;
    @BindView(R.id.tv_zt)
    TextView tvZt;
    @BindView(R.id.tv_xd)
    TextView tvXd;
    @BindView(R.id.tv_name)
    TextView tvName;
    @BindView(R.id.ll_name)
    LinearLayout llName;
    @BindView(R.id.tv_next)
    TextView tvNext;
    @Autowired
    String type;

    @Override
    public int getContentViewId() {
        return R.layout.activity_identity_success;
    }

    @Override
    public void init(Bundle savedInstanceState) {
        ARouter.getInstance().inject(this);
        if (getIntent().getStringExtra("type")!=null) {
            type = getIntent().getStringExtra("type");
        }
        showProgressDialog(getString(R.string.wait),true);
        NetService.getInstance().getAuthStatus()
                .compose(this.bindLifeCycle())
                .subscribe(new CustomApiCallback<AuthStatusEntily>() {
                    @Override
                    protected void onResultError(ApiException ex) {
                        hideProgress();
                        ToastUtils.showToast(IdentitySuccessActivity.this,ex.getDisplayMessage(),1000);

                    }

                    @Override
                    public void onNext(AuthStatusEntily authStatusEntily) {

                        switch (type) {
                            case Constants.IDENTITY_STATUS.AUTHED:
                                tvNext.setVisibility(View.GONE);
                                tvTop.setText("个人认证已通过");
                                tvTop.setTextColor(Color.parseColor("#2D6EF2"));
                                tvWas.setText("认证方式");
                                if (authStatusEntily.authType.equals("ALI")){
                                    tvZt.setText("支付宝");
                                }else {
                                    tvZt.setText("个人身份证");
                                }
                                tvName.setText(authStatusEntily.fullname);
                                break;
                            case Constants.IDENTITY_STATUS.FAILD:
                                tvNext.setVisibility(View.VISIBLE);
                                llName.setVisibility(View.GONE);
                                tvXd.setVisibility(View.GONE);
                                imgIcon.setImageResource(R.mipmap.ic_cashout_apply);
                                tvTop.setText("个人认证失败！");
                                tvWas.setText("失败原因");
                                tvTop.setTextColor(Color.parseColor("#FFA84A"));
                                tvZt.setText(authStatusEntily.rejected);
                                break;
                             case Constants.IDENTITY_STATUS.AUTHING:
                                 tvNext.setVisibility(View.VISIBLE);
                                 llName.setVisibility(View.GONE);
                                 tvXd.setVisibility(View.GONE);
                                 imgIcon.setImageResource(R.mipmap.ic_attest_continue);
                                 tvTop.setText("个人认证审核中！");
                                 tvWas.setText("预计需要5个工作日，请耐心等待~");
                                 tvTop.setTextColor(Color.parseColor("#6BAB44"));
                                 tvZt.setVisibility(View.GONE);
                                 tvNext.setVisibility(View.GONE);
                                 break;
                        }
                        hideProgress();
                    }
                });

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }


    @OnClick({R.id.img_top_back, R.id.tv_next})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.img_top_back:
                finish();
                break;
            case R.id.tv_next:
                ActivityUtils.setActivity(ACTIVITY_CERTIFICATION);
                finish();
                break;
        }
    }

}

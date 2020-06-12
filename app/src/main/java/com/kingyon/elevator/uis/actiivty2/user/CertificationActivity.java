package com.kingyon.elevator.uis.actiivty2.user;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.czh.myversiontwo.activity.ActivityUtils;
import com.kingyon.elevator.R;
import com.kingyon.elevator.constants.Constants;
import com.kingyon.elevator.entities.IdentityInfoEntity;
import com.kingyon.elevator.nets.CustomApiCallback;
import com.kingyon.elevator.nets.NetService;
import com.kingyon.elevator.uis.activities.user.IdentityPersonActivity;
import com.kingyon.elevator.uis.activities.user.IdentitySuccessActivity;
import com.leo.afbaselibrary.nets.exceptions.ApiException;
import com.leo.afbaselibrary.nets.exceptions.ResultException;
import com.leo.afbaselibrary.uis.activities.BaseActivity;
import com.leo.afbaselibrary.utils.GlideUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.czh.myversiontwo.utils.Constance.ACTIVITY_CERTIFICATION;
import static com.czh.myversiontwo.utils.Constance.ACTIVITY_IDENTITY_CERTIFICATION;
import static com.czh.myversiontwo.utils.Constance.ACTIVITY_PAYTREASURECERT;

/**
 * @Created By Admin  on 2020/6/11
 * @Email : 163235610@qq.com
 * @Author:Mrczh
 * @Instructions: 认证选择
 */
@Route(path = ACTIVITY_CERTIFICATION)
public class CertificationActivity extends BaseActivity {
    @BindView(R.id.img_top_back)
    ImageView imgTopBack;
    @BindView(R.id.tv_top_title)
    TextView tvTopTitle;
    @BindView(R.id.tv_right)
    TextView tvRight;
    @BindView(R.id.ll_zfb)
    LinearLayout llZfb;
    @BindView(R.id.ll_sfz)
    LinearLayout llSfz;
    private boolean jumping;
    @Override
    public int getContentViewId() {
        return R.layout.activity_certification;
    }

    @Override
    public void init(Bundle savedInstanceState) {
        NetService.getInstance().getIdentityInformation()
                .compose(this.<IdentityInfoEntity>bindLifeCycle())
                .subscribe(new CustomApiCallback<IdentityInfoEntity>() {
                    @Override
                    protected void onResultError(ApiException ex) {
                        showToast(ex.getDisplayMessage());
                        switch (ex.getCode()) {
                            case 9002:
                                if (!jumping) {
                                    jumping = true;
                                    new Thread() {
                                        @Override
                                        public void run() {
                                            super.run();
                                            try {
                                                Thread.sleep(1000);//休眠3秒

                                                finish();
                                            } catch (InterruptedException e) {
                                                e.printStackTrace();
                                            }
                                        }
                                    }.start();

                                }
                                break;
                            case 9003:
                                if (!jumping) {
                                    jumping = true;
                                    new Thread() {
                                        @Override
                                        public void run() {
                                            super.run();
                                            try {
                                                Thread.sleep(1000);//休眠3秒
                                                startActivity(IdentitySuccessActivity.class);
                                                    finish();
                                            } catch (InterruptedException e) {
                                                e.printStackTrace();
                                            }
                                        }
                                    }.start();
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
                    }
                });

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }

    @OnClick({R.id.img_top_back, R.id.ll_zfb, R.id.ll_sfz})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.img_top_back:
                finish();
                break;
            case R.id.ll_zfb:
                ActivityUtils.setActivity(ACTIVITY_PAYTREASURECERT);
                break;
            case R.id.ll_sfz:
                ActivityUtils.setActivity(ACTIVITY_IDENTITY_CERTIFICATION);
                break;
        }
    }
}

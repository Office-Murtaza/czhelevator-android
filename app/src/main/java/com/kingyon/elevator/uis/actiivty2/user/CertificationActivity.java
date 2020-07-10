package com.kingyon.elevator.uis.actiivty2.user;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.blankj.utilcode.util.LogUtils;
import com.czh.myversiontwo.activity.ActivityUtils;
import com.kingyon.elevator.R;
import com.kingyon.elevator.constants.Constants;
import com.kingyon.elevator.entities.IdentityInfoEntity;
import com.kingyon.elevator.nets.CustomApiCallback;
import com.kingyon.elevator.nets.NetService;
import com.kingyon.elevator.uis.activities.user.IdentitySuccessActivity;
import com.leo.afbaselibrary.nets.exceptions.ApiException;
import com.leo.afbaselibrary.nets.exceptions.ResultException;
import com.leo.afbaselibrary.uis.activities.BaseActivity;

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
    public static CertificationActivity certificationActivity;

    @Override
    public int getContentViewId() {
        return R.layout.activity_certification;
    }

    @Override
    public void init(Bundle savedInstanceState) {
        certificationActivity = this;
        tvTopTitle.setText("资质认证");
        NetService.getInstance().getIdentityInformation()
                .compose(this.<IdentityInfoEntity>bindLifeCycle())
                .subscribe(new CustomApiCallback<IdentityInfoEntity>() {
                    @Override
                    protected void onResultError(ApiException ex) {
                        if (ex.getCode()!=-102){
                            showToast(ex.getDisplayMessage());
                        }
                    }
                    @Override
                    public void onNext(IdentityInfoEntity identityInfoEntity) {
                        LogUtils.e(identityInfoEntity.getStatus());
                        if (identityInfoEntity == null) {
                            throw new ResultException(9001, "返回参数异常");
                        }
                        if (TextUtils.equals(Constants.IDENTITY_STATUS.AUTHING, identityInfoEntity.getStatus())) {
//                            throw new ResultException(9002, "新的认证资料审核中，即将跳转");
                            Bundle bundle = new Bundle();
                            bundle.putString("type",Constants.IDENTITY_STATUS.AUTHING);
                            startActivity(IdentitySuccessActivity.class,bundle);
                            finish();
                        }
//                        if (TextUtils.equals(Constants.IDENTITY_STATUS.AUTHED, identityInfoEntity.getStatus())) {
////                            throw new ResultException(9003, "新的认证资料已通过，即将跳转");
//                            Bundle bundle = new Bundle();
//                            bundle.putString("type",Constants.IDENTITY_STATUS.AUTHED);
//                            startActivity(IdentitySuccessActivity.class,bundle);
//                            finish();
//                        }
//                        if (TextUtils.equals(Constants.IDENTITY_STATUS.FAILD, identityInfoEntity.getStatus())) {
////                            throw new ResultException(9004, "新的认证失败，即将跳转");
//                            Bundle bundle = new Bundle();
//                            bundle.putString("type",Constants.IDENTITY_STATUS.FAILD);
//                            startActivity(IdentitySuccessActivity.class,bundle);
//                        }
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

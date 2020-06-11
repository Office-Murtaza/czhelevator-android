package com.kingyon.elevator.uis.actiivty2.user;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.czh.myversiontwo.activity.ActivityUtils;
import com.kingyon.elevator.R;
import com.leo.afbaselibrary.uis.activities.BaseActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.czh.myversiontwo.utils.Constance.ACTIVITY_CERTIFICATION;
import static com.czh.myversiontwo.utils.Constance.ACTIVITY_IDENTITY_CERTIFICATION;

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

    @Override
    public int getContentViewId() {
        return R.layout.activity_certification;
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

    @OnClick({R.id.img_top_back, R.id.ll_zfb, R.id.ll_sfz})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.img_top_back:
                finish();
                break;
            case R.id.ll_zfb:

                break;
            case R.id.ll_sfz:
                ActivityUtils.setActivity(ACTIVITY_IDENTITY_CERTIFICATION);
                break;
        }
    }
}

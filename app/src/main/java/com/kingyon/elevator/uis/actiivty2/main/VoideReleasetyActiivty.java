package com.kingyon.elevator.uis.actiivty2.main;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.kingyon.elevator.R;
import com.kingyon.elevator.mvpbase.MvpBaseActivity;
import com.kingyon.elevator.presenter.presenter2.VoideReleasetyPresenter;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.czh.myversiontwo.utils.Constance.ACTIVITY_MAIN2_VOIDE_RELEASETY;

/**
 * @Created By Admin  on 2020/4/20
 * @Email : 163235610@qq.com
 * @Author:Mrczh 2765740671
 * @Instructions: 视频发布
 */
@Route(path = ACTIVITY_MAIN2_VOIDE_RELEASETY)
public class VoideReleasetyActiivty extends MvpBaseActivity<VoideReleasetyPresenter> {
    @BindView(R.id.img_bake)
    ImageView imgBake;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.tv_releaset)
    TextView tvReleaset;
    @BindView(R.id.img_icon)
    ImageView imgIcon;
    @BindView(R.id.img_aite)
    ImageView imgAite;
    @BindView(R.id.img_huati)
    ImageView imgHuati;
    @BindView(R.id.tv_zishu)
    TextView tvZishu;
    @BindView(R.id.ll_bq)
    LinearLayout llBq;

    @Override
    public VoideReleasetyPresenter initPresenter() {
        return new VoideReleasetyPresenter(this);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_voide_releasty);
        ButterKnife.bind(this);
    }


    @OnClick({R.id.img_bake, R.id.tv_releaset, R.id.img_icon, R.id.img_aite, R.id.img_huati})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.img_bake:
                finish();
                break;
            case R.id.tv_releaset:

                break;
            case R.id.img_icon:

                break;
            case R.id.img_aite:

                break;
            case R.id.img_huati:

                break;

                default:
        }
    }
}

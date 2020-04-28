package com.kingyon.elevator.uis.actiivty2.activityutils;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.kingyon.elevator.R;
import com.kingyon.elevator.mvpbase.MvpBaseActivity;
import com.kingyon.elevator.presenter.presenter2.VoideChoosePresenter;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.czh.myversiontwo.utils.Constance.ACTIVITY_ACTIVITYUTILS_VIDEO_CHOOSE;

/**
 * @Created By Admin  on 2020/4/24
 * @Email : 163235610@qq.com
 * @Author:Mrczh
 * @Instructions:
 */
@Route(path = ACTIVITY_ACTIVITYUTILS_VIDEO_CHOOSE)
public class VoideChooseActiivty extends MvpBaseActivity<VoideChoosePresenter> {
    @BindView(R.id.img_top_back)
    ImageView imgTopBack;
    @BindView(R.id.tv_top_title)
    TextView tvTopTitle;
    @BindView(R.id.img_image_dx)
    ImageView imgImageDx;
    @BindView(R.id.tv_right)
    TextView tvRight;
    @BindView(R.id.rcv_voide)
    RecyclerView rcvVoide;

    @Override
    public VoideChoosePresenter initPresenter() {
        return new VoideChoosePresenter(this);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_voide_choose);
        ButterKnife.bind(this);
    }

    @OnClick({R.id.img_top_back, R.id.tv_right})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.img_top_back:
                finish();
                break;
            case R.id.tv_right:

                break;
        }
    }
}

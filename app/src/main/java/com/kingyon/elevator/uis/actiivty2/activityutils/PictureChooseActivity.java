package com.kingyon.elevator.uis.actiivty2.activityutils;

import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.kingyon.elevator.R;
import com.leo.afbaselibrary.uis.activities.BaseActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * @Created By Admin  on 2020/4/23
 * @Email : 163235610@qq.com
 * @Author:Mrczh
 * @Instructions:
 */
public class PictureChooseActivity extends BaseActivity {

    @BindView(R.id.img_top_back)
    ImageView imgTopBack;
    @BindView(R.id.tv_top_title)
    TextView tvTopTitle;
    @BindView(R.id.img_image_dx)
    ImageView imgImageDx;
    @BindView(R.id.tv_right)
    TextView tvRight;
    @BindView(R.id.rcv_image)
    RecyclerView rcvImage;

    @Override
    public int getContentViewId() {
        return R.layout.activity_picture_choose;
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

    @OnClick({R.id.img_top_back, R.id.tv_top_title, R.id.img_image_dx, R.id.tv_right})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.img_top_back:

                break;
            case R.id.tv_top_title:

                break;
            case R.id.img_image_dx:

                break;
            case R.id.tv_right:

                break;
        }
    }
}

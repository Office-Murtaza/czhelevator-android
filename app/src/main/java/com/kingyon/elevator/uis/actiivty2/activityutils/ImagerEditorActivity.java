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
 * @Created By Admin  on 2020/4/30
 * @Email : 163235610@qq.com
 * @Author:Mrczh
 * @Instructions:图片编辑
 */
public class ImagerEditorActivity extends BaseActivity {

    @BindView(R.id.img_top_back)
    ImageView imgTopBack;
    @BindView(R.id.tv_top_title)
    TextView tvTopTitle;
    @BindView(R.id.tv_right)
    TextView tvRight;
    @BindView(R.id.rv_image)
    RecyclerView rvImage;

    @Override
    public int getContentViewId() {
        return R.layout.activity_image_editor;
    }

    @Override
    public void init(Bundle savedInstanceState) {
        tvTopTitle.setText("图片编辑");
        tvRight.setVisibility(View.VISIBLE);


    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }

    @OnClick({R.id.img_top_back, R.id.tv_right})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.img_top_back:
                finish();

                break;
            case R.id.tv_right:
                /*完成*/


                break;
        }
    }
}

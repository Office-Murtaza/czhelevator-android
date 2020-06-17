package com.kingyon.elevator.uis.activities.salesman;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.kingyon.elevator.R;
import com.leo.afbaselibrary.uis.activities.BaseActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * @Created By Admin  on 2020/6/17
 * @Email : 163235610@qq.com
 * @Author:Mrczh
 * @Instructions:添加小区内容
 */
public class AddSalesActiviry extends BaseActivity {
    @BindView(R.id.img_top_back)
    ImageView imgTopBack;
    @BindView(R.id.tv_top_title)
    TextView tvTopTitle;
    @BindView(R.id.tv_right)
    TextView tvRight;
    @BindView(R.id.et_conent)
    EditText etConent;
    @BindView(R.id.tv_add)
    TextView tvAdd;

    @Override
    public int getContentViewId() {
        return R.layout.activity_add_sales;
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

    @OnClick({R.id.img_top_back, R.id.tv_add})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.img_top_back:
                finish();
                break;
            case R.id.tv_add:


                break;
        }
    }


}

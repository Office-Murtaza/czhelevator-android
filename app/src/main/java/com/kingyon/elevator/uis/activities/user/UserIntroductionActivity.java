package com.kingyon.elevator.uis.activities.user;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.kingyon.elevator.R;
import com.kingyon.elevator.utils.utilstwo.ConentUtils;
import com.leo.afbaselibrary.uis.activities.BaseActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.czh.myversiontwo.utils.Constance.ACTIVITY_USER_INTRODUCTION;

/**
 * @Created By Admin  on 2020/7/2
 * @Email : 163235610@qq.com
 * @Author:Mrczh
 * @Instructions: 编辑简介
 */
@Route(path = ACTIVITY_USER_INTRODUCTION)
public class UserIntroductionActivity extends BaseActivity {

    @BindView(R.id.img_top_back)
    ImageView imgTopBack;
    @BindView(R.id.tv_top_title)
    TextView tvTopTitle;
    @BindView(R.id.tv_right)
    TextView tvRight;
    @BindView(R.id.tv_zinumber)
    TextView tvZinumber;
    @BindView(R.id.et_nick)
    EditText etNick;
    @BindView(R.id.tv_next)
    TextView tvNext;

    @Override
    public int getContentViewId() {
        return R.layout.activity_user_introduction;
    }

    @Override
    public void init(Bundle savedInstanceState) {
        tvTopTitle.setText("编辑资料");
        etNick.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                tvZinumber.setText(s.length()+"/140");
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
                /*确认*/
                ConentUtils.httpEidtProfile(UserIntroductionActivity.this, "",
                        "", "", "", "", etNick.getText().toString(), "", new ConentUtils.AddCollect() {
                            @Override
                            public void Collect(boolean is) {
                                if (is) {
                                    finish();
                                }
                            }
                        });
                break;
        }
    }
}

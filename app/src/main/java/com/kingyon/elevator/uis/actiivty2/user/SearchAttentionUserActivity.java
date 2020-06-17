package com.kingyon.elevator.uis.actiivty2.user;

import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.czh.myversiontwo.activity.ActivityUtils;
import com.kingyon.elevator.R;
import com.leo.afbaselibrary.uis.activities.BaseActivity;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.czh.myversiontwo.utils.Constance.ACTIVITY_SEARCH_ATTENTION_USERA;

/**
 * @Created By Admin  on 2020/6/17
 * @Email : 163235610@qq.com
 * @Author:Mrczh
 * @Instructions:搜索关注
 */
@Route(path = ACTIVITY_SEARCH_ATTENTION_USERA)
public class SearchAttentionUserActivity extends BaseActivity {
    @BindView(R.id.edit_search)
    EditText editSearch;
    @BindView(R.id.tv_bake)
    TextView tvBake;
    @BindView(R.id.rv_comment)
    RecyclerView rvComment;
    @BindView(R.id.rl_error)
    RelativeLayout rlError;
    @BindView(R.id.rl_null)
    RelativeLayout rlNull;
    @BindView(R.id.rl_notlogin)
    RelativeLayout rlNotlogin;
    @BindView(R.id.smart_refresh_layout)
    SmartRefreshLayout smartRefreshLayout;

    @Override
    public int getContentViewId() {
        return R.layout.activity_search_attention_user;
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

    @OnClick({R.id.tv_bake, R.id.rl_error, R.id.rl_notlogin})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_bake:
                finish();
                break;
            case R.id.rl_error:

                break;
            case R.id.rl_notlogin:
                ActivityUtils.setLoginActivity();
                break;
        }
    }
}

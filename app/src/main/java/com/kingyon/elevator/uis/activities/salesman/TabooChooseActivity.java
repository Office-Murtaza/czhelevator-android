package com.kingyon.elevator.uis.activities.salesman;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.blankj.utilcode.util.LogUtils;
import com.google.gson.Gson;
import com.kingyon.elevator.R;
import com.kingyon.elevator.entities.IndustryEntity;
import com.kingyon.elevator.nets.CustomApiCallback;
import com.kingyon.elevator.nets.NetService;
import com.kingyon.elevator.uis.adapters.adaptertwo.TabooChooseAdapter;
import com.kingyon.elevator.utils.CommonUtil;
import com.leo.afbaselibrary.nets.exceptions.ApiException;
import com.leo.afbaselibrary.uis.activities.BaseActivity;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * @Created By Admin  on 2020/6/18
 * @Email : 163235610@qq.com
 * @Author:Mrczh
 * @Instructions:禁忌行业选择
 */
public class TabooChooseActivity extends BaseActivity {
    @BindView(R.id.img_top_back)
    ImageView imgTopBack;
    @BindView(R.id.tv_top_title)
    TextView tvTopTitle;
    @BindView(R.id.tv_right)
    TextView tvRight;
    @BindView(R.id.rv_list)
    RecyclerView rvList;
    boolean ischoose  =false;
    List<IndustryEntity> industryEntities1;
    @Override
    public int getContentViewId() {
        return R.layout.activity_taboo_choose;
    }

    @Override
    public void init(Bundle savedInstanceState) {
        tvTopTitle.setText("禁忌行业选择");
        showProgressDialog(getString(R.string.wait),true);
        NetService.getInstance().getIndustrys()
                .compose(this.<List<IndustryEntity>>bindLifeCycle())
                .subscribe(new CustomApiCallback<List<IndustryEntity>>() {
                    @Override
                    protected void onResultError(ApiException ex) {
                        LogUtils.e(ex.getDisplayMessage());
                        hideProgress();
                    }

                    @Override
                    public void onNext(List<IndustryEntity> industryEntities) {
                        industryEntities1 = industryEntities;
                        hideProgress();
                        LogUtils.e(industryEntities.size(),industryEntities.toString());
                        TabooChooseAdapter adapter = new TabooChooseAdapter(TabooChooseActivity.this,industryEntities);
                        rvList.setAdapter(adapter);
                        rvList.setLayoutManager(new GridLayoutManager(TabooChooseActivity.this, 1, GridLayoutManager.VERTICAL, false));
                        adapter.setClick(new TabooChooseAdapter.OnItmeonClick() {
                            @Override
                            public void onClock(int position) {
                                adapter.multipleChoose(position);
                            }
                        });
                    }
                });

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
                /*确认选择*/
                Gson gson = new Gson();
                String jsonString = gson.toJson(industryEntities1);
                Intent intent = new Intent();
                intent.putExtra(CommonUtil.KEY_VALUE_1,  jsonString);
                setResult(RESULT_OK, intent);
                finish();

                break;
        }
    }
}

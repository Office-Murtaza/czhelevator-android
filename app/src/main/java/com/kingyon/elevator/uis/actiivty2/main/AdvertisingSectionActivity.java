package com.kingyon.elevator.uis.actiivty2.main;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.blankj.utilcode.util.LogUtils;
import com.czh.myversiontwo.utils.EditTextUtils;
import com.kingyon.elevator.R;
import com.kingyon.elevator.entities.entities.AdZoneEntiy;
import com.kingyon.elevator.nets.CustomApiCallback;
import com.kingyon.elevator.nets.NetService;
import com.kingyon.elevator.uis.adapters.adaptertwo.AdvertisingSectionAdapter;
import com.kingyon.elevator.utils.utilstwo.OrdinaryActivity;
import com.leo.afbaselibrary.nets.exceptions.ApiException;
import com.leo.afbaselibrary.uis.activities.BaseActivity;
import com.leo.afbaselibrary.utils.ToastUtils;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.czh.myversiontwo.utils.Constance.ACTIVITY_MAIN2_ADVERTISING;

/**
 * @Created By Admin  on 2020/4/23
 * @Email : 163235610@qq.com
 * @Author:Mrczh
 * @Instructions:广告专区
 */
@Route(path = ACTIVITY_MAIN2_ADVERTISING)
public class AdvertisingSectionActivity extends BaseActivity {
    @BindView(R.id.edit_search)
    EditText editSearch;
    @BindView(R.id.tv_bake)
    TextView tvBake;
    @BindView(R.id.rv_list)
    RecyclerView rvList;
    int page = 1;
    int rows = 15;
    String params;
    AdvertisingSectionAdapter advertisingSectionAdapter;
    List<AdZoneEntiy.DataBean> listdata = new ArrayList<>();
    @BindView(R.id.rl_error)
    RelativeLayout rlError;
    @BindView(R.id.rl_null)
    RelativeLayout rlNull;
    @BindView(R.id.smart_refresh_layout)
    SmartRefreshLayout smartRefreshLayout;
    @BindView(R.id.img_delete)
    ImageView imgDelete;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
        EditTextUtils.setEditTextInhibitInputSpace(editSearch);

    }

    @Override
    public int getContentViewId() {
        return R.layout.activity_advertising_section;
    }

    @Override
    public void init(Bundle savedInstanceState) {
        editSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                listdata.clear();
                params = s.toString();
                initData(page, rows, params);
                if (s.length() > 0) {
                    imgDelete.setVisibility(View.VISIBLE);
                } else {
                    imgDelete.setVisibility(View.GONE);
                }
            }
        });
        smartRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                page = 1;
                listdata.clear();
                initData(page, rows, params);
            }
        });
        smartRefreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                LogUtils.e("onLoadMore");
                page++;
                initData(page, rows, params);
            }
        });
        initData(page, rows, params);
    }

    private void initData(int page, int rows, String params) {
        NetService.getInstance().getAdvList(page, rows, params)
                .compose(this.bindLifeCycle())
                .subscribe(new CustomApiCallback<AdZoneEntiy>() {
                    @Override
                    protected void onResultError(ApiException ex) {
                        LogUtils.e(ex.getCode(), ex.getDisplayMessage());
                        OrdinaryActivity.closeRefresh(smartRefreshLayout);
                        if (ex.getCode() == -102) {
                            if (page > 1) {
                                ToastUtils.showToast(AdvertisingSectionActivity.this, ex.getDisplayMessage(), 1000);
                            } else {
                                rvList.setVisibility(View.GONE);
                                rlError.setVisibility(View.GONE);
                                rlNull.setVisibility(View.VISIBLE);
                            }
                        } else {
                            rvList.setVisibility(View.GONE);
                            rlError.setVisibility(View.VISIBLE);
                            rlNull.setVisibility(View.GONE);
                        }
                    }

                    @Override
                    public void onNext(AdZoneEntiy adZoneEntiy) {
                        OrdinaryActivity.closeRefresh(smartRefreshLayout);
                        addData(adZoneEntiy.getData());
                        if (adZoneEntiy.getData().size() > 0 || page > 1) {
                            rvList.setVisibility(View.VISIBLE);
                            rlError.setVisibility(View.GONE);
                            rlNull.setVisibility(View.GONE);
                        } else {
                            rvList.setVisibility(View.GONE);
                            rlError.setVisibility(View.GONE);
                            rlNull.setVisibility(View.VISIBLE);
                        }
                    }
                });


    }

    private void addData(List<AdZoneEntiy.DataBean> list) {
        for (int i = 0; i < list.size(); i++) {
            AdZoneEntiy.DataBean queryRecommendEntity = new AdZoneEntiy.DataBean();
            queryRecommendEntity = list.get(i);
            listdata.add(queryRecommendEntity);
        }
        if (advertisingSectionAdapter == null || page == 1) {
            advertisingSectionAdapter = new AdvertisingSectionAdapter(this);
            advertisingSectionAdapter.data(listdata);
            rvList.setAdapter(advertisingSectionAdapter);
            GridLayoutManager layoutManage = new GridLayoutManager(AdvertisingSectionActivity.this, 6);
            rvList.setLayoutManager(layoutManage);
            layoutManage.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                @Override
                public int getSpanSize(int i) {
                    return setSpanSize(i);
                }
            });
        } else {
            advertisingSectionAdapter.data(listdata);
            advertisingSectionAdapter.notifyDataSetChanged();
        }
        rvList.setNestedScrollingEnabled(false);
    }

    private int setSpanSize(int position) {
        int count;
        if (position % 3 == 0) {
            count = 6;
        } else {
            count = 3;
        }
        return count;
    }


    @OnClick({R.id.tv_bake, R.id.rl_error,R.id.img_delete})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_bake:
                finish();
                break;
            case R.id.rl_error:
                initData(1, rows, params);
                break;
            case R.id.img_delete:
                editSearch.setText("");
                break;
        }
    }
}

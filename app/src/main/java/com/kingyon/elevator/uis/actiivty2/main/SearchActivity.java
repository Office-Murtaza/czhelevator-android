package com.kingyon.elevator.uis.actiivty2.main;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.donkingliang.labels.LabelsView;
import com.kingyon.elevator.R;
import com.kingyon.elevator.entities.entities.ConentEntity;
import com.kingyon.elevator.entities.entities.QueryRecommendEntity;
import com.kingyon.elevator.entities.entities.SearchEntuy;
import com.kingyon.elevator.nets.CustomApiCallback;
import com.kingyon.elevator.nets.NetService;
import com.kingyon.elevator.uis.adapters.adaptertwo.AttentionAdapter;
import com.kingyon.elevator.uis.adapters.adaptertwo.SearchAdapter;
import com.leo.afbaselibrary.nets.exceptions.ApiException;
import com.leo.afbaselibrary.uis.activities.BaseActivity;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.czh.myversiontwo.utils.Constance.ACTIVITY_MAIN2_SEARCH;

/**
 * Created By Admin  on 2020/4/15
 * Email : 163235610@qq.com
 *
 * @Author:Mrczh Instructions:搜索
 */
@Route(path = ACTIVITY_MAIN2_SEARCH)
public class SearchActivity extends BaseActivity {
    @BindView(R.id.edit_search)
    EditText editSearch;
    @BindView(R.id.tv_bake)
    TextView tvBake;
    @BindView(R.id.img_swarch_delete)
    ImageView imgSwarchDelete;
    @BindView(R.id.labels)
    LabelsView labels;
    @BindView(R.id.rcv_view)
    RecyclerView rcvView;
    @BindView(R.id.ll_top)
    LinearLayout llTop;
    @BindView(R.id.rv_attention_list1)
    RecyclerView rvAttentionList;
    @BindView(R.id.rl_error)
    RelativeLayout rlError;
    @BindView(R.id.rl_null)
    RelativeLayout rlNull;
    @BindView(R.id.smart_refresh_layout)
    SmartRefreshLayout smartRefreshLayout;
    @BindView(R.id.ll_top1)
    LinearLayout llTop1;

    int page = 1;
    AttentionAdapter attentionAdapter;
    List<QueryRecommendEntity> recommendEntityList = new ArrayList<>();
    String account;
    String title;
    @BindView(R.id.img_delete)
    ImageView imgDelete;

    @Override
    public int getContentViewId() {
        return R.layout.activity_main_search;
    }

    @Override
    public void init(Bundle savedInstanceState) {
        initSearch();
        editSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                title = s.toString();
                if (s.length() <= 0) {
                    llTop1.setVisibility(View.GONE);
                    llTop.setVisibility(View.VISIBLE);
                    imgDelete.setVisibility(View.GONE);
                }else {
                    imgDelete.setVisibility(View.VISIBLE);
                }
            }
        });

        smartRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                recommendEntityList.clear();
                httpRecommend(1, title);
            }
        });
        smartRefreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                page++;
                httpRecommend(page, title);
            }
        });

        editSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {

            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                /*判断是否是“搜索”键*/
                if(actionId == EditorInfo.IME_ACTION_SEARCH){
                    /*隐藏软键盘*/
                    InputMethodManager imm = (InputMethodManager) v
                            .getContext().getSystemService(
                                    Context.INPUT_METHOD_SERVICE);
                    if (imm.isActive()) {
                        imm.hideSoftInputFromWindow(
                                v.getApplicationWindowToken(), 0);
                    }
                    if (!editSearch.getText().toString().isEmpty()) {
                        String etText = editSearch.getText().toString();
                        SearchEntuy searchEntuy = new SearchEntuy();
                        searchEntuy.search = etText;
                        searchEntuy.save();
                        httpRecommend(1, etText);
                        initSearch();
                        llTop1.setVisibility(View.VISIBLE);
                        llTop.setVisibility(View.GONE);
                    } else {
                        showToast("内容为空");
                    }

                    return true;
                }
                return false;
            }
        });
    }



    private void initSearch() {
        ArrayList<String> label1 = new ArrayList<>();
        List<SearchEntuy> all = DataSupport.findAll(SearchEntuy.class);
        Collections.reverse(all);
        label1.clear();
        if (all.size() <= 10) {
            for (int i = 0; i < all.size(); i++) {
                label1.add(all.get(i).search);
            }
        } else {
            for (int i = 0; i < 10; i++) {
                label1.add(all.get(i).search);
            }
        }
        labels.setLabels(label1);
        SearchAdapter searchAdapter = new SearchAdapter(this);
        rcvView.setLayoutManager(new LinearLayoutManager(this));
        rcvView.setAdapter(searchAdapter);
        labels.setOnLabelClickListener(new LabelsView.OnLabelClickListener() {
            @Override
            public void onLabelClick(TextView label, Object data, int position) {
                editSearch.setText(label1.get(position));
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }


    @OnClick({R.id.tv_bake, R.id.rl_error, R.id.img_swarch_delete,R.id.img_delete})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_bake:
                finish();
                break;
            case R.id.rl_error:
                /*错误*/
                httpRecommend(1, title);
                break;
            case R.id.img_swarch_delete:
                /*清除*/
                DataSupport.deleteAll(SearchEntuy.class);
                initSearch();
                break;
            case R.id.img_delete:
                editSearch.setText("");
                break;
        }
    }

    private void httpRecommend(int page, String title) {
        LogUtils.e(page, title, account);
        NetService.getInstance().setQueryRecommend(page, title, account)
                .compose(this.bindLifeCycle())
                .subscribe(new CustomApiCallback<ConentEntity<QueryRecommendEntity>>() {
                    @Override
                    protected void onResultError(ApiException ex) {
                        closeRefresh();
                        LogUtils.e(ex.getDisplayMessage(), ex.getCode());
                        if (ex.getCode() == -102) {
                            if (page > 1) {
                                ToastUtils.showShort("已经没有更多了");
                                smartRefreshLayout.finishLoadMoreWithNoMoreData();
                            } else {
                                rvAttentionList.setVisibility(View.GONE);
                                rlError.setVisibility(View.GONE);
                                rlNull.setVisibility(View.VISIBLE);
                            }
                        } else {
                            rvAttentionList.setVisibility(View.GONE);
                            rlError.setVisibility(View.VISIBLE);
                            rlNull.setVisibility(View.GONE);
                        }
                    }

                    @Override
                    public void onNext(ConentEntity<QueryRecommendEntity> conentEntity) {
                        closeRefresh();
                        rvAttentionList.setVisibility(View.VISIBLE);
                        rlError.setVisibility(View.GONE);
                        rlNull.setVisibility(View.GONE);
                        dataAdd(conentEntity);
                    }
                });
    }

    private void dataAdd(ConentEntity<QueryRecommendEntity> conentEntity) {
        for (int i = 0; i < conentEntity.getContent().size(); i++) {
            QueryRecommendEntity queryRecommendEntity = new QueryRecommendEntity();
            queryRecommendEntity = conentEntity.getContent().get(i);
            recommendEntityList.add(queryRecommendEntity);
        }
        if (attentionAdapter == null || page == 1) {
            attentionAdapter = new AttentionAdapter(this);
            attentionAdapter.addData(recommendEntityList);
            rvAttentionList.setAdapter(attentionAdapter);
            rvAttentionList.setLayoutManager(new GridLayoutManager(this, 1, GridLayoutManager.VERTICAL, false));
        } else {
            attentionAdapter.addData(recommendEntityList);
            attentionAdapter.notifyDataSetChanged();
        }
    }


    public void closeRefresh() {
        smartRefreshLayout.finishRefresh();
        smartRefreshLayout.finishLoadMore();
    }
}

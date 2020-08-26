package com.kingyon.elevator.uis.actiivty2.main;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.blankj.utilcode.util.LogUtils;
import com.czh.myversiontwo.utils.EditTextUtils;
import com.kingyon.elevator.R;
import com.kingyon.elevator.entities.entities.ConentEntity;
import com.kingyon.elevator.entities.entities.QueryRecommendEntity;
import com.kingyon.elevator.entities.entities.QueryTopicEntity;
import com.kingyon.elevator.entities.entities.TopicLabelEntity;
import com.kingyon.elevator.nets.CustomApiCallback;
import com.kingyon.elevator.nets.NetService;
import com.kingyon.elevator.uis.adapters.adaptertwo.AttentionAdapter;
import com.kingyon.elevator.uis.adapters.adaptertwo.TopicSearchAdapter;
import com.kingyon.elevator.uis.fragments.main2.found.TopicSearchFragment;
import com.kingyon.elevator.uis.fragments.main2.found.utilsf.CustomFragmentPagerAdapter;
import com.kingyon.elevator.utils.utilstwo.OrdinaryActivity;
import com.kingyon.elevator.view.ModifyTabLayout;
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

import static com.czh.myversiontwo.utils.Constance.ACTIVITY_MAIN2_TOPIC_SEARCH;
import static com.kingyon.elevator.uis.actiivty2.input.TagList.RESULT_TAG;

/**
 * @Created By Admin  on 2020/4/20
 * @Email : 163235610@qq.com
 * @Author:Mrczh
 * @Instructions:话题搜索
 */
@Route(path = ACTIVITY_MAIN2_TOPIC_SEARCH)
public class TopicSelectionActivity extends BaseActivity {

    @BindView(R.id.edit_search)
    EditText editSearch;
    @BindView(R.id.tv_bake)
    TextView tvBake;
    @BindView(R.id.modiftTabLayout)
    ModifyTabLayout tabLayout;
    @BindView(R.id.view_pager)
    ViewPager vp;
    @BindView(R.id.ll_topic)
    LinearLayout llTopic;
    @BindView(R.id.rl_error)
    RelativeLayout rlError;
    @BindView(R.id.rl_null)
    RelativeLayout rlNull;
    @BindView(R.id.rl_list)
    RecyclerView rlList;
    String title;
    @BindView(R.id.smart_refresh_layout_topic)
    SmartRefreshLayout smartRefreshLayoutTopic;
    int page = 1;
    TopicSearchAdapter topicSearchAdapter;
    List<QueryTopicEntity.PageContentBean> list = new ArrayList<>();
    public static Intent getIntent(Activity activity) {
        return new Intent(activity, TopicSelectionActivity.class);
    }

    @Override
    public int getContentViewId() {
        return R.layout.activity_topic_selection;
    }

    @Override
    public void init(Bundle savedInstanceState) {
        ButterKnife.bind(this);
        initUi();
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
                if (s.length() > 0) {
                    list.clear();
                    llTopic.setVisibility(View.GONE);
                    smartRefreshLayoutTopic.setVisibility(View.VISIBLE);
                    httpQuerTopic(1, title, 0);
                } else {
                    llTopic.setVisibility(View.VISIBLE);
                    smartRefreshLayoutTopic.setVisibility(View.GONE);
                }
            }
        });
        smartRefreshLayoutTopic.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                list.clear();
                httpQuerTopic(1,title,0);
            }
        });
        smartRefreshLayoutTopic.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                page++;
                httpQuerTopic(page,title,0);
            }
        });
    }

    private void httpQuerTopic(int page, String title, int label) {
        showProgressDialog("请稍等", true);
        NetService.getInstance().setOueryTopic(page, title, label)
                .compose(this.bindLifeCycle())
                .subscribe(new CustomApiCallback<ConentEntity<QueryTopicEntity.PageContentBean>>() {
                    @Override
                    protected void onResultError(ApiException ex) {
//                        com.blankj.utilcode.util.ToastUtils.showShort(ex.getDisplayMessage());
                        OrdinaryActivity.closeRefresh(smartRefreshLayoutTopic);
                        if (ex.getCode() == -102) {
                            /*没有数据*/
                            rlError.setVisibility(View.GONE);
                            rlNull.setVisibility(View.VISIBLE);
                            rlList.setVisibility(View.GONE);
                            smartRefreshLayoutTopic.setVisibility(View.GONE);
                        } else {
                            rlError.setVisibility(View.VISIBLE);
                            rlNull.setVisibility(View.GONE);
                            rlList.setVisibility(View.GONE);
                            smartRefreshLayoutTopic.setVisibility(View.GONE);
                        }
                        hideProgress();
                    }

                    @Override
                    public void onNext(ConentEntity<QueryTopicEntity.PageContentBean> pageContentBeanQueryTopicEntity) {
                        OrdinaryActivity.closeRefresh(smartRefreshLayoutTopic);
                        if (pageContentBeanQueryTopicEntity.getContent().size() > 0) {
                            rlError.setVisibility(View.GONE);
                            rlNull.setVisibility(View.GONE);
                            rlList.setVisibility(View.VISIBLE);
                            smartRefreshLayoutTopic.setVisibility(View.VISIBLE);
                            addData(pageContentBeanQueryTopicEntity);

                        } else {
                            rlError.setVisibility(View.GONE);
                            rlNull.setVisibility(View.VISIBLE);
                            rlList.setVisibility(View.GONE);
                            smartRefreshLayoutTopic.setVisibility(View.GONE);
                        }
                    }
                });
    }

    private void addData(ConentEntity<QueryTopicEntity.PageContentBean> content) {
        hideProgress();
        for (int i = 0; i < content.getContent().size(); i++) {
            QueryTopicEntity.PageContentBean queryRecommendEntity = new QueryTopicEntity.PageContentBean();
            queryRecommendEntity = content.getContent().get(i);
            list.add(queryRecommendEntity);
        }
        if (topicSearchAdapter == null || page == 1) {
            topicSearchAdapter = new TopicSearchAdapter(TopicSelectionActivity.this);
            topicSearchAdapter.addData(list);
            rlList.setAdapter(topicSearchAdapter);
            rlList.setLayoutManager(new GridLayoutManager(TopicSelectionActivity.this, 1, GridLayoutManager.VERTICAL, false));
        } else {
            topicSearchAdapter.addData(list);
            topicSearchAdapter.notifyDataSetChanged();
        }


        topicSearchAdapter.setOnItemClickListener(new TopicSearchAdapter.ItemClickListener() {
            @Override
            public void onItemClick(int position) {
                List<QueryTopicEntity.PageContentBean> data = content.getContent();
                QueryTopicEntity.PageContentBean tag = data.get(position);
                setResult(tag);
            }
        });
    }


    private void setResult(QueryTopicEntity.PageContentBean tag) {
        Intent intent = getIntent();
        intent.putExtra(RESULT_TAG, tag);
        setResult(RESULT_OK, intent);
        finish();
    }

    private void initUi() {
        showProgressDialog("请稍等", true);
        NetService.getInstance().setTopicLable()
                .compose(this.bindLifeCycle())
                .subscribe(new CustomApiCallback<TopicLabelEntity<TopicLabelEntity.PageContentBean>>() {
                    @Override
                    protected void onResultError(ApiException ex) {
//                        ToastUtils.showToast(TopicSelectionActivity.this, ex.getDisplayMessage(), 1000);
                        llTopic.setVisibility(View.GONE);
                        rlError.setVisibility(View.VISIBLE);
                        rlNull.setVisibility(View.GONE);
                        hideProgress();
                        LogUtils.e(ex.getDisplayMessage(), ex.getCode(), ex);
                    }

                    @Override
                    public void onNext(TopicLabelEntity<TopicLabelEntity.PageContentBean> pageContentBeanTopicLabelEntity) {
                        LogUtils.e(pageContentBeanTopicLabelEntity.getPageContent().toString());
                        tabLayout.setViewHeight(dp2px(30));
                        tabLayout.setBottomLineWidth(dp2px(10));
                        tabLayout.setBottomLineHeight(dp2px(3));
                        tabLayout.setBottomLineHeightBgResId(R.color.color_00000000);
                        tabLayout.setItemInnerPaddingLeft(dp2px(10));
                        tabLayout.setItemInnerPaddingRight(dp2px(10));
                        tabLayout.setInnerLeftMargin(dp2px(5));
                        tabLayout.setInnerRightMargin(dp2px(5));
                        tabLayout.setmTextColorSelect(ContextCompat.getColor(TopicSelectionActivity.this, R.color.type1));
                        tabLayout.setmTextColorUnSelect(ContextCompat.getColor(TopicSelectionActivity.this, R.color.type2));
                        tabLayout.setmTextBgUnSelectResId(R.drawable.bg_ad_type1);
                        tabLayout.setmTextBgSelectResId(R.drawable.bg_ad_type2);
                        tabLayout.setTextSize(16);
                        CustomFragmentPagerAdapter adapter = new CustomFragmentPagerAdapter(getSupportFragmentManager());
                        if (pageContentBeanTopicLabelEntity.getPageContent().size() > 0) {
                            llTopic.setVisibility(View.VISIBLE);
                            rlError.setVisibility(View.GONE);
                            rlNull.setVisibility(View.GONE);
                            for (int i = 0; i < pageContentBeanTopicLabelEntity.getPageContent().size(); i++) {
                                LogUtils.e(pageContentBeanTopicLabelEntity.getPageContent().get(i).getLabelName(),
                                        pageContentBeanTopicLabelEntity.getPageContent().get(i).getId());

                                adapter.addFrag(new TopicSearchFragment().setIndex(pageContentBeanTopicLabelEntity.getPageContent().get(i).getId()),
                                        pageContentBeanTopicLabelEntity.getPageContent().get(i).getLabelName());
                            }
                            vp.setAdapter(adapter);
                            vp.setOffscreenPageLimit(adapter.getCount());
                            tabLayout.setupWithViewPager(vp);
                            vp.setOffscreenPageLimit(0);
                        } else {
                            llTopic.setVisibility(View.GONE);
                            rlError.setVisibility(View.GONE);
                            rlNull.setVisibility(View.VISIBLE);
                        }
                    }
                });
    }

    public int dp2px(float dpValue) {
        float scale = getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
        EditTextUtils.setEditTextInhibitInputSpace(editSearch);
    }

    @OnClick({R.id.tv_bake, R.id.rl_error})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_bake:
                finish();
                break;
            case R.id.rl_error:
                initUi();
                break;
        }
    }
}

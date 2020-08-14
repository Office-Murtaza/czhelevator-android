package com.kingyon.elevator.uis.fragments.main2.found;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.blankj.utilcode.util.ToastUtils;
import com.kingyon.elevator.R;
import com.kingyon.elevator.entities.entities.ConentEntity;
import com.kingyon.elevator.entities.entities.QueryTopicEntity;
import com.kingyon.elevator.nets.CustomApiCallback;
import com.kingyon.elevator.nets.NetService;
import com.kingyon.elevator.uis.actiivty2.main.TopicSelectionActivity;
import com.kingyon.elevator.uis.adapters.adaptertwo.TopicSearchAdapter;
import com.kingyon.elevator.utils.utilstwo.OrdinaryActivity;
import com.leo.afbaselibrary.nets.exceptions.ApiException;
import com.leo.afbaselibrary.uis.fragments.BaseFragment;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

import static android.app.Activity.RESULT_OK;
import static com.kingyon.elevator.uis.actiivty2.input.TagList.RESULT_TAG;

/**
 * @Created By Admin  on 2020/4/20
 * @Email : 163235610@qq.com
 * @Author:Mrczh
 * @Instructions:话题选择
 */
public class TopicSearchFragment extends BaseFragment {
    @BindView(R.id.rv_list)
    RecyclerView rvList;
    Unbinder unbinder;
    int label;
    TopicSearchFragment topicSearchFragment;
    @BindView(R.id.rl_error)
    RelativeLayout rlError;
    @BindView(R.id.rl_null)
    RelativeLayout rlNull;
    @BindView(R.id.smart_refresh_layout_topic)
    SmartRefreshLayout smartRefreshLayoutTopic;
    TopicSearchAdapter topicSearchAdapter;
    int page = 1;
    List<QueryTopicEntity.PageContentBean> list = new ArrayList<>();
    public TopicSearchFragment setIndex(int label) {
        this.label = label;
        return (this);
    }

    @Override
    public int getContentViewId() {
        return R.layout.fragment_topic_search;
    }

    @Override
    public void init(Bundle savedInstanceState) {
        list.clear();
        httpQuerTopic(1, "", label);

        smartRefreshLayoutTopic.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                list.clear();
                httpQuerTopic(1, "", label);
            }
        });
        smartRefreshLayoutTopic.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                page++;
                httpQuerTopic(page, "", label);
            }
        });


    }

    private void httpQuerTopic(int page, String title, int label) {
        showProgressDialog("请稍等");
        NetService.getInstance().setOueryTopic(page, title, label)
                .compose(this.bindLifeCycle())
                .subscribe(new CustomApiCallback<ConentEntity<QueryTopicEntity.PageContentBean>>() {
                    @Override
                    protected void onResultError(ApiException ex) {
//                        ToastUtils.showShort(ex.getDisplayMessage());
                        OrdinaryActivity.closeRefresh(smartRefreshLayoutTopic);
                        if (ex.getCode() == -102) {
                            /*没有数据*/
                            rlError.setVisibility(View.GONE);
                            rlNull.setVisibility(View.VISIBLE);
                            rvList.setVisibility(View.GONE);
                        } else {
                            rlError.setVisibility(View.VISIBLE);
                            rlNull.setVisibility(View.GONE);
                            rvList.setVisibility(View.GONE);
                        }
                        hideProgress();
                    }

                    @Override
                    public void onNext(ConentEntity<QueryTopicEntity.PageContentBean> pageContentBeanQueryTopicEntity) {
                        OrdinaryActivity.closeRefresh(smartRefreshLayoutTopic);
                        if (pageContentBeanQueryTopicEntity.getContent().size() > 0) {
                            rlError.setVisibility(View.GONE);
                            rlNull.setVisibility(View.GONE);
                            rvList.setVisibility(View.VISIBLE);
                            addData(pageContentBeanQueryTopicEntity);

                        } else {
                            rlError.setVisibility(View.GONE);
                            rlNull.setVisibility(View.VISIBLE);
                            rvList.setVisibility(View.GONE);
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
            topicSearchAdapter = new TopicSearchAdapter(getActivity());
            topicSearchAdapter.addData(list);
            rvList.setAdapter(topicSearchAdapter);
            rvList.setLayoutManager(new GridLayoutManager(getActivity(), 1, GridLayoutManager.VERTICAL, false));
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
        Intent intent = getActivity().getIntent();
        intent.putExtra(RESULT_TAG, tag);
        getActivity().setResult(RESULT_OK, intent);
        getActivity().finish();
    }


    @Override
    protected void dealLeackCanary() {

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // TODO: inflate a fragment view
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        unbinder = ButterKnife.bind(this, rootView);
        return rootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick(R.id.rl_error)
    public void onViewClicked() {
        httpQuerTopic(1, "", label);
    }
}

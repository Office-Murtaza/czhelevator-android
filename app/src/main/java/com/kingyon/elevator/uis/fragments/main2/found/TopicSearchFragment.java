package com.kingyon.elevator.uis.fragments.main2.found;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.kingyon.elevator.R;
import com.kingyon.elevator.uis.adapters.adaptertwo.TopicSearchAdapter;
import com.leo.afbaselibrary.uis.fragments.BaseFragment;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * @Created By Admin  on 2020/4/20
 * @Email : 163235610@qq.com
 * @Author:Mrczh
 * @Instructions:
 */
public class TopicSearchFragment extends BaseFragment {
    @BindView(R.id.rv_list)
    RecyclerView rvList;
    Unbinder unbinder;

    @Override
    public int getContentViewId() {
        return R.layout.fragment_topic_search;
    }

    @Override
    public void init(Bundle savedInstanceState) {
        TopicSearchAdapter topicSearchAdapter = new TopicSearchAdapter(getActivity());
        rvList.setLayoutManager(new LinearLayoutManager(getActivity()));
        rvList.setAdapter(topicSearchAdapter);
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
}

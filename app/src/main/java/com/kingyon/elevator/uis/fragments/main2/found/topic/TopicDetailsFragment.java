package com.kingyon.elevator.uis.fragments.main2.found.topic;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.kingyon.elevator.uis.adapters.adaptertwo.AttentionAdapter;
import com.kingyon.elevator.R;
import com.kingyon.elevator.uis.fragments.main2.found.utilsf.BaseFragment;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * @Created By Admin  on 2020/4/22
 * @Email : 163235610@qq.com
 * @Author:Mrczh
 * @Instructions:
 */
public class TopicDetailsFragment extends BaseFragment {
    AttentionAdapter attentionAdapter;
    List<String> list = new ArrayList<>();
    @BindView(R.id.rv_attention_list)
    RecyclerView rvAttentionList;
    Unbinder unbinder;


    @Override
    protected int setContentView() {
        return R.layout.fragment_topic_deatils;
    }

    @Override
    protected void lazyLoad() {
        initUI();
    }


    private void initUI() {
        /**
         * 1文字
         * 2图片
         * 3视频
         * 4文字+图片
         * 5文字+视频
         * */
        list.add("1");
        list.add("2");
        list.add("3");
        list.add("4");
        list.add("5");
        list.add("1");
        list.add("5");
        list.add("4");
        list.add("3");
        list.add("2");
        list.add("2");
        list.add("1");
        rvAttentionList = getContentView().findViewById(R.id.rv_attention_list);
        if (rvAttentionList != null) {
            attentionAdapter = new AttentionAdapter(getActivity(), list);
            rvAttentionList.setLayoutManager(new LinearLayoutManager(getActivity()));
            rvAttentionList.setAdapter(attentionAdapter);
        }

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

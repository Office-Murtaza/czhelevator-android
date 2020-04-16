package com.kingyon.elevator.uis.fragments.main2.found;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.blankj.utilcode.util.LogUtils;
import com.kingyon.elevator.R;
import com.kingyon.elevator.uis.adapters.adapter2.AttentionAdapter;
import com.kingyon.elevator.uis.fragments.main2.found.utilsf.LazyFragment;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created By Admin  on 2020/4/14
 * Email : 163235610@qq.com
 * Author:Mrczh
 * Instructions:关注
 */
public class AttentionFragment extends LazyFragment {
    @BindView(R.id.rv_attention_list)
    RecyclerView rvAttentionList;
    @BindView(R.id.smart_refresh_layout)
    SmartRefreshLayout smartRefreshLayout;
    Unbinder unbinder;

    private View view;
    // 标志位，标志已经初始化完成。
    private boolean isPrepared;
    AttentionAdapter attentionAdapter;
    List<String> list = new ArrayList<>();

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_attention, container, false);
        unbinder = ButterKnife.bind(this, view);
//        initUI();
        isPrepared = true;
        lazyLoad();//加载数据
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
//        initUI();
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
        if (rvAttentionList != null) {
            attentionAdapter = new AttentionAdapter(getActivity(), list);
            rvAttentionList.setLayoutManager(new LinearLayoutManager(getActivity()));
            rvAttentionList.setAdapter(attentionAdapter);
        }
        smartRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                attentionAdapter = new AttentionAdapter(getActivity(), list);
                rvAttentionList.setLayoutManager(new LinearLayoutManager(getActivity()));
                rvAttentionList.setAdapter(attentionAdapter);
                smartRefreshLayout.finishRefresh();
            }
        });
        smartRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                smartRefreshLayout.finishRefresh();
            }
        });

    }


    @Override
    protected void lazyLoad() {
        initUI();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}

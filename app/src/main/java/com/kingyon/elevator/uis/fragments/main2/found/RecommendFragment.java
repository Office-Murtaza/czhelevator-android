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
import com.kingyon.elevator.uis.adapters.adapter2.RecommendtopAdapter;
import com.kingyon.elevator.uis.fragments.main2.found.utilsf.LazyFragment;
import com.kingyon.elevator.uis.fragments.main2.found.utilsf.LazyloadFragment;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
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
 * Instructions:推荐
 */
public class RecommendFragment extends LazyloadFragment {
    RecyclerView rvAttentionTop;
    RecyclerView rvAttentionList;
    SmartRefreshLayout smartRefreshLayout;
    private View view;
    // 标志位，标志已经初始化完成。
    private boolean isPrepared;
    AttentionAdapter attentionAdapter;
    RecommendtopAdapter recommendtopAdapter;
    List<String> list =new ArrayList<>();

    @Override
    protected int setContentView() {
        return R.layout.fragment_recommend;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


    }

    @Override
    protected void init() {
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
        rvAttentionList = rootView.findViewById(R.id.rv_attention_list1);
        rvAttentionTop = rootView.findViewById(R.id.rv_attention_top);
        smartRefreshLayout = rootView.findViewById(R.id.smart_refresh_layout);

        recommendtopAdapter = new RecommendtopAdapter(getActivity(),2);
        rvAttentionTop.setLayoutManager(new LinearLayoutManager(getActivity()));
        rvAttentionTop.setAdapter(recommendtopAdapter);
        attentionAdapter = new AttentionAdapter(getActivity(),list);
        rvAttentionList.setLayoutManager(new LinearLayoutManager(getActivity()));
        rvAttentionList.setAdapter(attentionAdapter);
        attentionAdapter.notifyDataSetChanged();
        recommendtopAdapter.notifyDataSetChanged();


        smartRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                recommendtopAdapter = new RecommendtopAdapter(getActivity(),2);
                rvAttentionTop.setLayoutManager(new LinearLayoutManager(getActivity()));
                rvAttentionTop.setAdapter(recommendtopAdapter);
                attentionAdapter = new AttentionAdapter(getActivity(),list);
                rvAttentionList.setLayoutManager(new LinearLayoutManager(getActivity()));
                rvAttentionList.setAdapter(attentionAdapter);
                attentionAdapter.notifyDataSetChanged();
                recommendtopAdapter.notifyDataSetChanged();
                smartRefreshLayout.finishRefresh();
            }
        });
      smartRefreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
          @Override
          public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
              smartRefreshLayout.finishLoadMore();
          }
      });



        LogUtils.e("推荐开始");
    }



    @Override
    protected void lazyLoad() {
        init();
    }


}

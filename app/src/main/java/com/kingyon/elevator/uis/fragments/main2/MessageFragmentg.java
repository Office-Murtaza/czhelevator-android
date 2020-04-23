package com.kingyon.elevator.uis.fragments.main2;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.kingyon.elevator.R;
import com.kingyon.elevator.uis.adapters.adaptertwo.MessageAdapter;
import com.kingyon.elevator.utils.StatusBarUtil;
import com.leo.afbaselibrary.uis.fragments.BaseFragment;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * Created By Admin  on 2020/4/14
 * Email : 163235610@qq.com
 * Author:Mrczh
 * Instructions:消息
 */
public class MessageFragmentg extends BaseFragment {
    @BindView(R.id.tv_read)
    TextView tvRead;
    @BindView(R.id.tv_massage_number)
    TextView tvMassageNumber;
    @BindView(R.id.ll_msagger)
    LinearLayout llMsagger;
    @BindView(R.id.tv_attention_number)
    TextView tvAttentionNumber;
    @BindView(R.id.ll_attention)
    LinearLayout llAttention;
    @BindView(R.id.tv_like_number)
    TextView tvLikeNumber;
    @BindView(R.id.ll_like)
    LinearLayout llLike;
    @BindView(R.id.tv_comment_number)
    TextView tvCommentNumber;
    @BindView(R.id.ll_comment)
    LinearLayout llComment;
    @BindView(R.id.rcv_list_massage)
    RecyclerView rcvListMassage;
    Unbinder unbinder;
    MessageAdapter messageAdapter;
    @BindView(R.id.rl_bj)
    RelativeLayout rlBj;

    @Override
    public int getContentViewId() {
        return R.layout.fragment_message;
    }

    @Override
    public void init(Bundle savedInstanceState) {

        messageAdapter = new MessageAdapter(getActivity());
        rcvListMassage.setLayoutManager(new LinearLayoutManager(getActivity()));
        rcvListMassage.setAdapter(messageAdapter);

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

    @OnClick({R.id.tv_read, R.id.ll_msagger, R.id.ll_attention, R.id.ll_like, R.id.ll_comment})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_read:
                break;
            case R.id.ll_msagger:
                break;
            case R.id.ll_attention:
                break;
            case R.id.ll_like:
                break;
            case R.id.ll_comment:
                break;
        }
    }
}
package com.kingyon.elevator.uis.fragments.homepage;


import android.os.Bundle;
import android.widget.ListView;
import android.widget.TextView;

import com.kingyon.elevator.R;
import com.kingyon.elevator.entities.CommentEntity;
import com.kingyon.elevator.mvpbase.MvpBaseFragment;
import com.kingyon.elevator.presenter.CommentDetailsPresenter;
import com.kingyon.elevator.uis.adapters.adapterone.NewsReplyAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * 评论详情界面
 */
public class CommentDetailsFragment extends MvpBaseFragment<CommentDetailsPresenter> {

    @BindView(R.id.user_head)
    CircleImageView user_head;
    @BindView(R.id.user_name)
    TextView user_name;
    @BindView(R.id.comment_time)
    TextView comment_time;
    @BindView(R.id.comment_content)
    TextView comment_content;
    @BindView(R.id.reply_list_container)
    ListView reply_list_container;
    NewsReplyAdapter newsReplyAdapter;

    private List<CommentEntity> commentEntityList;

    @Override
    public CommentDetailsPresenter initPresenter() {
        return new CommentDetailsPresenter(getActivity());
    }

    @Override
    public void initView(Bundle savedInstanceState) {
        ButterKnife.bind(this, getContentView());
        commentEntityList = new ArrayList<>();
        for (int i = 0; i <10 ; i++) {
            commentEntityList.add(new CommentEntity());
        }
        newsReplyAdapter = new NewsReplyAdapter(getActivity(),commentEntityList);
        reply_list_container.setAdapter(newsReplyAdapter);
    }

    @Override
    public int getContentViewId() {
        return R.layout.fragment_comment_details;
    }

    public static CommentDetailsFragment newInstance() {
        
        Bundle args = new Bundle();
        
        CommentDetailsFragment fragment = new CommentDetailsFragment();
        fragment.setArguments(args);
        return fragment;
    }
}

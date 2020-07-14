package com.kingyon.elevator.uis.actiivty2.massage;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.kingyon.elevator.R;
import com.kingyon.elevator.uis.fragments.main2.found.utilsf.CustomFragmentPagerAdapter;
import com.kingyon.elevator.uis.fragments.message.MessageAtListFragment;
import com.kingyon.elevator.uis.fragments.message.MessageCommentMeListFragment;
import com.kingyon.elevator.uis.fragments.message.MessageLikeCommentFragment;
import com.kingyon.elevator.uis.fragments.message.MessageLikeContentFragment;
import com.leo.afbaselibrary.uis.activities.BaseActivity;
import com.ogaclejapan.smarttablayout.SmartTabLayout;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.czh.myversiontwo.utils.Constance.ACTIVITY_MASSAGE_COMMENT;

/**
 * @Created By Admin  on 2020/4/22
 * @Email : 163235610@qq.com
 * @Author:Mrczh
 * @Instructions:消息评论
 */
@Route(path = ACTIVITY_MASSAGE_COMMENT)
public class CommentActivity extends BaseActivity {

    @BindView(R.id.img_top_back)
    ImageView imgTopBack;
    @BindView(R.id.tv_top_title)
    TextView tvTopTitle;
    @BindView(R.id.tv_right)
    TextView tvRight;
    @BindView(R.id.viewpagertab)
    SmartTabLayout viewpagertab;
    @BindView(R.id.vp)
    ViewPager vp;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);

    }


    @OnClick(R.id.img_top_back)
    public void onViewClicked() {
        finish();
    }

    @Override
    public int getContentViewId() {
        return R.layout.activity_comment;
    }

    @Override
    public void init(Bundle savedInstanceState) {
        tvTopTitle.setText("@和评论");
        CustomFragmentPagerAdapter adapter = new CustomFragmentPagerAdapter(getSupportFragmentManager());
        adapter.addFrag(new MessageAtListFragment(), "@我");
        adapter.addFrag(new MessageCommentMeListFragment(),"评论我");
        vp.setAdapter(adapter);
        vp.setOffscreenPageLimit(adapter.getCount());
        viewpagertab.setViewPager(vp);
    }

}

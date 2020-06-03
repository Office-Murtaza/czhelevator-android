package com.kingyon.elevator.uis.actiivty2.main;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.blankj.utilcode.util.LogUtils;
import com.kingyon.elevator.R;
import com.kingyon.elevator.entities.entities.TopicLabelEntity;
import com.kingyon.elevator.nets.CustomApiCallback;
import com.kingyon.elevator.nets.NetService;
import com.kingyon.elevator.uis.fragments.main2.found.TopicSearchFragment;
import com.kingyon.elevator.uis.fragments.main2.found.utilsf.CustomFragmentPagerAdapter;
import com.kingyon.elevator.view.ModifyTabLayout;
import com.leo.afbaselibrary.nets.exceptions.ApiException;
import com.leo.afbaselibrary.uis.activities.BaseActivity;
import com.leo.afbaselibrary.utils.ToastUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.czh.myversiontwo.utils.Constance.ACTIVITY_MAIN2_TOPIC_SEARCH;

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
    }

    private void initUi() {
        showProgressDialog("请稍等");
        NetService.getInstance().setTopicLable()
                .compose(this.bindLifeCycle())
                .subscribe(new CustomApiCallback<TopicLabelEntity<TopicLabelEntity.PageContentBean>>() {
                    @Override
                    protected void onResultError(ApiException ex) {
                        ToastUtils.showToast(TopicSelectionActivity.this, ex.getDisplayMessage(), 1000);
                        llTopic.setVisibility(View.GONE);
                        rlError.setVisibility(View.VISIBLE);
                        rlNull.setVisibility(View.GONE);
                        hideProgress();
                        LogUtils.e(ex.getDisplayMessage(),ex.getCode(),ex);
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

package com.kingyon.elevator.uis.actiivty2.main;

import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.widget.EditText;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.kingyon.elevator.R;
import com.kingyon.elevator.uis.fragments.main2.found.TopicSearchFragment;
import com.kingyon.elevator.uis.fragments.main2.found.utilsf.CustomFragmentPagerAdapter;
import com.kingyon.elevator.view.ModifyTabLayout;
import com.leo.afbaselibrary.uis.activities.BaseActivity;

import butterknife.BindView;
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


    @Override
    public int getContentViewId() {
        return R.layout.activity_topic_selection;
    }

    @Override
    public void init(Bundle savedInstanceState) {
        initUi();
    }

    private void initUi() {
        tabLayout.setViewHeight(dp2px(30));
        tabLayout.setBottomLineWidth(dp2px(10));
        tabLayout.setBottomLineHeight(dp2px(3));
        tabLayout.setBottomLineHeightBgResId(R.color.color_00000000);
        tabLayout.setItemInnerPaddingLeft(dp2px(10));
        tabLayout.setItemInnerPaddingRight(dp2px(10));
        tabLayout.setmTextColorSelect(ContextCompat.getColor(this,R.color.type1));
        tabLayout.setmTextColorUnSelect(ContextCompat.getColor(this,R.color.type2));
        tabLayout.setmTextBgUnSelectResId(R.drawable.bg_ad_type1);
        tabLayout.setmTextBgSelectResId(R.drawable.bg_ad_type2);
        tabLayout.setTextSize(16);
        //等寬
//        int width=getResources().getDisplayMetrics().widthPixels;
//        tabLayout.setNeedEqual(true,width);
        CustomFragmentPagerAdapter adapter = new CustomFragmentPagerAdapter(getSupportFragmentManager());
        for (int i = 0 ; i <=10 ; i ++) {
            adapter.addFrag(new TopicSearchFragment(), "栏目"+i);
        }
        vp.setAdapter(adapter);
        vp.setOffscreenPageLimit(adapter.getCount());
        tabLayout.setupWithViewPager(vp);
//        vp.setCurrentItem(3);
        vp.setOffscreenPageLimit(0);


    }

    public int dp2px( float dpValue){
        float scale=getResources().getDisplayMetrics().density;
        return (int)(dpValue*scale+0.5f);
    }
    @OnClick(R.id.tv_bake)
    public void onViewClicked() {
        finish();
    }
}

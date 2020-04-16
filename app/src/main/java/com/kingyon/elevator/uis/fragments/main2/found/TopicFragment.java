package com.kingyon.elevator.uis.fragments.main2.found;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.blankj.utilcode.util.LogUtils;
import com.kingyon.elevator.R;
import com.kingyon.elevator.uis.fragments.main2.found.utilsf.CustomFragmentPagerAdapter;
import com.kingyon.elevator.uis.fragments.main2.found.utilsf.LazyloadFragment;
import com.kingyon.elevator.view.ModifyTabLayout;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created By Admin  on 2020/4/14
 * Email : 163235610@qq.com
 * Author:Mrczh
 * Instructions:话题
 */
public class TopicFragment extends LazyloadFragment {


    ModifyTabLayout tabLayout;
    ViewPager vp;
    Unbinder unbinder;

    @Override
    protected int setContentView() {
        return R.layout.fragment_topic;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        init();
    }

    @Override
    protected void init() {
        tabLayout = rootView.findViewById(R.id.modiftTabLayout);
        vp = rootView.findViewById(R.id.vp);
        tabLayout.setViewHeight(dp2px(30));
        tabLayout.setBottomLineWidth(dp2px(10));
        tabLayout.setBottomLineHeight(dp2px(3));
        tabLayout.setBottomLineHeightBgResId(R.color.color_00000000);
        tabLayout.setItemInnerPaddingLeft(dp2px(6));
        tabLayout.setItemInnerPaddingRight(dp2px(6));
        tabLayout.setmTextColorSelect(ContextCompat.getColor(getActivity(),R.color.type1));
        tabLayout.setmTextColorUnSelect(ContextCompat.getColor(getActivity(),R.color.type2));
        tabLayout.setmTextBgUnSelectResId(R.drawable.bg_ad_type1);
        tabLayout.setmTextBgSelectResId(R.drawable.bg_ad_type2);
        tabLayout.setTextSize(16);
        //等寬
//        int width=getResources().getDisplayMetrics().widthPixels;
//        tabLayout.setNeedEqual(true,width);
        CustomFragmentPagerAdapter adapter = new CustomFragmentPagerAdapter(getFragmentManager());
        for (int i=0;i<=10;i++) {
            adapter.addFrag(new TopicTypeFragment(), "栏目"+i);
        }
        vp.setAdapter(adapter);
        vp.setOffscreenPageLimit(adapter.getCount());
        tabLayout.setupWithViewPager(vp);
    }

    @Override
    protected void lazyLoad() {

        LogUtils.e("话题首页");
    }

    /**
     * dp转换成px
     */
    public int dp2px( float dpValue){
        float scale=getResources().getDisplayMetrics().density;
        return (int)(dpValue*scale+0.5f);
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

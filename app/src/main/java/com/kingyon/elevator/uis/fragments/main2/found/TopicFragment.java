package com.kingyon.elevator.uis.fragments.main2.found;

import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.kingyon.elevator.R;
import com.kingyon.elevator.uis.fragments.main2.found.utilsf.CustomFragmentPagerAdapter;
import com.kingyon.elevator.uis.fragments.main2.found.utilsf.LazyFragment;
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
public class TopicFragment extends LazyFragment {

    @BindView(R.id.modiftTabLayout)
    ModifyTabLayout tabLayout;
    @BindView(R.id.vp)
    ViewPager vp;
    Unbinder unbinder1;
    private View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_topic, container, false);
        lazyLoad();//加载数据
        unbinder1 = ButterKnife.bind(this, view);
        initUi();
        return view;
    }



    private void initUi() {
        tabLayout.setViewHeight(dp2px(30));
        tabLayout.setBottomLineWidth(dp2px(10));
        tabLayout.setBottomLineHeight(dp2px(3));
        tabLayout.setBottomLineHeightBgResId(R.color.color_00000000);
        tabLayout.setItemInnerPaddingLeft(dp2px(10));
        tabLayout.setItemInnerPaddingRight(dp2px(10));
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
//        vp.setCurrentItem(3);
    }

    @Override
    protected void lazyLoad() {

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder1.unbind();
    }


    public int dp2px( float dpValue){
        float scale=getResources().getDisplayMetrics().density;
        return (int)(dpValue*scale+0.5f);
    }

}

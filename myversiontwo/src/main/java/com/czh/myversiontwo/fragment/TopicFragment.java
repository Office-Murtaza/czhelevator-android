package com.czh.myversiontwo.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.blankj.utilcode.util.LogUtils;
import com.czh.myversiontwo.R;
import com.czh.myversiontwo.uiutils.BaseFragment;
import com.czh.myversiontwo.view.ModifyTablayTwo;

/**
 * @Created By Admin  on 2020/4/21
 * @Email : 163235610@qq.com
 * @Author:Mrczh
 * @Instructions:
 */
public class TopicFragment  extends BaseFragment {
    ModifyTablayTwo tabLayout;
    ViewPager vp;
    @Override
    protected int setContentView() {
        return R.layout.fragment_topic;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }



    @Override
    protected void lazyLoad() {
        Log.e("TAG","TopicFragment话题");
        Toast.makeText(getContext(),"TopicFragment话题",Toast.LENGTH_LONG).show();


    }


    public int dp2px( float dpValue){
        float scale=getResources().getDisplayMetrics().density;
        return (int)(dpValue*scale+0.5f);
    }

}


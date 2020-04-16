package com.kingyon.elevator.uis.fragments.main2;

import android.content.res.Resources;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.android.arouter.launcher.ARouter;
import com.kingyon.elevator.R;
import com.kingyon.elevator.uis.fragments.main2.found.AttentionFragment;
import com.kingyon.elevator.uis.fragments.main2.found.RecommendFragment;
import com.kingyon.elevator.uis.fragments.main2.found.TopicFragment;
import com.kingyon.elevator.uis.fragments.main2.found.utilsf.ConfirmPopWindow;
import com.kingyon.elevator.uis.fragments.main2.found.utilsf.FragmentAdapter;
import com.leo.afbaselibrary.uis.fragments.BaseFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

import static com.kingyon.elevator.utils.Constance.ACTIVITY_MAIN2_ADVERTISING;
import static com.kingyon.elevator.utils.Constance.ACTIVITY_MAIN2_SEARCH;

/**
 * Created By Admin  on 2020/4/14
 * Email : 163235610@qq.com
 * Author:Mrczh
 * Instructions:发现
 */
public class FoundFragment extends BaseFragment {
    @BindView(R.id.img_advertising)
    ImageView imgAdvertising;
    @BindView(R.id.img_search)
    ImageView imgSearch;
    @BindView(R.id.img_edit)
    ImageView imgEdit;
    @BindView(R.id.view_pager)
    ViewPager viewPager;
    Unbinder unbinder;
    @BindView(R.id.id_tab01_info)
    TextView idTab01Info;
    @BindView(R.id.id_tab01)
    LinearLayout idTab01;
    @BindView(R.id.id_tab02_info)
    TextView idTab02Info;
    @BindView(R.id.id_tab02)
    LinearLayout idTab02;
    @BindView(R.id.id_tab03_info)
    TextView idTab03Info;
    @BindView(R.id.id_tab03)
    LinearLayout idTab03;
    @BindView(R.id.ll_tab)
    LinearLayout llTab;
    @BindView(R.id.id_tab_line1)
    ImageView idTabLine1;
    @BindView(R.id.id_tab_line2)
    ImageView idTabLine2;
    @BindView(R.id.id_tab_line3)
    ImageView idTabLine3;
    //屏幕的宽度
    private int screenWidth;

    private FragmentAdapter mAdapter;
    private List<Fragment> fragments = new ArrayList<Fragment>();
    private Resources res;

    @Override
    public int getContentViewId() {
        return R.layout.fragment_found;
    }

    @Override
    public void init(Bundle savedInstanceState) {
        res = getResources();

        initView();
        //初始化Adapter
        mAdapter = new FragmentAdapter(getFragmentManager(), fragments);

        viewPager.setAdapter(mAdapter);
        viewPager.addOnPageChangeListener(new TabOnPageChangeListener());
        viewPager.setCurrentItem(1);//选择某一页
    }

    private void initView() {

        idTab01.setOnClickListener(new TabOnClickListener(0));
        idTab02.setOnClickListener(new TabOnClickListener(1));
        idTab03.setOnClickListener(new TabOnClickListener(2));

        fragments.add(new AttentionFragment());
        fragments.add(new RecommendFragment());
        fragments.add(new TopicFragment());
    }


    @Override
    protected void dealLeackCanary() {

    }

    /**
     * 功能：点击主页TAB事件
     */
    public class TabOnClickListener implements View.OnClickListener {
        private int index = 0;

        public TabOnClickListener(int i) {
            index = i;
        }

        public void onClick(View v) {
            viewPager.setCurrentItem(index);//选择某一页
        }

    }

    /**
     * dp转换成px
     */
    public int dp2px(float dpValue) {
        float scale = getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
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

    @OnClick({R.id.img_advertising, R.id.img_search, R.id.img_edit})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.img_advertising:
                ARouter.getInstance().build(ACTIVITY_MAIN2_ADVERTISING).navigation();
                break;
            case R.id.img_search:
                ARouter.getInstance().build(ACTIVITY_MAIN2_SEARCH).navigation();
                break;
            case R.id.img_edit:
                new ConfirmPopWindow(getActivity()).showAtBottom(imgEdit);
                break;
        }
    }


    /**
     * 功能：Fragment页面改变事件
     */
    public class TabOnPageChangeListener implements ViewPager.OnPageChangeListener {

        //当滑动状态改变时调用
        public void onPageScrollStateChanged(int state) {

        }

        //当前页面被滑动时调用
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {


        }

        //当新的页面被选中时调用
        public void onPageSelected(int position) {
            //重置所有TextView的字体颜色
            resetTextView();
            switch (position) {
                case 0:
                    idTab01Info.setTextColor(res.getColor(R.color.black));
                    idTab01Info.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
                    idTabLine1.setVisibility(View.VISIBLE);
                    break;
                case 1:
                    idTab02Info.setTextColor(res.getColor(R.color.black));
                    idTab02Info.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
                    idTabLine2.setVisibility(View.VISIBLE);
                    break;
                case 2:
                    idTab03Info.setTextColor(res.getColor(R.color.black));
                    idTab03Info.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
                    idTabLine3.setVisibility(View.VISIBLE);
                    break;
            }
        }
    }


    private void resetTextView() {
        idTab01Info.setTextColor(res.getColor(R.color.color_666666));
        idTab02Info.setTextColor(res.getColor(R.color.color_666666));
        idTab03Info.setTextColor(res.getColor(R.color.color_666666));
        idTabLine1.setVisibility(View.GONE);
        idTabLine2.setVisibility(View.GONE);
        idTabLine3.setVisibility(View.GONE);
        idTab01Info.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
        idTab02Info.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
        idTab03Info.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
    }
}

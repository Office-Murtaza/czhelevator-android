package com.leo.afbaselibrary.uis.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.ViewGroup;

import com.leo.afbaselibrary.listeners.ITabContent;
import com.leo.afbaselibrary.listeners.ITabPager;
import com.leo.afbaselibrary.widgets.lazyViewPager.LazyFragmentPagerAdapter;

import java.util.List;

/**
 * Created by arvin on 2016/2/4 15:26
 * .
 */
public class TabPagerAdapter<T extends ITabPager> extends LazyFragmentPagerAdapter {
    private List<T> mList;
    private ITabContent tabContent;
    private Fragment mCurrentFragment;

    public TabPagerAdapter(FragmentManager fm, List<T> list, ITabContent tabContent) {
        super(fm);
        this.mList = list;
        this.tabContent = tabContent;
    }

    public void setList(List<T> mList) {
        this.mList = mList;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mList.get(position).getTitle();
    }

    @Override
    public int getCount() {
        return mList.size();
    }

//    @Override
//    public Fragment getItem(int position) {
//        return tabContent.getContent(position);
//    }

    @Override
    protected Fragment getItem(ViewGroup container, int position) {
        return tabContent.getContent(position);
    }

    //----------------------下面才是重点-----------------

    @Override
    public void setPrimaryItem(ViewGroup container, int position, Object object) {
        mCurrentFragment = (Fragment) object;
        super.setPrimaryItem(container, position, object);
    }


    public Fragment getCurrentFragment() {
        return mCurrentFragment;
    }
}

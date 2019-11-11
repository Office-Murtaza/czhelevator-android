package com.kingyon.elevator.entities;

import com.leo.afbaselibrary.listeners.ITabPager;

/**
 * Created by GongLi on 2017/9/11.
 * Email：lc824767150@163.com
 */

public class TabPagerEntity implements ITabPager {
    private CharSequence title;
    private CharSequence realTitle;
    private String type;

    public TabPagerEntity(CharSequence title, String type) {
        this.title = title;
        this.type = type;
    }

    public TabPagerEntity(CharSequence title, CharSequence realTitle, String type) {
        this.title = title;
        this.realTitle = realTitle;
        this.type = type;
    }

    public CharSequence getRealTitle() {
        return realTitle;
    }

    public void setRealTitle(CharSequence realTitle) {
        this.realTitle = realTitle;
    }

    public void setTitle(CharSequence title) {
        this.title = title;
    }

    @Override
    public CharSequence getTitle() {
        return title;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}

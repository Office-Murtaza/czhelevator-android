package com.czh.myversiontwo.uiutils;

import android.graphics.Color;

/**
 * @Created By Admin  on 2020/8/5
 * @Email : 163235610@qq.com
 * @Author:Mrczh
 * @Instructions:
 */
public class TipItem {
    private String title;

    private int textColor = Color.WHITE;

    public TipItem(String title) {
        this.title = title;
    }

    public TipItem(String title, int textColor) {
        this.title = title;

        this.textColor = textColor;
    }

    public String getTitle() {
        return title;
    }

    public int getTextColor() {
        return textColor;
    }

    public void setTextColor(int textColor) {
        this.textColor = textColor;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}

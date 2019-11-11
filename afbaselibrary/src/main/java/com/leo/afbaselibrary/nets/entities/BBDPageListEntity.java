package com.leo.afbaselibrary.nets.entities;

import java.util.List;

/**
 * created by arvin on 16/8/3 14:48
 * email：1035407623@qq.com
 */
public class BBDPageListEntity<T> {
    private List<T> data;

    public List<T> getData() {
        return data;
    }

    public void setData(List<T> data) {
        this.data = data;
    }
}

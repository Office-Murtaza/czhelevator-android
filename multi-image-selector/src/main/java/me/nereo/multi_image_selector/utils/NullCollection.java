package me.nereo.multi_image_selector.utils;

import java.io.Serializable;
import java.util.AbstractList;
import java.util.RandomAccess;

/**
 * Created by GongLi on 2018/10/24.
 * Email：lc824767150@163.com
 */

public class NullCollection extends AbstractList<Object>
        implements RandomAccess, Serializable {
    private static final long serialVersionUID = 5206887786441397812L;

    @Override
    public Object get(int index) {
        return null;
    }

    @Override
    public int size() {
        return 1;
    }

    public boolean contains(Object obj) {
        return null == obj;
    }

    private Object readResolve() {
        return null;
    }
}

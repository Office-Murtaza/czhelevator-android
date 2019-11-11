package me.nereo.multi_image_selector.utils;

import java.util.Collection;
import java.util.List;

/**
 * Created by GongLi on 2018/10/24.
 * Email：lc824767150@163.com
 */

public class CollectionUtils {
    public static final Collection NULL_COLLECTION = new NullCollection();

    public static final <T> Collection<T> nullCollection() {
        return (List<T>) NULL_COLLECTION;
    }
}

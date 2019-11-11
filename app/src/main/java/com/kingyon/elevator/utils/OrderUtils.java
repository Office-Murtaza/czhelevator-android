package com.kingyon.elevator.utils;

/**
 * Created by GongLi on 2018/9/20.
 * Email：lc824767150@163.com
 */

public class OrderUtils {
    private static OrderUtils orderUtils;
    private int nextStepStatus;

    private OrderUtils() {
    }

    public static OrderUtils getInstance() {
        if (orderUtils == null) {
            orderUtils = new OrderUtils();
        }
        return orderUtils;
    }
}

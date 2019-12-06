package com.kingyon.elevator.utils;

/**
 * Created By SongPeng  on 2019/12/2
 * Email : 1531603384@qq.com
 */
public class QuickClickUtils {

    private static final int MIN_CLICK_DELAY_TIME = 500;
    private static long lastClickTime=0L;

    public static boolean isFastClick() {
        boolean flag = false;
        long curClickTime = System.currentTimeMillis();
        if ((curClickTime - lastClickTime)<= MIN_CLICK_DELAY_TIME) {
            flag = true;
        }
        lastClickTime = curClickTime;
        return flag;
    }
}

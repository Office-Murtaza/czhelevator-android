package com.kingyon.elevator.utils;

import android.content.Context;

/**
 * Created by Administrator on 2017/11/2 0002.
 */

public class DensityUtil {
    private static Context appContext;

    public static void init(Context context) {
        appContext = context;
    }

    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    public static int dip2px(float dpValue) {
        final float scale = appContext.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    /**
     * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
     */
    public static int px2dip(float pxValue) {
        final float scale = appContext.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }


    /**
     * sp转换成px
     */
    private int sp2px(float spValue){
        float fontScale=appContext.getResources().getDisplayMetrics().scaledDensity;
        return (int) (spValue*fontScale+0.5f);
    }
    /**
     * px转换成sp
     */
    private int px2sp(float pxValue){
        float fontScale=appContext.getResources().getDisplayMetrics().scaledDensity;
        return (int) (pxValue/fontScale+0.5f);
    }
}

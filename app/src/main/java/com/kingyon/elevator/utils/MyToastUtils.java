package com.kingyon.elevator.utils;

import android.view.Gravity;

import com.blankj.utilcode.util.ToastUtils;

/**
 * Created By SongPeng  on 2019/12/16
 * Email : 1531603384@qq.com
 */
public class MyToastUtils {

    public static  void showShort(String tips) {
        ToastUtils.setGravity(Gravity.CENTER, 0, 0);
        ToastUtils.setMsgTextSize(16);
        ToastUtils.showShort(tips);
    }


    public static void showLength(String tips) {
        ToastUtils.setGravity(Gravity.CENTER, 0, 0);
        ToastUtils.setMsgTextSize(16);
        ToastUtils.showLong(tips);
    }

}

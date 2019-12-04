package com.kingyon.elevator.utils;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.kingyon.elevator.uis.activities.FragmentContainerActivity;

/**
 * Created By SongPeng  on 2019/11/25
 * Email : 1531603384@qq.com
 */
public class MyActivityUtils {

    /**
     * 跳转到对应的fragment界面
     *
     * @param context 上下文
     * @param tag     标志参数，跳转对应界面
     */
    public static void goFragmentContainerActivity(Context context, String tag) {
        try {
            Intent intent = new Intent(context, FragmentContainerActivity.class);
            intent.putExtra("tag", tag);
            context.startActivity(intent);
        } catch (Exception e) {
            ToastUtils.showShort("跳转失败，请重试");
            LogUtils.e("跳转失败：" + e.toString());
        }
    }

    /**
     * 跳转到对应的fragment界面
     *
     * @param context 上下文
     * @param tag     标志参数，跳转对应界面
     */
    public static void goFragmentContainerActivity(Context context, String tag, Boolean isRememberPwd) {
        try {
            Intent intent = new Intent(context, FragmentContainerActivity.class);
            intent.putExtra("tag", tag);
            intent.putExtra("isRememberPwd", isRememberPwd);
            context.startActivity(intent);
        } catch (Exception e) {
            ToastUtils.showShort("跳转失败，请重试");
            LogUtils.e("跳转失败：" + e.toString());
        }
    }

    /**
     * 跳转到对应的fragment界面
     *
     * @param context 上下文
     * @param tag     标志参数，跳转对应界面
     * @param from    来自于哪个界面
     */
    public static void goFragmentContainerActivity(Context context, String tag, String from) {
        try {
            Intent intent = new Intent(context, FragmentContainerActivity.class);
            intent.putExtra("tag", tag);
            intent.putExtra("from", from);
            context.startActivity(intent);
        } catch (Exception e) {
            ToastUtils.showShort("跳转失败，请重试");
            LogUtils.e("跳转失败：" + e.toString());
        }
    }

    /**
     * 带bundle参数的跳转FragmentContainer
     *
     * @param context
     * @param classes
     * @param bundle
     */
    public static void goActivity(Context context, Class classes, String tag, Bundle bundle) {
        try {
            Intent intent = new Intent(context, classes);
            intent.putExtra("tag", tag);
            intent.putExtras(bundle);
            context.startActivity(intent);
        } catch (Exception e) {
            ToastUtils.showShort("跳转失败，请重试");
            LogUtils.e("跳转失败：" + e.toString());
        }
    }

    /**
     * 带bundle参数的跳转
     *
     * @param context
     * @param classes
     * @param bundle
     */
    public static void goActivity(Context context, Class classes, Bundle bundle) {
        try {
            Intent intent = new Intent(context, classes);
            intent.putExtras(bundle);
            context.startActivity(intent);
        } catch (Exception e) {
            ToastUtils.showShort("跳转失败，请重试");
            LogUtils.e("跳转失败：" + e.toString());
        }
    }

    /**
     * 不带参数的跳转
     *
     * @param context
     * @param classes
     */
    public static void goActivity(Context context, Class classes) {
        try {
            Intent intent = new Intent(context, classes);
            context.startActivity(intent);
        } catch (Exception e) {
            ToastUtils.showShort("跳转失败，请重试");
            LogUtils.e("跳转失败：" + e.toString());
        }
    }

    /**
     * 带一个参数的跳转
     *
     * @param context
     * @param classes
     */
    public static void goActivity(Context context, Class classes, String value1) {
        try {
            Intent intent = new Intent(context, classes);
            intent.putExtra("value1", value1);
            context.startActivity(intent);
        } catch (Exception e) {
            ToastUtils.showShort("跳转失败，请重试");
            LogUtils.e("跳转失败：" + e.toString());
        }
    }

    /**
     * 带一个参数的跳转
     *
     * @param context
     * @param classes
     */
    public static void goActivity(Context context, Class classes, Bundle bundle, String value1) {
        try {
            Intent intent = new Intent(context, classes);
            intent.putExtras(bundle);
            intent.putExtra("value1", value1);
            context.startActivity(intent);
        } catch (Exception e) {
            ToastUtils.showShort("跳转失败，请重试");
            LogUtils.e("跳转失败：" + e.toString());
        }
    }

}

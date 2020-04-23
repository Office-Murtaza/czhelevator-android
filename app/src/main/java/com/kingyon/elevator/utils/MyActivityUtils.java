package com.kingyon.elevator.utils;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.kingyon.elevator.entities.NormalMessageEntity;
import com.kingyon.elevator.entities.NormalParamEntity;
import com.kingyon.elevator.uis.activities.FragmentContainerActivity;
import com.kingyon.elevator.uis.activities.NewsDetailsActivity;
import com.kingyon.elevator.uis.activities.OrderContainerActivity;
import com.kingyon.elevator.uis.activities.PhotoPickerActivity;
import com.kingyon.elevator.uis.activities.order.ConfirmOrderActivity;
import com.kingyon.elevator.videocrop.VideoEditorActivity;

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
        if (QuickClickUtils.isFastClick()){
        try {
            Intent intent = new Intent(context, FragmentContainerActivity.class);
            intent.putExtra("tag", tag);
            context.startActivity(intent);
        } catch (Exception e) {
            ToastUtils.showShort("跳转失败，请重试");
            LogUtils.e("跳转失败：" + e.toString());
        }
        }
    }

    /**
     * 跳转到对应的fragment界面
     *
     * @param context 上下文
     * @param tag     标志参数，跳转对应界面
     */
    public static void goFragmentContainerActivity(Context context, String tag, Boolean isRememberPwd) {
        if (QuickClickUtils.isFastClick()) {
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
    }

    /**
     * 跳转到对应的fragment界面
     *
     * @param context 上下文
     * @param tag     标志参数，跳转对应界面
     * @param from    来自于哪个界面
     */
    public static void goFragmentContainerActivity(Context context, String tag, String from) {
        if (QuickClickUtils.isFastClick()) {
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
    }

    /**
     * 带bundle参数的跳转FragmentContainer
     *
     * @param context
     * @param classes
     * @param bundle
     */
    public static void goActivity(Context context, Class classes, String tag, Bundle bundle) {
        if (QuickClickUtils.isFastClick()) {
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
    }

    /**
     * 带bundle参数的跳转
     *
     * @param context
     * @param classes
     * @param bundle
     */
    public static void goActivity(Context context, Class classes, Bundle bundle) {
        if (QuickClickUtils.isFastClick()) {
            try {
                Intent intent = new Intent(context, classes);
                intent.putExtras(bundle);
                context.startActivity(intent);
            } catch (Exception e) {
                ToastUtils.showShort("跳转失败，请重试");
                LogUtils.e("跳转失败：" + e.toString());
            }
        }
    }

    /**
     * 不带参数的跳转
     *
     * @param context
     * @param classes
     */
    public static void goActivity(Context context, Class classes) {
        if (QuickClickUtils.isFastClick()) {
            try {
                Intent intent = new Intent(context, classes);
                context.startActivity(intent);
            } catch (Exception e) {
                ToastUtils.showShort("跳转失败，请重试");
                LogUtils.e("跳转失败：" + e.toString());
            }
        }
    }

    /**
     * 带一个参数的跳转
     *
     * @param context
     * @param classes
     */
    public static void goActivity(Context context, Class classes, String value1) {
        if (QuickClickUtils.isFastClick()) {
            try {
                Intent intent = new Intent(context, classes);
                intent.putExtra("value1", value1);
                context.startActivity(intent);
            } catch (Exception e) {
                ToastUtils.showShort("跳转失败，请重试");
                LogUtils.e("跳转失败：" + e.toString());
            }
        }
    }

    /**
     * 带一个参数的跳转
     *
     * @param context
     * @param classes
     */
    public static void goActivity(Context context, Class classes, Bundle bundle, String value1) {
        if (QuickClickUtils.isFastClick()) {
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


    /**
     * 跳转到视频预览界面
     *
     * @param context
     * @param classes
     */
    public static void goPreviewVideoActivity(Context context, Class classes, String videoPath, long videoTime,int fromType) {
        if (QuickClickUtils.isFastClick()) {
            try {
                Intent intent = new Intent(context, classes);
                intent.putExtra("videoPath", videoPath);
                intent.putExtra("videoTime", videoTime);
                intent.putExtra("fromType", fromType);
                context.startActivity(intent);
            } catch (Exception e) {
                ToastUtils.showShort("跳转失败，请重试");
                LogUtils.e("跳转失败：" + e.toString());
            }
        }
    }

    /**
     * 跳转到订单确认界面
     *
     * @param context
     * @param path
     * @param resType 资源文件的类型  视频还是图片
     */
    public static void goConfirmOrderActivity(Context context, int fromType, String path, String resType) {
        if (QuickClickUtils.isFastClick()) {
            try {
                Intent intent = new Intent(context, ConfirmOrderActivity.class);
                intent.putExtra("path", path);
                intent.putExtra("fromType", fromType);
                intent.putExtra("resType", resType);
                context.startActivity(intent);
            } catch (Exception e) {
                ToastUtils.showShort("跳转失败，请重试");
                LogUtils.e("跳转失败：" + e.toString());
            }
        }
    }


    /**
     * 跳转到视频或图片选择界面
     *
     * @param context
     * @param fromType 来自于哪个界面
     * @param planType 广告计划的类型
     */
    public static void goPhotoPickerActivity(Context context, int fromType, String planType) {
        if (QuickClickUtils.isFastClick()) {
            try {
                Intent intent = new Intent(context, PhotoPickerActivity.class);
                intent.putExtra("fromType", fromType);
                intent.putExtra("planType", planType);
                context.startActivity(intent);
            } catch (Exception e) {
                ToastUtils.showShort("跳转失败，请重试");
                LogUtils.e("跳转失败：" + e.toString());
            }
        }
    }

    /**
     * 跳转到视频裁剪界面
     *
     * @param context
     * @param fromType 来自于哪个界面
     * @param planType 广告计划的类型
     */
    public static void goVideoEditorActivity(Context context, int fromType, String planType) {
        if (QuickClickUtils.isFastClick()) {
            try {
//            Intent intent = new Intent(context, EditVideoActivity.class);
                Intent intent = new Intent(context, VideoEditorActivity.class);
                intent.putExtra("fromType", fromType);
                intent.putExtra("planType", planType);
//            intent.putExtra("path",RuntimeUtils.selectVideoPath);
                context.startActivity(intent);
            } catch (Exception e) {
                ToastUtils.showShort("跳转失败，请重试");
                LogUtils.e("跳转失败：" + e.toString());
            }
        }
    }


    /**
     * 跳转到订单详情
     *
     * @param context
     */
    public static void goOrderCOntainerActivity(Context context, NormalParamEntity normalParamEntity) {
        if (QuickClickUtils.isFastClick()) {
            try {
                Intent intent = new Intent(context, OrderContainerActivity.class);
                intent.putExtra("normalEntity", normalParamEntity);
                context.startActivity(intent);
            } catch (Exception e) {
                ToastUtils.showShort("跳转失败，请重试");
                LogUtils.e("跳转失败：" + e.toString());
            }
        }
    }


    /**
     * 跳转新闻详情界面
     *
     * @param context
     */
    public static void goNewsDetailsActivity(Context context,int newsId) {
        if (QuickClickUtils.isFastClick()) {
            try {
                Intent intent = new Intent(context, NewsDetailsActivity.class);
                intent.putExtra("newsId", newsId);
                context.startActivity(intent);
            } catch (Exception e) {
                ToastUtils.showShort("跳转失败，请重试");
                LogUtils.e("跳转失败：" + e.toString());
            }
        }
    }
}

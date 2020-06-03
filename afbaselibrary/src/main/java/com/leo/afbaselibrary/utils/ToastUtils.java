package com.leo.afbaselibrary.utils;

import android.content.Context;
import android.view.Gravity;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.leo.afbaselibrary.R;

import java.lang.ref.WeakReference;


/**
 * 16/11/22
 * 17/06/14 modified by gongli
 *
 * @author zhengliao
 */

public class ToastUtils {
    private static WeakReference<Toast> mToastRefrence;

    public static void hide() {
        Toast toast;
        if (mToastRefrence != null) {
            toast = mToastRefrence.get();
            if (toast != null) {
                toast.cancel();
            }
        }
    }

    public static void toast(Context context, String text) {
        showToast(context, text, text.length() > 8 ? Toast.LENGTH_LONG : Toast.LENGTH_SHORT);
    }

    public static void toast(Context context, int textRes) {
        toast(context, context.getString(textRes));
    }


    public static void showToast(Context context, int resId, int duration) {
        showToast(context, context.getString(resId), duration);
    }

    public static void showToast(Context context, String text, int duration) {
        Toast toast = null;
        if (mToastRefrence == null) {
            toast = Toast.makeText(context, text, duration);
            mToastRefrence = new WeakReference<>(toast);
        }
        toast = mToastRefrence.get();
        if (toast == null) {
            toast = Toast.makeText(context, text, duration);

            mToastRefrence = new WeakReference<>(toast);
        } else {
            toast.setText(text);
            toast.setDuration(duration);
        }
        try {
            LinearLayout linearLayout = (LinearLayout) toast.getView();
            TextView messageTextView = (TextView) linearLayout.getChildAt(0);
            messageTextView.setTextSize(16);
        }catch (Exception e){

        }
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
    }


    public static void showToast(Context context, String text, int duration,int textSize) {
        Toast toast = null;
        if (mToastRefrence == null) {
            toast = Toast.makeText(context, text, duration);
            mToastRefrence = new WeakReference<>(toast);
        }
        toast = mToastRefrence.get();
        if (toast == null) {
            toast = Toast.makeText(context, text, duration);

            mToastRefrence = new WeakReference<>(toast);
        } else {
            toast.setText(text);
            toast.setDuration(duration);
        }
        try {
            LinearLayout linearLayout = (LinearLayout) toast.getView();
            TextView messageTextView = (TextView) linearLayout.getChildAt(0);
            messageTextView.setTextSize(textSize);
        }catch (Exception e){

        }
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
    }
}

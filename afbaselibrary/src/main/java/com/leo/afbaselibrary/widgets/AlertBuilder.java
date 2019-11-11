package com.leo.afbaselibrary.widgets;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;

/**
 * Created by GongLi on 2018/1/29.
 * Email：lc824767150@163.com
 */

public class AlertBuilder extends AlertDialog.Builder {
    public AlertBuilder(@NonNull Context context) {
        super(context);
    }

    public AlertBuilder(@NonNull Context context, int themeResId) {
        super(context, themeResId);
    }

    private CharSequence getSpanString(CharSequence text, int color, int size, boolean dip) {
        CharSequence realText;
        if (!TextUtils.isEmpty(text)) {
            SpannableString spannableString = new SpannableString(text);
            ForegroundColorSpan foregroundColorSpan = new ForegroundColorSpan(color);
            AbsoluteSizeSpan absoluteSizeSpan = new AbsoluteSizeSpan(size, dip);
            spannableString.setSpan(foregroundColorSpan, 0, spannableString.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            spannableString.setSpan(absoluteSizeSpan, 0, spannableString.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            realText = spannableString;
        } else {
            realText = text;
        }
        return realText;
    }

    public AlertBuilder setTitle(int resId, int color, int size) {
        return (AlertBuilder) super.setTitle(getSpanString(getContext().getString(resId), color, size, false));
    }

    public AlertBuilder setTitle(int resId, int color, int size, boolean dip) {
        return (AlertBuilder) super.setTitle(getSpanString(getContext().getString(resId), color, size, dip));
    }

    public AlertBuilder setTitle(@Nullable CharSequence title, int color, int size) {
        return (AlertBuilder) super.setTitle(getSpanString(title, color, size, false));
    }

    public AlertBuilder setTitle(@Nullable CharSequence title, int color, int size, boolean dip) {
        return (AlertBuilder) super.setTitle(getSpanString(title, color, size, dip));
    }

    public AlertBuilder setMessage(int messageId, int color, int size) {
        return (AlertBuilder) super.setMessage(getSpanString(getContext().getString(messageId), color, size, false));
    }

    public AlertBuilder setMessage(int messageId, int color, int size, boolean dip) {
        return (AlertBuilder) super.setMessage(getSpanString(getContext().getString(messageId), color, size, dip));
    }

    public AlertBuilder setMessage(@Nullable CharSequence message, int color, int size) {
        return (AlertBuilder) super.setMessage(getSpanString(message, color, size, false));
    }

    public AlertBuilder setMessage(@Nullable CharSequence message, int color, int size, boolean dip) {
        return (AlertBuilder) super.setMessage(getSpanString(message, color, size, dip));
    }

//    public AlertBuilder setPositiveButton(int textId, int color, int size, DialogInterface.OnClickListener listener) {
//        return (AlertBuilder) super.setPositiveButton(getSpanString(getContext().getString(textId), color, size, false), listener);
//    }
//
//    public AlertBuilder setPositiveButton(int textId, int color, int size, boolean dip, DialogInterface.OnClickListener listener) {
//        return (AlertBuilder) super.setPositiveButton(getSpanString(getContext().getString(textId), color, size, dip), listener);
//    }
//
//    public AlertBuilder setPositiveButton(CharSequence text, int color, int size, DialogInterface.OnClickListener listener) {
//        return (AlertBuilder) super.setPositiveButton(getSpanString(text, color, size, false), listener);
//    }
//
//    public AlertBuilder setPositiveButton(CharSequence text, int color, int size, boolean dip, DialogInterface.OnClickListener listener) {
//        return (AlertBuilder) super.setPositiveButton(getSpanString(text, color, size, dip), listener);
//    }
//
//    public AlertBuilder setNegativeButton(int textId, int color, int size, DialogInterface.OnClickListener listener) {
//        return (AlertBuilder) super.setNegativeButton(getSpanString(getContext().getString(textId), color, size, false), listener);
//    }
//
//    public AlertBuilder setNegativeButton(int textId, int color, int size, boolean dip, DialogInterface.OnClickListener listener) {
//        return (AlertBuilder) super.setNegativeButton(getSpanString(getContext().getString(textId), color, size, dip), listener);
//    }
//
//    public AlertBuilder setNegativeButton(CharSequence text, int color, int size, DialogInterface.OnClickListener listener) {
//        return (AlertBuilder) super.setNegativeButton(getSpanString(text, color, size, false), listener);
//    }
//
//    public AlertBuilder setNegativeButton(CharSequence text, int color, int size, boolean dip, DialogInterface.OnClickListener listener) {
//        return (AlertBuilder) super.setNegativeButton(getSpanString(text, color, size, dip), listener);
//    }
//
//    public AlertBuilder setNeutralButton(int textId, int color, int size, DialogInterface.OnClickListener listener) {
//        return (AlertBuilder) super.setNeutralButton(getSpanString(getContext().getString(textId), color, size, false), listener);
//    }
//
//    public AlertBuilder setNeutralButton(int textId, int color, int size, boolean dip, DialogInterface.OnClickListener listener) {
//        return (AlertBuilder) super.setNeutralButton(getSpanString(getContext().getString(textId), color, size, dip), listener);
//    }
//
//    public AlertBuilder setNeutralButton(CharSequence text, int color, int size, DialogInterface.OnClickListener listener) {
//        return (AlertBuilder) super.setNeutralButton(getSpanString(text, color, size, false), listener);
//    }
//
//    public AlertBuilder setNeutralButton(CharSequence text, int color, int size, boolean dip, DialogInterface.OnClickListener listener) {
//        return (AlertBuilder) super.setNeutralButton(getSpanString(text, color, size, dip), listener);
//    }
}

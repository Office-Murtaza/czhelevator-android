package com.leo.afbaselibrary.uis.adapters.holders;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.StrikethroughSpan;
import android.text.style.UnderlineSpan;
import android.text.util.Linkify;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.widget.Checkable;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;

import com.leo.afbaselibrary.utils.GlideUtils;

import java.util.List;


/**
 * created by arvin on 16/10/24 15:02
 * email：1035407623@qq.com
 */
public class CommonHolder extends RecyclerView.ViewHolder {
    private SparseArray<View> mViews;
    private View mConvertView;
    private Context mContext;
    public String videoPath;

    public CommonHolder(Context context, View itemView) {
        super(itemView);
        mContext = context;
        mConvertView = itemView;
        mViews = new SparseArray<View>();
    }


    public static CommonHolder createViewHolder(Context context, View itemView) {
        return new CommonHolder(context, itemView);
    }

    public static CommonHolder createViewHolder(Context context,
                                                ViewGroup parent, int layoutId) {
        View itemView = LayoutInflater.from(context).inflate(layoutId, parent,
                false);
        return new CommonHolder(context, itemView);
    }

    /**
     * 通过viewId获取控件
     */
    public <T extends View> T getView(int viewId) {
        View view = mViews.get(viewId);
        if (view == null) {
            view = mConvertView.findViewById(viewId);
            mViews.put(viewId, view);
        }
        return (T) view;
    }

    public View getConvertView() {
        return mConvertView;
    }


    /****以下为辅助方法*****/

    /**
     * 设置TextView的值
     */
    public CommonHolder setTextWithTag(int viewId, String text, List<String> tags, int color) {
        TextView tv = getView(viewId);
        if (tags != null && tags.size() >= 0) {
            StringBuilder stringBuilder = new StringBuilder();
            for (String tag : tags) {
                stringBuilder.append("#").append(tag == null ? "" : tag).append("# ");
            }
            SpannableString s = new SpannableString(stringBuilder + (text == null ? "" : text));
            ForegroundColorSpan f = new ForegroundColorSpan(color);
            s.setSpan(f, 0, stringBuilder.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            tv.setText(s);
        } else {
            tv.setText(text == null ? "" : text);
        }
        return this;
    }

    public CommonHolder setTextWithTagObj(int viewId, String text, List tags, int color) {
        TextView tv = getView(viewId);
        if (tags != null && tags.size() >= 0) {
            StringBuilder stringBuilder = new StringBuilder();
            for (Object tag : tags) {
                stringBuilder.append("#").append(tag == null ? "" : tag.toString()).append("# ");
            }
            SpannableString s = new SpannableString(stringBuilder + (text == null ? "" : text));
            ForegroundColorSpan f = new ForegroundColorSpan(color);
            s.setSpan(f, 0, stringBuilder.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            tv.setText(s);
        } else {
            tv.setText(text == null ? "" : text);
        }
        return this;
    }

    /**
     * 设置TextView的值
     */
    public CommonHolder setText(int viewId, String text) {
        TextView tv = getView(viewId);
        if (TextUtils.isEmpty(text)) {
            setVisible(viewId, false);
        } else {
            setVisible(viewId, true);
            tv.setText(text);
        }
        return this;
    }

    /**
     * 设置TextView的值
     */
    public CommonHolder setTextUnderLine(int viewId, String text) {
        TextView tv = getView(viewId);
        if (TextUtils.isEmpty(text)) {
            setVisible(viewId, false);
        } else {
            setVisible(viewId, true);
            SpannableString ss = new SpannableString(text);
            UnderlineSpan underlineSpan = new UnderlineSpan();
            ss.setSpan(underlineSpan, 0, text.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            tv.setText(ss);
        }
        return this;
    }

    /**
     * 设置TextView的值
     */
    public CommonHolder setPriceDeleteLine(int viewId, String text, boolean deleteLine) {
        TextView tv = getView(viewId);
        if (TextUtils.isEmpty(text)) {
            tv.setText("");
        } else {
            SpannableString ss = new SpannableString(String.format("￥%s", text));
            if (deleteLine) {
                ss.setSpan(new StrikethroughSpan(), 0, text.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
            tv.setText(ss);
        }
        return this;
    }

    /**
     * 设置TextView的值
     */
    public CommonHolder setText(int viewId, CharSequence text) {
        TextView tv = getView(viewId);
        if (TextUtils.isEmpty(text)) {
            setVisible(viewId, false);
        } else {
            setVisible(viewId, true);
            tv.setText(text);
        }
        return this;
    }

    public CommonHolder setTextNotHide(int viewId, CharSequence text) {
        TextView tv = getView(viewId);
        tv.setText(text != null ? text : "");
        return this;
    }

    public CommonHolder setTextSize(int viewId, int size) {
        TextView tv = getView(viewId);
        tv.setTextSize(size);
        return this;
    }

    public CommonHolder setWeight(int viewId, float weight) {
        View view = getView(viewId);
        ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
        if (layoutParams != null && layoutParams instanceof LinearLayout.LayoutParams) {
            ((LinearLayout.LayoutParams) layoutParams).weight = weight > 0 ? weight : 0;
            view.setLayoutParams(layoutParams);
        }
        return this;
    }

    public CommonHolder setPriceNotHide(int viewId, CharSequence text) {
        TextView tv = getView(viewId);
        tv.setText(String.format("￥%s", text != null ? text : ""));
        return this;
    }

    public CommonHolder setOrderItemPrice(int viewId, String twoFloat) {
        TextView tv = getView(viewId);
        if (!TextUtils.isEmpty(twoFloat)) {
            if (twoFloat.contains(".")) {
                SpannableString spannableString = new SpannableString(twoFloat);
                spannableString.setSpan(new AbsoluteSizeSpan(12, true), twoFloat.indexOf("."), twoFloat.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                tv.setText(spannableString);
            } else {
                tv.setText(twoFloat);
            }
        } else {
            tv.setText("");
        }
        return this;
    }

    public CommonHolder setSelected(int viewId, boolean selected) {
        View v = getView(viewId);
        v.setSelected(selected);
        return this;
    }

    public CommonHolder setEnabled(int viewId, boolean enabled) {
        View v = getView(viewId);
        v.setEnabled(enabled);
        return this;
    }

    public CommonHolder setTextDrawableTop(int viewId, int resId) {
        TextView tv = getView(viewId);
        Drawable drawable = getDrawable(resId);
        tv.setCompoundDrawables(null, drawable, null, null);
        return this;
    }

    public CommonHolder setTextDrawableRight(int viewId, int resId) {
        TextView tv = getView(viewId);
        tv.setCompoundDrawablesWithIntrinsicBounds(0, 0, resId, 0);
        return this;
    }

    public CommonHolder setTextDrawableLeft(int viewId, int resId) {
        TextView tv = getView(viewId);
        tv.setCompoundDrawablesWithIntrinsicBounds(resId, 0, 0, 0);
        return this;
    }

    @SuppressWarnings("ConstantConditions")
    @NonNull
    private Drawable getDrawable(int resId) {
        Drawable drawable = mContext.getResources().getDrawable(resId);
        drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
        return drawable;
    }

//    public CommonHolder setImage(int viewId, String imgUrl) {
//        ImageView view = getView(viewId);
//        GlideUtils.loadImage(mContext, imgUrl, view);
//        return this;
//    }
//
//    public CommonHolder setVideoImage(int viewId, String imgUrl) {
//        ImageView view = getView(viewId);
//        GlideUtils.loadVideoFrame(mContext, imgUrl, view);
//        return this;
//    }

    public CommonHolder setAgateImage(int viewId, String url, boolean isVideo) {
        ImageView view = getView(viewId);
        GlideUtils.loadAgateImage(mContext, url, isVideo, view);
        return this;
    }

    public CommonHolder setImage(int viewId, String url) {
        ImageView view = getView(viewId);
        GlideUtils.loadImage(mContext, url, view);
        return this;
    }

    public CommonHolder setVideoImage(int viewId, String url) {
        ImageView view = getView(viewId);
        GlideUtils.loadVideoFrame(mContext, url, view);
        return this;
    }

    public CommonHolder setLocalImage(int viewId, int imgRes) {
        ImageView view = getView(viewId);
        GlideUtils.loadLocalImage(mContext, imgRes, view);
        return this;
    }

    public CommonHolder setCircleImage(int viewId, String imgUrl) {
        ImageView view = getView(viewId);
        GlideUtils.loadCircleImage(mContext, imgUrl, view);
        return this;
    }

    public CommonHolder setRoundImage(int viewId, String imgUrl) {
        ImageView view = getView(viewId);
        GlideUtils.loadRoundImage(mContext, imgUrl, view, 4);
        return this;
    }

    public CommonHolder setRoundImage(int viewId, String imgUrl, int dp) {
        ImageView view = getView(viewId);
        GlideUtils.loadRoundImage(mContext, imgUrl, view, dp);
        return this;
    }

    public CommonHolder setAvatarImage(int viewId, String imgUrl) {
        ImageView view = getView(viewId);
        GlideUtils.loadAvatarImage(mContext, imgUrl, view);
        return this;
    }

    public CommonHolder setImageResource(int viewId, int resId) {
        ImageView view = getView(viewId);
        view.setImageResource(resId);
        return this;
    }

    public CommonHolder setImageBitmap(int viewId, Bitmap bitmap) {
        ImageView view = getView(viewId);
        view.setImageBitmap(bitmap);
        return this;
    }

    public CommonHolder setImageDrawable(int viewId, Drawable drawable) {
        ImageView view = getView(viewId);
        view.setImageDrawable(drawable);
        return this;
    }

    public CommonHolder setBackgroundColor(int viewId, int color) {
        View view = getView(viewId);
        view.setBackgroundColor(color);
        return this;
    }

    public CommonHolder setBackgroundRes(int viewId, int backgroundRes) {
        View view = getView(viewId);
        view.setBackgroundResource(backgroundRes);
        return this;
    }

    public CommonHolder setBackgroundDrawable(int viewId, Drawable drawable) {
        View view = getView(viewId);
        view.setBackgroundDrawable(drawable);
        return this;
    }

    public CommonHolder setTextColor(int viewId, int textColor) {
        TextView view = getView(viewId);
        view.setTextColor(textColor);
        return this;
    }

    public CommonHolder setTextColorRes(int viewId, int textColorRes) {
        TextView view = getView(viewId);
        view.setTextColor(mContext.getResources().getColor(textColorRes));
        return this;
    }

    @SuppressLint("NewApi")
    public CommonHolder setAlpha(int viewId, float value) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            getView(viewId).setAlpha(value);
        } else {
            // Pre-honeycomb hack to set Alpha value
            AlphaAnimation alpha = new AlphaAnimation(value, value);
            alpha.setDuration(0);
            alpha.setFillAfter(true);
            getView(viewId).startAnimation(alpha);
        }
        return this;
    }

    public CommonHolder setVisible(int viewId, boolean visible) {
        View view = getView(viewId);
        view.setVisibility(visible ? View.VISIBLE : View.GONE);
        return this;
    }

    public CommonHolder linkify(int viewId) {
        TextView view = getView(viewId);
        Linkify.addLinks(view, Linkify.ALL);
        return this;
    }

    public CommonHolder setTypeface(Typeface typeface, int... viewIds) {
        for (int viewId : viewIds) {
            TextView view = getView(viewId);
            view.setTypeface(typeface);
            view.setPaintFlags(view.getPaintFlags() | Paint.SUBPIXEL_TEXT_FLAG);
        }
        return this;
    }

    public CommonHolder setProgress(int viewId, int progress) {
        ProgressBar view = getView(viewId);
        view.setProgress(progress);
        return this;
    }

    public CommonHolder setProgress(int viewId, int progress, int max) {
        ProgressBar view = getView(viewId);
        view.setMax(max);
        view.setProgress(progress);
        return this;
    }

    public CommonHolder setMax(int viewId, int max) {
        ProgressBar view = getView(viewId);
        view.setMax(max);
        return this;
    }

    public CommonHolder setRating(int viewId, float rating) {
        RatingBar view = getView(viewId);
        view.setRating(rating);
        return this;
    }

    public CommonHolder setRating(int viewId, float rating, int max) {
        RatingBar view = getView(viewId);
        view.setMax(max);
        view.setRating(rating);
        return this;
    }

    public CommonHolder setTag(int viewId, Object tag) {
        View view = getView(viewId);
        view.setTag(tag);
        return this;
    }

    public CommonHolder setTag(int viewId, int key, Object tag) {
        View view = getView(viewId);
        view.setTag(key, tag);
        return this;
    }

    public CommonHolder setChecked(int viewId, boolean checked) {
        Checkable view = (Checkable) getView(viewId);
        view.setChecked(checked);
        return this;
    }

    /**
     * 关于事件的
     */
    public CommonHolder setOnClickListener(int viewId,
                                           View.OnClickListener listener) {
        View view = getView(viewId);
        view.setOnClickListener(listener);
        return this;
    }

    public CommonHolder setOnTouchListener(int viewId,
                                           View.OnTouchListener listener) {
        View view = getView(viewId);
        view.setOnTouchListener(listener);
        return this;
    }

    public CommonHolder setOnLongClickListener(int viewId,
                                               View.OnLongClickListener listener) {
        View view = getView(viewId);
        view.setOnLongClickListener(listener);
        return this;
    }
}

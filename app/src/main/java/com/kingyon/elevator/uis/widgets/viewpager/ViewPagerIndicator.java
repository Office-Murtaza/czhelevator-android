package com.kingyon.elevator.uis.widgets.viewpager;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.view.ViewPager;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by gongli on 2017/5/18 14:49
 * email: lc824767150@163.com
 * ViewPager指示器
 * 以一个LinearLayout为父容器，在里面加入指定个数大小和间距的CheckedTextView，指定的selector实现指示器功能
 * selector的形状决定了指示器单元的形状，不一定是小圆点，同时可以对外提供监听
 */

public class ViewPagerIndicator {
    private Context context;
    private ViewPager viewPager;
    //圆点数量
    private int dotNum;
    //圆点父容器
    private LinearLayout dotContainer;
    //统一图片选择器
    private int selectorDrawable;
    //个性化每一单云的图片选择器
    private int[] selectorDrawables;
    //宽高和间距属性
    private float dotWidth;
    private float dotHeight;
    private float margin;
    private List<IndicatorDefultView> indicatorDefultViews;
    //指示器状态改变监听实例
    private OnIndicatorSelectedChangedListener onIndicatorSelectedChangedListener;
    //设置是否需要指示器与ViewPager相互联动
    private boolean linkage;
    //如果需要的话设置文字
    private String[] titleTexts;
    //如果需要的话设置文字颜色选择器
    private int textColorResources;
    //设置是否宽高关联铺满父容器分散居中(一定要在设置宽高前进行设置)
    private boolean isDistributedCenter;

//    /**
//     * @param context           上下文对象
//     * @param viewPager         关联的ViewPager
//     * @param dotContainer      指示器的容器LinearLayout
//     * @param dotNum            指示器单元数量 ViewPager页数
//     * @param selectorDrawables 个性化图片选择器
//     */
//    protected ViewPagerIndicator(Context context, ViewPager viewPager, LinearLayout dotContainer
//            , int dotNum, int[] selectorDrawables) {
//        this(context, viewPager, dotContainer, dotNum);
//        this.selectorDrawables = selectorDrawables;
//    }
//
//    /**
//     * @param context
//     * @param viewPager        关联的ViewPager
//     * @param dotContainer     指示器的容器LinearLayout
//     * @param dotNum           指示器单元数量 ViewPager页数
//     * @param selectorDrawable 图片选择器
//     */
//    protected ViewPagerIndicator(Context context, ViewPager viewPager, LinearLayout dotContainer
//            , int dotNum, int selectorDrawable) {
//        this(context, viewPager, dotContainer, dotNum);
//        this.selectorDrawable = selectorDrawable;
//    }

    /**
     * @param context      上下文对象
     * @param viewPager    关联的ViewPager
     * @param dotContainer 指示器的容器LinearLayout
     * @param dotNum       指示器单元数量 ViewPager页数
     */
    protected ViewPagerIndicator(Context context, ViewPager viewPager, LinearLayout dotContainer
            , int dotNum) {
        if (dotNum == 0) {
            dotNum = 1;
        }
        this.context = context;
        this.dotNum = dotNum;
        this.viewPager = viewPager;
        this.dotContainer = dotContainer;
        dotContainer.setGravity(Gravity.CENTER);
        dotContainer.setOrientation(LinearLayout.HORIZONTAL);
    }

    protected ViewPagerIndicator create() {
        if (dotContainer != null) {
            dotContainer.postDelayed(new Runnable() {
                @Override
                public void run() {
                    setDotHeightByPx(dotHeight);
                    if (dotWidth > 0) {
                        setDotWidthByPx(dotWidth);
                    }
                    if (margin > 0) {
                        setMarginByPx(margin);
                    }
                    addIndicatorDefultView();
                    viewPager.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
                        @Override
                        public void onPageSelected(int position) {
                            super.onPageSelected(position);
                            dotCheckedChange(position % dotNum);
                            if (onIndicatorSelectedChangedListener != null) {
                                onIndicatorSelectedChangedListener.onIndicatorSelectedChanged(position % dotNum);
                            }
                        }
                    });
                    dotCheckedChange(viewPager.getCurrentItem() % dotNum);
                }
            }, 50);
        }
        return this;
    }

    protected void addIndicatorDefultView() {
        dotContainer.removeAllViews();
        indicatorDefultViews = new ArrayList<>();
        //建立公共的宽高间距等属性
        int width = dotWidth == 0 ? LinearLayout.LayoutParams.MATCH_PARENT : (int) dotWidth;
        int height = dotHeight == 0 ? LinearLayout.LayoutParams.WRAP_CONTENT : (int) dotHeight;
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(width, height);
        params.setMargins((int) (margin / 2), 0, (int) (margin / 2), 0);
        for (int i = 0; i < dotNum; i++) {
            //设置属性并添加到父容器和集合进行管理
            IndicatorDefultView indicatorDefultView = new IndicatorDefultView(context);
            indicatorDefultView.setLayoutParams(params);
            indicatorDefultView.setGravity(Gravity.CENTER_HORIZONTAL);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                indicatorDefultView.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
            }
            dotContainer.addView(indicatorDefultView);
            indicatorDefultViews.add(indicatorDefultView);
            if (selectorDrawable != 0) {
                indicatorDefultView.setBackgroundResource(selectorDrawable);
                indicatorDefultView.setHasSelector(true);
            } else if (selectorDrawables != null) {
//                indicatorDefultView.setBackgroundResource(selectorDrawables[i]);
                Drawable drawable = context.getResources().getDrawable(selectorDrawables[i]);
                drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
                indicatorDefultView.setCompoundDrawables(null, drawable, null, null);
                indicatorDefultView.setHasSelector(true);
                if (titleTexts != null) {
                    indicatorDefultView.setText(titleTexts[i]);
                    if (textColorResources != 0) {
                        ColorStateList colorStateList = context.getResources()
                                .getColorStateList(textColorResources);
                        indicatorDefultView.setTextColor(colorStateList);
                    }
                }
            }
            //如果需要指示器与ViewPager相互关联则设置单击改变状态切换ViewPager的部分
            if (linkage) {
                indicatorDefultView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (indicatorDefultViews.get(viewPager.getCurrentItem() % dotNum) != v) {
                            for (int j = 0; j < dotNum; j++) {
                                if (indicatorDefultViews.get(j) == v) {
                                    indicatorDefultViews.get(j).setChecked(true);
                                    viewPager.setCurrentItem(j);
                                } else
                                    indicatorDefultViews.get(j).setChecked(false);
                            }
                        }
                    }
                });
            }
        }
    }

    /**
     * 响应指示器状态改变
     *
     * @param position 下标
     */
    protected void dotCheckedChange(int position) {
        for (int i = 0; i < indicatorDefultViews.size(); i++) {
            if (i == position)
                indicatorDefultViews.get(i).setChecked(true);
            else
                indicatorDefultViews.get(i).setChecked(false);
        }
    }

    /**
     * 设置单元指示器的高度
     *
     * @param dotHeight 单位 px 单元高度
     */
    protected void setDotHeightByPx(float dotHeight) {
        this.dotHeight = dotHeight;
    }

    /**
     * 设置指示器单元的宽度，同时根据总宽度铺满和居中原则调整间距
     *
     * @param dotWidth 单位 px 指示器单元宽度
     */
    protected void setDotWidthByPx(float dotWidth) {
        this.dotWidth = dotWidth;
        if (isDistributedCenter) {
            int width = dotContainer.getWidth();
            if (width == 0) {
                dotContainer.measure(0, 0);
                width = dotContainer.getMeasuredWidth();
            }
            if (width > 0) {
                this.margin = width / dotNum - dotWidth;
            }
        }
    }

    /**
     * 设置指示器单元的间距，同时根据总宽度铺满和居中原则调整宽度
     *
     * @param margin 单位 px 间距
     */
    protected void setMarginByPx(float margin) {
        this.margin = margin;
        if (isDistributedCenter) {
            int width = dotContainer.getWidth();
            if (width == 0) {
                dotContainer.measure(0, 0);
                width = dotContainer.getMeasuredWidth();
            }
            if (width > 0) {
                this.dotWidth = width / dotNum - margin;
            }
        }
    }

    /**
     * 外部如果需要监听指示器的状态变化则可以实现此接口并设置到本指示器
     */
    public interface OnIndicatorSelectedChangedListener {
        void onIndicatorSelectedChanged(int position);
    }

    public static class Builder {
        private Context mContext;
        private ViewPager mViewPager;
        private LinearLayout dotContainer;
        private int childNum;
        private int margin;
        private int width;
        private int height;
        private int selectorDrawable;
        private int[] selectorDrawables;
        private OnIndicatorSelectedChangedListener mOnIndicatorSelectedChangedListener;
        private boolean linkage;
        private String[] titleTexts;
        private int textColorResources;
        private boolean isDistributedCenter;

        public Builder(@NonNull Context context, @NonNull ViewPager viewPager, @NonNull LinearLayout dotContainer
                , int dotNum) {
            mContext = context;
            mViewPager = viewPager;
            this.dotContainer = dotContainer;
            childNum = dotNum;
            if (mContext == null || mViewPager == null || dotContainer == null) {
                throw new IllegalArgumentException("传递参数有误,请检查传递的参数中是否包含null");
            }
        }

        /**
         * 设置指示器单元的间距，同时根据总宽度铺满和居中原则调整宽度
         *
         * @param margin 单位 dp 间距
         */
        public Builder setMarginByDp(float margin) {
            this.margin = dip2px(margin);
            return this;
        }

        /**
         * 设置单元指示器的高度
         *
         * @param dotHeight 单位 dp 单元高度
         */
        public Builder setDotHeightByDp(float dotHeight) {
            height = dip2px(dotHeight);
            return this;
        }


        /**
         * 设置指示器单元的宽度，同时根据总宽度铺满和居中原则调整间距
         *
         * @param dotWidth 单位 dp 指示器单元宽度
         */
        public Builder setDotWidthByDp(float dotWidth) {
            width = dip2px(dotWidth);
            return this;
        }

        public Builder setSelectorDrawable(int selectorDrawable) {
            this.selectorDrawable = selectorDrawable;
            return this;
        }

        public Builder setSelectorDrawables(int[] selectorDrawables) {
            this.selectorDrawables = selectorDrawables;
            return this;
        }

        /**
         * 设置监听的方法
         *
         * @param onIndicatorSelectedChangedListener 监听器
         */
        public Builder setOnIndicatorSelectedChangedListener(OnIndicatorSelectedChangedListener
                                                                     onIndicatorSelectedChangedListener) {
            this.mOnIndicatorSelectedChangedListener = onIndicatorSelectedChangedListener;
            return this;
        }

        /**
         * 设置指示器是否与ViewPager联动
         *
         * @param linkage
         */
        public Builder setLinkage(boolean linkage) {
            this.linkage = linkage;
            return this;
        }

        /**
         * 本方法可以设置文字
         *
         * @param titleTexts
         */
        public Builder setTitle(String[] titleTexts, int textColorResources) {
            this.titleTexts = titleTexts;
            this.textColorResources = textColorResources;
            return this;
        }

        /**
         * 设置是否宽高关联铺满父容器分散居中(一定要在设置宽高前进行设置,如果容器或其父容器处于GONE状态会导致错乱)
         *
         * @param distributedCenter
         */
        public Builder setDistributedCenter(boolean distributedCenter) {
            this.isDistributedCenter = distributedCenter;
            return this;
        }

        public ViewPagerIndicator build() {
            ViewPagerIndicator indicator = new ViewPagerIndicator(mContext, mViewPager, dotContainer, childNum);
            if (selectorDrawable != 0) {
                indicator.selectorDrawable = selectorDrawable;
            }
            if (selectorDrawables != null) {
                if (selectorDrawables.length == childNum) {
                    indicator.selectorDrawables = selectorDrawables;
                } else {
                    throw new IllegalArgumentException("指示器图片集与ViewPager真实页数不符");
                }
            }
            indicator.onIndicatorSelectedChangedListener = mOnIndicatorSelectedChangedListener;
            if (titleTexts != null) {
                if (titleTexts.length == childNum) {
                    indicator.titleTexts = titleTexts;
                } else {
                    throw new IllegalArgumentException("指示器文字集与ViewPager真实页数不符");
                }
            }
            indicator.textColorResources = textColorResources;
            indicator.isDistributedCenter = isDistributedCenter;
            indicator.dotHeight = height;
            indicator.margin = margin;
            indicator.dotWidth = width;
            indicator.linkage = this.linkage;
            return indicator.create();
        }

        /**
         * dip转px
         */
        private int dip2px(float dpValue) {
            float scale = mContext.getResources().getDisplayMetrics().density;
            return (int) (dpValue * scale + 0.5f);
        }

        /**
         * px转dip
         */
        private int px2dip(float pxValue) {
            float scale = mContext.getResources().getDisplayMetrics().density;
            return (int) (pxValue / scale + 0.5f);
        }
    }
}

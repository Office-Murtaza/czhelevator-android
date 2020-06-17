package com.kingyon.elevator.uis.widgets;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;

import com.leo.afbaselibrary.utils.ScreenUtil;
import com.leo.afbaselibrary.widgets.PagerSlidingTabStrip;

/**
 * Created by lc on 2017/11/4.
 */

public class RoundPagerSlidingTabStrip extends PagerSlidingTabStrip {

    private int lineMargin = 0;

    public RoundPagerSlidingTabStrip(Context context) {
        super(context);
    }

    public RoundPagerSlidingTabStrip(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public RoundPagerSlidingTabStrip(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected void drawSelf(Canvas canvas) {
        if (isInEditMode() || tabCount == 0) {
            return;
        }

        final int height = getHeight();

        // draw underline
        if (clear) {
            rectPaint.setColor(Color.WHITE);
        } else {
            rectPaint.setColor(underlineColor);
        }


        canvas.drawRect(0, height - underlineHeight, tabsContainer.getWidth(),
                height, rectPaint);

        // draw indicator line
        if (clear) {
            rectPaint.setColor(Color.WHITE);
        } else {
            rectPaint.setColor(indicatorColor);
        }

        // default: line below current tab
        TextView currentTab = (TextView) tabsContainer.getChildAt(currentPosition);

//        int textLength = currentTab.getText().length() * ScreenUtil.sp2px(16);
//        int textLength = currentTab.getText().length() * tabTextSize;
//        int textLength = 2 * tabTextSize;
        int textLength = (int) currentTab.getPaint().measureText(currentTab.getText().toString());
//        float lineLeft = (currentTab.getRight() - currentTab.getLeft() - textLength) / 2 + ScreenUtil.dp2px(4) + currentTab.getLeft();
//        float lineRight = lineLeft + textLength - ScreenUtil.dp2px(4) * 2;
        float lineLeft = (currentTab.getRight() - currentTab.getLeft() - textLength) / 2 + currentTab.getLeft();
        float lineRight = lineLeft + textLength;
//        if (tabAddWay == TabAddWay.ITEM_MATCH) {
//            lineLeft = currentTab.getLeft();
//            lineRight = currentTab.getRight();
//        }

        // if there is an offset, start interpolating left and right coordinates
        // between current and next tab
        if (currentPositionOffset > 0f && currentPosition < tabCount - 1) {

            View nextTab = tabsContainer.getChildAt(currentPosition + 1);
            float nextTabLeft = (nextTab.getRight() - nextTab.getLeft() - textLength) / 2 + ScreenUtil.dp2px(4) + nextTab.getLeft();
            float nextTabRight = nextTabLeft + textLength - ScreenUtil.dp2px(4) * 2;

//            if (tabAddWay == TabAddWay.ITEM_MATCH) {
//                nextTabLeft = nextTab.getLeft();
//                nextTabRight = nextTab.getRight();
//            }


            lineLeft = (currentPositionOffset * nextTabLeft + (1f - currentPositionOffset)
                    * lineLeft);
            lineRight = (currentPositionOffset * nextTabRight + (1f - currentPositionOffset)
                    * lineRight);
        }

        canvas.drawRoundRect(new RectF(lineLeft, height - indicatorHeight - lineMargin, lineRight
                , height - lineMargin), indicatorHeight / 2, indicatorHeight / 2, rectPaint);
//        canvas.drawRect(lineLeft, height - indicatorHeight, lineRight, height,
//                rectPaint);

        // draw divider

        if (clear) {
            dividerPaint.setColor(Color.WHITE);
        } else {
            dividerPaint.setColor(dividerColor);
        }
        if (isDrawDivider) {
            for (int i = 0; i < tabCount - 1; i++) {
                View tab = tabsContainer.getChildAt(i);
                canvas.drawLine(tab.getRight(), dividerPadding, tab.getRight(),
                        height - dividerPadding, dividerPaint);
            }
        }
    }

//    protected void updateTabStyles() {
//        for (int i = 0; i < tabCount; i++) {
//
//            View v = tabsContainer.getChildAt(i);
//
//            v.setBackgroundResource(tabBackgroundResId);
//
//            if (v instanceof TextView) {
//
//                TextView tab = (TextView) v;
//
//                // setAllCaps() is only available from API 14, so the upper case
//                // is made manually if we are on a
//                // pre-ICS-build
//                if (textAllCaps) {
//                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
//                        tab.setAllCaps(true);
//                    } else {
//                        tab.setText(tab.getText().toString()
//                                .toUpperCase(locale));
//                    }
//                }
//                String text = tab.getText().toString();
//                Pattern patternNumber = Pattern.compile("\\d+\\s\\S+\\s\\d+");
//                boolean hasNumber = patternNumber.matcher(text).find();
//                int firstSpaceIndex = text.indexOf(" ");
//                int lastSpaceIndex = text.lastIndexOf(" ");
//
//
//                if (i == selectedPosition && !clear) {
//                    if (hasNumber) {
//                        SpannableString spannableString = getSpannableString(text, firstSpaceIndex, lastSpaceIndex);
//                        spannableString.setSpan(new StyleSpan(Typeface.BOLD), firstSpaceIndex, lastSpaceIndex, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
//                        tab.setText(spannableString);
//                    } else {
//                        tab.setTypeface(tabTypeface, Typeface.BOLD);
//                    }
////                    tab.setTypeface(tabTypeface, Typeface.BOLD);
//                    tab.setTextSize(TypedValue.COMPLEX_UNIT_PX, tabTextSize + ScreenUtil.sp2px(1));
//                    tab.setTextColor(selectedTabTextColor);
////                    if (pager.getAdapter() != null) {
////                        tab.setText(pager.getAdapter().getPageTitle(i));
////                    }
//                } else {
//                    if (hasNumber) {
//                        SpannableString spannableString = getSpannableString(text, firstSpaceIndex, lastSpaceIndex);
//                        spannableString.setSpan(new StyleSpan(tabTypefaceStyle), firstSpaceIndex, lastSpaceIndex, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
//                        tab.setText(spannableString);
//                    } else {
//                        tab.setTypeface(tabTypeface, tabTypefaceStyle);
//                    }
////                    tab.setTypeface(tabTypeface, tabTypefaceStyle);
//                    tab.setTextSize(TypedValue.COMPLEX_UNIT_PX, tabTextSize);
//                    tab.setTextColor(tabTextColor);
////                    if (pager.getAdapter() != null) {
////                        tab.setText(pager.getAdapter().getPageTitle(i));
////                    }
//                }
//            }
//        }
//
//    }

//    @NonNull
//    private SpannableString getSpannableString(String text, int firstSpaceIndex, int lastSpaceIndex) {
//        Pattern patternNotZero = Pattern.compile("[1-9]{1}");
//        boolean notZero = patternNotZero.matcher(text).find();
//
//        SpannableString spannableString = new SpannableString(text);
//        if (notZero) {
//            spannableString.setSpan(new ForegroundColorSpan(0xFF9B9B9B), lastSpaceIndex, text.length(), SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE);
//            spannableString.setSpan(new AbsoluteSizeSpan(12, true), lastSpaceIndex, text.length(), SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE);
//            spannableString.setSpan(new ForegroundColorSpan(0x00FFFFFF), 0, firstSpaceIndex, SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE);
//            spannableString.setSpan(new AbsoluteSizeSpan(12, true), 0, firstSpaceIndex, SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE);
////            spannableString.setSpan(new StyleSpan(Typeface.NORMAL), total.lastIndexOf(numberText), total.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
//        } else {
//            spannableString.setSpan(new ForegroundColorSpan(0x00FFFFFF), lastSpaceIndex, text.length(), SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE);
//            spannableString.setSpan(new AbsoluteSizeSpan(12, true), lastSpaceIndex, text.length(), SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE);
//            spannableString.setSpan(new ForegroundColorSpan(0x00FFFFFF), 0, firstSpaceIndex, SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE);
//            spannableString.setSpan(new AbsoluteSizeSpan(12, true), 0, firstSpaceIndex, SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE);
//        }
//        return spannableString;
//    }
}

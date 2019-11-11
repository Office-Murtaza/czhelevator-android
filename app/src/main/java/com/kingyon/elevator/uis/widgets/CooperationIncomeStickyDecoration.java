package com.kingyon.elevator.uis.widgets;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.text.TextPaint;
import android.text.TextUtils;
import android.view.View;

import com.kingyon.elevator.R;
import com.kingyon.elevator.utils.CommonUtil;
import com.leo.afbaselibrary.utils.ScreenUtil;

/**
 * Created by GongLi on 2019/1/14.
 * Email：lc824767150@163.com
 */

public class CooperationIncomeStickyDecoration extends RecyclerView.ItemDecoration {

    private Context mContext;
    private GroupStickyListener mDepositGroupListener;
    private int mGroupHeight;
    private int mGroupTop;
    private int mLeftMargin;
    private Paint mGroutPaint;
    private Paint mTextPaint;
    private int dividerHeight;
    private boolean showTotal;

    public CooperationIncomeStickyDecoration(Context context, GroupStickyListener depositGroupListener, boolean showTotal) {
        super();
        mContext = context;
        dividerHeight = mContext.getResources().getDimensionPixelOffset(R.dimen.spacing_divider);
        this.showTotal = showTotal;
        mDepositGroupListener = depositGroupListener;
        mGroupHeight = ScreenUtil.dp2px(40);
//        mGroupTop = ScreenUtil.dp2px(48) + StatusBarUtil.getStatusBarHeight(context) + mGroupHeight;
        mGroupTop = mGroupHeight;
        mLeftMargin = ScreenUtil.dp2px(16);
        mGroutPaint = new Paint();
        mGroutPaint.setAntiAlias(true);
        mGroutPaint.setStyle(Paint.Style.FILL);
        mTextPaint = new TextPaint();
        mTextPaint.setAntiAlias(true);
        mTextPaint.setColor(0xFFABABAB);
        mTextPaint.setTextSize(context.getResources().getDimensionPixelSize(R.dimen.txt_fifteen));
        mTextPaint.setTypeface(Typeface.create(Typeface.SANS_SERIF, Typeface.BOLD));
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);
        int pos = parent.getChildAdapterPosition(view);
        String groupId = mDepositGroupListener.getGroupName(pos);
        if (groupId == null) return;
        //只有是同一组的第一个才显示悬浮栏
        if (mDepositGroupListener.isFirstInGroup(pos)) {
            outRect.top = mGroupHeight;
        }
    }

    @Override
    public void onDrawOver(Canvas c, RecyclerView parent, RecyclerView.State state) {
        super.onDrawOver(c, parent, state);
        final int itemCount = state.getItemCount();
        final int childCount = parent.getChildCount();
        final int left = parent.getLeft() + parent.getPaddingLeft();
        final int right = parent.getRight() - parent.getPaddingRight();
        String preGroupName;      //标记上一个item对应的Group
        String currentGroupName = null;       //当前item对应的Group
        for (int i = 0; i < childCount; i++) {
            View view = parent.getChildAt(i);
            int position = parent.getChildAdapterPosition(view);
            preGroupName = currentGroupName;
            currentGroupName = mDepositGroupListener.getGroupName(position);
            if (currentGroupName == null || TextUtils.equals(currentGroupName, preGroupName))
                continue;
            int viewBottom = view.getBottom();
            float top = Math.max(mGroupTop, view.getTop());//top 决定当前顶部第一个悬浮Group的位置
            if (position + 1 < itemCount) {
                //获取下个GroupName
                String nextGroupName = mDepositGroupListener.getGroupName(position + 1);
                //下一组的第一个View接近头部
                if (!currentGroupName.equals(nextGroupName) && viewBottom < top) {
                    top = viewBottom;
                }
            }
            //根据top绘制group
            mGroutPaint.setColor(ContextCompat.getColor(mContext, R.color.white_normal));
            c.drawRect(left, top - mGroupHeight, right, top, mGroutPaint);

            mGroutPaint.setColor(ContextCompat.getColor(mContext, R.color.black_divider));
            c.drawRect(left + mLeftMargin, top - dividerHeight, right - mLeftMargin, top, mGroutPaint);

            Bitmap icon = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.ic_sticky_calendar);
            RectF rectF = new RectF(left + mLeftMargin, top - (mGroupHeight + ScreenUtil.dp2px(18)) / 2, left + mLeftMargin + ScreenUtil.dp2px(18), top - (mGroupHeight - ScreenUtil.dp2px(18)) / 2);
            c.drawBitmap(icon, null, rectF, null);

            Paint.FontMetrics fm = mTextPaint.getFontMetrics();
            //文字竖直居中显示
            float baseLine = top - (mGroupHeight - (fm.bottom - fm.top)) / 2 - fm.bottom;
            c.drawText(currentGroupName, left + mLeftMargin + ScreenUtil.dp2px(30), baseLine, mTextPaint);

            if (showTotal) {
                String total = String.format("合计：%s(元)", CommonUtil.getTwoFloat(mDepositGroupListener.getTotal()));
                int limit = (int) (left + mLeftMargin + ScreenUtil.dp2px(30) + mTextPaint.measureText(currentGroupName) + ScreenUtil.dp2px(10));
                int totalLeft = (int) (right - mTextPaint.measureText(total) - ScreenUtil.dp2px(16));
                if (totalLeft < limit) {
                    totalLeft = limit;
                }
                c.drawText(total, totalLeft, baseLine, mTextPaint);
            }
        }
    }

    public interface GroupStickyListener {
        boolean isFirstInGroup(int pos);

        String getGroupName(int position);

        double getTotal();
    }
}
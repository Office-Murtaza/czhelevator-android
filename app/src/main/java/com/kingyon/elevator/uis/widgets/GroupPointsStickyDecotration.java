package com.kingyon.elevator.uis.widgets;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.text.TextPaint;
import android.text.TextUtils;
import android.view.View;

import com.kingyon.elevator.R;
import com.kingyon.elevator.utils.CommonUtil;
import com.leo.afbaselibrary.utils.ScreenUtil;

/**
 * Created by GongLi on 2018/9/19.
 * Email：lc824767150@163.com
 */

public class GroupPointsStickyDecotration extends RecyclerView.ItemDecoration {

    private Context mContext;
    private GroupPointsListener mGroupPointsListener;
    private int mGroupHeight;
    private int mLeftMargin;
    private Paint mGroutPaint;
    private Paint mTextPaint;
    private int mType;

    public GroupPointsStickyDecotration(Context context, int groupTop, GroupPointsListener groupPointsListener, int type) {
        super();
        mContext = context;
        mType = type;
        mGroupPointsListener = groupPointsListener;
        mGroupHeight = ScreenUtil.dp2px(36);
        mLeftMargin = ScreenUtil.dp2px(16);
        mGroutPaint = new Paint();
        mGroutPaint.setAntiAlias(true);
        mGroutPaint.setStyle(Paint.Style.FILL);
        mGroutPaint.setColor(ContextCompat.getColor(context, R.color.background));
        mTextPaint = new TextPaint();
        mTextPaint.setAntiAlias(true);
        mTextPaint.setTextSize(context.getResources().getDimensionPixelSize(R.dimen.txt_tips));
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);
        int pos = parent.getChildAdapterPosition(view);
        String groupId = mGroupPointsListener.getGroupName(pos);
        if (groupId == null) return;
        //只有是同一组的第一个才显示悬浮栏
        if (pos == 0 || isFirstInGroup(pos)) {
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
            currentGroupName = mGroupPointsListener.getGroupName(position);
            if (currentGroupName == null || TextUtils.equals(currentGroupName, preGroupName))
                continue;
            int viewBottom = view.getBottom();
            float top = Math.max(mGroupHeight, view.getTop());//top 决定当前顶部第一个悬浮Group的位置
            if (position + 1 < itemCount) {
                //获取下个GroupName
                String nextGroupName = mGroupPointsListener.getGroupName(position + 1);
                //下一组的第一个View接近头部
                if (!currentGroupName.equals(nextGroupName) && viewBottom < top) {
                    top = viewBottom;
                }
            }
            //根据top绘制group
            c.drawRect(left, top - mGroupHeight, right, top, mGroutPaint);

            Bitmap icon = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.ic_sticky_calendar);
            RectF rectF = new RectF(left + mLeftMargin, top - (mGroupHeight + ScreenUtil.dp2px(15)) / 2, left + mLeftMargin + ScreenUtil.dp2px(17), top - (mGroupHeight - ScreenUtil.dp2px(15)) / 2);
            c.drawBitmap(icon, null, rectF, null);

            Paint.FontMetrics fm = mTextPaint.getFontMetrics();
            //文字竖直居中显示
            float baseLine = top - (mGroupHeight - (fm.bottom - fm.top)) / 2 - fm.bottom;

            mTextPaint.setColor(0xFFBDBDBD);
            c.drawText(currentGroupName, left + mLeftMargin + ScreenUtil.dp2px(23), baseLine, mTextPaint);

            mTextPaint.setColor(0xFF999999);
            double[] groupNum = mGroupPointsListener.getGroupNum(position);
            String usedStr = mType == 1 ? "" : String.format("使用：%s", CommonUtil.getOneDigits(groupNum[0]));
            String getStr = mType == 2 ? "" : String.format("获得：%s", CommonUtil.getOneDigits(groupNum[1]));
            float usedSizeY = mTextPaint.measureText(usedStr);
            float getSizeY = mTextPaint.measureText(getStr);
            c.drawText(usedStr, right - mLeftMargin - usedSizeY, baseLine, mTextPaint);
            if (TextUtils.isEmpty(usedStr)) {
                c.drawText(getStr, right - mLeftMargin - usedSizeY - getSizeY, baseLine, mTextPaint);
            } else {
                c.drawText(getStr, right - mLeftMargin - usedSizeY - mLeftMargin - getSizeY, baseLine, mTextPaint);
            }
        }
    }

    //判断是不是组中的第一个位置
    //根据前一个组名，判断当前是否为新的组
    private boolean isFirstInGroup(int pos) {
        if (pos == 0) {
            return true;
        } else {
            String prevGroupId = mGroupPointsListener.getGroupName(pos - 1);
            String groupId = mGroupPointsListener.getGroupName(pos);
            return !TextUtils.equals(prevGroupId, groupId);
        }
    }

    public interface GroupPointsListener {
        String getGroupName(int position);

        double[] getGroupNum(int position);
    }
}

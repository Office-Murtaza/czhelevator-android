package com.kingyon.elevator.customview;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.GridView;

/**
 * Created By SongPeng  on 2019/12/16
 * Email : 1531603384@qq.com
 */
public class MyGridView extends GridView {


    public MyGridView(Context context) {
        super(context);
    }

    public MyGridView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MyGridView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    /*
  高度实现自适应
   */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int heightSpec;
        //if (getLayoutParams().height == AbsListView.LayoutParams.WRAP_CONTENT) {
            heightSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2, MeasureSpec.AT_MOST);
//        } else {
//            heightSpec = heightMeasureSpec;
//        }
        super.onMeasure(widthMeasureSpec, heightSpec);
    }
}

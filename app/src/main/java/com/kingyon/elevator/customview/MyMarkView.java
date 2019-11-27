package com.kingyon.elevator.customview;

import android.content.Context;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.LogUtils;
import com.github.mikephil.charting.components.MarkerView;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.utils.MPPointF;
import com.kingyon.elevator.R;

/**
 * Created By SongPeng  on 2019/11/26
 * Email : 1531603384@qq.com
 * 折线图上点击某个点时弹出的提示
 */
public class MyMarkView extends MarkerView {

    LinearLayout marker_container;
    TextView income_date;
    TextView income_money;

    /**
     * Constructor. Sets up the MarkerView with a custom layout resource.
     *
     * @param context
     * @param layoutResource the layout resource to use for the MarkerView
     */
    public MyMarkView(Context context, int layoutResource) {
        super(context, layoutResource);
        marker_container = findViewById(R.id.marker_container);
        income_date = findViewById(R.id.income_date);
        income_money = findViewById(R.id.income_money);

    }

    @Override
    public void refreshContent(Entry e, Highlight highlight) {
        income_date.setText("2019年" + (int)e.getX() + "月");
        income_money.setText("¥" + e.getY());
        super.refreshContent(e, highlight);
    }

    @Override
    public MPPointF getOffset() {
        return new MPPointF(-(getWidth() / 2), -800);
    }

}

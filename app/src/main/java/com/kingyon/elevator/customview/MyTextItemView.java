package com.kingyon.elevator.customview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.kingyon.elevator.R;

/**
 * Created By SongPeng  on 2019/11/25
 * Email : 1531603384@qq.com
 * 收益界面布局差不多的控件封装
 */
public class MyTextItemView extends RelativeLayout {

    private Integer smallTitleColor = null;
    private Integer smallTitleSize = 10;
    private Integer smallTitleStyle = 0;
    private Integer mainTitleColor = null;
    private Integer mainTitleSize = 14;
    private Integer mainTitleStyle = 0;
    private Integer mainTitleTop = 10;
    private float mainTitleAlpha = 1.0f;
    private float smallTitleAlpha = 1.0f;
    private String mainTitleText = "";
    private String smallTitleText = "";
    protected View contentView;
    protected Context mContext;
    private TextView small_title;
    private TextView mian_title;

    public MyTextItemView(Context context) {
        this(context, null);
    }

    public MyTextItemView(Context context, AttributeSet attrs) {
        this(context, attrs, -1);
    }

    public MyTextItemView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = getContext();
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.MyTextItemView);
        smallTitleText = array.getString(R.styleable.MyTextItemView_smallTitleText);
        mainTitleText = array.getString(R.styleable.MyTextItemView_mainTitleText);
        smallTitleColor = array.getColor(R.styleable.MyTextItemView_smallTitleColor, 0);
        mainTitleColor = array.getColor(R.styleable.MyTextItemView_mainTitleColor, 0);
        smallTitleSize = array.getInteger(R.styleable.MyTextItemView_smallTitleSize, 10);
        mainTitleSize = array.getInteger(R.styleable.MyTextItemView_mainTitleSize, 14);
        mainTitleTop = array.getInteger(R.styleable.MyTextItemView_mainTitleSize, 10);
        mainTitleAlpha = array.getFloat(R.styleable.MyTextItemView_mainTitleSize, 1.0f);
        smallTitleAlpha = array.getFloat(R.styleable.MyTextItemView_mainTitleSize, 1.0f);
        mainTitleStyle = array.getInteger(R.styleable.MyTextItemView_mainTitleStyle, 14);
        smallTitleStyle = array.getInteger(R.styleable.MyTextItemView_smallTitleStyle, 10);
        array.recycle();
        initView();
    }


    private void initView() {
        contentView = RelativeLayout.inflate(mContext, R.layout.mytext_item_view_layout, this);
        small_title = contentView.findViewById(R.id.small_title);
        mian_title = contentView.findViewById(R.id.mian_title);
        if (mainTitleColor != null && mainTitleColor != 0) {
            mian_title.setTextColor(mainTitleColor);
        }
        if (smallTitleColor != null && smallTitleColor != 0) {
            small_title.setTextColor(smallTitleColor);
        }
        small_title.setText(smallTitleText);
        mian_title.setText(mainTitleText);
        mian_title.setTextSize(mainTitleSize);
        small_title.setTextSize(smallTitleSize);
        mian_title.setAlpha(mainTitleAlpha);
        small_title.setAlpha(smallTitleAlpha);
        if (mainTitleStyle == 1) {
            mian_title.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
        } else {
            mian_title.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
        }
        if (smallTitleStyle == 1) {
            small_title.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
        } else {
            small_title.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
        }
        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) mian_title.getLayoutParams();
        layoutParams.topMargin = mainTitleTop;
        mian_title.setLayoutParams(layoutParams);
    }


    /**
     * 设置小标题的文字颜色
     *
     * @param color
     */
    public void setSmallTitleColor(int color) {
        small_title.setTextColor(color);
    }

    public void setMainTitleColor(int mainTitleColor) {
        mian_title.setTextColor(mainTitleColor);
    }


    public void setMainTitleText(String mainTitleText) {
        this.mainTitleText = mainTitleText;
        mian_title.setText(mainTitleText);
    }

    public void setSmallTitleText(String smallTitleText) {
        this.smallTitleText = smallTitleText;
        small_title.setText(smallTitleText);
    }
}

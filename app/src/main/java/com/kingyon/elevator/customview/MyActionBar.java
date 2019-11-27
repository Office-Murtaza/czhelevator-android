package com.kingyon.elevator.customview;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.LogUtils;
import com.kingyon.elevator.R;
import com.kingyon.elevator.interfaces.OnItemClick;


@SuppressLint("AppCompatCustomView")
public class MyActionBar extends RelativeLayout {
    private String title = "";
    private String rightMenuName = "";//右边的菜单名字
    private Integer rightIcon = null;//右边的菜单名字
    private Integer leftIcon = null;//左边默认要显示
    private Integer leftIconDefalt = R.mipmap.partner_ffanhuianniu;//左边默认要显示
    private Integer barBg = null;
    private Integer defaltBarBg = R.color.white;
    private Integer titleColor = null;
    private Integer defalttitleColor = R.color.black;
    private Integer rightMenuColor = null;
    private Integer defalrightMenuColor = R.color.black;
    protected View contentView;
    protected Context mContext;
    TextView tv_title;
    TextView tv_right_name;
    OnItemClick onItemLeftClick;
    OnItemClick onItemrightIconClick;
    OnItemClick onItemrightTextClick;
    ImageView leftIconView;
    ImageView rightIconView;
    RelativeLayout action_bar_container;


    public MyActionBar(Context context) {
        this(context, null);
    }

    public MyActionBar(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }


    public MyActionBar(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        //获取到设置的宽高比，然后设置自己的值
        mContext = getContext();
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.MyActionBar);
        title = array.getString(R.styleable.MyActionBar_title);
        rightMenuName = array.getString(R.styleable.MyActionBar_rightmenuname);
        rightIcon = array.getResourceId(R.styleable.MyActionBar_righticon, 0);
        leftIcon = array.getResourceId(R.styleable.MyActionBar_lefticon, 0);
        barBg = array.getColor(R.styleable.MyActionBar_action_bar_background, 0);
        titleColor = array.getResourceId(R.styleable.MyActionBar_title_color, 0);
        rightMenuColor = array.getResourceId(R.styleable.MyActionBar_rightmenuname_color, 0);
        array.recycle();
        initView();
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }


    /**
     * 设置左边的点击事件
     *
     * @param onItemClick
     */
    public void setLeftIconClick(OnItemClick onItemClick) {
        onItemLeftClick = onItemClick;
    }


    /**
     * 设置右边图片点击事件
     *
     * @param onItemClick
     */
    public void setRightIconClick(OnItemClick onItemClick) {
        onItemrightIconClick = onItemClick;
    }


    /**
     * 设置右边文字描述的点击shijian
     *
     * @param onItemClick
     */
    public void setRightTextClick(OnItemClick onItemClick) {
        onItemrightTextClick = onItemClick;
    }


    /**
     * 初始化布局
     */
    private void initView() {
        contentView = RelativeLayout.inflate(mContext, R.layout.base_action_bar_layout, this);
        tv_title = contentView.findViewById(R.id.tv_action_title);
        action_bar_container = contentView.findViewById(R.id.action_bar_container);
        tv_right_name = contentView.findViewById(R.id.right_text_btn);
        leftIconView = contentView.findViewById(R.id.back_close);
        rightIconView = contentView.findViewById(R.id.right_img_btn);
        tv_title.setText(title);
        leftIconView.setImageResource(leftIconDefalt);
        if (barBg != null && barBg != 0) {
            LogUtils.e("颜色值：" + barBg);
            if (barBg == -1) {
                action_bar_container.setBackgroundColor(Color.parseColor("#00000000"));
            } else {
                action_bar_container.setBackgroundColor(barBg);
            }
        }
        if (titleColor != null && titleColor != 0) {
            tv_title.setTextColor(getResources().getColor(titleColor));
        }
        if (rightMenuName == null || rightMenuName.equals("")) {
            tv_right_name.setVisibility(GONE);
        } else {
            tv_right_name.setVisibility(VISIBLE);
            tv_right_name.setText(rightMenuName);
            if (rightMenuColor != null && rightMenuColor != 0) {
                tv_right_name.setTextColor(getResources().getColor(rightMenuColor));
            }
            tv_right_name.setOnClickListener(v -> {
                if (onItemrightTextClick != null) {
                    onItemrightTextClick.onItemClick(v.getId());
                }
            });
        }
        leftIconView.setOnClickListener(v -> {
            if (onItemLeftClick != null) {
                onItemLeftClick.onItemClick(v.getId());
            } else {
                ((Activity) mContext).finish();
            }
        });
        if (leftIcon != null && leftIcon != 0) {
            leftIconView.setImageResource(leftIcon);
        }
        if (rightIcon != null && rightIcon != 0) {
            rightIconView.setImageResource(rightIcon);
            rightIconView.setVisibility(VISIBLE);
            rightIconView.setOnClickListener(v -> {
                if (onItemrightIconClick != null) {
                    onItemrightIconClick.onItemClick(v.getId());
                }
            });
        }
    }

    /**
     * 设置标题
     *
     * @param title
     */
    public void setTitle(String title) {
        tv_title.setText(title);
    }


    /**
     * 设置右边按钮是否可见
     *
     * @param status
     */
    public void setRightMenuNameIsShow(int status) {
        tv_right_name.setVisibility(status);
    }


    public void setRightDrawble(int rightDrawble) {
        try {
            Drawable drawable = getResources().getDrawable(rightDrawble);
            tv_right_name.setCompoundDrawablesWithIntrinsicBounds(null, null, drawable, null);
        } catch (Exception e) {

        }
    }


    public void setRightMenuName(String name) {
        tv_right_name.setText(name);
    }

    public void setRightIcon(int resoureId) {
        if (rightIconView != null) {
            rightIconView.setVisibility(VISIBLE);
            rightIconView.setImageResource(resoureId);
            rightIconView.setOnClickListener(v -> {
                if (onItemrightIconClick != null) {
                    onItemrightIconClick.onItemClick(v.getId());
                }
            });
        }
    }

    public void setRightMenuColor(String color) {
        tv_right_name.setTextColor(Color.parseColor(color));
        tv_right_name.setVisibility(VISIBLE);
        tv_right_name.setOnClickListener(v -> {
            if (onItemrightTextClick != null) {
                onItemrightTextClick.onItemClick(0);
            }
        });
    }


    public void hideRightIcon() {
        if (rightIconView != null) {
            rightIconView.setVisibility(GONE);
        }
    }


    /**
     * 置灰按钮 不可点击
     */
    public void disableAddFriend() {
        rightIconView.setColorFilter(Color.GRAY, PorterDuff.Mode.SRC_IN);
        rightIconView.setOnClickListener(null);
    }

}

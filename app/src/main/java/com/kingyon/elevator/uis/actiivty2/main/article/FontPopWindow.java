package com.kingyon.elevator.uis.actiivty2.main.article;

import android.app.ActionBar;
import android.app.Activity;
import android.graphics.drawable.ColorDrawable;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.PopupWindow;

import com.kingyon.elevator.R;

/**
 * @Created By Admin  on 2020/4/15
 * @Email : 163235610@qq.com
 * @Author:Mrczh
 * @Instructions:字体设置
 */
public class FontPopWindow extends PopupWindow implements View.OnClickListener {
    private Activity context;
    private ImageView img_point, img_number,img_level,img_bold,img_tilt,img_blue;
    FontPopWindow fontPopWindow;
    public FontPopWindow(Activity context) {
        super(context);
        this.context = context;
       fontPopWindow = this;
        initalize();
    }

    private void initalize() {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.article_popwindow, null);
        img_point = view.findViewById(R.id.img_point);
        img_number = view.findViewById(R.id.img_number);
        img_level = view.findViewById(R.id.img_level);
        img_bold = view.findViewById(R.id.img_bold);
        img_tilt = view.findViewById(R.id.img_tilt);
        img_blue = view.findViewById(R.id.img_blue);
        img_point.setOnClickListener(this);
        img_number.setOnClickListener(this);
        img_level.setOnClickListener(this);
        img_bold.setOnClickListener(this);
        img_tilt.setOnClickListener(this);
        img_blue.setOnClickListener(this);

        setContentView(view);
        initWindow();
    }

    private void initWindow() {
        DisplayMetrics d = context.getResources().getDisplayMetrics();
        this.setWidth((int) (d.widthPixels * 0.58));
        this.setHeight(ActionBar.LayoutParams.WRAP_CONTENT);
        this.setFocusable(true);
        this.setOutsideTouchable(true);
        this.update();
        //实例化一个ColorDrawable颜色为半透明
        ColorDrawable dw = new ColorDrawable(0x00000000);
        //设置SelectPicPopupWindow弹出窗体的背景
        this.setBackgroundDrawable(dw);
        backgroundAlpha((Activity) context, 1.0f);
        this.setOnDismissListener(new OnDismissListener() {
            @Override
            public void onDismiss() {
                backgroundAlpha((Activity) context, 1f);
            }
        });
    }

//    设置添加屏幕的背景透明度

    public void backgroundAlpha(Activity context, float bgAlpha) {
        WindowManager.LayoutParams lp = context.getWindow().getAttributes();
        lp.alpha = bgAlpha;
        context.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        context.getWindow().setAttributes(lp);
    }

    public void showAtBottom(View view) {
        //弹窗位置设置
//        int offsetX = Math.abs(getContentView().getMeasuredWidth()-view.getWidth()) / 2;
//        int offsetY = -(getContentView().getMeasuredHeight()+view.getHeight());
//       showAsDropDown(view, offsetX, offsetY, Gravity.START);
        showAsDropDown(view, Math.abs((view.getWidth() - getWidth()) / 2), 220);
//        showAtLocation(view, Gravity.BOTTOM | Gravity.RIGHT, 400, 250);
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.img_point:

                break;
            case R.id.img_number:

                break;
            case R.id.img_level:

                break;
            case R.id.img_bold:

                break;
            case R.id.img_tilt:

                break;
            case R.id.img_blue:

                break;
            default:
                break;
        }
    }

}

package com.kingyon.elevator.customview;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.view.Gravity;
import android.view.WindowManager;

import com.kingyon.elevator.R;

/**
 * Created By SongPeng  on 2019/11/28
 * Email : 1531603384@qq.com
 */
public class MyBaseBottomDialog extends Dialog {


    public MyBaseBottomDialog(@NonNull Context context) {
        super(context, R.style.MyDialog);
    }


    @Override
    public void show() {
        super.show();
        WindowManager.LayoutParams params = getWindow().getAttributes();
        //设置窗口宽度为充满全屏
        params.width = WindowManager.LayoutParams.MATCH_PARENT;
        //设置窗口高度为包裹内容
        params.height = WindowManager.LayoutParams.WRAP_CONTENT;
        params.gravity = Gravity.BOTTOM;
        getWindow().setAttributes(params);
    }
}

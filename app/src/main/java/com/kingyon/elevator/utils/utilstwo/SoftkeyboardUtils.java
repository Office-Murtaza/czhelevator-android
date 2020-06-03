package com.kingyon.elevator.utils.utilstwo;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import static android.content.Context.INPUT_METHOD_SERVICE;

/**
 * @Created By Admin  on 2020/5/22
 * @Email : 163235610@qq.com
 * @Author:Mrczh
 * @Instructions:
 */
public class SoftkeyboardUtils {

    /**
     * 显示键盘
     *
     * @param et 输入焦点
     */
    public static  void showInput(Context context,final EditText et) {
        et.requestFocus();
        InputMethodManager imm = (InputMethodManager) context.getSystemService(INPUT_METHOD_SERVICE);
        imm.showSoftInput(et, InputMethodManager.SHOW_IMPLICIT);
    }

    /**
     * 隐藏键盘
     */
    public static void hideInput(Activity context) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(INPUT_METHOD_SERVICE);
        View v =  context.getWindow().peekDecorView();
        if (null != v) {
            imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
        }
    }

}

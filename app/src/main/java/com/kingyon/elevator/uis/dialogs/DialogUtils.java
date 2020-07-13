package com.kingyon.elevator.uis.dialogs;

import android.app.ActionBar;
import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import com.czh.myversiontwo.R;
import com.czh.myversiontwo.activity.ActivityUtils;
import com.kingyon.elevator.constants.Constants;
import com.kingyon.elevator.data.DataSharedPreferences;
import com.kingyon.elevator.entities.UserEntity;

import static com.czh.myversiontwo.utils.Constance.ACTIVITY_CERTIFICATION;
import static com.czh.myversiontwo.utils.Constance.ACTIVITY_IDENTITY_INFO;

/**
 * @Created By Admin  on 2020/6/3
 * @Email : 163235610@qq.com
 * @Author:Mrczh
 * @Instructions:
 */
public class DialogUtils {

    public static void shwoCertificationDialog(Context context) {
        final AlertDialog alertDialog = new AlertDialog.Builder(context).create();
        alertDialog.show();
        /* 添加对话框自定义布局 */
        alertDialog.setContentView(R.layout.layout_partner1);
        /* 获取对话框窗口 */
        Window window = alertDialog.getWindow();
        /* 设置显示窗口的宽高 */
        window.setLayout(ActionBar.LayoutParams.MATCH_PARENT, ActionBar.LayoutParams.WRAP_CONTENT);
        /* 设置窗口显示位置 */
        window.setGravity(Gravity.CENTER);
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        alertDialog.setCanceledOnTouchOutside(true);
        TextView tv_content = window.findViewById(R.id.tv_content);
        TextView tv_next = window.findViewById(R.id.tv_next);
        TextView tv_cancel = window.findViewById(R.id.tv_cancel);
        UserEntity user = DataSharedPreferences.getUserBean();
        if (user.getAuthStatus()!=null) {
            if (user.getAuthStatus().equals(Constants.IDENTITY_STATUS.NO_AUTH)) {
                /*没有认证*/
                tv_content.setText("根据法律规定，您当前未通过实名认证，不能进行广告发布，请先完成实名认证。");
                tv_next.setVisibility(View.VISIBLE);
            } else if (user.getAuthStatus().equals(Constants.IDENTITY_STATUS.FAILD)){
                /*认证失败*/
                tv_content.setText("认证失败，请重新认证");
                tv_next.setVisibility(View.VISIBLE);
            }else if (user.getAuthStatus().equals(Constants.IDENTITY_STATUS.AUTHING)){
                /*认证中*/
                tv_content.setText("您已提交个人认证申请，工作人员正在审核处理中，预计需要5个工作日。");
                tv_next.setVisibility(View.GONE);
            }else {
                /*认证通过*/
                tv_content.setText("认证已经通过请刷新后重试");
                tv_next.setVisibility(View.GONE);
            }
        }
        tv_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*去认证*/
                alertDialog.dismiss();
                ActivityUtils.setActivity(ACTIVITY_CERTIFICATION);
            }
        });
        tv_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });
    }

}

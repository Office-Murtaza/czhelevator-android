package com.kingyon.elevator.customview;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.kingyon.elevator.R;
import com.kingyon.elevator.interfaces.OnItemClick;
import com.kingyon.elevator.utils.DialogUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Date:2018/10/30
 * Time:15:24
 * author:songpeng
 * Email:1531603384@qq.com
 * 合伙人界面提示信息弹窗
 */
public class CashTipsDialog extends Dialog {



    public CashTipsDialog(Context context,String content,boolean isLink, OnItemClick onItemClick) {
        super(context, R.style.MyDialog);

    }



}

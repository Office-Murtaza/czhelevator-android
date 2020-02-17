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

    private OnItemClick onItemClick;
    private String content;
    @BindView(R.id.tv_tips_content)
    TextView tv_tips_content;
    @BindView(R.id.tv_go_cat_content)
    TextView tv_go_cat_content;

    private Boolean isLink=false;

    public CashTipsDialog(Context context,String content,boolean isLink, OnItemClick onItemClick) {
        super(context, R.style.MyDialog);
        this.onItemClick = onItemClick;
        this.content=content;
        this.isLink=isLink;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_cash_tips_layout);
        ButterKnife.bind(this);
        if (content.isEmpty()) {
            content="";
        }
        tv_tips_content.setText(content);
        if (!isLink) {
            tv_go_cat_content.setText("确定");
        }
    }


    @OnClick({R.id.tv_go_cat_content})
    public void OnClick(View view) {
        switch (view.getId()) {
            case R.id.tv_go_cat_content:
                onItemClick.onItemClick(0);
                DialogUtils.getInstance().hideCashTipsDialog();
                break;
        }
    }


    @Override
    public void dismiss() {
        super.dismiss();
        
    }
}

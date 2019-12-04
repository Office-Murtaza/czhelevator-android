package com.kingyon.elevator.customview;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;

import com.kingyon.elevator.R;
import com.kingyon.elevator.interfaces.OnItemClick;
import com.kingyon.elevator.interfaces.SelectCashBindTypeListener;
import com.kingyon.elevator.utils.DialogUtils;

import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Date:2018/10/30
 * Time:15:24
 * author:songpeng
 * Email:1531603384@qq.com
 * 修改成功提示对话框
 */
public class EditSuccessTipsDialog extends Dialog {

    private OnItemClick onItemClick;

    public EditSuccessTipsDialog(Context context,OnItemClick onItemClick) {
        super(context, R.style.MyDialog);
        this.onItemClick = onItemClick;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_success_dialog_layout);
        ButterKnife.bind(this);
    }


    @OnClick({R.id.close_dialog})
    public void OnClick(View view) {
        switch (view.getId()) {
            case R.id.close_dialog:
                onItemClick.onItemClick(0);
                break;
        }
    }


    @Override
    public void dismiss() {
        super.dismiss();
    }
}

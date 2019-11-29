package com.kingyon.elevator.customview;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.TextView;

import com.kingyon.elevator.R;
import com.kingyon.elevator.interfaces.SelectCashBindTypeListener;
import com.kingyon.elevator.utils.DialogUtils;
import com.kingyon.elevator.utils.HtmlFormUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Date:2018/10/30
 * Time:15:24
 * author:songpeng
 * Email:1531603384@qq.com
 * 选择绑定提现方式对话框
 */
public class SelectCashBindTypeDialog extends Dialog {

    private SelectCashBindTypeListener selectCashBindTypeListener;

    public SelectCashBindTypeDialog(Context context, SelectCashBindTypeListener selectCashBindTypeListener) {
        super(context, R.style.MyDialog);
        this.selectCashBindTypeListener = selectCashBindTypeListener;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_bank_card_select_type_layout);
        ButterKnife.bind(this);
    }


    @OnClick({R.id.select_zfb, R.id.select_bank_card})
    public void OnClick(View view) {
        switch (view.getId()) {
            case R.id.select_zfb:
                selectCashBindTypeListener.selectBindZfb();
                DialogUtils.getInstance().hideSelectCashBindTypeDialog();
                break;
            case R.id.select_bank_card:
                selectCashBindTypeListener.selectBindBankCard();
                DialogUtils.getInstance().hideSelectCashBindTypeDialog();
                break;
        }
    }


    @Override
    public void dismiss() {
        super.dismiss();
    }
}

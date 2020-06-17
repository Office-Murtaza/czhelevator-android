package com.kingyon.elevator.customview;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.widget.TextView;

import com.kingyon.elevator.R;
import com.kingyon.elevator.utils.DialogUtils;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Date:2018/10/30
 * Time:15:24
 * author:songpeng
 * Email:1531603384@qq.com
 * 广告上传规格说明
 */
public class RuleDescTipsDialog extends Dialog {

    @BindView(R.id.tv_i_know)
    TextView tv_i_know;


    public RuleDescTipsDialog(Context context) {
        super(context, R.style.MyDialog);

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.rule_desc_tips_dialog_layout);
        ButterKnife.bind(this);
        tv_i_know.setOnClickListener(v -> {
            DialogUtils.getInstance().hideRuleDescTipsDialog();
        });

    }


    @Override
    public void dismiss() {
        super.dismiss();
    }
}

package com.kingyon.elevator.customview;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.widget.TextView;

import com.kingyon.elevator.R;
import com.kingyon.elevator.interfaces.OnItemClick;
import com.kingyon.elevator.utils.DialogUtils;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Date:2018/10/30
 * Time:15:24
 * author:songpeng
 * Email:1531603384@qq.com
 * 视频预览返回提示对话框
 */
public class PreviewVideoBackTipsDialog extends Dialog {
    @BindView(R.id.tv_keep_go_back)
    TextView tv_keep_go_back;
    @BindView(R.id.tv_cancel_back)
    TextView tv_cancel_back;
    OnItemClick onItemClick;


    public PreviewVideoBackTipsDialog(Context context, OnItemClick onItemClick) {
        super(context, R.style.MyDialog);
        this.onItemClick = onItemClick;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.preview_video_back_tips_dialog);
        ButterKnife.bind(this);
        tv_keep_go_back.setOnClickListener(v -> {
            onItemClick.onItemClick(0);
            DialogUtils.getInstance().hideBackTipsDialog();
        });
        tv_cancel_back.setOnClickListener(v -> {
            DialogUtils.getInstance().hideBackTipsDialog();
        });
    }


    @Override
    public void dismiss() {
        super.dismiss();
    }
}

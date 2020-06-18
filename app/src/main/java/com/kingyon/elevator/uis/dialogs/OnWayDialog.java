package com.kingyon.elevator.uis.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.kingyon.elevator.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * @Created By Admin  on 2020/6/18
 * @Email : 163235610@qq.com
 * @Author:Mrczh
 * @Instructions:
 */
public class OnWayDialog extends Dialog {

    @BindView(R.id.tv_video_image)
    TextView tvVideoImage;
    @BindView(R.id.tv_video)
    TextView tvVideo;
    @BindView(R.id.tv_image)
    TextView tvImage;
    @BindView(R.id.share_btn_cancel)
    TextView shareBtnCancel;
    OnWayString onWayString;

    public OnWayDialog(@NonNull Context context,OnWayString onWayString) {
        super(context, com.kingyon.library.social.R.style.ShareDialog);
        this.onWayString = onWayString;
        setContentView(getLayoutId());
        Window window = getWindow();
        if (window != null) {
            window.setLayout(WindowManager.LayoutParams.MATCH_PARENT,
                    WindowManager.LayoutParams.WRAP_CONTENT);
            window.setWindowAnimations(com.kingyon.library.social.R.style.dialog_show_anim);
            window.setGravity(Gravity.BOTTOM);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
    }

    protected int getLayoutId() {
        return R.layout.dialog_on_way;
    }

    public void setPaddingTop(int top) {
        Window window = getWindow();
        if (window != null) {
            WindowManager.LayoutParams lp = window.getAttributes();
            lp.y = top;
            window.setAttributes(lp);
        }
    }

    @OnClick({R.id.tv_video_image, R.id.tv_video, R.id.tv_image, R.id.share_btn_cancel})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_video_image:
                onWayString.onWay("视频/海报");
                dismiss();
                break;
            case R.id.tv_video:
                onWayString.onWay("视频");
                dismiss();
                break;
            case R.id.tv_image:
                onWayString.onWay("海报");
                dismiss();
                break;
            case R.id.share_btn_cancel:
                dismiss();
                break;
        }
    }

    public  interface OnWayString{
        void onWay(String str);
    }
}

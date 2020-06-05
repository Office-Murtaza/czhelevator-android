package com.kingyon.elevator.customview;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.ColorRes;

import com.kingyon.elevator.R;
import com.kingyon.elevator.interfaces.FingerCheckListener;
import com.kingyon.elevator.utils.DialogUtils;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.czh.myversiontwo.utils.CodeType.KEYBOARD_SETTING;

/**
 * Created By SongPeng  on 2019/11/28
 * Email : 1531603384@qq.com
 * 指纹验证对话框
 */
public class FingerCheckDailog extends MyBaseBottomDialog {

    @BindView(R.id.cancel_finger_check)
    TextView cancel_finger_check;
    @BindView(R.id.tv_check_tips)
    TextView tv_check_tips;
    @BindView(R.id.error_hint_msg)
    TextView error_hint_msg;
    @BindView(R.id.tv_password)
    TextView tvPassword;
    private Context context;
    FingerCheckListener fingerCheckListener;
    String type;

    public FingerCheckDailog(@NonNull Context context, String type,FingerCheckListener fingerCheckListener) {
        super(context);
        this.context = context;
        this.type = type;
        this.fingerCheckListener = fingerCheckListener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.finger_check_dialog_layout);
        ButterKnife.bind(this);
        if (type.equals(KEYBOARD_SETTING)){
            tvPassword.setVisibility(View.GONE);
        }
        cancel_finger_check.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //关闭指纹识别，弹出密码输入框
//                if (fingerCheckListener != null) {
//                    fingerCheckListener.onCancle();
//                    fingerCheckListener.onUsepwd();
//                }
                DialogUtils.getInstance().hideFingerCheckDailog();
            }
        });

        tvPassword.setOnClickListener(v -> {
            //关闭指纹识别，弹出密码输入框
            DialogUtils.getInstance().hideFingerCheckDailog();
            fingerCheckListener.onUsepwd();
        });
    }


    /**
     * 根据指纹验证的结果更新tip的文字内容和文字颜色
     *
     * @param tip
     * @param colorId
     */
    public void setTip(String tip, @ColorRes int colorId) {
        tv_check_tips.setText(tip);
        tv_check_tips.setTextColor(context.getResources().getColor(colorId));
    }

    @Override
    public void dismiss() {
        super.dismiss();
        if (fingerCheckListener != null)
            fingerCheckListener.onDismiss();
        context = null;
    }
}

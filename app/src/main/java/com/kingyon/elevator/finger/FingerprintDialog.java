package com.kingyon.elevator.finger;

import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.ColorRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.kingyon.elevator.R;
import com.kingyon.elevator.finger.bean.VerificationDialogStyleBean;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * Created by ZuoHailong on 2019/3/12.
 */
public class FingerprintDialog extends DialogFragment {

    private static FingerprintDialog mDialog;
    private OnDialogActionListener actionListener;

    @BindView(R.id.cancel_finger_check)
    ImageView cancel_finger_check;
    @BindView(R.id.tv_check_tips)
    TextView tv_check_tips;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        setCancelable(false);
        View view = inflater.inflate(R.layout.biometricprompt_layout_fingerprint_dialog, container);
        ButterKnife.bind(this, view);
        cancel_finger_check.setOnClickListener(v -> {
            if (actionListener != null) {
                actionListener.onCancle();
                actionListener.onUsepwd();
            }
            dismiss();
        });

//        //调用者定义验证框样式
//        if (verificationDialogStyleBean != null) {
//            if (verificationDialogStyleBean.getCancelTextColor() != 0)
//                tvCancel.setTextColor(verificationDialogStyleBean.getCancelTextColor());
//            if (verificationDialogStyleBean.getUsepwdTextColor() != 0)
//                tvUsepwd.setTextColor(verificationDialogStyleBean.getUsepwdTextColor());
//
//            if (verificationDialogStyleBean.getFingerprintColor() != 0) {
//                Drawable drawable = ivFingerprint.getDrawable();
//                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {//Android 5.0
//                    drawable.setTint(verificationDialogStyleBean.getFingerprintColor());
//                }
//            }
//        }

        return view;
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
        if (actionListener != null)
            actionListener.onDismiss();
    }

    public static FingerprintDialog newInstance() {
        if (mDialog == null) {
            synchronized (FingerprintDialog.class) {
                if (mDialog == null) {
                    mDialog = new FingerprintDialog();
                }
            }
        }
        return mDialog;
    }

    public FingerprintDialog setActionListener(OnDialogActionListener actionListener) {
        this.actionListener = actionListener;
        return mDialog;
    }

    /**
     * 设定dialog样式
     *
     * @param bean
     */
    public FingerprintDialog setDialogStyle(VerificationDialogStyleBean bean) {
        return mDialog;
    }

    /**
     * 根据指纹验证的结果更新tip的文字内容和文字颜色
     *
     * @param tip
     * @param colorId
     */
    public void setTip(String tip, @ColorRes int colorId) {
        tv_check_tips.setText(tip);
        tv_check_tips.setTextColor(getResources().getColor(colorId));
    }

    public interface OnDialogActionListener {
        void onUsepwd();

        void onCancle();

        void onDismiss();
    }
}

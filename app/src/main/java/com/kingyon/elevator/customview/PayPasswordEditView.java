package com.kingyon.elevator.customview;

import android.content.Context;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.kingyon.elevator.R;
import com.kingyon.elevator.interfaces.PayPasswordListener;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created By SongPeng  on 2019/11/27
 * Email : 1531603384@qq.com
 * 自定义密支付密码输入框
 */
public class PayPasswordEditView extends LinearLayout {
    protected View contentView;
    protected Context mContext;
    @BindView(R.id.tv_pwd1)
    TextView tv_pwd1;
    @BindView(R.id.tv_pwd2)
    TextView tv_pwd2;
    @BindView(R.id.tv_pwd3)
    TextView tv_pwd3;
    @BindView(R.id.tv_pwd4)
    TextView tv_pwd4;
    @BindView(R.id.tv_pwd5)
    TextView tv_pwd5;
    @BindView(R.id.tv_pwd6)
    TextView tv_pwd6;
    @BindView(R.id.et_input_password)
    EditText et_input_password;
    private PayPasswordListener payPasswordListener;


    public PayPasswordEditView(Context context) {
        this(context, null);
    }

    public PayPasswordEditView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, -1);
    }

    public PayPasswordEditView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setOrientation(VERTICAL);
        mContext = getContext();
        initView();
    }


    private void initView() {
        contentView = RelativeLayout.inflate(mContext, R.layout.pay_password_edit_view_layout, this);
        ButterKnife.bind(this, contentView);
        et_input_password.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                int length = s.toString().trim().length();
                if (length > 0) {
                    switch (length) {
                        case 1:
                            tv_pwd1.setText("*");
                            tv_pwd2.setText("");
                            tv_pwd3.setText("");
                            tv_pwd4.setText("");
                            tv_pwd5.setText("");
                            tv_pwd6.setText("");
                            break;
                        case 2:
                            tv_pwd1.setText("*");
                            tv_pwd2.setText("*");
                            tv_pwd3.setText("");
                            tv_pwd4.setText("");
                            tv_pwd5.setText("");
                            tv_pwd6.setText("");
                            break;
                        case 3:
                            tv_pwd1.setText("*");
                            tv_pwd2.setText("*");
                            tv_pwd3.setText("*");
                            tv_pwd4.setText("");
                            tv_pwd5.setText("");
                            tv_pwd6.setText("");
                            break;
                        case 4:
                            tv_pwd1.setText("*");
                            tv_pwd2.setText("*");
                            tv_pwd3.setText("*");
                            tv_pwd4.setText("*");
                            tv_pwd5.setText("");
                            tv_pwd6.setText("");
                            break;
                        case 5:
                            tv_pwd1.setText("*");
                            tv_pwd2.setText("*");
                            tv_pwd3.setText("*");
                            tv_pwd4.setText("*");
                            tv_pwd5.setText("*");
                            tv_pwd6.setText("");
                            break;
                        case 6:
                            tv_pwd1.setText("*");
                            tv_pwd2.setText("*");
                            tv_pwd3.setText("*");
                            tv_pwd4.setText("*");
                            tv_pwd5.setText("*");
                            tv_pwd6.setText("*");
                            //触发监听事件
                            if (payPasswordListener != null) {
                                payPasswordListener.passwordIsInputDown(et_input_password.getText().toString());
                            }
                            break;
                        default:
                            clearAllText();
                    }
                } else {
                    clearAllText();
                }
            }
        });

    }


    /**
     * 设置密码输入完成监听事件
     *
     * @param payPasswordListener
     */
    public void setPayPasswordListener(PayPasswordListener payPasswordListener) {
        this.payPasswordListener = payPasswordListener;
    }

    public void clearAllText() {
        tv_pwd1.setText("");
        tv_pwd2.setText("");
        tv_pwd3.setText("");
        tv_pwd4.setText("");
        tv_pwd5.setText("");
        tv_pwd6.setText("");
    }

    public void clearEditText(){
        et_input_password.setText("");
    }

    public EditText getEt_input_password() {
        return et_input_password;
    }

}

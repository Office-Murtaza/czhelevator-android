package com.kingyon.paylibrary;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.kingyon.paylibrary.entitys.PayWay;


/**
 * Created by Leo on 2015/11/17
 */
class PayDialog1 extends Dialog {
    private TextView tvTips;
    private Context mContext;
    private PayWayInterface payWayInterface;

    public PayDialog1(Context context) {
        super(context, R.style.AvatarTransDialog);
        this.mContext = context;
        setContentView(R.layout.view_pay_interface);
        tvTips = (TextView) findViewById(R.id.tv_tips);
        getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
    }

    private void initView() {
        findViewById(R.id.my_alipay).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (payWayInterface != null) {
                    payWayInterface.onPay(PayWay.ALIPAY);
                }
                dismiss();
            }
        });

        findViewById(R.id.my_wechat).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (payWayInterface != null) {
                    payWayInterface.onPay(PayWay.WECHAT);
                }
                dismiss();
            }
        });

        findViewById(R.id.my_unionpay).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (payWayInterface != null) {
                    payWayInterface.onPay(PayWay.UNIONPAY);
                }
                dismiss();
            }
        });

    }

    public void setTips(String tips) {
        tvTips.setText(tips);
        tvTips.setVisibility(TextUtils.isEmpty(tips) ? View.GONE : View.VISIBLE);
    }


    public interface PayWayInterface {
        void onPay(PayWay way);
    }

    public void setPayWayInterface(PayWayInterface payWayInterface) {
        this.payWayInterface = payWayInterface;
    }
}

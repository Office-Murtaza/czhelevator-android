package com.kingyon.elevator.uis.actiivty2.user.invoice;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.kingyon.elevator.R;
import com.kingyon.elevator.entities.entities.Chartentily;
import com.kingyon.elevator.nets.CustomApiCallback;
import com.kingyon.elevator.nets.NetService;
import com.kingyon.elevator.utils.utilstwo.OrdinaryActivity;
import com.leo.afbaselibrary.nets.exceptions.ApiException;
import com.leo.afbaselibrary.uis.activities.BaseActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.czh.myversiontwo.utils.Constance.ACTIVITY_PERSONAL;

/**
 * @Created By Admin  on 2020/6/28
 * @Email : 163235610@qq.com
 * @Author:Mrczh
 * @Instructions:
 */
@Route(path = ACTIVITY_PERSONAL)
public class PersonalActivity extends BaseActivity {
    @BindView(R.id.img_top_back)
    ImageView imgTopBack;
    @BindView(R.id.tv_top_title)
    TextView tvTopTitle;
    @BindView(R.id.tv_right)
    TextView tvRight;
    @BindView(R.id.et_top)
    EditText etTop;
    @BindView(R.id.et_qy_number)
    EditText etQyNumber;
    @BindView(R.id.ll_qy_number)
    LinearLayout llQyNumber;
    @BindView(R.id.tv_qy_number)
    TextView tvQyNumber;
    @BindView(R.id.et_conent)
    TextView etConent;
    @BindView(R.id.et_money)
    EditText etMoney;
    @BindView(R.id.tv_all)
    TextView tvAll;
    @BindView(R.id.et_email)
    EditText etEmail;
    @BindView(R.id.tv_login_next)
    TextView tvLoginNext;
    private String invoiced;
    @Override
    public int getContentViewId() {
        return R.layout.activity_enterprise_invoice;
    }

    @Override
    public void init(Bundle savedInstanceState) {
        llQyNumber.setVisibility(View.GONE);
        tvQyNumber.setVisibility(View.GONE);
        NetService.getInstance().setInvoiceInfo()
                .compose(this.bindLifeCycle())
                .subscribe(new CustomApiCallback<Chartentily>() {
                    @Override
                    protected void onResultError(ApiException ex) {

                    }

                    @Override
                    public void onNext(Chartentily chartentily) {
                        etMoney.setHint("当前最多可开"+chartentily.waitting);
                        invoiced = chartentily.waitting;
                    }
                });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }

    @OnClick({R.id.img_top_back, R.id.tv_login_next,R.id.tv_all})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.img_top_back:
                finish();
                break;
            case R.id.tv_login_next:
                OrdinaryActivity.onSubmitClick(this,etTop,
                        false,etQyNumber,etConent,etMoney,
                        etEmail,tvLoginNext,"PERSON");
                break;
            case R.id.tv_all:
                etMoney.setText(invoiced);
                break;
        }
    }
}

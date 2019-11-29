package com.kingyon.elevator.uis.activities.cooperation;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.kingyon.elevator.R;
import com.kingyon.elevator.entities.CooperationInfoEntity;
import com.kingyon.elevator.entities.CooperationInfoNewEntity;
import com.kingyon.elevator.interfaces.SelectCashBindTypeListener;
import com.kingyon.elevator.mvpbase.MvpBaseActivity;
import com.kingyon.elevator.presenter.AddNewBankCardPresenter;
import com.kingyon.elevator.utils.CommonUtil;
import com.kingyon.elevator.utils.DialogUtils;
import com.kingyon.elevator.utils.MyActivityUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AddNewBankCardActivity extends MvpBaseActivity<AddNewBankCardPresenter> {

    private String bingType = "ZFB";
    @BindView(R.id.kaihuhang_container)
    RelativeLayout kaihuhang_container;
    @BindView(R.id.tv_account_type)
    TextView tv_account_type;

    @BindView(R.id.tv_zfb_account)
    EditText tv_zfb_account;
    @BindView(R.id.tv_zfb_name)
    EditText tv_zfb_name;
    @BindView(R.id.tv_bank_card_kaihuhang)
    EditText tv_bank_card_kaihuhang;
    private CooperationInfoNewEntity entity;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_bank_card);
        ButterKnife.bind(this);
        entity = getIntent().getParcelableExtra(CommonUtil.KEY_VALUE_1);
        if (entity == null) {
            entity = new CooperationInfoNewEntity();
        }
        bingType = getIntent().getStringExtra("value1");
        if (bingType.equals("ZFB")) {
             //绑定支付宝
            tv_account_type.setText("支付宝提现");
            tv_zfb_account.setHint("请输入支付宝账号");
            tv_zfb_name.setHint("请输入支付宝绑定的真实姓名");
            kaihuhang_container.setVisibility(View.GONE);
        }else {
            //绑定银行卡
            tv_account_type.setText("银行卡提现");
            tv_zfb_account.setHint("请输入银行卡账号");
            tv_zfb_name.setHint("请输入真实姓名");
            kaihuhang_container.setVisibility(View.VISIBLE);
        }

    }

    @Override
    public AddNewBankCardPresenter initPresenter() {
        return new AddNewBankCardPresenter(this);
    }

    @OnClick({R.id.tv_confirm_bind})
    public void OnClick(View view) {
        switch (view.getId()) {
            case R.id.tv_confirm_bind:
                Bundle bundle = new Bundle();
                bundle.putParcelable(CommonUtil.KEY_VALUE_1, entity);
                MyActivityUtils.goActivity(AddNewBankCardActivity.this, CooperationWithdrawActivity.class, bundle);
                break;
        }
    }

}

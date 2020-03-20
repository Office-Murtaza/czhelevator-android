package com.kingyon.elevator.uis.activities.cooperation;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.InputType;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.LogUtils;
import com.kingyon.elevator.R;

import com.kingyon.elevator.entities.BindAccountEntity;
import com.kingyon.elevator.entities.CooperationInfoEntity;
import com.kingyon.elevator.entities.CooperationInfoNewEntity;
import com.zhaoss.weixinrecorded.util.EventBusObjectEntity;
import com.kingyon.elevator.interfaces.SelectCashBindTypeListener;
import com.kingyon.elevator.mvpbase.MvpBaseActivity;
import com.kingyon.elevator.presenter.AddNewBankCardPresenter;
import com.kingyon.elevator.utils.CommonUtil;
import com.kingyon.elevator.utils.DialogUtils;
import com.kingyon.elevator.utils.MyActivityUtils;
import com.kingyon.elevator.utils.QuickClickUtils;
import com.kingyon.elevator.utils.RuntimeUtils;
import com.kingyon.elevator.utils.TextChangedListener;
import com.kingyon.elevator.view.AddNewBankCardView;
import com.zhaoss.weixinrecorded.util.EventBusConstants;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AddNewBankCardActivity extends MvpBaseActivity<AddNewBankCardPresenter> implements AddNewBankCardView {

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
    BindAccountEntity bindAccountEntity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_bank_card);
        ButterKnife.bind(this);
        bindAccountEntity = new BindAccountEntity();
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
            bindAccountEntity.setCashType(2);
            tv_zfb_account.setFilters(new InputFilter[] {new InputFilter.LengthFilter(30)});
            TextChangedListener.StringWatcher(tv_zfb_account);
        } else {
            //绑定银行卡
            tv_account_type.setText("银行卡提现");
            tv_zfb_account.setHint("请输入银行卡账号");
            tv_zfb_name.setHint("请输入银行卡绑定的真实姓名");
            bindAccountEntity.setCashType(1);
            tv_zfb_account.setInputType(InputType.TYPE_CLASS_NUMBER);
            tv_zfb_account.setFilters(new InputFilter[] {new InputFilter.LengthFilter(19)});
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
                if (QuickClickUtils.isFastClick()) {
                    return;
                }
                presenter.checkBindAccountData(bingType,
                        tv_zfb_account.getText().toString().trim(),
                        tv_zfb_name.getText().toString().trim(),
                        tv_bank_card_kaihuhang.getText().toString().trim());
                break;
        }
    }

    @Override
    public void bindSuccess(String bingType, String account, String name, String kaihuhang) {
        Bundle bundle = new Bundle();
        bundle.putParcelable(CommonUtil.KEY_VALUE_1, entity);
        if (bindAccountEntity == null) {
            bindAccountEntity = new BindAccountEntity();
        }
        bindAccountEntity.setCashType(Integer.parseInt(bingType));
        bindAccountEntity.setCashAccount(account);
        bindAccountEntity.setCashName(name);
        bindAccountEntity.setOpeningBank(kaihuhang);
        RuntimeUtils.selectBindAccountEntity = bindAccountEntity;
        MyActivityUtils.goActivity(AddNewBankCardActivity.this, CooperationWithdrawActivity.class, bundle);
        EventBus.getDefault().post(new EventBusObjectEntity(EventBusConstants.ReflashBindAccountList, null));
        finish();
    }

}

package com.kingyon.elevator.uis.fragments.user;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.blankj.utilcode.util.LogUtils;
import com.kingyon.elevator.R;
import com.kingyon.elevator.constants.EventBusConstants;
import com.kingyon.elevator.constants.FragmentConstants;
import com.kingyon.elevator.data.DataSharedPreferences;
import com.kingyon.elevator.entities.EventBusObjectEntity;
import com.kingyon.elevator.entities.UserEntity;
import com.kingyon.elevator.mvpbase.MvpBaseFragment;
import com.kingyon.elevator.presenter.EditPayPasswordFragmentPresenter;
import com.kingyon.elevator.utils.AccountNumUtils;
import com.kingyon.elevator.utils.MyActivityUtils;
import com.kingyon.elevator.utils.RuntimeUtils;
import com.kingyon.elevator.view.EditPayPasswordFragmentView;
import com.leo.afbaselibrary.utils.ToastUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 获取验证码进行验证修改支付密码
 */
public class CheckPayVerCodeFragment extends MvpBaseFragment<EditPayPasswordFragmentPresenter> implements EditPayPasswordFragmentView {

    @BindView(R.id.tv_phone_tips)
    TextView tv_phone_tips;
    @BindView(R.id.tv_input_code)
    EditText tv_input_code;
    @BindView(R.id.tv_get_code)
    TextView tv_get_code;
    @BindView(R.id.tv_next_confirm)
    TextView tv_next_confirm;
    UserEntity userEntity;

    @Override
    public EditPayPasswordFragmentPresenter initPresenter() {
        return new EditPayPasswordFragmentPresenter(getActivity());
    }

    @Override
    public void initView(Bundle savedInstanceState) {
        ButterKnife.bind(this, getContentView());
        EventBus.getDefault().register(this);
        userEntity = DataSharedPreferences.getUserBean();
        if (userEntity != null) {
            tv_phone_tips.setText("请输入" + AccountNumUtils.hidePhoneNum(userEntity.getPhone()) + "手机号收到的验证码");
        } else {
            ToastUtils.showToast(getActivity(), "您还未登录", Toast.LENGTH_SHORT);
            getActivity().finish();
        }

    }

    @Override
    public int getContentViewId() {
        return R.layout.fragment_check_pay_ver_code;
    }

    public static CheckPayVerCodeFragment newInstance() {
        Bundle args = new Bundle();
        CheckPayVerCodeFragment fragment = new CheckPayVerCodeFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void checkPayPwdIsInit(Boolean isInit) {

    }

    @Override
    public void showCountDownTime(int downtime) {
        if (downtime == 0) {
            tv_get_code.setEnabled(true);
            tv_get_code.setText("获取验证码");
        } else {
            tv_get_code.setEnabled(false);
            tv_get_code.setText("重新获取"+downtime + "S");
        }
    }

    @Override
    public void payPasswordEditSuccess() {

    }

    @Override
    public void clearAllInputPwd() {

    }

    @OnClick({R.id.tv_get_code, R.id.tv_next_confirm})
    public void OnClick(View view) {
        switch (view.getId()) {
            case R.id.tv_get_code:
                presenter.getVerCode(userEntity.getPhone());
                break;
            case R.id.tv_next_confirm:
                if (tv_input_code.getText().toString().trim().isEmpty()) {
                    showShortToast("请输入验证码");
                    return;
                }
                RuntimeUtils.payVerCode = tv_input_code.getText().toString().trim();
                MyActivityUtils.goFragmentContainerActivity(getActivity(), FragmentConstants.EditPayPasswordFragment, false);
                break;
            default:
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void eventHandler(EventBusObjectEntity eventBusObjectEntity) {
        if (eventBusObjectEntity.getEventCode() == EventBusConstants.ClosePayPwdVerCodeSence) {
            presenter.cancel();
            getActivity().finish();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}

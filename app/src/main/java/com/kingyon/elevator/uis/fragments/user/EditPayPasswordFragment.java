package com.kingyon.elevator.uis.fragments.user;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.kingyon.elevator.R;
import com.zhaoss.weixinrecorded.util.EventBusConstants;
import com.kingyon.elevator.customview.PayPasswordEditView;
import com.kingyon.elevator.data.DataSharedPreferences;
import com.zhaoss.weixinrecorded.util.EventBusObjectEntity;
import com.kingyon.elevator.entities.UserEntity;
import com.kingyon.elevator.interfaces.PayPasswordListener;
import com.kingyon.elevator.mvpbase.MvpBaseFragment;
import com.kingyon.elevator.presenter.EditPayPasswordFragmentPresenter;
import com.kingyon.elevator.utils.AccountNumUtils;
import com.kingyon.elevator.utils.KeyBoardUtils;
import com.kingyon.elevator.utils.RuntimeUtils;
import com.kingyon.elevator.view.EditPayPasswordFragmentView;
import com.leo.afbaselibrary.utils.ToastUtils;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 修改支付密码输入界面
 */
public class EditPayPasswordFragment extends MvpBaseFragment<EditPayPasswordFragmentPresenter> implements EditPayPasswordFragmentView {

    @BindView(R.id.input_pwd_tips)
    TextView input_pwd_tips;
    @BindView(R.id.pay_password_input_view)
    PayPasswordEditView pay_password_input_view;
    private Boolean isRememberPwd = false;//是否记得原密码
    UserEntity userEntity;
    private String oldPwd = "";
    private String newPwd1 = "";
    private String newPwd2 = "";
    private String verCode = "";


    @Override
    public EditPayPasswordFragmentPresenter initPresenter() {
        return new EditPayPasswordFragmentPresenter(getActivity());
    }

    @Override
    public void initView(Bundle savedInstanceState) {
        ButterKnife.bind(this, getContentView());
        userEntity = DataSharedPreferences.getUserBean();
        if (userEntity != null) {
            isRememberPwd = getArguments().getBoolean("isRememberPwd");
            pay_password_input_view.setPayPasswordListener(pwd -> {
                if (isRememberPwd) {
                    if (oldPwd.isEmpty()) {
                        oldPwd = pwd;
                        pay_password_input_view.clearAllText();
                        pay_password_input_view.clearEditText();
                        input_pwd_tips.setText(getString(R.string.input_pwd));
                    } else {
                        if (newPwd1.isEmpty()) {
                            newPwd1 = pwd;
                            pay_password_input_view.clearAllText();
                            pay_password_input_view.clearEditText();
                            input_pwd_tips.setText(getString(R.string.again_input_pwd));
                        } else {
                            if (newPwd2.isEmpty()) {
                                newPwd2 = pwd;
                                if (newPwd2.equals(newPwd1)) {
                                    //进行回调设置
                                    pay_password_input_view.clearAllText();
                                    pay_password_input_view.clearEditText();
                                    KeyBoardUtils.closeKeybord(pay_password_input_view.getEt_input_password(), getActivity());
                                    presenter.oldPasswordEditPayPwd(oldPwd, newPwd1);
                                } else {
                                    pay_password_input_view.clearAllText();
                                    pay_password_input_view.clearEditText();
                                    newPwd1 = "";
                                    newPwd2 = "";
                                    showShortToast("两次输入的新支付密码不一致，请重新输入");
                                    input_pwd_tips.setText(getString(R.string.input_pwd));
                                }
                            }
                        }
                    }
                } else {
                    if (newPwd1.isEmpty()) {
                        newPwd1 = pwd;
                        pay_password_input_view.clearAllText();
                        pay_password_input_view.clearEditText();
                        input_pwd_tips.setText(getString(R.string.again_input_pwd));
                    } else {
                        if (newPwd2.isEmpty()) {
                            newPwd2 = pwd;
                            if (newPwd2.equals(newPwd1)) {
                                //进行回调设置
                                pay_password_input_view.clearAllText();
                                pay_password_input_view.clearEditText();
                                KeyBoardUtils.closeKeybord(pay_password_input_view.getEt_input_password(), getActivity());
                                presenter.changePwdByCode(userEntity.getPhone(), verCode, newPwd1);
                            } else {
                                pay_password_input_view.clearAllText();
                                pay_password_input_view.clearEditText();
                                newPwd1 = "";
                                newPwd2 = "";
                                showShortToast("两次输入的新支付密码不一致，请重新输入");
                                input_pwd_tips.setText(getString(R.string.input_pwd));
                            }
                        }
                    }
                }
            });
            if (isRememberPwd) {
                input_pwd_tips.setText(getString(R.string.input_old_pwd));
            } else {
                input_pwd_tips.setText(getString(R.string.input_pwd));
                verCode = RuntimeUtils.payVerCode;

            }
            pay_password_input_view.postDelayed(() -> KeyBoardUtils.openKeybord(pay_password_input_view.getEt_input_password(), getActivity()), 150);
        } else {
            ToastUtils.showToast(getActivity(), "您还未登录", Toast.LENGTH_SHORT);
            getActivity().finish();
        }
    }

    @Override
    public int getContentViewId() {
        return R.layout.fragment_edit_pay_password;
    }

    public static EditPayPasswordFragment newInstance(Boolean isRememberPwd) {
        Bundle args = new Bundle();
        args.putBoolean("isRememberPwd", isRememberPwd);
        EditPayPasswordFragment fragment = new EditPayPasswordFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void checkPayPwdIsInit(Boolean isInit) {

    }

    @Override
    public void showCountDownTime(int downtime) {

    }

    @Override
    public void payPasswordEditSuccess() {
        EventBus.getDefault().post(new EventBusObjectEntity(EventBusConstants.ClosePayPwdVerCodeSence,null));
        getActivity().finish();
    }

    @Override
    public void clearAllInputPwd() {
        newPwd1 = "";
        newPwd2 = "";
        oldPwd = "";
        input_pwd_tips.setText(getString(R.string.input_old_pwd));
        KeyBoardUtils.openKeybord(pay_password_input_view.getEt_input_password(), getActivity());
    }
}

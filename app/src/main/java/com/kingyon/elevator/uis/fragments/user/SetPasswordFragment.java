package com.kingyon.elevator.uis.fragments.user;


import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.kingyon.elevator.R;
import com.kingyon.elevator.customview.PayPasswordEditView;
import com.kingyon.elevator.interfaces.PayPasswordListener;
import com.kingyon.elevator.mvpbase.MvpBaseFragment;
import com.kingyon.elevator.presenter.SetPasswordPresenter;
import com.kingyon.elevator.uis.activities.cooperation.CooperationActivity;
import com.kingyon.elevator.utils.KeyBoardUtils;
import com.kingyon.elevator.view.SetPasswordView;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 设置支付密码界面
 */
public class SetPasswordFragment extends MvpBaseFragment<SetPasswordPresenter> implements SetPasswordView {

    @BindView(R.id.ppe_pwd_text)
    PayPasswordEditView ppe_pwd_text;
    @BindView(R.id.tv_input_tips)
    TextView tv_input_tips;

    private String from = "";

    private String lastPayPwd = "";


    @Override
    public SetPasswordPresenter initPresenter() {
        return new SetPasswordPresenter(getActivity());
    }

    @Override
    public void initView(Bundle savedInstanceState) {
        ButterKnife.bind(this, getContentView());
        from = getArguments().getString("from");
        ppe_pwd_text.setPayPasswordListener(pwd -> {
            if (lastPayPwd.isEmpty()) {
                lastPayPwd = pwd;
                tv_input_tips.setText(getString(R.string.again_input_pwd));
                ppe_pwd_text.clearEditText();
                ppe_pwd_text.clearAllText();
            } else {
                //比较两次密码是否一致
                if (lastPayPwd.equals(pwd)) {
                    //两次密码相同
                    KeyBoardUtils.closeKeybord(ppe_pwd_text.getEt_input_password(), getActivity());
                    presenter.initPayPassword(pwd);
                } else {
                    lastPayPwd = "";
                    showShortToast("两次输入的支付密码不一致，请重新输入");
                    tv_input_tips.setText(getString(R.string.input_pwd));
                    ppe_pwd_text.clearEditText();
                    ppe_pwd_text.clearAllText();
                }
            }
        });
        ppe_pwd_text.postDelayed(() -> KeyBoardUtils.openKeybord(ppe_pwd_text.getEt_input_password(), getActivity()), 150);

    }

    @Override
    public int getContentViewId() {
        return R.layout.fragment_set_password;
    }

    public static SetPasswordFragment newInstance(String from) {
        Bundle args = new Bundle();
        args.putString("from", from);
        SetPasswordFragment fragment = new SetPasswordFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onPause() {
        super.onPause();
        KeyBoardUtils.closeKeybord(ppe_pwd_text.getEt_input_password(), getActivity());
    }

    @Override
    public void passwordInitSuccess() {
        if (from.equals("partner")) {
            startActivity(new Intent(getActivity(), CooperationActivity.class));
        }
        getActivity().finish();
    }
}

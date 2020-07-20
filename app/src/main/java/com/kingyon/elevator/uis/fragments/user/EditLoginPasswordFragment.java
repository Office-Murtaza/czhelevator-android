package com.kingyon.elevator.uis.fragments.user;


import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.blankj.utilcode.util.ActivityUtils;
import com.kingyon.elevator.R;
import com.kingyon.elevator.constants.FragmentConstants;
import com.kingyon.elevator.data.DataSharedPreferences;
import com.kingyon.elevator.entities.UserEntity;
import com.kingyon.elevator.mvpbase.MvpBaseFragment;
import com.kingyon.elevator.presenter.EditLoginPasswordPresenter;
import com.kingyon.elevator.uis.actiivty2.login.LoginActiivty;
import com.kingyon.elevator.utils.AccountNumUtils;
import com.kingyon.elevator.utils.MyActivityUtils;
import com.kingyon.elevator.view.EditLoginPasswordView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * 修改登录密码界面
 */
public class EditLoginPasswordFragment extends MvpBaseFragment<EditLoginPasswordPresenter> implements EditLoginPasswordView {


    @BindView(R.id.tv_next_confirm)
    TextView tv_next_confirm;
    @BindView(R.id.edit_tips)
    TextView edit_tips;
    @BindView(R.id.forgive_password)
    TextView forgive_password;
    @BindView(R.id.et_old_password)
    EditText et_old_password;
    @BindView(R.id.et_new_password)
    EditText et_new_password;
    @BindView(R.id.et_again_input_password)
    EditText et_again_input_password;
    UserEntity userEntity;
    @BindView(R.id.img_password)
    ImageView imgPassword;
    @BindView(R.id.img_password1)
    ImageView imgPassword1;
    Unbinder unbinder;
    private Boolean catShowPwd1 = true;
    private Boolean catShowPwd2 = true;
    @Override
    public EditLoginPasswordPresenter initPresenter() {
        return new EditLoginPasswordPresenter(getActivity());
    }

    @Override
    public void initView(Bundle savedInstanceState) {
        ButterKnife.bind(this, getContentView());
        userEntity = DataSharedPreferences.getUserBean();
        if (userEntity != null) {
            edit_tips.setText("正在为您的账号（" + AccountNumUtils.hidePhoneNum(userEntity.getPhone()) + "）修改登录密码");
        } else {
            getActivity().finish();
        }
    }

    @Override
    public int getContentViewId() {
        return R.layout.fragment_edit_login_password;
    }

    public static EditLoginPasswordFragment newInstance() {
        Bundle args = new Bundle();
        EditLoginPasswordFragment fragment = new EditLoginPasswordFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @OnClick({R.id.tv_next_confirm, R.id.forgive_password,R.id.img_password, R.id.img_password1})
    public void OnClick(View view) {
        switch (view.getId()) {
            case R.id.tv_next_confirm:
                //确认修改
                presenter.noCodeEditPassword(et_old_password.getText().toString().trim(), et_new_password.getText().toString().trim(), et_again_input_password.getText().toString().trim());
                break;
            case R.id.forgive_password:
                MyActivityUtils.goFragmentContainerActivity(getContext(), FragmentConstants.ResetLoginPasswordFragment);
                break;
            case R.id.img_password:
                if (catShowPwd1) {
                    catShowPwd1 = false;
                    imgPassword.setImageResource(R.mipmap.ic_login_password_on);
                    et_new_password.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                } else {
                    catShowPwd1 = true;
                    imgPassword.setImageResource(R.mipmap.ic_login_pasword_off);
                    et_new_password.setTransformationMethod(PasswordTransformationMethod.getInstance());
                }
                break;
            case R.id.img_password1:
                if (catShowPwd2) {
                    catShowPwd2 = false;
                    imgPassword1.setImageResource(R.mipmap.ic_login_password_on);
                    et_again_input_password.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                } else {
                    catShowPwd2 = true;
                    imgPassword1.setImageResource(R.mipmap.ic_login_pasword_off);
                    et_again_input_password.setTransformationMethod(PasswordTransformationMethod.getInstance());
                }
                break;
            default:
        }
    }

    @Override
    public void passwordEditSuccess() {
        // DialogUtils.getInstance().showEditSuccessTipsDialog(getActivity(), position -> {
        //DialogUtils.getInstance().hideEditSuccessTipsDialog();
        ActivityUtils.finishAllActivities();
        MyActivityUtils.goActivity(getContext(), LoginActiivty.class);
        //});
    }

    @Override
    public void showCountDownTime(int downtime) {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // TODO: inflate a fragment view
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        unbinder = ButterKnife.bind(this, rootView);
        return rootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }


}

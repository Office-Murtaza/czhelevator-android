package com.kingyon.elevator.uis.fragments.user;


import android.os.Bundle;
import android.support.v4.app.Fragment;
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
import com.kingyon.elevator.mvpbase.MvpBaseFragment;
import com.kingyon.elevator.presenter.EditLoginPasswordPresenter;
import com.kingyon.elevator.uis.activities.password.LoginActivity;
import com.kingyon.elevator.utils.DialogUtils;
import com.kingyon.elevator.utils.MyActivityUtils;
import com.kingyon.elevator.view.EditLoginPasswordView;
import com.kingyon.elevator.view.EditPayPasswordFragmentView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import me.nereo.multi_image_selector.bean.Image;

/**
 * 重置登录密码
 */
public class ResetLoginPasswordFragment extends MvpBaseFragment<EditLoginPasswordPresenter> implements EditLoginPasswordView {

    @BindView(R.id.et_input_phone)
    EditText et_input_phone;
    @BindView(R.id.et_code)
    EditText et_code;
    @BindView(R.id.et_password1)
    EditText et_password1;
    @BindView(R.id.et_password2)
    EditText et_password2;
    @BindView(R.id.cat_password1)
    ImageView cat_password1;
    @BindView(R.id.cat_password2)
    ImageView cat_password2;
    @BindView(R.id.tv_get_code)
    TextView tv_get_code;
    @BindView(R.id.tv_next_confirm)
    TextView tv_next_confirm;

    private Boolean catShowPwd1 = false;
    private Boolean catShowPwd2 = false;

    @Override
    public EditLoginPasswordPresenter initPresenter() {
        return new EditLoginPasswordPresenter(getActivity());
    }

    @Override
    public void initView(Bundle savedInstanceState) {
        ButterKnife.bind(this, getContentView());
    }

    @Override
    public int getContentViewId() {
        return R.layout.fragment_reset_login_password;
    }

    public static ResetLoginPasswordFragment newInstance() {
        Bundle args = new Bundle();
        ResetLoginPasswordFragment fragment = new ResetLoginPasswordFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @OnClick({R.id.tv_next_confirm, R.id.tv_get_code, R.id.cat_password1, R.id.cat_password2})
    public void OnClick(View view) {
        switch (view.getId()) {
            case R.id.tv_next_confirm:
                presenter.resetLoginPassword(et_input_phone.getText().toString().trim()
                        , et_code.getText().toString().trim(),
                        et_password1.getText().toString().trim(), et_password2.getText().toString().trim());
                break;
            case R.id.tv_get_code:
                presenter.getVerCode(et_input_phone.getText().toString().trim());
                break;
            case R.id.cat_password2:
                if (catShowPwd2) {
                    catShowPwd2 = false;
                    cat_password2.setImageResource(R.mipmap.mimachongzhi_kejiananniuer);
                    et_password2.setTransformationMethod(PasswordTransformationMethod.getInstance());
                } else {
                    catShowPwd2 = true;
                    cat_password2.setImageResource(R.mipmap.mimachongzhi_kejiananniuyi);
                    et_password2.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                }
                break;
            case R.id.cat_password1:
                if (catShowPwd1) {
                    catShowPwd1 = false;
                    cat_password1.setImageResource(R.mipmap.mimachongzhi_kejiananniuer);
                    et_password1.setTransformationMethod(PasswordTransformationMethod.getInstance());
                } else {
                    catShowPwd1 = true;
                    cat_password1.setImageResource(R.mipmap.mimachongzhi_kejiananniuyi);
                    et_password1.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                }
                break;
            default:
        }
    }


    @Override
    public void passwordEditSuccess() {
        //DialogUtils.getInstance().showEditSuccessTipsDialog(getActivity(), position -> {
           // DialogUtils.getInstance().hideEditSuccessTipsDialog();
            presenter.cancel();
            ActivityUtils.finishAllActivities();
            MyActivityUtils.goActivity(getContext(), LoginActivity.class);
       // });
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
}

package com.kingyon.elevator.uis.fragments.user;


import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.blankj.utilcode.util.ActivityUtils;
import com.kingyon.elevator.R;
import com.kingyon.elevator.constants.FragmentConstants;
import com.kingyon.elevator.data.DataSharedPreferences;
import com.kingyon.elevator.entities.UserEntity;
import com.kingyon.elevator.interfaces.OnItemClick;
import com.kingyon.elevator.mvpbase.MvpBaseFragment;
import com.kingyon.elevator.presenter.EditLoginPasswordPresenter;
import com.kingyon.elevator.uis.activities.password.LoginActivity;
import com.kingyon.elevator.utils.DialogUtils;
import com.kingyon.elevator.utils.MyActivityUtils;
import com.kingyon.elevator.view.EditLoginPasswordView;
import com.leo.afbaselibrary.utils.ActivityUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

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

    @Override
    public EditLoginPasswordPresenter initPresenter() {
        return new EditLoginPasswordPresenter(getActivity());
    }

    @Override
    public void initView(Bundle savedInstanceState) {
        ButterKnife.bind(this, getContentView());
        userEntity = DataSharedPreferences.getUserBean();
        if (userEntity != null) {
            edit_tips.setText("正在为您的账号（" + userEntity.getPhone() + "）修改登录密码");
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

    @OnClick({R.id.tv_next_confirm, R.id.forgive_password})
    public void OnClick(View view) {
        switch (view.getId()) {
            case R.id.tv_next_confirm:
                //确认修改
                presenter.noCodeEditPassword(et_old_password.getText().toString().trim(), et_new_password.getText().toString().trim(), et_again_input_password.getText().toString().trim());
                break;
            case R.id.forgive_password:
                MyActivityUtils.goFragmentContainerActivity(getContext(), FragmentConstants.ResetLoginPasswordFragment);
                break;
            default:
        }
    }

    @Override
    public void passwordEditSuccess() {
       // DialogUtils.getInstance().showEditSuccessTipsDialog(getActivity(), position -> {
            //DialogUtils.getInstance().hideEditSuccessTipsDialog();
            ActivityUtils.finishAllActivities();
            MyActivityUtils.goActivity(getContext(), LoginActivity.class);
        //});
    }

    @Override
    public void showCountDownTime(int downtime) {
        
    }
}

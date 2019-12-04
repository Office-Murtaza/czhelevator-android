package com.kingyon.elevator.uis.fragments.user;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.kingyon.elevator.R;
import com.kingyon.elevator.constants.FragmentConstants;
import com.kingyon.elevator.data.DataSharedPreferences;
import com.kingyon.elevator.entities.UserEntity;
import com.kingyon.elevator.utils.AccountNumUtils;
import com.kingyon.elevator.utils.MyActivityUtils;
import com.leo.afbaselibrary.utils.ToastUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 是否记得原密码提示界面
 */
public class RemeberPasswordFragment extends Fragment {

    View contentView;
    @BindView(R.id.account_tips)
    TextView account_tips;
    @BindView(R.id.tv_remember)
    TextView tv_remember;
    @BindView(R.id.tv_no_remember)
    TextView tv_no_remember;
    UserEntity userEntity;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        contentView = inflater.inflate(R.layout.fragment_remeber_password, container, false);
        ButterKnife.bind(this, contentView);
        userEntity = DataSharedPreferences.getUserBean();
        if (userEntity != null) {
            account_tips.setText("正在为您的账号（" + AccountNumUtils.hidePhoneNum(userEntity.getPhone()) + "）修改支付密码");
        } else {
            ToastUtils.showToast(getActivity(), "您还未登录", Toast.LENGTH_SHORT);
            getActivity().finish();
        }
        return contentView;
    }

    public static RemeberPasswordFragment newInstance() {
        Bundle args = new Bundle();
        RemeberPasswordFragment fragment = new RemeberPasswordFragment();
        fragment.setArguments(args);
        return fragment;
    }


    @OnClick({R.id.tv_remember, R.id.tv_no_remember})
    public void OnClick(View view) {
        switch (view.getId()) {
            case R.id.tv_remember:
                MyActivityUtils.goFragmentContainerActivity(getContext(), FragmentConstants.EditPayPasswordFragment, true);
                getActivity().finish();
                break;
            case R.id.tv_no_remember:
                MyActivityUtils.goFragmentContainerActivity(getContext(), FragmentConstants.CheckPayVerCodeFragment);
                getActivity().finish();
                break;
            default:
        }
    }


}

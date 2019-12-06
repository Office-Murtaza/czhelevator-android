package com.kingyon.elevator.uis.fragments.user;


import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;

import android.support.v4.hardware.fingerprint.FingerprintManagerCompat;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.blankj.utilcode.util.LogUtils;
import com.kingyon.elevator.R;
import com.kingyon.elevator.constants.FragmentConstants;
import com.kingyon.elevator.customview.InputPayPwdToCashDailog;
import com.kingyon.elevator.data.DataSharedPreferences;
import com.kingyon.elevator.entities.UserEntity;
import com.kingyon.elevator.finger.FingerprintCallback;
import com.kingyon.elevator.finger.FingerprintVerifyManager;
import com.kingyon.elevator.interfaces.IOnAuthKeyPrepared;
import com.kingyon.elevator.interfaces.InputPayPwdListener;
import com.kingyon.elevator.mvpbase.MvpBaseFragment;
import com.kingyon.elevator.presenter.SecuritySettingFragmentPresenter;
import com.kingyon.elevator.utils.DialogUtils;
import com.kingyon.elevator.utils.MyActivityUtils;
import com.kingyon.elevator.view.SecuritySettingFragmentView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 安全设置界面
 */
public class SecuritySettingFragment extends MvpBaseFragment<SecuritySettingFragmentPresenter> implements SecuritySettingFragmentView {

    @BindView(R.id.password_setting)
    LinearLayout password_setting;
    @BindView(R.id.finger_switch)
    LinearLayout finger_switch;
    @BindView(R.id.finger_status)
    TextView finger_status;
    UserEntity userEntity;

    @Override
    public SecuritySettingFragmentPresenter initPresenter() {
        return new SecuritySettingFragmentPresenter(getActivity());
    }

    @Override
    public void initView(Bundle savedInstanceState) {
        ButterKnife.bind(this, getContentView());
        userEntity = DataSharedPreferences.getUserBean();
        if (userEntity != null) {
            if (DataSharedPreferences.getBoolean(DataSharedPreferences.IS_OPEN_FINGER, false)) {
                finger_status.setText("已开启");
            } else {
                finger_status.setText("未开启");
            }
        } else {
            showShortToast("您未登录");
            getActivity().finish();
        }
    }

    @Override
    public int getContentViewId() {
        return R.layout.fragment_security_setting;
    }


    @OnClick({R.id.finger_switch, R.id.password_setting})
    public void OnClick(View view) {
        switch (view.getId()) {
            case R.id.finger_switch:
                try {
                    if (!FingerprintManagerCompat.from(getActivity()).isHardwareDetected()) {
                        showShortToast("您的设备不支持指纹识别");
                        return;
                    }
                    if (Build.VERSION.SDK_INT < 6.0) {
                        showShortToast("您的设备系统版本过低");
                        return;
                    }
                    if (DataSharedPreferences.getBoolean(DataSharedPreferences.IS_OPEN_FINGER, false)) {
                        showTipsDialog();
                    } else {
                        presenter.checkIsInitPayPwd();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case R.id.password_setting:
                MyActivityUtils.goFragmentContainerActivity(getContext(), FragmentConstants.EditPassWordFragment);
                break;
        }
    }

    private void showTipsDialog() {
        new AlertDialog.Builder(getActivity())
                .setTitle("提示")
                .setMessage("指纹识别已经开启，是否需要关闭？")
                .setPositiveButton("关闭", (dialog, which) -> {
                    DataSharedPreferences.saveBoolean(DataSharedPreferences.IS_OPEN_FINGER, false);
                    finger_status.setText("未开启");
                    showShortToast("指纹识别已经关闭");
                })
                .setNegativeButton("取消", null)
                .show();
    }

    private void fingerprintInit() {
        FingerprintVerifyManager.Builder builder = new FingerprintVerifyManager.Builder(getActivity());
        builder.callback(fingerprintCallback)
                .fingerprintColor(ContextCompat.getColor(getActivity(), R.color.colorPrimary))
                .build();
    }


    private FingerprintCallback fingerprintCallback = new FingerprintCallback() {
        @Override
        public void onSucceeded() {
            showShortToast("指纹验证成功！");
            DataSharedPreferences.saveBoolean(DataSharedPreferences.IS_OPEN_FINGER, true);
            finger_status.setText("已开启");
        }

        @Override
        public void onFailed() {

        }

        @Override
        public void onUsepwd() {

        }

        @Override
        public void onCancel() {
            showShortToast("您取消了指纹验证");
        }

        @Override
        public void tooManyAttempts() {
            showShortToast("验证错误次数过多，请30s稍后再试");
            DialogUtils.getInstance().hideFingerCheckDailog();
        }

        @Override
        public void onHwUnavailable() {
            showShortToast("您的手机暂不支持指纹识别或指纹识别不可用");
        }

        @Override
        public void onNoneEnrolled() {
            showShortToast("您还未录入指纹，请先去系统设置里录入指纹！");
        }

    };

    public static SecuritySettingFragment newInstance() {

        Bundle args = new Bundle();

        SecuritySettingFragment fragment = new SecuritySettingFragment();
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void checkPayPwdIsInit(Boolean isInit) {
        if (isInit) {
            DialogUtils.getInstance().showInputPayPwdToCashDailog(getContext(), password -> {
                DialogUtils.getInstance().hideInputPayPwdToCashDailog();
                presenter.checkPayPasswordIsRight(password);
            });
        } else {
            showShortToast("当前还未设置支付密码，请先设置支付密码");
            MyActivityUtils.goFragmentContainerActivity(getContext(), FragmentConstants.SetPasswordFragment, "setting");
        }
    }

    @Override
    public void checkPayPwdIsSuccess() {
        fingerprintInit();
    }
}

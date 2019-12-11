package com.kingyon.elevator.uis.activities.password;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.kingyon.elevator.R;
import com.kingyon.elevator.nets.CustomApiCallback;
import com.kingyon.elevator.nets.NetService;
import com.kingyon.elevator.utils.CommonUtil;
import com.leo.afbaselibrary.nets.exceptions.ApiException;
import com.leo.afbaselibrary.uis.activities.BaseSwipeBackActivity;
import com.leo.afbaselibrary.utils.ActivityUtil;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by GongLi on 2018/8/24.
 * Email：lc824767150@163.com
 */

public class ModifyPasswordActivity extends BaseSwipeBackActivity {
    @BindView(R.id.pre_v_right)
    TextView preVRight;
    @BindView(R.id.et_old_password)
    EditText etOldPassword;
    @BindView(R.id.ll_old_pwd)
    LinearLayout llOldPwd;
    @BindView(R.id.et_new_password)
    EditText etNewPassword;
    @BindView(R.id.et_new_repeate)
    EditText etNewRepeate;
    @BindView(R.id.tv_reset)
    TextView tvReset;

    @Override
    protected String getTitleText() {
        return "修改密码";
    }

    @Override
    protected void initViews(Bundle savedInstanceState) {
        preVRight.setText("完成");
    }

    @Override
    public int getContentViewId() {
        return R.layout.activity_modify_password;
    }

    @OnClick({R.id.pre_v_right, R.id.tv_reset})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.pre_v_right:
                modifyPwd();
                break;
            case R.id.tv_reset:
                startActivity(ResetPasswordActivity.class);
                break;
        }
    }

    private void modifyPwd() {
        if (TextUtils.isEmpty(CommonUtil.getEditText(etOldPassword))) {
            showToast("请输入旧密码");
            return;
        }
        if (TextUtils.isEmpty(CommonUtil.getEditText(etNewPassword))) {
            showToast("请输入新密码");
            return;
        }
        if (TextUtils.equals(CommonUtil.getEditText(etOldPassword), CommonUtil.getEditText(etNewPassword))) {
            showToast("输入新旧密码不能一致");
            return;
        }
        if (CommonUtil.getEditText(etNewPassword).length() < 8) {
            showToast("新密码长度不足8位");
            return;
        }
        if (TextUtils.isEmpty(CommonUtil.getEditText(etNewRepeate))) {
            showToast("请再次输入新密码");
            return;
        }
        if (!TextUtils.equals(CommonUtil.getEditText(etNewPassword), CommonUtil.getEditText(etNewRepeate))) {
            showToast("两次输入的密码不一致");
            return;
        }
        showProgressDialog(getString(R.string.wait));
        preVRight.setEnabled(false);
        NetService.getInstance().changePassword(CommonUtil.getEditText(etOldPassword), CommonUtil.getEditText(etNewPassword))
                .compose(this.<String>bindLifeCycle())
                .subscribe(new CustomApiCallback<String>() {
                    @Override
                    protected void onResultError(ApiException ex) {
                        showToast(ex.getDisplayMessage());
                        preVRight.setEnabled(true);
                        hideProgress();
                    }

                    @Override
                    public void onNext(String s) {
                        preVRight.setEnabled(true);
                        showToast("操作成功");
                        hideProgress();
                        ActivityUtil.finishAllNotThis("ModifyPasswordActivity");
                        startActivity(LoginActivity.class);
                        finish();
                    }
                });
    }
}

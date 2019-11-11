package com.kingyon.elevator.uis.activities.password;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.kingyon.elevator.R;
import com.kingyon.elevator.data.DataSharedPreferences;
import com.kingyon.elevator.nets.CustomApiCallback;
import com.kingyon.elevator.nets.NetService;
import com.kingyon.elevator.utils.CheckCodePresenter;
import com.kingyon.elevator.utils.CommonUtil;
import com.leo.afbaselibrary.nets.exceptions.ApiException;
import com.leo.afbaselibrary.uis.activities.BaseSwipeBackActivity;
import com.leo.afbaselibrary.utils.ActivityUtil;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by GongLi on 2018/9/12.
 * Email：lc824767150@163.com
 */

public class ModifyPhoneSecondActivity extends BaseSwipeBackActivity {

    @BindView(R.id.et_mobile)
    EditText etMobile;
    @BindView(R.id.tv_code)
    TextView tvCode;
    @BindView(R.id.et_code)
    EditText etCode;
    @BindView(R.id.tv_tips)
    TextView tvTips;
    @BindView(R.id.tv_ensure)
    TextView tvEnsure;

    private CheckCodePresenter checkCodePresenter;

    @Override
    protected String getTitleText() {
        return "修改手机";
    }

    @Override
    public int getContentViewId() {
        return R.layout.activity_modify_phone_second;
    }

    @Override
    protected void initViews(Bundle savedInstanceState) {
        checkCodePresenter = new CheckCodePresenter(this, tvCode);
    }

    @OnClick({R.id.tv_code, R.id.tv_ensure})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_code:
                checkCodePresenter.getCode(CommonUtil.getEditText(etMobile), CheckCodePresenter.VerifyCodeType.BIND_NEW);
                break;
            case R.id.tv_ensure:
                modify();
                break;
        }
    }

    private void modify() {
        if (TextUtils.isEmpty(CommonUtil.getEditText(etMobile))) {
            showToast("请输入手机号");
            return;
        }
        if (TextUtils.isEmpty(CommonUtil.getEditText(etCode))) {
            showToast("请输入验证码");
            return;
        }
        showProgressDialog(getString(R.string.wait));
        tvEnsure.setEnabled(false);
        NetService.getInstance().unbindPhone(CommonUtil.getEditText(etMobile), CommonUtil.getEditText(etCode), "NEW")
                .compose(this.<String>bindLifeCycle())
                .subscribe(new CustomApiCallback<String>() {
                    @Override
                    protected void onResultError(ApiException ex) {
                        hideProgress();
                        tvTips.setText(ex.getDisplayMessage());
                        showToast(ex.getDisplayMessage());
                        tvEnsure.setEnabled(true);
                    }

                    @Override
                    public void onNext(String s) {
                        tvEnsure.setEnabled(true);
                        showToast("操作成功");
                        DataSharedPreferences.saveLoginName(CommonUtil.getEditText(etMobile));
                        hideProgress();
                        startActivity(LoginActivity.class);
                        ActivityUtil.finishAllNotLogin();
                    }
                });
    }
}

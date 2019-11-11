package com.kingyon.elevator.uis.activities.password;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.kingyon.elevator.R;
import com.kingyon.elevator.nets.CustomApiCallback;
import com.kingyon.elevator.nets.NetService;
import com.kingyon.elevator.utils.CheckCodePresenter;
import com.kingyon.elevator.utils.CommonUtil;
import com.leo.afbaselibrary.nets.exceptions.ApiException;
import com.leo.afbaselibrary.uis.activities.BaseSwipeBackActivity;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by GongLi on 2018/9/12.
 * Email：lc824767150@163.com
 */

public class ModifyPhoneFirstActivity extends BaseSwipeBackActivity {
    @BindView(R.id.tv_mobile)
    TextView tvMobile;
    @BindView(R.id.tv_code)
    TextView tvCode;
    @BindView(R.id.et_code)
    EditText etCode;
    @BindView(R.id.tv_tips)
    TextView tvTips;
    @BindView(R.id.tv_next)
    TextView tvNext;

    private CheckCodePresenter checkCodePresenter;
    private String mobile;

    @Override
    protected String getTitleText() {
        mobile = getIntent().getStringExtra(CommonUtil.KEY_VALUE_1);
        return "修改手机";
    }

    @Override
    public int getContentViewId() {
        return R.layout.activity_modify_phone_first;
    }

    @Override
    protected void initViews(Bundle savedInstanceState) {
        checkCodePresenter = new CheckCodePresenter(this, tvCode);
        tvMobile.setText(CommonUtil.getHideMobile(mobile));
    }

    @OnClick({R.id.tv_code, R.id.tv_next})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_code:
                checkCodePresenter.getCode(mobile, CheckCodePresenter.VerifyCodeType.UNBIND_OLD);
                break;
            case R.id.tv_next:
                verify();
                break;
        }
    }

    private void verify() {
        if (TextUtils.isEmpty(CommonUtil.getEditText(etCode))) {
            showToast("请输入验证码");
            return;
        }
        showProgressDialog(getString(R.string.wait));
        tvNext.setEnabled(false);
        NetService.getInstance().unbindPhone(mobile, CommonUtil.getEditText(etCode), "OLD")
                .compose(this.<String>bindLifeCycle())
                .subscribe(new CustomApiCallback<String>() {
                    @Override
                    protected void onResultError(ApiException ex) {
                        hideProgress();
                        tvTips.setText(ex.getDisplayMessage());
                        showToast(ex.getDisplayMessage());
                        tvNext.setEnabled(true);
                    }

                    @Override
                    public void onNext(String s) {
                        tvNext.setEnabled(true);
                        hideProgress();
                        startActivity(ModifyPhoneSecondActivity.class);
                        finish();
                    }
                });
    }
}

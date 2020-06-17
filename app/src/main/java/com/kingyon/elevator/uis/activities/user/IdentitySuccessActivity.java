package com.kingyon.elevator.uis.activities.user;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.TextView;

import com.kingyon.elevator.R;
import com.kingyon.elevator.constants.Constants;
import com.kingyon.elevator.entities.IdentityInfoEntity;
import com.kingyon.elevator.nets.CustomApiCallback;
import com.kingyon.elevator.nets.NetService;
import com.leo.afbaselibrary.nets.exceptions.ApiException;
import com.leo.afbaselibrary.nets.exceptions.ResultException;
import com.leo.afbaselibrary.uis.activities.BaseStateLoadingActivity;

import butterknife.BindView;

/**
 * Created by GongLi on 2019/1/9.
 * Email：lc824767150@163.com
 */

public class IdentitySuccessActivity extends BaseStateLoadingActivity {
    @BindView(R.id.tv_name)
    TextView tvName;

    @Override
    protected String getTitleText() {
        return "认证成功";
    }

    @Override
    public int getContentViewId() {
        return R.layout.activity_identity_success;
    }

    @Override
    protected void initViews(Bundle savedInstanceState) {

    }

    @Override
    protected void loadData() {
        NetService.getInstance().getIdentityInformation()
                .compose(this.<IdentityInfoEntity>bindLifeCycle())
                .subscribe(new CustomApiCallback<IdentityInfoEntity>() {
                    @Override
                    protected void onResultError(ApiException ex) {
                        showToast(ex.getDisplayMessage());
                        loadingComplete(STATE_ERROR);
                    }

                    @Override
                    public void onNext(IdentityInfoEntity identityInfoEntity) {
                        if (identityInfoEntity == null) {
                            throw new ResultException(9001, "返回参数异常");
                        }
                        if (!TextUtils.equals(Constants.IDENTITY_STATUS.AUTHED, identityInfoEntity.getStatus())) {
                            throw new ResultException(9001, "返回参数异常");
                        }
                        boolean company = TextUtils.equals(Constants.IDENTITY_TYPE.COMPANY, identityInfoEntity.getType());
                        tvName.setText(company ? identityInfoEntity.getCompanyName() : identityInfoEntity.getPersonName());
                        tvTitle.setText(company ? "企业认证" : "个人认证");
                        loadingComplete(STATE_CONTENT);
                    }
                });
    }
}

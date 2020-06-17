package com.kingyon.elevator.uis.activities.user;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.kingyon.elevator.R;
import com.kingyon.elevator.utils.CommonUtil;
import com.kingyon.elevator.utils.OCRUtil;
import com.leo.afbaselibrary.uis.activities.BaseSwipeBackActivity;

import butterknife.OnClick;

/**
 * Created by GongLi on 2019/1/9.
 * Email：lc824767150@163.com
 */

public class IdentityInfoActivity extends BaseSwipeBackActivity {
    @Override
    protected String getTitleText() {
        return "认证信息";
    }

    @Override
    public int getContentViewId() {
        return R.layout.activity_identity_info;
    }

    @Override
    protected void initViews(Bundle savedInstanceState) {
    }

    @OnClick({R.id.tv_auth_person, R.id.tv_auth_company})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_auth_person:
                if (OCRUtil.getInstance().isHasGotToken()) {
                    startActivityForResult(IdentityPersonActivity.class, CommonUtil.REQ_CODE_1);
                } else {
                    showToast("初始化未完成");
                }
                break;
            case R.id.tv_auth_company:
                startActivityForResult(IdentityCompanyActivity.class, CommonUtil.REQ_CODE_1);
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (RESULT_OK == resultCode) {
            finish();
        }
    }
}

package com.kingyon.elevator.uis.activities;

import com.kingyon.elevator.application.AppContent;
import com.kingyon.elevator.utils.JumpUtils;

/**
 * Created by GongLi on 2018/8/15.
 * Email：lc824767150@163.com
 */

public class AdvertisionActivity extends HtmlActivity {

    @Override
    public void onBackPressed() {
        JumpUtils.getInstance().jumpToRoleMain(this, AppContent.getInstance().getMyUserRole());
        finish();
    }
}

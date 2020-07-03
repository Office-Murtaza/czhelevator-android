package com.kingyon.elevator.uis.activities.user;

import android.os.Bundle;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.leo.afbaselibrary.uis.activities.BaseActivity;

import static com.czh.myversiontwo.utils.Constance.ACTIVITY_USER_REGION;

/**
 * @Created By Admin  on 2020/7/2
 * @Email : 163235610@qq.com
 * @Author:Mrczh
 * @Instructions:选择地区
 */
@Route(path = ACTIVITY_USER_REGION)
public class UserRegionActivity extends BaseActivity {
    @Override
    public int getContentViewId() {
        return 0;
    }

    @Override
    public void init(Bundle savedInstanceState) {

    }
}

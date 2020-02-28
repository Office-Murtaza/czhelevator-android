package com.kingyon.elevator.uis.activities;

import android.app.Activity;
import android.os.Bundle;

import com.kingyon.elevator.R;
import com.kingyon.elevator.utils.MyActivityUtils;
import com.leo.afbaselibrary.uis.activities.BaseActivity;
import com.leo.afbaselibrary.uis.activities.BaseHtmlActivity;
import com.leo.afbaselibrary.utils.AFUtil;

/**
 * Created by GongLi on 2018/3/1.
 * Email：lc824767150@163.com
 */

public class HtmlActivity extends BaseHtmlActivity {

    public static void start(Activity activity, String title, String url) {
        Bundle bundle = new Bundle();
        bundle.putString(BaseHtmlActivity.TITLE, "    ");
        bundle.putString(BaseHtmlActivity.URL, url);
        MyActivityUtils.goActivity(activity,HtmlActivity.class,bundle);
        //activity.startActivity(HtmlActivity.class, bundle);
    }

    @Override
    protected String getTitleText() {
        return getIntent().getStringExtra(TITLE);
    }

    @Override
    public int getContentViewId() {
        return R.layout.activity_html;
    }

    @Override
    protected OnLoadUrl getOnLoadUrl() {
        return new OnLoadUrl() {
            @Override
            public boolean onLoadUrl(String url) {
                if (url != null && !url.startsWith("http")) {
                    AFUtil.openScheme(HtmlActivity.this, url);
                    return true;
                }
                return false;
            }
        };
    }

    @Override
    protected void refreshComplete() {
        super.refreshComplete();
        mLayoutRefresh.setEnabled(false);
    }
}

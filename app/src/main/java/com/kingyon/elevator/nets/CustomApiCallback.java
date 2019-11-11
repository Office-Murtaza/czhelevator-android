package com.kingyon.elevator.nets;

import android.app.Activity;

import com.kingyon.elevator.entities.VersionEntity;
import com.leo.afbaselibrary.nets.callbacks.AbsAPICallback;
import com.leo.afbaselibrary.nets.exceptions.ApiException;
import com.leo.afbaselibrary.nets.exceptions.ResultException;
import com.leo.afbaselibrary.uis.activities.BaseActivity;
import com.leo.afbaselibrary.utils.ActivityUtil;
import com.leo.afbaselibrary.utils.download.DownloadApkUtil;

/**
 * Created by GongLi on 2017/10/24.
 * Email：lc824767150@163.com
 */

public abstract class CustomApiCallback<T> extends AbsAPICallback<T> {

    public CustomApiCallback() {
        super();
    }

    @Override
    protected void resultError(ResultException e) {
        super.resultError(e);
        if (e.getErrCode() == ApiException.VERSION_LOW) {
            Activity currentActivity = ActivityUtil.getCurrentActivity();
            if (currentActivity instanceof BaseActivity) {
                checkVersion((BaseActivity) currentActivity);
            }
        }
    }

    private void checkVersion(final BaseActivity currentActivity) {
        NetService.getInstance().getLatestVersion(currentActivity)
                .compose(currentActivity.<VersionEntity>bindLifeCycle())
                .subscribe(new CustomApiCallback<VersionEntity>() {
                    @Override
                    public void onResultError(ApiException ex) {
                    }

                    @Override
                    public void onNext(VersionEntity versionEntity) {
                        if (versionEntity != null) {
                            DownloadApkUtil.getInstance(currentActivity)
                                    .isDownloadNewVersion(currentActivity, versionEntity, false);
                        }
                    }
                });
    }
}

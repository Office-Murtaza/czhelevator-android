package com.leo.afbaselibrary.utils.download;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.leo.afbaselibrary.R;
import com.leo.afbaselibrary.utils.ToastUtils;
import com.liulishuo.filedownloader.BaseDownloadTask;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.ButterKnife;

/**
 * Created by gongli on 2017/1/19 11:28
 * email: lc824767150@163.com
 */

public class DownloadProgressDialog extends Dialog implements DialogInterface.OnDismissListener {

    private TextView tvTips;
    private RoundProgressBar progressBar;
    private BaseDownloadTask downloadTask;
    private boolean isTaskCompleted;
    private boolean notTip;

    DownloadProgressDialog(Context context) {
        super(context, R.style.normal_dialog_corner);
        setContentView(R.layout.download_dialog_progress);
        ButterKnife.bind(this);

        tvTips = findViewById(R.id.tv_tips);
        progressBar = findViewById(R.id.progress_bar);

        progressBar.setMax(100);

        setCancelable(true);
        setCanceledOnTouchOutside(true);

        setOnDismissListener(this);
    }

    public void setProgress(int progress) {
        if (progress < 0) {
            return;
        }
        if (tvTips.getVisibility() == View.VISIBLE) {
            tvTips.setVisibility(View.GONE);
        }
        progressBar.setProgress(progress);
    }

    public void setTip(String tip) {
        if (TextUtils.isEmpty(tip)) {
            return;
        }
        progressBar.setProgress(0);
        tvTips.setVisibility(View.VISIBLE);
        tvTips.setText(tip);
    }

    @Override
    public void show() {
        super.show();
        EventBus.getDefault().register(this);
        if (downloadTask != null) {
            isTaskCompleted = false;
            downloadTask.start();
        }
    }

    public void dismiss(boolean notTip) {
        this.notTip = notTip;
        super.dismiss();
    }

    BaseDownloadTask getDownloadTask() {
        return downloadTask;
    }

    void setDownloadTask(BaseDownloadTask downloadTask) {
        this.downloadTask = downloadTask;
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        if (!notTip && downloadTask != null && downloadTask.isRunning() && !isTaskCompleted) {
            ToastUtils.toast(getContext(), "已转至后台下载");
        }
        EventBus.getDefault().unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onProgress(ApkDownloadProgressEntity entity) {
        if (isShowing()) {
            if (entity.getType() != DownloadApkUtil.DownloadApkCallBackType) {
                return;
            }
            int progress = entity.getProgress();
            if (progress == DownloadApkUtil.DOWNLOAD_STARTED) {
                setTip("开始下载");
            }
            if (progress == DownloadApkUtil.DOWNLOAD_ERROR) {
                setTip("下载失败");
            }
            if (progress == DownloadApkUtil.DOWNLOAD_PAUSED) {
                setTip("已暂停");
            }
            if (progress == DownloadApkUtil.DOWNLOAD_WAIT) {
                setTip("准备中");
            }
            if (progress >= 0) {
                setProgress(progress);
            }
            if (progress >= 100) {
                if (isShowing()) {
                    isTaskCompleted = true;
                    dismiss();
                }
            }
        }
    }
}

package com.kingyon.elevator.utils;

import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.support.v4.content.FileProvider;
import android.widget.Toast;

import com.kingyon.elevator.application.App;
import com.kingyon.elevator.data.DataSharedPreferences;
import com.kingyon.elevator.entities.ReportDownloadEntity;
import com.kingyon.elevator.entities.UserEntity;
import com.kingyon.elevator.uis.dialogs.DownloadReportProgressDialog;
import com.leo.afbaselibrary.utils.AFUtil;
import com.leo.afbaselibrary.utils.download.DownloadApkUtil;
import com.liulishuo.filedownloader.BaseDownloadTask;
import com.liulishuo.filedownloader.FileDownloadListener;
import com.liulishuo.filedownloader.FileDownloader;

import org.greenrobot.eventbus.EventBus;

import java.io.File;

/**
 * Created by GongLi on 2018/6/5.
 * Email：lc824767150@163.com
 */

public class DownloadUtils {

    private static DownloadUtils downloadUtils;

    private DownloadReportProgressDialog downloadDialog;

    public static DownloadUtils getInstance() {
        if (downloadUtils == null) {
            downloadUtils = new DownloadUtils();
        }
        return downloadUtils;
    }

    public String getResourceDownloadPath(String name) {
        UserEntity userBean = DataSharedPreferences.getUserBean();
        String userTag = userBean != null ? String.valueOf(userBean.getObjectId()) : "";
        return getDownloadFile() + File.separator + "contract" + File.separator + String.format("%s_%s", userTag, name);
    }

    public File getResourceDownloadFile(String date) {
        return new File(getResourceDownloadPath(date));
    }

    public File getDownloadFile() {
        return App.getInstance().getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS);
    }

    public void openReportFile(Context context, String url, String name) {
        File resourceDownloadFile = getResourceDownloadFile(name);
        if (resourceDownloadFile.exists()) {
            OpenDocumentsUtils.getInstance().openFile(App.getInstance(), resourceDownloadFile, OpenDocumentsUtils.getInstance().getFileSuffix(resourceDownloadFile.getAbsolutePath()));
        } else {
            downloadReportFile(context, url, name);
        }
    }

    private void openReportFile(String date) {
        OpenDocumentsUtils.getInstance().openFile(App.getInstance(), getResourceDownloadFile(date)
                , OpenDocumentsUtils.getInstance().getFileSuffix(getResourceDownloadPath(date)));
    }

    public void downloadReportFile(Context context, String url, String name) {
        if (downloadDialog == null) {
            downloadDialog = new DownloadReportProgressDialog(context);
        }
        downloadDialog.setDownloadTask(creatDownloadFileTask(context, url, name));
        downloadDialog.setDate(name);
        downloadDialog.show();
        downloadDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                onDestroy();
            }
        });
    }

    private BaseDownloadTask creatDownloadFileTask(final Context context, String url, final String date) {
        BaseDownloadTask baseDownloadTask = FileDownloader.getImpl().create(url);
        baseDownloadTask.setPath(getResourceDownloadPath(date))
                .setListener(new FileDownloadListener() {

                    @Override
                    protected void pending(BaseDownloadTask task, int soFarBytes, int totalBytes) {
                        EventBus.getDefault().post(new ReportDownloadEntity(date, DownloadApkUtil.DOWNLOAD_STARTED));
                    }

                    @Override
                    protected void progress(BaseDownloadTask task, int soFarBytes, int totalBytes) {
                        int currentProgress = (int) (((long) soFarBytes) * 100 / totalBytes);
                        EventBus.getDefault().post(new ReportDownloadEntity(date, currentProgress));
                    }

                    @Override
                    protected void completed(BaseDownloadTask task) {
                        EventBus.getDefault().post(new ReportDownloadEntity(date, 100));
                        openReportFile(date);
                    }

                    @Override
                    protected void paused(BaseDownloadTask task, int soFarBytes, int totalBytes) {
                        EventBus.getDefault().post(new ReportDownloadEntity(date, DownloadApkUtil.DOWNLOAD_PAUSED));
                    }

                    @Override
                    protected void error(BaseDownloadTask task, Throwable e) {
                        EventBus.getDefault().post(new ReportDownloadEntity(date, DownloadApkUtil.DOWNLOAD_ERROR));
                    }

                    @Override
                    protected void warn(BaseDownloadTask task) {
                        EventBus.getDefault().post(new ReportDownloadEntity(date, DownloadApkUtil.DOWNLOAD_WAIT));
                    }
                });
        return baseDownloadTask;
    }

    public void onDestroy() {
        if (downloadDialog != null) {
            downloadDialog = null;
        }
    }

    //下载器
    private DownloadManager downloadManager;
    //下载的ID
    private long downloadId;

    //下载apk
    public void downloadAPK(Context context, String url) {
        //创建下载任务
        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(url));
        //移动网络情况下是否允许漫游
        request.setAllowedOverRoaming(false);
        //在通知栏中显示，默认就是显示的
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE);
        request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI);
        request.setTitle("海报工厂");
        request.setDescription("海报工厂下载中...");
        request.setVisibleInDownloadsUi(true);

        //设置下载的路径
        File file = new File(context.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS), "hbgc.apk");
        request.setDestinationUri(Uri.fromFile(file));
        //获取DownloadManager
        if (downloadManager == null)
            downloadManager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
        //将下载请求加入下载队列，加入下载队列后会给该任务返回一个long型的id，通过该id可以取消任务，重启任务、获取下载的文件等等
        if (downloadManager != null) {
            downloadId = downloadManager.enqueue(request);
        }

        //注册广播接收者，监听下载状态
        App.getInstance().registerReceiver(receiver,
                new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));
    }

    //广播监听下载的各个状态
    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            checkStatus();
        }
    };

    //检查下载状态
    private void checkStatus() {
        DownloadManager.Query query = new DownloadManager.Query();
        //通过下载的id查找
        query.setFilterById(downloadId);
        Cursor cursor = downloadManager.query(query);
        if (cursor.moveToFirst()) {
            int status = cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_STATUS));
            switch (status) {
                //下载暂停
                case DownloadManager.STATUS_PAUSED:
                    break;
                //下载延迟
                case DownloadManager.STATUS_PENDING:
                    break;
                //正在下载
                case DownloadManager.STATUS_RUNNING:
                    break;
                //下载完成
                case DownloadManager.STATUS_SUCCESSFUL:
                    //下载完成安装APK
                    installAPK();
                    cursor.close();
                    break;
                //下载失败
                case DownloadManager.STATUS_FAILED:
                    Toast.makeText(App.getContext(), "下载失败", Toast.LENGTH_SHORT).show();
                    cursor.close();
                    App.getInstance().unregisterReceiver(receiver);
                    break;
            }
        }
    }

    public void installAPK() {
        File apkFile = new File(App.getContext().getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS), "hbgc.apk");
        if (apkFile.exists()) {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            //判断是否是AndroidN以及更高的版本
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                Uri contentUri = FileProvider.getUriForFile(App.getContext(), AFUtil.getAppProcessName(App.getContext()) + ".provider", apkFile);
                intent.setDataAndType(contentUri, "application/vnd.android.package-archive");
            } else {
                intent.setDataAndType(Uri.fromFile(apkFile), "application/vnd.android.package-archive");
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            }
            App.getContext().startActivity(intent);
        }
    }

//    private static String getFileName(String name, String url) {
//        String result;
//        if (!TextUtils.isEmpty(url)) {
//            int index1 = url.lastIndexOf("/");
//            int index2 = url.lastIndexOf(".");
//            if (index1 >= 0 && index2 >= 0 && index1 < index2) {
//                result = url.substring(index1, index2);
//            } else {
//                result = "";
//            }
//        } else {
//            result = "";
//        }
//        return result + name;
//    }
}

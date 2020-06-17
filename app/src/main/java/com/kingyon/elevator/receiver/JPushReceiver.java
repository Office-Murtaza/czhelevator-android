package com.kingyon.elevator.receiver;

import android.app.ActivityManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.text.TextUtils;
import android.util.Log;

import com.blankj.utilcode.util.LogUtils;
import com.czh.myversiontwo.activity.ActivityUtils;
import com.google.gson.Gson;
import com.kingyon.elevator.R;
import com.kingyon.elevator.data.DataSharedPreferences;
import com.kingyon.elevator.entities.PushMessageEntity;
import com.kingyon.elevator.entities.ReceivedPushEntity;
import com.kingyon.elevator.entities.RegisterIdEntity;
import com.kingyon.elevator.uis.activities.MainActivity;
import com.kingyon.elevator.uis.activities.user.MessageCenterActivity;
import com.kingyon.elevator.utils.CommonUtil;
import com.leo.afbaselibrary.utils.AFUtil;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

import cn.jpush.android.api.JPushInterface;

import static com.czh.myversiontwo.utils.Constance.ACTIVITY_TEXT_CONTENT;

/**
 * Created by gongli on 2017/7/17 17:39
 * email: lc824767150@163.com
 */
public class JPushReceiver extends BroadcastReceiver {
    private static final String TAG = "JPushReceiver";
    private static int notifIndex;

    @Override
    public void onReceive(Context context, Intent intent) {
        Bundle bundle = intent.getExtras();

        if (JPushInterface.ACTION_REGISTRATION_ID.equals(intent.getAction())) {
            if (bundle != null) {
                String regId = bundle.getString(JPushInterface.EXTRA_REGISTRATION_ID);
                LogUtils.e( bundle.getString(JPushInterface.EXTRA_EXTRA),bundle.getString("content"));

                Log.d(TAG, "[MyReceiver] 接收Registration Id : " + regId);
//            JPushInterface.getRegistrationID(App.getInstance());
                DataSharedPreferences.setPushRegisterId(regId);

                EventBus.getDefault().post(new RegisterIdEntity());

                //send the Registration Id to your server...
            }
        } else if (JPushInterface.ACTION_MESSAGE_RECEIVED.equals(intent.getAction())) {
            if (bundle != null) {
                Log.d(TAG, "[MyReceiver] 接收到推送下来的自定义消息: " + bundle.getString(JPushInterface.EXTRA_MESSAGE));
                String token = DataSharedPreferences.getToken();
                EventBus.getDefault().post(new ReceivedPushEntity());
                if (!TextUtils.isEmpty(token)) {
                    processCustomMessage(context, bundle);
                }
            }
        } else if (JPushInterface.ACTION_NOTIFICATION_RECEIVED.equals(intent.getAction())) {
            if (bundle != null) {
                Log.d(TAG, "[MyReceiver] 接收到推送下来的通知");
                LogUtils.e( bundle.getString(JPushInterface.EXTRA_EXTRA),bundle.getString("content"),bundle.toString());
                int notifactionId = bundle.getInt(JPushInterface.EXTRA_NOTIFICATION_ID);
                Log.d(TAG, "[MyReceiver] 接收到推送下来的通知的ID: " + notifactionId);
            }

        } else if (JPushInterface.ACTION_NOTIFICATION_OPENED.equals(intent.getAction())) {
            Log.d(TAG, "[MyReceiver] 用户点击打开了通知");
            LogUtils.e( bundle.getString(JPushInterface.EXTRA_EXTRA),bundle.getString("content"));

//           LogUtils.e(
//                    bundle.getString(JPushInterface.ACTION_CONNECTION_CHANGE),
//                    bundle.getString(JPushInterface.ACTION_REGISTRATION_ID),
//                    bundle.getString(JPushInterface.ACTION_MESSAGE_RECEIVED),
//                    bundle.getString(JPushInterface.ACTION_NOTIFICATION_RECEIVED),
//                    bundle.getString(JPushInterface.ACTION_NOTIFICATION_OPENED),
//                    bundle.getString(JPushInterface.ACTION_NOTIFICATION_CLICK_ACTION),
//                    bundle.getString(JPushInterface.ACTION_NOTIFICATION_RECEIVED_PROXY),
//                    bundle.getString(JPushInterface.EXTRA_CONNECTION_CHANGE),
//                    bundle.getString(JPushInterface.EXTRA_REGISTRATION_ID),
//                    bundle.getString(JPushInterface.EXTRA_APP_KEY),
//                    bundle.getString(JPushInterface.EXTRA_NOTIFICATION_DEVELOPER_ARG0),
//                    bundle.getString(JPushInterface.EXTRA_NOTIFICATION_TITLE),
//                    bundle.getString(JPushInterface.EXTRA_PUSH_ID),
//                    bundle.getString(JPushInterface.EXTRA_MSG_ID),
//                    bundle.getString(JPushInterface.EXTRA_NOTI_TYPE),
//                    bundle.getString(JPushInterface.EXTRA_ALERT),
//                    bundle.getString(JPushInterface.EXTRA_ALERT_TYPE),
//                    bundle.getString(JPushInterface.EXTRA_MESSAGE),
//                    bundle.getString(JPushInterface.EXTRA_CONTENT_TYPE),
//                    bundle.getString(JPushInterface.EXTRA_TITLE),
//                    bundle.getString(JPushInterface.EXTRA_BIG_TEXT),
//                    bundle.getString(JPushInterface.EXTRA_INBOX),
//                    bundle.getString(JPushInterface.EXTRA_BIG_PIC_PATH),
//                    bundle.getString(JPushInterface.EXTRA_NOTI_PRIORITY),
//                    bundle.getString(JPushInterface.EXTRA_NOTI_CATEGORY),
//                    bundle.getString(JPushInterface.EXTRA_NOTIFICATION_ID),
//                    bundle.getString(JPushInterface.EXTRA_NOTIFICATION_ACTION_EXTRA),
//                    bundle.getString(JPushInterface.EXTRA_ACTIVITY_PARAM),
//                    bundle.getString(JPushInterface.EXTRA_RICHPUSH_FILE_PATH),
//                    bundle.getString(JPushInterface.EXTRA_RICHPUSH_FILE_TYPE),
//                    bundle.getString(JPushInterface.EXTRA_RICHPUSH_HTML_PATH),
//                    bundle.getString(JPushInterface.EXTRA_RICHPUSH_HTML_RES),
//                    bundle.getString(JPushInterface.EXTRA_STATUS),
//                    bundle.getString("content")
//                    );

            ActivityUtils.setActivity(ACTIVITY_TEXT_CONTENT,"conent",
                    "EXTRA_EXTRA="+bundle.getString(JPushInterface.EXTRA_EXTRA)
                            +"\ncontent="+bundle.getString("content")
                            +"\nACTION_CONNECTION_CHANGE=="+bundle.getString(JPushInterface.ACTION_CONNECTION_CHANGE)
                            +"\nACTION_REGISTRATION_ID=="+bundle.getString(JPushInterface.ACTION_REGISTRATION_ID)
                            +"\nACTION_MESSAGE_RECEIVED=="+bundle.getString(JPushInterface.ACTION_MESSAGE_RECEIVED)
                            +"\nACTION_NOTIFICATION_RECEIVED=="+bundle.getString(JPushInterface.ACTION_NOTIFICATION_RECEIVED)
                            +"\nACTION_NOTIFICATION_OPENED=="+bundle.getString(JPushInterface.ACTION_NOTIFICATION_OPENED)
                            +"\nACTION_NOTIFICATION_CLICK_ACTION=="+bundle.getString(JPushInterface.ACTION_NOTIFICATION_CLICK_ACTION)
                            +"\nACTION_NOTIFICATION_RECEIVED_PROXY=="+bundle.getString(JPushInterface.ACTION_NOTIFICATION_RECEIVED_PROXY)
                            +"\nEXTRA_CONNECTION_CHANGE=="+bundle.getString(JPushInterface.EXTRA_CONNECTION_CHANGE)
                            +"\nEXTRA_REGISTRATION_ID=="+bundle.getString(JPushInterface.EXTRA_REGISTRATION_ID)
                            +"\nEXTRA_APP_KEY=="+bundle.getString(JPushInterface.EXTRA_APP_KEY)
                            +"\nEXTRA_NOTIFICATION_DEVELOPER_ARG0=="+bundle.getString(JPushInterface.EXTRA_NOTIFICATION_DEVELOPER_ARG0)
                            +"\nEXTRA_PUSH_ID=="+bundle.getString(JPushInterface.EXTRA_PUSH_ID)
                            +"\nEXTRA_MSG_ID=="+bundle.getString(JPushInterface.EXTRA_MSG_ID)
                            +"\nEXTRA_NOTI_TYPE=="+bundle.getString(JPushInterface.EXTRA_NOTI_TYPE)
                            +"\nEXTRA_ALERT=="+bundle.getString(JPushInterface.EXTRA_ALERT)
                            +"\nEXTRA_ALERT_TYPE=="+bundle.getString(JPushInterface.EXTRA_ALERT_TYPE)
                            +"\nEXTRA_MESSAGE=="+bundle.getString(JPushInterface.EXTRA_MESSAGE)
                            +"\nEXTRA_CONTENT_TYPE=="+bundle.getString(JPushInterface.EXTRA_CONTENT_TYPE)
                            +"\nEXTRA_TITLE=="+bundle.getString(JPushInterface.EXTRA_TITLE)
                            +"\nEXTRA_BIG_TEXT=="+bundle.getString(JPushInterface.EXTRA_BIG_TEXT)
                            +"\nEXTRA_INBOX=="+bundle.getString(JPushInterface.EXTRA_INBOX)
                            +"\nEXTRA_BIG_PIC_PATH=="+bundle.getString(JPushInterface.EXTRA_BIG_PIC_PATH)
                            +"\nEXTRA_EXTRA=="+bundle.getString(JPushInterface.EXTRA_EXTRA)
                            +"\nEXTRA_NOTI_PRIORITY=="+bundle.getString(JPushInterface.EXTRA_NOTI_PRIORITY)
                            +"\nEXTRA_NOTI_CATEGORY=="+bundle.getString(JPushInterface.EXTRA_NOTI_CATEGORY)
                            +"\nEXTRA_NOTIFICATION_ID=="+bundle.getString(JPushInterface.EXTRA_NOTIFICATION_ID)
                            +"\nEXTRA_NOTIFICATION_ACTION_EXTRA=="+bundle.getString(JPushInterface.EXTRA_NOTIFICATION_ACTION_EXTRA)
                            +"\nEXTRA_ACTIVITY_PARAM=="+bundle.getString(JPushInterface.EXTRA_ACTIVITY_PARAM)
                            +"\nEXTRA_RICHPUSH_FILE_PATH=="+bundle.getString(JPushInterface.EXTRA_RICHPUSH_FILE_PATH)
                            +"\nEXTRA_RICHPUSH_FILE_TYPE=="+bundle.getString(JPushInterface.EXTRA_RICHPUSH_FILE_TYPE)
                            +"\nEXTRA_RICHPUSH_HTML_PATH=="+bundle.getString(JPushInterface.EXTRA_RICHPUSH_HTML_PATH)
                            +"\nEXTRA_RICHPUSH_HTML_RES=="+bundle.getString(JPushInterface.EXTRA_RICHPUSH_HTML_RES)
                            +"\nEXTRA_RICHPUSH_HTML_RES=="+bundle.getString(JPushInterface.EXTRA_RICHPUSH_HTML_RES)
                            +"\ntoString=="+bundle.toString()
                        );
//            openNotification(context, bundle);
        } else if (JPushInterface.ACTION_RICHPUSH_CALLBACK.equals(intent.getAction())) {
            if (bundle != null) {
                Log.d(TAG, "[MyReceiver] 用户收到到RICH PUSH CALLBACK: " + bundle.getString(JPushInterface.EXTRA_EXTRA));
                //在这里根据 JPushInterface.EXTRA_EXTRA 的内容处理代码，比如打开新的Activity， 打开一个网页等..
            }
        } else if (JPushInterface.ACTION_CONNECTION_CHANGE.equals(intent.getAction())) {
            boolean connected = intent.getBooleanExtra(JPushInterface.EXTRA_CONNECTION_CHANGE, false);
            Log.w(TAG, "[MyReceiver]" + intent.getAction() + " connected state change to " + connected);
        } else {
            Log.d(TAG, "[MyReceiver] Unhandled intent - " + intent.getAction());
        }
    }

    private void openNotification(Context context, Bundle bundle) {
        try {
            PushMessageEntity messageEntity = new Gson().fromJson(bundle.getString("content"), PushMessageEntity.class);
            if (messageEntity == null) {
                return;
            }
            Intent intent;
            Integer taskId = isExsitMianActivity(context);
            if (taskId != null) {
                intent = new Intent(context, MessageCenterActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.putExtra(CommonUtil.KEY_VALUE_1, messageEntity);
                context.startActivity(intent);
            } else {
                intent = new Intent(context, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.putExtra("pushEntity", messageEntity.getType());
                context.startActivity(intent);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void foregroundTask(Context context, int taskId) {
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        if (am != null)
            am.moveTaskToFront(taskId, ActivityManager.MOVE_TASK_WITH_HOME);
    }

    private Integer isExsitMianActivity(Context context) {
        Integer flag = -1;
        if (context != null) {
            Intent intent = new Intent(context, MainActivity.class);
            ComponentName cmpName = intent.resolveActivity(context.getPackageManager());
            if (cmpName != null) { // 说明系统中存在这个activity
                ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
                if (am != null) {
                    List<ActivityManager.RunningTaskInfo> taskInfoList = am.getRunningTasks(100);
                    for (ActivityManager.RunningTaskInfo taskInfo : taskInfoList) {
                        if (taskInfo.baseActivity.equals(cmpName)) { // 说明它已经启动了
                            flag = taskInfo.id;
                            break;
                        }
                    }
                }
            }
        }
        return flag;
    }

    private void processCustomMessage(Context context, Bundle bundle) {
        if (bundle == null) {
            return;
        }
        String value = bundle.getString(JPushInterface.EXTRA_MESSAGE);
        try {
            PushMessageEntity messageEntity = new Gson().fromJson(value, PushMessageEntity.class);
//            if (messageEntity.getType().equals("3")) {
//                if (ChatActivity.instance != null &&
//                        ChatActivity.instance.otherId.equals(messageEntity.getNotice_id()) && ChatActivity.instance.isVisable) {
//                    ChatActivity.instance.loadData(1);
//                } else {
//                    showMoreNotification(context, messageEntity);
//                    if (HomeFragment.instance != null) {
//                        HomeFragment.instance.getUnreadMessageCount();
//                    }
//                    if(NormalMessageListActivity.instance!=null){
//                        NormalMessageListActivity.instance.onRefresh();
//                    }
//                }
//            } else {
            showMoreNotification(context, messageEntity);
//            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void showMoreNotification(Context context, PushMessageEntity message) {
        Intent notificationIntent = new Intent();
        notificationIntent.setClass(context, JPushReceiver.class);
        notificationIntent.setAction(JPushInterface.ACTION_NOTIFICATION_OPENED);
        notificationIntent.putExtra("content", new Gson().toJson(message));

        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, getNextNoticeNum(), notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        String channelID = AFUtil.getAppProcessName(context) + ":jpush";
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            String channelName = ":jpush";
            NotificationChannel channel = new NotificationChannel(channelID, channelName, NotificationManager.IMPORTANCE_HIGH);
            NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            if (manager != null) {
                manager.createNotificationChannel(channel);
            }
        }

        Notification notification = new NotificationCompat.Builder(context, channelID)
                .setAutoCancel(true)
                .setContentTitle(message.getTitle())
                .setContentText(message.getContent())
                .setContentIntent(pendingIntent)
                .setDefaults(Notification.DEFAULT_ALL)
                .setLargeIcon(BitmapFactory.decodeResource(context.getResources(), R.mipmap.ic_launcher))
                .setSmallIcon(R.mipmap.ic_launcher)
                .setStyle(new NotificationCompat.BigTextStyle().bigText(message.getContent()))
                .build();
        NotificationManager manager = (NotificationManager) context
                .getSystemService(Context.NOTIFICATION_SERVICE);
        if (manager != null) {
            manager.notify((int) System.currentTimeMillis(), notification);
        }
    }

    public static int getNextNoticeNum() {
        return ++notifIndex;
    }
}

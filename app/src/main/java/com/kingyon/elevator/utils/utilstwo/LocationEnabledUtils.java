package com.kingyon.elevator.utils.utilstwo;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.provider.Settings;
import android.text.TextUtils;

/**
 * @Created By Admin  on 2020/7/20
 * @Email : 163235610@qq.com
 * @Author:Mrczh
 * @Instructions:
 */
public class LocationEnabledUtils {

    /**
     * 判断定位服务是否开启
     *
     * @param
     * @return true 表示开启
     */
    public static boolean isLocationEnabled(Context context) {
        int locationMode = 0;
        String locationProviders;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            try {
                locationMode = Settings.Secure.getInt(context.getContentResolver(), Settings.Secure.LOCATION_MODE);
            } catch (Settings.SettingNotFoundException e) {
                e.printStackTrace();
                return false;
            }
            return locationMode != Settings.Secure.LOCATION_MODE_OFF;
        } else {
            locationProviders = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.LOCATION_PROVIDERS_ALLOWED);
            return !TextUtils.isEmpty(locationProviders);
        }
    }

    /**
     * 提示用户去开启定位服务
     **/
    public static void toOpenGPS(Activity activity) {
        new AlertDialog.Builder(activity)
                .setTitle("提示")
                .setMessage("手机定位服务未开启，无法获取到您的准确位置信息，是否前往开启？")
                .setNegativeButton("取消",null)
                .setPositiveButton("去开启", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                        activity.startActivity(intent);
                        dialogInterface.dismiss();
                    }
                })
                .show();
    }
}

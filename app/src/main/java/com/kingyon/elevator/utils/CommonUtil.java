package com.kingyon.elevator.utils;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.text.TextUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by gongli on 2017/7/17 17:39
 * email: lc824767150@163.com
 */
public class CommonUtil {
    public static final int RC_READ_PHONE_STATE = 3001;
    public static final int RC_SETTING = 3000;
    public static final String KEY_LENTH = "lenth";
    public static final String KEY_VALUE_1 = "value_1";
    public static final String KEY_VALUE_2 = "value_2";
    public static final String KEY_VALUE_3 = "value_3";
    public static final String KEY_VALUE_4 = "value_4";
    public static final String KEY_VALUE_5 = "value_5";
    public static final String KEY_VALUE_6 = "value_6";
    public static final String KEY_VALUE_7 = "value_7";
    public static final String KEY_VALUE_8 = "value_8";
    public static final int REQ_CODE_1 = 4001;
    public static final int REQ_CODE_2 = 4002;
    public static final int REQ_CODE_3 = 4003;
    public static final int REQ_CODE_4 = 4004;
    public static final int REQ_CODE_5 = 4005;
    public static final int REQ_CODE_6 = 4006;
    public static final int REQ_CODE_7 = 4007;

    public static final int DEFULT_AREA_ID = 0;

    public static String getEditText(EditText editText) {
        return editText.getText().toString().trim();
    }

    public static String getEditText(TextView editText) {
        return editText.getText().toString().trim();
    }

    public static boolean editTextIsEmpty(EditText editText) {
        return TextUtils.isEmpty(editText.getText().toString().trim());
    }

    public static String getOneDigits(double num) {
        DecimalFormat df = new DecimalFormat("#0");
        return df.format(num);
    }

    public static String getTwoDigits(double num) {
        DecimalFormat df = new DecimalFormat("#00");
        return df.format(num);
    }

    public static String getShareDigits(double num) {
        DecimalFormat df = new DecimalFormat("#0000000000000");
        return df.format(num);
    }


    public static CharSequence getDeviceId(long deviceId) {
        DecimalFormat df = new DecimalFormat("#0000000");
        return df.format(deviceId);
    }

    public static String getOneFloat(double num) {
        DecimalFormat df = new DecimalFormat("#0.0");
        return df.format(num);
    }

    public static String getMayOneFloat(double num) {
        DecimalFormat df = new DecimalFormat("#0.#");
        return df.format(num);
    }

    public static String getMayTwoFloat(double num) {
        DecimalFormat df = new DecimalFormat("#0.##");
        return df.format(num);
    }

    public static String getTwoFloat(double num) {
        DecimalFormat df = new DecimalFormat("#0.00");
        return df.format(num);
    }

    public static String getMoneyFormat(double num) {
        DecimalFormat df = new DecimalFormat("#,##0.00");
        return df.format(num);
    }

    /**
     * 获取版本号
     */
    public static String getVersion(Context context) {
        try {
            PackageManager manager = context.getPackageManager();
            PackageInfo info = manager.getPackageInfo(context.getPackageName(), 0);
            String version = info.versionName;
            return "" + version;
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }


    public static long calculatePercent(double x, double total) {
        return Math.round(x / total * 100);
    }

    public static String getHideMobile(String phone) {
        String result;
        if (phone != null && phone.length() > 6) {
            result = phone.substring(0, 3) + "****" + phone.substring(phone.length() - 4, phone.length());
        } else {
            result = phone != null ? phone : "";
        }
        return result;
    }

    public static String getBankCardNum(String phone) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < phone.length(); i++) {
            if (i < 4 && i > phone.length() - 4) {
                sb.append(phone.charAt(i));
            } else {
                sb.append("*" + (((i + 1) % 4 == 0) ? " " : ""));
            }
        }
        return sb.toString();
    }

    public static String sexChar2Str(String sex) {
        if (TextUtils.isEmpty(sex)) {
            return "";
        }
        String result;
        switch (sex) {
            case "男":
            case "M":
                result = "男";
                break;
            case "女":
            case "F":
                result = "女";
                break;
            case "保密":
            case "o":
            case "O":
                result = "保密";
                break;
            default:
                result = "";
                break;
        }
        return result;
    }

    public static String sexStr2Char(String sex) {
        if (TextUtils.isEmpty(sex)) {
            return "";
        }
        String result;
        switch (sex) {
            case "男":
            case "M":
                result = "M";
                break;
            case "女":
            case "F":
                result = "F";
                break;
            case "保密":
            case "o":
            case "O":
                result = "O";
                break;
            default:
                result = "";
                break;
        }
        return result;
    }

    public static int sexPositon(String sex) {
        if (TextUtils.isEmpty(sex)) {
            return 0;
        }
        int result;
        switch (sex) {
            case "女":
            case "F":
                result = 1;
                break;
            case "保密":
            case "o":
            case "O":
                result = 2;
                break;
            default:
                result = 0;
                break;
        }
        return result;
    }

    public static boolean isMan(String sex) {
        if (TextUtils.isEmpty(sex)) {
            return false;
        }
        boolean result;
        switch (sex) {
            case "男":
            case "M":
                result = true;
                break;
            default:
                result = false;
                break;
        }
        return result;
    }

    public static boolean isWoman(String sex) {
        if (TextUtils.isEmpty(sex)) {
            return false;
        }
        boolean result;
        switch (sex) {
            case "女":
            case "F":
                result = true;
                break;
            default:
                result = false;
                break;
        }
        return result;
    }

    public static final String[] constellationArr = {"水瓶座", "双鱼座", "白羊座", "金牛座", "双子座", "巨蟹座", "狮子座", "处女座", "天秤座", "天蝎座", "射手座", "魔羯座"};
    public static final int[] constellationEdgeDay = {20, 19, 21, 21, 21, 22, 23, 23, 23, 23, 22, 22};

    public static String getConstellation(Date date) {

        if (date == null) {

            return "";
        }
        Calendar cal = Calendar.getInstance();

        cal.setTime(date);

        int month = cal.get(Calendar.MONTH);

        int day = cal.get(Calendar.DAY_OF_MONTH);

        if (day < constellationEdgeDay[month]) {

            month = month - 1;
        }

        if (month >= 0) {

            return constellationArr[month];
        }
        // default to return 魔羯
        return constellationArr[11];
    }

    public static void hideAndShowInputMethod(Context context) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
    }
}

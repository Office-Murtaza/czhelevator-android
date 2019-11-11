package com.kingyon.elevator.utils;

/**
 * Created by GongLi on 2017/10/30.
 * Email：lc824767150@163.com
 */

public class NumberFormatUtils {
    private final String[] units = {"", "十", "百", "千", "万", "十万", "百万", "千万", "亿",
            "十亿", "百亿", "千亿", "万亿"};
    private final char[] numArray = {'零', '一', '二', '三', '四', '五', '六', '七', '八', '九'};

    private static NumberFormatUtils numberFormatUtils;

    private NumberFormatUtils() {

    }

    public static NumberFormatUtils getInstance() {
        if (numberFormatUtils == null) {
            numberFormatUtils = new NumberFormatUtils();
        }
        return numberFormatUtils;
    }

    public String formatInteger(int num) {
        if (num < 0) {
            num = 0;
        }
        if (num == 0) {
            return "零";
        }
        char[] val = String.valueOf(num).toCharArray();
        int len = val.length;
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < len; i++) {
            String m = val[i] + "";
            int n = Integer.valueOf(m);
            boolean isZero = n == 0;
            String unit = units[(len - 1) - i];
            if (isZero) {
                if ('0' == val[i]) {
                    continue;
                } else {
                    sb.append(numArray[n]);
                }
            } else {
                sb.append(numArray[n]);
                sb.append(unit);
            }
        }
        return sb.toString();
    }


    public String formatDecimal(double decimal) {
        String decimals = String.valueOf(decimal);
        int decIndex = decimals.indexOf(".");
        int integ = Integer.valueOf(decimals.substring(0, decIndex));
        int dec = Integer.valueOf(decimals.substring(decIndex + 1));
        String result = formatInteger(integ) + "." + formatFractionalPart(dec);
        return result;
    }


    public String formatFractionalPart(int decimal) {
        char[] val = String.valueOf(decimal).toCharArray();
        int len = val.length;
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < len; i++) {
            int n = Integer.valueOf(val[i] + "");
            sb.append(numArray[n]);
        }
        return sb.toString();
    }
}

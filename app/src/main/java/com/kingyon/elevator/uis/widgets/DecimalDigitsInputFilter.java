package com.kingyon.elevator.uis.widgets;

import android.content.Context;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextUtils;

import com.leo.afbaselibrary.utils.ToastUtils;

/**
 * Created by GongLi on 2018/4/18.
 * Email：lc824767150@163.com
 */

public class DecimalDigitsInputFilter implements InputFilter {
    private int mMaxLength;
    private boolean isNum;
    private String note;
    private String message = null;
    Context context;
    /**
     *
     * @param max 支持的最大长度
     * @param isNum 是不是数字
     * @param note 超过后的提示
     */
    public DecimalDigitsInputFilter(Context context,int max, boolean isNum, String note) {
        mMaxLength = max;
        this.isNum = isNum;
        this.note = note;
        this.context = context;
    }

    public DecimalDigitsInputFilter setMessage(String message) {
        this.message = message;
        return this;
    }

    public CharSequence filter(CharSequence source, int start, int end,
                               Spanned dest, int dstart, int dend) {
        int keep = mMaxLength - (dest.length() - (dend - dstart));
        if (keep < (end - start)) {
            if (TextUtils.isEmpty(message))
                ToastUtils.showToast(context,String.format("%s最多%s位", note, mMaxLength),1000);
            else {
                ToastUtils.showToast(context,message,1000);
            }
        }
        if (keep <= 0) {
            return "";
        } else if (keep >= end - start) {
            if (isNum) {
                int posDot = dest.toString().indexOf(".");
                if (start < end && posDot > 0 && (dest.length() - posDot) > 2) {//小数点后要保留两位小数
                    ToastUtils.showToast(context,"小数点后只能输两位",1000);
                    return "";
                }
                if (start < end && posDot > 1 && dest.toString().equals("0.") && source.equals("0")) {//确保不会出现不是小数的第一位为0
                    ToastUtils.showToast(context,"请输入正确格式的金额",1000);
                    return "";
                } else if (start < end && posDot < 0 && dest.toString().equals("0") && !source.equals(".")) {
                    ToastUtils.showToast(context,"请输入正确格式的金额",1000);
                    return "";
                }
            } else
                return null;
        } else
            return source.subSequence(start, start + keep);
        return null;
    }
}

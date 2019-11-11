package com.kingyon.elevator.utils;

import android.content.Context;
import android.view.View;

import com.bigkoo.pickerview.OptionsPickerView;
import com.kingyon.elevator.nets.CustomApiCallback;
import com.leo.afbaselibrary.nets.exceptions.ApiException;
import com.leo.afbaselibrary.utils.ToastUtils;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by GongLi on 2017/8/28.
 * Email：lc824767150@163.com
 */

public class ThreeLinkageUtil<T extends ThreeLinkageUtil.Linkage<T>> {

    private Context mContext;

    private List<T> options1Items = new ArrayList<>();
    private List<List<T>> options2Items = new ArrayList<>();
    private List<List<List<T>>> options3Items = new ArrayList<>();

    private OptionsPickerView<T> pvOptions;

    private boolean isInitOK = false;
    private boolean isInitTimeClicked = false;

    private OnCompanyCategorySelectListener<T> onCompanyCategorySelectListener;

    public ThreeLinkageUtil(Context mContext, List<T> roots, OnCompanyCategorySelectListener<T> onCompanyCategorySelectListener) {
        this.mContext = mContext;
        this.onCompanyCategorySelectListener = onCompanyCategorySelectListener;
        options1Items = roots;
        initProvince();
    }

    private void initProvince() {
        Observable.just(options1Items).map(new Func1<List<T>, Object>() {
            @Override
            public Object call(List<T> linkages) {
                for (T item1 : linkages) {
                    List<List<T>> options3Items_01 = new ArrayList<>();
                    List<T> second = item1.getChildren();
                    if (second != null && second.size() > 0) {
                        for (T item2 : second) {
                            List<T> third = item2.getChildren();
                            if (third != null && third.size() > 0) {
                                options3Items_01.add(third);
                            }
                        }
                        options2Items.add(second);
                        options3Items.add(options3Items_01);
                    }
                }
                return null;
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread()).subscribe(new CustomApiCallback<Object>() {

            @Override
            public void onNext(Object o) {
                isInitOK = true;
                initPicker();
            }

            @Override
            protected void onResultError(ApiException ex) {
                if (mContext != null) {
                    ToastUtils.toast(mContext, "初始化错误");
                }
            }
        });

    }

    private void initPicker() {
        if (pvOptions == null) {
            OptionsPickerView.Builder builder = new OptionsPickerView.Builder(mContext, new OptionsPickerView.OnOptionsSelectListener() {
                @Override
                public void onOptionsSelect(int op1, int op2, int op3, View v) {
                    if (onCompanyCategorySelectListener != null) {
                        onCompanyCategorySelectListener.onCompanyCategorySelect(getFirstCategory(op1), getSecondCategory(op1, op2), getThirdCategory(op1, op2, op3), op1, op2, op3, v);
                    }
                }
            });
            builder.setTitleText("");
            builder.setCyclic(false, false, false);
            pvOptions = new OptionsPickerView<>(builder);
            try {
                pvOptions.setPicker(options1Items, options2Items, options3Items);
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (isInitTimeClicked) {
                pvOptions.show();
                ToastUtils.hide();
            }
        }
    }

    private T getFirstCategory(int op1) {
        T result;
        if (options1Items != null && options1Items.size() > op1) {
            result = options1Items.get(op1);
        } else {
            result = null;
        }
        return result;
    }

    private T getSecondCategory(int op1, int op2) {
        T result;
        if (options2Items != null && options2Items.size() > op1
                && options2Items.get(op1) != null && options2Items.get(op1).size() > op2) {
            result = options2Items.get(op1).get(op2);
        } else {
            result = null;
        }
        return result;
    }

    private T getThirdCategory(int op1, int op2, int op3) {
        T result;
        if (options3Items != null && options3Items.size() > op1
                && options3Items.get(op1) != null && options3Items.get(op1).size() > op2
                && options3Items.get(op1).get(op2) != null && options3Items.get(op1).get(op2).size() > op3) {
            result = options3Items.get(op1).get(op2).get(op3);
        } else {
            result = null;
        }
        return result;
    }

    public void showPicker() {
        if (pvOptions != null) {
            pvOptions.show();
            ToastUtils.hide();
        } else {
            if (!isInitOK) {
                isInitTimeClicked = true;
                if (mContext != null) {
                    ToastUtils.toast(mContext, "正在初始化...");
                }
            }
        }
    }

    public void dismiss() {
        if (pvOptions != null) {
            pvOptions.dismiss();
        }
    }

    public void onDestroy() {
        if (pvOptions != null && pvOptions.isShowing()) {
            pvOptions.dismiss();
        }
        pvOptions = null;
        mContext = null;
    }

    public interface OnCompanyCategorySelectListener<K> {
        void onCompanyCategorySelect(K first, K second, K third, int op1, int op2, int op3, View v);
    }

    public static abstract class Linkage<J> {
        public abstract List<J> getChildren();

        public abstract String toString();
    }
}

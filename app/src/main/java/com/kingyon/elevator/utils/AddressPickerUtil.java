package com.kingyon.elevator.utils;

import android.content.Context;
import android.view.View;

import com.bigkoo.pickerview.OptionsPickerView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.kingyon.elevator.entities.AMapCityEntity;
import com.kingyon.elevator.nets.CustomApiCallback;
import com.leo.afbaselibrary.nets.exceptions.ApiException;
import com.leo.afbaselibrary.utils.ToastUtils;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * created by arvin on 16/12/27 10:17
 * email：1035407623@qq.com
 */
public class AddressPickerUtil {
    private Context mContext;

    private List<AMapCityEntity> options1Items = new ArrayList<>();
    private List<List<AMapCityEntity>> options2Items = new ArrayList<>();
    private List<List<List<AMapCityEntity>>> options3Items = new ArrayList<>();

    private OptionsPickerView<AMapCityEntity> pvOptions;

    private int option1 = 0, option2 = 0, option3 = 0;

    private int defaultPos1, defaultPos2, defaultPos3;

    private boolean first = true, second = true, third = true;

    //    private CityEntity receAddress;
    private boolean isInitOK = false;
    private boolean isInitTimeClicked = false;

    private OnAreaSelectedListener onAreaSelectedListener;
    private boolean isInitListener;

    private String fileName;

    public AddressPickerUtil(Context mContext) {
        this.mContext = mContext;
        resetLocation();
    }

    public AddressPickerUtil(Context mContext, boolean first, boolean second, boolean third, String fileName) {
        this.mContext = mContext;
        this.first = first;
        this.second = second;
        this.third = third;
        this.fileName = fileName;
        resetLocation();
    }

    private void resetLocation() {
        initProvince();
    }

    public void setSelectListener(OnAreaSelectedListener onAreaSelectedListener) {
        this.onAreaSelectedListener = onAreaSelectedListener;
    }

    private void initProvince() {
        Observable.just("").map(new Func1<String, String>() {
            @Override
            public String call(String s) {
                String result = "";
                InputStream in = null;
                try {
                    in = mContext.getResources().getAssets().open(fileName); // 从Assets中的文件获取输入流
                    int length = in.available(); // 获取文件的字节数
                    byte[] buffer = new byte[length]; // 创建byte数组
                    in.read(buffer); // 将文件中的数据读取到byte数组中
                    result = new String(buffer, "utf-8");
                } catch (IOException e) {
                    e.printStackTrace(); // 捕获异常并打印
                } finally {
                    try {
                        if (in != null) {
                            in.close();
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                return result;
            }
        }).map(new Func1<String, List<AMapCityEntity>>() {
            @Override
            public List<AMapCityEntity> call(String s) {
                return new Gson().fromJson(s, new TypeToken<List<AMapCityEntity>>() {
                }.getType());
            }
        }).map(new Func1<List<AMapCityEntity>, String>() {
            @Override
            public String call(List<AMapCityEntity> cityEntities) {
                for (int i = 0; i < cityEntities.size(); i++) {
                    AMapCityEntity bean = cityEntities.get(i);
//                    if (receAddress != null) {
//                        if (bean.getCode() == receAddress.getCode()) {
//                            option1 = i;
//                        }
//                    }
                    if (bean.getDistricts() == null || bean.getDistricts().size() < 1) {
                        continue;
                    }
                    options1Items.add(bean);

                    ArrayList<AMapCityEntity> options2Items_01 = new ArrayList<>();
                    List<List<AMapCityEntity>> options3Items_01 = new ArrayList<>();


                    for (int j = 0; j < bean.getDistricts().size(); j++) {
                        AMapCityEntity temp1 = bean.getDistricts().get(j);
//                            if (receAddress != null) {
//                                if (temp1.getCode() == receAddress.getCode()) {
//                                    option2 = j;
//                                    temp1.getChildren();
//                                    for (int k = 0; k < temp1.getChildren().size(); k++) {
//                                        if (temp1.getChildren().get(k).getId() == receAddress.getDistrictId()) {
//                                            option3 = k;
//                                        }
//                                    }
//                                }
//                            }
                        options2Items_01.add(temp1);
//                            第三级  区
                        ArrayList<AMapCityEntity> list2 = new ArrayList<>();
                        if (temp1.getDistricts() != null) {
                            list2.addAll(temp1.getDistricts());
                        }
                        if (list2.size() < 1) {
                            list2.add(new AMapCityEntity(""));
                        }
                        options3Items_01.add(list2);
                    }
                    options2Items.add(options2Items_01);
                    options3Items.add(options3Items_01);
                }
                return "suc";
            }
        })
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new CustomApiCallback<String>() {

                    @Override
                    public void onNext(String s) {
                        if (pvOptions == null) {
                            defaultPos1 = option1;
                            defaultPos2 = option2;
                            defaultPos3 = option3;
                        }
                        isInitOK = true;
                        initPicker();
                    }

                    @Override
                    protected void onResultError(ApiException ex) {
                        ToastUtils.toast(mContext, "初始化错误");
                    }
                });

    }

    private void initPicker() {
        if (pvOptions == null) {
            OptionsPickerView.Builder builder = new OptionsPickerView.Builder(mContext, new OptionsPickerView.OnOptionsSelectListener() {
                @Override
                public void onOptionsSelect(int op1, int op2, int op3, View v) {
                    option1 = op1;
                    option2 = op2;
                    option3 = op3;

                    if (onAreaSelectedListener != null) {
                        onAreaSelectedListener.onAreaSelect(getFirstCategory(op1), getSecondCategory(op1, op2), getThirdCategory(op1, op2, op3), op1, op2, op3, v);
                    }
                }
            })
                    .setTitleText("")
                    .setCyclic(false, false, false);
            pvOptions = new OptionsPickerView<>(builder);
            options1Items = first ? options1Items : null;
            options2Items = second ? options2Items : null;
            options3Items = third ? options3Items : null;
            pvOptions.setPicker(options1Items, options2Items, options3Items);
        }
        //设置默认选中的三级项目
        //监听确定选择按钮
        pvOptions.setSelectOptions(defaultPos1, defaultPos2, defaultPos3);
        if (isInitTimeClicked) {
            pvOptions.show();
        }
        if (isInitListener && onAreaSelectedListener != null) {
            onAreaSelectedListener.onAreaSelect(getFirstCategory(defaultPos1)
                    , getSecondCategory(defaultPos1, defaultPos2)
                    , getThirdCategory(defaultPos1, defaultPos2, defaultPos3)
                    , defaultPos1, defaultPos2, defaultPos3, null);
        }
    }

//    public AddressEntity getReceAddress() {
//        return receAddress;
//    }

    public void showPicker() {
        if (pvOptions != null) {
            pvOptions.show();
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

    public interface OnAreaSelectedListener {
        void onAreaSelect(AMapCityEntity first, AMapCityEntity second, AMapCityEntity third, int op1, int op2, int op3, View v);
    }

    private AMapCityEntity getFirstCategory(int op1) {
        AMapCityEntity result;
        if (options1Items != null && options1Items.size() > op1) {
            result = options1Items.get(op1);
        } else {
            result = null;
        }
        return result;
    }

    private AMapCityEntity getSecondCategory(int op1, int op2) {
        AMapCityEntity result;
        if (options2Items != null && options2Items.size() > op1
                && options2Items.get(op1) != null && options2Items.get(op1).size() > op2) {
            result = options2Items.get(op1).get(op2);
        } else {
            result = null;
        }
        return result;
    }

    private AMapCityEntity getThirdCategory(int op1, int op2, int op3) {
        AMapCityEntity result;
        if (options3Items != null && options3Items.size() > op1
                && options3Items.get(op1) != null && options3Items.get(op1).size() > op2
                && options3Items.get(op1).get(op2) != null && options3Items.get(op1).get(op2).size() > op3) {
            result = options3Items.get(op1).get(op2).get(op3);
        } else {
            result = null;
        }
        return result;
    }
}

package com.kingyon.elevator.utils;

import android.content.Context;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Leo on 2016/2/26
 */
public class LocationUtils implements AMapLocationListener {

    private static LocationUtils locationUtils;
    private static Map<Object, AMapLocationListener> registers;

    public static LocationUtils getInstance(Context context) {
        if (locationUtils == null) {
            registers = new HashMap<>();
            locationUtils = new LocationUtils(context);
        }
        return locationUtils;
    }

    //声明AMapLocationClient类对象
    public AMapLocationClient mLocationClient = null;
    //声明定位回调监听器
    public AMapLocationClientOption mLocationOption = null;

    private AMapLocation aMapLocation;

    private LocationUtils(Context context) {
        mLocationClient = new AMapLocationClient(context);
        mLocationClient.setLocationListener(this);
        mLocationOption = new AMapLocationClientOption();
        //设置定位模式为高精度模式，Battery_Saving为低功耗模式，Device_Sensors是仅设备模式
        mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
        //设置是否返回地址信息（默认返回地址信息）
        mLocationOption.setNeedAddress(true);
        mLocationOption.setOnceLocation(false);
        //设置是否强制刷新WIFI，默认为强制刷新
        mLocationOption.setWifiActiveScan(true);
        //设置是否允许模拟位置,默认为false，不允许模拟位置
        mLocationOption.setMockEnable(false);
        //设置定位间隔,单位毫秒,默认为2000ms
//        mLocationOption.setInterval(2000);
        mLocationClient.setLocationOption(mLocationOption);
    }

    public void startLocation() {
        mLocationClient.startLocation();
    }

    public void stopLocation() {
        mLocationClient.stopLocation();
    }

    public void onFinish() {
        mLocationClient.onDestroy();
        locationUtils = null;
    }

    @Override
    public void onLocationChanged(AMapLocation mapLocation) {
        this.aMapLocation = mapLocation;
        if (registers.size() > 0) {
            for (Object key : registers.keySet()) {
                registers.get(key).onLocationChanged(mapLocation);
            }
        }
    }

    public AMapLocation getMapLocation() {
        return aMapLocation;
    }

    public void setMapLocation(AMapLocation aMapLocation) {
        this.aMapLocation = aMapLocation;
    }

    public void register(Object object, AMapLocationListener changeListener) {
        registers.put(object, changeListener);
    }

    public void unregister(Object object) {
        if (registers.containsKey(object)) {
            registers.remove(object);
        }
    }

}

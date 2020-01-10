package com.kingyon.elevator.nets;

import android.content.Context;
import android.text.TextUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.kingyon.elevator.application.App;
import com.kingyon.elevator.data.DataSharedPreferences;
import com.leo.afbaselibrary.nets.BaseNet;
import com.leo.afbaselibrary.nets.HttpsCerUtils;
import com.orhanobut.logger.Logger;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Comparator;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import okhttp3.FormBody;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

/**
 * Created by gongli on 2017/7/17 17:39
 * email: lc824767150@163.com
 */
public class Net extends BaseNet<NetApi> {
    private static Net mNet;

//    @Override
//    protected Class<NetApi> getApiClazz() {
//        return NetApi.class;
//    }


    @Override
    protected String getCustomUrl() {
        return NetApi.rapUrl;
    }

    @Override
    protected String getBaseUrl() {
        return NetApi.baseUrl;
    }

    @Override
    protected Context getContext() {
        return App.getInstance().getApplicationContext();
    }

    @Override
    protected Request dealRequest(Request request) {
        String timestamp = System.currentTimeMillis() + "";
        HttpUrl url = request.url().newBuilder()
                .build();
        HttpUrl.Builder urlBuild = url.newBuilder();
        Map<String, Object> sortedMap = new TreeMap<>(new Comparator<String>() {
            @Override
            public int compare(String o1, String o2) {
                return o1.compareTo(o2);
            }
        });
        if (TextUtils.equals("POST", request.method()) && request.body() instanceof FormBody) {
            FormBody formBody = (FormBody) request.body();
            for (int i = 0; i < formBody.size(); i++) {
                String name = formBody.encodedName(i);
                String value;
                try {
                    value = URLDecoder.decode(formBody.encodedValue(i), "UTF-8");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                    value = URLDecoder.decode(formBody.encodedValue(i));
                }
                sortedMap.put(name, value);
            }
        } else {
            Set<String> names = url.queryParameterNames();
            for (String name : names) {
                sortedMap.put(name, url.queryParameter(name));
            }
        }
        sortedMap.put("timestamp", timestamp);
        urlBuild.addQueryParameter("timestamp", timestamp);
        String s = JSON.toJSONString(sortedMap);
        Logger.i("加密src--->" + s);
        String md5Query = EncryptUtil.getInstance().md5Digest(JSONObject.toJSONString(sortedMap));
        urlBuild.addQueryParameter("encryption", md5Query);
        return request.newBuilder()
                .url(urlBuild.build())
                .build();


//        HttpUrl url = request.url().newBuilder()
//                .addQueryParameter("timestamp", System.currentTimeMillis() + "")
//                .build();
//        HttpUrl.Builder urlBuild = url.newBuilder();
//        Map<String, Object> sortedMap = new TreeMap<>(new Comparator<String>() {
//            @Override
//            public int compare(String o1, String o2) {
//                return o1.compareTo(o2);
//            }
//        });
//        Set<String> names = url.queryParameterNames();
//        for (String name : names) {
//            sortedMap.put(name, url.queryParameter(name));
//        }
//
//
//        String s = JSON.toJSONString(sortedMap);
//        Logger.i("加密src--->"+s);
//        String md5Query = EncryptUtil.getInstance().md5Digest(JSONObject.toJSONString(sortedMap));
//        urlBuild.addQueryParameter("encryption", md5Query);
//        return request.newBuilder()
//                .url(urlBuild.build())
//                .build();
    }

    private Net() {
        super();
    }

    public static Net getInstance() {
        if (mNet == null) {
            mNet = new Net();
        }
        return mNet;
    }


    @Override
    protected boolean isNeedHttps() {
        return false;
    }

    @Override
    public String getToken() {
        return DataSharedPreferences.getToken();
    }
}

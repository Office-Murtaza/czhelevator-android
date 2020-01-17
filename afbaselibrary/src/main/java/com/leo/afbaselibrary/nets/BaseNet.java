package com.leo.afbaselibrary.nets;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.bumptech.glide.Glide;
import com.bumptech.glide.integration.okhttp3.OkHttpUrlLoader;
import com.bumptech.glide.load.model.GlideUrl;
import com.leo.afbaselibrary.nets.converters.GsonConverterFactory;
import com.leo.afbaselibrary.nets.exceptions.NoNetworkException;
import com.leo.afbaselibrary.utils.CertificateUtil;
import com.leo.afbaselibrary.utils.HTTPSCerUtils;
import com.leo.afbaselibrary.utils.HttpUtils;
import com.orhanobut.logger.BuildConfig;
import com.orhanobut.logger.Logger;


import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.ParameterizedType;
import java.security.GeneralSecurityException;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;

import okhttp3.FormBody;
import okhttp3.Interceptor;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.CallAdapter;
import retrofit2.Converter;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;

/**
 * created by arvin on 16/11/21 23:06
 * email：1035407623@qq.com
 */
public abstract class BaseNet<T> {
    private T api;
    private T customApi;
    private Class<T> clazz;
    private OkHttpClient okHttpClient;
    protected String token;
    private String deviceId;

    private Converter.Factory converterFactory;
    private CallAdapter.Factory rxJavaCallAdapterFactory;

    @SuppressWarnings("unchecked")
    protected BaseNet() {
        converterFactory = GsonConverterFactory.create();
        rxJavaCallAdapterFactory = RxJavaCallAdapterFactory.create();
//        clazz = getApiClazz();
        clazz = (Class<T>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
    }

    public T getApi() {
        if (api == null) {
            initHttpClient();
            Retrofit retrofit = new Retrofit.Builder()
                    .client(okHttpClient)
                    .baseUrl(getBaseUrl())
                    .addConverterFactory(converterFactory)
                    .addCallAdapterFactory(rxJavaCallAdapterFactory)
                    .build();

            api = retrofit.create(clazz);
        }
        return api;
    }

    public T getCustomApi() {
        if (customApi == null) {
            initHttpClient();
            Retrofit retrofit = new Retrofit.Builder()
                    .client(okHttpClient)
                    .baseUrl(getCustomUrl())
                    .addConverterFactory(converterFactory)
                    .addCallAdapterFactory(rxJavaCallAdapterFactory)
                    .build();

            customApi = retrofit.create(clazz);
        }
        return customApi;
    }

    @SuppressWarnings("ConstantConditions")
    private void initHttpClient() {
        try {
            if (okHttpClient == null) {
                OkHttpClient.Builder builder = new OkHttpClient.Builder().addInterceptor(new Interceptor() {
                    @Override
                    public Response intercept(Chain chain) throws IOException {
                        if (HttpUtils.isNetworkAvailable(getContext()) == HttpUtils.NET_TYPE.NET_NO) {
                            throw new NoNetworkException();
                        }
                        Request request;
                        Request.Builder requestBuilder = chain.request().newBuilder();
                        String token = getToken();
                        if (!TextUtils.isEmpty(token)) {
                            requestBuilder.addHeader("token", token);
                        }
                        if (!TextUtils.isEmpty(deviceId)) {
                            requestBuilder.addHeader("deviceId", deviceId);
                        }
                        requestBuilder.addHeader("ApiVersion", String.valueOf(1));
                        requestBuilder.addHeader("deviceType", "Android");
//                        requestBuilder.addHeader("Connection","close");
//                        if (!TextUtils.isEmpty(token)) {
//                            request = chain.request().newBuilder()
//                                    .addHeader("token", token)
//                                    .addHeader("version", "1.0")
//                                    .build();
//                        } else {
//                            request = chain.request().newBuilder()
//                                    .addHeader("version", "1.0")
//                                    .build();
//                        }
                        request = requestBuilder.build();

                        request = dealRequest(request);
                        if (TextUtils.equals("POST", request.method())) {
                            StringBuilder sb = new StringBuilder();
                            sb.append("POST ---> url=").append(request.url()).append("?");
                            if (request.body() instanceof FormBody) {
                                FormBody formBody = (FormBody) request.body();
                                for (int i = 0; i < formBody.size(); i++) {
                                    sb.append(formBody.encodedName(i)).append("=").append(formBody.encodedValue(i)).append("&");
                                }
                                sb.delete(sb.length() - 1, sb.length());
                            }
                            Logger.i(sb.toString());
                        } else {
                            Logger.i(request.method() + " ---> url=" + request.url());
                        }
//                        Logger.i("request" + request);

//                        request = dealRequest(request);
                        Response response = chain.proceed(request);
                        dealResponse(response);
                        return response;
                    }
                });
                if (isNeedHttps()) {
                    HTTPSCerUtils.setTrustAllCertificate(builder);
                }
                builder.connectTimeout(60, TimeUnit.SECONDS);
                builder.readTimeout(60, TimeUnit.SECONDS);
                builder.writeTimeout(60, TimeUnit.SECONDS);
                okHttpClient = builder.build();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    protected void dealResponse(Response response) {
    }

    /**
     * 若需要使用https请求,请设置证书信息 getCertificateNames()
     */
    protected boolean isNeedHttps() {
        return false;
    }

    protected String[] getCertificateNames() {
        return null;
    }

    public void setToken(String token) {
        this.token = token;
        clear();
    }

    public String getToken() {
        return token;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public String getDeviceId() {
        return deviceId;
    }

    private void clear() {
        okHttpClient = null;
        api = null;
        customApi = null;
    }

    protected Request dealRequest(Request request) {
        return request;
    }

    public void registerHttps() {
//        Glide.get(getContext()).register(GlideUrl.class, InputStream.class, new OkHttpUrlLoader.Factory(okHttpClient));
    }

//    protected abstract Class<T> getApiClazz();

    protected abstract String getBaseUrl();

    protected abstract String getCustomUrl();

    protected abstract Context getContext();

}

package com.kingyon.elevator.application;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.kingyon.elevator.data.DataSharedPreferences;
import com.kingyon.elevator.entities.AMapCityEntity;
import com.kingyon.elevator.entities.LocationEntity;
import com.kingyon.elevator.entities.UserEntity;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by GongLi on 2018/8/23.
 * Email：lc824767150@163.com
 */

public class AppContent {
    private static AppContent instance;
    //线程池
    private ExecutorService threadPool;

    private Gson gson;

    private Long myUserId;
    private String myUserRole;
    private LocationEntity location;
    private List<AMapCityEntity> cities;


    //APP启动时调用
    public void init(Context context) {
    }

    private AppContent() {
    }

    public static AppContent getInstance() {
        if (instance == null) {
            synchronized (AppContent.class) {
                if (instance == null) {
                    instance = new AppContent();
                }
            }
        }
        return instance;
    }

    private void initUserId() {
        if (myUserId == null) {
            UserEntity userBean = DataSharedPreferences.getUserBean();
            myUserId = userBean == null ? null : userBean.getObjectId();
        }
    }

    private void initUserRole() {
//        if (myUserRole == null) {
        UserEntity userEntity = DataSharedPreferences.getUserBean();
        myUserRole = userEntity == null ? null : userEntity.getRole();
//        }
    }

    public boolean isMySelf(long id) {
        initUserId();
        return myUserId != null && myUserId.longValue() == id;
    }

    public long getMyUserId() {
//        initUserId();
//        return myUserId != null ? myUserId.longValue() : 0;
        UserEntity userBean = DataSharedPreferences.getUserBean();
        return userBean == null ? 0 : userBean.getObjectId();
    }

    public String getMyUserRole() {
        initUserRole();
//        if (App.getInstance().isDebug()) {
//            return String.format("%s,%s,%s", Constants.RoleType.PARTNER, Constants.RoleType.INSTALLER, Constants.RoleType.NORMAL);
//        }
        return myUserRole != null ? myUserRole : "";
    }

    public Gson getGson() {
        if (gson == null) {
            gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();
        }
        return gson;
    }

    public ExecutorService getThreadPool() {
        if (threadPool == null) {
            threadPool = Executors.newFixedThreadPool(4);
        }
        return threadPool;
    }

    public LocationEntity getLocation() {
        return location;
    }

    public void setLocation(LocationEntity location) {
        this.location = location;
    }

    public Observable<List<AMapCityEntity>> getCities() {
        Observable<List<AMapCityEntity>> observable;
        if (cities == null) {
            observable = getCitiesObservable().doOnNext(new Action1<List<AMapCityEntity>>() {
                @Override
                public void call(List<AMapCityEntity> cityEntities) {
                    cities = cityEntities;
                }
            }).subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread());
        } else {
            observable = Observable.just(cities);
        }
        return observable;
    }

    private Observable<List<AMapCityEntity>> getCitiesObservable() {
        return Observable.just("gd_district_py.json")
                .flatMap(new Func1<String, Observable<List<AMapCityEntity>>>() {//读取文件里面的json
                    @Override
                    public Observable<List<AMapCityEntity>> call(String s) {
                        String result = "";
                        InputStream in = null;
                        try {
                            in = App.getInstance().getResources().getAssets().open(s); // 从Assets中的文件获取输入流
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
                        List<AMapCityEntity> cityEntities = AppContent.getInstance().getGson().fromJson(result, new TypeToken<List<AMapCityEntity>>() {
                        }.getType());
//                        for (AMapCityEntity province : cityEntities) {
//                            province.setPingYin(PinyinUtils.ccs2Pinyin(province.getName()));
//
//                            List<AMapCityEntity> cities = province.getDistricts();
//                            for (AMapCityEntity city : cities) {
//                                city.setPingYin(PinyinUtils.ccs2Pinyin(city.getName()));
//
//                                List<AMapCityEntity> areas = province.getDistricts();
//                                for (AMapCityEntity area : areas) {
//                                    area.setPingYin(PinyinUtils.ccs2Pinyin(area.getName()));
//
//                                    List<AMapCityEntity> others = province.getDistricts();
//                                    for (AMapCityEntity other : others) {
//                                        other.setPingYin(PinyinUtils.ccs2Pinyin(other.getName()));
//                                    }
//                                }
//                            }
//                        }
                        return Observable.just(cityEntities);
                    }
                })
                .flatMap(new Func1<List<AMapCityEntity>, Observable<List<AMapCityEntity>>>() {
                    @Override
                    public Observable<List<AMapCityEntity>> call(List<AMapCityEntity> entities) {
                        List<AMapCityEntity> cityEntities = new ArrayList<>();
                        if (entities != null && entities.size() > 0) {
                            for (AMapCityEntity city : entities) {
                                if (city.getDistricts() != null && city.getDistricts().size() > 0) {
                                    cityEntities.addAll(city.getDistricts());
                                }
                            }
                        }
                        return Observable.just(cityEntities);
                    }
                });
    }

    public void clear() {
        myUserId = null;
        myUserRole = null;
    }

}

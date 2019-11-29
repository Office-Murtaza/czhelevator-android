package com.kingyon.elevator.nets;

import android.content.Context;
import android.media.MediaMetadataRetriever;
import android.text.TextUtils;

import com.alibaba.fastjson.JSON;
import com.amap.api.services.core.AMapException;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.district.DistrictItem;
import com.amap.api.services.district.DistrictResult;
import com.amap.api.services.district.DistrictSearch;
import com.amap.api.services.district.DistrictSearchQuery;
import com.kingyon.elevator.application.App;
import com.kingyon.elevator.application.AppContent;
import com.kingyon.elevator.constants.Constants;
import com.kingyon.elevator.data.DataSharedPreferences;
import com.kingyon.elevator.entities.ADEntity;
import com.kingyon.elevator.entities.AMapCityEntity;
import com.kingyon.elevator.entities.AdDetectingEntity;
import com.kingyon.elevator.entities.AdTempletEntity;
import com.kingyon.elevator.entities.AdvertisionEntity;
import com.kingyon.elevator.entities.AnnouncementEntity;
import com.kingyon.elevator.entities.AvInfoEntity;
import com.kingyon.elevator.entities.BannerEntity;
import com.kingyon.elevator.entities.CameraBrandEntity;
import com.kingyon.elevator.entities.CellDetailsEntity;
import com.kingyon.elevator.entities.CellDeviceNumberEntity;
import com.kingyon.elevator.entities.CellItemEntity;
import com.kingyon.elevator.entities.CityCellEntity;
import com.kingyon.elevator.entities.CommitOrderEntiy;
import com.kingyon.elevator.entities.CooperationEntity;
import com.kingyon.elevator.entities.CooperationIdentityEntity;
import com.kingyon.elevator.entities.CooperationInfoEntity;
import com.kingyon.elevator.entities.CooperationInfoNewEntity;
import com.kingyon.elevator.entities.CouponItemEntity;
import com.kingyon.elevator.entities.DeviceDetailsInfo;
import com.kingyon.elevator.entities.DeviceNumberEntity;
import com.kingyon.elevator.entities.DeviceNumberInfo;
import com.kingyon.elevator.entities.FeedBackCache;
import com.kingyon.elevator.entities.FeedBackEntity;
import com.kingyon.elevator.entities.FeedBackMessageEntity;
import com.kingyon.elevator.entities.HomepageDataEntity;
import com.kingyon.elevator.entities.IdentityInfoEntity;
import com.kingyon.elevator.entities.IncomeOrPayEntity;
import com.kingyon.elevator.entities.IncomeRecordEntity;
import com.kingyon.elevator.entities.IncomeStatisticsEntity;
import com.kingyon.elevator.entities.IndustryEntity;
import com.kingyon.elevator.entities.InvoiceEntity;
import com.kingyon.elevator.entities.InvoiceInfoEntity;
import com.kingyon.elevator.entities.LiftElemEntity;
import com.kingyon.elevator.entities.LocalMaterialEntity;
import com.kingyon.elevator.entities.LocationEntity;
import com.kingyon.elevator.entities.LoginResultEntity;
import com.kingyon.elevator.entities.MateriaEntity;
import com.kingyon.elevator.entities.MonthOrDayIncomeOrPayEntity;
import com.kingyon.elevator.entities.MyWalletInfo;
import com.kingyon.elevator.entities.NormalElemEntity;
import com.kingyon.elevator.entities.NormalMessageEntity;
import com.kingyon.elevator.entities.NormalOptionEntity;
import com.kingyon.elevator.entities.OrderDetailsEntity;
import com.kingyon.elevator.entities.OrderFailedNumberEntity;
import com.kingyon.elevator.entities.OrderIdentityEntity;
import com.kingyon.elevator.entities.PointItemEntity;
import com.kingyon.elevator.entities.PropertyEntity;
import com.kingyon.elevator.entities.PropertyIdentityEntity;
import com.kingyon.elevator.entities.PropertyInfoEntity;
import com.kingyon.elevator.entities.PropertyInfomationInfo;
import com.kingyon.elevator.entities.RecommendInfoEntity;
import com.kingyon.elevator.entities.RecommendListInfo;
import com.kingyon.elevator.entities.SettlementEntity;
import com.kingyon.elevator.entities.UnreadNumberEntity;
import com.kingyon.elevator.entities.UserEntity;
import com.kingyon.elevator.entities.VersionEntity;
import com.kingyon.elevator.entities.WalletRecordEntity;
import com.kingyon.elevator.entities.WithdrawItemEntity;
import com.kingyon.elevator.utils.CheckCodePresenter;
import com.kingyon.elevator.utils.DBUtils;
import com.kingyon.elevator.utils.FormatUtils;
import com.kingyon.elevator.utils.RoleUtils;
import com.leo.afbaselibrary.nets.entities.DataEntity;
import com.leo.afbaselibrary.nets.entities.PageListEntity;
import com.leo.afbaselibrary.nets.entities.WxPayEntity;
import com.leo.afbaselibrary.nets.exceptions.ApiException;
import com.leo.afbaselibrary.nets.exceptions.ResultException;
import com.leo.afbaselibrary.uis.activities.BaseActivity;
import com.leo.afbaselibrary.utils.AFUtil;
import com.leo.afbaselibrary.utils.FileUtil;
import com.leo.afbaselibrary.utils.TimeUtil;
import com.orhanobut.logger.Logger;
import com.qiniu.android.storage.UploadManager;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.http.Field;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.functions.Func2;
import rx.functions.Func3;
import rx.schedulers.Schedulers;

/**
 * Created by gongli on 2017/7/17 17:39
 * email: lc824767150@163.com
 */
public class NetService {

    private static NetService sInstance;
    private Net mNet;
    private final int DEFAULT_SIZE = 15;
    private NetUpload netUpload;

    private NetService() {
        mNet = Net.getInstance();
        netUpload = new NetUpload(getApi(), new UploadManager());
    }

    public static NetService getInstance() {
        if (sInstance == null) {
            sInstance = new NetService();
        }
        return sInstance;
    }

    private NetApi getApi() {
        return mNet.getApi();
    }

    private <K> Observable<K> addSchedulers(Observable<K> observable) {
        return observable.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }

    //验证码
    public Observable<String> sendVerifyCode(String mobile, CheckCodePresenter.VerifyCodeType type) {
        return addSchedulers(getApi().getVerifyCode(mobile, type.getValue()));
    }

    public void uploadFile(final BaseActivity baseActivity, final byte[] bytes, final String suffix, final NetUpload.OnUploadCompletedListener onUploadCompletedListener) {
        netUpload.uploadFile(baseActivity, bytes, suffix, onUploadCompletedListener);
    }

    public void uploadFile(BaseActivity baseActivity, File file, NetUpload.OnUploadCompletedListener onUploadCompletedListener) {
        netUpload.uploadFile(baseActivity, file, onUploadCompletedListener, true);
    }

    public void uploadFile(BaseActivity baseActivity, File file, NetUpload.OnUploadCompletedListener onUploadCompletedListener, boolean needComptess) {
        Logger.i("upload File size:%s", FileUtil.convertFileSize(file.length()));
        netUpload.uploadFile(baseActivity, file, onUploadCompletedListener, needComptess);
    }

    public void uploadFiles(final BaseActivity baseActivity, final List<File> files, final NetUpload.OnUploadCompletedListener onUploadCompletedListener) {
        netUpload.uploadFiles(baseActivity, files, onUploadCompletedListener, true);
    }

    public void uploadFiles(final BaseActivity baseActivity, final List<File> files, final NetUpload.OnUploadCompletedListener onUploadCompletedListener, final boolean needCompress) {
        netUpload.uploadFiles(baseActivity, files, onUploadCompletedListener, needCompress);
    }

    public String getUploadResultString(List<String> images) {
        return netUpload.getUploadResultString(images);
    }

    //静态/通用
    public Observable<String> bindPushId(String pushId) {
        return addSchedulers(getApi().bindPushId(pushId, "ANDROID"));
    }

    public Observable<DataEntity<String>> richText(String type) {
        return addSchedulers(getApi().richText(type));
    }

    public Observable<VersionEntity> getLatestVersion(Context context) {
        return addSchedulers(getApi().getLatestVersion("ANDROID", AFUtil.getVersionCode(context)));
    }

    public Observable<PageListEntity<IncomeRecordEntity>> incomeRecordsList(String type, Long deviceId
            , String billSn, String role, int page) {
        return addSchedulers(getApi().incomeRecordsList(type, deviceId, billSn, role, page));
    }

    public Observable<List<PointItemEntity>> cellDeviceList(long cellId) {
        return addSchedulers(getApi().cellDeviceList(cellId));
    }

    public Observable<DeviceNumberInfo> deviceNumber() {
        Observable<DeviceNumberInfo> observable = Observable.zip(getApi().devicesNumber()
                , getApi().cellDevicesNumber(), new Func2<DeviceNumberEntity, List<CellDeviceNumberEntity>, DeviceNumberInfo>() {
                    @Override
                    public DeviceNumberInfo call(DeviceNumberEntity deviceNumberEntity, List<CellDeviceNumberEntity> cellDeviceNumberEntities) {
                        return new DeviceNumberInfo(deviceNumberEntity, cellDeviceNumberEntities);
                    }
                });
        return addSchedulers(observable);
    }

    public Observable<DeviceDetailsInfo> deviceDetails(long deviceId, String role, int page) {
        Observable<DeviceDetailsInfo> observable;
        if (page == 1) {
            observable = Observable.zip(getApi().incomeRecordsList("ALL", deviceId, null, role, page)
                    , getApi().deviceDetails(deviceId), new Func2<PageListEntity<IncomeRecordEntity>, PointItemEntity, DeviceDetailsInfo>() {
                        @Override
                        public DeviceDetailsInfo call(PageListEntity<IncomeRecordEntity> incomeRecordEntityPageListEntity, PointItemEntity entity) {
                            return new DeviceDetailsInfo(entity, incomeRecordEntityPageListEntity);
                        }
                    });
        } else {
            observable = getApi().incomeRecordsList("ALL", deviceId, null, role, page)
                    .flatMap(new Func1<PageListEntity<IncomeRecordEntity>, Observable<DeviceDetailsInfo>>() {
                        @Override
                        public Observable<DeviceDetailsInfo> call(PageListEntity<IncomeRecordEntity> incomeRecordEntityPageListEntity) {
                            return Observable.just(new DeviceDetailsInfo(null, incomeRecordEntityPageListEntity));
                        }
                    });
        }
        return addSchedulers(observable);
    }

    public Observable<PointItemEntity> deviceDetails(long deviceId) {
        return addSchedulers(getApi().deviceDetails(deviceId));
    }

    public Observable<String> repairDevice(long deviceId, Long reasonId, String remarks, String images) {
        return addSchedulers(getApi().repairDevice(deviceId, reasonId, remarks, images));
    }

    public Observable<List<NormalElemEntity>> repairReasons() {
        return addSchedulers(getApi().repairReasons());
    }

    public Observable<String> addDevice(Long objectId, String deviceNo, String deviceLocation, String deviceType
            , long cellId, long buildId, long unitId, long liftId, Long cameraBrand, String cameraIp) {
        return addSchedulers(getApi().addDevice(objectId, deviceNo, deviceLocation, deviceType
                , cellId, buildId, unitId, liftId, cameraBrand, cameraIp));
    }

    public Observable<String> addCell(Long objectId, String adcode, String address, String cellName
            , String cellType, Long humanTraffic, String cellLogo, String cellBanner
            , double longitude, double latitude) {
        return addSchedulers(getApi().addCell(objectId, adcode, address, cellName, cellType, humanTraffic
                , cellLogo, cellBanner, longitude, latitude));
    }

    public Observable<PageListEntity<PointItemEntity>> installerDeviceList(int page) {
        return addSchedulers(getApi().installerDeviceList(page));
    }

    public Observable<AdvertisionEntity> getAdertising() {
        return addSchedulers(getApi().getAdertising());
    }
//    public Observable<CameraEntity> liftCamera(long liftId) {
//        return addSchedulers(getApi().liftCamera(liftId));
//    }

    public Observable<List<CameraBrandEntity>> cameraBrandInfo() {
        return addSchedulers(getApi().cameraBrandInfo());
    }

    //用户
    public Observable<LoginResultEntity> login(String way, String unique, String userName, String password) {
        return addSchedulers(getApi().login(way, unique, userName, password));
    }

    public Observable<LoginResultEntity> register(String way, String phone, String vaildCode, String password
            , String unique, String avatar, String nickName) {
        return addSchedulers(getApi().register(way, phone, vaildCode, password, unique, avatar, nickName));
    }

    public Observable<String> unbindPhone(String phone, String code, String type) {
        return addSchedulers(getApi().unbindPhone(phone, code, type));
    }

    public Observable<String> resetPassword(String phone, String vaildCode, String newPassword) {
        return addSchedulers(getApi().resetPassword(phone, vaildCode, newPassword));
    }

    public Observable<String> changePassword(String oldPassword, String newPasswrod) {
        return addSchedulers(getApi().changePassword(oldPassword, newPasswrod));
    }

    public Observable<String> logout() {
        return addSchedulers(getApi().logout());
    }

    //首页
    public Observable<HomepageDataEntity> homepageDatas(LocationEntity locationParam) {
        Observable<HomepageDataEntity> observable;
        if (locationParam.getLongitude() == null || locationParam.getLatitude() == null) {
            observable = Observable.just(locationParam)
                    .flatMap(new Func1<LocationEntity, Observable<HomepageDataEntity>>() {
                        @Override
                        public Observable<HomepageDataEntity> call(LocationEntity locationEntity) {
                            DistrictSearch search = new DistrictSearch(App.getContext());
                            DistrictSearchQuery query = new DistrictSearchQuery();
                            query.setKeywords(locationEntity.getCity());//传入关键字
                            query.setShowBoundary(false);//是否返回边界值
                            query.setShowChild(false);
                            search.setQuery(query);
                            try {
                                DistrictResult districtResult = search.searchDistrict();//开始搜索
                                ArrayList<DistrictItem> districts = districtResult.getDistrict();
                                if (districts != null && districts.size() > 0) {
                                    DistrictItem districtItem = districts.get(0);
                                    LatLonPoint center = districtItem.getCenter();

                                    locationEntity.setLongitude(center.getLongitude());
                                    locationEntity.setLatitude(center.getLatitude());
                                    locationEntity.setCityCode(districtItem.getAdcode());
                                } else {
                                    Logger.e("onCityChoosed(AMapCityEntity entity) 出错");
                                }
                            } catch (AMapException e) {
                                e.printStackTrace();
                                Logger.e("onCityChoosed(AMapCityEntity entity) 出错");
                            }
                            if (locationEntity.getLongitude() == null || locationEntity.getLatitude() == null) {
                                throw new ResultException(9002, "AMap DistrictSearch error!");
                            }
                            DataSharedPreferences.saveLocationCache(locationEntity);
                            return Observable.zip(getApi().nearlyCell(locationEntity.getLongitude(), locationEntity.getLatitude())
                                    , getApi().homepageBanners(), getApi().announcementList()
                                    , new Func3<List<CellItemEntity>, List<BannerEntity>, List<AnnouncementEntity>, HomepageDataEntity>() {
                                        @Override
                                        public HomepageDataEntity call(List<CellItemEntity> cellItemEntities, List<BannerEntity> bannerEntities, List<AnnouncementEntity> announcementEntities) {
                                            return new HomepageDataEntity(cellItemEntities, bannerEntities, announcementEntities);
                                        }
                                    });
                        }
                    });
        } else {
            DataSharedPreferences.saveLocationCache(locationParam);
            observable = Observable.zip(getApi().nearlyCell(locationParam.getLongitude(), locationParam.getLatitude())
                    , getApi().homepageBanners(), getApi().announcementList()
                    , new Func3<List<CellItemEntity>, List<BannerEntity>, List<AnnouncementEntity>, HomepageDataEntity>() {
                        @Override
                        public HomepageDataEntity call(List<CellItemEntity> cellItemEntities, List<BannerEntity> bannerEntities, List<AnnouncementEntity> announcementEntities) {
                            return new HomepageDataEntity(cellItemEntities, bannerEntities, announcementEntities);
                        }
                    });
        }
        return addSchedulers(observable);
    }

    public Observable<List<CellItemEntity>> nearlyCell(double longitude, double latitude) {
        return addSchedulers(getApi().nearlyCell(longitude, latitude));
    }

    public Observable<List<NormalOptionEntity>> liftknowledges() {
        return addSchedulers(getApi().liftknowledges());
    }

    public Observable<DataEntity<String>> knowledgeDetils(long objectId) {
        return addSchedulers(getApi().knowledgeDetils(objectId));
    }

    public Observable<PageListEntity<ADEntity>> recommendAds(int page) {
        return addSchedulers(getApi().recommendAds(page));
    }

//    public Observable<List<CellItemEntity>> nearlyCell(double longitude, double latitude) {
//        return addSchedulers(getApi().nearlyCell(longitude, latitude));
//    }

    public Observable<PageListEntity<CellItemEntity>> searchCell(final String keyWord, AMapCityEntity cityEntity
            , final String distance, final String areaId, final String cellType, final int page, final LocationEntity locationEntity) {
        Observable<PageListEntity<CellItemEntity>> observable;
        if (TextUtils.isEmpty(cityEntity.getAdcode()) || TextUtils.isEmpty(cityEntity.getCenter())) {
            observable = Observable.just(cityEntity)
                    .flatMap(new Func1<AMapCityEntity, Observable<PageListEntity<CellItemEntity>>>() {
                        @Override
                        public Observable<PageListEntity<CellItemEntity>> call(AMapCityEntity cityEntity) {
                            DistrictSearch search = new DistrictSearch(App.getContext());
                            DistrictSearchQuery query = new DistrictSearchQuery();
                            query.setKeywords(cityEntity.getName());//传入关键字
                            query.setShowBoundary(false);//是否返回边界值
                            query.setShowChild(false);
                            search.setQuery(query);
                            try {
                                DistrictResult districtResult = search.searchDistrict();//开始搜索
                                ArrayList<DistrictItem> districts = districtResult.getDistrict();
                                if (districts != null && districts.size() > 0) {
                                    DistrictItem districtItem = districts.get(0);
                                    LatLonPoint center = districtItem.getCenter();
                                    cityEntity.setCenter(String.format("%s,%s", center.getLongitude(), center.getLatitude()));
                                    cityEntity.setAdcode(districtItem.getAdcode());
                                } else {
                                    Logger.e("onCityChoosed(AMapCityEntity entity) 出错");
                                }
                            } catch (AMapException e) {
                                e.printStackTrace();
                                Logger.e("onCityChoosed(AMapCityEntity entity) 出错");
                            }
                            if (TextUtils.isEmpty(cityEntity.getAdcode())) {
                                throw new ResultException(9002, "AMap DistrictSearch error!");
                            }
                            Observable<PageListEntity<CellItemEntity>> pageListEntityObservable;
                            if (locationEntity == null) {
                                double[] centerLatLon = FormatUtils.getInstance().getCenterLatLon(cityEntity.getCenter());
                                pageListEntityObservable = getApi().searchCell(keyWord, cityEntity.getAdcode(), distance, areaId, cellType, page, centerLatLon[0], centerLatLon[1]);
                            } else {
                                pageListEntityObservable = getApi().searchCell(keyWord, cityEntity.getAdcode(), distance, areaId, cellType, page, locationEntity.getLongitude(), locationEntity.getLatitude());
                            }
                            return pageListEntityObservable;
                        }
                    });
        } else {
            if (locationEntity == null) {
                double[] centerLatLon = FormatUtils.getInstance().getCenterLatLon(cityEntity.getCenter());
                observable = getApi().searchCell(keyWord, cityEntity.getAdcode(), distance, areaId, cellType, page, centerLatLon[0], centerLatLon[1]);
            } else {
                observable = getApi().searchCell(keyWord, cityEntity.getAdcode(), distance, areaId, cellType, page, locationEntity.getLongitude(), locationEntity.getLatitude());
            }
        }
//        if (App.getInstance().isDebug()) {
//            observable = observable.doOnNext(new Action1<PageListEntity<CellItemEntity>>() {
//                @Override
//                public void call(PageListEntity<CellItemEntity> cellItemEntityPageListEntity) {
//                    double[] longitude = new double[]{104.073694, 104.189026, 103.955395, 104.069035};
//                    double[] latitude = new double[]{30.697218, 30.651446, 30.670495, 30.536632};
//                    List<CellItemEntity> content = cellItemEntityPageListEntity.getContent();
//                    for (int i = 0; i < content.size(); i++) {
//                        CellItemEntity cellItemEntity = content.get(i);
//                        cellItemEntity.setCellName(String.format("%s%s", cellItemEntity.getCellName(), i));
//                        cellItemEntity.setLiftNum((i + 1) * 100);
//                        cellItemEntity.setObjctId(i + (i + 1) * 100);
//                        cellItemEntity.setLongitude(longitude[i % 3]);
//                        cellItemEntity.setLatitude(latitude[i % 3]);
//                    }
//                }
//            });
//        }
        return addSchedulers(observable);
    }

    //小区&点位
    public Observable<List<CityCellEntity>> cityCellNums() {
        return addSchedulers(getApi().cityCellNums());
    }

    public Observable<CellDetailsEntity> cellDetails(long objectId) {
        return addSchedulers(getApi().cellDetails(objectId));
    }

    public Observable<PageListEntity<CellItemEntity>> plansList(String type
            , Long startTime, Long endTime, int page) {
//        return addSchedulers(getApi().plansList(type, startTime, endTime)
//                .doOnNext(new Action1<List<CellItemEntity>>() {
//                    @Override
//                    public void call(List<CellItemEntity> cellItemEntities) {
//                        if (cellItemEntities != null && cellItemEntities.size() > 0) {
//                            Iterator<CellItemEntity> iterator = cellItemEntities.iterator();
//                            while (iterator.hasNext()) {
//                                CellItemEntity next = iterator.next();
//                                if (next.getTargetScreenNum() <= 0) {
//                                    iterator.remove();
//                                }
//                            }
//                        }
//                    }
//                }));
        return addSchedulers(getApi().plansList(type, startTime, endTime, page));
    }

    public Observable<List<PointItemEntity>> planCellsPoinList(long cellId, String type
            , long startTime, long endTime) {
        return addSchedulers(getApi().planCellsPoinList(cellId, type, startTime, endTime));
    }

    public Observable<String> plansAdd(Long planId
            , String planName, String type) {
        return addSchedulers(getApi().plansAdd(planId, planName, type));
    }

    public Observable<String> plansDelete(String planIds) {
        return addSchedulers(getApi().plansDelete(planIds));
    }

    public Observable<String> plansAddCells(String type, String cells) {
        return addSchedulers(getApi().plansAddCells(type, cells));
    }

    public Observable<String> plansRemoveCells(String type, String cells) {
        return addSchedulers(getApi().plansRemoveCells(type, cells));
    }

    public Observable<List<NormalElemEntity>> getBuildByCell(long objectId) {
        return addSchedulers(getApi().getBuildByCell(objectId).doOnNext(new Action1<List<NormalElemEntity>>() {
            @Override
            public void call(List<NormalElemEntity> normalElemEntities) {
                if (normalElemEntities != null) {
                    for (NormalElemEntity entity : normalElemEntities) {
                        entity.setCanEdit(false);
                    }
                }
            }
        }));
    }

    public Observable<List<NormalElemEntity>> getUnitByBuild(long objectId) {
        return addSchedulers(getApi().getUnitByBuild(objectId).doOnNext(new Action1<List<NormalElemEntity>>() {
            @Override
            public void call(List<NormalElemEntity> normalElemEntities) {
                if (normalElemEntities != null) {
                    for (NormalElemEntity entity : normalElemEntities) {
                        entity.setCanEdit(false);
                    }
                }
            }
        }));
    }

    public Observable<List<LiftElemEntity>> getLiftByUnit(long objectId) {
        return addSchedulers(getApi().getLiftByUnit(objectId).doOnNext(new Action1<List<LiftElemEntity>>() {
            @Override
            public void call(List<LiftElemEntity> normalElemEntities) {
                if (normalElemEntities != null) {
                    for (LiftElemEntity entity : normalElemEntities) {
                        entity.setCanEdit(false);
                    }
                }
            }
        }));
    }

    public Observable<String> addBuilding(Long objectId, long cellId, String name) {
        return addSchedulers(getApi().addBuilding(objectId, cellId, name));
    }

    public Observable<String> addLift(Long objectId, long unitId, String sn, String name, int max
            , int negative, int base) {
        return addSchedulers(getApi().addLift(objectId, unitId, sn, name, max, negative, base));
    }

    public Observable<String> addUnit(Long objectid, long buldingId, String name) {
        return addSchedulers(getApi().addUnit(objectid, buldingId, name));
    }

    public Observable<String> removeCell(long objectId) {
        return addSchedulers(getApi().removeCell(objectId));
    }

    public Observable<String> removeBuilding(long objectId) {
        return addSchedulers(getApi().removeBuilding(objectId));
    }

    public Observable<String> removeUnit(long objectId) {
        return addSchedulers(getApi().removeUnit(objectId));
    }

    public Observable<String> removeLift(long objectId) {
        return addSchedulers(getApi().removeLift(objectId));
    }

    //订单
    public Observable<CommitOrderEntiy> commitOrder(String type, long startTime, long endTime
            , Long adId, String deviceParams, String coupons, long adIndustry) {
        long startRealTime;
        long curTime = System.currentTimeMillis();
        if (TextUtils.equals(TimeUtil.getYMdTime(startTime), TimeUtil.getYMdTime(curTime))) {
            startRealTime = curTime;
        } else {
            startRealTime = startTime;
        }
        return addSchedulers(getApi().commitOrder(type, startRealTime, endTime, adId, deviceParams
                , coupons, adIndustry));
    }

    public Observable<OrderDetailsEntity> orderDetatils(long orderId) {
        return addSchedulers(getApi().orderDetatils(orderId));
    }

    public Observable<OrderIdentityEntity> orderIdentityInfo() {
        return addSchedulers(getApi().orderIdentityInfo());
    }

    public Observable<WxPayEntity> orderPay(long orderId, String way) {
        Observable<WxPayEntity> zip = Observable.zip(getApi().orderPay(orderId, way), Observable.just(way), new Func2<WxPayEntity, String, WxPayEntity>() {
            @Override
            public WxPayEntity call(WxPayEntity wxPayEntity, String s) {
                if (wxPayEntity == null) {
                    wxPayEntity = new WxPayEntity();
                }
                wxPayEntity.setPayType(s);
                return wxPayEntity;
            }
        });
        return addSchedulers(zip);
    }

    public Observable<PageListEntity<OrderDetailsEntity>> orderList(String status, int page) {
        return addSchedulers(getApi().orderList(status, page));
    }

    public Observable<DataEntity<String>> downloadContract() {
        return addSchedulers(getApi().downloadContract());
    }

    public Observable<List<PointItemEntity>> orderPoints(long orderId) {
        return addSchedulers(getApi().orderPoints(orderId));
    }

    public Observable<String> downAd(long orderId, long tagReasonId, String undercastRemarks) {
        return addSchedulers(getApi().downAd(orderId, tagReasonId, undercastRemarks));
    }

    public Observable<PageListEntity<AdDetectingEntity>> adPlayList(long orderId, long deviceId, int page) {
        return addSchedulers(getApi().adPlayList(orderId, deviceId, page));
    }

    public Observable<List<NormalElemEntity>> downAdTags() {
        return addSchedulers(getApi().downAdTags());
    }

    public Observable<String> orderCancel(long orderId) {
        return addSchedulers(getApi().orderCancel(orderId));
    }

    public Observable<String> orderDelete(long orderId) {
        return addSchedulers(getApi().orderDelete(orderId));
    }

    //广告
    public Observable<ADEntity> createOrEidtAd(final Long objectId, final boolean onlyInfo, String planType, final String screenType
            , final String title, final String videoUrl, final String imageUrl, final String bgMusic, final String videoPath, final String imagePath) {
        final Long[] durationResult = new Long[1];
        Observable<ADEntity> observable;
        if (TextUtils.isEmpty(videoUrl)) {
            observable = getApi().createOrEidtAd(objectId, onlyInfo, planType, screenType, title, videoUrl
                    , imageUrl, bgMusic, null);
        } else {
            observable = getAvInfoDuration(videoUrl)
                    .flatMap(new Func1<Long, Observable<ADEntity>>() {
                        @Override
                        public Observable<ADEntity> call(Long duration) {
                            durationResult[0] = duration;
                            return getApi().createOrEidtAd(objectId, onlyInfo, planType, screenType, title, videoUrl
                                    , imageUrl, bgMusic, duration);
                        }
                    });
        }
        return addSchedulers(observable).doOnNext(new Action1<ADEntity>() {
            @Override
            public void call(ADEntity adEntity) {
                if (!TextUtils.isEmpty(adEntity.getVideoUrl()) && !TextUtils.isEmpty(videoPath) && !videoPath.startsWith("http")) {
                    LocalMaterialEntity localMaterialEntity = new LocalMaterialEntity();
                    localMaterialEntity.setUserId(AppContent.getInstance().getMyUserId());
                    localMaterialEntity.setPlanType(adEntity.getPlanType());
                    localMaterialEntity.setScreenType(screenType);
                    localMaterialEntity.setAdId(adEntity.getObjctId());
                    localMaterialEntity.setType(Constants.Materia_Type.VIDEO);
                    localMaterialEntity.setUrl(adEntity.getVideoUrl());
                    localMaterialEntity.setPath(videoPath);
                    localMaterialEntity.setCreateTime(System.currentTimeMillis());
                    localMaterialEntity.setDuration(durationResult[0]);
                    localMaterialEntity.setName(adEntity.getTitle());
                    DBUtils.getInstance().updateLocalMateria(localMaterialEntity);
                }
                if (!TextUtils.isEmpty(adEntity.getImageUrl()) && !TextUtils.isEmpty(imagePath) && !imagePath.startsWith("http")) {
                    LocalMaterialEntity localMaterialEntity = new LocalMaterialEntity();
                    localMaterialEntity.setUserId(AppContent.getInstance().getMyUserId());
                    localMaterialEntity.setPlanType(adEntity.getPlanType());
                    localMaterialEntity.setScreenType(screenType);
                    localMaterialEntity.setAdId(adEntity.getObjctId());
                    localMaterialEntity.setType(Constants.Materia_Type.IMAGE);
                    localMaterialEntity.setUrl(adEntity.getImageUrl());
                    localMaterialEntity.setPath(imagePath);
                    localMaterialEntity.setCreateTime(System.currentTimeMillis());
                    localMaterialEntity.setDuration(0);
                    localMaterialEntity.setName(adEntity.getTitle());
                    DBUtils.getInstance().updateLocalMateria(localMaterialEntity);
                }
            }
        });
    }

    public Observable<Long> getAvInfoDuration(String url) {
        return Observable.just(url)
                .flatMap(new Func1<String, Observable<Long>>() {
                    @Override
                    public Observable<Long> call(String url) {
                        Long duration = null;
                        MediaMetadataRetriever mmr = new MediaMetadataRetriever();
                        if (url != null) {
                            try {
                                if (url.startsWith("http")) {
                                    HashMap<String, String> headers = new HashMap<>();
                                    headers.put("User-Agent", "Mozilla/5.0 (Linux; U; Android 4.4.2; zh-CN; MW-KW-001 Build/JRO03C) AppleWebKit/533.1 (KHTML, like Gecko) Version/4.0 UCBrowser/1.0.0.001 U4/0.8.0 Mobile Safari/533.1");
                                    mmr.setDataSource(url, headers);
                                } else {
                                    mmr.setDataSource(url);
                                }
                                duration = Long.parseLong(mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION));
                            } catch (Exception ex) {
                                Logger.w("MediaUtils getVideoDuring(String mUri) 出错");
                            } finally {
                                mmr.release();
                            }
                        } else {
                            Logger.w("MediaUtils getVideoSize(String mUri, int[] size) mUri == null");
                        }
                        return Observable.just(duration);
                    }
                });
    }

    public Observable<AvInfoEntity> getQiniuAvInfo(String url) {
        Observable<AvInfoEntity> observable = Observable.just(url)
                .flatMap(new Func1<String, Observable<AvInfoEntity>>() {
                    @Override
                    public Observable<AvInfoEntity> call(String s) {
                        HttpClient httpClient = new DefaultHttpClient();
                        HttpUriRequest getRequest = new HttpGet(String.format("%s?avinfo", s));
                        String requestResult;
                        try {
                            HttpResponse response = httpClient.execute(getRequest);
                            if (response.getStatusLine().getStatusCode() == 200) {
                                HttpEntity entity = response.getEntity();
                                requestResult = EntityUtils.toString(entity, "utf-8");//将entity当中的数据转换为字符串
                            } else {
                                requestResult = null;
                            }
                        } catch (IOException e) {
                            requestResult = null;
                            e.printStackTrace();
                        }
                        return Observable.just(JSON.parseObject(requestResult, AvInfoEntity.class));
                    }
                });
        return addSchedulers(observable);
    }

    public Observable<PageListEntity<ADEntity>> myAdList(String planType, int page) {
        return addSchedulers(getApi().myAdList(planType, page));
    }

    public Observable<String> deleteAd(long objectId) {
        return addSchedulers(getApi().deleteAd(objectId));
    }

    public Observable<PageListEntity<AdTempletEntity>> getPicAdTemplet(String screenSplit, String sort
            , Long type, int page) {
        return addSchedulers(getApi().getPicAdTemplet(screenSplit, sort, type, page));
    }

    public Observable<List<NormalElemEntity>> getAdTempletType() {
        return addSchedulers(getApi().getAdTempletType());
    }

    public Observable<PageListEntity<MateriaEntity>> getMaterials(String planType, String screenSplit, String type, int page) {
        return addSchedulers(getApi().getMaterials(planType, screenSplit, type, page));
    }

    //个人中心
    public Observable<List<CouponItemEntity>> getMeetCoupons(float price, String type) {
        return addSchedulers(getApi().getMeetCoupons(price, type));
    }

    public Observable<PageListEntity<CouponItemEntity>> getCoupons(String status, int page) {
        return addSchedulers(getApi().getCoupons(status, page));
    }

    public Observable<String> donateCoupons(String phone, String couponCounts, String couponIds) {
        return addSchedulers(getApi().donateCoupons(phone, couponCounts, couponIds));
    }

    public Observable<IdentityInfoEntity> getIdentityInformation() {
        return addSchedulers(getApi().getIdentityInformation());
    }

    public Observable<List<IndustryEntity>> getIndustrys() {
        return addSchedulers(getApi().getIndustrys());
    }

    public Observable<String> identityAuth(String type, String personName
            , String idNum, String idFace
            , String idBack, String companyName
            , String businessCert) {
        return addSchedulers(getApi().identityAuth(type, personName, idNum, idFace, idBack, companyName, businessCert));
    }

    public Observable<UserEntity> userProfile() {
        return addSchedulers(getApi().userProfile());
    }

    public Observable<UserEntity> userEidtProfile(Map<String, String> params) {
        return addSchedulers(getApi().userEidtProfile(params));
    }

    public Observable<DataEntity<Float>> myWallet() {
        return addSchedulers(getApi().myWallet());
    }

    public Observable<MyWalletInfo> myWalletInfo(int page) {
        Observable<MyWalletInfo> observable;
        if (page == 1) {
            observable = Observable.zip(getApi().myWalletRecords(page), getApi().myWallet()
                    , new Func2<PageListEntity<WalletRecordEntity>, DataEntity<Float>, MyWalletInfo>() {
                        @Override
                        public MyWalletInfo call(PageListEntity<WalletRecordEntity> walletRecordEntityPageListEntity, DataEntity<Float> floatDataEntity) {
                            return new MyWalletInfo(floatDataEntity.getData(), walletRecordEntityPageListEntity);
                        }
                    });
        } else {
            observable = getApi().myWalletRecords(page)
                    .flatMap(new Func1<PageListEntity<WalletRecordEntity>, Observable<MyWalletInfo>>() {
                        @Override
                        public Observable<MyWalletInfo> call(PageListEntity<WalletRecordEntity> walletRecordEntityPageListEntity) {
                            return Observable.just(new MyWalletInfo(null, walletRecordEntityPageListEntity));
                        }
                    });
        }
        return addSchedulers(observable);
    }

    public Observable<WxPayEntity> rechageWallet(String way, float amount) {
        Observable<WxPayEntity> zip = Observable.zip(getApi().rechageWallet(way, amount), Observable.just(way)
                , new Func2<WxPayEntity, String, WxPayEntity>() {
                    @Override
                    public WxPayEntity call(WxPayEntity wxPayEntity, String s) {
                        wxPayEntity.setPayType(s);
                        return wxPayEntity;
                    }
                });
        return addSchedulers(zip);
    }

    public Observable<InvoiceInfoEntity> invoiceInfo() {
        return addSchedulers(getApi().invoiceInfo());
    }

    public Observable<String> createInvoice(String invoiceType, String invoiceStart, String invoiceNo
            , String bank, float invoiceAmount, String receiveEmail, String content) {
        return addSchedulers(getApi().createInvoice(invoiceType, invoiceStart, invoiceNo, bank
                , invoiceAmount, receiveEmail, content));
    }

    public Observable<PageListEntity<InvoiceEntity>> invoiceList(int page) {
        return addSchedulers(getApi().invoiceList(page));
    }

    public Observable<RecommendInfoEntity> recommedInfo() {
        return addSchedulers(getApi().recommedInfo());
    }

    public Observable<RecommendListInfo> recommendListInfo(int page) {
        Observable<RecommendListInfo> observable;
        if (page == 1) {
            observable = Observable.zip(getApi().recommendList(page), getApi().recommedInfo()
                    , new Func2<PageListEntity<UserEntity>, RecommendInfoEntity, RecommendListInfo>() {
                        @Override
                        public RecommendListInfo call(PageListEntity<UserEntity> userEntityPageListEntity, RecommendInfoEntity recommendInfoEntity) {
                            return new RecommendListInfo(recommendInfoEntity, userEntityPageListEntity);
                        }
                    });
        } else {
            observable = getApi().recommendList(page)
                    .flatMap(new Func1<PageListEntity<UserEntity>, Observable<RecommendListInfo>>() {
                        @Override
                        public Observable<RecommendListInfo> call(PageListEntity<UserEntity> userEntityPageListEntity) {
                            return Observable.just(new RecommendListInfo(null, userEntityPageListEntity));
                        }
                    });
        }
        return addSchedulers(observable);
    }

    public Observable<PageListEntity<CellItemEntity>> myCollects(int page) {
        return addSchedulers(getApi().myCollects(page));
    }

    public Observable<String> collectCell(long cellId) {
        return addSchedulers(getApi().collectCell(cellId));
    }

    public Observable<String> cancelCollect(String cellIds) {
        return addSchedulers(getApi().cancelCollect(cellIds));
    }

    public Observable<PageListEntity<FeedBackEntity>> myFeedBackList(int page) {
        return addSchedulers(getApi().myFeedBackList(page));
    }

    public Observable<String> createFeedBack(String titile, String images) {
        return addSchedulers(getApi().createFeedBack(titile, images));
    }

    public Observable<String> commentFeedBack(long objectId, String content) {
        return addSchedulers(getApi().commentFeedBack(objectId, content));
    }

    public Observable<FeedBackCache> getFeedBackInfo(long objectId, int page) {
        Observable<FeedBackCache> observable;
        if (page == 1) {
            observable = Observable.zip(getApi().getFeedBack(objectId), getApi().feedBackDetail(objectId, page)
                    , new Func2<FeedBackEntity, PageListEntity<FeedBackMessageEntity>, FeedBackCache>() {
                        @Override
                        public FeedBackCache call(FeedBackEntity feedBackEntity, PageListEntity<FeedBackMessageEntity> feedBackMessageEntityPageListEntity) {
                            return new FeedBackCache(feedBackEntity, feedBackMessageEntityPageListEntity);
                        }
                    });
        } else {
            observable = getApi().feedBackDetail(objectId, page)
                    .flatMap(new Func1<PageListEntity<FeedBackMessageEntity>, Observable<FeedBackCache>>() {
                        @Override
                        public Observable<FeedBackCache> call(PageListEntity<FeedBackMessageEntity> feedBackMessageEntityPageListEntity) {
                            return Observable.just(new FeedBackCache(null, feedBackMessageEntityPageListEntity));
                        }
                    });
        }
        return addSchedulers(observable);
    }

    public Observable<UnreadNumberEntity> unreadCount() {
        return addSchedulers(getApi().unreadCount());
    }

    public Observable<OrderFailedNumberEntity> orderPublishFailed() {
        return addSchedulers(getApi().orderPublishFailedNum());
    }

    public Observable<PageListEntity<NormalMessageEntity>> getMessageList(String type, int page) {
        return addSchedulers(getApi().getMessageList(type, page));
    }

    public Observable<String> deleteMessage(long messageId) {
        return addSchedulers(getApi().deleteMessage(messageId));
    }

    public Observable<String> messageRead(long messageId) {
        return addSchedulers(getApi().messageRead(messageId));
    }

    //合伙人
    public Observable<CooperationEntity> cooperationInfo() {
        final CooperationEntity entity = new CooperationEntity();
        Observable<CooperationEntity> observable = Observable.just(DataSharedPreferences.getUserBean())
                .flatMap(new Func1<UserEntity, Observable<CooperationIdentityEntity>>() {
                    @Override
                    public Observable<CooperationIdentityEntity> call(UserEntity userEntity) {
                        if (userEntity == null) {
                            throw new ResultException(ApiException.NO_LOGIN, "未登录");
                        }
//                        boolean bePartner = TextUtils.equals(Constants.RoleType.PARTNER, userEntity.getRole());
                        boolean bePartner = RoleUtils.getInstance().roleBeTarget(Constants.RoleType.PARTNER, userEntity.getRole());
                        entity.setBePartner(bePartner);
                        Observable<CooperationIdentityEntity> identityEntityObservable;
                        if (bePartner) {
                            identityEntityObservable = Observable.just(null);
                        } else {
                            identityEntityObservable = getApi().cooperationIentityInfo();
                        }
                        return identityEntityObservable;
                    }
                })
                .flatMap(new Func1<CooperationIdentityEntity, Observable<CooperationInfoNewEntity>>() {
                    @Override
                    public Observable<CooperationInfoNewEntity> call(CooperationIdentityEntity cooperationIdentityEntity) {
                        Observable<CooperationInfoNewEntity> observable;
                        if (entity.isBePartner() || (cooperationIdentityEntity != null && TextUtils.equals(Constants.IDENTITY_STATUS.AUTHED, cooperationIdentityEntity.getStatus()))) {
                            observable = getApi().cooperationInfoNew();
                        } else {
                            observable = Observable.just(null);
                        }
                        entity.setIdentity(cooperationIdentityEntity);
                        return observable;
                    }
                }).flatMap(new Func1<CooperationInfoNewEntity, Observable<CooperationEntity>>() {
                    @Override
                    public Observable<CooperationEntity> call(CooperationInfoNewEntity cooperationInfoEntity) {
                        entity.setInfo(cooperationInfoEntity);
                        return Observable.just(entity);
                    }
                });
        return addSchedulers(observable);
    }

    public Observable<String> cooperationApply(final String partnerName, final String phone, final AMapCityEntity city) {
        Observable<String> observable;
        if (TextUtils.isEmpty(city.getAdcode())) {
            observable = Observable.just(city)
                    .flatMap(new Func1<AMapCityEntity, Observable<String>>() {
                        @Override
                        public Observable<String> call(AMapCityEntity cityEntity) {
                            DistrictSearch search = new DistrictSearch(App.getContext());
                            DistrictSearchQuery query = new DistrictSearchQuery();
                            query.setKeywords(cityEntity.getName());//传入关键字
                            query.setShowBoundary(false);//是否返回边界值
                            query.setShowChild(false);
                            search.setQuery(query);
                            try {
                                DistrictResult districtResult = search.searchDistrict();//开始搜索
                                ArrayList<DistrictItem> districts = districtResult.getDistrict();
                                if (districts != null && districts.size() > 0) {
                                    DistrictItem districtItem = districts.get(0);
                                    LatLonPoint center = districtItem.getCenter();
                                    cityEntity.setCenter(String.format("%s,%s", center.getLongitude(), center.getLatitude()));
                                    cityEntity.setAdcode(districtItem.getAdcode());
                                } else {
                                    Logger.e("cooperationApply() 出错");
                                }
                            } catch (AMapException e) {
                                e.printStackTrace();
                                Logger.e("cooperationApply() 出错");
                            }
                            if (TextUtils.isEmpty(cityEntity.getAdcode())) {
                                throw new ResultException(9002, "AMap DistrictSearch error!");
                            }
                            return getApi().cooperationApply(partnerName, phone, cityEntity.getAdcode());
                        }
                    });
        } else {
            observable = getApi().cooperationApply(partnerName, phone, city.getAdcode());
        }
        return addSchedulers(observable);
    }

    public Observable<String> partnerWithdraw(double amount, String withDrawWay, String aliAcount
            , String bankName, String cardNo, String cardholder) {
        return addSchedulers(getApi().partnerWithdraw(amount, withDrawWay, aliAcount, bankName, cardNo, cardholder));
    }

    public Observable<PageListEntity<WithdrawItemEntity>> partnerWithdrawList(int page) {
        return addSchedulers(getApi().partnerWithdrawList(page));
    }

    public Observable<PageListEntity<CellItemEntity>> partnerCellList(Double longitude, Double latitude, int page) {
        return addSchedulers(getApi().partnerCellList(longitude, latitude, page).doOnNext(new Action1<PageListEntity<CellItemEntity>>() {
            @Override
            public void call(PageListEntity<CellItemEntity> cellItemEntityPageListEntity) {
                if (cellItemEntityPageListEntity != null && cellItemEntityPageListEntity.getContent() != null) {
                    for (CellItemEntity entity : cellItemEntityPageListEntity.getContent()) {
                        entity.setCanEdit(false);
                    }
                }
            }
        }));
    }

//    public Observable<List<PointItemEntity>> partnerCellDevices(long cellId) {
//        return addSchedulers(getApi().partnerCellDevices(cellId));
//    }

    public Observable<PageListEntity<IncomeStatisticsEntity>> partnerIncomeStatistics(String filter, int page) {
        return addSchedulers(getApi().partnerIncomeStatistics(filter, page));
    }

    public Observable<PageListEntity<IncomeStatisticsEntity>> partnerEarningsDetails(long startTime
            , long endTime, int page) {
        return addSchedulers(getApi().partnerEarningsDetails(startTime, endTime, page));
    }

    public Observable<PageListEntity<IncomeStatisticsEntity>> partnerPropertyFeeRecords(int page) {
        return addSchedulers(getApi().partnerPropertyFeeRecords(page));
    }

    public Observable<PageListEntity<IncomeStatisticsEntity>> partnerOpticalFeeRecords(int page) {
        return addSchedulers(getApi().partnerOpticalFeeRecords(page));
    }

    //物业
    public Observable<String> propertyApply(String name, String contact, String phone, String image) {
        return addSchedulers(getApi().propertyApply(name, contact, phone, image));
    }

    public Observable<PropertyEntity> propertyInfo() {
        final PropertyEntity entity = new PropertyEntity();
        Observable<PropertyEntity> observable = Observable.just(DataSharedPreferences.getUserBean())
                .flatMap(new Func1<UserEntity, Observable<PropertyIdentityEntity>>() {
                    @Override
                    public Observable<PropertyIdentityEntity> call(UserEntity userEntity) {
                        if (userEntity == null) {
                            throw new ResultException(ApiException.NO_LOGIN, "未登录");
                        }
//                        boolean bePropertyCompany = TextUtils.equals(Constants.RoleType.PROPERTY, userEntity.getRole());
                        boolean bePropertyCompany = RoleUtils.getInstance().roleBeTarget(Constants.RoleType.PROPERTY, userEntity.getRole());
                        entity.setBePropertyCompany(bePropertyCompany);
//                        boolean bePropertyCell = TextUtils.equals(Constants.RoleType.NEIGHBORHOODS, userEntity.getRole());
                        boolean bePropertyCell = RoleUtils.getInstance().roleBeTarget(Constants.RoleType.NEIGHBORHOODS, userEntity.getRole());
                        entity.setBePropertyCell(bePropertyCell);
                        Observable<PropertyIdentityEntity> identityEntityObservable;
                        if (bePropertyCell || bePropertyCompany) {
                            identityEntityObservable = Observable.just(null);
                        } else {
                            identityEntityObservable = getApi().propertyIdentityInfo();
                        }
                        return identityEntityObservable;
                    }
                })
                .flatMap(new Func1<PropertyIdentityEntity, Observable<PropertyInfoEntity>>() {
                    @Override
                    public Observable<PropertyInfoEntity> call(PropertyIdentityEntity propertyIdentityEntity) {
                        Observable<PropertyInfoEntity> observable;
                        if (entity.isBePropertyCell() || entity.isBePropertyCompany() || (propertyIdentityEntity != null && TextUtils.equals(Constants.IDENTITY_STATUS.AUTHED, propertyIdentityEntity.getStatus()))) {
                            observable = getApi().propertyInfo();
                        } else {
                            observable = Observable.just(null);
                        }
                        entity.setIdentity(propertyIdentityEntity);
                        return observable;
                    }
                })
                .flatMap(new Func1<PropertyInfoEntity, Observable<PropertyEntity>>() {
                    @Override
                    public Observable<PropertyEntity> call(PropertyInfoEntity propertyInfoEntity) {
                        entity.setInfo(propertyInfoEntity);
                        return Observable.just(entity);
                    }
                });
        return addSchedulers(observable);
    }

    public Observable<PageListEntity<IncomeStatisticsEntity>> propertyIncomeStatistics(String filter, int page) {
        return addSchedulers(getApi().propertyIncomeStatistics(filter, page));
    }

    public Observable<PageListEntity<IncomeStatisticsEntity>> propertyEarningsDetails(long startTime
            , long endTime, int page) {
        return addSchedulers(getApi().propertyEarningsDetails(startTime, endTime, page));
    }

    public Observable<PageListEntity<SettlementEntity>> propertySettlementList(String type, int page) {
        return addSchedulers(getApi().settlementList(type, page));
    }

    public Observable<PropertyInfomationInfo> propertyInfomationList(int page) {
        Observable<PropertyInfomationInfo> observable;
        if (page == 1) {
            observable = Observable.zip(getApi().propertyInfomationList(page), getApi().freeNumber()
                    , new Func2<PageListEntity<OrderDetailsEntity>, DataEntity<Integer>, PropertyInfomationInfo>() {
                        @Override
                        public PropertyInfomationInfo call(PageListEntity<OrderDetailsEntity> orderDetailsEntityPageListEntity, DataEntity<Integer> integerDataEntity) {
                            return new PropertyInfomationInfo(integerDataEntity.getData(), orderDetailsEntityPageListEntity);
                        }
                    });
        } else {
            observable = getApi().propertyInfomationList(page)
                    .flatMap(new Func1<PageListEntity<OrderDetailsEntity>, Observable<PropertyInfomationInfo>>() {
                        @Override
                        public Observable<PropertyInfomationInfo> call(PageListEntity<OrderDetailsEntity> orderDetailsEntityPageListEntity) {
                            return Observable.just(new PropertyInfomationInfo(null, orderDetailsEntityPageListEntity));
                        }
                    });
        }
        return addSchedulers(observable);
    }

    public Observable<String> createPropertyInfomation(String content, String deviceIds, long startTime, long endTime) {
        return addSchedulers(getApi().createPropertyInfomation(content, deviceIds, startTime, endTime));
    }

    public Observable<List<PointItemEntity>> propertyDeviceList() {
        return addSchedulers(getApi().propertyDeviceList());
    }

    //@(?:Field|Query)\([\s\S].*?\)
    //@(?:GET|POST)\([\s\S].*?\)
    //


    /**
     * 获取收益记录 详情  总收益 收入 支出等
     *
     * @param date
     * @return
     */
    public Observable<IncomeOrPayEntity> getIncomeAndPayByDate(String date) {
        return addSchedulers(getApi().getIncomeAndPayByDate(date));
    }


    /**
     * 获取月收入 或者支出的数据 填充图表
     *
     * @param date
     * @return
     */
    public Observable<List<MonthOrDayIncomeOrPayEntity>> getMonthIncomeOrPayByDate(String type, String date) {
        return addSchedulers(getApi().getMonthIncomeAndPayByDate(type, date));
    }

    /**
     * 获取年收入 或者支出的数据 填充图表
     *
     * @param date
     * @return
     */
    public Observable<List<MonthOrDayIncomeOrPayEntity>> getYearIncomeOrPayByDate(String type, String date) {
        return addSchedulers(getApi().getYearIncomeAndPayByDate(type, date));
    }

}


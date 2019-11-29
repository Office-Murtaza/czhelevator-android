package com.kingyon.elevator.nets;

import com.kingyon.elevator.entities.*;
import com.leo.afbaselibrary.nets.entities.DataEntity;
import com.leo.afbaselibrary.nets.entities.PageListEntity;
import com.leo.afbaselibrary.nets.entities.WxPayEntity;

import java.util.List;
import java.util.Map;

import retrofit2.http.Field;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by gongli on 2017/7/17 17:39
 * email: lc824767150@163.com
 */
public interface NetApi {
    String socketDomainName = "ws://219.151.9.234:8282";

    //    String domainName = "http://rap2api.taobao.org/app/mock/121571/";
    //String domainName = "http://api.pddtv.cn/";//外网正式服地址
    //String domainName = "http://pddapi.scjiazhidao.com/";//外网测试服地址
    String domainName = "http://192.168.1.16:1510/";  //公司测试服
    //    String domainName = "http://192.168.0.50:1510/";
//    String domainName = "http://192.168.0.86:1510/"; //白小川
    //    String domainName = "http://192.168.0.104:2510/";//代国伟
    String baseUrl = domainName;
    //    String rapUrl = "http://ky-rap2-server.i-te.cn/app/mock/17/";
    String rapUrl = "http://rap2api.taobao.org/app/mock/121571/";

    //静态/通用
    @GET("common/getQiNiu")
    Observable<UploadParamsEnitty> getUploadToken();

    @POST("common/bindPushId")
    @FormUrlEncoded
    Observable<String> bindPushId(@Field("pushId") String pushId, @Field("deviceType") String deviceType);

    @POST("common/getRichText")
    @FormUrlEncoded
    Observable<DataEntity<String>> richText(@Field("type") String type);

    @GET("common/getLatestVersion")
    Observable<VersionEntity> getLatestVersion(@Query("platform") String platform, @Query("versionCode") int versionCode);

    @POST("common/incomeList")
    @FormUrlEncoded
    Observable<PageListEntity<IncomeRecordEntity>> incomeRecordsList(@Field("type") String type
            , @Field("deviceId") Long deviceId, @Field("billSn") String billSn
            , @Field("role") String role, @Field("page") int page);

    @GET("common/cellDeviceList")
    Observable<List<PointItemEntity>> cellDeviceList(@Query("cellId") long cellId);

    @GET("common/cellDevicesNumber")
    Observable<List<CellDeviceNumberEntity>> cellDevicesNumber();

    @GET("common/devicesNumber")
    Observable<DeviceNumberEntity> devicesNumber();

    @GET("common/deviceDetails")
    Observable<PointItemEntity> deviceDetails(@Query("deviceId") long deviceId);

    @POST("common/repairDevice")
    @FormUrlEncoded
    Observable<String> repairDevice(@Field("deviceId") long deviceId, @Field("reasonId") Long reasonId
            , @Field("remarks") String remarks, @Field("images") String images);

    @GET("common/repairReasons")
    Observable<List<NormalElemEntity>> repairReasons();

    @POST("common/addDevice")
    @FormUrlEncoded
    Observable<String> addDevice(@Field("objectId") Long objectId, @Field("deviceNo") String deviceNo
            , @Field("deviceLocation") String deviceLocation, @Field("deviceType") String deviceType
            , @Field("cellId") long cellId, @Field("buildId") long buildId
            , @Field("unitId") long unitId, @Field("liftId") long liftId
            , @Field("cameraBrand") Long cameraBrand, @Field("cameraIp") String cameraIp);

    @POST("common/addCell")
    @FormUrlEncoded
    Observable<String> addCell(@Field("objctId") Long objectId, @Field("adcode") String adcode
            , @Field("address") String address, @Field("cellName") String cellName
            , @Field("cellType") String cellType, @Field("humanTraffic") Long humanTraffic
            , @Field("cellLogo") String cellLogo, @Field("cellBanner") String cellBanner
            , @Field("longitude") double longitude, @Field("latitude") double latitude);

    @POST("common/deviceList")
    @FormUrlEncoded
    Observable<PageListEntity<PointItemEntity>> installerDeviceList(@Field("page") int page);

    @GET("common/advertising")
    Observable<AdvertisionEntity> getAdertising();

//    @GET("common/liftCamera")
//    Observable<CameraEntity> liftCamera(@Query("liftId") long liftId);

    @GET("common/cameraBrandInfo")
    Observable<List<CameraBrandEntity>> cameraBrandInfo();

    //用户
    @POST("user/login")
    @FormUrlEncoded
    Observable<LoginResultEntity> login(@Field("way") String way, @Field("unique") String unique
            , @Field("userName") String userName, @Field("password") String password);

    @POST("user/register")
    @FormUrlEncoded
    Observable<LoginResultEntity> register(@Field("way") String way
            , @Field("phone") String phone, @Field("vaildCode") String vaildCode, @Field("password") String password
            , @Field("unique") String unique, @Field("avatar") String avatar, @Field("nickName") String nickName);

    @POST("user/unbindPhone")
    @FormUrlEncoded
    Observable<String> unbindPhone(@Field("phone") String phone, @Field("code") String code
            , @Field("type") String type);

    @POST("user/sendCheckCode")
    @FormUrlEncoded
    Observable<String> getVerifyCode(@Field("phone") String phone, @Field("type") String type);

    @POST("user/resetPassword")
    @FormUrlEncoded
    Observable<String> resetPassword(@Field("phone") String phone
            , @Field("vaildCode") String vaildCode, @Field("newPassword") String newPassword);

    @POST("user/changePassword")
    @FormUrlEncoded
    Observable<String> changePassword(@Field("oldPassword") String oldPassword, @Field("newPasswrod") String newPasswrod);

    @POST("user/logout")
    Observable<String> logout();

    //首页
    @GET("home/banners")
    Observable<List<BannerEntity>> homepageBanners();

    @GET("home/liftknowledges")
    Observable<List<NormalOptionEntity>> liftknowledges();

    @POST("home/knowledgeDetils")
    @FormUrlEncoded
    Observable<DataEntity<String>> knowledgeDetils(@Field("objectId") long objectId);

    @POST("home/recommendAds")
    @FormUrlEncoded
    Observable<PageListEntity<ADEntity>> recommendAds(@Field("page") int page);

    @POST("home/nearlyCell")
    @FormUrlEncoded
    Observable<List<CellItemEntity>> nearlyCell(@Field("longitude") double longitude, @Field("latitude") double latitude);

    @POST("home/searchCell")
    @FormUrlEncoded
    Observable<PageListEntity<CellItemEntity>> searchCell(@Field("keyWord") String keyWord, @Field("cityId") String cityId
            , @Field("distance") String distance, @Field("areaId") String areaId
            , @Field("cellType") String cellType, @Field("page") int page
            , @Field("longitude") Double longitude, @Field("latitude") Double latitude);

    @POST("home/announcementList")
    Observable<List<AnnouncementEntity>> announcementList();

    //小区&点位
    @POST("cell/cityCellNums")
    Observable<List<CityCellEntity>> cityCellNums();

    @POST("cell/cellDetails")
    @FormUrlEncoded
    Observable<CellDetailsEntity> cellDetails(@Field("objectId") long objectId);

    @POST("point/plansList")
    @FormUrlEncoded
    Observable<PageListEntity<CellItemEntity>> plansList(@Field("type") String type, @Field("startTime") Long startTime
            , @Field("endTime") Long endTime, @Field("page") int page);

    @POST("point/planCellsPoinList")
    @FormUrlEncoded
    Observable<List<PointItemEntity>> planCellsPoinList(@Field("cellId") long cellId, @Field("type") String type
            , @Field("startTime") long startTime, @Field("endTime") long endTime);

    @POST("point/plansAdd")
    @FormUrlEncoded
    Observable<String> plansAdd(@Field("planId") Long planId
            , @Field("planName") String planName, @Field("type") String type);

    @POST("point/plansDelete")
    @FormUrlEncoded
    Observable<String> plansDelete(@Field("planIds") String planIds);

    @POST("point/plansAddCells")
    @FormUrlEncoded
    Observable<String> plansAddCells(@Field("type") String type, @Field("cells") String cells);

    @POST("point/plansRemoveCells")
    @FormUrlEncoded
    Observable<String> plansRemoveCells(@Field("type") String type, @Field("cells") String cells);

    @POST("cell/getBuildByCell")
    @FormUrlEncoded
    Observable<List<NormalElemEntity>> getBuildByCell(@Field("objectId") long objectId);

    @POST("cell/getUnitByBuild")
    @FormUrlEncoded
    Observable<List<NormalElemEntity>> getUnitByBuild(@Field("objectId") long objectId);

    @POST("cell/getLiftByUnit")
    @FormUrlEncoded
    Observable<List<LiftElemEntity>> getLiftByUnit(@Field("objectId") long objectId);

    @POST("cell/addBuilding")
    @FormUrlEncoded
    Observable<String> addBuilding(@Field("objectId") Long objectId, @Field("cellId") long cellId, @Field("name") String name);

    @POST("cell/addUnit")
    @FormUrlEncoded
    Observable<String> addUnit(@Field("objectid") Long objectid, @Field("buldingId") long buldingId, @Field("name") String name);

    @POST("cell/addLift")
    @FormUrlEncoded
    Observable<String> addLift(@Field("objectId") Long objectId, @Field("unitId") long unitId, @Field("sn") String sn
            , @Field("name") String name, @Field("max") int max, @Field("negative") int negative, @Field("base") int base);

    @POST("cell/removeCell")
    @FormUrlEncoded
    Observable<String> removeCell(@Field("objectId") long objectId);

    @GET("cell/removeBuilding")
    Observable<String> removeBuilding(@Query("objectId") long objectId);

    @GET("cell/removeBuilding")
    Observable<String> removeUnit(@Query("objectId") long objectId);

    @POST("cell/removeLift")
    @FormUrlEncoded
    Observable<String> removeLift(@Field("objectId") long objectId);

    //订单
    @POST("order/commitOrder")
    @FormUrlEncoded
    Observable<CommitOrderEntiy> commitOrder(@Field("type") String type, @Field("startTime") long startTime
            , @Field("endTime") long endTime
            , @Field("adId") Long adId, @Field("deviceParams") String deviceParams
            , @Field("coupons") String coupons, @Field("adIndustry") long adIndustry);

    @POST("order/orderDetatils")
    @FormUrlEncoded
    Observable<OrderDetailsEntity> orderDetatils(@Field("orderId") long orderId);

    @POST("order/getIdentityInfo")
    Observable<OrderIdentityEntity> orderIdentityInfo();

    @POST("order/orderPay")
    @FormUrlEncoded
    Observable<WxPayEntity> orderPay(@Field("orderId") long orderId, @Field("way") String way);

    @POST("order/orderList")
    @FormUrlEncoded
    Observable<PageListEntity<OrderDetailsEntity>> orderList(@Field("status") String status, @Field("page") int page);

    @POST("order/downloadContract")
    Observable<DataEntity<String>> downloadContract();

    @POST("order/orderPoints")
    @FormUrlEncoded
    Observable<List<PointItemEntity>> orderPoints(@Field("orderId") long orderId);

    @GET("order/downAd")
    Observable<String> downAd(@Query("orderId") long orderId, @Query("tagReasonId") long tagReasonId
            , @Query("undercastRemarks") String undercastRemarks);

    @POST("order/adPlayList")
    @FormUrlEncoded
    Observable<PageListEntity<AdDetectingEntity>> adPlayList(@Field("orderId") long orderId
            , @Field("deviceId") long deviceId, @Field("page") int page);

    @POST("order/downAdTags")
    Observable<List<NormalElemEntity>> downAdTags();

    @POST("order/orderCancel")
    @FormUrlEncoded
    Observable<String> orderCancel(@Field("orderId") long orderId);

    @POST("order/orderDelete")
    @FormUrlEncoded
    Observable<String> orderDelete(@Field("orderId") long orderId);

    //广告
    @POST("ad/createOrEidtAd")
    @FormUrlEncoded
    Observable<ADEntity> createOrEidtAd(@Field("objectId") Long objectId, @Field("onlyInfo") boolean onlyInfo
            , @Field("planType") String planType
            , @Field("screenType") String screenType, @Field("title") String title
            , @Field("videoUrl") String videoUrl, @Field("imageUrl") String imageUrl
            , @Field("bgMusic") String bgMusic, @Field("duration") Long duration);

    @POST("ad/adlist")
    @FormUrlEncoded
    Observable<PageListEntity<ADEntity>> myAdList(@Field("planType") String planType, @Field("page") int page);

    @POST("ad/deleteAd")
    @FormUrlEncoded
    Observable<String> deleteAd(@Field("objectId") long objectId);

    @POST("ad/getPicAdTemplet")
    @FormUrlEncoded
    Observable<PageListEntity<AdTempletEntity>> getPicAdTemplet(@Field("screenSplit") String screenSplit
            , @Field("sort") String sort, @Field("type") Long type, @Field("page") int page);

    @POST("ad/getAdTempletType")
    Observable<List<NormalElemEntity>> getAdTempletType();

    @POST("ad/getMaterial")
    @FormUrlEncoded
    Observable<PageListEntity<MateriaEntity>> getMaterials(@Field("planType") String planType, @Field("screenSplit") String screenSplit
            , @Field("type") String type, @Field("page") int page);

    //个人中心
    @POST("user/getMeetCoupons")
    @FormUrlEncoded
    Observable<List<CouponItemEntity>> getMeetCoupons(@Field("price") float price, @Field("type") String type);

    @POST("user/getCoupons")
    @FormUrlEncoded
    Observable<PageListEntity<CouponItemEntity>> getCoupons(@Field("status") String status, @Field("page") int page);

    @POST("user/donateCoupons")
    @FormUrlEncoded
    Observable<String> donateCoupons(@Field("phone") String phone, @Field("count") String count, @Field("couponIds") String couponIds);

    @POST("user/getIdentityInformation")
    Observable<IdentityInfoEntity> getIdentityInformation();

    @POST("user/industrys")
    Observable<List<IndustryEntity>> getIndustrys();

    @POST("user/identityAuth")
    @FormUrlEncoded
    Observable<String> identityAuth(@Field("type") String type, @Field("personName") String personName
            , @Field("idNum") String idNum, @Field("idFace") String idFace
            , @Field("idBack") String idBack, @Field("companyName") String companyName
            , @Field("businessCert") String businessCert);

    @POST("user/userProfile")
    Observable<UserEntity> userProfile();

    @POST("user/userEidtProfile")
    @FormUrlEncoded
    Observable<UserEntity> userEidtProfile(@FieldMap Map<String, String> params);

    @POST("user/myWallet")
    Observable<DataEntity<Float>> myWallet();

    @POST("user/myWalletList")
    @FormUrlEncoded
    Observable<PageListEntity<WalletRecordEntity>> myWalletRecords(@Field("page") int page);

    @POST("user/rechageWallet")
    @FormUrlEncoded
    Observable<WxPayEntity> rechageWallet(@Field("way") String way, @Field("amount") float amount);

    @POST("user/invoiceInfo")
    Observable<InvoiceInfoEntity> invoiceInfo();

    @POST("user/createInvoice")
    @FormUrlEncoded
    Observable<String> createInvoice(@Field("invoiceType") String invoiceType
            , @Field("invoiceStart") String invoiceStart, @Field("invoiceNo") String invoiceNo
            , @Field("bank") String bank, @Field("invoiceAmount") float invoiceAmount
            , @Field("receiveEmail") String receiveEmail, @Field("content") String content);

    @POST("user/invoiceList")
    @FormUrlEncoded
    Observable<PageListEntity<InvoiceEntity>> invoiceList(@Field("page") int page);

    @POST("user/recommedInfo")
    Observable<RecommendInfoEntity> recommedInfo();

    @POST("user/recommendList")
    @FormUrlEncoded
    Observable<PageListEntity<UserEntity>> recommendList(@Field("page") int page);

    @POST("user/myCollects")
    @FormUrlEncoded
    Observable<PageListEntity<CellItemEntity>> myCollects(@Field("page") int page);

    @POST("user/collectCell")
    @FormUrlEncoded
    Observable<String> collectCell(@Field("cellId") long cellId);

    @POST("user/cancelCollect")
    @FormUrlEncoded
    Observable<String> cancelCollect(@Field("cellIds") String cellIds);

    @POST("user/myFeedBackList")
    @FormUrlEncoded
    Observable<PageListEntity<FeedBackEntity>> myFeedBackList(@Field("page") int page);

    @POST("user/feedBackDetail")
    @FormUrlEncoded
    Observable<PageListEntity<FeedBackMessageEntity>> feedBackDetail(@Field("objectId") long objectId, @Field("page") int page);

    @POST("user/createFeedBack")
    @FormUrlEncoded
    Observable<String> createFeedBack(@Field("titile") String titile, @Field("images") String images);

    @POST("user/commentFeedBack")
    @FormUrlEncoded
    Observable<String> commentFeedBack(@Field("objectId") long objectId, @Field("content") String content);

    @POST("user/getFeedBack")
    @FormUrlEncoded
    Observable<FeedBackEntity> getFeedBack(@Field("objectId") long objectId);

    @POST("user/unReadCount")
    Observable<UnreadNumberEntity> unreadCount();

    @POST("order/publishFailed")
    Observable<OrderFailedNumberEntity> orderPublishFailedNum();

    @POST("user/getMessageList")
    @FormUrlEncoded
    Observable<PageListEntity<NormalMessageEntity>> getMessageList(@Field("type") String type, @Field("page") int page);

    @POST("user/deleteMessage")
    @FormUrlEncoded
    Observable<String> deleteMessage(@Field("messageId") long messageId);

    @POST("user/messageRead")
    @FormUrlEncoded
    Observable<String> messageRead(@Field("messageId") long messageId);

    //合伙人
    @POST("partner/getInfo")
    Observable<CooperationInfoEntity> cooperationInfo();

    //新版合伙人查询接口
    @POST("partner/getPartnerInfo")
    Observable<CooperationInfoNewEntity> cooperationInfoNew();

    @GET("partner/applyStatus")
    Observable<CooperationIdentityEntity> cooperationIentityInfo();


    /**
     * 获取收益记录里的总收益  收入 支出三个数据
     *
     * @param date
     * @return
     */
    @POST("partner/getIncomeAndPayByDate")
    @FormUrlEncoded
    Observable<IncomeOrPayEntity> getIncomeAndPayByDate(@Field("date") String date);


    /**
     * 获取月支出 收入数据
     *
     * @param date
     * @return
     */
    @POST("partner/getIncomePayDataPerDay")
    @FormUrlEncoded
    Observable<List<MonthOrDayIncomeOrPayEntity>> getMonthIncomeAndPayByDate(@Field("type") String type, @Field("date") String date);

    /**
     * 获取年收入 支出数据
     *
     * @param date
     * @return
     */
    @POST("partner/getIncomePayDataPerMonth")
    @FormUrlEncoded
    Observable<List<MonthOrDayIncomeOrPayEntity>> getYearIncomeAndPayByDate(@Field("type") String type, @Field("date") String date);


    @POST("partner/apply")
    @FormUrlEncoded
    Observable<String> cooperationApply(@Field("partnerName") String partnerName, @Field("phone") String phone, @Field("city") String city);

    @POST("partner/withdraw")
    @FormUrlEncoded
    Observable<String> partnerWithdraw(@Field("amount") double amount, @Field("withDrawWay") String withDrawWay
            , @Field("aliAcount") String aliAcount, @Field("bankName") String bankName
            , @Field("cardNo") String cardNo, @Field("cardholder") String cardholder);

    @POST("partner/withdrawList")
    @FormUrlEncoded
    Observable<PageListEntity<WithdrawItemEntity>> partnerWithdrawList(@Field("page") int page);

    @POST("partner/myCellList")
    @FormUrlEncoded
    Observable<PageListEntity<CellItemEntity>> partnerCellList(@Field("longitude") Double longitude
            , @Field("latitude") Double latitude, @Field("page") int page);

//    @POST("partner/myCellDevices")
//    @FormUrlEncoded
//    Observable<List<PointItemEntity>> partnerCellDevices(@Field("cellId") long cellId);

    @GET("partner/incomeRecords")
    Observable<PageListEntity<IncomeStatisticsEntity>> partnerIncomeStatistics(@Query("filter") String filter, @Query("page") int page);

    @GET("partner/incomeStatistics")
    Observable<PageListEntity<IncomeStatisticsEntity>> partnerEarningsDetails(@Query("startTime") long startTime
            , @Query("endTime") long endTime, @Query("page") int page);

    @GET("partner/propertyFeeRecords")
    Observable<PageListEntity<IncomeStatisticsEntity>> partnerPropertyFeeRecords(@Query("page") int page);

    @GET("partner/opticalFeeRecords")
    Observable<PageListEntity<IncomeStatisticsEntity>> partnerOpticalFeeRecords(@Query("page") int page);

    //物业
    @POST("property/apply")
    @FormUrlEncoded
    Observable<String> propertyApply(@Field("name") String name, @Field("contact") String contact
            , @Field("phone") String phone, @Field("image") String image);

    @GET("property/applyStatus")
    Observable<PropertyIdentityEntity> propertyIdentityInfo();

    @POST("property/info")
    Observable<PropertyInfoEntity> propertyInfo();

    @GET("property/incomeRecords")
    Observable<PageListEntity<IncomeStatisticsEntity>> propertyIncomeStatistics(@Query("filter") String filter, @Query("page") int page);

    @GET("property/incomeStatistics")
    Observable<PageListEntity<IncomeStatisticsEntity>> propertyEarningsDetails(@Query("startTime") long startTime
            , @Query("endTime") long endTime, @Query("page") int page);

    @POST("property/settlementList")
    @FormUrlEncoded
    Observable<PageListEntity<SettlementEntity>> settlementList(@Field("type") String type, @Field("page") int page);

    @POST("property/convenientList")
    @FormUrlEncoded
    Observable<PageListEntity<OrderDetailsEntity>> propertyInfomationList(@Field("page") int page);

    @POST("property/createConvenient")
    @FormUrlEncoded
    Observable<String> createPropertyInfomation(@Field("content") String content, @Field("deviceIds") String deviceIds
            , @Field("startTime") long startTime, @Field("endTime") long endTime);

    @GET("property/freeNumber")
    Observable<DataEntity<Integer>> freeNumber();

    @POST("property/deviceList")
    Observable<List<PointItemEntity>> propertyDeviceList();
}
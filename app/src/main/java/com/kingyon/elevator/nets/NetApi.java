package com.kingyon.elevator.nets;

import com.blankj.utilcode.util.AppUtils;
import com.kingyon.elevator.entities.*;
import com.kingyon.elevator.entities.entities.AttenionUserEntiy;
import com.kingyon.elevator.entities.entities.CityFacilityInfoEntiy;
import com.kingyon.elevator.entities.entities.CodeEntity;
import com.kingyon.elevator.entities.entities.CommentListEntity;
import com.kingyon.elevator.entities.entities.ConentEntity;
import com.kingyon.elevator.entities.entities.ConentOdjerEntity;
import com.kingyon.elevator.entities.entities.HomeTopicConentEntity;
import com.kingyon.elevator.entities.entities.HomeTopicEntity;
import com.kingyon.elevator.entities.entities.PointClassicEntiy;
import com.kingyon.elevator.entities.entities.QueryRecommendEntity;
import com.kingyon.elevator.entities.entities.QueryRecommendTopEntity;
import com.kingyon.elevator.entities.entities.QueryTopicEntity;
import com.kingyon.elevator.entities.entities.RecommendHouseEntiy;
import com.kingyon.elevator.entities.entities.TopicLabelEntity;
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
    String socketDomainName = "wss://gate.tlwgz.com:8282";
    String domainReleaseName = "https://api.pddtv.cn/";//外网正式服地址
//    1.0测试接口
//    String domainDebugName = "http://47.96.105.139:1510/";  //公司测试服
//    2.0测试接口
    String domainDebugName = "http://192.168.1.166:8080/app/v2/";  //公司测试服
//    String domainDebugName = "http://192.168.1.32:8080/app/v2/";  //公司测试服
//    String domainDebugName = "http://192.168.1.190:1510/";  //公司测试服

    String baseUrl = AppUtils.isAppDebug() ? domainDebugName : domainReleaseName;
    //    String rapUrl = "http://ky-rap2-server.i-te.cn/app/mock/17/";
    String rapUrl = "http://rap2api.taobao.org/app/mock/121571/";
//2.0
    /*2.0验证码*/
    @POST("userSecurity/sendCheckCode")
    @FormUrlEncoded
    Observable<String> getSendCheckCode(@Field("type") String type, @Field("phone") String phone);

    /*2.0登录*/
    @POST("userSecurity/login")
    @FormUrlEncoded
    Observable<
            CodeEntity> getLogin(@Field("phone") String phone,@Field("password") String password
            ,@Field("way")String way,@Field("unique")String unique,@Field("avatar") String avatar,@Field("nickName") String nickName);

    /*2.0设置密码*/
    @POST("userSecurity/setPassword")
    @FormUrlEncoded
    Observable<CodeEntity> getPassswordSetting(@Field("phone") String phone,@Field("password") String password,@Field("inviter") String inviter);

    /*2.0绑定手机*/
    @POST("userSecurity/bindPhone")
    @FormUrlEncoded
    Observable<CodeEntity> getBindPhone(@Field("phone") String phone, @Field("verifyCode") String verifyCode,
                                    @Field("unique") String unique, @Field("avatar") String avatar,
                                    @Field("nickName") String nickName);
    /*2.0验证码验证*/
    @POST("userSecurity/checkVerifyCode")
    @FormUrlEncoded
    Observable<CodeEntity> getCheckVerifyCode (@Field("phone") String phone,@Field("verifyCode") String verifyCode);

    /*2.0忘记密码*/
    @POST("userSecurity/resetPassword")
    @FormUrlEncoded
    Observable<String> getResetPassword (@Field("phone") String phone,@Field("verifyCode") String verifyCode,@Field("newPassword") String newPassword );

    /*2.0获取话题*/
    @POST("topic/queryTopic")
    @FormUrlEncoded
    Observable<ConentEntity<QueryTopicEntity.PageContentBean>>
    getOueryTopic(@Field("page") int page,
                  @Field("title") String title,@Field("label") int label);

    /*2.0静态/通用获取七牛云参数*/
    @GET("common/getQiNiu")
    Observable<UploadParamsEnitty> getUploadToken();

    /*2.0获取话题栏目*/
    @POST("topic/queryTopicLabel")
    Observable<TopicLabelEntity<TopicLabelEntity.PageContentBean>> getTopicLabel();

    /*2.0内容发布*/
    @POST("content/contentPublish")
    @FormUrlEncoded
    Observable<String> getContentPublish(@Field("title") String title,@Field("content") String content,
                                         @Field("image") String image,@Field("video") String video,
                                         @Field("type") String type ,@Field("combination") String combination ,
                                         @Field("topicId") String topicId ,@Field("atAccount") String atAccount,
                                         @Field("videoSize") int videoSize,@Field("videoCover") String videoCover,
                                         @Field("playTime")long playTime,@Field("videoHorizontalVertical") int videoHorizontalVertical);

    /*2.0置顶内容*/
    @POST("content/queryRecommendTop")
    Observable<ConentEntity<QueryRecommendTopEntity>> getQueryRecommendTop();

    /*2.0推荐内容*/
    @POST("content/queryRecommend")
    @FormUrlEncoded
    Observable<ConentEntity<QueryRecommendEntity>> getQueryRecommend(@Field("page") int page ,@Field("title") String title);

    /*2.0获取关注内容*/
    @POST("content/queryAttention")
    @FormUrlEncoded
    Observable<ConentEntity<QueryRecommendEntity>> getQueryAttention(@Field("page") int page,@Field("title") String title,@Field("orderBy") String orderBy);

    /*2.0内容点赞*/
    @POST("content/handlerLikeOrNot")
    @FormUrlEncoded
    Observable<String> getLikeNot(@Field("objId") String objId ,@Field("likeType") String likeType,@Field("handleType") String handleType);

    /*2.0首页话题栏目*/
    @POST("topic/queryTopicLabel")
    Observable<ConentEntity<HomeTopicEntity>> getQueryTopicLabel();

    /*2.0话题查询*/
    @POST("topic/queryTopic")
    @FormUrlEncoded
    Observable<ConentEntity<HomeTopicConentEntity>> getQueryTopicConetn(@Field("page") int page
            ,@Field("label") int label,@Field("title") String title,@Field("id") String id);


    /*2.0内容点赞*/
    @POST("content/handlerLikeOrNot")
    @FormUrlEncoded
    Observable<String> getHandlerLikeOrNot(@Field("objId") int objId,
                                           @Field("likeType") String likeType,
                                           @Field("handleType") String handleType );
    /*2.0删除内容*/
    @POST("content/delContent")
    @FormUrlEncoded
    Observable<String> getDelContent(@Field("contentId") int contentId);

    /*2.0内容举报*/
    @POST("content/report")
    @FormUrlEncoded
    Observable<String> getReport(@Field("objId") int objId,@Field("reportType") String reportType,
                                 @Field("reportContent") String reportContent );

    /*2.0内容评论*/
    @POST("content/comment")
    @FormUrlEncoded
    Observable<String> getComment(@Field("contentId") int contentId,@Field("parentId") int parentId ,
                                  @Field("comment") String comment);

    /*2.0添加浏览数*/
    @POST("content/addBrowseTime")
    @FormUrlEncoded
    Observable<String> getAddBrowse(@Field("contentId") int contentId );

    /*2.0获取内容评论*/
    @POST("content/queryComment")
    @FormUrlEncoded
    Observable<ConentEntity<CommentListEntity>> getQueryListComment(@Field("page") int page , @Field("contentId") int contentId );

    /*2.0获取子级评论*/
    @POST("content/queryCommentByParentId")
    @FormUrlEncoded
    Observable<ConentEntity<CommentListEntity>> getCommentBy(@Field("page") int page
            , @Field("contentId") int contentId, @Field("parentId") int parentId );

    /*2.0用户关注*/
    @POST("user/addOrCancelAttention")
    @FormUrlEncoded
    Observable<String> getAddAttention(@Field("handlerType") String handlerType,@Field("beFollowerAccount") String beFollowerAccount);

    /*2.0获取用户*/
    @POST("user/getAttention")
    @FormUrlEncoded
    Observable<ConentEntity<AttenionUserEntiy>> getAttention(@Field("page") int page, @Field("handlerType") String handlerType);

    /*2.0话题内容*/
    @POST("topic/queryTopicAttention")
    @FormUrlEncoded
    Observable<ConentEntity<QueryRecommendEntity>> getTopicAttention(@Field("page") int page,@Field("topicId") int topicId ,
                                         @Field("orderBy") String orderBy);


    /*2.0投放首页*/
    @POST("throwIn/cityFacilityInfo")
    @FormUrlEncoded
    Observable<ConentOdjerEntity> getCityFacility(@Field("provinceCode") String provinceCode, @Field("cityCode") String cityCode ,
                                                                         @Field("countyCode") String countyCode);
    /*2.0获取推荐小区*/
    @POST("throwIn/getRecommendHouse")
    @FormUrlEncoded
    Observable<ConentEntity<RecommendHouseEntiy>> getRecommendHouse(@Field("page") int page , @Field("latitude") String latitude ,
                                                                    @Field("longitude") String longitude , @Field("cityId") int cityId,
                                                                    @Field("areaId") String areaId, @Field("pointType") String pointType,
                                                                    @Field("keyWord") String keyWord, @Field("distance") String distance);

    /*2.0评论删除*/
    @POST("content/delComment")
    @FormUrlEncoded
    Observable<String> getDelcomment(@Field("commentId") int commentId );

    /*2.0添加编辑计划*/
//    @POST("point/plansAddOrModify")
//    @FormUrlEncoded
//    Observable<String> getPlasAddOrModify(@Field("planId") int planId,@Field("planName") String planName,@Field("type") String type);

    @POST("point/plansAddOrModify")
    @FormUrlEncoded
    Observable<String> plansAdd(@Field("planId") Long planId
            , @Field("planName") String planName, @Field("type") String type);

    /*2.0点位筛选内容*/
    @POST("throwIn/getPointClassic")
    Observable<ConentEntity<PointClassicEntiy>> getPointClassic();

    /*2.0计划列表*/
    @POST("point/findAdPlanList")
    @FormUrlEncoded
    Observable<ConentEntity<CellItemEntity>> plansList(@Field("type") String type, @Field("startTime") Long startTime
            , @Field("endTime") Long endTime, @Field("page") int page);


    /*2.0小区单独指定*/
    @POST("point/findPlansCellsPointList")
    @FormUrlEncoded
    Observable<List<PointItemEntity>> planCellsPoinList(@Field("cellId") long cellId, @Field("type") String type
            , @Field("startTime") long startTime, @Field("endTime") long endTime);

    /*2.0小区添加计划*/
    @POST("point/plansAddCells")
    @FormUrlEncoded
    Observable<String> plansAddCells(@Field("type") String type, @Field("cells") String cells);

    /*2.0点位计划删除*/
    @POST("point/plansDelete")
    @FormUrlEncoded
    Observable<String> plansDelete(@Field("planIds") String planIds);

    /*2.0点位计划删除小区*/
    @POST("point/plansRemoveCells")
    @FormUrlEncoded
    Observable<String> plansRemoveCells(@Field("type") String type, @Field("cells") String cells);

    /*2.0我的广告列表*/
    @POST("ad/adlist")
    @FormUrlEncoded
    Observable<ConentEntity<ADEntity>> myAdList(@Field("planType") String planType, @Field("page") int page);

    /*2.0创建编辑广告*/
    @POST("ad/createOrEidtAd")
    @FormUrlEncoded
    Observable<ADEntity> createOrEidtAd(@Field("objectId") Long objectId, @Field("onlyInfo") boolean onlyInfo
            , @Field("planType") String planType
            , @Field("screenType") String screenType, @Field("title") String title
            , @Field("videoUrl") String videoUrl, @Field("imageUrl") String imageUrl
            , @Field("bgMusic") String bgMusic, @Field("duration") Long duration,@Field("hashCode") String hashCode);

    /*2.0广告删除*/
    @POST("ad/deleteAd")
    @FormUrlEncoded
    Observable<String> deleteAd(@Field("objectId") long objectId);

    /*2.0获取分类模板*/
    @POST("ad/getAdTempletType")
    Observable<List<NormalElemEntity>> getAdTempletType();

    /*2.0获取订单优惠信息*/
    @POST("order/getCouponsInfo")
    @FormUrlEncoded
    Observable<AutoCalculationDiscountEntity> getCouponsInfo(@Field("amount") double amount,
                                                             @Field("type") String type,
                                                             @Field("isManual") Boolean isManual,
                                                             @Field("consIds") String consIds);

    /*2.0获取订单身份认证信息*/
    @POST("order/getIdentityInfo")
    Observable<OrderIdentityEntity> orderIdentityInfo();

    /*2.0订单提交*/
    @POST("order/commitOrder")
    @FormUrlEncoded
    Observable<CommitOrderEntiy> commitOrder(@Field("type") String type, @Field("startTime") long startTime
            , @Field("endTime") long endTime
            , @Field("adId") Long adId, @Field("deviceParams") String deviceParams
            , @Field("coupons") String coupons, @Field("adIndustry") long adIndustry);

    /*2.0查看监播*/
    @POST("order/adPlayList")
    @FormUrlEncoded
    Observable<PageListEntity<AdDetectingEntity>> adPlayList(@Field("orderId") long orderId
            , @Field("deviceId") long deviceId, @Field("page") int page);


    /*2.0申请下播*/
    @GET("order/downAd")
    Observable<String> downAd(@Query("orderId") long orderId, @Query("tagReasonId") long tagReasonId
            , @Query("undercastRemarks") String undercastRemarks);

    /*2.0获取=播原因*/
    @POST("order/downAdTags")
    Observable<List<NormalElemEntity>> downAdTags();

    /*2.0合同下载地址*/
    @POST("order/downloadContract")
    Observable<DataEntity<String>> downloadContract();


    //    1.0
    //静态/通用获取七牛云参数
//    @GET("common/getQiNiu")
//    Observable<UploadParamsEnitty> getUploadToken();
//POST /common/bindPushId绑定推送id
    @POST("common/bindPushId")
    @FormUrlEncoded
    Observable<String> bindPushId(@Field("pushId") String pushId, @Field("deviceType") String deviceType);
//请求富文本接口
    @POST("common/getRichText")
    @FormUrlEncoded
    Observable<DataEntity<String>> richText(@Field("type") String type);
//获取最新版本
    @GET("common/getLatestVersion")
    Observable<VersionEntity> getLatestVersion(@Query("platform") String platform, @Query("versionCode") int versionCode);
//收入明细
    @POST("common/incomeList")
    @FormUrlEncoded
    Observable<PageListEntity<IncomeRecordEntity>> incomeRecordsList(@Field("type") String type
            , @Field("deviceId") Long deviceId, @Field("billSn") String billSn
            , @Field("role") String role, @Field("page") int page);
//物业/合伙人 小区设备列表(add)
    @GET("common/cellDeviceList")
    Observable<List<PointItemEntity>> cellDeviceList(@Query("cellId") long cellId);
//物业/合伙人 小区设备数量列表(add
    @GET("common/cellDevicesNumber")
    Observable<List<CellDeviceNumberEntity>> cellDevicesNumber();
//物业/合伙人 设备数(add
    @GET("common/devicesNumber")
    Observable<DeviceNumberEntity> devicesNumber();
//设备详情(add)
    @GET("common/deviceDetails")
    Observable<PointItemEntity> deviceDetails(@Query("deviceId") long deviceId);
//设备报修
    @POST("common/repairDevice")
    @FormUrlEncoded
    Observable<String> repairDevice(@Field("deviceId") long deviceId, @Field("reasonId") Long reasonId
            , @Field("remarks") String remarks, @Field("images") String images);
//获取设备报修原因列表（add)
    @GET("common/repairReasons")
    Observable<List<NormalElemEntity>> repairReasons();
//添加/修改设备
    @POST("common/addDevice")
    @FormUrlEncoded
    Observable<String> addDevice(@Field("objectId") Long objectId, @Field("deviceNo") String deviceNo
            , @Field("deviceLocation") String deviceLocation, @Field("deviceType") String deviceType
            , @Field("cellId") long cellId, @Field("buildId") long buildId
            , @Field("unitId") long unitId, @Field("liftId") long liftId
            , @Field("cameraBrand") Long cameraBrand, @Field("cameraIp") String cameraIp);
//添加小区&编辑小区
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


    /*2.0订单详情*/
    @POST("order/orderDetatils")
    @FormUrlEncoded
    Observable<OrderDetailsEntity> orderDetatils(@Field("orderId") long orderId);


    /*2.0订单支付*/
    @POST("order/orderPay")
    @FormUrlEncoded
    Observable<WxPayEntity> orderPay(@Field("preOrderSn") long orderId, @Field("way") String way,@Field("payPwd") String payPwd );

    /*2.0订单详情*/
    @POST("order/orderList")
    @FormUrlEncoded
    Observable<PageListEntity<OrderDetailsEntity>> orderList(@Field("status") String status, @Field("page") int page);


    /*2.0查看订单点位列表*/
    @POST("order/orderPoints")
    @FormUrlEncoded
    Observable<List<PointItemEntity>> orderPoints(@Field("orderId") long orderId);


    /*2.0取消订单*/
    @POST("order/orderCancel")
    @FormUrlEncoded
    Observable<String> orderCancel(@Field("orderId") long orderId);

    /*2.0删除订单*/
    @POST("order/orderDelete")
    @FormUrlEncoded
    Observable<String> orderDelete(@Field("orderId") long orderId);





    @POST("ad/getPicAdTemplet")
    @FormUrlEncoded
    Observable<ConentEntity<AdTempletEntity>> getPicAdTemplet(@Field("screenSplit") String screenSplit
            , @Field("sort") String sort, @Field("type") Long type, @Field("page") int page);



    @POST("ad/getMaterial")
    @FormUrlEncoded
    Observable<PageListEntity<MateriaEntity>> getMaterials(@Field("planType") String planType, @Field("screenSplit") String screenSplit
            , @Field("type") String type, @Field("page") int page);

    //个人中心
    @POST("user/getMeetCoupons")
    @FormUrlEncoded
    Observable<List<CouponItemEntity>> getMeetCoupons(@Field("price") float price, @Field("type") String type);

    /*2.0获取我的优惠卷*/
    @POST("user/getCoupons")
    @FormUrlEncoded
    Observable<ConentEntity<CouponItemEntity>> getCoupons(@Field("status") String status, @Field("page") int page);

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

    /*2.0用户信息修改*/
    @POST("user/userEidtProfile")
    @FormUrlEncoded
    Observable<UserEntity> userEidtProfile(@FieldMap Map<String, String> params);

    @POST("user/myWallet")
    Observable<DataEntity<Float>> myWallet();

    @POST("user/myWalletList")
    @FormUrlEncoded
    Observable<PageListEntity<WalletRecordEntity>> myWalletRecords(@Field("page") int page);

    /*2.0充值*/
    @POST("user/rechargeWallet")
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

    /*2.0审核未通过的订单数量*/
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
    Observable<MonthOrDayIncomeOrPayEntity> getMonthIncomeAndPayByDate(@Field("type") String type, @Field("date") String date);

    /**
     * 获取年收入 支出数据
     *
     * @param date
     * @return
     */
    @POST("partner/getIncomePayDataPerMonth")
    @FormUrlEncoded
    Observable<MonthOrDayIncomeOrPayEntity> getYearIncomeAndPayByDate(@Field("type") String type, @Field("date") String date);


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

    /**
     * 根据日期查询合伙人支出详情列表
     *
     * @param startPosition
     * @param size
     * @return
     */
    @POST("partner/getPayDetailedList")
    @FormUrlEncoded
    Observable<List<IncomeDetailsEntity>> getPayDetailedList(@Field("start") String startPosition, @Field("size") String size, @Field("date") String date);


    /**
     * 根据日期查询合伙人收入详情列表
     *
     * @param startPosition
     * @param size
     * @return
     */
    @POST("partner/getIncomeDetailedList")
    @FormUrlEncoded
    Observable<List<IncomeDetailsEntity>> getIncomeDetailedList(@Field("start") String startPosition, @Field("size") String size, @Field("date") String date);


    /**
     * 根据年份和月份查询某一个月的收入或支出详情
     *
     * @param startPosition
     * @param size
     * @return
     */
    @POST("partner/getIncomePayDataDayList")
    @FormUrlEncoded
    Observable<List<IncomeDetailsEntity>> getIncomePayDataDayList(@Field("start") String startPosition, @Field("size") String size, @Field("type") String type, @Field("date") String date);


    /**
     * 查询昨日收益
     *
     * @param startPosition
     * @param size
     * @return
     */
    @POST("partner/getYesterdayIncomeDetailedList")
    @FormUrlEncoded
    Observable<List<YesterdayIncomeEntity>> getYesterdayIncomeDetailedList(@Field("start") String startPosition, @Field("size") String size);

    /**
     * 查询已提现的数据
     *
     * @param startPosition
     * @param size
     * @return
     */
    @POST("partner/getCashedList")
    @FormUrlEncoded
    Observable<List<YesterdayIncomeEntity>> getCashedList(@Field("start") String startPosition, @Field("size") String size);


    /**
     * 查询提现绑定的账号
     *
     * @param startPosition
     * @param size
     * @return
     */
    @POST("partner/cashType/getUserCashTypeList")
    @FormUrlEncoded
    Observable<List<BindAccountEntity>> getUserCashTypeList(@Field("start") String startPosition, @Field("size") String size);

    /**
     * 绑定提现账号
     *
     * @param cashAccount 账号
     * @param cashType    提现类型 1银行卡 2支付宝
     * @param cashName    名字
     * @param openingBank 开户行
     * @return
     */
    @POST("partner/cashType/add")
    @FormUrlEncoded
    Observable<String> addCashAccount(@Field("cashAccount") String cashAccount, @Field("cashType") String cashType
            , @Field("cashName") String cashName, @Field("openingBank") String openingBank);


    /**
     * 初始化支付密码
     *
     * @param password 密码
     * @return
     */
    @POST("user/pay/initPassword")
    @FormUrlEncoded
    Observable<String> initPayPassword(@Field("password") String password);


    /**
     * 原密码的方式修改支付密码
     *
     * @param oldPassword 旧密码
     * @param newPasswrod 新密码
     * @return
     */
    @POST("user/pay/changePasswordByOld")
    @FormUrlEncoded
    Observable<String> changePasswordByOld(@Field("oldPassword") String oldPassword, @Field("newPassword") String newPasswrod);


    /**
     * 验证码的方式修改支付密码
     *
     * @param phone     手机号
     * @param vaildCode 验证码
     * @param password  密码
     * @return
     */
    @POST("user/pay/changePasswordByCode")
    @FormUrlEncoded
    Observable<String> changePasswordByCode(@Field("phone") String phone, @Field("vaildCode") String vaildCode, @Field("password") String password);


    /**
     * 验证是否已经设置支付密码
     *
     * @return
     */
    @POST("user/pay/vaildInit")
    Observable<Boolean> vaildInitPayPwd();


    /**
     * 验证码的方式修改支付密码
     *
     * @param password 支付密码
     * @return
     */
    @POST("user/pay/vaildPassword")
    @FormUrlEncoded
    Observable<String> vaildPasswordIsRight(@Field("password") String password);




    /**
     * 获取弹窗广告数据或者固定位置通知
     *
     * @return
     */
    @GET("common/getTipsList")
    Observable<List<AdNoticeWindowEntity>> getTipsList(@Query("position") String position, @Query("showWay") int showWay);


    /**
     * 加载首页新闻数据列表
     *
     * @return
     */
    @GET("promotion/news/list")
    Observable<List<NewsEntity>> getNewsList(@Query("start") int start, @Query("size") int size, @Query("category") Integer category, @Query("keywords") String keywords);

    /**
     * 获取新闻链接和是否点赞
     *
     * @return
     */
    @GET("promotion/news/getById")
    Observable<NewsDetailsEntity> getNewDetailInfo(@Query("id") int newsId);

    /**
     * 点赞或取消点赞接口
     *
     * @return
     */
    @POST("promotion/news/like")
    @FormUrlEncoded
    Observable<String> setLike(@Field("id") int newsId);


    /**
     * 获取新闻评论列表
     *
     * @return
     */
    @GET("user/comment/list")
    Observable<List<CommentEntity>> getListComment(@Query("id") int newsId, @Query("start") int start, @Query("size") int size, @Query("sort") int sort);


    /**
     * 获取新闻子评论列表
     *
     * @return
     */
    @GET("user/comment/childrenList")
    Observable<List<CommentEntity>> getChildListComment(@Query("id") int newsId, @Query("start") int start, @Query("size") int size);


    /**
     * 添加一条评论
     *
     * @return
     */
    @POST("user/comment/add")
    @FormUrlEncoded
    Observable<String> addComment(@Field("newsId") Long newsId,@Field("parentId") Long parentId,@Field("replyId") Long replyId,@Field("level") int level,@Field("comment") String comment);

    /**
     * 给评论点赞
     *
     * @return
     */
    @POST("user/comment/like")
    @FormUrlEncoded
    Observable<String> addLikeComment(@Field("id") Long commentId);


    /**
     * 获取各类消息的未读数
     *
     * @return
     */
    @GET("user/msg/unReadCount")
    Observable<MsgUnreadCountEntity> getUnreadCount();


    /**
     * 获取点赞消息列表
     *
     * @return
     */
    @GET("user/msg/getUserLikeList")
    Observable<List<DianZanEntity>> getUserLikeList(@Query("start") int start, @Query("size") int size);

    /**
     * 获取通知列表
     *
     * @return
     */
    @GET("user/msg/getNoticeList")
    Observable<List<MsgNoticeEntity>> getNoticeList(@Query("start") int start, @Query("size") int size);


    /**
     * 获取小助手列表
     *
     * @return
     */
    @GET("user/msg/getReviewList")
    Observable<List<MsgNoticeEntity>> getReviewList(@Query("start") int start, @Query("size") int size);


    /**
     * 获取消息首页列表
     *
     * @return
     */
    @GET("user/msg/getIndexList")
    Observable<List<MsgNoticeEntity>> getIndexList(@Query("start") int start, @Query("size") int size);


    /**
     * 获取评论消息列表
     *
     * @return
     */
    @GET("user/msg/getUserCommentList")
    Observable<List<MsgCommentEntity>> getUserCommentList(@Query("start") int start, @Query("size") int size);

    /**
     * 获取评论消息列表
     *
     * @return
     */
    @POST("user/msg/read")
    @FormUrlEncoded
    Observable<String> setMsgRead(@Field("id") int msgId);

    /**
     * 删除某一条评论
     *
     * @return
     */
    @POST("user/msg/delete")
    @FormUrlEncoded
    Observable<String> deleteMsg(@Field("id") int msgId);

    /**
     * 设置全部已读
     *
     * @return
     */
    @POST("user/msg/readAll")
    Observable<String> setAllIsRead();
}
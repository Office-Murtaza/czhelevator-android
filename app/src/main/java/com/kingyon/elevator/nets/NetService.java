package com.kingyon.elevator.nets;

import android.content.Context;
import android.content.pm.PackageManager;
import android.media.MediaMetadataRetriever;
import android.text.TextUtils;

import com.alibaba.fastjson.JSON;
import com.amap.api.services.core.AMapException;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.district.DistrictItem;
import com.amap.api.services.district.DistrictResult;
import com.amap.api.services.district.DistrictSearch;
import com.amap.api.services.district.DistrictSearchQuery;
import com.blankj.utilcode.util.LogUtils;
import com.kingyon.elevator.application.App;
import com.kingyon.elevator.application.AppContent;
import com.kingyon.elevator.constants.Constants;
import com.kingyon.elevator.data.DataSharedPreferences;
import com.kingyon.elevator.entities.ADEntity;
import com.kingyon.elevator.entities.AMapCityEntity;
import com.kingyon.elevator.entities.AdDetectingEntity;
import com.kingyon.elevator.entities.AdNoticeWindowEntity;
import com.kingyon.elevator.entities.AdTempletEntity;
import com.kingyon.elevator.entities.AdvertisionEntity;
import com.kingyon.elevator.entities.AnnouncementEntity;
import com.kingyon.elevator.entities.AutoCalculationDiscountEntity;
import com.kingyon.elevator.entities.AvInfoEntity;
import com.kingyon.elevator.entities.BannerEntity;
import com.kingyon.elevator.entities.BindAccountEntity;
import com.kingyon.elevator.entities.CameraBrandEntity;
import com.kingyon.elevator.entities.CellDetailsEntity;
import com.kingyon.elevator.entities.CellDeviceNumberEntity;
import com.kingyon.elevator.entities.CellItemEntity;
import com.kingyon.elevator.entities.CityCellEntity;
import com.kingyon.elevator.entities.CommentEntity;
import com.kingyon.elevator.entities.CommitOrderEntiy;
import com.kingyon.elevator.entities.CooperationEntity;
import com.kingyon.elevator.entities.CooperationIdentityEntity;
import com.kingyon.elevator.entities.CooperationInfoNewEntity;
import com.kingyon.elevator.entities.CouponItemEntity;
import com.kingyon.elevator.entities.DeviceDetailsInfo;
import com.kingyon.elevator.entities.DeviceNumberEntity;
import com.kingyon.elevator.entities.DeviceNumberInfo;
import com.kingyon.elevator.entities.DianZanEntity;
import com.kingyon.elevator.entities.FeedBackCache;
import com.kingyon.elevator.entities.FeedBackEntity;
import com.kingyon.elevator.entities.FeedBackMessageEntity;
import com.kingyon.elevator.entities.HomepageDataEntity;
import com.kingyon.elevator.entities.IdentityInfoEntity;
import com.kingyon.elevator.entities.IncomeDetailsEntity;
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
import com.kingyon.elevator.entities.MsgCommentEntity;
import com.kingyon.elevator.entities.MsgNoticeEntity;
import com.kingyon.elevator.entities.MsgUnreadCountEntity;
import com.kingyon.elevator.entities.MyWalletInfo;
import com.kingyon.elevator.entities.NewsDetailsEntity;
import com.kingyon.elevator.entities.NewsEntity;
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
import com.kingyon.elevator.entities.YesterdayIncomeEntity;
import com.kingyon.elevator.entities.entities.AdZoneEntiy;
import com.kingyon.elevator.entities.entities.AtListEntiy;
import com.kingyon.elevator.entities.entities.AttenionUserEntiy;
import com.kingyon.elevator.entities.entities.AuthStatusEntily;
import com.kingyon.elevator.entities.entities.BalancePaymentsEntily;
import com.kingyon.elevator.entities.entities.CertifiCationEntiy;
import com.kingyon.elevator.entities.entities.Chartentily;
import com.kingyon.elevator.entities.entities.CodeEntity;
import com.kingyon.elevator.entities.entities.CommentLikesListEntiy;
import com.kingyon.elevator.entities.entities.CommentListEntity;
import com.kingyon.elevator.entities.entities.ConentEntity;
import com.kingyon.elevator.entities.entities.ConentOdjerEntity;
import com.kingyon.elevator.entities.entities.ConentTxEntity;
import com.kingyon.elevator.entities.entities.ContentLikesListEntiy;
import com.kingyon.elevator.entities.entities.EarningsTopEntity;
import com.kingyon.elevator.entities.entities.EarningsTwoYearlistEntity;
import com.kingyon.elevator.entities.entities.EarningsTwolistEntity;
import com.kingyon.elevator.entities.entities.EarningsYesterdayEnity;
import com.kingyon.elevator.entities.entities.EquipmentDetailsRevenueEntiy;
import com.kingyon.elevator.entities.entities.HomeTopicConentEntity;
import com.kingyon.elevator.entities.entities.HomeTopicEntity;
import com.kingyon.elevator.entities.entities.MassageHomeEntiy;
import com.kingyon.elevator.entities.entities.MassageListMentiy;
import com.kingyon.elevator.entities.entities.MassageLitsEntiy;
import com.kingyon.elevator.entities.entities.MassagePushEntiy;
import com.kingyon.elevator.entities.entities.OrderComeEntiy;
import com.kingyon.elevator.entities.entities.PartnerIndexInfoEntity;
import com.kingyon.elevator.entities.entities.PartnershipStatusEntily;
import com.kingyon.elevator.entities.entities.PlanNumberEntiy;
import com.kingyon.elevator.entities.entities.PointClassicEntiy;
import com.kingyon.elevator.entities.entities.PublicEntity;
import com.kingyon.elevator.entities.entities.QueryRecommendEntity;
import com.kingyon.elevator.entities.entities.QueryRecommendTopEntity;
import com.kingyon.elevator.entities.entities.QueryTopicEntity;
import com.kingyon.elevator.entities.entities.RecommendHouseEntiy;
import com.kingyon.elevator.entities.entities.StatisticalEnity;
import com.kingyon.elevator.entities.entities.TopicLabelEntity;
import com.kingyon.elevator.entities.entities.UserCashTypeListEnity;
import com.kingyon.elevator.entities.entities.UserTwoEntity;
import com.kingyon.elevator.entities.entities.WikipediaEntiy;
import com.kingyon.elevator.entities.entities.WithdrawEntily;
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

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    /*2.0验证码*/

    public Observable<String> setSendCheckConde(String type, String phone){
        return addSchedulers(getApi().getSendCheckCode(type,phone));
    }

    /*2.0登录*/
    public Observable<CodeEntity> setLogin(String phone,String password,String way,String unique,String avatar,String nickName ){
        return addSchedulers(getApi().getLogin(phone,password,way,unique,avatar,nickName));
    }
    /*2.0设置密码*/
    public Observable<CodeEntity> setPasswordSetting(String phone,String password,String inviter){
        return addSchedulers(getApi().getPassswordSetting(phone,password,inviter));
    }
    /*2.0绑定手机*/
    public Observable<CodeEntity> setBindPhone(String phone, String verifyCode,String unique,String avatar, String nickName,String way){
        return addSchedulers(getApi().getBindPhone(phone,verifyCode,unique,avatar,nickName,way));
    }
    /*2.0验证码验证*/
    public Observable<CodeEntity> setCheckVerifyCode(String phone, String verifyCode){
        return addSchedulers(getApi().getCheckVerifyCode(phone,verifyCode));
    }
    /*2.0找回密码*/
    public Observable<String> setResetPassword(String phone,String verifyCode,String newPassword){
        return addSchedulers(getApi().getResetPassword(phone,verifyCode,newPassword));
    }
    /*2.0话题*/
    public Observable<ConentEntity<QueryTopicEntity.PageContentBean>> setOueryTopic(int page,String title,int label){
        if (label==0){
            return addSchedulers(getApi().getOueryTopic(page,title,""));
        }else {
            return addSchedulers(getApi().getOueryTopic(page,title, String.valueOf(label)));
        }
    }
    /*2.0话题栏目*/
    public Observable<TopicLabelEntity<TopicLabelEntity.PageContentBean>> setTopicLable(){
        return addSchedulers(getApi().getTopicLabel());
    }

    /*2.0内容发布*/
    public Observable<String> setContentPublish(String title, String content, String image,String video, String type ,
                                                String combination , String topicId , String atAccount ,int videoSize,
                                                String videoCover,long playTime, int videoHorizontalVertical,boolean isOriginal){
        return addSchedulers(getApi().getContentPublish(title,content,image,video,
                type,combination,topicId,atAccount,videoSize,videoCover,playTime,videoHorizontalVertical,isOriginal));
    }

    /*2.0置顶内容*/
    public Observable<ConentEntity<QueryRecommendTopEntity>> setQueryRecommendTop(){
        return addSchedulers(getApi().getQueryRecommendTop());
    }
    /*2.0推荐内容*/
    public Observable<ConentEntity<QueryRecommendEntity>> setQueryRecommend(int page,String title,String account){
        return addSchedulers(getApi().getQueryRecommend(page,title,account));
    }
    /*2.0内容详情*/
    public Observable<QueryRecommendEntity> setQueryContentById(String contentId ,String account){
        return addSchedulers(getApi().getQueryContentById(contentId,account));
    }

    /*2.0关注内容*/
    public Observable<ConentEntity<QueryRecommendEntity>> setQueryAttention(int page,String title,String orderBy){
        return addSchedulers(getApi().getQueryAttention(page,title,orderBy));
    }
    /*2.0 内容点赞*/
    public Observable<String> setLikeNot(String objId,String likeType,String handleType  ){
        return addSchedulers(getApi().getLikeNot(objId,likeType,handleType));
    }

    /*2.0首页话题栏目*/
    public Observable<ConentEntity<HomeTopicEntity>> setQueryTopicLabel(){
        return addSchedulers(getApi().getQueryTopicLabel());
    }
    /*2.0话题内容*/
    public Observable<ConentEntity<HomeTopicConentEntity>> setQueryTopicConetn(int page,String label,String title,int id){
        if (id==0){
            return addSchedulers(getApi().getQueryTopicConetn(page, label, title, ""));
        }else {
            return addSchedulers(getApi().getQueryTopicConetn(page, label, title, String.valueOf(id)));
        }
    }
    /*2.0点赞*/
    public Observable<String> setHandlerLikeOrNot(int objId,String likeType,String handleType ){
        return addSchedulers(getApi().getHandlerLikeOrNot(objId,likeType,handleType));
    }
    /*2.0删除*/
    public Observable<String> setDelContent(int contentId){
        return addSchedulers(getApi().getDelContent(contentId));
    }

    /*2.0举报*/
    public Observable<String> setReport(int objId, String reportType,String reportContent){
        return addSchedulers(getApi().getReport(objId,reportType,reportContent));
    }

    /*2.0内容评论*/
    public Observable<String> setComment(int contentId,int parentId,String comment){
        return addSchedulers(getApi().getComment(contentId,parentId,comment));
    }

    /*2.0添加浏览数*/
    public Observable<String> setAddBrowse(int contentId){
        return addSchedulers(getApi().getAddBrowse(contentId));
    }

    /*2.0获取内容评论数据*/
    public Observable<ConentEntity<CommentListEntity>> setQueryListComment(int page, int contentId){
        return addSchedulers(getApi().getQueryListComment(page,contentId));
    }

    /*2.0获取子级评论内容*/
    public Observable<ConentEntity<CommentListEntity>> setCommentBy(int page, int contentId,int parentId ){
        return addSchedulers(getApi().getCommentBy(page,contentId,parentId));
    }

    /*2.0用户关注*/
    public Observable<String> setAddAttention( String handlerType,String beFollowerAccount,String followerAccount ){
        return addSchedulers(getApi().getAddAttention(handlerType,beFollowerAccount,followerAccount ));
    }

    /*2.0获取用户*/
    public Observable<ConentEntity<AttenionUserEntiy>> setAttention(int page,String handlerType,String extend){
        return addSchedulers(getApi().getAttention(page,handlerType,extend));
    }

    /*2.0获取搜索用户*/
    public Observable<ConentEntity<AttenionUserEntiy>> getMatching(int page,String keyWords){
        return addSchedulers(getApi().getMatching(page, keyWords));
    }

    /*2.0获取推送消息*/
    public Observable<ConentEntity<MassagePushEntiy>> getPushMagList(int robot_id,int page,int rows){
        return addSchedulers(getApi().getPushMagList(robot_id,page,rows));
    }

    /*2.0消息-查询点赞内容*/
    public Observable<ConentEntity<ContentLikesListEntiy>> getContentLikesList(int page,int rows){
        return addSchedulers(getApi().getContentLikesList(page, rows));
    }

    /*2.0消息-查询点赞内容*/
    public Observable<ConentEntity<CommentLikesListEntiy>> getCommentMeList(int page, int rows){
        return addSchedulers(getApi().getCommentMeList(page, rows));
    }
  /*2.0消息-查询@我*/
    public Observable<ConentEntity<AtListEntiy>> getAtList(int page, int rows){
        return addSchedulers(getApi().getAtList(page, rows));
    }

    /*2.0消息-查询点赞内容*/
    public Observable<ConentEntity<CommentLikesListEntiy>> getCommentLikesList(int page, int rows){
        return addSchedulers(getApi().getCommentLikesList(page, rows));
    }

    /*2.0标记指定已读*/
    public Observable<String> getMarkRead(String id,String type,String isAll){
        return addSchedulers(getApi().getMarkRead(id, type, isAll));
    }

    /*2.0标记全部已读*/
    public Observable<String> markAll(){
        return addSchedulers(getApi().markAll());
    }

    /*2.0获取投放首页*/
    public Observable<ConentOdjerEntity> setCityFacilty(String provinceCode, String cityCode, String countyCode){
        if (cityCode!=null) {
            return addSchedulers(getApi().getCityFacility(provinceCode, cityCode, countyCode));
        }else {
            return addSchedulers(getApi().getCityFacility(provinceCode, "520100", countyCode));
        }
    }
    /*2.0获取话题内容*/

    public Observable<ConentEntity<QueryRecommendEntity>>steTopicAttention(int page,int topicId ,String orderBy){
        return addSchedulers(getApi().getTopicAttention(page,topicId,orderBy));
    }

    /*2.0评论删除*/
    public Observable<String> setDelcomment(int commentId){
        return addSchedulers(getApi().getDelcomment(commentId));
    }

    /*2.0获取推荐小区*/

    public Observable<ConentEntity<RecommendHouseEntiy>> setRecommendHouse(int page , String latitude , String longitude , int cityId,
                                                                           String areaId,  String pointType, String keyWord, String distance){
        if (latitude!=null) {
            return addSchedulers(getApi().getRecommendHouse(page, latitude, longitude, cityId, areaId, pointType, keyWord, distance));
        }else {
            return addSchedulers(getApi().getRecommendHouse(page, "26.578343", "106.713478", cityId, areaId, pointType, keyWord, distance));
        }
    }

    /*2.0添加编辑计划*/

    public Observable<String> plansAdd(Long planId
            , String planName, String type) {
        return addSchedulers(getApi().plansAdd(planId, planName, type));
    }


//    public Observable<String> setPlasAddOrModify(int planId,String planName,String type){
//        return addSchedulers(getApi().getPlasAddOrModify(planId,planName,type));
//    }

    /*2.0获取点位筛选内容*/
    public Observable<ConentEntity<PointClassicEntiy>> setPointClassic(){
        return addSchedulers(getApi().getPointClassic());
    }

    /*2.0小区添加计划*/
    public Observable<String> plansAddCells(String type, String cells) {
        return addSchedulers(getApi().plansAddCells(type, cells));
    }

    /*2.0收藏内容点位*/
    public Observable<String> setAddCollect(String objectId,String type){
        return addSchedulers(getApi().getAddCollect(objectId,type));
    }

    /*2.0取消收藏*/
    public Observable<String> setCancelCollect(String collectId){
        return addSchedulers(getApi().getCancelCollect(collectId));
    }

    /*2.0获取开票内容*/
    public Observable<Chartentily> setInvoiceInfo(){
        return addSchedulers(getApi().getInvoiceInfo());
    }

    /*2.0获取用户动态*/
    public Observable<ConentEntity<QueryRecommendEntity>> setUserCenterContent(int page,String otherUserAccount ){
        return addSchedulers(getApi().userCenterContent(page,otherUserAccount));
    }

    /*2.0获取其他用户信息*/
    public Observable<UserTwoEntity> setUserCenterInfo(String otherUserAccount){
        return addSchedulers(getApi().userCenterInfo(otherUserAccount));
    }

    /*2.0获取用户认证状态*/
    public Observable<AuthStatusEntily>getAuthStatus(){
        return addSchedulers(getApi().getAuthStatus());
    }

    /*2.0获取第三方绑定用户*/
    public Observable<UserEntity> getCheckBind3Rd(){
        return addSchedulers(getApi().checkBind3Rd());
    }

    /*2.0消息首页数据*/
    public Observable<MassageHomeEntiy<MassageLitsEntiy>> getMsgOverview(int page,int rows){
        return addSchedulers(getApi().getMsgOverview(page, rows));
    }
    /*2.0消息列表*/
    public Observable<ConentEntity<MassageListMentiy>> getMessageList(int page,int rows){
        return addSchedulers(getApi().getMessageList(page, rows));
    }

    /*2.0消息 -获取关注*/
    public Observable<ConentEntity<AttenionUserEntiy>> getFollowerList(int page,int rows){
        return addSchedulers(getApi().getFollowerList(page, rows));
    }


    /*2.0删除消息机器人*/
    public Observable<String> removeRobot(String robot_id){
        return addSchedulers(getApi().removeRobot(robot_id));
    }


    /*2.0删除@我的记录*/
    public Observable<String> removeAtMe(String robot_id){
        return addSchedulers(getApi().removeAtMe(robot_id));
    }


    /*2.0删除评论点赞*/
    public Observable<String> removeCommentLikes(String robot_id){
        return addSchedulers(getApi().removeCommentLikes(robot_id));
    }


    /*2.0删除评论我的记录*/
    public Observable<String> removeCommentMe(String robot_id){
        return addSchedulers(getApi().removeCommentMe(robot_id));
    }

    /*2.删除内容点赞*/
    public Observable<String> removeContentLikes(String robot_id){
        return addSchedulers(getApi().removeContentLikes(robot_id));
    }


    /*2.删除关注消息*/
    public Observable<String> removeLikeMsg(String robot_id){
        return addSchedulers(getApi().removeLikeMsg(robot_id));
    }

    /*2.删除我的消息*/
    public Observable<String> removeMsg(String robot_id){
        return addSchedulers(getApi().removeMsg(robot_id));
    }

    /*2.删除推送消息*/
    public Observable<String> removePushMsg(String robot_id){
        return addSchedulers(getApi().removePushMsg(robot_id));
    }


    /*2.0客服管理内容*/
    public Observable<WikipediaEntiy> getWikipedia(){
        return addSchedulers(getApi().getWikipedia());
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

    public void uploadFileNoActivity(Context context, File file, NetUpload.OnUploadCompletedListener onUploadCompletedListener, boolean needComptess) {
        Logger.i("upload File size:%s", FileUtil.convertFileSize(file.length()));
        netUpload.uploadFileNoActivity(context, file, onUploadCompletedListener, needComptess);
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

    /*2.0设备详情*/
    public Observable<PointItemEntity> deviceDetails(long deviceId) {
        return addSchedulers(getApi().deviceDetails(deviceId));
    }
    /*2.0物业设备收益*/
    public Observable<ConentEntity<EquipmentDetailsRevenueEntiy>> getEquipmentDetailsRevenue(int page,String month,long deviceId ){
        return addSchedulers(getApi().setEquipmentDetailsRevenue(page,month,deviceId));
    }

    /*2.0合伙人设备收益*/
    public Observable<ConentEntity<EquipmentDetailsRevenueEntiy>> getIncomeList(int page,String month,long deviceId ){
        return addSchedulers(getApi().setincomeList(page,month,deviceId));
    }

    public Observable<String> repairDevice(long deviceId, Long reasonId, String remarks, String images,String app_version) {
        return addSchedulers(getApi().repairDevice(deviceId, reasonId, remarks, images,android.os.Build.BRAND,app_version));
    }

    public Observable<List<NormalElemEntity>> repairReasons() {
        return addSchedulers(getApi().repairReasons());
    }

    public Observable<String> addDevice(Long objectId, String deviceNo, String deviceLocation, String deviceType
            , long cellId, long buildId, long unitId, long liftId, Long cameraBrand, String cameraIp) {
        return addSchedulers(getApi().addDevice(objectId, deviceNo, deviceLocation, deviceType
                , cellId, buildId, unitId, liftId, cameraBrand, cameraIp));
    }

    public Observable<String> addCell(Long objectId, String adcode,
                                      String address, String cellName,
                                      String cellType, Long humanTraffic,
                                      String cellLogo, String cellBanner,
                                      String throwWay,String occupancyRate,
                                      String averageSellingPrice,String siteNumber,
                                      String propertyFee,String peopleCoverd,
                                      String rent,String numberArea,
                                      String exclusiveAdvertising,String deliveryTime,
                                      double longitude, double latitude) {
        return addSchedulers(getApi().addCell(objectId, adcode, address, cellName, cellType, humanTraffic
                , cellLogo, cellBanner,throwWay,occupancyRate,averageSellingPrice,siteNumber,propertyFee,peopleCoverd,
                rent,numberArea,exclusiveAdvertising,deliveryTime, longitude, latitude));
    }

    public Observable<ConentEntity<PointItemEntity>> installerDeviceList(int page) {
        return addSchedulers(getApi().installerDeviceList(page));
    }

    public Observable<AdvertisionEntity> getAdertising() {
        return addSchedulers(getApi().getAdertising());
    }
//    public Observable<CameraEntity> liftCamera(long liftId) {
//        return addSchedulers(getApi().liftCamera(liftId));
//    }

    /*2.0摄像头厂家*/
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

    /*2.0跟换手机*/
    public Observable<String> unbindPhone(String phone, String code, String type) {
        return addSchedulers(getApi().unbindPhone(phone, code, type));
    }

    /*2.0忘记密码*/
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

    /*2.0小区详情*/
    public Observable<CellDetailsEntity> cellDetails(long objectId,String account) {
        return addSchedulers(getApi().cellDetails(objectId,account));
    }
    /*2.0计划列表*/
    public Observable<ConentEntity<CellItemEntity>> plansList(String type
            , Long startTime, Long endTime, int page) {
        return addSchedulers(getApi().plansList(type, startTime, endTime, page));
    }
    /*2.0小区单独指定*/
    public Observable<List<PointItemEntity>> planCellsPoinList(long cellId, String type
            , long startTime, long endTime) {
        return addSchedulers(getApi().planCellsPoinList(cellId, type, startTime, endTime));
    }

    /*2.0点位计划删除*/
    public Observable<String> plansDelete(String planIds) {
        return addSchedulers(getApi().plansDelete(planIds));
    }


    /*2.0点位计划删除小区*/
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

    /*2.0订单提交*/
    public Observable<CommitOrderEntiy> commitOrder(String type, long startTime, long endTime
            , Long adId, String deviceParams, String coupons, long adIndustry) {
        LogUtils.e(type,startTime,endTime,adId,deviceParams,coupons,adIndustry);
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
    /*2.0订单详情*/
    public Observable<OrderDetailsEntity> orderDetatils(String orderId) {
        return addSchedulers(getApi().orderDetatils(orderId));
    }
    /*2.0获取订单身份认证信息*/
    public Observable<OrderIdentityEntity> orderIdentityInfo() {
        return addSchedulers(getApi().orderIdentityInfo());
    }


    /*2.0订单支付*/
    public Observable<WxPayEntity> orderPay(String orderId, String way,String payPwd ) {
        Observable<WxPayEntity> zip = Observable.zip(getApi().orderPay(orderId, way,payPwd), Observable.just(way), new Func2<WxPayEntity, String, WxPayEntity>() {
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
    /*2.0订单列表*/
    public Observable<ConentEntity<OrderDetailsEntity>> orderList(String status,String auditStatus, int page) {
        return addSchedulers(getApi().orderList(status, auditStatus,page));
    }

    /*2.0再来一单*/
    public Observable<List<OrderComeEntiy>> orderAgain(String orderSn){
        return addSchedulers(getApi().orderAgain(orderSn));
    }

    /*2.0合同下载地址*/
    public Observable<DataEntity<String>> downloadContract() {
        return addSchedulers(getApi().downloadContract());
    }

    /*2.0查看订单点位列表*/
    public Observable<List<PointItemEntity>> orderPoints(String orderId) {
        return addSchedulers(getApi().orderPoints(orderId));
    }
    /*2.0申请下播*/
    public Observable<String> downAd(String orderId, long tagReasonId, String undercastRemarks) {
        return addSchedulers(getApi().downAd(orderId, tagReasonId, undercastRemarks));
    }
    /*2.0查看监播*/
    public Observable<PageListEntity<AdDetectingEntity>> adPlayList(String orderId, long deviceId, int page) {
        return addSchedulers(getApi().adPlayList(orderId, deviceId, page));
    }
    /*2.0获取=播原因*/
    public Observable<List<NormalElemEntity>> downAdTags() {
        return addSchedulers(getApi().downAdTags());
    }
    /*2.0取消订单*/
    public Observable<String> orderCancel(String orderId) {
        return addSchedulers(getApi().orderCancel(orderId));
    }

    /*2.0删除订单*/
    public Observable<String> orderDelete(String orderId) {
        return addSchedulers(getApi().orderDelete(orderId));
    }

    /*2.0创建编辑广告*/
    public Observable<ADEntity> createOrEidtAd(final Long objectId, final boolean onlyInfo, String planType, final String screenType
            , final String title, final String videoUrl, final String imageUrl, final String bgMusic, final String videoPath, final String imagePath,String hashCode) {
        final Long[] durationResult = new Long[1];
        Observable<ADEntity> observable;
        if (TextUtils.isEmpty(videoUrl)) {
            observable = getApi().createOrEidtAd(objectId, onlyInfo, planType, screenType, title, videoUrl
                    , imageUrl, bgMusic, null,hashCode);
        } else {
            observable = getAvInfoDuration(videoUrl)
                    .flatMap(new Func1<Long, Observable<ADEntity>>() {
                        @Override
                        public Observable<ADEntity> call(Long duration) {
                            durationResult[0] = duration;
                            return getApi().createOrEidtAd(objectId, onlyInfo, planType, screenType, title, videoUrl
                                    , imageUrl, bgMusic, duration,hashCode);
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

    /*2.获取计划单数*/
    public Observable<PlanNumberEntiy> setAdPlan(){
        return addSchedulers(getApi().getAdPlan());
    }

    /*2.0支付宝认证*/
    public Observable<PlanNumberEntiy> setAliIdentityAuth(String cerName,String certNo){
        return addSchedulers(getApi().getAliIdentityAuth(cerName,certNo,"CUSTOMER"));
    }
    /*2.0身份认证上传内容*/
    public Observable<CertifiCationEntiy> setIdentyAuth(String personName, String idCardNum, String idCardPic, String type){
       return addSchedulers(getApi().getIdentyAuth(personName,idCardNum,idCardPic,type));
    }
    /*2.0合伙人首页内容*/
    public Observable<ConentEntity<PartnerIndexInfoEntity>> setPartnerIndexInfo(){
        return addSchedulers(getApi().getPartnerIndexInfo());
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
    /*2.0我的广告列表*/
    public Observable<ConentEntity<ADEntity>> myAdList(String planType, int page) {
        return addSchedulers(getApi().myAdList(planType, page));
    }
    /*2.0广告删除*/
    public Observable<String> deleteAd(long objectId) {
        return addSchedulers(getApi().deleteAd(objectId));
    }
    /*2.0广告专区*/
    public Observable<AdZoneEntiy> getAdvList(int page,int rows, String params){
        return addSchedulers(getApi().getAdvList(page, rows, params));
    }

    public Observable<ConentEntity<AdTempletEntity>> getPicAdTemplet(String screenSplit, String sort
            , Long type, int page) {
        return addSchedulers(getApi().getPicAdTemplet(screenSplit, sort, type, page));
    }
    /*2.0获取分类模板*/
    public Observable<List<NormalElemEntity>> getAdTempletType() {
        return addSchedulers(getApi().getAdTempletType());
    }

    public Observable<PageListEntity<MateriaEntity>> getMaterials(String planType, String screenSplit, String type, int page) {
        return addSchedulers(getApi().getMaterials(planType, screenSplit, type, page));
    }

    /* 2.0获取可用优惠卷*/
    public Observable<List<CouponItemEntity>> getMeetCoupons(float price, String type) {
        return addSchedulers(getApi().getMeetCoupons(price, type));
    }

    public Observable<ConentEntity<CouponItemEntity>> getCoupons(String status, int page) {
        return addSchedulers(getApi().getCoupons(status, page));
    }

    /*2.0赠送优惠卷*/
    public Observable<String> donateCoupons(String phone, String couponCounts, String couponIds) {
        return addSchedulers(getApi().donateCoupons(phone, couponCounts, couponIds));
    }

    /*2.0第三方绑定*/
    public Observable<String> setBin3Rd(String thirdId, String bindType){
        return addSchedulers(getApi().getBinRd(thirdId, bindType));
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

    /*2.0用户资料修改*/
    public Observable<UserEntity> userEidtProfile(String avatar,String nikeName,
                                                  String sex,String city,String birthday,
                                                  String intro,String cover) {
        return addSchedulers(getApi().userEidtProfile(avatar,nikeName,sex,city,birthday,intro,cover));
    }

    public Observable<DataEntity<Float>> myWallet() {
        return addSchedulers(getApi().myWallet());
    }

    public Observable<MyWalletInfo> myWalletInfo(int page,String handleType) {
        Observable<MyWalletInfo> observable;
        if (page == 1) {
            observable = Observable.zip(getApi().myWalletRecords(page,handleType), getApi().myWallet()
                    , new Func2<ConentEntity<WalletRecordEntity>, DataEntity<Float>, MyWalletInfo>() {
                        @Override
                        public MyWalletInfo call(ConentEntity<WalletRecordEntity> walletRecordEntityPageListEntity, DataEntity<Float> floatDataEntity) {
                            return new MyWalletInfo(floatDataEntity.getData(), walletRecordEntityPageListEntity);
                        }
                    });
        } else {
            observable = getApi().myWalletRecords(page,handleType)
                    .flatMap(new Func1<ConentEntity<WalletRecordEntity>, Observable<MyWalletInfo>>() {
                        @Override
                        public Observable<MyWalletInfo> call(ConentEntity<WalletRecordEntity> walletRecordEntityPageListEntity) {
                            return Observable.just(new MyWalletInfo(null, walletRecordEntityPageListEntity));
                        }
                    });
        }
        return addSchedulers(observable);
    }
/*2.0充值接口*/
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
    /*2.0认证提交id*/
    public Observable<String> setAliAuthQuery(String certifyId){
        return addSchedulers(getApi().setAliAuthQuery(certifyId));
    }

    /*2.0个人收藏内容*/
    public Observable<ConentEntity<QueryRecommendEntity>> setmyCollects(int page ){
        return addSchedulers(getApi().getmyCollects(page, "CONTENT", 0, 0));
    }
    /*2.0个人收藏点位*/
    public Observable<ConentEntity<RecommendHouseEntiy>> setmyCollects1(int page,String latitude,String longitude  ){
        if (latitude!=null) {
            return addSchedulers(getApi().getmyCollects1(page, "POINT", latitude, longitude));
        }else {
            return addSchedulers(getApi().getmyCollects1(page, "POINT", "26.578343", "106.713478"));
        }
    }

    public Observable<InvoiceInfoEntity> invoiceInfo() {
        return addSchedulers(getApi().invoiceInfo());
    }

    public Observable<String> createInvoice(String invoiceType, String invoiceStart, String invoiceNo
            , String bank, float invoiceAmount, String receiveEmail, String content) {
        return addSchedulers(getApi().createInvoice(invoiceType, invoiceStart, invoiceNo, bank
                , invoiceAmount, receiveEmail, content));
    }

    public Observable<ConentEntity<InvoiceEntity>> invoiceList(int page) {
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

    public Observable<ConentEntity<FeedBackEntity>> myFeedBackList(int page) {
        return addSchedulers(getApi().myFeedBackList(page));
    }

    public Observable<String> createFeedBack(String titile, String images,String app_version) {
        return addSchedulers(getApi().createFeedBack(titile, images, android.os.Build.BRAND,app_version));
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
    /*2.0审核未通过的订单数量*/
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

    //2.0合伙人
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
                        if (entity.isBePartner() || (cooperationIdentityEntity != null && TextUtils.equals(Constants.COOPERATION_STATUS.AUDITING, cooperationIdentityEntity.getStatus()))) {
                            observable = getApi().cooperationInfoNew();
                        } else {
                            observable = Observable.just(null);
                        }
                        entity.setIdentity(cooperationIdentityEntity);
                        return observable;
                    }
                })
                .flatMap(new Func1<CooperationInfoNewEntity, Observable<CooperationEntity>>() {
                    @Override
                    public Observable<CooperationEntity> call(CooperationInfoNewEntity cooperationInfoEntity) {
                        entity.setInfo(cooperationInfoEntity);
                        return Observable.just(entity);
                    }
                });
        return addSchedulers(observable);
    }

    /*2.0合伙人申请状态*/
    public Observable<CooperationIdentityEntity> cooperationInfotwo(){
        return addSchedulers(getApi().cooperationIentityInfotwo());
    }

    /*2.0 获取合伙人提现账户*/
    public Observable<List<UserCashTypeListEnity>> steUserCashTypeList(int page){
        return addSchedulers(getApi().getUserCashTypeList(page));
    }

    public Observable<PublicEntity> setverifyPayPasswordInit(){
        return addSchedulers(getApi().setverifyPayPasswordInit());
    }


//    public Observable<String> cooperationApply(final String partnerName, final String phone, final AMapCityEntity city) {
//        Observable<String> observable;
//        if (TextUtils.isEmpty(city.getAdcode())) {
//            observable = Observable.just(city)
//                    .flatMap(new Func1<AMapCityEntity, Observable<String>>() {
//                        @Override
//                        public Observable<String> call(AMapCityEntity cityEntity) {
//                            DistrictSearch search = new DistrictSearch(App.getContext());
//                            DistrictSearchQuery query = new DistrictSearchQuery();
//                            query.setKeywords(cityEntity.getName());//传入关键字
//                            query.setShowBoundary(false);//是否返回边界值
//                            query.setShowChild(false);
//                            search.setQuery(query);
//                            try {
//                                DistrictResult districtResult = search.searchDistrict();//开始搜索
//                                ArrayList<DistrictItem> districts = districtResult.getDistrict();
//                                if (districts != null && districts.size() > 0) {
//                                    DistrictItem districtItem = districts.get(0);
//                                    LatLonPoint center = districtItem.getCenter();
//                                    cityEntity.setCenter(String.format("%s,%s", center.getLongitude(), center.getLatitude()));
//                                    cityEntity.setAdcode(districtItem.getAdcode());
//                                } else {
//                                    Logger.e("cooperationApply() 出错");
//                                }
//                            } catch (AMapException e) {
//                                e.printStackTrace();
//                                Logger.e("cooperationApply() 出错");
//                            }
//                            if (TextUtils.isEmpty(cityEntity.getAdcode())) {
//                                throw new ResultException(9002, "AMap DistrictSearch error!");
//                            }
//                            return getApi().cooperationApply(partnerName, phone, cityEntity.getAdcode());
//                        }
//                    });
//        } else {
//            observable = getApi().cooperationApply(partnerName, phone, city.getAdcode());
//        }
//        return addSchedulers(observable);
//    }
public Observable<String> cooperationApply(final String partnerName, final String phone, final String city) {
    Observable<String> observable;
//        if (TextUtils.isEmpty(city.getAdcode())) {
//            observable = Observable.just(city)
//                    .flatMap(new Func1<AMapCityEntity, Observable<String>>() {
//                        @Override
//                        public Observable<String> call(AMapCityEntity cityEntity) {
//                            DistrictSearch search = new DistrictSearch(App.getContext());
//                            DistrictSearchQuery query = new DistrictSearchQuery();
//                            query.setKeywords(cityEntity.getName());//传入关键字
//                            query.setShowBoundary(false);//是否返回边界值
//                            query.setShowChild(false);
//                            search.setQuery(query);
//                            try {
//                                DistrictResult districtResult = search.searchDistrict();//开始搜索
//                                ArrayList<DistrictItem> districts = districtResult.getDistrict();
//                                if (districts != null && districts.size() > 0) {
//                                    DistrictItem districtItem = districts.get(0);
//                                    LatLonPoint center = districtItem.getCenter();
//                                    cityEntity.setCenter(String.format("%s,%s", center.getLongitude(), center.getLatitude()));
//                                    cityEntity.setAdcode(districtItem.getAdcode());
//                                } else {
//                                    Logger.e("cooperationApply() 出错");
//                                }
//                            } catch (AMapException e) {
//                                e.printStackTrace();
//                                Logger.e("cooperationApply() 出错");
//                            }
//                            if (TextUtils.isEmpty(cityEntity.getAdcode())) {
//                                throw new ResultException(9002, "AMap DistrictSearch error!");
//                            }
//                            return getApi().cooperationApply(partnerName, phone, cityEntity.getAdcode());
//                        }
//                    });
//        } else {
    observable = getApi().cooperationApply(city);
//        }
    return addSchedulers(observable);
}
    public Observable<String> partnerWithdraw(double amount, String withDrawWay, String aliAcount,String wChatAcount
            , String bankName, String cardNo, String cardholder) {
        return addSchedulers(getApi().partnerWithdraw(amount, withDrawWay, aliAcount,wChatAcount, bankName, cardNo, cardholder));
    }

    public Observable<WithdrawEntily<WithdrawEntily.PageContentBean<WithdrawItemEntity>>> partnerWithdrawList(int page) {
        return addSchedulers(getApi().partnerWithdrawList(page));
    }

    public Observable<ConentEntity<CellItemEntity>> partnerCellList(Double longitude, Double latitude, int page,String keywords) {
        return addSchedulers(getApi().partnerCellList(longitude, latitude, page,keywords).doOnNext(new Action1<ConentEntity<CellItemEntity>>() {
            @Override
            public void call(ConentEntity<CellItemEntity> cellItemEntityPageListEntity) {
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

    public Observable<ConentEntity<IncomeStatisticsEntity>> propertyIncomeStatistics(String filter, int page) {
        return addSchedulers(getApi().propertyIncomeStatistics(filter, page));
    }

    public Observable<ConentEntity<IncomeStatisticsEntity>> propertyEarningsDetails(long startTime
            , long endTime, int page) {
        return addSchedulers(getApi().propertyEarningsDetails(startTime, endTime, page));
    }

    public Observable<ConentEntity<SettlementEntity>> propertySettlementList(String type, int page) {
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
     * 2.0合伙人收益记录（年）
     *
     * @param date
     * @return
     */
    public Observable<EarningsTopEntity<EarningsTwoYearlistEntity>> getIncomeAndPayByDate(String date) {
        return addSchedulers(getApi().getIncomeAndPayByDate(date));
    }

    /**
     *
     *2.0合伙人收益记录（月）
     * */
    public Observable<EarningsTopEntity<EarningsTwoYearlistEntity>> setEarningsRecordMonth(String month){
        return addSchedulers(getApi().getEarningsRecordMonth(month));
    }


    /**
     * 2.0合伙人收支记录（天）
     *
     */
    public Observable<BalancePaymentsEntily> getMonthIncomeOrPayByDate(int page,String type, String day) {
        return addSchedulers(getApi().getMonthIncomeAndPayByDate(page,type, day));
    }

    /**
     * 2.0合伙人收支记录（月）
     *
     */
    public Observable<BalancePaymentsEntily> getYearIncomeOrPayByDate(int page,String type, String month) {
        return addSchedulers(getApi().getYearIncomeAndPayByDate(page,type, month));
    }

    /**
     * 根据日期查询合伙人支出详情列表
     *
     * @param date
     * @return
     */
    public Observable<List<IncomeDetailsEntity>> getPayDetailedList(String startPosition, String size, String date) {
        return addSchedulers(getApi().getPayDetailedList(startPosition, size, date));
    }

    /**
     * 根据日期查询合伙人收入详情列表
     *
     * @param date
     * @return
     */
    public Observable<List<IncomeDetailsEntity>> getIncomeDetailedList(String startPosition, String size, String date) {
        return addSchedulers(getApi().getIncomeDetailedList(startPosition, size, date));
    }


    /**
     * 根据年份和月份查询某一个月的收入或支出详情
     *
     * @return
     */
    public Observable<List<IncomeDetailsEntity>> getIncomePayDataDayList(String startPosition, String size, String type, String date) {
        return addSchedulers(getApi().getIncomePayDataDayList(startPosition, size, type, date));
    }


    /**
     * 2.0查询昨日收益
     *
     * @return
     */
    public Observable<ConentTxEntity<StatisticalEnity<EarningsYesterdayEnity>>> getYesterdayIncomeDetailedList(int page) {
        return addSchedulers(getApi().getYesterdayIncomeDetailedList(page));
    }

    /**
     * 2.0查询已提现数据
     *
     * @return
     */
    public Observable<ConentTxEntity<StatisticalEnity<EarningsYesterdayEnity>>> getCashedList(int  page) {
        return addSchedulers(getApi().getCashedList(page));
    }

    /**
     * 查询已绑定的账号
     *
     * @return
     */
    public Observable<List<BindAccountEntity>> getUserCashTypeList(String startPosition, String size) {
        return addSchedulers(getApi().getUserCashTypeList(startPosition, size));
    }


    /**
     * 2.0绑定提现账号
     *
     * @return
     */
    public Observable<String> addCashAccount(String cashAccount, String cashType, String cashName, String openingBank) {
        return addSchedulers(getApi().addCashAccount(cashAccount, cashType, cashName, openingBank));
    }


    /**
     * 初始化支付密码
     *
     * @return
     */
    public Observable<String> initPayPassword(String password) {
        return addSchedulers(getApi().initPayPassword(password));
    }

    /**
     * 2.0验证是否设置支付密码
     *
     * @return
     */
    public Observable<CooperationInfoNewEntity> vaildInitPayPwd() {
        return addSchedulers(getApi().vaildInitPayPwd());
    }


    /**
     * 2.0用验证码修改支付密码
     *
     * @return
     */
    public Observable<String> changePasswordByCode(String phone, String vaildCode, String password) {
        return addSchedulers(getApi().changePasswordByCode(phone, vaildCode, password));
    }


    /**
     * 2.0用原密码修改支付密码
     *
     * @return
     */
    public Observable<String> changePasswordByOld(String oldPassword, String newPasswrod) {
        return addSchedulers(getApi().changePasswordByOld(oldPassword, newPasswrod));
    }


    /**
     * 2.0支付密码验证是否正确
     *
     * @return
     */
    public Observable<String> vaildPasswordIsRight(String pwd) {
        return addSchedulers(getApi().vaildPasswordIsRight(pwd));
    }


    /**
     * 获取自动计算的优惠信息
     *
     * @return
     */
    /*2.0获取订单优惠信息*/
    public Observable<AutoCalculationDiscountEntity> getCouponsInfo(double amount, String type, Boolean isManual, String consIds) {
        return addSchedulers(getApi().getCouponsInfo(amount, type, isManual, consIds));
    }


    /**
     * 获取弹窗通知以及固定位置通知等
     *
     * @return
     */
    public Observable<List<AdNoticeWindowEntity>> getTipsList(String position) {
        return addSchedulers(getApi().getTipsList(position));
    }

    /**
     * 获取新闻列表
     *
     * @return
     */
    public Observable<List<NewsEntity>> getNewsList(int start, int size, Integer category, String keywords) {
        return addSchedulers(getApi().getNewsList(start, size, category, keywords));
    }

    /**
     * 获取新闻详情
     *
     * @return
     */
    public Observable<NewsDetailsEntity> getNewsInfo(int id) {
        return addSchedulers(getApi().getNewDetailInfo(id));
    }

    /**
     * 取消点赞或点赞
     *
     * @return
     */
    public Observable<String> setLike(int id) {
        return addSchedulers(getApi().setLike(id));
    }

    /**
     * 获取品论列表
     *
     * @return
     */
    public Observable<List<CommentEntity>> getCommentList(int id, int start, int size, int sort) {
        return addSchedulers(getApi().getListComment(id, start, size, sort));
    }

    /**
     * 获取子评论的列表
     *
     * @return
     */
    public Observable<List<CommentEntity>> getChildListComment(int id, int start, int size) {
        return addSchedulers(getApi().getChildListComment(id, start, size));
    }


    /**
     * 增加一条评论
     *
     * @return
     */
    public Observable<String> addComment(Long newsId, Long parentId, Long replyId, int level, String comment) {
        return addSchedulers(getApi().addComment(newsId, parentId, replyId, level, comment));
    }


    /**
     * 点赞或取消点赞评论
     *
     * @return
     */
    public Observable<String> addLikeComment(Long commentId) {
        return addSchedulers(getApi().addLikeComment(commentId));
    }

    /**
     * 获取消息未读数
     *
     * @return
     */
    public Observable<MsgUnreadCountEntity> getUnreadCount() {
        return addSchedulers(getApi().getUnreadCount());
    }


    /**
     * 获取点赞消息
     *
     * @return
     */
    public Observable<List<DianZanEntity>> getUserLikeList(int start, int size) {
        return addSchedulers(getApi().getUserLikeList(start, size));
    }


    /**
     * 获取通知消息
     *
     * @return
     */
    public Observable<List<MsgNoticeEntity>> getNoticeList(int start, int size) {
        return addSchedulers(getApi().getNoticeList(start, size));
    }


    /**
     * 获取小助手消息
     *
     * @return
     */
    public Observable<List<MsgNoticeEntity>> getReviewList(int start, int size) {
        return addSchedulers(getApi().getReviewList(start, size));
    }


    /**
     * 获取消息首页数据列表
     *
     * @return
     */
    public Observable<List<MsgNoticeEntity>> getIndexList(int start, int size) {
        return addSchedulers(getApi().getIndexList(start, size));
    }

    /**
     * 获取评论消息列表
     *
     * @return
     */
    public Observable<List<MsgCommentEntity>> getUserCommentList(int start, int size) {
        return addSchedulers(getApi().getUserCommentList(start, size));
    }

    /**
     * 设置消息已读
     *
     * @return
     */
    public Observable<String> setMsgRead(int id) {
        return addSchedulers(getApi().setMsgRead(id));
    }

    /**
     * 删除某条消息
     *
     * @return
     */
    public Observable<String> deleteMsg(int id) {
        return addSchedulers(getApi().deleteMsg(id));
    }

    /**
     * 设置全部已读
     *
     * @return
     */
    public Observable<String> setAllIsRead() {
        return addSchedulers(getApi().setAllIsRead());
    }
}


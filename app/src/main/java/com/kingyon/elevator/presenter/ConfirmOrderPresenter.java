package com.kingyon.elevator.presenter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.blankj.utilcode.util.GsonUtils;
import com.blankj.utilcode.util.LogUtils;
import com.kingyon.elevator.application.AppContent;
import com.kingyon.elevator.constants.Constants;
import com.kingyon.elevator.entities.ADEntity;
import com.kingyon.elevator.entities.AutoCalculationDiscountEntity;
import com.kingyon.elevator.entities.CellItemEntity;
import com.kingyon.elevator.entities.CommitOrderEntiy;
import com.kingyon.elevator.entities.CouponItemEntity;
import com.kingyon.elevator.entities.DeviceParamCache;
import com.kingyon.elevator.entities.GoPlaceAnOrderEntity;
import com.kingyon.elevator.entities.OrderDetailsEntity;
import com.kingyon.elevator.entities.OrderIdentityEntity;
import com.kingyon.elevator.entities.PointItemEntity;
import com.kingyon.elevator.mvpbase.BasePresenter;
import com.kingyon.elevator.nets.CustomApiCallback;
import com.kingyon.elevator.nets.NetService;
import com.kingyon.elevator.nets.NetUpload;
import com.kingyon.elevator.uis.activities.order.OrderPayActivity;
import com.kingyon.elevator.uis.activities.order.PaySuccessActivity;
import com.kingyon.elevator.utils.CommonUtil;
import com.kingyon.elevator.utils.MyActivityUtils;
import com.kingyon.elevator.view.ConfirmOrderView;
import com.leo.afbaselibrary.nets.exceptions.ApiException;
import com.leo.afbaselibrary.nets.exceptions.ResultException;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created By SongPeng  on 2019/12/23
 * Email : 1531603384@qq.com
 */
public class ConfirmOrderPresenter extends BasePresenter<ConfirmOrderView> {

    /**
     * 上传过后的资源文件
     */
    private String uploadUrl = "";

    public ConfirmOrderPresenter(Context mContext) {
        super(mContext);
    }


    /**
     * 加载自动计算的优惠信息
     *
     * @param amount 总金额
     * @param adType 广告类型
     */
    public void loadCouponsInfo(double amount, String adType, Boolean isManual, String consIds) {
        NetService.getInstance().getCouponsInfo(amount, adType, isManual, consIds)
                .subscribe(new CustomApiCallback<AutoCalculationDiscountEntity>() {
                    @Override
                    protected void onResultError(ApiException ex) {
                        if (isViewAttached()) {
                            getView().showShortToast(ex.getDisplayMessage());
                            getView().showErrorView();
                        }
                    }

                    @Override
                    public void onNext(AutoCalculationDiscountEntity autoCalculationDiscountEntity) {
                        if (isViewAttached()) {
                            getView().showCouponsInfo(autoCalculationDiscountEntity);
                            getView().showContentView();
                        }
                    }
                });
    }

    /**
     * 获取用户是否认证的信息
     */
    public void loadIdentityInfo(double amount, String adType, Boolean isManual, String consIds) {
        if (isViewAttached()) {
            getView().showProgressView();
        }
        NetService.getInstance().orderIdentityInfo()
                .subscribe(new CustomApiCallback<OrderIdentityEntity>() {
                    @Override
                    protected void onResultError(ApiException ex) {
                        if (isViewAttached()) {
                            getView().showShortToast(ex.getDisplayMessage());
                            getView().showErrorView();
                        }
                    }

                    @Override
                    public void onNext(OrderIdentityEntity orderIdentityEntity) {
                        if (isViewAttached()) {
                            getView().setIdentityInfo(orderIdentityEntity);
                        }
                        loadCouponsInfo(amount, adType, isManual, consIds);
                    }
                });
    }


    /**
     * 上传视频或图片
     *
     * @param resPath  视频或图片路径
     * @param planType 广告的类型
     */
    public void uploadAdVideoOrImg(String resPath, String planType, String screenType, String adName) {
        if (isViewAttached()) {
            getView().showProgressDialog("文件上传中，请等待...", false);
        }
        Handler handler = new Handler();
        NetService.getInstance().uploadFileNoActivity(mContext, new File(resPath), new NetUpload.OnUploadCompletedListener() {
            @Override
            public void uploadSuccess(List<String> images) {
                handler.post(() -> {
                    if (images != null && images.size() > 0) {
                        uploadUrl = images.get(0);
                        commitAd(uploadUrl, planType, screenType, adName, resPath);
                    } else {
                        if (isViewAttached()) {
                            getView().hideProgressDailog();
                            getView().showShortToast("上传图片失败，请重试");
                        }
                    }
                });
            }

            @Override
            public void uploadFailed(ApiException ex) {
                handler.post(() -> {
                    if (isViewAttached()) {
                        getView().hideProgressDailog();
                        getView().showShortToast("图片上传失败");
                    }
                });
            }
        }, false);
    }


    /**
     * 提交广告到服务器
     *
     * @param url
     * @param planType   广告计划的类型
     * @param screenType 全屏图片还是视频
     */
    public void commitAd(String url, String planType, String screenType, String adName, String localPath) {
        String videoUrl = "";
        String imageUrl = "";
        String videoLocalPath = "";
        String imageLocalPath = "";
        if (screenType.equals(Constants.AD_SCREEN_TYPE.FULL_IMAGE)) {
            imageUrl = url;
            imageLocalPath = localPath;
        }
        if (screenType.equals(Constants.AD_SCREEN_TYPE.FULL_VIDEO)) {
            videoUrl = url;
            videoLocalPath = localPath;
        }
        NetService.getInstance().createOrEidtAd(null, false
                , planType, screenType, adName, videoUrl, imageUrl, null, videoLocalPath, imageLocalPath)
                .subscribe(new CustomApiCallback<ADEntity>() {
                    @Override
                    protected void onResultError(ApiException ex) {
                        if (isViewAttached()) {
                            getView().hideProgressDailog();
                            getView().showShortToast(ex.getDisplayMessage());
                        }
                    }

                    @Override
                    public void onNext(ADEntity entity) {
                        if (isViewAttached()) {
                            getView().hideProgressDailog();
                            getView().adUploadSuccess(entity);
                        }
                    }
                });
    }


    public void commitOrder(GoPlaceAnOrderEntity goPlaceAnOrderEntity, List<CouponItemEntity> couponItemEntityList, String planType, long startTime, long endTime, ADEntity adEntity) {
        if (isViewAttached()) {
            getView().showProgressDialog("订单提交中，请稍候...", false);
        }
        List<DeviceParamCache> deviceParams = new ArrayList<>();
        if (goPlaceAnOrderEntity.getCellItemEntityArrayList() != null) {
            for (CellItemEntity item : goPlaceAnOrderEntity.getCellItemEntityArrayList()) {
                ArrayList<PointItemEntity> points = item.getPoints();
                if (points == null || points.size() > 0) {
                    DeviceParamCache cache = new DeviceParamCache();
                    cache.setCellId(item.getObjctId());
                    cache.setCount(item.getChoosedScreenNum());
                    if (points != null) {
                        StringBuilder stringBuilder = new StringBuilder();
                        for (PointItemEntity point : points) {
                            stringBuilder.append(point.getObjectId()).append(",");
                        }
                        cache.setDeviceIds(stringBuilder.length() > 1 ? stringBuilder.substring(0, stringBuilder.length() - 1) : "");
                    } else {
                        cache.setDeviceIds(null);
                    }
                    deviceParams.add(cache);
                }
            }
        }
        if (deviceParams.size() < 1) {
            getView().showShortToast("投放数量不能为0");
            return;
        }
        String couponIds = "";
        if (couponItemEntityList != null) {
            StringBuilder stringBuilder = new StringBuilder();
            for (CouponItemEntity item : couponItemEntityList) {
                stringBuilder.append(item.getObjctId()).append(",");
            }
            couponIds = stringBuilder.length() > 1 ? stringBuilder.substring(0, stringBuilder.length() - 1) : "";
        }
        NetService.getInstance().commitOrder(planType, startTime, endTime, adEntity.getObjctId()
                , AppContent.getInstance().getGson().toJson(deviceParams), couponIds, 100053)
                .subscribe(new CustomApiCallback<CommitOrderEntiy>() {
                    @Override
                    protected void onResultError(ApiException ex) {
                        if (isViewAttached()) {
                            getView().showShortToast(ex.getDisplayMessage());
                            getView().hideProgressDailog();
                        }
                    }

                    @Override
                    public void onNext(CommitOrderEntiy orderEntiy) {
                        if (isViewAttached()) {
                            getView().hideProgressDailog();
                            LogUtils.d("订单提交成功：", GsonUtils.toJson(orderEntiy));
                            getView().orderCommitSuccess(orderEntiy);
                        }
                    }
                });
    }

    /**
     * 发布便民信息
     *
     * @param informationContent
     */
    public void sendInformation(String informationContent) {
        if (isViewAttached()) {
            getView().showProgressDialog("便民信息发布中...", false);
        }
        NetService.getInstance().createOrEidtAd(null
                , true, Constants.PLAN_TYPE.INFORMATION, Constants.AD_SCREEN_TYPE.INFORMATION
                , informationContent, null, null, null, null, null)
                .subscribe(new CustomApiCallback<ADEntity>() {
                    @Override
                    protected void onResultError(ApiException ex) {
                        if (isViewAttached()) {
                            getView().showShortToast(ex.getDisplayMessage());
                            getView().hideProgressDailog();
                        }
                    }

                    @Override
                    public void onNext(ADEntity adEntity) {
                        if (isViewAttached()) {
                            getView().hideProgressDailog();
                            getView().adUploadSuccess(adEntity);
                        }
                    }
                });
    }

    /**
     * 加载手动选择优惠券后的信息
     *
     * @param amount 总金额
     * @param adType 广告类型
     */
    public void loadCouponsNewInfo(double amount, String adType, List<CouponItemEntity> couponItemEntityList) {
        if (isViewAttached()) {
            getView().showProgressDialog("请稍候...", false);
        }
        String couponIds = "";
        if (couponItemEntityList != null) {
            StringBuilder stringBuilder = new StringBuilder();
            for (CouponItemEntity item : couponItemEntityList) {
                stringBuilder.append(item.getObjctId()).append(",");
            }
            couponIds = stringBuilder.length() > 1 ? stringBuilder.substring(0, stringBuilder.length() - 1) : "";
        }
        NetService.getInstance().getCouponsInfo(amount, adType, true, couponIds)
                .subscribe(new CustomApiCallback<AutoCalculationDiscountEntity>() {
                    @Override
                    protected void onResultError(ApiException ex) {
                        if (isViewAttached()) {
                            getView().showShortToast(ex.getDisplayMessage());
                            getView().showErrorView();
                            getView().hideProgressDailog();
                        }
                    }

                    @Override
                    public void onNext(AutoCalculationDiscountEntity autoCalculationDiscountEntity) {
                        if (isViewAttached()) {
                            getView().showManualSelectCouponsInfo(autoCalculationDiscountEntity);
                            getView().hideProgressDailog();
                        }
                    }
                });
    }

}

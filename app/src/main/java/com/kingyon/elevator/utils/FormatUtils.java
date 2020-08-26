package com.kingyon.elevator.utils;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.ImageView;

import com.czh.myversiontwo.utils.StringContent;
import com.kingyon.elevator.R;
import com.kingyon.elevator.constants.Constants;
import com.kingyon.elevator.data.DataSharedPreferences;
import com.kingyon.elevator.entities.ADEntity;
import com.kingyon.elevator.entities.CellGroupEntity;
import com.kingyon.elevator.entities.LatLonCache;
import com.kingyon.elevator.entities.OrderDetailsEntity;
import com.kingyon.elevator.entities.PlanPointGroup;
import com.kingyon.elevator.entities.PointItemEntity;
import com.kingyon.elevator.utils.utilstwo.StringUtils;
import com.leo.afbaselibrary.utils.GlideUtils;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * Created by GongLi on 2018/12/25.
 * Email：lc824767150@163.com
 */

public class FormatUtils {
    private static FormatUtils formatUtils;

    private FormatUtils() {
    }

    public static FormatUtils getInstance() {
        if (formatUtils == null) {
            formatUtils = new FormatUtils();
        }
        return formatUtils;
    }

    public String getCellLift(int liftNum) {
        return String.format("%s部电梯", liftNum);
    }

    public String getCellUnit(int unitNum) {
        return String.format("%s个单元", unitNum);
    }

    public String getCellScreen(int screenNum) {
        return String.format("%s面屏", screenNum);
    }

    public SpannableString getCellPrice(float price) {
        String priceStr = String.format("￥%s/天", CommonUtil.getMayTwoFloat(price));
        SpannableString result = new SpannableString(priceStr);
        int index1 = priceStr.indexOf("￥");
        int index2 = index1 + 1;
        int index3 = priceStr.indexOf("/");
        int index4 = priceStr.length();
        result.setSpan(new ForegroundColorSpan(0xFFABABAB), index1, index2, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        result.setSpan(new ForegroundColorSpan(0xFFff3049), index3, index4, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        result.setSpan(new AbsoluteSizeSpan(13, true), index1, index2, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        result.setSpan(new AbsoluteSizeSpan(13, true), index3, index4, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        return result;
    }

    public String getCellDistance(double longitude, double latitude, float distance) {
        String result;
        LatLonCache latLonCache = DataSharedPreferences.getLatLon();
        if (latLonCache != null) {
            double distanceCalcute = getDistance(longitude, latitude, latLonCache.getLongitude(), latLonCache.getLatitude());
            if (distanceCalcute >= 1000) {
                result = String.format("<%skm", CommonUtil.getMayOneFloat(distanceCalcute / 1000));
            } else {
                result = String.format("<%sm", CommonUtil.getOneDigits(distanceCalcute));
            }
        } else {
            result = "";
        }
        return result;
//        return String.format("<%sm", CommonUtil.getOneDigits(distance));
    }

    public Double getCellDistanceNum(double longitude, double latitude, float distance) {
        Double result;
        LatLonCache latLonCache = DataSharedPreferences.getLatLon();
        if (latLonCache != null) {
            result = getDistance(longitude, latitude, latLonCache.getLongitude(), latLonCache.getLatitude());
        } else {
            result = null;
        }
        return result;
//        return String.format("<%sm", CommonUtil.getOneDigits(distance));
    }

    /**
     * 根据经纬度获取两点间的距离
     *
     * @param lon1 起点经度
     * @param lat1 起点纬度
     * @param lon2 终点经度
     * @param lat2 终点经度
     * @return 起终点之间的距离
     */
    public double getDistance(double lon1, double lat1, double lon2, double lat2) {
        double radLat1 = rad(lat1);
        double radLat2 = rad(lat2);
        double a = radLat1 - radLat2;
        double b = rad(lon1) - rad(lon2);
        double s = 2 * Math.asin(Math.sqrt(Math.pow(Math.sin(a / 2), 2) + Math.cos(radLat1) * Math.cos(radLat2) * Math.pow(Math.sin(b / 2), 2)));
        double earth_radius = 6378137;
        s = s * earth_radius;
        return s;//单位米
    }

    private double rad(double d) {
        return d * Math.PI / 180.0;
    }

    public String getCityName(String cityName) {
        String result;
        if (!TextUtils.isEmpty(cityName) && cityName.endsWith("市")) {
            result = cityName.substring(0, cityName.length() - 1);
        } else {
            result = cityName;
        }
        return result;
    }

    public void updateAdCoverShow(Context context, ADEntity adEntity
            , View fullVideo, ImageView fullVideoImg
            , ImageView fullImageImg
            , View incise, ImageView top, ImageView bottom) {
        switch (adEntity.getScreenType()) {
            case Constants.AD_SCREEN_TYPE.FULL_VIDEO:
                fullVideo.setVisibility(View.VISIBLE);
                fullImageImg.setVisibility(View.GONE);
                incise.setVisibility(View.GONE);
                GlideUtils.loadRoundCornersImage(context, adEntity.getVideoUrl(), fullVideoImg,20);
                break;
            case Constants.AD_SCREEN_TYPE.FULL_IMAGE:
                fullVideo.setVisibility(View.GONE);
                fullImageImg.setVisibility(View.VISIBLE);
                incise.setVisibility(View.GONE);
                GlideUtils.loadImage(context, adEntity.getImageUrl(), fullImageImg);
                break;
            case Constants.AD_SCREEN_TYPE.VIDEO_IMAGE:
                fullVideo.setVisibility(View.GONE);
                fullImageImg.setVisibility(View.GONE);
                incise.setVisibility(View.VISIBLE);
                GlideUtils.loadVideoFrame(context, adEntity.getVideoUrl(), top);
                GlideUtils.loadImage(context, adEntity.getImageUrl(), bottom);
                break;
            default:
                fullVideo.setVisibility(View.GONE);
                fullImageImg.setVisibility(View.GONE);
                incise.setVisibility(View.GONE);
                break;
        }
    }

    public double[] getCenterLatLon(String center) {
        if (TextUtils.isEmpty(center)) {
            return new double[]{0, 0};
        }
        String[] split = center.split(",");
        double[] result;
        if (split.length == 2) {
            result = new double[]{Double.parseDouble(split[0]), Double.parseDouble(split[1])};
        } else {
            result = new double[]{0, 0};
        }
        return result;
    }

    public String getCellType(String cellType) {
        if (cellType == null) {
            return "";
        }
        String result;
        switch (cellType) {
            case Constants.CELL_TYPE.COMMERCIAL:
                result = "商业";
                break;
            case Constants.CELL_TYPE.HOUSE:
                result = "住宅";
                break;
            case Constants.CELL_TYPE.OFFICE:
                result = "写字楼";
                break;
            default:
                result = "";
                break;
        }
        return result;
    }


    public String getthrowWay(String throwWay) {
        if (throwWay == null) {
            return "";
        }
        String result;
        switch (throwWay) {
            case "VIDEO":
                result = "视频";
            break;
            case "VIDEO_AND_POSTER":
                result =  "视频和海报";
            break;
            case "POSTER":
                result = "海报";
            break;
            default:
                result =  throwWay;
                break;
        }
        return result;
    }

public static String incomeType(String  source){
    if (source == null) {
        return "";
    }
    String result;
    switch (source) {
        case Constants.INCOME_TYPE.EARNINGS:
            result = "广告";
            break;
        case Constants.INCOME_TYPE.POINT_MONEY_PAY:
            result = "物业";
            break;
        case Constants.INCOME_TYPE.BROADBAND_MONEY_PAY:
        case Constants.INCOME_TYPE.BANDWIDTH_PAY:
            result = "光纤";
            break;
        default:
            result = "";
            break;
    }
    return result;
}


    public List<CellGroupEntity> getCellGroups(List<PointItemEntity> pointItemEntities, List<PointItemEntity> chooseds) {
        LinkedHashMap<String, CellGroupEntity> map = new LinkedHashMap<>();
        if (pointItemEntities != null) {
            for (PointItemEntity item : pointItemEntities) {
                item.setChoosed(hasChoosed(chooseds, item.getObjectId()));
                String keyStr = String.valueOf(item.getCellId());
                CellGroupEntity pointGroup;
                if (map.containsKey(keyStr)) {
                    pointGroup = map.get(keyStr);
                } else {
                    pointGroup = new CellGroupEntity(keyStr);
                    pointGroup.setCellName(item.getCellName());
                    pointGroup.setCellId(item.getCellId());
                }
                pointGroup.addPoint(item);
                map.put(keyStr, pointGroup);
            }
        }
        ArrayList<CellGroupEntity> groups = new ArrayList<>();
        groups.addAll(map.values());
        return groups;
    }

    public List<PlanPointGroup> getPlanPointGroup(List<PointItemEntity> pointItemEntities, List<PointItemEntity> chooseds) {
        LinkedHashMap<String, PlanPointGroup> map = new LinkedHashMap<>();
        if (pointItemEntities != null) {
            for (PointItemEntity item : pointItemEntities) {
                item.setChoosed(hasChoosed(chooseds, item.getObjectId()));
                String keyStr = String.format("%s-%s", item.getCellId(), item.getBuildId());
                PlanPointGroup pointGroup;
                if (map.containsKey(keyStr)) {
                    pointGroup = map.get(keyStr);
                } else {
                    pointGroup = new PlanPointGroup(keyStr);
                    pointGroup.setCellName(item.getCellName());
                    pointGroup.setCellId(item.getCellId());
                    pointGroup.setBuildName(item.getBuild());
                    pointGroup.setBuildId(item.getBuildId());
                }
                pointGroup.addPoint(item);
                map.put(keyStr, pointGroup);
            }
        }
        ArrayList<PlanPointGroup> groups = new ArrayList<>();
        groups.addAll(map.values());
        return groups;
    }

    public List<PlanPointGroup> getPlanPointGroupAssign(List<PointItemEntity> pointItemEntities, List<PointItemEntity> chooseds,
                                                        int choosedNumber,String type) {
        if (type.equals("order")) {
            LinkedHashMap<String, PlanPointGroup> map = new LinkedHashMap<>();
            if (pointItemEntities != null) {
                int cacheNumber = 0;
                for (PointItemEntity item : pointItemEntities) {
                    if (chooseds == null || chooseds.size() < 1) {
                        if (cacheNumber < choosedNumber && TextUtils.equals(Constants.DELIVER_STATE.USABLE, item.getDeliverState())) {
                            cacheNumber++;
//                            item.setChoosed(true);
                        } else {
//                            item.setChoosed(false);
                        }
                    } else {
                        if (TextUtils.equals(Constants.DELIVER_STATE.USABLE, item.getDeliverState()) && hasChoosed(chooseds, item.getObjectId())) {
                            item.setChoosed(true);
                        } else {
                            item.setChoosed(false);
                        }
                    }
                    String keyStr = String.format("%s-%s", item.getCellId(), item.getBuildId());
                    PlanPointGroup pointGroup;
                    if (map.containsKey(keyStr)) {
                        pointGroup = map.get(keyStr);
                    } else {
                        pointGroup = new PlanPointGroup(keyStr);
                        pointGroup.setCellName(item.getCellName());
                        pointGroup.setCellId(item.getCellId());
                        pointGroup.setBuildName(item.getBuild());
                        pointGroup.setBuildId(item.getBuildId());
                    }
                    pointGroup.addPoint(item);
                    map.put(keyStr, pointGroup);
                }
            }
            ArrayList<PlanPointGroup> groups = new ArrayList<>();
            groups.addAll(map.values());
            return groups;
        }else {
            LinkedHashMap<String, PlanPointGroup> map = new LinkedHashMap<>();
            if (pointItemEntities != null) {
                int cacheNumber = 0;
                for (PointItemEntity item : pointItemEntities) {
                    if (chooseds == null || chooseds.size() < 1) {
                        if (cacheNumber < choosedNumber && TextUtils.equals(Constants.DELIVER_STATE.USABLE, item.getDeliverState())) {
                            cacheNumber++;
                            item.setChoosed(true);
                        } else {
                            item.setChoosed(false);
                        }
                    } else {
                        if (TextUtils.equals(Constants.DELIVER_STATE.USABLE, item.getDeliverState()) && hasChoosed(chooseds, item.getObjectId())) {
                            item.setChoosed(true);
                        } else {
                            item.setChoosed(false);
                        }
                    }
                    String keyStr = String.format("%s-%s", item.getCellId(), item.getBuildId());
                    PlanPointGroup pointGroup;
                    if (map.containsKey(keyStr)) {
                        pointGroup = map.get(keyStr);
                    } else {
                        pointGroup = new PlanPointGroup(keyStr);
                        pointGroup.setCellName(item.getCellName());
                        pointGroup.setCellId(item.getCellId());
                        pointGroup.setBuildName(item.getBuild());
                        pointGroup.setBuildId(item.getBuildId());
                    }
                    pointGroup.addPoint(item);
                    map.put(keyStr, pointGroup);
                }
            }
            ArrayList<PlanPointGroup> groups = new ArrayList<>();
            groups.addAll(map.values());
            return groups;
        }
    }

    private boolean hasChoosed(List<PointItemEntity> chooseds, long objectId) {
        boolean result = false;
        if (chooseds != null) {
            for (PointItemEntity item : chooseds) {
                if (item.getObjectId() == objectId) {
                    result = true;
                    break;
                }
            }
        } else {
            result = true;
        }
        return result;
    }

    public String getPlanType(String type) {
        if (type == null) {
            return "";
        }
        String result;
        switch (type) {
            case Constants.PLAN_TYPE.BUSINESS:
                result = "商业广告";
                break;
            case Constants.PLAN_TYPE.DIY:
                result = "DIY";
                break;
            case Constants.PLAN_TYPE.INFORMATION:
                result = "便民服务";
                break;
            default:
                result = "";
                break;
        }
        return result;
    }

    public boolean beInfomationPlan(String type) {
        if (type == null) {
            return false;
        }
        return TextUtils.equals(type, Constants.PLAN_TYPE.INFORMATION);
    }


    public int getTimeDays(Long start, Long end) {
        if (start == null || end == null) {
            return 0;
        }
        long startTime = start.longValue();
        long endTime = end.longValue() + 1;

        Calendar startCalendar = Calendar.getInstance();
        startCalendar.setTimeInMillis(startTime);

        Calendar endCalendar = Calendar.getInstance();
        endCalendar.setTimeInMillis(endTime);
        int betweenDays = (int) ((endTime - startTime) / (1000 * 60 * 60 * 24));//先算出两时间的毫秒数之差大于一天的天数

        int result;
        endCalendar.add(Calendar.DAY_OF_MONTH, -betweenDays);//使endCalendar减去这些天数，将问题转换为两时间的毫秒数之差不足一天的情况
        endCalendar.add(Calendar.DAY_OF_MONTH, -1);//再使endCalendar减去1天
        if (startCalendar.get(Calendar.DAY_OF_MONTH) == endCalendar.get(Calendar.DAY_OF_MONTH))//比较两日期的DAY_OF_MONTH是否相等
            result = betweenDays + 1;    //相等说明确实跨天了
        else
            result = betweenDays;    //不相等说明确实未跨天
        return result;
    }

    public String getOrderStatusStr(String orderStatus) {
        if (orderStatus == null) {
            return "";
        }
        String result;
        switch (orderStatus) {
            case Constants.OrderStatus.WAIT_PAY:
                result = "等待付款";
                break;
            case Constants.OrderStatus.CANCEL:
                result = "已取消";
                break;
            case Constants.OrderStatus.WAIT_RELEASE:
                result = "待发布";
                break;
            case Constants.OrderStatus.RELEASEING:
                result = "发布中";
                break;
            case Constants.OrderStatus.COMPLETE:
                result = "已完成";
                break;
            case Constants.OrderStatus.FAILED:
                result = "已失败";
                break;
            case Constants.OrderStatus.OVERDUE:
                result = "已过期";
                break;
            case Constants.OrderStatus.SOWING:
                result = "已下播";
                break;
            default:
                result = "";
                break;
        }
        return result;
    }

    public String getOrderStatusStr(OrderDetailsEntity entity) {
        String orderStatus = entity.getOrderStatus();
        if (orderStatus == null) {
            return "";
        }
        String result;
        switch (orderStatus) {
            case Constants.OrderStatus.WAIT_PAY:
                result = "等待付款";
                break;
            case Constants.OrderStatus.CANCEL:
                result = "已取消";
                break;
            case Constants.OrderStatus.WAIT_RELEASE:
//                result = "待发布";
                result = getWaitReleaseOrderStatusStr(entity);
                break;
            case Constants.OrderStatus.RELEASEING:
                result = "发布中";
                break;
            case Constants.OrderStatus.COMPLETE:
                result = "已完成";
                break;
            case Constants.OrderStatus.FAILED:
                result = "已失败";
                break;
            case Constants.OrderStatus.OVERDUE:
                result = "已过期";
                break;
            case Constants.OrderStatus.SOWING:
                result = "已下播";
                break;
            default:
                result = "";
                break;
        }
        return result;
    }

    public String getWaitReleaseOrderStatusStr(OrderDetailsEntity order) {
        String result;
        ADEntity advertising = order.getAdvertising();
        String adStatus = advertising != null && advertising.getAdStatus() != null ? advertising.getAdStatus() : "";
        switch (adStatus) {
            case Constants.AD_STATUS.REVIEW_FAILED:
                result = "未通过";
                break;
            case Constants.AD_STATUS.WAIT_REVIEW:
                result = "待审核";
                break;
            default:
                result = "待发布";
                break;
        }
        return result;
    }

    public String getOrderOperateStr(String orderStatus) {
        if (orderStatus == null) {
            return "";
        }
        String result;
        switch (orderStatus) {
            case Constants.OrderStatus.WAIT_PAY:
                result = "去付款";
                break;
            case Constants.OrderStatus.CANCEL:
                result = "删除订单";
                break;
            case Constants.OrderStatus.WAIT_RELEASE:
                result = "";
                break;
            case Constants.OrderStatus.RELEASEING:
                result = "";
                break;
            case Constants.OrderStatus.COMPLETE:
                result = "删除订单";
                break;
            case Constants.OrderStatus.FAILED:
                result = "删除订单";
                break;
            case Constants.OrderStatus.OVERDUE:
                result = "删除订单";
                break;
            case Constants.OrderStatus.SOWING:
                result = "删除订单";
                break;
            default:
                result = "";
                break;
        }
        return result;
    }

    public String getPayWay(String payWay) {
        if (payWay == null) {
            return "";
        }
        String result;
        switch (payWay) {
            case Constants.PayType.ALI_PAY:
                result = "支付宝支付";
                break;
            case Constants.PayType.WX_PAY:
                result = "微信支付";
                break;
            case Constants.PayType.BALANCE_PAY:
                result = "余额支付";
                break;
            case Constants.PayType.FREE:
                result = "免费";
                break;
            case Constants.PayType.APPLY:
                result = "苹果支付";
                break;
            case Constants.PayType.OFFLINE:
                result = "线下支付";
                break;
            case Constants.PayType.COUPON:
                result = "优惠券支付";
                break;
            case Constants.PayType.ALI_COUPON:
                result = "支付宝+优惠券支付";
                break;
            case Constants.PayType.WX_PAY_COUPON:
                result = "微信+优惠券支付";
                break;
            case Constants.PayType.BALANCE_COUPON:
                result = "余额+优惠券支付";
                break;
            default:
                result = "";
                break;
        }
        return result;
    }

    public String getWithdrawState(String status,String faildReason) {
        if (status == null) {
            return "";
        }
        String result;
        switch (status) {
            case Constants.Withdraw_Status.DEALING:
                result = String.format(StringContent.REVIEW_STATUS3,"审核中");
                break;
            case Constants.Withdraw_Status.SUCCESS:
                result = String.format(StringContent.REVIEW_STATUS1,"通过");
                break;
            case Constants.Withdraw_Status.FAILED:
                result = String.format(StringContent.REVIEW_STATUS2,"未通过("+faildReason+")");
                break;
            default:
                result = "";
                break;
        }
        return result;
    }

    public String getDeviceOritation(String device) {
        String result;
        if (device != null) {
            switch (device) {
                case Constants.DEVICE_PLACE.LEFT:
                    result = "左屏";
                    break;
                case Constants.DEVICE_PLACE.CENTER:
                    result = "中屏";
                    break;
                case Constants.DEVICE_PLACE.RIGHT:
                    result = "右屏";
                    break;
                default:
                    result = "";
                    break;
            }
        } else {
            result = "";
        }
        return result;
    }

    public Drawable getDeviceOritationDrawable(Context context, String device) {
        Drawable drawable;
        if (device != null) {
            switch (device) {
                case Constants.DEVICE_PLACE.LEFT:
                    drawable = ContextCompat.getDrawable(context, R.mipmap.ic_screen_left);
                    break;
                case Constants.DEVICE_PLACE.CENTER:
                    drawable = ContextCompat.getDrawable(context, R.mipmap.ic_screen_middle);
                    break;
                case Constants.DEVICE_PLACE.RIGHT:
                    drawable = ContextCompat.getDrawable(context, R.mipmap.ic_screen_right);
                    break;
                default:
                    drawable = null;
                    break;
            }
        } else {
            drawable = null;
        }
        return drawable;
    }

    public String getLiveLiftNo(String liveAddress) {
        String result;
        if (!TextUtils.isEmpty(liveAddress)) {
            if (liveAddress.contains("/") && !liveAddress.endsWith("/")) {
                result = liveAddress.substring(liveAddress.lastIndexOf("/") + 1, liveAddress.length());
            } else {
                result = "";
            }
        } else {
            result = null;
        }
        return result;
    }

    public String getDeviceNo(String deviceStr) {
        String result;
        if (!TextUtils.isEmpty(deviceStr)) {
            if ((deviceStr.startsWith("http://pdd.tlwgz.com/api/v1/check/") || deviceStr.startsWith("https://portal.tlwgz.com/api/v1/check/")) && !deviceStr.endsWith("check/")) {
                result = deviceStr.substring(deviceStr.lastIndexOf("check/") + 6);
            } else {
                result = "";
            }
        } else {
            result = null;
        }
        return result;
    }

    public String getInvoiceType(String invoiceType) {
        String result;
        if (invoiceType != null) {
            switch (invoiceType) {
                case Constants.INVOICE_TYPE.COMPANY:
                    result = "企业发票";
                    break;
                case Constants.INVOICE_TYPE.PERSON:
                    result = "个人发票";
                    break;
                default:
                    result = "";
                    break;
            }
        } else {
            result = "";
        }
        return result;
    }

    public CharSequence getAdStatus(String status) {
        String result;
        if (status != null) {
            switch (status) {
                case Constants.AD_STATUS.NO_AUDIT:
                    result = "";
                    break;
                case Constants.AD_STATUS.REVIEW_SUCCESS:
                    result = "审核通过";
                    break;
                case Constants.AD_STATUS.WAIT_REVIEW:
                    result = "待审核";
                    break;
                case Constants.AD_STATUS.REVIEW_FAILED:
                    result = "审核失败";
                    break;
                default:
                    result = "";
                    break;
            }
        } else {
            result = "";
        }
        return result;
    }

    public int getPointsListNumber(List<PointItemEntity> points) {
        HashSet<Long> liftIds = new HashSet<>();
        if (points != null) {
            for (PointItemEntity entity : points) {
                liftIds.add(entity.getLiftId());
            }
        }
        return liftIds.size();
    }


    public String withdrawalType(String withDrawWay){
        String result;
        if (withDrawWay != null) {
            switch (withDrawWay) {
                case Constants.WithdrawType.ALI:
                    result = "支付宝";
                    break;
                case Constants.WithdrawType.BANKCARD:
                    result = "银行卡";
                    break;
                case Constants.WithdrawType.WX:
                    result = "微信";
                    break;
                case Constants.WithdrawType.WCHAT:
                    result = "微信";

                    break;
                default:
                    result = "";
                    break;
            }
        } else {
            result = "";
        }
        return result;
    }
}

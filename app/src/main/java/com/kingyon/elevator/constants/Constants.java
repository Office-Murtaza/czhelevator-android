package com.kingyon.elevator.constants;

/**
 * Created by gongli on 2017/7/17 16:57
 * email: lc824767150@163.com
 */

public interface Constants {
    String RD_APP_KEY = "c0b511dfdaf0a5cc";
    String RD_APP_SECRET = "060e62ab0a77b5ebfa585958774e4068wOeflhUWbnyJR/5YHMVe3kUe1pI1W0pL6nquxnGR8ZeURZWGsPeD+hvqx2G6xMC5K4pczwKnQkoas01/UF6KwQfRXX7l57g+MDh3CKrDSiQGHfTZqllSWANizuhVN5SA3YM+ycncy/Hxy3ySvvhQAhnJl3+zSYmvw+X4Rt+aHU7wGHO7WFlcErQCfwfdhPrU4tDr2Ggrs9hCuPRtUmOosLZUsioRS0qNmD9SmyTroFy+15mzh10iDt75IsmQPMkh9R3sICZzqSp99tr9c4epEEXKqv/6bzjmswUCWZY1VTJFFwuObYdeNQUs459m9mQf44jPg2dfk3WWRtMuBFaBh2I60LrsodD1tf/XZf8/kexbtQnpJC9wwzqKOGeW70p1KyL7W80mMsho/34kcmxd3LWh+wLZr+KhM1H0RtYynXw3+piwCc/Am4rApHETRjGnOO2fB1eIswfscRhiEh4hnz4rNLRZeSNLeJkjxUTzXqw=";
    //    float adScreenProperty = 0.85492f;

//    float adScreenProperty = 0.576f;
//    float adVideoProperty = 1.152f;
//    float adImageProperty = 1.152f;

    float adScreenProperty = 0.62176f;//1737*1080++++
    float adVideoProperty = 1f;//1080*1080
    float adImageProperty = 1.64384f;//657*1080

    enum AgreementType {//	HELP：帮助，SERVICE_TERMS：服务条款，AD_TERMS：广告投放协议,USER_RULE：用户协议

        HELP("HELP"), SERVICE_TERMS("SERVICE_TERMS"), AD_TERMS("AD_TERMS"), USER_RULE("USER_RULE");

        private String value;

        AgreementType(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }
    }

    interface BANNER_TYPE {// image:图片 link:链接 example：案例精品 baike：百科 invite:邀请注册 cell:小区详情
        String IMAGE = "IMAGE";
        String LINK = "LINK";
        String EXAMPLE = "EXAMPLE";
        String BAIKE = "BAIKE";
        String INVITE = "INVITE";
        String CELL = "CELL";
    }

    interface RoleType {// 0-普通用户，1-业务员，2-安装员，3-合伙人，4-小区，5-物业
        String NORMAL = "NORMAL";
        String SALESMAN = "SALESMAN";
        String INSTALLER = "INSTALLER";
        String PARTNER = "PARTNER";
        String NEIGHBORHOODS = "NEIGHBORHOOD";
        String PROPERTY = "PROPERTY";
    }

    interface LoginType {//登录方式（nor：密码登录，wx：微信，qq：QQ，wb：新浪微博）
        String NORMAL = "NOR";
        String WX = "WX";
        String QQ = "QQ";
        String SINA = "WB";
    }

    interface PayType {//ali，wechat，balance
        String ALI_PAY = "ALI";
        String WX_PAY = "WECHAT";
        String BALANCE_PAY = "BALANCE";
        String FREE = "FREE";//免费订单
        String APPLY = "APPLY";//内购
        String OFFLINE = "OFFLINE";//线下支付
    }

    interface WithdrawType {//ali，bankCard
        String ALI = "ALI";
        String BANKCARD = "BANKCARD";
    }

    interface OrderStatus {//waitPay:待支付，cancel：取消，waitRelease，待发布，releaseing：发布中，complete:完成 SOWING:下播 OVERDUE:过期
        String WAIT_PAY = "WAITPAY";
        String CANCEL = "CANCEL";
        String WAIT_RELEASE = "WAITRELEASE";
        String RELEASEING = "RELEASEING";
        String COMPLETE = "COMPLETE";
        String FAILED = "FAILED";
        String SOWING = "SOWING";
        String OVERDUE = "OVERDUE";
    }

    interface AD_SCREEN_TYPE {//fullVideo:全屏视频，fullImage:全屏图片，videoImage:上视频下图片，INFORMATION：便民信息
        String FULL_VIDEO = "FULLVIDEO";
        String FULL_IMAGE = "FULLIMAGE";
        String VIDEO_IMAGE = "VIDEOIMAGE";
        String INFORMATION = "INFORMATION";
    }

    interface AD_STATUS {//NO_AUDIT未下订单，无需审核,waitReview:待审核，reviewSuccess:审核通过，reviewFaild:审核失败
        String WAIT_REVIEW = "WAITREVIEW";
        String REVIEW_SUCCESS = "REVIEWSUCCESS";
        String REVIEW_FAILED = "REVIEWFAILD";
        String NO_AUDIT = "NO_AUDIT";
    }

    interface CELL_TYPE {//office_build:写字楼，house_build：住宅，commercial_build:商业楼
        String OFFICE = "OFFICE_BUILD";
        String HOUSE = "HOUSE_BUILD";
        String COMMERCIAL = "COMMERCIAL_BUILD";
    }

    interface PLAN_TYPE {//DIY：DIY广告 BUSINESS:商业广告 INFORMATION:便民信息
        String DIY = "DIY";
        String BUSINESS = "BUSINESS";
        String INFORMATION = "INFORMATION";
    }

    interface DEVICE_PLACE {//L左屏，C中屏，R右屏
        String LEFT = "L";
        String CENTER = "C";
        String RIGHT = "R";
    }

    interface IDENTITY_STATUS {//auth 已认证，faild:失败，authing：认证中 noauth：未认证
        String NO_AUTH = "NOAUTH";
        String AUTHING = "AUTHING";
        String FAILD = "FAILD";
        String AUTHED = "AUTH";
    }

    interface IDENTITY_TYPE {//company:公司，person：个人。
        String COMPANY = "COMPANY";
        String PERSON = "PERSONAL";
    }

    interface INVOICE_TYPE {//company:公司，person：个人。
        String COMPANY = "COMPANY";
        String PERSON = "PERSON";
    }

    interface CouponType {//voucher：代金券，discount：折扣券
        String VOUCHER = "VOUCHER";
        String DISCOUNT = "DISCOUNT";
    }

    interface CouponStatus {//used:"已使用"，expired:已过期，normal：待使用
        String USED = "USED";
        String EXPIRED = "EXPIRED";
        String NORMAL = "NORMAL";
    }

    enum MessageType {//system,review,official
        SYSTEM("SYSTEM", "系统消息"), REVIEW("REVIEW", "消息通知"), OFFICIAL("OFFICIAL", "官方推送");

        private String value;
        private String name;

        MessageType(String value, String name) {
            this.value = value;
            this.name = name;
        }

        public String getValue() {
            return value;
        }

        public String getName() {
            return name;
        }
    }

    interface Materia_Type {//video,image
        String VIDEO = "VIDEO";
        String IMAGE = "IMAGE";
    }

    interface Withdraw_Status {//dealing:处理中，success：成功，failed：失败
        String DEALING = "DEALING";
        String SUCCESS = "SUCCESS";
        String FAILED = "FAILED";
    }

    interface INCOME_FILTER {//筛选类型 （ DAY：天 MONTH：月 YEAR 年）
        String DAY = "DAY";
        String MONTH = "MONTH";
        String YEAR = "YEAR";
    }

    interface MATERIAL_SCREEN_TYPE {//ALL-全部，SPLIT-分屏素材，FULL-全屏
        String ALL = "ALL";
        String SPLIT = "SPLIT";
        String FULL = "FULL";
    }

    interface TEMPLATE_SORT {//NEWEST-最新，RECOMMEND-推荐
        String NEWEST = "NEWEST";
        String RECOMMEND = "RECOMMEND";
    }

    interface TEMPLATE_ELEMENT_TYPE {//image/text
        String IMAGE = "IMAGE";
        String TEXT = "TEXT";
    }

    interface DEVICE_STATUS {//   NORMAL, // 正常{广告投放中} MAINTENANCE, // 维修 ONLINE, // 上线
        String NORMAL = "NORMAL";
        String MAINTENANCE = "MAINTENANCE";
        String ONLINE = "ONLINE";
    }

    interface Device_AD_STATUS {//PROCESSING, // 投放中 LEISURE, // 空闲
        String PROCESSING = "PROCESSING";
        String LEISURE = "LEISURE";
    }

    interface DELIVER_STATE {//USABLE-可用的，MAINTAIN-维修，OCCUPY-被占用
        String USABLE = "USABLE";
        String MAINTAIN = "MAINTAIN";
        String OCCUPY = "OCCUPY";
    }

    interface QueryDataType {//收益详情 查询支出还是收入
        String PayType = "2";
        String IncomeType = "1";
    }


    interface FROM_TYPE {//MYAD:来自我的广告，MEDIADATA：来自多媒体选择。
        int MYAD = 1001;
        int MEDIADATA = 1002;
    }

    interface FROM_TYPE_TO_SELECT_MEDIA {//PLAN:来自于计划下单时选择图片和视频，MYADSELECT：来自于我的广告界面选择视频或图片。
        int PLAN = 1001;
        int MYADSELECT = 1002;
    }
}

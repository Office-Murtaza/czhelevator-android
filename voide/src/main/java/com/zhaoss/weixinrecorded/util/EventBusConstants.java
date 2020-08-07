package com.zhaoss.weixinrecorded.util;

/**
 * Created By SongPeng  on 2019/12/3
 * Email : 1531603384@qq.com
 */
public class EventBusConstants {

    public static int ReflashBindAccountList = 100001; //绑定成功之后，刷新绑定的账户列表
    public static int ClosePayPwdVerCodeSence = 100002; //验证码方式修改密码成功后，关闭验证码接收界面
    public static int ReflashPartnerInfo = 100003; //刷新合伙人信息
    public static int VideoCropSuccessResult = 100004; //视频裁剪成功之后发送消息到广告编辑界面
    public static int AddHomeCellToPlanSuccess = 100005; //首页添加到列表成功
    public static int AddToCartToPlanSuccess = 100006; //小区地图或列表添加到购物车成功
    public static int AdPublishSuccess = 100007; //广告发布成功之后，关闭媒体选择界面等
    public static int VideoOrImageSelectSuccess = 100008; //图片或者视频选择成功
    public static int ReflashPlanCount = 100009; //计划单数据获取成功，刷新底部tab红点
    public static int ReflashPlanList = 1000010; //下单成功之后 刷新计划单列表，因为会删除下单成功的小区

    public static int AdeditSuccess = 1000011; //下单成功之后 刷新计划单列表，因为会删除下单成功的小区
    public static int CommunityReleasety = 1000012; //下单成功之后 刷新计划单列表，因为会删除下单成功的小区
}

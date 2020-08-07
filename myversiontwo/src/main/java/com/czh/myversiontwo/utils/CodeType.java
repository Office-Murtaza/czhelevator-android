package com.czh.myversiontwo.utils;

/**
 * @Created By Admin  on 2020/4/26
 * @Email : 163235610@qq.com
 * @Author:Mrczh
 * @Instructions:
 */
public class CodeType {
 /**
  *验证码类型 REGISTER:注册，RESETPASSWORD:找回密码, UNBIND_OLD:解绑原手机，BIND_NEW:解绑新手机
  * 类型 REGISTER_OR_LOGIN:注册/登陆，RESETPASSWORD:找回密码, UNBIND_OLD:解绑原手机，BIND_NEW:解绑新手机
  * */
  public static String REGISTER = "REGISTER_OR_LOGIN";
  public static String RESETPASSWORD = "RESETPASSWORD";
  public static String UNBIND_OLD = "UNBIND_OLD";
  public static String BIND_NEW = "BIND_NEW";

  /**
   * 登录方式（验证码:CODE，密码：NOR，微信：WX，qq：QQ，新浪微博：WB）
   **/
  public static String CODE = "CODE";
  public static String NOR = "NOR";
  public static String WX = "WX";
  public static String QQ = "QQ";
  public static String WB = "WB";

  /**
   *(query)发布类型：wsq/video/article
   * */
  public static String TYPE_WSQ = "wsq";
  public static String TYPE_VIDEO = "video";
  public static String TYPE_ARTICLE = "article";


  /**
   * 选择视频
   * */

  public static final  int ACCESS_IMAGE_PATH  = 10001;

  public static final  int ACCESS_VOIDE_PATH  = 10002;

  public static final  int ACCESS_VOIDE_COVER  = 10003;

 /**
  *选择视频视频发布中心
  * */

 public static final  int ACCESS_VOIDE_RELEASETY = 10004;

  /**
   * 用户视频
   * */

  public static final  int ACCESS_VOIDE_CODE  = 10010;

  /**
   * 喜欢与否（like—喜欢，cancelLike—取消喜欢）
   *
   * */

  public static final  String LIKE  = "like";
  public static final  String CANCEL_LIKE  = "cancelLike";


  /**
   * （CONTENT/COMMENT）内容/评论
   (query)
   举报类型（CONTENT/COMMENT/…）
   * **/

  public static final  String HOME_CONTENT  = "CONTENT";
  public static final  String HOME_COMMENT  = "COMMENT";

 /**
  * 获取用户类型attention/fans/friend
  * **/

 public static final String ATTENTION = "attention";
 public static final String FANS = "fans";
 public static final String FRIEND = "friend";


 /**
  * 艾特 话题
  * */

 public static final int REQUEST_USER_APPEND = 1 << 2;
 public static final int REQUEST_TAG_APPEND = 1 << 3;


 /**
  * 广告类型（DIY, BUSINESS, INFORMATION）
  * */

 public static final String ADV_DAY = "DIY";
 public static final String ADV_BUSINESS = "BUSINESS";
 public static final String ADV_INFORMATION = "INFORMATION";

 /**
  * 键盘弹框
  * pay 支付
  * setting 设置
  * */
 public static final String KEYBOARD_PAY = "KEYBOARD_PAY";
 public static final String KEYBOARD_SETTING = "KEYBOARD_SETTING";

/**
 * 订单状态（WAITRELEASE-待发布、RELEASEING-发布中、COMPLETE-完成、SOWING-下播）
 * */
 public static final String OEDER_WAITRELEASE = "WAITRELEASE";
 public static final String OEDER_RELEASEING = "RELEASEING";
 public static final String OEDER_COMPLETE = "COMPLETE";
 public static final String OEDER_SOWING = "SOWING";

 /**
  * 审核状态（NEED_AUDIT-待审、WAITREVIEW-审核中、PASS-审核通过、REJECT-拒绝/驳回）
  *
  *        NO_AUDIT, // 不需要审核
  *         WAITREVIEW, // 待审核
  *         REVIEWFAILD, // 审核未通过
  *         REVIEWSUCCESS, // 待发布/审核通过
  *         RELEASEING, // 发布中
  *         COMPLETE, // 完成
  *         SOWING, // 下播
  * */
 /*不需要审核*/
 public static final String NO_AUDIT = "NO_AUDIT";
 /*待审核*/
 public static final String WAITREVIEW = "WAITREVIEW";
 /*审核未通过*/
 public static final String REVIEWFAILD = "REVIEWFAILD";
 /*待发布/审核通过*/
 public static final String REVIEWSUCCESS = "REVIEWSUCCESS";
 /*发布中*/
 public static final String RELEASEING = "RELEASEING";
 /*完成*/
 public static final String COMPLETE = "COMPLETE";
 /*下播*/
 public static final String SOWING = "SOWING";



}

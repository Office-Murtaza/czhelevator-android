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
}

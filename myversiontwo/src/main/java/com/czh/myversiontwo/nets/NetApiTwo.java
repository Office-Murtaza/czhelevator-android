package com.czh.myversiontwo.nets;

import com.blankj.utilcode.util.AppUtils;

/**
 * @Created By Admin  on 2020/4/26
 * @Email : 163235610@qq.com
 * @Author:Mrczh
 * @Instructions:2.0接口
 */
public class NetApiTwo {

    public static String domainReleaseName = "https://api.pddtv.cn/";//外网正式服地址
//    2.0测试接口
    public static String domainDebugName = "http://192.168.1.156:8080/";  //公司测试服
    public static String baseUrl = AppUtils.isAppDebug() ? domainDebugName : domainReleaseName;

    public static String MAIN_CODE = baseUrl + "app/userSecurity/sendCheckCode";

    public static String MAIN_LOGIN = baseUrl + "app/userSecurity/login";

}

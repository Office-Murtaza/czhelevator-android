package com.czh.myversiontwo.utils;

/**
 * @Created By Admin  on 2020/5/27
 * @Email : 163235610@qq.com
 * @Author:Mrczh
 * @Instructions:
 */
public class DistanceUtils {

    public static String distance(int distance){
        if (distance<1000){
            return distance+"m";
        }else {
            return (distance/1000)+"km";
        }
    }
}

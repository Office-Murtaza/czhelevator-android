package com.kingyon.elevator.utils.utilstwo;

import com.google.gson.Gson;

/**
 * @Created By Admin  on 2020/5/13
 * @Email : 163235610@qq.com
 * @Author:Mrczh
 * @Instructions:
 */
public class JsonUtils {

    public static String beanToJson(Object bean) {
        Gson gson = new Gson();
        String jsonStr = gson.toJson(bean);
        System.out.println(jsonStr);
        return jsonStr;
    }
}

package com.leo.afbaselibrary.nets.entities;

import com.google.gson.JsonElement;

/**
 * created by arvin on 16/12/5 10:15
 * email：1035407623@qq.com
 */
public class BBDResultEntity {
    private int code;
    private String msg;
    private JsonElement data;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public JsonElement getData() {
        return data;
    }

    public void setData(JsonElement data) {
        this.data = data;
    }
}

package com.kingyon.elevator.entities.entities;

import org.litepal.crud.DataSupport;

/**
 * @Created By Admin  on 2020/7/22
 * @Email : 163235610@qq.com
 * @Author:Mrczh
 * @Instructions:
 */
public class FingerprintEntiy extends DataSupport {

    private String userId;

    private String isFin;

    @Override
    public String toString() {
        return "FingerprintEntiy{" +
                "userId='" + userId + '\'' +
                ", isFin=" + isFin +
                '}';
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getIsFin() {
        return isFin;
    }

    public void setIsFin(String isFin) {
        this.isFin = isFin;
    }
}

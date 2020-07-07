package com.kingyon.elevator.entities.entities;

/**
 * @Created By Admin  on 2020/7/7
 * @Email : 163235610@qq.com
 * @Author:Mrczh
 * @Instructions:
 */
public class MassageLitsEntiy {
    /*
    *   "robotId": 1,
        "pushId": 110,
        "robotName": "小度",
        "pushTitle": "测试",
        "pushTime": null,
        "num": 8
    * */

    public int robotId;
    public int pushId;
    public String robotName;
    public String pushTitle;
    public String pushTime;
    public int num;
    public String robotPhoto;

    @Override
    public String toString() {
        return "MassageLitsEntiy{" +
                "robotId=" + robotId +
                ", pushId=" + pushId +
                ", robotName=" + robotName +
                ", pushTitle=" + pushTitle +
                ", pushTime=" + pushTime +
                ", num=" + num +
                '}';
    }
}

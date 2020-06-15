package com.kingyon.elevator.entities.entities;

/**
 * @Created By Admin  on 2020/6/15
 * @Email : 163235610@qq.com
 * @Author:Mrczh
 * @Instructions:
 */
public class OrderCommunityEntiy {
    /**
     *
     "houseName": "金利大厦",
     "housePic": "http:\/\/cdn.tlwgz.com\/\/7c9d6483-957b-4535-9242-b12f02645e8a.png",
     "deviceNum": 0,
     "money": null
     * */
    public String houseName;
    public String housePic;
    public int deviceNum;
    public String money;

    @Override
    public String toString() {
        return "OrderCommunityEntiy{" +
                "houseName='" + houseName + '\'' +
                ", housePic='" + housePic + '\'' +
                ", deviceNum=" + deviceNum +
                ", money='" + money + '\'' +
                '}';
    }
}

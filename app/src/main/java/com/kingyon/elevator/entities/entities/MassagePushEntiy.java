package com.kingyon.elevator.entities.entities;

/**
 * @Created By Admin  on 2020/7/14
 * @Email : 163235610@qq.com
 * @Author:Mrczh
 * @Instructions:
 */
public class MassagePushEntiy {
    /**
     *          "pushId": 242,
     *         "account": "5118186638865507893",
     *         "robotName": "天猫精灵",
     *         "robotPhoto": "http://cdn.tlwgz.com/Fs4YZ1FISjdMwoH8zH1ocFCdn2UG",
     *         "robotId": 7,
     *         "pushTitle": "系统维护升级",
     *         "pushContent": "2020-7-10系统维护升级，期间请勿进行操作，谢谢配合，给您带来不便敬请谅解！",
     *         "msgType": "MSG",
     *         "msgTypeName": null,
     *         "isRead": 0,
     *         "msgParams": "0",
     *         "pushTime": "07-10",
     *         "msgImage": null
     * */

    public int pushId;
    public String account;
    public String robotName;
    public String robotPhoto;
    public int robotId;
    public String pushTitle;
    public String pushContent;
    public String msgType;
    public String msgTypeName;
    public String isRead;
    public String msgParams;
    public String pushTime;
    public String msgImage;
    public String videoHorizontalVertical;

    @Override
    public String toString() {
        return "MassagePushEntiy{" +
                "pushId=" + pushId +
                ", account='" + account + '\'' +
                ", robotName='" + robotName + '\'' +
                ", robotPhoto='" + robotPhoto + '\'' +
                ", robotId=" + robotId +
                ", pushTitle='" + pushTitle + '\'' +
                ", pushContent='" + pushContent + '\'' +
                ", msgType='" + msgType + '\'' +
                ", msgTypeName='" + msgTypeName + '\'' +
                ", isRead='" + isRead + '\'' +
                ", msgParams='" + msgParams + '\'' +
                ", pushTime='" + pushTime + '\'' +
                ", msgImage='" + msgImage + '\'' +
                '}';
    }
}

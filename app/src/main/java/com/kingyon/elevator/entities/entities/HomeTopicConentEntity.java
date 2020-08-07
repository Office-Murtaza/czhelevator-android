package com.kingyon.elevator.entities.entities;

/**
 * @Created By Admin  on 2020/5/12
 * @Email : 163235610@qq.com
 * @Author:Mrczh
 * @Instructions:
 */
public class HomeTopicConentEntity {

    /**
     * ""id": 100012,
     *         "title": "topic13",
     *         "content": "topic13",
     *         "image": "topic13",
     *         "label": 4,
     *         "createAccount": "5829344419552182597",
     *         "atAccount": null,
     *         "createTime": 1588742167000,
     *         "nickname": "用户 104036",
     *         "photo": "http://cdn.tlwgz.com/default-photo.gif",
     *         "uAuthStatus": "AUTH",
     *         "peopleNum": 0,
     *         "labelName": "日常",
     *         "delete": false
     *
     *
     "id": 100049,

     "title": "铲屎官集合点",
     "content": "快来晒晒你家毛孩
     "image": "http:\/\/c
     "label": 89,
     "createAccount": "2"
     "atAccount": null,
     "createTime": 159667
     "nickname": "万吉相",
     "photo": "http:\/\/c
     "uAuthStatus": null,
     "peopleNum": 0,
     "labelName": "宠物",
     "latestPhoto": "http
     "latestNickname": "宝
     "latestTitle": "",
     "latestContent": "不不
     "latestImage": null,
     "delete": null
     *
     *         */

    public int id;
    public String title;
    public String content;
    public String latestContent;
    public String latestTitle;
    public String latestPhoto;
    public String latestNickname;
    public String image;
    public String latestImage;
    public int label;
    public String createAccount;
    public Object atAccount;
    public long createTime;
    public String nickname;
    public String photo;
    public String uAuthStatus;
    public int peopleNum;
    public String labelName;
    public boolean delete;


    @Override
    public String toString() {
        return "HomeTopicConentEntity{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", content='" + content + '\'' +
                ", image='" + image + '\'' +
                ", label=" + label +
                ", createAccount=" + createAccount +
                ", atAccount=" + atAccount +
                ", createTime=" + createTime +
                ", peopleNum=" + peopleNum +
                ", labelName='" + labelName + '\'' +
                ", delete=" + delete +
                '}';
    }
}

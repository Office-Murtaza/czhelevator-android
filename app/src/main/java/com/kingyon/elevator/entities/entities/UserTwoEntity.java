package com.kingyon.elevator.entities.entities;

/**
 * @Created By Admin  on 2020/6/29
 * @Email : 163235610@qq.com
 * @Author:Mrczh
 * @Instructions:
 */
public class UserTwoEntity {
    /**
     *  "id": 100014,
     *     "nickname": "项瑞青",
     *     "photo": "http://cdn.tlwgz.com/FlqESyUU95Qf3RsdurvVQDBTf9NS",
     *     "sex": "S",
     *     "phone": "18983257717",
     *     "personalizedSignature": null,
     *     "followers": 2,
     *     "beFollowers": 1,
     *     "isFollower": 0,
     *     "contentNum": 18
     * */

    public int id;
    public String nickname;
    public String photo;
    public String sex;
    public String phone;
    public String personalizedSignature;
    public int followers;
    public int beFollowers;
    public int isFollower;
    public int contentNum;
    public int isAttent;
    public String account;

    @Override
    public String toString() {
        return "UserTwoEntity{" +
                "id=" + id +
                ", nickname='" + nickname + '\'' +
                ", photo='" + photo + '\'' +
                ", sex='" + sex + '\'' +
                ", phone='" + phone + '\'' +
                ", personalizedSignature='" + personalizedSignature + '\'' +
                ", followers=" + followers +
                ", beFollowers=" + beFollowers +
                ", isFollower=" + isFollower +
                ", contentNum=" + contentNum +
                ", isAttent=" + isAttent +
                '}';
    }
}

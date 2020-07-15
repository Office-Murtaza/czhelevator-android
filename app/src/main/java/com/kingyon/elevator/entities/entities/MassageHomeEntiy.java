package com.kingyon.elevator.entities.entities;

import java.util.List;

/**
 * @Created By Admin  on 2020/7/7
 * @Email : 163235610@qq.com
 * @Author:Mrczh
 * @Instructions:
 */
public class MassageHomeEntiy<T> {
    /*
    *   "commentNum": 0,
        "likesNum": 0,
        "unreadMessages": 1,
        "followerNum": 2,
        "pushMessage": [
    * */

    public int commentNum;
    public int likesNum;
    public int unreadMessages;
    public int followerNum;
    public int totalNum;
    public List<T> pushMessage;

    @Override
    public String toString() {
        return "MassageHomeEntiy{" +
                "commentNum=" + commentNum +
                ", likesNum=" + likesNum +
                ", unreadMessages=" + unreadMessages +
                ", followerNum=" + followerNum +
                ", pushMessage=" + pushMessage +
                '}';
    }
}

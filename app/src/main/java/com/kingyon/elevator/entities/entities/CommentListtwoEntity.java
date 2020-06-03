package com.kingyon.elevator.entities.entities;

import java.util.List;

/**
 * @Created By Admin  on 2020/5/14
 * @Email : 163235610@qq.com
 * @Author:Mrczh
 * @Instructions:
 */
public class CommentListtwoEntity {

    public int id;
    public int contentId;
    public int parentId;
    public String comment;
    public String createAccount;
    public long createTime;
    public String nickname;
    public String photo;
    public int likesNum;
    public Object child;
    public boolean report;

    @Override
    public String toString() {
        return "CommentListtwoEntity{" +
                "id=" + id +
                ", contentId=" + contentId +
                ", parentId=" + parentId +
                ", comment='" + comment + '\'' +
                ", createAccount='" + createAccount + '\'' +
                ", createTime=" + createTime +
                ", nickname='" + nickname + '\'' +
                ", photo='" + photo + '\'' +
                ", likesNum=" + likesNum +
                ", child=" + child +
                ", report=" + report +
                '}';
    }
}

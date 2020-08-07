package com.kingyon.elevator.entities.entities;

import java.util.List;

/**
 * @Created By Admin  on 2020/5/14
 * @Email : 163235610@qq.com
 * @Author:Mrczh
 * @Instructions:
 */
public class CommentListEntity {
    /**
     *         "id": 100007,
     *         "contentId": 100064,
     *         "parentId": 0,
     *         "comment": "多喝点继续继续",
     *         "createAccount": "5196646915807042859",
     *         "createTime": 1589435560000,
     *         "nickname": "Time",
     *         "photo": "http://thirdwx.qlogo.cn/mmopen/vi_32/60GYa8icicicYfF892kJjnk9yiaau7KhSV9DPibSyBnyVlGvbqMtbsnYzicqsSWMwowKQTwcuE1kxemw3SmCwsNaufaQ/132",
     *         "likesNum": 0,
     *         "child": [],
     *         "report": false
     *         */

    public int id;
    public int contentId;
    public int parentId;
    public String comment;
    public String createAccount;
    public long createTime;
    public String nickname;
    public String photo;
    public int likesNum;
    public List<CommentListtwoEntity> child;
    public boolean report;
    public int isLiked;

    @Override
    public String toString() {
        return "CommentListEntity{" +
                "id=" + id +
                ", contentId=" + contentId +
                ", parentId=" + parentId +
                ", comment='" + comment + '\'' +
                ", createAccount='" + createAccount + '\'' +
                ", createTime='" + createTime + '\'' +
                ", nickname='" + nickname + '\'' +
                ", photo='" + photo + '\'' +
                ", likesNum=" + likesNum +
                ", child=" + child +
                ", report=" + report +
                '}';
    }
}

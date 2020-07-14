package com.kingyon.elevator.entities.entities;

/**
 * @Created By Admin  on 2020/7/14
 * @Email : 163235610@qq.com
 * @Author:Mrczh
 * @Instructions:
 */
public class ContentLikesListEntiy {
    /**
     *          "id": 100287,
     *         "title": "",
     *         "content": "凤凰国际放假放假",
     *         "image": null,
     *         "videoCover": null,
     *         "objId": "100541",
     *         "nickname": "项瑞青",
     *         "photo": "http://cdn.tlwgz.com/FlqESyUU95Qf3RsdurvVQDBTf9NS",
     *         "createTime": "07-14",
     *         "isRead": "0",
     *         "videoHorizontalVertical": null,
     *         "type": "wsq"
     *
     *
     *
     * */

    public int id;
    public String title;
    public String content;
    public String image;
    public String videoCover;
    public int objId;
    public String nickname;
    public String photo;
    public String createTime;
    public int isRead;
    public int videoHorizontalVertical;
    public String type;

    @Override
    public String toString() {
        return "ContentLikesListEntiy{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", content='" + content + '\'' +
                ", image='" + image + '\'' +
                ", videoCover='" + videoCover + '\'' +
                ", objId=" + objId +
                ", nickname='" + nickname + '\'' +
                ", photo='" + photo + '\'' +
                ", createTime='" + createTime + '\'' +
                ", isRead=" + isRead +
                ", videoHorizontalVertical='" + videoHorizontalVertical + '\'' +
                ", type='" + type + '\'' +
                '}';
    }
}

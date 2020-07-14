package com.kingyon.elevator.entities.entities;

/**
 * @Created By Admin  on 2020/7/14
 * @Email : 163235610@qq.com
 * @Author:Mrczh
 * @Instructions:
 */
public class CommentLikesListEntiy {

    /**
     *          "id": 100313,
     *         "contentType": "article",
     *         "contentId": 100504,
     *         "comment": "一样一样YY",
     *         "commentAccount": "5196646915807042859",
     *         "commentNickName": "巴拉巴拉",
     *         "commentPhoto": "http://cdn.tlwgz.com/FjJlFVoqTZDpDF8kk6dBYh2n5an4",
     *         "createTime": "07-13",
     *         "isRead": 0,
     *         "title": "fjffjf",
     *         "content": "<font size=\"4\">fuffkfk</font><img src=\"http://cdn.tlwgz.com/FuqkkhCbKhviwsfGxZhCvVSAfgZk\" alt=\"picvision\" style=\"margin-top:10px;max-width:100%;\"><br><br>",
     *         "image": "",
     *         "video_cover": "http://cdn.tlwgz.com/FuqkkhCbKhviwsfGxZhCvVSAfgZk"
     *
     *            "id": 100205,
     *         "comment": "&nbsp;<user style='color: #4dacee;' id='5196646915807042859' name='巴拉巴拉'>@巴拉巴拉</user>&nbsp;:哈哈哈哈",
     *         "objId": 100297,
     *         "parentId": null,
     *         "nickname": "用户 104548",
     *         "photo": "http://cdn.tlwgz.com/FnE60-qqySxQlMY9_z4KDrPc2CQq",
     *         "createTime": "07-13",
     *         "isRead": "0"
     *
     * */

    public int id;
    public String contentType;
    public int contentId;
    public int objId;
    public String comment;
    public String commentAccount;
    public String commentNickName;
    public String nickname;
    public String commentPhoto;
    public String photo;
    public String createTime;
    public int isRead;
    public int videoHorizontalVertical;
    public String title;
    public String content;
    public String image;
    public String video_cover;

    @Override
    public String toString() {
        return "CommentLikesListEntiy{" +
                "id=" + id +
                ", contentType='" + contentType + '\'' +
                ", contentId=" + contentId +
                ", comment='" + comment + '\'' +
                ", commentAccount='" + commentAccount + '\'' +
                ", commentNickName='" + commentNickName + '\'' +
                ", commentPhoto='" + commentPhoto + '\'' +
                ", createTime='" + createTime + '\'' +
                ", isRead=" + isRead +
                ", title='" + title + '\'' +
                ", content='" + content + '\'' +
                ", image='" + image + '\'' +
                ", video_cover='" + video_cover + '\'' +
                '}';
    }
}

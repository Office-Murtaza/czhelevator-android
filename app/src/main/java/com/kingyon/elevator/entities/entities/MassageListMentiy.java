package com.kingyon.elevator.entities.entities;

/**
 * @Created By Admin  on 2020/7/7
 * @Email : 163235610@qq.com
 * @Author:Mrczh
 * @Instructions:
 */
public class MassageListMentiy {
/**
 *          "id": 18,
 *         "createAccount": "5196646915807042859",
 *         "isDelete": null,
 *         "state": null,
 *         "type": null,
 *         "title": "广告审核成功",
 *         "content": "恭喜您,您的广告不准备这不是已通过审核!",
 *         "messageId": null,
 *         "isRead": 1,
 *         "typeChild": "ADVERTIS",
 *         "postId": "177",
 *         "createTime": "06-12",
 *         "image": null,
 *         "url": null,
 *         "contentType": null,
 *         "videoHorizontalVertical": null,
 *         "success": false
 *
 * */

    public int id;
    public String createAccount;
    public String isDelete;
    public String state;
    public String type;
    public String title;
    public String content;
    public String messageId;
    public int isRead;
    public String typeChild;
    public String postId;
    public String image;
    public String createTime;
    public String url;
    public String delete;
    public String contentType;
    public int videoHorizontalVertical;
    public boolean success;
    public boolean auth;

    @Override
    public String toString() {
        return "MassageListMentiy{" +
                "id=" + id +
                ", createAccount='" + createAccount + '\'' +
                ", isDelete='" + isDelete + '\'' +
                ", state='" + state + '\'' +
                ", type='" + type + '\'' +
                ", title='" + title + '\'' +
                ", content='" + content + '\'' +
                ", messageId='" + messageId + '\'' +
                ", isRead='" + isRead + '\'' +
                ", typeChild='" + typeChild + '\'' +
                ", postId='" + postId + '\'' +
                ", image='" + image + '\'' +
                ", url='" + url + '\'' +
                ", delete='" + delete + '\'' +
                '}';
    }
}

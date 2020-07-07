package com.kingyon.elevator.entities.entities;

/**
 * @Created By Admin  on 2020/7/7
 * @Email : 163235610@qq.com
 * @Author:Mrczh
 * @Instructions:
 */
public class MassageListMentiy {
    /*  "id": 2,
      "createAccount": "8829701662735382983",
      "isDelete": null,
      "state": null,
      "type": null,
      "title": "广告审核失败",
      "content": "很抱歉,您的广告hello审核不通过,请您再次编辑并提交广告。",
      "messageId": null,
      "isRead": false,
      "typeChild": null,
      "postId": 69,
      "image": null,
      "url": null,
      "delete": null*/

    public int id;
    public String createAccount;
    public String isDelete;
    public String state;
    public String type;
    public String title;
    public String content;
    public String messageId;
    public String isRead;
    public String typeChild;
    public String postId;
    public String image;
    public String url;
    public String delete;

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

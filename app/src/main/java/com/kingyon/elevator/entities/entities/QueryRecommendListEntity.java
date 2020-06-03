package com.kingyon.elevator.entities.entities;

import java.io.Serializable;
import java.util.List;

/**
 * @Created By Admin  on 2020/5/9
 * @Email : 163235610@qq.com
 * @Author:Mrczh
 * @Instructions:
 */

public class QueryRecommendListEntity<T> implements Serializable{
            /**
             * id : 100064
             * title : 
             * content : 33333333333333333333333333333333333333333333333333333
             * image : http://cdn.tlwgz.com/Fi6DYSSgOLVkG3fLoRUu5vMPOpI5&_&http://cdn.tlwgz.com/FiuLIS8zOoBABm7EfHtqrlhYhGC2&_&http://cdn.tlwgz.com/FmCESKsMi_6ZoWw89FXyZ1sxqePe&_&http://cdn.tlwgz.com/Fphd3SAIShHYKAPqRAAAUACrF2l_&_&http://cdn.tlwgz.com/FrWtVXa7S1fL9FrcDNmFkbsVkH14&_&http://cdn.tlwgz.com/FvhCprjTxW_BoKXvhnVWX0C00QvV
             * video : null
             * videoSize : 0
             * videoCover : null
             * videoHorizontalVertical : 1
             * playTime : 0
             * browseTimes : 14
             * createAccount : 5196646915807042859
             * type : wsq
             * combination : 2
             * topicId : 0
             * atAccount : null
             * status : 0
             * createTime : 1589425049000
             * topicTitle : null
             * nickname : Time
             * photo : http://thirdwx.qlogo.cn/mmopen/vi_32/60GYa8icicicYfF892kJjnk9yiaau7KhSV9DPibSyBnyVlGvbqMtbsnYzicqsSWMwowKQTwcuE1kxemw3SmCwsNaufaQ/132
             * likes : 2
             * comments : 61
             * shares : 0
             * likesItem : [{"id":100026,"objId":100064,"likeType":"CONTENT","createAccount":"6096907287229370962","nickname":"＆","photo":"http://thirdqq.qlogo.cn/g?b=oidb&k=NABxwwWMHiajw9D3T7EaTlg&s=100&t=1571720286","createTime":1589427883000},{"id":100027,"objId":100064,"likeType":"CONTENT","createAccount":"5196646915807042859","nickname":"Time","photo":"http://thirdwx.qlogo.cn/mmopen/vi_32/60GYa8icicicYfF892kJjnk9yiaau7KhSV9DPibSyBnyVlGvbqMtbsnYzicqsSWMwowKQTwcuE1kxemw3SmCwsNaufaQ/132","createTime":1589427903000}]
             * uAuthStatus : 1
             * hasMedal : false
             * recommend : true
             * liked : false
             * delete : false
             * report : true
             */

            public int id;
            public String title;
            public String content;
            public String image;
            public String video;
            public int videoSize;
            public String videoCover;
            public int videoHorizontalVertical;
            public int playTime;
            public int browseTimes;
            public String createAccount;
            public String type;
            public int combination;
            public int topicId;
            public Object atAccount;
            public int status;
            public long createTime;
            public Object topicTitle;
            public String nickname;
            public String photo;
            public int likes;
            public int comments;
            public int shares;
            public int uAuthStatus;
            public boolean hasMedal;
            public boolean recommend;
            public boolean liked;
            public boolean delete;
            public boolean report;
            public List<T> likesItem;

    @Override
    public String toString() {
        return "QueryRecommendEntity{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", content='" + content + '\'' +
                ", image='" + image + '\'' +
                ", video=" + video +
                ", videoSize=" + videoSize +
                ", videoCover=" + videoCover +
                ", videoHorizontalVertical=" + videoHorizontalVertical +
                ", playTime=" + playTime +
                ", browseTimes=" + browseTimes +
                ", createAccount='" + createAccount + '\'' +
                ", type='" + type + '\'' +
                ", combination=" + combination +
                ", topicId=" + topicId +
                ", atAccount=" + atAccount +
                ", status=" + status +
                ", createTime=" + createTime +
                ", topicTitle=" + topicTitle +
                ", nickname='" + nickname + '\'' +
                ", photo='" + photo + '\'' +
                ", likes=" + likes +
                ", comments=" + comments +
                ", shares=" + shares +
                ", uAuthStatus=" + uAuthStatus +
                ", hasMedal=" + hasMedal +
                ", recommend=" + recommend +
                ", liked=" + liked +
                ", delete=" + delete +
                ", report=" + report +
                ", likesItem=" + likesItem +
                '}';
    }
}

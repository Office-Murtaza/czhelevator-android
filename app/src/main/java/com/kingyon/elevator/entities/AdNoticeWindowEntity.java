package com.kingyon.elevator.entities;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class AdNoticeWindowEntity {
    
        /**
         * id : 24
         * timestamp : null
         * createAccount : czh
         * createAccountName : null
         * createTime : null
         * name : SaaS
         * isPut : 0
         * urlImage : http://cdn.tlwgz.com/FlK1Jw7s2ny2dbYN1352P-fIoGyP
         * urlLink : http://test.pddtv.cn
         * position : 0
         * type : 0
         * isDelete : 0
         * updateAccount : null
         * updateAccountName : null
         * updateTime : null
         * popText : null
         */
        
        public int id;
        public String timestamp;
        public String createAccount;
        public String createAccountName;
        public String createTime;
        public String name;
        public int isPut;
        public String urlImage;
        public String urlLink;
        public int position;
        public int type;
        public int isDelete;
        public String updateAccount;
        public String updateAccountName;
        public String updateTime;
        public String popText;

    @Override
    public String toString() {
        return "AdNoticeWindowEntity{" +
                "id=" + id +
                ", timestamp='" + timestamp + '\'' +
                ", createAccount='" + createAccount + '\'' +
                ", createAccountName='" + createAccountName + '\'' +
                ", createTime='" + createTime + '\'' +
                ", name='" + name + '\'' +
                ", isPut=" + isPut +
                ", urlImage='" + urlImage + '\'' +
                ", urlLink='" + urlLink + '\'' +
                ", position=" + position +
                ", type=" + type +
                ", isDelete=" + isDelete +
                ", updateAccount='" + updateAccount + '\'' +
                ", updateAccountName='" + updateAccountName + '\'' +
                ", updateTime='" + updateTime + '\'' +
                ", popText='" + popText + '\'' +
                '}';
    }
}

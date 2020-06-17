package com.kingyon.elevator.entities.entities;

/**
 * @Created By Admin  on 2020/5/9
 * @Email : 163235610@qq.com
 * @Author:Mrczh
 * @Instructions:
 */
public class QueryRecommendTopEntity {
        /**
         * id : 1
         * title : 1
         * content : 1
         * sourceId : 1
         * sourceName : 1
         * readNum : 1
         * image : 1
         * createTime : 1588830829000
         * updateTime : 1589004294000
         * status : 0
         * reserve1 : null
         * reserve2 : null
         */

        public int id;
        public String title;
        public String content;
        public String sourceId;
        public String sourceName;
        public int readNum;
        public String image;
        public long createTime;
        public long updateTime;
        public int status;
        public Object reserve1;
        public Object reserve2;

        @Override
        public String toString() {
                return "QueryRecommendTopEntity{" +
                        "id=" + id +
                        ", title='" + title + '\'' +
                        ", content='" + content + '\'' +
                        ", sourceId='" + sourceId + '\'' +
                        ", sourceName='" + sourceName + '\'' +
                        ", readNum=" + readNum +
                        ", image='" + image + '\'' +
                        ", createTime=" + createTime +
                        ", updateTime=" + updateTime +
                        ", status=" + status +
                        ", reserve1=" + reserve1 +
                        ", reserve2=" + reserve2 +
                        '}';
        }
}

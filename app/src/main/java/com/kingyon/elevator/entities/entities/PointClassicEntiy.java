package com.kingyon.elevator.entities.entities;

import java.util.List;

/**
 * @Created By Admin  on 2020/5/28
 * @Email : 163235610@qq.com
 * @Author:Mrczh
 * @Instructions:
 */
public class PointClassicEntiy {
        /**
         * id : 1
         * pointName : 社区楼宇
         * level : 1
         * parent : 0
         * child : [{"id":10,"pointName":"写字楼","level":"2","parent":1,"child":null},{"id":11,"pointName":"住宅楼","level":"2","parent":1,"child":null},{"id":12,"pointName":"酒店","level":"2","parent":1,"child":null}]
         */

        public int id;
        public String pointName;
        public String level;
        public int parent;
        public List<ChildBean> child;

    @Override
    public String toString() {
        return "PointClassicEntiy{" +
                "id=" + id +
                ", pointName='" + pointName + '\'' +
                ", level='" + level + '\'' +
                ", parent=" + parent +
                ", child=" + child +
                '}';
    }

    public static class ChildBean {
        /**
         * id : 10
         * pointName : 写字楼
         * level : 2
         * parent : 1
         * child : null
         */

        public int id;
        public String pointName;
        public String level;
        public int parent;
        public Object child;

        @Override
        public String toString() {
            return "ChildBean{" +
                    "id=" + id +
                    ", pointName='" + pointName + '\'' +
                    ", level='" + level + '\'' +
                    ", parent=" + parent +
                    ", child=" + child +
                    '}';
        }
    }
}

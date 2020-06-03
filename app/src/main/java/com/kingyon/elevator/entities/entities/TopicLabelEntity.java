package com.kingyon.elevator.entities.entities;

import java.util.List;

/**
 * @Created By Admin  on 2020/5/8
 * @Email : 163235610@qq.com
 * @Author:Mrczh
 * @Instructions:
 */
public class TopicLabelEntity<T> {

        /**
         * pageContent : [{"id":1,"labelName":"热门","createTime":1588903200000,"reserve1":null,"reserve2":null},{"id":2,"labelName":"团购","createTime":1588906800000,"reserve1":null,"reserve2":null},{"id":3,"labelName":"运动","createTime":1588910400000,"reserve1":null,"reserve2":null},{"id":4,"labelName":"日常","createTime":1588914000000,"reserve1":null,"reserve2":null},{"id":5,"labelName":"趣味发现","createTime":1588917600000,"reserve1":null,"reserve2":null}]
         * totalElements : 5
         * size : 5
         * totalPages : 1
         * totalAmount : null
         */

        private int totalElements;
        private int size;
        private int totalPages;
        private Object totalAmount;
        private List<T> pageContent;

        public int getTotalElements() {
            return totalElements;
        }

        public void setTotalElements(int totalElements) {
            this.totalElements = totalElements;
        }

        public int getSize() {
            return size;
        }

        public void setSize(int size) {
            this.size = size;
        }

        public int getTotalPages() {
            return totalPages;
        }

        public void setTotalPages(int totalPages) {
            this.totalPages = totalPages;
        }

        public Object getTotalAmount() {
            return totalAmount;
        }

        public void setTotalAmount(Object totalAmount) {
            this.totalAmount = totalAmount;
        }

        public List<T> getPageContent() {
            return pageContent;
        }

        public void setPageContent(List<T> pageContent) {
            this.pageContent = pageContent;
        }

        public static class PageContentBean {
            /**
             * id : 1
             * labelName : 热门
             * createTime : 1588903200000
             * reserve1 : null
             * reserve2 : null
             */

            private int id;
            private String labelName;
            private long createTime;
            private Object reserve1;
            private Object reserve2;

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }

            public String getLabelName() {
                return labelName;
            }

            public void setLabelName(String labelName) {
                this.labelName = labelName;
            }

            public long getCreateTime() {
                return createTime;
            }

            public void setCreateTime(long createTime) {
                this.createTime = createTime;
            }

            public Object getReserve1() {
                return reserve1;
            }

            public void setReserve1(Object reserve1) {
                this.reserve1 = reserve1;
            }

            public Object getReserve2() {
                return reserve2;
            }

            public void setReserve2(Object reserve2) {
                this.reserve2 = reserve2;
            }
        }
}

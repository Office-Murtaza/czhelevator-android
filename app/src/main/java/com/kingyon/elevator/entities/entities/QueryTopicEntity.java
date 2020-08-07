package com.kingyon.elevator.entities.entities;

import com.bobomee.android.mentions.edit.listener.InsertData;
import com.bobomee.android.mentions.model.FormatRange;

import java.io.Serializable;
import java.util.List;

/**
 * @Created By Admin  on 2020/5/8
 * @Email : 163235610@qq.com
 * @Author:Mrczh
 * @Instructions:
 */
public class QueryTopicEntity<T> {

        private int totalElements;
        private int size;
        private int totalPages;
        private Object totalAmount;
        private List<T> pageContent;

    @Override
    public String toString() {
        return "QueryTopicEntity{" +
                "totalElements=" + totalElements +
                ", size=" + size +
                ", totalPages=" + totalPages +
                ", totalAmount=" + totalAmount +
                ", pageContent=" + pageContent +
                '}';
    }

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

        public static class PageContentBean implements Serializable, InsertData {
            /**
             * id : 100013
             * title : topic14
             * content : topic14
             * image : topic14
             * label : topic14
             * peopleNum : 0
             * createAccount : 5829344419552182597
             * createTime : 1588742227000
             * updateTime : 1588842777000
             * isDelete : false
             * reserve1 : null
             * reserve2 : null
             */

            private int id;
            private String title;
            private String content;
            private String image;
            private String latestImage;
            private String label;
            private int peopleNum;
            private String createAccount;
            private long createTime;
            private long updateTime;
            private boolean isDelete;
            private Object reserve1;
            private Object reserve2;


            @Override
            public String toString() {
                return "PageContentBean{" +
                        "id=" + id +
                        ", title='" + title + '\'' +
                        ", content='" + content + '\'' +
                        ", image='" + image + '\'' +
                        ", latestImage='" + latestImage + '\'' +
                        ", label='" + label + '\'' +
                        ", peopleNum=" + peopleNum +
                        ", createAccount='" + createAccount + '\'' +
                        ", createTime=" + createTime +
                        ", updateTime=" + updateTime +
                        ", isDelete=" + isDelete +
                        ", reserve1=" + reserve1 +
                        ", reserve2=" + reserve2 +
                        '}';
            }



            public String getLatestImage() {
                return latestImage;
            }

            public void setLatestImage(String latestImage) {
                this.latestImage = latestImage;
            }

            public boolean isDelete() {
                return isDelete;
            }

            public void setDelete(boolean delete) {
                isDelete = delete;
            }


            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }

            public String getTitle() {
                return title;
            }

            public void setTitle(String title) {
                this.title = title;
            }

            public String getContent() {
                return content;
            }

            public void setContent(String content) {
                this.content = content;
            }

            public String getImage() {
                return image;
            }

            public void setImage(String image) {
                this.image = image;
            }

            public String getLabel() {
                return label;
            }

            public void setLabel(String label) {
                this.label = label;
            }

            public int getPeopleNum() {
                return peopleNum;
            }

            public void setPeopleNum(int peopleNum) {
                this.peopleNum = peopleNum;
            }

            public String getCreateAccount() {
                return createAccount;
            }

            public void setCreateAccount(String createAccount) {
                this.createAccount = createAccount;
            }

            public long getCreateTime() {
                return createTime;
            }

            public void setCreateTime(long createTime) {
                this.createTime = createTime;
            }

            public long getUpdateTime() {
                return updateTime;
            }

            public void setUpdateTime(long updateTime) {
                this.updateTime = updateTime;
            }

            public boolean isIsDelete() {
                return isDelete;
            }

            public void setIsDelete(boolean isDelete) {
                this.isDelete = isDelete;
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

            @Override
            public CharSequence charSequence() {
                return "#" + title + "#";
            }

            @Override
            public FormatRange.FormatData formatData() {
                return new TagConvert(this);
            }

            @Override
            public int color() {
                return 0xFF4DACEE;
            }
        }

    private static class TagConvert implements FormatRange.FormatData {
        public static final String TAG_FORMAT = "&nbsp;<tag id='%s' name='%s' style='color: #4dacee;'>%s</tag>&nbsp;";
        private final PageContentBean tag;

        public TagConvert(PageContentBean tag) {
            this.tag = tag;
        }

        @Override
        public CharSequence formatCharSequence() {
            return String.format(TAG_FORMAT, tag.getId(), tag.getTitle(),
                    "#" + tag.getTitle() + "#");
        }
    }
}

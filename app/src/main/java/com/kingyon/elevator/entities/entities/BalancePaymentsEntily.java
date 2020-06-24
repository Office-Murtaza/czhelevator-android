package com.kingyon.elevator.entities.entities;

import java.util.List;

/**
 * @Created By Admin  on 2020/6/24
 * @Email : 163235610@qq.com
 * @Author:Mrczh
 * @Instructions:
 */
public class BalancePaymentsEntily {
        /**
         * pageContent : {"date":"2020-06-11","subtotal":4.9,"lstResponse":[{"total":4.9,"yesterdayMoney":0,"withdrawal":0,"canWithdraw":0,"source":"EARNINGS","createTime":1591844259000,"type":"INCOME"}]}
         * totalElements : 1
         * size : 15
         * totalPages : 1
         * totalAmount : null
         */
        private PageContentBean pageContent;
        private int totalElements;
        private int size;
        private int totalPages;
        private Object totalAmount;

    @Override
    public String toString() {
        return "BalancePaymentsEntily{" +
                "pageContent=" + pageContent +
                ", totalElements=" + totalElements +
                ", size=" + size +
                ", totalPages=" + totalPages +
                ", totalAmount=" + totalAmount +
                '}';
    }

    public PageContentBean getPageContent() {
            return pageContent;
        }

        public void setPageContent(PageContentBean pageContent) {
            this.pageContent = pageContent;
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

        public static class PageContentBean {
            @Override
            public String toString() {
                return "PageContentBean{" +
                        "date='" + date + '\'' +
                        ", subtotal=" + subtotal +
                        ", lstResponse=" + lstResponse +
                        '}';
            }

            /**
             * date : 2020-06-11
             * subtotal : 4.9
             * lstResponse : [{"total":4.9,"yesterdayMoney":0,"withdrawal":0,"canWithdraw":0,"source":"EARNINGS","createTime":1591844259000,"type":"INCOME"}]
             */

            private String date;
            private double subtotal;
            private List<LstResponseBean> lstResponse;

            public String getDate() {
                return date;
            }

            public void setDate(String date) {
                this.date = date;
            }

            public double getSubtotal() {
                return subtotal;
            }

            public void setSubtotal(double subtotal) {
                this.subtotal = subtotal;
            }

            public List<LstResponseBean> getLstResponse() {
                return lstResponse;
            }

            public void setLstResponse(List<LstResponseBean> lstResponse) {
                this.lstResponse = lstResponse;
            }

            public static class LstResponseBean {
                @Override
                public String toString() {
                    return "LstResponseBean{" +
                            "total=" + total +
                            ", yesterdayMoney=" + yesterdayMoney +
                            ", withdrawal=" + withdrawal +
                            ", canWithdraw=" + canWithdraw +
                            ", source='" + source + '\'' +
                            ", createTime=" + createTime +
                            ", type='" + type + '\'' +
                            '}';
                }

                /**
                 * total : 4.9
                 * yesterdayMoney : 0
                 * withdrawal : 0
                 * canWithdraw : 0
                 * source : EARNINGS
                 * createTime : 1591844259000
                 * type : INCOME
                 */

                private double total;
                private int yesterdayMoney;
                private int withdrawal;
                private int canWithdraw;
                private String source;
                private long createTime;
                private String type;

                public double getTotal() {
                    return total;
                }

                public void setTotal(double total) {
                    this.total = total;
                }

                public int getYesterdayMoney() {
                    return yesterdayMoney;
                }

                public void setYesterdayMoney(int yesterdayMoney) {
                    this.yesterdayMoney = yesterdayMoney;
                }

                public int getWithdrawal() {
                    return withdrawal;
                }

                public void setWithdrawal(int withdrawal) {
                    this.withdrawal = withdrawal;
                }

                public int getCanWithdraw() {
                    return canWithdraw;
                }

                public void setCanWithdraw(int canWithdraw) {
                    this.canWithdraw = canWithdraw;
                }

                public String getSource() {
                    return source;
                }

                public void setSource(String source) {
                    this.source = source;
                }

                public long getCreateTime() {
                    return createTime;
                }

                public void setCreateTime(long createTime) {
                    this.createTime = createTime;
                }

                public String getType() {
                    return type;
                }

                public void setType(String type) {
                    this.type = type;
                }
            }
        }
}

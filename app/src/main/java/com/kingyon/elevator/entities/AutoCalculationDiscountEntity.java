package com.kingyon.elevator.entities;

import java.util.ArrayList;
import java.util.List;

/**
 * Created By SongPeng  on 2020/1/6
 * Email : 1531603384@qq.com
 * 进入订单详情页 自动计算优惠的实体
 */
public class AutoCalculationDiscountEntity {

    private double actualAmount;
    private double concessionalRate;
    private double discountRate;
    private boolean hasMore;
    private ArrayList<Integer> cons;
    private List<ConsCountBean> consCount;

    public double getActualAmount() {
        return actualAmount;
    }

    public void setActualAmount(double actualAmount) {
        this.actualAmount = actualAmount;
    }

    public double getConcessionalRate() {
        return concessionalRate;
    }

    public void setConcessionalRate(double concessionalRate) {
        this.concessionalRate = concessionalRate;
    }

    public double getDiscountRate() {
        return discountRate;
    }

    public void setDiscountRate(double discountRate) {
        this.discountRate = discountRate;
    }

    public boolean isHasMore() {
        return hasMore;
    }

    public void setHasMore(boolean hasMore) {
        this.hasMore = hasMore;
    }

    public ArrayList<Integer> getCons() {
        return cons;
    }

    public void setCons(ArrayList<Integer> cons) {
        this.cons = cons;
    }

    public List<ConsCountBean> getConsCount() {
        return consCount;
    }

    public void setConsCount(List<ConsCountBean> consCount) {
        this.consCount = consCount;
    }

    public static class ConsCountBean {
        /**
         * name : 满0减100
         * count : 20
         */

        private String name;
        private int count;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getCount() {
            return count;
        }

        public void setCount(int count) {
            this.count = count;
        }
    }
}

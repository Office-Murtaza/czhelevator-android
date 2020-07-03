package com.kingyon.elevator.entities.entities;

import java.util.List;

/**
 * @Created By Admin  on 2020/7/3
 * @Email : 163235610@qq.com
 * @Author:Mrczh
 * @Instructions:
 */
public class WithdrawEntily<T> {

        /**
         * pageContent : {"subAmount":11,"lstData":[{"objectId":2,"withDrawWay":"BANKCARD","status":"DEALING","amount":1,"faildReason":"","aliAcount":"6227001261500518147","bankName":"服服服","cardNo":"6227001261500518147","cardholder":"项瑞青","time":1593691294000},{"objectId":1,"withDrawWay":"ALI","status":"SUCCESS","amount":10,"faildReason":"","aliAcount":"454386264@qq.com","bankName":"","cardNo":"454386264@qq.com","cardholder":"","time":1592990201000}]}
         * totalElements : 2
         * size : 10
         * totalPages : 1
         * totalAmount : null
         */

    public T pageContent;
    public int totalElements;
    public int size;
    public int totalPages;
    public Object totalAmount;



    public static class PageContentBean<T> {
        /**
         * subAmount : 11
         * lstData : [{"objectId":2,"withDrawWay":"BANKCARD","status":"DEALING","amount":1,"faildReason":"","aliAcount":"6227001261500518147","bankName":"服服服","cardNo":"6227001261500518147","cardholder":"项瑞青","time":1593691294000},{"objectId":1,"withDrawWay":"ALI","status":"SUCCESS","amount":10,"faildReason":"","aliAcount":"454386264@qq.com","bankName":"","cardNo":"454386264@qq.com","cardholder":"","time":1592990201000}]
         */

        public int subAmount;
        public List<T> lstData;

    }

}

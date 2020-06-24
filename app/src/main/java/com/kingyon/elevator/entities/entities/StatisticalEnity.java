package com.kingyon.elevator.entities.entities;

import java.util.List;

/**
 * @Created By Admin  on 2020/6/23
 * @Email : 163235610@qq.com
 * @Author:Mrczh
 * @Instructions:
 */
public class StatisticalEnity<T> {
    /*
    * "date": null,
      "subtotal": 8.586,
      "lstResponse": [
      *
      * */

    public String data;
    public String subtotal;
    public List<EarningsYesterdayEnity> lstResponse;

    @Override
    public String toString() {
        return "StatisticalEnity{" +
                "data='" + data + '\'' +
                ", subtotal='" + subtotal + '\'' +
                ", lstResponse=" + lstResponse +
                '}';
    }
}

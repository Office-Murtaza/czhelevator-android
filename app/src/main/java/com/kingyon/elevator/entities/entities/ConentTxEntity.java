package com.kingyon.elevator.entities.entities;

import java.util.List;

/**
 * @Created By Admin  on 2020/6/23
 * @Email : 163235610@qq.com
 * @Author:Mrczh
 * @Instructions:
 */
public class ConentTxEntity <T>{

    public int totalElements;
    public int size;
    public int totalPages;
    public Object totalAmount;
    public StatisticalEnity<T> pageContent;


    @Override
    public String toString() {
        return "ConentTxEntity{" +
                "totalElements=" + totalElements +
                ", size=" + size +
                ", totalPages=" + totalPages +
                ", totalAmount=" + totalAmount +
                ", pageContent=" + pageContent +
                '}';
    }
}

package com.kingyon.elevator.entities.entities;

import android.support.annotation.NonNull;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;

/**
 * @Created By Admin  on 2020/5/9
 * @Email : 163235610@qq.com
 * @Author:Mrczh
 * @Instructions: 内容头
 */
public class ConentEntity<T>  {

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

    public List<T> getContent() {
        return pageContent;
    }

    public void setContent(List<T> content) {
        this.pageContent = content;
    }

}

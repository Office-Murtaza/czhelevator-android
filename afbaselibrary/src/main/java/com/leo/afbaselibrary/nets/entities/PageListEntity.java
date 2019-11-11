package com.leo.afbaselibrary.nets.entities;

import java.util.List;

/**
 * created by arvin on 16/8/3 14:48
 * email：1035407623@qq.com
 */
public class PageListEntity<T> {
    protected List<T> content;
    protected int totalPages;
    protected int totalElements;
    private double totalAmount;

    public PageListEntity() {
    }

    public PageListEntity(List<T> content) {
        this.content = content;
    }

    public List<T> getContent() {
        return content;
    }

    public void setContent(List<T> content) {
        this.content = content;
    }

    public int getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(int totalPages) {
        this.totalPages = totalPages;
    }

    public int getTotalElements() {
        return totalElements;
    }

    public void setTotalElements(int totalElements) {
        this.totalElements = totalElements;
    }

    public double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(double totalAmount) {
        this.totalAmount = totalAmount;
    }
}

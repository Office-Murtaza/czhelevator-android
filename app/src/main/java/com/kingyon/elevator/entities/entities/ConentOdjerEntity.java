package com.kingyon.elevator.entities.entities;

/**
 * @Created By Admin  on 2020/5/9
 * @Email : 163235610@qq.com
 * @Author:Mrczh
 * @Instructions: 内容头
 */
public class ConentOdjerEntity {

    private int totalElements;
    private int size;
    private int totalPages;
    private Object totalAmount;
    private CityFacilityInfoEntiy pageContent;

    @Override
    public String toString() {
        return "ConentOdjerEntity{" +
                "totalElements=" + totalElements +
                ", size=" + size +
                ", totalPages=" + totalPages +
                ", totalAmount=" + totalAmount +
                ", pageContent=" + pageContent +
                '}';
    }

    public CityFacilityInfoEntiy getPageContent() {
        return pageContent;
    }

    public void setPageContent(CityFacilityInfoEntiy pageContent) {
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


}

package com.kingyon.elevator.entities;

/**
 * Created by GongLi on 2019/1/11.
 * Email：lc824767150@163.com
 */

public class InvoiceInfoEntity {

    /**
     * invoiced : 3265.08
     * waitting : 5632.02
     */

    private double invoiced;
    private double waitting;

    public double getInvoiced() {
        return invoiced;
    }

    public void setInvoiced(double invoiced) {
        this.invoiced = invoiced;
    }

    public double getWaitting() {
        return waitting;
    }

    public void setWaitting(double waitting) {
        this.waitting = waitting;
    }
}

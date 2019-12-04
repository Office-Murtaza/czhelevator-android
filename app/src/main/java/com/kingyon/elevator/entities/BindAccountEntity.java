package com.kingyon.elevator.entities;

/**
 * Created By SongPeng  on 2019/12/3
 * Email : 1531603384@qq.com
 */
public class BindAccountEntity {

    private int id;
    private int cashType; //1银行卡  2支付宝
    private String cashAccount;
    private String cashName;
    private String openingBank;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCashType() {
        return cashType;
    }

    public void setCashType(int cashType) {
        this.cashType = cashType;
    }

    public String getCashAccount() {
        return cashAccount;
    }

    public void setCashAccount(String cashAccount) {
        this.cashAccount = cashAccount;
    }

    public String getCashName() {
        return cashName;
    }

    public void setCashName(String cashName) {
        this.cashName = cashName;
    }

    public String getOpeningBank() {
        return openingBank;
    }

    public void setOpeningBank(String openingBank) {
        this.openingBank = openingBank;
    }


}

package com.kingyon.elevator.entities;

/**
 * Created by GongLi on 2019/1/14.
 * Email：lc824767150@163.com
 */

public class PropertyEntity {
    private boolean bePropertyCell;
    private boolean bePropertyCompany;
    private PropertyInfoEntity info;
    private PropertyIdentityEntity identity;

    public boolean isBePropertyCell() {
        return bePropertyCell;
    }

    public void setBePropertyCell(boolean bePropertyCell) {
        this.bePropertyCell = bePropertyCell;
    }

    public PropertyInfoEntity getInfo() {
        return info;
    }

    public void setInfo(PropertyInfoEntity info) {
        this.info = info;
    }

    public PropertyIdentityEntity getIdentity() {
        return identity;
    }

    public void setIdentity(PropertyIdentityEntity identity) {
        this.identity = identity;
    }

    public boolean isBePropertyCompany() {
        return bePropertyCompany;
    }

    public void setBePropertyCompany(boolean bePropertyCompany) {
        this.bePropertyCompany = bePropertyCompany;
    }
}

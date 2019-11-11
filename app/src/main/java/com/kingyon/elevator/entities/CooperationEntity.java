package com.kingyon.elevator.entities;

public class CooperationEntity {
    private boolean bePartner;
    private CooperationInfoEntity info;
    private CooperationIdentityEntity identity;

    public CooperationInfoEntity getInfo() {
        return info;
    }

    public void setInfo(CooperationInfoEntity info) {
        this.info = info;
    }

    public CooperationIdentityEntity getIdentity() {
        return identity;
    }

    public void setIdentity(CooperationIdentityEntity identity) {
        this.identity = identity;
    }

    public boolean isBePartner() {
        return bePartner;
    }

    public void setBePartner(boolean bePartner) {
        this.bePartner = bePartner;
    }
}

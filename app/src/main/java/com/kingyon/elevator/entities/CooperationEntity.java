package com.kingyon.elevator.entities;

public class CooperationEntity {
    private boolean bePartner;
    private CooperationInfoNewEntity info;
    private CooperationIdentityEntity identity;

    public CooperationInfoNewEntity getInfo() {
        return info;
    }

    public void setInfo(CooperationInfoNewEntity info) {
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

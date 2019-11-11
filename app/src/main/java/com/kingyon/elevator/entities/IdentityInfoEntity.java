package com.kingyon.elevator.entities;

/**
 * Created by GongLi on 2019/1/3.
 * Email：lc824767150@163.com
 */

public class IdentityInfoEntity {

    /**
     * type : company
     * companyName : edd
     * businessCert : 经营许可证
     * personName : name
     * idNum : 511321785555
     * idBack :
     * idFace :
     * faildReason :
     * status : auth
     */

    private String type;
    private String companyName;
    private String businessCert;
    private String personName;
    private String idNum;
    private String idBack;
    private String idFace;
    private String faildReason;
    private String status;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getBusinessCert() {
        return businessCert;
    }

    public void setBusinessCert(String businessCert) {
        this.businessCert = businessCert;
    }

    public String getPersonName() {
        return personName;
    }

    public void setPersonName(String personName) {
        this.personName = personName;
    }

    public String getIdNum() {
        return idNum;
    }

    public void setIdNum(String idNum) {
        this.idNum = idNum;
    }

    public String getIdBack() {
        return idBack;
    }

    public void setIdBack(String idBack) {
        this.idBack = idBack;
    }

    public String getIdFace() {
        return idFace;
    }

    public void setIdFace(String idFace) {
        this.idFace = idFace;
    }

    public String getFaildReason() {
        return faildReason;
    }

    public void setFaildReason(String faildReason) {
        this.faildReason = faildReason;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}

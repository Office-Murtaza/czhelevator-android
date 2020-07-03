package com.kingyon.elevator.entities.entities;

/**
 * @Created By Admin  on 2020/6/29
 * @Email : 163235610@qq.com
 * @Author:Mrczh
 * @Instructions:
 */
public class PartnershipStatusEntily {

    /**
     *     "id": 7,
     *     "applyAccount": "8225633536943650872",
     *     "applyTime": 1593420171000,
     *     "cityCode": "520100",
     *     "auditAccount": null,
     *     "auditTime": null,
     *     "auditStatus": "AUDITING",
     *     "note": null
     * */

    public int id;
    public String applyAccount;
    public long applyTime;
    public String cityCode;
    public String auditAccount;
    public String auditTime;
    public String auditStatus;
    public String note;

    @Override
    public String toString() {
        return "PartnershipStatusEntily{" +
                "id=" + id +
                ", applyAccount='" + applyAccount + '\'' +
                ", applyTime=" + applyTime +
                ", cityCode='" + cityCode + '\'' +
                ", auditAccount='" + auditAccount + '\'' +
                ", auditTime='" + auditTime + '\'' +
                ", auditStatus='" + auditStatus + '\'' +
                ", note='" + note + '\'' +
                '}';
    }
}

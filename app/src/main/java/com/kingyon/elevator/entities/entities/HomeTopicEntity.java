package com.kingyon.elevator.entities.entities;

/**
 * @Created By Admin  on 2020/5/12
 * @Email : 163235610@qq.com
 * @Author:Mrczh
 * @Instructions:
 */
public class HomeTopicEntity {
    
    public int id;
    public String labelName;
    public long createTime;
    public Object reserve1;
    public Object reserve2;

    @Override
    public String toString() {
        return "HomeTopicEntity{" +
                "id=" + id +
                ", labelName='" + labelName + '\'' +
                ", createTime=" + createTime +
                ", reserve1=" + reserve1 +
                ", reserve2=" + reserve2 +
                '}';
    }
}

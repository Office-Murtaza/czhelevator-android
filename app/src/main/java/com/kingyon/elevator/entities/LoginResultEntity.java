package com.kingyon.elevator.entities;

/**
 * Created by GongLi on 2018/12/24.
 * Email：lc824767150@163.com
 */

public class LoginResultEntity {
    private String token;
    private UserEntity user;
    private boolean needFill;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public UserEntity getUser() {
        return user;
    }

    public void setUser(UserEntity user) {
        this.user = user;
    }

    public boolean isNeedFill() {
        return needFill;
    }

    public void setNeedFill(boolean needFill) {
        this.needFill = needFill;
    }
}

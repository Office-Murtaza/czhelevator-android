package com.kingyon.elevator.entities.entities;

import com.kingyon.elevator.entities.UserEntity;

/**
 * @Created By Admin  on 2020/4/27
 * @Email : 163235610@qq.com
 * @Author:Mrczh
 * @Instructions:
 */
public class CodeEntity {

        private String token;
        private UserEntity user;
        private boolean needFill;
        private boolean needSetPwd;

    public boolean isNeedSetPwd() {
        return needSetPwd;
    }

    public void setNeedSetPwd(boolean needSetPwd) {
        this.needSetPwd = needSetPwd;
    }

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

        public static class UserBean {
            /**
             * objectId : 104001
             * avatar : http://cdn.tlwgz.com/FhGjpKVzevLfCt3cmbIrKeUNg56G
             * nikeName : Android测试
             * phone : 18892332998
             * role : ,NORMAL,PARTNER
             * authentication : true
             */

            private int objectId;
            private String avatar;
            private String nikeName;
            private String phone;
            private String role;
            private boolean authentication;

            public int getObjectId() {
                return objectId;
            }

            public void setObjectId(int objectId) {
                this.objectId = objectId;
            }

            public String getAvatar() {
                return avatar;
            }

            public void setAvatar(String avatar) {
                this.avatar = avatar;
            }

            public String getNikeName() {
                return nikeName;
            }

            public void setNikeName(String nikeName) {
                this.nikeName = nikeName;
            }

            public String getPhone() {
                return phone;
            }

            public void setPhone(String phone) {
                this.phone = phone;
            }

            public String getRole() {
                return role;
            }

            public void setRole(String role) {
                this.role = role;
            }

            public boolean isAuthentication() {
                return authentication;
            }

            public void setAuthentication(boolean authentication) {
                this.authentication = authentication;
            }
        }
}

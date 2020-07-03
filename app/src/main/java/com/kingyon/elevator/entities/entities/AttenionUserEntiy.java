package com.kingyon.elevator.entities.entities;

import com.bobomee.android.mentions.edit.listener.InsertData;
import com.bobomee.android.mentions.model.FormatRange;

import java.io.Serializable;

/**
 * @Created By Admin  on 2020/5/19
 * @Email : 163235610@qq.com
 * @Author:Mrczh
 * @Instructions:
 */
public class AttenionUserEntiy implements Serializable, InsertData {

    /**
     *         "id": 100006,
     *         "followerAccount": "6096907287229370962",
     *         "beFollowerAccount": "5196646915807042859",
     *         "createTime": 1589860771000,
     *         "nickname": "＆",
     *         "photo": "http://thirdqq.qlogo.cn/g?b=oidb&k=NABxwwWMHiajw9D3T7EaTlg&s=100&t=1571720286"
     * */

    public int id;
    public String followerAccount;
    public String beFollowerAccount;
    public String nickname;
    public String photo;
    public String personalizedSignature;
    public long createTime;
    public int isAttention;


    @Override
    public String toString() {
        return "AttenionUserEntiy{" +
                "id=" + id +
                ", followerAccount='" + followerAccount + '\'' +
                ", beFollowerAccount='" + beFollowerAccount + '\'' +
                ", nickname='" + nickname + '\'' +
                ", photo='" + photo + '\'' +
                ", createTime=" + createTime +
                '}';
    }

    @Override public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        AttenionUserEntiy user = (AttenionUserEntiy) o;

        if (followerAccount != null ? !followerAccount.equals(user.followerAccount) : user.followerAccount != null) return false;
        if (nickname != null ? !nickname.equals(user.nickname) : user.nickname != null)
            return false;
        return false;
    }

    @Override public int hashCode() {
        int result = followerAccount != null ? followerAccount.hashCode() : 0;
        result = 31 * result + (nickname != null ? nickname.hashCode() : 0);
        return result;
    }

    @Override
    public CharSequence charSequence() {
        return "@"+nickname;
    }

    @Override
    public FormatRange.FormatData formatData() {
        return new UserConvert(this);
    }

    @Override
    public int color() {
        return 0xFF4DACEE;
    }

    private class UserConvert implements FormatRange.FormatData {

        public static final String USER_FORMART = "&nbsp;<user id='%s' name='%s' style='color: #4dacee;'>%s</user>&nbsp;";
        private final AttenionUserEntiy user;

        public UserConvert(AttenionUserEntiy user) {
            this.user = user;
        }

        @Override public CharSequence formatCharSequence() {
            return String.format(USER_FORMART, user.beFollowerAccount,user.nickname, "@"+user.nickname);
        }
    }
}

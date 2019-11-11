package com.kingyon.library.social;

import android.content.Context;
import android.text.TextUtils;

import java.util.HashMap;

import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.PlatformDb;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.sina.weibo.SinaWeibo;
import cn.sharesdk.tencent.qq.QQ;
import cn.sharesdk.wechat.friends.Wechat;

public class AuthorizeUtils implements PlatformActionListener {

    private AuthorizeListener authorizeListener;

    private Context mContext;

    public AuthorizeUtils(Context mContext, AuthorizeListener authorizeListener) {
        super();
        this.authorizeListener = authorizeListener;
        this.mContext = mContext;
    }
//更新至shareSdk3.0注释
//	public void authSina() {
//		Platform sinaWeibo = ShareSDK.getPlatform(mContext, SinaWeibo.NAME);
//		authorize(sinaWeibo);
//	}
//
//	public void authWechat() {
//		Platform sinaWeibo = ShareSDK.getPlatform(mContext, Wechat.NAME);
//		authorize(sinaWeibo);
//	}
//
//	public void authQQ() {
//		Platform sinaWeibo = ShareSDK.getPlatform(mContext, QQ.NAME);
//		authorize(sinaWeibo);
//	}

    public void authSina() {
        Platform sinaWeibo = ShareSDK.getPlatform(SinaWeibo.NAME);
        authorize(sinaWeibo);
    }

    public void authWechat() {
        Platform sinaWeibo = ShareSDK.getPlatform(Wechat.NAME);
        authorize(sinaWeibo);
    }

    public void authQQ() {
        Platform sinaWeibo = ShareSDK.getPlatform(QQ.NAME);
        authorize(sinaWeibo);
    }

    private void authorize(Platform plat) {
        plat.setPlatformActionListener(this);
        if (plat.isAuthValid()) {
            plat.removeAccount(true);
        }
        plat.SSOSetting(false);
        plat.showUser(null);
    }

    @Override
    public void onCancel(Platform arg0, int arg1) {
        if (authorizeListener != null) {
            authorizeListener.onError();
        }
    }

    @Override
    public void onComplete(Platform platform, int action, HashMap<String, Object> res) {


        AuthorizeUser authorizeUser = new AuthorizeUser();

        if (action == Platform.ACTION_USER_INFOR) {
            PlatformDb platDB = platform.getDb();
            authorizeUser.setUsername(platDB.getUserId());
            authorizeUser.setNickname(platDB.getUserName());
            if (!TextUtils.isEmpty(platDB.getUserGender())) {
                authorizeUser.setSex("m".equals(platDB.getUserGender()) ? "M" : "F");
            }
            authorizeUser.setNativePlace("");
            authorizeUser.setHeadimgurl(platDB.getUserIcon());
            authorizeUser.setPlat_form(platDB.getPlatformNname());
        } else {
            if (TextUtils.equals(platform.getName(), Wechat.NAME)) {
                authorizeUser.setUsername(getItemValue("openid", res));
                authorizeUser.setNickname(getItemValue("nickname", res));
                String sex = getItemValue("sex", res);
                if (!TextUtils.isEmpty(sex)) {
                    authorizeUser.setSex(TextUtils.equals(sex, "2") ? "M" : "F");
                }
                authorizeUser.setNativePlace(getItemValue("country", res) + getItemValue("city", res));
                authorizeUser.setHeadimgurl(getItemValue("headimgurl", res));
                authorizeUser.setPlat_form(Wechat.NAME);
            } else if (TextUtils.equals(platform.getName(), QQ.NAME)) {

                String foto = getItemValue("figureurl_2", res);
                if (foto != null && foto.length() > 10) {
                    String[] all = foto.split("/");
                    authorizeUser.setUsername(all[all.length - 2]);
                }
                authorizeUser.setNickname(getItemValue("nickname", res));
                String sex = getItemValue("gender", res);
                if (!TextUtils.isEmpty(sex)) {
                    authorizeUser.setSex(TextUtils.equals(sex, "男") ? "M" : "F");
                }
                authorizeUser.setNativePlace(getItemValue("province", res) + getItemValue("city", res));
                authorizeUser.setHeadimgurl(getItemValue("figureurl_2", res));
                authorizeUser.setPlat_form(QQ.NAME);

            } else if (TextUtils.equals(platform.getName(), SinaWeibo.NAME)) {
                authorizeUser.setPlat_form(SinaWeibo.NAME);
            }
        }
        judgetPlat(authorizeUser);
        if (authorizeUser.getUsername() != null && authorizeListener != null) {
            authorizeListener.onComplete(authorizeUser);
        }
    }

    private void judgetPlat(AuthorizeUser authorizeUser) {
        if (TextUtils.equals(QQ.NAME, authorizeUser.getPlat_form())) {
            authorizeUser.setPlat_form("TENCENT_QQ");
        } else if (TextUtils.equals(SinaWeibo.NAME, authorizeUser.getPlat_form())) {
            authorizeUser.setPlat_form("XINLAN_WEIBO");
        } else if (TextUtils.equals(Wechat.NAME, authorizeUser.getPlat_form())) {
            authorizeUser.setPlat_form("TENCENT_WEI_XIN");
        }
    }

    private String getItemValue(String key, HashMap<String, Object> res) {
        if (res.containsKey(key)) {
            return res.get(key).toString();
        }
        return null;
    }

    @Override
    public void onError(Platform arg0, int arg1, Throwable arg2) {
        arg2.printStackTrace();
        if (authorizeListener != null) {
            authorizeListener.onError();
        }
    }

    public interface AuthorizeListener {
        void onComplete(AuthorizeUser user);

        void onError();
    }

    public void setAuthorizeListener(AuthorizeListener authorizeListener) {
        this.authorizeListener = authorizeListener;
    }

}

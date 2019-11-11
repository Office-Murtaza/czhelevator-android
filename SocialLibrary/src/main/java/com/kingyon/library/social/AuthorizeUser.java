package com.kingyon.library.social;


public class AuthorizeUser {
	
	private String username;
	
	private String nickname;
	
	/**
	 * M MAN    F FEMALE
	 */
	private String sex;
	
	private String nativePlace;
	
	private String headimgurl;
	
	private String plat_form;
	
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getNickname() {
		return nickname;
	}
	public void setNickname(String nickname) {
		this.nickname = nickname;
	}
	public String getSex() {
		return sex;
	}
	public void setSex(String sex) {
		this.sex = sex;
	}
	public String getNativePlace() {
		return nativePlace;
	}
	public void setNativePlace(String nativePlace) {
		this.nativePlace = nativePlace;
	}
	public String getHeadimgurl() {
		return headimgurl;
	}
	public void setHeadimgurl(String headimgurl) {
		this.headimgurl = headimgurl;
	}
	public String getPlat_form() {
		return plat_form;
	}
	public void setPlat_form(String plat_form) {
		this.plat_form = plat_form;
	}

	@Override
	public String toString() {
		return "AuthorizeUser{" +
				"username='" + username + '\'' +
				", nickname='" + nickname + '\'' +
				", sex='" + sex + '\'' +
				", nativePlace='" + nativePlace + '\'' +
				", headimgurl='" + headimgurl + '\'' +
				", plat_form='" + plat_form + '\'' +
				'}';
	}
}

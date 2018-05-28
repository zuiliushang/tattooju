package com.tattooju.dto;

public class WechatAccountDto {

	/**
	 * 微信用户名
	 */
	private String userName;
	
	/**
	 * 微信的OpenId
	 */
	private String openId;
	
	/**
	 * 微信头像
	 */
	private String headimgurl;

	/**
	 * 登录凭证
	 */
	private String token;

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getOpenId() {
		return openId;
	}

	public void setOpenId(String openId) {
		this.openId = openId;
	}

	public String getHeadimgurl() {
		return headimgurl;
	}

	public void setHeadimgurl(String headimgurl) {
		this.headimgurl = headimgurl;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}
	
}

package com.tattooju.dto;

public class WechatAccountDto {

	/**
	 * 微信用户名
	 */
	private String nickName;
	
	/**
	 * 微信的OpenId
	 */
	private String openId;
	
	/**
	 * 微信头像
	 */
	private String headimgurl;

	/**
	 * 性别
	 */
	private byte sex;
	
	/**
	 * 登录凭证
	 */
	private String token;

	public byte getSex() {
		return sex;
	}

	public void setSex(byte sex) {
		this.sex = sex;
	}

	public String getNickName() {
		return nickName;
	}

	public void setNickName(String nickName) {
		this.nickName = nickName;
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

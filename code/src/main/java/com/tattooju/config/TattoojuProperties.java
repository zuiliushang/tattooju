package com.tattooju.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class TattoojuProperties {

	@Value("${wechat.url.appId}")
	private String wechatAppId;
	
	@Value("${wechat.url.appSecret}")
	private String wechatAppSecret;

	@Value("${token.verify.switch}")
	private Boolean tokenVerifySwitch;
	
	@Value("${token.verify.ttl}")
	private Long tokenVerifyTTL;
	
	public Long getTokenVerifyTTL() {
		return tokenVerifyTTL;
	}

	public void setTokenVerifyTTL(Long tokenVerifyTTL) {
		this.tokenVerifyTTL = tokenVerifyTTL;
	}

	public Boolean getTokenVerifySwitch() {
		return tokenVerifySwitch;
	}

	public void setTokenVerifySwitch(Boolean tokenVerifySwitch) {
		this.tokenVerifySwitch = tokenVerifySwitch;
	}

	public String getWechatAppId() {
		return wechatAppId;
	}

	public void setWechatAppId(String wechatAppId) {
		this.wechatAppId = wechatAppId;
	}

	public String getWechatAppSecret() {
		return wechatAppSecret;
	}

	public void setWechatAppSecret(String wechatAppSecret) {
		this.wechatAppSecret = wechatAppSecret;
	}
	
}

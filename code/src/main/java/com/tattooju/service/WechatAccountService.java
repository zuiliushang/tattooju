package com.tattooju.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tattooju.entity.WechatAccount;
import com.tattooju.entity.WechatAccountMapper;

@Service
public class WechatAccountService extends BaseService<WechatAccount>{

	@Autowired
	WechatAccountMapper wechatAccountMapper;
	
	public int getLastId() {
		return wechatAccountMapper.getLastId();
	}
	
}

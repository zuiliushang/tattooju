package com.tattooju.ctrl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.tattooju.business.WechatBusiness;
import com.tattooju.config.ResponseContent;
import com.tattooju.dto.WechatAccountDto;

@RestController
@RequestMapping("/account/")
public class AccountCtrl {
	
	@Autowired
	WechatBusiness wechatBusiness;
	
	@GetMapping("auth")
	@ResponseBody
	public ResponseContent wechatAuth(@RequestParam(required=true) String code) throws Exception {
		WechatAccountDto result = wechatBusiness.bindWechat(code);
		return ResponseContent.ok(result);
	}
	
}

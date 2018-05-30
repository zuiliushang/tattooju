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
	public ResponseContent wechatAuth(@RequestParam(required=true) String code,
			@RequestParam(required=true) String iv,
			@RequestParam(required=true) String encryptedData
			) throws Exception {
		System.out.println("============================获取到的code==>"+code);
		System.out.println("============================获取到的iv==>"+iv);
		System.out.println("============================获取到的encryptedData==>"+encryptedData);
		WechatAccountDto result = wechatBusiness.bindWechat(code,iv,encryptedData);
		return ResponseContent.ok(result);
	}
	
}

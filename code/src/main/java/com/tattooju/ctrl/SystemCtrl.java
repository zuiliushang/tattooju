package com.tattooju.ctrl;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@RequestMapping("/sys/")
public class SystemCtrl {

	@RequestMapping("greeting")
	@ResponseBody
	public String greeting() {
		return "hello world.";
	}
	
}

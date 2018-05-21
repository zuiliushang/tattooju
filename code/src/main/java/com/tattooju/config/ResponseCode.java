package com.tattooju.config;

public enum ResponseCode {
	OK(1000,"ok"),
	TEST(1,"test"),
	TOKEN_INVALID(1001,"访问令牌已失效,请重新登录"),
	FAILED(500,"请求失败"),
	NULLPARAMETER(50001,"请求参数为空"),
	NOT_SUP_FILE(10010,"不支持的文件类型"),
	REPORT_NOT_EXISTS(50002,"报告不存在"),
	PARSER_FALURE(50003,"报告解析失败"),
	FILE_MD5_CHECKOUT_FAULT(50004,"md5校验失败"),
	INST_NOT_EXISTS(50005,"不存在的机构");
	
	private int value;
	
	private String msg;

	private ResponseCode(int value, String msg) {
		this.value = value;
		this.msg = msg;
	}

	public int getValue() {
		return value;
	}

	public void setValue(int value) {
		this.value = value;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}
	
	
	
}

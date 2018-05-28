package com.tattooju.exception;


import com.tattooju.config.ResponseCode;

public class NullParamException extends CommonException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3240806493593104594L;

	public NullParamException() {
		super();
	}

	public NullParamException(int code, String description) {
		super(code, description + "不能为空");
	}

	public NullParamException(String field) {
		super(ResponseCode.FAILED.getValue(), field + "不能为空");
	}
	
}

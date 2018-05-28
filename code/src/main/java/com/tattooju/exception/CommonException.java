package com.tattooju.exception;

import com.tattooju.config.ResponseCode;

public class CommonException extends Exception {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	protected int code;
	protected String description;

	public static final int SUCCESS_CODE_COMMON=1000;
	
	public static final int ERROR_CODE_COMMON=1001;
	
	public CommonException(int code, String description) {
		super(description);
		this.code = code;
		this.description = description;
	}
	
	public CommonException(ResponseCode code) {
		super(code.getMsg());
		this.code = code.getValue();
		this.description = code.getMsg();
	}
	
	public CommonException() {
		super();
	}

	public CommonException(String description) {
		super(description);
		this.description = description;
	}

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	
}

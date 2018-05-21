package com.tattooju.config;


public class CommonException extends Exception {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	protected int code;
	protected String description;

	
	
	public CommonException(int code, String description) {
		super(description);
		this.code = code;
		this.description = description;
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

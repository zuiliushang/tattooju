package com.tattooju.config;

/**
 * 
 * @desc 统一返回结构
 * @author xusihan
 * @date 2018-01-17
 */
public class ResponseContent  {
	
	/**
	 * 响应码
	 */
	private int code;
	/**
	 * 具体数据
	 */
	private Object data;
	/**
	 * 响应信息
	 */
	private String msg;

	
	
	public ResponseContent() {
		super();
		// TODO Auto-generated constructor stub
	}

	public ResponseContent(int code, Object data, String msg) {
		super();
		this.code = code;
		this.data = data;
		this.msg = msg;
	}
	
	public ResponseContent(ResponseCode code, Object data) {
		super();
		this.code = code.getValue();
		this.data = data;
		this.msg = code.getMsg();
	}
	
	public static ResponseContent ok(Object data) {
		ResponseContent responseContent = new ResponseContent(ResponseCode.OK.getValue(), data, ResponseCode.OK.getMsg());
		return responseContent;
	}
	
	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public Object getData() {
		return data;
	}

	public void setData(Object data) {
		this.data = data;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}
	
}

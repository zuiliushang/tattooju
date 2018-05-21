package com.tattooju.config;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartException;


/**
 * 
 * 全局异常处理
 * @author xusihan
 * @date 2018-05-21
 *
 */
@ControllerAdvice
public class GlobalExceptionHandler {

	private Logger logger = LoggerFactory.getLogger(getClass());
	
	/**
	 * rest 业务错误处理
	 * @param request
	 * @param ex
	 * @return
	 */
	@ExceptionHandler({Exception.class})
	@ResponseBody
	public ResponseContent handleRestRespErrorException(HttpServletRequest request, Exception ex) {
		logger.error("GlobalExceptionHandler 处理错误 => [ex->{}]",ex.getMessage());
	 	ex.printStackTrace();
		CommonException exception = null;
		if (ex instanceof CommonException) {
			exception = (CommonException) ex;
			return new ResponseContent(exception.getCode(),null,exception.getMessage());
		}
		if (ex instanceof MultipartException) {
			return new ResponseContent(ResponseCode.FAILED,null);
		}
		return new ResponseContent(ResponseCode.FAILED, null);
	}
}

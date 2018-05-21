package com.tattooju.util;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 */
public class RequestUtil {
	
	private static Logger logger=LoggerFactory.getLogger(RequestUtil.class);
	
	/**
	 * 从请求头中获取用户id
	 * @param request
	 * @return
	 */
	public static Long getUserIdFromHeader(HttpServletRequest request){
		Long userId=null;
		String uid=request.getHeader("uid");		
		try {
			 userId=Long.parseLong(uid);
		} catch (NumberFormatException e1) {		
			logger.error("Invalid uid from reqeust head {}",uid);
			return null;
		}
		
		return userId;
	}


	public static String getRemoteHost(HttpServletRequest request) {
		
		String ip=request.getRemoteHost();
		  
		 if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
	            ip = request.getRemoteAddr();
	     }
		
		if(ip==null || ip.length()==0 || "unkown".equalsIgnoreCase(ip)){
			ip = request.getHeader("x-forwarded-for");
		}
       
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
              
        
        return "0:0:0:0:0:0:0:1".equals(ip) ? "127.0.0.1" : ip;
    }
	public static String getHtmlHost(HttpServletRequest request) {
			
		String ip="";
		  
		 
		
		if(ip==null || ip.length()==0 || "unkown".equalsIgnoreCase(ip)){
			ip = request.getHeader("X-Forwarded-For");
	        logger.info("从X-Forwarded-For获取终端ip为{}",ip);
		}
       
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("X-Real-IP");
            logger.info("从X-Real-IP获取终端ip为{}",ip);
        }
        
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("PROXY_FORWARDED_FOR");
            logger.info("从PROXY_FORWARDED_FOR获取终端ip为{}",ip);
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
            logger.info("从RemoteAddr获取终端ip为{}",ip);

        }    
//        if(ip.contains(",")) {
//        	ip = ip.substring(ip.lastIndexOf(",")+1);
//        }
        logger.info("获取终端ip为{}",ip);
        if(ip.contains(",")) {
        	ip = ip.substring(0,ip.indexOf(","));
        }
        return "0:0:0:0:0:0:0:1".equals(ip.trim()) ? "127.0.0.1" : ip.trim();
    }
	/**
	 * 获取用户请求的uri
	 * @param request
	 * @return
	 */
	public static String getRequestUri(HttpServletRequest request) {
		String requestURI = request.getRequestURI(); // 包含webapp应用上下文名字的相对路径
		String contextPath=getRequestContextPath(request);//webapp应用上下文名字
		
		if (contextPath != null && requestURI.startsWith(contextPath)) {
			requestURI = requestURI.substring(contextPath.length());
			if (!requestURI.startsWith("/")) {
				requestURI = "/" + requestURI;
			}
		}
		
		return requestURI;
		
	}
	
	/**
	 * 获取用户请求的应用上下文
	 * @param request
	 * @return
	 */
	public static String getRequestContextPath(HttpServletRequest request) {
		
		String contextPath = request.getContextPath();

		if (contextPath == null || contextPath.length() == 0) {
			contextPath = "/";
		}
		
		return contextPath;
	}
	
}

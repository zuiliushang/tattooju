package com.tattooju.ctrl.interceptor;

import java.io.PrintWriter;
import java.util.concurrent.TimeUnit;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSON;
import com.tattooju.config.ResponseCode;
import com.tattooju.config.ResponseContent;
import com.tattooju.config.SpringContext;
import com.tattooju.config.TattoojuConstant;
import com.tattooju.config.TattoojuProperties;
import com.tattooju.util.JwtUtil;

public class TokenInterceptor implements HandlerInterceptor{

	private Logger logger = LoggerFactory.getLogger(getClass());
	
	private StringRedisTemplate stringRedisTemplate = SpringContext.getBean(StringRedisTemplate.class);
	
	private TattoojuProperties tattoojuProperties = SpringContext.getBean(TattoojuProperties.class);
	
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		// 标识token是否通过校验
				boolean isPass = true;
				// 开关控制是否校验token
				if (tattoojuProperties.getTokenVerifySwitch()) {
					String token = request.getHeader("token");
					if (StringUtils.isEmpty(token)) {
						// token在请求头中取不到，需要从url请求参数中取,下载文件时使用此中方式
						if (logger.isDebugEnabled()) {
							logger.debug("请求头中读取不到token,将从url中读取");
						}
						token = request.getParameter("token");
						if (StringUtils.isEmpty(token)) {
							logger.error("url中读取不到token");
							isPass = false;
						}
					}

					logger.debug("header token:{}", token);
					if (isPass) {
						// 校验token格式是否正确
						try {
							
							String key = TattoojuConstant.PREFIX_ACCESS_TOKEN + JwtUtil.getUserId(JwtUtil.JWT_SECRET, token);
							String cachedToken = stringRedisTemplate.opsForValue().get(key);
							logger.debug("cached token key:{}", key);
							logger.debug("cached token:{}", cachedToken);
							
							if (!token.equals(cachedToken)) {
								logger.error("token无效");
								isPass = false;
							} else {
								// token有效,则将其有效时长继续延后
								stringRedisTemplate.expire(key, tattoojuProperties.getTokenVerifyTTL(), TimeUnit.MINUTES);
							}

						} catch (Exception e) {
							logger.error("解析token异常", e);
							isPass = false;
						}
					}

					// TOKEN无效则返回错误提示
					if (!isPass) {
						ResponseContent responseContent = new ResponseContent(ResponseCode.TOKEN_INVALID, null);
						// response.setCharacterEncoding("UTF-8");

						response.setContentType("application/json; charset=UTF-8");

						PrintWriter out = response.getWriter();
						out.println(JSON.toJSONString(responseContent));
						out.flush();
						out.close();
					}
				}
				return isPass;
	}

	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
			throws Exception {
		// TODO Auto-generated method stub
		
	}
	
}

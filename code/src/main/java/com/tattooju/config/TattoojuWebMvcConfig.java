package com.tattooju.config;

import java.nio.charset.Charset;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.tattooju.ctrl.interceptor.TokenInterceptor;
import com.tattooju.util.RequestUtil;

@Configuration
public class TattoojuWebMvcConfig extends WebMvcConfigurerAdapter{
private Logger logger=LoggerFactory.getLogger(TattoojuWebMvcConfig.class);
	
	@Value("${token.interceptor.exclude.url}")
	private String[] excludePathPatterns;
	
	//添加IP拦截器
	@Override
	 public void addInterceptors(InterceptorRegistry registry) {
		 logger.debug("添加IP拦截器.");
	      registry.addInterceptor(new HandlerInterceptorAdapter() {

	          @Override
	          public boolean preHandle(HttpServletRequest request, HttpServletResponse response,
	                                   Object handler) throws Exception {
	        	  String clientIp= RequestUtil.getRemoteHost(request);
	        	  logger.info("客户IP({})对{}的{}请求.",clientIp,RequestUtil.getRequestUri(request),request.getMethod());
	        		  return true;
	          }
	      }).addPathPatterns("/**").excludePathPatterns("/favicon.ico");
	      
	      //	      增加校验访问令牌的拦截器
	     /* registry.addInterceptor(new TokenInterceptor()).addPathPatterns("/**")
	      .excludePathPatterns(excludePathPatterns);*/
	  }
	
	/**
	 * 
	 * @Description 增加自定义的jackson的序列化配置,将long转为string输出<br>
	 * springboot提供Jackson2ObjectMapperBuilderCustomizer，凡是此类的实例，都会被应用到Jackson2ObjectMapperBuilder所产生的ObjectMapper 
	 * @Author lzq 
	 * Created on 2017年10月11日  
	 * @return
	 */
	@Bean
	public Jackson2ObjectMapperBuilderCustomizer addCustomBigDecimalDeserialization() {
		return new Jackson2ObjectMapperBuilderCustomizer() {
			@Override
			public void customize(Jackson2ObjectMapperBuilder jacksonObjectMapperBuilder) {
				  SimpleModule simpleModule = new SimpleModule();
			      simpleModule.addSerializer(Long.class, ToStringSerializer.instance);
			      simpleModule.addSerializer(Long.TYPE, ToStringSerializer.instance);
				jacksonObjectMapperBuilder.modulesToInstall(simpleModule);
				
			}

		};
	}
	

	
	@Override
	public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
		converters.add(new StringHttpMessageConverter(Charset.forName("UTF-8")));
		super.configureMessageConverters(converters);
	}

}

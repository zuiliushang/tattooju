package com.tattooju.util;

import java.util.Date;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import com.tattooju.config.CommonException;
import com.tattooju.config.ResponseCode;
import com.tattooju.entity.WechatAccount;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

public class JwtUtil {
	
	private static Logger logger=LoggerFactory.getLogger(JwtUtil.class);
	
	/**
	 * jwt相关设置
	 */
	public static final String JWT_ID = "jwt";
	public static final String JWT_SECRET = "7786df7fc3a34e26a61c034d5ec8245d";
	public static final int JWT_TTL = 60*60*1000;  //millisecond， TTL=time to live
	public static final int JWT_REFRESH_INTERVAL = 55*60*1000;  //millisecond
	public static final int JWT_REFRESH_TTL = 12*60*60*1000;  //millisecond
	
	public static final String JWT_CLAIM_ACCOUNT_ID="id";
	public static final String JWT_CLAIM_NONCE="tattooju";//随机字符串
	
	/**
	 * 由字符串生成加密key
	 * @return
	 */
	public static SecretKey generalKey(){
		String stringKey = JWT_SECRET;
		byte[] encodedKey = Base64.decodeBase64(stringKey);
	    SecretKey key = new SecretKeySpec(encodedKey, 0, encodedKey.length, "AES");
	    return key;
	}

	/**
	 * 创建jwt
	 * @param id
	 * @param subject
	 * @param ttlMillis
	 * @return
	 * @throws Exception
	 */
	public static String createJWT(String key,String subject, long ttlMillis) throws Exception {
		SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;
		long nowMillis = System.currentTimeMillis();
		Date now = new Date(nowMillis);
		JwtBuilder builder = Jwts.builder()
			.setIssuedAt(now)
			.setSubject(subject)
		    .signWith(signatureAlgorithm, key);
		if (ttlMillis >= 0) {
		    long expMillis = nowMillis + ttlMillis;
		    Date exp = new Date(expMillis);
		    builder.setExpiration(exp);
		}
		return builder.compact();
	}
	
	/**
	 * 创建jwt
	 * @param id
	 * @param subject
	 * @param ttlMillis
	 * @return
	 * @throws Exception
	 */
	public static String createJWT(WechatAccount wechatAccount,String key, long ttlMillis)  {
		SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;
		long nowMillis = System.currentTimeMillis();
		Date now = new Date(nowMillis);
		String nonce=RamdomUtil.generateRandomString(6);
		
		JwtBuilder builder = Jwts.builder()
			.setIssuedAt(now)
			.claim(JWT_CLAIM_ACCOUNT_ID, wechatAccount.getId())
			.claim(JWT_CLAIM_NONCE, nonce)
		    .signWith(signatureAlgorithm, key);
		
		if(!StringUtils.isEmpty(wechatAccount.getRole()+"")) {
			builder.setSubject(wechatAccount.getRole()+"");
		}
		//不设置过期时间,使用随机字符串代替
//		if (ttlMillis > 0) {
//		    long expMillis = nowMillis + ttlMillis;
//		    Date exp = new Date(expMillis);
//		    builder.setExpiration(exp);
//		}
		return builder.compact();
	}
	
	/**
	 * 解密jwt
	 * @param jwt
	 * @return
	 * @throws Exception
	 */
	public static Claims parseJWT(String key,String jwt) throws Exception{
		Claims claims = Jwts.parser()         
		   .setSigningKey(key)
		   .parseClaimsJws(jwt).getBody();
		return claims;
	}
	
	/**
	 * 从jwt中获取账号id
	 * @param claims
	 * @return
	 * @throws Exception
	 */
	public static Integer getUserId(String key,String jwt) throws Exception{
		Claims claims=parseJWT(key,jwt);
		return getUserId(claims);
	}
	
	/**
	 * 从jwt中获取账号id
	 * @param claims
	 * @return
	 * @throws Exception
	 */
	public static Integer getUserId(Claims claims) throws Exception{
		if(claims==null){
			logger.error("claims为null");
			 throw new CommonException(ResponseCode.FAILED.getValue(),"token解析出的数据为空"); 
		}
		
		Object userIdObj= claims.get(JwtUtil.JWT_CLAIM_ACCOUNT_ID);
		Integer currentUserId=null;
		if(userIdObj==null){
			throw new CommonException(ResponseCode.FAILED.getValue(),"访问令牌中用户id不存在"); 
		}
		
		if(userIdObj instanceof Integer){
			 currentUserId=((Integer) userIdObj);
		 }else if(userIdObj instanceof Long){
			 currentUserId=((Integer) userIdObj);
		 }else if(userIdObj instanceof String){
			 try {
				currentUserId=Integer.valueOf((String)userIdObj);
			} catch (Exception e) {
				logger.error("userId={}不是有效的数字类型.",(String)userIdObj,e);
				 throw new CommonException(ResponseCode.FAILED.getValue(),"用户id数据类型不正确.");  
			}
		 }else{
			 throw new CommonException(ResponseCode.FAILED.getValue(),"用户id数据类型不正确.");  
		 }
		return currentUserId;
	}
	
	
	 public static void main(String[] args) throws Exception {
		/*WechatAccount u=new WechatAccount();
		u.setId(1);
		String token=JwtUtil.createJWT(u, JwtUtil.JWT_SECRET, 0);
		 System.out.println(JwtUtil.createJWT(u, JwtUtil.JWT_SECRET, 0));
		Claims c= JwtUtil.parseJWT(JwtUtil.JWT_SECRET, token);
		 System.out.println(c.get(JwtUtil.JWT_CLAIM_ACCOUNT_ID));*/
		 System.out.println(JwtUtil.getUserId(JwtUtil.JWT_SECRET, "eyJhbGciOiJIUzI1NiJ9.eyJpYXQiOjE1Mjc4MTg3MTIsImlkIjo0LCJ0YXR0b29qdSI6Im5MREhveSIsInN1YiI6IjEifQ.m4guApG1hX2b2DNvQ-eUnys9KwNbB9AfQ2vw___v2gs"));
	}
	
}
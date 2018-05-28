package com.tattooju.business;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.apache.http.HttpException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import com.alibaba.fastjson.JSON;
import com.tattooju.config.Constant;
import com.tattooju.config.ResponseCode;
import com.tattooju.config.TattoojuProperties;
import com.tattooju.dto.WechatAccountDto;
import com.tattooju.entity.WechatAccount;
import com.tattooju.exception.CommonException;
import com.tattooju.service.WechatAccountService;
import com.tattooju.status.AccountRoleEnum;
import com.tattooju.util.HttpResponseContent;
import com.tattooju.util.HttpUtil;
import com.tattooju.util.JwtUtil;

import tk.mybatis.mapper.entity.Example;

/**
 * 
 * @author xusihan
 * @date 2018-05-21
 *
 */
@Service
public class WechatBusiness {

	Logger logger = LoggerFactory.getLogger(getClass());
	
	@Autowired
	TattoojuProperties properties;
	
	@Autowired
	StringRedisTemplate stringRedisTemplate;
	
	@Autowired
	WechatAccountService wechatAccountService;
	
	@Transactional(rollbackFor=CommonException.class)
	public WechatAccountDto bindWechat(String code) throws Exception {
		// 获取openId
		String accessTokenOpenIdString = getOpenId(code);
		Map<String, Object> dataMap = parseOpenIdAndAccessToken(accessTokenOpenIdString);
		String openId = (String) dataMap.get("openid");
		// 查询openID是否绑定用户
		Example wechatAccountExample = new Example(WechatAccount.class);
		wechatAccountExample
			.createCriteria()
			.andEqualTo("openId", openId);
		List<WechatAccount> wechatAccounts = wechatAccountService.selectByExample(wechatAccountExample);
		String wechatRespContent = getUserInfo(accessTokenOpenIdString);
		@SuppressWarnings("unchecked")
		Map<String, Object> weChatUserInfoMap = JSON.parseObject(wechatRespContent, Map.class);
		WechatAccountDto dto = new WechatAccountDto();
		dto.setHeadimgurl((String)weChatUserInfoMap.get("headimgurl"));
		dto.setOpenId(openId);
		dto.setUserName((String) weChatUserInfoMap.get("nickname"));
		if (CollectionUtils.isEmpty(wechatAccounts)) { // 空的话 绑定一个
			WechatAccount wechatAccount = new WechatAccount();
			wechatAccount.setOpenId(openId);
			wechatAccount.setRole(AccountRoleEnum.ACCOUNT.value());
			int row = wechatAccountService.saveNotNull(wechatAccount);
			if (row < 1) {
				throw new CommonException(ResponseCode.FAILED);
			}
			// 查找最后的插入的ID
			int id = wechatAccountService.getLastId();
			// 生成token
			wechatAccount.setId(id);
			//获取token
			String token = JwtUtil.createJWT(wechatAccount, JwtUtil.JWT_SECRET, 0);
			String key = Constant.PREFIX_ACCOUNT_TOKEN + id;
			stringRedisTemplate.opsForValue().set(key, token, properties.getTokenVerifyTTL(), TimeUnit.MINUTES);
			dto.setToken(token);
		}else {// 不为空
			WechatAccount account = wechatAccounts.get(0);
			String key = Constant.PREFIX_ACCOUNT_TOKEN + account.getId();
			String cachedToken = stringRedisTemplate.opsForValue().get(key);
			if (StringUtils.isEmpty(cachedToken)) {
				stringRedisTemplate.opsForValue().set(key, cachedToken, properties.getTokenVerifyTTL(), TimeUnit.MINUTES);
				dto.setToken(cachedToken);
			}else {
				stringRedisTemplate.expire(key, properties.getTokenVerifyTTL(), TimeUnit.MINUTES);
				dto.setToken(cachedToken);
			}
		}
		return dto;
	}
	
	/**
	 * 根据前端传过来的code，走微信的oauth2的流程，获取access_token和openId，<br>
	 * 校验openId是否已绑定过，已绑定则任务登录成功返回访问凭证,<br>
	 * 否则再根据access_token和openId获取用户基本信息 <br>
	 * @Author xusihan
	 * @date 2018-05-21
	 * @param authCode
	 *            授权码
	 * @param isDefault
	 *            是否默认授权，是则只返回微信默认授权的内容，只包含openId.<br>
	 * 			非默认授权,则返回用户包含openid的详细信息
	 * @return 微信服务器返回的原始用户信息
	 */
	public String getWeChatUsernfo(String authCode, boolean isDefault) throws Exception {
		String result = null;
		String appId = properties.getWechatAppId();
		String appSecret = properties.getWechatAppSecret();
		String getAccessTokenUrl = 
				"https://api.weixin.qq.com/sns/oauth2/access_token?appid="+appId+"&secret="+appSecret+"&code="+authCode+"&grant_type=authorization_code";
//		HttpResponseContent responseContent = null;
//
//		Map<String, Object> accessTokenMap = null;
		try {
			// responseContent = HttpUtil.getUrlRespContent(getAccessTokenUrl);
			// result = responseContent.getContent();
			result = getOpenId(authCode);
			// 如正确返回，则返回的数据格式如下:
			/*
			 * { "access_token":"ACCESS_TOKEN", "expires_in":7200,
			 * "refresh_token":"REFRESH_TOKEN", "openid":"OPENID", "scope":"SCOPE",
			 * "unionid": "o6_bmasdasdsad6_2sgVt7hMZOPfL" }
			 */
			// accessTokenMap = JSON.parseObject(result, Map.class);
//			if (result == null) {
//				throw new Exception("获取accessToken时微信服务器无响应,请重新进入!");
//			}
//
//			if (result.contains("errcode")) {
//				logger.error("获取accessToken时从微信服务器返回的响应信息中有错误,{}", result);
//				throw new Exception("获取accessToken时微信服务器返回错误信息:" + result);
//			}
//
//			if (!result.contains("openid")) {
//				logger.error("获取accessToken时从微信服务器返回的响应信息中获取不到openId,{}", result);
//				throw new Exception("获取accessToken时,从微信服务器返回的响应信息中获取不到openId:" + result);
//			}

			if (!isDefault) {
				// 非静默授权，则通过accesToken获取用户的微信信息
				// accessTokenMap=JSON.parseObject(result, Map.class);
				// logger.debug("根据授权码获取到的accessToken信息:{}", accessTokenMap);
				// String userInfoUrl = WECHAT_USERINFO_URL.replace("{ACCESS_TOKEN}",
				// (String) accessTokenMap.get("access_token"));
				// userInfoUrl = userInfoUrl.replace("{OPENID}",
				// (String)accessTokenMap.get("openid"));

				// 微信服务器返回的网页应用用户信息格式如下:
				// {
				// "openid":" OPENID",
				// " nickname": NICKNAME,
				// "sex":"1",
				// "province":"PROVINCE"
				// "city":"CITY",
				// "country":"COUNTRY",
				// "headimgurl":
				// "http://wx.qlogo.cn/mmopen/g3MonUZtNHkdmzicIlibx6iaFqAc56vxLSUfpb6n5WKSYVY0ChQKkiaJSgQ1dZuTOgvLLrhJbERQQ4eMsv84eavHiaiceqxibJxCfHe/46",
				// "privilege":[
				// "PRIVILEGE1"
				// "PRIVILEGE2"
				// ],
				// "unionid": "o6_bmasdasdsad6_2sgVt7hMZOPfL"
				// }

				// try {
				// responseContent = HttpUtil.getUrlRespContent(userInfoUrl);
				// result = responseContent.getContent();
				//
				// if (result == null) {
				// throw new Exception("获取微信用户的信息时微信服务器无响应,请重新进入!");
				// }
				//
				// if (result.contains("errcode")) {
				// logger.error("获取微信用户的信息时从微信服务器返回的响应信息中获取不到openId,{}",result);
				// throw new Exception("获取微信用户的信息时微信服务器返回错误信息:"+result);
				// }
				//
				// if (!result.contains("openid")) {
				// logger.error("获取微信用户的信息时从微信服务器返回的响应信息中获取不到openId");
				// throw new Exception("获取微信用户的信息时,从微信服务器返回的响应信息中获取不到openId:"+result);
				// }
				//
				// } catch (HttpException | IOException e) {
				// logger.error("获取微信用户的信息失败.", e);
				// throw new Exception("获取微信返回用户信息失败.",e);
				// }
				result = getUserInfo(result);
			}

		} catch (Exception e) {
			logger.error("根据授权码获取微信用户的信息失败.", e);
			throw e;
		}

		return result;
	}

	/**
	 * 根据前端传过来的code，走微信的oauth2的流程，获取access_token和openId<br>
	 *
	 * @Author xusihan
	 * @date 2018-05-21
	 * @param authCode
	 *            微信授权码
	 * @return 微信服务器返回的原始包含openId和accessToken的信息
	 * 
	 */
	public String getOpenId(String authCode) throws Exception {

		String result = null;
		
		String appId = properties.getWechatAppId();
		String appSecret = properties.getWechatAppSecret();
		String getAccessTokenUrl = 
				"https://api.weixin.qq.com/sns/oauth2/access_token?appid="+appId+"&secret="+appSecret+"&code="+authCode+"&grant_type=authorization_code";
//		
		HttpResponseContent responseContent = null;

		try {
			responseContent = HttpUtil.getUrlRespContent(getAccessTokenUrl);
			result = responseContent.getContent();
			// 如正确返回，则返回的数据格式如下:
			/*
			 * { "access_token":"ACCESS_TOKEN", "expires_in":7200,
			 * "refresh_token":"REFRESH_TOKEN", "openid":"OPENID", "scope":"SCOPE",
			 * "unionid": "o6_bmasdasdsad6_2sgVt7hMZOPfL" }
			 */
			// accessTokenMap = JSON.parseObject(result, Map.class);
			if (result == null) {
				throw new Exception("获取accessToken时微信服务器无响应,请重新进入!");
			}

			if (result.contains("errcode")) {
				logger.error("获取accessToken时从微信服务器返回的响应信息中有错误,{}", result);
				throw new Exception("获取accessToken时微信服务器返回错误信息:" + result);
			}

			if (!result.contains("openid")) {
				logger.error("获取accessToken时从微信服务器返回的响应信息中获取不到openId,{}", result);
				throw new Exception("获取accessToken时,从微信服务器返回的响应信息中获取不到openId:" + result);
			}

		} catch (HttpException | IOException e) {
			logger.error("根据授权码获取accessToken失败.", e);
			throw new Exception("获取微信访问令牌失败,", e);
		}

		return result;
	}

	/**
	 * 
	 * @Description 解析getOpenId(authCode)返回的字符串，获取openId和accessToken
	 * @param wxAccessTokenString
	 *            getOpenId(authCode)中返回的包含openId和accessToken的原始字符串
	 * @Author xusihan
	 * @date 2018-05-21
	 * @return 返回包含open_id和access_token的map
	 */
	public Map<String, Object> parseOpenIdAndAccessToken(String wxAccessTokenString) throws Exception {

		if (wxAccessTokenString == null) {
			throw new Exception("获取accessToken时微信服务器无响应,请重新进入!");
		}

		if (wxAccessTokenString.contains("errcode")) {
			logger.error("获取accessToken时从微信服务器返回的响应信息中有错误,{}", wxAccessTokenString);
			throw new Exception("获取accessToken时微信服务器返回错误信息:" + wxAccessTokenString);
		}

		if (!wxAccessTokenString.contains("openid")) {
			logger.error("获取accessToken时从微信服务器返回的响应信息中获取不到openId,{}", wxAccessTokenString);
			throw new Exception("获取accessToken时,从微信服务器返回的响应信息中获取不到openId:" + wxAccessTokenString);
		}

		Map<String, Object> accessTokenMap = JSON.parseObject(wxAccessTokenString, Map.class);
		logger.debug("根据授权码获取到的accessToken信息:{}", accessTokenMap);
		return accessTokenMap;
	}

	/**
	 * 
	 * @Description 根据getOpenId(authCode)返回的包含openId和accessToken信息的响应获取用户的微信信息。<br>
	 * 				注:非静默授权下返回的openId和accessToken才能获取
	 * @Author xusihan
	 * @date 2018-05-21
	 * @return
	 */
	public String getUserInfo(String wxAccessTokenString) throws Exception {

		if (wxAccessTokenString == null) {
			throw new Exception("获取accessToken时微信服务器无响应,请重新进入!");
		}

		if (wxAccessTokenString.contains("errcode")) {
			logger.error("获取accessToken时从微信服务器返回的响应信息中有错误,{}", wxAccessTokenString);
			throw new Exception("获取accessToken时微信服务器返回错误信息:" + wxAccessTokenString);
		}

		if (!wxAccessTokenString.contains("openid")) {
			logger.error("获取accessToken时从微信服务器返回的响应信息中获取不到openId,{}", wxAccessTokenString);
			throw new Exception("获取accessToken时,从微信服务器返回的响应信息中获取不到openId:" + wxAccessTokenString);
		}

		String result = null;
		// 非静默授权，则通过accesToken获取用户的微信信息
		Map<String, Object> accessTokenMap = parseOpenIdAndAccessToken(wxAccessTokenString);
		logger.debug("根据授权码获取到的accessToken信息:{}", accessTokenMap);
		String userInfoUrl = "https://api.weixin.qq.com/sns/userinfo?access_token="+accessTokenMap.get("access_token")+"&openid="+accessTokenMap.get("openid")+"&lang=zh_CN";
		// 微信服务器返回的网页应用用户信息格式如下:
		// {
		// "openid":" OPENID",
		// " nickname": NICKNAME,
		// "sex":"1",
		// "province":"PROVINCE"
		// "city":"CITY",
		// "country":"COUNTRY",
		// "headimgurl":
		// "http://wx.qlogo.cn/mmopen/g3MonUZtNHkdmzicIlibx6iaFqAc56vxLSUfpb6n5WKSYVY0ChQKkiaJSgQ1dZuTOgvLLrhJbERQQ4eMsv84eavHiaiceqxibJxCfHe/46",
		// "privilege":[
		// "PRIVILEGE1"
		// "PRIVILEGE2"
		// ],
		// "unionid": "o6_bmasdasdsad6_2sgVt7hMZOPfL"
		// }
		try {
			HttpResponseContent responseContent = HttpUtil.getUrlRespContent(userInfoUrl);
			result = responseContent.getContent();

			if (result == null) {
				throw new Exception("获取微信用户的信息时微信服务器无响应,请重新进入!");
			}

			if (result.contains("errcode")) {
				logger.error("获取微信用户的信息时从微信服务器返回的响应信息中获取不到openid,{}", result);
				throw new Exception("获取微信用户的信息时微信服务器返回错误信息:" + result);
			}

			if (!result.contains("openid")) {
				logger.error("获取微信用户的信息时从微信服务器返回的响应信息中获取不到openId");
				throw new Exception("获取微信用户的信息时,从微信服务器返回的响应信息中获取不到openid:" + result);
			}

		} catch (HttpException | IOException e) {
			logger.error("获取微信用户的信息失败.", e);
			throw new Exception("获取微信返回用户信息失败.", e);
		}
		return result;

	}
	
}

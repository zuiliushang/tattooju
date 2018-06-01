package com.tattooju.test;

import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import com.tattooju.TattoojuApp;
import com.tattooju.config.Constant;
import com.tattooju.config.ResponseCode;
import com.tattooju.config.TattoojuProperties;
import com.tattooju.entity.WechatAccount;
import com.tattooju.exception.CommonException;
import com.tattooju.service.WechatAccountService;
import com.tattooju.util.JwtUtil;

import tk.mybatis.mapper.entity.Example;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes=TattoojuApp.class)
public class AccountTest {

	@Autowired
	WechatAccountService wechatAccountService;
	@Autowired
	TattoojuProperties properties;
	@Autowired
	StringRedisTemplate stringRedisTemplate;
	@Test
	public void testBind() throws CommonException {
		String openId = "sdfasfd22s111";
		Example wechatAccountExample = new Example(WechatAccount.class);
		wechatAccountExample
			.createCriteria()
			.andEqualTo("openId", openId);
		List<WechatAccount> wechatAccounts = wechatAccountService.selectByExample(wechatAccountExample);
		if (CollectionUtils.isEmpty(wechatAccounts)) { // 空的话 绑定一个
			WechatAccount wechatAccount = new WechatAccount();
			wechatAccount.setHeadImgUrl("1.png");
			wechatAccount.setNickname("测试");
			wechatAccount.setOpenId("sdfasfd22s111");
			wechatAccount.setRole((byte) 1);
			wechatAccount.setSex((byte) 1);
			int row = wechatAccountService.saveNotNull(wechatAccount);
			if (row < 1) {
				throw new CommonException(ResponseCode.FAILED);
			}
			// 查找最后的插入的ID
			int id = wechatAccountService.getLastId();
			String token = JwtUtil.createJWT(wechatAccount, JwtUtil.JWT_SECRET, 0);
			String key = Constant.PREFIX_ACCOUNT_TOKEN + id;
			stringRedisTemplate.opsForValue().set(key, token, properties.getTokenVerifyTTL(), TimeUnit.MINUTES);
			System.out.println(222);
		}else {// 不为空
			WechatAccount account = wechatAccounts.get(0);
			wechatAccountService.updateNotNull(account);//获取更新
			String key = Constant.PREFIX_ACCOUNT_TOKEN + account.getId();
			String cachedToken = stringRedisTemplate.opsForValue().get(key);
			if (StringUtils.isEmpty(cachedToken)) {
				cachedToken = JwtUtil.createJWT(account, JwtUtil.JWT_SECRET, 0);
				stringRedisTemplate.opsForValue().set(key, cachedToken, properties.getTokenVerifyTTL(), TimeUnit.MINUTES);
			}else {
				stringRedisTemplate.expire(key, properties.getTokenVerifyTTL(), TimeUnit.MINUTES);
			}
			System.out.println(111);
		}
	}
	
}

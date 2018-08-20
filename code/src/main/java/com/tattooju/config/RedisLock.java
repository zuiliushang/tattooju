package com.tattooju.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.util.StringUtils;

import com.tattooju.util.SpringContextUtil;

public class RedisLock {
	
	
	private static Logger logger = LoggerFactory.getLogger(RedisLock.class);
	
	/***
     * 加锁
     * @param key
     * @param value 当前时间+超时时间
     * @return 锁住返回true
     */
    public static boolean lock(String key,String value){
    	StringRedisTemplate template = SpringContextUtil.getBean(StringRedisTemplate.class);
        if(template.opsForValue().setIfAbsent(key,value)){//setNX 返回boolean
            return true;
        }
        //如果锁超时 ***
        String currentValue = template.opsForValue().get(key);
        if(!StringUtils.isEmpty(currentValue) && Long.parseLong(currentValue)<System.currentTimeMillis()){
            //获取上一个锁的时间
            String oldvalue  = template.opsForValue().getAndSet(key,value);
            if(!StringUtils.isEmpty(oldvalue)&&oldvalue.equals(currentValue)){
                return true;
            }
        }
        return false;
    }
    /***
     * 解锁
     * @param key
     * @param value
     * @return
     */
    public static void unlock(String key,String value){
    	StringRedisTemplate template = SpringContextUtil.getBean(StringRedisTemplate.class);
        try {
            String currentValue = template.opsForValue().get(key);//获取值是日期
            if(!StringUtils.isEmpty(currentValue)&&currentValue.equals(value)){
                template.opsForValue().getOperations().delete(key);
            }
        } catch (Exception e) {
            logger.error("解锁异常");
        }
    }
	
	
}

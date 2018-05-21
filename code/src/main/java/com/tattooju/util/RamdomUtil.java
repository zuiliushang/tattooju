package com.tattooju.util;

import java.util.concurrent.ThreadLocalRandom;

public class RamdomUtil {
	/**
	 * 
	 * @Description 产生任意长度的随机字符串
	 * @Author xusihan
	 * @date 2018-05-21
	 * @param length
	 *            长度
	 * @return 指定长度的随机字符串
	 */
	public static String generateRandomString(int length) {
		String chars = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
		StringBuffer res = new StringBuffer(length);
		ThreadLocalRandom rnd = ThreadLocalRandom.current();
		for (int i = 0; i < length; i++) {
			res.append(chars.charAt(rnd.nextInt(chars.length())));
		}
		return res.toString();
	}

}

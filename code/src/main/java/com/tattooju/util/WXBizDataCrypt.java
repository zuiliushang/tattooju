package com.tattooju.util;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import com.alibaba.fastjson.JSONObject;

import sun.misc.BASE64Decoder;

public class WXBizDataCrypt {

	public static String decrypt(String encryptedData, String sessionKey, String iv, String encodingFormat) throws Exception {
		try {
			System.out.println("encrydata=" + encryptedData + "    sesskey=" + sessionKey + "   iv=" + iv);
			Cipher cipher = Cipher.getInstance("AES/CBC/NoPadding");
			BASE64Decoder base64Decoder = new BASE64Decoder();
			byte[] _encryptedData = base64Decoder.decodeBuffer(encryptedData);
			byte[] _sessionKey = base64Decoder.decodeBuffer(sessionKey);
			byte[] _iv = base64Decoder.decodeBuffer(iv);
			SecretKeySpec secretKeySpec = new SecretKeySpec(_sessionKey, "AES");
			IvParameterSpec ivParameterSpec = new IvParameterSpec(_iv);
			cipher.init(Cipher.DECRYPT_MODE, secretKeySpec, ivParameterSpec);
			byte[] original = cipher.doFinal(_encryptedData);
			byte[] bytes = PKCS7Encoder.decode(original);
			String originalString = new String(bytes, "UTF-8");
			return originalString;
		} catch (Exception ex) {
			return null;
		}
	}

	public static void main(String[] args) throws Exception {
		// ��Ҫ���ܵ��ִ�
		String appId = "wxec269fa0eec3fa83";
		String appSecret = "d61c8f9057a8308ce6d6f57ca69c90e8";

		String jsCode = "013AEpmK0Gqnu32JflpK05uumK0AEpmn";

		//JSONObject aString = {"session_key":"zcBBxE7o0h4pX4yniENWug==","openid":"o3fr40MRMG3161w5zUEOR8XNP6Cg"};

		//if (aString != null)
		//	System.out.println("astring=" + aString.getString("session_key"));

		String sessionKey = "zcBBxE7o0h4pX4yniENWug==";//aString.getString("session_key");

		String encryptedData = "ER8YhVBR6iWiUtLgl2ig37iF09mEgbvrz/+my4o41n3UTK52VOgZNkCzdehXbPhAiiidzK3a/+2Sbtz81xqzrmqNfFkLQFNrOEeefarwhhza2cJJCkD06RTp5biuB5PptwMMid1De5dImfCDd8jOvUIoP5RvQ0RFTkSwU9+LI380+1r7Gy5PE0CNJ2oaol05t3uFmlHCKrmcP9o6IOib0Ys0XyTY7cyOgt/dGAsebb+DmMLM5vVg56RoeCoOLXIH1KlTngYPeufLh8kXEzFvAfwxYl30LGdpyiaW8qqNAloYbiFpBO/QeuLFrIJD/jbPLQgQaH38wNxsmywNWOKYJph6mqtYqIG+OPPxacrpkBmAoNRaM1rxz2j08T+5kAiTjrRSx0GZx2SBZyuR5S92LtP5ejlfe/OMowFcejKsNHI5A1/1UJvdb2OSLcJswB09qHufg7qtz0uraBHxMMog2K5Rj30c+GGwSEReX4/cJAY=";

		String iv = "USqWyz/bFKy3NVuZvu5Nvg==";

		String deString = WXBizDataCrypt.decrypt(encryptedData, sessionKey, iv, "utf-8");
		System.out.println(deString);
		JSONObject jsonObject = JSONObject.parseObject(deString);
		System.out.println(jsonObject);
	}
}

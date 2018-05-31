package com.tattooju.util;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLEncoder;
import java.security.SignatureException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.SimpleTimeZone;
import java.util.UUID;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import org.apache.http.HttpException;

public class SignatureUtils {

	private final static String VOD_DOMAIN = "http://vod.cn-shanghai.aliyuncs.com";
	private final static String HTTP_METHOD = "GET";
	private final static String access_key_secret = "";
    private final static String HMAC_SHA1_ALGORITHM = "HmacSHA1";
    private final static String UTF_8 = "utf-8";
    private final static String ISO8601_DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ss'Z'";
    private final static String access_key_id = "";
	//对所有参数名称和参数值做URL编码
    private static List<String> getAllParams(Map<String, String> publicParams, Map<String, String> privateParams) {
        List<String> encodeParams = new ArrayList<String>();
        if (publicParams != null) {
            for (String key : publicParams.keySet()) {
                String value = publicParams.get(key);
                //将参数和值都urlEncode一下。
                String encodeKey = percentEncode(key);
                String encodeVal = percentEncode(value);
                encodeParams.add(encodeKey + "=" + encodeVal);
            }
        }
        if (privateParams != null) {
            for (String key : privateParams.keySet()) {
                String value = privateParams.get(key);
                //将参数和值都urlEncode一下。
                String encodeKey = percentEncode(key);
                String encodeVal = percentEncode(value);
                encodeParams.add(encodeKey + "=" + encodeVal);
            }
        }
        return encodeParams;
    }
	//获取 CanonicalizedQueryString
    private static String getCQS(List<String> allParams) {
        ParamsComparator paramsComparator = new ParamsComparator();
        Collections.sort(allParams, paramsComparator);
        String cqString = "";
        for (int i = 0; i < allParams.size(); i++) {
            cqString += allParams.get(i);
            if (i != allParams.size() - 1) {
                cqString += "&";
            }
        }

        return cqString;
    }
    private static class ParamsComparator implements Comparator<String> {
        @Override
        public int compare(String lhs, String rhs) {
            return lhs.compareTo(rhs);
        }
    }
	
	//构造待签名的字符串
	//String StringToSign = httpMethod + "&" + percentEncode("/") + "&" + percentEncode(CanonicalizedQueryString);
	//特殊字符替换为转义字符
	private static String percentEncode(String value) {
        try {
            String urlEncodeOrignStr = URLEncoder.encode(value, "UTF-8");
            String plusReplaced = urlEncodeOrignStr.replace("+", "%20");
            String starReplaced = plusReplaced.replace("*", "%2A");
            String waveReplaced = starReplaced.replace("%7E", "~");
            return waveReplaced;
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return value;
    }
	
    private static String hmacSHA1Signature(String accessKeySecret, String stringtoSign) {
        try {
            String key = accessKeySecret + "&";
            try {
                SecretKeySpec signKey = new SecretKeySpec(key.getBytes(), HMAC_SHA1_ALGORITHM);
                Mac mac = Mac.getInstance(HMAC_SHA1_ALGORITHM);
                mac.init(signKey);
                byte[] rawHmac = mac.doFinal(stringtoSign.getBytes());
                //按照Base64 编码规则把上面的 HMAC 值编码成字符串，即得到签名值（Signature）
                return new String(Base64.getEncoder().encode(rawHmac));
            } catch (Exception e) {
                throw new SignatureException("Failed to generate HMAC : " + e.getMessage());
            }
        } catch (SignatureException e) {
            e.printStackTrace();
        }
        return "";
    }
	
	public static String newStringByBase64(byte[] bytes)
	         throws UnsupportedEncodingException {
	    if (bytes == null || bytes.length == 0) {
	        return null;
	    }
	    return new String( Base64.getEncoder().encode(bytes));
	}
	
	public static String generateTimestamp() {
	    Date date = new Date(System.currentTimeMillis());
	    SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
	    df.setTimeZone(new SimpleTimeZone(0, "GMT"));
	    return df.format(date);
	}
	
	private static String generateRandom() {
        String signatureNonce = UUID.randomUUID().toString();
        return signatureNonce;
    }
	
	
    private static String generateOpenAPIURL(Map<String, String> publicParams, Map<String, String> privateParams) {
        return generateURL(VOD_DOMAIN, HTTP_METHOD, publicParams, privateParams);
    }

    
    
    /**
     * @param domain        请求地址
     * @param httpMethod    HTTP请求方式GET，POST等
     * @param publicParams  公共参数
     * @param privateParams 接口的私有参数
     * @return 最后的url
     */
    private static String generateURL(String domain, String httpMethod, Map<String, String> publicParams, Map<String, String> privateParams) {
        List<String> allEncodeParams = getAllParams(publicParams, privateParams);
        String cqsString = getCQS(allEncodeParams);
        System.out.println("CanonicalizedQueryString = " + cqsString);
        String stringToSign = httpMethod + "&" + percentEncode("/") + "&" + percentEncode(cqsString);
        System.out.println("StringtoSign = " + stringToSign);
        String signature = hmacSHA1Signature(access_key_secret, stringToSign);
        System.out.println("Signature = " + signature);
        return domain + "?" + cqsString + "&" + percentEncode("Signature") + "=" + percentEncode(signature);
    }
	
    /**
     * 生成视频点播OpenAPI私有参数
     * 不同API需要修改此方法中的参数
     *
     * @return
     */
    private static Map<String, String> generatePrivateParamters() {
        // 接口私有参数列表, 不同API请替换相应参数
        Map<String, String> privateParams = new HashMap<>();
        // 视频ID
        privateParams.put("VideoId", "2e43d795e06b4a669c9a99c52a0e7385");
        //privateParams.put("Title", "测试视频Title");
        //privateParams.put("FileName", "测试视屏名字.mp4");
        // API名称
        privateParams.put("Action", "GetVideoPlayAuth");
        //privateParams.put("Action", "CreateUploadVideo");
        return privateParams;
    }

    /**
     * 生成视频点播OpenAPI公共参数
     * 不需要修改
     *
     * @return
     */
    private static Map<String, String> generatePublicParamters() {
        Map<String, String> publicParams = new HashMap<>();
        publicParams.put("Format", "JSON");
        publicParams.put("Version", "2017-03-21");
        publicParams.put("AccessKeyId", access_key_id);
        publicParams.put("SignatureMethod", "HMAC-SHA1");
        publicParams.put("Timestamp", generateTimestamp());
        publicParams.put("SignatureVersion", "1.0");
        publicParams.put("SignatureNonce", generateRandom());
        /*if (security_token != null && security_token.length() > 0) {
            publicParams.put("SecurityToken", security_token);
        }*/
        return publicParams;
    }
    private static String httpGet(String url) throws IOException {
    	/*
         * Read and covert a inputStream to a String.
         * Referred this:
         * http://stackoverflow.com/questions/309424/read-convert-an-inputstream-to-a-string
         */
        System.out.println("URL = " +  url);
        @SuppressWarnings("resource")
        Scanner s = new Scanner(new URL(url).openStream(), UTF_8).useDelimiter("\\A");
        try {
            String resposne = s.hasNext() ? s.next() : "true";
            System.out.println("Response = " + resposne);
            return resposne;
        } finally {
            s.close();
        }
    }
    
	public static void main(String[] args) throws HttpException, IOException {
		/*String timeStamp = generateTimestamp();
		String random = generateRandom();
		Map<String,String> publicParams = new HashMap<String, String>();
		Map<String,String> privateParams = new HashMap<String, String>();
		String reqUrl = "http://vod.cn-shanghai.aliyuncs.com?Format=JSON&Version=2017-03-21&AccessKeyId=LTAIKGcm5gyqIHSZ&SignatureMethod=HMAC-SHA1"+
		"&Timestamp="+timeStamp+"&SignatureVersion=1.0&SignatureNonce="+random+"&Action=CreateUploadVideo&Title=测试视频123&FileName=测试视频.mp4&Signature=";
		publicParams.put("Format", "JSON");
		publicParams.put("Version", "2017-03-21");
		publicParams.put("AccessKeyId", "LTAIKGcm5gyqIHSZ");
		publicParams.put("SignatureMethod", "HMAC-SHA1");
		publicParams.put("Timestamp", timeStamp);
		publicParams.put("SignatureVersion", "1.0");
		publicParams.put("SignatureNonce", random);
		privateParams.put("Action", "CreateUploadVideo");
		privateParams.put("Title", "测试视频123");
		privateParams.put("FileName", "测试视频.mp4");
		String CanonicalizedQueryString = getCQS(getAllParams(publicParams, privateParams));
		String StringToSign = "GET" + "&" + percentEncode("/") + "&" + percentEncode(CanonicalizedQueryString);
		System.out.println(StringToSign);
		String newString = newStringByBase64(sha1s);
		System.out.println(newString);
		System.out.println(reqUrl+newString);*/
		
		//生成私有参数，不同API需要修改
        Map<String, String> privateParams = generatePrivateParamters();
        //生成公共参数，不需要修改
        Map<String, String> publicParams = generatePublicParamters();
        //生成OpenAPI地址，不需要修改
        String URL = generateOpenAPIURL(publicParams, privateParams);
		/*System.out.println(URL);
		System.out.println(httpGet(URL));*/
        HttpResponseContent responseContent = HttpUtil.getUrlRespContent(URL, UTF_8);
        System.out.println(responseContent.getContent());
	}
}

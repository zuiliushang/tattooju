package com.tattooju.util;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.apache.http.HttpException;
import org.apache.http.entity.mime.content.ByteArrayBody;
import org.apache.http.entity.mime.content.FileBody;

import com.alibaba.fastjson.JSON;

/**
 *  HTTP工具类，封装HttpClient4.4.x来对外提供简化的HTTP请求
 * @author Lu Zuoqi
 * @since  2015年5月22日 下午4:10:28
 * 
 */

public class HttpUtil {
 
    private static Integer socketTimeout            = 1000;
    private static Integer connectTimeout           = 6000;
    private static Integer connectionRequestTimeout = 100;
 
    /**
     * 使用Get方式 根据URL地址，获取ResponseContent对象
     * 
     * @param url
     *            完整的URL地址
     * @return ResponseContent 如果发生异常则返回null，否则返回ResponseContent对象
     * @throws IOException 
     * @throws HttpException 
     */
    public static HttpResponseContent getUrlRespContent(String url) throws HttpException, IOException {
        HttpClientWrapper hw = new HttpClientWrapper(connectionRequestTimeout, connectTimeout, socketTimeout);
        HttpResponseContent response = null;       
        response = hw.getResponse(url);
        
        return response;
    }
 
    /**
     * 使用Get方式 根据URL地址，获取ResponseContent对象
     * 
     * @param url
     *            完整的URL地址
     * @param urlEncoding
     *            编码，可以为null
     * @return ResponseContent 如果发生异常则返回null，否则返回ResponseContent对象
     * @throws IOException 
     * @throws HttpException 
     */
    public static HttpResponseContent getUrlRespContent(String url, String urlEncoding) throws HttpException, IOException {
        HttpClientWrapper hw = new HttpClientWrapper(connectionRequestTimeout, connectTimeout, socketTimeout);
        HttpResponseContent response = null;
        response = hw.getResponse(url, urlEncoding);
      
        return response;
    }
 
    /**
     * 将参数拼装在url中，进行post请求。
     * 
     * @param url
     * @return
     * @throws IOException 
     * @throws HttpException 
     */
    public static HttpResponseContent postUrl(String url) throws HttpException, IOException {
        //HttpClientWrapper hw = new HttpClientWrapper();
    	 HttpClientWrapper hw = new HttpClientWrapper(connectionRequestTimeout, connectTimeout, socketTimeout);
        HttpResponseContent ret = null;
        setParams(url, hw);
        ret = hw.postNV(url);
        
        return ret;
    }
 
    private static void setParams(String url, HttpClientWrapper hw) {
        String[] paramStr = url.split("[?]", 2);
        if (paramStr == null || paramStr.length != 2) {
            return;
        }
        String[] paramArray = paramStr[1].split("[&]");
        if (paramArray == null) {
            return;
        }
        for (String param : paramArray) {
            if (param == null || "".equals(param.trim())) {
                continue;
            }
            String[] keyValue = param.split("[=]", 2);
            if (keyValue == null || keyValue.length != 2) {
                continue;
            }
            hw.addNV(keyValue[0], keyValue[1]);
        }
    }
 
    
    
    
    /**
     * 发送post请求
     * 将参数放在请求体中,进行post请求。
     *  
     * @param url
     *            请求URL
     * @param paramsMap
     *            参数和值
     * @return
     * @throws IOException 
     * @throws HttpException 
     */
    public static HttpResponseContent postUrl(String url, Map<String, String> paramsMap) throws HttpException, IOException {
    	 HttpClientWrapper hw = new HttpClientWrapper(connectionRequestTimeout, connectTimeout, socketTimeout);
        HttpResponseContent ret = null;
        setParams(paramsMap, hw);
        ret = hw.postNV(url);
            //ret =postEntity(url,  "application/x-www-form-urlencoded; charset=utf-8");
       
        return ret;
    }
    
    private static void setParams( Map<String, String> paramsMap, HttpClientWrapper hw) {
    	
    	 Iterator<String> iterator = paramsMap.keySet().iterator();
        
         while (iterator.hasNext()) {
             String key = iterator.next();
             String value = paramsMap.get(key);            
            hw.addNV(key,value);
            
         }
    	 
    }
 
    
    /**
     * 上传文件（包括图片）
     * 
     * @param url
     *            请求URL
     * @param paramsMap
     *            参数和值
     * @return
     * @throws IOException 
     * @throws HttpException 
     */
    public static HttpResponseContent postEntity(String url, Map<String, Object> paramsMap) throws HttpException, IOException {
        HttpClientWrapper hw = new HttpClientWrapper();
        HttpResponseContent ret = null;
        
            setParams(url, hw);
            Iterator<String> iterator = paramsMap.keySet().iterator();
            while (iterator.hasNext()) {
                String key = iterator.next();
                Object value = paramsMap.get(key);
                if (value instanceof File) {
                    FileBody fileBody = new FileBody((File) value);
                    hw.getContentBodies().add(fileBody);
                } else if (value instanceof byte[]) {
                    byte[] byteVlue = (byte[]) value;
                    ByteArrayBody byteArrayBody = new ByteArrayBody(byteVlue, key);
                    hw.getContentBodies().add(byteArrayBody);
                } else {
                    if (value != null && !"".equals(value)) {
                        hw.addNV(key, String.valueOf(value));
                    } else {
                        hw.addNV(key, "");
                    }
                }
            }
          
            ret = hw.postEntity(url);
        
        return ret;
    }
 
    /**
     * 使用post方式，发布对象转成的json给Rest服务。
     * 
     * @param url
     * @param jsonBody
     * @return
     * @throws IOException 
     * @throws HttpException 
     */
    public static HttpResponseContent postJsonEntity(String url, String jsonBody) throws HttpException, IOException {
        return postEntity(url,null, jsonBody, "application/json");
    }
    
    /**
     * 使用post方式，发布对象转成的json给Rest服务。
     * 
     * @param url 请求URL
     * @param jsonBody json字符串
     * @param headerMap 请求头
     * @return
     * @throws IOException 
     * @throws HttpException 
     */
    public static HttpResponseContent postJsonEntity(String url,Map<String,String> headerMap,  String jsonBody) throws HttpException, IOException {
        return postEntity(url,headerMap, jsonBody, "application/json");
    }
   
    /**
     * 使用post方式，发布对象转成的xml给Rest服务
     * 
     * @param url
     *            URL地址
     * @param xmlBody
     *            xml文本字符串
     * @return ResponseContent 如果发生异常则返回空，否则返回ResponseContent对象
     * @throws IOException 
     * @throws HttpException 
     */
    public static HttpResponseContent postXmlEntity(String url, String xmlBody) throws HttpException, IOException {
        return postEntity(url, null,xmlBody, "application/xml");
    }
 
    private static HttpResponseContent postEntity(String url,Map<String,String> headerMap, String body, String contentType) throws HttpException, IOException {
        HttpClientWrapper hw = new HttpClientWrapper();
        HttpResponseContent ret = null;
//        hw.addNV("body", body);
//        ret = hw.postNV(url, contentType);
       
        ret=hw.postStringEntity(url, "UTF-8",headerMap, contentType, body);
        		
        return ret;
    }
    
    /**
     * 将entity以JSON字符串的方式用POST发送
     * @param url
     * @param t 要被序列化为json的java类对象
     * @return
     * @throws HttpException
     * @throws IOException
     */
    public static <T> HttpResponseContent postJsonEntity(String url, T t) throws HttpException, IOException {
        HttpClientWrapper hw = new HttpClientWrapper();
        HttpResponseContent ret =  hw.postStringEntity(url, "UTF-8",  "application/json", JSON.toJSONString(t));
        return ret;
    }
    
    /**
     * 将entity以JSON字符串的方式用POST发送
     * @param url 请求的url
     * @param headerMap 请求头
     * @param t 要被序列化为json的java类对象
     * @return
     * @throws HttpException
     * @throws IOException
     */
    public static <T> HttpResponseContent postJsonEntity(String url,Map<String,String> headerMap,  T t) throws HttpException, IOException {
        HttpClientWrapper hw = new HttpClientWrapper();
        HttpResponseContent ret =  hw.postStringEntity(url, "UTF-8", headerMap, "application/json", JSON.toJSONString(t));
        return ret;
    }
    
   /**
    * 将entity以JSON字符串的方式用POST发送
    * @param url url
    * @param urlEncoding 编码
    * @param t 要被序列化为json的java类对象
    * @return
    * @throws HttpException
    * @throws IOException
    */
    public static <T> HttpResponseContent postJsonEntity(String url,String urlEncoding, T t) throws HttpException, IOException {
        HttpClientWrapper hw = new HttpClientWrapper();
        HttpResponseContent ret =  hw.postStringEntity(url, urlEncoding,  "application/json", JSON.toJSONString(t));
        return ret;
    }
    
    /**
     * 将entity以JSON字符串的方式用POST发送
     * @param url url
     * @param urlEncoding 编码
     * @param headerMap 请求头键值对
     * @param t 要被序列化为json的java类对象
     * @return
     * @throws HttpException
     * @throws IOException
     */
     public static <T> HttpResponseContent postJsonEntity(String url,String urlEncoding,Map<String,String> headerMap, T t) throws HttpException, IOException {
         HttpClientWrapper hw = new HttpClientWrapper();
         HttpResponseContent ret =  hw.postStringEntity(url, urlEncoding,headerMap,  "application/json", JSON.toJSONString(t));
         return ret;
     }
    
    /**
     * POST方式发送JSON字符串
     * @param url
     * @param stringContent 要发送的JSON字符串内容
     * @param contentType 请求内容格式,请求头中的content-type
     * @return
     * @throws HttpException
     * @throws IOException
     */
    public static HttpResponseContent postJsonString(String url, String stringContent) throws HttpException, IOException {
        HttpClientWrapper hw = new HttpClientWrapper();
        HttpResponseContent ret =  hw.postStringEntity(url, "UTF-8",  "application/json", stringContent);
        return ret;
    }
    
    /**
     * POST方式发送字符串
     * @param url
     * @param stringContent 要发送的字符串内容
     * @param contentType 请求内容格式,请求头中的content-type
     * @return
     * @throws HttpException
     * @throws IOException
     */
    public static HttpResponseContent postString(String url, String contentType, String stringContent) throws HttpException, IOException {
        HttpClientWrapper hw = new HttpClientWrapper();
        HttpResponseContent ret =  hw.postStringEntity(url, "UTF-8", contentType, stringContent);
        return ret;
    }
    
    /**
     * POST方式发送字符串
     * @param url
     * @param urlEncoding 字符编码
     * @param stringContent 要发送的字符串内容
     * @param contentType 请求内容格式,请求头中的content-type
     * @return
     * @throws HttpException
     * @throws IOException
     */
    public static HttpResponseContent postString(String url,String urlEncoding,  String contentType,String stringContent) throws HttpException, IOException {
        HttpClientWrapper hw = new HttpClientWrapper();
        HttpResponseContent ret =  hw.postStringEntity(url, urlEncoding, contentType, stringContent);
        return ret;
    }
    
    /**
     * POST方式发送字符串以及请求头
     * @param url
     * @param headerMap 请求头键值对
     * @param stringContent  发送的JSON字符串内容
     * @param contentType 请求内容格式,请求头中的content-type
     * @return
     * @throws HttpException
     * @throws IOException
     */
    public static HttpResponseContent postJsonString(String url, Map<String,String> headerMap, String stringContent) throws HttpException, IOException {
        HttpClientWrapper hw = new HttpClientWrapper();
        HttpResponseContent ret =  hw.postStringEntity(url, "UTF-8",headerMap,  "application/json", stringContent);
        return ret;
    }
    
    /**
     * POST方式发送字符串以及请求头
     * @param url
     * @param headerMap 请求头键值对
     * @param stringContent 发送的字符串内容
     * @param contentType 请求内容格式,请求头中的content-type
     * @return
     * @throws HttpException
     * @throws IOException
     */
    public static HttpResponseContent postString(String url, String contentType,Map<String,String> headerMap, String stringContent) throws HttpException, IOException {
        HttpClientWrapper hw = new HttpClientWrapper();
        HttpResponseContent ret =  hw.postStringEntity(url, "UTF-8",headerMap, contentType, stringContent);
        return ret;
    }
    
    /**
     * POST方式发送字符串以及请求头
     * @param url
     * @param urlEncoding 编码
     * @param headerMap 请求头键值对
     * @param stringContent 要发送的字符串内容
     * @param contentType 请求头中的content-type
     * @return
     * @throws HttpException
     * @throws IOException
     */
    public static HttpResponseContent postString(String url,String urlEncoding,Map<String,String> headerMap,  String contentType,String stringContent) throws HttpException, IOException {
        HttpClientWrapper hw = new HttpClientWrapper();
        HttpResponseContent ret =  hw.postStringEntity(url, urlEncoding,headerMap, contentType, stringContent);
        return ret;
    }
    
    
    public static void main(String[] args) {
        try {
			testGet();
		} catch (HttpException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        //testUploadFile();
    }
 
    //test
    public static void testGet() throws HttpException, IOException {
        String url = "http://crs.rich-healthcare.com:8068/WebToken/TokenCreater.asmx";
        HttpResponseContent responseContent = getUrlRespContent(url);
        try {
            System.out.println(responseContent.getContent());
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }
 
    //test
    public static void testUploadFile() {
        try {
            String url = "http://localhost:8280/jfly/action/admin/user/upload.do";
            Map<String, Object> paramsMap = new HashMap<String, Object>();
            paramsMap.put("userName", "jj");
            paramsMap.put("password", "jj");
            paramsMap.put("filePath", new File("C:\\Users\\yangjian1004\\Pictures\\default (1).jpeg"));
            HttpResponseContent ret = postEntity(url, paramsMap);
            System.out.println(ret.getContent());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
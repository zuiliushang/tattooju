package com.tattooju.ctrl;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Stream;

import org.csource.common.MyException;
import org.csource.fastdfs.StorageClient1;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.tattooju.entity.WechatAccount;
import com.tattooju.entity.WechatAccountMapper;
import com.tattooju.util.JwtUtil;

@RequestMapping("/sys/")
@RestController
public class SystemCtrl {

	@Autowired
	StorageClient1 storageClient;
	
	@Autowired
	WechatAccountMapper wechatAccountMapper;
	
	@RequestMapping("greeting")
	public String greeting() {
		return "hello world.";
	}
	
	@RequestMapping("upload")
	public String upload() throws IOException, MyException {
		String file = "E:\\test.jpg";
		byte[] bs = Files.readAllBytes(new File(file).toPath());
		String[] result;
		try {
			result = storageClient.upload_file(bs, "jpg", null);
		} catch (Exception e) {
			e.printStackTrace();
			result = storageClient.upload_file(bs, "jpg", null);
			return "aa";
			}
		Stream.of(result).forEach(System.out::println);
		return result[0];
	}
	
	/*@RequestMapping("token")
	public String getToken(int id) {
		WechatAccount wechatAccount = wechatAccountMapper.selectByPrimaryKey(id);
		return JwtUtil.createJWT(wechatAccount, JwtUtil.JWT_SECRET, 0);
	}*/
}

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

@RequestMapping("/sys/")
@RestController
public class SystemCtrl {

	@Autowired
	StorageClient1 storageClient;
	
	@RequestMapping("greeting")
	@ResponseBody
	public String greeting() {
		return "hello world.";
	}
	
	@RequestMapping("upload")
	@ResponseBody
	public String upload() throws IOException, MyException {
		String file = "E:\\test.png";
		byte[] bs = Files.readAllBytes(new File(file).toPath());
		String[] result = storageClient.upload_file(bs, "png", null);
		Stream.of(result).forEach(System.out::println);
		return result[0];
	}
	
}

package com.tattooju.ctrl;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.tattooju.business.MediaBusiness;
import com.tattooju.config.ResponseContent;
import com.tattooju.exception.CommonException;

@RestController
@RequestMapping("media")
public class MediaCtrl {
	
	@Autowired
	MediaBusiness mediaBusiness;
	
	@PostMapping("upload")
	@ResponseBody
	public ResponseContent mediaUpload(
			@RequestParam(value = "mediaType",required=true) int mediaType
			,@RequestParam(value = "file", required = true)MultipartFile file) throws CommonException, IOException {
		System.out.println(mediaType);
		String result = mediaBusiness.mediaUpload(file.getBytes(), file.getOriginalFilename(), mediaType);
		return ResponseContent.ok(result);
	}
	
}

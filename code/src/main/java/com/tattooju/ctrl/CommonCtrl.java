package com.tattooju.ctrl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.tattooju.business.MediaBusiness;
import com.tattooju.config.ResponseContent;
import com.tattooju.util.JwtUtil;

@RestController
@RequestMapping("common")
public class CommonCtrl {

	@Autowired
	MediaBusiness mediaBusiness;
	
	@PostMapping("upload")
	public ResponseContent mediaUpload(
			@RequestParam(value = "mediaType",required=true) int mediaType
			,@RequestParam(value = "file", required = true)MultipartFile file,
			@RequestHeader(value = "token", required = true)String token) throws Exception {
		int accountId = JwtUtil.getUserId(JwtUtil.JWT_SECRET,token);
		String result = mediaBusiness.mediaUpload(file.getBytes(), file.getOriginalFilename(), mediaType,accountId);
		return ResponseContent.ok(result);
	}
	
}

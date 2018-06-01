package com.tattooju.ctrl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.github.pagehelper.PageInfo;
import com.tattooju.business.MediaBusiness;
import com.tattooju.config.ResponseCode;
import com.tattooju.config.ResponseContent;
import com.tattooju.entity.Media;
import com.tattooju.exception.CommonException;
import com.tattooju.util.JwtUtil;

@RestController
@RequestMapping("media")
public class MediaCtrl {
	
	@Autowired
	MediaBusiness mediaBusiness;
	
	@PostMapping
	public ResponseContent addMedia(
			@RequestParam(required=true) String content,
			@RequestParam(required=true) String mediaPath,
			@RequestParam(required=true) String tagContent,
			@RequestParam(required=true) byte type,
			String token) throws Exception {
		if (StringUtils.isEmpty(token)) {
			throw new CommonException(ResponseCode.TOKEN_INVALID);
		}
		int accountId = JwtUtil.getUserId(JwtUtil.JWT_SECRET,token);
		mediaBusiness.addMedia(content, mediaPath, tagContent, type, accountId);
		return ResponseContent.ok(null);
	}
	
	@PutMapping
	public ResponseContent updateMedia(
			@RequestParam(required=true) int id,
			@RequestParam(required=true) String content,
			@RequestParam(required=true) String mediaPath,
			@RequestParam(required=true) String tagContent,
			@RequestParam(required=true) byte type,
			String token) throws Exception {
		if (StringUtils.isEmpty(token)) {
			throw new CommonException(ResponseCode.TOKEN_INVALID);
		}
		int accountId = JwtUtil.getUserId(JwtUtil.JWT_SECRET,token);
		mediaBusiness.updateMedia(id,content, mediaPath, tagContent, type, accountId);
		return ResponseContent.ok(null);
	}
	
	@GetMapping
	public ResponseContent getMediaById(
			@RequestParam(required=true) int id) {
		Media media = mediaBusiness.getMediaById(id);
		return ResponseContent.ok(media);
	}
	
	@GetMapping("list")
	public ResponseContent getMediaSearch(
			@RequestParam(defaultValue="1") int pageNum,
			@RequestParam(defaultValue="5") int pageSize,
			String keyword,
			String tag,
			Byte type) {
		PageInfo<Media> pageInfo = mediaBusiness.getMediaList(pageNum,pageSize,keyword,tag,type);
		return ResponseContent.ok(pageInfo);
	}
	
	@DeleteMapping
	public ResponseContent deleteMedia(@RequestParam(required=true) int id
			,String token) throws Exception {
		if (StringUtils.isEmpty(token)) {
			throw new CommonException(ResponseCode.TOKEN_INVALID);
		}
		int accountId = JwtUtil.getUserId(JwtUtil.JWT_SECRET,token);
		mediaBusiness.deleteMediaById(id,accountId);
		return ResponseContent.ok(null);
	}
	
}

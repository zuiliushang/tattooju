package com.tattooju.ctrl;

import java.util.List;

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
import com.tattooju.business.DraftBusiness;
import com.tattooju.config.ResponseCode;
import com.tattooju.config.ResponseContent;
import com.tattooju.entity.Draft;
import com.tattooju.exception.CommonException;
import com.tattooju.util.JwtUtil;

@RestController
@RequestMapping("draft")
public class DraftCtrl {
	
	@Autowired
	DraftBusiness draftBusiness;
	
	@PostMapping
	public ResponseContent addMedia(
			@RequestParam(required=true) String content,
			@RequestParam(required=true) String mediaPath,
			@RequestParam(required=true) String tagContent,
			@RequestParam(required=true) byte type,
			@RequestHeader()String token) throws Exception {
		if (StringUtils.isEmpty(token)) {
			throw new CommonException(ResponseCode.TOKEN_INVALID);
		}
		int accountId = JwtUtil.getUserId(JwtUtil.JWT_SECRET,token);
		draftBusiness.addMedia(content, mediaPath, tagContent, type, accountId);
		return ResponseContent.ok(null);
	}
	
	@PutMapping
	public ResponseContent updateMedia(
			@RequestParam(required=true) int id,
			@RequestParam(required=true) String content,
			@RequestParam(required=true) String mediaPath,
			@RequestParam(required=true) String tagContent,
			@RequestParam(required=true) byte type,
			@RequestHeader()String token) throws Exception {
		if (StringUtils.isEmpty(token)) {
			throw new CommonException(ResponseCode.TOKEN_INVALID);
		}
		int accountId = JwtUtil.getUserId(JwtUtil.JWT_SECRET,token);
		draftBusiness.updateMedia(id,content, mediaPath, tagContent, type, accountId);
		return ResponseContent.ok(null);
	}
	
	@GetMapping
	public ResponseContent getMediaById(
			@RequestParam(required=true) int id) {
		Draft draft = draftBusiness.getMediaById(id);
		return ResponseContent.ok(draft);
	}
	
	@GetMapping("list")
	public ResponseContent getMediaSearch(
			@RequestParam(defaultValue="1") int pageNum,
			@RequestParam(defaultValue="5") int pageSize,
			String keyword,
			String tag,
			Byte type) {
		PageInfo<Draft> pageInfo = draftBusiness.getMediaList(pageNum,pageSize,keyword,tag,type);
		return ResponseContent.ok(pageInfo);
	}
	
	@DeleteMapping
	public ResponseContent deleteMedia(@RequestParam(required=true) int id
			,@RequestHeader() String token) throws Exception {
		if (StringUtils.isEmpty(token)) {
			throw new CommonException(ResponseCode.TOKEN_INVALID);
		}
		int accountId = JwtUtil.getUserId(JwtUtil.JWT_SECRET,token);
		draftBusiness.deleteMediaById(id,accountId);
		return ResponseContent.ok(null);
	}
	
	@GetMapping("tag/list")
	public ResponseContent getTags() {
		List<String> tags = draftBusiness.getTags();
		return ResponseContent.ok(tags);
	}
	
}

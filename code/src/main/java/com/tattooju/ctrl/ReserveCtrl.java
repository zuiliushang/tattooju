package com.tattooju.ctrl;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.github.pagehelper.PageInfo;
import com.tattooju.business.ReserveBusiness;
import com.tattooju.config.ResponseCode;
import com.tattooju.config.ResponseContent;
import com.tattooju.dto.ReserveDto;
import com.tattooju.entity.Reserve;
import com.tattooju.exception.CommonException;
import com.tattooju.status.ReserveStatus;
import com.tattooju.util.JwtUtil;

@RequestMapping("reserve")
@RestController
public class ReserveCtrl {

	@Autowired
	ReserveBusiness reserveBusiness;
	
	@PostMapping
	public ResponseContent addReserve(@RequestParam(required=true)String wxAccount,
			@RequestParam(required=true) String mobile,
			@RequestParam(required=true) String body,
			@RequestParam(required=true) String content,
			@RequestParam(required=true) @DateTimeFormat(pattern="yyyy-MM-dd HH:mm") Date reserveTime,
			String token) throws Exception {
		if (StringUtils.isEmpty(token)) {
			throw new CommonException(ResponseCode.TOKEN_INVALID);
		}
		Integer accountId = JwtUtil.getUserId(JwtUtil.JWT_SECRET,token);
		Reserve reserve = new Reserve();
		reserve.setBody(body);
		reserve.setContent(content);
		reserve.setMobile(mobile);
		reserve.setReserveTime(reserveTime);
		reserve.setWxAccount(wxAccount);
		reserve.setAccountId(accountId);
		reserve.setStatus(ReserveStatus.RESERVED.value());
		reserveBusiness.addReserve(reserve);
		return ResponseContent.ok(null);
	}
	
	@PutMapping
	public ResponseContent updateReserve(
			@RequestParam(required=true) int id,
			@RequestParam(required=true)String wxAccount,
			@RequestParam(required=true) String mobile,
			@RequestParam(required=true) String body,
			@RequestParam(required=true) String content,
			@RequestParam(required=true) @DateTimeFormat(pattern="yyyy-MM-dd HH:mm") Date reserveTime,
			 String token) throws Exception {
		if (StringUtils.isEmpty(token)) {
			throw new CommonException(ResponseCode.TOKEN_INVALID);
		}
		Reserve reserve = new Reserve();
		reserve.setId(id);
		reserve.setBody(body);
		reserve.setContent(content);
		reserve.setMobile(mobile);
		reserve.setReserveTime(reserveTime);
		reserve.setWxAccount(wxAccount);
		reserve.setUpdateTime(new Date());
		Integer accountId = JwtUtil.getUserId(JwtUtil.JWT_SECRET,token);
		reserveBusiness.updateReserve(reserve,accountId);
		return ResponseContent.ok(null);
	}
	
	@PutMapping("status")
	public ResponseContent updateReserveStatus(
			@RequestParam(required=true) int id,
			@RequestParam(required=true) byte status,
			String token) throws Exception {
		if (StringUtils.isEmpty(token)) {
			throw new CommonException(ResponseCode.TOKEN_INVALID);
		}
		Integer accountId = JwtUtil.getUserId(JwtUtil.JWT_SECRET,token);
		reserveBusiness.updateReserveStatus(id,accountId,status);
		return ResponseContent.ok(null);
	}
	
	@GetMapping
	public ResponseContent getReserveById(@RequestParam(required=true) int id) {
		ReserveDto reserve = reserveBusiness.getReserveById(id);
		return ResponseContent.ok(reserve);
	}
	
	@GetMapping("list")
	public ResponseContent getReserveList(
			String token,
			@RequestParam(defaultValue="1") int pageNum,
			@RequestParam(defaultValue="5") int pageSize,
			@DateTimeFormat(pattern = "yyyy-MM-dd") Date date) throws Exception {
		if (StringUtils.isEmpty(token)) {
			throw new CommonException(ResponseCode.TOKEN_INVALID);
		}
		Integer accountId = JwtUtil.getUserId(JwtUtil.JWT_SECRET,token);
		PageInfo<ReserveDto> result = reserveBusiness.getReserveList(accountId, pageNum, pageSize, date);
		return ResponseContent.ok(result);
	}
	
}

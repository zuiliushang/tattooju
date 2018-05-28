package com.tattooju.ctrl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.tattooju.business.ReserveBusiness;
import com.tattooju.config.ResponseContent;
import com.tattooju.entity.Reserve;
import com.tattooju.exception.CommonException;
import com.tattooju.exception.NullParamException;
import com.tattooju.status.ReserveStatus;
import com.tattooju.util.JwtUtil;

@RequestMapping("reserve")
@RestController
public class ReserveCtrl {

	@Autowired
	ReserveBusiness reserveBusiness;
	
	@PostMapping()
	@ResponseBody
	public ResponseContent addReserve(Reserve reserve,
			@RequestHeader(value = "token",required = true) String token) throws Exception {
		if (reserve == null) {
			throw new NullParamException("参数");
		}
		Integer accountId = JwtUtil.getUserId(token, JwtUtil.JWT_SECRET);
		reserve.setAccountId(accountId);
		reserve.setStatus(ReserveStatus.RESERVED.value());
		reserveBusiness.addReserve(reserve);
		return ResponseContent.ok(null);
	}
	
	@PutMapping()
	@ResponseBody
	public ResponseContent updateReserve(Reserve reserve,
			@RequestHeader(value = "token",required = true) String token) throws Exception {
		if (reserve == null) {
			throw new NullParamException("参数");
		}
		if (reserve.getId() == null) {
			throw new NullParamException("id");
		}
		Integer accountId = JwtUtil.getUserId(token, JwtUtil.JWT_SECRET);
		reserveBusiness.updateReserve(reserve,accountId);
		return ResponseContent.ok(null);
	}
	
	@GetMapping()
	@ResponseBody
	public ResponseContent getReserveById(@RequestParam(required=true) int id) {
		Reserve reserve = reserveBusiness.getReserveById(id);
		return ResponseContent.ok(reserve);
	}
	
	@GetMapping("list")
	@ResponseBody
	public ResponseContent getReserveList(
			@RequestParam(required=true) int accountId,
			@RequestParam(defaultValue="1") int pageNum,
			@RequestParam(defaultValue="10") int pageSize) {
		
		return null;
	}
	
}

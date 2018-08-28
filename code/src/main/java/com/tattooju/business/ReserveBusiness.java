package com.tattooju.business;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.tattooju.config.Constant;
import com.tattooju.config.RedisLock;
import com.tattooju.config.ResponseCode;
import com.tattooju.dto.ReserveDto;
import com.tattooju.entity.Reserve;
import com.tattooju.entity.WechatAccount;
import com.tattooju.exception.CommonException;
import com.tattooju.service.ReserveService;
import com.tattooju.service.WechatAccountService;
import com.tattooju.status.AccountRoleEnum;
import com.tattooju.status.ReserveStatus;
import com.tattooju.util.DateUtil;

import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.entity.Example.Criteria;

@Service
public class ReserveBusiness {

	@Autowired
	ReserveService reserveService;
	
	@Autowired
	WechatAccountService wechatAccountService;
	
	public void addReserve(Reserve reserve) throws CommonException {
		Date date = reserve.getReserveTime();
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		int hour = cal.get(Calendar.HOUR);
		if (hour < 12) {
			hour = 10;
		}else {
			hour = 15;
		}
		cal.set(Calendar.HOUR, hour);
		date = cal.getTime();
		long reserveTime = date.getTime();//预约时间 这个时间是资源
		long expireTime = System.currentTimeMillis()+5000;// 5秒锁超时
		boolean lockSwi = true;
		String key = Constant.PREFIX_RESERVE + reserveTime;
		while (lockSwi) {
			if (RedisLock.lock(key, String.valueOf(expireTime))) {
				try {
					Example reservExample = new Example(Reserve.class);
					reservExample.createCriteria().andEqualTo("status", ReserveStatus.RESERVED.value())
							.andEqualTo("reserveTime", date);
					List<Reserve> reserves = reserveService.selectByExample(reservExample);
					if (!CollectionUtils.isEmpty(reserves)) {
						throw new CommonException(ResponseCode.FAILED.getValue(), "该时间已被预约");
					}
					int row = reserveService.saveNotNull(reserve);
					if (row < 1) {
						throw new CommonException(ResponseCode.FAILED);
					} 
				} finally {
					RedisLock.unlock(key, String.valueOf(expireTime));
					lockSwi = false;
				}
			}
		}
	}
	
	public void updateReserve(Reserve reserve,int accountId) throws CommonException {
		// 查询是不是管理员
		WechatAccount wechatAccount = wechatAccountService.selectByKey(accountId);
		if (wechatAccount == null) {
			throw new CommonException(ResponseCode.FAILED.getValue(), "用户不存在");
		}
		if (!wechatAccount.getRole().equals(AccountRoleEnum.ADMIN.value())) {// 不是管理员
			// 查询该数据的用户ID
			Example reserveExample = new Example(Reserve.class);
			reserveExample
				.selectProperties("accountId")
				.createCriteria()
				.andEqualTo("id", reserve.getId());
			List<Reserve> res = reserveService.selectByExample(reserveExample);
			if (!CollectionUtils.isEmpty(res)) {
				if (!res.get(0).getId().equals(accountId)) {//普通用户不能修改别人的预约
					throw new CommonException(ResponseCode.FAILED.getValue(), "没有权限操作");
				}
			}else {//找不到数据
				throw new CommonException(ResponseCode.FAILED.getValue(), "数据出错");
			}
		}
		//开始更新
		Date date = reserve.getReserveTime();
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		int hour = cal.get(Calendar.HOUR);
		if (hour < 12) {
			hour = 10;
		}else {
			hour = 15;
		}
		cal.set(Calendar.HOUR, hour);
		date = cal.getTime();
		long reserveTime = date.getTime();//预约时间 这个时间是资源
		long expireTime = System.currentTimeMillis()+5000;// 5秒锁超时
		boolean lockSwi = true;
		String key = Constant.PREFIX_RESERVE + reserveTime;	
		while (lockSwi) {
			if (RedisLock.lock(key, String.valueOf(expireTime))) {
				try {
					Example reservExample = new Example(Reserve.class);
					reservExample.createCriteria().andEqualTo("status", ReserveStatus.RESERVED.value())
							.andEqualTo("reserveTime", date);
					List<Reserve> reserves = reserveService.selectByExample(reservExample);
					if (!CollectionUtils.isEmpty(reserves)) {
						throw new CommonException(ResponseCode.FAILED.getValue(), "该时间已被预约");
					}
					int row = reserveService.updateNotNull(reserve);
					if (row < 1) {
						throw new CommonException(ResponseCode.FAILED.getValue(),"更新出错");
					}
				} finally {
					RedisLock.unlock(key, String.valueOf(expireTime));
					lockSwi = false;
				}
			}
		}
	}

	public ReserveDto getReserveById(int id) {
		Reserve reserve = reserveService.selectByKey(id);
		if (reserve == null || reserve.getStatus().equals(ReserveStatus.DELETE.value())) {
			return null;
		}
		ReserveDto reserveDto = new ReserveDto();
		reserveDto.setAccountId(reserve.getAccountId());
		reserveDto.setSex(reserve.getSex());
		reserveDto.setBody(reserve.getBody());
		reserveDto.setContent(reserve.getContent());
		reserveDto.setCreateTime(reserve.getCreateTime());
		WechatAccount account = wechatAccountService.selectByKey(reserve.getAccountId());
		reserveDto.setHeadImgUrl(account.getHeadImgUrl());
		reserveDto.setId(reserve.getId());
		reserveDto.setMobile(reserve.getMobile());
		reserveDto.setNickName(account.getNickname());
		reserveDto.setReserveTime(reserve.getReserveTime());
		reserveDto.setStatus(reserve.getStatus());
		reserveDto.setUpdateTime(reserve.getUpdateTime());
		reserveDto.setWxAccount(reserve.getWxAccount());
		return reserveDto;
	}
	
	public PageInfo<ReserveDto> getReserveList(int accountId,int pageNum,int pageSize,Date date) throws CommonException{
		// 查询是不是管理员
		WechatAccount wechatAccount = wechatAccountService.selectByKey(accountId);
		if (wechatAccount==null) {
			throw new CommonException(ResponseCode.FAILED.getValue(),"登录失效,请重新登录");
		}
		Example reserveExample = new Example(Reserve.class);
		Criteria criteria = reserveExample.createCriteria();
		criteria.andNotEqualTo("status", ReserveStatus.DELETE.value());
		if (date != null) {
			criteria.andGreaterThanOrEqualTo("createTime", DateUtil.getTodayStartTime(date));
			criteria.andLessThanOrEqualTo("createTime", DateUtil.getTodayEnd(date));
		}
		if (!wechatAccount.getRole().equals(AccountRoleEnum.ADMIN.value())) {//是管理员就查询全部
			criteria.andEqualTo("accountId", accountId);
		}
		reserveExample.orderBy("createTime").desc();
		PageInfo pageInfo = reserveService.selectByExample(reserveExample,pageNum, pageSize);
		List<ReserveDto> reserveDtos = new ArrayList<ReserveDto>();
		pageInfo.getList().stream().forEach(re->{
			Reserve reserve = (Reserve)re;
			reserve = (Reserve)reserve;
			ReserveDto reserveDto = new ReserveDto();
			reserveDto.setAccountId(reserve.getAccountId());
			reserveDto.setBody(reserve.getBody());
			reserveDto.setSex(reserve.getSex());
			reserveDto.setContent(reserve.getContent());
			reserveDto.setCreateTime(reserve.getCreateTime());
			WechatAccount account = wechatAccountService.selectByKey(reserve.getAccountId());
			reserveDto.setHeadImgUrl(account.getHeadImgUrl());
			reserveDto.setId(reserve.getId());
			reserveDto.setMobile(reserve.getMobile());
			reserveDto.setNickName(account.getNickname());
			reserveDto.setReserveTime(reserve.getReserveTime());
			reserveDto.setStatus(reserve.getStatus());
			reserveDto.setUpdateTime(reserve.getUpdateTime());
			reserveDto.setWxAccount(reserve.getWxAccount());
			reserveDtos.add(reserveDto);
		});
		pageInfo.setList(reserveDtos);
		System.out.println("is last page ? => "+pageInfo.isIsLastPage());
		return pageInfo;
	}

	public void updateReserveStatus(int id, Integer accountId, byte status) throws CommonException {
		WechatAccount wechatAccount = wechatAccountService.selectByKey(accountId);
		if (wechatAccount == null) {
			throw new CommonException(ResponseCode.FAILED.getValue(), "用户不存在");
		}
		if (!wechatAccount.getRole().equals(AccountRoleEnum.ADMIN.value())) {// 不是管理员
			// 查询该数据的用户ID
			Example reserveExample = new Example(Reserve.class);
			reserveExample
				.selectProperties("accountId")
				.createCriteria()
				.andEqualTo("id", id);
			List<Reserve> res = reserveService.selectByExample(reserveExample);
			if (!CollectionUtils.isEmpty(res)) {
				if (!res.get(0).getId().equals(accountId)) {//普通用户不能修改别人的预约
					throw new CommonException(ResponseCode.FAILED.getValue(), "没有权限操作");
				}
			}else {//找不到数据
				throw new CommonException(ResponseCode.FAILED.getValue(), "数据出错");
			}
		}
		Reserve reserve= new Reserve();
		reserve.setId(id);
		reserve.setStatus(status);
		reserve.setUpdateTime(new Date());
		int row = reserveService.updateNotNull(reserve);
		if (row < 1) {
			throw new CommonException(ResponseCode.FAILED.getValue(),"更新出错");
		}
		
	}
	
	public List<Date> getReserveDateList(Date startTime,Date endTime){
		Example reserveExample = new Example(Reserve.class);
		endTime = DateUtil.addHour(endTime, 24);
		reserveExample
			.createCriteria()
			.andEqualTo("status", ReserveStatus.RESERVED.value())
			.andGreaterThanOrEqualTo("reserveTime", startTime)
			.andLessThanOrEqualTo("reserveTime", endTime);
		reserveExample.orderBy("reserveTime").asc();
		List<Reserve> reserves = reserveService.selectByExample(reserveExample);
		return reserves.stream().map(t->(t.getReserveTime())).collect(Collectors.toList());
	}
	
}

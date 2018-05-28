package com.tattooju.business;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.github.pagehelper.PageInfo;
import com.tattooju.config.ResponseCode;
import com.tattooju.entity.Reserve;
import com.tattooju.entity.WechatAccount;
import com.tattooju.exception.CommonException;
import com.tattooju.service.ReserveService;
import com.tattooju.service.WechatAccountService;
import com.tattooju.status.AccountRoleEnum;

import tk.mybatis.mapper.entity.Example;

@Service
public class ReserveBusiness {

	@Autowired
	ReserveService reserveService;
	
	@Autowired
	WechatAccountService wechatAccountService;
	
	public void addReserve(Reserve reserve) throws CommonException {
		int row = reserveService.saveNotNull(reserve);
		if (row < 1) {
			throw new CommonException(ResponseCode.FAILED);
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
		int row = reserveService.updateNotNull(reserve);
		if (row < 1) {
			throw new CommonException(ResponseCode.FAILED);
		}
	}

	public Reserve getReserveById(int id) {
		return reserveService.selectByKey(id);
	}
	
	public PageInfo<Reserve> getReserveList(int accountId,int pageNum,int pageSize) throws CommonException{
		// 查询是不是管理员
		WechatAccount wechatAccount = wechatAccountService.selectByKey(accountId);
		if (wechatAccount==null) {
			throw new CommonException(ResponseCode.FAILED.getValue(),"登录失效,请重新登录");
		}
		if (wechatAccount.getRole().equals(AccountRoleEnum.ADMIN.value())) {//是管理员就查询全部
			
		}
		return null;
	}
	
}

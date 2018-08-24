package com.tattooju.business;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.github.pagehelper.PageInfo;
import com.tattooju.config.FastdfsConfig;
import com.tattooju.config.MyStorageClient;
import com.tattooju.config.ResponseCode;
import com.tattooju.entity.Draft;
import com.tattooju.entity.WechatAccount;
import com.tattooju.exception.CommonException;
import com.tattooju.service.DraftService;
import com.tattooju.service.WechatAccountService;
import com.tattooju.status.AccountRoleEnum;

import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.entity.Example.Criteria;

@Service
public class DraftBusiness {

	private Logger logger = LoggerFactory.getLogger(getClass());
	
	@Autowired
	MyStorageClient myStorageClient;
	
	@Autowired
	DraftService draftService;
	
	@Autowired
	FastdfsConfig config;
	
	@Autowired
	WechatAccountService wechatAccountService;
	
	
	public void addMedia(String content,String mediaPath,String tagContent, byte type,int accountId) throws CommonException {
		WechatAccount wechatAccount = wechatAccountService.selectByKey(accountId);
		if (wechatAccount==null || !wechatAccount.getRole().equals(AccountRoleEnum.ADMIN.value())) {
			throw new CommonException(ResponseCode.FAILED.getValue(), "没有权限操作");
		}
		Draft draft = new Draft();
		draft.setContent(content);
		draft.setCreateTime(new Date());
		draft.setMediaPath(mediaPath);
		draft.setTagContent(","+tagContent+",");//抖机灵方便查询
		draft.setType(type);
		int row = draftService.saveNotNull(draft);
		if (row < 1) {
			throw new CommonException(ResponseCode.FAILED.getValue(),"更新出错");
		}
	}


	public void updateMedia(int id,String content, String mediaPath, String tagContent, byte type, int accountId) throws CommonException {
		WechatAccount wechatAccount = wechatAccountService.selectByKey(accountId);
		if (wechatAccount==null || !wechatAccount.getRole().equals(AccountRoleEnum.ADMIN.value())) {
			throw new CommonException(ResponseCode.FAILED.getValue(), "没有权限操作");
		}
		Draft draft = new Draft();
		draft.setId(id);
		draft.setContent(content);
		draft.setCreateTime(new Date());
		draft.setMediaPath(mediaPath);
		draft.setTagContent(","+tagContent+",");//抖机灵方便查询
		draft.setType(type);
		int row = draftService.updateNotNull(draft);
		if (row < 1) {
			throw new CommonException(ResponseCode.FAILED.getValue(),"更新出错");
		}
	}


	public Draft getMediaById(int id) {
		Draft draft = draftService.selectByKey(id);
		String tagContent = draft.getTagContent();
		tagContent = tagContent.replaceFirst(",", "").substring(0, tagContent.length()-1);
		draft.setContent(tagContent);
		return draft;
	}


	@SuppressWarnings("unchecked")
	public PageInfo<Draft> getMediaList(int pageNum, int pageSize, String keyword, String tag, Byte type) {
		Example mediaExample = new Example(Draft.class);
		Criteria criteria = mediaExample.createCriteria();
		if (!StringUtils.isEmpty(keyword)) {
			criteria.andLike("content", "%"+keyword+"%");
		}
		if (!StringUtils.isEmpty(tag)) {
			String[] tags = tag.split(",");
			StringBuffer sb = new StringBuffer();
			sb.append(" ( ");
			for (int i = 0; i < tags.length; i++) {
				if (i==0) {
					sb.append("tag_content like '%,"+tags[i].trim()+",%'");
				}else {
					sb.append(" or tag_content like '%,"+tags[i].trim()+",%'");
				}
			}
			sb.append(" ) ");
			criteria.andCondition(sb.toString());
		}
		if (type!=null) {
			criteria.andEqualTo("type", type);
		}
		mediaExample.orderBy("rank").desc();
		mediaExample.orderBy("createTime").desc();
		return draftService.selectByExample(mediaExample, pageNum, pageSize);
	}


	public void deleteMediaById(int id, int accountId) throws CommonException {
		WechatAccount wechatAccount = wechatAccountService.selectByKey(accountId);
		if (wechatAccount==null || !wechatAccount.getRole().equals(AccountRoleEnum.ADMIN.value())) {
			throw new CommonException(ResponseCode.FAILED.getValue(), "没有权限操作");
		}
		int row = draftService.delete(id);
		if (row < 1) {
			throw new CommonException(ResponseCode.FAILED.getValue(),"删除出错");
		}
	}


	public List<String> getTags() {
		Example tagExample = new Example(Draft.class);
		tagExample.selectProperties("tagContent");
		List<Draft> medias = draftService.selectByExample(tagExample);
		List<String> str = medias.stream().reduce((new ArrayList<String>()),(t1,t2)->{
			t1.addAll(Arrays.asList(t2.getTagContent().split(",")));
			return t1;
		},(t1,t2)->(t1=t2));
		str = str.stream().distinct().collect(Collectors.toList());
		return str;
	}
	
}

package com.tattooju.business;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.commons.io.FilenameUtils;
import org.csource.common.MyException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.alibaba.fastjson.JSONArray;
import com.github.pagehelper.PageInfo;
import com.tattooju.config.FastdfsConfig;
import com.tattooju.config.MyStorageClient;
import com.tattooju.config.ResponseCode;
import com.tattooju.dto.ArticleDto;
import com.tattooju.entity.Media;
import com.tattooju.entity.WechatAccount;
import com.tattooju.exception.CommonException;
import com.tattooju.service.MediaService;
import com.tattooju.service.WechatAccountService;
import com.tattooju.status.AccountRoleEnum;
import com.tattooju.status.MediaTypeEnum;

import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.entity.Example.Criteria;

@Service
public class MediaBusiness {

	private Logger logger = LoggerFactory.getLogger(getClass());
	
	@Autowired
	MyStorageClient myStorageClient;
	
	@Autowired
	MediaService mediaService;
	
	@Autowired
	FastdfsConfig config;
	
	@Autowired
	WechatAccountService wechatAccountService;
	
	public String mediaUpload(byte[] data,String fileName, int type,int accountId) throws CommonException {
		WechatAccount wechatAccount = wechatAccountService.selectByKey(accountId);
		if (wechatAccount==null || !wechatAccount.getRole().equals(AccountRoleEnum.ADMIN.value())) {
			throw new CommonException(ResponseCode.FAILED.getValue(), "没有权限操作");
		}
		if (type == MediaTypeEnum.OTHER.value()) {//非视频类
			String ext = FilenameUtils.getExtension(fileName);
			try {
				String[] path = myStorageClient.upload_file(data, ext, null);
				String result = Stream.of(path).reduce(new StringBuffer(config.getFdfsNginxPath()), 
						(sb,p)->(sb.append("/"+p)),(t1,t2)->(t1 = t2)).toString();
				return result;
			} catch (IOException | MyException e) {
				logger.error("上传失败 msg=>{}",e.getMessage());
				throw new CommonException(ResponseCode.FAILED);
			}
		}
		// 视频类
		
		return null;
	}
	
	
	public void addMedia(String content,String mediaPath,String tagContent, byte type,int accountId) throws CommonException {
		WechatAccount wechatAccount = wechatAccountService.selectByKey(accountId);
		if (wechatAccount==null || !wechatAccount.getRole().equals(AccountRoleEnum.ADMIN.value())) {
			throw new CommonException(ResponseCode.FAILED.getValue(), "没有权限操作");
		}
		Media media = new Media();
		media.setContent(content);
		media.setCreateTime(new Date());
		media.setMediaPath(mediaPath);
		media.setTagContent(","+tagContent+",");//抖机灵方便查询
		media.setType(type);
		int row = mediaService.saveNotNull(media);
		if (row < 1) {
			throw new CommonException(ResponseCode.FAILED.getValue(),"更新出错");
		}
	}


	public void updateMedia(int id,String content, String mediaPath, String tagContent, byte type, int accountId) throws CommonException {
		WechatAccount wechatAccount = wechatAccountService.selectByKey(accountId);
		if (wechatAccount==null || !wechatAccount.getRole().equals(AccountRoleEnum.ADMIN.value())) {
			throw new CommonException(ResponseCode.FAILED.getValue(), "没有权限操作");
		}
		Media media = new Media();
		media.setId(id);
		media.setContent(content);
		media.setCreateTime(new Date());
		media.setMediaPath(mediaPath);
		media.setTagContent(","+tagContent+",");//抖机灵方便查询
		media.setType(type);
		int row = mediaService.updateNotNull(media);
		if (row < 1) {
			throw new CommonException(ResponseCode.FAILED.getValue(),"更新出错");
		}
	}


	public Media getMediaById(int id) {
		Media media = mediaService.selectByKey(id);
		String tagContent = media.getTagContent();
		tagContent = tagContent.replaceFirst(",", "").substring(0, tagContent.length()-1);
		media.setContent(tagContent);
		return media;
	}


	@SuppressWarnings("unchecked")
	public PageInfo<Media> getMediaList(int pageNum, int pageSize, String keyword, String tag, Byte type) {
		Example mediaExample = new Example(Media.class);
		Criteria criteria = mediaExample.createCriteria();
		if (!StringUtils.isEmpty(keyword)) {
			criteria.andLike("content", "%"+keyword+"%");
		}
		if (!StringUtils.isEmpty(tag)) {
			String[] tags = tag.split(",");
			StringBuffer sb = new StringBuffer();
			sb.append("( ");
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
		mediaExample.orderBy("createTime").desc();
		return mediaService.selectByExample(mediaExample, pageNum, pageSize);
	}


	public void deleteMediaById(int id, int accountId) throws CommonException {
		WechatAccount wechatAccount = wechatAccountService.selectByKey(accountId);
		if (wechatAccount==null || !wechatAccount.getRole().equals(AccountRoleEnum.ADMIN.value())) {
			throw new CommonException(ResponseCode.FAILED.getValue(), "没有权限操作");
		}
		int row = mediaService.delete(id);
		if (row < 1) {
			throw new CommonException(ResponseCode.FAILED.getValue(),"删除出错");
		}
	}


	public List<String> getTags() {
		Example tagExample = new Example(Media.class);
		tagExample.selectProperties("tagContent");
		List<Media> medias = mediaService.selectByExample(tagExample);
		List<String> str = medias.stream().reduce((new ArrayList<String>()),(t1,t2)->{
			t1.addAll(Arrays.asList(t2.getTagContent().split(",")));
			return t1;
		},(t1,t2)->(t1=t2));
		str = str.stream().distinct().collect(Collectors.toList());
		return str;
	}
	
}

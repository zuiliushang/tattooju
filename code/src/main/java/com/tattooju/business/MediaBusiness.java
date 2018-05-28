package com.tattooju.business;

import java.io.IOException;
import java.util.stream.Stream;

import org.apache.commons.io.FilenameUtils;
import org.csource.common.MyException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tattooju.config.FastdfsConfig;
import com.tattooju.config.MyStorageClient;
import com.tattooju.config.ResponseCode;
import com.tattooju.exception.CommonException;
import com.tattooju.status.MediaTypeEnum;

@Service
public class MediaBusiness {

	private Logger logger = LoggerFactory.getLogger(getClass());
	
	@Autowired
	MyStorageClient myStorageClient;
	
	@Autowired
	FastdfsConfig config;
	
	public String mediaUpload(byte[] data,String fileName, int type) throws CommonException {
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
	
}

package com.tattooju.ctrl;

import java.util.Date;
import java.util.stream.Stream;

import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.tattooju.config.FastdfsConfig;
import com.tattooju.config.MyStorageClient;
import com.tattooju.config.ResponseContent;
import com.tattooju.entity.Article;
import com.tattooju.entity.Draft;
import com.tattooju.entity.Media;
import com.tattooju.service.ArticleService;
import com.tattooju.service.DraftService;
import com.tattooju.service.MediaService;

@RestController
@RequestMapping("cms")
public class CmsCtrl {

	@Autowired
	MyStorageClient myStorageClient;
	
	@Autowired
	FastdfsConfig config;
	
	@PostMapping("upload")
	public ResponseContent mediaUpload(
			@RequestParam(value = "file", required = true)MultipartFile file
		) throws Exception {
		String ext = FilenameUtils.getExtension(file.getOriginalFilename());
		String[] path = myStorageClient.upload_file(file.getBytes(), ext, null);
		String result = Stream.of(path).reduce(new StringBuffer(config.getFdfsNginxPath()), 
				(sb,p)->(sb.append("/"+p)),(t1,t2)->(t1 = t2)).toString();
		return ResponseContent.ok(result);
	}
	
	@Autowired
	ArticleService articleService;
	
	@Autowired
	MediaService mediaService;
	
	@Autowired
	DraftService draftService;
	
	
	@PostMapping("add")
	public ResponseContent addMedia(@RequestParam(required=true) byte type,
			@RequestParam(required=true) String coverImg,
			@RequestParam(required=true) String body,
			String tagContent,
			String title) {
		/**
		 *  <option value="1">文章</option>
        <option value="2">活动</option>
        <option value="3">纹身作品图片</option>
        <option value="4">纹身作品视频</option>
        <option value="5">手稿图片</option>
        <option value="6">手稿视频</option>
		 */
		if (type == 1) {
			Article article = new Article();
			article.setContent(body);
			article.setCoverImg(coverImg);
			article.setCreateTime(new Date());
			article.setTitle(title);
			article.setType((byte)2);
			article.setUpdateTime(new Date());
			articleService.saveNotNull(article);
		}else if (type==2) {
			Article article = new Article();
			article.setContent(body);
			article.setCoverImg(coverImg);
			article.setCreateTime(new Date());
			article.setTitle(title);
			article.setType((byte)1);
			article.setUpdateTime(new Date());
			articleService.saveNotNull(article);
		}else if (type == 3) {
			Media media = new Media();
			media.setContent(body);
			media.setCreateTime(new Date());
			media.setMediaPath(coverImg);
			media.setTagContent(","+tagContent+",");//抖机灵方便查询
			media.setType((byte)1);
			mediaService.saveNotNull(media);
		}else if (type==4) {
			Media media = new Media();
			media.setContent(body);
			media.setCreateTime(new Date());
			media.setMediaPath(coverImg);
			media.setTagContent(","+tagContent+",");//抖机灵方便查询
			media.setType((byte)2);
			mediaService.saveNotNull(media);
		}else if(type == 5) {
			Draft draft = new Draft();
			draft.setContent(body);
			draft.setCreateTime(new Date());
			draft.setMediaPath(coverImg);
			draft.setTagContent(","+tagContent+",");//抖机灵方便查询
			draft.setType((byte)1);
			draftService.saveNotNull(draft);
		}else if(type == 6) {
			Draft draft = new Draft();
			draft.setContent(body);
			draft.setCreateTime(new Date());
			draft.setMediaPath(coverImg);
			draft.setTagContent(","+tagContent+",");//抖机灵方便查询
			draft.setType((byte)2);
			draftService.saveNotNull(draft);
		}
		return ResponseContent.ok(null);
	}
}

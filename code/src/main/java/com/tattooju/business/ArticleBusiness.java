package com.tattooju.business;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.tattooju.config.ResponseCode;
import com.tattooju.dto.ArticleCommentDto;
import com.tattooju.entity.Article;
import com.tattooju.entity.ArticleComment;
import com.tattooju.entity.WechatAccount;
import com.tattooju.exception.CommonException;
import com.tattooju.service.ArticleCommentService;
import com.tattooju.service.ArticleService;
import com.tattooju.service.WechatAccountService;
import com.tattooju.status.AccountRoleEnum;

import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.entity.Example.Criteria;

@Service
public class ArticleBusiness {

	@Autowired
	ArticleService articleService;
	
	@Autowired
	WechatAccountService wechatAccountService;
	
	@Autowired
	ArticleCommentService articleCommentService;
	
	public void addArticle(String content,String coverImg,String title,byte type,int accountId) throws CommonException {
		WechatAccount wechatAccount = wechatAccountService.selectByKey(accountId);
		if (wechatAccount==null || !wechatAccount.getRole().equals(AccountRoleEnum.ADMIN.value())) {
			throw new CommonException(ResponseCode.FAILED.getValue(), "没有权限操作");
		}
		Article article = new Article();
		article.setContent(content);
		article.setCoverImg(coverImg);
		article.setCreateTime(new Date());
		article.setTitle(title);
		article.setType(type);
		article.setUpdateTime(new Date());
		int row = articleService.saveNotNull(article);
		if (row < 1) {
			throw new CommonException(ResponseCode.FAILED.getValue(),"保存失败");
		}
	}

	public void updateArticle(int id, String content, String coverImg, String title, int accountId) throws CommonException {
		WechatAccount wechatAccount = wechatAccountService.selectByKey(accountId);
		if (wechatAccount==null || !wechatAccount.getRole().equals(AccountRoleEnum.ADMIN.value())) {
			throw new CommonException(ResponseCode.FAILED.getValue(), "没有权限操作");
		}
		Article article = new Article();
		article.setId(id);
		article.setContent(content);
		article.setCoverImg(coverImg);
		article.setTitle(title);
		article.setUpdateTime(new Date());
		int row = articleService.updateNotNull(article);
		if (row < 1) {
			throw new CommonException(ResponseCode.FAILED.getValue(),"更新失败");
		}
	}
	
	public Article getArticleById(int articleId) {
		return articleService.selectByKey(articleId);
	}
	
	@SuppressWarnings("unchecked")
	public PageInfo<Article> getArticleList(int pageNum,int pageSize,Byte type){
		Example articleExample = new Example(Article.class);
		Criteria criteria = articleExample.createCriteria();
		articleExample.orderBy("rank").desc();
		if (type!=null) {
			criteria.andEqualTo("type", type);
		}
		articleExample.orderBy("createTime").desc();
		return articleService.selectByExample(articleExample,pageNum,pageSize);
	}
	
	public PageInfo<ArticleCommentDto> getArticleComment(int pageNum,int pageSize,int articleId){
		PageHelper.startPage(pageNum, pageSize);
		Example example = new Example(ArticleComment.class);
		example.orderBy("createTime").desc();
		List<ArticleComment> articleComments = articleCommentService.selectByExample(example);
		PageInfo resuls = new PageInfo<>(articleComments);
		List<ArticleCommentDto> dtos = articleComments.stream().map(comment->{
			ArticleCommentDto articleCommentDto = new ArticleCommentDto();
			int accountId = comment.getAccountId();
			WechatAccount wechatAccount = wechatAccountService.selectByKey(accountId);
			articleCommentDto.setContent(comment.getContent());
			articleCommentDto.setHeadImgUrl(wechatAccount.getHeadImgUrl());
			articleCommentDto.setCreateTime(comment.getCreateTime());
			articleCommentDto.setId(comment.getId());
			articleCommentDto.setNickName(wechatAccount.getNickname());
			return articleCommentDto;
		}).collect(Collectors.toList());
		resuls.setList(dtos);
		return resuls;
	}
	
	public void writeComment(int articleId,int accountId,String content) throws CommonException {
		ArticleComment articleComment = new ArticleComment();
		articleComment.setAccountId(accountId);
		articleComment.setContent(content);
		articleComment.setCreateTime(new Date());
		articleComment.setArticleId(articleId);
		int row = articleCommentService.saveNotNull(articleComment);
		if (row < 1) {
			throw new CommonException(ResponseCode.FAILED.getValue(),"写入失败");
		}
	}
	@Transactional(rollbackFor=CommonException.class)
	public void deleteArticleById(int id, int accountId) throws CommonException {
		WechatAccount wechatAccount = wechatAccountService.selectByKey(accountId);
		if (wechatAccount==null || !wechatAccount.getRole().equals(AccountRoleEnum.ADMIN.value())) {
			throw new CommonException(ResponseCode.FAILED.getValue(), "没有权限操作");
		}
		Example commentExample = new Example(ArticleComment.class);
		commentExample.createCriteria().andEqualTo("articleId", id);
		articleCommentService.deleteByExample(commentExample);
		int row = articleService.delete(id);
		if (row < 1) {
			throw new CommonException(ResponseCode.FAILED.getValue(),"删除失败");
		}
	}

	public void deleteArticleCommentById(int id, int accountId) throws CommonException {
		WechatAccount wechatAccount = wechatAccountService.selectByKey(accountId);
		if (wechatAccount==null || !wechatAccount.getRole().equals(AccountRoleEnum.ADMIN.value())) {
			throw new CommonException(ResponseCode.FAILED.getValue(), "没有权限操作");
		}
		int row = articleCommentService.delete(id);
		if (row < 1) {
			throw new CommonException(ResponseCode.FAILED.getValue(),"删除失败");
		}
	}
	
}

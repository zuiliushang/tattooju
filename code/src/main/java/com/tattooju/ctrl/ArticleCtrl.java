package com.tattooju.ctrl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.github.pagehelper.PageInfo;
import com.tattooju.business.ArticleBusiness;
import com.tattooju.config.ResponseContent;
import com.tattooju.dto.ArticleCommentDto;
import com.tattooju.entity.Article;
import com.tattooju.util.JwtUtil;

@RestController
@RequestMapping("article")
public class ArticleCtrl {

	@Autowired
	ArticleBusiness articleBusiness;
	
	@PostMapping
	public ResponseContent addArticle(
			@RequestParam(required=true) String content,
			@RequestParam(required=true) String coverImg,
			@RequestParam(required=true) String title,
			@RequestParam(required=true) byte type,
			@RequestHeader(required=true) String token
			) throws Exception {
		int accountId = JwtUtil.getUserId(JwtUtil.JWT_SECRET,token);
		articleBusiness.addArticle(content, coverImg, title, type,accountId);
		return ResponseContent.ok(null);
	}
	
	@PutMapping
	public ResponseContent updateArticle(
			@RequestParam(required=true) int id,
			@RequestParam(required=true) String content,
			@RequestParam(required=true) String coverImg,
			@RequestParam(required=true) String title,
			@RequestHeader(required=true) String token
			) throws Exception {
		int accountId = JwtUtil.getUserId(JwtUtil.JWT_SECRET,token);
		articleBusiness.updateArticle(id,content, coverImg, title,accountId);
		return ResponseContent.ok(null);
	}
	
	@GetMapping
	public ResponseContent getArticle(@RequestParam(required=true) int id) {
		Article article = articleBusiness.getArticleById(id);
		return ResponseContent.ok(article);
	}
	
	@DeleteMapping
	public ResponseContent deleteArticle(@RequestParam(required=true) int id,@RequestHeader(required=true) String token) throws Exception {
		int accountId = JwtUtil.getUserId(JwtUtil.JWT_SECRET,token);
		articleBusiness.deleteArticleById(id,accountId);
		return ResponseContent.ok(null);
	}
	
	@GetMapping("list")
	public ResponseContent getArticleList(@RequestParam(defaultValue="1") int pageNum,
			@RequestParam(defaultValue="5") int pageSize,
			@RequestParam() Byte type) {
		PageInfo<Article> pageInfo = articleBusiness.getArticleList(pageNum, pageSize,type);
		return ResponseContent.ok(pageInfo);
	}
	
	@GetMapping("comment/list")
	public ResponseContent getArticleCommentList(
			@RequestParam(defaultValue="1") int pageNum,
			@RequestParam(defaultValue="5") int pageSize,
			@RequestParam(required=true) int articleId) {
		PageInfo<ArticleCommentDto> pageInfo = articleBusiness.getArticleComment(pageNum, pageSize, articleId);
		return ResponseContent.ok(pageInfo);
	}
	
	@PostMapping("comment")
	public ResponseContent addArticleComment(
			@RequestParam(required=true)int id,
			@RequestParam(required=true) String content,
			@RequestHeader(value="token",required=true) String token) throws Exception {
		int accountId = JwtUtil.getUserId(JwtUtil.JWT_SECRET,token);
		articleBusiness.writeComment(id, accountId, content);
		return ResponseContent.ok(null);
	}
	
	@DeleteMapping("comment")
	public ResponseContent deleteArticleComment(@RequestParam(required=true) int id,
			@RequestHeader(required =true) String token) throws Exception {
		int accountId = JwtUtil.getUserId(JwtUtil.JWT_SECRET,token);
		articleBusiness.deleteArticleCommentById(id, accountId);
		return ResponseContent.ok(null);
	}
	
}

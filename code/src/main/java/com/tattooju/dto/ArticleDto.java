package com.tattooju.dto;

import java.util.Date;

import com.alibaba.fastjson.JSONArray;

public class ArticleDto {

	/**
     * 主键
     */
    private Integer id;

    /**
     * 内容
     */
    private JSONArray content;

    /**
     * 对象地址
     */
    private String mediaPath;

    /**
     * 类型 1)图片 2)视频
     */
    private Byte type;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 用逗号隔开的tag
     */
    private String tagContent;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public JSONArray getContent() {
		return content;
	}

	public void setContent(JSONArray content) {
		this.content = content;
	}

	public String getMediaPath() {
		return mediaPath;
	}

	public void setMediaPath(String mediaPath) {
		this.mediaPath = mediaPath;
	}

	public Byte getType() {
		return type;
	}

	public void setType(Byte type) {
		this.type = type;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public String getTagContent() {
		return tagContent;
	}

	public void setTagContent(String tagContent) {
		this.tagContent = tagContent;
	}

    
	
}

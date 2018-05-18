package com.tattooju.entity;

import java.util.Date;
import javax.persistence.*;

@Table(name = "`tbl_article`")
public class Article {
    /**
     * ID
     */
    @Id
    @Column(name = "`id`")
    private Long id;

    /**
     * 封面
     */
    @Column(name = "`cover_img`")
    private String coverImg;

    /**
     * 标题
     */
    @Column(name = "`title`")
    private String title;

    /**
     * 类型 1)活动 2)文章
     */
    @Column(name = "`type`")
    private Byte type;

    /**
     * 创建时间
     */
    @Column(name = "`create_time`")
    private Date createTime;

    /**
     * 最后编辑时间
     */
    @Column(name = "`update_time`")
    private Date updateTime;

    /**
     * 内容正文
     */
    @Column(name = "`content`")
    private String content;

    /**
     * 获取ID
     *
     * @return id - ID
     */
    public Long getId() {
        return id;
    }

    /**
     * 设置ID
     *
     * @param id ID
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * 获取封面
     *
     * @return cover_img - 封面
     */
    public String getCoverImg() {
        return coverImg;
    }

    /**
     * 设置封面
     *
     * @param coverImg 封面
     */
    public void setCoverImg(String coverImg) {
        this.coverImg = coverImg == null ? null : coverImg.trim();
    }

    /**
     * 获取标题
     *
     * @return title - 标题
     */
    public String getTitle() {
        return title;
    }

    /**
     * 设置标题
     *
     * @param title 标题
     */
    public void setTitle(String title) {
        this.title = title == null ? null : title.trim();
    }

    /**
     * 获取类型 1)活动 2)文章
     *
     * @return type - 类型 1)活动 2)文章
     */
    public Byte getType() {
        return type;
    }

    /**
     * 设置类型 1)活动 2)文章
     *
     * @param type 类型 1)活动 2)文章
     */
    public void setType(Byte type) {
        this.type = type;
    }

    /**
     * 获取创建时间
     *
     * @return create_time - 创建时间
     */
    public Date getCreateTime() {
        return createTime;
    }

    /**
     * 设置创建时间
     *
     * @param createTime 创建时间
     */
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    /**
     * 获取最后编辑时间
     *
     * @return update_time - 最后编辑时间
     */
    public Date getUpdateTime() {
        return updateTime;
    }

    /**
     * 设置最后编辑时间
     *
     * @param updateTime 最后编辑时间
     */
    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    /**
     * 获取内容正文
     *
     * @return content - 内容正文
     */
    public String getContent() {
        return content;
    }

    /**
     * 设置内容正文
     *
     * @param content 内容正文
     */
    public void setContent(String content) {
        this.content = content == null ? null : content.trim();
    }
}
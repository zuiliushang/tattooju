package com.tattooju.entity;

import java.util.Date;
import javax.persistence.*;

@Table(name = "`tbl_media`")
public class Media {
    /**
     * 主键
     */
    @Id
    @Column(name = "`id`")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * 内容
     */
    @Column(name = "`content`")
    private String content;

    /**
     * 对象地址
     */
    @Column(name = "`media_path`")
    private String mediaPath;

    /**
     * 类型 1)图片 2)视频
     */
    @Column(name = "`type`")
    private Byte type;

    /**
     * 排序 大->小
     */
    @Column(name = "`rank`")
    private Integer rank;

    /**
     * 创建时间
     */
    @Column(name = "`create_time`")
    private Date createTime;

    /**
     * 用逗号隔开的tag
     */
    @Column(name = "`tag_content`")
    private String tagContent;

    /**
     * 获取主键
     *
     * @return id - 主键
     */
    public Integer getId() {
        return id;
    }

    /**
     * 设置主键
     *
     * @param id 主键
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * 获取内容
     *
     * @return content - 内容
     */
    public String getContent() {
        return content;
    }

    /**
     * 设置内容
     *
     * @param content 内容
     */
    public void setContent(String content) {
        this.content = content == null ? null : content.trim();
    }

    /**
     * 获取对象地址
     *
     * @return media_path - 对象地址
     */
    public String getMediaPath() {
        return mediaPath;
    }

    /**
     * 设置对象地址
     *
     * @param mediaPath 对象地址
     */
    public void setMediaPath(String mediaPath) {
        this.mediaPath = mediaPath == null ? null : mediaPath.trim();
    }

    /**
     * 获取类型 1)图片 2)视频
     *
     * @return type - 类型 1)图片 2)视频
     */
    public Byte getType() {
        return type;
    }

    /**
     * 设置类型 1)图片 2)视频
     *
     * @param type 类型 1)图片 2)视频
     */
    public void setType(Byte type) {
        this.type = type;
    }

    /**
     * 获取排序 大->小
     *
     * @return rank - 排序 大->小
     */
    public Integer getRank() {
        return rank;
    }

    /**
     * 设置排序 大->小
     *
     * @param rank 排序 大->小
     */
    public void setRank(Integer rank) {
        this.rank = rank;
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
     * 获取用逗号隔开的tag
     *
     * @return tag_content - 用逗号隔开的tag
     */
    public String getTagContent() {
        return tagContent;
    }

    /**
     * 设置用逗号隔开的tag
     *
     * @param tagContent 用逗号隔开的tag
     */
    public void setTagContent(String tagContent) {
        this.tagContent = tagContent == null ? null : tagContent.trim();
    }
}
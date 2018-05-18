package com.tattooju.entity;

import javax.persistence.*;

@Table(name = "`tbl_media_tag`")
public class MediaTag {
    /**
     * ID
     */
    @Id
    @Column(name = "`id`")
    private Long id;

    /**
     * media主键
     */
    @Column(name = "`media_id`")
    private Long mediaId;

    /**
     * 标签Id
     */
    @Column(name = "`tag_id`")
    private Long tagId;

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
     * 获取media主键
     *
     * @return media_id - media主键
     */
    public Long getMediaId() {
        return mediaId;
    }

    /**
     * 设置media主键
     *
     * @param mediaId media主键
     */
    public void setMediaId(Long mediaId) {
        this.mediaId = mediaId;
    }

    /**
     * 获取标签Id
     *
     * @return tag_id - 标签Id
     */
    public Long getTagId() {
        return tagId;
    }

    /**
     * 设置标签Id
     *
     * @param tagId 标签Id
     */
    public void setTagId(Long tagId) {
        this.tagId = tagId;
    }
}
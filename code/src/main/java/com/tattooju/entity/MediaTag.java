package com.tattooju.entity;

import javax.persistence.*;

@Table(name = "`tbl_media_tag`")
public class MediaTag {
    /**
     * ID
     */
    @Id
    @Column(name = "`id`")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * media主键
     */
    @Column(name = "`media_id`")
    private Integer mediaId;

    /**
     * 标签Id
     */
    @Column(name = "`tag_id`")
    private Integer tagId;

    /**
     * 获取ID
     *
     * @return id - ID
     */
    public Integer getId() {
        return id;
    }

    /**
     * 设置ID
     *
     * @param id ID
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * 获取media主键
     *
     * @return media_id - media主键
     */
    public Integer getMediaId() {
        return mediaId;
    }

    /**
     * 设置media主键
     *
     * @param mediaId media主键
     */
    public void setMediaId(Integer mediaId) {
        this.mediaId = mediaId;
    }

    /**
     * 获取标签Id
     *
     * @return tag_id - 标签Id
     */
    public Integer getTagId() {
        return tagId;
    }

    /**
     * 设置标签Id
     *
     * @param tagId 标签Id
     */
    public void setTagId(Integer tagId) {
        this.tagId = tagId;
    }
}
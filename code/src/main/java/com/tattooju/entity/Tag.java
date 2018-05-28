package com.tattooju.entity;

import javax.persistence.*;

@Table(name = "`tbl_tag`")
public class Tag {
    /**
     * 主键
     */
    @Id
    @Column(name = "`id`")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * 标签名字
     */
    @Column(name = "`name`")
    private String name;

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
     * 获取标签名字
     *
     * @return name - 标签名字
     */
    public String getName() {
        return name;
    }

    /**
     * 设置标签名字
     *
     * @param name 标签名字
     */
    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }
}
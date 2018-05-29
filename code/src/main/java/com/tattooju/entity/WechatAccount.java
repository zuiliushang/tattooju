package com.tattooju.entity;

import java.util.Date;
import javax.persistence.*;

@Table(name = "`tbl_wechat_account`")
public class WechatAccount {
    /**
     * 主键
     */
    @Id
    @Column(name = "`id`")
    private Integer id;

    /**
     * openid
     */
    @Column(name = "`open_id`")
    private String openId;

    /**
     * 性别 1)男 2)女
     */
    @Column(name = "`sex`")
    private Byte sex;

    /**
     * 角色 1）普通用户 2）管理员
     */
    @Column(name = "`role`")
    private Byte role;

    /**
     * 头像地址
     */
    @Column(name = "`head_img_url`")
    private String headImgUrl;

    /**
     * 昵称
     */
    @Column(name = "`nickname`")
    private String nickname;

    /**
     * 创建时间
     */
    @Column(name = "`create_time`")
    private Date createTime;

    /**
     * 更新时间
     */
    @Column(name = "`update_time`")
    private Date updateTime;

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
     * 获取openid
     *
     * @return open_id - openid
     */
    public String getOpenId() {
        return openId;
    }

    /**
     * 设置openid
     *
     * @param openId openid
     */
    public void setOpenId(String openId) {
        this.openId = openId == null ? null : openId.trim();
    }

    /**
     * 获取性别 1)男 2)女
     *
     * @return sex - 性别 1)男 2)女
     */
    public Byte getSex() {
        return sex;
    }

    /**
     * 设置性别 1)男 2)女
     *
     * @param sex 性别 1)男 2)女
     */
    public void setSex(Byte sex) {
        this.sex = sex;
    }

    /**
     * 获取角色 1）普通用户 2）管理员
     *
     * @return role - 角色 1）普通用户 2）管理员
     */
    public Byte getRole() {
        return role;
    }

    /**
     * 设置角色 1）普通用户 2）管理员
     *
     * @param role 角色 1）普通用户 2）管理员
     */
    public void setRole(Byte role) {
        this.role = role;
    }

    /**
     * 获取头像地址
     *
     * @return head_img_url - 头像地址
     */
    public String getHeadImgUrl() {
        return headImgUrl;
    }

    /**
     * 设置头像地址
     *
     * @param headImgUrl 头像地址
     */
    public void setHeadImgUrl(String headImgUrl) {
        this.headImgUrl = headImgUrl == null ? null : headImgUrl.trim();
    }

    /**
     * 获取昵称
     *
     * @return nickname - 昵称
     */
    public String getNickname() {
        return nickname;
    }

    /**
     * 设置昵称
     *
     * @param nickname 昵称
     */
    public void setNickname(String nickname) {
        this.nickname = nickname == null ? null : nickname.trim();
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
     * 获取更新时间
     *
     * @return update_time - 更新时间
     */
    public Date getUpdateTime() {
        return updateTime;
    }

    /**
     * 设置更新时间
     *
     * @param updateTime 更新时间
     */
    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }
}
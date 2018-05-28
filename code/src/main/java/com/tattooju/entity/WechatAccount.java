package com.tattooju.entity;

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
     * 微信账号
     */
    @Column(name = "`wx_account`")
    private String wxAccount;

    /**
     * 角色 1）普通用户 2）管理员
     */
    @Column(name = "`role`")
    private Byte role;

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
     * 获取微信账号
     *
     * @return wx_account - 微信账号
     */
    public String getWxAccount() {
        return wxAccount;
    }

    /**
     * 设置微信账号
     *
     * @param wxAccount 微信账号
     */
    public void setWxAccount(String wxAccount) {
        this.wxAccount = wxAccount == null ? null : wxAccount.trim();
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
}
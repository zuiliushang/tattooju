package com.tattooju.entity;

import java.util.Date;
import javax.persistence.*;

@Table(name = "`tbl_reserve`")
public class Reserve {
    /**
     * 预约主键
     */
    @Id
    @Column(name = "`id`")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * 预约用户ID
     */
    @Column(name = "`account_id`")
    private Integer accountId;

    /**
     * 微信账号
     */
    @Column(name = "`wx_account`")
    private String wxAccount;

    /**
     * 手机号
     */
    @Column(name = "`mobile`")
    private String mobile;

    /**
     * 选择的部位 "," 隔开
     */
    @Column(name = "`body`")
    private String body;

    /**
     * 预约状态 0)已取消 1)已预约 2)已删除 3)已完成
     */
    @Column(name = "`status`")
    private Byte status;

    /**
     * 预约时间
     */
    @Column(name = "`reserve_time`")
    private Date reserveTime;

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
     * 内容
     */
    @Column(name = "`content`")
    private String content;

    /**
     * 获取预约主键
     *
     * @return id - 预约主键
     */
    public Integer getId() {
        return id;
    }

    /**
     * 设置预约主键
     *
     * @param id 预约主键
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * 获取预约用户ID
     *
     * @return account_id - 预约用户ID
     */
    public Integer getAccountId() {
        return accountId;
    }

    /**
     * 设置预约用户ID
     *
     * @param accountId 预约用户ID
     */
    public void setAccountId(Integer accountId) {
        this.accountId = accountId;
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
     * 获取手机号
     *
     * @return mobile - 手机号
     */
    public String getMobile() {
        return mobile;
    }

    /**
     * 设置手机号
     *
     * @param mobile 手机号
     */
    public void setMobile(String mobile) {
        this.mobile = mobile == null ? null : mobile.trim();
    }

    /**
     * 获取选择的部位 "," 隔开
     *
     * @return body - 选择的部位 "," 隔开
     */
    public String getBody() {
        return body;
    }

    /**
     * 设置选择的部位 "," 隔开
     *
     * @param body 选择的部位 "," 隔开
     */
    public void setBody(String body) {
        this.body = body == null ? null : body.trim();
    }

    /**
     * 获取预约状态 0)已取消 1)已预约 2)已删除 3)已完成
     *
     * @return status - 预约状态 0)已取消 1)已预约 2)已删除 3)已完成
     */
    public Byte getStatus() {
        return status;
    }

    /**
     * 设置预约状态 0)已取消 1)已预约 2)已删除 3)已完成
     *
     * @param status 预约状态 0)已取消 1)已预约 2)已删除 3)已完成
     */
    public void setStatus(Byte status) {
        this.status = status;
    }

    /**
     * 获取预约时间
     *
     * @return reserve_time - 预约时间
     */
    public Date getReserveTime() {
        return reserveTime;
    }

    /**
     * 设置预约时间
     *
     * @param reserveTime 预约时间
     */
    public void setReserveTime(Date reserveTime) {
        this.reserveTime = reserveTime;
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
}
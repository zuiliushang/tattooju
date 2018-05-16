/*
Navicat MySQL Data Transfer

Source Server         : localhost_3306
Source Server Version : 50721
Source Host           : 127.0.0.1:3306
Source Database       : tattooju

Target Server Type    : MYSQL
Target Server Version : 50721
File Encoding         : 65001

Date: 2018-05-16 18:21:24
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for `tbl_wechat_user`
-- ----------------------------
DROP TABLE IF EXISTS `tbl_wechat_user`;
CREATE TABLE `tbl_wechat_user` (
  `id` bigint(20) NOT NULL COMMENT '主键',
  `open_id` varchar(45) NOT NULL COMMENT 'openid',
  `nickname` tinytext COMMENT '名称',
  `gender` tinyint(4) DEFAULT '0' COMMENT '性别 0)未知 1)男 2)女',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='用户表';

-- ----------------------------
-- Records of tbl_wechat_user
-- ----------------------------

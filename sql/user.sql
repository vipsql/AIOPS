/*
 Navicat Premium Data Transfer

 Source Server         : myComputer
 Source Server Type    : MySQL
 Source Server Version : 80012
 Source Host           : localhost:3306
 Source Schema         : home_work

 Target Server Type    : MySQL
 Target Server Version : 80012
 File Encoding         : 65001

 Date: 25/07/2019 20:06:30
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for user
-- ----------------------------
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `phone` varchar(11) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT '用来接收警报的手机号/登录名',
  `password` varchar(64) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT '登录密码',
  `salt` varchar(16) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
  `logic` int(11) NOT NULL DEFAULT 0 COMMENT '删除标志',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_bin ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for user_message
-- ----------------------------
DROP TABLE IF EXISTS `user_message`;
CREATE TABLE `user_message`  (
  `id` int(11) NOT NULL,
  `nick_name` varchar(7) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT '用户名',
  `email` varchar(40) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT '用来接收警报的邮箱',
  `logic` int(11) DEFAULT 0 COMMENT '删除标志',
  PRIMARY KEY (`id`) USING BTREE,
  CONSTRAINT `user_message_ibfk_1` FOREIGN KEY (`id`) REFERENCES `user` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_bin ROW_FORMAT = Dynamic;

SET FOREIGN_KEY_CHECKS = 1;

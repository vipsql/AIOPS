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

 Date: 29/07/2019 09:58:50
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for task
-- ----------------------------
DROP TABLE IF EXISTS `task`;
CREATE TABLE `task`  (
  `task_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '任务 ID',
  `task_name` varchar(25) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT '任务名',
  `task_description` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT '任务描述',
  `task_cron` varchar(20) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL DEFAULT '0/15 * * * * ?' COMMENT '计划任务定时',
  `logic` int(11) NOT NULL DEFAULT 0 COMMENT '逻辑删除键',
  PRIMARY KEY (`task_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 14 CHARACTER SET = utf8 COLLATE = utf8_bin ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of task
-- ----------------------------
INSERT INTO `task` VALUES (1, 'getDateFromPrometheus', '从 Prometheus 获取指标数据', '0/15 * * * * ?', 0);

SET FOREIGN_KEY_CHECKS = 1;

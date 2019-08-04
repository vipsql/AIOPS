/*
SQLyog Ultimate v12.08 (64 bit)
MySQL - 5.6.44 : Database - aiops
*********************************************************************
*/

/*!40101 SET NAMES utf8 */;

/*!40101 SET SQL_MODE=''*/;

/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;
CREATE DATABASE /*!32312 IF NOT EXISTS*/`aiops` /*!40100 DEFAULT CHARACTER SET utf8 */;

USE `aiops`;

/*Table structure for table `log_api` */

DROP TABLE IF EXISTS `log_api`;

CREATE TABLE `log_api` (
  `id` bigint(64) NOT NULL COMMENT '编号',
  `tenant_code` varchar(12) DEFAULT '000000' COMMENT '租户编号',
  `service_id` varchar(32) DEFAULT NULL COMMENT '服务ID',
  `server_host` varchar(255) DEFAULT NULL COMMENT '服务器名',
  `server_ip` varchar(255) DEFAULT NULL COMMENT '服务器IP地址',
  `env` varchar(255) DEFAULT NULL COMMENT '服务器环境',
  `type` char(1) DEFAULT '1' COMMENT '日志类型',
  `title` varchar(255) DEFAULT '' COMMENT '日志标题',
  `method` varchar(10) DEFAULT NULL COMMENT '操作方式',
  `request_uri` varchar(255) DEFAULT NULL COMMENT '请求URI',
  `user_agent` varchar(1000) DEFAULT NULL COMMENT '用户代理',
  `remote_ip` varchar(255) DEFAULT NULL COMMENT '操作IP地址',
  `method_class` varchar(255) DEFAULT NULL COMMENT '方法类',
  `method_name` varchar(255) DEFAULT NULL COMMENT '方法名',
  `params` text COMMENT '操作提交的数据',
  `time` varchar(64) DEFAULT NULL COMMENT '执行时间',
  `create_by` varchar(64) DEFAULT NULL COMMENT '创建者',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

/*Data for the table `log_api` */

/*Table structure for table `log_error` */

DROP TABLE IF EXISTS `log_error`;

CREATE TABLE `log_error` (
  `id` bigint(64) NOT NULL COMMENT '编号',
  `tenant_code` varchar(12) DEFAULT '000000' COMMENT '租户编号',
  `service_id` varchar(32) DEFAULT NULL COMMENT '服务ID',
  `server_host` varchar(255) DEFAULT NULL COMMENT '服务器名',
  `server_ip` varchar(255) DEFAULT NULL COMMENT '服务器IP地址',
  `env` varchar(255) DEFAULT NULL COMMENT '系统环境',
  `method` varchar(10) DEFAULT NULL COMMENT '操作方式',
  `request_uri` varchar(255) DEFAULT NULL COMMENT '请求URI',
  `user_agent` varchar(1000) DEFAULT NULL COMMENT '用户代理',
  `stack_trace` text COMMENT '堆栈',
  `exception_name` varchar(255) DEFAULT NULL COMMENT '异常名',
  `message` text COMMENT '异常信息',
  `line_number` int(11) DEFAULT NULL COMMENT '错误行数',
  `method_class` varchar(255) DEFAULT NULL COMMENT '方法类',
  `file_name` varchar(1000) DEFAULT NULL COMMENT '文件名',
  `method_name` varchar(255) DEFAULT NULL COMMENT '方法名',
  `params` text COMMENT '操作提交的数据',
  `create_by` varchar(64) DEFAULT NULL COMMENT '创建者',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

/*Data for the table `log_error` */

/*Table structure for table `log_usual` */

DROP TABLE IF EXISTS `log_usual`;

CREATE TABLE `log_usual` (
  `id` bigint(64) NOT NULL COMMENT '编号',
  `tenant_code` varchar(12) DEFAULT '000000' COMMENT '租户编号',
  `service_id` varchar(32) DEFAULT NULL COMMENT '服务ID',
  `server_host` varchar(255) DEFAULT NULL COMMENT '服务器名',
  `server_ip` varchar(255) DEFAULT NULL COMMENT '服务器IP地址',
  `env` varchar(255) DEFAULT NULL COMMENT '系统环境',
  `log_level` varchar(10) DEFAULT NULL COMMENT '日志级别',
  `log_id` varchar(100) DEFAULT NULL COMMENT '日志业务id',
  `log_data` text COMMENT '日志数据',
  `method` varchar(10) DEFAULT NULL COMMENT '操作方式',
  `request_uri` varchar(255) DEFAULT NULL COMMENT '请求URI',
  `user_agent` varchar(1000) DEFAULT NULL COMMENT '用户代理',
  `params` text COMMENT '操作提交的数据',
  `create_by` varchar(64) DEFAULT NULL COMMENT '创建者',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

/*Data for the table `log_usual` */

/*Table structure for table `login_log` */

DROP TABLE IF EXISTS `login_log`;

CREATE TABLE `login_log` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `user_id` int(11) NOT NULL COMMENT '用户id',
  `type` int(11) NOT NULL COMMENT '日志类型:1登录 2登出',
  `description` varchar(255) DEFAULT NULL COMMENT '日志描述',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2045 DEFAULT CHARSET=utf8mb4 COMMENT='登录日志表';

/*Data for the table `login_log` */

/*Table structure for table `machine` */

DROP TABLE IF EXISTS `machine`;

CREATE TABLE `machine` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `ip` varchar(15) DEFAULT NULL,
  `port` int(10) DEFAULT NULL,
  `user_id` bigint(20) DEFAULT NULL,
  `metrics` varchar(200) DEFAULT NULL,
  `create_time` datetime DEFAULT NULL,
  `update_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4;

/*Data for the table `machine` */

insert  into `machine`(`id`,`ip`,`port`,`user_id`,`metrics`,`create_time`,`update_time`) values (1,'47.107.139.6',8087,1,NULL,'2019-07-31 11:10:32','2019-07-31 11:10:37'),(3,'47.107.139.6',8087,1,NULL,NULL,NULL);

/*Table structure for table `msg_log` */

DROP TABLE IF EXISTS `msg_log`;

CREATE TABLE `msg_log` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '消息唯一标识',
  `msg` text COMMENT '消息体, json格式化',
  `exchange` varchar(255) NOT NULL DEFAULT '' COMMENT '交换机',
  `routing_key` varchar(255) NOT NULL DEFAULT '' COMMENT '路由键',
  `status` int(11) NOT NULL DEFAULT '0' COMMENT '状态: 0投递中 1投递成功 2投递失败 3已消费',
  `try_count` int(11) NOT NULL DEFAULT '0' COMMENT '重试次数',
  `next_try_time` datetime DEFAULT NULL COMMENT '下一次重试时间',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `unq_id` (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=20 DEFAULT CHARSET=utf8mb4 COMMENT='消息投递日志';

/*Data for the table `msg_log` */

insert  into `msg_log`(`id`,`msg`,`exchange`,`routing_key`,`status`,`try_count`,`next_try_time`,`create_time`,`update_time`) values (18,'{\"to\":\"13058142866@163.com\",\"title\":\"string\",\"content\":\"string\",\"msgId\":null}','mail.exchange','mail.routing.key',3,1,'2019-07-30 09:11:50','2019-07-30 08:28:11','2019-07-30 09:11:53'),(19,'{\"to\":\"13058142866@163.com\",\"title\":\"string\",\"content\":\"string\",\"msgId\":null}','mail.exchange','mail.routing.key',3,1,'2019-07-30 09:11:52','2019-07-30 08:38:24','2019-07-30 09:11:54');

/*Table structure for table `task` */

DROP TABLE IF EXISTS `task`;

CREATE TABLE `task` (
  `task_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '任务 ID',
  `task_name` varchar(25) COLLATE utf8_bin NOT NULL COMMENT '任务名',
  `task_description` varchar(255) COLLATE utf8_bin NOT NULL COMMENT '任务描述',
  `task_cron` varchar(20) COLLATE utf8_bin NOT NULL DEFAULT '0/15 * * * * ?' COMMENT '计划任务定时',
  `logic` int(11) NOT NULL DEFAULT '0' COMMENT '逻辑删除键',
  `query_metric` varchar(50) COLLATE utf8_bin NOT NULL,
  `create_user_id` bigint(20) DEFAULT NULL,
  `args` varchar(150) COLLATE utf8_bin DEFAULT NULL,
  `status` tinyint(11) DEFAULT NULL,
  `machine_id` bigint(20) DEFAULT NULL,
  `create_time` datetime DEFAULT NULL,
  `update_time` datetime DEFAULT NULL,
  `type` int(11) DEFAULT NULL,
  PRIMARY KEY (`task_id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=26 DEFAULT CHARSET=utf8 COLLATE=utf8_bin ROW_FORMAT=DYNAMIC;

/*Data for the table `task` */

insert  into `task`(`task_id`,`task_name`,`task_description`,`task_cron`,`logic`,`query_metric`,`create_user_id`,`args`,`status`,`machine_id`,`create_time`,`update_time`,`type`) values (1,'getDateFromPrometheus','从 Prometheus 获取指标数据','0/15 * * * * ?',0,'',NULL,NULL,0,NULL,NULL,NULL,NULL),(14,'string','string','0/15 * * * * ?',1,'http请求量',NULL,'{\"query\":\"http_requests_total\",\"timeout\":0}',0,NULL,NULL,NULL,NULL),(15,'string','string','0/15 * * * * ?',1,'http请求量',NULL,'{\"query\":\"http_requests_total\",\"timeout\":1000}',0,NULL,NULL,NULL,NULL),(16,'string','string','0/18 * * * * ?',0,'string',NULL,'{\"end\":1564660799000,\"query\":\"http_requests_total\",\"start\":1564657199000,\"step\":15}',0,NULL,NULL,NULL,NULL),(17,'string','string','0/17 * * * * ?',1,'http请求量',NULL,'{\"query\":\"http_requests_total\",\"timeout\":1000}',0,NULL,NULL,NULL,NULL),(18,'string','string','0/17 * * * * ?',1,'http请求量',NULL,'{\"query\":\"http_requests_total\",\"timeout\":1000}',0,NULL,NULL,NULL,0),(19,'string','string','0/17 * * * * ?',0,'http请求量',NULL,'{\"query\":\"http_requests_total\",\"timeout\":1000}',0,NULL,NULL,NULL,0),(20,'string','string','0/17 * * * * ?',0,'http请求量',NULL,'{\"query\":\"http_requests_total\",\"timeout\":1000}',0,NULL,NULL,NULL,0),(21,'string','string','0/17 * * * * ?',0,'string',NULL,'{\"end\":1564631999000,\"query\":\"http_requests_total\",\"start\":1564628399000,\"step\":15}',0,NULL,NULL,NULL,0),(22,'string','string','0/18 * * * * ?',0,'string',NULL,'{\"end\":1564631999000,\"query\":\"http_requests_total\",\"start\":1564628399000,\"step\":15}',0,NULL,NULL,NULL,0),(23,'string','string','0/18 * * * * ?',0,'string',NULL,'{\"end\":1564631999000,\"query\":\"http_requests_total\",\"start\":1564628399000,\"step\":15}',0,NULL,NULL,NULL,0),(24,'string','string','0/18 * * * * ?',0,'string',NULL,'{\"end\":1564631999000,\"query\":\"http_requests_total\",\"start\":1564628399000,\"step\":15}',0,NULL,NULL,NULL,0),(25,'string','string','0/18 * * * * ?',0,'string',NULL,'{\"end\":1564631999000,\"query\":\"http_requests_total\",\"start\":1564628399000,\"step\":15}',0,NULL,NULL,NULL,0);

/*Table structure for table `user` */

DROP TABLE IF EXISTS `user`;

CREATE TABLE `user` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `mobile` varchar(11) COLLATE utf8_bin NOT NULL COMMENT '用来接收警报的手机号/登录名',
  `password` varchar(64) COLLATE utf8_bin NOT NULL COMMENT '登录密码',
  `salt` varchar(16) COLLATE utf8_bin DEFAULT NULL,
  `logic` int(11) DEFAULT '0' COMMENT '删除标志',
  `account` varchar(25) COLLATE utf8_bin DEFAULT NULL COMMENT '登录账号',
  `company` varchar(25) COLLATE utf8_bin DEFAULT NULL,
  `department` varchar(25) COLLATE utf8_bin DEFAULT NULL,
  `department_group` varchar(25) COLLATE utf8_bin DEFAULT NULL,
  `organization` varchar(25) COLLATE utf8_bin DEFAULT NULL,
  `mail` varchar(25) COLLATE utf8_bin DEFAULT NULL,
  `name` varchar(25) COLLATE utf8_bin DEFAULT NULL,
  `create_time` datetime DEFAULT NULL,
  `update_time` datetime DEFAULT NULL,
  `version` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `mail` (`mail`)
) ENGINE=InnoDB AUTO_INCREMENT=39 DEFAULT CHARSET=utf8 COLLATE=utf8_bin ROW_FORMAT=DYNAMIC;

/*Data for the table `user` */

insert  into `user`(`id`,`mobile`,`password`,`salt`,`logic`,`account`,`company`,`department`,`department_group`,`organization`,`mail`,`name`,`create_time`,`update_time`,`version`) values (1,'1','90b9aa7e25f80cf4f64e990b78a9fc5ebd6cecad','1',0,'admin',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL),(21,'15623790232','admin',NULL,0,'张宏浩','酷开公司','云技术部','技术开发组','媒资管理','zhanghonghao@coocaa.com','张宏浩','2019-07-31 02:37:43',NULL,NULL);

/*Table structure for table `user_message` */

DROP TABLE IF EXISTS `user_message`;

CREATE TABLE `user_message` (
  `id` int(11) NOT NULL,
  `nick_name` varchar(7) COLLATE utf8_bin NOT NULL COMMENT '用户名',
  `email` varchar(40) COLLATE utf8_bin NOT NULL COMMENT '用来接收警报的邮箱',
  `logic` int(11) DEFAULT '0' COMMENT '删除标志',
  PRIMARY KEY (`id`) USING BTREE,
  CONSTRAINT `user_message_ibfk_1` FOREIGN KEY (`id`) REFERENCES `user` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin ROW_FORMAT=DYNAMIC;

/*Data for the table `user_message` */

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

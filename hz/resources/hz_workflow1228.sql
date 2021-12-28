/*
Navicat MySQL Data Transfer

Source Server         : local-mysql
Source Server Version : 50505
Source Host           : localhost:3306
Source Database       : hz_workflow

Target Server Type    : MYSQL
Target Server Version : 50505
File Encoding         : 65001

Date: 2021-12-28 09:13:22
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for `bizdata`
-- ----------------------------
DROP TABLE IF EXISTS `bizdata`;
CREATE TABLE `bizdata` (
  `processId` varchar(10) NOT NULL COMMENT '流程实例id',
  `bizName` varchar(20) NOT NULL DEFAULT '',
  `title` varchar(200) DEFAULT NULL,
  `createBy` varchar(10) NOT NULL,
  `createTime` datetime NOT NULL,
  `createformdatajson` text DEFAULT NULL,
  `status` varchar(10) DEFAULT '' COMMENT '枚举:BizDataStatus:未开始,处理中,已结束',
  PRIMARY KEY (`processId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Records of bizdata
-- ----------------------------

-- ----------------------------
-- Table structure for `bizdataex`
-- ----------------------------
DROP TABLE IF EXISTS `bizdataex`;
CREATE TABLE `bizdataex` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `processId` varchar(10) NOT NULL,
  `eKey` varchar(10) NOT NULL,
  `eValue` varchar(1000) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Records of bizdataex
-- ----------------------------

-- ----------------------------
-- Table structure for `bizdatasub`
-- ----------------------------
DROP TABLE IF EXISTS `bizdatasub`;
CREATE TABLE `bizdatasub` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `processId` varchar(10) NOT NULL COMMENT '流程实例id',
  `taskId` varchar(10) NOT NULL,
  `assignee` varchar(20) NOT NULL,
  `assigneeCompleted` varchar(10) NOT NULL COMMENT '枚举:TaskStatus:处理中,已完成',
  `completeformdatajson` text DEFAULT NULL,
  `status` varchar(10) NOT NULL COMMENT '枚举:TaskStatus:处理中,已完成',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Records of bizdatasub
-- ----------------------------

-- ----------------------------
-- Table structure for `dict`
-- ----------------------------
DROP TABLE IF EXISTS `dict`;
CREATE TABLE `dict` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `dk` varchar(20) NOT NULL,
  `dv` varchar(1000) NOT NULL,
  `category` int(11) NOT NULL,
  `pid` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Records of dict
-- ----------------------------

-- ----------------------------
-- Table structure for `procdefex`
-- ----------------------------
DROP TABLE IF EXISTS `procdefex`;
CREATE TABLE `procdefex` (
  `procdefKey` varchar(20) NOT NULL,
  `bizName` varchar(20) NOT NULL,
  `ico` varchar(100) DEFAULT NULL,
  `createformId` int(11) DEFAULT NULL,
  `approveformId` int(11) DEFAULT NULL,
  PRIMARY KEY (`procdefKey`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Records of procdefex
-- ----------------------------

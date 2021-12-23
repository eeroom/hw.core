/*
Navicat MySQL Data Transfer

Source Server         : local-mysql
Source Server Version : 50505
Source Host           : localhost:3306
Source Database       : workflowsf

Target Server Type    : MYSQL
Target Server Version : 50505
File Encoding         : 65001

Date: 2021-12-23 22:21:36
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for `bizdata`
-- ----------------------------
DROP TABLE IF EXISTS `bizdata`;
CREATE TABLE `bizdata` (
  `processId` varchar(10) NOT NULL COMMENT '流程实例id',
  `bizType` int(11) NOT NULL,
  `title` varchar(200) DEFAULT NULL,
  `createBy` varchar(10) NOT NULL,
  `createTime` datetime NOT NULL,
  `createformdatajson` text DEFAULT NULL,
  `status` int(11) DEFAULT NULL COMMENT '0处理中 1已完成',
  PRIMARY KEY (`processId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Records of bizdata
-- ----------------------------
INSERT INTO `bizdata` VALUES ('201', '1', null, 'hz', '2021-12-23 19:49:59', '{\"sender\":\"dcq\",\"senderCityId\":33,\"thirdpartId\":\"hz\",\"reciver\":\"wch\",\"reciveCityId\":11}', '0');
INSERT INTO `bizdata` VALUES ('501', '1', null, 'hz', '2021-12-23 20:15:34', '{\"sender\":\"邓常青\",\"senderCityId\":59,\"thirdpartId\":\"hz\",\"reciver\":\"吴传花\",\"reciveCityId\":106}', '0');

-- ----------------------------
-- Table structure for `bizdatasub`
-- ----------------------------
DROP TABLE IF EXISTS `bizdatasub`;
CREATE TABLE `bizdatasub` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `processId` varchar(10) NOT NULL COMMENT '流程实例id',
  `taskId` varchar(10) NOT NULL,
  `handlerId` varchar(20) NOT NULL,
  `handlerByMe` int(11) NOT NULL COMMENT '0-未处理 1-通过 2-驳回 3-转审',
  `completeformdatajson` text DEFAULT NULL,
  `taskstatus` int(11) NOT NULL COMMENT '0-正常 1-已关闭',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Records of bizdatasub
-- ----------------------------
INSERT INTO `bizdatasub` VALUES ('1', '201', '210', 'dcq', '0', null, '1');
INSERT INTO `bizdatasub` VALUES ('2', '201', '303', 'guobang', '0', null, '1');
INSERT INTO `bizdatasub` VALUES ('3', '201', '406', 'hz', '0', null, '0');
INSERT INTO `bizdatasub` VALUES ('4', '501', '510', '邓常青', '0', '{\"additionalProp1\":1,\"additionalProp3\":3,\"additionalProp2\":2}', '1');
INSERT INTO `bizdatasub` VALUES ('5', '501', '515', 'guobang', '0', '{\"zhongliang\":4399}', '1');
INSERT INTO `bizdatasub` VALUES ('6', '501', '522', 'hz', '1', '{\"processInstanceId\":\"501\",\"payType\":\"支付宝\",\"moneyCount\":400,\"thirdpartId\":\"hz\"}', '1');

-- ----------------------------
-- Table structure for `biztype`
-- ----------------------------
DROP TABLE IF EXISTS `biztype`;
CREATE TABLE `biztype` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `camundaKey` varchar(20) NOT NULL,
  `name` varchar(20) NOT NULL,
  `ico` varchar(100) DEFAULT NULL,
  `createformId` int(11) DEFAULT NULL,
  `approveformId` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Records of biztype
-- ----------------------------
INSERT INTO `biztype` VALUES ('1', 'Process_17gm53d', '国内快件', 'ico-kuaidi', '100', '200');

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
-- Table structure for `jijiancustomer`
-- ----------------------------
DROP TABLE IF EXISTS `jijiancustomer`;
CREATE TABLE `jijiancustomer` (
  `id` varchar(255) NOT NULL,
  `name` varchar(20) NOT NULL,
  `callbackurl` varchar(1000) NOT NULL,
  `hasvip` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Records of jijiancustomer
-- ----------------------------
INSERT INTO `jijiancustomer` VALUES ('hz', '暴风城', 'http://localhost:8084/kuaidi/notice', '0');

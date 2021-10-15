/*
 Navicat Premium Data Transfer

 Source Server         : 172.26.9.2
 Source Server Type    : MySQL
 Source Server Version : 80016
 Source Host           : 172.26.9.2:3306
 Source Schema         : u_dts

 Target Server Type    : MySQL
 Target Server Version : 80016
 File Encoding         : 65001

 Date: 15/10/2021 13:53:40
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for u_entity
-- ----------------------------
DROP TABLE IF EXISTS `u_entity`;
CREATE TABLE `u_entity`  (
  `id` int(20) NOT NULL AUTO_INCREMENT COMMENT '主键 ',
  `name` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '数据源信息名称',
  `type` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '数据库类型，默认是MYSQL类型',
  `encode` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '编码方式',
  `slave_id` bigint(20) NOT NULL,
  `url` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT 'jdbc url',
  `driver` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT 'driver  eg: com.mysql.cj.jdbc.Driver',
  `username` varchar(30) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '数据库用户名',
  `password` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '数据库密码',
  `mysql` json NULL COMMENT 'mysql的特有属性',
  `modify_time` datetime(0) NOT NULL COMMENT '更新时间',
  `create_time` datetime(0) NOT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `u_entity_UN`(`slave_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for u_pair
-- ----------------------------
DROP TABLE IF EXISTS `u_pair`;
CREATE TABLE `u_pair`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `pipeline_id` int(11) NOT NULL,
  `source_datamedia_id` int(11) NOT NULL COMMENT 'u_source_datamedia',
  `target_datamedia_id` int(11) NOT NULL COMMENT 'u_target_datamedia',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `pid_sdid_tdid_unique_index`(`pipeline_id`, `source_datamedia_id`, `target_datamedia_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for u_pipeline
-- ----------------------------
DROP TABLE IF EXISTS `u_pipeline`;
CREATE TABLE `u_pipeline`  (
  `id` int(20) NOT NULL COMMENT '主键 ',
  `task_id` int(11) NOT NULL,
  `name` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT 'pipeline 名称',
  `source_entity_id` int(11) NOT NULL COMMENT 'select的实体',
  `target_entity_id` int(11) NOT NULL COMMENT 'load的实体',
  `pipeline_params` json NOT NULL COMMENT 'pipeline 参数',
  `modify_time` datetime(0) NOT NULL COMMENT '更新时间',
  `create_time` datetime(0) NOT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `task_id_index`(`task_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for u_region
-- ----------------------------
DROP TABLE IF EXISTS `u_region`;
CREATE TABLE `u_region`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `region` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT 'region',
  `mode` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '模式select，load',
  `pipeline_id` int(11) NOT NULL COMMENT 'u_pipeline主键id',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `pipeline_id_index`(`pipeline_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for u_source_datamedia
-- ----------------------------
DROP TABLE IF EXISTS `u_source_datamedia`;
CREATE TABLE `u_source_datamedia`  (
  `id` int(10) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `name` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '目标映射端名称',
  `namespace` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT 'datasouce映射库',
  `table` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT 'datasouce映射表',
  `create_time` datetime(0) NOT NULL COMMENT '创建时间',
  `modify_time` datetime(0) NOT NULL COMMENT '修改时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for u_target_datamedia
-- ----------------------------
DROP TABLE IF EXISTS `u_target_datamedia`;
CREATE TABLE `u_target_datamedia`  (
  `id` int(10) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `name` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '目标映射端名称',
  `namespace` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT 'datasouce映射库',
  `table` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT 'datasouce映射表',
  `rule` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '同步规则',
  `create_time` datetime(0) NOT NULL COMMENT '创建时间',
  `modify_time` datetime(0) NOT NULL COMMENT '修改时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for u_task
-- ----------------------------
DROP TABLE IF EXISTS `u_task`;
CREATE TABLE `u_task`  (
  `id` int(20) NOT NULL AUTO_INCREMENT COMMENT '主键 ',
  `name` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '任务id 名称',
  `valid` tinyint(1) NOT NULL DEFAULT 1 COMMENT '任务停用启用,0 停用，1:启用,默认1',
  `modify_time` datetime(0) NOT NULL COMMENT '更新时间',
  `create_time` datetime(0) NOT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

SET FOREIGN_KEY_CHECKS = 1;

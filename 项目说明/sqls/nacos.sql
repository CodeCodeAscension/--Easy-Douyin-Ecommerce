/*
 Navicat Premium Dump SQL

 Source Server         : Ubuntu虚拟机的MySql
 Source Server Type    : MySQL
 Source Server Version : 80036 (8.0.36)
 Source Host           : vlsmb-nagamori.local:3306
 Source Schema         : nacos

 Target Server Type    : MySQL
 Target Server Version : 80036 (8.0.36)
 File Encoding         : 65001

 Date: 12/02/2025 21:37:09
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for config_info
-- ----------------------------
DROP TABLE IF EXISTS `config_info`;
CREATE TABLE `config_info`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT 'id',
  `data_id` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NOT NULL COMMENT 'data_id',
  `group_id` varchar(128) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NULL DEFAULT NULL,
  `content` longtext CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NOT NULL COMMENT 'content',
  `md5` varchar(32) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NULL DEFAULT NULL COMMENT 'md5',
  `gmt_create` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `gmt_modified` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '修改时间',
  `src_user` text CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NULL COMMENT 'source user',
  `src_ip` varchar(50) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NULL DEFAULT NULL COMMENT 'source ip',
  `app_name` varchar(128) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NULL DEFAULT NULL,
  `tenant_id` varchar(128) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NULL DEFAULT '' COMMENT '租户字段',
  `c_desc` varchar(256) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NULL DEFAULT NULL,
  `c_use` varchar(64) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NULL DEFAULT NULL,
  `effect` varchar(64) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NULL DEFAULT NULL,
  `type` varchar(64) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NULL DEFAULT NULL,
  `c_schema` text CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NULL,
  `encrypted_data_key` text CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NOT NULL COMMENT '秘钥',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uk_configinfo_datagrouptenant`(`data_id` ASC, `group_id` ASC, `tenant_id` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 25 CHARACTER SET = utf8mb3 COLLATE = utf8mb3_bin COMMENT = 'config_info' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of config_info
-- ----------------------------
INSERT INTO `config_info` VALUES (2, 'jdbc-config.yaml', 'DEFAULT_GROUP', 'spring:\n  datasource:\n    url: jdbc:mysql://${ecommerce.database.host}:${ecommerce.database.port}/${ecommerce.database.table}?useUnicode=true&characterEncoding=UTF-8&autoReconnect=true&serverTimezone=Asia/Shanghai\n    username: ${ecommerce.database.user}\n    password: ${ecommerce.database.pwd}\n    driver-class-name: com.mysql.cj.jdbc.Driver\n\nmybatis-plus:\n  configuration:\n    default-enum-type-handler: com.baomidou.mybatisplus.core.handlers.MybatisEnumTypeHandler\n    cache-enabled: true\n  global-config:\n    db-config:\n      update-strategy: not_null\n      id-type: assign_id', 'f340a1fff232a8f245a54e397cf05a0a', '2025-02-09 09:55:02', '2025-02-11 02:32:13', 'nacos', '172.18.0.1', '', '', '数据库连接以及MyBatis的配置', '', '', 'yaml', '', '');
INSERT INTO `config_info` VALUES (3, 'log-config.yaml', 'DEFAULT_GROUP', 'logging:\r\n  level:\r\n    com.example: debug\r\n  pattern:\r\n    dateformat: HH:mm:ss:SSS\r\n  file:\r\n    path: \"logs/${spring.application.name}\"', 'cff25120c22850da8594bc84585580ba', '2025-02-09 09:57:36', '2025-02-09 09:57:36', NULL, '172.18.0.1', '', '', '日志配置', NULL, NULL, 'yaml', NULL, '');
INSERT INTO `config_info` VALUES (4, 'knife4j-config.yaml', 'DEFAULT_GROUP', 'knife4j:\n  enable: true\n  openapi:\n    title: ${ecommerce.knife4j.title:标题}\n    description: ${ecommerce.knife4j.description:描述}\n    base-package: com.example', 'fa87b35f725b8c715906447ae8e1df71', '2025-02-09 09:59:42', '2025-02-09 19:21:54', 'nacos', '172.18.0.1', '', '', 'knife4j配置', '', '', 'yaml', '', '');
INSERT INTO `config_info` VALUES (5, 'feign-config.yaml', 'DEFAULT_GROUP', 'feign:\n  okhttp:\n    enabled: true\n  sentinel:\n    enabled: true', '8d8ea2b480dc2fa68b69ab9d89ae6060', '2025-02-09 10:00:20', '2025-02-09 12:42:12', 'nacos', '172.18.0.1', '', '', 'feign配置', '', '', 'yaml', '', '');
INSERT INTO `config_info` VALUES (7, 'sentinel-config.yaml', 'DEFAULT_GROUP', 'spring:\r\n  cloud:\r\n    sentinel:\r\n      transport:\r\n        dashboard: ${ecommerce.sentinel.dashboard}\r\n      http-method-specify: true', '0ed3e5ebcd4e8845744b121c9cbd078a', '2025-02-09 12:40:06', '2025-02-09 12:40:06', NULL, '172.18.0.1', '', '', 'sentinel相关配置', NULL, NULL, 'yaml', NULL, '');
INSERT INTO `config_info` VALUES (8, 'seata-config.yaml', 'DEFAULT_GROUP', 'seata:\n  enabled: true\n  data-source-proxy-mode: AT\n  registry:\n    type: nacos\n    nacos:\n      server-addr: ${ecommerce.nacos.dashboard}\n      # namespace: \"\"\n      group: DEFAULT_GROUP\n      application: seata-server\n      username: nacos\n      password: nacos\n  tx-service-group: ecommerce\n  service:\n    vgroup-mapping:\n      ecommerce: default\n\n', '93fd902d906bcfe185c1715deaf93e21', '2025-02-09 19:05:46', '2025-02-09 23:54:05', 'nacos', '172.18.0.1', '', '', '', '', '', 'yaml', '', '');
INSERT INTO `config_info` VALUES (21, 'redis-config.yaml', 'DEFAULT_GROUP', 'spring:\r\n  redis:\r\n    cluster:\r\n      nodes: ${ecommerce.redis.cluster-addr}\r\n    lettuce:\r\n      pool:\r\n        max-active: 8\r\n        max-idle: 8\r\n        min-idle: 0', 'b02ac41a76311e73bb0e9c7d787ab338', '2025-02-11 02:16:08', '2025-02-11 02:16:08', NULL, '172.18.0.1', '', '', 'redis集群的配置', NULL, NULL, 'yaml', NULL, '');
INSERT INTO `config_info` VALUES (23, 'rabbitmq-config.yaml', 'DEFAULT_GROUP', 'spring:\r\n  rabbitmq:\r\n    host: ${ecommerce.rabbitmq.host}\r\n    port: ${ecommerce.rabbitmq.port}\r\n    virtual-host: ${ecommerce.rabbitmq.virtual}\r\n    username: ${ecommerce.rabbitmq.user}\r\n    password: ${ecommerce.rabbitmq.pwd}\r\n    listener:\r\n      simple:\r\n        prefetch: 1', '635547cd03b7aa15ca171a760e87209a', '2025-02-12 21:08:10', '2025-02-12 21:08:10', NULL, '172.19.0.1', '', '', 'rabbitmq的配置信息', NULL, NULL, 'yaml', NULL, '');

-- ----------------------------
-- Table structure for config_info_aggr
-- ----------------------------
DROP TABLE IF EXISTS `config_info_aggr`;
CREATE TABLE `config_info_aggr`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT 'id',
  `data_id` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NOT NULL COMMENT 'data_id',
  `group_id` varchar(128) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NOT NULL COMMENT 'group_id',
  `datum_id` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NOT NULL COMMENT 'datum_id',
  `content` longtext CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NOT NULL COMMENT '内容',
  `gmt_modified` datetime NOT NULL COMMENT '修改时间',
  `app_name` varchar(128) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NULL DEFAULT NULL,
  `tenant_id` varchar(128) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NULL DEFAULT '' COMMENT '租户字段',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uk_configinfoaggr_datagrouptenantdatum`(`data_id` ASC, `group_id` ASC, `tenant_id` ASC, `datum_id` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb3 COLLATE = utf8mb3_bin COMMENT = '增加租户字段' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of config_info_aggr
-- ----------------------------

-- ----------------------------
-- Table structure for config_info_beta
-- ----------------------------
DROP TABLE IF EXISTS `config_info_beta`;
CREATE TABLE `config_info_beta`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT 'id',
  `data_id` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NOT NULL COMMENT 'data_id',
  `group_id` varchar(128) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NOT NULL COMMENT 'group_id',
  `app_name` varchar(128) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NULL DEFAULT NULL COMMENT 'app_name',
  `content` longtext CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NOT NULL COMMENT 'content',
  `beta_ips` varchar(1024) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NULL DEFAULT NULL COMMENT 'betaIps',
  `md5` varchar(32) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NULL DEFAULT NULL COMMENT 'md5',
  `gmt_create` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `gmt_modified` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '修改时间',
  `src_user` text CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NULL COMMENT 'source user',
  `src_ip` varchar(50) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NULL DEFAULT NULL COMMENT 'source ip',
  `tenant_id` varchar(128) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NULL DEFAULT '' COMMENT '租户字段',
  `encrypted_data_key` text CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NOT NULL COMMENT '秘钥',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uk_configinfobeta_datagrouptenant`(`data_id` ASC, `group_id` ASC, `tenant_id` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb3 COLLATE = utf8mb3_bin COMMENT = 'config_info_beta' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of config_info_beta
-- ----------------------------

-- ----------------------------
-- Table structure for config_info_tag
-- ----------------------------
DROP TABLE IF EXISTS `config_info_tag`;
CREATE TABLE `config_info_tag`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT 'id',
  `data_id` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NOT NULL COMMENT 'data_id',
  `group_id` varchar(128) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NOT NULL COMMENT 'group_id',
  `tenant_id` varchar(128) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NULL DEFAULT '' COMMENT 'tenant_id',
  `tag_id` varchar(128) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NOT NULL COMMENT 'tag_id',
  `app_name` varchar(128) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NULL DEFAULT NULL COMMENT 'app_name',
  `content` longtext CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NOT NULL COMMENT 'content',
  `md5` varchar(32) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NULL DEFAULT NULL COMMENT 'md5',
  `gmt_create` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `gmt_modified` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '修改时间',
  `src_user` text CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NULL COMMENT 'source user',
  `src_ip` varchar(50) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NULL DEFAULT NULL COMMENT 'source ip',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uk_configinfotag_datagrouptenanttag`(`data_id` ASC, `group_id` ASC, `tenant_id` ASC, `tag_id` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb3 COLLATE = utf8mb3_bin COMMENT = 'config_info_tag' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of config_info_tag
-- ----------------------------

-- ----------------------------
-- Table structure for config_tags_relation
-- ----------------------------
DROP TABLE IF EXISTS `config_tags_relation`;
CREATE TABLE `config_tags_relation`  (
  `id` bigint NOT NULL COMMENT 'id',
  `tag_name` varchar(128) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NOT NULL COMMENT 'tag_name',
  `tag_type` varchar(64) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NULL DEFAULT NULL COMMENT 'tag_type',
  `data_id` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NOT NULL COMMENT 'data_id',
  `group_id` varchar(128) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NOT NULL COMMENT 'group_id',
  `tenant_id` varchar(128) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NULL DEFAULT '' COMMENT 'tenant_id',
  `nid` bigint NOT NULL AUTO_INCREMENT,
  PRIMARY KEY (`nid`) USING BTREE,
  UNIQUE INDEX `uk_configtagrelation_configidtag`(`id` ASC, `tag_name` ASC, `tag_type` ASC) USING BTREE,
  INDEX `idx_tenant_id`(`tenant_id` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb3 COLLATE = utf8mb3_bin COMMENT = 'config_tag_relation' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of config_tags_relation
-- ----------------------------

-- ----------------------------
-- Table structure for group_capacity
-- ----------------------------
DROP TABLE IF EXISTS `group_capacity`;
CREATE TABLE `group_capacity`  (
  `id` bigint UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `group_id` varchar(128) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NOT NULL DEFAULT '' COMMENT 'Group ID，空字符表示整个集群',
  `quota` int UNSIGNED NOT NULL DEFAULT 0 COMMENT '配额，0表示使用默认值',
  `usage` int UNSIGNED NOT NULL DEFAULT 0 COMMENT '使用量',
  `max_size` int UNSIGNED NOT NULL DEFAULT 0 COMMENT '单个配置大小上限，单位为字节，0表示使用默认值',
  `max_aggr_count` int UNSIGNED NOT NULL DEFAULT 0 COMMENT '聚合子配置最大个数，，0表示使用默认值',
  `max_aggr_size` int UNSIGNED NOT NULL DEFAULT 0 COMMENT '单个聚合数据的子配置大小上限，单位为字节，0表示使用默认值',
  `max_history_count` int UNSIGNED NOT NULL DEFAULT 0 COMMENT '最大变更历史数量',
  `gmt_create` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `gmt_modified` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '修改时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uk_group_id`(`group_id` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb3 COLLATE = utf8mb3_bin COMMENT = '集群、各Group容量信息表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of group_capacity
-- ----------------------------

-- ----------------------------
-- Table structure for his_config_info
-- ----------------------------
DROP TABLE IF EXISTS `his_config_info`;
CREATE TABLE `his_config_info`  (
  `id` bigint UNSIGNED NOT NULL,
  `nid` bigint UNSIGNED NOT NULL AUTO_INCREMENT,
  `data_id` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NOT NULL,
  `group_id` varchar(128) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NOT NULL,
  `app_name` varchar(128) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NULL DEFAULT NULL COMMENT 'app_name',
  `content` longtext CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NOT NULL,
  `md5` varchar(32) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NULL DEFAULT NULL,
  `gmt_create` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `gmt_modified` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `src_user` text CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NULL,
  `src_ip` varchar(50) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NULL DEFAULT NULL,
  `op_type` char(10) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NULL DEFAULT NULL,
  `tenant_id` varchar(128) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NULL DEFAULT '' COMMENT '租户字段',
  `encrypted_data_key` text CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NOT NULL COMMENT '秘钥',
  PRIMARY KEY (`nid`) USING BTREE,
  INDEX `idx_gmt_create`(`gmt_create` ASC) USING BTREE,
  INDEX `idx_gmt_modified`(`gmt_modified` ASC) USING BTREE,
  INDEX `idx_did`(`data_id` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 28 CHARACTER SET = utf8mb3 COLLATE = utf8mb3_bin COMMENT = '多租户改造' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of his_config_info
-- ----------------------------
INSERT INTO `his_config_info` VALUES (0, 1, 'vlsmb-test.yaml', 'DEFAULT_GROUP', '', 'vlsmb:\r\n  config:\r\n    title: VLSMB', '550408744085e5fe6f827d077a34699c', '2025-02-09 09:40:16', '2025-02-09 09:40:17', NULL, '172.18.0.1', 'I', '', '');
INSERT INTO `his_config_info` VALUES (1, 2, 'vlsmb-test.yaml', 'DEFAULT_GROUP', '', 'vlsmb:\r\n  config:\r\n    title: VLSMB', '550408744085e5fe6f827d077a34699c', '2025-02-09 09:47:27', '2025-02-09 09:47:28', NULL, '172.18.0.1', 'D', '', '');
INSERT INTO `his_config_info` VALUES (0, 3, 'jdbc-config.yaml', 'DEFAULT_GROUP', '', 'spring:\r\n  datasource:\r\n  url: jdbc:mysql://${ecommerce.database.host}:${ecommerce.database.port}/${ecommerce.database.table}?useUnicode=true&characterEncoding=UTF-8&autoReconnect=true&serverTimezone=Asia/Shanghai\r\n  username: ${ecommerce.database.user}\r\n  password: ${ecommerce.database.pwd}\r\n  driver-class-name: com.mysql.cj.jdbc.Driver\r\n\r\nmybatis-plus:\r\n  configuration:\r\n    default-enum-type-handler: com.baomidou.mybatisplus.core.handlers.MybatisEnumTypeHandler\r\n  global-config:\r\n    db-config:\r\n      update-strategy: not_null\r\n      id-type: assign_id', '577220a4bb31a81457a3de5c6f15c7fc', '2025-02-09 09:55:01', '2025-02-09 09:55:02', NULL, '172.18.0.1', 'I', '', '');
INSERT INTO `his_config_info` VALUES (0, 4, 'log-config.yaml', 'DEFAULT_GROUP', '', 'logging:\r\n  level:\r\n    com.example: debug\r\n  pattern:\r\n    dateformat: HH:mm:ss:SSS\r\n  file:\r\n    path: \"logs/${spring.application.name}\"', 'cff25120c22850da8594bc84585580ba', '2025-02-09 09:57:36', '2025-02-09 09:57:36', NULL, '172.18.0.1', 'I', '', '');
INSERT INTO `his_config_info` VALUES (0, 5, 'knife4j-config.yaml', 'DEFAULT_GROUP', '', 'knife4j:\r\n  enable: true\r\n  openapi:\r\n    title: ${ecommerce.knife4j.title:标题}\r\n    description: ${ecommerce.knife4j.description:描述}', 'c2186a809b822b0515f6631a4278bf21', '2025-02-09 09:59:42', '2025-02-09 09:59:42', NULL, '172.18.0.1', 'I', '', '');
INSERT INTO `his_config_info` VALUES (0, 6, 'feign-config.yaml', 'DEFAULT_GROUP', '', 'feign:\r\n  okhttp:\r\n    enabled: true', 'a8eb57128b4575d0d2819792ffc497f8', '2025-02-09 10:00:20', '2025-02-09 10:00:20', NULL, '172.18.0.1', 'I', '', '');
INSERT INTO `his_config_info` VALUES (2, 7, 'jdbc-config.yaml', 'DEFAULT_GROUP', '', 'spring:\r\n  datasource:\r\n  url: jdbc:mysql://${ecommerce.database.host}:${ecommerce.database.port}/${ecommerce.database.table}?useUnicode=true&characterEncoding=UTF-8&autoReconnect=true&serverTimezone=Asia/Shanghai\r\n  username: ${ecommerce.database.user}\r\n  password: ${ecommerce.database.pwd}\r\n  driver-class-name: com.mysql.cj.jdbc.Driver\r\n\r\nmybatis-plus:\r\n  configuration:\r\n    default-enum-type-handler: com.baomidou.mybatisplus.core.handlers.MybatisEnumTypeHandler\r\n  global-config:\r\n    db-config:\r\n      update-strategy: not_null\r\n      id-type: assign_id', '577220a4bb31a81457a3de5c6f15c7fc', '2025-02-09 10:17:54', '2025-02-09 10:17:54', 'nacos', '172.18.0.1', 'U', '', '');
INSERT INTO `his_config_info` VALUES (0, 8, 'sentinel-config.yaml', 'DEFAULT_GROUP', '', 'spring:\r\n  cloud:\r\n    sentinel:\r\n      transport:\r\n        dashboard: ${ecommerce.sentinel.dashboard}\r\n      http-method-specify: true', '0ed3e5ebcd4e8845744b121c9cbd078a', '2025-02-09 12:40:05', '2025-02-09 12:40:06', NULL, '172.18.0.1', 'I', '', '');
INSERT INTO `his_config_info` VALUES (5, 9, 'feign-config.yaml', 'DEFAULT_GROUP', '', 'feign:\r\n  okhttp:\r\n    enabled: true', 'a8eb57128b4575d0d2819792ffc497f8', '2025-02-09 12:42:07', '2025-02-09 12:42:07', 'nacos', '172.18.0.1', 'U', '', '');
INSERT INTO `his_config_info` VALUES (5, 10, 'feign-config.yaml', 'DEFAULT_GROUP', '', 'feign:\n  okhttp:\n    enabled: true\n  sentinel:\n    enabled: true', '8d8ea2b480dc2fa68b69ab9d89ae6060', '2025-02-09 12:42:12', '2025-02-09 12:42:12', 'nacos', '172.18.0.1', 'U', '', '');
INSERT INTO `his_config_info` VALUES (0, 11, 'seata-config.yaml', 'DEFAULT_GROUP', '', 'seata:\r\n  registry:\r\n    type: nacos\r\n    nacos:\r\n      server-addr: ${ecommerce.nacos.dashboard}\r\n      namespace: \"\"\r\n      group: DEFAULT_GROUP\r\n      application: seata-server\r\n      username: nacos\r\n      password: nacos\r\n  tx-service-group: ecommerce\r\n  service:\r\n    vgroup-mapping:\r\n      ecommerce: \"default\"', '7d974bb565d07fef2fc5bb87a749e643', '2025-02-09 19:05:45', '2025-02-09 19:05:46', NULL, '192.168.11.1', 'I', '', '');
INSERT INTO `his_config_info` VALUES (8, 12, 'seata-config.yaml', 'DEFAULT_GROUP', '', 'seata:\r\n  registry:\r\n    type: nacos\r\n    nacos:\r\n      server-addr: ${ecommerce.nacos.dashboard}\r\n      namespace: \"\"\r\n      group: DEFAULT_GROUP\r\n      application: seata-server\r\n      username: nacos\r\n      password: nacos\r\n  tx-service-group: ecommerce\r\n  service:\r\n    vgroup-mapping:\r\n      ecommerce: \"default\"', '7d974bb565d07fef2fc5bb87a749e643', '2025-02-09 19:11:18', '2025-02-09 19:11:18', 'nacos', '192.168.11.1', 'U', '', '');
INSERT INTO `his_config_info` VALUES (4, 13, 'knife4j-config.yaml', 'DEFAULT_GROUP', '', 'knife4j:\r\n  enable: true\r\n  openapi:\r\n    title: ${ecommerce.knife4j.title:标题}\r\n    description: ${ecommerce.knife4j.description:描述}', 'c2186a809b822b0515f6631a4278bf21', '2025-02-09 19:21:53', '2025-02-09 19:21:54', 'nacos', '172.18.0.1', 'U', '', '');
INSERT INTO `his_config_info` VALUES (8, 14, 'seata-config.yaml', 'DEFAULT_GROUP', '', 'seata:\n  data-source-proxy-mode: AT\n  registry:\n    type: nacos\n    nacos:\n      server-addr: ${ecommerce.nacos.dashboard}\n      namespace: \"\"\n      group: DEFAULT_GROUP\n      application: seata-server\n      username: nacos\n      password: nacos\n  tx-service-group: ecommerce\n  service:\n    vgroup-mapping:\n      ecommerce: \"default\"', 'e2b824b3be3d9951a2ae112722924172', '2025-02-09 22:50:22', '2025-02-09 22:50:22', 'nacos', '172.18.0.1', 'U', '', '');
INSERT INTO `his_config_info` VALUES (8, 15, 'seata-config.yaml', 'DEFAULT_GROUP', '', 'seata:\n  enabled: true\n  data-source-proxy-mode: AT\n  registry:\n    type: nacos\n    nacos:\n      server-addr: ${ecommerce.nacos.dashboard}\n      namespace: \"\"\n      group: DEFAULT_GROUP\n      application: seata-server\n      username: nacos\n      password: nacos\n  # tx-service-group: ecommerce\n  # service:\n  #   vgroup-mapping:\n  #     ecommerce: \"default\"\n\n# seata:\n#   enabled: true\n#   # Seata 应用编号，默认为 ${spring.application.name}\n#   application-id: ${spring.application.name}\n#   # Seata 事务组编号，用于 TC 集群名\n#   tx-service-group: ${spring.application.name}-group\n#   # 关闭自动代理\n#   enable-auto-data-source-proxy: true\n#   # 服务配置项\n#   service:\n#     # 虚拟组和分组的映射\n#     vgroup-mapping:\n#       seata-user-group: default\n#   registry:\n#     type: nacos\n#     nacos:\n#       server-addr: ${ecommerce.nacos.dashboard}\n#       namespace: \"\"\n#       group: DEFAULT_GROUP\n#       application: seata-server\n#       username: nacos\n#       password: nacos', '8746e74ac30560cc0d5441d25d92e6b4', '2025-02-09 22:58:25', '2025-02-09 22:58:26', 'nacos', '172.18.0.1', 'U', '', '');
INSERT INTO `his_config_info` VALUES (8, 16, 'seata-config.yaml', 'DEFAULT_GROUP', '', 'seata:\n  enabled: true\n  data-source-proxy-mode: AT\n  registry:\n    type: nacos\n    nacos:\n      server-addr: ${ecommerce.nacos.dashboard}\n      namespace: \"\"\n      group: DEFAULT_GROUP\n      application: seata-server\n      username: nacos\n      password: nacos\n  tx-service-group: my_test_tx_group\n  # tx-service-group: ecommerce\n  # service:\n  #   vgroup-mapping:\n  #     ecommerce: \"default\"\n\n# seata:\n#   enabled: true\n#   # Seata 应用编号，默认为 ${spring.application.name}\n#   application-id: ${spring.application.name}\n#   # Seata 事务组编号，用于 TC 集群名\n#   tx-service-group: ${spring.application.name}-group\n#   # 关闭自动代理\n#   enable-auto-data-source-proxy: true\n#   # 服务配置项\n#   service:\n#     # 虚拟组和分组的映射\n#     vgroup-mapping:\n#       seata-user-group: default\n#   registry:\n#     type: nacos\n#     nacos:\n#       server-addr: ${ecommerce.nacos.dashboard}\n#       namespace: \"\"\n#       group: DEFAULT_GROUP\n#       application: seata-server\n#       username: nacos\n#       password: nacos', '2cfddc6f2621f277e526296770f5a9b9', '2025-02-09 23:11:12', '2025-02-09 23:11:12', 'nacos', '172.18.0.1', 'U', '', '');
INSERT INTO `his_config_info` VALUES (8, 17, 'seata-config.yaml', 'DEFAULT_GROUP', '', 'seata:\n  enabled: true\n  data-source-proxy-mode: AT\n  registry:\n    type: nacos\n    nacos:\n      server-addr: ${ecommerce.nacos.dashboard}\n      # namespace: \"\"\n      group: DEFAULT_GROUP\n      application: seata-server\n      username: nacos\n      password: nacos\n  tx-service-group: my_test_tx_group\n  tx-service-group: ecommerce\n  service:\n    vgroup-mapping:\n      ecommerce: default\n\n# seata:\n#   enabled: true\n#   # Seata 应用编号，默认为 ${spring.application.name}\n#   application-id: ${spring.application.name}\n#   # Seata 事务组编号，用于 TC 集群名\n#   tx-service-group: ${spring.application.name}-group\n#   # 关闭自动代理\n#   enable-auto-data-source-proxy: true\n#   # 服务配置项\n#   service:\n#     # 虚拟组和分组的映射\n#     vgroup-mapping:\n#       seata-user-group: default\n#   registry:\n#     type: nacos\n#     nacos:\n#       server-addr: ${ecommerce.nacos.dashboard}\n#       namespace: \"\"\n#       group: DEFAULT_GROUP\n#       application: seata-server\n#       username: nacos\n#       password: nacos', 'aa3fde1d74ab85bac8d9dd2bc6f3a718', '2025-02-09 23:13:26', '2025-02-09 23:13:26', 'nacos', '172.18.0.1', 'U', '', '');
INSERT INTO `his_config_info` VALUES (8, 18, 'seata-config.yaml', 'DEFAULT_GROUP', '', 'seata:\n  enabled: true\n  data-source-proxy-mode: AT\n  registry:\n    type: nacos\n    nacos:\n      server-addr: ${ecommerce.nacos.dashboard}\n      # namespace: \"\"\n      group: DEFAULT_GROUP\n      application: seata-server\n      username: nacos\n      password: nacos\n  tx-service-group: ecommerce\n  service:\n    vgroup-mapping:\n      ecommerce: default\n\n# seata:\n#   enabled: true\n#   # Seata 应用编号，默认为 ${spring.application.name}\n#   application-id: ${spring.application.name}\n#   # Seata 事务组编号，用于 TC 集群名\n#   tx-service-group: ${spring.application.name}-group\n#   # 关闭自动代理\n#   enable-auto-data-source-proxy: true\n#   # 服务配置项\n#   service:\n#     # 虚拟组和分组的映射\n#     vgroup-mapping:\n#       seata-user-group: default\n#   registry:\n#     type: nacos\n#     nacos:\n#       server-addr: ${ecommerce.nacos.dashboard}\n#       namespace: \"\"\n#       group: DEFAULT_GROUP\n#       application: seata-server\n#       username: nacos\n#       password: nacos', 'f78cb2733c38534b80a8269103af2a8e', '2025-02-09 23:23:47', '2025-02-09 23:23:47', 'nacos', '172.18.0.1', 'U', '', '');
INSERT INTO `his_config_info` VALUES (8, 19, 'seata-config.yaml', 'DEFAULT_GROUP', '', 'seata:\n  enabled: true\n  data-source-proxy-mode: AT\n  registry:\n    type: nacos\n    nacos:\n      server-addr: ${ecommerce.nacos.dashboard}\n      # namespace: \"\"\n      group: DEFAULT_GROUP\n      application: seata-server\n      username: nacos\n      password: nacos\n  tx-service-group: ecommerce\n  service:\n    vgroup-mapping:\n      ecommerce: default', '060838a9a21f847c4835faa6c3270670', '2025-02-09 23:50:55', '2025-02-09 23:50:56', NULL, '172.18.0.1', 'U', '', '');
INSERT INTO `his_config_info` VALUES (8, 20, 'seata-config.yaml', 'DEFAULT_GROUP', '', 'seata:\n  enabled: true\n  data-source-proxy-mode: AT\n  registry:\n    type: nacos\n    nacos:\n      server-addr: ${ecommerce.nacos.dashboard}\n      # namespace: \"\"\n      group: DEFAULT_GROUP\n      application: seata-server\n      username: nacos\n      password: nacos\n  tx-service-group: ecommerce\n  service:\n    vgroup-mapping:\n      ecommerce: default\n\n# seata:\n#   enabled: true\n#   # Seata 应用编号，默认为 ${spring.application.name}\n#   application-id: ${spring.application.name}\n#   # Seata 事务组编号，用于 TC 集群名\n#   tx-service-group: ${spring.application.name}-group\n#   # 关闭自动代理\n#   enable-auto-data-source-proxy: true\n#   # 服务配置项\n#   service:\n#     # 虚拟组和分组的映射\n#     vgroup-mapping:\n#       seata-user-group: default\n#   registry:\n#     type: nacos\n#     nacos:\n#       server-addr: ${ecommerce.nacos.dashboard}\n#       namespace: \"\"\n#       group: DEFAULT_GROUP\n#       application: seata-server\n#       username: nacos\n#       password: nacos', 'f78cb2733c38534b80a8269103af2a8e', '2025-02-09 23:52:22', '2025-02-09 23:52:22', 'nacos', '172.18.0.1', 'U', '', '');
INSERT INTO `his_config_info` VALUES (8, 21, 'seata-config.yaml', 'DEFAULT_GROUP', '', 'seata:\n  enabled: true\n  data-source-proxy-mode: AT\n  registry:\n    type: nacos\n    nacos:\n      server-addr: ${ecommerce.nacos.dashboard}\n      # namespace: \"\"\n      group: DEFAULT_GROUP\n      application: seata-server\n      username: nacos\n      password: nacos\n  tx-service-group: ecommerce\n  service:\n    vgroup-mapping:\n      ecommerce: default\n\n', '93fd902d906bcfe185c1715deaf93e21', '2025-02-09 23:52:57', '2025-02-09 23:52:58', 'nacos', '172.18.0.1', 'U', '', '');
INSERT INTO `his_config_info` VALUES (8, 22, 'seata-config.yaml', 'DEFAULT_GROUP', '', 'seata:\n  enabled: true\n  data-source-proxy-mode: AT\n  registry:\n    type: nacos\n    nacos:\n      server-addr: ${ecommerce.nacos.dashboard}\n      # namespace: \"\"\n      group: DEFAULT_GROUP\n      application: seata-server\n      username: nacos\n      password: nacos\n  tx-service-group: ecommerce\n  service:\n    vgroup-mapping:\n      ecommerce: default', '060838a9a21f847c4835faa6c3270670', '2025-02-09 23:53:46', '2025-02-09 23:53:47', NULL, '172.18.0.1', 'U', '', '');
INSERT INTO `his_config_info` VALUES (8, 23, 'seata-config.yaml', 'DEFAULT_GROUP', '', 'seata:\n  enabled: true\n  data-source-proxy-mode: AT\n  registry:\n    type: nacos\n    nacos:\n      server-addr: ${ecommerce.nacos.dashboard}\n      # namespace: \"\"\n      group: DEFAULT_GROUP\n      application: seata-server\n      username: nacos\n      password: nacos\n  tx-service-group: ecommerce\n  service:\n    vgroup-mapping:\n      ecommerce: default\n\n', '93fd902d906bcfe185c1715deaf93e21', '2025-02-09 23:54:05', '2025-02-09 23:54:05', 'nacos', '172.18.0.1', 'U', '', '');
INSERT INTO `his_config_info` VALUES (0, 24, 'redis-config.yaml', 'DEFAULT_GROUP', '', 'spring:\r\n  redis:\r\n    cluster:\r\n      nodes: ${ecommerce.redis.cluster-addr}\r\n    lettuce:\r\n      pool:\r\n        max-active: 8\r\n        max-idle: 8\r\n        min-idle: 0', 'b02ac41a76311e73bb0e9c7d787ab338', '2025-02-11 02:16:07', '2025-02-11 02:16:08', NULL, '172.18.0.1', 'I', '', '');
INSERT INTO `his_config_info` VALUES (2, 25, 'jdbc-config.yaml', 'DEFAULT_GROUP', '', 'spring:\n  datasource:\n    url: jdbc:mysql://${ecommerce.database.host}:${ecommerce.database.port}/${ecommerce.database.table}?useUnicode=true&characterEncoding=UTF-8&autoReconnect=true&serverTimezone=Asia/Shanghai\n    username: ${ecommerce.database.user}\n    password: ${ecommerce.database.pwd}\n    driver-class-name: com.mysql.cj.jdbc.Driver\n\nmybatis-plus:\n  configuration:\n    default-enum-type-handler: com.baomidou.mybatisplus.core.handlers.MybatisEnumTypeHandler\n  global-config:\n    db-config:\n      update-strategy: not_null\n      id-type: assign_id', '6ed3167a7239bae6c034691c633a453f', '2025-02-11 02:32:13', '2025-02-11 02:32:13', 'nacos', '172.18.0.1', 'U', '', '');
INSERT INTO `his_config_info` VALUES (0, 26, 'rabbitmq-config.yaml', 'DEFAULT_GROUP', '', 'spring:\r\n  rabbitmq:\r\n    host: ${ecommerce.rabbitmq.host}\r\n    port: ${ecommerce.rabbitmq.port}\r\n    virtual-host: ${ecommerce.rabbitmq.virtual}\r\n    username: ${ecommerce.rabbitmq.user}\r\n    password: ${ecommerce.rabbitmq.pwd}\r\n    listener:\r\n      simple:\r\n        prefetch: 1', '635547cd03b7aa15ca171a760e87209a', '2025-02-12 21:08:10', '2025-02-12 21:08:10', NULL, '172.19.0.1', 'I', '', '');
INSERT INTO `his_config_info` VALUES (23, 27, 'rabbitmq-config.yaml', 'DEFAULT_GROUP', '', 'spring:\r\n  rabbitmq:\r\n    host: ${ecommerce.rabbitmq.host}\r\n    port: ${ecommerce.rabbitmq.port}\r\n    virtual-host: ${ecommerce.rabbitmq.virtual}\r\n    username: ${ecommerce.rabbitmq.user}\r\n    password: ${ecommerce.rabbitmq.pwd}\r\n    listener:\r\n      simple:\r\n        prefetch: 1', '635547cd03b7aa15ca171a760e87209a', '2025-02-12 21:08:10', '2025-02-12 21:08:10', NULL, '172.19.0.1', 'U', '', '');

-- ----------------------------
-- Table structure for permissions
-- ----------------------------
DROP TABLE IF EXISTS `permissions`;
CREATE TABLE `permissions`  (
  `role` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `resource` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `action` varchar(8) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  UNIQUE INDEX `uk_role_permission`(`role` ASC, `resource` ASC, `action` ASC) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of permissions
-- ----------------------------

-- ----------------------------
-- Table structure for roles
-- ----------------------------
DROP TABLE IF EXISTS `roles`;
CREATE TABLE `roles`  (
  `username` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `role` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  UNIQUE INDEX `idx_user_role`(`username` ASC, `role` ASC) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of roles
-- ----------------------------
INSERT INTO `roles` VALUES ('nacos', 'ROLE_ADMIN');

-- ----------------------------
-- Table structure for tenant_capacity
-- ----------------------------
DROP TABLE IF EXISTS `tenant_capacity`;
CREATE TABLE `tenant_capacity`  (
  `id` bigint UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `tenant_id` varchar(128) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NOT NULL DEFAULT '' COMMENT 'Tenant ID',
  `quota` int UNSIGNED NOT NULL DEFAULT 0 COMMENT '配额，0表示使用默认值',
  `usage` int UNSIGNED NOT NULL DEFAULT 0 COMMENT '使用量',
  `max_size` int UNSIGNED NOT NULL DEFAULT 0 COMMENT '单个配置大小上限，单位为字节，0表示使用默认值',
  `max_aggr_count` int UNSIGNED NOT NULL DEFAULT 0 COMMENT '聚合子配置最大个数',
  `max_aggr_size` int UNSIGNED NOT NULL DEFAULT 0 COMMENT '单个聚合数据的子配置大小上限，单位为字节，0表示使用默认值',
  `max_history_count` int UNSIGNED NOT NULL DEFAULT 0 COMMENT '最大变更历史数量',
  `gmt_create` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `gmt_modified` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '修改时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uk_tenant_id`(`tenant_id` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb3 COLLATE = utf8mb3_bin COMMENT = '租户容量信息表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of tenant_capacity
-- ----------------------------

-- ----------------------------
-- Table structure for tenant_info
-- ----------------------------
DROP TABLE IF EXISTS `tenant_info`;
CREATE TABLE `tenant_info`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT 'id',
  `kp` varchar(128) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NOT NULL COMMENT 'kp',
  `tenant_id` varchar(128) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NULL DEFAULT '' COMMENT 'tenant_id',
  `tenant_name` varchar(128) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NULL DEFAULT '' COMMENT 'tenant_name',
  `tenant_desc` varchar(256) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NULL DEFAULT NULL COMMENT 'tenant_desc',
  `create_source` varchar(32) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NULL DEFAULT NULL COMMENT 'create_source',
  `gmt_create` bigint NOT NULL COMMENT '创建时间',
  `gmt_modified` bigint NOT NULL COMMENT '修改时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uk_tenant_info_kptenantid`(`kp` ASC, `tenant_id` ASC) USING BTREE,
  INDEX `idx_tenant_id`(`tenant_id` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb3 COLLATE = utf8mb3_bin COMMENT = 'tenant_info' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of tenant_info
-- ----------------------------

-- ----------------------------
-- Table structure for users
-- ----------------------------
DROP TABLE IF EXISTS `users`;
CREATE TABLE `users`  (
  `username` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `password` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `enabled` tinyint(1) NOT NULL,
  PRIMARY KEY (`username`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of users
-- ----------------------------
INSERT INTO `users` VALUES ('nacos', '$2a$10$EuWPZHzz32dJN7jexM34MOeYirDdFAZm2kuWj7VEOJhhZkDrxfvUu', 1);

SET FOREIGN_KEY_CHECKS = 1;

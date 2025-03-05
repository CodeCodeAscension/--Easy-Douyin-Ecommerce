-- MySQL dump 10.13  Distrib 8.0.41, for Linux (x86_64)
--
-- Host: 127.0.0.1    Database: nacos
-- ------------------------------------------------------
-- Server version	8.0.36

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8mb4 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `config_info`
--

DROP TABLE IF EXISTS `config_info`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `config_info` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT 'id',
  `data_id` varchar(255) COLLATE utf8mb3_bin NOT NULL COMMENT 'data_id',
  `group_id` varchar(128) COLLATE utf8mb3_bin DEFAULT NULL,
  `content` longtext COLLATE utf8mb3_bin NOT NULL COMMENT 'content',
  `md5` varchar(32) COLLATE utf8mb3_bin DEFAULT NULL COMMENT 'md5',
  `gmt_create` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `gmt_modified` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '修改时间',
  `src_user` text COLLATE utf8mb3_bin COMMENT 'source user',
  `src_ip` varchar(50) COLLATE utf8mb3_bin DEFAULT NULL COMMENT 'source ip',
  `app_name` varchar(128) COLLATE utf8mb3_bin DEFAULT NULL,
  `tenant_id` varchar(128) COLLATE utf8mb3_bin DEFAULT '' COMMENT '租户字段',
  `c_desc` varchar(256) COLLATE utf8mb3_bin DEFAULT NULL,
  `c_use` varchar(64) COLLATE utf8mb3_bin DEFAULT NULL,
  `effect` varchar(64) COLLATE utf8mb3_bin DEFAULT NULL,
  `type` varchar(64) COLLATE utf8mb3_bin DEFAULT NULL,
  `c_schema` text COLLATE utf8mb3_bin,
  `encrypted_data_key` text COLLATE utf8mb3_bin NOT NULL COMMENT '秘钥',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_configinfo_datagrouptenant` (`data_id`,`group_id`,`tenant_id`)
) ENGINE=InnoDB AUTO_INCREMENT=35 DEFAULT CHARSET=utf8mb3 COLLATE=utf8mb3_bin COMMENT='config_info';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `config_info`
--

LOCK TABLES `config_info` WRITE;
/*!40000 ALTER TABLE `config_info` DISABLE KEYS */;
INSERT INTO `config_info` VALUES (2,'jdbc-config.yaml','DEFAULT_GROUP','spring:\n  datasource:\n    type: com.alibaba.druid.pool.DruidDataSource\n    url: jdbc:mysql://${ecommerce.database.host}:${ecommerce.database.port}/${ecommerce.database.table}?useUnicode=true&characterEncoding=UTF-8&autoReconnect=true&serverTimezone=Asia/Shanghai\n    username: ${ecommerce.database.user}\n    password: ${ecommerce.database.pwd}\n    driver-class-name: com.mysql.cj.jdbc.Driver\n\nmybatis-plus:\n  configuration:\n    default-enum-type-handler: com.baomidou.mybatisplus.core.handlers.MybatisEnumTypeHandler\n    cache-enabled: true\n  global-config:\n    db-config:\n      update-strategy: not_null\n      id-type: assign_id','3aa24a679f5818183dced16d89bc089f','2025-02-09 09:55:02','2025-03-04 20:26:02','nacos','172.18.0.1','','','数据库连接以及MyBatis的配置','','','yaml','',''),(3,'log-config.yaml','DEFAULT_GROUP','logging:\r\n  level:\r\n    com.example: debug\r\n  pattern:\r\n    dateformat: HH:mm:ss:SSS\r\n  file:\r\n    path: \"logs/${spring.application.name}\"','cff25120c22850da8594bc84585580ba','2025-02-09 09:57:36','2025-02-09 09:57:36',NULL,'172.18.0.1','','','日志配置',NULL,NULL,'yaml',NULL,''),(4,'knife4j-config.yaml','DEFAULT_GROUP','knife4j:\n  enable: true\n  openapi:\n    title: ${ecommerce.knife4j.title:标题}\n    description: ${ecommerce.knife4j.description:描述}\n    base-package: com.example','fa87b35f725b8c715906447ae8e1df71','2025-02-09 09:59:42','2025-02-09 19:21:54','nacos','172.18.0.1','','','knife4j配置','','','yaml','',''),(5,'feign-config.yaml','DEFAULT_GROUP','feign:\n  okhttp:\n    enabled: true\n  sentinel:\n    enabled: true','8d8ea2b480dc2fa68b69ab9d89ae6060','2025-02-09 10:00:20','2025-02-09 12:42:12','nacos','172.18.0.1','','','feign配置','','','yaml','',''),(7,'sentinel-config.yaml','DEFAULT_GROUP','spring:\r\n  cloud:\r\n    sentinel:\r\n      transport:\r\n        dashboard: ${ecommerce.sentinel.dashboard}\r\n      http-method-specify: true','0ed3e5ebcd4e8845744b121c9cbd078a','2025-02-09 12:40:06','2025-02-09 12:40:06',NULL,'172.18.0.1','','','sentinel相关配置',NULL,NULL,'yaml',NULL,''),(8,'seata-config.yaml','DEFAULT_GROUP','seata:\n  enabled: true\n  data-source-proxy-mode: AT\n  registry:\n    type: nacos\n    nacos:\n      server-addr: ${ecommerce.nacos.dashboard}\n      # namespace: \"\"\n      group: DEFAULT_GROUP\n      application: seata-server\n      username: nacos\n      password: nacos\n  tx-service-group: ecommerce\n  service:\n    vgroup-mapping:\n      ecommerce: default\n\n','93fd902d906bcfe185c1715deaf93e21','2025-02-09 19:05:46','2025-02-09 23:54:05','nacos','172.18.0.1','','','','','','yaml','',''),(21,'redis-config.yaml','DEFAULT_GROUP','spring:\r\n  redis:\r\n    cluster:\r\n      nodes: ${ecommerce.redis.cluster-addr}\r\n    lettuce:\r\n      pool:\r\n        max-active: 8\r\n        max-idle: 8\r\n        min-idle: 0','b02ac41a76311e73bb0e9c7d787ab338','2025-02-11 02:16:08','2025-02-17 23:22:15',NULL,'172.18.0.1','','',NULL,NULL,NULL,'text',NULL,''),(23,'rabbitmq-config.yaml','DEFAULT_GROUP','spring:\n  rabbitmq:\n    host: ${ecommerce.rabbitmq.host}\n    port: ${ecommerce.rabbitmq.port}\n    virtual-host: ${ecommerce.rabbitmq.virtual}\n    username: ${ecommerce.rabbitmq.user}\n    password: ${ecommerce.rabbitmq.pwd}\n    listener:\n      simple:\n        prefetch: 1\n        acknowledge-mode: auto\n        retry:\n          enabled: true\n          initial-interval: 1000ms\n          multiplier: 1\n          max-attempts: 3\n          stateless: true\n\n    publisher-confirm-type: correlated','80d0441b7269004c59762947dfd424be','2025-02-12 21:08:10','2025-03-03 14:01:37','nacos','172.18.0.1','','','rabbitmq的配置信息','','','yaml','',''),(25,'jwt-config.yaml','DEFAULT_GROUP','ecommerce:\n  jwt:\n    key: \"aB3$kL9@qW2^zX8&amp;pR7*\"\n    expire-hour: 1\n    refresh-expire-day: 7   # 刷新令牌过期时间','e4062f25437ceaa6ce3d9a30926c9be8','2025-02-17 01:37:39','2025-02-21 10:55:41','nacos','172.18.0.1','','','JWT令牌的配置信息','','','yaml','',''),(29,'mq-queues-config.yaml','DEFAULT_GROUP','ecommerce:\n  rabbitmq:\n    exchange-name: \"directExchange\"\n    queues:\n      pay:\n        start: \"pay.start\"\n        cancel: \"pay.cancel\"\n        fail: \"pay.fail\"\n        success: \"pay.success\"','64f965d24935f7999c3fea786d9ff490','2025-02-22 17:22:19','2025-03-01 00:27:38','nacos','172.18.0.1','','','','','','yaml','',''),(31,'elasticsearch-config.yaml','DEFAULT_GROUP','ecommerce:\n  elasticsearch:\n    connect-timeout: 5000\n    socket-timeout: 10000','4882f840b8fe5d4a97224c676982b3bb','2025-03-01 23:47:12','2025-03-01 23:47:12',NULL,'172.18.0.1','','',NULL,NULL,NULL,'yaml',NULL,'');
/*!40000 ALTER TABLE `config_info` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `config_info_aggr`
--

DROP TABLE IF EXISTS `config_info_aggr`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `config_info_aggr` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT 'id',
  `data_id` varchar(255) COLLATE utf8mb3_bin NOT NULL COMMENT 'data_id',
  `group_id` varchar(128) COLLATE utf8mb3_bin NOT NULL COMMENT 'group_id',
  `datum_id` varchar(255) COLLATE utf8mb3_bin NOT NULL COMMENT 'datum_id',
  `content` longtext COLLATE utf8mb3_bin NOT NULL COMMENT '内容',
  `gmt_modified` datetime NOT NULL COMMENT '修改时间',
  `app_name` varchar(128) COLLATE utf8mb3_bin DEFAULT NULL,
  `tenant_id` varchar(128) COLLATE utf8mb3_bin DEFAULT '' COMMENT '租户字段',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_configinfoaggr_datagrouptenantdatum` (`data_id`,`group_id`,`tenant_id`,`datum_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3 COLLATE=utf8mb3_bin COMMENT='增加租户字段';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `config_info_aggr`
--

LOCK TABLES `config_info_aggr` WRITE;
/*!40000 ALTER TABLE `config_info_aggr` DISABLE KEYS */;
/*!40000 ALTER TABLE `config_info_aggr` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `config_info_beta`
--

DROP TABLE IF EXISTS `config_info_beta`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `config_info_beta` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT 'id',
  `data_id` varchar(255) COLLATE utf8mb3_bin NOT NULL COMMENT 'data_id',
  `group_id` varchar(128) COLLATE utf8mb3_bin NOT NULL COMMENT 'group_id',
  `app_name` varchar(128) COLLATE utf8mb3_bin DEFAULT NULL COMMENT 'app_name',
  `content` longtext COLLATE utf8mb3_bin NOT NULL COMMENT 'content',
  `beta_ips` varchar(1024) COLLATE utf8mb3_bin DEFAULT NULL COMMENT 'betaIps',
  `md5` varchar(32) COLLATE utf8mb3_bin DEFAULT NULL COMMENT 'md5',
  `gmt_create` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `gmt_modified` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '修改时间',
  `src_user` text COLLATE utf8mb3_bin COMMENT 'source user',
  `src_ip` varchar(50) COLLATE utf8mb3_bin DEFAULT NULL COMMENT 'source ip',
  `tenant_id` varchar(128) COLLATE utf8mb3_bin DEFAULT '' COMMENT '租户字段',
  `encrypted_data_key` text COLLATE utf8mb3_bin NOT NULL COMMENT '秘钥',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_configinfobeta_datagrouptenant` (`data_id`,`group_id`,`tenant_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3 COLLATE=utf8mb3_bin COMMENT='config_info_beta';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `config_info_beta`
--

LOCK TABLES `config_info_beta` WRITE;
/*!40000 ALTER TABLE `config_info_beta` DISABLE KEYS */;
/*!40000 ALTER TABLE `config_info_beta` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `config_info_tag`
--

DROP TABLE IF EXISTS `config_info_tag`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `config_info_tag` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT 'id',
  `data_id` varchar(255) COLLATE utf8mb3_bin NOT NULL COMMENT 'data_id',
  `group_id` varchar(128) COLLATE utf8mb3_bin NOT NULL COMMENT 'group_id',
  `tenant_id` varchar(128) COLLATE utf8mb3_bin DEFAULT '' COMMENT 'tenant_id',
  `tag_id` varchar(128) COLLATE utf8mb3_bin NOT NULL COMMENT 'tag_id',
  `app_name` varchar(128) COLLATE utf8mb3_bin DEFAULT NULL COMMENT 'app_name',
  `content` longtext COLLATE utf8mb3_bin NOT NULL COMMENT 'content',
  `md5` varchar(32) COLLATE utf8mb3_bin DEFAULT NULL COMMENT 'md5',
  `gmt_create` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `gmt_modified` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '修改时间',
  `src_user` text COLLATE utf8mb3_bin COMMENT 'source user',
  `src_ip` varchar(50) COLLATE utf8mb3_bin DEFAULT NULL COMMENT 'source ip',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_configinfotag_datagrouptenanttag` (`data_id`,`group_id`,`tenant_id`,`tag_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3 COLLATE=utf8mb3_bin COMMENT='config_info_tag';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `config_info_tag`
--

LOCK TABLES `config_info_tag` WRITE;
/*!40000 ALTER TABLE `config_info_tag` DISABLE KEYS */;
/*!40000 ALTER TABLE `config_info_tag` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `config_tags_relation`
--

DROP TABLE IF EXISTS `config_tags_relation`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `config_tags_relation` (
  `id` bigint NOT NULL COMMENT 'id',
  `tag_name` varchar(128) COLLATE utf8mb3_bin NOT NULL COMMENT 'tag_name',
  `tag_type` varchar(64) COLLATE utf8mb3_bin DEFAULT NULL COMMENT 'tag_type',
  `data_id` varchar(255) COLLATE utf8mb3_bin NOT NULL COMMENT 'data_id',
  `group_id` varchar(128) COLLATE utf8mb3_bin NOT NULL COMMENT 'group_id',
  `tenant_id` varchar(128) COLLATE utf8mb3_bin DEFAULT '' COMMENT 'tenant_id',
  `nid` bigint NOT NULL AUTO_INCREMENT,
  PRIMARY KEY (`nid`),
  UNIQUE KEY `uk_configtagrelation_configidtag` (`id`,`tag_name`,`tag_type`),
  KEY `idx_tenant_id` (`tenant_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3 COLLATE=utf8mb3_bin COMMENT='config_tag_relation';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `config_tags_relation`
--

LOCK TABLES `config_tags_relation` WRITE;
/*!40000 ALTER TABLE `config_tags_relation` DISABLE KEYS */;
/*!40000 ALTER TABLE `config_tags_relation` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `group_capacity`
--

DROP TABLE IF EXISTS `group_capacity`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `group_capacity` (
  `id` bigint unsigned NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `group_id` varchar(128) COLLATE utf8mb3_bin NOT NULL DEFAULT '' COMMENT 'Group ID，空字符表示整个集群',
  `quota` int unsigned NOT NULL DEFAULT '0' COMMENT '配额，0表示使用默认值',
  `usage` int unsigned NOT NULL DEFAULT '0' COMMENT '使用量',
  `max_size` int unsigned NOT NULL DEFAULT '0' COMMENT '单个配置大小上限，单位为字节，0表示使用默认值',
  `max_aggr_count` int unsigned NOT NULL DEFAULT '0' COMMENT '聚合子配置最大个数，，0表示使用默认值',
  `max_aggr_size` int unsigned NOT NULL DEFAULT '0' COMMENT '单个聚合数据的子配置大小上限，单位为字节，0表示使用默认值',
  `max_history_count` int unsigned NOT NULL DEFAULT '0' COMMENT '最大变更历史数量',
  `gmt_create` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `gmt_modified` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '修改时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_group_id` (`group_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3 COLLATE=utf8mb3_bin COMMENT='集群、各Group容量信息表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `group_capacity`
--

LOCK TABLES `group_capacity` WRITE;
/*!40000 ALTER TABLE `group_capacity` DISABLE KEYS */;
/*!40000 ALTER TABLE `group_capacity` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `his_config_info`
--

DROP TABLE IF EXISTS `his_config_info`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `his_config_info` (
  `id` bigint unsigned NOT NULL,
  `nid` bigint unsigned NOT NULL AUTO_INCREMENT,
  `data_id` varchar(255) COLLATE utf8mb3_bin NOT NULL,
  `group_id` varchar(128) COLLATE utf8mb3_bin NOT NULL,
  `app_name` varchar(128) COLLATE utf8mb3_bin DEFAULT NULL COMMENT 'app_name',
  `content` longtext COLLATE utf8mb3_bin NOT NULL,
  `md5` varchar(32) COLLATE utf8mb3_bin DEFAULT NULL,
  `gmt_create` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `gmt_modified` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `src_user` text COLLATE utf8mb3_bin,
  `src_ip` varchar(50) COLLATE utf8mb3_bin DEFAULT NULL,
  `op_type` char(10) COLLATE utf8mb3_bin DEFAULT NULL,
  `tenant_id` varchar(128) COLLATE utf8mb3_bin DEFAULT '' COMMENT '租户字段',
  `encrypted_data_key` text COLLATE utf8mb3_bin NOT NULL COMMENT '秘钥',
  PRIMARY KEY (`nid`),
  KEY `idx_gmt_create` (`gmt_create`),
  KEY `idx_gmt_modified` (`gmt_modified`),
  KEY `idx_did` (`data_id`)
) ENGINE=InnoDB AUTO_INCREMENT=38 DEFAULT CHARSET=utf8mb3 COLLATE=utf8mb3_bin COMMENT='多租户改造';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `his_config_info`
--

LOCK TABLES `his_config_info` WRITE;
/*!40000 ALTER TABLE `his_config_info` DISABLE KEYS */;
INSERT INTO `his_config_info` VALUES (0,1,'vlsmb-test.yaml','DEFAULT_GROUP','','vlsmb:\r\n  config:\r\n    title: VLSMB','550408744085e5fe6f827d077a34699c','2025-02-09 09:40:16','2025-02-09 09:40:17',NULL,'172.18.0.1','I','',''),(1,2,'vlsmb-test.yaml','DEFAULT_GROUP','','vlsmb:\r\n  config:\r\n    title: VLSMB','550408744085e5fe6f827d077a34699c','2025-02-09 09:47:27','2025-02-09 09:47:28',NULL,'172.18.0.1','D','',''),(0,3,'jdbc-config.yaml','DEFAULT_GROUP','','spring:\r\n  datasource:\r\n  url: jdbc:mysql://${ecommerce.database.host}:${ecommerce.database.port}/${ecommerce.database.table}?useUnicode=true&characterEncoding=UTF-8&autoReconnect=true&serverTimezone=Asia/Shanghai\r\n  username: ${ecommerce.database.user}\r\n  password: ${ecommerce.database.pwd}\r\n  driver-class-name: com.mysql.cj.jdbc.Driver\r\n\r\nmybatis-plus:\r\n  configuration:\r\n    default-enum-type-handler: com.baomidou.mybatisplus.core.handlers.MybatisEnumTypeHandler\r\n  global-config:\r\n    db-config:\r\n      update-strategy: not_null\r\n      id-type: assign_id','577220a4bb31a81457a3de5c6f15c7fc','2025-02-09 09:55:01','2025-02-09 09:55:02',NULL,'172.18.0.1','I','',''),(0,4,'log-config.yaml','DEFAULT_GROUP','','logging:\r\n  level:\r\n    com.example: debug\r\n  pattern:\r\n    dateformat: HH:mm:ss:SSS\r\n  file:\r\n    path: \"logs/${spring.application.name}\"','cff25120c22850da8594bc84585580ba','2025-02-09 09:57:36','2025-02-09 09:57:36',NULL,'172.18.0.1','I','',''),(0,5,'knife4j-config.yaml','DEFAULT_GROUP','','knife4j:\r\n  enable: true\r\n  openapi:\r\n    title: ${ecommerce.knife4j.title:标题}\r\n    description: ${ecommerce.knife4j.description:描述}','c2186a809b822b0515f6631a4278bf21','2025-02-09 09:59:42','2025-02-09 09:59:42',NULL,'172.18.0.1','I','',''),(0,6,'feign-config.yaml','DEFAULT_GROUP','','feign:\r\n  okhttp:\r\n    enabled: true','a8eb57128b4575d0d2819792ffc497f8','2025-02-09 10:00:20','2025-02-09 10:00:20',NULL,'172.18.0.1','I','',''),(2,7,'jdbc-config.yaml','DEFAULT_GROUP','','spring:\r\n  datasource:\r\n  url: jdbc:mysql://${ecommerce.database.host}:${ecommerce.database.port}/${ecommerce.database.table}?useUnicode=true&characterEncoding=UTF-8&autoReconnect=true&serverTimezone=Asia/Shanghai\r\n  username: ${ecommerce.database.user}\r\n  password: ${ecommerce.database.pwd}\r\n  driver-class-name: com.mysql.cj.jdbc.Driver\r\n\r\nmybatis-plus:\r\n  configuration:\r\n    default-enum-type-handler: com.baomidou.mybatisplus.core.handlers.MybatisEnumTypeHandler\r\n  global-config:\r\n    db-config:\r\n      update-strategy: not_null\r\n      id-type: assign_id','577220a4bb31a81457a3de5c6f15c7fc','2025-02-09 10:17:54','2025-02-09 10:17:54','nacos','172.18.0.1','U','',''),(0,8,'sentinel-config.yaml','DEFAULT_GROUP','','spring:\r\n  cloud:\r\n    sentinel:\r\n      transport:\r\n        dashboard: ${ecommerce.sentinel.dashboard}\r\n      http-method-specify: true','0ed3e5ebcd4e8845744b121c9cbd078a','2025-02-09 12:40:05','2025-02-09 12:40:06',NULL,'172.18.0.1','I','',''),(5,9,'feign-config.yaml','DEFAULT_GROUP','','feign:\r\n  okhttp:\r\n    enabled: true','a8eb57128b4575d0d2819792ffc497f8','2025-02-09 12:42:07','2025-02-09 12:42:07','nacos','172.18.0.1','U','',''),(5,10,'feign-config.yaml','DEFAULT_GROUP','','feign:\n  okhttp:\n    enabled: true\n  sentinel:\n    enabled: true','8d8ea2b480dc2fa68b69ab9d89ae6060','2025-02-09 12:42:12','2025-02-09 12:42:12','nacos','172.18.0.1','U','',''),(0,11,'seata-config.yaml','DEFAULT_GROUP','','seata:\r\n  registry:\r\n    type: nacos\r\n    nacos:\r\n      server-addr: ${ecommerce.nacos.dashboard}\r\n      namespace: \"\"\r\n      group: DEFAULT_GROUP\r\n      application: seata-server\r\n      username: nacos\r\n      password: nacos\r\n  tx-service-group: ecommerce\r\n  service:\r\n    vgroup-mapping:\r\n      ecommerce: \"default\"','7d974bb565d07fef2fc5bb87a749e643','2025-02-09 19:05:45','2025-02-09 19:05:46',NULL,'192.168.11.1','I','',''),(8,12,'seata-config.yaml','DEFAULT_GROUP','','seata:\r\n  registry:\r\n    type: nacos\r\n    nacos:\r\n      server-addr: ${ecommerce.nacos.dashboard}\r\n      namespace: \"\"\r\n      group: DEFAULT_GROUP\r\n      application: seata-server\r\n      username: nacos\r\n      password: nacos\r\n  tx-service-group: ecommerce\r\n  service:\r\n    vgroup-mapping:\r\n      ecommerce: \"default\"','7d974bb565d07fef2fc5bb87a749e643','2025-02-09 19:11:18','2025-02-09 19:11:18','nacos','192.168.11.1','U','',''),(4,13,'knife4j-config.yaml','DEFAULT_GROUP','','knife4j:\r\n  enable: true\r\n  openapi:\r\n    title: ${ecommerce.knife4j.title:标题}\r\n    description: ${ecommerce.knife4j.description:描述}','c2186a809b822b0515f6631a4278bf21','2025-02-09 19:21:53','2025-02-09 19:21:54','nacos','172.18.0.1','U','',''),(8,14,'seata-config.yaml','DEFAULT_GROUP','','seata:\n  data-source-proxy-mode: AT\n  registry:\n    type: nacos\n    nacos:\n      server-addr: ${ecommerce.nacos.dashboard}\n      namespace: \"\"\n      group: DEFAULT_GROUP\n      application: seata-server\n      username: nacos\n      password: nacos\n  tx-service-group: ecommerce\n  service:\n    vgroup-mapping:\n      ecommerce: \"default\"','e2b824b3be3d9951a2ae112722924172','2025-02-09 22:50:22','2025-02-09 22:50:22','nacos','172.18.0.1','U','',''),(8,15,'seata-config.yaml','DEFAULT_GROUP','','seata:\n  enabled: true\n  data-source-proxy-mode: AT\n  registry:\n    type: nacos\n    nacos:\n      server-addr: ${ecommerce.nacos.dashboard}\n      namespace: \"\"\n      group: DEFAULT_GROUP\n      application: seata-server\n      username: nacos\n      password: nacos\n  # tx-service-group: ecommerce\n  # service:\n  #   vgroup-mapping:\n  #     ecommerce: \"default\"\n\n# seata:\n#   enabled: true\n#   # Seata 应用编号，默认为 ${spring.application.name}\n#   application-id: ${spring.application.name}\n#   # Seata 事务组编号，用于 TC 集群名\n#   tx-service-group: ${spring.application.name}-group\n#   # 关闭自动代理\n#   enable-auto-data-source-proxy: true\n#   # 服务配置项\n#   service:\n#     # 虚拟组和分组的映射\n#     vgroup-mapping:\n#       seata-user-group: default\n#   registry:\n#     type: nacos\n#     nacos:\n#       server-addr: ${ecommerce.nacos.dashboard}\n#       namespace: \"\"\n#       group: DEFAULT_GROUP\n#       application: seata-server\n#       username: nacos\n#       password: nacos','8746e74ac30560cc0d5441d25d92e6b4','2025-02-09 22:58:25','2025-02-09 22:58:26','nacos','172.18.0.1','U','',''),(8,16,'seata-config.yaml','DEFAULT_GROUP','','seata:\n  enabled: true\n  data-source-proxy-mode: AT\n  registry:\n    type: nacos\n    nacos:\n      server-addr: ${ecommerce.nacos.dashboard}\n      namespace: \"\"\n      group: DEFAULT_GROUP\n      application: seata-server\n      username: nacos\n      password: nacos\n  tx-service-group: my_test_tx_group\n  # tx-service-group: ecommerce\n  # service:\n  #   vgroup-mapping:\n  #     ecommerce: \"default\"\n\n# seata:\n#   enabled: true\n#   # Seata 应用编号，默认为 ${spring.application.name}\n#   application-id: ${spring.application.name}\n#   # Seata 事务组编号，用于 TC 集群名\n#   tx-service-group: ${spring.application.name}-group\n#   # 关闭自动代理\n#   enable-auto-data-source-proxy: true\n#   # 服务配置项\n#   service:\n#     # 虚拟组和分组的映射\n#     vgroup-mapping:\n#       seata-user-group: default\n#   registry:\n#     type: nacos\n#     nacos:\n#       server-addr: ${ecommerce.nacos.dashboard}\n#       namespace: \"\"\n#       group: DEFAULT_GROUP\n#       application: seata-server\n#       username: nacos\n#       password: nacos','2cfddc6f2621f277e526296770f5a9b9','2025-02-09 23:11:12','2025-02-09 23:11:12','nacos','172.18.0.1','U','',''),(8,17,'seata-config.yaml','DEFAULT_GROUP','','seata:\n  enabled: true\n  data-source-proxy-mode: AT\n  registry:\n    type: nacos\n    nacos:\n      server-addr: ${ecommerce.nacos.dashboard}\n      # namespace: \"\"\n      group: DEFAULT_GROUP\n      application: seata-server\n      username: nacos\n      password: nacos\n  tx-service-group: my_test_tx_group\n  tx-service-group: ecommerce\n  service:\n    vgroup-mapping:\n      ecommerce: default\n\n# seata:\n#   enabled: true\n#   # Seata 应用编号，默认为 ${spring.application.name}\n#   application-id: ${spring.application.name}\n#   # Seata 事务组编号，用于 TC 集群名\n#   tx-service-group: ${spring.application.name}-group\n#   # 关闭自动代理\n#   enable-auto-data-source-proxy: true\n#   # 服务配置项\n#   service:\n#     # 虚拟组和分组的映射\n#     vgroup-mapping:\n#       seata-user-group: default\n#   registry:\n#     type: nacos\n#     nacos:\n#       server-addr: ${ecommerce.nacos.dashboard}\n#       namespace: \"\"\n#       group: DEFAULT_GROUP\n#       application: seata-server\n#       username: nacos\n#       password: nacos','aa3fde1d74ab85bac8d9dd2bc6f3a718','2025-02-09 23:13:26','2025-02-09 23:13:26','nacos','172.18.0.1','U','',''),(8,18,'seata-config.yaml','DEFAULT_GROUP','','seata:\n  enabled: true\n  data-source-proxy-mode: AT\n  registry:\n    type: nacos\n    nacos:\n      server-addr: ${ecommerce.nacos.dashboard}\n      # namespace: \"\"\n      group: DEFAULT_GROUP\n      application: seata-server\n      username: nacos\n      password: nacos\n  tx-service-group: ecommerce\n  service:\n    vgroup-mapping:\n      ecommerce: default\n\n# seata:\n#   enabled: true\n#   # Seata 应用编号，默认为 ${spring.application.name}\n#   application-id: ${spring.application.name}\n#   # Seata 事务组编号，用于 TC 集群名\n#   tx-service-group: ${spring.application.name}-group\n#   # 关闭自动代理\n#   enable-auto-data-source-proxy: true\n#   # 服务配置项\n#   service:\n#     # 虚拟组和分组的映射\n#     vgroup-mapping:\n#       seata-user-group: default\n#   registry:\n#     type: nacos\n#     nacos:\n#       server-addr: ${ecommerce.nacos.dashboard}\n#       namespace: \"\"\n#       group: DEFAULT_GROUP\n#       application: seata-server\n#       username: nacos\n#       password: nacos','f78cb2733c38534b80a8269103af2a8e','2025-02-09 23:23:47','2025-02-09 23:23:47','nacos','172.18.0.1','U','',''),(8,19,'seata-config.yaml','DEFAULT_GROUP','','seata:\n  enabled: true\n  data-source-proxy-mode: AT\n  registry:\n    type: nacos\n    nacos:\n      server-addr: ${ecommerce.nacos.dashboard}\n      # namespace: \"\"\n      group: DEFAULT_GROUP\n      application: seata-server\n      username: nacos\n      password: nacos\n  tx-service-group: ecommerce\n  service:\n    vgroup-mapping:\n      ecommerce: default','060838a9a21f847c4835faa6c3270670','2025-02-09 23:50:55','2025-02-09 23:50:56',NULL,'172.18.0.1','U','',''),(8,20,'seata-config.yaml','DEFAULT_GROUP','','seata:\n  enabled: true\n  data-source-proxy-mode: AT\n  registry:\n    type: nacos\n    nacos:\n      server-addr: ${ecommerce.nacos.dashboard}\n      # namespace: \"\"\n      group: DEFAULT_GROUP\n      application: seata-server\n      username: nacos\n      password: nacos\n  tx-service-group: ecommerce\n  service:\n    vgroup-mapping:\n      ecommerce: default\n\n# seata:\n#   enabled: true\n#   # Seata 应用编号，默认为 ${spring.application.name}\n#   application-id: ${spring.application.name}\n#   # Seata 事务组编号，用于 TC 集群名\n#   tx-service-group: ${spring.application.name}-group\n#   # 关闭自动代理\n#   enable-auto-data-source-proxy: true\n#   # 服务配置项\n#   service:\n#     # 虚拟组和分组的映射\n#     vgroup-mapping:\n#       seata-user-group: default\n#   registry:\n#     type: nacos\n#     nacos:\n#       server-addr: ${ecommerce.nacos.dashboard}\n#       namespace: \"\"\n#       group: DEFAULT_GROUP\n#       application: seata-server\n#       username: nacos\n#       password: nacos','f78cb2733c38534b80a8269103af2a8e','2025-02-09 23:52:22','2025-02-09 23:52:22','nacos','172.18.0.1','U','',''),(8,21,'seata-config.yaml','DEFAULT_GROUP','','seata:\n  enabled: true\n  data-source-proxy-mode: AT\n  registry:\n    type: nacos\n    nacos:\n      server-addr: ${ecommerce.nacos.dashboard}\n      # namespace: \"\"\n      group: DEFAULT_GROUP\n      application: seata-server\n      username: nacos\n      password: nacos\n  tx-service-group: ecommerce\n  service:\n    vgroup-mapping:\n      ecommerce: default\n\n','93fd902d906bcfe185c1715deaf93e21','2025-02-09 23:52:57','2025-02-09 23:52:58','nacos','172.18.0.1','U','',''),(8,22,'seata-config.yaml','DEFAULT_GROUP','','seata:\n  enabled: true\n  data-source-proxy-mode: AT\n  registry:\n    type: nacos\n    nacos:\n      server-addr: ${ecommerce.nacos.dashboard}\n      # namespace: \"\"\n      group: DEFAULT_GROUP\n      application: seata-server\n      username: nacos\n      password: nacos\n  tx-service-group: ecommerce\n  service:\n    vgroup-mapping:\n      ecommerce: default','060838a9a21f847c4835faa6c3270670','2025-02-09 23:53:46','2025-02-09 23:53:47',NULL,'172.18.0.1','U','',''),(8,23,'seata-config.yaml','DEFAULT_GROUP','','seata:\n  enabled: true\n  data-source-proxy-mode: AT\n  registry:\n    type: nacos\n    nacos:\n      server-addr: ${ecommerce.nacos.dashboard}\n      # namespace: \"\"\n      group: DEFAULT_GROUP\n      application: seata-server\n      username: nacos\n      password: nacos\n  tx-service-group: ecommerce\n  service:\n    vgroup-mapping:\n      ecommerce: default\n\n','93fd902d906bcfe185c1715deaf93e21','2025-02-09 23:54:05','2025-02-09 23:54:05','nacos','172.18.0.1','U','',''),(0,24,'redis-config.yaml','DEFAULT_GROUP','','spring:\r\n  redis:\r\n    cluster:\r\n      nodes: ${ecommerce.redis.cluster-addr}\r\n    lettuce:\r\n      pool:\r\n        max-active: 8\r\n        max-idle: 8\r\n        min-idle: 0','b02ac41a76311e73bb0e9c7d787ab338','2025-02-11 02:16:07','2025-02-11 02:16:08',NULL,'172.18.0.1','I','',''),(2,25,'jdbc-config.yaml','DEFAULT_GROUP','','spring:\n  datasource:\n    url: jdbc:mysql://${ecommerce.database.host}:${ecommerce.database.port}/${ecommerce.database.table}?useUnicode=true&characterEncoding=UTF-8&autoReconnect=true&serverTimezone=Asia/Shanghai\n    username: ${ecommerce.database.user}\n    password: ${ecommerce.database.pwd}\n    driver-class-name: com.mysql.cj.jdbc.Driver\n\nmybatis-plus:\n  configuration:\n    default-enum-type-handler: com.baomidou.mybatisplus.core.handlers.MybatisEnumTypeHandler\n  global-config:\n    db-config:\n      update-strategy: not_null\n      id-type: assign_id','6ed3167a7239bae6c034691c633a453f','2025-02-11 02:32:13','2025-02-11 02:32:13','nacos','172.18.0.1','U','',''),(0,26,'rabbitmq-config.yaml','DEFAULT_GROUP','','spring:\r\n  rabbitmq:\r\n    host: ${ecommerce.rabbitmq.host}\r\n    port: ${ecommerce.rabbitmq.port}\r\n    virtual-host: ${ecommerce.rabbitmq.virtual}\r\n    username: ${ecommerce.rabbitmq.user}\r\n    password: ${ecommerce.rabbitmq.pwd}\r\n    listener:\r\n      simple:\r\n        prefetch: 1','635547cd03b7aa15ca171a760e87209a','2025-02-12 21:08:10','2025-02-12 21:08:10',NULL,'172.19.0.1','I','',''),(23,27,'rabbitmq-config.yaml','DEFAULT_GROUP','','spring:\r\n  rabbitmq:\r\n    host: ${ecommerce.rabbitmq.host}\r\n    port: ${ecommerce.rabbitmq.port}\r\n    virtual-host: ${ecommerce.rabbitmq.virtual}\r\n    username: ${ecommerce.rabbitmq.user}\r\n    password: ${ecommerce.rabbitmq.pwd}\r\n    listener:\r\n      simple:\r\n        prefetch: 1','635547cd03b7aa15ca171a760e87209a','2025-02-12 21:08:10','2025-02-12 21:08:10',NULL,'172.19.0.1','U','',''),(0,28,'jwt-config.yaml','DEFAULT_GROUP','','ecommerce:\n  jwt:\n    key: \"aB3$kL9@qW2^zX8&amp;pR7*\"\n    expire-hour: 1','35be35d444886528e90f4fdbeca5039b','2025-02-17 01:37:39','2025-02-17 01:37:39',NULL,'172.18.0.1','I','',''),(21,29,'redis-config.yaml','DEFAULT_GROUP','','spring:\r\n  redis:\r\n    cluster:\r\n      nodes: ${ecommerce.redis.cluster-addr}\r\n    lettuce:\r\n      pool:\r\n        max-active: 8\r\n        max-idle: 8\r\n        min-idle: 0','b02ac41a76311e73bb0e9c7d787ab338','2025-02-17 23:12:31','2025-02-17 23:12:31','nacos','172.18.0.1','U','',''),(21,30,'redis-config.yaml','DEFAULT_GROUP','','spring:\n  redis:\n    cluster:\n      nodes:\n        - vlsmb-kotori.local:7001\n        - vlsmb-kotori.local:7002\n        - vlsmb-kotori.local:7003\n        - vlsmb-kotori.local:7004\n        - vlsmb-kotori.local:7005\n        - vlsmb-kotori.local:7006\n    lettuce:\n      pool:\n        max-active: 8\n        max-idle: 8\n        min-idle: 0','465b05fad1fca216711e831bd60a584d','2025-02-17 23:22:15','2025-02-17 23:22:15',NULL,'172.18.0.1','U','',''),(25,31,'jwt-config.yaml','DEFAULT_GROUP','','ecommerce:\n  jwt:\n    key: \"aB3$kL9@qW2^zX8&amp;pR7*\"\n    expire-hour: 1','35be35d444886528e90f4fdbeca5039b','2025-02-21 10:55:41','2025-02-21 10:55:41','nacos','172.18.0.1','U','',''),(0,32,'mq-queues-config.yaml','DEFAULT_GROUP','','ecommerce:\n  rabbitmq:\n    exchange-name: \"directExchange\"\n    queues:\n      pay:\n        start: \"pay.start\"\n        cancel: \"pay.cancel\"\n        fail: \"pay.fail\"\n        success: \"success\"','6cbfe5f9cc8f9f723ea0ab9887fea3da','2025-02-22 17:22:18','2025-02-22 17:22:19',NULL,'172.18.0.1','I','',''),(29,33,'mq-queues-config.yaml','DEFAULT_GROUP','','ecommerce:\n  rabbitmq:\n    exchange-name: \"directExchange\"\n    queues:\n      pay:\n        start: \"pay.start\"\n        cancel: \"pay.cancel\"\n        fail: \"pay.fail\"\n        success: \"success\"','6cbfe5f9cc8f9f723ea0ab9887fea3da','2025-03-01 00:27:38','2025-03-01 00:27:38','nacos','172.18.0.1','U','',''),(0,34,'elasticsearch-config.yaml','DEFAULT_GROUP','','ecommerce:\n  elasticsearch:\n    connect-timeout: 5000\n    socket-timeout: 10000','4882f840b8fe5d4a97224c676982b3bb','2025-03-01 23:47:12','2025-03-01 23:47:12',NULL,'172.18.0.1','I','',''),(23,35,'rabbitmq-config.yaml','DEFAULT_GROUP','','spring:\r\n  rabbitmq:\r\n    host: ${ecommerce.rabbitmq.host}\r\n    port: ${ecommerce.rabbitmq.port}\r\n    virtual-host: ${ecommerce.rabbitmq.virtual}\r\n    username: ${ecommerce.rabbitmq.user}\r\n    password: ${ecommerce.rabbitmq.pwd}\r\n    listener:\r\n      simple:\r\n        prefetch: 1','635547cd03b7aa15ca171a760e87209a','2025-03-03 10:03:40','2025-03-03 10:03:41','nacos','172.18.0.1','U','',''),(23,36,'rabbitmq-config.yaml','DEFAULT_GROUP','','spring:\n  rabbitmq:\n    host: ${ecommerce.rabbitmq.host}\n    port: ${ecommerce.rabbitmq.port}\n    virtual-host: ${ecommerce.rabbitmq.virtual}\n    username: ${ecommerce.rabbitmq.user}\n    password: ${ecommerce.rabbitmq.pwd}\n    listener:\n      simple:\n        prefetch: 1\n\n    publisher-confirm-type: correlated','6ffab6b09a95ef4cec9c6b1931bc4f2a','2025-03-03 14:01:37','2025-03-03 14:01:37','nacos','172.18.0.1','U','',''),(2,37,'jdbc-config.yaml','DEFAULT_GROUP','','spring:\n  datasource:\n    url: jdbc:mysql://${ecommerce.database.host}:${ecommerce.database.port}/${ecommerce.database.table}?useUnicode=true&characterEncoding=UTF-8&autoReconnect=true&serverTimezone=Asia/Shanghai\n    username: ${ecommerce.database.user}\n    password: ${ecommerce.database.pwd}\n    driver-class-name: com.mysql.cj.jdbc.Driver\n\nmybatis-plus:\n  configuration:\n    default-enum-type-handler: com.baomidou.mybatisplus.core.handlers.MybatisEnumTypeHandler\n    cache-enabled: true\n  global-config:\n    db-config:\n      update-strategy: not_null\n      id-type: assign_id','f340a1fff232a8f245a54e397cf05a0a','2025-03-04 20:26:01','2025-03-04 20:26:02','nacos','172.18.0.1','U','','');
/*!40000 ALTER TABLE `his_config_info` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `permissions`
--

DROP TABLE IF EXISTS `permissions`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `permissions` (
  `role` varchar(50) COLLATE utf8mb4_general_ci NOT NULL,
  `resource` varchar(255) COLLATE utf8mb4_general_ci NOT NULL,
  `action` varchar(8) COLLATE utf8mb4_general_ci NOT NULL,
  UNIQUE KEY `uk_role_permission` (`role`,`resource`,`action`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `permissions`
--

LOCK TABLES `permissions` WRITE;
/*!40000 ALTER TABLE `permissions` DISABLE KEYS */;
/*!40000 ALTER TABLE `permissions` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `roles`
--

DROP TABLE IF EXISTS `roles`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `roles` (
  `username` varchar(50) COLLATE utf8mb4_general_ci NOT NULL,
  `role` varchar(50) COLLATE utf8mb4_general_ci NOT NULL,
  UNIQUE KEY `idx_user_role` (`username`,`role`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `roles`
--

LOCK TABLES `roles` WRITE;
/*!40000 ALTER TABLE `roles` DISABLE KEYS */;
INSERT INTO `roles` VALUES ('nacos','ROLE_ADMIN');
/*!40000 ALTER TABLE `roles` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tenant_capacity`
--

DROP TABLE IF EXISTS `tenant_capacity`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `tenant_capacity` (
  `id` bigint unsigned NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `tenant_id` varchar(128) COLLATE utf8mb3_bin NOT NULL DEFAULT '' COMMENT 'Tenant ID',
  `quota` int unsigned NOT NULL DEFAULT '0' COMMENT '配额，0表示使用默认值',
  `usage` int unsigned NOT NULL DEFAULT '0' COMMENT '使用量',
  `max_size` int unsigned NOT NULL DEFAULT '0' COMMENT '单个配置大小上限，单位为字节，0表示使用默认值',
  `max_aggr_count` int unsigned NOT NULL DEFAULT '0' COMMENT '聚合子配置最大个数',
  `max_aggr_size` int unsigned NOT NULL DEFAULT '0' COMMENT '单个聚合数据的子配置大小上限，单位为字节，0表示使用默认值',
  `max_history_count` int unsigned NOT NULL DEFAULT '0' COMMENT '最大变更历史数量',
  `gmt_create` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `gmt_modified` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '修改时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_tenant_id` (`tenant_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3 COLLATE=utf8mb3_bin COMMENT='租户容量信息表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tenant_capacity`
--

LOCK TABLES `tenant_capacity` WRITE;
/*!40000 ALTER TABLE `tenant_capacity` DISABLE KEYS */;
/*!40000 ALTER TABLE `tenant_capacity` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tenant_info`
--

DROP TABLE IF EXISTS `tenant_info`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `tenant_info` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT 'id',
  `kp` varchar(128) COLLATE utf8mb3_bin NOT NULL COMMENT 'kp',
  `tenant_id` varchar(128) COLLATE utf8mb3_bin DEFAULT '' COMMENT 'tenant_id',
  `tenant_name` varchar(128) COLLATE utf8mb3_bin DEFAULT '' COMMENT 'tenant_name',
  `tenant_desc` varchar(256) COLLATE utf8mb3_bin DEFAULT NULL COMMENT 'tenant_desc',
  `create_source` varchar(32) COLLATE utf8mb3_bin DEFAULT NULL COMMENT 'create_source',
  `gmt_create` bigint NOT NULL COMMENT '创建时间',
  `gmt_modified` bigint NOT NULL COMMENT '修改时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_tenant_info_kptenantid` (`kp`,`tenant_id`),
  KEY `idx_tenant_id` (`tenant_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3 COLLATE=utf8mb3_bin COMMENT='tenant_info';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tenant_info`
--

LOCK TABLES `tenant_info` WRITE;
/*!40000 ALTER TABLE `tenant_info` DISABLE KEYS */;
/*!40000 ALTER TABLE `tenant_info` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `users`
--

DROP TABLE IF EXISTS `users`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `users` (
  `username` varchar(50) COLLATE utf8mb4_general_ci NOT NULL,
  `password` varchar(500) COLLATE utf8mb4_general_ci NOT NULL,
  `enabled` tinyint(1) NOT NULL,
  PRIMARY KEY (`username`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `users`
--

LOCK TABLES `users` WRITE;
/*!40000 ALTER TABLE `users` DISABLE KEYS */;
INSERT INTO `users` VALUES ('nacos','$2a$10$EuWPZHzz32dJN7jexM34MOeYirDdFAZm2kuWj7VEOJhhZkDrxfvUu',1);
/*!40000 ALTER TABLE `users` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2025-03-05 16:09:38

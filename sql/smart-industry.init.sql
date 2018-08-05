CREATE DATABASE  IF NOT EXISTS `smart-industry` /*!40100 DEFAULT CHARACTER SET utf8 */;
USE `smart-industry`;
-- MySQL dump 10.13  Distrib 5.7.12, for Win64 (x86_64)
--
-- Host: localhost    Database: smart-industry
-- ------------------------------------------------------
-- Server version	5.7.15-log

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `design_class`
--

DROP TABLE IF EXISTS `design_class`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `design_class` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `createdate` date DEFAULT NULL COMMENT '创建时间',
  `createby` int(11) DEFAULT NULL COMMENT '创建人',
  `modifydate` date DEFAULT NULL COMMENT '修改时间',
  `modifyby` int(11) DEFAULT NULL COMMENT '修改人',
  `name` varchar(200) DEFAULT NULL COMMENT '分类名',
  `pId` int(11) DEFAULT NULL COMMENT '父级id',
  `sort` int(11) DEFAULT NULL COMMENT '排序码(系统自动创建)',
  PRIMARY KEY (`id`),
  UNIQUE KEY `id_UNIQUE` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=13 DEFAULT CHARSET=utf8 COMMENT='方案分类';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `design_class`
--

LOCK TABLES `design_class` WRITE;
/*!40000 ALTER TABLE `design_class` DISABLE KEYS */;
INSERT INTO `design_class` VALUES (1,'2018-08-04',1,NULL,NULL,'解决方案中心',NULL,0),(2,'2018-08-04',1,NULL,NULL,'设计一部',1,0),(3,'2018-08-04',1,NULL,NULL,'设计二部',1,1),(4,'2018-08-04',1,NULL,NULL,'车灯设计',2,2),(5,'2018-08-04',1,NULL,NULL,'底盘设计',3,2),(6,'2018-08-04',1,NULL,NULL,'升降弓设计',3,3),(7,'2018-08-04',1,NULL,NULL,'尾灯设计',3,1),(8,'2018-08-04',1,'2018-08-04',1,'车厢设计',2,1),(9,'2018-08-04',1,'2018-08-04',1,'一部底盘',2,0),(10,'2018-08-04',1,NULL,NULL,'我的分类',3,0);
/*!40000 ALTER TABLE `design_class` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user`
--

DROP TABLE IF EXISTS `user`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `user` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(50) DEFAULT NULL,
  `psw` varchar(50) DEFAULT NULL,
  `sex` bit(2) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `id_UNIQUE` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user`
--

LOCK TABLES `user` WRITE;
/*!40000 ALTER TABLE `user` DISABLE KEYS */;
INSERT INTO `user` VALUES (1,'张三','123',''),(2,'李四','123','');
/*!40000 ALTER TABLE `user` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2018-08-05 13:59:40

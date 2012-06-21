-- MySQL dump 10.13  Distrib 5.5.16, for osx10.5 (i386)
--
-- Host: 127.0.0.1    Database: servicedb
-- ------------------------------------------------------
-- Server version	5.5.16

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
-- Current Database: `servicedb`
--

CREATE DATABASE /*!32312 IF NOT EXISTS*/ `servicedb` /*!40100 DEFAULT CHARACTER SET utf8 */;

USE `servicedb`;

--
-- Table structure for table `customers`
--

DROP TABLE IF EXISTS `customers`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `customers` (
  `id` int(10) NOT NULL COMMENT '	',
  `name` varchar(45) DEFAULT NULL,
  `phone` varchar(45) DEFAULT NULL,
  `fax` varchar(45) DEFAULT NULL,
  `email` varchar(45) DEFAULT NULL,
  `contact_person` varchar(45) DEFAULT NULL,
  `code` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `customers`
--

LOCK TABLES `customers` WRITE;
/*!40000 ALTER TABLE `customers` DISABLE KEYS */;
INSERT INTO `customers` VALUES (10027,'SOLAAPPS','98381267','','ivanagxu@gmail.com','Ivan Xu','C001');
/*!40000 ALTER TABLE `customers` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `department`
--

DROP TABLE IF EXISTS `department`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `department` (
  `id` int(10) NOT NULL,
  `name` varchar(100) NOT NULL,
  `remark` varchar(1000) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `department`
--

LOCK TABLES `department` WRITE;
/*!40000 ALTER TABLE `department` DISABLE KEYS */;
INSERT INTO `department` VALUES (10001,'力劲',''),(10002,'Ohtune','');
/*!40000 ALTER TABLE `department` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `division`
--

DROP TABLE IF EXISTS `division`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `division` (
  `id` int(10) NOT NULL,
  `name` varchar(100) NOT NULL,
  `department` int(10) NOT NULL,
  `remark` varchar(1000) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `division`
--

LOCK TABLES `division` WRITE;
/*!40000 ALTER TABLE `division` DISABLE KEYS */;
INSERT INTO `division` VALUES (10001,'办公室',10001,''),(10002,'工厂',10001,'');
/*!40000 ALTER TABLE `division` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `job_types`
--

DROP TABLE IF EXISTS `job_types`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `job_types` (
  `name` varchar(255) NOT NULL,
  `role` int(10) DEFAULT NULL,
  `status` varchar(45) DEFAULT NULL,
  `remark` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`name`),
  UNIQUE KEY `name_UNIQUE` (`name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `job_types`
--

LOCK TABLES `job_types` WRITE;
/*!40000 ALTER TABLE `job_types` DISABLE KEYS */;
INSERT INTO `job_types` VALUES ('仓库',10002,'有效','仓库部门'),('仪表',10007,'有效','仪表部门'),('包装',10014,'有效','包装部门'),('半成品',10015,'有效','半成品仓'),('压铸',10006,'有效','压铸部门'),('发货',10012,'有效','发货部门'),('外生产',10020,'有效','外生产部门'),('手工',10009,'有效','手工部门'),('抛光',10013,'有效','抛光部门'),('数控',10021,'有效','数控部门'),('机加',10008,'有效','机加部门'),('模具',10005,'有效','模具部门'),('电镀',10011,'有效','电镀部门'),('质检',10017,'有效','质检部门'),('过沙',10010,'有效','过沙部门');
/*!40000 ALTER TABLE `job_types` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `jobs`
--

DROP TABLE IF EXISTS `jobs`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `jobs` (
  `id` int(10) NOT NULL,
  `orders` int(10) DEFAULT NULL COMMENT '	',
  `user` int(10) DEFAULT NULL COMMENT '	',
  `job_type` varchar(255) DEFAULT NULL,
  `total` int(10) NOT NULL DEFAULT '0',
  `finished` int(10) NOT NULL DEFAULT '0',
  `status` varchar(45) DEFAULT NULL,
  `complete_date` datetime DEFAULT NULL,
  `start_date` datetime DEFAULT NULL,
  `deadline` datetime DEFAULT NULL,
  `finish_remark` varchar(255) DEFAULT NULL,
  `remaining` int(10) NOT NULL DEFAULT '0',
  `total_rejected` int(10) NOT NULL DEFAULT '0',
  `assigned_to` int(10) DEFAULT NULL,
  `previous_jobid` int(10) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `jobs`
--

LOCK TABLES `jobs` WRITE;
/*!40000 ALTER TABLE `jobs` DISABLE KEYS */;
INSERT INTO `jobs` VALUES (10297,10187,10007,'压铸',1,0,'进行中',NULL,'2012-05-29 19:51:18','2012-05-29 00:00:00',NULL,1,0,NULL,NULL);
/*!40000 ALTER TABLE `jobs` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `molds`
--

DROP TABLE IF EXISTS `molds`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `molds` (
  `code` varchar(255) NOT NULL,
  `name` varchar(255) NOT NULL,
  `stand_no` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `molds`
--

LOCK TABLES `molds` WRITE;
/*!40000 ALTER TABLE `molds` DISABLE KEYS */;
/*!40000 ALTER TABLE `molds` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `orders`
--

DROP TABLE IF EXISTS `orders`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `orders` (
  `id` int(10) NOT NULL,
  `number` varchar(45) DEFAULT NULL,
  `creator` varchar(255) DEFAULT NULL,
  `product_name` varchar(255) DEFAULT NULL,
  `requirement_1` varchar(1000) DEFAULT NULL,
  `requirement_2` varchar(1000) DEFAULT NULL,
  `requirement_3` varchar(1000) DEFAULT NULL,
  `requirement_4` varchar(1000) DEFAULT NULL,
  `create_date` datetime DEFAULT NULL,
  `deadline` datetime DEFAULT NULL,
  `status` varchar(45) DEFAULT NULL,
  `customer_name` varchar(255) DEFAULT NULL,
  `customer_code` varchar(45) DEFAULT NULL,
  `product_our_name` varchar(45) DEFAULT NULL,
  `quantity` int(11) NOT NULL DEFAULT '0',
  `use_finished` int(11) NOT NULL DEFAULT '0',
  `use_semi_finished` int(11) NOT NULL DEFAULT '0',
  `c_deadline` datetime DEFAULT NULL,
  `product_rate` float DEFAULT NULL,
  `e_quantity` varchar(45) DEFAULT '0',
  `priority` int(10) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `orders`
--

LOCK TABLES `orders` WRITE;
/*!40000 ALTER TABLE `orders` DISABLE KEYS */;
INSERT INTO `orders` VALUES (10187,'20120529-001','administrator','SOLAAPP','','','','','2012-05-29 19:50:55','2012-05-29 00:00:00','进行中','SOLAAPPS','C001','SOLAAPP',1,0,0,'2012-05-29 00:00:00',1,'1',0);
/*!40000 ALTER TABLE `orders` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `post`
--

DROP TABLE IF EXISTS `post`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `post` (
  `id` int(10) NOT NULL,
  `name` varchar(100) NOT NULL,
  `remark` varchar(1000) DEFAULT NULL,
  `section` int(10) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `post`
--

LOCK TABLES `post` WRITE;
/*!40000 ALTER TABLE `post` DISABLE KEYS */;
INSERT INTO `post` VALUES (10008,'系统管理员',NULL,10001);
/*!40000 ALTER TABLE `post` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `post_role`
--

DROP TABLE IF EXISTS `post_role`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `post_role` (
  `post_id` int(10) NOT NULL,
  `role_id` int(10) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `post_role`
--

LOCK TABLES `post_role` WRITE;
/*!40000 ALTER TABLE `post_role` DISABLE KEYS */;
INSERT INTO `post_role` VALUES (10008,10001);
/*!40000 ALTER TABLE `post_role` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `productlog`
--

DROP TABLE IF EXISTS `productlog`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `productlog` (
  `section_name` varchar(255) NOT NULL,
  `product_name` varchar(255) NOT NULL,
  `quantity` int(10) NOT NULL,
  `process_date` datetime NOT NULL,
  `handled_by` varchar(255) DEFAULT NULL,
  `process_type` varchar(10) DEFAULT NULL,
  `product_our_name` varchar(255) DEFAULT NULL,
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `jobid` int(10) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `id_UNIQUE` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=73 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `productlog`
--

LOCK TABLES `productlog` WRITE;
/*!40000 ALTER TABLE `productlog` DISABLE KEYS */;
/*!40000 ALTER TABLE `productlog` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `products`
--

DROP TABLE IF EXISTS `products`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `products` (
  `name` varchar(255) NOT NULL,
  `name_eng` varchar(255) DEFAULT NULL,
  `status` varchar(45) NOT NULL,
  `image` varchar(500) DEFAULT NULL,
  `drawing` varchar(500) DEFAULT NULL,
  `our_name` varchar(255) DEFAULT NULL,
  `mold_rate` varchar(45) DEFAULT NULL,
  `machining_pos` varchar(45) DEFAULT NULL,
  `handwork_pos` varchar(45) DEFAULT NULL,
  `polishing` varchar(45) DEFAULT NULL,
  `finished` int(10) NOT NULL DEFAULT '0',
  `semi_finished` int(10) NOT NULL DEFAULT '0',
  `mold` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `products`
--

LOCK TABLES `products` WRITE;
/*!40000 ALTER TABLE `products` DISABLE KEYS */;
INSERT INTO `products` VALUES ('SOLAAPP','','有效','','','SOLAAPP','','','','',0,0,NULL);
/*!40000 ALTER TABLE `products` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `role`
--

DROP TABLE IF EXISTS `role`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `role` (
  `id` int(10) NOT NULL,
  `name` varchar(100) NOT NULL,
  `parent_role` int(10) DEFAULT NULL,
  `remark` varchar(1000) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `role`
--

LOCK TABLES `role` WRITE;
/*!40000 ALTER TABLE `role` DISABLE KEYS */;
INSERT INTO `role` VALUES (10001,'管理员',NULL,''),(10002,'仓库',NULL,''),(10003,'经理',NULL,''),(10004,'销售部',NULL,''),(10005,'模具',NULL,''),(10006,'压铸部',NULL,''),(10007,'仪表',NULL,''),(10008,'机加',NULL,''),(10009,'手工',NULL,''),(10010,'过沙',NULL,''),(10011,'电镀',NULL,''),(10012,'发货',NULL,''),(10013,'抛光',NULL,''),(10014,'包装',NULL,''),(10015,'半成品',NULL,''),(10016,'普通员工',NULL,''),(10017,'质检',NULL,''),(10018,'厂长',NULL,''),(10019,'生产部',NULL,''),(10020,'外生产',NULL,''),(10021,'数控',NULL,'');
/*!40000 ALTER TABLE `role` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `section`
--

DROP TABLE IF EXISTS `section`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `section` (
  `id` int(10) NOT NULL,
  `name` varchar(100) NOT NULL,
  `division` int(10) NOT NULL,
  `remark` varchar(1000) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `section`
--

LOCK TABLES `section` WRITE;
/*!40000 ALTER TABLE `section` DISABLE KEYS */;
INSERT INTO `section` VALUES (10001,'默认部门',10001,'');
/*!40000 ALTER TABLE `section` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sequence`
--

DROP TABLE IF EXISTS `sequence`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `sequence` (
  `name` varchar(45) NOT NULL,
  `value` int(10) DEFAULT NULL,
  `remark` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`name`),
  UNIQUE KEY `name_UNIQUE` (`name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sequence`
--

LOCK TABLES `sequence` WRITE;
/*!40000 ALTER TABLE `sequence` DISABLE KEYS */;
INSERT INTO `sequence` VALUES ('CustomerID',10027,''),('DepartmentID',10002,''),('DivisionID',10002,''),('JobID',10297,''),('OrderID',10187,''),('PostID',10027,''),('RoleID',10021,''),('SectionID',10002,''),('UserACID',10027,'');
/*!40000 ALTER TABLE `sequence` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `userac`
--

DROP TABLE IF EXISTS `userac`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `userac` (
  `id` int(10) NOT NULL,
  `login_id` varchar(20) NOT NULL,
  `password` varchar(255) NOT NULL,
  `name` varchar(255) NOT NULL,
  `employ_date` datetime DEFAULT NULL,
  `status` varchar(10) NOT NULL COMMENT '1 enabled, 0 disabled',
  `create_date` datetime DEFAULT NULL,
  `update_date` datetime DEFAULT NULL,
  `email` varchar(100) DEFAULT NULL,
  `birthday` date DEFAULT NULL,
  `sex` varchar(10) NOT NULL COMMENT '0 Male, 1 Female',
  `salary` float DEFAULT NULL COMMENT 'per month',
  `post` int(10) DEFAULT NULL,
  `remark` varchar(1000) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=COMPRESSED;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `userac`
--

LOCK TABLES `userac` WRITE;
/*!40000 ALTER TABLE `userac` DISABLE KEYS */;
INSERT INTO `userac` VALUES (10007,'admin','password','administrator',NULL,'有效','2011-11-20 16:22:32','2011-11-20 16:22:32','admin@solaapps.tk',NULL,'男',NULL,10008,NULL);
/*!40000 ALTER TABLE `userac` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2012-05-29 19:52:08

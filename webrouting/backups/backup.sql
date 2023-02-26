-- MySQL dump 10.13  Distrib 8.0.30, for Win64 (x86_64)
--
-- Host: localhost    Database: webrouting
-- ------------------------------------------------------
-- Server version	8.0.30

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
-- Current Database: `webrouting`
--

CREATE DATABASE /*!32312 IF NOT EXISTS*/ `webrouting` /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci */ /*!80016 DEFAULT ENCRYPTION='N' */;

USE `webrouting`;

--
-- Table structure for table `bids`
--

DROP TABLE IF EXISTS `bids`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `bids` (
  `id` bigint NOT NULL,
  `date` varchar(10) DEFAULT NULL,
  `price` varchar(16) DEFAULT NULL,
  `time` varchar(11) DEFAULT NULL,
  `carrier_id` bigint DEFAULT NULL,
  `shipment_id` bigint DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKf3xb3vt53knnaqxh1kv32m26t` (`carrier_id`),
  KEY `FK5mb835fr3iylp1thi0lib4ynk` (`shipment_id`),
  CONSTRAINT `FK5mb835fr3iylp1thi0lib4ynk` FOREIGN KEY (`shipment_id`) REFERENCES `shipments` (`id`),
  CONSTRAINT `FKf3xb3vt53knnaqxh1kv32m26t` FOREIGN KEY (`carrier_id`) REFERENCES `carriers` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `bids`
--

LOCK TABLES `bids` WRITE;
/*!40000 ALTER TABLE `bids` DISABLE KEYS */;
INSERT INTO `bids` VALUES (1,'2022-04-20','$50.00','11:03:58',1,3),(2,'2022-04-20','$150.00','11:52:58',1,2),(3,'2022-04-20','$700.00','12:23:58',1,1);
/*!40000 ALTER TABLE `bids` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `carriers`
--

DROP TABLE IF EXISTS `carriers`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `carriers` (
  `id` bigint NOT NULL,
  `carrier_name` varchar(128) DEFAULT NULL,
  `ftl` varchar(3) DEFAULT NULL,
  `ltl` varchar(3) DEFAULT NULL,
  `pallets` varchar(32) DEFAULT NULL,
  `scac` varchar(4) DEFAULT NULL,
  `weight` varchar(32) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `carriers`
--

LOCK TABLES `carriers` WRITE;
/*!40000 ALTER TABLE `carriers` DISABLE KEYS */;
INSERT INTO `carriers` VALUES (1,'THANGIAH SHIPPING','Yes','Yes','24','GZBO','40000'),(2,'WONKA SHIPPING','Yes','Yes','24','COCO','40000'),(3,'WE SHIP 4 U','Yes','Yes','24','SH1P','40000');
/*!40000 ALTER TABLE `carriers` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `contacts`
--

DROP TABLE IF EXISTS `contacts`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `contacts` (
  `id` bigint NOT NULL,
  `city` varchar(64) DEFAULT NULL,
  `email_address` varchar(64) DEFAULT NULL,
  `first_name` varchar(32) DEFAULT NULL,
  `last_name` varchar(32) DEFAULT NULL,
  `middle_initial` varchar(1) DEFAULT NULL,
  `primary_phone` varchar(64) DEFAULT NULL,
  `state` varchar(64) DEFAULT NULL,
  `street_address1` varchar(128) DEFAULT NULL,
  `street_address2` varchar(64) DEFAULT NULL,
  `work_phone` varchar(64) DEFAULT NULL,
  `zip` varchar(12) DEFAULT NULL,
  `carrier_id` bigint DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKsqhjjcul70wfk3wj8jhn0va4x` (`carrier_id`),
  CONSTRAINT `FKsqhjjcul70wfk3wj8jhn0va4x` FOREIGN KEY (`carrier_id`) REFERENCES `carriers` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `contacts`
--

LOCK TABLES `contacts` WRITE;
/*!40000 ALTER TABLE `contacts` DISABLE KEYS */;
INSERT INTO `contacts` VALUES (1,'kittanning','imbwwe@yahoo.com','Ian','Black','M','7248411924','PA','143 roundtop drive','','NA','16201',1),(2,'Ford City','BobBuilder@yahoo.com','Bob','Builder','B','7248889900','PA','153 building drive','','N/A','16226',1),(3,'Kittanning','CocoFactory@yahoo.com','Willy','Wonka','C','7571234567','PA','chocolate','','N/A','15252',1),(4,'Butler','john@yahoo.com','John','Driver','B','5555555555','PA','724 HereILive','','N/A','345345',2),(5,'West Kittanning','Phil@yahoo.com','Phil','Drove','C','1234567890','PA','444 drove road','','N/A','16201',2),(6,'Latrobe','Rob@yahoo.com','Rob','Wheeler','M','9990009898','PA','678 peachtree','','N/A','12376',2),(7,'Ford City','Rich@yahoo.com','Rich','White','','1114446677','PA','555 Thisway','','','234234',3),(8,'Butler','Ronnie@yahoo.com','Ronnie','Brown','C','724-545-7676','PA','909 whitemoon road','','','13456',3),(9,'Ford City','WandaTech@yahoo.com','Wanda','Dots','C','7248597676','PA','153 Window Road','','','16226',3),(10,'Slippery Rock','sam.thangiah@sru.edu','Sam','Thangiah','','1234567890','PA','1 Morrow Way','Gazebo','','16057',1);
/*!40000 ALTER TABLE `contacts` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `driver`
--

DROP TABLE IF EXISTS `driver`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `driver` (
  `id` bigint NOT NULL,
  `license_class` varchar(12) DEFAULT NULL,
  `license_expiration` varchar(12) DEFAULT NULL,
  `license_number` varchar(32) DEFAULT NULL,
  `carrier_id` bigint DEFAULT NULL,
  `contact_id` bigint DEFAULT NULL,
  `vehicle_id` bigint DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK32ykh20dfjyfys1h3p2683dt5` (`carrier_id`),
  KEY `FK98tmt19amigmfqjgrfrkvp77i` (`contact_id`),
  KEY `FKawwh9v2jt72ns1w2u51lc3moj` (`vehicle_id`),
  CONSTRAINT `FK32ykh20dfjyfys1h3p2683dt5` FOREIGN KEY (`carrier_id`) REFERENCES `carriers` (`id`),
  CONSTRAINT `FK98tmt19amigmfqjgrfrkvp77i` FOREIGN KEY (`contact_id`) REFERENCES `contacts` (`id`),
  CONSTRAINT `FKawwh9v2jt72ns1w2u51lc3moj` FOREIGN KEY (`vehicle_id`) REFERENCES `vehicles` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `driver`
--

LOCK TABLES `driver` WRITE;
/*!40000 ALTER TABLE `driver` DISABLE KEYS */;
INSERT INTO `driver` VALUES (1,'A','2022-11-16','384-374',1,10,1),(2,'B','2022-02-08','434-555',2,6,2),(3,'A','2022-02-26','454-090',3,9,3);
/*!40000 ALTER TABLE `driver` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `locations`
--

DROP TABLE IF EXISTS `locations`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `locations` (
  `id` bigint NOT NULL,
  `city` varchar(64) DEFAULT NULL,
  `latitude` varchar(12) DEFAULT NULL,
  `location_type` varchar(64) DEFAULT NULL,
  `longitude` varchar(12) DEFAULT NULL,
  `name` varchar(32) DEFAULT NULL,
  `state` varchar(64) DEFAULT NULL,
  `street_address1` varchar(128) DEFAULT NULL,
  `street_address2` varchar(64) DEFAULT NULL,
  `zip` varchar(12) DEFAULT NULL,
  `carrier_id` bigint DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKp3g4bqr62h6wcf1ee3t48p5pl` (`carrier_id`),
  CONSTRAINT `FKp3g4bqr62h6wcf1ee3t48p5pl` FOREIGN KEY (`carrier_id`) REFERENCES `carriers` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `locations`
--

LOCK TABLES `locations` WRITE;
/*!40000 ALTER TABLE `locations` DISABLE KEYS */;
INSERT INTO `locations` VALUES (1,'Butler','','Workshop','','Factory','PA','111 Chocolate street','','12121',2),(2,'Kittanning','','Office','','Ians House','Pennsylvania','143 roundtop drive','','16201',1),(3,'Pittsburgh ','','warehouse','','LogHouse','PA','654 tree lane','','12345',3);
/*!40000 ALTER TABLE `locations` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `maintenance_orders`
--

DROP TABLE IF EXISTS `maintenance_orders`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `maintenance_orders` (
  `id` bigint NOT NULL,
  `cost` varchar(16) DEFAULT NULL,
  `details` varchar(128) DEFAULT NULL,
  `maintenance_type` varchar(64) NOT NULL,
  `scheduled_date` varchar(12) DEFAULT NULL,
  `service_type_key` varchar(12) NOT NULL,
  `status_key` varchar(64) NOT NULL,
  `carrier_id` bigint DEFAULT NULL,
  `technician_id` bigint DEFAULT NULL,
  `vehicle_id` bigint DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK89fn6g5o0x8krv0a1yuvm08iw` (`carrier_id`),
  KEY `FKmtnuyw4krhodcc0pyf2sb3q0u` (`technician_id`),
  KEY `FKgn5axos0swaggitftos8t73y` (`vehicle_id`),
  CONSTRAINT `FK89fn6g5o0x8krv0a1yuvm08iw` FOREIGN KEY (`carrier_id`) REFERENCES `carriers` (`id`),
  CONSTRAINT `FKgn5axos0swaggitftos8t73y` FOREIGN KEY (`vehicle_id`) REFERENCES `vehicles` (`id`),
  CONSTRAINT `FKmtnuyw4krhodcc0pyf2sb3q0u` FOREIGN KEY (`technician_id`) REFERENCES `technicians` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `maintenance_orders`
--

LOCK TABLES `maintenance_orders` WRITE;
/*!40000 ALTER TABLE `maintenance_orders` DISABLE KEYS */;
INSERT INTO `maintenance_orders` VALUES (1,'$5000.45','Brake Replacement','Brakes','2022-02-26','Repair','Pending',1,2,2),(2,'$50.00','Oil Change','Oil','2022-02-23','Maintenance','Completed ',1,1,3);
/*!40000 ALTER TABLE `maintenance_orders` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `notifications`
--

DROP TABLE IF EXISTS `notifications`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `notifications` (
  `id` bigint NOT NULL,
  `isread` bit(1) NOT NULL DEFAULT b'0',
  `message` varchar(225) NOT NULL,
  `timesent` varchar(225) NOT NULL,
  `user_id` bigint DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK3dt2b80521eynbjg4nehtjnhy` (`user_id`),
  CONSTRAINT `FK3dt2b80521eynbjg4nehtjnhy` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `notifications`
--

LOCK TABLES `notifications` WRITE;
/*!40000 ALTER TABLE `notifications` DISABLE KEYS */;
/*!40000 ALTER TABLE `notifications` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `role`
--

DROP TABLE IF EXISTS `role`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `role` (
  `id` bigint NOT NULL,
  `name` varchar(24) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK_8sewwnpamngi6b1dwaa88askk` (`name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `role`
--

LOCK TABLES `role` WRITE;
/*!40000 ALTER TABLE `role` DISABLE KEYS */;
INSERT INTO `role` VALUES (1,'ADMIN'),(3,'CARRIER'),(4,'MASTERLIST'),(5,'SHADOWADMIN'),(2,'SHIPPER');
/*!40000 ALTER TABLE `role` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `shipments`
--

DROP TABLE IF EXISTS `shipments`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `shipments` (
  `id` bigint NOT NULL,
  `client` varchar(64) NOT NULL,
  `client_mode` varchar(3) NOT NULL,
  `commodity_class` varchar(12) NOT NULL,
  `commodity_paid_weight` varchar(16) NOT NULL,
  `commodity_pieces` varchar(64) NOT NULL,
  `consignee_city` varchar(64) NOT NULL,
  `consignee_latitude` varchar(12) DEFAULT NULL,
  `consignee_longitude` varchar(12) DEFAULT NULL,
  `consignee_state` varchar(64) NOT NULL,
  `consignee_zip` varchar(12) NOT NULL,
  `freightbill_number` varchar(32) NOT NULL,
  `full_freight_terms` varchar(24) NOT NULL,
  `paid_amount` varchar(16) NOT NULL,
  `scac` varchar(4) NOT NULL,
  `ship_date` varchar(12) NOT NULL,
  `shipper_city` varchar(64) NOT NULL,
  `shipper_latitude` varchar(12) DEFAULT NULL,
  `shipper_longitude` varchar(12) DEFAULT NULL,
  `shipper_state` varchar(64) NOT NULL,
  `shipper_zip` varchar(12) NOT NULL,
  `carrier_id` bigint DEFAULT NULL,
  `user_id` bigint DEFAULT NULL,
  `vehicle_id` bigint DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKnlkvexse79ctwyx2ngngw84w8` (`carrier_id`),
  KEY `FK8vya64o37sfrh48kxslnwvv03` (`user_id`),
  KEY `FKnajg0k0dstjtsp64u3125burs` (`vehicle_id`),
  CONSTRAINT `FK8vya64o37sfrh48kxslnwvv03` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`),
  CONSTRAINT `FKnajg0k0dstjtsp64u3125burs` FOREIGN KEY (`vehicle_id`) REFERENCES `vehicles` (`id`),
  CONSTRAINT `FKnlkvexse79ctwyx2ngngw84w8` FOREIGN KEY (`carrier_id`) REFERENCES `carriers` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `shipments`
--

LOCK TABLES `shipments` WRITE;
/*!40000 ALTER TABLE `shipments` DISABLE KEYS */;
INSERT INTO `shipments` VALUES (1,'STBLLC','FTL','15','1000','52','Inwood','39.3578781','78.0399994','WV','25428','','SHIPMENT AVAILABLE','','','2022-04-17','AVON','41.4517093','-82.0354225','OH','44011',NULL,4,NULL),(2,'STBLLC','LTL','15','1000','52','CHEYENNE','41.079185','-104.7987710','WY','82007','','SHIPMENT AVAILABLE','','','2022-12-25','PHOENIX','33.43040260','33.43040260','AZ','85043',NULL,4,NULL),(3,'STBLLC','FTL','15','1000','52','LANDER','42.8330140','-108.7306725','WY','82520','3587239','BID ACCEPTED','$50.00','','2022-07-26','OKLAHOMA CITY','35.5638694','-97.4705935','OK','73131',1,4,1),(4,'STBLLC','FTL','55.0','862.0','64.0','BUFFALO','42.88023','-78.878738','NY','12950.0','1','AVAILABLE SHIPMENT','1','1','2022-03-31','PITTSBURGH','-78.878738','-79.995888','PA','16001.0',NULL,4,NULL),(5,'STBLLC','LTL','55.0','862.0','64.0','CHEYENNE','41.0791985','-104.798771','WY','82007.0','1','AVAILABLE SHIPMENT','1','1','2022-04-05','PHOENIX','-104.798771','-112.2026347','AZ','85043.0',NULL,4,NULL);
/*!40000 ALTER TABLE `shipments` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `technicians`
--

DROP TABLE IF EXISTS `technicians`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `technicians` (
  `id` bigint NOT NULL,
  `skill_grade` varchar(12) DEFAULT NULL,
  `contact_id` bigint DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKiplnxr5bgq82wfh6bdgyxfj7q` (`contact_id`),
  CONSTRAINT `FKiplnxr5bgq82wfh6bdgyxfj7q` FOREIGN KEY (`contact_id`) REFERENCES `contacts` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `technicians`
--

LOCK TABLES `technicians` WRITE;
/*!40000 ALTER TABLE `technicians` DISABLE KEYS */;
INSERT INTO `technicians` VALUES (1,'A',3),(2,'B',5);
/*!40000 ALTER TABLE `technicians` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user`
--

DROP TABLE IF EXISTS `user`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `user` (
  `id` bigint NOT NULL,
  `auctioning_allowed` bit(1) NOT NULL,
  `email` varchar(64) NOT NULL,
  `enabled` bit(1) NOT NULL DEFAULT b'0',
  `otp` varchar(6) DEFAULT NULL,
  `password` varchar(255) NOT NULL,
  `username` varchar(32) NOT NULL,
  `verification_code` varchar(64) DEFAULT NULL,
  `carrier_id` bigint DEFAULT NULL,
  `role_id` bigint DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK4paetr7gimreoarimesgmi6a8` (`carrier_id`),
  KEY `FKn82ha3ccdebhokx3a8fgdqeyy` (`role_id`),
  CONSTRAINT `FK4paetr7gimreoarimesgmi6a8` FOREIGN KEY (`carrier_id`) REFERENCES `carriers` (`id`),
  CONSTRAINT `FKn82ha3ccdebhokx3a8fgdqeyy` FOREIGN KEY (`role_id`) REFERENCES `role` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user`
--

LOCK TABLES `user` WRITE;
/*!40000 ALTER TABLE `user` DISABLE KEYS */;
INSERT INTO `user` VALUES (1,_binary '','AdminTry@Gmail.com',_binary '',NULL,'$2a$10$1czLv.unEEJZMLTIS2sTxe6DL7CuopPOcCCEGmKwak3H4KFfUBVOm','AdminTry',NULL,NULL,1),(2,_binary '','Master@gmail.com',_binary '',NULL,'$2a$10$kijCY6WxZXMfsJ4NcW3SkOqeB6BDiXyR3Pmk7UExr1rJAjVmECF7i','Master',NULL,NULL,4),(3,_binary '','carrier@gmail.com',_binary '',NULL,'$2a$10$EVBsfb2HGqaMlI9z443kR.zM.Tn66fT.7nbvsVhDeqAw.fc4HyXOG','Carrier',NULL,1,3),(4,_binary '','shipper@gmail.com',_binary '',NULL,'$2a$10$j4JWTe7EP6vPiptRf1WP1ujvRRNVPzvuQXkO7KH9Ot.YBY0znyKam','Shipper',NULL,NULL,2),(5,_binary '','wonka@gmail.com',_binary '',NULL,'$2a$10$xcIr8bwuPpCA5/dRvIOmMuVpmFZ.i18oFA5qnaQ8eiNgVKp/L.T9K','WillyWonka',NULL,2,3),(6,_binary '','ship4u@gmail.com',_binary '',NULL,'$2a$10$0mjUJWe24cNS.XoMLb/Ybur3EK0ps.787omMxs4y3DbGB2y6StgNC','Ship4U',NULL,3,3),(7,_binary '','ShadowAdmin@gmail.com',_binary '',NULL,'$2a$10$0mjUJWe24cNS.XoMLb/Ybur3EK0ps.787omMxs4y3DbGB2y6StgNC','ShadowAdmin',NULL,NULL,5);
/*!40000 ALTER TABLE `user` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `vehicle_types`
--

DROP TABLE IF EXISTS `vehicle_types`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `vehicle_types` (
  `id` bigint NOT NULL,
  `capacity` varchar(16) DEFAULT NULL,
  `description` varchar(64) DEFAULT NULL,
  `empty_weight` varchar(16) NOT NULL,
  `height` varchar(16) NOT NULL,
  `length` varchar(16) DEFAULT NULL,
  `make` varchar(32) NOT NULL,
  `maximum_cubic_weight` varchar(16) NOT NULL,
  `maximum_range` varchar(16) NOT NULL,
  `maximum_weight` varchar(16) NOT NULL,
  `minimum_cubic_weight` varchar(16) NOT NULL,
  `minimum_weight` varchar(16) NOT NULL,
  `model` varchar(32) NOT NULL,
  `restrictions` varchar(128) DEFAULT NULL,
  `sub_type` varchar(32) NOT NULL,
  `type` varchar(32) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `vehicle_types`
--

LOCK TABLES `vehicle_types` WRITE;
/*!40000 ALTER TABLE `vehicle_types` DISABLE KEYS */;
INSERT INTO `vehicle_types` VALUES (1,'3000','','10000','80','85','FreightLiner','0','400','80000','0','20000','FL-384DK','N/A','Open','Tanker'),(2,'','','7000','90','95','Mack','0','400','40000','0','20000','M-DJ48DC','','Closed','Flatbed'),(3,'','','0','70','80','FreightLiner','0','200','30000','0','10000','FL-20134','No Dirt Roads','Open','Flatbed'),(4,'','','225','20','25','Ford','0','500','1500','0','500','F150','N/A','Super','Truck'),(5,'','','100','10','13','Ford','0','700','400','0','100','Focus','','4 door','Car'),(6,'','','10000','87','90','Mack','0','300','65000','0','20000','M-DD44DC','N/A','Closed','Tanker');
/*!40000 ALTER TABLE `vehicle_types` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `vehicles`
--

DROP TABLE IF EXISTS `vehicles`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `vehicles` (
  `id` bigint NOT NULL,
  `manufactured_year` varchar(4) DEFAULT NULL,
  `plate_number` varchar(12) DEFAULT NULL,
  `vin_number` varchar(17) DEFAULT NULL,
  `carrier_id` bigint DEFAULT NULL,
  `location_id` bigint DEFAULT NULL,
  `vehicle_type_id` bigint DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKtgx3kt1tha985p2325h1c3tel` (`carrier_id`),
  KEY `FK3f7prh5yeaqk02pb4pbetgxca` (`location_id`),
  KEY `FKk3cs3vldlan30kg1x6b57i4fe` (`vehicle_type_id`),
  CONSTRAINT `FK3f7prh5yeaqk02pb4pbetgxca` FOREIGN KEY (`location_id`) REFERENCES `locations` (`id`),
  CONSTRAINT `FKk3cs3vldlan30kg1x6b57i4fe` FOREIGN KEY (`vehicle_type_id`) REFERENCES `vehicle_types` (`id`),
  CONSTRAINT `FKtgx3kt1tha985p2325h1c3tel` FOREIGN KEY (`carrier_id`) REFERENCES `carriers` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `vehicles`
--

LOCK TABLES `vehicles` WRITE;
/*!40000 ALTER TABLE `vehicles` DISABLE KEYS */;
INSERT INTO `vehicles` VALUES (1,'1980','DEC-DEC1','4B7DH3LDJNEE945D',1,2,3),(2,'1995','APR-APR1','9F2AA2LDJMYT11R',3,3,4),(3,'2010','JAN-JAN1','8O9DH3LAVBNN343T',2,1,1),(4,'2014','MAR-MAR2','1O1DH3LMWTRE675T',1,2,5);
/*!40000 ALTER TABLE `vehicles` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2023-02-23 20:14:30

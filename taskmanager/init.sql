-- MySQL dump 10.13  Distrib 8.0.43, for Win64 (x86_64)
--
-- Host: localhost    Database: taskmanager
-- ------------------------------------------------------
-- Server version	8.0.43

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
-- Table structure for table `activity_log`
--

DROP TABLE IF EXISTS `activity_log`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `activity_log` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `action` varchar(255) DEFAULT NULL,
  `description` varchar(255) DEFAULT NULL,
  `created_at` datetime DEFAULT NULL,
  `user_id` bigint DEFAULT NULL,
  `username` varchar(255) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_activity_user` (`user_id`),
  CONSTRAINT `fk_activity_user` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`) ON DELETE SET NULL
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `activity_log`
--

LOCK TABLES `activity_log` WRITE;
/*!40000 ALTER TABLE `activity_log` DISABLE KEYS */;
INSERT INTO `activity_log` VALUES (1,'THÊM CÔNG VIỆC','Đã tạo công việc: Viết báo cáo JavaScript','2026-07-17 12:35:02',1,'Phạm Đức Cường'),(2,'THÊM CÔNG VIỆC','Đã tạo công việc: Lập trình JavaScript','2026-07-17 12:38:20',1,'Phạm Đức Cường'),(3,'SỬA CÔNG VIỆC','Đã cập nhật công việc: Sửa code lỗi C++','2026-07-17 12:38:42',1,'Phạm Đức Cường'),(4,'SỬA CÔNG VIỆC','Đã cập nhật công việc: Sửa lại code','2026-07-17 12:39:31',5,'Nguyễn Tiến Dũng'),(5,'SỬA CÔNG VIỆC','Đã cập nhật công việc: Lập trình JavaScript','2026-07-17 12:39:47',5,'Nguyễn Tiến Dũng'),(6,'SỬA CÔNG VIỆC','Đã cập nhật công việc: Làm báo cáo Java','2026-07-17 12:40:12',2,'Nguyễn Xuân Đông');
/*!40000 ALTER TABLE `activity_log` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tasks`
--

DROP TABLE IF EXISTS `tasks`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `tasks` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `description` varchar(1000) DEFAULT NULL,
  `status` varchar(255) DEFAULT NULL,
  `title` varchar(255) DEFAULT NULL,
  `deadline` date DEFAULT NULL,
  `user_id` bigint DEFAULT NULL,
  `priority` varchar(255) DEFAULT NULL,
  `progress` int DEFAULT '0',
  `created_at` datetime DEFAULT NULL,
  `updated_at` datetime DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK6s1ob9k4ihi75xbxe2w0ylsdh` (`user_id`),
  CONSTRAINT `FK6s1ob9k4ihi75xbxe2w0ylsdh` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=14 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tasks`
--

LOCK TABLES `tasks` WRITE;
/*!40000 ALTER TABLE `tasks` DISABLE KEYS */;
INSERT INTO `tasks` VALUES (1,'Hoàn thành báo cáo tuần','Hoàn thành','Làm báo cáo C++','2026-07-02',1,'LOW',100,NULL,'2026-07-16 13:56:35'),(3,'Hoàn thành báo cáo trước thứ 6','Hoàn thành','Làm báo cáo Java',NULL,2,'MEDIUM',100,NULL,'2026-07-17 12:40:12'),(5,'Làm ra một trang Web','Đang làm','Lập Trình Web','2026-09-30',5,'MEDIUM',70,NULL,'2026-07-03 09:49:14'),(6,'Lập trình trí tuệ nhân tạo','Chưa làm','Lập trình python','2026-07-12',1,'HIGH',0,NULL,'2026-07-13 15:58:20'),(7,'Gửi code lỗi và sửa lại','Đang làm','Sửa code lỗi C++','2026-07-30',2,'HIGH',60,NULL,'2026-07-17 12:38:42'),(9,'Tạo website bán quần áo\r\n','Hoàn thành','Tạo một trang website','2026-07-05',12,'HIGH',100,'2026-07-05 13:19:29','2026-07-05 13:19:29'),(10,'Fix lại lỗi ','Đang làm','Sửa lại code',NULL,5,'MEDIUM',60,NULL,'2026-07-17 12:39:31'),(12,'Viết báo cáo ','Đang làm','Viết báo cáo JavaScript','2026-07-28',2,'LOW',40,'2026-07-17 12:35:02','2026-07-17 12:35:02'),(13,'Viết một đoạn JavaScript','Hoàn thành','Lập trình JavaScript','2026-07-16',5,'MEDIUM',100,NULL,'2026-07-17 12:39:47');
/*!40000 ALTER TABLE `tasks` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `users`
--

DROP TABLE IF EXISTS `users`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `users` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `fullname` varchar(100) NOT NULL,
  `password` varchar(255) DEFAULT NULL,
  `username` varchar(50) NOT NULL,
  `role` varchar(255) DEFAULT NULL,
  `avatar` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=16 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `users`
--

LOCK TABLES `users` WRITE;
/*!40000 ALTER TABLE `users` DISABLE KEYS */;
INSERT INTO `users` VALUES (1,'Phạm Đức Cường','$2a$10$I12S.PXBBXPKvt7WQvk6oeOebVNa98nlhQXnnNa5J4Y5y1WHHfjlK','admin','ADMIN','admin_1782524916994.jpg'),(2,'Nguyễn Xuân Đông','$2a$10$x57lLVCRxnT45C6OPYoy2.Lrp7SEikr3XbFq25w5SrxGs38bGefrK','user1','USER','user1_1782786558402.jpg'),(5,'Nguyễn Tiến Dũng','$2a$10$Pzd1UHktkJtvGhpA8/FO/eHDP.sXd73niqBdoVOD4sp/yx0oJJDo2','user2','USER','user2_1782786567886.jpg'),(9,'Người không ảnh','$2a$10$zv4rO2Qf83HrEsT5zS8XfeiAN9TFIlKQbLMsf6YKo27RUpHawdp82','user3','USER','user3_1782786576546.jpg'),(10,'Nguyễn Văn A','$2a$10$rYiTolEIahVCXoEd.vaFBeizFsx7OM8uHY2O9DW0XGRKkCj76o5ai','user4','USER','user4_1783762641393.png'),(12,'Nguyễn Văn C','$2a$10$jBHeo.00rwiFh6vWeOhuXOcOHVTL12g9vUiPxn71dOTjxMW3OSuBC','user6','USER','default.png'),(13,'nguyen van b','$2a$10$3XZNSUUMD32d9/v5N/SQh.yM958AyLZkvL0OgUSwTxAH17pQE4do.','user6','USER','user6_1783936178601.png');
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

-- Dump completed on 2026-07-20 17:17:15

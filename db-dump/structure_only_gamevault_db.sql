CREATE DATABASE  IF NOT EXISTS `gamevault_db` /*!40100 DEFAULT CHARACTER SET utf8mb3 */ /*!80016 DEFAULT ENCRYPTION='N' */;
USE `gamevault_db`;
-- MySQL dump 10.13  Distrib 8.0.40, for Win64 (x86_64)
--
-- Host: localhost    Database: gamevault_db
-- ------------------------------------------------------
-- Server version	8.0.40

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `friend_comment`
--

DROP TABLE IF EXISTS `friend_comment`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `friend_comment` (
  `id` int NOT NULL AUTO_INCREMENT,
  `content` mediumtext NOT NULL,
  `posted_at` date NOT NULL,
  `users_friends_id` int NOT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_friend_comment_users_friends1_idx` (`users_friends_id`),
  CONSTRAINT `fk_friend_comment_users_friends1` FOREIGN KEY (`users_friends_id`) REFERENCES `users_friends` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=14 DEFAULT CHARSET=utf8mb3;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `friend_request`
--

DROP TABLE IF EXISTS `friend_request`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `friend_request` (
  `id` int NOT NULL AUTO_INCREMENT,
  `uid1` int NOT NULL,
  `uid2` int NOT NULL,
  PRIMARY KEY (`id`),
  KEY `uid1_idx` (`uid1`),
  KEY `uid2_idx` (`uid2`),
  CONSTRAINT `uid1` FOREIGN KEY (`uid1`) REFERENCES `user` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `uid2` FOREIGN KEY (`uid2`) REFERENCES `user` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=40 DEFAULT CHARSET=utf8mb3;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `game`
--

DROP TABLE IF EXISTS `game`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `game` (
  `id` int NOT NULL AUTO_INCREMENT,
  `title` varchar(60) NOT NULL,
  `description` mediumtext NOT NULL,
  `developer` varchar(45) NOT NULL,
  `release_date` date NOT NULL,
  `number_of_reviews` bigint DEFAULT NULL,
  `overall_rating` enum('Mostly_Positive','Positive','Mixed','Negative','Mostly_Negative') DEFAULT NULL,
  `overall_rating_percentage` double DEFAULT NULL,
  `download_url` varchar(255) NOT NULL,
  `number_of_acquisitions` bigint NOT NULL,
  `is_published` tinyint NOT NULL DEFAULT '0',
  `deployment_date` date DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `title_UNIQUE` (`title`)
) ENGINE=InnoDB AUTO_INCREMENT=42 DEFAULT CHARSET=utf8mb3;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `game_image`
--

DROP TABLE IF EXISTS `game_image`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `game_image` (
  `id` int NOT NULL AUTO_INCREMENT,
  `type` enum('Catalog','Product_Page','Icon','Library') NOT NULL,
  `url` varchar(255) NOT NULL,
  `game_id` int NOT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_game_image_game1_idx` (`game_id`),
  CONSTRAINT `fk_game_image_game1` FOREIGN KEY (`game_id`) REFERENCES `game` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=47 DEFAULT CHARSET=utf8mb3;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `game_review`
--

DROP TABLE IF EXISTS `game_review`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `game_review` (
  `id` int NOT NULL AUTO_INCREMENT,
  `rating` enum('Mostly_Positive','Positive','Mixed','Negative','Mostly_Negative') NOT NULL,
  `content` mediumtext NOT NULL,
  `posted_at` date NOT NULL,
  `users_games_id` int NOT NULL,
  PRIMARY KEY (`id`,`users_games_id`),
  KEY `fk_game_review_users_games1_idx` (`users_games_id`),
  CONSTRAINT `fk_game_review_users_games1` FOREIGN KEY (`users_games_id`) REFERENCES `users_games` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=22 DEFAULT CHARSET=utf8mb3;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `game_system_requirements`
--

DROP TABLE IF EXISTS `game_system_requirements`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `game_system_requirements` (
  `id` int NOT NULL AUTO_INCREMENT,
  `type` enum('Minimum','Recommended') NOT NULL,
  `operating_system` varchar(30) NOT NULL,
  `cpu` varchar(45) NOT NULL,
  `gpu` varchar(45) NOT NULL,
  `ram` int NOT NULL,
  `storage` int NOT NULL,
  `expected_storage` int NOT NULL,
  `game_id` int NOT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_game_system_requirements_game1_idx` (`game_id`),
  CONSTRAINT `fk_game_system_requirements_game1` FOREIGN KEY (`game_id`) REFERENCES `game` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=19 DEFAULT CHARSET=utf8mb3;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `games_genres`
--

DROP TABLE IF EXISTS `games_genres`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `games_genres` (
  `genre_id` int NOT NULL,
  `game_id` int NOT NULL,
  PRIMARY KEY (`genre_id`,`game_id`),
  KEY `fk_games_genres_genre1_idx` (`genre_id`),
  KEY `fk_games_genres_game1_idx` (`game_id`),
  CONSTRAINT `fk_games_genres_game1` FOREIGN KEY (`game_id`) REFERENCES `game` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `fk_games_genres_genre1` FOREIGN KEY (`genre_id`) REFERENCES `genre` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `genre`
--

DROP TABLE IF EXISTS `genre`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `genre` (
  `id` int NOT NULL AUTO_INCREMENT,
  `name` varchar(45) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `name_UNIQUE` (`name`)
) ENGINE=InnoDB AUTO_INCREMENT=19 DEFAULT CHARSET=utf8mb3;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `user`
--

DROP TABLE IF EXISTS `user`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `user` (
  `id` int NOT NULL AUTO_INCREMENT,
  `username` varchar(20) NOT NULL,
  `password` varchar(100) NOT NULL,
  `image_url` varchar(255) DEFAULT NULL,
  `description` mediumtext,
  `role` enum('ADMIN','USER') NOT NULL,
  `created_at` date NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `username_UNIQUE` (`username`)
) ENGINE=InnoDB AUTO_INCREMENT=26 DEFAULT CHARSET=utf8mb3;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `users_friends`
--

DROP TABLE IF EXISTS `users_friends`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `users_friends` (
  `id` int NOT NULL AUTO_INCREMENT,
  `added_at` date NOT NULL,
  `user_id` int NOT NULL,
  `user_id1` int NOT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_users_friends_user1_idx` (`user_id`),
  KEY `fk_users_friends_user2_idx` (`user_id1`),
  CONSTRAINT `fk_users_friends_user1` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `fk_users_friends_user2` FOREIGN KEY (`user_id1`) REFERENCES `user` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=25 DEFAULT CHARSET=utf8mb3;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `users_games`
--

DROP TABLE IF EXISTS `users_games`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `users_games` (
  `id` int NOT NULL AUTO_INCREMENT,
  `time_played` bigint DEFAULT NULL,
  `last_played_at` datetime DEFAULT NULL,
  `acquisition_date` date NOT NULL,
  `user_id` int NOT NULL,
  `game_id` int NOT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_users_games_user1_idx` (`user_id`),
  KEY `fk_users_games_game1_idx` (`game_id`),
  CONSTRAINT `fk_users_games_game1` FOREIGN KEY (`game_id`) REFERENCES `game` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `fk_users_games_user1` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=26 DEFAULT CHARSET=utf8mb3;
/*!40101 SET character_set_client = @saved_cs_client */;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2025-09-25 19:01:30

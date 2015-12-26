-- MySQL dump 10.13  Distrib 5.5.24, for Linux (x86_64)
--
-- Host: localhost    Database: voizd_db
-- ------------------------------------------------------
-- Server version	5.5.24-log

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
-- Table structure for table `advt_info`
--

DROP TABLE IF EXISTS `advt_info`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `advt_info` (
  `advt_code` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `advt_name` varchar(45) DEFAULT NULL,
  `advt_desc` varchar(500) DEFAULT NULL,
  `status` enum('ACTIVE','INACTIVE','DELETED') DEFAULT 'INACTIVE',
  `max_views` int(11) DEFAULT '-1',
  `max_clicks` int(11) DEFAULT '-1',
  `start_date` datetime DEFAULT NULL,
  `end_date` datetime DEFAULT NULL,
  `mis_destination` varchar(500) DEFAULT NULL,
  `mis_report_type` varchar(45) DEFAULT 'EXCEL',
  `access_type` varchar(15) DEFAULT 'ALL',
  `advt_type` smallint(5) unsigned DEFAULT NULL,
  PRIMARY KEY (`advt_code`)
) ENGINE=InnoDB AUTO_INCREMENT=27 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `advt_mic_info`
--

DROP TABLE IF EXISTS `advt_mic_info`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `advt_mic_info` (
  `advt_code` int(10) unsigned NOT NULL,
  `head_line` varchar(45) DEFAULT NULL,
  `description` varchar(500) DEFAULT NULL,
  `tags` varchar(100) DEFAULT NULL,
  `status` tinyint(3) unsigned DEFAULT '0' COMMENT '0-deactive,1-active',
  `file_ids` varchar(450) DEFAULT NULL,
  `duration` int(10) unsigned NOT NULL DEFAULT '300' COMMENT 'in seconds',
  `click_text` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`advt_code`),
  KEY `Index_advt` (`advt_code`),
  KEY `Index_status` (`status`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `clienttypes`
--

DROP TABLE IF EXISTS `clienttypes`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `clienttypes` (
  `Clientid` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `Type` varchar(60) CHARACTER SET utf8 NOT NULL DEFAULT '0',
  `Version` varchar(100) CHARACTER SET utf8 NOT NULL DEFAULT '0',
  `InstallationImage` varchar(500) CHARACTER SET utf8 DEFAULT NULL,
  `StillSupported` enum('Y','N') DEFAULT 'Y',
  `DevldCalcAlgorithm` mediumblob,
  `VoiceFormatSupported` set('QCP_FIXED_HALF_RATE','QCP_MAX_HALF_RATE','QCP_FIXED_FULL_RATE','QCP_MAX_FULL_RATE','PCM_FORMAT','MULAW_FORMAT','AMR_FORMAT','MS_GSM0610_FORMAT','caf','mp3') NOT NULL DEFAULT 'MULAW_FORMAT',
  `VoiceSizeLimit` int(11) unsigned DEFAULT '0',
  `PictureFormatSupported` varchar(20) CHARACTER SET utf8 DEFAULT NULL,
  `PictureSizeLimit` int(11) DEFAULT '0',
  `RingToneFormatSupported` varchar(20) CHARACTER SET utf8 DEFAULT NULL,
  `RingToneSizeLimit` int(11) unsigned DEFAULT '0',
  `MusicFormatSupported` varchar(20) CHARACTER SET utf8 DEFAULT NULL,
  `MusicSizeLimit` int(11) DEFAULT '0',
  `VideoFormatSupported` varchar(20) CHARACTER SET utf8 DEFAULT '0',
  `VedioSizeLimit` int(11) DEFAULT '0',
  `TextSizeLimit` int(11) DEFAULT '0',
  `FileSizeLimit` int(11) DEFAULT '0',
  `LatestVersion` int(11) DEFAULT '0',
  `MobileId` int(10) unsigned NOT NULL,
  `VoiceRecFormatSupported` set('QCP_FIXED_HALF_RATE','QCP_MAX_HALF_RATE','QCP_FIXED_FULL_RATE','QCP_MAX_FULL_RATE','PCM_FORMAT','MULAW_FORMAT','AMR_FORMAT','MS_GSM0610_FORMAT','caf','mp3') NOT NULL DEFAULT 'AMR_FORMAT',
  `VoicePlayFormatSupported` set('QCP_FIXED_HALF_RATE','QCP_MAX_HALF_RATE','QCP_FIXED_FULL_RATE','QCP_MAX_FULL_RATE','PCM_FORMAT','MULAW_FORMAT','AMR_FORMAT','MS_GSM0610_FORMAT','caf','mp3') NOT NULL DEFAULT 'AMR_FORMAT',
  `PlatformType` enum('SYMBIAN','J2ME','WINDOWS','BREW','SYMBIAN_3RD_FP1','IPHONE','BADA','Android','MTK') DEFAULT NULL,
  `IsModelSupported` tinyint(1) unsigned NOT NULL DEFAULT '0',
  `UserCategory` set('DEVELOPER','ALPHA','BETA','ALL_GROUP1') DEFAULT 'DEVELOPER',
  `server_version_id` int(10) unsigned DEFAULT NULL,
  `ImageResizeTo` enum('128x160','176x208','176x220','240x320','320x240','208x208','352x416','480x360','240x400','480x800','480x854','240x376','360x640','320x480','320x460','360x480') DEFAULT NULL,
  `release_date` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `isprotocolsupported` enum('Y','N') NOT NULL DEFAULT 'N',
  `upgradertml` varchar(15000) DEFAULT NULL,
  PRIMARY KEY (`Clientid`),
  UNIQUE KEY `Version` (`Version`),
  KEY `Mobileid` (`MobileId`),
  KEY `FK_server_version_id` (`server_version_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `content_counter`
--

DROP TABLE IF EXISTS `content_counter`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `content_counter` (
  `content_id` bigint(10) unsigned NOT NULL,
  `view` int(10) DEFAULT '0',
  `likes` int(10) DEFAULT '0',
  `dislikes` int(10) DEFAULT '0',
  `follower` int(10) DEFAULT '0',
  `comments` int(10) DEFAULT '0',
  `share` int(10) unsigned DEFAULT '0',
  `amplify` int(10) unsigned DEFAULT '0',
  PRIMARY KEY (`content_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `content_follower`
--

DROP TABLE IF EXISTS `content_follower`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `content_follower` (
  `id` bigint(10) unsigned NOT NULL AUTO_INCREMENT,
  `content_id` bigint(10) unsigned NOT NULL,
  `follower_id` bigint(10) unsigned NOT NULL,
  `status` tinyint(1) unsigned NOT NULL DEFAULT '0' COMMENT '0-FOLLOWED, 1-UNFOLLOWED,2-REMOVEDBYADMIN',
  `time_stamp` datetime DEFAULT NULL,
  `stream_id` bigint(10) unsigned NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`),
  KEY `Index_contentid` (`content_id`) USING BTREE,
  KEY `Index_followerId` (`follower_id`) USING BTREE,
  KEY `Index_streamId` (`stream_id`)
) ENGINE=InnoDB AUTO_INCREMENT=288 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `content_likes`
--

DROP TABLE IF EXISTS `content_likes`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `content_likes` (
  `user_id` int(11) NOT NULL,
  `content_id` bigint(10) unsigned NOT NULL,
  `likes_value` tinyint(3) unsigned NOT NULL DEFAULT '0' COMMENT '0-like,1-unlike',
  `modified_date` datetime DEFAULT NULL,
  `created_date` datetime DEFAULT NULL,
  UNIQUE KEY `user_likes_unique_ind` (`content_id`,`user_id`),
  KEY `user_likes_ind_user_id` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `content_master`
--

DROP TABLE IF EXISTS `content_master`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `content_master` (
  `content_id` bigint(10) unsigned NOT NULL AUTO_INCREMENT,
  `station_id` bigint(10) unsigned NOT NULL,
  `tag` varchar(100) DEFAULT NULL,
  `title` varchar(200) NOT NULL,
  `creator_id` bigint(10) unsigned NOT NULL,
  `status` tinyint(1) unsigned NOT NULL DEFAULT '1' COMMENT '1-ACTIVE,2-PENDING,3-REJECTED,4-ESCALATED,5-DELETED,6-REMOVEDBYADMIN',
  `is_private` tinyint(1) unsigned NOT NULL DEFAULT '0' COMMENT '0-no, 1-yes',
  `admin_id` int(10) unsigned DEFAULT NULL,
  `time_stamp` datetime DEFAULT NULL,
  `content_type` set('VOICE','TEXT','PICTURE','VIDEO','MEDIA') DEFAULT NULL,
  `modified_date` datetime DEFAULT NULL,
  `longitude` decimal(20,16) DEFAULT '0.0000000000000000',
  `latitude` decimal(20,16) DEFAULT '0.0000000000000000',
  `location` varchar(150) DEFAULT NULL,
  `city` varchar(45) CHARACTER SET latin1 COLLATE latin1_bin DEFAULT NULL,
  `country` varchar(45) DEFAULT 'United States',
  `state` varchar(45) DEFAULT NULL,
  `language` varchar(100) DEFAULT 'English',
  `address` varchar(450) DEFAULT NULL,
  `tag_cloud` varchar(450) DEFAULT NULL,
  PRIMARY KEY (`content_id`),
  KEY `station_content_mapping_1` (`station_id`),
  KEY `station_content_mapping_2` (`creator_id`),
  KEY `station_content_mapping_3` (`status`)
) ENGINE=InnoDB AUTO_INCREMENT=948 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `content_media`
--

DROP TABLE IF EXISTS `content_media`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `content_media` (
  `id` bigint(10) unsigned NOT NULL AUTO_INCREMENT,
  `content_id` bigint(10) unsigned NOT NULL,
  `ordering` int(10) DEFAULT NULL,
  `status` tinyint(3) unsigned NOT NULL DEFAULT '1' COMMENT '0-deactive,1-active,2-deleted',
  `file_id` varchar(128) NOT NULL DEFAULT '',
  `media_type` varchar(10) DEFAULT NULL,
  `ext` varchar(15) DEFAULT NULL,
  `size` int(10) unsigned DEFAULT '0',
  `time_stamp` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `duration` varchar(15) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `content_unique_ind` (`content_id`,`file_id`),
  KEY `content_media_1` (`file_id`)
) ENGINE=InnoDB AUTO_INCREMENT=1553 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `country_list`
--

DROP TABLE IF EXISTS `country_list`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `country_list` (
  `country_name` varchar(45) DEFAULT NULL,
  `country_code` varchar(2) NOT NULL DEFAULT '',
  PRIMARY KEY (`country_code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='InnoDB free: 7168 kB';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `default_media_file`
--

DROP TABLE IF EXISTS `default_media_file`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `default_media_file` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `default_name` varchar(45) NOT NULL,
  `file_id` varchar(128) NOT NULL,
  `status` tinyint(3) unsigned NOT NULL DEFAULT '1' COMMENT '0-deactive,1-active,2-deleted',
  `ext` varchar(15) DEFAULT NULL,
  `size` int(10) unsigned DEFAULT '0',
  `media_type` varchar(15) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `earth_voice`
--

DROP TABLE IF EXISTS `earth_voice`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `earth_voice` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `status` tinyint(3) unsigned NOT NULL DEFAULT '1' COMMENT '0-deactive,1-active,2-deleted',
  `file_id` varchar(128) NOT NULL DEFAULT '',
  `media_type` varchar(10) DEFAULT NULL,
  `ext` varchar(15) DEFAULT NULL,
  `size` int(10) unsigned DEFAULT '0',
  `duration` varchar(15) DEFAULT NULL,
  `globe` tinyint(3) unsigned NOT NULL DEFAULT '1' COMMENT '1-one,2-two',
  `time_stamp` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `idx_file_id` (`file_id`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `elastic_search_server`
--

DROP TABLE IF EXISTS `elastic_search_server`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `elastic_search_server` (
  `host` varchar(20) NOT NULL,
  `port` int(11) NOT NULL,
  `status` varchar(20) NOT NULL,
  PRIMARY KEY (`host`,`port`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `forget_password_verification`
--

DROP TABLE IF EXISTS `forget_password_verification`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `forget_password_verification` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `user_id` int(10) unsigned NOT NULL,
  `verification_code` varchar(200) NOT NULL,
  `created_date` datetime NOT NULL,
  `expiry_date` datetime DEFAULT NULL,
  `status` tinyint(1) NOT NULL DEFAULT '0' COMMENT '0-unused\n1-used',
  PRIMARY KEY (`id`),
  KEY `forget_password_verification_fk1` (`user_id`),
  CONSTRAINT `forget_password_verification_fk1` FOREIGN KEY (`user_id`) REFERENCES `user_auth` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=50 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `language_list`
--

DROP TABLE IF EXISTS `language_list`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `language_list` (
  `language_name` varchar(45) DEFAULT NULL,
  `language_code` varchar(2) NOT NULL DEFAULT '',
  PRIMARY KEY (`language_code`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `media_master`
--

DROP TABLE IF EXISTS `media_master`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `media_master` (
  `status` tinyint(3) unsigned NOT NULL DEFAULT '1' COMMENT '0-deactive,1-active,2-deleted',
  `file_id` varchar(128) NOT NULL DEFAULT '',
  `media_type` varchar(10) DEFAULT NULL,
  `ext` varchar(15) DEFAULT NULL,
  `size` int(10) unsigned DEFAULT '0',
  `duration` varchar(15) DEFAULT NULL,
  `time_stamp` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`file_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `media_server`
--

DROP TABLE IF EXISTS `media_server`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `media_server` (
  `host` varchar(20) NOT NULL,
  `port` int(11) NOT NULL,
  `operation` varchar(20) NOT NULL,
  `category` varchar(20) NOT NULL,
  `mediaType` varchar(20) NOT NULL,
  PRIMARY KEY (`host`,`port`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `mobile_models`
--

DROP TABLE IF EXISTS `mobile_models`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `mobile_models` (
  `mobile_model` varchar(50) NOT NULL,
  `screen_resolution` varchar(9) NOT NULL DEFAULT '176x220',
  `browser_patterns` varchar(500) DEFAULT NULL,
  `vendor` varchar(50) NOT NULL DEFAULT 'SonyEricsson',
  `platform_ver_id` int(10) unsigned NOT NULL DEFAULT '11',
  `operating_system` varchar(50) DEFAULT 'J2me Sony',
  `supported_image_formats` set('jpeg','jpg','gif','UPLD_NOT_ALLWD') DEFAULT 'jpeg',
  `supported_voice_formats` set('amr','wav','caf','mp3','NotSupported') DEFAULT 'amr',
  `supported_ringtones_formats` varchar(500) DEFAULT NULL,
  `supported_video_formats` set('3gp','mp4','NotSupported') DEFAULT '3gp',
  `img_path` varchar(500) DEFAULT NULL,
  `status` enum('SUPPORTED','NOTSUPPORTED','NOTSUPPORTEDNOW') DEFAULT 'SUPPORTED',
  `OTA_Supported` enum('YES','NO') NOT NULL DEFAULT 'YES',
  `notification` text,
  `flag` enum('YES','NO') NOT NULL DEFAULT 'YES',
  `supported_vas` set('CALL','SMS') DEFAULT 'CALL,SMS',
  `wap` enum('YES','NO') NOT NULL DEFAULT 'YES',
  `image_protocol` varchar(15) DEFAULT NULL,
  `voice_protocol` varchar(15) DEFAULT NULL,
  `video_protocol` varchar(15) DEFAULT NULL,
  `use_video_format_streaming` int(1) DEFAULT '0',
  PRIMARY KEY (`mobile_model`),
  KEY `FK_mobile_models_1` (`screen_resolution`),
  KEY `FK_mobile_models_2` (`vendor`),
  KEY `FK_mobile_models_3` (`platform_ver_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `mongo_server`
--

DROP TABLE IF EXISTS `mongo_server`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `mongo_server` (
  `host` varchar(20) NOT NULL,
  `port` int(11) NOT NULL,
  `status` varchar(20) NOT NULL,
  PRIMARY KEY (`host`,`port`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `station_counter`
--

DROP TABLE IF EXISTS `station_counter`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `station_counter` (
  `station_id` bigint(10) unsigned NOT NULL,
  `number_of_content` int(10) DEFAULT '0',
  `view` int(10) DEFAULT '0',
  `likes` int(10) DEFAULT '0',
  `dislikes` int(10) DEFAULT '0',
  `follower` int(10) DEFAULT '0',
  `comments` int(10) DEFAULT '0',
  `share` int(10) DEFAULT '0',
  PRIMARY KEY (`station_id`),
  KEY `Index_views` (`view`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `station_follower`
--

DROP TABLE IF EXISTS `station_follower`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `station_follower` (
  `id` bigint(10) unsigned NOT NULL AUTO_INCREMENT,
  `station_id` bigint(10) unsigned NOT NULL,
  `follower_id` bigint(10) unsigned NOT NULL,
  `status` tinyint(1) unsigned NOT NULL DEFAULT '0' COMMENT '0- FOLLOWED, 1- UNFOLLOWED,2-REMOVEDBYADMIN',
  `time_stamp` datetime DEFAULT NULL,
  `stream_id` bigint(10) unsigned NOT NULL DEFAULT '0',
  `followee_id` bigint(10) unsigned NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uni_follower_station_id` (`follower_id`,`station_id`,`followee_id`) USING BTREE,
  KEY `indx_station_id` (`station_id`) USING BTREE,
  KEY `Index_followeeid` (`followee_id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=441 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `station_likes`
--

DROP TABLE IF EXISTS `station_likes`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `station_likes` (
  `user_id` int(11) NOT NULL,
  `station_id` bigint(10) unsigned NOT NULL,
  `likes_value` tinyint(3) unsigned NOT NULL DEFAULT '0' COMMENT 'COMMENT ''0- LIKE, 1- UNLIKE''',
  `modified_date` datetime DEFAULT NULL,
  `created_date` datetime DEFAULT NULL,
  UNIQUE KEY `user_likes_unique_ind` (`station_id`,`user_id`),
  KEY `user_likes_ind_user_id` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `station_master`
--

DROP TABLE IF EXISTS `station_master`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `station_master` (
  `station_id` bigint(10) unsigned NOT NULL AUTO_INCREMENT,
  `station_name` varchar(100) NOT NULL,
  `tag` varchar(500) DEFAULT NULL,
  `category` varchar(50) DEFAULT NULL,
  `description` varchar(500) NOT NULL,
  `location` varchar(150) DEFAULT NULL,
  `language` varchar(100) DEFAULT NULL,
  `creator_id` bigint(10) unsigned NOT NULL,
  `created_date` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `file_id` varchar(128) NOT NULL,
  `status` tinyint(1) unsigned NOT NULL DEFAULT '0' COMMENT '0- DEACTIVATED, 1-ACTIVE ,2-DELETED',
  `modified_date` datetime DEFAULT NULL,
  `hasContent` tinyint(1) unsigned NOT NULL DEFAULT '0' COMMENT '0- no media, 1- has media',
  `isAdult` tinyint(1) unsigned NOT NULL DEFAULT '0' COMMENT '0-not adult, 1-adult',
  `station_update_date` datetime DEFAULT NULL,
  PRIMARY KEY (`station_id`) USING BTREE,
  UNIQUE KEY `station_master_1` (`creator_id`,`station_name`,`status`) USING BTREE,
  KEY `station_master_2` (`status`)
) ENGINE=InnoDB AUTO_INCREMENT=443 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `station_media`
--

DROP TABLE IF EXISTS `station_media`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `station_media` (
  `station_id` bigint(10) unsigned NOT NULL,
  `ordering` int(10) DEFAULT NULL,
  `status` tinyint(3) unsigned NOT NULL DEFAULT '1' COMMENT '0-deactive,1-active,2-deleted',
  `file_id` varchar(128) NOT NULL DEFAULT '',
  `media_type` varchar(10) DEFAULT NULL,
  `ext` varchar(15) DEFAULT NULL,
  `size` int(10) unsigned DEFAULT '0',
  `time_stamp` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`station_id`,`file_id`),
  KEY `station_media_1` (`file_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `stream_media`
--

DROP TABLE IF EXISTS `stream_media`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `stream_media` (
  `stream_id` bigint(10) unsigned NOT NULL,
  `ordering` int(10) DEFAULT NULL,
  `status` tinyint(3) unsigned NOT NULL DEFAULT '1' COMMENT '0-deactive,1-active,2-deleted',
  `file_id` varchar(128) NOT NULL DEFAULT '',
  `media_type` varchar(10) DEFAULT NULL,
  `ext` varchar(15) DEFAULT NULL,
  `size` int(10) unsigned DEFAULT '0',
  `time_stamp` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`stream_id`,`file_id`),
  KEY `strean_media_1` (`file_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `tags`
--

DROP TABLE IF EXISTS `tags`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tags` (
  `tag_id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `tag_name` varchar(100) NOT NULL,
  `city` varchar(45) DEFAULT 'US',
  `state` varchar(45) DEFAULT 'US',
  `country` varchar(100) DEFAULT 'United States',
  `language` varchar(100) DEFAULT 'English',
  `counter` int(10) unsigned NOT NULL,
  `created_date` datetime NOT NULL,
  `modified_date` datetime DEFAULT NULL,
  `file_ids` varchar(4000) NOT NULL,
  PRIMARY KEY (`tag_id`),
  UNIQUE KEY `idx_uniq` (`tag_name`,`city`,`state`,`country`,`language`),
  KEY `idx_country` (`country`),
  KEY `idx_language` (`language`),
  KEY `idx_counter` (`counter`),
  KEY `idx_state` (`state`),
  KEY `idx_city` (`city`)
) ENGINE=InnoDB AUTO_INCREMENT=998 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `user_auth`
--

DROP TABLE IF EXISTS `user_auth`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `user_auth` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `username` varchar(100) NOT NULL,
  `password` varchar(100) NOT NULL,
  `status` tinyint(1) NOT NULL DEFAULT '1' COMMENT '0-inactive\n1-active\n2-blocked\n',
  `created_date` datetime NOT NULL,
  `modified_date` datetime NOT NULL,
  `last_login_time` datetime NOT NULL,
  `registration_mode` tinyint(2) NOT NULL COMMENT '0-app\n1-facebook\n2-googleplus\n3-twitter\n4-tumblr',
  `last_login_mode` tinyint(2) NOT NULL COMMENT '0-app\n1-facebook\n2-googleplus\n3-twitter\n4-tumblr',
  `login_status` tinyint(1) NOT NULL DEFAULT '0' COMMENT '0-logout\n1-loggedin\n2-idle',
  `current_client_version` varchar(100) DEFAULT NULL,
  `current_platform` varchar(45) DEFAULT NULL,
  `latitude` decimal(20,16) DEFAULT '0.0000000000000000',
  `longitude` decimal(20,16) DEFAULT '0.0000000000000000',
  `last_location` varchar(150) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `username_UNIQUE` (`username`)
) ENGINE=InnoDB AUTO_INCREMENT=377 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `user_counter`
--

DROP TABLE IF EXISTS `user_counter`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `user_counter` (
  `user_id` bigint(10) unsigned NOT NULL,
  `follow` int(10) DEFAULT '0',
  `view` int(10) DEFAULT '0',
  `likes` int(10) DEFAULT '0',
  `comments` int(10) DEFAULT '0',
  `amplified` int(10) DEFAULT '0',
  `share` int(10) DEFAULT '0',
  PRIMARY KEY (`user_id`),
  KEY `Index_views` (`view`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `user_email_verification`
--

DROP TABLE IF EXISTS `user_email_verification`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `user_email_verification` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `user_id` int(10) unsigned NOT NULL,
  `email_id` varchar(100) NOT NULL,
  `verification_code` varchar(100) NOT NULL,
  `status` tinyint(1) NOT NULL DEFAULT '1' COMMENT '0-expired\n1-active\n2-used',
  `created_date` datetime NOT NULL,
  `expiry_date` datetime DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `user_email_verification_fk1` (`user_id`),
  CONSTRAINT `user_email_verification_fk1` FOREIGN KEY (`user_id`) REFERENCES `user_auth` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=290 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `user_info`
--

DROP TABLE IF EXISTS `user_info`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `user_info` (
  `id` int(10) unsigned NOT NULL,
  `first_name` varchar(100) NOT NULL,
  `last_name` varchar(100) DEFAULT NULL,
  `dob` date DEFAULT NULL,
  `gender` tinyint(1) NOT NULL COMMENT '1-male\n2-female	',
  `secondary_email_address` varchar(100) DEFAULT NULL,
  `contact_number` varchar(45) DEFAULT NULL,
  `profile_pic_file_id` varchar(100) DEFAULT NULL,
  `profile_pic_file_ext` varchar(5) DEFAULT NULL,
  `contact_address1` varchar(45) DEFAULT NULL,
  `contact_address2` varchar(45) DEFAULT NULL,
  `zipcode` varchar(10) DEFAULT NULL,
  `city` varchar(45) DEFAULT NULL,
  `state` varchar(45) DEFAULT NULL,
  `country` varchar(100) DEFAULT 'United States',
  `time_zone` varchar(20) DEFAULT NULL,
  `modified_date` datetime DEFAULT NULL,
  `language` varchar(100) DEFAULT 'English',
  `location` varchar(150) DEFAULT NULL,
  `user_desc` varchar(500) DEFAULT NULL,
  `web_site` varchar(150) DEFAULT NULL,
  `primary_email_address` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `user_info_fk1` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `user_stream`
--

DROP TABLE IF EXISTS `user_stream`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `user_stream` (
  `stream_id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `user_id` bigint(10) unsigned NOT NULL,
  `stream_name` varchar(45) NOT NULL,
  `created_date` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `status` tinyint(3) unsigned NOT NULL DEFAULT '1' COMMENT '0- DEACTIVATED, 1-ACTIVE ,2-DELETED',
  `modified_date` datetime DEFAULT NULL,
  `stream_state` tinyint(3) unsigned NOT NULL DEFAULT '1' COMMENT '0-private,1-public',
  `hasContent` tinyint(3) unsigned NOT NULL DEFAULT '0' COMMENT '0-no content,1-content',
  `tag` varchar(500) DEFAULT NULL,
  `category` varchar(45) DEFAULT NULL,
  `description` varchar(500) NOT NULL,
  `file_id` varchar(128) NOT NULL,
  PRIMARY KEY (`stream_id`),
  UNIQUE KEY `Index_uid` (`user_id`,`stream_name`,`status`) USING BTREE,
  KEY `Index_state` (`stream_state`)
) ENGINE=InnoDB AUTO_INCREMENT=21 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `user_stream_counter`
--

DROP TABLE IF EXISTS `user_stream_counter`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `user_stream_counter` (
  `stream_id` bigint(10) unsigned NOT NULL,
  `number_of_content` int(10) DEFAULT '0',
  `view` int(10) DEFAULT '0',
  `likes` int(10) DEFAULT '0',
  `comments` int(10) DEFAULT '0',
  `share` int(10) DEFAULT '0',
  PRIMARY KEY (`stream_id`),
  KEY `Index_views` (`view`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `user_stream_likes`
--

DROP TABLE IF EXISTS `user_stream_likes`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `user_stream_likes` (
  `user_id` int(11) NOT NULL,
  `stream_id` bigint(10) unsigned NOT NULL,
  `likes_value` tinyint(3) unsigned NOT NULL DEFAULT '0' COMMENT 'COMMENT ''0- LIKE, 1- UNLIKE''',
  `modified_date` datetime DEFAULT NULL,
  `created_date` datetime DEFAULT NULL,
  UNIQUE KEY `user_likes_unique_ind` (`stream_id`,`user_id`),
  KEY `user_likes_ind_user_id` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `user_third_party_auth`
--

DROP TABLE IF EXISTS `user_third_party_auth`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `user_third_party_auth` (
  `id` int(10) unsigned NOT NULL,
  `third_party_id` tinyint(2) NOT NULL,
  `user_key` varchar(100) NOT NULL,
  `app_key` varchar(100) NOT NULL,
  UNIQUE KEY `user_third_party_auth_uq` (`id`,`third_party_id`),
  KEY `user_third_party_auth_fk1` (`id`),
  CONSTRAINT `user_third_party_auth_fk1` FOREIGN KEY (`id`) REFERENCES `user_auth` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2013-10-10 12:26:23

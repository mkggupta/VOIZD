DROP TABLE IF EXISTS `voizd_db`.`amplify_info`;
CREATE TABLE `amplify_info` (
  `amplify_id` bigint(10) unsigned NOT NULL AUTO_INCREMENT,
  `content_Id` bigint(10) unsigned NOT NULL,
  `amplifier_id` bigint(10) unsigned NOT NULL,
  `creator_id` bigint(10) unsigned NOT NULL,
  `status` tinyint(1) unsigned NOT NULL DEFAULT '0' COMMENT '0-ACTIVE,1-INACTIVE,2-PENDING,3-REJECTED,4-ESCALATED,5-DELETED,6-REMOVEDBYADMIN',
  `time_stamp` datetime DEFAULT NULL,
  `modified_date` datetime DEFAULT NULL,
  PRIMARY KEY (`amplify_id`),
  UNIQUE KEY `amplify_info_unique_id` (`content_Id`,`amplifier_id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=latin1;
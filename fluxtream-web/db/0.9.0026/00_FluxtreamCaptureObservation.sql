CREATE TABLE `Facet_FluxtreamCaptureObservation` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `api` int(11) NOT NULL,
  `comment` longtext,
  `end` bigint(20) NOT NULL,
  `fullTextDescription` longtext,
  `guestId` bigint(20) NOT NULL,
  `isEmpty` char(1) NOT NULL,
  `objectType` int(11) NOT NULL,
  `start` bigint(20) NOT NULL,
  `tags` longtext,
  `timeUpdated` bigint(20) NOT NULL,
  `note` longtext,
  `mymeeId` varchar(255) NOT NULL,
  `name` varchar(255) DEFAULT NULL,
  `user` varchar(255) DEFAULT NULL,
  `timezoneOffset` int(11) NOT NULL,
  `amount` double DEFAULT NULL,
  `baseAmount` int(11) DEFAULT NULL,
  `unit` varchar(255) DEFAULT NULL,
  `baseUnit` varchar(255) DEFAULT NULL,
  `imageURL` varchar(255) DEFAULT NULL,
  `longitude` double DEFAULT NULL,
  `latitude` double DEFAULT NULL,
  `apiKeyId` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `isEmpty_index` (`isEmpty`),
  KEY `end_index` (`end`),
  KEY `start_index` (`start`),
  KEY `api_index` (`api`),
  KEY `name` (`name`),
  KEY `objectType_index` (`objectType`),
  KEY `guestId_index` (`guestId`),
  KEY `timeUpdated_index` (`timeUpdated`),
  KEY `apiKeyId` (`apiKeyId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

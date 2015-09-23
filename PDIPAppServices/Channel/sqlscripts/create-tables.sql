# MySQL-Front Dump 2.1
#
# Host: localhost   Database: userdb
#--------------------------------------------------------
# Server version 4.0.0-alpha

USE userdb;

#
# Table structure for table 'channel_info'
#

DROP TABLE IF EXISTS channel_info;
CREATE TABLE IF NOT EXISTS channel_info (
  UID bigint(20) NOT NULL DEFAULT '0' ,
  Name varchar(30) NOT NULL DEFAULT '' ,
  Description varchar(80) ,
  RefId varchar(30) ,
  TptProtocolType varchar(10) NOT NULL DEFAULT '' ,
  TptCommInfo bigint(20) NOT NULL DEFAULT '0' ,
  PackagingProfile bigint(20) NOT NULL default '0',
  SecurityProfile bigint(20) NOT NULL default '0',
  Version double NOT NULL DEFAULT '0' ,
  isMaster tinyint(1) default '0',
  isPartner tinyint(1) default '0',
  CanDelete tinyint(1) unsigned NOT NULL DEFAULT '1' ,
  PartnerCategory tinyint(1) unsigned DEFAULT NULL,
  isDisable tinyint(1) default '0',
  isRelay tinyint(1) unsigned DEFAULT NULL,
  FlowControlProfile blob,
  PRIMARY KEY (UID),
  UNIQUE KEY name_idx (Name,RefId),
   KEY type_idx (TptProtocolType),
   KEY refid_idx (RefId),
   KEY comminfo_idx (TptCommInfo)
);



#
# Table structure for table 'comm_info'
#

DROP TABLE IF EXISTS comm_info;
CREATE TABLE IF NOT EXISTS comm_info (
  UID bigint(20) NOT NULL DEFAULT '0' ,
  Name varchar(30) NOT NULL DEFAULT '' ,
  Description varchar(80) ,
  ProtocolType varchar(10) NOT NULL DEFAULT '' ,
#  ProtocolVersion varchar(10) ,
#  Host varchar(30) ,
#  Port int(10) NOT NULL DEFAULT '0' ,
  TptImplVersion varchar(6) NOT NULL DEFAULT '000001' ,
#  ProtocolDetail text ,
  RefId varchar(30),
  IsDefaultTpt tinyint(1) NOT NULL DEFAULT '0' ,
  Version double NOT NULL DEFAULT '0' ,
  CanDelete tinyint(1) unsigned NOT NULL DEFAULT '1' ,
  isPartner tinyint(1) default '0',
  PartnerCategory tinyint(1) unsigned DEFAULT NULL,
  isDisable tinyint(1) default '0',
  Url varchar(255),
  PRIMARY KEY (UID),
  UNIQUE KEY name_idx (Name,RefId)
);


#
# Table structure for table 'packaging_profile'
#


DROP TABLE IF EXISTS packaging_profile;
CREATE TABLE `packaging_profile` (
  `UID` bigint(20) NOT NULL default '0',
  `Name` varchar(30) default NULL,
  `Description` varchar(80) NOT NULL default '',
  `Envelope` varchar(20) default NULL,
  `Zip` tinyint(1) unsigned NOT NULL DEFAULT '1',
  `ZipThreshold` int(11) default '500',
  `CanDelete` tinyint(1) unsigned NOT NULL DEFAULT '1' ,
  `Version` double NOT NULL DEFAULT '1' ,
   `RefId` varchar(30),
  isPartner tinyint(1) default '0',
  PartnerCategory tinyint(1) unsigned DEFAULT NULL,
  `isDisable` tinyint(1) default '0',
  `Split` tinyint(1) unsigned NOT NULL DEFAULT '0',
  `SplitThreshold` int(11) default '5000',
  `SplitSize` int(11) default '5000',
  `PackagingInfoExtension` blob,
  PRIMARY KEY  (`UID`),
  UNIQUE KEY name_idx (Name,RefId)
) TYPE=MyISAM COMMENT='Packaging Profile Information';


#
# Table structure for table 'security_profile'
#


DROP TABLE IF EXISTS security_profile;

CREATE TABLE `security_profile` (
  `UID` bigint(20) NOT NULL default '0',
  `Name` varchar(30) default NULL,
  `Description` varchar(80) NOT NULL default '',
  `EncryptionType` varchar(30) default NULL,
  `EncryptionLevel` int(10) default NULL,
  `EncryptionCertificate` bigint(20) default NULL,
  `SignatureType` varchar(20) default NULL,
  `DigestAlgorithm` varchar(10) default NULL,
  `SignatureEncryptionCertificate` bigint(20) default NULL,
  `CanDelete` tinyint(1) unsigned NOT NULL DEFAULT '1' ,
  `Version` double NOT NULL DEFAULT '1' ,
   `RefId` varchar(30),
   isPartner tinyint(1) default '0',
   PartnerCategory tinyint(1) unsigned DEFAULT NULL,
   `isDisable` tinyint(1) default '0',
   `CompressionType` varchar(30) default NULL,
   `CompressionMethod` varchar(30) default NULL,
   `CompressionLevel` int(10) default NULL,
   `Sequence` varchar(30) default NULL,
   `EncryptionAlgorithm` varchar(30) default NULL,
   PRIMARY KEY  (`UID`),
   UNIQUE KEY name_idx (Name,RefId)
) TYPE=MyISAM COMMENT='Security Profile per Channel';


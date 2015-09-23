USE userdb;

#
# Table structure for table 'certificate'
# version:2.0
# build:2.0.16
# iteration:8
# date:27/02/03
# Updated the sql script for Certificate to modify the column width for
# a. SerialNum     - From: varchar(30)  to varchar(64).
# b. IssuerName  - From : varchar(120) to varchar(255).
#
# 16 Jun 2004 
# Updated Script for Certificate to modify the column attributes for
# a. SerialNum     - From: varchar(64)  to varchar(64) BINARY.
# b. IssuerName  - From : varchar(255) to varchar(255) BINARY.
#
# TWX 28 July 2006
# New field StartDate, EndDate, IsCA, ReplacementCertUid 
#

DROP TABLE IF EXISTS certificate;
CREATE TABLE `certificate` (
  `UID` bigint(20) NOT NULL default '0',
  `ID` int(20) NOT NULL default '0',
  `Name` varchar(50) default NULL,
  `SerialNum` varchar(64) BINARY default NULL,
  `IssuerName` varchar(255) BINARY default NULL,
  `Certificate` longtext,
  `PublicKey` longtext,
  `PrivateKey` longtext,
  `RevokeID` int(11) default '0',
  `isMaster` tinyint(1) NOT NULL default '0',
  `isPartner` tinyint(1) NOT NULL default '0',
  `Category` tinyint(1) unsigned DEFAULT NULL,
  `iSINKS` tinyint(1) NOT NULL default '0',
  `iSINTS` tinyint(1) NOT NULL default '0',
  `relatedCertUid` bigint(20) default NULL,
  `StartDate` datetime default NULL,
  `EndDate` datetime default NULL,
  `IsCA` tinyint(1) NOT NULL default '0',
  `ReplacementCertUid` bigint(20) default NULL
) TYPE=MyISAM;


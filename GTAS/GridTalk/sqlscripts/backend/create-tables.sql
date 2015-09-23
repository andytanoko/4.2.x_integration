# MySQL-Front Dump 2.1
#
# Host: localhost   Database: userdb
#--------------------------------------------------------
# Server version 4.0.0-alpha


# Modified by Tam Wei Xiang 23 Aug 2005        
# Deleted the field 'AttachmentDir varchar(80) DEFAULT NULL' from table 'port'

USE userdb;

#
# Table structure for table 'port'
#

DROP TABLE IF EXISTS port;
CREATE TABLE IF NOT EXISTS port (
  UID bigint(20) NOT NULL DEFAULT '0',
  PortName varchar(15) NOT NULL DEFAULT '',
  Description varchar(80) NOT NULL DEFAULT '',
  IsRfc tinyint(1) NOT NULL DEFAULT '0',
  RfcUid bigint(20) DEFAULT NULL,
  HostDir varchar(80) NOT NULL DEFAULT '',
  IsDiffFileName tinyint(1) NOT NULL DEFAULT '1',
  IsOverwrite tinyint(1) NOT NULL DEFAULT '1',
  FileName varchar(80) DEFAULT NULL,
  IsAddFileExt tinyint(1) NOT NULL DEFAULT '0',
  FileExtType smallint(2),
  FileExtValue varchar(64) DEFAULT NULL,
  IsExportGdoc tinyint(1) NOT NULL DEFAULT '0',
  CanDelete tinyint(1) NOT NULL DEFAULT '1',
  Version double NOT NULL DEFAULT '1',
  StartNum int(10) DEFAULT NULL,
  RolloverNum int(10) DEFAULT NULL,
  NextNum int(10)  DEFAULT NULL,
  IsPadded tinyint(1) DEFAULT '1',
  FixNumLength int(10) DEFAULT NULL,
  FileGrouping int(1) DEFAULT '2',
  PRIMARY KEY (UID),
  UNIQUE KEY ID (PortName)
);

#
# Table structure for table 'rfc'
#

DROP TABLE IF EXISTS rfc;
CREATE TABLE IF NOT EXISTS rfc (
  UID bigint(20) NOT NULL DEFAULT '0',
  RfcName varchar(18) NOT NULL DEFAULT '',
  Description varchar(80) NOT NULL DEFAULT '',
  ConnectionType varchar(2) NOT NULL DEFAULT 'T',
  Host varchar(80) NOT NULL DEFAULT '',
  PortNumber int(5),
  UseCommandFile tinyint(1) NOT NULL DEFAULT '0',
  CommandFileDir varchar(120) DEFAULT NULL,
  CommandFile varchar(80) DEFAULT NULL,
  CommandLine varchar(120) DEFAULT NULL,
  CanDelete tinyint(1) NOT NULL DEFAULT '1',
  Version double NOT NULL DEFAULT '1',
  PRIMARY KEY (UID),
  UNIQUE KEY ID (RfcName)
);


# MySQL-Front Dump 2.1
#
# Host: localhost   Database: userdb
#--------------------------------------------------------
# Server version 4.0.0-alpha

USE userdb;

#
# Table structure for table 'gridtalk_license'
#

DROP TABLE IF EXISTS gridtalk_license;
CREATE TABLE IF NOT EXISTS gridtalk_license (
  UID bigint(20) NOT NULL DEFAULT '0',
  LicenseUid bigint(20) NOT NULL,
  OsName text NOT NULL DEFAULT '',
  OsVersion text NOT NULL DEFAULT '',
  MachineName text NOT NULL DEFAULT '',
  StartDate text NOT NULL DEFAULT '',
  EndDate text NOT NULL DEFAULT '',
  CanDelete tinyint(1) NOT NULL DEFAULT '1',
  Version double NOT NULL DEFAULT '1',
  PRIMARY KEY (UID),
  UNIQUE KEY ID (LicenseUid)
);

# MySQL-Front Dump 2.1
#
# Host: localhost   Database: userdb
#--------------------------------------------------------
# Server version 4.0.0-alpha

USE userdb;


#
# Table structure for table 'session_audit'
#

DROP TABLE IF EXISTS session_audit;
CREATE TABLE IF NOT EXISTS session_audit (
  UID bigint(20) NOT NULL auto_increment,
  SessionId varchar(30) NOT NULL DEFAULT '' ,
  SessionName varchar(30) ,
  State smallint(6) NOT NULL DEFAULT '0' ,
  SessionData longblob ,
  OpenTime datetime NOT NULL DEFAULT '0000-00-00 00:00:00' ,
  LastActiveTime datetime NOT NULL DEFAULT '0000-00-00 00:00:00' ,
  DestroyTime datetime ,
  PRIMARY KEY (UID),
  UNIQUE KEY SessionIDIdx (SessionId)
);




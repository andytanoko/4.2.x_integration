# MySQL-Front Dump 2.1
#
# Host: localhost   Database: userdb
#--------------------------------------------------------
# Server version 4.0.0-alpha

USE userdb;

#
# Table structure for table 'search_query'
#

DROP TABLE IF EXISTS search_query;
CREATE TABLE IF NOT EXISTS search_query (
  UID bigint(20) NOT NULL DEFAULT '0' ,
  Name varchar(30) NOT NULL DEFAULT '' ,
  Description varchar(80) NOT NULL DEFAULT '' ,
  CreatedBy varchar(15) NOT NULL DEFAULT '' ,
  Conditions blob,
  CanDelete tinyint(1) NOT NULL DEFAULT '1' ,
  Version double NOT NULL DEFAULT '1' ,
  PRIMARY KEY (UID),
  UNIQUE KEY NAME_IDX (Name)
);


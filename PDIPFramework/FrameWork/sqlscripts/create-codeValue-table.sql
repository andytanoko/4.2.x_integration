# MySQL-Front Dump 2.1
#
# Host: localhost   Database: userdb
#--------------------------------------------------------
# Server version 4.0.0-alpha

USE userdb;


#
# Table structure for table 'code_value'
#

DROP TABLE IF EXISTS code_value;
CREATE TABLE IF NOT EXISTS code_value (
  UID bigint(20) NOT NULL auto_increment,
  Code varchar(80) NOT NULL DEFAULT '' ,
  Description varchar(80) ,
  EntityType varchar(255) ,
  FieldID int(3) ,
  PRIMARY KEY (UID),
  UNIQUE KEY Code_idx (Code)
);
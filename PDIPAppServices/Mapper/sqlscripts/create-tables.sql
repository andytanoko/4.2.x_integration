# MySQL-Front Dump 2.1
#
# Host: localhost   Database: userdb
#--------------------------------------------------------
# Server version 4.0.0-alpha

USE userdb;

#
# Table structure for table 'mapping_file'
#

DROP TABLE IF EXISTS mapping_file;
CREATE TABLE IF NOT EXISTS mapping_file (
  UID bigint(20) NOT NULL DEFAULT '0' ,
  Name varchar(30) NOT NULL DEFAULT '' ,
  Description varchar(80) NOT NULL DEFAULT '' ,
  Filename varchar(80) NOT NULL DEFAULT '' ,
  Path varchar(80) NOT NULL DEFAULT '' ,
  SubPath varchar(80) NOT NULL DEFAULT '' ,
  Type int(1) NOT NULL DEFAULT '-1' ,
  CanDelete tinyint(1) NOT NULL DEFAULT '1' ,
  Version double NOT NULL DEFAULT '1' ,
  PRIMARY KEY (UID),
  UNIQUE KEY NAME_IDX (Name)
);

DROP TABLE IF EXISTS mapping_rule;
CREATE TABLE IF NOT EXISTS mapping_rule (
  UID bigint(20) NOT NULL DEFAULT '0' ,
  Name varchar(30) NOT NULL DEFAULT '' ,
  Description varchar(80) NOT NULL DEFAULT '' ,
  Type int(1) NOT NULL DEFAULT '-1' ,
  MappingFileUID bigint(20) NOT NULL DEFAULT '0' ,
  TransformRefDoc tinyint(1) DEFAULT '0',
  ReferenceDocUID bigint(20) ,
  XPath longtext DEFAULT '',
  ParamName varchar(40) DEFAULT '',
  KeepOriginal tinyint(1) DEFAULT '0' ,
  CanDelete tinyint(1) NOT NULL DEFAULT '1' ,
  Version double NOT NULL DEFAULT '1' ,
  PRIMARY KEY (UID),
  UNIQUE KEY NAME_IDX (Name)
);

DROP TABLE IF EXISTS xpath_mapping;
CREATE TABLE IF NOT EXISTS xpath_mapping (
  UID bigint(20) NOT NULL DEFAULT '0' ,
  RootElement varchar(120) NOT NULL DEFAULT '' ,
  XpathUid bigint(20) NOT NULL DEFAULT '0' ,
  CanDelete tinyint(1) NOT NULL DEFAULT '1' ,
  Version double NOT NULL DEFAULT '1' ,
  PRIMARY KEY (UID),
  UNIQUE KEY ID1 (RootElement),
  UNIQUE KEY ID2 (XpathUid)
);

# MySQL-Front Dump 2.1
#
# Host: localhost   Database: userdb
#--------------------------------------------------------
# Server version 4.0.0-alpha

USE userdb;

#
# Table structure for table 'partner_type'
#

DROP TABLE IF EXISTS partner_type;
CREATE TABLE IF NOT EXISTS partner_type (
  UID bigint(20) NOT NULL DEFAULT '0' ,
  Name char(3) NOT NULL DEFAULT '' ,
  Description varchar(80) NOT NULL DEFAULT '' ,
  CanDelete tinyint(1) NOT NULL DEFAULT '1' ,
  Version double NOT NULL DEFAULT '1' ,
  PRIMARY KEY (UID),
  UNIQUE KEY NAME_IDX (Name)
);

#
# Table structure for table 'partner_group'
#

DROP TABLE IF EXISTS partner_group;
CREATE TABLE IF NOT EXISTS partner_group (
  UID bigint(20) NOT NULL DEFAULT '0' ,
  Name char(3) NOT NULL DEFAULT '' ,
  Description varchar(80) NOT NULL DEFAULT '' ,
  PartnerTypeUID bigint(20) NOT NULL DEFAULT '0' ,
  CanDelete tinyint(1) NOT NULL DEFAULT '1' ,
  Version double NOT NULL DEFAULT '1' ,
  PRIMARY KEY (UID),
  UNIQUE KEY NAME_PARTNERTYPE_IDX (Name,PartnerTypeUID)
);

#
# Table structure for table 'partner'
#

DROP TABLE IF EXISTS partner;
CREATE TABLE IF NOT EXISTS partner (
  UID bigint(20) NOT NULL DEFAULT '0' ,
  PartnerID varchar(20) NOT NULL DEFAULT '' ,
  Name varchar(20) NOT NULL DEFAULT '' ,
  Description varchar(80) NOT NULL DEFAULT '' ,
  PartnerTypeUID bigint(20) NOT NULL DEFAULT '0' ,
  PartnerGroupUID bigint(20) ,
  CreateTime datetime ,
  CreateBy varchar(15) ,
  State smallint(1) NOT NULL DEFAULT '1' ,
  CanDelete tinyint(1) NOT NULL DEFAULT '1' ,
  Version double NOT NULL DEFAULT '1' ,
  PRIMARY KEY (UID),
  UNIQUE KEY PARTNER_ID_IDX (PartnerID)
);
# Change Log
#
# 18 Aug 2003 GT 2.2 I1 [Neo Sok Lay] Add table for RegistryObjectMapping entity.
# 24 Sep 2003 GT 2.2 I1 [Neo Sok Lay] Add table for RegistryConnectInfo entity.
# 23 Dec 2003 GT 2.3 I1 [Neo Sok Lay] Add table for DomainIdentifier entity.
# 17 Oct 2005 GT 4.0 I1 [Neo Sok Lay] Change to create registry_obj_idx on 253chars of RegistryQueryUrl -- to cater for max 1000byte limit on utf8 charset
#

USE userdb;


#
# Table structure for table 'business_entity'
#

DROP TABLE IF EXISTS business_entity;
CREATE TABLE IF NOT EXISTS business_entity (
  UID bigint(20) NOT NULL DEFAULT '0' ,
  ID varchar(4) NOT NULL DEFAULT '' ,
  EnterpriseID varchar(20) DEFAULT NULL ,
  Description varchar(80) NOT NULL DEFAULT '' ,
  IsPartner tinyint(1) unsigned NOT NULL DEFAULT '0' ,
  PartnerCat tinyint(1) unsigned DEFAULT NULL,
  IsPublishable tinyint(1) unsigned NOT NULL DEFAULT '1' ,
  IsSyncToServer tinyint(1) unsigned NOT NULL DEFAULT '1' ,
  State smallint(2) NOT NULL DEFAULT '0' ,
  CanDelete tinyint(1) unsigned NOT NULL DEFAULT '1' ,
  Version double NOT NULL DEFAULT '1' ,
  PRIMARY KEY (UID),
  UNIQUE KEY enterprise_be_idx (EnterpriseID,ID)
);



#
# Table structure for table 'whitepage'
#

DROP TABLE IF EXISTS whitepage;
CREATE TABLE IF NOT EXISTS whitepage (
  UID bigint(20) NOT NULL DEFAULT '0' ,
  BizEntityUID bigint(20) NOT NULL DEFAULT '0' ,
  BusinessDesc varchar(80) ,
  DUNS varchar(20) ,
  GlobalSCCode varchar(255) ,
  ContactPerson varchar(80) ,
  Email varchar(255) ,
  Tel varchar(16) ,
  Fax varchar(16) ,
  Website varchar(255) ,
  Address varchar(255) ,
  City varchar(50) ,
  State varchar(6) ,
  ZipCode varchar(10) ,
  POBox varchar(10) ,
  Country char(3) ,
  Language char(3) ,
  PRIMARY KEY (UID),
  UNIQUE KEY whitepage_by_be_idx (BizEntityUID)
);

#
# Table structure for table 'registry_object_map'
#

DROP TABLE IF EXISTS registry_object_map;
CREATE TABLE IF NOT EXISTS registry_object_map (
  UID bigint(20) NOT NULL DEFAULT '0' ,
  RegistryObjectKey varchar(50) NOT NULL,
  RegistryObjectType varchar(30)NOT NULL,
  RegistryQueryUrl varchar(255) NOT NULL,
  RegistryPublishUrl varchar(255) NOT NULL,
  ProprietaryObjectKey varchar(50) NOT NULL,
  ProprietaryObjectType varchar(30) NOT NULL,
  IsPublishedObject tinyint(1) unsigned NOT NULL DEFAULT '1' ,
  State smallint(2) NOT NULL DEFAULT '0' ,
  PRIMARY KEY (UID),
  UNIQUE KEY registry_obj_idx (RegistryObjectKey,RegistryObjectType,RegistryQueryUrl(253))
);

#
# Table structure for table 'registry_connect_info'
#

DROP TABLE IF EXISTS registry_connect_info;
CREATE TABLE IF NOT EXISTS registry_connect_info (
  UID bigint(20) NOT NULL DEFAULT '0' ,
  Name varchar(50) NOT NULL,
  QueryUrl varchar(255) NOT NULL,
  PublishUrl varchar(255),
  PublishUser varchar(20),
  PublishPassword varchar(20),
  CanDelete tinyint(1) unsigned NOT NULL DEFAULT '1' ,
  Version double NOT NULL DEFAULT '1' ,
  PRIMARY KEY (UID),
  UNIQUE KEY registry_conn_idx (Name)
);

#
# Table structure for table 'domain_identifier'
#

DROP TABLE IF EXISTS domain_identifier;
CREATE TABLE IF NOT EXISTS domain_identifier (
  UID bigint(20) NOT NULL DEFAULT '0' ,
  BizEntityUID bigint(20) NOT NULL,
  Domain varchar(30) NOT NULL,
  Type varchar(50) NOT NULL,
  Value varchar(255),
  CanDelete tinyint(1) unsigned NOT NULL DEFAULT '1' ,
  PRIMARY KEY (UID),
  UNIQUE KEY identifier_idx (Type,Value)
);


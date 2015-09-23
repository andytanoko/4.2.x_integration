USE userdb;


#
# Table structure for table 'coy_profile'
#

DROP TABLE IF EXISTS coy_profile;
CREATE TABLE IF NOT EXISTS coy_profile (
  UID bigint(20) NOT NULL DEFAULT '0' ,
  CoyName varchar(255) NOT NULL DEFAULT '' ,
  Email varchar(255) ,
  AltEmail varchar(255) ,
  Tel varchar(16) ,
  AltTel varchar(16) ,
  Fax varchar(16) ,
  Address varchar(255) ,
  City varchar(50) ,
  State varchar(6) ,
  ZipCode varchar(10) ,
  Country char(3) ,
  Language char(2) ,
  IsPartner tinyint(1) UNSIGNED NOT NULL DEFAULT '1',
  CanDelete tinyint(1) UNSIGNED NOT NULL DEFAULT '1',
  Version double NOT NULL DEFAULT '1',
  PRIMARY KEY (UID),
  UNIQUE KEY Coy_name_idx (CoyName)
);


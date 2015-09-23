USE userdb;


#
# Table structure for table 'license'
#

DROP TABLE IF EXISTS license;
CREATE TABLE IF NOT EXISTS license (
  UID bigint(20) NOT NULL DEFAULT '0' ,
  ProductKey varchar(30) NOT NULL DEFAULT '' ,
  ProductName varchar(80) NOT NULL DEFAULT '',
  ProductVersion varchar(20) NOT NULL DEFAULT '' ,
  StartDate datetime NOT NULL ,
  EndDate datetime NOT NULL,
  State smallint(1) NOT NULL DEFAULT '1',
  Version double NOT NULL DEFAULT '1',
  PRIMARY KEY (UID),
  KEY product_license_idx (ProductName,ProductVersion)
);


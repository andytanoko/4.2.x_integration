USE userdb;

#
# Table structure for table 'webservice'
#

DROP TABLE IF EXISTS webservice;
CREATE TABLE IF NOT EXISTS webservice (
  UID bigint(20) NOT NULL DEFAULT '0' ,
  WsdlUrl varchar(255) NOT NULL DEFAULT '' ,
  EndPoint varchar(255) NOT NULL DEFAULT '' ,
  ServiceName varchar(20) NOT NULL ,
  ServiceGroup varchar(20) NOT NULL ,
  CanDelete tinyint(1) unsigned NOT NULL DEFAULT '1' ,
  Version double NOT NULL DEFAULT '1' ,
  PRIMARY KEY (UID),
  UNIQUE KEY webservice_grp_idx (ServiceName,ServiceGroup)
);

#
# Table structure for table 'service_assignment'
#

DROP TABLE IF EXISTS service_assignment;
CREATE TABLE IF NOT EXISTS service_assignment (
  UID bigint(20) NOT NULL DEFAULT '0' ,
  UserName varchar(20) NOT NULL ,
  Password varchar(80) NOT NULL ,
  UserType varchar(20) NOT NULL ,
  WebServiceUIds text ,
  CanDelete tinyint(1) unsigned NOT NULL DEFAULT '1' ,
  Version double NOT NULL DEFAULT '1' ,
  PRIMARY KEY (UID),
  UNIQUE KEY service_asigmt_user_idx (UserName,UserType)
);  
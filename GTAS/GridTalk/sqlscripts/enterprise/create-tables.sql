USE userdb;


#
# Table structure for table 'resource_link'
#

DROP TABLE IF EXISTS resource_link;
CREATE TABLE IF NOT EXISTS resource_link (
  UID bigint(20) NOT NULL DEFAULT '0' ,
  FromResourceType varchar(50) NOT NULL DEFAULT '' ,
  FromResourceUID bigint(20) NOT NULL DEFAULT '0',
  ToResourceType varchar(50) NOT NULL DEFAULT '' ,
  ToResourceUID bigint(20) NOT NULL DEFAULT '0' ,
  Priority smallint(5)  NOT NULL DEFAULT '0' ,
  CanDelete tinyint(1) unsigned NOT NULL DEFAULT '1' ,
  Version double NOT NULL DEFAULT '1' ,
  PRIMARY KEY (UID),
  UNIQUE KEY hierarchy_idx (FromResourceType,FromResourceUID,ToResourceType,ToResourceUID)
);

#
# Table structure for table 'shared_resource'
#

DROP TABLE IF EXISTS shared_resource;
CREATE TABLE IF NOT EXISTS shared_resource (
  UID bigint(20) NOT NULL DEFAULT '0' ,
  ToEnterpriseID varchar(20) NOT NULL DEFAULT '' ,
  ResourceUID bigint(20) NOT NULL DEFAULT '0' ,
  ResourceType varchar(50) NOT NULL DEFAULT '' ,
  State tinyint(1) unsigned NOT NULL DEFAULT '0' ,
  SyncChecksum bigint(20) ,
  CanDelete tinyint(1) unsigned NOT NULL DEFAULT '1' ,
  Version double NOT NULL DEFAULT '1' ,
  PRIMARY KEY (UID),
  UNIQUE KEY shareto_idx (ToEnterpriseID,ResourceType,ResourceUID)
);



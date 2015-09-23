USE appdb;


#
# Table structure for table 'gncategory'
#

DROP TABLE IF EXISTS gncategory;
CREATE TABLE IF NOT EXISTS gncategory (
  Code char(3) NOT NULL DEFAULT '' ,
  Name varchar(50) NOT NULL DEFAULT '' ,
  NodeUsage varchar(10) NOT NULL DEFAULT '',
  PRIMARY KEY (Code),
  UNIQUE KEY gncategory_idx (Name)
);


USE userdb;

#
# Table structure for table 'gridnode'
#

DROP TABLE IF EXISTS gridnode;
CREATE TABLE IF NOT EXISTS gridnode (
  UID bigint(20) NOT NULL DEFAULT '0',
  ID varchar(10) NOT NULL DEFAULT '',
  Name varchar(80) NOT NULL DEFAULT '',
  Category char(3) NOT NULL DEFAULT '',
  State smallint(2) NOT NULL,
  ActivationReason varchar(255),
  DTCreated datetime DEFAULT NULL,
  DTReqActivate datetime DEFAULT NULL,
  DTActivated datetime DEFAULT NULL,
  DTDeactivated datetime DEFAULT NULL,
  CoyProfileUID bigint(20) DEFAULT NULL ,
  Version double NOT NULL DEFAULT '1',
  PRIMARY KEY (UID),
  UNIQUE KEY gridnodeid_idx (ID)
);

#
# Table structure for table 'connection_status'
#

DROP TABLE IF EXISTS connection_status;
CREATE TABLE IF NOT EXISTS connection_status (
  UID bigint(20) NOT NULL DEFAULT '0',
  NodeID varchar(10) NOT NULL DEFAULT '',
  StatusFlag smallint(2) NOT NULL,
  DTLastOnline datetime DEFAULT NULL,
  DTLastOffline datetime DEFAULT NULL,
  ReconnectionKey blob DEFAULT NULL,
  ConnectedServerNode varchar(10) DEFAULT NULL,
  PRIMARY KEY (UID),
  UNIQUE KEY gridnode_conn_idx (NodeID)
);


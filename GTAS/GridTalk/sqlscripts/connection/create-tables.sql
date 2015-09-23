USE userdb;

#
# Table structure for table 'jms_router'
#

DROP TABLE IF EXISTS jms_router;
CREATE TABLE IF NOT EXISTS jms_router (
  UID bigint(20) NOT NULL DEFAULT '0',
  Name varchar(50) NOT NULL,
  IpAddress varchar(50) NOT NULL,
  PRIMARY KEY (UID)
);



USE userdb;

#
# Table structure for table 'activation_record'
#

DROP TABLE IF EXISTS activation_record;
CREATE TABLE IF NOT EXISTS activation_record (
  UID bigint(20) NOT NULL DEFAULT '0',
  CurrentType tinyint(1) NOT NULL DEFAULT '0',
  ActDirection tinyint(1) DEFAULT NULL,
  DeactDirection tinyint(1) DEFAULT NULL,
  GridNodeID integer(10) NOT NULL,
  GridNodeName varchar(80),
  DTRequested datetime DEFAULT NULL,
  DTApproved datetime DEFAULT NULL,
  DTAborted datetime DEFAULT NULL,
  DTDeactivated datetime DEFAULT NULL,
  DTDenied datetime DEFAULT NULL ,
  IsLatest tinyint(1) NOT NULL DEFAULT '1',
  TransCompleted tinyint(1) NOT NULL DEFAULT '0',
  TransFailReason varchar(255),
  ActivationDetails mediumtext,
  PRIMARY KEY (UID)
);



# 08 Feb 2006 GT4.0 [Neo Sok Lay] Increate PartnerFunctionId to 30 chars

USE userdb;

#
# Table structure for table 'partner_function'
#
DROP TABLE IF EXISTS partner_function;
CREATE TABLE IF NOT EXISTS partner_function (
  UID bigint(20) NOT NULL DEFAULT '0' ,
  PartnerFunctionId varchar(30) NOT NULL DEFAULT '' ,
  Description varchar(80) NOT NULL DEFAULT '' ,
  TriggerOn smallint(2) NOT NULL DEFAULT '0' ,
  WorkflowActivityUids text ,
  CanDelete tinyint(1) NOT NULL DEFAULT '1' ,
  Version double NOT NULL DEFAULT '1' ,
  PRIMARY KEY (UID),
  UNIQUE KEY ID (PartnerFunctionId)
);

DROP TABLE IF EXISTS workflow_activity;
CREATE TABLE IF NOT EXISTS workflow_activity (
  UID bigint(20) NOT NULL DEFAULT '0' ,
  ActivityType smallint(2) NOT NULL ,
  Description varchar(80) NOT NULL DEFAULT '' ,
  ParamList text ,
  CanDelete tinyint(1) NOT NULL DEFAULT '1' ,
  Version double NOT NULL DEFAULT '1' ,
  PRIMARY KEY (UID)
);

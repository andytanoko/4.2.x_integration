# Change Log
# 06 Oct 2005 GT 4.0 I1  [Neo Sok Lay] Change 'trigger' table name to 'pf_trigger' -- clash with Trigger introduced in MySQL5
#                                      Invalid default value for 'TriggerLevel', 'TriggerType' - rejected by MySQL5 
# 08 Feb 2006 GT 4.0 I1  [Neo Sok Lay] Increase PartnerFunctionId column size to 30chars
# 05 Dec 2006 GT 4.0 I3  [Neo Sok Lay] Increase DocType field length to 30 chars.


USE userdb;

#
# Table structure for table 'pf_trigger'
#

DROP TABLE IF EXISTS pf_trigger;
CREATE TABLE IF NOT EXISTS pf_trigger (
  UID bigint(20) NOT NULL DEFAULT '0' ,
  TriggerLevel smallint(2) NOT NULL DEFAULT '0' ,
  PartnerFunctionId varchar(30) NOT NULL DEFAULT '' ,
  ProcessId varchar(80) DEFAULT NULL ,
  DocType varchar(30) DEFAULT NULL ,
  PartnerType varchar(3) DEFAULT NULL ,
  PartnerGroup varchar(3) DEFAULT NULL ,
  PartnerId varchar(20) DEFAULT NULL ,
  TriggerType smallint(2) NOT NULL DEFAULT '0' ,
  IsRequest tinyint(1) NOT NULL DEFAULT '1' ,
  CanDelete tinyint(1) NOT NULL DEFAULT '1' ,
  Version double NOT NULL DEFAULT '1' ,
  IsLocalPending tinyint(1) NOT NULL DEFAULT '1' ,
  NumOfRetries smallint DEFAULT '0',
  RetryInterval int DEFAULT '0',
  ChannelUID bigint DEFAULT '-99999999',
  PRIMARY KEY (UID),
  UNIQUE KEY ID (TriggerLevel,TriggerType,DocType,PartnerType,PartnerGroup,PartnerId)
);

DROP TABLE IF EXISTS process_mapping;
CREATE TABLE IF NOT EXISTS process_mapping (
  UID bigint(20) NOT NULL DEFAULT '0' ,
  ProcessDef varchar(80) NOT NULL DEFAULT '' ,
  PartnerID varchar(20) NOT NULL DEFAULT '' ,
  IsInitiatingRole tinyint(1) NOT NULL DEFAULT '1',
  DocType varchar(30) DEFAULT NULL,
  SendChannelUID bigint(20) DEFAULT NULL,
  ProcessIndicatorCode varchar(80),
  ProcessVersionID varchar(80),
  PartnerRoleMapping bigint(20) DEFAULT NULL,
  CanDelete tinyint(1) NOT NULL DEFAULT '1' ,
  Version double NOT NULL DEFAULT '1' ,
  PRIMARY KEY (UID),
  UNIQUE KEY process_mapping_idx (ProcessDef,PartnerID,IsInitiatingRole)
);

DROP TABLE IF EXISTS biz_cert_mapping;
CREATE TABLE IF NOT EXISTS biz_cert_mapping (
  UID bigint(20) NOT NULL DEFAULT '0' ,
  PartnerID varchar(20) NOT NULL DEFAULT '' ,
  PartnerCertUID bigint(20) NOT NULL DEFAULT '0',
  OwnCertUID bigint(20) NOT NULL DEFAULT '0',
  CanDelete tinyint(1) NOT NULL DEFAULT '1' ,
  Version double NOT NULL DEFAULT '1' ,
  PRIMARY KEY (UID),
  UNIQUE KEY biz_cert_mapping_idx (PartnerID)
);

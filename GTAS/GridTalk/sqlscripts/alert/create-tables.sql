# 06 Oct 2005 GT 4.0 I1  [Neo Sok Lay] Invalid default value for 'Level' - rejected by MySQL5
# 05 Dec 2006 GT 4.0 I3  [Neo Sok Lay] Increase DocType length to 30 chars.

USE userdb;

#
# Table structure for table 'alert_trigger'
#

DROP TABLE IF EXISTS alert_trigger;
CREATE TABLE IF NOT EXISTS alert_trigger (
  UID bigint(20) NOT NULL DEFAULT '0' ,
  Level smallint(2) NOT NULL DEFAULT '0' ,
  AlertType varchar(35) NOT NULL DEFAULT '' ,
  AlertUID bigint(20) NOT NULL DEFAULT '0' ,
  DocType varchar(30) DEFAULT NULL ,
  PartnerType varchar(3) DEFAULT NULL ,
  PartnerGroup varchar(3) DEFAULT NULL ,
  PartnerId varchar(20) DEFAULT NULL ,
  Recipients text DEFAULT NULL ,
  Enabled tinyint(1) NOT NULL DEFAULT '1' ,
  AttachDoc tinyint(1) NOT NULL DEFAULT '0' ,
  CanDelete tinyint(1) NOT NULL DEFAULT '1' ,
  Version double NOT NULL DEFAULT '1' ,
  PRIMARY KEY (UID),
  UNIQUE KEY ID (Level,AlertType,DocType,PartnerType,PartnerGroup,PartnerId)
);

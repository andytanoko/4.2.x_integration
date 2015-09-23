# Change Log
# 03 Mar 2003 I8 v2.0.18 [Neo Sok Lay] Add Version and CanDelete columns to various Alert entities except AlertAction.
# 26 Jun 2003 I1 V2.1    [Neo Sok Lay] Change alert.Category to DEFAULT NULL.
# 06 Oct 2005 I1 V4.0    [Neo Sok Lay] Change alert.Trigger field to alert.TriggerCond
# 28 Dec 2005 I1 V4.0    [Tam Wei Xiang] Added table jms_destination and jms_msg_record
# 17 Feb 2006 I1 v4.0    [Neo Sok Lay] Change jms_msg_record.JmsDestName to JmsDestUid

USE userdb;


#
# Table structure for table 'action'
#

DROP TABLE  IF EXISTS action;
CREATE TABLE IF NOT EXISTS action (
  UID bigint(20) NOT NULL DEFAULT '0' ,
  Name varchar(80) NOT NULL DEFAULT '' ,
  Description varchar(255) ,
  MsgUid bigint(20) ,
  Version double NOT NULL DEFAULT '1',
  CanDelete tinyint(1) NOT NULL DEFAULT '1',
  PRIMARY KEY (UID),
  UNIQUE name_idx (Name)
);



#
# Table structure for table 'alert'
#

DROP TABLE  IF EXISTS alert;
CREATE TABLE IF NOT EXISTS alert (
  UID bigint(20) NOT NULL DEFAULT '0' ,
  Name varchar(80) NOT NULL DEFAULT '' ,
  AlertType bigint(20) NOT NULL DEFAULT '0' ,
  Category bigint(20) DEFAULT NULL ,
  TriggerCond longtext DEFAULT NULL ,
  Description varchar(255) ,
  Version double NOT NULL DEFAULT '1',
  CanDelete tinyint(1) NOT NULL DEFAULT '1',
  PRIMARY KEY (UID),
  UNIQUE name_idx (Name)
);



#
# Table structure for table 'alert_action'
#

DROP TABLE  IF EXISTS alert_action;
CREATE TABLE IF NOT EXISTS alert_action (
  UID bigint(20) NOT NULL DEFAULT '0' ,
  AlertUid bigint(20) NOT NULL DEFAULT '0' ,
  ActionUid bigint(20) NOT NULL DEFAULT '0' ,
  PRIMARY KEY (UID),
  UNIQUE AlertAction_idx (AlertUid,ActionUid)
);



#
# Table structure for table 'alert_category'
#

DROP TABLE  IF EXISTS alert_category;
CREATE TABLE IF NOT EXISTS alert_category (
  UID bigint(20) NOT NULL DEFAULT '0' ,
  Code varchar(20) NOT NULL DEFAULT '0' ,
  Name varchar(80) ,
  Description varchar(255) ,
  Version double NOT NULL DEFAULT '1',
  CanDelete tinyint(1) NOT NULL DEFAULT '1',
  PRIMARY KEY (UID),
  UNIQUE name_index (Code)
);



#
# Table structure for table 'alert_list'
#

DROP TABLE  IF EXISTS alert_list;
CREATE TABLE IF NOT EXISTS alert_list (
  UID bigint(20) NOT NULL DEFAULT '0' ,
  UserUid bigint(20) NOT NULL DEFAULT '0' ,
  FromUid bigint(20) ,
  Title varchar(35) ,
  Message longtext ,
  ReadStatus tinyint(1) DEFAULT '0' ,
  Date date ,
  Version double NOT NULL DEFAULT '1',
  CanDelete tinyint(1) NOT NULL DEFAULT '1',
  PRIMARY KEY (UID)
);


#
# Table structure for table 'message_template'
#

DROP TABLE IF EXISTS message_template;
CREATE TABLE IF NOT EXISTS message_template (
  UID bigint(20) NOT NULL DEFAULT '0' ,
  Name varchar(80) NOT NULL DEFAULT '' ,
  ContentType varchar(30) ,
  MessageType varchar(30) ,
  FromAddr varchar(255) ,
  ToAddr varchar(255) ,
  CcAddr varchar(255) ,
  Subject varchar(255) ,
  Message text ,
  Location varchar(255) ,
  Append tinyint(1) DEFAULT '0' ,
  Version double NOT NULL DEFAULT '1',
  CanDelete tinyint(1) NOT NULL DEFAULT '1',
  JmsDestination bigint(20) default NULL,
  MessageProperties mediumtext collate utf8_unicode_ci,
  PRIMARY KEY (UID),
  UNIQUE name_index (Name)
);



#
# Table structure for table 'alert_type'
#

DROP TABLE  IF EXISTS alert_type;
CREATE TABLE IF NOT EXISTS alert_type (
  UID bigint(20) NOT NULL DEFAULT '0' ,
  Name varchar(35) NOT NULL DEFAULT '' ,
  Description varchar(100) ,
  PRIMARY KEY (UID),
  Version double NOT NULL DEFAULT '1',
  CanDelete tinyint(1) NOT NULL DEFAULT '1',
  UNIQUE name_idx (Name)
);

#
# Table structure for table 'jms_destination'
#

DROP TABLE  IF EXISTS jms_destination;
CREATE TABLE IF NOT EXISTS jms_destination (
  `UID` bigint(20) NOT NULL,
  `Version` double NOT NULL default '1',
  `CanDelete` tinyint(1) NOT NULL default '1',
  `Name` varchar(30) collate utf8_unicode_ci default NULL,
  `Type` int(1) default '1',
  `JndiName` varchar(255) collate utf8_unicode_ci default NULL,
  `DeliveryMode` int(1) default '0',
  `Priority` int(2) default '-1',
  `ConnectionFactoryJndi` varchar(255) collate utf8_unicode_ci default NULL,
  `ConnectionUser` varchar(30) collate utf8_unicode_ci default NULL,
  `ConnectionPassword` varchar(30) collate utf8_unicode_ci default NULL,
  `LookupProperties` mediumtext collate utf8_unicode_ci,
  `RetryInterval` int(11) default NULL,
  `MaximumRetries` int(11) default NULL,
   UNIQUE KEY `Name` (`Name`),
   PRIMARY KEY  (`UID`)
);

#
# Table structure for table 'jms_msg_record'
#

DROP TABLE  IF EXISTS jms_msg_record;
CREATE TABLE `jms_msg_record` (
  `UID` bigint(20) NOT NULL,
  `Version` double NOT NULL default '1',
  `CanDelete` tinyint(1) NOT NULL default '1',
  `JmsDestUid` bigint(20) NOT NULL,
  `AlertTimestamp` datetime default NULL,
  `MsgData` mediumtext collate utf8_unicode_ci,
  `PermanentFailed` tinyint(1) default '0',
  `AlertTimeInLong` bigint(20) default NULL,
  PRIMARY KEY  (`UID`)
);
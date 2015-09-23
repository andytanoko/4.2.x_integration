# Change Log
# 25 Mar 2004 GT 2.1.20 [Neo Sok Lay] Index rtprocess, rtprocessdoc, rtrestriction, and rtactivity tables for 
#                                     better performance.
# 04 Apr 2007 GT 4.1    [Tam Wei Xiang]  Create view for 'rtprocess' and 'rtprocessdoc' in order
#                                        to support archive by customer
# 29 FEB 2008 GT 4.0.2.3[Tam Wei Xiang] Expand rtprocessdoc's documentId to 255 chars
#                                        

USE userdb;

#
#
# Table structure for table 'rtprocess'
#

DROP TABLE IF EXISTS rtprocess;
CREATE TABLE `rtprocess` (
  `UID` bigint(11) NOT NULL default '0',
  `ProcessUId` bigint(11) NOT NULL default '0',
  `State` tinyint(20) NOT NULL default '0',
  `ProcessType` varchar(100) NOT NULL default '',
  `ParentRtActivityUId` bigint(11) default NULL,
  `MaxConcurrency` int(11) NOT NULL default '0',
  `FinishInterval` bigint(11) default NULL,
  `StartTime` datetime default NULL,
  `EndTime` datetime default NULL,
  `ContextUId` bigint(11) default NULL,
  `EngineType` varchar(20) default NULL ,
  `ProcessDefKey` varchar(200) default NULL ,
  PRIMARY KEY  (`UID`),
  INDEX parentrtactivityuid_i (ParentRtActivityUId)
) TYPE = MyISAM;


#
#
# Table structure for table 'rtactivity'
#

DROP TABLE IF EXISTS rtactivity;
CREATE TABLE `rtactivity` (
  `UID` bigint(11) NOT NULL default '0',
  `ActivityUId` bigint(11) NOT NULL default '0',
  `State` tinyint(20) NOT NULL default '0',
  `Priority` tinyint(20) default NULL,
  `RtProcessUId` bigint(11) NOT NULL default '0',
  `ActivityType` varchar(100) NOT NULL default '',
  `FinishInterval` bigint(11) default NULL,
  `StartTime` datetime default NULL,
  `EndTime` datetime default NULL,
  `ContextUId` bigint(11) default NULL,
  `EngineType` varchar(20) default NULL ,
  `BranchName` varchar(50) default NULL ,
  `ProcessDefKey` varchar(200) default NULL ,
  PRIMARY KEY  (`UID`),
  INDEX rtactivity_processuid_i (RtProcessUId)
) TYPE = MyISAM;

#
#
# Table structure for table 'rtrestriction'
#

DROP TABLE IF EXISTS rtrestriction;
CREATE TABLE `rtrestriction` (
  `UID` bigint(11) NOT NULL default '0',
  `RestrictionUId` bigint(11) default NULL,
  `RestrictionType` varchar(80) default NULL,
  `SubRestrictionType` varchar(80) default NULL,
  `RtProcessUId` bigint(11) default NULL,
  `TransActivationStateListUId` bigint(11) default NULL,
  `ProcessDefKey` varchar(200) default NULL ,
  PRIMARY KEY  (`UID`),
  INDEX rtrestriction_processuid_i (RtProcessUId)
) TYPE = MyISAM;


#
#
# Table structure for table 'trans_activation_state'
#

DROP TABLE IF EXISTS trans_activation_state;
CREATE TABLE `trans_activation_state` (
  `UID` bigint(11) NOT NULL default '0',
  `TransitionUId` bigint(11) default NULL,
  `RtRestrictionUId` bigint(11) default NULL,
  `RtRestrictionType` varchar(80) default NULL,
  `State` tinyint(1) default 0,
  `ListUId` bigint(20),
  PRIMARY KEY  (`UID`)
) TYPE = MyISAM;

#
#
# Table structure for table 'rtprocessdoc'
#

DROP TABLE IF EXISTS rtprocessdoc;
CREATE TABLE `rtprocessdoc` (
  `UID` bigint(11) NOT NULL default '0',
  `DocumentId` varchar(255) default NULL,
  `DocumentType` varchar(80) default NULL,
  `DocumentName` varchar(80) default NULL,
  `BusinessTransActivityId` varchar(80) default NULL,
  `BinaryCollaborationUId` bigint(11) default NULL,
  `RtBinaryCollaborationUId` bigint(11) default NULL,
  `RtBusinessTransactionUId` bigint(11) default NULL,
  `IsPositiveResponse` tinyint(1) default 1,
  `DocProcessedFlag` tinyint(1) default 0,
  `AckReceiptSignalFlag` tinyint(1) default 0,
  `AckAcceptSignalFlag` tinyint(1) default 0,
  `ExceptionSignalType` varchar(80) default NULL,
  `RoleType` varchar(20) default NULL,
  `RetryCount` bigint(11) default 0,
  `RequestDocType` varchar(80) default NULL,
  `ResponseDocTypes` mediumblob default NULL,
  `PartnerKey` varchar(80) default NULL,
  `Status` bigint(11) default 0,
  `Reason` mediumblob default NULL,
  `CustomerBEId` varchar(4) default NULL,
  PRIMARY KEY  (`UID`),
  INDEX documentid_i(DocumentId),
  INDEX rtbinarycollaborationuid_i(RtBinaryCollaborationUId),
  INDEX rtbusinesstransactionuid_i(RtBusinessTransactionUId)
) TYPE = MyISAM;

DROP VIEW IF EXISTS archive_process_view;
CREATE VIEW `archive_process_view` AS
  SELECT
    `rp`.`UID` AS `RTProcessUID`,
    `rp`.`EngineType` AS `EngineType`,
    `rp`.`ProcessType` AS `ProcessType`,
    `rp`.`StartTime` AS `StartTime`,
    `rp`.`EndTime` AS `EndTime`,
    `rp`.`ProcessUId` AS `ProcessUId`,
    `rpd`.`UID` AS `RTProcessDocUID`,
    `rpd`.`CustomerBEId` AS `CustomerBEId`,
    `rpd`.`PartnerKey` AS `PartnerKey`
  FROM
    (`rtprocess` `rp` join `rtprocessdoc` `rpd`)
  WHERE
    (`rp`.`UID` = `rpd`.`RtBinaryCollaborationUId`);

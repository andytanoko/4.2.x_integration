# MySQL-Front Dump 2.1
#
# Host: localhost   Database: userdb
#--------------------------------------------------------
# Server version 4.0.0-alpha

USE archivedb;


#
# Table structure for table 'document_meta_info'
#

DROP TABLE IF EXISTS document_meta_info;
CREATE TABLE IF NOT EXISTS document_meta_info (
  UID bigint(20) NOT NULL auto_increment,
  GdocID bigint(20) ,
  Folder varchar(10) ,
  ProcessInstanceID varchar(200) ,
  OriginatorID varchar(15) ,
  PartnerDuns varchar(20) ,
  PartnerID varchar(15) ,
  PartnerName varchar(20) ,
  DocType varchar(12) ,
  Filename varchar(255) default NULL,
  GdocFilename varchar(255) default NULL,
  UDocFilename varchar(255) default NULL,
  ReceiptAuditFilename varchar(200) default NULL,
  AttachmentFilenames varchar(255) default NULL,
  IsContainAttachment tinyint(1) default '0',
  IsOriginalDoc tinyint(1) default '0',
  IsArchivedByPI tinyint(1) default '0',
  DocNumber varchar(50) ,
  DocDateGenerated varchar(20) default NULL,
  DateTimeSendStart datetime ,
  DateTimeSendEnd datetime ,
  DateTimeReceiveStart datetime ,
  DateTimeReceiveEnd datetime ,
  DateTimeCreate datetime ,
  DateTimeImport datetime ,
  DateTimeExport datetime ,
  Process_DefID varchar(80) ,
  FilePath varchar(255) ,
  SenderCert varchar(50) ,
  ReceiverCert varchar(50) ,
  RNIFVersion varchar(80) ,
  ProcessInstanceInfoUID bigint(20) DEFAULT 0,
  Remark varchar(255) default NULL,
  DocTransStatus varchar(255) default NULL,
  UserTrackingID varchar(80) default NULL,
  CanDelete tinyint(1) NOT NULL DEFAULT '1' ,
  Version double NOT NULL DEFAULT '1' ,
  PRIMARY KEY (UID),
  UNIQUE KEY (GdocID,Folder)
);



#
# Table structure for table 'process_instance_meta_info'
#

DROP TABLE IF EXISTS process_instance_meta_info;
CREATE TABLE IF NOT EXISTS process_instance_meta_info (
  UID bigint(20) NOT NULL auto_increment,
  CanDelete tinyint(1) NOT NULL DEFAULT '1' ,
  Version double NOT NULL DEFAULT '1' ,
  ProcessInstanceID varchar(200) ,
  ProcessState varchar(20) ,
  ProcessStartDate datetime ,
  ProcessEndDate datetime ,
  PartnerDuns varchar(20) ,
  ProcessDef varchar(80) ,
  RoleType varchar(20) ,
  PartnerID varchar(15) ,
  PartnerName varchar(20) ,
  DocNumber varchar(50) ,
  DocDateGenerated varchar(20) default NULL ,
  OriginatorID varchar(15) ,
  Remark varchar(255) default NULL,
  UserTrackingID varchar(80) default NULL,
  FailedReason tinyint(2) default NULL,
  DetailReason varchar(250) default NULL,
  RetryNumber bigint(11) default NULL,
  PRIMARY KEY (UID),
  UNIQUE KEY ProcessInstanceID (ProcessInstanceID,OriginatorID, ProcessStartDate)
);

#index for document_meta_info
create INDEX search1 on document_meta_info (DocType, PartnerName, DateTimeSendStart);
create INDEX search2 on document_meta_info (DocType, PartnerName, DateTimeReceiveEnd);
create INDEX search3 on document_meta_info (DocNumber);
create INDEX search4 on document_meta_info (PartnerName);
create INDEX search5 on document_meta_info (DocType, PartnerName, DocDateGenerated);
create INDEX search6 on document_meta_info (GdocID, Folder);
create INDEX search7 on document_meta_info (DocType, PartnerName, DateTimeCreate);
create INDEX search8 on document_meta_info (ProcessInstanceID, OriginatorID, processInstanceInfoUID);

#index for process_instance_meta_info
create INDEX search1 on process_instance_meta_info (docNumber);
create INDEX search2 on process_instance_meta_info (partnerID);
create INDEX search3 on process_instance_meta_info (docDateGenerated);
create INDEX search4 on process_instance_meta_info (processDef, docDateGenerated);
create INDEX search5 on process_instance_meta_info (processDef, processState, docDateGenerated);
create INDEX search6 on process_instance_meta_info (partnerName);
create INDEX search7 on process_instance_meta_info (processState);
create INDEX search8 on process_instance_meta_info (ProcessInstanceID, OriginatorID);
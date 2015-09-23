-- Tam Wei Xiang       11 April 2008    GT4.2        #19: Set the permission for accessing the view
--                                                        archive_process_view.

--- ------------------------------------------------------------------------
--- This script includes some of the CREATE queries for all tables in USERDB
--- ------------------------------------------------------------------------

SET client_encoding = 'UTF8';
SET check_function_bodies = false;
SET client_min_messages = warning;
SET search_path = userdb;
---
--- Table structure for table 'code_value'
---
DROP TABLE IF EXISTS "code_value";
CREATE TABLE "code_value" (
  "UID" BIGSERIAL NOT NULL,
  "Code" VARCHAR(80) NOT NULL,
  "Description" VARCHAR(80) ,
  "EntityType" VARCHAR(255) ,
  "FieldID" DECIMAL(3) ,
  PRIMARY KEY ("UID"),
  CONSTRAINT "CODE_VALUE_CON" UNIQUE ("Code")
);
ALTER SEQUENCE "code_value_UID_seq" OWNED BY "code_value"."UID"; -- so that while we drop the column or table, the sequence will be dropped as well
ALTER TABLE "code_value" OWNER TO userdb;

--- #### activation ####
---
--- Table structure for table 'activation_record'
---

DROP TABLE IF EXISTS "activation_record";
CREATE TABLE "activation_record" (
  "UID" BIGINT DEFAULT 0 NOT NULL,
  "CurrentType" DECIMAL(1) DEFAULT 0 NOT NULL,
  "ActDirection" DECIMAL(1) DEFAULT NULL,
  "DeactDirection" DECIMAL(1) DEFAULT NULL,
  "GridNodeID" DECIMAL(10) NOT NULL,
  "GridNodeName" VARCHAR(80),
  "DTRequested" TIMESTAMP DEFAULT NULL,
  "DTApproved" TIMESTAMP DEFAULT NULL,
  "DTAborted" TIMESTAMP DEFAULT NULL,
  "DTDeactivated" TIMESTAMP DEFAULT NULL,
  "DTDenied" TIMESTAMP DEFAULT NULL,
  "IsLatest" DECIMAL(1) DEFAULT 1 NOT NULL,
  "TransCompleted" DECIMAL(1) DEFAULT 0 NOT NULL,
  "TransFailReason" VARCHAR(255),
  "ActivationDetails" TEXT,
  PRIMARY KEY ("UID")
);
ALTER TABLE "activation_record" OWNER TO userdb;

--- #### alert ####
---
--- Table structure for table 'alert_trigger'
---
--- Changed: Extended length of DocType from 12 to 30

DROP TABLE IF EXISTS "alert_trigger";
CREATE TABLE "alert_trigger" (
  "UID" BIGINT DEFAULT 0 NOT NULL ,
  "Level" DECIMAL(2,0) DEFAULT 0 NOT NULL ,
  "AlertType" VARCHAR(35) NOT NULL ,
  "AlertUID" BIGINT DEFAULT 0 NOT NULL ,
  "DocType" VARCHAR(30)  ,
  "PartnerType" VARCHAR(3)  ,
  "PartnerGroup" VARCHAR(3)  ,
  "PartnerId" VARCHAR(20)  ,
  "Recipients" TEXT  ,
  "Enabled" DECIMAL(1) DEFAULT 1 NOT NULL ,
  "AttachDoc" DECIMAL(1) DEFAULT 0 NOT NULL ,
  "CanDelete" DECIMAL(1) DEFAULT 1 NOT NULL ,
  "Version" DECIMAL(7,5) DEFAULT 1 NOT NULL ,
  PRIMARY KEY ("UID"),
  CONSTRAINT "ALERT_TRIGGER_CON" UNIQUE ("Level","AlertType","DocType","PartnerType","PartnerGroup","PartnerId")
);
ALTER TABLE "alert_trigger" OWNER TO userdb;

--- #### backend ####
---
--- Table structure for table 'port'
---
DROP TABLE IF EXISTS "port";
CREATE TABLE "port" (
  "UID" BIGINT DEFAULT 0 NOT NULL,
  "PortName" VARCHAR(15) NOT NULL,
  "Description" VARCHAR(80) NOT NULL,
  "IsRfc" DECIMAL(1) DEFAULT 0 NOT NULL,
  "RfcUid" BIGINT ,
  "HostDir" VARCHAR(80) NOT NULL,
  "IsDiffFileName" DECIMAL(1) DEFAULT 1 NOT NULL,
  "IsOverwrite" DECIMAL(1) DEFAULT 1 NOT NULL,
  "FileName" VARCHAR(80) ,
  "IsAddFileExt" DECIMAL(1) DEFAULT 0 NOT NULL,
  "FileExtType" DECIMAL(2,0),
  "FileExtValue" VARCHAR(64) ,
  "IsExportGdoc" DECIMAL(1) DEFAULT 0 NOT NULL,
  "CanDelete" DECIMAL(1) DEFAULT 1 NOT NULL,
  "Version" DECIMAL(7,5) DEFAULT 1 NOT NULL,
  "StartNum" DECIMAL(10) ,
  "RolloverNum" DECIMAL(10) ,
  "NextNum" DECIMAL(10)  ,
  "IsPadded" DECIMAL(1) DEFAULT 1,
  "FixNumLength" DECIMAL(10) ,
  "FileGrouping" DECIMAL(1) DEFAULT 2,
  PRIMARY KEY ("UID"),
  CONSTRAINT "PORT_CON" UNIQUE ("PortName")
);
ALTER TABLE "port" OWNER TO userdb;

---
--- Table structure for table 'rfc'
---
DROP TABLE IF EXISTS "rfc";
CREATE TABLE "rfc" (
  "UID" BIGINT DEFAULT 0 NOT NULL,
  "RfcName" VARCHAR(18) NOT NULL,
  "Description" VARCHAR(80)  NOT NULL,
  "ConnectionType" VARCHAR(2) DEFAULT 'T' NOT NULL,
  "Host" VARCHAR(80)  NOT NULL,
  "PortNumber" DECIMAL(5),
  "UseCommandFile" DECIMAL(1) DEFAULT 0 NOT NULL,
  "CommandFileDir" VARCHAR(120) ,
  "CommandFile" VARCHAR(80) ,
  "CommandLine" VARCHAR(120) ,
  "CanDelete" DECIMAL(1) DEFAULT 1 NOT NULL,
  "Version" DECIMAL(7,5) DEFAULT 1 NOT NULL,
  PRIMARY KEY ("UID"),
  CONSTRAINT "RFC_CON" UNIQUE ("RfcName")
);
ALTER TABLE "rfc" OWNER TO userdb;

--- #### connection ####
---
--- Table structure for table 'jms_router'
---
DROP TABLE IF EXISTS "jms_router";
CREATE TABLE "jms_router" (
  "UID" BIGINT DEFAULT 0 NOT NULL,
  "Name" VARCHAR(50) NOT NULL,
  "IpAddress" VARCHAR(50) NOT NULL,
  PRIMARY KEY ("UID")
);
ALTER TABLE "jms_router" OWNER TO userdb;


--- #### docalert ####
---
--- Table structure for table 'response_track_record'
---
DROP TABLE IF EXISTS "response_track_record";
CREATE TABLE "response_track_record" (
  "UID" BIGINT DEFAULT 0 NOT NULL ,
  "SentDocType" VARCHAR(30)  NOT NULL ,
  "SentDocIdXpath" VARCHAR(255),
  "StartTrackDateXpath" VARCHAR(255),
  "ResponseDocType" VARCHAR(30)  NOT NULL,
  "ResponseDocIdXpath" VARCHAR(255),
  "ReceiveResponseAlert" VARCHAR(80),
  "AlertRecipientXpath" VARCHAR(255),
  "Version" DECIMAL(7,5) DEFAULT 1 NOT NULL,
  "AttachRespDoc" DECIMAL(1) DEFAULT 1 NOT NULL,
  PRIMARY KEY ("UID"),
  CONSTRAINT "RESPONSE_TRACK_CON1" UNIQUE ("SentDocType"),
  CONSTRAINT "RESPONSE_TRACK_CON2" UNIQUE ("ResponseDocType")
);
ALTER TABLE "response_track_record" OWNER TO userdb;

---
--- Table structure for table 'reminder_alert'
---
DROP TABLE IF EXISTS "reminder_alert";
CREATE TABLE "reminder_alert" (
  "UID" BIGINT DEFAULT 0 NOT NULL ,
  "TrackRecordUID" BIGINT DEFAULT 0 NOT NULL ,
  "DaysToReminder" DECIMAL(3) DEFAULT 1 NOT NULL ,
  "AlertToRaise" VARCHAR(80)  NOT NULL ,
  "DocRecipientXpath" VARCHAR(255),
  "DocSenderXpath" VARCHAR(255),
  "Version" DECIMAL(7,5) DEFAULT 1 NOT NULL,
  PRIMARY KEY ("UID"),
  CONSTRAINT "REMINDER_ALERT_CON" UNIQUE ("TrackRecordUID","DaysToReminder")
);
ALTER TABLE "reminder_alert" OWNER TO userdb;

---
--- Table structure for table 'active_track_record'
---
DROP TABLE IF EXISTS "active_track_record";
CREATE TABLE "active_track_record" (
  "UID" BIGINT DEFAULT 0 NOT NULL ,
  "TrackRecordUID" BIGINT DEFAULT 0 NOT NULL ,
  "DaysToReminder" DECIMAL(3) DEFAULT 0 NOT NULL ,
  "AlarmUID" BIGINT DEFAULT 0 NOT NULL ,
  "SentGridDocUID" BIGINT DEFAULT 0 NOT NULL ,
  PRIMARY KEY ("UID"),
  CONSTRAINT "ACTIVE_TRACK_CON1" UNIQUE ("AlarmUID"),
  CONSTRAINT "ACTIVE_TRACK_CON2" UNIQUE ("SentGridDocUID")
);
ALTER TABLE "active_track_record" OWNER TO userdb;

--- #### document ####

---
--- Table structure for table 'document_type'
---
DROP TABLE IF EXISTS "document_type";
CREATE TABLE "document_type" (
  "UID" BIGINT DEFAULT 0 NOT NULL ,
  "DocType" VARCHAR(30)  NOT NULL ,
  "Description" VARCHAR(80)  NOT NULL ,
  "CanDelete" DECIMAL(1) DEFAULT 1 NOT NULL ,
  "Version" DECIMAL(7,5) DEFAULT 1 NOT NULL,
  PRIMARY KEY ("UID"),
  CONSTRAINT "DOCUMENT_TYPE_CON" UNIQUE ("DocType")
);
ALTER TABLE "document_type" OWNER TO userdb;

---
--- Table structure for table 'file_type'
---
DROP TABLE IF EXISTS "file_type";
CREATE TABLE "file_type" (
  "UID" BIGINT DEFAULT 0 NOT NULL ,
  "FileType" VARCHAR(10)  NOT NULL ,
  "Description" VARCHAR(80)  NOT NULL ,
  "ProgramName" VARCHAR(120)  ,
  "ProgramPath" VARCHAR(120)  ,
  "Parameters" VARCHAR(120)  ,
  "WorkingDirectory" VARCHAR(120)  ,
  "CanDelete" DECIMAL(1) DEFAULT 1 NOT NULL ,
  "Version" DECIMAL(7,5) DEFAULT 1 NOT NULL,
  PRIMARY KEY ("UID"),
  CONSTRAINT "FILE_TYPE_CON" UNIQUE ("FileType")
);
ALTER TABLE "file_type" OWNER TO userdb;

---
--- Table structure for table 'grid_document'
---
DROP TABLE IF EXISTS "grid_document";
CREATE TABLE "grid_document" (
  "UID" BIGINT DEFAULT 0 NOT NULL ,
  "CanDelete" DECIMAL(1) DEFAULT 1 NOT NULL ,
  "Version" DECIMAL(7,5) DEFAULT 1 NOT NULL,
  "GdocId" BIGINT DEFAULT 0 NOT NULL,
  "RefGdocId" BIGINT,
  "GdocFilename" VARCHAR(80),
  "UdocNum" VARCHAR(80),
  "RefUdocNum" VARCHAR(80),
  "UdocFilename" VARCHAR(255),
  "RefUdocFilename" VARCHAR(255),
  "UdocVersion" DECIMAL(10) DEFAULT 1,
  "UdocDocType" VARCHAR(30),
  "UdocFileType" VARCHAR(10),
  "Exported" DECIMAL(1) DEFAULT 0 ,
  "ViewAckReq" DECIMAL(1) DEFAULT 0 ,
  "ExportAckReq" DECIMAL(1) DEFAULT 0 ,
  "ReceiveAckReq" DECIMAL(1) DEFAULT 0 ,
  "Viewed" DECIMAL(1) DEFAULT 0 ,
  "Sent" DECIMAL(1) DEFAULT 0 ,
  "LocalPending" DECIMAL(1) DEFAULT 0 ,
  "Expired" DECIMAL(1) DEFAULT 0 ,
  "RecAckProcessed" DECIMAL(1) DEFAULT 0 ,
  "EncryptionLevel" DECIMAL(10) DEFAULT 64 ,
  "Folder" VARCHAR(10),
  "CreateBy" VARCHAR(15),
  "RecipientNodeId" DECIMAL(9,0),
  "RecipientPartnerId" VARCHAR(15),
  "RecipientPartnerType" VARCHAR(3),
  "RecipientPartnerGroup" VARCHAR(3),
  "RecipientBizEntityId" VARCHAR(4),
  "RecipientBizEntityUuid" VARCHAR(50),
  "RecipientRegistryQueryUrl" VARCHAR(255),
  "RecipientPartnerFunction" VARCHAR(30),
  "RecipientGdocId" BIGINT,
  "SenderNodeId" DECIMAL(9,0),
  "SenderGdocId" BIGINT,
  "SenderUserId" VARCHAR(15),
  "SenderUserName" VARCHAR(50),
  "SenderBizEntityId" VARCHAR(4),
  "SenderBizEntityUuid" VARCHAR(50),
  "SenderRegistryQueryUrl" VARCHAR(255),
  "SenderRoute" VARCHAR(20),
  "SenderPartnerId" VARCHAR(15),
  "SenderPartnerType" VARCHAR(3),
  "SenderPartnerGroup" VARCHAR(3),
  "DateTimeImport" TIMESTAMP,
  "DateTimeSendEnd" TIMESTAMP,
  "DateTimeReceiveEnd" TIMESTAMP,
  "DateTimeExport" TIMESTAMP,
  "DateTimeCreate" TIMESTAMP,
  "DateTimeTransComplete" TIMESTAMP,
  "DateTimeReceiveStart" TIMESTAMP,
  "DateTimeView" TIMESTAMP,
  "DateTimeSendStart" TIMESTAMP,
  "DateTimeRecipientView" TIMESTAMP,
  "DateTimeRecipientExport" TIMESTAMP,
  "PortUid" BIGINT,
  "SrcFolder" VARCHAR(10),
  "NotifyUserEmail" VARCHAR(50),
  "RecipientPartnerName" VARCHAR(20),
  "SenderPartnerName" VARCHAR(20),
  "PortName" VARCHAR(15),
  "RnProfileUid" BIGINT,
  "ProcessDefId" VARCHAR(80),
  "IsRequest" DECIMAL(1) DEFAULT 0,
  "HasAttachment" DECIMAL(1) DEFAULT 0,
  "AttachmentLinkUpdated" DECIMAL(1) DEFAULT 1,
  "Attachments" VARCHAR(200),
  "TriggerType" DECIMAL(2,0),
  "UniqueDocIdentifier" VARCHAR(80),
  "UdocFullPath" VARCHAR(500)  NOT NULL,
  "ExportedUdocFullPath" VARCHAR(500),
  "Rejected" DECIMAL(1) DEFAULT 0 ,
  "Custom1" VARCHAR(255),
  "Custom2" VARCHAR(255),
  "Custom3" VARCHAR(255),
  "Custom4" VARCHAR(255),
  "Custom5" VARCHAR(255),
  "Custom6" VARCHAR(255),
  "Custom7" VARCHAR(255),
  "Custom8" VARCHAR(255),
  "Custom9" VARCHAR(255),
  "Custom10" VARCHAR(255),
  "ProcessInstanceUid" BIGINT,
  "ProcessInstanceID" VARCHAR(255) default NULL,
  "UserTrackingID" VARCHAR(255) default NULL,
  "DocTransStatus" TEXT,
  "MessageDigest" VARCHAR(80),
  "AuditFileName" VARCHAR(200),
  "ReceiptAuditFileName" VARCHAR(200),
  "DocDateGen" VARCHAR(25) ,
  "SenderCert" BIGINT ,
  "ReceiverCert" BIGINT ,
  "OriginalDoc" DECIMAL(1) DEFAULT 0,
  "TracingID" VARCHAR(36) ,
  PRIMARY KEY ("UID")
);
ALTER TABLE "grid_document" OWNER TO userdb;

---
--- Table structure for table 'attachment'
---
DROP TABLE IF EXISTS "attachment";
CREATE TABLE "attachment" (
  "UID" BIGINT DEFAULT 0 NOT NULL ,
  "CanDelete" DECIMAL(1) DEFAULT 1 NOT NULL ,
  "Version" DECIMAL(7,5) DEFAULT 1 NOT NULL,
  "PartnerId" VARCHAR(20),
  "OriginalUid" BIGINT,
  "Filename" VARCHAR(255)  NOT NULL ,
  "OriginalFilename" VARCHAR(255)  NOT NULL ,
  "IsOutgoing" DECIMAL(1) DEFAULT 1 NOT NULL,
  PRIMARY KEY ("UID")
);
ALTER TABLE "attachment" OWNER TO userdb;

---
--- Table structure for table 'attachmentRegistry'
---
DROP TABLE IF EXISTS "attachmentRegistry";
CREATE TABLE "attachmentRegistry" (
  "UID" BIGINT DEFAULT 0 NOT NULL ,
  "CanDelete" DECIMAL(1) DEFAULT 1 NOT NULL ,
  "Version" DECIMAL(7,5) DEFAULT 1 NOT NULL,
  "PartnerId" VARCHAR(20)  NOT NULL ,
  "AttachmentUid" BIGINT DEFAULT -1 NOT NULL ,
  "DateProcessed" TIMESTAMP,
  PRIMARY KEY ("UID")
);
ALTER TABLE "attachmentRegistry" OWNER TO userdb;


--- #### enterprise ####
---
--- Table structure for table 'resource_link'
---
DROP TABLE IF EXISTS "resource_link";
CREATE TABLE "resource_link" (
  "UID" BIGINT DEFAULT 0 NOT NULL ,
  "FromResourceType" VARCHAR(50)  NOT NULL ,
  "FromResourceUID" BIGINT DEFAULT 0 NOT NULL,
  "ToResourceType" VARCHAR(50)  NOT NULL ,
  "ToResourceUID" BIGINT DEFAULT 0 NOT NULL ,
  "Priority" DECIMAL(5)  DEFAULT 0 NOT NULL ,
  "CanDelete" DECIMAL(1) DEFAULT 1 NOT NULL,
  "Version" DECIMAL(7,5) DEFAULT 1 NOT NULL,
  PRIMARY KEY ("UID"),
  CONSTRAINT "RESOURCE_LINK_CON" UNIQUE ("FromResourceType","FromResourceUID","ToResourceType","ToResourceUID")
);
ALTER TABLE "resource_link" OWNER TO userdb;

---
--- Table structure for table 'shared_resource'
---
DROP TABLE IF EXISTS "shared_resource";
CREATE TABLE "shared_resource" (
  "UID" BIGINT DEFAULT 0 NOT NULL ,
  "ToEnterpriseID" VARCHAR(20)  NOT NULL ,
  "ResourceUID" BIGINT DEFAULT 0 NOT NULL ,
  "ResourceType" VARCHAR(50)  NOT NULL ,
  "State" DECIMAL(1) DEFAULT 0 NOT NULL ,
  "SyncChecksum" BIGINT ,
  "CanDelete" DECIMAL(1) DEFAULT 1 NOT NULL,
  "Version" DECIMAL(7,5) DEFAULT 1 NOT NULL,
  PRIMARY KEY ("UID"),
  CONSTRAINT "SHARED_RESOURCE_CON" UNIQUE ("ToEnterpriseID","ResourceType","ResourceUID")
);
ALTER TABLE "shared_resource" OWNER TO userdb;

--- #### gridnode ####
---
--- Table structure for table 'gridnode'
---
DROP TABLE IF EXISTS "gridnode";
CREATE TABLE "gridnode" (
  "UID" BIGINT DEFAULT 0 NOT NULL,
  "ID" VARCHAR(10)  NOT NULL,
  "Name" VARCHAR(80)  NOT NULL,
  "Category" CHAR(3)  NOT NULL,
  "State" DECIMAL(2,0) NOT NULL,
  "ActivationReason" VARCHAR(255),
  "DTCreated" TIMESTAMP ,
  "DTReqActivate" TIMESTAMP ,
  "DTActivated" TIMESTAMP ,
  "DTDeactivated" TIMESTAMP ,
  "CoyProfileUID" BIGINT  ,
  "Version" DECIMAL(7,5) DEFAULT 1 NOT NULL,
  PRIMARY KEY ("UID"),
  CONSTRAINT "GRIDNODE_CON" UNIQUE ("ID")
);
ALTER TABLE "gridnode" OWNER TO userdb;

---
--- Table structure for table 'connection_status'
---
DROP TABLE IF EXISTS "connection_status";
CREATE TABLE "connection_status" (
  "UID" BIGINT DEFAULT 0 NOT NULL,
  "NodeID" VARCHAR(10)  NOT NULL,
  "StatusFlag" DECIMAL(2,0) NOT NULL,
  "DTLastOnline" TIMESTAMP ,
  "DTLastOffline" TIMESTAMP ,
  "ReconnectionKey" BYTEA NULL,
  "ConnectedServerNode" VARCHAR(10) ,
  PRIMARY KEY ("UID"),
  CONSTRAINT "CONNECTION_STATUS_CON" UNIQUE ("NodeID")
);
ALTER TABLE "connection_status" OWNER TO userdb;

--- #### mapper ####
---
--- Table structure for table 'gridtalk_mapping_rule'
---
DROP TABLE IF EXISTS "gridtalk_mapping_rule";
CREATE TABLE "gridtalk_mapping_rule" (
  "UID" BIGINT DEFAULT 0 NOT NULL ,
  "Name" VARCHAR(30)  NOT NULL ,
  "Description" VARCHAR(80)  NOT NULL DEFAULT '',
  "SourceDocType" VARCHAR(30) NOT NULL DEFAULT '',  
  "TargetDocType" VARCHAR(30)  NOT NULL DEFAULT '',  
  "SourceDocFileType" VARCHAR(30) NOT NULL DEFAULT '',  
  "TargetDocFileType" VARCHAR(30)  NOT NULL DEFAULT '', 
  "HeaderTransformation" DECIMAL(1) DEFAULT 0 NOT NULL ,
  "TransformWithHeader" DECIMAL(1) DEFAULT 0 NOT NULL ,
  "TransformWithSource" DECIMAL(1) DEFAULT 0 NOT NULL ,
  "MappingRuleUID" BIGINT DEFAULT 0 NOT NULL ,
  "CanDelete" DECIMAL(1) DEFAULT 1 NOT NULL,
  "Version" DECIMAL(7,5) DEFAULT 1 NOT NULL,
  PRIMARY KEY ("UID"),
  CONSTRAINT "GRIDTALK_MAPPING_RULE_CON" UNIQUE ("Name")
);
ALTER TABLE "gridtalk_mapping_rule" OWNER TO userdb;

--- #### partnerfunstion ####
---
--- Table structure for table 'partner_function'
---
DROP TABLE IF EXISTS "partner_function";
CREATE TABLE "partner_function" (
  "UID" BIGINT DEFAULT 0 NOT NULL ,
  "PartnerFunctionId" VARCHAR(30)  NOT NULL ,
  "Description" VARCHAR(80)  NOT NULL ,
  "TriggerOn" DECIMAL(2,0) DEFAULT 0 NOT NULL ,
  "WorkflowActivityUids" VARCHAR(300) ,
  "CanDelete" DECIMAL(1) DEFAULT 1 NOT NULL,
  "Version" DECIMAL(7,5) DEFAULT 1 NOT NULL,
  PRIMARY KEY ("UID"),
  CONSTRAINT "PARTNER_FUNCTION_CON" UNIQUE ("PartnerFunctionId")
);
ALTER TABLE "partner_function" OWNER TO userdb;

---
--- Table structure for table 'workflow_activity'
---
DROP TABLE IF EXISTS "workflow_activity";
CREATE TABLE "workflow_activity" (
  "UID" BIGINT DEFAULT 0 NOT NULL ,
  "ActivityType" DECIMAL(2,0) NOT NULL ,
  "Description" VARCHAR(80)  NOT NULL ,
  "ParamList" VARCHAR(200) ,
  "CanDelete" DECIMAL(1) DEFAULT 1 NOT NULL,
  "Version" DECIMAL(7,5) DEFAULT 1 NOT NULL,
  PRIMARY KEY ("UID")
);
ALTER TABLE "workflow_activity" OWNER TO userdb;

--- #### partnerprocess ####
---
--- Table structure for table 'pf_trigger'
---
DROP TABLE IF EXISTS "pf_trigger";
CREATE TABLE "pf_trigger" (
  "UID" BIGINT DEFAULT 0 NOT NULL ,
  "TriggerLevel" DECIMAL(2,0) DEFAULT 0 NOT NULL ,
  "PartnerFunctionId" VARCHAR(30) NOT NULL  ,
  "ProcessId" VARCHAR(80)  ,
  "DocType" VARCHAR(30)  ,
  "PartnerType" VARCHAR(3)  ,
  "PartnerGroup" VARCHAR(3)  ,
  "PartnerId" VARCHAR(20)  ,
  "TriggerType" DECIMAL(2,0) DEFAULT 0 NOT NULL ,
  "IsRequest" DECIMAL(1) DEFAULT 1 NOT NULL,
  "CanDelete" DECIMAL(1) DEFAULT 1 NOT NULL,
  "Version" DECIMAL(7,5) DEFAULT 1 NOT NULL,
  "IsLocalPending" DECIMAL(1) DEFAULT 1 NOT NULL,
  "NumOfRetries" DECIMAL(5) DEFAULT 0,
  "RetryInterval" DECIMAL(10) DEFAULT 0,
  "ChannelUID" BIGINT DEFAULT -99999999,
  PRIMARY KEY ("UID"),
  CONSTRAINT "PF_TRIGGER_CON" UNIQUE ("TriggerLevel","TriggerType","DocType","PartnerType","PartnerGroup","PartnerId")
);
ALTER TABLE "pf_trigger" OWNER TO userdb;

---
--- Table structure for table 'process_mapping'
---
--- Changed: Extended length of DocType from 12 to 30
DROP TABLE IF EXISTS "process_mapping";
CREATE TABLE "process_mapping" (
  "UID" BIGINT DEFAULT 0 NOT NULL ,
  "ProcessDef" VARCHAR(80) NOT NULL  ,
  "PartnerID" VARCHAR(20) NOT NULL  ,
  "IsInitiatingRole" DECIMAL(1) DEFAULT 1 NOT NULL,
  "DocType" VARCHAR(30) ,
  "SendChannelUID" BIGINT ,
  "ProcessIndicatorCode" VARCHAR(80),
  "ProcessVersionID" VARCHAR(80),
  "PartnerRoleMapping" BIGINT ,
  "CanDelete" DECIMAL(1) DEFAULT 1 NOT NULL,
  "Version" DECIMAL(7,5) DEFAULT 1 NOT NULL,
  PRIMARY KEY ("UID"),
  CONSTRAINT "PROCESS_MAPPING_CON" UNIQUE ("ProcessDef","PartnerID","IsInitiatingRole")
);
ALTER TABLE "process_mapping" OWNER TO userdb;

---
--- Table structure for table 'biz_cert_mapping'
---
DROP TABLE IF EXISTS "biz_cert_mapping";
CREATE TABLE "biz_cert_mapping" (
  "UID" BIGINT DEFAULT 0 NOT NULL ,
  "PartnerID" VARCHAR(20)  NOT NULL ,
  "PartnerCertUID" BIGINT DEFAULT 0 NOT NULL,
  "OwnCertUID" BIGINT DEFAULT 0 NOT NULL,
  "CanDelete" DECIMAL(1) DEFAULT 1 NOT NULL,
  "Version" DECIMAL(7,5) DEFAULT 1 NOT NULL,
  PRIMARY KEY ("UID"),
  CONSTRAINT "BIZ_CERT_MAPPING_CON" UNIQUE ("PartnerID")
);
ALTER TABLE "biz_cert_mapping" OWNER TO userdb;

--- #### registration ####
---
--- Table structure for table 'gridtalk_license'
---
DROP TABLE IF EXISTS "gridtalk_license";
CREATE TABLE "gridtalk_license" (
  "UID" BIGINT DEFAULT 0 NOT NULL,
  "LicenseUid" BIGINT NOT NULL,
  "OsName" VARCHAR(100)  NOT NULL,
  "OsVersion" VARCHAR(30)  NOT NULL,
  "MachineName" VARCHAR(200)  NOT NULL,
  "StartDate" VARCHAR(80)  NOT NULL,
  "EndDate" VARCHAR(80)  NOT NULL,
  "CanDelete" DECIMAL(1) DEFAULT 1 NOT NULL,
  "Version" DECIMAL(7,5) DEFAULT 1 NOT NULL,
  PRIMARY KEY ("UID"),
  CONSTRAINT "GRIDTALK_LICENSE_CON" UNIQUE ("LicenseUid")
);
ALTER TABLE "gridtalk_license" OWNER TO userdb;

--- #### rnif ####
DROP TABLE IF EXISTS "rn_profile";
CREATE TABLE "rn_profile" ( 
  "UID" BIGINT DEFAULT 0 NOT NULL,
  "DocumentUid" BIGINT,
  "ProcessInstanceId" VARCHAR(255) ,
  "ProcessOriginatorId" VARCHAR(80) ,
  "ProcessResponderId" VARCHAR(80) ,
  "ProcessDefName" VARCHAR(80) ,
  "ReceiverDomain" VARCHAR(80) ,
  "ReceiverGlobalBusIdentifier" VARCHAR(80) ,
  "ReceiverLocationId" VARCHAR(80) ,
  "SenderDomain" VARCHAR(80) ,
  "SenderGlobalBusIdentifier" VARCHAR(80) ,
  "SenderLocationId" VARCHAR(80) ,
  "DeliveryMessageTrackingId" VARCHAR(80) ,
  "BusActivityIdentifier" VARCHAR(80) ,
  "FromGlobalPartnerRoleClassCode" VARCHAR(80) ,
  "FromGlobalBusServiceCode" VARCHAR(80) ,
  "InReplyToGlobalBusActionCode" VARCHAR(80) ,
  "InReplyToMessageStandard" VARCHAR(80) ,
  "InReplyToStandardVersion" VARCHAR(80) ,
  "InReplyToVersionIdentifier" VARCHAR(80) ,
  "ServiceMessageTrackingId" VARCHAR(80) ,
  "ActionIdentityGlobalBusActionCode" VARCHAR(80) , -- Original:ActionIdentityGlobalBusActionCode
  "ActionIdentityToMessageStandard" VARCHAR(80) , -- Original:ActionIdentityToMessageStandard
  "ActionIdentityStandardVersion" VARCHAR(80) , -- Original:ActionIdentityStandardVersion
  "ActionIdentityVersionIdentifier" VARCHAR(80) , -- Original:ActionIdentityVersionIdentifier
  "SignalIdentityGlobalBusSignalCode" VARCHAR(80) , -- Original:SignalIdentityGlobalBusSignalCode
  "SignalIdentityVersionIdentifier" VARCHAR(80) , -- Original:SignalIdentityVersionIdentifier
  "ToGlobalPartnerRoleClassCode" VARCHAR(80) ,
  "ToGlobalBusServiceCode" VARCHAR(80) ,
  "GlobalUsageCode" VARCHAR(80) ,
  "PartnerGlobalBusIdentifier" VARCHAR(80) ,
  "PIPGlobalProcessCode" VARCHAR(80) ,
  "PIPInstanceIdentifier" VARCHAR(200) ,
  "PIPVersionIdentifier" VARCHAR(80) ,
  "ProcessTransactionId" VARCHAR(200) ,
  "ProcessActionId" VARCHAR(240) ,
  "FromGlobalPartnerClassCode" VARCHAR(80) ,
  "ToGlobalPartnerClassCode" VARCHAR(80) ,
  "NumberOfAttas" DECIMAL(10) ,
  "IsSignalDoc" DECIMAL(1) ,
  "IsRequestMsg" DECIMAL(1) ,	
  "UniqueValue" VARCHAR(80) ,	
  "AttemptCount" DECIMAL(10) ,		
  "MsgDigest" VARCHAR(80) ,	
  "RNIFVersion" VARCHAR(80) ,
  "InResponseToActionID" VARCHAR(80) ,
  "UserTrackingID" VARCHAR(255) ,
  PRIMARY KEY ("UID")
);
ALTER TABLE "rn_profile" OWNER TO userdb;

--COMMENT ON COLUMN "rn_profile"."ActionIdGlobalBusActCode" IS 'Column name has been shortened. Original:ActionIdentityGlobalBusActionCode';
--COMMENT ON COLUMN "rn_profile"."ActionIdToMsgStandard" IS 'Column name has been shortened. Original:ActionIdentityToMessageStandard';
--COMMENT ON COLUMN "rn_profile"."ActionIdStandardVersion" IS 'Column name has been shortened. Original:ActionIdentityStandardVersion';
--COMMENT ON COLUMN "rn_profile"."ActionIdVersionIdentifier" IS 'Column name has been shortened. Original:ActionIdentityVersionIdentifier';
--COMMENT ON COLUMN "rn_profile"."SignalIdGlobalBusSignalCode" IS 'Column name has been shortened. Original:SignalIdentityGlobalBusSignalCode';
--COMMENT ON COLUMN "rn_profile"."SignalIdVersionIdentifier" IS 'Column name has been shortened. Original:SignalIdentityVersionIdentifier';

-- view for archival
CREATE OR REPLACE VIEW "archive_process_view" ("RTProcessUID","EngineType","ProcessType","StartTime","EndTime","ProcessUId","RTProcessDocUID","CustomerBEId","PartnerKey","ProcessStatus") AS SELECT "rtprocess"."UID" AS "RTProcessUID", "rtprocess"."EngineType" AS "EngineType", "rtprocess"."ProcessType" AS "ProcessType","rtprocess"."StartTime" AS "StartTime", "rtprocess"."EndTime" AS "EndTime", "rtprocess"."ProcessUId" AS "ProcessUId","rtprocessdoc"."UID" AS "RTProcessDocUID", "rtprocessdoc"."CustomerBEId" AS "CustomerBEId", "rtprocessdoc"."PartnerKey" AS "PartnerKey", "rtprocessdoc"."Status" AS "ProcessStatus" FROM "rtprocess" JOIN "rtprocessdoc" ON "rtprocess"."UID" = "rtprocessdoc"."RtBinaryCollaborationUId"; 
ALTER table "archive_process_view" OWNER to userdb;

COMMIT;
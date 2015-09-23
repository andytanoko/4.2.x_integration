--- ------------------------------------------------------------------------
--- This script includes some of the CREATE queries for all tables in USERDB
--- ------------------------------------------------------------------------

CONNECT USERDB/gridnode;

ALTER SESSION SET NLS_DATE_FORMAT = 'YYYY-MM-DD HH24:MI:SS';
---
--- Table structure for table 'code_value'
---

CREATE TABLE "code_value" (
  "UID" NUMBER(19) NOT NULL ENABLE,
  "Code" VARCHAR2(80) NOT NULL ENABLE,
  "Description" VARCHAR2(80) ,
  "EntityType" VARCHAR2(255) ,
  "FieldID" NUMBER(3) ,
  PRIMARY KEY ("UID"),
  CONSTRAINT "CODE_VALUE_CON" UNIQUE ("Code") ENABLE
);

CREATE SEQUENCE CODE_VALUE_SEQ 
START WITH 1 
INCREMENT BY 1 
NOMAXVALUE; 

CREATE TRIGGER CODE_VALUE_TRIGGER 
BEFORE INSERT ON "code_value"
REFERENCING NEW AS NEW OLD AS OLD 
FOR EACH ROW
BEGIN
     SELECT CODE_VALUE_SEQ.NEXTVAL INTO :NEW."UID" FROM DUAL;
END;
/

--- #### activation ####
---
--- Table structure for table 'activation_record'
---

CREATE TABLE "activation_record" (
  "UID" NUMBER(19) DEFAULT 0 NOT NULL ENABLE,
  "CurrentType" NUMBER(1) DEFAULT 0 NOT NULL ENABLE,
  "ActDirection" NUMBER(1) DEFAULT NULL,
  "DeactDirection" NUMBER(1) DEFAULT NULL,
  "GridNodeID" NUMBER(10) NOT NULL,
  "GridNodeName" VARCHAR2(80),
  "DTRequested" TIMESTAMP(3) ,
  "DTApproved" TIMESTAMP(3) ,
  "DTAborted" TIMESTAMP(3) ,
  "DTDeactivated" TIMESTAMP(3) ,
  "DTDenied" TIMESTAMP(3)  ,
  "IsLatest" NUMBER(1) DEFAULT 1 NOT NULL ENABLE,
  "TransCompleted" NUMBER(1) DEFAULT 0 NOT NULL ENABLE,
  "TransFailReason" VARCHAR2(255),
  "ActivationDetails" VARCHAR2(4000),
  PRIMARY KEY ("UID")
);


--- #### alert ####
---
--- Table structure for table 'alert_trigger'
---
--- Changed: Extended length of DocType from 12 to 30

CREATE TABLE "alert_trigger" (
  "UID" NUMBER(19) DEFAULT 0 NOT NULL ENABLE ,
  "Level" NUMBER(2,0) DEFAULT 0 NOT NULL ENABLE ,
  "AlertType" VARCHAR2(35) NOT NULL ENABLE ,
  "AlertUID" NUMBER(19) DEFAULT 0 NOT NULL ENABLE ,
  "DocType" VARCHAR2(30)  ,
  "PartnerType" VARCHAR2(3)  ,
  "PartnerGroup" VARCHAR2(3)  ,
  "PartnerId" VARCHAR2(20)  ,
  "Recipients" VARCHAR2(2000)  ,
  "Enabled" NUMBER(1) DEFAULT 1 NOT NULL ENABLE ,
  "AttachDoc" NUMBER(1) DEFAULT 0 NOT NULL ENABLE ,
  "CanDelete" NUMBER(1) DEFAULT 1 NOT NULL ENABLE ,
  "Version" NUMBER(7,5) DEFAULT 1 NOT NULL ENABLE ,
  PRIMARY KEY ("UID"),
  CONSTRAINT "ALERT_TRIGGER_CON" UNIQUE ("Level","AlertType","DocType","PartnerType","PartnerGroup","PartnerId") ENABLE
);


--- #### backend ####
---
--- Table structure for table 'port'
---

CREATE TABLE "port" (
  "UID" NUMBER(19) DEFAULT 0 NOT NULL ENABLE,
  "PortName" VARCHAR2(15) NOT NULL ENABLE,
  "Description" VARCHAR2(80) NOT NULL ENABLE,
  "IsRfc" NUMBER(1) DEFAULT 0 NOT NULL ENABLE,
  "RfcUid" NUMBER(19) ,
  "HostDir" VARCHAR2(80) NOT NULL ENABLE,
  "IsDiffFileName" NUMBER(1) DEFAULT 1 NOT NULL ENABLE,
  "IsOverwrite" NUMBER(1) DEFAULT 1 NOT NULL ENABLE,
  "FileName" VARCHAR2(80) ,
  "IsAddFileExt" NUMBER(1) DEFAULT 0 NOT NULL ENABLE,
  "FileExtType" NUMBER(2,0),
  "FileExtValue" VARCHAR2(64) ,
  "IsExportGdoc" NUMBER(1) DEFAULT 0 NOT NULL ENABLE,
  "CanDelete" NUMBER(1) DEFAULT 1 NOT NULL ENABLE,
  "Version" NUMBER(7,5) DEFAULT 1 NOT NULL ENABLE,
  "StartNum" NUMBER(10) ,
  "RolloverNum" NUMBER(10) ,
  "NextNum" NUMBER(10)  ,
  "IsPadded" NUMBER(1) DEFAULT 1,
  "FixNumLength" NUMBER(10) ,
  "FileGrouping" NUMBER(1) DEFAULT 2,
  PRIMARY KEY ("UID"),
  CONSTRAINT "PORT_CON" UNIQUE ("PortName") ENABLE
);


---
--- Table structure for table 'rfc'
---

CREATE TABLE "rfc" (
  "UID" NUMBER(19) DEFAULT 0 NOT NULL ENABLE,
  "RfcName" VARCHAR2(18) NOT NULL ENABLE,
  "Description" VARCHAR2(80)  NOT NULL ENABLE,
  "ConnectionType" VARCHAR2(2) DEFAULT 'T' NOT NULL ENABLE,
  "Host" VARCHAR2(80)  NOT NULL ENABLE,
  "PortNumber" NUMBER(5),
  "UseCommandFile" NUMBER(1) DEFAULT 0 NOT NULL ENABLE,
  "CommandFileDir" VARCHAR2(120) ,
  "CommandFile" VARCHAR2(80) ,
  "CommandLine" VARCHAR2(120) ,
  "CanDelete" NUMBER(1) DEFAULT 1 NOT NULL ENABLE,
  "Version" NUMBER(7,5) DEFAULT 1 NOT NULL ENABLE,
  PRIMARY KEY ("UID"),
  CONSTRAINT "RFC_CON" UNIQUE ("RfcName") ENABLE
);


--- #### connection ####
---
--- Table structure for table 'jms_router'
---

CREATE TABLE "jms_router" (
  "UID" NUMBER(19) DEFAULT 0 NOT NULL ENABLE,
  "Name" VARCHAR2(50) NOT NULL,
  "IpAddress" VARCHAR2(50) NOT NULL,
  PRIMARY KEY ("UID")
);



--- #### docalert ####
---
--- Table structure for table 'response_track_record'
---

CREATE TABLE "response_track_record" (
  "UID" NUMBER(19) DEFAULT 0 NOT NULL ENABLE ,
  "SentDocType" VARCHAR2(30)  NOT NULL ENABLE ,
  "SentDocIdXpath" VARCHAR2(255),
  "StartTrackDateXpath" VARCHAR2(255),
  "ResponseDocType" VARCHAR2(30)  NOT NULL ENABLE,
  "ResponseDocIdXpath" VARCHAR2(255),
  "ReceiveResponseAlert" VARCHAR2(80),
  "AlertRecipientXpath" VARCHAR2(255),
  "Version" NUMBER(7,5) DEFAULT 1 NOT NULL ENABLE,
  "AttachRespDoc" NUMBER(1) DEFAULT 1 NOT NULL ENABLE,
  PRIMARY KEY ("UID"),
  CONSTRAINT "RESPONSE_TRACK_CON1" UNIQUE ("SentDocType") ENABLE,
  CONSTRAINT "RESPONSE_TRACK_CON2" UNIQUE ("ResponseDocType") ENABLE
);


---
--- Table structure for table 'reminder_alert'
---

CREATE TABLE "reminder_alert" (
  "UID" NUMBER(19) DEFAULT 0 NOT NULL ENABLE ,
  "TrackRecordUID" NUMBER(19) DEFAULT 0 NOT NULL ENABLE ,
  "DaysToReminder" NUMBER(3) DEFAULT 1 NOT NULL ENABLE ,
  "AlertToRaise" VARCHAR2(80)  NOT NULL ENABLE ,
  "DocRecipientXpath" VARCHAR2(255),
  "DocSenderXpath" VARCHAR2(255),
  "Version" NUMBER(7,5) DEFAULT 1 NOT NULL ENABLE,
  PRIMARY KEY ("UID"),
  CONSTRAINT "REMINDER_ALERT_CON" UNIQUE ("TrackRecordUID","DaysToReminder") ENABLE
);


---
--- Table structure for table 'active_track_record'
---

CREATE TABLE "active_track_record" (
  "UID" NUMBER(19) DEFAULT 0 NOT NULL ENABLE ,
  "TrackRecordUID" NUMBER(19) DEFAULT 0 NOT NULL ENABLE ,
  "DaysToReminder" NUMBER(3) DEFAULT 0 NOT NULL ENABLE ,
  "AlarmUID" NUMBER(19) DEFAULT 0 NOT NULL ENABLE ,
  "SentGridDocUID" NUMBER(19) DEFAULT 0 NOT NULL ENABLE ,
  PRIMARY KEY ("UID"),
  CONSTRAINT "ACTIVE_TRACK_CON1" UNIQUE ("AlarmUID") ENABLE,
  CONSTRAINT "ACTIVE_TRACK_CON2" UNIQUE ("SentGridDocUID") ENABLE
);


--- #### document ####

---
--- Table structure for table 'document_type'
---

CREATE TABLE "document_type" (
  "UID" NUMBER(19) DEFAULT 0 NOT NULL ENABLE ,
  "DocType" VARCHAR2(30)  NOT NULL ENABLE ,
  "Description" VARCHAR2(80)  NOT NULL ENABLE ,
  "CanDelete" NUMBER(1) DEFAULT 1 NOT NULL ENABLE ,
  "Version" NUMBER(7,5) DEFAULT 1 NOT NULL ENABLE,
  PRIMARY KEY ("UID"),
  CONSTRAINT "DOCUMENT_TYPE_CON" UNIQUE ("DocType") ENABLE
);


---
--- Table structure for table 'file_type'
---

CREATE TABLE "file_type" (
  "UID" NUMBER(19) DEFAULT 0 NOT NULL ENABLE ,
  "FileType" VARCHAR2(10)  NOT NULL ENABLE ,
  "Description" VARCHAR2(80)  NOT NULL ENABLE ,
  "ProgramName" VARCHAR2(120)  ,
  "ProgramPath" VARCHAR2(120)  ,
  "Parameters" VARCHAR2(120)  ,
  "WorkingDirectory" VARCHAR2(120)  ,
  "CanDelete" NUMBER(1) DEFAULT 1 NOT NULL ENABLE ,
  "Version" NUMBER(7,5) DEFAULT 1 NOT NULL ENABLE,
  PRIMARY KEY ("UID"),
  CONSTRAINT "FILE_TYPE_CON" UNIQUE ("FileType") ENABLE
);


---
--- Table structure for table 'grid_document'
---

CREATE TABLE "grid_document" (
  "UID" NUMBER(19) DEFAULT 0 NOT NULL ENABLE ,
  "CanDelete" NUMBER(1) DEFAULT 1 NOT NULL ENABLE ,
  "Version" NUMBER(7,5) DEFAULT 1 NOT NULL ENABLE,
  "GdocId" NUMBER(19) DEFAULT 0 NOT NULL ENABLE,
  "RefGdocId" NUMBER(19),
  "GdocFilename" VARCHAR2(80),
  "UdocNum" VARCHAR2(80),
  "RefUdocNum" VARCHAR2(80),
  "UdocFilename" VARCHAR2(255),
  "RefUdocFilename" VARCHAR2(255),
  "UdocVersion" NUMBER(10) DEFAULT 1,
  "UdocDocType" VARCHAR2(30),
  "UdocFileType" VARCHAR2(10),
  "Exported" NUMBER(1) DEFAULT 0 ,
  "ViewAckReq" NUMBER(1) DEFAULT 0 ,
  "ExportAckReq" NUMBER(1) DEFAULT 0 ,
  "ReceiveAckReq" NUMBER(1) DEFAULT 0 ,
  "Viewed" NUMBER(1) DEFAULT 0 ,
  "Sent" NUMBER(1) DEFAULT 0 ,
  "LocalPending" NUMBER(1) DEFAULT 0 ,
  "Expired" NUMBER(1) DEFAULT 0 ,
  "RecAckProcessed" NUMBER(1) DEFAULT 0 ,
  "EncryptionLevel" NUMBER(10) DEFAULT 64 ,
  "Folder" VARCHAR2(10),
  "CreateBy" VARCHAR2(15),
  "RecipientNodeId" NUMBER(9,0),
  "RecipientPartnerId" VARCHAR2(15),
  "RecipientPartnerType" VARCHAR2(3),
  "RecipientPartnerGroup" VARCHAR2(3),
  "RecipientBizEntityId" VARCHAR2(4),
  "RecipientBizEntityUuid" VARCHAR2(50),
  "RecipientRegistryQueryUrl" VARCHAR2(255),
  "RecipientPartnerFunction" VARCHAR2(30),
  "RecipientGdocId" NUMBER(19),
  "SenderNodeId" NUMBER(9,0),
  "SenderGdocId" NUMBER(19),
  "SenderUserId" VARCHAR2(15),
  "SenderUserName" VARCHAR2(50),
  "SenderBizEntityId" VARCHAR2(4),
  "SenderBizEntityUuid" VARCHAR2(50),
  "SenderRegistryQueryUrl" VARCHAR2(255),
  "SenderRoute" VARCHAR2(20),
  "SenderPartnerId" VARCHAR2(15),
  "SenderPartnerType" VARCHAR2(3),
  "SenderPartnerGroup" VARCHAR2(3),
  "DateTimeImport" DATE,
  "DateTimeSendEnd" DATE,
  "DateTimeReceiveEnd" DATE,
  "DateTimeExport" DATE,
  "DateTimeCreate" DATE,
  "DateTimeTransComplete" DATE,
  "DateTimeReceiveStart" DATE,
  "DateTimeView" DATE,
  "DateTimeSendStart" DATE,
  "DateTimeRecipientView" DATE,
  "DateTimeRecipientExport" DATE,
  "PortUid" NUMBER(19),
  "SrcFolder" VARCHAR2(10),
  "NotifyUserEmail" VARCHAR2(50),
  "RecipientPartnerName" VARCHAR2(20),
  "SenderPartnerName" VARCHAR2(20),
  "PortName" VARCHAR2(15),
  "RnProfileUid" NUMBER(19),
  "ProcessDefId" VARCHAR2(80),
  "IsRequest" NUMBER(1) DEFAULT 0,
  "HasAttachment" NUMBER(1) DEFAULT 0,
  "AttachmentLinkUpdated" NUMBER(1) DEFAULT 1,
  "Attachments" VARCHAR2(200),
  "TriggerType" NUMBER(2,0),
  "UniqueDocIdentifier" VARCHAR2(80),
  "UdocFullPath" VARCHAR2(500)  NOT NULL ENABLE,
  "ExportedUdocFullPath" VARCHAR2(500),
  "Rejected" NUMBER(1) DEFAULT 0 ,
  "Custom1" VARCHAR2(255),
  "Custom2" VARCHAR2(255),
  "Custom3" VARCHAR2(255),
  "Custom4" VARCHAR2(255),
  "Custom5" VARCHAR2(255),
  "Custom6" VARCHAR2(255),
  "Custom7" VARCHAR2(255),
  "Custom8" VARCHAR2(255),
  "Custom9" VARCHAR2(255),
  "Custom10" VARCHAR2(255),
  "ProcessInstanceUid" NUMBER(19),
  "ProcessInstanceID" VARCHAR2(255) default NULL,
  "UserTrackingID" VARCHAR2(255) default NULL,
  "DocTransStatus" CLOB,
  "MessageDigest" VARCHAR2(80),
  "AuditFileName" VARCHAR2(200),
  "ReceiptAuditFileName" VARCHAR2(200),
  "DocDateGen" VARCHAR2(25) ,
  "SenderCert" NUMBER(19) ,
  "ReceiverCert" NUMBER(19) ,
  "OriginalDoc" NUMBER(1) DEFAULT 0,
  "TracingID" VARCHAR2(36) ,
  PRIMARY KEY ("UID")
);


---
--- Table structure for table 'attachment'
---

CREATE TABLE "attachment" (
  "UID" NUMBER(19) DEFAULT 0 NOT NULL ENABLE ,
  "CanDelete" NUMBER(1) DEFAULT 1 NOT NULL ENABLE ,
  "Version" NUMBER(7,5) DEFAULT 1 NOT NULL ENABLE,
  "PartnerId" VARCHAR2(20),
  "OriginalUid" NUMBER(19),
  "Filename" VARCHAR2(255)  NOT NULL ENABLE ,
  "OriginalFilename" VARCHAR2(255)  NOT NULL ENABLE ,
  "IsOutgoing" NUMBER(1) DEFAULT 1 NOT NULL ENABLE,
  PRIMARY KEY ("UID")
);


---
--- Table structure for table 'attachmentRegistry'
---

CREATE TABLE "attachmentRegistry" (
  "UID" NUMBER(19) DEFAULT 0 NOT NULL ENABLE ,
  "CanDelete" NUMBER(1) DEFAULT 1 NOT NULL ENABLE ,
  "Version" NUMBER(7,5) DEFAULT 1 NOT NULL ENABLE,
  "PartnerId" VARCHAR2(20)  NOT NULL ENABLE ,
  "AttachmentUid" NUMBER(19) DEFAULT -1 NOT NULL ENABLE ,
  "DateProcessed" DATE,
  PRIMARY KEY ("UID")
);



--- #### enterprise ####
---
--- Table structure for table 'resource_link'
---

CREATE TABLE "resource_link" (
  "UID" NUMBER(19) DEFAULT 0 NOT NULL ENABLE ,
  "FromResourceType" VARCHAR2(50)  NOT NULL ENABLE ,
  "FromResourceUID" NUMBER(19) DEFAULT 0 NOT NULL ENABLE,
  "ToResourceType" VARCHAR2(50)  NOT NULL ENABLE ,
  "ToResourceUID" NUMBER(19) DEFAULT 0 NOT NULL ENABLE ,
  "Priority" NUMBER(5)  DEFAULT 0 NOT NULL ENABLE ,
  "CanDelete" NUMBER(1) DEFAULT 1 NOT NULL ENABLE,
  "Version" NUMBER(7,5) DEFAULT 1 NOT NULL ENABLE,
  PRIMARY KEY ("UID"),
  CONSTRAINT "RESOURCE_LINK_CON" UNIQUE ("FromResourceType","FromResourceUID","ToResourceType","ToResourceUID") ENABLE
);


---
--- Table structure for table 'shared_resource'
---

CREATE TABLE "shared_resource" (
  "UID" NUMBER(19) DEFAULT 0 NOT NULL ENABLE ,
  "ToEnterpriseID" VARCHAR2(20)  NOT NULL ENABLE ,
  "ResourceUID" NUMBER(19) DEFAULT 0 NOT NULL ENABLE ,
  "ResourceType" VARCHAR2(50)  NOT NULL ENABLE ,
  "State" NUMBER(1) DEFAULT 0 NOT NULL ENABLE ,
  "SyncChecksum" NUMBER(19) ,
  "CanDelete" NUMBER(1) DEFAULT 1 NOT NULL ENABLE,
  "Version" NUMBER(7,5) DEFAULT 1 NOT NULL ENABLE,
  PRIMARY KEY ("UID"),
  CONSTRAINT "SHARED_RESOURCE_CON" UNIQUE ("ToEnterpriseID","ResourceType","ResourceUID") ENABLE
);


--- #### gridnode ####
---
--- Table structure for table 'gridnode'
---

CREATE TABLE "gridnode" (
  "UID" NUMBER(19) DEFAULT 0 NOT NULL ENABLE,
  "ID" VARCHAR2(10)  NOT NULL ENABLE,
  "Name" VARCHAR2(80)  NOT NULL ENABLE,
  "Category" CHAR(3)  NOT NULL ENABLE,
  "State" NUMBER(2,0) NOT NULL,
  "ActivationReason" VARCHAR2(255),
  "DTCreated" TIMESTAMP(3) ,
  "DTReqActivate" TIMESTAMP(3) ,
  "DTActivated" TIMESTAMP(3) ,
  "DTDeactivated" TIMESTAMP(3) ,
  "CoyProfileUID" NUMBER(19)  ,
  "Version" NUMBER(7,5) DEFAULT 1 NOT NULL ENABLE,
  PRIMARY KEY ("UID"),
  CONSTRAINT "GRIDNODE_CON" UNIQUE ("ID") ENABLE
);


---
--- Table structure for table 'connection_status'
---

CREATE TABLE "connection_status" (
  "UID" NUMBER(19) DEFAULT 0 NOT NULL ENABLE,
  "NodeID" VARCHAR2(10)  NOT NULL ENABLE,
  "StatusFlag" NUMBER(2,0) NOT NULL,
  "DTLastOnline" TIMESTAMP(3) ,
  "DTLastOffline" TIMESTAMP(3) ,
  "ReconnectionKey" BLOB NULL,
  "ConnectedServerNode" VARCHAR2(10) ,
  PRIMARY KEY ("UID"),
  CONSTRAINT "CONNECTION_STATUS_CON" UNIQUE ("NodeID") ENABLE
);


--- #### mapper ####
---
--- Table structure for table 'gridtalk_mapping_rule'
---

CREATE TABLE "gridtalk_mapping_rule" (
  "UID" NUMBER(19) DEFAULT 0 NOT NULL ENABLE ,
  "Name" VARCHAR2(30)  NOT NULL ENABLE ,
  "Description" VARCHAR2(80)  NOT NULL ENABLE ,
  "SourceDocType" VARCHAR2(30) ,  -- Removed NOT NULL constraints due to Oracle does not support "empty string"
  "TargetDocType" VARCHAR2(30)  ,  -- Removed NOT NULL constraints due to Oracle does not support "empty string"
  "SourceDocFileType" VARCHAR2(30) ,  -- Removed NOT NULL constraints due to Oracle does not support "empty string"
  "TargetDocFileType" VARCHAR2(30)  ,  -- Removed NOT NULL constraints due to Oracle does not support "empty string"
  "HeaderTransformation" NUMBER(1) DEFAULT 0 NOT NULL ENABLE ,
  "TransformWithHeader" NUMBER(1) DEFAULT 0 NOT NULL ENABLE ,
  "TransformWithSource" NUMBER(1) DEFAULT 0 NOT NULL ENABLE ,
  "MappingRuleUID" NUMBER(19) DEFAULT 0 NOT NULL ENABLE ,
  "CanDelete" NUMBER(1) DEFAULT 1 NOT NULL ENABLE,
  "Version" NUMBER(7,5) DEFAULT 1 NOT NULL ENABLE,
  PRIMARY KEY ("UID"),
  CONSTRAINT "GRIDTALK_MAPPING_RULE_CON" UNIQUE ("Name") ENABLE
);


--- #### partnerfunstion ####
---
--- Table structure for table 'partner_function'
---

CREATE TABLE "partner_function" (
  "UID" NUMBER(19) DEFAULT 0 NOT NULL ENABLE ,
  "PartnerFunctionId" VARCHAR2(30)  NOT NULL ENABLE ,
  "Description" VARCHAR2(80)  NOT NULL ENABLE ,
  "TriggerOn" NUMBER(2,0) DEFAULT 0 NOT NULL ENABLE ,
  "WorkflowActivityUids" VARCHAR2(300) ,
  "CanDelete" NUMBER(1) DEFAULT 1 NOT NULL ENABLE,
  "Version" NUMBER(7,5) DEFAULT 1 NOT NULL ENABLE,
  PRIMARY KEY ("UID"),
  CONSTRAINT "PARTNER_FUNCTION_CON" UNIQUE ("PartnerFunctionId") ENABLE
);


---
--- Table structure for table 'workflow_activity'
---

CREATE TABLE "workflow_activity" (
  "UID" NUMBER(19) DEFAULT 0 NOT NULL ENABLE ,
  "ActivityType" NUMBER(2,0) NOT NULL ,
  "Description" VARCHAR2(80)  NOT NULL ENABLE ,
  "ParamList" VARCHAR2(200) ,
  "CanDelete" NUMBER(1) DEFAULT 1 NOT NULL ENABLE,
  "Version" NUMBER(7,5) DEFAULT 1 NOT NULL ENABLE,
  PRIMARY KEY ("UID")
);


--- #### partnerprocess ####
---
--- Table structure for table 'pf_trigger'
---

CREATE TABLE "pf_trigger" (
  "UID" NUMBER(19) DEFAULT 0 NOT NULL ENABLE ,
  "TriggerLevel" NUMBER(2,0) DEFAULT 0 NOT NULL ENABLE ,
  "PartnerFunctionId" VARCHAR2(30) NOT NULL  ,
  "ProcessId" VARCHAR2(80)  ,
  "DocType" VARCHAR2(30)  ,
  "PartnerType" VARCHAR2(3)  ,
  "PartnerGroup" VARCHAR2(3)  ,
  "PartnerId" VARCHAR2(20)  ,
  "TriggerType" NUMBER(2,0) DEFAULT 0 NOT NULL ENABLE ,
  "IsRequest" NUMBER(1) DEFAULT 1 NOT NULL ENABLE,
  "CanDelete" NUMBER(1) DEFAULT 1 NOT NULL ENABLE,
  "Version" NUMBER(7,5) DEFAULT 1 NOT NULL ENABLE,
  "IsLocalPending" NUMBER(1) DEFAULT 1 NOT NULL ENABLE,
  "NumOfRetries" NUMBER(5) DEFAULT 0,
  "RetryInterval" NUMBER(10) DEFAULT 0,
  "ChannelUID" NUMBER(19) DEFAULT -99999999,
  PRIMARY KEY ("UID"),
  CONSTRAINT "PF_TRIGGER_CON" UNIQUE ("TriggerLevel","TriggerType","DocType","PartnerType","PartnerGroup","PartnerId") ENABLE
);


---
--- Table structure for table 'process_mapping'
---
--- Changed: Extended length of DocType from 12 to 30

CREATE TABLE "process_mapping" (
  "UID" NUMBER(19) DEFAULT 0 NOT NULL ENABLE ,
  "ProcessDef" VARCHAR2(80) NOT NULL  ,
  "PartnerID" VARCHAR2(20) NOT NULL  ,
  "IsInitiatingRole" NUMBER(1) DEFAULT 1 NOT NULL ENABLE,
  "DocType" VARCHAR2(30) ,
  "SendChannelUID" NUMBER(19) ,
  "ProcessIndicatorCode" VARCHAR2(80),
  "ProcessVersionID" VARCHAR2(80),
  "PartnerRoleMapping" NUMBER(19) ,
  "CanDelete" NUMBER(1) DEFAULT 1 NOT NULL ENABLE,
  "Version" NUMBER(7,5) DEFAULT 1 NOT NULL ENABLE,
  PRIMARY KEY ("UID"),
  CONSTRAINT "PROCESS_MAPPING_CON" UNIQUE ("ProcessDef","PartnerID","IsInitiatingRole") ENABLE
);


---
--- Table structure for table 'biz_cert_mapping'
---

CREATE TABLE "biz_cert_mapping" (
  "UID" NUMBER(19) DEFAULT 0 NOT NULL ENABLE ,
  "PartnerID" VARCHAR2(20)  NOT NULL ENABLE ,
  "PartnerCertUID" NUMBER(19) DEFAULT 0 NOT NULL ENABLE,
  "OwnCertUID" NUMBER(19) DEFAULT 0 NOT NULL ENABLE,
  "CanDelete" NUMBER(1) DEFAULT 1 NOT NULL ENABLE,
  "Version" NUMBER(7,5) DEFAULT 1 NOT NULL ENABLE,
  PRIMARY KEY ("UID"),
  CONSTRAINT "BIZ_CERT_MAPPING_CON" UNIQUE ("PartnerID") ENABLE
);


--- #### registration ####
---
--- Table structure for table 'gridtalk_license'
---

CREATE TABLE "gridtalk_license" (
  "UID" NUMBER(19) DEFAULT 0 NOT NULL ENABLE,
  "LicenseUid" NUMBER(19) NOT NULL,
  "OsName" VARCHAR2(100)  NOT NULL ENABLE,
  "OsVersion" VARCHAR2(30)  NOT NULL ENABLE,
  "MachineName" VARCHAR2(200)  NOT NULL ENABLE,
  "StartDate" VARCHAR2(80)  NOT NULL ENABLE,
  "EndDate" VARCHAR2(80)  NOT NULL ENABLE,
  "CanDelete" NUMBER(1) DEFAULT 1 NOT NULL ENABLE,
  "Version" NUMBER(7,5) DEFAULT 1 NOT NULL ENABLE,
  PRIMARY KEY ("UID"),
  CONSTRAINT "GRIDTALK_LICENSE_CON" UNIQUE ("LicenseUid") ENABLE
);


--- #### rnif ####
CREATE TABLE "rn_profile" ( 
  "UID" NUMBER(19) DEFAULT 0 NOT NULL ENABLE,
  "DocumentUid" NUMBER(19),
  "ProcessInstanceId" VARCHAR2(255) ,
  "ProcessOriginatorId" VARCHAR2(80) ,
  "ProcessResponderId" VARCHAR2(80) ,
  "ProcessDefName" VARCHAR2(80) ,
  "ReceiverDomain" VARCHAR2(80) ,
  "ReceiverGlobalBusIdentifier" VARCHAR2(80) ,
  "ReceiverLocationId" VARCHAR2(80) ,
  "SenderDomain" VARCHAR2(80) ,
  "SenderGlobalBusIdentifier" VARCHAR2(80) ,
  "SenderLocationId" VARCHAR2(80) ,
  "DeliveryMessageTrackingId" VARCHAR2(80) ,
  "BusActivityIdentifier" VARCHAR2(80) ,
  "FromGlobalPartnerRoleClassCode" VARCHAR2(80) ,
  "FromGlobalBusServiceCode" VARCHAR2(80) ,
  "InReplyToGlobalBusActionCode" VARCHAR2(80) ,
  "InReplyToMessageStandard" VARCHAR2(80) ,
  "InReplyToStandardVersion" VARCHAR2(80) ,
  "InReplyToVersionIdentifier" VARCHAR2(80) ,
  "ServiceMessageTrackingId" VARCHAR2(80) ,
  "ActionIdGlobalBusActCode" VARCHAR2(80) , -- Original:ActionIdentityGlobalBusActionCode
  "ActionIdToMsgStandard" VARCHAR2(80) , -- Original:ActionIdentityToMessageStandard
  "ActionIdStandardVersion" VARCHAR2(80) , -- Original:ActionIdentityStandardVersion
  "ActionIdVersionIdentifier" VARCHAR2(80) , -- Original:ActionIdentityVersionIdentifier
  "SignalIdGlobalBusSignalCode" VARCHAR2(80) , -- Original:SignalIdentityGlobalBusSignalCode
  "SignalIdVersionIdentifier" VARCHAR2(80) , -- Original:SignalIdentityVersionIdentifier
  "ToGlobalPartnerRoleClassCode" VARCHAR2(80) ,
  "ToGlobalBusServiceCode" VARCHAR2(80) ,
  "GlobalUsageCode" VARCHAR2(80) ,
  "PartnerGlobalBusIdentifier" VARCHAR2(80) ,
  "PIPGlobalProcessCode" VARCHAR2(80) ,
  "PIPInstanceIdentifier" VARCHAR2(200) ,
  "PIPVersionIdentifier" VARCHAR2(80) ,
  "ProcessTransactionId" VARCHAR2(200) ,
  "ProcessActionId" VARCHAR2(240) ,
  "FromGlobalPartnerClassCode" VARCHAR2(80) ,
  "ToGlobalPartnerClassCode" VARCHAR2(80) ,
  "NumberOfAttas" NUMBER(10) ,
  "IsSignalDoc" NUMBER(1) ,
  "IsRequestMsg" NUMBER(1) ,	
  "UniqueValue" VARCHAR2(80) ,	
  "AttemptCount" NUMBER(10) ,		
  "MsgDigest" VARCHAR2(80) ,	
  "RNIFVersion" VARCHAR2(80) ,
  "InResponseToActionID" VARCHAR2(80) ,
  "UserTrackingID" VARCHAR2(255) ,
  PRIMARY KEY ("UID")
);

COMMENT ON COLUMN "rn_profile"."ActionIdGlobalBusActCode" IS 'Column name has been shortened. Original:ActionIdentityGlobalBusActionCode';
COMMENT ON COLUMN "rn_profile"."ActionIdToMsgStandard" IS 'Column name has been shortened. Original:ActionIdentityToMessageStandard';
COMMENT ON COLUMN "rn_profile"."ActionIdStandardVersion" IS 'Column name has been shortened. Original:ActionIdentityStandardVersion';
COMMENT ON COLUMN "rn_profile"."ActionIdVersionIdentifier" IS 'Column name has been shortened. Original:ActionIdentityVersionIdentifier';
COMMENT ON COLUMN "rn_profile"."SignalIdGlobalBusSignalCode" IS 'Column name has been shortened. Original:SignalIdentityGlobalBusSignalCode';
COMMENT ON COLUMN "rn_profile"."SignalIdVersionIdentifier" IS 'Column name has been shortened. Original:SignalIdentityVersionIdentifier';



-- To store the JMS msg that failed to be delivered to the destination

CREATE TABLE "jms_failed_msg" (
   "UID" NUMBER(19) NOT NULL ENABLE,
   "DestinationType" VARCHAR(20),
   "ConfigName" VARCHAR(20),
   "DestName" VARCHAR(100) NOT NULL ENABLE,
   "MsgObj" BLOB NOT NULL ENABLE,
   "MsgProps" BLOB,
   "CreatedDate" TIMESTAMP(3) NOT NULL ENABLE,
   "RetryCount" NUMBER(10) DEFAULT 0 NOT NULL ENABLE,
   PRIMARY KEY("UID")
);

COMMIT;
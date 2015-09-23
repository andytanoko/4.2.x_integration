-- 26 June 2007    [Tam Wei Xiang]       GTVAN     i)   Moved create script for archive_process_view
--                                                 from first-create-userdb-gtas.sql. 
--                                                 ii)  The rtprocess uid under archive_process_view has been pointed to wrong field
--                                                 iii) Added processStatus into archive_process_view 
--

--- ------------------------------------------------------------------------
--- This script includes part of the CREATE queries for all tables in USERDB
--- ------------------------------------------------------------------------

CONNECT USERDB/gridnode;

ALTER SESSION SET NLS_DATE_FORMAT = 'YYYY-MM-DD HH24:MI:SS';
--- #### alert ####
---
--- Table structure for table 'action'
---

CREATE TABLE "action" (
  "UID" NUMBER(19) DEFAULT 0 NOT NULL ENABLE ,
  "Name" VARCHAR2(80)  NOT NULL ENABLE ,
  "Description" VARCHAR2(255) ,
  "MsgUid" NUMBER(19) ,
  "Version" NUMBER(7,5) DEFAULT 1 NOT NULL ENABLE,
  "CanDelete" NUMBER(1) DEFAULT 1 NOT NULL ENABLE,
  PRIMARY KEY ("UID"),
  CONSTRAINT "ACTION_CON" UNIQUE ("Name") ENABLE
);


---
--- Table structure for table 'alert'
---

CREATE TABLE "alert" (
  "UID" NUMBER(19) DEFAULT 0 NOT NULL ENABLE ,
  "Name" VARCHAR2(80)  NOT NULL ENABLE ,
  "AlertType" NUMBER(19) DEFAULT 0 NOT NULL ENABLE ,
  "Category" NUMBER(19) DEFAULT NULL ,
  "TriggerCond" VARCHAR2(2000) ,
  "Description" VARCHAR2(255) ,
  "Version" NUMBER(7,5) DEFAULT 1 NOT NULL ENABLE,
  "CanDelete" NUMBER(1) DEFAULT 1 NOT NULL ENABLE,
  PRIMARY KEY ("UID"),
  CONSTRAINT "ALERT_CON" UNIQUE ("Name") ENABLE
);


---
--- Table structure for table 'alert_action'
---

CREATE TABLE "alert_action" (
  "UID" NUMBER(19) DEFAULT 0 NOT NULL ENABLE ,
  "AlertUid" NUMBER(19) DEFAULT 0 NOT NULL ENABLE ,
  "ActionUid" NUMBER(19) DEFAULT 0 NOT NULL ENABLE ,
  PRIMARY KEY ("UID"),
  CONSTRAINT "ALERT_ACT_CON" UNIQUE ("AlertUid","ActionUid") ENABLE
);


---
--- Table structure for table 'alert_category'
---

CREATE TABLE "alert_category" (
  "UID" NUMBER(19) DEFAULT 0 NOT NULL ENABLE ,
  "Code" VARCHAR2(20) DEFAULT '0' NOT NULL ENABLE ,
  "Name" VARCHAR2(80) ,
  "Description" VARCHAR2(255) ,
  "Version" NUMBER(7,5) DEFAULT 1 NOT NULL ENABLE,
  "CanDelete" NUMBER(1) DEFAULT 1 NOT NULL ENABLE,
  PRIMARY KEY ("UID"),
  CONSTRAINT "ALERT_CAT_CON" UNIQUE ("Code") ENABLE
);


---
--- Table structure for table 'alert_list'
---

CREATE TABLE "alert_list" (
  "UID" NUMBER(19) DEFAULT 0 NOT NULL ENABLE ,
  "UserUid" NUMBER(19) DEFAULT 0 NOT NULL ENABLE ,
  "FromUid" NUMBER(19) ,
  "Title" VARCHAR2(35) ,
  "Message" VARCHAR2(2000) NULL ,
  "ReadStatus" NUMBER(1) DEFAULT 0 ,
  "Date" DATE ,
  "Version" NUMBER(7,5) DEFAULT 1 NOT NULL ENABLE,
  "CanDelete" NUMBER(1) DEFAULT 1 NOT NULL ENABLE,
  PRIMARY KEY ("UID")
);



---
--- Table structure for table 'message_template'
---

CREATE TABLE "message_template" (
  "UID" NUMBER(19) DEFAULT 0 NOT NULL ENABLE ,
  "Name" VARCHAR2(80)  NOT NULL ENABLE ,
  "ContentType" VARCHAR2(30) ,
  "MessageType" VARCHAR2(30) ,
  "FromAddr" VARCHAR2(255) ,
  "ToAddr" VARCHAR2(255) ,
  "CcAddr" VARCHAR2(255) ,
  "Subject" VARCHAR2(255) ,
  "Message" VARCHAR2(2000) NULL ,
  "Location" VARCHAR2(255) ,
  "Append" NUMBER(1) DEFAULT 0 ,
  "Version" NUMBER(7,5) DEFAULT 1 NOT NULL ENABLE,
  "CanDelete" NUMBER(1) DEFAULT 1 NOT NULL ENABLE,
  "JmsDestination" NUMBER(19) ,
  "MessageProperties" VARCHAR2(2000),
  PRIMARY KEY ("UID"),
  CONSTRAINT "MESSAGE_TEMPLATE_CON" UNIQUE ("Name") ENABLE
);


---
--- Table structure for table 'alert_type'
---

CREATE TABLE "alert_type" (
  "UID" NUMBER(19) DEFAULT 0 NOT NULL ENABLE ,
  "Name" VARCHAR2(35)  NOT NULL ENABLE ,
  "Description" VARCHAR2(100) ,
  "Version" NUMBER(7,5) DEFAULT 1 NOT NULL ENABLE,
  "CanDelete" NUMBER(1) DEFAULT 1 NOT NULL ENABLE,
  PRIMARY KEY ("UID"),
  CONSTRAINT "ALERT_TYPE_CON" UNIQUE ("Name") ENABLE
);


---
--- Table structure for table 'jms_destination'
---

CREATE TABLE "jms_destination" (
  "UID" NUMBER(19) NOT NULL,
  "Version" NUMBER(7,5) DEFAULT 1 NOT NULL ENABLE,
  "CanDelete" NUMBER(1) DEFAULT 1 NOT NULL ENABLE,
  "Name" VARCHAR2(30) ,
  "Type" NUMBER(1) DEFAULT 1,
  "JndiName" VARCHAR2(255) ,
  "DeliveryMode" NUMBER(1) DEFAULT 0,
  "Priority" NUMBER(2,0) DEFAULT -1,
  "ConnectionFactoryJndi" VARCHAR2(255) ,
  "ConnectionUser" VARCHAR2(30) ,
  "ConnectionPassword" VARCHAR2(30) ,
  "LookupProperties" VARCHAR2(2000) ,
  "RetryInterval" NUMBER(10),
  "MaximumRetries" NUMBER(10),
  PRIMARY KEY  ("UID"),
  CONSTRAINT "JMS_DEST_CON" UNIQUE ("Name") ENABLE
);


---
--- Table structure for table 'jms_msg_record'
---

CREATE TABLE "jms_msg_record" (
  "UID" NUMBER(19) NOT NULL,
  "Version" NUMBER(7,5) DEFAULT 1 NOT NULL ENABLE,
  "CanDelete" NUMBER(1) DEFAULT 1 NOT NULL ENABLE,
  "JmsDestUid" NUMBER(19) NOT NULL,
  "AlertTimestamp" DATE,
  "MsgData" VARCHAR2(4000) ,
  "PermanentFailed" NUMBER(1) DEFAULT 0,
  "AlertTimeInLong" NUMBER(19),
  PRIMARY KEY  ("UID")
);



--- #### bizreg ####

---
--- Table structure for table 'business_entity'
---

CREATE TABLE "business_entity" (
  "UID" NUMBER(19) DEFAULT 0 NOT NULL ENABLE ,
  "ID" VARCHAR2(4)  NOT NULL ENABLE,
  "EnterpriseID" VARCHAR2(20) ,
  "Description" VARCHAR2(80)  NOT NULL ENABLE,
  "IsPartner" NUMBER(1)  DEFAULT 0 NOT NULL ENABLE ,
  "PartnerCat" NUMBER(1) ,
  "IsPublishable" NUMBER(1) DEFAULT 1 NOT NULL ENABLE ,
  "IsSyncToServer" NUMBER(1) DEFAULT 1 NOT NULL ENABLE ,
  "State" NUMBER(2,0) DEFAULT 0 NOT NULL ENABLE ,
  "CanDelete" NUMBER(1) DEFAULT 1 NOT NULL ENABLE ,
  "Version" NUMBER(7,5) DEFAULT 1 NOT NULL ENABLE ,
  PRIMARY KEY ("UID"),
  CONSTRAINT "BUSS_ENT_CON" UNIQUE ("EnterpriseID","ID") ENABLE
);




---
--- Table structure for table 'whitepage'
---

CREATE TABLE "whitepage" (
  "UID" NUMBER(19) DEFAULT 0 NOT NULL ENABLE ,
  "BizEntityUID" NUMBER(19) DEFAULT 0 NOT NULL ENABLE ,
  "BusinessDesc" VARCHAR2(80) ,
  "DUNS" VARCHAR2(20) ,
  "GlobalSCCode" VARCHAR2(255) ,
  "ContactPerson" VARCHAR2(80) ,
  "Email" VARCHAR2(255) ,
  "Tel" VARCHAR2(16) ,
  "Fax" VARCHAR2(16) ,
  "Website" VARCHAR2(255) ,
  "Address" VARCHAR2(255) ,
  "City" VARCHAR2(50) ,
  "State" VARCHAR2(6) ,
  "ZipCode" VARCHAR2(10) ,
  "POBox" VARCHAR2(10) ,
  "Country" CHAR(3) ,
  "Language" CHAR(3) ,
  PRIMARY KEY ("UID"),
  CONSTRAINT "WHITEPAGE_CON" UNIQUE ("BizEntityUID") ENABLE
);


---
--- Table structure for table 'registry_object_map'
---

CREATE TABLE "registry_object_map" (
  "UID" NUMBER(19) DEFAULT 0 NOT NULL ENABLE ,
  "RegistryObjectKey" VARCHAR2(50) NOT NULL,
  "RegistryObjectType" VARCHAR2(30)NOT NULL,
  "RegistryQueryUrl" VARCHAR2(255) NOT NULL,
  "RegistryPublishUrl" VARCHAR2(255) NOT NULL,
  "ProprietaryObjectKey" VARCHAR2(50) NOT NULL,
  "ProprietaryObjectType" VARCHAR2(30) NOT NULL,
  "IsPublishedObject" NUMBER(1) DEFAULT 1 NOT NULL ENABLE ,
  "State" NUMBER(2,0) DEFAULT 0 NOT NULL ENABLE ,
  PRIMARY KEY ("UID"),
  CONSTRAINT "REG_OBJ_CON" UNIQUE ("RegistryObjectKey","RegistryObjectType","RegistryQueryUrl") ENABLE
);


---
--- Table structure for table 'registry_connect_info'
---

CREATE TABLE "registry_connect_info" (
  "UID" NUMBER(19) DEFAULT 0 NOT NULL ENABLE ,
  "Name" VARCHAR2(50) NOT NULL,
  "QueryUrl" VARCHAR2(255) NOT NULL,
  "PublishUrl" VARCHAR2(255),
  "PublishUser" VARCHAR2(20),
  "PublishPassword" VARCHAR2(20),
  "CanDelete" NUMBER(1) DEFAULT 1 NOT NULL ENABLE ,
  "Version" NUMBER(7,5) DEFAULT 1 NOT NULL ENABLE ,
  PRIMARY KEY ("UID"),
  CONSTRAINT "REG_CONNECT_CON" UNIQUE ("Name") ENABLE
);


---
--- Table structure for table 'domain_identifier'
---

CREATE TABLE "domain_identifier" (
  "UID" NUMBER(19) DEFAULT 0 NOT NULL ENABLE ,
  "BizEntityUID" NUMBER(19) NOT NULL,
  "Domain" VARCHAR2(30) NOT NULL,
  "Type" VARCHAR2(50) NOT NULL,
  "Value" VARCHAR2(255),
  "CanDelete" NUMBER(1) DEFAULT 1 NOT NULL ENABLE ,
  PRIMARY KEY ("UID"),
  CONSTRAINT "DOMAIN_ID_CON" UNIQUE ("Type","Value") ENABLE
);



--- #### channel ####

---
--- Table structure for table 'channel_info'
---

CREATE TABLE "channel_info" (
  "UID" NUMBER(19) DEFAULT 0 NOT NULL ENABLE ,
  "Name" VARCHAR2(30) NOT NULL ENABLE ,
  "Description" VARCHAR2(80) ,
  "RefId" VARCHAR2(30) ,
  "TptProtocolType" VARCHAR2(10) NOT NULL ENABLE ,
  "TptCommInfo" NUMBER(19) DEFAULT 0 NOT NULL ENABLE ,
  "PackagingProfile" NUMBER(19) DEFAULT 0 NOT NULL ENABLE,
  "SecurityProfile" NUMBER(19) DEFAULT 0 NOT NULL ENABLE,
  "Version" NUMBER(7,5) DEFAULT 0 NOT NULL ENABLE ,
  "isMaster" NUMBER(1) DEFAULT 0,
  "isPartner" NUMBER(1) DEFAULT 0,
  "CanDelete" NUMBER(1) DEFAULT 1 NOT NULL ENABLE ,
  "PartnerCategory" NUMBER(1) ,
  "isDisable" NUMBER(1) DEFAULT 0,
  "isRelay" NUMBER(1) ,
  "FlowControlProfile" BLOB NULL,
  PRIMARY KEY ("UID"),
  CONSTRAINT "CHANNEL_INFO_CON" UNIQUE ("Name","RefId") ENABLE
);

CREATE INDEX  "CHANNEL_INFO_IDX1" ON  "channel_info" ("TptProtocolType");
CREATE INDEX  "CHANNEL_INFO_IDX2" ON  "channel_info" ("RefId");
CREATE INDEX  "CHANNEL_INFO_IDX3" ON  "channel_info" ("TptCommInfo");


---
--- Table structure for table 'comm_info'
---

CREATE TABLE "comm_info" (
  "UID" NUMBER(19) DEFAULT 0 NOT NULL ENABLE ,
  "Name" VARCHAR2(30) NOT NULL ENABLE ,
  "Description" VARCHAR2(80) ,
  "ProtocolType" VARCHAR2(10) NOT NULL ENABLE ,
---  ProtocolVersion VARCHAR2(10) ,
---  Host VARCHAR2(30) ,
---  Port NUMBER(10) DEFAULT 0 NOT NULL ENABLE ,
  "TptImplVersion" VARCHAR2(6) DEFAULT '000001' NOT NULL ENABLE ,
---  ProtocolDetail BLOB NULL ,
  "RefId" VARCHAR2(30),
  "IsDefaultTpt" NUMBER(1) DEFAULT 0 NOT NULL ENABLE ,
  "Version" NUMBER(7,5) DEFAULT 0 NOT NULL ENABLE ,
  "CanDelete" NUMBER(1) DEFAULT 1 NOT NULL ENABLE ,
  "isPartner" NUMBER(1) DEFAULT 0,
  "PartnerCategory" NUMBER(1) ,
  "isDisable" NUMBER(1) DEFAULT 0,
  "Url" VARCHAR2(255),
  PRIMARY KEY ("UID"),
  CONSTRAINT "COMM_INFO_CON" UNIQUE ("Name","RefId") ENABLE
);



---
--- Table structure for table 'packaging_profile'
---
--- COMMENT='Packaging Profile Information'

CREATE TABLE "packaging_profile" (
  "UID" NUMBER(19) DEFAULT 0 NOT NULL ENABLE,
  "Name" VARCHAR2(30) ,
  "Description" VARCHAR2(80) NOT NULL ENABLE,
  "Envelope" VARCHAR2(20) ,
  "Zip" NUMBER(1) DEFAULT 1 NOT NULL ENABLE,
  "ZipThreshold" NUMBER(10) DEFAULT 500,
  "CanDelete" NUMBER(1) DEFAULT 1 NOT NULL ENABLE ,
  "Version" NUMBER(7,5) DEFAULT 1 NOT NULL ENABLE ,
  "RefId" VARCHAR2(30),
  "isPartner" NUMBER(1) DEFAULT 0,
  "PartnerCategory" NUMBER(1) DEFAULT NULL,
  "isDisable" NUMBER(1) DEFAULT 0,
  "Split" NUMBER(1) DEFAULT 0 NOT NULL ENABLE,
  "SplitThreshold" NUMBER(10) DEFAULT 5000,
  "SplitSize" NUMBER(10) DEFAULT 5000,
  "PackagingInfoExtension" BLOB NULL,
  PRIMARY KEY  ("UID"),
  CONSTRAINT "PACKAGING_PROFILE_CON" UNIQUE ("Name","RefId") ENABLE
);



---
--- Table structure for table 'security_profile'
---
--- COMMENT='Security Profile per Channel'

CREATE TABLE "security_profile" (
  "UID" NUMBER(19) DEFAULT 0 NOT NULL ENABLE,
  "Name" VARCHAR2(30) ,
  "Description" VARCHAR2(80) NOT NULL ENABLE,
  "EncryptionType" VARCHAR2(30) ,
  "EncryptionLevel" NUMBER(10) ,
  "EncryptionCertificate" NUMBER(19) ,
  "SignatureType" VARCHAR2(20) ,
  "DigestAlgorithm" VARCHAR2(10) ,
  "SignatureEncryptionCertificate" NUMBER(19) ,
  "CanDelete" NUMBER(1) DEFAULT 1 NOT NULL ENABLE ,
  "Version" NUMBER(7,5) DEFAULT 1 NOT NULL ENABLE ,
  "RefId" VARCHAR2(30),
  "isPartner" NUMBER(1) DEFAULT 0,
  "PartnerCategory" NUMBER(1) ,
  "isDisable" NUMBER(1) DEFAULT 0,
  "CompressionType" VARCHAR2(30) ,
  "CompressionMethod" VARCHAR2(30) ,
  "CompressionLevel" NUMBER(10) ,
  "Sequence" VARCHAR2(30) ,
  "EncryptionAlgorithm" VARCHAR2(30) ,
  PRIMARY KEY  ("UID"),
  CONSTRAINT "SECURITY_PROFILE_CON" UNIQUE ("Name","RefId") ENABLE
);

COMMENT ON TABLE "security_profile" IS 'Security Profile per Channel';


--- #### coyprofile ####
---
--- Table structure for table 'coy_profile'
---

CREATE TABLE "coy_profile" (
  "UID" NUMBER(19) DEFAULT 0 NOT NULL ENABLE ,
  "CoyName" VARCHAR2(255) NOT NULL ENABLE ,
  "Email" VARCHAR2(255) ,
  "AltEmail" VARCHAR2(255) ,
  "Tel" VARCHAR2(16) ,
  "AltTel" VARCHAR2(16) ,
  "Fax" VARCHAR2(16) ,
  "Address" VARCHAR2(255) ,
  "City" VARCHAR2(50) ,
  "State" VARCHAR2(6) ,
  "ZipCode" VARCHAR2(10) ,
  "Country" CHAR(3) ,
  "Language" CHAR(2) ,
  "IsPartner" NUMBER(1) DEFAULT 1 NOT NULL ENABLE,
  "CanDelete" NUMBER(1) DEFAULT 1 NOT NULL ENABLE,
  "Version" NUMBER(7,5) DEFAULT 1 NOT NULL ENABLE,
  PRIMARY KEY ("UID"),
  CONSTRAINT "COY_PROFILE_CON" UNIQUE ("CoyName") ENABLE
);


--- #### gridform ####
---
--- Table structure for table 'gridform_definition'
---

CREATE TABLE "gridform_definition" (
  "UID" NUMBER(19) DEFAULT 0 NOT NULL ENABLE ,
  "Name" VARCHAR2(50) NOT NULL ENABLE ,
  "Filename" VARCHAR2(80) NOT NULL ENABLE ,
  "TemplateUID" NUMBER(19) DEFAULT 0 NOT NULL ENABLE ,
  "CanDelete" NUMBER(1) DEFAULT 1 NOT NULL ENABLE ,
  "Version" NUMBER(7,5) DEFAULT 1 NOT NULL ENABLE ,
  PRIMARY KEY ("UID"),
  CONSTRAINT "GRIDFORM_DEF_CON" UNIQUE ("Name") ENABLE
);



---
--- Table structure for table 'gridform_template'
---

CREATE TABLE "gridform_template" (
  "UID" NUMBER(19) DEFAULT 0 NOT NULL ENABLE ,
  "Name" VARCHAR2(50) NOT NULL ENABLE ,
  "Filename" VARCHAR2(80) NOT NULL ENABLE ,
  "CanDelete" NUMBER(1) DEFAULT 1 NOT NULL ENABLE ,
  "Version" NUMBER(7,5) DEFAULT 1 NOT NULL ENABLE ,
  PRIMARY KEY ("UID"),
  CONSTRAINT "GRIDFORM_TEMPLATE_CON" UNIQUE ("Name") ENABLE
);


--- #### license ####
---
--- Table structure for table 'license'
---

CREATE TABLE "license" (
  "UID" NUMBER(19) DEFAULT 0 NOT NULL ENABLE ,
  "ProductKey" VARCHAR2(30)  NOT NULL ENABLE ,
  "ProductName" VARCHAR2(80)  NOT NULL ENABLE,
  "ProductVersion" VARCHAR2(20)  NOT NULL ENABLE ,
  "StartDate" DATE NOT NULL ,
  "EndDate" DATE NOT NULL,
  "State" NUMBER(1) DEFAULT 1 NOT NULL ENABLE,
  "Version" NUMBER(7,5) DEFAULT 1 NOT NULL ENABLE,
  PRIMARY KEY ("UID")
);

CREATE INDEX  "LICENSE_IDX" ON  "license" ("ProductName","ProductVersion");



--- #### mapper ####
---
--- Table structure for table 'mapping_file'
---

CREATE TABLE "mapping_file" (
  "UID" NUMBER(19) DEFAULT 0 NOT NULL ENABLE ,
  "Name" VARCHAR2(30)  NOT NULL ENABLE ,
  "Description" VARCHAR2(80)  NOT NULL ENABLE ,
  "Filename" VARCHAR2(80)  NOT NULL ENABLE ,
  "Path" VARCHAR2(80)  NOT NULL ENABLE ,
  "SubPath" VARCHAR2(80) , -- Removed NOT NULL constraints due to Oracle does not support "empty string"
  "Type" NUMBER(1) DEFAULT -1 NOT NULL ENABLE ,
  "CanDelete" NUMBER(1) DEFAULT 1 NOT NULL ENABLE ,
  "Version" NUMBER(7,5) DEFAULT 1 NOT NULL ENABLE ,
  PRIMARY KEY ("UID"),
  CONSTRAINT "MAPPING_FILE_CON" UNIQUE ("Name") ENABLE
);


---
--- Table structure for table 'mapping_rule'
---

CREATE TABLE "mapping_rule" (
  "UID" NUMBER(19) DEFAULT 0 NOT NULL ENABLE ,
  "Name" VARCHAR2(30)  NOT NULL ENABLE ,
  "Description" VARCHAR2(80)  NOT NULL ENABLE ,
  "Type" NUMBER(1) DEFAULT -1 NOT NULL ENABLE ,
  "MappingFileUID" NUMBER(19) DEFAULT 0 NOT NULL ENABLE ,
  "TransformRefDoc" NUMBER(1) DEFAULT 0,
  "ReferenceDocUID" NUMBER(19) ,
  "XPath" VARCHAR2(1000) ,
  "ParamName" VARCHAR2(40) ,
  "KeepOriginal" NUMBER(1) DEFAULT 0 ,
  "CanDelete" NUMBER(1) DEFAULT 1 NOT NULL ENABLE ,
  "Version" NUMBER(7,5) DEFAULT 1 NOT NULL ENABLE ,
  PRIMARY KEY ("UID"),
  CONSTRAINT "MAPPING_RULE_CON" UNIQUE ("Name") ENABLE
);


---
--- Table structure for table 'xpath_mapping'
---

CREATE TABLE "xpath_mapping" (
  "UID" NUMBER(19) DEFAULT 0 NOT NULL ENABLE ,
  "RootElement" VARCHAR2(120)  NOT NULL ENABLE ,
  "XpathUid" NUMBER(19) DEFAULT 0 NOT NULL ENABLE ,
  "CanDelete" NUMBER(1) DEFAULT 1 NOT NULL ENABLE ,
  "Version" NUMBER(7,5) DEFAULT 1 NOT NULL ENABLE ,
  PRIMARY KEY ("UID"),
  CONSTRAINT "XPATH_MAPPING_CON1" UNIQUE ("RootElement") ENABLE,
  CONSTRAINT "XPATH_MAPPING_CON2" UNIQUE ("XpathUid") ENABLE
);



--- #### partner ####

---
--- Table structure for table 'partner_type'
---

CREATE TABLE "partner_type" (
  "UID" NUMBER(19) DEFAULT 0 NOT NULL ENABLE ,
  "Name" CHAR(3)  NOT NULL ENABLE ,
  "Description" VARCHAR2(80)  NOT NULL ENABLE ,
  "CanDelete" NUMBER(1) DEFAULT 1 NOT NULL ENABLE ,
  "Version" NUMBER(7,5) DEFAULT 1 NOT NULL ENABLE ,
  PRIMARY KEY ("UID"),
  CONSTRAINT "PARTNER_TYPE_CON" UNIQUE ("Name") ENABLE
);

 
---
--- Table structure for table 'partner_group'
---

CREATE TABLE "partner_group" (
  "UID" NUMBER(19) DEFAULT 0 NOT NULL ENABLE ,
  "Name" CHAR(3)  NOT NULL ENABLE ,
  "Description" VARCHAR2(80)  NOT NULL ENABLE ,
  "PartnerTypeUID" NUMBER(19) DEFAULT 0 NOT NULL ENABLE ,
  "CanDelete" NUMBER(1) DEFAULT 1 NOT NULL ENABLE ,
  "Version" NUMBER(7,5) DEFAULT 1 NOT NULL ENABLE ,
  PRIMARY KEY ("UID"),
  CONSTRAINT "PARTNER_GROUP_CON" UNIQUE ("Name","PartnerTypeUID") ENABLE
);


---
--- Table structure for table 'partner'
---

CREATE TABLE "partner" (
  "UID" NUMBER(19) DEFAULT 0 NOT NULL ENABLE ,
  "PartnerID" VARCHAR2(20)  NOT NULL ENABLE ,
  "Name" VARCHAR2(20)  NOT NULL ENABLE ,
  "Description" VARCHAR2(80)  NOT NULL ENABLE ,
  "PartnerTypeUID" NUMBER(19) DEFAULT 0 NOT NULL ENABLE ,
  "PartnerGroupUID" NUMBER(19) ,
  "CreateTime" DATE ,
  "CreateBy" VARCHAR2(15) ,
  "State" NUMBER(1) DEFAULT 1 NOT NULL ENABLE ,
  "CanDelete" NUMBER(1) DEFAULT 1 NOT NULL ENABLE ,
  "Version" NUMBER(7,5) DEFAULT 1 NOT NULL ENABLE ,
  PRIMARY KEY ("UID"),
  CONSTRAINT "PARTNER_CON" UNIQUE ("PartnerID") ENABLE
);



--- #### rnif ####

---
--- Table structure for table 'process_def'
---
CREATE TABLE "process_def" ( 
  "UID" NUMBER(19) DEFAULT 0 NOT NULL ENABLE,
  "DefName" VARCHAR2(80) ,
  "ActionTimeOut" NUMBER(10) ,
  "ProcessType" VARCHAR2(80) ,
  "RNIFVersion" VARCHAR2(80) ,
  "FromPartnerRoleClassCode" VARCHAR2(80) ,
  "FromBizServiceCode" VARCHAR2(80) ,
  "FromPartnerClassCode" VARCHAR2(80) ,
  "GToPartnerRoleClassCode" VARCHAR2(80) ,
  "GToBizServiceCode" VARCHAR2(80) ,
  "GToPartnerClassCode" VARCHAR2(80) ,
  "GProcessIndicatorCode" VARCHAR2(80) ,
  "VersionIdentifier" VARCHAR2(80) ,
  "GUsageCode" VARCHAR2(80) ,
  "RequestDocThisDocIdentifier" VARCHAR2(150) ,
  "ResponseDocThisDocIdentifier" VARCHAR2(150) ,
  "RespDocReqDocIdentifier" VARCHAR2(150) ,
  "UserTrackingIdentifier" VARCHAR2(150) ,
  "IsSynchronous" NUMBER(1) ,
  "CanDelete" NUMBER(1) DEFAULT 1 NOT NULL ENABLE,
  PRIMARY KEY ("UID")
);


---
--- Table structure for table 'process_act'
---
CREATE TABLE "process_act" ( 
  "UID" NUMBER(19) DEFAULT 0 NOT NULL ENABLE,
  "ProcessDefUid" NUMBER(19) ,
  "ProcessDefAct" NUMBER(19) ,
  "MsgType" NUMBER(19) ,
  "Retries" NUMBER(10) ,
  "TimeToAcknowledge" NUMBER(10) ,
  "IsAuthorizationRequired" NUMBER(1) ,
  "IsNonRepudiationRequired" NUMBER(1) ,
  "IsSecureTransportRequired" NUMBER(1) ,
  "BizActivityIdentifier" VARCHAR2(80) ,
  "GBizActionCode" VARCHAR2(80) ,
  "DictFile" NUMBER(19) ,
  "XMLSchema" NUMBER(19) ,		
  "GDigestAlgCode" VARCHAR2(80) ,
  "DisableDTD" NUMBER(1) ,
  "DisableSchema" NUMBER(1) ,
  "DisableEncryption" NUMBER(1) ,
  "DisableSignature" NUMBER(1) ,
  "ValidateAtSender" NUMBER(1) ,
  "OnlyEncryptPayload" NUMBER(1) ,
  "DigestAlgorithm" VARCHAR2(80) ,
  "EncryptionAlgorithm" VARCHAR2(80) ,
  "EncryptionAlgorithmLength" NUMBER(10) ,
  PRIMARY KEY ("UID")
);


--- #### searchquery ####

---
--- Table structure for table 'search_query'
---

CREATE TABLE "search_query" (
  "UID" NUMBER(19) DEFAULT 0 NOT NULL ENABLE ,
  "Name" VARCHAR2(30)  NOT NULL ENABLE ,
  "Description" VARCHAR2(80)  NOT NULL ENABLE ,
  "CreatedBy" VARCHAR2(15)  NOT NULL ENABLE ,
  "Conditions" BLOB NULL,
  "CanDelete" NUMBER(1) DEFAULT 1 NOT NULL ENABLE ,
  "Version" NUMBER(7,5) DEFAULT 1 NOT NULL ENABLE ,
  PRIMARY KEY ("UID"),
  CONSTRAINT "SEARCH_QUERY_CON" UNIQUE ("Name") ENABLE
);


--- #### servicemgmt ####

---
--- Table structure for table 'webservice'
---

CREATE TABLE "webservice" (
  "UID" NUMBER(19) DEFAULT 0 NOT NULL ENABLE ,
  "WsdlUrl" VARCHAR2(255)  NOT NULL ENABLE ,
  "EndPoint" VARCHAR2(255)  NOT NULL ENABLE ,
  "ServiceName" VARCHAR2(20) NOT NULL ,
  "ServiceGroup" VARCHAR2(20) NOT NULL ,
  "CanDelete" NUMBER(1) DEFAULT 1 NOT NULL ENABLE ,
  "Version" NUMBER(7,5) DEFAULT 1 NOT NULL ENABLE ,
  PRIMARY KEY ("UID"),
  CONSTRAINT "WEBSERVICE_CON" UNIQUE ("ServiceName","ServiceGroup") ENABLE
);


---
--- Table structure for table 'service_assignment'
---

CREATE TABLE "service_assignment" (
  "UID" NUMBER(19) DEFAULT 0 NOT NULL ENABLE ,
  "UserName" VARCHAR2(20) NOT NULL ,
  "Password" VARCHAR2(80) NOT NULL ,
  "UserType" VARCHAR2(20) NOT NULL ,
  "WebServiceUIds" VARCHAR2(500) ,
  "CanDelete" NUMBER(1) DEFAULT 1 NOT NULL ENABLE ,
  "Version" NUMBER(7,5) DEFAULT 1 NOT NULL ENABLE ,
  PRIMARY KEY ("UID"),
  CONSTRAINT "SERVICE_ASSIGNMENT_CON" UNIQUE ("UserName","UserType") ENABLE
);  


--- #### user ####
---
--- Table structure for table 'user_account'
---

CREATE TABLE "user_account" (
  "UID" NUMBER(19) DEFAULT 0 NOT NULL ENABLE ,
  "Name" VARCHAR2(50)  NOT NULL ENABLE ,
  "ID" VARCHAR2(15)  NOT NULL ENABLE ,
  "Password" VARCHAR2(12)  NOT NULL ENABLE ,
  "Phone" VARCHAR2(16) ,
  "Email" VARCHAR2(255) ,
  "Property" VARCHAR2(255) ,
  "Version" NUMBER(7,5) DEFAULT 1 NOT NULL ENABLE,
  PRIMARY KEY ("UID"),
  CONSTRAINT "USER_ACCOUNT_CON" UNIQUE ("ID") ENABLE
);

CREATE INDEX  "USER_ACCOUNT_IDX" ON  "user_account" ("Name");



---
--- Table structure for table 'user_account_state'
---

CREATE TABLE "user_account_state" (
  "UID" NUMBER(19) DEFAULT 0 NOT NULL ENABLE ,
  "UserID" VARCHAR2(15)  NOT NULL ENABLE ,
  "NumLoginTries" NUMBER(5) DEFAULT 0 NOT NULL ENABLE ,
  "IsFreeze" NUMBER(1) DEFAULT 0 NOT NULL ENABLE ,
  "FreezeTime" DATE ,
  "LastLoginTime" DATE ,
  "LastLogoutTime" DATE ,
  "State" NUMBER(1) DEFAULT 1 NOT NULL ENABLE ,
  "CanDelete" NUMBER(1) DEFAULT 1 NOT NULL ENABLE ,
  "CreateTime" DATE ,
  "CreateBy" VARCHAR2(15) ,
  PRIMARY KEY ("UID"),
  CONSTRAINT "USER_ACCOUNT_STATE_CON" UNIQUE ("UserID") ENABLE
);



--- #### workflow ####
---
--- Table structure for table 'rtprocess'
---

CREATE TABLE "rtprocess" (
  "UID" NUMBER(19) DEFAULT 0 NOT NULL ENABLE,
  "ProcessUId" NUMBER(19) DEFAULT 0 NOT NULL ENABLE,
  "State" NUMBER(19) DEFAULT 0 NOT NULL ENABLE,
  "ProcessType" VARCHAR2(100)  NOT NULL ENABLE,
  "ParentRtActivityUId" NUMBER(19) ,
  "MaxConcurrency" NUMBER(10) DEFAULT 0 NOT NULL ENABLE,
  "FinishInterval" NUMBER(10) ,
  "StartTime" DATE ,
  "EndTime" DATE ,
  "ContextUId" NUMBER(19) ,
  "EngineType" VARCHAR2(20)  ,
  "ProcessDefKey" VARCHAR2(200)  ,
  PRIMARY KEY  ("UID")
);

CREATE INDEX  "RTPROCESS_IDX1" ON  "rtprocess" ("ParentRtActivityUId");


---
--- Table structure for table 'rtactivity'
---

CREATE TABLE "rtactivity" (
  "UID" NUMBER(19) DEFAULT 0 NOT NULL ENABLE,
  "ActivityUId" NUMBER(19) DEFAULT 0 NOT NULL ENABLE,
  "State" NUMBER(19) DEFAULT 0 NOT NULL ENABLE,
  "Priority" NUMBER(19) ,
  "RtProcessUId" NUMBER(19) DEFAULT 0 NOT NULL ENABLE,
  "ActivityType" VARCHAR2(100)  NOT NULL ENABLE,
  "FinishInterval" NUMBER(10) ,
  "StartTime" DATE ,
  "EndTime" DATE ,
  "ContextUId" NUMBER(19) ,
  "EngineType" VARCHAR2(20)  ,
  "BranchName" VARCHAR2(50)  ,
  "ProcessDefKey" VARCHAR2(200)  ,
  PRIMARY KEY  ("UID")
);

CREATE INDEX  "RTACTIVITY_IDX1" ON  "rtactivity" ("RtProcessUId");


---
--- Table structure for table 'rtrestriction'
---

CREATE TABLE "rtrestriction" (
  "UID" NUMBER(19) DEFAULT 0 NOT NULL ENABLE,
  "RestrictionUId" NUMBER(19) ,
  "RestrictionType" VARCHAR2(80) ,
  "SubRestrictionType" VARCHAR2(80) ,
  "RtProcessUId" NUMBER(19) ,
  "TransActivationStateListUId" NUMBER(19) ,
  "ProcessDefKey" VARCHAR2(200)  ,
  PRIMARY KEY  ("UID")
);

CREATE INDEX  "RTRESTRICTION_IDX1" ON  "rtrestriction" ("RtProcessUId");


---
--- Table structure for table 'trans_activation_state'
---

CREATE TABLE "trans_activation_state" (
  "UID" NUMBER(19) DEFAULT 0 NOT NULL ENABLE,
  "TransitionUId" NUMBER(19) ,
  "RtRestrictionUId" NUMBER(19) ,
  "RtRestrictionType" VARCHAR2(80) ,
  "State" NUMBER(1) DEFAULT 0,
  "ListUId" NUMBER(19),
  PRIMARY KEY  ("UID")
);

---
--- Table structure for table 'rtprocessdoc'
---

CREATE TABLE "rtprocessdoc" (
  "UID" NUMBER(19) DEFAULT 0 NOT NULL ENABLE,
  "DocumentId" VARCHAR2(300) ,
  "DocumentType" VARCHAR2(80) ,
  "DocumentName" VARCHAR2(80) ,
  "BusinessTransActivityId" VARCHAR2(80) ,
  "BinaryCollaborationUId" NUMBER(19) ,
  "RtBinaryCollaborationUId" NUMBER(19) ,
  "RtBusinessTransactionUId" NUMBER(19) ,
  "IsPositiveResponse" NUMBER(1) DEFAULT 1,
  "DocProcessedFlag" NUMBER(1) DEFAULT 0,
  "AckReceiptSignalFlag" NUMBER(1) DEFAULT 0,
  "AckAcceptSignalFlag" NUMBER(1) DEFAULT 0,
  "ExceptionSignalType" VARCHAR2(80) ,
  "RoleType" VARCHAR2(20) ,
  "RetryCount" NUMBER(10) DEFAULT 0,
  "RequestDocType" VARCHAR2(80) ,
  "ResponseDocTypes" BLOB NULL ,
  "PartnerKey" VARCHAR2(80) ,
  "CustomerBEId" VARCHAR2(4),
  "Status" NUMBER(10) DEFAULT 0,
  "Reason" BLOB NULL ,
  PRIMARY KEY ("UID")
);

CREATE INDEX  "RTPROCESSDOC_IDX1" ON  "rtprocessdoc" ("DocumentId");
CREATE INDEX  "RTPROCESSDOC_IDX2" ON  "rtprocessdoc" ("RtBinaryCollaborationUId");
CREATE INDEX  "RTPROCESSDOC_IDX3" ON  "rtprocessdoc" ("RtBusinessTransactionUId");

-- TWX 26 June 2007 Archvie process view: to support archive by customer
CREATE OR REPLACE VIEW "USERDB"."archive_process_view" ("RTProcessUID","EngineType","ProcessType","StartTime","EndTime","ProcessUId","RTProcessDocUID","CustomerBEId","PartnerKey","ProcessStatus") AS SELECT "rtprocess"."UID" AS "RTProcessUID", "rtprocess"."EngineType" AS "EngineType", "rtprocess"."ProcessType" AS "ProcessType","rtprocess"."StartTime" AS "StartTime", "rtprocess"."EndTime" AS "EndTime", "rtprocess"."ProcessUId" AS "ProcessUId","rtprocessdoc"."UID" AS "RTProcessDocUID", "rtprocessdoc"."CustomerBEId" AS "CustomerBEId", "rtprocessdoc"."PartnerKey" AS "PartnerKey", "rtprocessdoc"."Status" AS "ProcessStatus" FROM "rtprocess" JOIN "rtprocessdoc" ON "rtprocess"."UID" = "rtprocessdoc"."RtBinaryCollaborationUId"; 

COMMIT;
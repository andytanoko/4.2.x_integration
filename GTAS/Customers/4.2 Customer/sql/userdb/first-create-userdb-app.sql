-- 10 April 2008        Tam Wei Xiang              GT4.2               #15: Update column type of Name in partner_type, partner_group to varchar(3)

--- ------------------------------------------------------------------------
--- This script includes part of the CREATE queries for all tables in USERDB
--- ------------------------------------------------------------------------

SET client_encoding = 'UTF8';
SET check_function_bodies = false;
SET client_min_messages = warning;
SET search_path = userdb;

--- #### alert ####
---
--- Table structure for table 'action'
---
DROP TABLE IF EXISTS "action";
CREATE TABLE "action" (
  "UID" BIGINT DEFAULT 0 NOT NULL,
  "Name" VARCHAR(80)  NOT NULL,
  "Description" VARCHAR(255) ,
  "MsgUid" BIGINT ,
  "Version" DECIMAL(7,5) DEFAULT 1 NOT NULL,
  "CanDelete" DECIMAL(1) DEFAULT 1 NOT NULL,
  PRIMARY KEY ("UID"),
  CONSTRAINT "ACTION_CON" UNIQUE ("Name")
);
ALTER TABLE "action" OWNER TO userdb;

---
--- Table structure for table 'alert'
---
DROP TABLE IF EXISTS "alert";
CREATE TABLE "alert" (
  "UID" BIGINT DEFAULT 0 NOT NULL,
  "Name" VARCHAR(80)  NOT NULL,
  "AlertType" BIGINT DEFAULT 0 NOT NULL,
  "Category" BIGINT DEFAULT NULL,
  "TriggerCond" TEXT ,
  "Description" VARCHAR(255) ,
  "Version" DECIMAL(7,5) DEFAULT 1 NOT NULL,
  "CanDelete" DECIMAL(1) DEFAULT 1 NOT NULL,
  PRIMARY KEY ("UID"),
  CONSTRAINT "ALERT_CON" UNIQUE ("Name")
);
ALTER TABLE "alert" OWNER TO userdb;

---
--- Table structure for table 'alert_action'
---
DROP TABLE IF EXISTS "alert_action";
CREATE TABLE "alert_action" (
  "UID" BIGINT DEFAULT 0 NOT NULL,
  "AlertUid" BIGINT DEFAULT 0 NOT NULL,
  "ActionUid" BIGINT DEFAULT 0 NOT NULL,
  PRIMARY KEY ("UID"),
  CONSTRAINT "ALERT_ACT_CON" UNIQUE ("AlertUid","ActionUid")
);
ALTER TABLE "alert_action" OWNER TO userdb;

---
--- Table structure for table 'alert_category'
---
DROP TABLE IF EXISTS "alert_category";
CREATE TABLE "alert_category" (
  "UID" BIGINT DEFAULT 0 NOT NULL,
  "Code" VARCHAR(20) DEFAULT '0' NOT NULL,
  "Name" VARCHAR(80),
  "Description" VARCHAR(255) ,
  "Version" DECIMAL(7,5) DEFAULT 1 NOT NULL,
  "CanDelete" DECIMAL(1) DEFAULT 1 NOT NULL,
  PRIMARY KEY ("UID"),
  CONSTRAINT "ALERT_CAT_CON" UNIQUE ("Code")
);
ALTER TABLE "alert_category" OWNER TO userdb;

---
--- Table structure for table 'alert_list'
---
DROP TABLE IF EXISTS "alert_list";
CREATE TABLE "alert_list" (
  "UID" BIGINT DEFAULT 0 NOT NULL,
  "UserUid" BIGINT DEFAULT 0 NOT NULL,
  "FromUid" BIGINT ,
  "Title" VARCHAR(35) ,
  "Message" TEXT NULL ,
  "ReadStatus" DECIMAL(1) DEFAULT 0 ,
  "Date" DATE ,
  "Version" DECIMAL(7,5) DEFAULT 1 NOT NULL ,
  "CanDelete" DECIMAL(1) DEFAULT 1 NOT NULL ,
  PRIMARY KEY ("UID")
);
ALTER TABLE "alert_list" OWNER TO userdb;


---
--- Table structure for table 'message_template'
---
DROP TABLE IF EXISTS "message_template";
CREATE TABLE "message_template" (
  "UID" BIGINT DEFAULT 0 NOT NULL  ,
  "Name" VARCHAR(80)  NOT NULL  ,
  "ContentType" VARCHAR(30) ,
  "MessageType" VARCHAR(30) ,
  "FromAddr" VARCHAR(255) ,
  "ToAddr" VARCHAR(255) ,
  "CcAddr" VARCHAR(255) ,
  "Subject" VARCHAR(255) ,
  "Message" TEXT NULL ,
  "Location" VARCHAR(255) ,
  "Append" DECIMAL(1) DEFAULT 0 ,
  "Version" DECIMAL(7,5) DEFAULT 1 NOT NULL ,
  "CanDelete" DECIMAL(1) DEFAULT 1 NOT NULL ,
  "JmsDestination" BIGINT ,
  "MessageProperties" TEXT,
  PRIMARY KEY ("UID"),
  CONSTRAINT "MESSAGE_TEMPLATE_CON" UNIQUE ("Name") 
);
ALTER TABLE "message_template" OWNER TO userdb;

---
--- Table structure for table 'alert_type'
---
DROP TABLE IF EXISTS "alert_type";
CREATE TABLE "alert_type" (
  "UID" BIGINT DEFAULT 0 NOT NULL  ,
  "Name" VARCHAR(35)  NOT NULL  ,
  "Description" VARCHAR(100) ,
  "Version" DECIMAL(7,5) DEFAULT 1 NOT NULL ,
  "CanDelete" DECIMAL(1) DEFAULT 1 NOT NULL ,
  PRIMARY KEY ("UID"),
  CONSTRAINT "ALERT_TYPE_CON" UNIQUE ("Name") 
);
ALTER TABLE "alert_type" OWNER TO userdb;

---
--- Table structure for table 'jms_destination'
---
DROP TABLE IF EXISTS "jms_destination";
CREATE TABLE "jms_destination" (
  "UID" BIGINT NOT NULL,
  "Version" DECIMAL(7,5) DEFAULT 1 NOT NULL ,
  "CanDelete" DECIMAL(1) DEFAULT 1 NOT NULL ,
  "Name" VARCHAR(30) ,
  "Type" DECIMAL(1) DEFAULT 1,
  "JndiName" VARCHAR(255) ,
  "DeliveryMode" DECIMAL(1) DEFAULT 0,
  "Priority" DECIMAL(2,0) DEFAULT -1,
  "ConnectionFactoryJndi" VARCHAR(255) ,
  "ConnectionUser" VARCHAR(30) ,
  "ConnectionPassword" VARCHAR(30) ,
  "LookupProperties" TEXT ,
  "RetryInterval" DECIMAL(10),
  "MaximumRetries" DECIMAL(10),
  PRIMARY KEY  ("UID"),
  CONSTRAINT "JMS_DEST_CON" UNIQUE ("Name") 
);
ALTER TABLE "jms_destination" OWNER TO userdb;

---
--- Table structure for table 'jms_msg_record'
---
DROP TABLE IF EXISTS "jms_msg_record";
CREATE TABLE "jms_msg_record" (
  "UID" BIGINT NOT NULL,
  "Version" DECIMAL(7,5) DEFAULT 1 NOT NULL ,
  "CanDelete" DECIMAL(1) DEFAULT 1 NOT NULL ,
  "JmsDestUid" BIGINT NOT NULL,
  "AlertTimestamp" TIMESTAMP,
  "MsgData" TEXT ,
  "PermanentFailed" DECIMAL(1) DEFAULT 0,
  "AlertTimeInLong" BIGINT,
  PRIMARY KEY  ("UID")
);
ALTER TABLE "jms_msg_record" OWNER TO userdb;


--- #### bizreg ####

---
--- Table structure for table 'business_entity'
---
DROP TABLE IF EXISTS "business_entity";
CREATE TABLE "business_entity" (
  "UID" BIGINT DEFAULT 0 NOT NULL  ,
  "ID" VARCHAR(4)  NOT NULL ,
  "EnterpriseID" VARCHAR(20) ,
  "Description" VARCHAR(80)  NOT NULL ,
  "IsPartner" DECIMAL(1)  DEFAULT 0 NOT NULL  ,
  "PartnerCat" DECIMAL(1) ,
  "IsPublishable" DECIMAL(1) DEFAULT 1 NOT NULL  ,
  "IsSyncToServer" DECIMAL(1) DEFAULT 1 NOT NULL  ,
  "State" DECIMAL(2) DEFAULT 0 NOT NULL  ,
  "CanDelete" DECIMAL(1) DEFAULT 1 NOT NULL  ,
  "Version" DECIMAL(7,5) DEFAULT 1 NOT NULL  ,
  PRIMARY KEY ("UID"),
  CONSTRAINT "BUSS_ENT_CON" UNIQUE ("EnterpriseID","ID") 
);
ALTER TABLE "business_entity" OWNER TO userdb;



---
--- Table structure for table 'whitepage'
---
DROP TABLE IF EXISTS "whitepage";
CREATE TABLE "whitepage" (
  "UID" BIGINT DEFAULT 0 NOT NULL  ,
  "BizEntityUID" BIGINT DEFAULT 0 NOT NULL  ,
  "BusinessDesc" VARCHAR(80) ,
  "DUNS" VARCHAR(20) ,
  "GlobalSCCode" VARCHAR(255) ,
  "ContactPerson" VARCHAR(80) ,
  "Email" VARCHAR(255) ,
  "Tel" VARCHAR(16) ,
  "Fax" VARCHAR(16) ,
  "Website" VARCHAR(255) ,
  "Address" VARCHAR(255) ,
  "City" VARCHAR(50) ,
  "State" VARCHAR(6) ,
  "ZipCode" VARCHAR(10) ,
  "POBox" VARCHAR(10) ,
  "Country" CHAR(3) ,
  "Language" VARCHAR(3) ,
  PRIMARY KEY ("UID"),
  CONSTRAINT "WHITEPAGE_CON" UNIQUE ("BizEntityUID") 
);
ALTER TABLE "whitepage" OWNER TO userdb;

---
--- Table structure for table 'registry_object_map'
---
DROP TABLE IF EXISTS "registry_object_map";
CREATE TABLE "registry_object_map" (
  "UID" BIGINT DEFAULT 0 NOT NULL  ,
  "RegistryObjectKey" VARCHAR(50) NOT NULL,
  "RegistryObjectType" VARCHAR(30)NOT NULL,
  "RegistryQueryUrl" VARCHAR(255) NOT NULL,
  "RegistryPublishUrl" VARCHAR(255) NOT NULL,
  "ProprietaryObjectKey" VARCHAR(50) NOT NULL,
  "ProprietaryObjectType" VARCHAR(30) NOT NULL,
  "IsPublishedObject" DECIMAL(1) DEFAULT 1 NOT NULL  ,
  "State" DECIMAL(2,0) DEFAULT 0 NOT NULL  ,
  PRIMARY KEY ("UID"),
  CONSTRAINT "REG_OBJ_CON" UNIQUE ("RegistryObjectKey","RegistryObjectType","RegistryQueryUrl") 
);
ALTER TABLE "registry_object_map" OWNER TO userdb;

---
--- Table structure for table 'registry_connect_info'
---
DROP TABLE IF EXISTS "registry_connect_info";
CREATE TABLE "registry_connect_info" (
  "UID" BIGINT DEFAULT 0 NOT NULL  ,
  "Name" VARCHAR(50) NOT NULL,
  "QueryUrl" VARCHAR(255) NOT NULL,
  "PublishUrl" VARCHAR(255),
  "PublishUser" VARCHAR(20),
  "PublishPassword" VARCHAR(20),
  "CanDelete" DECIMAL(1) DEFAULT 1 NOT NULL  ,
  "Version" DECIMAL(7,5) DEFAULT 1 NOT NULL  ,
  PRIMARY KEY ("UID"),
  CONSTRAINT "REG_CONNECT_CON" UNIQUE ("Name") 
);
ALTER TABLE "registry_connect_info" OWNER TO userdb;

---
--- Table structure for table 'domain_identifier'
---
DROP TABLE IF EXISTS "domain_identifier";
CREATE TABLE "domain_identifier" (
  "UID" BIGINT DEFAULT 0 NOT NULL  ,
  "BizEntityUID" BIGINT NOT NULL,
  "Domain" VARCHAR(30) NOT NULL,
  "Type" VARCHAR(50) NOT NULL,
  "Value" VARCHAR(255),
  "CanDelete" DECIMAL(1) DEFAULT 1 NOT NULL  ,
  PRIMARY KEY ("UID"),
  CONSTRAINT "DOMAIN_ID_CON" UNIQUE ("Type","Value") 
);
ALTER TABLE "domain_identifier" OWNER TO userdb;


--- #### channel ####

---
--- Table structure for table 'channel_info'
---
DROP TABLE IF EXISTS "channel_info";
CREATE TABLE "channel_info" (
  "UID" BIGINT DEFAULT 0 NOT NULL  ,
  "Name" VARCHAR(30) NOT NULL  ,
  "Description" VARCHAR(80) ,
  "RefId" VARCHAR(30) ,
  "TptProtocolType" VARCHAR(10) NOT NULL  ,
  "TptCommInfo" BIGINT DEFAULT 0 NOT NULL  ,
  "PackagingProfile" BIGINT DEFAULT 0 NOT NULL ,
  "SecurityProfile" BIGINT DEFAULT 0 NOT NULL ,
  "Version" DECIMAL(7,5) DEFAULT 0 NOT NULL  ,
  "isMaster" DECIMAL(1) DEFAULT 0,
  "isPartner" DECIMAL(1) DEFAULT 0,
  "CanDelete" DECIMAL(1) DEFAULT 1 NOT NULL  ,
  "PartnerCategory" DECIMAL(1) ,
  "isDisable" DECIMAL(1) DEFAULT 0,
  "isRelay" DECIMAL(1) ,
  "FlowControlProfile" BYTEA NULL,
  PRIMARY KEY ("UID"),
  CONSTRAINT "CHANNEL_INFO_CON" UNIQUE ("Name","RefId") 
);

CREATE INDEX  "CHANNEL_INFO_IDX1" ON  "channel_info" ("TptProtocolType");
CREATE INDEX  "CHANNEL_INFO_IDX2" ON  "channel_info" ("RefId");
CREATE INDEX  "CHANNEL_INFO_IDX3" ON  "channel_info" ("TptCommInfo");

ALTER TABLE "channel_info" OWNER TO userdb;
---
--- Table structure for table 'comm_info'
---
DROP TABLE IF EXISTS "comm_info";
CREATE TABLE "comm_info" (
  "UID" BIGINT DEFAULT 0 NOT NULL  ,
  "Name" VARCHAR(30) NOT NULL  ,
  "Description" VARCHAR(80) ,
  "ProtocolType" VARCHAR(10) NOT NULL  ,
---  ProtocolVersion VARCHAR2(10) ,
---  Host VARCHAR2(30) ,
---  Port NUMBER(10) DEFAULT 0 NOT NULL ENABLE ,
  "TptImplVersion" VARCHAR(6) DEFAULT '000001' NOT NULL  ,
---  ProtocolDetail BLOB NULL ,
  "RefId" VARCHAR(30),
  "IsDefaultTpt" DECIMAL(1) DEFAULT 0 NOT NULL  ,
  "Version" DECIMAL(7,5) DEFAULT 0 NOT NULL  ,
  "CanDelete" DECIMAL(1) DEFAULT 1 NOT NULL  ,
  "isPartner" DECIMAL(1) DEFAULT 0,
  "PartnerCategory" DECIMAL(1) ,
  "isDisable" DECIMAL(1) DEFAULT 0,
  "Url" VARCHAR(255),
  PRIMARY KEY ("UID"),
  CONSTRAINT "COMM_INFO_CON" UNIQUE ("Name","RefId") 
);
ALTER TABLE "comm_info" OWNER TO userdb;


---
--- Table structure for table 'packaging_profile'
---
--- COMMENT='Packaging Profile Information'
DROP TABLE IF EXISTS "packaging_profile";
CREATE TABLE "packaging_profile" (
  "UID" BIGINT DEFAULT 0 NOT NULL ,
  "Name" VARCHAR(30) ,
  "Description" VARCHAR(80) NOT NULL ,
  "Envelope" VARCHAR(20) ,
  "Zip" DECIMAL(1) DEFAULT 1 NOT NULL ,
  "ZipThreshold" DECIMAL(10) DEFAULT 500,
  "CanDelete" DECIMAL(1) DEFAULT 1 NOT NULL  ,
  "Version" DECIMAL(7,5) DEFAULT 1 NOT NULL  ,
  "RefId" VARCHAR(30),
  "isPartner" DECIMAL(1) DEFAULT 0,
  "PartnerCategory" DECIMAL(1) DEFAULT NULL,
  "isDisable" DECIMAL(1) DEFAULT 0,
  "Split" DECIMAL(1) DEFAULT 0 NOT NULL ,
  "SplitThreshold" DECIMAL(10) DEFAULT 5000,
  "SplitSize" DECIMAL(10) DEFAULT 5000,
  "PackagingInfoExtension" BYTEA NULL,
  PRIMARY KEY  ("UID"),
  CONSTRAINT "PACKAGING_PROFILE_CON" UNIQUE ("Name","RefId") 
);
ALTER TABLE "packaging_profile" OWNER TO userdb;


---
--- Table structure for table 'security_profile'
---
--- COMMENT='Security Profile per Channel'
DROP TABLE IF EXISTS "security_profile";
CREATE TABLE "security_profile" (
  "UID" BIGINT DEFAULT 0 NOT NULL ,
  "Name" VARCHAR(30) ,
  "Description" VARCHAR(80) NOT NULL ,
  "EncryptionType" VARCHAR(30) ,
  "EncryptionLevel" DECIMAL(10) ,
  "EncryptionCertificate" BIGINT ,
  "SignatureType" VARCHAR(20) ,
  "DigestAlgorithm" VARCHAR(10) ,
  "SignatureEncryptionCertificate" BIGINT ,
  "CanDelete" DECIMAL(1) DEFAULT 1 NOT NULL  ,
  "Version" DECIMAL(7,5) DEFAULT 1 NOT NULL  ,
  "RefId" VARCHAR(30),
  "isPartner" DECIMAL(1) DEFAULT 0,
  "PartnerCategory" DECIMAL(1) ,
  "isDisable" DECIMAL(1) DEFAULT 0,
  "CompressionType" VARCHAR(30) ,
  "CompressionMethod" VARCHAR(30) ,
  "CompressionLevel" DECIMAL(10) ,
  "Sequence" VARCHAR(30) ,
  "EncryptionAlgorithm" VARCHAR(30) ,
  PRIMARY KEY  ("UID"),
  CONSTRAINT "SECURITY_PROFILE_CON" UNIQUE ("Name","RefId") 
);

COMMENT ON TABLE "security_profile" IS 'Security Profile per Channel';
ALTER TABLE "security_profile" OWNER TO userdb;

--- #### coyprofile ####
---
--- Table structure for table 'coy_profile'
---
DROP TABLE IF EXISTS "coy_profile";
CREATE TABLE "coy_profile" (
  "UID" BIGINT DEFAULT 0 NOT NULL  ,
  "CoyName" VARCHAR(255) NOT NULL  ,
  "Email" VARCHAR(255) ,
  "AltEmail" VARCHAR(255) ,
  "Tel" VARCHAR(16) ,
  "AltTel" VARCHAR(16) ,
  "Fax" VARCHAR(16) ,
  "Address" VARCHAR(255) ,
  "City" VARCHAR(50) ,
  "State" VARCHAR(6) ,
  "ZipCode" VARCHAR(10) ,
  "Country" CHAR(3) ,
  "Language" CHAR(2) ,
  "IsPartner" DECIMAL(1) DEFAULT 1 NOT NULL ,
  "CanDelete" DECIMAL(1) DEFAULT 1 NOT NULL ,
  "Version" DECIMAL(7,5) DEFAULT 1 NOT NULL ,
  PRIMARY KEY ("UID"),
  CONSTRAINT "COY_PROFILE_CON" UNIQUE ("CoyName") 
);
ALTER TABLE "coy_profile" OWNER TO userdb;

--- #### gridform ####
---
--- Table structure for table 'gridform_definition'
---
DROP TABLE IF EXISTS "gridform_definition";
CREATE TABLE "gridform_definition" (
  "UID" BIGINT DEFAULT 0 NOT NULL  ,
  "Name" VARCHAR(50) NOT NULL  ,
  "Filename" VARCHAR(80) NOT NULL  ,
  "TemplateUID" BIGINT DEFAULT 0 NOT NULL  ,
  "CanDelete" DECIMAL(1) DEFAULT 1 NOT NULL  ,
  "Version" DECIMAL(7,5) DEFAULT 1 NOT NULL  ,
  PRIMARY KEY ("UID"),
  CONSTRAINT "GRIDFORM_DEF_CON" UNIQUE ("Name") 
);
ALTER TABLE "gridform_definition" OWNER TO userdb;


---
--- Table structure for table 'gridform_template'
---
DROP TABLE IF EXISTS "gridform_template";
CREATE TABLE "gridform_template" (
  "UID" BIGINT DEFAULT 0 NOT NULL  ,
  "Name" VARCHAR(50) NOT NULL  ,
  "Filename" VARCHAR(80) NOT NULL  ,
  "CanDelete" DECIMAL(1) DEFAULT 1 NOT NULL  ,
  "Version" DECIMAL(7,5) DEFAULT 1 NOT NULL  ,
  PRIMARY KEY ("UID"),
  CONSTRAINT "GRIDFORM_TEMPLATE_CON" UNIQUE ("Name") 
);
ALTER TABLE "gridform_template" OWNER TO userdb;

--- #### license ####
---
--- Table structure for table 'license'
---
DROP TABLE IF EXISTS "license";
CREATE TABLE "license" (
  "UID" BIGINT DEFAULT 0 NOT NULL  ,
  "ProductKey" VARCHAR(30)  NOT NULL  ,
  "ProductName" VARCHAR(80)  NOT NULL ,
  "ProductVersion" VARCHAR(20)  NOT NULL  ,
  "StartDate" TIMESTAMP NOT NULL ,
  "EndDate" TIMESTAMP NOT NULL,
  "State" DECIMAL(1) DEFAULT 1 NOT NULL ,
  "Version" DECIMAL(7,5) DEFAULT 1 NOT NULL ,
  PRIMARY KEY ("UID")
);

CREATE INDEX  "LICENSE_IDX" ON  "license" ("ProductName","ProductVersion");
ALTER TABLE "license" OWNER TO userdb;


--- #### mapper ####
---
--- Table structure for table 'mapping_file'
---
DROP TABLE IF EXISTS "mapping_file";
CREATE TABLE "mapping_file" (
  "UID" BIGINT DEFAULT 0 NOT NULL  ,
  "Name" VARCHAR(30)  NOT NULL  ,
  "Description" VARCHAR(80)  NOT NULL  ,
  "Filename" VARCHAR(80)  NOT NULL  ,
  "Path" VARCHAR(80)  NOT NULL  ,
  "SubPath" VARCHAR(80) NOT NULL,
  "Type" DECIMAL(1) DEFAULT -1 NOT NULL  ,
  "CanDelete" DECIMAL(1) DEFAULT 1 NOT NULL  ,
  "Version" DECIMAL(7,5) DEFAULT 1 NOT NULL  ,
  PRIMARY KEY ("UID"),
  CONSTRAINT "MAPPING_FILE_CON" UNIQUE ("Name") 
);
ALTER TABLE "mapping_file" OWNER TO userdb;

---
--- Table structure for table 'mapping_rule'
---
DROP TABLE IF EXISTS "mapping_rule";
CREATE TABLE "mapping_rule" (
  "UID" BIGINT DEFAULT 0 NOT NULL  ,
  "Name" VARCHAR(30)  NOT NULL  ,
  "Description" VARCHAR(80)  NOT NULL  ,
  "Type" DECIMAL(1) DEFAULT -1 NOT NULL  ,
  "MappingFileUID" BIGINT DEFAULT 0 NOT NULL  ,
  "TransformRefDoc" DECIMAL(1) DEFAULT 0,
  "ReferenceDocUID" BIGINT ,
  "XPath" TEXT ,
  "ParamName" VARCHAR(40) ,
  "KeepOriginal" DECIMAL(1) DEFAULT 0 ,
  "CanDelete" DECIMAL(1) DEFAULT 1 NOT NULL  ,
  "Version" DECIMAL(7,5) DEFAULT 1 NOT NULL  ,
  PRIMARY KEY ("UID"),
  CONSTRAINT "MAPPING_RULE_CON" UNIQUE ("Name") 
);
ALTER TABLE "mapping_rule" OWNER TO userdb;

---
--- Table structure for table 'xpath_mapping'
---
DROP TABLE IF EXISTS "xpath_mapping";
CREATE TABLE "xpath_mapping" (
  "UID" BIGINT DEFAULT 0 NOT NULL  ,
  "RootElement" VARCHAR(120)  NOT NULL  ,
  "XpathUid" BIGINT DEFAULT 0 NOT NULL  ,
  "CanDelete" DECIMAL(1) DEFAULT 1 NOT NULL  ,
  "Version" DECIMAL(7,5) DEFAULT 1 NOT NULL  ,
  PRIMARY KEY ("UID"),
  CONSTRAINT "XPATH_MAPPING_CON1" UNIQUE ("RootElement") ,
  CONSTRAINT "XPATH_MAPPING_CON2" UNIQUE ("XpathUid") 
);
ALTER TABLE "xpath_mapping" OWNER TO userdb;


--- #### partner ####

---
--- Table structure for table 'partner_type'
---
DROP TABLE IF EXISTS "partner_type";
CREATE TABLE "partner_type" (
  "UID" BIGINT DEFAULT 0 NOT NULL  ,
  "Name" VARCHAR(3)  NOT NULL  ,
  "Description" VARCHAR(80)  NOT NULL  ,
  "CanDelete" DECIMAL(1) DEFAULT 1 NOT NULL  ,
  "Version" DECIMAL(7,5) DEFAULT 1 NOT NULL  ,
  PRIMARY KEY ("UID"),
  CONSTRAINT "PARTNER_TYPE_CON" UNIQUE ("Name") 
);
ALTER TABLE "partner_type" OWNER TO userdb;
 
---
--- Table structure for table 'partner_group'
---
DROP TABLE IF EXISTS "partner_group";
CREATE TABLE "partner_group" (
  "UID" BIGINT DEFAULT 0 NOT NULL  ,
  "Name" VARCHAR(3)  NOT NULL  ,
  "Description" VARCHAR(80)  NOT NULL  ,
  "PartnerTypeUID" BIGINT DEFAULT 0 NOT NULL  ,
  "CanDelete" DECIMAL(1) DEFAULT 1 NOT NULL  ,
  "Version" DECIMAL(7,5) DEFAULT 1 NOT NULL  ,
  PRIMARY KEY ("UID"),
  CONSTRAINT "PARTNER_GROUP_CON" UNIQUE ("Name","PartnerTypeUID") 
);
ALTER TABLE "partner_group" OWNER TO userdb;

---
--- Table structure for table 'partner'
---
DROP TABLE IF EXISTS "partner";
CREATE TABLE "partner" (
  "UID" BIGINT DEFAULT 0 NOT NULL  ,
  "PartnerID" VARCHAR(20)  NOT NULL  ,
  "Name" VARCHAR(20)  NOT NULL  ,
  "Description" VARCHAR(80)  NOT NULL  ,
  "PartnerTypeUID" BIGINT DEFAULT 0 NOT NULL  ,
  "PartnerGroupUID" BIGINT ,
  "CreateTime" TIMESTAMP ,
  "CreateBy" VARCHAR(15) ,
  "State" DECIMAL(1) DEFAULT 1 NOT NULL  ,
  "CanDelete" DECIMAL(1) DEFAULT 1 NOT NULL  ,
  "Version" DECIMAL(7,5) DEFAULT 1 NOT NULL  ,
  PRIMARY KEY ("UID"),
  CONSTRAINT "PARTNER_CON" UNIQUE ("PartnerID") 
);
ALTER TABLE "partner" OWNER TO userdb;


--- #### rnif ####

---
--- Table structure for table 'process_def'
---
DROP TABLE IF EXISTS "process_def";
CREATE TABLE "process_def" ( 
  "UID" BIGINT DEFAULT 0 NOT NULL ,
  "DefName" VARCHAR(80) ,
  "ActionTimeOut" DECIMAL(10) ,
  "ProcessType" VARCHAR(80) ,
  "RNIFVersion" VARCHAR(80) ,
  "FromPartnerRoleClassCode" VARCHAR(80) ,
  "FromBizServiceCode" VARCHAR(80) ,
  "FromPartnerClassCode" VARCHAR(80) ,
  "GToPartnerRoleClassCode" VARCHAR(80) ,
  "GToBizServiceCode" VARCHAR(80) ,
  "GToPartnerClassCode" VARCHAR(80) ,
  "GProcessIndicatorCode" VARCHAR(80) ,
  "VersionIdentifier" VARCHAR(80) ,
  "GUsageCode" VARCHAR(80) ,
  "RequestDocThisDocIdentifier" VARCHAR(150) ,
  "ResponseDocThisDocIdentifier" VARCHAR(150) ,
  "RespDocReqDocIdentifier" VARCHAR(150) ,
  "UserTrackingIdentifier" VARCHAR(150) ,
  "IsSynchronous" DECIMAL(1) ,
  "CanDelete" DECIMAL(1) DEFAULT 1 NOT NULL ,
  PRIMARY KEY ("UID")
);
ALTER TABLE "process_def" OWNER TO userdb;

---
--- Table structure for table 'process_act'
---
DROP TABLE IF EXISTS "process_act";
CREATE TABLE "process_act" ( 
  "UID" BIGINT DEFAULT 0 NOT NULL ,
  "ProcessDefUid" BIGINT ,
  "ProcessDefAct" BIGINT ,
  "MsgType" BIGINT ,
  "Retries" DECIMAL(10) ,
  "TimeToAcknowledge" DECIMAL(10) ,
  "IsAuthorizationRequired" DECIMAL(1) ,
  "IsNonRepudiationRequired" DECIMAL(1) ,
  "IsSecureTransportRequired" DECIMAL(1) ,
  "BizActivityIdentifier" VARCHAR(80) ,
  "GBizActionCode" VARCHAR(80) ,
  "DictFile" BIGINT ,
  "XMLSchema" BIGINT ,		
  "GDigestAlgCode" VARCHAR(80) ,
  "DisableDTD" DECIMAL(1) ,
  "DisableSchema" DECIMAL(1) ,
  "DisableEncryption" DECIMAL(1) ,
  "DisableSignature" DECIMAL(1) ,
  "ValidateAtSender" DECIMAL(1) ,
  "OnlyEncryptPayload" DECIMAL(1) ,
  "DigestAlgorithm" VARCHAR(80) ,
  "EncryptionAlgorithm" VARCHAR(80) ,
  "EncryptionAlgorithmLength" DECIMAL(10) ,
  "IsCompressRequired" DECIMAL(1) DEFAULT NULL,
  PRIMARY KEY ("UID")
);
ALTER TABLE "process_act" OWNER TO userdb;

--- #### searchquery ####

---
--- Table structure for table 'search_query'
---
DROP TABLE IF EXISTS "search_query";
CREATE TABLE "search_query" (
  "UID" BIGINT DEFAULT 0 NOT NULL  ,
  "Name" VARCHAR(30)  NOT NULL  ,
  "Description" VARCHAR(80)  NOT NULL  ,
  "CreatedBy" VARCHAR(15)  NOT NULL  ,
  "Conditions" BYTEA NULL,
  "CanDelete" DECIMAL(1) DEFAULT 1 NOT NULL  ,
  "Version" DECIMAL(7,5) DEFAULT 1 NOT NULL  ,
  PRIMARY KEY ("UID"),
  CONSTRAINT "SEARCH_QUERY_CON" UNIQUE ("Name") 
);
ALTER TABLE "search_query" OWNER TO userdb;

--- #### servicemgmt ####

---
--- Table structure for table 'webservice'
---
DROP TABLE IF EXISTS "webservice";
CREATE TABLE "webservice" (
  "UID" BIGINT DEFAULT 0 NOT NULL  ,
  "WsdlUrl" VARCHAR(255)  NOT NULL  ,
  "EndPoint" VARCHAR(255)  NOT NULL  ,
  "ServiceName" VARCHAR(20) NOT NULL ,
  "ServiceGroup" VARCHAR(20) NOT NULL ,
  "CanDelete" DECIMAL(1) DEFAULT 1 NOT NULL  ,
  "Version" DECIMAL(7,5) DEFAULT 1 NOT NULL  ,
  PRIMARY KEY ("UID"),
  CONSTRAINT "WEBSERVICE_CON" UNIQUE ("ServiceName","ServiceGroup") 
);
ALTER TABLE "webservice" OWNER TO userdb;

---
--- Table structure for table 'service_assignment'
---
DROP TABLE IF EXISTS "service_assignment";
CREATE TABLE "service_assignment" (
  "UID" BIGINT DEFAULT 0 NOT NULL  ,
  "UserName" VARCHAR(20) NOT NULL ,
  "Password" VARCHAR(80) NOT NULL ,
  "UserType" VARCHAR(20) NOT NULL ,
  "WebServiceUIds" VARCHAR(500) ,
  "CanDelete" DECIMAL(1) DEFAULT 1 NOT NULL  ,
  "Version" DECIMAL(7,5) DEFAULT 1 NOT NULL  ,
  PRIMARY KEY ("UID"),
  CONSTRAINT "SERVICE_ASSIGNMENT_CON" UNIQUE ("UserName","UserType") 
);  
ALTER TABLE "service_assignment" OWNER TO userdb;

--- #### user ####
---
--- Table structure for table 'user_account'
---
DROP TABLE IF EXISTS "user_account";
CREATE TABLE "user_account" (
  "UID" BIGINT DEFAULT 0 NOT NULL  ,
  "Name" VARCHAR(50)  NOT NULL  ,
  "ID" VARCHAR(15)  NOT NULL  ,
  "Password" VARCHAR(12)  NOT NULL  ,
  "Phone" VARCHAR(16) ,
  "Email" VARCHAR(255) ,
  "Property" VARCHAR(255) ,
  "Version" DECIMAL(7,5) DEFAULT 1 NOT NULL ,
  PRIMARY KEY ("UID"),
  CONSTRAINT "USER_ACCOUNT_CON" UNIQUE ("ID") 
);

CREATE INDEX  "USER_ACCOUNT_IDX" ON  "user_account" ("Name");
ALTER TABLE "user_account" OWNER TO userdb;


---
--- Table structure for table 'user_account_state'
---
DROP TABLE IF EXISTS "user_account_state";
CREATE TABLE "user_account_state" (
  "UID" BIGINT DEFAULT 0 NOT NULL  ,
  "UserID" VARCHAR(15)  NOT NULL  ,
  "NumLoginTries" DECIMAL(5) DEFAULT 0 NOT NULL  ,
  "IsFreeze" DECIMAL(1) DEFAULT 0 NOT NULL  ,
  "FreezeTime" TIMESTAMP ,
  "LastLoginTime" TIMESTAMP ,
  "LastLogoutTime" TIMESTAMP ,
  "State" DECIMAL(1) DEFAULT 1 NOT NULL  ,
  "CanDelete" DECIMAL(1) DEFAULT 1 NOT NULL  ,
  "CreateTime" TIMESTAMP ,
  "CreateBy" VARCHAR(15) ,
  PRIMARY KEY ("UID"),
  CONSTRAINT "USER_ACCOUNT_STATE_CON" UNIQUE ("UserID") 
);
ALTER TABLE "user_account_state" OWNER TO userdb;


--- #### workflow ####
---
--- Table structure for table 'rtprocess'
---
DROP TABLE IF EXISTS "rtprocess";
CREATE TABLE "rtprocess" (
  "UID" BIGINT,
  "ProcessUId" BIGINT,
  "State" BIGINT DEFAULT 0 NOT NULL ,
  "ProcessType" VARCHAR(100)  NOT NULL ,
  "ParentRtActivityUId" BIGINT ,
  "MaxConcurrency" DECIMAL(10) DEFAULT 0 NOT NULL ,
  "FinishInterval" DECIMAL(10) ,
  "StartTime" TIMESTAMP ,
  "EndTime" TIMESTAMP ,
  "ContextUId" BIGINT ,
  "EngineType" VARCHAR(20)  ,
  "ProcessDefKey" VARCHAR(200)  ,
  PRIMARY KEY  ("UID")
);

CREATE INDEX  "RTPROCESS_IDX1" ON  "rtprocess" ("ParentRtActivityUId");
ALTER TABLE "rtprocess" OWNER TO userdb;

---
--- Table structure for table 'rtactivity'
---
DROP TABLE IF EXISTS "rtactivity";
CREATE TABLE "rtactivity" (
  "UID" BIGINT,
  "ActivityUId" BIGINT,
  "State" BIGINT DEFAULT 0 NOT NULL ,
  "Priority" BIGINT ,
  "RtProcessUId" BIGINT,
  "ActivityType" VARCHAR(100)  NOT NULL ,
  "FinishInterval" DECIMAL(10) ,
  "StartTime" TIMESTAMP ,
  "EndTime" TIMESTAMP ,
  "ContextUId" BIGINT ,
  "EngineType" VARCHAR(20)  ,
  "BranchName" VARCHAR(50)  ,
  "ProcessDefKey" VARCHAR(200)  ,
  PRIMARY KEY  ("UID")
);

CREATE INDEX  "RTACTIVITY_IDX1" ON  "rtactivity" ("RtProcessUId");
ALTER TABLE "rtactivity" OWNER TO userdb;

---
--- Table structure for table 'rtrestriction'
---
DROP TABLE IF EXISTS "rtrestriction";
CREATE TABLE "rtrestriction" (
  "UID" BIGINT,
  "RestrictionUId" BIGINT ,
  "RestrictionType" VARCHAR(80) ,
  "SubRestrictionType" VARCHAR(80) ,
  "RtProcessUId" BIGINT ,
  "TransActivationStateListUId" BIGINT ,
  "ProcessDefKey" VARCHAR(200)  ,
  PRIMARY KEY  ("UID")
);

CREATE INDEX  "RTRESTRICTION_IDX1" ON  "rtrestriction" ("RtProcessUId");
ALTER TABLE "rtrestriction" OWNER TO userdb;

---
--- Table structure for table 'trans_activation_state'
---
DROP TABLE IF EXISTS "trans_activation_state";
CREATE TABLE "trans_activation_state" (
  "UID" BIGINT,
  "TransitionUId" BIGINT ,
  "RtRestrictionUId" BIGINT ,
  "RtRestrictionType" VARCHAR(80) ,
  "State" DECIMAL(1) DEFAULT 0,
  "ListUId" BIGINT,
  PRIMARY KEY  ("UID")
);
ALTER TABLE "trans_activation_state" OWNER TO userdb;

---
--- Table structure for table 'rtprocessdoc'
---
DROP TABLE IF EXISTS "rtprocessdoc";
CREATE TABLE "rtprocessdoc" (
  "UID" BIGINT,
  "DocumentId" VARCHAR(300) ,
  "DocumentType" VARCHAR(80) ,
  "DocumentName" VARCHAR(80) ,
  "BusinessTransActivityId" VARCHAR(80) ,
  "BinaryCollaborationUId" BIGINT ,
  "RtBinaryCollaborationUId" BIGINT ,
  "RtBusinessTransactionUId" BIGINT ,
  "IsPositiveResponse" DECIMAL(1) DEFAULT 1,
  "DocProcessedFlag" DECIMAL(1) DEFAULT 0,
  "AckReceiptSignalFlag" DECIMAL(1) DEFAULT 0,
  "AckAcceptSignalFlag" DECIMAL(1) DEFAULT 0,
  "ExceptionSignalType" VARCHAR(80) ,
  "RoleType" VARCHAR(20) ,
  "RetryCount" DECIMAL(10) DEFAULT 0,
  "RequestDocType" VARCHAR(80) ,
  "ResponseDocTypes" BYTEA NULL ,
  "PartnerKey" VARCHAR(80) ,
  "CustomerBEId" VARCHAR(4),
  "Status" DECIMAL(10) DEFAULT 0,
  "Reason" BYTEA NULL ,
  PRIMARY KEY ("UID")
);

CREATE INDEX  "RTPROCESSDOC_IDX1" ON  "rtprocessdoc" ("DocumentId");
CREATE INDEX  "RTPROCESSDOC_IDX2" ON  "rtprocessdoc" ("RtBinaryCollaborationUId");
CREATE INDEX  "RTPROCESSDOC_IDX3" ON  "rtprocessdoc" ("RtBusinessTransactionUId");
ALTER TABLE "rtprocessdoc" OWNER TO userdb;

COMMIT;
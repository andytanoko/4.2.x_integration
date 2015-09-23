SET client_encoding = 'UTF8';
SET check_function_bodies = false;
SET client_min_messages = warning;
SET search_path = userdb;

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

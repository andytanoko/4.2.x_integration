SET client_encoding = 'UTF8';
SET check_function_bodies = false;
SET client_min_messages = warning;
SET search_path = userdb;
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

SET client_encoding = 'UTF8';
SET check_function_bodies = false;
SET client_min_messages = warning;
SET search_path = userdb;

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
  "DigestAlgorithm" VARCHAR(30) ,
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
ALTER TABLE "security_profile" OWNER TO userdb;
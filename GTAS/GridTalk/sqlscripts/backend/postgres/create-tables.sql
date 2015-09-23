SET client_encoding = 'UTF8';
SET check_function_bodies = false;
SET client_min_messages = warning;
SET search_path = userdb;

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

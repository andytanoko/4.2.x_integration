SET client_encoding = 'UTF8';
SET check_function_bodies = false;
SET client_min_messages = warning;
SET search_path = userdb;

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
  "MappingClass" VARCHAR(255),
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

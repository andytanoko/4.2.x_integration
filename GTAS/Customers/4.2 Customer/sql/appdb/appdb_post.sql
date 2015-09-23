--- --------------------------------------------------------------------
--- This script includes all the CREATE queries for all tables in APPDB
--- --------------------------------------------------------------------

SET client_encoding = 'UTF8';
SET check_function_bodies = false;
SET client_min_messages = warning;
SET search_path = appdb;

---
--- TABLE STRUCTURE FOR TABLE 'entitymetainfo'
---
-- Framework/sqlscripts/create-metainfo-table.sql
DROP TABLE IF EXISTS "entitymetainfo";
CREATE TABLE "entitymetainfo" (
  "ObjectName" VARCHAR(80) NOT NULL DEFAULT '' ,
  "EntityName" VARCHAR(80) NOT NULL DEFAULT '' ,
  "SqlName" VARCHAR(40) ,
  PRIMARY KEY ("ObjectName")
);
ALTER TABLE "entitymetainfo" OWNER TO appdb;
---------------------------------------------------------------------------


---------------------------------------------------------------------------
---
--- TABLE STRUCTURE FOR TABLE 'fieldmetainfo'
---
-- Framework/sqlscripts/create-metainfo-table.sql
DROP TABLE IF EXISTS "fieldmetainfo";
CREATE TABLE  "fieldmetainfo" 
(
  "UID" BIGSERIAL NOT NULL, 
  "ObjectName" VARCHAR(80) , 
  "FieldName" VARCHAR(40) , 
  "SqlName" VARCHAR(40), 
  "ValueClass" VARCHAR(80) , 
  "EntityObjectName" VARCHAR(255) , 
  "Label" VARCHAR(255), 
  "Length" DECIMAL(10) DEFAULT 0, 
  "Proxy" DECIMAL(1) DEFAULT 0, 
  "Mandatory" DECIMAL(1) DEFAULT 0, 
  "Editable" DECIMAL(1) DEFAULT 1, 
  "Displayable" DECIMAL(1) DEFAULT 1, 
  "OqlName" VARCHAR(40), 
  "Sequence" DECIMAL(3) DEFAULT 999, 
  "Presentation" VARCHAR(1000), 
  "Constraints" VARCHAR(1000), 
  PRIMARY KEY ("UID"), 
  CONSTRAINT "FIELDMETAINFO_UNIQUE_CON" UNIQUE ("EntityObjectName", "FieldName")
);
ALTER SEQUENCE "fieldmetainfo_UID_seq" OWNED BY "fieldmetainfo"."UID"; -- so that while we drop the column or table, the sequence will be dropped as well
ALTER TABLE "fieldmetainfo" OWNER TO appdb;
---------------------------------------------------------------------------

---
--- Table structure for table 'referencenum'
---
--- Framework/sqlscripts/create-refnum.sql
DROP TABLE IF EXISTS "referencenum";
CREATE TABLE "referencenum" 
(
  "RefName" VARCHAR(50)  ,
  "LastRefNum" BIGINT DEFAULT 0 ,
  "MaxRefNum" BIGINT DEFAULT -1 ,
  PRIMARY KEY ("RefName")
);
ALTER TABLE "referencenum" OWNER TO appdb;

---------------------------------------------------------------------------

---
--- Table structure for table 'country_code'
---
--- PDIPBaseServices\Locale\sqlscripts\create-tables.sql
DROP TABLE IF EXISTS "country_code";
CREATE TABLE "country_code" 
(
  "Name" VARCHAR(255)  NOT NULL,
  "NumericalCode" DECIMAL(5) DEFAULT 0 NOT NULL,
  "Alpha2Code" VARCHAR(2)  NOT NULL,
  "Alpha3Code" VARCHAR(3)  NOT NULL ,
  PRIMARY KEY ("NumericalCode"),
  CONSTRAINT "COUNTRY_CODE_CON" UNIQUE ("Alpha3Code"),
  CONSTRAINT "COUNTRY_CODE_CON2" UNIQUE ("Alpha2Code")   
);
CREATE INDEX  "COUNTRY_CODE_IDX" ON  "country_code" ("Name");
ALTER TABLE "country_code" OWNER TO appdb;

---------------------------------------------------------------------------

---
--- Table structure for table 'language_code'
---
--- PDIPBaseServices\Locale\sqlscripts\create-tables.sql
DROP TABLE IF EXISTS "language_code";
CREATE TABLE "language_code" (
  "Name" VARCHAR(255) DEFAULT '0' NOT NULL,
  "Alpha2Code" VARCHAR(2) DEFAULT '0' NOT NULL,
  "BAlpha3Code" VARCHAR(3) DEFAULT '0' NOT NULL,
  "TAlpha3Code" VARCHAR(3)  NOT NULL,
  PRIMARY KEY ("Alpha2Code"),
  CONSTRAINT "LANGUAGE_CODE_CON" UNIQUE ("TAlpha3Code"),
  CONSTRAINT "LANGUAGE_CODE_CON2" UNIQUE ("BAlpha3Code")   
);
CREATE INDEX  "LANGUAGE_CODE_IDX" ON  "language_code" ("Name");
ALTER TABLE "language_code" OWNER TO appdb;

---------------------------------------------------------------------------

---
--- Table structure for table 'gncategory'
---
--- GTAS\GridTalk\sqlscripts\gridnode\create-tables.sql

DROP TABLE IF EXISTS "gncategory";
CREATE TABLE "gncategory" (
  "Code" VARCHAR(3)  NOT NULL,
  "Name" VARCHAR(50)  NOT NULL,
  "NodeUsage" VARCHAR(10)  NOT NULL,
  PRIMARY KEY ("Code"),
  CONSTRAINT "GNCATEGORY_CON" UNIQUE ("Name")
);
ALTER TABLE "gncategory" OWNER TO appdb;

COMMIT;
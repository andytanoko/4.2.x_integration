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


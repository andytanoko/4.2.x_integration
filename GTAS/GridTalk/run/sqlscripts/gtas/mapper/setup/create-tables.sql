SET client_encoding = 'UTF8';
SET check_function_bodies = false;
SET client_min_messages = warning;
SET search_path = userdb;

--- #### mapper ####
---
--- Table structure for table 'gridtalk_mapping_rule'
---
DROP TABLE IF EXISTS "gridtalk_mapping_rule";
CREATE TABLE "gridtalk_mapping_rule" (
  "UID" BIGINT DEFAULT 0 NOT NULL ,
  "Name" VARCHAR(30)  NOT NULL ,
  "Description" VARCHAR(80)  NOT NULL DEFAULT '',
  "SourceDocType" VARCHAR(30) NOT NULL DEFAULT '',  
  "TargetDocType" VARCHAR(30)  NOT NULL DEFAULT '',  
  "SourceDocFileType" VARCHAR(30) NOT NULL DEFAULT '',  
  "TargetDocFileType" VARCHAR(30)  NOT NULL DEFAULT '', 
  "HeaderTransformation" DECIMAL(1) DEFAULT 0 NOT NULL ,
  "TransformWithHeader" DECIMAL(1) DEFAULT 0 NOT NULL ,
  "TransformWithSource" DECIMAL(1) DEFAULT 0 NOT NULL ,
  "MappingRuleUID" BIGINT DEFAULT 0 NOT NULL ,
  "CanDelete" DECIMAL(1) DEFAULT 1 NOT NULL,
  "Version" DECIMAL(7,5) DEFAULT 1 NOT NULL,
  PRIMARY KEY ("UID"),
  CONSTRAINT "GRIDTALK_MAPPING_RULE_CON" UNIQUE ("Name")
);
ALTER TABLE "gridtalk_mapping_rule" OWNER TO userdb;

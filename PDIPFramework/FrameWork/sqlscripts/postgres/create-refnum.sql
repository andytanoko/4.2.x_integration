SET client_encoding = 'UTF8';
SET check_function_bodies = false;
SET client_min_messages = warning;
SET search_path = appdb;

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



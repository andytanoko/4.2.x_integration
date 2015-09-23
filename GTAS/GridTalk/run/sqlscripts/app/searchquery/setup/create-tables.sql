SET client_encoding = 'UTF8';
SET check_function_bodies = false;
SET client_min_messages = warning;
SET search_path = userdb;

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
  "IsPublic" BOOLEAN DEFAULT FALSE,
  PRIMARY KEY ("UID"),
  CONSTRAINT "SEARCH_QUERY_CON" UNIQUE ("Name") 
);
ALTER TABLE "search_query" OWNER TO userdb;


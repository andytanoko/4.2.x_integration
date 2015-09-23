SET client_encoding = 'UTF8';
SET check_function_bodies = false;
SET client_min_messages = warning;
SET search_path = userdb;


---
--- Table structure for table 'license'
---
DROP TABLE IF EXISTS "license";
CREATE TABLE "license" (
  "UID" BIGINT DEFAULT 0 NOT NULL  ,
  "ProductKey" VARCHAR(30)  NOT NULL  ,
  "ProductName" VARCHAR(80)  NOT NULL ,
  "ProductVersion" VARCHAR(20)  NOT NULL  ,
  "StartDate" TIMESTAMP NOT NULL ,
  "EndDate" TIMESTAMP NOT NULL,
  "State" DECIMAL(1) DEFAULT 1 NOT NULL ,
  "Version" DECIMAL(7,5) DEFAULT 1 NOT NULL ,
  PRIMARY KEY ("UID")
);

CREATE INDEX  "LICENSE_IDX" ON  "license" ("ProductName","ProductVersion");
ALTER TABLE "license" OWNER TO userdb;


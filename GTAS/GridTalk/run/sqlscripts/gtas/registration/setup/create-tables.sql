SET client_encoding = 'UTF8';
SET check_function_bodies = false;
SET client_min_messages = warning;
SET search_path = userdb;

--- #### registration ####
---
--- Table structure for table 'gridtalk_license'
---
DROP TABLE IF EXISTS "gridtalk_license";
CREATE TABLE "gridtalk_license" (
  "UID" BIGINT DEFAULT 0 NOT NULL,
  "LicenseUid" BIGINT NOT NULL,
  "OsName" VARCHAR(100)  NOT NULL,
  "OsVersion" VARCHAR(30)  NOT NULL,
  "MachineName" VARCHAR(200)  NOT NULL,
  "StartDate" VARCHAR(80)  NOT NULL,
  "EndDate" VARCHAR(80)  NOT NULL,
  "CanDelete" DECIMAL(1) DEFAULT 1 NOT NULL,
  "Version" DECIMAL(7,5) DEFAULT 1 NOT NULL,
  PRIMARY KEY ("UID"),
  CONSTRAINT "GRIDTALK_LICENSE_CON" UNIQUE ("LicenseUid")
);
ALTER TABLE "gridtalk_license" OWNER TO userdb;
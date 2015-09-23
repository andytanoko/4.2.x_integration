SET client_encoding = 'UTF8';
SET check_function_bodies = false;
SET client_min_messages = warning;
SET search_path = userdb;


---
--- Table structure for table 'coy_profile'
---
DROP TABLE IF EXISTS "coy_profile";
CREATE TABLE "coy_profile" (
  "UID" BIGINT DEFAULT 0 NOT NULL  ,
  "CoyName" VARCHAR(255) NOT NULL  ,
  "Email" VARCHAR(255) ,
  "AltEmail" VARCHAR(255) ,
  "Tel" VARCHAR(16) ,
  "AltTel" VARCHAR(16) ,
  "Fax" VARCHAR(16) ,
  "Address" VARCHAR(255) ,
  "City" VARCHAR(50) ,
  "State" VARCHAR(6) ,
  "ZipCode" VARCHAR(10) ,
  "Country" CHAR(3) ,
  "Language" CHAR(2) ,
  "IsPartner" DECIMAL(1) DEFAULT 1 NOT NULL ,
  "CanDelete" DECIMAL(1) DEFAULT 1 NOT NULL ,
  "Version" DECIMAL(7,5) DEFAULT 1 NOT NULL ,
  PRIMARY KEY ("UID"),
  CONSTRAINT "COY_PROFILE_CON" UNIQUE ("CoyName") 
);
ALTER TABLE "coy_profile" OWNER TO userdb;


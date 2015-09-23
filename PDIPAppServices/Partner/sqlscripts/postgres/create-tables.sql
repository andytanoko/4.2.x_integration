SET client_encoding = 'UTF8';
SET check_function_bodies = false;
SET client_min_messages = warning;
SET search_path = userdb;

---
--- Table structure for table 'partner_type'
---
DROP TABLE IF EXISTS "partner_type";
CREATE TABLE "partner_type" (
  "UID" BIGINT DEFAULT 0 NOT NULL  ,
  "Name" VARCHAR(3)  NOT NULL  ,
  "Description" VARCHAR(80)  NOT NULL  ,
  "CanDelete" DECIMAL(1) DEFAULT 1 NOT NULL  ,
  "Version" DECIMAL(7,5) DEFAULT 1 NOT NULL  ,
  PRIMARY KEY ("UID"),
  CONSTRAINT "PARTNER_TYPE_CON" UNIQUE ("Name") 
);
ALTER TABLE "partner_type" OWNER TO userdb;

---
--- Table structure for table 'partner_group'
---
DROP TABLE IF EXISTS "partner_group";
CREATE TABLE "partner_group" (
  "UID" BIGINT DEFAULT 0 NOT NULL  ,
  "Name" VARCHAR(3)  NOT NULL  ,
  "Description" VARCHAR(80)  NOT NULL  ,
  "PartnerTypeUID" BIGINT DEFAULT 0 NOT NULL  ,
  "CanDelete" DECIMAL(1) DEFAULT 1 NOT NULL  ,
  "Version" DECIMAL(7,5) DEFAULT 1 NOT NULL  ,
  PRIMARY KEY ("UID"),
  CONSTRAINT "PARTNER_GROUP_CON" UNIQUE ("Name","PartnerTypeUID") 
);
ALTER TABLE "partner_group" OWNER TO userdb;

---
--- Table structure for table 'partner'
---
DROP TABLE IF EXISTS "partner";
CREATE TABLE "partner" (
  "UID" BIGINT DEFAULT 0 NOT NULL  ,
  "PartnerID" VARCHAR(20)  NOT NULL  ,
  "Name" VARCHAR(20)  NOT NULL  ,
  "Description" VARCHAR(80)  NOT NULL  ,
  "PartnerTypeUID" BIGINT DEFAULT 0 NOT NULL  ,
  "PartnerGroupUID" BIGINT ,
  "CreateTime" TIMESTAMP ,
  "CreateBy" VARCHAR(15) ,
  "State" DECIMAL(1) DEFAULT 1 NOT NULL  ,
  "CanDelete" DECIMAL(1) DEFAULT 1 NOT NULL  ,
  "Version" DECIMAL(7,5) DEFAULT 1 NOT NULL  ,
  PRIMARY KEY ("UID"),
  CONSTRAINT "PARTNER_CON" UNIQUE ("PartnerID") 
);
ALTER TABLE "partner" OWNER TO userdb;
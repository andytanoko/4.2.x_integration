SET client_encoding = 'UTF8';
SET check_function_bodies = false;
SET client_min_messages = warning;
SET search_path = userdb;
---
--- Table structure for table 'alert_trigger'
---
--- Changed: Extended length of DocType from 12 to 30

DROP TABLE IF EXISTS "alert_trigger";
CREATE TABLE "alert_trigger" (
  "UID" BIGINT DEFAULT 0 NOT NULL ,
  "Level" DECIMAL(2,0) DEFAULT 0 NOT NULL ,
  "AlertType" VARCHAR(35) NOT NULL ,
  "AlertUID" BIGINT DEFAULT 0 NOT NULL ,
  "DocType" VARCHAR(30)  ,
  "PartnerType" VARCHAR(3)  ,
  "PartnerGroup" VARCHAR(3)  ,
  "PartnerId" VARCHAR(20)  ,
  "Recipients" TEXT  ,
  "Enabled" DECIMAL(1) DEFAULT 1 NOT NULL ,
  "AttachDoc" DECIMAL(1) DEFAULT 0 NOT NULL ,
  "CanDelete" DECIMAL(1) DEFAULT 1 NOT NULL ,
  "Version" DECIMAL(7,5) DEFAULT 1 NOT NULL ,
  PRIMARY KEY ("UID"),
  CONSTRAINT "ALERT_TRIGGER_CON" UNIQUE ("Level","AlertType","DocType","PartnerType","PartnerGroup","PartnerId")
);
ALTER TABLE "alert_trigger" OWNER TO userdb;

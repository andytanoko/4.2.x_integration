SET client_encoding = 'UTF8';
SET check_function_bodies = false;
SET client_min_messages = warning;
SET search_path = userdb;

DROP TABLE IF EXISTS "partner_function";
CREATE TABLE "partner_function" (
  "UID" BIGINT DEFAULT 0 NOT NULL ,
  "PartnerFunctionId" VARCHAR(30)  NOT NULL ,
  "Description" VARCHAR(80)  NOT NULL ,
  "TriggerOn" DECIMAL(2,0) DEFAULT 0 NOT NULL ,
  "WorkflowActivityUids" VARCHAR(300) ,
  "CanDelete" DECIMAL(1) DEFAULT 1 NOT NULL,
  "Version" DECIMAL(7,5) DEFAULT 1 NOT NULL,
  PRIMARY KEY ("UID"),
  CONSTRAINT "PARTNER_FUNCTION_CON" UNIQUE ("PartnerFunctionId")
);
ALTER TABLE "partner_function" OWNER TO userdb;

---
--- Table structure for table 'workflow_activity'
---
DROP TABLE IF EXISTS "workflow_activity";
CREATE TABLE "workflow_activity" (
  "UID" BIGINT DEFAULT 0 NOT NULL ,
  "ActivityType" DECIMAL(2,0) NOT NULL ,
  "Description" VARCHAR(80)  NOT NULL ,
  "ParamList" VARCHAR(200) ,
  "CanDelete" DECIMAL(1) DEFAULT 1 NOT NULL,
  "Version" DECIMAL(7,5) DEFAULT 1 NOT NULL,
  PRIMARY KEY ("UID")
);
ALTER TABLE "workflow_activity" OWNER TO userdb;

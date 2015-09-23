---20090818    Tam Wei Xiang      [GT4.2.1]    Added db index from GT4.1.4
---

SET client_encoding = 'UTF8';
SET check_function_bodies = false;
SET client_min_messages = warning;
SET search_path = userdb;


---
--- Table structure for table 'rtprocess'
---
DROP TABLE IF EXISTS "rtprocess";
CREATE TABLE "rtprocess" (
  "UID" BIGINT,
  "ProcessUId" BIGINT,
  "State" BIGINT DEFAULT 0 NOT NULL ,
  "ProcessType" VARCHAR(100)  NOT NULL ,
  "ParentRtActivityUId" BIGINT ,
  "MaxConcurrency" DECIMAL(10) DEFAULT 0 NOT NULL ,
  "FinishInterval" DECIMAL(10) ,
  "StartTime" TIMESTAMP ,
  "EndTime" TIMESTAMP ,
  "ContextUId" BIGINT ,
  "EngineType" VARCHAR(20)  ,
  "ProcessDefKey" VARCHAR(200)  ,
  PRIMARY KEY  ("UID")
);

DROP index IF EXISTS "RTPROCESS_IDX1";
DROP index IF EXISTS "RTPROCESS_IDX2";
DROP index IF EXISTS "RTPROCESS_IDX3";
DROP index IF EXISTS "RTPROCESS_IDX4";
DROP index IF EXISTS "RTPROCESS_IDX5";
DROP index IF EXISTS "RTPROCESS_IDX6";

CREATE INDEX "RTPROCESS_IDX1" ON "rtprocess" ("ParentRtActivityUId");
CREATE INDEX "RTPROCESS_IDX2" ON "rtprocess" ("EngineType", "ProcessType", "StartTime"); 
CREATE INDEX "RTPROCESS_IDX3" ON "rtprocess" ("EngineType", "ProcessType");
CREATE INDEX "RTPROCESS_IDX4" ON "rtprocess" ("ProcessUId", "ProcessType", "UID");
CREATE INDEX "RTPROCESS_IDX5" ON "rtprocess" ("StartTime", "ProcessUId", "UID");
CREATE INDEX "RTPROCESS_IDX6" ON "rtprocess" ("EndTime", "ProcessUId", "UID");
ALTER TABLE "rtprocess" OWNER TO userdb;

---
--- Table structure for table 'rtactivity'
---
DROP TABLE IF EXISTS "rtactivity";
CREATE TABLE "rtactivity" (
  "UID" BIGINT,
  "ActivityUId" BIGINT,
  "State" BIGINT DEFAULT 0 NOT NULL ,
  "Priority" BIGINT ,
  "RtProcessUId" BIGINT,
  "ActivityType" VARCHAR(100)  NOT NULL ,
  "FinishInterval" DECIMAL(10) ,
  "StartTime" TIMESTAMP ,
  "EndTime" TIMESTAMP ,
  "ContextUId" BIGINT ,
  "EngineType" VARCHAR(20)  ,
  "BranchName" VARCHAR(50)  ,
  "ProcessDefKey" VARCHAR(200)  ,
  PRIMARY KEY  ("UID")
);

CREATE INDEX  "RTACTIVITY_IDX1" ON  "rtactivity" ("RtProcessUId");
ALTER TABLE "rtactivity" OWNER TO userdb;

---
--- Table structure for table 'rtrestriction'
---
DROP TABLE IF EXISTS "rtrestriction";
CREATE TABLE "rtrestriction" (
  "UID" BIGINT,
  "RestrictionUId" BIGINT ,
  "RestrictionType" VARCHAR(80) ,
  "SubRestrictionType" VARCHAR(80) ,
  "RtProcessUId" BIGINT ,
  "TransActivationStateListUId" BIGINT ,
  "ProcessDefKey" VARCHAR(200)  ,
  PRIMARY KEY  ("UID")
);

CREATE INDEX  "RTRESTRICTION_IDX1" ON  "rtrestriction" ("RtProcessUId");
ALTER TABLE "rtrestriction" OWNER TO userdb;

---
--- Table structure for table 'trans_activation_state'
---
DROP TABLE IF EXISTS "trans_activation_state";
CREATE TABLE "trans_activation_state" (
  "UID" BIGINT,
  "TransitionUId" BIGINT ,
  "RtRestrictionUId" BIGINT ,
  "RtRestrictionType" VARCHAR(80) ,
  "State" DECIMAL(1) DEFAULT 0,
  "ListUId" BIGINT,
  PRIMARY KEY  ("UID")
);
ALTER TABLE "trans_activation_state" OWNER TO userdb;

---
--- Table structure for table 'rtprocessdoc'
---
DROP TABLE IF EXISTS "rtprocessdoc";
CREATE TABLE "rtprocessdoc" (
  "UID" BIGINT,
  "DocumentId" VARCHAR(300) ,
  "DocumentType" VARCHAR(80) ,
  "DocumentName" VARCHAR(80) ,
  "BusinessTransActivityId" VARCHAR(80) ,
  "BinaryCollaborationUId" BIGINT ,
  "RtBinaryCollaborationUId" BIGINT ,
  "RtBusinessTransactionUId" BIGINT ,
  "IsPositiveResponse" DECIMAL(1) DEFAULT 1,
  "DocProcessedFlag" DECIMAL(1) DEFAULT 0,
  "AckReceiptSignalFlag" DECIMAL(1) DEFAULT 0,
  "AckAcceptSignalFlag" DECIMAL(1) DEFAULT 0,
  "ExceptionSignalType" VARCHAR(80) ,
  "RoleType" VARCHAR(20) ,
  "RetryCount" DECIMAL(10) DEFAULT 0,
  "RequestDocType" VARCHAR(80) ,
  "ResponseDocTypes" BYTEA NULL ,
  "PartnerKey" VARCHAR(80) ,
  "CustomerBEId" VARCHAR(4),
  "Status" DECIMAL(10) DEFAULT 0,
  "Reason" BYTEA NULL ,
  PRIMARY KEY ("UID")
);

CREATE INDEX  "RTPROCESSDOC_IDX1" ON  "rtprocessdoc" ("DocumentId");
CREATE INDEX  "RTPROCESSDOC_IDX2" ON  "rtprocessdoc" ("RtBinaryCollaborationUId");
CREATE INDEX  "RTPROCESSDOC_IDX3" ON  "rtprocessdoc" ("RtBusinessTransactionUId");
ALTER TABLE "rtprocessdoc" OWNER TO userdb;

-- view for archival
CREATE OR REPLACE VIEW "archive_process_view" ("RTProcessUID","EngineType","ProcessType","StartTime","EndTime","ProcessUId","RTProcessDocUID","CustomerBEId","PartnerKey","ProcessStatus") 
AS SELECT "rtprocess"."UID" AS "RTProcessUID", "rtprocess"."EngineType" AS "EngineType", "rtprocess"."ProcessType" AS "ProcessType","rtprocess"."StartTime" AS "StartTime", "rtprocess"."EndTime" AS "EndTime", "rtprocess"."ProcessUId" AS "ProcessUId","rtprocessdoc"."UID" AS "RTProcessDocUID", "rtprocessdoc"."CustomerBEId" AS "CustomerBEId", "rtprocessdoc"."PartnerKey" AS "PartnerKey", "rtprocessdoc"."Status" AS "ProcessStatus" FROM "rtprocess" JOIN "rtprocessdoc" ON "rtprocess"."UID" = "rtprocessdoc"."RtBinaryCollaborationUId"; 

ALTER TABLE "archive_process_view" OWNER TO userdb;
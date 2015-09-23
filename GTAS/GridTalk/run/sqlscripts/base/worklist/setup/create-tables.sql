SET client_encoding = 'UTF8';
SET check_function_bodies = false;
SET client_min_messages = warning;
SET search_path = userdb;
---
--- Table structure for table 'worklistvalue'
---
DROP TABLE IF EXISTS "worklistvalue";
CREATE TABLE "worklistvalue" (
  "UID" BIGINT,
  "wi_description" VARCHAR(50) ,
  "wi_comments" VARCHAR(50) ,
  "wi_status" CHAR(2) ,
  "wi_cdate" TIMESTAMP ,
  "user_id" VARCHAR(80) ,
  "unassigned" CHAR(2) ,
  "processDefKey" VARCHAR(255) ,
  "activityId" VARCHAR(255) ,
  "performer" VARCHAR(150) ,
  "rtActivityUId" BIGINT,
  "contextUId" BIGINT,
  PRIMARY KEY  ("UID")
);
ALTER TABLE "worklistvalue" OWNER TO userdb;

---
--- Table structure for table 'worklistuser'
---
DROP TABLE IF EXISTS "worklistuser";
CREATE TABLE "worklistuser" (
  "UID" BIGINT DEFAULT 0 NOT NULL,
  "workitem_id" BIGINT,
  "user_id" VARCHAR(80) ,
  PRIMARY KEY  ("UID")
);
ALTER TABLE "worklistuser" OWNER TO userdb;

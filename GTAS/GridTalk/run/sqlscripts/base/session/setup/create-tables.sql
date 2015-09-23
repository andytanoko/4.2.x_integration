SET client_encoding = 'UTF8';
SET check_function_bodies = false;
SET client_min_messages = warning;
SET search_path = userdb;
---
--- Table structure for table 'session_audit'
---
DROP TABLE IF EXISTS "session_audit";
CREATE TABLE "session_audit" (
  "UID" BIGINT NOT NULL,
  "SessionId" VARCHAR(30) NOT NULL,
  "SessionName" VARCHAR(30) ,
  "State" DECIMAL(5) DEFAULT 0 NOT NULL,
  "SessionData" BYTEA NULL ,
  "OpenTime" TIMESTAMP NOT NULL ,
  "LastActiveTime" TIMESTAMP NOT NULL,
  "DestroyTime" TIMESTAMP NULL,
  PRIMARY KEY ("UID"),
  CONSTRAINT "SESSION_AUDIT_CON" UNIQUE ("SessionId")
);
ALTER TABLE "session_audit" OWNER TO userdb;




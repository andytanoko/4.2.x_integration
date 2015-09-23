---20090818    Tam Wei Xiang      [GT4.2.1]    Added db index from GT4.1.4
---

SET client_encoding = 'UTF8';
SET check_function_bodies = false;
SET client_min_messages = warning;
SET search_path = userdb;

DROP index IF EXISTS "RTPROCESS_IDX2";
DROP index IF EXISTS "RTPROCESS_IDX3";
DROP index IF EXISTS "RTPROCESS_IDX4";
DROP index IF EXISTS "RTPROCESS_IDX5";
DROP index IF EXISTS "RTPROCESS_IDX6";

CREATE INDEX "RTPROCESS_IDX2" ON "rtprocess" ("EngineType", "ProcessType", "StartTime"); 
CREATE INDEX "RTPROCESS_IDX3" ON "rtprocess" ("EngineType", "ProcessType");
CREATE INDEX "RTPROCESS_IDX4" ON "rtprocess" ("ProcessUId", "ProcessType", "UID");
CREATE INDEX "RTPROCESS_IDX5" ON "rtprocess" ("StartTime", "ProcessUId", "UID");
CREATE INDEX "RTPROCESS_IDX6" ON "rtprocess" ("EndTime", "ProcessUId", "UID");
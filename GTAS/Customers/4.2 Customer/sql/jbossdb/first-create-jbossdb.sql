--- ------------------------------------------------------------------------
--- This script includes the CREATE queries for  table "schedule" in jbossdb,
--- the rest will be auto created by JBOSS.
--- ------------------------------------------------------------------------
SET client_encoding = 'UTF8';
SET check_function_bodies = false;
SET client_min_messages = warning;
SET search_path = jbossdb;


CREATE TABLE "schedule"
(
  "schedule_uid" SERIAL,
  "target" VARCHAR(100),
  "method_name" VARCHAR(100),
  "method_signature" VARCHAR(100),
  "start_date" VARCHAR(20),
  "period" BIGINT,
  "repetitions" DECIMAL(10),
  "date_format" VARCHAR(20),
  PRIMARY KEY ("schedule_uid")
);

ALTER TABLE "schedule" OWNER TO "jbossdb";

COMMIT;
-- ------------------------------------------------------------------
-- This script Creates default data for some of the tables in JBOSSDB
-- ------------------------------------------------------------------
SET client_encoding = 'UTF8';
SET check_function_bodies = false;
SET client_min_messages = warning;
SET search_path = jbossdb;

-- schedule - default mbean schedules
INSERT INTO "schedule" ("target", "method_name", "method_signature", "start_date", "period", "repetitions")
VALUES('base.time:service=TimerTasksLoaderService', 'loadTasks', 'DATE, NEXT_DATE', 'NOW', 50000, -1);

INSERT INTO JBOSSDB."schedule" ("target", "method_name", "method_signature", "start_date", "period", "repetitions")
VALUES('base.time:service=TimerFailedJMSLoaderService', 'loadFailedJMS', '', 'NOW', 180000, -1);
-- ------------------------------------------------------------------
-- This script Creates default data for some of the tables in JBOSSDB
-- ------------------------------------------------------------------

CONNECT JBOSSDB/gridnode;

ALTER SESSION SET NLS_DATE_FORMAT = 'YYYY-MM-DD HH24:MI:SS';

-- schedule - default mbean schedules
INSERT INTO JBOSSDB."schedule" ("target", "method_name", "method_signature", "start_date", "period", "repetitions")
VALUES('base.time:service=TimerTasksLoaderService', 'loadTasks', 'DATE, NEXT_DATE', 'NOW', 50000, -1);

-- To add in the timer for handling the failed to delivered jms msg
INSERT INTO JBOSSDB."schedule" ("target", "method_name", "method_signature", "start_date", "period", "repetitions")
VALUES('base.time:service=TimerFailedJMSLoaderService', 'loadFailedJMS', '', 'NOW', 50000, -1);

COMMIT;

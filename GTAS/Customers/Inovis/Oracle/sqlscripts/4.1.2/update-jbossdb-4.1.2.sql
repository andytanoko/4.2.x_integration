-- To add in the timer for handling the failed to delivered jms msg

INSERT INTO JBOSSDB."schedule" ("target", "method_name", "method_signature", "start_date", "period", "repetitions")
VALUES('base.time:service=TimerFailedJMSLoaderService', 'loadFailedJMS', '', 'NOW', 50000, -1);
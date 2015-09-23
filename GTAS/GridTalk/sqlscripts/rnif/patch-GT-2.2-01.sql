# 16 Jul 2003 GT 2.2 I1 [Neo Sok Lay] Set CanDelete column value to "false" for default ProcessDefs.

USE userdb;

UPDATE process_def
SET CanDelete='0'
WHERE DefName IN ("0A1FailureNotification1.1","0A1FailureNotification");


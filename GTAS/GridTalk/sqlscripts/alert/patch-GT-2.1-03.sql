# 14 Aug 2003 I1 v2.1    [Neo Sok Lay] Mis-link AlertAction for PROCESS_INSTANCE_FAILURE alert.
#

USE userdb;

UPDATE alert_action
SET ActionUid='-6'
WHERE UID='-6';

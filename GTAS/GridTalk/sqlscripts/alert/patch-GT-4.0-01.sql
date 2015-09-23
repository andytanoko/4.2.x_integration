# 06 Oct 2005 I1 v4.0 [Neo Sok Lay]  Set default value for alert_trigger.Level column

USE userdb;

ALTER TABLE alert_trigger
  ALTER Level SET DEFAULT '0';

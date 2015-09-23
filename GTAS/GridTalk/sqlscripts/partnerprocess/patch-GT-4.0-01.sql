# 06 Oct 2005 I1 v4.0 [Neo Sok Lay]  Change trigger table name to pf_trigger, Set default value for TriggerLevel, TriggerType column.

USE userdb;

RENAME TABLE `trigger` TO pf_trigger;
ALTER TABLE pf_trigger
  ALTER TriggerLevel SET DEFAULT '0',
  ALTER TriggerType SET DEFAULT '0';

USE appdb;

UPDATE entitymetainfo
SET SqlName='pf_trigger'
WHERE SqlName='trigger';

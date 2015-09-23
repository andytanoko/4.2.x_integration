# 06 Oct 2005 I1 v4.0 [Neo Sok Lay]  Change ical_alarm.Repeat field name to RepeatCount

USE userdb;

ALTER TABLE ical_alarm
  CHANGE `Repeat` RepeatCount int default NULL;

USE appdb;

UPDATE fieldmetainfo
SET SqlName='RepeatCount'
WHERE EntityObjectName='com.gridnode.pdip.base.time.entities.model.iCalAlarm'
AND SqlName='Repeat';

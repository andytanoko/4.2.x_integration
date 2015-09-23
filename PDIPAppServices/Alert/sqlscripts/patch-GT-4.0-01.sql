USE userdb;

ALTER TABLE alert
  CHANGE `Trigger` TriggerCond longtext DEFAULT NULL;

UPDATE ical_alarm
SET StartDt=NULL
WHERE StartDt='0000-00-00 00:00' and UID=-10;

UPDATE ical_alarm
SET NextDueTime=NULL
WHERE NextDueTime='0000-00-00 00:00' and UID=-10;

USE appdb;
UPDATE fieldmetainfo SET SqlName='TriggerCond'
WHERE EntityObjectName='com.gridnode.pdip.app.alert.model.Alert'
AND SqlName='Trigger';

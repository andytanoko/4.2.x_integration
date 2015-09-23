# 20 Sep 2004 v2.4 [Mahesh] removed some fields from housekeeping
# 

USE appdb;

# HousekeepingInfo
DELETE FROM fieldmetainfo WHERE EntityObjectName LIKE '%HousekeepingInfo';
INSERT INTO fieldmetainfo VALUES
(NULL, '_tempFilesDaysToKeep', 'TEMP_FILES_DAYS_TO_KEEP', NULL, 'java.lang.Integer', 'com.gridnode.gtas.server.housekeeping.model.HousekeepingInfo', 'houseKeeping.tempFilesDaysToKeep', 0, 0, 0, 1, 1, NULL, 999, 'displayable=true\r\nmandatory=true\r\neditable=true', 'type=range\r\nrange.min=0');
INSERT INTO fieldmetainfo VALUES
(NULL, '_logFilesDaysToKeep', 'LOG_FILES_DAYS_TO_KEEP', NULL, 'java.lang.Integer', 'com.gridnode.gtas.server.housekeeping.model.HousekeepingInfo', 'houseKeeping.logFilesDaysToKeep', 0, 0, 0, 1, 1, NULL, 999, 'displayable=true\r\nmandatory=true\r\neditable=true', 'type=range\r\nrange.min=0');


USE userdb;

DELETE FROM ical_alarm WHERE Category LIKE '%HousekeepingInfo';
DELETE FROM ical_alarm WHERE UID = -20;
DELETE FROM ical_event WHERE UID = -20;

# ical_alarm for Housekeeping
INSERT INTO ical_alarm (UID,StartDuration,StartDt,Related,DelayPeriod,Repeat,Category,SenderKey,ReceiverKey,Disabled,NextDueTime,Count,ParentUid,ParentKind,RecurListStr,IsRecurComplete,CurRecur,IsPseudoParent,IncludeParentStartTime,TaskId) VALUES
(-20,NULL,"2004-07-14 15:20:13","1","300",NULL,"HousekeepingInfo","HousekeepingInfo","HousekeepingInfo","1",NULL,NULL,"-20","1",NULL,NULL,NULL,NULL,NULL,NULL);


INSERT INTO ical_event (UID,Kind,OwnerId,Classification,CreateDt,LastModifyDt,IsDateType,IsDtFloat,StartDt,Priority,SequenceNum,Status,iCalUid,EndDt,Duration,PropertiesStr,TimeTransparency) VALUES
(-20,1,NULL,NULL,NULL,NULL,NULL,NULL,"2004-09-17 00:00:00",NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL);
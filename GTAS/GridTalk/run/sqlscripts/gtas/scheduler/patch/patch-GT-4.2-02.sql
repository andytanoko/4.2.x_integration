-- 20100603     Tam Wei Xiang      #1100 Add in the user procedure for clearing the mapping jar that is cached by the jvm


SET client_encoding = 'UTF8';
SET check_function_bodies = false;
SET client_min_messages = warning;
SET search_path = userdb;

DELETE FROM "ical_alarm" WHERE "UID"=-2;
INSERT INTO "ical_alarm" 
("UID", "StartDuration", "StartDt", "Related", "DelayPeriod", "RepeatCount", "Category", "SenderKey", "ReceiverKey", "Disabled", "NextDueTime", "Count", "ParentUid", "ParentKind", "RecurListStr", "IsRecurComplete", "CurRecur", "IsPseudoParent", "IncludeParentStartTime", "TaskId") 
VALUES (-2,NULL,NULL,1,NULL,NULL,'UserProcedure',NULL,NULL,0,LOCALTIMESTAMP(0),NULL,-2,1,NULL,0,CURRENT_DATE,NULL,NULL,'MAP_FILE_HOUSEKEEPER_PROC');

--- ical_event
DELETE FROM "ical_event" WHERE "UID"=-2;
INSERT INTO "ical_event" 
("UID", "Kind", "OwnerId", "Classification", "CreateDt", "LastModifyDt", "IsDateType", "IsDtFloat", "StartDt", "Priority", "SequenceNum", "Status", "iCalUid", "EndDt", "Duration", "PropertiesStr", "TimeTransparency") 
VALUES (-2,1,NULL,NULL,NULL,NULL,NULL,NULL,LOCALTIMESTAMP(0),NULL,NULL,NULL,NULL,NULL,NULL,'BEGIN:VEVENT'||chr(13)||chr(10)||'RRULE:FREQ=DAILY'||chr(13)||chr(10)||'END:VEVENT',NULL);

SET client_encoding = 'UTF8';
SET check_function_bodies = false;
SET client_min_messages = warning;
SET search_path = userdb;

--- ical_alarm - default for Housekeeping
DELETE FROM "ical_alarm" WHERE "Category" LIKE '%HousekeepingInfo';
DELETE FROM "ical_alarm" WHERE "UID" = -20;
DELETE FROM "ical_event" WHERE "UID" = -20;

INSERT INTO "ical_alarm" ("UID","StartDuration","StartDt","Related","DelayPeriod","RepeatCount","Category","SenderKey","ReceiverKey","Disabled","NextDueTime","Count","ParentUid","ParentKind","RecurListStr","IsRecurComplete","CurRecur","IsPseudoParent","IncludeParentStartTime","TaskId") 
VALUES(-20,NULL,'2004-07-14 15:20:13',1,300,NULL,'HousekeepingInfo','HousekeepingInfo','HousekeepingInfo',1,NULL,NULL,-20,1,NULL,NULL,NULL,NULL,NULL,NULL);

INSERT INTO "ical_event" ("UID","Kind","OwnerId","Classification","CreateDt","LastModifyDt","IsDateType","IsDtFloat","StartDt","Priority","SequenceNum","Status","iCalUid","EndDt","Duration","PropertiesStr","TimeTransparency") 
VALUES(-20,1,NULL,NULL,NULL,NULL,NULL,NULL,'2004-09-17 00:00:00',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL);

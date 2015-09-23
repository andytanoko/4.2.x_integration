SET client_encoding = 'UTF8';
SET check_function_bodies = false;
SET client_min_messages = warning;
SET search_path = userdb;

--- ical_alarm - default to retry failed emails
INSERT INTO "ical_alarm" ("UID","StartDuration","StartDt","Related","DelayPeriod","RepeatCount","Category","SenderKey","ReceiverKey","Disabled","NextDueTime","Count","ParentUid","ParentKind","RecurListStr","IsRecurComplete","CurRecur","IsPseudoParent","IncludeParentStartTime","TaskId") 
VALUES (-10,NULL,LOCALTIMESTAMP(0),NULL,300,NULL,'EmailConfig','EmailConfig','EmailConfig',0,NULL,0,NULL,NULL,NULL,0,NULL,0,0,NULL);

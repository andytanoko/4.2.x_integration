# 13 Jul 2004 v2.4  [Mahesh] Add 
# 06 Oct 2005 v4.0  [Neo Sok Lay] Change ical_alarm.Repeat field name to RepeatCount

USE userdb;

# ical_alarm to retry failed emails
INSERT INTO ical_alarm (UID,StartDuration,StartDt,Related,DelayPeriod,RepeatCount,Category,SenderKey,ReceiverKey,Disabled,NextDueTime,Count,ParentUid,ParentKind,RecurListStr,IsRecurComplete,CurRecur,IsPseudoParent,IncludeParentStartTime,TaskId) VALUES
(-10,NULL,"2004-07-14 15:20:13",NULL,"300",NULL,"EmailConfig","EmailConfig","EmailConfig",0,NULL,0,NULL,NULL,NULL,0,NULL,0,0,NULL);
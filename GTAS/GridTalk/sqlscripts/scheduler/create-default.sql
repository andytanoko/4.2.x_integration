# 23 Aug 2006  [GT4]   [Tam Wei Xiang]          i) Add scheduler task for Cert Expiry Checking

USE userdb;

SET @dt = NOW() + 0;

# ical_alarm
INSERT INTO `ical_alarm` 
(`UID`, `StartDuration`, `StartDt`, `Related`, `DelayPeriod`, `RepeatCount`, 
`Category`, `SenderKey`, `ReceiverKey`, `Disabled`, `NextDueTime`, `Count`, 
`ParentUid`, `ParentKind`, `RecurListStr`, `IsRecurComplete`, `CurRecur`, 
`IsPseudoParent`, `IncludeParentStartTime`, `TaskId`) VALUES 
  (-1,NULL,NULL,1,NULL,NULL,
'UserProcedure',NULL,NULL,0,str_to_date(@dt,'%Y%m%d%H%i%s'),NULL,
-1,1,NULL,0,@dt,NULL,NULL,'CHECK_CERT_EXPIRY_PROC');

# ical_event
INSERT INTO `ical_event` 
(`UID`, `Kind`, `OwnerId`, `Classification`, `CreateDt`, `LastModifyDt`, 
`IsDateType`, `IsDtFloat`, `StartDt`, `Priority`, `SequenceNum`, `Status`, 
`iCalUid`, `EndDt`, `Duration`, `PropertiesStr`, `TimeTransparency`) VALUES 
  (-1,1,NULL,NULL,NULL,NULL,
NULL,NULL,str_to_date(@dt,'%Y%m%d%H%i%s'),NULL,NULL,NULL,
NULL,NULL,NULL,'BEGIN:VEVENT\r\nRRULE:FREQ=DAILY\r\nEND:VEVENT\r\n',NULL);
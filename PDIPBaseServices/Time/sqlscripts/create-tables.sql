# 09 Feb 2004 I5 v2.2 [Koh Han Sing] Add in field TaskId in ical_alarm
# 06 Oct 2005 I1 v4.0 [Neo Sok Lay]  Change ical_alarm.Repeat field name to RepeatCount
# 
USE userdb;

	CREATE TABLE `ical_alarm` (
		`UID` bigint NOT NULL default '0',
		`StartDuration` bigint default NULL,
		`StartDt` datetime default NULL,
		`Related` int default NULL,
		`DelayPeriod` bigint default NULL,
		`RepeatCount` int default NULL,
		`Category` varchar(100) default NULL,
		`SenderKey` varchar(255) default NULL,
		`ReceiverKey` varchar(255) default NULL,
		`Disabled` tinyint(1) default NULL,
		`NextDueTime` datetime default NULL,
		`Count` int default NULL,
		 ParentUid bigint default NULL,
		 ParentKind smallint default NULL,
		 RecurListStr text default NULL,
		 IsRecurComplete tinyint(1) default NULL,
		 CurRecur varchar(80) default NULL,
		 IsPseudoParent tinyint(1) default NULL,
		 IncludeParentStartTime tinyint(1) default NULL,
		 TaskId varchar(120) default NULL	
	) TYPE=MyISAM;

	CREATE TABLE `ical_event` (
		`UID` bigint NOT NULL default '0',
		`Kind` smallint default NULL,
		`OwnerId` int default NULL,
		`Classification` varchar(80) default NULL,
		`CreateDt` datetime default NULL,
		`LastModifyDt` datetime default NULL,
		`IsDateType` tinyint(1) default NULL,
		`IsDtFloat` tinyint(1) default NULL,
		`StartDt` datetime default NULL,
		`Priority` int default NULL,
		`SequenceNum` int default NULL,
		`Status` int default NULL,
		`iCalUid` varchar(80) default NULL,
		`EndDt` datetime default NULL,
		`Duration` bigint default NULL,
		`PropertiesStr` text default NULL, 
		`TimeTransparency` int default NULL
	) TYPE=MyISAM;

	CREATE TABLE `ical_todo` (
		`UID` bigint NOT NULL default '0',
		`Kind` smallint default NULL,
		`OwnerId` int default NULL,
		`Classification` varchar(80) default NULL,
		`CreateDt` datetime default NULL,
		`LastModifyDt` datetime default NULL,
		`IsDateType` tinyint(1) default NULL,
		`IsDtFloat` tinyint(1) default NULL,
		`StartDt` datetime default NULL,
		`Priority` int default NULL,
		`SequenceNum` int default NULL,
		`Status` int default NULL,
		`iCalUid` varchar(80) default NULL,
		`EndDt` datetime default NULL,
		`Duration` bigint default NULL,
		`CompleteDt` datetime default NULL,
		`PropertiesStr` text default NULL, 
		`PercentCompleted` int default NULL
	) TYPE=MyISAM;

	CREATE TABLE `ical_property` (
		`UID` bigint NOT NULL default '0',
		`Kind` smallint default NULL,
		`CompKind` smallint default NULL,
		`iCalCompId` bigint default NULL
	) TYPE=MyISAM;

	CREATE TABLE `ical_int` (
		`UID` bigint NOT NULL default '0',
		`CompKind` smallint default NULL,
		`PropKind` smallint default NULL,
		`ParamKind` smallint default NULL,
		`RefKind` smallint default NULL,
		`ValueKind` smallint default NULL,
		`iCalCompId` bigint default NULL,
		`RefId` bigint default NULL,
		`IntValue` int default NULL
	) TYPE=MyISAM;

	CREATE TABLE `ical_date` (
		`UID` bigint NOT NULL default '0',
		`CompKind` smallint default NULL,
		`PropKind` smallint default NULL,
		`ParamKind` smallint default NULL,
		`RefKind` smallint default NULL,
		`ValueKind` smallint default NULL,
		`iCalCompId` bigint default NULL,
		`RefId` bigint default NULL,
		`DateValue` datetime default NULL
	) TYPE=MyISAM;

	CREATE TABLE `ical_string` (
		`UID` bigint NOT NULL default '0',
		`CompKind` smallint default NULL,
		`PropKind` smallint default NULL,
		`ParamKind` smallint default NULL,
		`RefKind` smallint default NULL,
		`ValueKind` smallint default NULL,
		`iCalCompId` bigint default NULL,
		`RefId` bigint default NULL,
		`StrValue` varchar(80) default NULL
	) TYPE=MyISAM;

	CREATE TABLE `ical_text` (
		`UID` bigint NOT NULL default '0',
		`CompKind` smallint default NULL,
		`PropKind` smallint default NULL,
		`ParamKind` smallint default NULL,
		`RefKind` smallint default NULL,
		`ValueKind` smallint default NULL,
		`iCalCompId` bigint default NULL,
		`RefId` bigint default NULL,
		`TextValue` text default NULL
	) TYPE=MyISAM;
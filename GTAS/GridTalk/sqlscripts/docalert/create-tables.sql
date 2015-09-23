#
# 30 Apr 2003 v2.1 I1 [Neo Sok Lay] Add AttachRespDoc field
# 05 Dec 2006 GT 4.0  [Neo Sok Lay] Increase SentDocType, ResponseDocType field length to 30 chars.
#

#
# Table structure for table 'response_track_record', 'reminder_alert', 'active_track_record'
#
use userdb;

DROP TABLE IF EXISTS response_track_record;
CREATE TABLE IF NOT EXISTS response_track_record (
  UID bigint(20) NOT NULL DEFAULT '0' ,
  SentDocType varchar(30) NOT NULL DEFAULT '' ,
  SentDocIdXpath varchar(255),
  StartTrackDateXpath varchar(255),
  ResponseDocType varchar(30) NOT NULL DEFAULT '',
  ResponseDocIdXpath varchar(255),
  ReceiveResponseAlert varchar(80),
  AlertRecipientXpath varchar(255),
  Version double NOT NULL DEFAULT '1',
  AttachRespDoc tinyint(1) NOT NULL DEFAULT '1',
  PRIMARY KEY (UID),
  UNIQUE KEY sent_doctype_idx (SentDocType),
  UNIQUE KEY resp_doctype_idx (ResponseDocType)
);

DROP TABLE IF EXISTS reminder_alert;
CREATE TABLE IF NOT EXISTS reminder_alert (
  UID bigint(20) NOT NULL DEFAULT '0' ,
  TrackRecordUID bigint(20) NOT NULL DEFAULT '0' ,
  DaysToReminder int(3) NOT NULL DEFAULT '1' ,
  AlertToRaise varchar(80) NOT NULL DEFAULT '' ,
  DocRecipientXpath varchar(255),
  DocSenderXpath varchar(255),
  Version double NOT NULL DEFAULT '1',
  PRIMARY KEY (UID),
  UNIQUE KEY reminder_alert_idx (TrackRecordUID,DaysToReminder)
);


DROP TABLE IF EXISTS active_track_record;
CREATE TABLE IF NOT EXISTS active_track_record (
  UID bigint(20) NOT NULL DEFAULT '0' ,
  TrackRecordUID bigint(20) NOT NULL DEFAULT '0' ,
  DaysToReminder int(3) NOT NULL DEFAULT '0' ,
  AlarmUID bigint(20) NOT NULL DEFAULT '0' ,
  SentGridDocUID bigint(20) NOT NULL DEFAULT '0' ,
  PRIMARY KEY (UID),
  UNIQUE KEY active_track_alarm_idx (AlarmUID),
  UNIQUE KEY active_track_doc_idx (SentGridDocUID)
);


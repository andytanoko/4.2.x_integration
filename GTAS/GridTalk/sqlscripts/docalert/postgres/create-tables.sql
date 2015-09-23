SET client_encoding = 'UTF8';
SET check_function_bodies = false;
SET client_min_messages = warning;
SET search_path = userdb;


--- #### docalert ####
---
--- Table structure for table 'response_track_record'
---
DROP TABLE IF EXISTS "response_track_record";
CREATE TABLE "response_track_record" (
  "UID" BIGINT DEFAULT 0 NOT NULL ,
  "SentDocType" VARCHAR(30)  NOT NULL ,
  "SentDocIdXpath" VARCHAR(255),
  "StartTrackDateXpath" VARCHAR(255),
  "ResponseDocType" VARCHAR(30)  NOT NULL,
  "ResponseDocIdXpath" VARCHAR(255),
  "ReceiveResponseAlert" VARCHAR(80),
  "AlertRecipientXpath" VARCHAR(255),
  "Version" DECIMAL(7,5) DEFAULT 1 NOT NULL,
  "AttachRespDoc" DECIMAL(1) DEFAULT 1 NOT NULL,
  PRIMARY KEY ("UID"),
  CONSTRAINT "RESPONSE_TRACK_CON1" UNIQUE ("SentDocType"),
  CONSTRAINT "RESPONSE_TRACK_CON2" UNIQUE ("ResponseDocType")
);
ALTER TABLE "response_track_record" OWNER TO userdb;


---
--- Table structure for table 'reminder_alert'
---
DROP TABLE IF EXISTS "reminder_alert";
CREATE TABLE "reminder_alert" (
  "UID" BIGINT DEFAULT 0 NOT NULL ,
  "TrackRecordUID" BIGINT DEFAULT 0 NOT NULL ,
  "DaysToReminder" DECIMAL(3) DEFAULT 1 NOT NULL ,
  "AlertToRaise" VARCHAR(80)  NOT NULL ,
  "DocRecipientXpath" VARCHAR(255),
  "DocSenderXpath" VARCHAR(255),
  "Version" DECIMAL(7,5) DEFAULT 1 NOT NULL,
  PRIMARY KEY ("UID"),
  CONSTRAINT "REMINDER_ALERT_CON" UNIQUE ("TrackRecordUID","DaysToReminder")
);
ALTER TABLE "reminder_alert" OWNER TO userdb;

---
--- Table structure for table 'active_track_record'
---
DROP TABLE IF EXISTS "active_track_record";
CREATE TABLE "active_track_record" (
  "UID" BIGINT DEFAULT 0 NOT NULL ,
  "TrackRecordUID" BIGINT DEFAULT 0 NOT NULL ,
  "DaysToReminder" DECIMAL(3) DEFAULT 0 NOT NULL ,
  "AlarmUID" BIGINT DEFAULT 0 NOT NULL ,
  "SentGridDocUID" BIGINT DEFAULT 0 NOT NULL ,
  PRIMARY KEY ("UID"),
  CONSTRAINT "ACTIVE_TRACK_CON1" UNIQUE ("AlarmUID"),
  CONSTRAINT "ACTIVE_TRACK_CON2" UNIQUE ("SentGridDocUID")
);
ALTER TABLE "active_track_record" OWNER TO userdb;


SET client_encoding = 'UTF8';
SET check_function_bodies = false;
SET client_min_messages = warning;
SET search_path = userdb;
---
--- Table structure for table 'ical_alarm'
---
DROP TABLE IF EXISTS "ical_alarm";
CREATE TABLE "ical_alarm" (
  "UID" BIGINT DEFAULT 0 NOT NULL,
  "StartDuration" BIGINT ,
  "StartDt" TIMESTAMP ,
  "Related" DECIMAL(10) ,
  "DelayPeriod" BIGINT ,
  "RepeatCount" DECIMAL(10) ,
  "Category" VARCHAR(100) ,
  "SenderKey" VARCHAR(255) ,
  "ReceiverKey" VARCHAR(255) ,
  "Disabled" DECIMAL(1) ,
  "NextDueTime" TIMESTAMP ,
  "Count" DECIMAL(10) ,
  "ParentUid" BIGINT ,
  "ParentKind" DECIMAL(5) ,
  "RecurListStr" TEXT ,
  "IsRecurComplete" DECIMAL(1) ,
  "CurRecur" VARCHAR(80) ,
  "IsPseudoParent" DECIMAL(1) ,
  "IncludeParentStartTime" DECIMAL(1) ,
  "TaskId" VARCHAR(120) 	
);
ALTER TABLE "ical_alarm" OWNER TO userdb;

---
--- Table structure for table 'ical_event'
---
DROP TABLE IF EXISTS "ical_event";
CREATE TABLE "ical_event" (
  "UID" BIGINT DEFAULT 0 NOT NULL,
  "Kind" DECIMAL(5) ,
  "OwnerId" DECIMAL(10) ,
  "Classification" VARCHAR(80) ,
  "CreateDt" TIMESTAMP ,
  "LastModifyDt" TIMESTAMP ,
  "IsDateType" DECIMAL(1) , 
  "IsDtFloat" DECIMAL(1) ,
  "StartDt" TIMESTAMP ,
  "Priority" DECIMAL(10) ,
  "SequenceNum" DECIMAL(10) ,
  "Status" DECIMAL(10) ,
  "iCalUid" VARCHAR(80) ,
  "EndDt" TIMESTAMP ,
  "Duration" BIGINT ,
  "PropertiesStr" VARCHAR(1000) , 
  "TimeTransparency" DECIMAL(10) 
);
ALTER TABLE "ical_event" OWNER TO userdb;

---
--- Table structure for table 'ical_todo'
---
DROP TABLE IF EXISTS "ical_todo";
CREATE TABLE "ical_todo" (
  "UID" BIGINT DEFAULT 0 NOT NULL,
  "Kind" DECIMAL(5) ,
  "OwnerId" DECIMAL(10) ,
  "Classification" VARCHAR(80) ,
  "CreateDt" TIMESTAMP ,
  "LastModifyDt" TIMESTAMP ,
  "IsDateType" DECIMAL(1) ,
  "IsDtFloat" DECIMAL(1) ,
  "StartDt" TIMESTAMP ,
  "Priority" DECIMAL(10) ,
  "SequenceNum" DECIMAL(10) ,
  "Status" DECIMAL(10) ,
  "iCalUid" VARCHAR(80) ,
  "EndDt" TIMESTAMP ,
  "Duration" BIGINT ,
  "CompleteDt" TIMESTAMP ,
  "PropertiesStr" TEXT , 
  "PercentCompleted" DECIMAL(10) 
);
ALTER TABLE "ical_todo" OWNER TO userdb;

---
--- Table structure for table 'ical_property'
---
DROP TABLE IF EXISTS "ical_property";
CREATE TABLE "ical_property" (
  "UID" BIGINT DEFAULT 0 NOT NULL,
  "Kind" DECIMAL(5) ,
  "CompKind" DECIMAL(5) ,
  "iCalCompId" BIGINT 
);
ALTER TABLE "ical_property" OWNER TO userdb;

---
--- Table structure for table 'ical_int'
---
DROP TABLE IF EXISTS "ical_int";
CREATE TABLE "ical_int" (
  "UID" BIGINT DEFAULT 0 NOT NULL,
  "CompKind" DECIMAL(5) ,
  "PropKind" DECIMAL(5) ,
  "ParamKind" DECIMAL(5) ,
  "RefKind" DECIMAL(5) ,
  "ValueKind" DECIMAL(5) ,
  "iCalCompId" BIGINT ,
  "RefId" BIGINT ,
  "IntValue" DECIMAL(10) 
);
ALTER TABLE "ical_int" OWNER TO userdb;

---
--- Table structure for table 'ical_date'
---
DROP TABLE IF EXISTS "ical_date";
CREATE TABLE "ical_date" (
  "UID" BIGINT DEFAULT 0 NOT NULL,
  "CompKind" DECIMAL(5) ,
  "PropKind" DECIMAL(5) ,
  "ParamKind" DECIMAL(5) ,
  "RefKind" DECIMAL(5) ,
  "ValueKind" DECIMAL(5) ,
  "iCalCompId" BIGINT ,
  "RefId" BIGINT ,
  "DateValue" TIMESTAMP 
);
ALTER TABLE "ical_date" OWNER TO userdb;


---
--- Table structure for table 'ical_string'
---
DROP TABLE IF EXISTS "ical_string";
CREATE TABLE "ical_string" (
  "UID" BIGINT DEFAULT 0 NOT NULL,
  "CompKind" DECIMAL(5) ,
  "PropKind" DECIMAL(5) ,
  "ParamKind" DECIMAL(5) ,
  "RefKind" DECIMAL(5) ,
  "ValueKind" DECIMAL(5) ,
  "iCalCompId" BIGINT ,
  "RefId" BIGINT ,
  "StrValue" VARCHAR(80) 
);
ALTER TABLE "ical_string" OWNER TO userdb;

---
--- Table structure for table 'ical_text'
---
DROP TABLE IF EXISTS "ical_text";
CREATE TABLE "ical_text" (
  "UID" BIGINT DEFAULT 0 NOT NULL,
  "CompKind" DECIMAL(5) ,
  "PropKind" DECIMAL(5) ,
  "ParamKind" DECIMAL(5) ,
  "RefKind" DECIMAL(5) ,
  "ValueKind" DECIMAL(5) ,
  "iCalCompId" BIGINT ,
  "RefId" BIGINT ,
  "TextValue" TEXT 
);
ALTER TABLE "ical_text" OWNER TO userdb;

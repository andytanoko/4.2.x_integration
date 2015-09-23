SET client_encoding = 'UTF8';
SET check_function_bodies = false;
SET client_min_messages = warning;
SET search_path = userdb;


--- #### alert ####
---
--- Table structure for table 'action'
---
DROP TABLE IF EXISTS "action";
CREATE TABLE "action" (
  "UID" BIGINT DEFAULT 0 NOT NULL,
  "Name" VARCHAR(80)  NOT NULL,
  "Description" VARCHAR(255) ,
  "MsgUid" BIGINT ,
  "Version" DECIMAL(7,5) DEFAULT 1 NOT NULL,
  "CanDelete" DECIMAL(1) DEFAULT 1 NOT NULL,
  PRIMARY KEY ("UID"),
  CONSTRAINT "ACTION_CON" UNIQUE ("Name")
);
ALTER TABLE "action" OWNER TO userdb;


---
--- Table structure for table 'alert'
---
DROP TABLE IF EXISTS "alert";
CREATE TABLE "alert" (
  "UID" BIGINT DEFAULT 0 NOT NULL,
  "Name" VARCHAR(80)  NOT NULL,
  "AlertType" BIGINT DEFAULT 0 NOT NULL,
  "Category" BIGINT DEFAULT NULL,
  "TriggerCond" TEXT ,
  "Description" VARCHAR(255) ,
  "Version" DECIMAL(7,5) DEFAULT 1 NOT NULL,
  "CanDelete" DECIMAL(1) DEFAULT 1 NOT NULL,
  PRIMARY KEY ("UID"),
  CONSTRAINT "ALERT_CON" UNIQUE ("Name")
);
ALTER TABLE "alert" OWNER TO userdb;



---
--- Table structure for table 'alert_action'
---
DROP TABLE IF EXISTS "alert_action";
CREATE TABLE "alert_action" (
  "UID" BIGINT DEFAULT 0 NOT NULL,
  "AlertUid" BIGINT DEFAULT 0 NOT NULL,
  "ActionUid" BIGINT DEFAULT 0 NOT NULL,
  PRIMARY KEY ("UID"),
  CONSTRAINT "ALERT_ACT_CON" UNIQUE ("AlertUid","ActionUid")
);
ALTER TABLE "alert_action" OWNER TO userdb;

---
--- Table structure for table 'alert_category'
---
DROP TABLE IF EXISTS "alert_category";
CREATE TABLE "alert_category" (
  "UID" BIGINT DEFAULT 0 NOT NULL,
  "Code" VARCHAR(20) DEFAULT '0' NOT NULL,
  "Name" VARCHAR(80),
  "Description" VARCHAR(255) ,
  "Version" DECIMAL(7,5) DEFAULT 1 NOT NULL,
  "CanDelete" DECIMAL(1) DEFAULT 1 NOT NULL,
  PRIMARY KEY ("UID"),
  CONSTRAINT "ALERT_CAT_CON" UNIQUE ("Code")
);
ALTER TABLE "alert_category" OWNER TO userdb;

---
--- Table structure for table 'alert_list'
---
DROP TABLE IF EXISTS "alert_list";
CREATE TABLE "alert_list" (
  "UID" BIGINT DEFAULT 0 NOT NULL,
  "UserUid" BIGINT DEFAULT 0 NOT NULL,
  "FromUid" BIGINT ,
  "Title" VARCHAR(35) ,
  "Message" TEXT NULL ,
  "ReadStatus" DECIMAL(1) DEFAULT 0 ,
  "Date" DATE ,
  "Version" DECIMAL(7,5) DEFAULT 1 NOT NULL ,
  "CanDelete" DECIMAL(1) DEFAULT 1 NOT NULL ,
  PRIMARY KEY ("UID")
);
ALTER TABLE "alert_list" OWNER TO userdb;


---
--- Table structure for table 'message_template'
---
DROP TABLE IF EXISTS "message_template";
CREATE TABLE "message_template" (
  "UID" BIGINT DEFAULT 0 NOT NULL  ,
  "Name" VARCHAR(80)  NOT NULL  ,
  "ContentType" VARCHAR(30) ,
  "MessageType" VARCHAR(30) ,
  "FromAddr" VARCHAR(255) ,
  "ToAddr" VARCHAR(255) ,
  "CcAddr" VARCHAR(255) ,
  "Subject" VARCHAR(255) ,
  "Message" TEXT NULL ,
  "Location" VARCHAR(255) ,
  "Append" DECIMAL(1) DEFAULT 0 ,
  "Version" DECIMAL(7,5) DEFAULT 1 NOT NULL ,
  "CanDelete" DECIMAL(1) DEFAULT 1 NOT NULL ,
  "JmsDestination" BIGINT ,
  "MessageProperties" TEXT,
  PRIMARY KEY ("UID"),
  CONSTRAINT "MESSAGE_TEMPLATE_CON" UNIQUE ("Name") 
);
ALTER TABLE "message_template" OWNER TO userdb;

---
--- Table structure for table 'alert_type'
---
DROP TABLE IF EXISTS "alert_type";
CREATE TABLE "alert_type" (
  "UID" BIGINT DEFAULT 0 NOT NULL  ,
  "Name" VARCHAR(35)  NOT NULL  ,
  "Description" VARCHAR(100) ,
  "Version" DECIMAL(7,5) DEFAULT 1 NOT NULL ,
  "CanDelete" DECIMAL(1) DEFAULT 1 NOT NULL ,
  PRIMARY KEY ("UID"),
  CONSTRAINT "ALERT_TYPE_CON" UNIQUE ("Name") 
);
ALTER TABLE "alert_type" OWNER TO userdb;

---
--- Table structure for table 'jms_destination'
---
DROP TABLE IF EXISTS "jms_destination";
CREATE TABLE "jms_destination" (
  "UID" BIGINT NOT NULL,
  "Version" DECIMAL(7,5) DEFAULT 1 NOT NULL ,
  "CanDelete" DECIMAL(1) DEFAULT 1 NOT NULL ,
  "Name" VARCHAR(30) ,
  "Type" DECIMAL(1) DEFAULT 1,
  "JndiName" VARCHAR(255) ,
  "DeliveryMode" DECIMAL(1) DEFAULT 0,
  "Priority" DECIMAL(2,0) DEFAULT -1,
  "ConnectionFactoryJndi" VARCHAR(255) ,
  "ConnectionUser" VARCHAR(30) ,
  "ConnectionPassword" VARCHAR(30) ,
  "LookupProperties" TEXT ,
  "RetryInterval" DECIMAL(10),
  "MaximumRetries" DECIMAL(10),
  PRIMARY KEY  ("UID"),
  CONSTRAINT "JMS_DEST_CON" UNIQUE ("Name") 
);
ALTER TABLE "jms_destination" OWNER TO userdb;

---
--- Table structure for table 'jms_msg_record'
---
DROP TABLE IF EXISTS "jms_msg_record";
CREATE TABLE "jms_msg_record" (
  "UID" BIGINT NOT NULL,
  "Version" DECIMAL(7,5) DEFAULT 1 NOT NULL ,
  "CanDelete" DECIMAL(1) DEFAULT 1 NOT NULL ,
  "JmsDestUid" BIGINT NOT NULL,
  "AlertTimestamp" TIMESTAMP,
  "MsgData" TEXT ,
  "PermanentFailed" DECIMAL(1) DEFAULT 0,
  "AlertTimeInLong" BIGINT,
  PRIMARY KEY  ("UID")
);
ALTER TABLE "jms_msg_record" OWNER TO userdb;

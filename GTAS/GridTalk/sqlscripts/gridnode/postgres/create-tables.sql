SET client_encoding = 'UTF8';
SET check_function_bodies = false;
SET client_min_messages = warning;
SET search_path = appdb;

---
--- Table structure for table 'gncategory'
---
--- GTAS\GridTalk\sqlscripts\gridnode\create-tables.sql

DROP TABLE IF EXISTS "gncategory";
CREATE TABLE "gncategory" (
  "Code" VARCHAR(3)  NOT NULL,
  "Name" VARCHAR(50)  NOT NULL,
  "NodeUsage" VARCHAR(10)  NOT NULL,
  PRIMARY KEY ("Code"),
  CONSTRAINT "GNCATEGORY_CON" UNIQUE ("Name")
);
ALTER TABLE "gncategory" OWNER TO appdb;


SET client_encoding = 'UTF8';
SET check_function_bodies = false;
SET client_min_messages = warning;
SET search_path = userdb;
---
--- Table structure for table 'gridnode'
---
DROP TABLE IF EXISTS "gridnode";
CREATE TABLE "gridnode" (
  "UID" BIGINT DEFAULT 0 NOT NULL,
  "ID" VARCHAR(10)  NOT NULL,
  "Name" VARCHAR(80)  NOT NULL,
  "Category" CHAR(3)  NOT NULL,
  "State" DECIMAL(2,0) NOT NULL,
  "ActivationReason" VARCHAR(255),
  "DTCreated" TIMESTAMP ,
  "DTReqActivate" TIMESTAMP ,
  "DTActivated" TIMESTAMP ,
  "DTDeactivated" TIMESTAMP ,
  "CoyProfileUID" BIGINT  ,
  "Version" DECIMAL(7,5) DEFAULT 1 NOT NULL,
  PRIMARY KEY ("UID"),
  CONSTRAINT "GRIDNODE_CON" UNIQUE ("ID")
);
ALTER TABLE "gridnode" OWNER TO userdb;

---
--- Table structure for table 'connection_status'
---
DROP TABLE IF EXISTS "connection_status";
CREATE TABLE "connection_status" (
  "UID" BIGINT DEFAULT 0 NOT NULL,
  "NodeID" VARCHAR(10)  NOT NULL,
  "StatusFlag" DECIMAL(2,0) NOT NULL,
  "DTLastOnline" TIMESTAMP ,
  "DTLastOffline" TIMESTAMP ,
  "ReconnectionKey" BYTEA NULL,
  "ConnectedServerNode" VARCHAR(10) ,
  PRIMARY KEY ("UID"),
  CONSTRAINT "CONNECTION_STATUS_CON" UNIQUE ("NodeID")
);
ALTER TABLE "connection_status" OWNER TO userdb;

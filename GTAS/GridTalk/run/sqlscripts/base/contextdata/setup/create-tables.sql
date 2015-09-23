SET client_encoding = 'UTF8';
SET check_function_bodies = false;
SET client_min_messages = warning;
SET search_path = userdb;
---
--- Table structure for table 'data_stringdata'
---
DROP TABLE IF EXISTS "data_stringdata";
CREATE TABLE "data_stringdata" (
  "UID" BIGINT DEFAULT 0 NOT NULL,
  "Data" VARCHAR(100) ,
  "DataType" VARCHAR(50) ,
  PRIMARY KEY ("UID")
);
ALTER TABLE "data_stringdata" OWNER TO userdb;

---
--- Table structure for table 'data_bytedata'
---
DROP TABLE IF EXISTS "data_bytedata";
CREATE TABLE "data_bytedata" (
  "UID" BIGINT DEFAULT 0 NOT NULL,
  "Data" BYTEA NULL ,
  "DataType" VARCHAR(50) ,
  PRIMARY KEY ("UID")
);
ALTER TABLE "data_bytedata" OWNER TO userdb;

---
--- Table structure for table 'data_contextdata'
---
DROP TABLE IF EXISTS "data_contextdata";
CREATE TABLE "data_contextdata" (
  "UID" BIGINT DEFAULT 0 NOT NULL ,
  "ContextUId" BIGINT DEFAULT 0 NOT NULL,
  "ContextData" BYTEA NULL ,
  PRIMARY KEY ("UID"),
  CONSTRAINT "DATA_CONTEXT_CON" UNIQUE ("ContextUId")
);
ALTER TABLE "data_contextdata" OWNER TO userdb;
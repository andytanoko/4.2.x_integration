SET client_encoding = 'UTF8';
SET check_function_bodies = false;
SET client_min_messages = warning;
SET search_path = userdb;
---
--- Table structure for table 'code_value'
---
DROP TABLE IF EXISTS "code_value";
CREATE TABLE "code_value" (
  "UID" BIGSERIAL NOT NULL,
  "Code" VARCHAR(80) NOT NULL,
  "Description" VARCHAR(80) ,
  "EntityType" VARCHAR(255) ,
  "FieldID" DECIMAL(3) ,
  PRIMARY KEY ("UID"),
  CONSTRAINT "CODE_VALUE_CON" UNIQUE ("Code")
);
ALTER SEQUENCE "code_value_UID_seq" OWNED BY "code_value"."UID"; -- so that while we drop the column or table, the sequence will be dropped as well
ALTER TABLE "code_value" OWNER TO userdb;
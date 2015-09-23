SET client_encoding = 'UTF8';
SET check_function_bodies = false;
SET client_min_messages = warning;
SET search_path = userdb;

---
--- Table structure for table 'procedure_definition_file'
---
DROP TABLE IF EXISTS "procedure_definition_file";
CREATE TABLE "procedure_definition_file" (
  "UID" BIGINT DEFAULT 0 NOT NULL,
  "Name" VARCHAR(30) ,
  "Description" VARCHAR(30) ,
  "Type" DECIMAL(10) ,
  "FileName" VARCHAR(30) ,
  "FilePath" VARCHAR(30) ,
  "CanDelete" DECIMAL(1) DEFAULT 1 NOT NULL,
  "Version" DECIMAL(7,5) DEFAULT 1 NOT NULL,
   PRIMARY KEY ("UID"),
   CONSTRAINT "PROC_DEF_CON" UNIQUE ("Name")
);

COMMENT ON TABLE "procedure_definition_file" IS 'Data Structure for Procedure Definition File';
ALTER TABLE "procedure_definition_file" OWNER TO userdb;

---
--- Table structure for table 'user_procedure'
---
DROP TABLE IF EXISTS "user_procedure";
CREATE TABLE "user_procedure" (
  "UID" DECIMAL(20) DEFAULT 0 NOT NULL,
  "Name" VARCHAR(30) ,
  "Description" VARCHAR(80) , -- increase length from 40 to 80
  "IsSynchronous" DECIMAL(1) ,
  "ProcType" DECIMAL(10) ,
  "ProcDefFile" BIGINT DEFAULT 0 NOT NULL,
  "ProcDef" BYTEA NULL,
  "ProcParamList" BYTEA NULL,
  "ReturnDataType" DECIMAL(10) ,
  "DefAction" DECIMAL(10) ,
  "DefAlert" BIGINT ,
  "ProcReturnList" BYTEA NULL,
  "CanDelete" DECIMAL(1) DEFAULT 1 NOT NULL,
  "Version" DECIMAL(7,5) DEFAULT 1 NOT NULL,
  "GridDocField" DECIMAL(3) ,
   PRIMARY KEY ("UID"),
   CONSTRAINT "USER_PROC_CON" UNIQUE ("Name")
);
ALTER TABLE "user_procedure" OWNER TO userdb;



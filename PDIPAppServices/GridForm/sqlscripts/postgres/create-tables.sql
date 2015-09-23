SET client_encoding = 'UTF8';
SET check_function_bodies = false;
SET client_min_messages = warning;
SET search_path = userdb;

---
--- Table structure for table 'gridform_definition'
---
DROP TABLE IF EXISTS "gridform_definition";
CREATE TABLE "gridform_definition" (
  "UID" BIGINT DEFAULT 0 NOT NULL  ,
  "Name" VARCHAR(50) NOT NULL  ,
  "Filename" VARCHAR(80) NOT NULL  ,
  "TemplateUID" BIGINT DEFAULT 0 NOT NULL  ,
  "CanDelete" DECIMAL(1) DEFAULT 1 NOT NULL  ,
  "Version" DECIMAL(7,5) DEFAULT 1 NOT NULL  ,
  PRIMARY KEY ("UID"),
  CONSTRAINT "GRIDFORM_DEF_CON" UNIQUE ("Name") 
);
ALTER TABLE "gridform_definition" OWNER TO userdb;

---
--- Table structure for table 'gridform_template'
---
DROP TABLE IF EXISTS "gridform_template";
CREATE TABLE "gridform_template" (
  "UID" BIGINT DEFAULT 0 NOT NULL  ,
  "Name" VARCHAR(50) NOT NULL  ,
  "Filename" VARCHAR(80) NOT NULL  ,
  "CanDelete" DECIMAL(1) DEFAULT 1 NOT NULL  ,
  "Version" DECIMAL(7,5) DEFAULT 1 NOT NULL  ,
  PRIMARY KEY ("UID"),
  CONSTRAINT "GRIDFORM_TEMPLATE_CON" UNIQUE ("Name") 
);
ALTER TABLE "gridform_template" OWNER TO userdb;


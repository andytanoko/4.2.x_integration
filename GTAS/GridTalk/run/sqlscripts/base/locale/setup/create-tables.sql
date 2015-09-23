SET client_encoding = 'UTF8';
SET check_function_bodies = false;
SET client_min_messages = warning;
SET search_path = appdb;

---
--- Table structure for table 'country_code'
---
--- PDIPBaseServices\Locale\sqlscripts\create-tables.sql
DROP TABLE IF EXISTS "country_code";
CREATE TABLE "country_code" 
(
  "Name" VARCHAR(255)  NOT NULL,
  "NumericalCode" DECIMAL(6) DEFAULT 0 NOT NULL,
  "Alpha2Code" VARCHAR(2)  NOT NULL,
  "Alpha3Code" VARCHAR(3)  NOT NULL ,
  PRIMARY KEY ("NumericalCode"),
  CONSTRAINT "COUNTRY_CODE_CON" UNIQUE ("Alpha3Code"),
  CONSTRAINT "COUNTRY_CODE_CON2" UNIQUE ("Alpha2Code")   
);
CREATE INDEX  "COUNTRY_CODE_IDX" ON  "country_code" ("Name");
ALTER TABLE "country_code" OWNER TO appdb;

---------------------------------------------------------------------------

---
--- Table structure for table 'language_code'
---
--- PDIPBaseServices\Locale\sqlscripts\create-tables.sql
DROP TABLE IF EXISTS "language_code";
CREATE TABLE "language_code" (
  "Name" VARCHAR(255) DEFAULT '0' NOT NULL,
  "Alpha2Code" VARCHAR(2) DEFAULT '0' NOT NULL,
  "BAlpha3Code" VARCHAR(3) DEFAULT '0' NOT NULL,
  "TAlpha3Code" VARCHAR(3)  NOT NULL,
  PRIMARY KEY ("Alpha2Code"),
  CONSTRAINT "LANGUAGE_CODE_CON" UNIQUE ("TAlpha3Code"),
  CONSTRAINT "LANGUAGE_CODE_CON2" UNIQUE ("BAlpha3Code")   
);
CREATE INDEX  "LANGUAGE_CODE_IDX" ON  "language_code" ("Name");
ALTER TABLE "language_code" OWNER TO appdb;
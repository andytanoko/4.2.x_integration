--- --------------------------------------------------------------------
--- This script includes all the CREATE queries for all tables in APPDB
--- --------------------------------------------------------------------

CONNECT APPDB/gridnode;

ALTER SESSION SET NLS_DATE_FORMAT = 'YYYY-MM-DD HH24:MI:SS';

---------------------------------------------------------------------------

---
--- TABLE STRUCTURE FOR TABLE 'entitymetainfo'
---
CREATE TABLE "entitymetainfo" 
(
  "ObjectName" VARCHAR2(80) ,
  "EntityName" VARCHAR2(80) ,
  "SqlName" VARCHAR2(40),
  PRIMARY KEY ("ObjectName")
);


---------------------------------------------------------------------------

---
--- TABLE STRUCTURE FOR TABLE 'fieldmetainfo'
---
CREATE TABLE  "fieldmetainfo" 
(
  "UID" NUMBER(19) NOT NULL ENABLE, 
  "ObjectName" VARCHAR2(80) , 
  "FieldName" VARCHAR2(40) , 
  "SqlName" VARCHAR2(40), 
  "ValueClass" VARCHAR2(80) , 
  "EntityObjectName" VARCHAR2(255) , 
  "Label" VARCHAR2(255), 
  "Length" NUMBER(10) DEFAULT 0, 
  "Proxy" NUMBER(1) DEFAULT 0, 
  "Mandatory" NUMBER(1) DEFAULT 0, 
  "Editable" NUMBER(1) DEFAULT 1, 
  "Displayable" NUMBER(1) DEFAULT 1, 
  "OqlName" VARCHAR2(40), 
  "Sequence" NUMBER(3) DEFAULT 999, 
  "Presentation" VARCHAR2(1000), 
  "Constraints" VARCHAR2(1000), 
  PRIMARY KEY ("UID") ENABLE, 
  CONSTRAINT "FIELDMETAINFO_CON" UNIQUE ("EntityObjectName", "FieldName") ENABLE
);

CREATE SEQUENCE FIELDMETAINFO_SEQ 
START WITH 1 
INCREMENT BY 1 
NOMAXVALUE; 

CREATE TRIGGER FIELDMETAINFO_TRIGGER 
BEFORE INSERT ON "fieldmetainfo"
REFERENCING NEW AS NEW OLD AS OLD 
FOR EACH ROW
BEGIN
     SELECT FIELDMETAINFO_SEQ.NEXTVAL INTO :NEW."UID" FROM DUAL;
END;
/


---------------------------------------------------------------------------

---
--- Table structure for table 'referencenum'
---
CREATE TABLE "referencenum" 
(
  "RefName" VARCHAR2(50)  ,
  "LastRefNum" NUMBER(19) DEFAULT 0 ,
  "MaxRefNum" NUMBER(19) DEFAULT -1 ,
  PRIMARY KEY ("RefName")
);


---------------------------------------------------------------------------

---
--- Table structure for table 'country_code'
---
CREATE TABLE "country_code" 
(
  "Name" VARCHAR2(255)  NOT NULL ENABLE ,
  "NumericalCode" NUMBER(5) DEFAULT 0 NOT NULL ENABLE ,
  "Alpha2Code" CHAR(2)  NOT NULL ENABLE ,
  "Alpha3Code" CHAR(3)  NOT NULL ENABLE ,
  PRIMARY KEY ("NumericalCode"),
  CONSTRAINT "COUNTRY_CODE_CON" UNIQUE ("Alpha3Code") ENABLE,
  CONSTRAINT "COUNTRY_CODE_CON2" UNIQUE ("Alpha2Code") ENABLE   
);
CREATE INDEX  "COUNTRY_CODE_IDX" ON  "country_code" ("Name");


---------------------------------------------------------------------------

---
--- Table structure for table 'language_code'
---
CREATE TABLE "language_code" (
  "Name" VARCHAR2(255) DEFAULT '0' NOT NULL ENABLE ,
  "Alpha2Code" CHAR(2) DEFAULT '0' NOT NULL ENABLE ,
  "BAlpha3Code" CHAR(3) DEFAULT '0' NOT NULL ENABLE ,
  "TAlpha3Code" CHAR(3)  NOT NULL ENABLE ,
  PRIMARY KEY ("Alpha2Code"),
  CONSTRAINT "LANGUAGE_CODE_CON" UNIQUE ("TAlpha3Code") ENABLE,
  CONSTRAINT "LANGUAGE_CODE_CON2" UNIQUE ("BAlpha3Code") ENABLE   
);
CREATE INDEX  "LANGUAGE_CODE_IDX" ON  "language_code" ("Name");


---------------------------------------------------------------------------

---
--- Table structure for table 'gncategory'
---
CREATE TABLE "gncategory" (
  "Code" CHAR(3)  NOT NULL ENABLE ,
  "Name" VARCHAR2(50)  NOT NULL ENABLE ,
  "NodeUsage" VARCHAR2(10)  NOT NULL ENABLE,
  PRIMARY KEY ("Code"),
  CONSTRAINT "GNCATEGORY_CON" UNIQUE ("Name") ENABLE
);

COMMIT;
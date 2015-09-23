--- ------------------------------------------------------------------------
--- This script includes all the CREATE queries for all tables in ARCHIVEDB
--- ------------------------------------------------------------------------

CONNECT ARCHIVEDB/gridnode;

ALTER SESSION SET NLS_DATE_FORMAT = 'YYYY-MM-DD HH24:MI:SS';

---------------------------------------------------------------------------
---
--- Table structure for table 'document_meta_info'
---
CREATE TABLE "document_meta_info" 
(
  "UID" NUMBER(19) NOT NULL ENABLE,
  "GdocID" NUMBER(19) ,
  "Folder" VARCHAR2(10) ,
  "ProcessInstanceID" VARCHAR2(255) ,
  "OriginatorID" VARCHAR2(15) ,
  "PartnerDuns" VARCHAR2(20) ,
  "PartnerID" VARCHAR2(20) ,
  "PartnerName" VARCHAR2(20) ,
  "DocType" VARCHAR2(30) ,
  "Filename" VARCHAR2(255) DEFAULT NULL,
  "GdocFilename" VARCHAR2(255) DEFAULT NULL,
  "UDocFilename" VARCHAR2(255) DEFAULT NULL,
  "ReceiptAuditFilename" VARCHAR2(200) DEFAULT NULL,
  "AttachmentFilenames" VARCHAR2(255) DEFAULT NULL,
  "IsContainAttachment" NUMBER(1) DEFAULT 0,
  "IsOriginalDoc" NUMBER(1) DEFAULT 0,
  "IsArchivedByPI" NUMBER(1) DEFAULT 0,
  "DocNumber" VARCHAR2(80) ,
  "DocDateGenerated" VARCHAR2(25) DEFAULT NULL,
  "DateTimeSendStart" DATE ,
  "DateTimeSendEnd" DATE ,
  "DateTimeReceiveStart" DATE ,
  "DateTimeReceiveEnd" DATE ,
  "DateTimeCreate" DATE ,
  "DateTimeImport" DATE ,
  "DateTimeExport" DATE ,
  "Process_DefID" VARCHAR2(80) ,
  "FilePath" VARCHAR2(255) ,
  "SenderCert" VARCHAR2(50) ,
  "ReceiverCert" VARCHAR2(50) ,
  "RNIFVersion" VARCHAR2(80) ,
  "ProcessInstanceInfoUID" NUMBER(19,0) DEFAULT 0,
  "Remark" VARCHAR2(255) DEFAULT NULL,
  "DocTransStatus" VARCHAR2(255) DEFAULT NULL,
  "UserTrackingID" VARCHAR2(255) DEFAULT NULL,
  "CanDelete" NUMBER(1) DEFAULT 1 NOT NULL ENABLE,
  "Version" NUMBER(7,5) DEFAULT 1 NOT NULL ENABLE,
  PRIMARY KEY ("UID"),
  CONSTRAINT "DOC_METAINFO_CON" UNIQUE ("GdocID","Folder") ENABLE
);

---INDEX for document_meta_info
CREATE INDEX doc_metainfo_search1 on "document_meta_info" ("DocType", "PartnerName", "DateTimeSendStart");
CREATE INDEX doc_metainfo_search2 on "document_meta_info" ("DocType", "PartnerName", "DateTimeReceiveEnd");
CREATE INDEX doc_metainfo_search3 on "document_meta_info" ("DocNumber");
CREATE INDEX doc_metainfo_search4 on "document_meta_info" ("PartnerName");
CREATE INDEX doc_metainfo_search5 on "document_meta_info" ("DocType", "PartnerName", "DocDateGenerated");
CREATE INDEX doc_metainfo_search6 on "document_meta_info" ("DocType", "PartnerName", "DateTimeCreate");
CREATE INDEX doc_metainfo_search7 on "document_meta_info" ("ProcessInstanceID", "OriginatorID", "ProcessInstanceInfoUID");

CREATE SEQUENCE DOC_METAINFO_SEQ 
START WITH 1 
INCREMENT BY 1 
NOMAXVALUE; 

CREATE TRIGGER DOC_METAINFO_TRIGGER 
BEFORE INSERT ON "document_meta_info"
REFERENCING NEW AS NEW OLD AS OLD 
FOR EACH ROW
BEGIN
     SELECT DOC_METAINFO_SEQ.NEXTVAL INTO :NEW."UID" FROM DUAL;
END;
/


---------------------------------------------------------------------------
---
--- Table structure for table 'process_instance_meta_info'
---
CREATE TABLE "process_instance_meta_info" (
  "UID" NUMBER(19) NOT NULL,
  "CanDelete" NUMBER(1)  DEFAULT 1 NOT NULL ENABLE ,
  "Version" NUMBER(7,5)  DEFAULT 1 NOT NULL ENABLE ,
  "ProcessInstanceID" VARCHAR2(255) ,
  "ProcessState" VARCHAR2(20) ,
  "ProcessStartDate" DATE ,
  "ProcessEndDate" DATE ,
  "PartnerDuns" VARCHAR2(20) ,
  "ProcessDef" VARCHAR2(80) ,
  "RoleType" VARCHAR2(20) ,
  "PartnerID" VARCHAR2(20) ,
  "PartnerName" VARCHAR2(20) ,
  "DocNumber" VARCHAR2(80) ,
  "DocDateGenerated" VARCHAR2(25) DEFAULT NULL ,
  "OriginatorID" VARCHAR2(20) ,
  "Remark" VARCHAR2(255) DEFAULT NULL,
  "UserTrackingID" VARCHAR2(255) DEFAULT NULL,
  "FailedReason" NUMBER(2,0) DEFAULT NULL,
  "DetailReason" VARCHAR2(250) DEFAULT NULL,
  "RetryNumber" NUMBER(10) DEFAULT NULL,
  PRIMARY KEY ("UID"),
  CONSTRAINT "PROC_INST_METAINFO_CON" UNIQUE ("ProcessInstanceID","OriginatorID","ProcessStartDate") ENABLE
);

---index for process_instance_meta_info
CREATE INDEX proc_inst_metainfo_search1 on "process_instance_meta_info" ("DocNumber");
CREATE INDEX proc_inst_metainfo_search2 on "process_instance_meta_info" ("PartnerID");
CREATE INDEX proc_inst_metainfo_search3 on "process_instance_meta_info" ("DocDateGenerated");
CREATE INDEX proc_inst_metainfo_search4 on "process_instance_meta_info" ("ProcessDef", "DocDateGenerated");
CREATE INDEX proc_inst_metainfo_search5 on "process_instance_meta_info" ("ProcessDef", "ProcessState", "DocDateGenerated");
CREATE INDEX proc_inst_metainfo_search6 on "process_instance_meta_info" ("PartnerName");
CREATE INDEX proc_inst_metainfo_search7 on "process_instance_meta_info" ("ProcessState");
CREATE INDEX proc_inst_metainfo_search8 on "process_instance_meta_info" ("ProcessInstanceID", "OriginatorID");

CREATE SEQUENCE PROC_INST_METAINFO_SEQ 
START WITH 1 
INCREMENT BY 1 
NOMAXVALUE; 

CREATE TRIGGER PROC_INST_METAINFO_TRIGGER 
BEFORE INSERT ON "process_instance_meta_info"
REFERENCING NEW AS NEW OLD AS OLD 
FOR EACH ROW
BEGIN
     SELECT PROC_INST_METAINFO_SEQ.NEXTVAL INTO :NEW."UID" FROM DUAL;
END;
/

COMMIT;
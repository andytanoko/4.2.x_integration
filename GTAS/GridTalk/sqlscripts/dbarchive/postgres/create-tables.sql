--- ------------------------------------------------------------------------
--- This script includes all the CREATE queries for all tables in ARCHIVEDB and userdb
--- ------------------------------------------------------------------------
SET client_encoding = 'UTF8';
SET check_function_bodies = false;
SET client_min_messages = warning;
SET search_path = archivedb;

---------------------------------------------------------------------------
---
--- Table structure for table 'document_meta_info'
---
DROP TABLE IF EXISTS "document_meta_info";
CREATE TABLE "document_meta_info" 
(
  "UID" BIGINT NOT NULL,
  "GdocID" BIGINT ,
  "Folder" VARCHAR(10) ,
  "ProcessInstanceID" VARCHAR(255) ,
  "OriginatorID" VARCHAR(15) ,
  "PartnerDuns" VARCHAR(20) ,
  "PartnerID" VARCHAR(20) ,
  "PartnerName" VARCHAR(20) ,
  "DocType" VARCHAR(30) ,
  "Filename" VARCHAR(255) DEFAULT NULL,
  "GdocFilename" VARCHAR(255) DEFAULT NULL,
  "UDocFilename" VARCHAR(255) DEFAULT NULL,
  "ReceiptAuditFilename" VARCHAR(200) DEFAULT NULL,
  "AttachmentFilenames" VARCHAR(255) DEFAULT NULL,
  "IsContainAttachment" DECIMAL(1) DEFAULT 0,
  "IsOriginalDoc" DECIMAL(1) DEFAULT 0,
  "IsArchivedByPI" DECIMAL(1) DEFAULT 0,
  "DocNumber" VARCHAR(80) ,
  "DocDateGenerated" VARCHAR(25) DEFAULT NULL,
  "DateTimeSendStart" TIMESTAMP(3) ,
  "DateTimeSendEnd"TIMESTAMP(3) ,
  "DateTimeReceiveStart" TIMESTAMP(3) ,
  "DateTimeReceiveEnd" TIMESTAMP(3) ,
  "DateTimeCreate" TIMESTAMP(3) ,
  "DateTimeImport" TIMESTAMP(3) ,
  "DateTimeExport" TIMESTAMP(3) ,
  "Process_DefID" VARCHAR(80) ,
  "FilePath" VARCHAR(255) ,
  "SenderCert" VARCHAR(50) ,
  "ReceiverCert" VARCHAR(50) ,
  "RNIFVersion" VARCHAR(80) ,
  "ProcessInstanceInfoUID" BIGINT DEFAULT 0,
  "Remark" VARCHAR(255) DEFAULT NULL,
  "DocTransStatus" TEXT DEFAULT NULL,
  "UserTrackingID" VARCHAR(255) DEFAULT NULL,
  "CanDelete" DECIMAL(1) DEFAULT 1 NOT NULL,
  "Version" DECIMAL(7,5) DEFAULT 1 NOT NULL,
  PRIMARY KEY ("UID"),
  UNIQUE ("GdocID","Folder")
);
ALTER TABLE "document_meta_info" OWNER TO archivedb;

---INDEX for document_meta_info
CREATE INDEX "doc_metainfo_search1" on "document_meta_info" ("DocType", "PartnerName", "DateTimeSendStart");
CREATE INDEX "doc_metainfo_search2" on "document_meta_info" ("DocType", "PartnerName", "DateTimeReceiveEnd");
CREATE INDEX "doc_metainfo_search3" on "document_meta_info" ("DocNumber");
CREATE INDEX "doc_metainfo_search4" on "document_meta_info" ("PartnerName");
CREATE INDEX "doc_metainfo_search5" on "document_meta_info" ("DocType", "PartnerName", "DocDateGenerated");
CREATE INDEX "doc_metainfo_search6" on "document_meta_info" ("DocType", "PartnerName", "DateTimeCreate");
CREATE INDEX "doc_metainfo_search7" on "document_meta_info" ("ProcessInstanceID", "OriginatorID", "ProcessInstanceInfoUID");


---------------------------------------------------------------------------
---
--- Table structure for table 'process_instance_meta_info'
---
DROP TABLE IF EXISTS "process_instance_meta_info";
CREATE TABLE "process_instance_meta_info" (
  "UID" BIGINT NOT NULL,
  "CanDelete" DECIMAL(1)  DEFAULT 1 NOT NULL,
  "Version" DECIMAL(7,5)  DEFAULT 1 NOT NULL,
  "ProcessInstanceID" VARCHAR(255) ,
  "ProcessState" VARCHAR(20) ,
  "ProcessStartDate" TIMESTAMP(3) ,
  "ProcessEndDate" TIMESTAMP(3) ,
  "PartnerDuns" VARCHAR(20) ,
  "ProcessDef" VARCHAR(80) ,
  "RoleType" VARCHAR(20) ,
  "PartnerID" VARCHAR(20) ,
  "PartnerName" VARCHAR(20) ,
  "DocNumber" VARCHAR(80) ,
  "DocDateGenerated" VARCHAR(25) DEFAULT NULL ,
  "OriginatorID" VARCHAR(20) ,
  "Remark" VARCHAR(255) DEFAULT NULL,
  "UserTrackingID" VARCHAR(255) DEFAULT NULL,
  "FailedReason" DECIMAL(2,0) DEFAULT NULL,
  "DetailReason" VARCHAR(250) DEFAULT NULL,
  "RetryNumber" DECIMAL(10) DEFAULT NULL,
  PRIMARY KEY ("UID"),
  UNIQUE ("ProcessInstanceID","OriginatorID","ProcessStartDate")
);
ALTER TABLE "process_instance_meta_info" OWNER TO archivedb;

---index for process_instance_meta_info
CREATE INDEX "proc_inst_metainfo_search1" on "process_instance_meta_info" ("DocNumber");
CREATE INDEX "proc_inst_metainfo_search2" on "process_instance_meta_info" ("PartnerID");
CREATE INDEX "proc_inst_metainfo_search3" on "process_instance_meta_info" ("DocDateGenerated");
CREATE INDEX "proc_inst_metainfo_search4" on "process_instance_meta_info" ("ProcessDef", "DocDateGenerated");
CREATE INDEX "proc_inst_metainfo_search5" on "process_instance_meta_info" ("ProcessDef", "ProcessState", "DocDateGenerated");
CREATE INDEX "proc_inst_metainfo_search6" on "process_instance_meta_info" ("PartnerName");
CREATE INDEX "proc_inst_metainfo_search7" on "process_instance_meta_info" ("ProcessState");
CREATE INDEX "proc_inst_metainfo_search8" on "process_instance_meta_info" ("ProcessInstanceID", "OriginatorID");




SET client_encoding = 'UTF8';
SET check_function_bodies = false;
SET client_min_messages = warning;
SET search_path = userdb;

--
-- TOC entry 1951 (class 1259 OID 28735)
-- Dependencies: 2219 2220 2221 9
-- Name: archive_task_meta_info; Type: TABLE; Schema: userdb; Owner: userdb; Tablespace: 
--

DROP TABLE IF EXISTS "archive_task_meta_info" CASCADE;
CREATE TABLE "archive_task_meta_info" (
    "UID" bigint NOT NULL,
    "ArchiveID" character varying(30),
    "ArchiveType" character varying(30),
    "ArchiveDescription" character varying(255),
    "FromStartDate" character varying(10),
    "ToStartDate" character varying(10),
    "FromStartTime" character varying(5),
    "ToStartTime" character varying(5),
    "ProcessDefNameList" character varying(255),
    "IncludeIncomplete" numeric(1,0) DEFAULT (1)::numeric NOT NULL,
    "FolderList" character varying(255),
    "DocumentTypeList" character varying(255),
    "EnableSearchArchived" numeric(1,0) DEFAULT (1)::numeric NOT NULL,
    "EnableRestoreArchived" numeric(1,0) DEFAULT (1)::numeric NOT NULL,
    "PartnerIDForArchive" character varying(255),
    "CustomerIDForArchive" character varying(255),
	"IsArchiveFrequencyOnce" numeric(1,0) DEFAULT (0),
	"ClientTz" character varying(50),
	"ArchiveRecordOlderThan" numeric(10),
	"CanDelete" DECIMAL(1) DEFAULT 1 NOT NULL ,
    "Version" DECIMAL(7,5) DEFAULT 1 NOT NULL ,
	PRIMARY KEY ("UID"),
	CONSTRAINT "archive_task_meta_info_archiveid_key" UNIQUE ("ArchiveID")
);
ALTER TABLE "archive_task_meta_info" OWNER TO userdb;


CREATE INDEX archive_task_metainfo_search1 ON "archive_task_meta_info" ("ArchiveID", "ArchiveType");


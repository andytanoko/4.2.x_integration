---20090818    Tam Wei Xiang      [GT4.2.1]    Added db index from GT4.1.4
---20101008    Tam Wei xiang      [GT4.2.3]    Added column "IsRead" for table "grid_document"


SET client_encoding = 'UTF8';
SET check_function_bodies = false;
SET client_min_messages = warning;
SET search_path = userdb;

---
--- Table structure for table 'document_type'
---
DROP TABLE IF EXISTS "document_type";
CREATE TABLE "document_type" (
  "UID" BIGINT DEFAULT 0 NOT NULL ,
  "DocType" VARCHAR(30)  NOT NULL ,
  "Description" VARCHAR(80)  NOT NULL ,
  "CanDelete" DECIMAL(1) DEFAULT 1 NOT NULL ,
  "Version" DECIMAL(7,5) DEFAULT 1 NOT NULL,
  PRIMARY KEY ("UID"),
  CONSTRAINT "DOCUMENT_TYPE_CON" UNIQUE ("DocType")
);
ALTER TABLE "document_type" OWNER TO userdb;

---
--- Table structure for table 'file_type'
---
DROP TABLE IF EXISTS "file_type";
CREATE TABLE "file_type" (
  "UID" BIGINT DEFAULT 0 NOT NULL ,
  "FileType" VARCHAR(10)  NOT NULL ,
  "Description" VARCHAR(80)  NOT NULL ,
  "ProgramName" VARCHAR(120)  ,
  "ProgramPath" VARCHAR(120)  ,
  "Parameters" VARCHAR(120)  ,
  "WorkingDirectory" VARCHAR(120)  ,
  "CanDelete" DECIMAL(1) DEFAULT 1 NOT NULL ,
  "Version" DECIMAL(7,5) DEFAULT 1 NOT NULL,
  PRIMARY KEY ("UID"),
  CONSTRAINT "FILE_TYPE_CON" UNIQUE ("FileType")
);
ALTER TABLE "file_type" OWNER TO userdb;


---
--- Table structure for table 'grid_document'
---
DROP TABLE IF EXISTS "grid_document";
CREATE TABLE "grid_document" (
  "UID" BIGINT DEFAULT 0 NOT NULL ,
  "CanDelete" DECIMAL(1) DEFAULT 1 NOT NULL ,
  "Version" DECIMAL(7,5) DEFAULT 1 NOT NULL,
  "GdocId" BIGINT DEFAULT 0 NOT NULL,
  "RefGdocId" BIGINT,
  "GdocFilename" VARCHAR(80),
  "UdocNum" VARCHAR(80),
  "RefUdocNum" VARCHAR(80),
  "UdocFilename" VARCHAR(255),
  "RefUdocFilename" VARCHAR(255),
  "UdocVersion" DECIMAL(10) DEFAULT 1,
  "UdocDocType" VARCHAR(30),
  "UdocFileType" VARCHAR(10),
  "Exported" DECIMAL(1) DEFAULT 0 ,
  "ViewAckReq" DECIMAL(1) DEFAULT 0 ,
  "ExportAckReq" DECIMAL(1) DEFAULT 0 ,
  "ReceiveAckReq" DECIMAL(1) DEFAULT 0 ,
  "Viewed" DECIMAL(1) DEFAULT 0 ,
  "Sent" DECIMAL(1) DEFAULT 0 ,
  "LocalPending" DECIMAL(1) DEFAULT 0 ,
  "Expired" DECIMAL(1) DEFAULT 0 ,
  "RecAckProcessed" DECIMAL(1) DEFAULT 0 ,
  "EncryptionLevel" DECIMAL(10) DEFAULT 64 ,
  "Folder" VARCHAR(10),
  "CreateBy" VARCHAR(15),
  "RecipientNodeId" DECIMAL(9,0),
  "RecipientPartnerId" VARCHAR(20),
  "RecipientPartnerType" VARCHAR(3),
  "RecipientPartnerGroup" VARCHAR(3),
  "RecipientBizEntityId" VARCHAR(4),
  "RecipientBizEntityUuid" VARCHAR(50),
  "RecipientRegistryQueryUrl" VARCHAR(255),
  "RecipientPartnerFunction" VARCHAR(30),
  "RecipientGdocId" BIGINT,
  "SenderNodeId" DECIMAL(9,0),
  "SenderGdocId" BIGINT,
  "SenderUserId" VARCHAR(15),
  "SenderUserName" VARCHAR(50),
  "SenderBizEntityId" VARCHAR(4),
  "SenderBizEntityUuid" VARCHAR(50),
  "SenderRegistryQueryUrl" VARCHAR(255),
  "SenderRoute" VARCHAR(20),
  "SenderPartnerId" VARCHAR(20),
  "SenderPartnerType" VARCHAR(3),
  "SenderPartnerGroup" VARCHAR(3),
  "DateTimeImport" TIMESTAMP,
  "DateTimeSendEnd" TIMESTAMP,
  "DateTimeReceiveEnd" TIMESTAMP,
  "DateTimeExport" TIMESTAMP,
  "DateTimeCreate" TIMESTAMP,
  "DateTimeTransComplete" TIMESTAMP,
  "DateTimeReceiveStart" TIMESTAMP,
  "DateTimeView" TIMESTAMP,
  "DateTimeSendStart" TIMESTAMP,
  "DateTimeRecipientView" TIMESTAMP,
  "DateTimeRecipientExport" TIMESTAMP,
  "PortUid" BIGINT,
  "SrcFolder" VARCHAR(10),
  "NotifyUserEmail" VARCHAR(50),
  "RecipientPartnerName" VARCHAR(20),
  "SenderPartnerName" VARCHAR(20),
  "PortName" VARCHAR(15),
  "RnProfileUid" BIGINT,
  "ProcessDefId" VARCHAR(80),
  "IsRequest" DECIMAL(1) DEFAULT 0,
  "HasAttachment" DECIMAL(1) DEFAULT 0,
  "AttachmentLinkUpdated" DECIMAL(1) DEFAULT 1,
  "Attachments" VARCHAR(200),
  "TriggerType" DECIMAL(2,0),
  "UniqueDocIdentifier" VARCHAR(80),
  "UdocFullPath" VARCHAR(500)  NOT NULL,
  "ExportedUdocFullPath" VARCHAR(500),
  "Rejected" DECIMAL(1) DEFAULT 0 ,
  "Custom1" VARCHAR(255),
  "Custom2" VARCHAR(255),
  "Custom3" VARCHAR(255),
  "Custom4" VARCHAR(255),
  "Custom5" VARCHAR(255),
  "Custom6" VARCHAR(255),
  "Custom7" VARCHAR(255),
  "Custom8" VARCHAR(255),
  "Custom9" VARCHAR(255),
  "Custom10" VARCHAR(255),
  "ProcessInstanceUid" BIGINT,
  "ProcessInstanceID" VARCHAR(255) default NULL,
  "UserTrackingID" VARCHAR(255) default NULL,
  "DocTransStatus" TEXT,
  "MessageDigest" VARCHAR(80),
  "AuditFileName" VARCHAR(200),
  "ReceiptAuditFileName" VARCHAR(200),
  "DocDateGen" VARCHAR(25) ,
  "SenderCert" BIGINT ,
  "ReceiverCert" BIGINT ,
  "OriginalDoc" DECIMAL(1) DEFAULT 0,
  "TracingID" VARCHAR(36) ,
  "IsRead" boolean default FALSE,
  PRIMARY KEY ("UID")
);

DROP index IF EXISTS "GRID_DOCUMENT_IDX1";
DROP index IF EXISTS "GRID_DOCUMENT_IDX2";
DROP index IF EXISTS "GRID_DOCUMENT_IDX3";
DROP index IF EXISTS "GRID_DOCUMENT_IDX4";

CREATE INDEX "GRID_DOCUMENT_IDX1" ON "grid_document" ("DateTimeCreate", "Folder", "UdocDocType"); 
CREATE INDEX "GRID_DOCUMENT_IDX2" ON "grid_document" ("DateTimeCreate", "Folder", "UdocDocType", "SenderPartnerId", "RecipientPartnerId");
CREATE INDEX "GRID_DOCUMENT_IDX3" ON "grid_document" ("DateTimeCreate", "Folder", "UdocDocType", "SenderBizEntityId", "RecipientBizEntityId");

CREATE INDEX "GRID_DOCUMENT_IDX4" ON "grid_document" ("Folder", "RecipientPartnerId", "IsRead");

ALTER TABLE "grid_document" OWNER TO userdb;

---
--- Table structure for table 'attachment'
---
DROP TABLE IF EXISTS "attachment";
CREATE TABLE "attachment" (
  "UID" BIGINT DEFAULT 0 NOT NULL ,
  "CanDelete" DECIMAL(1) DEFAULT 1 NOT NULL ,
  "Version" DECIMAL(7,5) DEFAULT 1 NOT NULL,
  "PartnerId" VARCHAR(20),
  "OriginalUid" BIGINT,
  "Filename" VARCHAR(255)  NOT NULL ,
  "OriginalFilename" VARCHAR(255)  NOT NULL ,
  "IsOutgoing" DECIMAL(1) DEFAULT 1 NOT NULL,
  PRIMARY KEY ("UID")
);
ALTER TABLE "attachment" OWNER TO userdb;

---
--- Table structure for table 'attachmentRegistry'
---
DROP TABLE IF EXISTS "attachmentRegistry";
CREATE TABLE "attachmentRegistry" (
  "UID" BIGINT DEFAULT 0 NOT NULL ,
  "CanDelete" DECIMAL(1) DEFAULT 1 NOT NULL ,
  "Version" DECIMAL(7,5) DEFAULT 1 NOT NULL,
  "PartnerId" VARCHAR(20)  NOT NULL ,
  "AttachmentUid" BIGINT DEFAULT -1 NOT NULL ,
  "DateProcessed" TIMESTAMP,
  PRIMARY KEY ("UID")
);
ALTER TABLE "attachmentRegistry" OWNER TO userdb;

DROP TABLE IF EXISTS "as2_doc_type_mapping"; 
CREATE TABLE "as2_doc_type_mapping" (
	"UID" BIGINT DEFAULT 0 NOT NULL ,
	"AS2DocType" varchar(30) NOT NULL DEFAULT '' ,
	"DocType" varchar(30) NOT NULL DEFAULT '' ,
    "PartnerId" varchar(15) NOT NULL DEFAULT '' ,
    "CanDelete" DECIMAL(1) DEFAULT 1 NOT NULL ,
    "Version" DECIMAL(7,5) DEFAULT 1 NOT NULL,
	PRIMARY KEY ("UID") ,
	CONSTRAINT "AS2_DOC_TYPE_MAPPING_CON" UNIQUE ("AS2DocType", "PartnerId")
);
ALTER TABLE "as2_doc_type_mapping" OWNER TO userdb;


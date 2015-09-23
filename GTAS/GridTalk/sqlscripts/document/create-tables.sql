# Change log
# 01 Oct 2003 GT 2.2 I1   [Neo Sok Lay] Add fields in grid_document table: SenderBizEntityUuid,SenderRegistryQueryUrl,RecipientBizEntityUuid,RecipientRegistryQueryUrl
# 05 Mar 2004 GT 2.2 I5   [Neo Sok Lay] Increase Attachment filename length to 255.
# 01 Jul 2004 GT 2.3.2a   [Neo Sok Lay] Expand udocfilename fields to 255chars, udocfullpath fields to text.
# 08 Feb 2006 GT 4.0      [Neo Sok Lay] Expand RecipientPartnerFunction to 30 chars.
# 24 Nov 2005 GT 2.4.7    [Tam Wei Xiang] Add in DocDateGen, OwnCert, TPCert, OriginalDoc fields to Gdoc
# 20 OCT 2006 GT 4.0_VAN  [Tam Wei Xiang] Add in TracingID to Gdoc.
# 20 Sep 2006 GT 4.0    [Tam Wei Xiang] change OwnCert, TPCert to SenderCert, ReceiverCert
# 05 Dec 2006 GT 4.0    [Neo Sok Lay] Expand DocType, UdocDocType to 30 chars.
# 29 FEB 2008 GT 4.0.2.3[Tam Wei Xiang] Expand ProcessInstanceID
#
use userdb;

DROP TABLE IF EXISTS document_type;
CREATE TABLE IF NOT EXISTS document_type (
  UID bigint(20) NOT NULL DEFAULT '0' ,
  DocType varchar(30) NOT NULL DEFAULT '' ,
  Description varchar(80) NOT NULL DEFAULT '' ,
  CanDelete tinyint(1) NOT NULL DEFAULT '1' ,
  Version double NOT NULL DEFAULT '1',
  PRIMARY KEY (UID),
  UNIQUE KEY ID (DocType)
);

DROP TABLE IF EXISTS file_type;
CREATE TABLE IF NOT EXISTS file_type (
  UID bigint(20) NOT NULL DEFAULT '0' ,
  FileType varchar(10) NOT NULL DEFAULT '' ,
  Description varchar(80) NOT NULL DEFAULT '' ,
  ProgramName varchar(120) DEFAULT '' ,
  ProgramPath varchar(120) DEFAULT '' ,
  Parameters varchar(120) DEFAULT '' ,
  WorkingDirectory varchar(120) DEFAULT '' ,
  CanDelete tinyint(1) NOT NULL DEFAULT '1' ,
  Version double NOT NULL DEFAULT '1',
  PRIMARY KEY (UID),
  UNIQUE KEY ID (FileType)
);

DROP TABLE IF EXISTS grid_document;
CREATE TABLE IF NOT EXISTS grid_document (
  UID bigint(20) NOT NULL DEFAULT '0' ,
  CanDelete tinyint(1) NOT NULL DEFAULT '1' ,
  Version double NOT NULL DEFAULT '1',
  GdocId bigint(20) NOT NULL DEFAULT '0',
  RefGdocId bigint(20),
  GdocFilename varchar(80),
  UdocNum varchar(20),
  RefUdocNum varchar(20),
  UdocFilename varchar(255),
  RefUdocFilename varchar(255),
  UdocVersion int(10) DEFAULT '1',
  UdocDocType varchar(30),
  UdocFileType varchar(10),
  Exported tinyint(1) DEFAULT '0' ,
  ViewAckReq tinyint(1) DEFAULT '0' ,
  ExportAckReq tinyint(1) DEFAULT '0' ,
  ReceiveAckReq tinyint(1) DEFAULT '0' ,
  Viewed tinyint(1) DEFAULT '0' ,
  Sent tinyint(1) DEFAULT '0' ,
  LocalPending tinyint(1) DEFAULT '0' ,
  Expired tinyint(1) DEFAULT '0' ,
  RecAckProcessed tinyint(1) DEFAULT '0' ,
  EncryptionLevel int(11) DEFAULT '64' ,
  Folder varchar(10),
  CreateBy varchar(15),
  RecipientNodeId int(9),
  RecipientPartnerId varchar(15),
  RecipientPartnerType varchar(3),
  RecipientPartnerGroup varchar(3),
  RecipientBizEntityId varchar(4),
  RecipientBizEntityUuid varchar(50),
  RecipientRegistryQueryUrl varchar(255),
  RecipientPartnerFunction varchar(30),
  RecipientGdocId bigint(20),
  SenderNodeId int(9),
  SenderGdocId bigint(20),
  SenderUserId varchar(15),
  SenderUserName varchar(50),
  SenderBizEntityId varchar(4),
  SenderBizEntityUuid varchar(50),
  SenderRegistryQueryUrl varchar(255),
  SenderRoute varchar(20),
  SenderPartnerId varchar(15),
  SenderPartnerType varchar(3),
  SenderPartnerGroup varchar(3),
  DateTimeImport datetime,
  DateTimeSendEnd datetime,
  DateTimeReceiveEnd datetime,
  DateTimeExport datetime,
  DateTimeCreate datetime,
  DateTimeTransComplete datetime,
  DateTimeReceiveStart datetime,
  DateTimeView datetime,
  DateTimeSendStart datetime,
  DateTimeRecipientView datetime,
  DateTimeRecipientExport datetime,
  PortUid bigint(20),
  SrcFolder varchar(10),
  NotifyUserEmail varchar(50),
  RecipientPartnerName varchar(20),
  SenderPartnerName varchar(20),
  PortName varchar(15),
  RnProfileUid bigint(20),
  ProcessDefId varchar(80),
  IsRequest tinyint(1) DEFAULT '0',
  HasAttachment tinyint(1) DEFAULT '0',
  AttachmentLinkUpdated tinyint(1) DEFAULT '1',
  Attachments text,
  TriggerType smallint(2),
  UniqueDocIdentifier varchar(80),
  UdocFullPath text NOT NULL DEFAULT '',
  ExportedUdocFullPath text,
  Rejected tinyint(1) DEFAULT '0' ,
  Custom1 varchar(255),
  Custom2 varchar(255),
  Custom3 varchar(255),
  Custom4 varchar(255),
  Custom5 varchar(255),
  Custom6 varchar(255),
  Custom7 varchar(255),
  Custom8 varchar(255),
  Custom9 varchar(255),
  Custom10 varchar(255),
  ProcessInstanceUid bigint(20),
  ProcessInstanceID varchar(255) default NULL,
  UserTrackingID varchar(80) default NULL,
  DocTransStatus varchar(255),
  MessageDigest  varchar(80),
  AuditFileName  varchar(200),
  ReceiptAuditFileName varchar(200),
  DocDateGen varchar(25) default NULL,
  SenderCert bigint(20) default NULL,
  ReceiverCert bigint(20) default NULL,
  OriginalDoc tinyint(1) default '0',
  TracingID varchar(36) default NULL,
  PRIMARY KEY (UID)
);

DROP TABLE IF EXISTS attachment;
CREATE TABLE IF NOT EXISTS attachment (
  UID bigint(20) NOT NULL DEFAULT '0' ,
  CanDelete tinyint(1) NOT NULL DEFAULT '1' ,
  Version double NOT NULL DEFAULT '1',
  PartnerId varchar(20),
  OriginalUid bigint(20),
  Filename varchar(255) NOT NULL DEFAULT '' ,
  OriginalFilename varchar(255) NOT NULL DEFAULT '' ,
  IsOutgoing tinyint(1) NOT NULL DEFAULT '1',
  PRIMARY KEY (UID)
);

DROP TABLE IF EXISTS attachmentRegistry;
CREATE TABLE IF NOT EXISTS attachmentRegistry (
  UID bigint(20) NOT NULL DEFAULT '0' ,
  CanDelete tinyint(1) NOT NULL DEFAULT '1' ,
  Version double NOT NULL DEFAULT '1',
  PartnerId varchar(20) NOT NULL DEFAULT '' ,
  AttachmentUid bigint(20) NOT NULL DEFAULT '-1' ,
  DateProcessed datetime,
  PRIMARY KEY (UID)
);

DROP TABLE IF EXISTS as2_doc_type_mapping; 
CREATE TABLE as2_doc_type_mapping (
	UID bigint(20) NOT NULL DEFAULT '0' ,
	AS2DocType varchar(30) NOT NULL DEFAULT '' ,
	DocType varchar(30) NOT NULL DEFAULT '' ,
    PartnerId varchar(15) NOT NULL DEFAULT '' ,
	CanDelete tinyint(1) DEFAULT '1' ,
	Version double DEFAULT '1' ,
	PRIMARY KEY (UID) ,
	UNIQUE KEY ID (AS2DocType, PartnerId)
);

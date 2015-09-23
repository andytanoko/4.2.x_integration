--- ------------------------------------------------------------------------
--- This script includes some of the CREATE queries for all tables in USERDB
--- ------------------------------------------------------------------------

SET client_encoding = 'UTF8';
SET check_function_bodies = false;
SET client_min_messages = warning;
SET search_path = userdb;

--- #### acl ####
--- 
--- Table structure for table 'role' 
--- 
DROP TABLE IF EXISTS "role";
CREATE TABLE "role" (
  "UID" BIGINT DEFAULT 0 NOT NULL, 
  "Role" VARCHAR(30)  NOT NULL, 
  "Description" VARCHAR(255), 
  "CanDelete" DECIMAL(1) DEFAULT 1 NOT NULL, 
  "Version" DECIMAL(7,5) DEFAULT 0 NOT NULL, 
  PRIMARY KEY ("UID"), 
  CONSTRAINT "ROLE_CON" UNIQUE ("Role")
);
ALTER TABLE "role" OWNER TO userdb;
 
--- 
--- Table structure for table 'subject_role' 
--- 
DROP TABLE IF EXISTS "subject_role";
CREATE TABLE "subject_role" (
  "UID" BIGINT DEFAULT 0 NOT NULL,
  "Subject" BIGINT DEFAULT 0 NOT NULL,
  "Role" BIGINT DEFAULT 0 NOT NULL,
  "SubjectType" VARCHAR(30)  NOT NULL,
  "Version" DECIMAL(7,5) DEFAULT 0 NOT NULL,
  PRIMARY KEY ("UID"),
  CONSTRAINT "SUBJECT_ROLE_CON" UNIQUE ("Role","Subject","SubjectType")
);
CREATE INDEX  "SUBJECT_ROLE_IDX1" ON  "subject_role" ("Subject","SubjectType");
CREATE INDEX  "SUBJECT_ROLE_IDX2" ON  "subject_role" ("Role","SubjectType");

ALTER TABLE "subject_role" OWNER TO userdb;
---
--- Table structure for table 'access_right'
---
DROP TABLE IF EXISTS "access_right";
CREATE TABLE "access_right" (
  "UID" BIGINT DEFAULT 0 NOT NULL, 
  "RoleUID" BIGINT DEFAULT 0 NOT NULL, 
  "Feature" VARCHAR(50)  NOT NULL, 
  "Description" VARCHAR(80)  NOT NULL, 
  "Action" VARCHAR(30)  NOT NULL, 
  "DataType" VARCHAR(30), 
  "Criteria" TEXT, 
  "CanDelete" DECIMAL(1) DEFAULT 0 NOT NULL, 
  "Version" DECIMAL(7,5) DEFAULT 0 NOT NULL, 
  PRIMARY KEY ("UID"),
  CONSTRAINT "ACCESS_RIGHT_CON" UNIQUE ("RoleUID","Feature","Action","DataType")
);

CREATE INDEX  "ACCESS_RIGHT_IDX1" ON  "access_right" ("RoleUID");
CREATE INDEX  "ACCESS_RIGHT_IDX2" ON  "access_right" ("RoleUID", "Feature");

ALTER TABLE "access_right" OWNER TO userdb;
---
--- Table structure for table 'feature'
---
DROP TABLE IF EXISTS "feature";
CREATE TABLE "feature"(
  "UID" BIGINT DEFAULT 0 NOT NULL,
  "Feature" VARCHAR(30)  NOT NULL,
  "Description" VARCHAR(80)  NOT NULL,
  "Actions" TEXT  NOT NULL,
  "DataTypes" TEXT  NOT NULL,
  "Version" DECIMAL(7,5) DEFAULT 0 NOT NULL,
  PRIMARY KEY ("UID"),
  CONSTRAINT "FEATURE_CON" UNIQUE ("Feature")
);
ALTER TABLE "feature" OWNER TO userdb;

--- #### certificate ####
--- 
--- Table structure for table 'certificate' 
--- 
DROP TABLE IF EXISTS "certificate";
CREATE TABLE "certificate" (
  "UID" BIGINT DEFAULT 0 NOT NULL,
  "ID" BIGINT DEFAULT 0 NOT NULL,
  "Name" VARCHAR(50) ,
  "SerialNum" VARCHAR(64) ,
  "IssuerName" VARCHAR(255) ,
  "Certificate" TEXT NULL,
  "PublicKey" TEXT NULL,
  "PrivateKey" TEXT NULL,
  "RevokeID" DECIMAL(10) DEFAULT 0,
  "isMaster" DECIMAL(1) DEFAULT 0 NOT NULL,
  "isPartner" DECIMAL(1) DEFAULT 0 NOT NULL,
  "Category" DECIMAL(1),
  "iSINKS" DECIMAL(1) DEFAULT 0 NOT NULL,
  "iSINTS" DECIMAL(1) DEFAULT 0 NOT NULL,
  "relatedCertUid" BIGINT,
  "StartDate" TIMESTAMP NULL,
  "EndDate" TIMESTAMP NULL,
  "IsCA" DECIMAL(1) DEFAULT 0 NOT NULL,
  "ReplacementCertUid" BIGINT NULL
);
ALTER TABLE "certificate" OWNER TO userdb;

--- #### contextdata ####
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

--- #### gwfbase #### 
---
--- Table structure for table 'bpss_bin_coll'
---
DROP TABLE IF EXISTS "bpss_bin_coll";
CREATE TABLE "bpss_bin_coll" (
  "UID" BIGINT,
  "Name" VARCHAR(50) ,
  "Pattern" VARCHAR(50) ,
  "TimeToPerform" VARCHAR(16) ,
  "PreCondition" VARCHAR(50) ,
  "PostCondition" VARCHAR(50) ,
  "BeginsWhen" VARCHAR(50) ,
  "EndsWhen" VARCHAR(50) ,
  "MaxConcurrency" DECIMAL(10) DEFAULT 0 NOT NULL,
  PRIMARY KEY  ("UID")
);
ALTER TABLE "bpss_bin_coll" OWNER TO userdb;

---
--- Table structure for table 'bpss_biz_partner_role'
---
DROP TABLE IF EXISTS "bpss_biz_partner_role";
CREATE TABLE "bpss_biz_partner_role" (
  "UID" BIGINT,
  "Name" VARCHAR(50) ,
  PRIMARY KEY  ("UID")
);
ALTER TABLE "bpss_biz_partner_role" OWNER TO userdb;

---
--- Table structure for table 'bpss_bin_coll_act'
---
DROP TABLE IF EXISTS "bpss_bin_coll_act";
CREATE TABLE "bpss_bin_coll_act" (
  "UID" BIGINT,
  "Name" VARCHAR(50) ,
  "BinCollProcessUId" BIGINT ,
  "DownLinkUId" BIGINT ,
  PRIMARY KEY  ("UID")
);
ALTER TABLE "bpss_bin_coll_act" OWNER TO userdb;

---
---
--- Table structure for table 'bpss_biz_tran'
---
DROP TABLE IF EXISTS "bpss_biz_tran";
CREATE TABLE "bpss_biz_tran" (
  "UID" BIGINT,
  "Name" VARCHAR(50) ,
  "Pattern" VARCHAR(50) ,
  "PreCondition" VARCHAR(50) ,
  "PostCondition" VARCHAR(50) ,
  "IsDeliveryReq" DECIMAL(1) DEFAULT 0,
  "BeginsWhen" VARCHAR(50) ,
  "EndsWhen" VARCHAR(50) ,
  "ReqUId" BIGINT,
  "ResUId" BIGINT,
  "MaxConcurrency" DECIMAL(10) DEFAULT 0 NOT NULL,
  PRIMARY KEY  ("UID")
);
ALTER TABLE "bpss_biz_tran" OWNER TO userdb;

---
--- Table structure for table 'bpss_biz_tran_activity'
---
DROP TABLE IF EXISTS "bpss_biz_tran_activity";
CREATE TABLE "bpss_biz_tran_activity" (
  "UID" BIGINT,
  "Name" VARCHAR(50)  NOT NULL,
  "TimeToPerform" VARCHAR(16) ,
  "IsConcurrent" DECIMAL(1) DEFAULT 0,
  "IsLegallyBinding" DECIMAL(1) DEFAULT 0,
  "BusinessTransUId" BIGINT ,
  "FromAuthorizedRole" VARCHAR(50) ,
  "ToAuthorizedRole" VARCHAR(50) ,
  PRIMARY KEY  ("UID")
);
ALTER TABLE "bpss_biz_tran_activity" OWNER TO userdb;

---
---
--- Table structure for table 'bpss_coll_activity'
---
DROP TABLE IF EXISTS "bpss_coll_activity";
CREATE TABLE "bpss_coll_activity" (
  "UID" BIGINT,
  "Name" VARCHAR(100) ,
  "BinCollProcessUId" BIGINT ,
  PRIMARY KEY  ("UID")
);
ALTER TABLE "bpss_coll_activity" OWNER TO userdb;

---
---
--- Table structure for table 'bpss_completion_state'
---
DROP TABLE IF EXISTS "bpss_completion_state";
CREATE TABLE "bpss_completion_state" (
  "UID" BIGINT,
  "ProcessUId" BIGINT ,
  "ProcessType" VARCHAR(50) ,
  "MpcUId" BIGINT ,
  "ConditionGuard" VARCHAR(50) ,
  "ExpressionLanguage" VARCHAR(50) ,
  "ConditionExpression" VARCHAR(250) ,
  "FromBusinessStateKey" VARCHAR(100) ,
  "CompletionType" VARCHAR(50) ,
  PRIMARY KEY  ("UID")
);
ALTER TABLE "bpss_completion_state" OWNER TO userdb;

---
---
--- Table structure for table 'bpss_fork'
---
DROP TABLE IF EXISTS "bpss_fork";
CREATE TABLE "bpss_fork" (
  "UID" BIGINT,
  "Name" VARCHAR(50) ,
  PRIMARY KEY  ("UID")
);
ALTER TABLE "bpss_fork" OWNER TO userdb;

---
---
--- Table structure for table 'bpss_join'
---
DROP TABLE IF EXISTS "bpss_join";
CREATE TABLE "bpss_join" (
  "UID" BIGINT DEFAULT 0 NOT NULL,
  "Name" VARCHAR(50) ,
  "WaitForAll" DECIMAL(1) DEFAULT 0,
  PRIMARY KEY  ("UID")
);
ALTER TABLE "bpss_join" OWNER TO userdb;

---
---
--- Table structure for table 'bpss_multiparty_coll'
---
DROP TABLE IF EXISTS "bpss_multiparty_coll";
CREATE TABLE "bpss_multiparty_coll" (
  "UID" BIGINT,
  "Name" VARCHAR(50) ,
  "MaxConcurrency" DECIMAL(10) DEFAULT 0 NOT NULL,
  PRIMARY KEY  ("UID")
);
ALTER TABLE "bpss_multiparty_coll" OWNER TO userdb;

---
---
--- Table structure for table 'bpss_req_biz_activity'
---
DROP TABLE IF EXISTS "bpss_req_biz_activity";
CREATE TABLE "bpss_req_biz_activity" (
  "UID" BIGINT,
  "Name" VARCHAR(50) ,
  "IsIntelligibleChkReq" DECIMAL(1) DEFAULT 0,
  "IsAuthReq" DECIMAL(1) DEFAULT 0,
  "TimeToAckReceipt" VARCHAR(16) ,
  "IsNonRepudiationReq" DECIMAL(1) DEFAULT 0,
  "IsNonRepudiationOfReceiptReq" DECIMAL(1) DEFAULT 0,
  "TimeToAckAccept" VARCHAR(16) ,
  PRIMARY KEY  ("UID")
);
ALTER TABLE "bpss_req_biz_activity" OWNER TO userdb;

---
---
--- Table structure for table 'bpss_res_biz_activity'
---
DROP TABLE IF EXISTS "bpss_res_biz_activity";
CREATE TABLE "bpss_res_biz_activity" (
  "UID" BIGINT,
  "Name" VARCHAR(100) ,
  "IsIntelligibleChkReq" DECIMAL(1) DEFAULT 0,
  "IsAuthReq" DECIMAL(1) DEFAULT 0 NOT NULL,
  "TimeToAckReceipt" VARCHAR(16) ,
  "IsNonRepudiationReq" DECIMAL(1) DEFAULT 0,
  "IsNonRepudiationOfReceiptReq" DECIMAL(1) DEFAULT 0,
  PRIMARY KEY  ("UID")
);
ALTER TABLE "bpss_res_biz_activity" OWNER TO userdb;

---
---
--- Table structure for table 'bpss_start'
---
DROP TABLE IF EXISTS "bpss_start";
CREATE TABLE "bpss_start" (
  "UID" BIGINT,
  "ProcessUId" BIGINT ,
  "IsDownLink" DECIMAL(1) DEFAULT 0,
  "ToBusinessStateKey" VARCHAR(200) ,
  PRIMARY KEY  ("UID")
);
ALTER TABLE "bpss_start" OWNER TO userdb;

---
---
--- Table structure for table 'bpss_transition'
---
DROP TABLE IF EXISTS "bpss_transition";
CREATE TABLE "bpss_transition" (
  "UID" BIGINT,
  "ProcessUId" BIGINT ,
  "ProcessType" VARCHAR(50) ,
  "OnInitiation" CHAR(1) ,
  "ConditionGuard" VARCHAR(50) ,
  "ExpressionLanguage" VARCHAR(50) ,
  "ConditionExpression" VARCHAR(250) ,
  "FromBusinessStateKey" VARCHAR(100) ,
  "ToBusinessStateKey" VARCHAR(100) ,
  PRIMARY KEY  ("UID")
);
ALTER TABLE "bpss_transition" OWNER TO userdb;

---
--- Table structure for table 'bpss_businessdocument'
---
DROP TABLE IF EXISTS "bpss_businessdocument";
CREATE TABLE "bpss_businessdocument" (
  "UID" BIGINT,
  "Name" VARCHAR(50) ,
  "ExpressionLanguage" VARCHAR(50) ,
  "ConditionExpr" VARCHAR(250) ,
  "SpecElement" VARCHAR(50) ,
  "SpecLocation" VARCHAR(80) ,
  PRIMARY KEY ("UID")
);
ALTER TABLE "bpss_businessdocument" OWNER TO userdb;

---
--- Table structure for table 'bpss_documentation'
---
DROP TABLE IF EXISTS "bpss_documentation";
CREATE TABLE "bpss_documentation" (
  "UID" BIGINT,
  "Uri" VARCHAR(50) ,
  "Documentation" VARCHAR(250) ,
  PRIMARY KEY ("UID")
);
ALTER TABLE "bpss_documentation" OWNER TO userdb;

---
--- Table structure for table 'bpss_documentenvelope'
---
DROP TABLE IF EXISTS "bpss_documentenvelope";
CREATE TABLE "bpss_documentenvelope" (
  "UID" BIGINT,
  "BusinessDocumentName" VARCHAR(50) ,
  "BusinessDocumentIDRef" VARCHAR(50) ,
  "IsPositiveResponse" DECIMAL(1) DEFAULT 0,
  "IsAuthenticated" DECIMAL(1) DEFAULT 0,
  "IsConfidential" DECIMAL(1) DEFAULT 0,
  "IsTamperProof" DECIMAL(1) DEFAULT 0,
  PRIMARY KEY ("UID")
);
ALTER TABLE "bpss_documentenvelope" OWNER TO userdb;

---
---
--- Table structure for table 'bpss_proc_spec'
---
DROP TABLE IF EXISTS "bpss_proc_spec";
CREATE TABLE "bpss_proc_spec" (
  "UID" BIGINT,
  "UUId" VARCHAR(80)  NOT NULL,
  "Version" VARCHAR(10)  NOT NULL,
  "Name" VARCHAR(100)  NOT NULL,
  "Timestamp" VARCHAR(100)  NOT NULL,
  PRIMARY KEY  ("UID")
);
ALTER TABLE "bpss_proc_spec" OWNER TO userdb;

---
---
--- Table structure for table 'bpss_proc_spec_entry'
---
DROP TABLE IF EXISTS "bpss_proc_spec_entry";
CREATE TABLE "bpss_proc_spec_entry" (
  "UID" BIGINT,
  "SpecUId" BIGINT,
  "EntryUId" BIGINT,
  "Name" VARCHAR(100) ,
  "Type" VARCHAR(100)  NOT NULL,
  "ParentEntryUId" BIGINT,
  PRIMARY KEY  ("UID")
);
ALTER TABLE "bpss_proc_spec_entry" OWNER TO userdb;

---
--- Table structure for table 'xpdl_activity'
---
DROP TABLE IF EXISTS "xpdl_activity";
CREATE TABLE "xpdl_activity" (
  "UID" BIGINT DEFAULT 0 NOT NULL ,
  "ActivityId" VARCHAR(50) ,
  "ActivityName" VARCHAR(50) ,
  "ActivityDescription" VARCHAR(255) , 
  "ExtendedAttributes" BYTEA NULL ,
  "ActivityLimit" DECIMAL(8,0) ,
  "IsRoute" DECIMAL(1) DEFAULT 0 NOT NULL ,
  "ImplementationType" VARCHAR(50) ,
  "PerformerId" VARCHAR(50) ,
  "StartMode" VARCHAR(50) ,
  "FinishMode" VARCHAR(50) ,
  "Priority" BIGINT ,
  "Instantiation" VARCHAR(50) ,
  "Cost" DECIMAL(8,0) ,
  "WaitingTime" DECIMAL(8,0) ,
  "Duration" DECIMAL(8,0) ,
  "IconUrl" VARCHAR(50) ,
  "DocumentationUrl" VARCHAR(50) ,
  "TransitionRestrictionListUId" BIGINT ,
  "WorkingTime" DECIMAL(8,0) ,
  "ProcessId" VARCHAR(50) ,
  "PackageId" VARCHAR(50) ,
  "PkgVersionId" VARCHAR(50) ,
  PRIMARY KEY ("UID")
);
ALTER TABLE "xpdl_activity" OWNER TO userdb;

---
--- Table structure for table 'xpdl_application'
---
DROP TABLE IF EXISTS "xpdl_application";
CREATE TABLE "xpdl_application" (
  "UID" BIGINT DEFAULT 0 NOT NULL ,
  "ApplicationId" VARCHAR(50) ,
  "ApplicationName" VARCHAR(50) ,
  "ApplicationDescription" VARCHAR(255) ,
  "ExtendedAttributes" BYTEA NULL ,
  "ProcessId" VARCHAR(50) ,
  "PackageId" VARCHAR(50) ,
  "PkgVersionId" VARCHAR(50) ,
  PRIMARY KEY ("UID")
);
ALTER TABLE "xpdl_application" OWNER TO userdb;

---
--- Table structure for table 'xpdl_datafield'
---
DROP TABLE IF EXISTS "xpdl_datafield";
CREATE TABLE "xpdl_datafield" (
  "UID" BIGINT DEFAULT 0 NOT NULL ,
  "DataFieldId" VARCHAR(50) ,
  "DataFieldName" VARCHAR(50) ,
  "DataFieldDescription" VARCHAR(50) ,
  "ExtendedAttributes" BYTEA NULL ,
  "IsArray" DECIMAL(1) DEFAULT 0 NOT NULL ,
  "InitialValue" VARCHAR(50) ,
  "LengthBytes" BIGINT ,
  "ProcessId" VARCHAR(50) ,
  "PackageId" VARCHAR(50) ,
  "PkgVersionId" VARCHAR(50) ,
  "DataTypeName" VARCHAR(50) ,
  "ComplexDataTypeUId" BIGINT ,
  PRIMARY KEY ("UID")
);
ALTER TABLE "xpdl_datafield" OWNER TO userdb;

---
--- Table structure for table 'xpdl_externalpackage'
---
DROP TABLE IF EXISTS "xpdl_externalpackage";
CREATE TABLE "xpdl_externalpackage" (
  "UID" BIGINT DEFAULT 0 NOT NULL ,
  "Href" VARCHAR(50) ,
  "ExtendedAttributes" BYTEA NULL ,
  "PackageId" VARCHAR(50) ,
  "PkgVersionId" VARCHAR(50) ,
  PRIMARY KEY ("UID")
);
ALTER TABLE "xpdl_externalpackage" OWNER TO userdb;

---
--- Table structure for table 'xpdl_formalparam'
---
DROP TABLE IF EXISTS "xpdl_formalparam";
CREATE TABLE "xpdl_formalparam" (
  "UID" BIGINT DEFAULT 0 NOT NULL ,
  "FormalParamId" VARCHAR(50) ,
  "Mode" VARCHAR(50) ,
  "IndexNumber" DECIMAL(10) ,
  "FormalParamDescription" VARCHAR(50) ,
  "ApplicationId" VARCHAR(50) ,
  "ProcessId" VARCHAR(50) ,
  "PackageId" VARCHAR(50) ,
  "PkgVersionId" VARCHAR(50) ,
  "DataTypeName" VARCHAR(50) ,
  "ComplexDataTypeUId" BIGINT ,
  PRIMARY KEY ("UID")
);
ALTER TABLE "xpdl_formalparam" OWNER TO userdb;

---
--- Table structure for table 'xpdl_package'
---
DROP TABLE IF EXISTS "xpdl_package";
CREATE TABLE "xpdl_package" (
  "UID" BIGINT DEFAULT 0 NOT NULL ,
  "PackageId" VARCHAR(50) ,
  "PackageName" VARCHAR(50) ,
  "PackageDescription" VARCHAR(50) ,
  "ExtendedAttributes" BYTEA NULL ,
  "SpecificationId" VARCHAR(50) ,
  "SpecificationVersion" VARCHAR(50) ,
  "SourceVendorInfo" VARCHAR(50) ,
  "CreationDateTime" TIMESTAMP ,
  "DocumentationUrl" VARCHAR(50) ,
  "PriorityUnit" VARCHAR(50) ,
  "CostUnit" VARCHAR(50) ,
  "Author" VARCHAR(50) ,
  "VersionId" VARCHAR(50) ,
  "Codepage" VARCHAR(50) ,
  "Countrykey" VARCHAR(50) ,
  "PublicationStatus" VARCHAR(50) ,
  "ResponsibleListUId" BIGINT ,
  "GraphConformance" VARCHAR(50) ,
  "State" DECIMAL(1)  DEFAULT 1 NOT NULL,
  PRIMARY KEY ("UID")
);
ALTER TABLE "xpdl_package" OWNER TO userdb;

---
--- Table structure for table 'xpdl_participant'
---
DROP TABLE IF EXISTS "xpdl_participant";
CREATE TABLE "xpdl_participant" (
  "UID" BIGINT DEFAULT 0 NOT NULL ,
  "ParticipantId" VARCHAR(50) ,
  "ParticipantName" VARCHAR(50) ,
  "ParticipantDescription" VARCHAR(50) ,
  "ExtendedAttributes" BYTEA NULL ,
  "ParticipantTypeId" VARCHAR(50) ,
  "ProcessId" VARCHAR(50) ,
  "PackageId" VARCHAR(50) ,
  "PkgVersionId" VARCHAR(50) ,
  PRIMARY KEY ("UID")
);
ALTER TABLE "xpdl_participant" OWNER TO userdb;

---
--- Table structure for table 'xpdl_participantlist'
---
DROP TABLE IF EXISTS "xpdl_participantlist";
CREATE TABLE "xpdl_participantlist" (
  "UID" BIGINT DEFAULT 0 NOT NULL ,
  "ParticipantId" VARCHAR(50) ,
  "ParticipantIndex" BIGINT ,
  "ListUId" BIGINT ,
  PRIMARY KEY ("UID")
);
ALTER TABLE "xpdl_participantlist" OWNER TO userdb;

---
--- Table structure for table 'xpdl_process'
---
DROP TABLE IF EXISTS "xpdl_process";
CREATE TABLE "xpdl_process" (
  "UID" BIGINT DEFAULT 0 NOT NULL ,
  "ProcessId" VARCHAR(50) ,
  "ProcessName" VARCHAR(50) ,
  "ProcessDescription" VARCHAR(50) ,
  "ExtendedAttributes" BYTEA NULL ,
  "DurationUnit" VARCHAR(50) ,
  "CreationDateTime" TIMESTAMP ,
  "HeaderDescription" VARCHAR(50) ,
  "Priority" BIGINT ,
  "ProcessLimit" DECIMAL(8,0) ,
  "ValidFromDate" TIMESTAMP ,
  "ValidToDate" TIMESTAMP ,
  "WaitingTime" DECIMAL(8,0) ,
  "WorkingTime" DECIMAL(8,0) ,
  "Duration" DECIMAL(8,0) ,
  "Author" VARCHAR(50) ,
  "VersionId" VARCHAR(50) ,
  "Codepage" VARCHAR(50) ,
  "Countrykey" VARCHAR(50) ,
  "PublicationStatus" VARCHAR(50) ,
  "ResponsibleListUId" BIGINT ,
  "PackageId" VARCHAR(50) ,
  "DefaultStartActivityId" VARCHAR(50) ,
  "PkgVersionId" VARCHAR(50) ,
  PRIMARY KEY ("UID")
);
ALTER TABLE "xpdl_process" OWNER TO userdb;

---
--- Table structure for table 'xpdl_subflow'
---
DROP TABLE IF EXISTS "xpdl_subflow";
CREATE TABLE "xpdl_subflow" (
  "UID" BIGINT DEFAULT 0 NOT NULL ,
  "SubFlowId" VARCHAR(100) ,
  "SubFlowType" VARCHAR(50) ,
  "ActualParameters" VARCHAR(255) ,
  "ActivityId" VARCHAR(50) ,
  "ProcessId" VARCHAR(50) ,
  "PackageId" VARCHAR(50) ,
  "PkgVersionId" VARCHAR(50) ,
  PRIMARY KEY ("UID")
);
ALTER TABLE "xpdl_subflow" OWNER TO userdb;

---
--- Table structure for table 'xpdl_tool'
---
DROP TABLE IF EXISTS "xpdl_tool";
CREATE TABLE "xpdl_tool" (
  "UID" BIGINT DEFAULT 0 NOT NULL ,
  "ToolId" VARCHAR(50) ,
  "ToolType" VARCHAR(50) ,
  "ToolDescription" VARCHAR(50) ,
  "ActualParameters" BYTEA NULL ,
  "ExtendedAttributes" BYTEA NULL ,
  "LoopKind" VARCHAR(50) ,
  "ConditionExpr" VARCHAR(50) ,
  "ActivityId" VARCHAR(50) ,
  "ProcessId" VARCHAR(50) ,
  "PackageId" VARCHAR(50) ,
  "PkgVersionId" VARCHAR(50) ,
  PRIMARY KEY ("UID")
);
ALTER TABLE "xpdl_tool" OWNER TO userdb;

---
--- Table structure for table 'xpdl_transition'
---
DROP TABLE IF EXISTS "xpdl_transition";
CREATE TABLE "xpdl_transition" (
  "UID" BIGINT DEFAULT 0 NOT NULL ,
  "TransitionId" VARCHAR(50) ,
  "TransitionName" VARCHAR(50) ,
  "TransitionDescription" VARCHAR(50) ,
  "ExtendedAttributes" BYTEA NULL ,
  "FromActivityId" VARCHAR(50) ,
  "ToActivityId" VARCHAR(50) ,
  "LoopType" VARCHAR(50) ,
  "ConditionType" VARCHAR(50) ,
  "ConditionExpr" VARCHAR(150) ,
  "ProcessId" VARCHAR(50) ,
  "PackageId" VARCHAR(50) ,
  "PkgVersionId" VARCHAR(50) ,
  PRIMARY KEY ("UID")
);
ALTER TABLE "xpdl_transition" OWNER TO userdb;

---
--- Table structure for table 'xpdl_transitionref'
---
DROP TABLE IF EXISTS "xpdl_transitionref";
CREATE TABLE "xpdl_transitionref" (
  "UID" BIGINT DEFAULT 0 NOT NULL ,
  "TransitionId" VARCHAR(50) ,
  "ListUId" BIGINT ,
  PRIMARY KEY ("UID")
);
ALTER TABLE "xpdl_transitionref" OWNER TO userdb;

---
--- Table structure for table 'xpdl_transitionrestriction'
---
DROP TABLE IF EXISTS "xpdl_transitionrestriction";
CREATE TABLE "xpdl_transitionrestriction" (
  "UID" BIGINT DEFAULT 0 NOT NULL ,
  "IsInlineBlock" DECIMAL(1) DEFAULT 0 NOT NULL ,
  "BlockName" VARCHAR(50) ,
  "BlockDescription" VARCHAR(50) ,
  "BlockIconUrl" VARCHAR(50) ,
  "BlockDocumentationUrl" VARCHAR(50) ,
  "BlockBeginActivityId" VARCHAR(50) ,
  "BlockEndActivityId" VARCHAR(50) ,
  "JoinType" VARCHAR(50) ,
  "SplitType" VARCHAR(50) ,
  "TransitionRefListUId" BIGINT ,
  "ListUId" BIGINT ,
  PRIMARY KEY ("UID")
);
ALTER TABLE "xpdl_transitionrestriction" OWNER TO userdb;

---
--- Table structure for table 'xpdl_typedeclaration'
---
DROP TABLE IF EXISTS "xpdl_typedeclaration";
CREATE TABLE "xpdl_typedeclaration" (
  "UID" BIGINT DEFAULT 0 NOT NULL ,
  "TypeId" VARCHAR(50) ,
  "TypeName" VARCHAR(50) ,
  "TypeDescription" VARCHAR(50) ,
  "ExtendedAttributes" BYTEA NULL ,
  "PackageId" VARCHAR(50) ,
  "PkgVersionId" VARCHAR(50) ,
  "DataTypeName" VARCHAR(50) ,
  "ComplexDataTypeUId" BIGINT ,
  PRIMARY KEY ("UID")
);
ALTER TABLE "xpdl_typedeclaration" OWNER TO userdb;

---
--- Table structure for table 'xpdl_complexdatatype'
---
DROP TABLE IF EXISTS "xpdl_complexdatatype";
CREATE TABLE "xpdl_complexdatatype" (
  "UID" BIGINT DEFAULT 0 NOT NULL ,
  "DataTypeName" VARCHAR(50) ,
  "ComplexDataTypeUId" BIGINT ,
  "SubTypeUId" BIGINT ,
  "ArrayLowerIndex" DECIMAL(10) ,
  "ArrayUpperIndex" DECIMAL(10) ,
  PRIMARY KEY ("UID")
);
ALTER TABLE "xpdl_complexdatatype" OWNER TO userdb;

--- #### session ####
---
--- Table structure for table 'session_audit'
---
DROP TABLE IF EXISTS "session_audit";
CREATE TABLE "session_audit" (
  "UID" BIGINT NOT NULL,
  "SessionId" VARCHAR(30) NOT NULL,
  "SessionName" VARCHAR(30) ,
  "State" DECIMAL(5) DEFAULT 0 NOT NULL,
  "SessionData" BYTEA NULL ,
  "OpenTime" TIMESTAMP NOT NULL ,
  "LastActiveTime" TIMESTAMP NOT NULL,
  "DestroyTime" TIMESTAMP NULL,
  PRIMARY KEY ("UID"),
  CONSTRAINT "SESSION_AUDIT_CON" UNIQUE ("SessionId")
);
ALTER TABLE "session_audit" OWNER TO userdb;

--- #### time ####
---
--- Table structure for table 'ical_alarm'
---
DROP TABLE IF EXISTS "ical_alarm";
CREATE TABLE "ical_alarm" (
  "UID" BIGINT DEFAULT 0 NOT NULL,
  "StartDuration" BIGINT ,
  "StartDt" TIMESTAMP ,
  "Related" DECIMAL(10) ,
  "DelayPeriod" BIGINT ,
  "RepeatCount" DECIMAL(10) ,
  "Category" VARCHAR(100) ,
  "SenderKey" VARCHAR(255) ,
  "ReceiverKey" VARCHAR(255) ,
  "Disabled" DECIMAL(1) ,
  "NextDueTime" TIMESTAMP ,
  "Count" DECIMAL(10) ,
  "ParentUid" BIGINT ,
  "ParentKind" DECIMAL(5) ,
  "RecurListStr" TEXT ,
  "IsRecurComplete" DECIMAL(1) ,
  "CurRecur" VARCHAR(80) ,
  "IsPseudoParent" DECIMAL(1) ,
  "IncludeParentStartTime" DECIMAL(1) ,
  "TaskId" VARCHAR(120) 	
);
ALTER TABLE "ical_alarm" OWNER TO userdb;

---
--- Table structure for table 'ical_event'
---
DROP TABLE IF EXISTS "ical_event";
CREATE TABLE "ical_event" (
  "UID" BIGINT DEFAULT 0 NOT NULL,
  "Kind" DECIMAL(5) ,
  "OwnerId" DECIMAL(10) ,
  "Classification" VARCHAR(80) ,
  "CreateDt" TIMESTAMP ,
  "LastModifyDt" TIMESTAMP ,
  "IsDateType" DECIMAL(1) , 
  "IsDtFloat" DECIMAL(1) ,
  "StartDt" TIMESTAMP ,
  "Priority" DECIMAL(10) ,
  "SequenceNum" DECIMAL(10) ,
  "Status" DECIMAL(10) ,
  "iCalUid" VARCHAR(80) ,
  "EndDt" TIMESTAMP ,
  "Duration" BIGINT ,
  "PropertiesStr" VARCHAR(1000) , 
  "TimeTransparency" DECIMAL(10) 
);
ALTER TABLE "ical_event" OWNER TO userdb;

---
--- Table structure for table 'ical_todo'
---
DROP TABLE IF EXISTS "ical_todo";
CREATE TABLE "ical_todo" (
  "UID" BIGINT DEFAULT 0 NOT NULL,
  "Kind" DECIMAL(5) ,
  "OwnerId" DECIMAL(10) ,
  "Classification" VARCHAR(80) ,
  "CreateDt" TIMESTAMP ,
  "LastModifyDt" TIMESTAMP ,
  "IsDateType" DECIMAL(1) ,
  "IsDtFloat" DECIMAL(1) ,
  "StartDt" TIMESTAMP ,
  "Priority" DECIMAL(10) ,
  "SequenceNum" DECIMAL(10) ,
  "Status" DECIMAL(10) ,
  "iCalUid" VARCHAR(80) ,
  "EndDt" TIMESTAMP ,
  "Duration" BIGINT ,
  "CompleteDt" TIMESTAMP ,
  "PropertiesStr" TEXT , 
  "PercentCompleted" DECIMAL(10) 
);
ALTER TABLE "ical_todo" OWNER TO userdb;

---
--- Table structure for table 'ical_property'
---
DROP TABLE IF EXISTS "ical_property";
CREATE TABLE "ical_property" (
  "UID" BIGINT DEFAULT 0 NOT NULL,
  "Kind" DECIMAL(5) ,
  "CompKind" DECIMAL(5) ,
  "iCalCompId" BIGINT 
);
ALTER TABLE "ical_property" OWNER TO userdb;

---
--- Table structure for table 'ical_int'
---
DROP TABLE IF EXISTS "ical_int";
CREATE TABLE "ical_int" (
  "UID" BIGINT DEFAULT 0 NOT NULL,
  "CompKind" DECIMAL(5) ,
  "PropKind" DECIMAL(5) ,
  "ParamKind" DECIMAL(5) ,
  "RefKind" DECIMAL(5) ,
  "ValueKind" DECIMAL(5) ,
  "iCalCompId" BIGINT ,
  "RefId" BIGINT ,
  "IntValue" DECIMAL(10) 
);
ALTER TABLE "ical_int" OWNER TO userdb;

---
--- Table structure for table 'ical_date'
---
DROP TABLE IF EXISTS "ical_date";
CREATE TABLE "ical_date" (
  "UID" BIGINT DEFAULT 0 NOT NULL,
  "CompKind" DECIMAL(5) ,
  "PropKind" DECIMAL(5) ,
  "ParamKind" DECIMAL(5) ,
  "RefKind" DECIMAL(5) ,
  "ValueKind" DECIMAL(5) ,
  "iCalCompId" BIGINT ,
  "RefId" BIGINT ,
  "DateValue" TIMESTAMP 
);
ALTER TABLE "ical_date" OWNER TO userdb;

---
--- Table structure for table 'ical_string'
---
DROP TABLE IF EXISTS "ical_string";
CREATE TABLE "ical_string" (
  "UID" BIGINT DEFAULT 0 NOT NULL,
  "CompKind" DECIMAL(5) ,
  "PropKind" DECIMAL(5) ,
  "ParamKind" DECIMAL(5) ,
  "RefKind" DECIMAL(5) ,
  "ValueKind" DECIMAL(5) ,
  "iCalCompId" BIGINT ,
  "RefId" BIGINT ,
  "StrValue" VARCHAR(80) 
);
ALTER TABLE "ical_string" OWNER TO userdb;

---
--- Table structure for table 'ical_text'
---
DROP TABLE IF EXISTS "ical_text";
CREATE TABLE "ical_text" (
  "UID" BIGINT DEFAULT 0 NOT NULL,
  "CompKind" DECIMAL(5) ,
  "PropKind" DECIMAL(5) ,
  "ParamKind" DECIMAL(5) ,
  "RefKind" DECIMAL(5) ,
  "ValueKind" DECIMAL(5) ,
  "iCalCompId" BIGINT ,
  "RefId" BIGINT ,
  "TextValue" TEXT 
);
ALTER TABLE "ical_text" OWNER TO userdb;

--- #### userprocedure ####
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

--- #### worklist ####
---
--- Table structure for table 'worklistvalue'
---
DROP TABLE IF EXISTS "worklistvalue";
CREATE TABLE "worklistvalue" (
  "UID" BIGINT,
  "wi_description" VARCHAR(50) ,
  "wi_comments" VARCHAR(50) ,
  "wi_status" CHAR(2) ,
  "wi_cdate" TIMESTAMP ,
  "user_id" VARCHAR(80) ,
  "unassigned" CHAR(2) ,
  "processDefKey" VARCHAR(255) ,
  "activityId" VARCHAR(255) ,
  "performer" VARCHAR(150) ,
  "rtActivityUId" BIGINT,
  "contextUId" BIGINT,
  PRIMARY KEY  ("UID")
);
ALTER TABLE "worklistvalue" OWNER TO userdb;

---
--- Table structure for table 'worklistuser'
---
DROP TABLE IF EXISTS "worklistuser";
CREATE TABLE "worklistuser" (
  "UID" BIGINT DEFAULT 0 NOT NULL,
  "workitem_id" BIGINT,
  "user_id" VARCHAR(80) ,
  PRIMARY KEY  ("UID")
);
ALTER TABLE "worklistuser" OWNER TO userdb;

COMMIT;
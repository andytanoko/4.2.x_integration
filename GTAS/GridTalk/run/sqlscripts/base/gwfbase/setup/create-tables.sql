SET client_encoding = 'UTF8';
SET check_function_bodies = false;
SET client_min_messages = warning;
SET search_path = userdb;

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
  "ActivityDescription" VARCHAR(50) ,
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


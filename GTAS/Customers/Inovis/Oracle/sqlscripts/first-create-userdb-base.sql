--- ------------------------------------------------------------------------
--- This script includes some of the CREATE queries for all tables in USERDB
--- ------------------------------------------------------------------------

CONNECT USERDB/gridnode;

ALTER SESSION SET NLS_DATE_FORMAT = 'YYYY-MM-DD HH24:MI:SS';
--- #### acl ####
--- 
--- Table structure for table 'role' 
--- 

CREATE TABLE "role" (
  "UID" NUMBER(19) DEFAULT 0 NOT NULL ENABLE, 
  "Role" VARCHAR2(30)  NOT NULL ENABLE, 
  "Description" VARCHAR2(255), 
  "CanDelete" NUMBER(1) DEFAULT 1 NOT NULL ENABLE, 
  "Version" NUMBER(7,5) DEFAULT 0 NOT NULL ENABLE, 
  PRIMARY KEY ("UID") ENABLE, 
  CONSTRAINT "ROLE_CON" UNIQUE ("Role") ENABLE
);
 
 
--- 
--- Table structure for table 'subject_role' 
--- 

CREATE TABLE "subject_role" (
  "UID" NUMBER(19) DEFAULT 0 NOT NULL ENABLE,
  "Subject" NUMBER(19) DEFAULT 0 NOT NULL ENABLE,
  "Role" NUMBER(19) DEFAULT 0 NOT NULL ENABLE,
  "SubjectType" VARCHAR2(30)  NOT NULL ENABLE,
  "Version" NUMBER(7,5) DEFAULT 0 NOT NULL ENABLE,
  PRIMARY KEY ("UID"),
  CONSTRAINT "SUBJECT_ROLE_CON" UNIQUE ("Role","Subject","SubjectType") ENABLE
);
CREATE INDEX  "SUBJECT_ROLE_IDX1" ON  "subject_role" ("Subject","SubjectType");
CREATE INDEX  "SUBJECT_ROLE_IDX2" ON  "subject_role" ("Role","SubjectType");


---
--- Table structure for table 'access_right'
---

CREATE TABLE "access_right" (
  "UID" NUMBER(19) DEFAULT 0 NOT NULL ENABLE, 
  "RoleUID" NUMBER(19) DEFAULT 0 NOT NULL ENABLE, 
  "Feature" VARCHAR2(50)  NOT NULL ENABLE, 
  "Description" VARCHAR2(80)  NOT NULL ENABLE, 
  "Action" VARCHAR2(30)  NOT NULL ENABLE, 
  "DataType" VARCHAR2(30), 
  "Criteria" VARCHAR2(2000), 
  "CanDelete" NUMBER(1) DEFAULT 0 NOT NULL ENABLE, 
  "Version" NUMBER(7,5) DEFAULT 0 NOT NULL ENABLE, 
  PRIMARY KEY ("UID") ENABLE,
  CONSTRAINT "ACCESS_RIGHT_CON" UNIQUE ("RoleUID","Feature","Action","DataType") ENABLE
);

CREATE INDEX  "ACCESS_RIGHT_IDX1" ON  "access_right" ("RoleUID");
CREATE INDEX  "ACCESS_RIGHT_IDX2" ON  "access_right" ("RoleUID", "Feature");


---
--- Table structure for table 'feature'
---

CREATE TABLE "feature"(
  "UID" NUMBER(19) DEFAULT 0 NOT NULL ENABLE,
  "Feature" VARCHAR2(30)  NOT NULL ENABLE,
  "Description" VARCHAR2(80)  NOT NULL ENABLE,
  "Actions" VARCHAR2(2000)  NOT NULL ENABLE,
  "DataTypes" VARCHAR2(1000)  NOT NULL ENABLE,
  "Version" NUMBER(7,5) DEFAULT 0 NOT NULL ENABLE,
  PRIMARY KEY ("UID"),
  CONSTRAINT "FEATURE_CON" UNIQUE ("Feature") ENABLE
);


--- #### certificate ####
--- 
--- Table structure for table 'certificate' 
--- 

CREATE TABLE "certificate" (
  "UID" NUMBER(19) DEFAULT 0 NOT NULL ENABLE,
  "ID" NUMBER(19) DEFAULT 0 NOT NULL ENABLE,
  "Name" VARCHAR2(50) ,
  "SerialNum" VARCHAR2(64) ,
  "IssuerName" VARCHAR2(255) ,
  "Certificate" VARCHAR2(4000) NULL,
  "PublicKey" VARCHAR2(4000) NULL,
  "PrivateKey" VARCHAR2(4000) NULL,
  "RevokeID" NUMBER(10) DEFAULT 0,
  "isMaster" NUMBER(1) DEFAULT 0 NOT NULL ENABLE,
  "isPartner" NUMBER(1) DEFAULT 0 NOT NULL ENABLE,
  "Category" NUMBER(1),
  "iSINKS" NUMBER(1) DEFAULT 0 NOT NULL ENABLE,
  "iSINTS" NUMBER(1) DEFAULT 0 NOT NULL ENABLE,
  "relatedCertUid" NUMBER(19),
  "StartDate" DATE NULL,
  "EndDate" DATE NULL,
  "IsCA" NUMBER(1) DEFAULT 0 NOT NULL ENABLE,
  "ReplacementCertUid" NUMBER(19) NULL
);


--- #### contextdata ####
---
--- Table structure for table 'data_stringdata'
---

CREATE TABLE "data_stringdata" (
  "UID" NUMBER(19) DEFAULT 0 NOT NULL ENABLE,
  "Data" VARCHAR2(100) ,
  "DataType" VARCHAR2(50) ,
  PRIMARY KEY ("UID")
);


---
--- Table structure for table 'data_bytedata'
---

CREATE TABLE "data_bytedata" (
  "UID" NUMBER(19) DEFAULT 0 NOT NULL ENABLE,
  "Data" BLOB NULL ,
  "DataType" VARCHAR2(50) ,
  PRIMARY KEY ("UID")
);


---
--- Table structure for table 'data_contextdata'
---

CREATE TABLE "data_contextdata" (
  "UID" NUMBER(19) DEFAULT 0 NOT NULL ENABLE ,
  "ContextUId" NUMBER(19) DEFAULT 0 NOT NULL ENABLE,
  "ContextData" BLOB NULL ,
  PRIMARY KEY ("UID"),
  CONSTRAINT "DATA_CONTEXT_CON" UNIQUE ("ContextUId") ENABLE
);


--- #### gwfbase #### 
---
--- Table structure for table 'bpss_bin_coll'
---

CREATE TABLE "bpss_bin_coll" (
  "UID" NUMBER(19) DEFAULT 0 NOT NULL ENABLE,
  "Name" VARCHAR2(50) ,
  "Pattern" VARCHAR2(50) ,
  "TimeToPerform" VARCHAR2(16) ,
  "PreCondition" VARCHAR2(50) ,
  "PostCondition" VARCHAR2(50) ,
  "BeginsWhen" VARCHAR2(50) ,
  "EndsWhen" VARCHAR2(50) ,
  "MaxConcurrency" NUMBER(10) DEFAULT 0 NOT NULL ENABLE,
  PRIMARY KEY  ("UID")
);


---
--- Table structure for table 'bpss_biz_partner_role'
---

CREATE TABLE "bpss_biz_partner_role" (
  "UID" NUMBER(19) DEFAULT 0 NOT NULL ENABLE,
  "Name" VARCHAR2(50) ,
  PRIMARY KEY  ("UID")
);


---
--- Table structure for table 'bpss_bin_coll_act'
---

CREATE TABLE "bpss_bin_coll_act" (
  "UID" NUMBER(19) DEFAULT 0 NOT NULL ENABLE,
  "Name" VARCHAR2(50) ,
  "BinCollProcessUId" NUMBER(19) ,
  "DownLinkUId" NUMBER(19) ,
  PRIMARY KEY  ("UID")
);


---
---
--- Table structure for table 'bpss_biz_tran'
---

CREATE TABLE "bpss_biz_tran" (
  "UID" NUMBER(19) DEFAULT 0 NOT NULL ENABLE,
  "Name" VARCHAR2(50) ,
  "Pattern" VARCHAR2(50) ,
  "PreCondition" VARCHAR2(50) ,
  "PostCondition" VARCHAR2(50) ,
  "IsDeliveryReq" NUMBER(1) DEFAULT 0,
  "BeginsWhen" VARCHAR2(50) ,
  "EndsWhen" VARCHAR2(50) ,
  "ReqUId" NUMBER(19) DEFAULT 0,
  "ResUId" NUMBER(19) DEFAULT 0,
  "MaxConcurrency" NUMBER(10) DEFAULT 0 NOT NULL ENABLE,
  PRIMARY KEY  ("UID")
);


---
--- Table structure for table 'bpss_biz_tran_activity'
---

CREATE TABLE "bpss_biz_tran_activity" (
  "UID" NUMBER(19) DEFAULT 0 NOT NULL ENABLE,
  "Name" VARCHAR2(50)  NOT NULL ENABLE,
  "TimeToPerform" VARCHAR2(16) ,
  "IsConcurrent" NUMBER(1) DEFAULT 0,
  "IsLegallyBinding" NUMBER(1) DEFAULT 0,
  "BusinessTransUId" NUMBER(19) ,
  "FromAuthorizedRole" VARCHAR2(50) ,
  "ToAuthorizedRole" VARCHAR2(50) ,
  PRIMARY KEY  ("UID")
);


---
---
--- Table structure for table 'bpss_coll_activity'
---

CREATE TABLE "bpss_coll_activity" (
  "UID" NUMBER(19) DEFAULT 0 NOT NULL ENABLE,
  "Name" VARCHAR2(100) ,
  "BinCollProcessUId" NUMBER(19) ,
  PRIMARY KEY  ("UID")
);


---
---
--- Table structure for table 'bpss_completion_state'
---

CREATE TABLE "bpss_completion_state" (
  "UID" NUMBER(19) DEFAULT 0 NOT NULL ENABLE,
  "ProcessUId" NUMBER(19) ,
  "ProcessType" VARCHAR2(50) ,
  "MpcUId" NUMBER(19) ,
  "ConditionGuard" VARCHAR2(50) ,
  "ExpressionLanguage" VARCHAR2(50) ,
  "ConditionExpression" VARCHAR2(250) ,
  "FromBusinessStateKey" VARCHAR2(100) ,
  "CompletionType" VARCHAR2(50) ,
  PRIMARY KEY  ("UID")
);


---
---
--- Table structure for table 'bpss_fork'
---

CREATE TABLE "bpss_fork" (
  "UID" NUMBER(19) DEFAULT 0 NOT NULL ENABLE,
  "Name" VARCHAR2(50) ,
  PRIMARY KEY  ("UID")
);


---
---
--- Table structure for table 'bpss_join'
---

CREATE TABLE "bpss_join" (
  "UID" NUMBER(19) DEFAULT 0 NOT NULL ENABLE,
  "Name" VARCHAR2(50) ,
  "WaitForAll" NUMBER(1) DEFAULT 0,
  PRIMARY KEY  ("UID")
);


---
---
--- Table structure for table 'bpss_multiparty_coll'
---

CREATE TABLE "bpss_multiparty_coll" (
  "UID" NUMBER(19) DEFAULT 0 NOT NULL ENABLE,
  "Name" VARCHAR2(50) ,
  "MaxConcurrency" NUMBER(10) DEFAULT 0 NOT NULL ENABLE,
  PRIMARY KEY  ("UID")
);


---
---
--- Table structure for table 'bpss_req_biz_activity'
---

CREATE TABLE "bpss_req_biz_activity" (
  "UID" NUMBER(19) DEFAULT 0 NOT NULL ENABLE,
  "Name" VARCHAR2(50) ,
  "IsIntelligibleChkReq" NUMBER(1) DEFAULT 0,
  "IsAuthReq" NUMBER(1) DEFAULT 0,
  "TimeToAckReceipt" VARCHAR2(16) ,
  "IsNonRepudiationReq" NUMBER(1) DEFAULT 0,
  "IsNonRepudiationOfReceiptReq" NUMBER(1) DEFAULT 0,
  "TimeToAckAccept" VARCHAR2(16) ,
  PRIMARY KEY  ("UID")
);


---
---
--- Table structure for table 'bpss_res_biz_activity'
---

CREATE TABLE "bpss_res_biz_activity" (
  "UID" NUMBER(19) DEFAULT 0 NOT NULL ENABLE,
  "Name" VARCHAR2(100) ,
  "IsIntelligibleChkReq" NUMBER(1) DEFAULT 0,
  "IsAuthReq" NUMBER(1) DEFAULT 0 NOT NULL ENABLE,
  "TimeToAckReceipt" VARCHAR2(16) ,
  "IsNonRepudiationReq" NUMBER(1) DEFAULT 0,
  "IsNonRepudiationOfReceiptReq" NUMBER(1) DEFAULT 0,
  PRIMARY KEY  ("UID")
);


---
---
--- Table structure for table 'bpss_start'
---

CREATE TABLE "bpss_start" (
  "UID" NUMBER(19) DEFAULT 0 NOT NULL ENABLE,
  "ProcessUId" NUMBER(19) ,
  "IsDownLink" NUMBER(1) DEFAULT 0,
  "ToBusinessStateKey" VARCHAR2(200) ,
  PRIMARY KEY  ("UID")
);


---
---
--- Table structure for table 'bpss_transition'
---

CREATE TABLE "bpss_transition" (
  "UID" NUMBER(19) DEFAULT 0 NOT NULL ENABLE,
  "ProcessUId" NUMBER(19) ,
  "ProcessType" VARCHAR2(50) ,
  "OnInitiation" CHAR(1) ,
  "ConditionGuard" VARCHAR2(50) ,
  "ExpressionLanguage" VARCHAR2(50) ,
  "ConditionExpression" VARCHAR2(250) ,
  "FromBusinessStateKey" VARCHAR2(100) ,
  "ToBusinessStateKey" VARCHAR2(100) ,
  PRIMARY KEY  ("UID")
);


---
--- Table structure for table 'bpss_businessdocument'
---

CREATE TABLE "bpss_businessdocument" (
  "UID" NUMBER(19) DEFAULT 0 NOT NULL ENABLE ,
  "Name" VARCHAR2(50) ,
  "ExpressionLanguage" VARCHAR2(50) ,
  "ConditionExpr" VARCHAR2(250) ,
  "SpecElement" VARCHAR2(50) ,
  "SpecLocation" VARCHAR2(80) ,
  PRIMARY KEY ("UID")
);


---
--- Table structure for table 'bpss_documentation'
---

CREATE TABLE "bpss_documentation" (
  "UID" NUMBER(19) DEFAULT 0 NOT NULL ENABLE ,
  "Uri" VARCHAR2(50) ,
  "Documentation" VARCHAR2(250) ,
  PRIMARY KEY ("UID")
);


---
--- Table structure for table 'bpss_documentenvelope'
---

CREATE TABLE "bpss_documentenvelope" (
  "UID" NUMBER(19) DEFAULT 0 NOT NULL ENABLE ,
  "BusinessDocumentName" VARCHAR2(50) ,
  "BusinessDocumentIDRef" VARCHAR2(50) ,
  "IsPositiveResponse" NUMBER(1) DEFAULT 0,
  "IsAuthenticated" NUMBER(1) DEFAULT 0,
  "IsConfidential" NUMBER(1) DEFAULT 0,
  "IsTamperProof" NUMBER(1) DEFAULT 0,
  PRIMARY KEY ("UID")
);


---
---
--- Table structure for table 'bpss_proc_spec'
---

CREATE TABLE "bpss_proc_spec" (
  "UID" NUMBER(19) DEFAULT 0 NOT NULL ENABLE,
  "UUId" VARCHAR2(80)  NOT NULL ENABLE,
  "Version" VARCHAR2(10)  NOT NULL ENABLE,
  "Name" VARCHAR2(100)  NOT NULL ENABLE,
  "Timestamp" VARCHAR2(100)  NOT NULL ENABLE,
  PRIMARY KEY  ("UID")
);


---
---
--- Table structure for table 'bpss_proc_spec_entry'
---

CREATE TABLE "bpss_proc_spec_entry" (
  "UID" NUMBER(19) DEFAULT 0 NOT NULL ENABLE,
  "SpecUId" NUMBER(19) DEFAULT 0 NOT NULL ENABLE,
  "EntryUId" NUMBER(19) DEFAULT 0 NOT NULL ENABLE,
  "Name" VARCHAR2(100) ,
  "Type" VARCHAR2(100)  NOT NULL ENABLE,
  "ParentEntryUId" NUMBER(19) DEFAULT 0,
  PRIMARY KEY  ("UID")
);


---
--- Table structure for table 'xpdl_activity'
---

CREATE TABLE "xpdl_activity" (
  "UID" NUMBER(19) DEFAULT 0 NOT NULL ENABLE ,
  "ActivityId" VARCHAR2(50) ,
  "ActivityName" VARCHAR2(50) ,
  "ActivityDescription" VARCHAR2(50) ,
  "ExtendedAttributes" BLOB NULL ,
  "ActivityLimit" NUMBER(8,0) ,
  "IsRoute" NUMBER(1) DEFAULT 0 NOT NULL ENABLE ,
  "ImplementationType" VARCHAR2(50) ,
  "PerformerId" VARCHAR2(50) ,
  "StartMode" VARCHAR2(50) ,
  "FinishMode" VARCHAR2(50) ,
  "Priority" NUMBER(19) ,
  "Instantiation" VARCHAR2(50) ,
  "Cost" NUMBER(8,0) ,
  "WaitingTime" NUMBER(8,0) ,
  "Duration" NUMBER(8,0) ,
  "IconUrl" VARCHAR2(50) ,
  "DocumentationUrl" VARCHAR2(50) ,
  "TransitionRestrictionListUId" NUMBER(19) ,
  "WorkingTime" NUMBER(8,0) ,
  "ProcessId" VARCHAR2(50) ,
  "PackageId" VARCHAR2(50) ,
  "PkgVersionId" VARCHAR2(50) ,
  PRIMARY KEY ("UID")
);


---
--- Table structure for table 'xpdl_application'
---

CREATE TABLE "xpdl_application" (
  "UID" NUMBER(19) DEFAULT 0 NOT NULL ENABLE ,
  "ApplicationId" VARCHAR2(50) ,
  "ApplicationName" VARCHAR2(50) ,
  "ApplicationDescription" VARCHAR2(255) ,
  "ExtendedAttributes" BLOB NULL ,
  "ProcessId" VARCHAR2(50) ,
  "PackageId" VARCHAR2(50) ,
  "PkgVersionId" VARCHAR2(50) ,
  PRIMARY KEY ("UID")
);


---
--- Table structure for table 'xpdl_datafield'
---

CREATE TABLE "xpdl_datafield" (
  "UID" NUMBER(19) DEFAULT 0 NOT NULL ENABLE ,
  "DataFieldId" VARCHAR2(50) ,
  "DataFieldName" VARCHAR2(50) ,
  "DataFieldDescription" VARCHAR2(50) ,
  "ExtendedAttributes" BLOB NULL ,
  "IsArray" NUMBER(1) DEFAULT 0 NOT NULL ENABLE ,
  "InitialValue" VARCHAR2(50) ,
  "LengthBytes" NUMBER(19) ,
  "ProcessId" VARCHAR2(50) ,
  "PackageId" VARCHAR2(50) ,
  "PkgVersionId" VARCHAR2(50) ,
  "DataTypeName" VARCHAR2(50) ,
  "ComplexDataTypeUId" NUMBER(19) ,
  PRIMARY KEY ("UID")
);


---
--- Table structure for table 'xpdl_externalpackage'
---

CREATE TABLE "xpdl_externalpackage" (
  "UID" NUMBER(19) DEFAULT 0 NOT NULL ENABLE ,
  "Href" VARCHAR2(50) ,
  "ExtendedAttributes" BLOB NULL ,
  "PackageId" VARCHAR2(50) ,
  "PkgVersionId" VARCHAR2(50) ,
  PRIMARY KEY ("UID")
);


---
--- Table structure for table 'xpdl_formalparam'
---

CREATE TABLE "xpdl_formalparam" (
  "UID" NUMBER(19) DEFAULT 0 NOT NULL ENABLE ,
  "FormalParamId" VARCHAR2(50) ,
  "Mode" VARCHAR2(50) ,
  "IndexNumber" NUMBER(10) ,
  "FormalParamDescription" VARCHAR2(50) ,
  "ApplicationId" VARCHAR2(50) ,
  "ProcessId" VARCHAR2(50) ,
  "PackageId" VARCHAR2(50) ,
  "PkgVersionId" VARCHAR2(50) ,
  "DataTypeName" VARCHAR2(50) ,
  "ComplexDataTypeUId" NUMBER(19) ,
  PRIMARY KEY ("UID")
);


---
--- Table structure for table 'xpdl_package'
---

CREATE TABLE "xpdl_package" (
  "UID" NUMBER(19) DEFAULT 0 NOT NULL ENABLE ,
  "PackageId" VARCHAR2(50) ,
  "PackageName" VARCHAR2(50) ,
  "PackageDescription" VARCHAR2(50) ,
  "ExtendedAttributes" BLOB NULL ,
  "SpecificationId" VARCHAR2(50) ,
  "SpecificationVersion" VARCHAR2(50) ,
  "SourceVendorInfo" VARCHAR2(50) ,
  "CreationDateTime" DATE ,
  "DocumentationUrl" VARCHAR2(50) ,
  "PriorityUnit" VARCHAR2(50) ,
  "CostUnit" VARCHAR2(50) ,
  "Author" VARCHAR2(50) ,
  "VersionId" VARCHAR2(50) ,
  "Codepage" VARCHAR2(50) ,
  "Countrykey" VARCHAR2(50) ,
  "PublicationStatus" VARCHAR2(50) ,
  "ResponsibleListUId" NUMBER(19) ,
  "GraphConformance" VARCHAR2(50) ,
  "State" NUMBER(1)  DEFAULT 1 NOT NULL,
  PRIMARY KEY ("UID")
);


---
--- Table structure for table 'xpdl_participant'
---

CREATE TABLE "xpdl_participant" (
  "UID" NUMBER(19) DEFAULT 0 NOT NULL ENABLE ,
  "ParticipantId" VARCHAR2(50) ,
  "ParticipantName" VARCHAR2(50) ,
  "ParticipantDescription" VARCHAR2(50) ,
  "ExtendedAttributes" BLOB NULL ,
  "ParticipantTypeId" VARCHAR2(50) ,
  "ProcessId" VARCHAR2(50) ,
  "PackageId" VARCHAR2(50) ,
  "PkgVersionId" VARCHAR2(50) ,
  PRIMARY KEY ("UID")
);


---
--- Table structure for table 'xpdl_participantlist'
---

CREATE TABLE "xpdl_participantlist" (
  "UID" NUMBER(19) DEFAULT 0 NOT NULL ENABLE ,
  "ParticipantId" VARCHAR2(50) ,
  "ParticipantIndex" NUMBER(19) ,
  "ListUId" NUMBER(19) ,
  PRIMARY KEY ("UID")
);


---
--- Table structure for table 'xpdl_process'
---

CREATE TABLE "xpdl_process" (
  "UID" NUMBER(19) DEFAULT 0 NOT NULL ENABLE ,
  "ProcessId" VARCHAR2(50) ,
  "ProcessName" VARCHAR2(50) ,
  "ProcessDescription" VARCHAR2(50) ,
  "ExtendedAttributes" BLOB NULL ,
  "DurationUnit" VARCHAR2(50) ,
  "CreationDateTime" DATE ,
  "HeaderDescription" VARCHAR2(50) ,
  "Priority" NUMBER(19) ,
  "ProcessLimit" NUMBER(8,0) ,
  "ValidFromDate" DATE ,
  "ValidToDate" DATE ,
  "WaitingTime" NUMBER(8,0) ,
  "WorkingTime" NUMBER(8,0) ,
  "Duration" NUMBER(8,0) ,
  "Author" VARCHAR2(50) ,
  "VersionId" VARCHAR2(50) ,
  "Codepage" VARCHAR2(50) ,
  "Countrykey" VARCHAR2(50) ,
  "PublicationStatus" VARCHAR2(50) ,
  "ResponsibleListUId" NUMBER(19) ,
  "PackageId" VARCHAR2(50) ,
  "DefaultStartActivityId" VARCHAR2(50) ,
  "PkgVersionId" VARCHAR2(50) ,
  PRIMARY KEY ("UID")
);


---
--- Table structure for table 'xpdl_subflow'
---

CREATE TABLE "xpdl_subflow" (
  "UID" NUMBER(19) DEFAULT 0 NOT NULL ENABLE ,
  "SubFlowId" VARCHAR2(100) ,
  "SubFlowType" VARCHAR2(50) ,
  "ActualParameters" VARCHAR2(255) ,
  "ActivityId" VARCHAR2(50) ,
  "ProcessId" VARCHAR2(50) ,
  "PackageId" VARCHAR2(50) ,
  "PkgVersionId" VARCHAR2(50) ,
  PRIMARY KEY ("UID")
);


---
--- Table structure for table 'xpdl_tool'
---

CREATE TABLE "xpdl_tool" (
  "UID" NUMBER(19) DEFAULT 0 NOT NULL ENABLE ,
  "ToolId" VARCHAR2(50) ,
  "ToolType" VARCHAR2(50) ,
  "ToolDescription" VARCHAR2(50) ,
  "ActualParameters" BLOB NULL ,
  "ExtendedAttributes" BLOB NULL ,
  "LoopKind" VARCHAR2(50) ,
  "ConditionExpr" VARCHAR2(50) ,
  "ActivityId" VARCHAR2(50) ,
  "ProcessId" VARCHAR2(50) ,
  "PackageId" VARCHAR2(50) ,
  "PkgVersionId" VARCHAR2(50) ,
  PRIMARY KEY ("UID")
);


---
--- Table structure for table 'xpdl_transition'
---

CREATE TABLE "xpdl_transition" (
  "UID" NUMBER(19) DEFAULT 0 NOT NULL ENABLE ,
  "TransitionId" VARCHAR2(50) ,
  "TransitionName" VARCHAR2(50) ,
  "TransitionDescription" VARCHAR2(50) ,
  "ExtendedAttributes" BLOB NULL ,
  "FromActivityId" VARCHAR2(50) ,
  "ToActivityId" VARCHAR2(50) ,
  "LoopType" VARCHAR2(50) ,
  "ConditionType" VARCHAR2(50) ,
  "ConditionExpr" VARCHAR2(150) ,
  "ProcessId" VARCHAR2(50) ,
  "PackageId" VARCHAR2(50) ,
  "PkgVersionId" VARCHAR2(50) ,
  PRIMARY KEY ("UID")
);


---
--- Table structure for table 'xpdl_transitionref'
---

CREATE TABLE "xpdl_transitionref" (
  "UID" NUMBER(19) DEFAULT 0 NOT NULL ENABLE ,
  "TransitionId" VARCHAR2(50) ,
  "ListUId" NUMBER(19) ,
  PRIMARY KEY ("UID")
);


---
--- Table structure for table 'xpdl_transitionrestriction'
---

CREATE TABLE "xpdl_transitionrestriction" (
  "UID" NUMBER(19) DEFAULT 0 NOT NULL ENABLE ,
  "IsInlineBlock" NUMBER(1) DEFAULT 0 NOT NULL ENABLE ,
  "BlockName" VARCHAR2(50) ,
  "BlockDescription" VARCHAR2(50) ,
  "BlockIconUrl" VARCHAR2(50) ,
  "BlockDocumentationUrl" VARCHAR2(50) ,
  "BlockBeginActivityId" VARCHAR2(50) ,
  "BlockEndActivityId" VARCHAR2(50) ,
  "JoinType" VARCHAR2(50) ,
  "SplitType" VARCHAR2(50) ,
  "TransitionRefListUId" NUMBER(19) ,
  "ListUId" NUMBER(19) ,
  PRIMARY KEY ("UID")
);


---
--- Table structure for table 'xpdl_typedeclaration'
---

CREATE TABLE "xpdl_typedeclaration" (
  "UID" NUMBER(19) DEFAULT 0 NOT NULL ENABLE ,
  "TypeId" VARCHAR2(50) ,
  "TypeName" VARCHAR2(50) ,
  "TypeDescription" VARCHAR2(50) ,
  "ExtendedAttributes" BLOB NULL ,
  "PackageId" VARCHAR2(50) ,
  "PkgVersionId" VARCHAR2(50) ,
  "DataTypeName" VARCHAR2(50) ,
  "ComplexDataTypeUId" NUMBER(19) ,
  PRIMARY KEY ("UID")
);


---
--- Table structure for table 'xpdl_complexdatatype'
---

CREATE TABLE "xpdl_complexdatatype" (
  "UID" NUMBER(19) DEFAULT 0 NOT NULL ENABLE ,
  "DataTypeName" VARCHAR2(50) ,
  "ComplexDataTypeUId" NUMBER(19) ,
  "SubTypeUId" NUMBER(19) ,
  "ArrayLowerIndex" NUMBER(10) ,
  "ArrayUpperIndex" NUMBER(10) ,
  PRIMARY KEY ("UID")
);


--- #### session ####
---
--- Table structure for table 'session_audit'
---

CREATE TABLE "session_audit" (
  "UID" NUMBER(19) NOT NULL,
  "SessionId" VARCHAR2(30) NOT NULL ENABLE,
  "SessionName" VARCHAR2(30) ,
  "State" NUMBER(5) DEFAULT 0 NOT NULL ENABLE,
  "SessionData" BLOB NULL ,
--  "OpenTime" DATE DEFAULT TO_DATE('0000-00-00', 'YYYY-MM-DD') NOT NULL ENABLE ,
--  "LastActiveTime" DATE DEFAULT TO_DATE('0000-00-00', 'YYYY-MM-DD') NOT NULL ENABLE,
  "OpenTime" DATE NOT NULL ENABLE ,
  "LastActiveTime" DATE NOT NULL ENABLE,
  "DestroyTime" DATE NULL,
  PRIMARY KEY ("UID"),
  CONSTRAINT "SESSION_AUDIT_CON" UNIQUE ("SessionId") ENABLE
);


--- #### time ####
---
--- Table structure for table 'ical_alarm'
---

CREATE TABLE "ical_alarm" (
  "UID" NUMBER(19) DEFAULT 0 NOT NULL ENABLE,
  "StartDuration" NUMBER(19) ,
  "StartDt" DATE ,
  "Related" NUMBER(10) ,
  "DelayPeriod" NUMBER(19) ,
  "RepeatCount" NUMBER(10) ,
  "Category" VARCHAR2(100) ,
  "SenderKey" VARCHAR2(255) ,
  "ReceiverKey" VARCHAR2(255) ,
  "Disabled" NUMBER(1) ,
  "NextDueTime" DATE ,
  "Count" NUMBER(10) ,
  "ParentUid" NUMBER(19) ,
  "ParentKind" NUMBER(5) ,
  "RecurListStr" VARCHAR2(2000) ,
  "IsRecurComplete" NUMBER(1) ,
  "CurRecur" VARCHAR2(80) ,
  "IsPseudoParent" NUMBER(1) ,
  "IncludeParentStartTime" NUMBER(1) ,
  "TaskId" VARCHAR2(120) 	
);


---
--- Table structure for table 'ical_event'
---

CREATE TABLE "ical_event" (
  "UID" NUMBER(19) DEFAULT 0 NOT NULL ENABLE,
  "Kind" NUMBER(5) ,
  "OwnerId" NUMBER(10) ,
  "Classification" VARCHAR2(80) ,
  "CreateDt" DATE ,
  "LastModifyDt" DATE ,
  "IsDateType" NUMBER(1) , 
  "IsDtFloat" NUMBER(1) ,
  "StartDt" DATE ,
  "Priority" NUMBER(10) ,
  "SequenceNum" NUMBER(10) ,
  "Status" NUMBER(10) ,
  "iCalUid" VARCHAR2(80) ,
  "EndDt" DATE ,
  "Duration" NUMBER(19) ,
  "PropertiesStr" VARCHAR2(1000) , 
  "TimeTransparency" NUMBER(10) 
);


---
--- Table structure for table 'ical_todo'
---

CREATE TABLE "ical_todo" (
  "UID" NUMBER(19) DEFAULT 0 NOT NULL ENABLE,
  "Kind" NUMBER(5) ,
  "OwnerId" NUMBER(10) ,
  "Classification" VARCHAR2(80) ,
  "CreateDt" DATE ,
  "LastModifyDt" DATE ,
  "IsDateType" NUMBER(1) ,
  "IsDtFloat" NUMBER(1) ,
  "StartDt" DATE ,
  "Priority" NUMBER(10) ,
  "SequenceNum" NUMBER(10) ,
  "Status" NUMBER(10) ,
  "iCalUid" VARCHAR2(80) ,
  "EndDt" DATE ,
  "Duration" NUMBER(19) ,
  "CompleteDt" DATE ,
  "PropertiesStr" VARCHAR2(1000) , 
  "PercentCompleted" NUMBER(10) 
);


---
--- Table structure for table 'ical_property'
---

CREATE TABLE "ical_property" (
  "UID" NUMBER(19) DEFAULT 0 NOT NULL ENABLE,
  "Kind" NUMBER(5) ,
  "CompKind" NUMBER(5) ,
  "iCalCompId" NUMBER(19) 
);


---
--- Table structure for table 'ical_int'
---

CREATE TABLE "ical_int" (
  "UID" NUMBER(19) DEFAULT 0 NOT NULL ENABLE,
  "CompKind" NUMBER(5) ,
  "PropKind" NUMBER(5) ,
  "ParamKind" NUMBER(5) ,
  "RefKind" NUMBER(5) ,
  "ValueKind" NUMBER(5) ,
  "iCalCompId" NUMBER(19) ,
  "RefId" NUMBER(19) ,
  "IntValue" NUMBER(10) 
);


---
--- Table structure for table 'ical_date'
---

CREATE TABLE "ical_date" (
  "UID" NUMBER(19) DEFAULT 0 NOT NULL ENABLE,
  "CompKind" NUMBER(5) ,
  "PropKind" NUMBER(5) ,
  "ParamKind" NUMBER(5) ,
  "RefKind" NUMBER(5) ,
  "ValueKind" NUMBER(5) ,
  "iCalCompId" NUMBER(19) ,
  "RefId" NUMBER(19) ,
  "DateValue" DATE 
);


---
--- Table structure for table 'ical_string'
---

CREATE TABLE "ical_string" (
  "UID" NUMBER(19) DEFAULT 0 NOT NULL ENABLE,
  "CompKind" NUMBER(5) ,
  "PropKind" NUMBER(5) ,
  "ParamKind" NUMBER(5) ,
  "RefKind" NUMBER(5) ,
  "ValueKind" NUMBER(5) ,
  "iCalCompId" NUMBER(19) ,
  "RefId" NUMBER(19) ,
  "StrValue" VARCHAR2(80) 
);


---
--- Table structure for table 'ical_text'
---

CREATE TABLE "ical_text" (
  "UID" NUMBER(19) DEFAULT 0 NOT NULL ENABLE,
  "CompKind" NUMBER(5) ,
  "PropKind" NUMBER(5) ,
  "ParamKind" NUMBER(5) ,
  "RefKind" NUMBER(5) ,
  "ValueKind" NUMBER(5) ,
  "iCalCompId" NUMBER(19) ,
  "RefId" NUMBER(19) ,
  "TextValue" VARCHAR2(1000) 
);


--- #### userprocedure ####
---
--- Table structure for table 'procedure_definition_file'
---

CREATE TABLE "procedure_definition_file" (
  "UID" NUMBER(19) DEFAULT 0 NOT NULL ENABLE,
  "Name" VARCHAR2(30) ,
  "Description" VARCHAR2(30) ,
  "Type" NUMBER(10) ,
  "FileName" VARCHAR2(30) ,
  "FilePath" VARCHAR2(30) ,
  "CanDelete" NUMBER(1) DEFAULT 1 NOT NULL ENABLE,
  "Version" NUMBER(7,5) DEFAULT 1 NOT NULL ENABLE,
   PRIMARY KEY ("UID"),
   CONSTRAINT "PROC_DEF_CON" UNIQUE ("Name") ENABLE
);

COMMENT ON TABLE "procedure_definition_file" IS 'Data Structure for Procedure Definition File';


---
--- Table structure for table 'user_procedure'
---

CREATE TABLE "user_procedure" (
  "UID" NUMBER(20) DEFAULT 0 NOT NULL ENABLE,
  "Name" VARCHAR2(30) ,
  "Description" VARCHAR2(80) , -- increase length from 40 to 80
  "IsSynchronous" NUMBER(1) ,
  "ProcType" NUMBER(10) ,
  "ProcDefFile" NUMBER(19) DEFAULT 0 NOT NULL ENABLE,
  "ProcDef" BLOB NULL,
  "ProcParamList" BLOB NULL,
  "ReturnDataType" NUMBER(10) ,
  "DefAction" NUMBER(10) ,
  "DefAlert" NUMBER(19) ,
  "ProcReturnList" BLOB NULL,
  "CanDelete" NUMBER(1) DEFAULT 1 NOT NULL ENABLE,
  "Version" NUMBER(7,5) DEFAULT 1 NOT NULL ENABLE,
  "GridDocField" NUMBER(3) ,
   PRIMARY KEY ("UID"),
   CONSTRAINT "USER_PROC_CON" UNIQUE ("Name") ENABLE
);


--- #### worklist ####
---
--- Table structure for table 'worklistvalue'
---

CREATE TABLE "worklistvalue" (
  "UID" NUMBER(19) DEFAULT 0 NOT NULL ENABLE,
  "wi_description" VARCHAR2(50) ,
  "wi_comments" VARCHAR2(50) ,
  "wi_status" CHAR(2) ,
  "wi_cdate" DATE ,
  "user_id" VARCHAR2(80) ,
  "unassigned" CHAR(2) ,
  "processDefKey" VARCHAR2(255) ,
  "activityId" VARCHAR2(255) ,
  "performer" VARCHAR2(150) ,
  "rtActivityUId" NUMBER(19) NOT NULL,
  "contextUId" NUMBER(19) NOT NULL,
  PRIMARY KEY  ("UID")
);


---
--- Table structure for table 'worklistuser'
---

CREATE TABLE "worklistuser" (
  "UID" NUMBER(19) DEFAULT 0 NOT NULL ENABLE,
  "workitem_id" NUMBER(19) DEFAULT 0 NOT NULL ENABLE,
  "user_id" VARCHAR2(80) ,
  PRIMARY KEY  ("UID")
);

COMMIT;
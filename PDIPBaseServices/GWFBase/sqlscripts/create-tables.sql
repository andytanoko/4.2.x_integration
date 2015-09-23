# 19 May 2003 GT 2.1 I1  [Neo Sok Lay] Expand xpdl_subflow.ActualParameters column size to 255 
# 06 Oct 2005 GT 4.0 I1  [Neo Sok Lay] Invalid default value for 'SpecUId' - rejected by MySQL5
#

USE userdb;

#
# Table structure for table 'bpss_bin_coll'
#

DROP TABLE IF EXISTS bpss_bin_coll;
CREATE TABLE `bpss_bin_coll` (
  `UID` bigint(11) NOT NULL default '0',
  `Name` varchar(50) default NULL,
  `Pattern` varchar(50) default NULL,
  `TimeToPerform` varchar(16) default NULL,
  `PreCondition` varchar(50) default NULL,
  `PostCondition` varchar(50) default NULL,
  `BeginsWhen` varchar(50) default NULL,
  `EndsWhen` varchar(50) default NULL,
  `MaxConcurrency` int(11) NOT NULL default '0',
  PRIMARY KEY  (`UID`)
) TYPE = MyISAM;


#
# Table structure for table 'bpss_biz_partner_role'
#

DROP TABLE IF EXISTS bpss_biz_partner_role;
CREATE TABLE `bpss_biz_partner_role` (
  `UID` bigint(11) NOT NULL default '0',
  `Name` varchar(50) default NULL,
  PRIMARY KEY  (`UID`)
) TYPE = MyISAM;


#
# Table structure for table 'bpss_bin_coll_act'
#

DROP TABLE IF EXISTS bpss_bin_coll_act;
CREATE TABLE `bpss_bin_coll_act` (
  `UID` bigint(11) NOT NULL default '0',
  `Name` varchar(50) default NULL,
  `BinCollProcessUId` bigint(11) default NULL,
  `DownLinkUId` bigint(11) default NULL,
  PRIMARY KEY  (`UID`)
) TYPE = MyISAM;

#
#
# Table structure for table 'bpss_biz_tran'
#

DROP TABLE IF EXISTS bpss_biz_tran;
CREATE TABLE `bpss_biz_tran` (
  `UID` bigint(11) NOT NULL default '0',
  `Name` varchar(50) default NULL,
  `Pattern` varchar(50) default NULL,
  `PreCondition` varchar(50) default NULL,
  `PostCondition` varchar(50) default NULL,
  `IsDeliveryReq` tinyint(1) default 0,
  `BeginsWhen` varchar(50) default NULL,
  `EndsWhen` varchar(50) default NULL,
  `ReqUId` bigint(11) default '0',
  `ResUId` bigint(11) default '0',
  `MaxConcurrency` int(11) NOT NULL default '0',
  PRIMARY KEY  (`UID`)
) TYPE = MyISAM;


#
# Table structure for table 'bpss_biz_tran_activity'
#

DROP TABLE IF EXISTS bpss_biz_tran_activity;
CREATE TABLE `bpss_biz_tran_activity` (
  `UID` bigint(11) NOT NULL default '0',
  `Name` varchar(50) NOT NULL default '',
  `TimeToPerform` varchar(16) default NULL,
  `IsConcurrent` tinyint(1) default 0,
  `IsLegallyBinding` tinyint(1) default 0,
  `BusinessTransUId` bigint(11) default NULL,
  `FromAuthorizedRole` varchar(50) ,
  `ToAuthorizedRole` varchar(50) ,
  PRIMARY KEY  (`UID`)
) TYPE = MyISAM;

#
#
# Table structure for table 'bpss_coll_activity'
#

DROP TABLE IF EXISTS bpss_coll_activity;
CREATE TABLE `bpss_coll_activity` (
  `UID` bigint(11) NOT NULL default '0',
  `Name` varchar(100) default NULL,
  `BinCollProcessUId` bigint(11) default NULL,
  PRIMARY KEY  (`UID`)
) TYPE = MyISAM;

#
#
# Table structure for table 'bpss_completion_state'
#

DROP TABLE IF EXISTS bpss_completion_state;
CREATE TABLE `bpss_completion_state` (
  `UID` bigint(11) NOT NULL default '0',
  `ProcessUId` bigint(11) default NULL,
  `ProcessType` varchar(50) default NULL,
  `MpcUId` bigint(11) default NULL,
  `ConditionGuard` varchar(50) default NULL,
  `ExpressionLanguage` varchar(50) default NULL,
  `ConditionExpression` varchar(250) default NULL,
  `FromBusinessStateKey` varchar(100) default NULL,
  `CompletionType` varchar(50) default NULL,
  PRIMARY KEY  (`UID`)
) TYPE = MyISAM;

#
#
# Table structure for table 'bpss_fork'
#

DROP TABLE IF EXISTS bpss_fork;
CREATE TABLE `bpss_fork` (
  `UID` bigint(11) NOT NULL default '0',
  `Name` varchar(50) default NULL,
  PRIMARY KEY  (`UID`)
) TYPE = MyISAM;

#
#
# Table structure for table 'bpss_join'
#

DROP TABLE IF EXISTS bpss_join;
CREATE TABLE `bpss_join` (
  `UID` bigint(11) NOT NULL default '0',
  `Name` varchar(50) default NULL,
  `WaitForAll` tinyint(1) default 0,
  PRIMARY KEY  (`UID`)
) TYPE = MyISAM;

#
#
# Table structure for table 'bpss_multiparty_coll'
#

DROP TABLE IF EXISTS bpss_multiparty_coll;
CREATE TABLE `bpss_multiparty_coll` (
  `UID` bigint(11) NOT NULL default '0',
  `Name` varchar(50) default NULL,
  `MaxConcurrency` tinyint(11) NOT NULL default '0',
  PRIMARY KEY  (`UID`)
) TYPE = MyISAM;

#
#
# Table structure for table 'bpss_req_biz_activity'
#

DROP TABLE IF EXISTS bpss_req_biz_activity;
CREATE TABLE `bpss_req_biz_activity` (
  `UID` bigint(11) NOT NULL default '0',
  `Name` varchar(50) default NULL,
  `IsIntelligibleChkReq` tinyint(1) default 0,
  `IsAuthReq` tinyint(1) default 0,
  `TimeToAckReceipt` varchar(16) default NULL,
  `IsNonRepudiationReq` tinyint(1) default 0,
  `IsNonRepudiationOfReceiptReq` tinyint(1) default 0,
  `TimeToAckAccept` varchar(16) default NULL,
  PRIMARY KEY  (`UID`)
) TYPE = MyISAM;

#
#
# Table structure for table 'bpss_res_biz_activity'
#

DROP TABLE IF EXISTS bpss_res_biz_activity;
CREATE TABLE `bpss_res_biz_activity` (
  `UID` bigint(11) NOT NULL default '0',
  `Name` varchar(100) default NULL,
  `IsIntelligibleChkReq` tinyint(1) default 0,
  `IsAuthReq` tinyint(1) NOT NULL default '0',
  `TimeToAckReceipt` varchar(16) default NULL,
  `IsNonRepudiationReq` tinyint(1) default 0,
  `IsNonRepudiationOfReceiptReq` tinyint(1) default 0,
  PRIMARY KEY  (`UID`)
) TYPE = MyISAM;

#
#
# Table structure for table 'bpss_start'
#

DROP TABLE IF EXISTS bpss_start;
CREATE TABLE `bpss_start` (
  `UID` bigint(11) NOT NULL default '0',
  `ProcessUId` bigint(11) default NULL,
  `IsDownLink` tinyint(1) default 0,
  `ToBusinessStateKey` varchar(200) default NULL,
  PRIMARY KEY  (`UID`)
) TYPE = MyISAM;

#
#
# Table structure for table 'bpss_transition'
#

DROP TABLE IF EXISTS bpss_transition;
CREATE TABLE `bpss_transition` (
  `UID` bigint(11) NOT NULL default '0',
  `ProcessUId` bigint(11) default NULL,
  `ProcessType` varchar(50) default NULL,
  `OnInitiation` char(1) default NULL,
  `ConditionGuard` varchar(50) default NULL,
  `ExpressionLanguage` varchar(50) default NULL,
  `ConditionExpression` varchar(250) default NULL,
  `FromBusinessStateKey` varchar(100) default NULL,
  `ToBusinessStateKey` varchar(100) default NULL,
  PRIMARY KEY  (`UID`)
) TYPE = MyISAM;

#
# Table structure for table 'bpss_businessdocument'
#

DROP TABLE IF EXISTS bpss_businessdocument;
CREATE TABLE IF NOT EXISTS bpss_businessdocument (
	UID bigint(11) NOT NULL DEFAULT '0' ,
	Name varchar(50) ,
        ExpressionLanguage varchar(50) default NULL,
	ConditionExpr varchar(250) default NULL,
	SpecElement varchar(50) ,
	SpecLocation varchar(50) ,
	PRIMARY KEY (UID)
) TYPE = MyISAM;


#
# Table structure for table 'bpss_documentation'
#

DROP TABLE IF EXISTS bpss_documentation;
CREATE TABLE IF NOT EXISTS bpss_documentation (
	UID bigint(11) NOT NULL DEFAULT '0' ,
	Uri varchar(50) ,
        Documentation varchar(80) ,
	PRIMARY KEY (UID)
) TYPE = MyISAM;

#
# Table structure for table 'bpss_documentenvelope'
#

DROP TABLE IF EXISTS bpss_documentenvelope;
CREATE TABLE IF NOT EXISTS bpss_documentenvelope (
	UID bigint(11) NOT NULL DEFAULT '0' ,
	BusinessDocumentName varchar(50) ,
	BusinessDocumentIDRef varchar(50) ,
	IsPositiveResponse tinyint(1) default 0,
	IsAuthenticated tinyint(1) default 0,
	IsConfidential tinyint(1) default 0,
	IsTamperProof tinyint(1) default 0,
	PRIMARY KEY (UID)
) TYPE = MyISAM;


#
#
# Table structure for table 'bpss_proc_spec'
#

DROP TABLE IF EXISTS bpss_proc_spec;
CREATE TABLE `bpss_proc_spec` (
  `UID` bigint(11) NOT NULL default '0',
  `UUId` varchar(80) NOT NULL default '',
  `Version` varchar(10) NOT NULL default '',
  `Name` varchar(100) NOT NULL default '',
  `Timestamp` varchar(100) NOT NULL default '',
  PRIMARY KEY  (`UID`)
) TYPE = MyISAM;

#
#
# Table structure for table 'bpss_proc_spec_entry'
#

DROP TABLE IF EXISTS bpss_proc_spec_entry;
CREATE TABLE `bpss_proc_spec_entry` (
  `UID` bigint(11) NOT NULL default '0',
  `SpecUId` bigint(11) NOT NULL default '0',
  `EntryUId` bigint(11) NOT NULL default '0',
  `Name` varchar(100) default '',
  `Type` varchar(100) NOT NULL default '',
  `ParentEntryUId` bigint(11) default '0',
  PRIMARY KEY  (`UID`)
) TYPE = MyISAM;


#
# Table structure for table 'xpdl_activity'
#

DROP TABLE IF EXISTS xpdl_activity;
CREATE TABLE IF NOT EXISTS xpdl_activity (
	UID bigint(20) NOT NULL DEFAULT '0' ,
	ActivityId varchar(50) ,
	ActivityName varchar(50) ,
	ActivityDescription varchar(50) ,
	ExtendedAttributes mediumblob ,
	ActivityLimit double ,
	IsRoute tinyint(1) NOT NULL DEFAULT '0' ,
	ImplementationType varchar(50) ,
	PerformerId varchar(50) ,
	StartMode varchar(50) ,
	FinishMode varchar(50) ,
	Priority bigint(20) ,
	Instantiation varchar(50) ,
	Cost double ,
	WaitingTime double ,
	Duration double ,
	IconUrl varchar(50) ,
	DocumentationUrl varchar(50) ,
	TransitionRestrictionListUId bigint(20) ,
	WorkingTime double ,
	ProcessId varchar(50) ,
	PackageId varchar(50) ,
	PkgVersionId varchar(50) ,
	PRIMARY KEY (UID)
) TYPE = MyISAM;


#
# Table structure for table 'xpdl_application'
#

DROP TABLE IF EXISTS xpdl_application;
CREATE TABLE IF NOT EXISTS xpdl_application (
	UID bigint(20) NOT NULL DEFAULT '0' ,
	ApplicationId varchar(50) ,
	ApplicationName varchar(50) ,
	ApplicationDescription varchar(50) ,
	ExtendedAttributes mediumblob ,
	ProcessId varchar(50) ,
	PackageId varchar(50) ,
	PkgVersionId varchar(50) ,
	PRIMARY KEY (UID)
) TYPE = MyISAM;


#
# Table structure for table 'xpdl_datafield'
#

DROP TABLE IF EXISTS xpdl_datafield;
CREATE TABLE IF NOT EXISTS xpdl_datafield (
	UID bigint(20) NOT NULL DEFAULT '0' ,
	DataFieldId varchar(50) ,
	DataFieldName varchar(50) ,
	DataFieldDescription varchar(50) ,
	ExtendedAttributes mediumblob ,
	IsArray tinyint(1) NOT NULL DEFAULT '0' ,
	InitialValue varchar(50) ,
	LengthBytes bigint(20) ,
	ProcessId varchar(50) ,
	PackageId varchar(50) ,
	PkgVersionId varchar(50) ,
        DataTypeName varchar(50) ,
        ComplexDataTypeUId bigint(20) ,
	PRIMARY KEY (UID)
) TYPE = MyISAM;


#
# Table structure for table 'xpdl_externalpackage'
#

DROP TABLE IF EXISTS xpdl_externalpackage;
CREATE TABLE IF NOT EXISTS xpdl_externalpackage (
	UID bigint(20) NOT NULL DEFAULT '0' ,
	Href varchar(50) ,
	ExtendedAttributes mediumblob ,
	PackageId varchar(50) ,
	PkgVersionId varchar(50) ,
	PRIMARY KEY (UID)
) TYPE = MyISAM;


#
# Table structure for table 'xpdl_formalparam'
#

DROP TABLE IF EXISTS xpdl_formalparam;
CREATE TABLE IF NOT EXISTS xpdl_formalparam (
	UID bigint(20) NOT NULL DEFAULT '0' ,
	FormalParamId varchar(50) ,
	Mode varchar(50) ,
	IndexNumber int(11) ,
	FormalParamDescription varchar(50) ,
	ApplicationId varchar(50) ,
	ProcessId varchar(50) ,
	PackageId varchar(50) ,
	PkgVersionId varchar(50) ,
        DataTypeName varchar(50) ,
        ComplexDataTypeUId bigint(20) ,
	PRIMARY KEY (UID)
) TYPE = MyISAM;


#
# Table structure for table 'xpdl_package'
#

DROP TABLE IF EXISTS xpdl_package;
CREATE TABLE IF NOT EXISTS xpdl_package (
	UID bigint(20) NOT NULL DEFAULT '0' ,
	PackageId varchar(50) ,
	PackageName varchar(50) ,
	PackageDescription varchar(50) ,
	ExtendedAttributes mediumblob ,
	SpecificationId varchar(50) ,
	SpecificationVersion varchar(50) ,
	SourceVendorInfo varchar(50) ,
	CreationDateTime datetime ,
	DocumentationUrl varchar(50) ,
	PriorityUnit varchar(50) ,
	CostUnit varchar(50) ,
	Author varchar(50) ,
	VersionId varchar(50) ,
	Codepage varchar(50) ,
	Countrykey varchar(50) ,
	PublicationStatus varchar(50) ,
	ResponsibleListUId bigint(20) ,
	GraphConformance varchar(50) ,
	State smallint(1)  DEFAULT 1 NOT NULL,
	PRIMARY KEY (UID)
) TYPE = MyISAM;


#
# Table structure for table 'xpdl_participant'
#

DROP TABLE IF EXISTS xpdl_participant;
CREATE TABLE IF NOT EXISTS xpdl_participant (
	UID bigint(20) NOT NULL DEFAULT '0' ,
	ParticipantId varchar(50) ,
	ParticipantName varchar(50) ,
	ParticipantDescription varchar(50) ,
	ExtendedAttributes mediumblob ,
	ParticipantTypeId varchar(50) ,
	ProcessId varchar(50) ,
	PackageId varchar(50) ,
	PkgVersionId varchar(50) ,
	PRIMARY KEY (UID)
) TYPE = MyISAM;


#
# Table structure for table 'xpdl_participantlist'
#

DROP TABLE IF EXISTS xpdl_participantlist;
CREATE TABLE IF NOT EXISTS xpdl_participantlist (
	UID bigint(20) NOT NULL DEFAULT '0' ,
	ParticipantId varchar(50) ,
	ParticipantIndex bigint(20) ,
	ListUId bigint(20) ,
	PRIMARY KEY (UID)
) TYPE = MyISAM;


#
# Table structure for table 'xpdl_process'
#

DROP TABLE IF EXISTS xpdl_process;
CREATE TABLE IF NOT EXISTS xpdl_process (
	UID bigint(20) NOT NULL DEFAULT '0' ,
	ProcessId varchar(50) ,
	ProcessName varchar(50) ,
	ProcessDescription varchar(50) ,
	ExtendedAttributes mediumblob ,
	DurationUnit varchar(50) ,
	CreationDateTime datetime ,
	HeaderDescription varchar(50) ,
	Priority bigint(20) ,
	ProcessLimit double ,
	ValidFromDate datetime ,
	ValidToDate datetime ,
	WaitingTime double ,
	WorkingTime double ,
	Duration double ,
	Author varchar(50) ,
	VersionId varchar(50) ,
	Codepage varchar(50) ,
	Countrykey varchar(50) ,
	PublicationStatus varchar(50) ,
	ResponsibleListUId bigint(20) ,
	PackageId varchar(50) ,
	DefaultStartActivityId varchar(50) ,
	PkgVersionId varchar(50) ,
	PRIMARY KEY (UID)
) TYPE = MyISAM;


#
# Table structure for table 'xpdl_subflow'
#

DROP TABLE IF EXISTS xpdl_subflow;
CREATE TABLE IF NOT EXISTS xpdl_subflow (
	UID bigint(20) NOT NULL DEFAULT '0' ,
	SubFlowId varchar(100) ,
	SubFlowType varchar(50) ,
	ActualParameters varchar(255) ,
	ActivityId varchar(50) ,
	ProcessId varchar(50) ,
	PackageId varchar(50) ,
	PkgVersionId varchar(50) ,
	PRIMARY KEY (UID)
) TYPE = MyISAM;


#
# Table structure for table 'xpdl_tool'
#

DROP TABLE IF EXISTS xpdl_tool;
CREATE TABLE IF NOT EXISTS xpdl_tool (
	UID bigint(20) NOT NULL DEFAULT '0' ,
	ToolId varchar(50) ,
	ToolType varchar(50) ,
	ToolDescription varchar(50) ,
	ActualParameters mediumblob ,
	ExtendedAttributes mediumblob ,
	LoopKind varchar(50) ,
	ConditionExpr varchar(50) ,
	ActivityId varchar(50) ,
	ProcessId varchar(50) ,
	PackageId varchar(50) ,
	PkgVersionId varchar(50) ,
	PRIMARY KEY (UID)
) TYPE = MyISAM;


#
# Table structure for table 'xpdl_transition'
#

DROP TABLE IF EXISTS xpdl_transition;
CREATE TABLE IF NOT EXISTS xpdl_transition (
	UID bigint(20) NOT NULL DEFAULT '0' ,
	TransitionId varchar(50) ,
	TransitionName varchar(50) ,
	TransitionDescription varchar(50) ,
	ExtendedAttributes mediumblob ,
	FromActivityId varchar(50) ,
	ToActivityId varchar(50) ,
	LoopType varchar(50) ,
	ConditionType varchar(50) ,
	ConditionExpr varchar(150) ,
	ProcessId varchar(50) ,
	PackageId varchar(50) ,
	PkgVersionId varchar(50) ,
	PRIMARY KEY (UID)
) TYPE = MyISAM;


#
# Table structure for table 'xpdl_transitionref'
#

DROP TABLE IF EXISTS xpdl_transitionref;
CREATE TABLE IF NOT EXISTS xpdl_transitionref (
	UID bigint(20) NOT NULL DEFAULT '0' ,
	TransitionId varchar(50) ,
	ListUId bigint(20) ,
	PRIMARY KEY (UID)
) TYPE = MyISAM;


#
# Table structure for table 'xpdl_transitionrestriction'
#

DROP TABLE IF EXISTS xpdl_transitionrestriction;
CREATE TABLE IF NOT EXISTS xpdl_transitionrestriction (
	UID bigint(20) NOT NULL DEFAULT '0' ,
	IsInlineBlock tinyint(1) NOT NULL DEFAULT '0' ,
	BlockName varchar(50) ,
	BlockDescription varchar(50) ,
	BlockIconUrl varchar(50) ,
	BlockDocumentationUrl varchar(50) ,
	BlockBeginActivityId varchar(50) ,
	BlockEndActivityId varchar(50) ,
	JoinType varchar(50) ,
	SplitType varchar(50) ,
	TransitionRefListUId bigint(20) ,
	ListUId bigint(20) ,
	PRIMARY KEY (UID)
) TYPE = MyISAM;


#
# Table structure for table 'xpdl_typedeclaration'
#

DROP TABLE IF EXISTS xpdl_typedeclaration;
CREATE TABLE IF NOT EXISTS xpdl_typedeclaration (
	UID bigint(20) NOT NULL DEFAULT '0' ,
	TypeId varchar(50) ,
	TypeName varchar(50) ,
	TypeDescription varchar(50) ,
	ExtendedAttributes mediumblob ,
	PackageId varchar(50) ,
	PkgVersionId varchar(50) ,
        DataTypeName varchar(50) ,
        ComplexDataTypeUId bigint(20) ,
	PRIMARY KEY (UID)
) TYPE = MyISAM;

#
# Table structure for table 'xpdl_complexdatatype'
#

DROP TABLE IF EXISTS xpdl_complexdatatype;
CREATE TABLE IF NOT EXISTS xpdl_complexdatatype (
        UID bigint(20) NOT NULL DEFAULT '0' ,
        DataTypeName varchar(50) ,
        ComplexDataTypeUId bigint(20) ,
        SubTypeUId bigint(20) ,
        ArrayLowerIndex bigint(11) ,
        ArrayUpperIndex bigint(11) ,
	PRIMARY KEY (UID)
) TYPE = MyISAM;


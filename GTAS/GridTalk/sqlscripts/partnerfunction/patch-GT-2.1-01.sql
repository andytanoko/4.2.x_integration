# 27 Aug 2003 v2.1 I1 [Koh Han Sing] Changed the XPDL structure for MappingRules.xpdl and UserProcedure.xpdl


use userdb;

DELETE FROM xpdl_activity WHERE PackageId IN ('MappingRules','UserProcedures') AND PkgVersionId <> '0';
DELETE FROM xpdl_application WHERE PackageId IN ('MappingRules','UserProcedures') AND PkgVersionId <> '0';
DELETE FROM xpdl_formalparam WHERE PackageId IN ('MappingRules','UserProcedures') AND PkgVersionId <> '0';
DELETE FROM xpdl_package WHERE PackageId IN ('MappingRules','UserProcedures') AND VersionId <> '0';
DELETE FROM xpdl_process WHERE PackageId IN ('MappingRules','UserProcedures') AND PkgVersionId <> '0';
DELETE FROM xpdl_tool WHERE PackageId IN ('MappingRules','UserProcedures') AND PkgVersionId <> '0';
DELETE FROM xpdl_transition WHERE PackageId IN ('MappingRules','UserProcedures') AND PkgVersionId <> '0';

--
-- Dumping data for table 'xpdl_activity'
--

INSERT INTO xpdl_activity (UID, ActivityId, ActivityName, ActivityDescription, ExtendedAttributes, ActivityLimit, IsRoute, ImplementationType, PerformerId, StartMode, FinishMode, Priority, Instantiation, Cost, WaitingTime, Duration, IconUrl, DocumentationUrl, TransitionRestrictionListUId, WorkingTime, ProcessId, PackageId, PkgVersionId) VALUES (-4,'START','','Start of Process',NULL,NULL,0,'No',NULL,'Automatic','Automatic',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'MappingRules','MappingRules','0');
INSERT INTO xpdl_activity (UID, ActivityId, ActivityName, ActivityDescription, ExtendedAttributes, ActivityLimit, IsRoute, ImplementationType, PerformerId, StartMode, FinishMode, Priority, Instantiation, Cost, WaitingTime, Duration, IconUrl, DocumentationUrl, TransitionRestrictionListUId, WorkingTime, ProcessId, PackageId, PkgVersionId) VALUES (-5,'END','','End of Process',NULL,NULL,0,'No',NULL,'Automatic','Automatic',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'MappingRules','MappingRules','0');
INSERT INTO xpdl_activity (UID, ActivityId, ActivityName, ActivityDescription, ExtendedAttributes, ActivityLimit, IsRoute, ImplementationType, PerformerId, StartMode, FinishMode, Priority, Instantiation, Cost, WaitingTime, Duration, IconUrl, DocumentationUrl, TransitionRestrictionListUId, WorkingTime, ProcessId, PackageId, PkgVersionId) VALUES (-6,'MAPPING_RULE','','GridTalk Mapping Rule',NULL,NULL,0,'Tool',NULL,'Automatic','Automatic',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'MappingRules','MappingRules','0');
INSERT INTO xpdl_activity (UID, ActivityId, ActivityName, ActivityDescription, ExtendedAttributes, ActivityLimit, IsRoute, ImplementationType, PerformerId, StartMode, FinishMode, Priority, Instantiation, Cost, WaitingTime, Duration, IconUrl, DocumentationUrl, TransitionRestrictionListUId, WorkingTime, ProcessId, PackageId, PkgVersionId) VALUES (-7,'START','','Start of Process',NULL,NULL,0,'No',NULL,'Automatic','Automatic',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'UserProcedures','UserProcedures','0');
INSERT INTO xpdl_activity (UID, ActivityId, ActivityName, ActivityDescription, ExtendedAttributes, ActivityLimit, IsRoute, ImplementationType, PerformerId, StartMode, FinishMode, Priority, Instantiation, Cost, WaitingTime, Duration, IconUrl, DocumentationUrl, TransitionRestrictionListUId, WorkingTime, ProcessId, PackageId, PkgVersionId) VALUES (-8,'END','','End of Process',NULL,NULL,0,'No',NULL,'Automatic','Automatic',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'UserProcedures','UserProcedures','0');
INSERT INTO xpdl_activity (UID, ActivityId, ActivityName, ActivityDescription, ExtendedAttributes, ActivityLimit, IsRoute, ImplementationType, PerformerId, StartMode, FinishMode, Priority, Instantiation, Cost, WaitingTime, Duration, IconUrl, DocumentationUrl, TransitionRestrictionListUId, WorkingTime, ProcessId, PackageId, PkgVersionId) VALUES (-9,'USER_PROCEDURE','','GridTalk User Procedure',NULL,NULL,0,'Tool',NULL,'Automatic','Automatic',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'UserProcedures','UserProcedures','0');


--
-- Dumping data for table 'xpdl_application'
--

INSERT INTO xpdl_application (UID, ApplicationId, ApplicationName, ApplicationDescription, ExtendedAttributes, ProcessId, PackageId, PkgVersionId) VALUES (-2,'MappingRuleAdapter','','Adapter to call java procedure to perform mapping','¬í\0t\0YAdapterMethod=execute|AdapterClass=com.gridnode.gtas.server.mapper.helpers.MapperDelegate','_NA_','MappingRules','0');
INSERT INTO xpdl_application (UID, ApplicationId, ApplicationName, ApplicationDescription, ExtendedAttributes, ProcessId, PackageId, PkgVersionId) VALUES (-3,'UserProcedureAdapter','','Adapter to call java procedure to perform mapping','¬í\0t\0gAdapterMethod=execute|AdapterClass=com.gridnode.gtas.server.userprocedure.helpers.UserProcedureDelegate','_NA_','UserProcedures','0');

--
-- Dumping data for table 'xpdl_formalparam'
--

INSERT INTO xpdl_formalparam (UID, FormalParamId, Mode, IndexNumber, FormalParamDescription, ApplicationId, ProcessId, PackageId, PkgVersionId, DataTypeName, ComplexDataTypeUId) VALUES (-9,'Param1','IN',1,NULL,'MappingRuleAdapter','_NA_','MappingRules','0','java.lang.String',NULL);
INSERT INTO xpdl_formalparam (UID, FormalParamId, Mode, IndexNumber, FormalParamDescription, ApplicationId, ProcessId, PackageId, PkgVersionId, DataTypeName, ComplexDataTypeUId) VALUES (-10,'Param2','IN',2,NULL,'MappingRuleAdapter','_NA_','MappingRules','0','java.util.Collection',NULL);
INSERT INTO xpdl_formalparam (UID, FormalParamId, Mode, IndexNumber, FormalParamDescription, ApplicationId, ProcessId, PackageId, PkgVersionId, DataTypeName, ComplexDataTypeUId) VALUES (-11,'Param3','OUT',3,NULL,'MappingRuleAdapter','_NA_','MappingRules','0','java.util.Collection',NULL);
INSERT INTO xpdl_formalparam (UID, FormalParamId, Mode, IndexNumber, FormalParamDescription, ApplicationId, ProcessId, PackageId, PkgVersionId, DataTypeName, ComplexDataTypeUId) VALUES (-12,'uid','IN',1,NULL,'_NA_','MappingRules','MappingRules','0','java.lang.String',NULL);
INSERT INTO xpdl_formalparam (UID, FormalParamId, Mode, IndexNumber, FormalParamDescription, ApplicationId, ProcessId, PackageId, PkgVersionId, DataTypeName, ComplexDataTypeUId) VALUES (-13,'in.gdocs','IN',2,NULL,'_NA_','MappingRules','MappingRules','0','java.util.Collection',NULL);
INSERT INTO xpdl_formalparam (UID, FormalParamId, Mode, IndexNumber, FormalParamDescription, ApplicationId, ProcessId, PackageId, PkgVersionId, DataTypeName, ComplexDataTypeUId) VALUES (-14,'out.gdocs','OUT',3,NULL,'_NA_','MappingRules','MappingRules','0','java.util.Collection',NULL);
INSERT INTO xpdl_formalparam (UID, FormalParamId, Mode, IndexNumber, FormalParamDescription, ApplicationId, ProcessId, PackageId, PkgVersionId, DataTypeName, ComplexDataTypeUId) VALUES (-15,'Param1','IN',1,NULL,'UserProcedureAdapter','_NA_','UserProcedures','0','java.lang.String',NULL);
INSERT INTO xpdl_formalparam (UID, FormalParamId, Mode, IndexNumber, FormalParamDescription, ApplicationId, ProcessId, PackageId, PkgVersionId, DataTypeName, ComplexDataTypeUId) VALUES (-16,'Param2','IN',2,NULL,'UserProcedureAdapter','_NA_','UserProcedures','0','java.util.Collection',NULL);
INSERT INTO xpdl_formalparam (UID, FormalParamId, Mode, IndexNumber, FormalParamDescription, ApplicationId, ProcessId, PackageId, PkgVersionId, DataTypeName, ComplexDataTypeUId) VALUES (-17,'Param3','OUT',3,NULL,'UserProcedureAdapter','_NA_','UserProcedures','0','java.util.Collection',NULL);
INSERT INTO xpdl_formalparam (UID, FormalParamId, Mode, IndexNumber, FormalParamDescription, ApplicationId, ProcessId, PackageId, PkgVersionId, DataTypeName, ComplexDataTypeUId) VALUES (-18,'uid','IN',1,NULL,'_NA_','UserProcedures','UserProcedures','0','java.lang.String',NULL);
INSERT INTO xpdl_formalparam (UID, FormalParamId, Mode, IndexNumber, FormalParamDescription, ApplicationId, ProcessId, PackageId, PkgVersionId, DataTypeName, ComplexDataTypeUId) VALUES (-19,'in.gdocs','IN',2,NULL,'_NA_','UserProcedures','UserProcedures','0','java.util.Collection',NULL);
INSERT INTO xpdl_formalparam (UID, FormalParamId, Mode, IndexNumber, FormalParamDescription, ApplicationId, ProcessId, PackageId, PkgVersionId, DataTypeName, ComplexDataTypeUId) VALUES (-20,'out.gdocs','OUT',3,NULL,'_NA_','UserProcedures','UserProcedures','0','java.util.Collection',NULL);


--
-- Dumping data for table 'xpdl_package'
--

INSERT INTO xpdl_package (UID, PackageId, PackageName, PackageDescription, ExtendedAttributes, SpecificationId, SpecificationVersion, SourceVendorInfo, CreationDateTime, DocumentationUrl, PriorityUnit, CostUnit, Author, VersionId, Codepage, Countrykey, PublicationStatus, ResponsibleListUId, GraphConformance) VALUES (-2,'MappingRules','',NULL,NULL,'XPDL',NULL,NULL,'2002-11-05 13:56:43',NULL,NULL,NULL,'GTAS','0',NULL,NULL,'',NULL,NULL);
INSERT INTO xpdl_package (UID, PackageId, PackageName, PackageDescription, ExtendedAttributes, SpecificationId, SpecificationVersion, SourceVendorInfo, CreationDateTime, DocumentationUrl, PriorityUnit, CostUnit, Author, VersionId, Codepage, Countrykey, PublicationStatus, ResponsibleListUId, GraphConformance) VALUES (-3,'UserProcedures','',NULL,NULL,'XPDL',NULL,NULL,'2002-11-05 13:56:43',NULL,NULL,NULL,'GTAS','0',NULL,NULL,'',NULL,NULL);

--
-- Dumping data for table 'xpdl_process'
--

INSERT INTO xpdl_process (UID, ProcessId, ProcessName, ProcessDescription, ExtendedAttributes, DurationUnit, CreationDateTime, HeaderDescription, Priority, ProcessLimit, ValidFromDate, ValidToDate, WaitingTime, WorkingTime, Duration, Author, VersionId, Codepage, Countrykey, PublicationStatus, ResponsibleListUId, PackageId, DefaultStartActivityId, PkgVersionId) VALUES (-2,'MappingRules','',NULL,NULL,'',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'MappingRules','START','0');
INSERT INTO xpdl_process (UID, ProcessId, ProcessName, ProcessDescription, ExtendedAttributes, DurationUnit, CreationDateTime, HeaderDescription, Priority, ProcessLimit, ValidFromDate, ValidToDate, WaitingTime, WorkingTime, Duration, Author, VersionId, Codepage, Countrykey, PublicationStatus, ResponsibleListUId, PackageId, DefaultStartActivityId, PkgVersionId) VALUES (-3,'UserProcedures','',NULL,NULL,'',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'UserProcedures','START','0');

--
-- Dumping data for table 'xpdl_tool'
--

INSERT INTO xpdl_tool (UID, ToolId, ToolType, ToolDescription, ActualParameters, ExtendedAttributes, LoopKind, ConditionExpr, ActivityId, ProcessId, PackageId, PkgVersionId) VALUES (-2,'MappingRuleAdapter','APPLICATION',NULL,'¬í\0t\0uid,in.gdocs,out.gdocs',NULL,NULL,NULL,'MAPPING_RULE','MappingRules','MappingRules','0');
INSERT INTO xpdl_tool (UID, ToolId, ToolType, ToolDescription, ActualParameters, ExtendedAttributes, LoopKind, ConditionExpr, ActivityId, ProcessId, PackageId, PkgVersionId) VALUES (-3,'UserProcedureAdapter','APPLICATION',NULL,'¬í\0t\0uid,in.gdocs,out.gdocs',NULL,NULL,NULL,'USER_PROCEDURE','UserProcedures','UserProcedures','0');

--
-- Dumping data for table 'xpdl_transition'
--

INSERT INTO xpdl_transition (UID, TransitionId, TransitionName, TransitionDescription, ExtendedAttributes, FromActivityId, ToActivityId, LoopType, ConditionType, ConditionExpr, ProcessId, PackageId, PkgVersionId) VALUES (-3,'START_MAPPING_RULE','',NULL,NULL,'START','MAPPING_RULE','',NULL,NULL,'MappingRules','MappingRules','0');
INSERT INTO xpdl_transition (UID, TransitionId, TransitionName, TransitionDescription, ExtendedAttributes, FromActivityId, ToActivityId, LoopType, ConditionType, ConditionExpr, ProcessId, PackageId, PkgVersionId) VALUES (-4,'MAPPING_RULE_END','',NULL,NULL,'MAPPING_RULE','END','',NULL,NULL,'MappingRules','MappingRules','0');
INSERT INTO xpdl_transition (UID, TransitionId, TransitionName, TransitionDescription, ExtendedAttributes, FromActivityId, ToActivityId, LoopType, ConditionType, ConditionExpr, ProcessId, PackageId, PkgVersionId) VALUES (-5,'START_USER_PROCEDURE','',NULL,NULL,'START','USER_PROCEDURE','',NULL,NULL,'UserProcedures','UserProcedures','0');
INSERT INTO xpdl_transition (UID, TransitionId, TransitionName, TransitionDescription, ExtendedAttributes, FromActivityId, ToActivityId, LoopType, ConditionType, ConditionExpr, ProcessId, PackageId, PkgVersionId) VALUES (-6,'USER_PROCEDURE_END','',NULL,NULL,'USER_PROCEDURE','END','',NULL,NULL,'UserProcedures','UserProcedures','0');


UPDATE xpdl_subflow SET SubFlowId='http://XPDL/MappingRules/0/XpdlProcess/MappingRules' WHERE ActivityId LIKE 'MappingRule%';
UPDATE xpdl_subflow SET SubFlowId='http://XPDL/UserProcedures/0/XpdlProcess/UserProcedures' WHERE ActivityId LIKE 'UserProcedure%';


# 29 Apr 2003 v2.1 I1 [Neo Sok Lay] Add Raise Alert Workflow Activity
# 

use appdb;

UPDATE fieldmetainfo 
SET Constraints="type=enum\r\nworkflowActivity.activityType.mappingRule=0\r\nworkflowActivity.activityType.userProcedure=1\r\nworkflowActivity.activityType.exitToImport=2\r\nworkflowActivity.activityType.exitToOutbound=3\r\nworkflowActivity.activityType.exitToExport=4\r\nworkflowActivity.activityType.exitWorkflow=5\r\nworkflowActivity.activityType.exitToPort=6\r\nworkflowActivity.activityType.saveToFolder=7\r\nworkflowActivity.activityType.exitToChannel=8\r\nworkflowActivity.activityType.raiseAlert=9"
WHERE FieldName="ACTIVITY_TYPE"
AND EntityObjectName="com.gridnode.gtas.server.partnerfunction.model.WorkflowActivity";

use userdb;

INSERT INTO xpdl_activity (UID, ActivityId, ActivityName, ActivityDescription, ExtendedAttributes, ActivityLimit, IsRoute, ImplementationType, PerformerId, StartMode, FinishMode, Priority, Instantiation, Cost, WaitingTime, Duration, IconUrl, DocumentationUrl, TransitionRestrictionListUId, WorkingTime, ProcessId, PackageId, PkgVersionId) VALUES (-1,'START','','Start of Process',NULL,NULL,0,'No',NULL,'Automatic','Automatic',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'Alert','Alert','1.0');
INSERT INTO xpdl_activity (UID, ActivityId, ActivityName, ActivityDescription, ExtendedAttributes, ActivityLimit, IsRoute, ImplementationType, PerformerId, StartMode, FinishMode, Priority, Instantiation, Cost, WaitingTime, Duration, IconUrl, DocumentationUrl, TransitionRestrictionListUId, WorkingTime, ProcessId, PackageId, PkgVersionId) VALUES (-2,'END','','End of Process',NULL,NULL,0,'No',NULL,'Automatic','Automatic',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'Alert','Alert','1.0');
INSERT INTO xpdl_activity (UID, ActivityId, ActivityName, ActivityDescription, ExtendedAttributes, ActivityLimit, IsRoute, ImplementationType, PerformerId, StartMode, FinishMode, Priority, Instantiation, Cost, WaitingTime, Duration, IconUrl, DocumentationUrl, TransitionRestrictionListUId, WorkingTime, ProcessId, PackageId, PkgVersionId) VALUES (-3,'RaiseAlert','','Raise Alert',NULL,NULL,0,'Tool',NULL,'Automatic','Automatic',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'Alert','Alert','1.0');

INSERT INTO xpdl_application (UID, ApplicationId, ApplicationName, ApplicationDescription, ExtendedAttributes, ProcessId, PackageId, PkgVersionId) VALUES (-1,'AlertAdapter','','Adapter to call java procedure to raise alert','¬í\0t\0bAdapterMethod=raiseAlert|AdapterClass=com.gridnode.gtas.server.docalert.helpers.RaiseAlertDelegate','_NA_','Alert','1.0');

INSERT INTO xpdl_formalparam (UID, FormalParamId, Mode, IndexNumber, FormalParamDescription, ApplicationId, ProcessId, PackageId, PkgVersionId, DataTypeName, ComplexDataTypeUId) VALUES (-1,'Param1','IN',1,NULL,'AlertAdapter','_NA_','Alert','1.0','java.lang.String',NULL);
INSERT INTO xpdl_formalparam (UID, FormalParamId, Mode, IndexNumber, FormalParamDescription, ApplicationId, ProcessId, PackageId, PkgVersionId, DataTypeName, ComplexDataTypeUId) VALUES (-2,'Param2','IN',2,NULL,'AlertAdapter','_NA_','Alert','1.0','java.lang.String',NULL);
INSERT INTO xpdl_formalparam (UID, FormalParamId, Mode, IndexNumber, FormalParamDescription, ApplicationId, ProcessId, PackageId, PkgVersionId, DataTypeName, ComplexDataTypeUId) VALUES (-3,'Param3','IN',3,NULL,'AlertAdapter','_NA_','Alert','1.0','java.util.Collection',NULL);
INSERT INTO xpdl_formalparam (UID, FormalParamId, Mode, IndexNumber, FormalParamDescription, ApplicationId, ProcessId, PackageId, PkgVersionId, DataTypeName, ComplexDataTypeUId) VALUES (-4,'Param4','OUT',4,NULL,'AlertAdapter','_NA_','Alert','1.0','java.util.Collection',NULL);
INSERT INTO xpdl_formalparam (UID, FormalParamId, Mode, IndexNumber, FormalParamDescription, ApplicationId, ProcessId, PackageId, PkgVersionId, DataTypeName, ComplexDataTypeUId) VALUES (-5,'alert.type','IN',1,NULL,'_NA_','Alert','Alert','1.0','java.lang.String',NULL);
INSERT INTO xpdl_formalparam (UID, FormalParamId, Mode, IndexNumber, FormalParamDescription, ApplicationId, ProcessId, PackageId, PkgVersionId, DataTypeName, ComplexDataTypeUId) VALUES (-6,'userdefined.alert','IN',2,NULL,'_NA_','Alert','Alert','1.0','java.lang.String',NULL);
INSERT INTO xpdl_formalparam (UID, FormalParamId, Mode, IndexNumber, FormalParamDescription, ApplicationId, ProcessId, PackageId, PkgVersionId, DataTypeName, ComplexDataTypeUId) VALUES (-7,'in.gdocs','IN',3,NULL,'_NA_','Alert','Alert','1.0','java.util.Collection',NULL);
INSERT INTO xpdl_formalparam (UID, FormalParamId, Mode, IndexNumber, FormalParamDescription, ApplicationId, ProcessId, PackageId, PkgVersionId, DataTypeName, ComplexDataTypeUId) VALUES (-8,'out.gdocs','OUT',4,NULL,'_NA_','Alert','Alert','1.0','java.util.Collection',NULL);

INSERT INTO xpdl_package (UID, PackageId, PackageName, PackageDescription, ExtendedAttributes, SpecificationId, SpecificationVersion, SourceVendorInfo, CreationDateTime, DocumentationUrl, PriorityUnit, CostUnit, Author, VersionId, Codepage, Countrykey, PublicationStatus, ResponsibleListUId, GraphConformance) VALUES (-1,'Alert','',NULL,NULL,'XPDL',NULL,NULL,'2003-04-29 10:24:00',NULL,NULL,NULL,'GTAS','1.0',NULL,NULL,'',NULL,NULL);

INSERT INTO xpdl_process (UID, ProcessId, ProcessName, ProcessDescription, ExtendedAttributes, DurationUnit, CreationDateTime, HeaderDescription, Priority, ProcessLimit, ValidFromDate, ValidToDate, WaitingTime, WorkingTime, Duration, Author, VersionId, Codepage, Countrykey, PublicationStatus, ResponsibleListUId, PackageId, DefaultStartActivityId, PkgVersionId) VALUES (-1,'Alert','',NULL,NULL,'',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'Alert','START','1.0');

INSERT INTO xpdl_tool (UID, ToolId, ToolType, ToolDescription, ActualParameters, ExtendedAttributes, LoopKind, ConditionExpr, ActivityId, ProcessId, PackageId, PkgVersionId) VALUES (-1,'AlertAdapter','APPLICATION',NULL,'¬í\0t\0/alert.type,userdefined.alert,in.gdocs,out.gdocs',NULL,NULL,NULL,'RaiseAlert','Alert','Alert','1.0');

INSERT INTO xpdl_transition (UID, TransitionId, TransitionName, TransitionDescription, ExtendedAttributes, FromActivityId, ToActivityId, LoopType, ConditionType, ConditionExpr, ProcessId, PackageId, PkgVersionId) VALUES (-1,'START_RaiseAlert','',NULL,NULL,'START','RaiseAlert','',NULL,NULL,'Alert','Alert','1.0');
INSERT INTO xpdl_transition (UID, TransitionId, TransitionName, TransitionDescription, ExtendedAttributes, FromActivityId, ToActivityId, LoopType, ConditionType, ConditionExpr, ProcessId, PackageId, PkgVersionId) VALUES (-2,'RaiseAlert_END','',NULL,NULL,'RaiseAlert','END','',NULL,NULL,'Alert','Alert','1.0');


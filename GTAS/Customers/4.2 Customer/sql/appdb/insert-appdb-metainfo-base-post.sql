-- -----------------------------------------------------------------
-- This script Creates default data for some of the tables in APPDB
-- -----------------------------------------------------------------
SET client_encoding = 'UTF8';
SET check_function_bodies = false;
SET client_min_messages = warning;
SET search_path = appdb;

---
--- data for table 'entitymetainfo'
---
--- Field 1 ObjectName: full qualified class name of the entity
--- Field 2 EntityName: short class name of the entity
--- Field 3 SqlName: table name of the entity
DELETE FROM "entitymetainfo" WHERE "EntityName" IN ('GWFWorkListValueEntity','GWFWorkListUserEntity');
INSERT INTO "entitymetainfo" VALUES ('com.gridnode.pdip.base.worklist.entities.model.GWFWorkListValueEntity','GWFWorkListValueEntity','"worklistvalue"');
INSERT INTO "entitymetainfo" VALUES ('com.gridnode.pdip.base.worklist.entities.model.GWFWorkListUserEntity','GWFWorkListUserEntity','"worklistuser"');

DELETE FROM "entitymetainfo" WHERE "EntityName" IN ('ProcedureDefFile','UserProcedure','JavaProcedure','ShellExecutable','SoapProcedure','ParamDef','ReturnDef');
INSERT INTO "entitymetainfo" VALUES ('com.gridnode.pdip.base.userprocedure.model.ProcedureDefFile','ProcedureDefFile','"procedure_definition_file"');
INSERT INTO "entitymetainfo" VALUES ('com.gridnode.pdip.base.userprocedure.model.UserProcedure','UserProcedure','"user_procedure"');
INSERT INTO "entitymetainfo" VALUES ('com.gridnode.pdip.base.userprocedure.model.JavaProcedure','JavaProcedure','');
INSERT INTO "entitymetainfo" VALUES ('com.gridnode.pdip.base.userprocedure.model.ShellExecutable','ShellExecutable','');
INSERT INTO "entitymetainfo" VALUES ('com.gridnode.pdip.base.userprocedure.model.SoapProcedure','SoapProcedure','');
INSERT INTO "entitymetainfo" VALUES ('com.gridnode.pdip.base.userprocedure.model.ParamDef','ParamDef','');
INSERT INTO "entitymetainfo" VALUES ('com.gridnode.pdip.base.userprocedure.model.ReturnDef','ReturnDef','');

DELETE FROM "entitymetainfo" WHERE "EntityName" LIKE '%iCalAlarm';
DELETE FROM "entitymetainfo" WHERE "EntityName" LIKE '%iCalEvent';
DELETE FROM "entitymetainfo" WHERE "EntityName" LIKE '%iCalTodo';
DELETE FROM "entitymetainfo" WHERE "EntityName" LIKE '%iCalProperty';
DELETE FROM "entitymetainfo" WHERE "EntityName" LIKE '%iCalInt';
DELETE FROM "entitymetainfo" WHERE "EntityName" LIKE '%iCalDate';
DELETE FROM "entitymetainfo" WHERE "EntityName" LIKE '%iCalString';
DELETE FROM "entitymetainfo" WHERE "EntityName" LIKE '%iCalText';
INSERT INTO "entitymetainfo" VALUES ('com.gridnode.pdip.base.time.entities.model.iCalAlarm','com.gridnode.pdip.base.time.entities.model.iCalAlarm','"ical_alarm"');
INSERT INTO "entitymetainfo" VALUES ('com.gridnode.pdip.base.time.entities.model.iCalEvent','com.gridnode.pdip.base.time.entities.model.iCalEvent','"ical_event"');
INSERT INTO "entitymetainfo" VALUES ('com.gridnode.pdip.base.time.entities.model.iCalTodo','com.gridnode.pdip.base.time.entities.model.iCalTodo','"ical_todo"');
INSERT INTO "entitymetainfo" VALUES ('com.gridnode.pdip.base.time.entities.model.iCalProperty','com.gridnode.pdip.base.time.entities.model.iCalProperty','"ical_property"');
INSERT INTO "entitymetainfo" VALUES ('com.gridnode.pdip.base.time.entities.model.iCalInt','com.gridnode.pdip.base.time.entities.model.iCalInt','"ical_int"');
INSERT INTO "entitymetainfo" VALUES ('com.gridnode.pdip.base.time.entities.model.iCalDate','com.gridnode.pdip.base.time.entities.model.iCalDate','"ical_date"');
INSERT INTO "entitymetainfo" VALUES ('com.gridnode.pdip.base.time.entities.model.iCalString','com.gridnode.pdip.base.time.entities.model.iCalString','"ical_string"');
INSERT INTO "entitymetainfo" VALUES ('com.gridnode.pdip.base.time.entities.model.iCalText','com.gridnode.pdip.base.time.entities.model.iCalText','"ical_text"');

DELETE FROM "entitymetainfo" WHERE "EntityName" = 'SessionAudit';
INSERT INTO "entitymetainfo" VALUES('com.gridnode.pdip.base.session.model.SessionAudit','SessionAudit','"session_audit"');

DELETE from "entitymetainfo" WHERE "EntityName" IN ('CountryCode','LanguageCode');
INSERT INTO "entitymetainfo" VALUES ('com.gridnode.pdip.base.locale.model.CountryCode', 'CountryCode', '"country_code"');
INSERT INTO "entitymetainfo" VALUES ('com.gridnode.pdip.base.locale.model.LanguageCode', 'LanguageCode', '"language_code"');

DELETE FROM "entitymetainfo" WHERE "EntityName" IN ('BpssReqBusinessActivity','BpssResBusinessActivity','BpssBusinessTransActivity','BpssCollaborationActivity','BpssBusinessTrans','BpssBinaryCollaboration');
INSERT INTO "entitymetainfo" VALUES('com.gridnode.pdip.base.gwfbase.bpss.model.BpssReqBusinessActivity','BpssReqBusinessActivity','"bpss_req_biz_activity"');
INSERT INTO "entitymetainfo" VALUES('com.gridnode.pdip.base.gwfbase.bpss.model.BpssResBusinessActivity','BpssResBusinessActivity','"bpss_res_biz_activity"');
INSERT INTO "entitymetainfo" VALUES('com.gridnode.pdip.base.gwfbase.bpss.model.BpssBusinessTransActivity','BpssBusinessTransActivity','"bpss_biz_tran_activity"');
INSERT INTO "entitymetainfo" VALUES('com.gridnode.pdip.base.gwfbase.bpss.model.BpssCollaborationActivity','BpssCollaborationActivity','"bpss_coll_activity"');
INSERT INTO "entitymetainfo" VALUES('com.gridnode.pdip.base.gwfbase.bpss.model.BpssBusinessTrans','BpssBusinessTrans','"bpss_biz_tran"');
INSERT INTO "entitymetainfo" VALUES('com.gridnode.pdip.base.gwfbase.bpss.model.BpssBinaryCollaboration','BpssBinaryCollaboration','"bpss_bin_coll"');

DELETE FROM "entitymetainfo" WHERE "EntityName" IN ('BpssBinaryCollaborationActivity','BpssMultiPartyCollaboration','BpssTransition','BpssCompletionState','BpssFork','BpssJoin');
INSERT INTO "entitymetainfo" VALUES('com.gridnode.pdip.base.gwfbase.bpss.model.BpssBinaryCollaborationActivity','BpssBinaryCollaborationActivity','"bpss_bin_coll_act"');
INSERT INTO "entitymetainfo" VALUES('com.gridnode.pdip.base.gwfbase.bpss.model.BpssMultiPartyCollaboration','BpssMultiPartyCollaboration','"bpss_multiparty_coll"');
INSERT INTO "entitymetainfo" VALUES('com.gridnode.pdip.base.gwfbase.bpss.model.BpssTransition','BpssTransition','"bpss_transition"');
INSERT INTO "entitymetainfo" VALUES('com.gridnode.pdip.base.gwfbase.bpss.model.BpssCompletionState','BpssCompletionState','"bpss_completion_state"');
INSERT INTO "entitymetainfo" VALUES('com.gridnode.pdip.base.gwfbase.bpss.model.BpssFork','BpssFork','"bpss_fork"');
INSERT INTO "entitymetainfo" VALUES('com.gridnode.pdip.base.gwfbase.bpss.model.BpssJoin','BpssJoin','"bpss_join"');

DELETE FROM "entitymetainfo" WHERE "EntityName" IN ('BpssProcessSpecification','BpssProcessSpecEntry','BpssStart','BpssBusinessPartnerRole','BpssBusinessDocument','BpssDocumentation','BpssDocumentEnvelope');
INSERT INTO "entitymetainfo" VALUES('com.gridnode.pdip.base.gwfbase.bpss.model.BpssProcessSpecification','BpssProcessSpecification','"bpss_proc_spec"');
INSERT INTO "entitymetainfo" VALUES('com.gridnode.pdip.base.gwfbase.bpss.model.BpssProcessSpecEntry','BpssProcessSpecEntry','"bpss_proc_spec_entry"');
INSERT INTO "entitymetainfo" VALUES('com.gridnode.pdip.base.gwfbase.bpss.model.BpssStart','BpssStart','"bpss_start"');
INSERT INTO "entitymetainfo" VALUES('com.gridnode.pdip.base.gwfbase.bpss.model.BpssBusinessPartnerRole','BpssBusinessPartnerRole','"bpss_biz_partner_role"');
INSERT INTO "entitymetainfo" VALUES('com.gridnode.pdip.base.gwfbase.bpss.model.BpssBusinessDocument','BpssBusinessDocument','"bpss_businessdocument"');
INSERT INTO "entitymetainfo" VALUES('com.gridnode.pdip.base.gwfbase.bpss.model.BpssDocumentation','BpssDocumentation','"bpss_documentation"');
INSERT INTO "entitymetainfo" VALUES('com.gridnode.pdip.base.gwfbase.bpss.model.BpssDocumentEnvelope','BpssDocumentEnvelope','"bpss_documentenvelope"');

DELETE FROM "entitymetainfo" WHERE "EntityName" IN ('XpdlActivity','XpdlApplication','XpdlDataField','XpdlExternalPackage','XpdlFormalParam','XpdlPackage','XpdlParticipant');
INSERT INTO "entitymetainfo" VALUES('com.gridnode.pdip.base.gwfbase.xpdl.model.XpdlActivity','XpdlActivity','"xpdl_activity"');
INSERT INTO "entitymetainfo" VALUES('com.gridnode.pdip.base.gwfbase.xpdl.model.XpdlApplication','XpdlApplication','"xpdl_application"');
INSERT INTO "entitymetainfo" VALUES('com.gridnode.pdip.base.gwfbase.xpdl.model.XpdlDataField','XpdlDataField','"xpdl_datafield"');
INSERT INTO "entitymetainfo" VALUES('com.gridnode.pdip.base.gwfbase.xpdl.model.XpdlExternalPackage','XpdlExternalPackage','"xpdl_externalpackage"');
INSERT INTO "entitymetainfo" VALUES('com.gridnode.pdip.base.gwfbase.xpdl.model.XpdlFormalParam','XpdlFormalParam','"xpdl_formalparam"');
INSERT INTO "entitymetainfo" VALUES('com.gridnode.pdip.base.gwfbase.xpdl.model.XpdlPackage','XpdlPackage','"xpdl_package"');
INSERT INTO "entitymetainfo" VALUES('com.gridnode.pdip.base.gwfbase.xpdl.model.XpdlParticipant','XpdlParticipant','"xpdl_participant"');

DELETE FROM "entitymetainfo" WHERE "EntityName" IN ('XpdlParticipantList','XpdlProcess','XpdlSubFlow','XpdlTool','XpdlTransition','XpdlTransitionRef','XpdlTransitionRestriction','XpdlTypeDeclaration','XpdlComplexDataType');
INSERT INTO "entitymetainfo" VALUES('com.gridnode.pdip.base.gwfbase.xpdl.model.XpdlParticipantList','XpdlParticipantList','"xpdl_participantlist"');
INSERT INTO "entitymetainfo" VALUES('com.gridnode.pdip.base.gwfbase.xpdl.model.XpdlProcess','XpdlProcess','"xpdl_process"');
INSERT INTO "entitymetainfo" VALUES('com.gridnode.pdip.base.gwfbase.xpdl.model.XpdlSubFlow','XpdlSubFlow','"xpdl_subflow"');
INSERT INTO "entitymetainfo" VALUES('com.gridnode.pdip.base.gwfbase.xpdl.model.XpdlTool','XpdlTool','"xpdl_tool"');
INSERT INTO "entitymetainfo" VALUES('com.gridnode.pdip.base.gwfbase.xpdl.model.XpdlTransition','XpdlTransition','"xpdl_transition"');
INSERT INTO "entitymetainfo" VALUES('com.gridnode.pdip.base.gwfbase.xpdl.model.XpdlTransitionRef','XpdlTransitionRef','"xpdl_transitionref"');
INSERT INTO "entitymetainfo" VALUES('com.gridnode.pdip.base.gwfbase.xpdl.model.XpdlTransitionRestriction','XpdlTransitionRestriction','"xpdl_transitionrestriction"');
INSERT INTO "entitymetainfo" VALUES('com.gridnode.pdip.base.gwfbase.xpdl.model.XpdlTypeDeclaration','XpdlTypeDeclaration','"xpdl_typedeclaration"');
INSERT INTO "entitymetainfo" VALUES('com.gridnode.pdip.base.gwfbase.xpdl.model.XpdlComplexDataType','XpdlComplexDataType','"xpdl_complexdatatype"');

DELETE FROM "entitymetainfo" WHERE "EntityName" IN ('StringData','ByteData','ContextData');
INSERT INTO "entitymetainfo" VALUES('com.gridnode.pdip.base.contextdata.entities.model.StringData','StringData','"data_stringdata"');
INSERT INTO "entitymetainfo" VALUES('com.gridnode.pdip.base.contextdata.entities.model.ByteData','ByteData','"data_bytedata"');
INSERT INTO "entitymetainfo" VALUES('com.gridnode.pdip.base.contextdata.entities.model.ContextData','ContextData','"data_contextdata"');

DELETE FROM "entitymetainfo" WHERE "EntityName" IN ('Certificate');
INSERT INTO "entitymetainfo" VALUES ('com.gridnode.pdip.base.certificate.model.Certificate','Certificate','"certificate"');

DELETE FROM "entitymetainfo" WHERE "EntityName" IN ('Role','SubjectRole','Feature','AccessRight');
INSERT INTO "entitymetainfo" VALUES('com.gridnode.pdip.base.acl.model.Role','Role','"role"');
INSERT INTO "entitymetainfo" VALUES('com.gridnode.pdip.base.acl.model.SubjectRole','SubjectRole','"subject_role"');
INSERT INTO "entitymetainfo" VALUES('com.gridnode.pdip.base.acl.model.Feature','Feature','"feature"');
INSERT INTO "entitymetainfo" VALUES('com.gridnode.pdip.base.acl.model.AccessRight','AccessRight','"access_right"');
COMMIT;


---
--- data for table 'fieldmetainfo'
---
--------- GWFWorkListValueEntity
DELETE FROM "fieldmetainfo" WHERE "EntityObjectName" LIKE '%GWFWorkListValueEntity';
INSERT INTO "fieldmetainfo" VALUES (DEFAULT,'_uId','UID','"UID"','java.lang.Long','com.gridnode.pdip.base.worklist.entities.model.GWFWorkListValueEntity','',0,0,0,1,1,'',999,'displayable=false','');
INSERT INTO "fieldmetainfo" VALUES (DEFAULT,'iCal_wi_description','ICAL_WI_DESCRIPTION','"wi_description"','java.lang.String','com.gridnode.pdip.base.worklist.entities.model.GWFWorkListValueEntity','Description',30,0,1,1,1,'desc',2,'displayable=false','');
INSERT INTO "fieldmetainfo" VALUES (DEFAULT,'iCal_comments','ICAL_COMMENTS','"wi_comments"','java.lang.String','com.gridnode.pdip.base.worklist.entities.model.GWFWorkListValueEntity','Document_type',30,0,1,0,1,'dcoType',1,'displayable=false','');
INSERT INTO "fieldmetainfo" VALUES (DEFAULT,'iCal_reqst_status','ICAL_REQST_STATUS','"wi_status"','java.lang.Boolean','com.gridnode.pdip.base.worklist.entities.model.GWFWorkListValueEntity','30',0,0,0,0,0,'uId',999,'displayable=false','');
INSERT INTO "fieldmetainfo" VALUES (DEFAULT,'iCal_Creation_DT','ICAL_CREATION_DT','"wi_cdate"','java.util.Date','com.gridnode.pdip.base.worklist.entities.model.GWFWorkListValueEntity','30',0,0,0,0,0,'canDelete',999,'displayable=false','');
INSERT INTO "fieldmetainfo" VALUES (DEFAULT,'user_id','USER_ID','"user_id"','java.lang.String','com.gridnode.pdip.base.worklist.entities.model.GWFWorkListValueEntity','30',10,0,0,0,0,'',999,'displayable=false','');
INSERT INTO "fieldmetainfo" VALUES (DEFAULT,'unassigned','UNASSIGNED','"unassigned"','java.lang.String','com.gridnode.pdip.base.worklist.entities.model.GWFWorkListValueEntity','30',10,0,0,0,0,'desc',999,'displayable=false','');
INSERT INTO "fieldmetainfo" VALUES (DEFAULT,'processDefKey','PROCESSDEF_KEY','"processDefKey"','java.lang.String','com.gridnode.pdip.base.worklist.entities.model.GWFWorkListValueEntity','80',10,0,0,0,0,'',999,'displayable=false','');
INSERT INTO "fieldmetainfo" VALUES (DEFAULT,'activityId','ACTIVITY_ID','"activityId"','java.lang.String','com.gridnode.pdip.base.worklist.entities.model.GWFWorkListValueEntity','30',10,0,0,0,0,'',999,'displayable=false','');
INSERT INTO "fieldmetainfo" VALUES (DEFAULT,'performer','PERFORMER','"performer"','java.lang.String','com.gridnode.pdip.base.worklist.entities.model.GWFWorkListValueEntity','30',10,0,0,0,0,'',999,'displayable=false','');
INSERT INTO "fieldmetainfo" VALUES (DEFAULT,'rtActivityUId','RTACTIVITY_UID','"rtActivityUId"','java.lang.Long','com.gridnode.pdip.base.worklist.entities.model.GWFWorkListValueEntity','',0,0,0,1,1,'',999,'displayable=false','');
INSERT INTO "fieldmetainfo" VALUES (DEFAULT,'contextUId','CONTEXT_UID','"contextUId"','java.lang.Long','com.gridnode.pdip.base.worklist.entities.model.GWFWorkListValueEntity','',0,0,0,1,1,'',999,'displayable=false','');

--------- GWFWorkListUserEntity
DELETE FROM "fieldmetainfo" WHERE "EntityObjectName" LIKE '%GWFWorkListUserEntity';
INSERT INTO "fieldmetainfo" VALUES (DEFAULT,'_uId','UID','"UID"','java.lang.Long','com.gridnode.pdip.base.worklist.entities.model.GWFWorkListUserEntity',NULL,0,0,0,1,1,'desc',999,'displayable=false','');
INSERT INTO "fieldmetainfo" VALUES (DEFAULT,'workitem_id','WORKITEM_ID','"workitem_id"','java.lang.Integer','com.gridnode.pdip.base.worklist.entities.model.GWFWorkListUserEntity','Document_type',30,0,1,0,1,'dcoType',1,'displayable=false','');
INSERT INTO "fieldmetainfo" VALUES (DEFAULT,'user_id','USER_ID','"user_id"','java.lang.String','com.gridnode.pdip.base.worklist.entities.model.GWFWorkListUserEntity','Document_type',30,0,1,0,1,'dcoType',1,'displayable=false','');

--------- UserProcedure.
DELETE FROM "fieldmetainfo" WHERE "EntityObjectName" LIKE '%UserProcedure';
INSERT INTO "fieldmetainfo" VALUES (DEFAULT,'_uId','UID','"UID"','java.lang.Long','com.gridnode.pdip.base.userprocedure.model.UserProcedure','userProcedure.uid',20,0,0,0,0,'0',999,'displayable=false'||chr(13)||chr(10)||'mandatory=false'||chr(13)||chr(10)||'editable=false'||chr(13)||chr(10),'type=uid'||chr(13)||chr(10));
INSERT INTO "fieldmetainfo" VALUES(DEFAULT,'_version','VERSION','"Version"','java.lang.Double','com.gridnode.pdip.base.userprocedure.model.UserProcedure','',0,0,0,0,0,'',999,'displayable=false'||chr(13)||chr(10)||'mandatory=false'||chr(13)||chr(10)||'editable=false','type=range');
INSERT INTO "fieldmetainfo" VALUES(DEFAULT,'_canDelete','CAN_DELETE','"CanDelete"','java.lang.Boolean','com.gridnode.pdip.base.userprocedure.model.UserProcedure','',0,0,0,0,0,'',999,'displayable=false'||chr(13)||chr(10)||'mandatory=false'||chr(13)||chr(10)||'editable=false','type=enum'||chr(13)||chr(10)||'candelete.enabled=true'||chr(13)||chr(10)||'candelete.disabled=false');
INSERT INTO "fieldmetainfo" VALUES (DEFAULT,'_name','NAME','"Name"','java.lang.String','com.gridnode.pdip.base.userprocedure.model.UserProcedure','userProcedure.name',15,0,0,0,1,'0',1,'displayable=true'||chr(13)||chr(10)||'editable.create=true'||chr(13)||chr(10)||'mandatory=true'||chr(13)||chr(10),'type=text'||chr(13)||chr(10)||'text.length.max=30'||chr(13)||chr(10));
INSERT INTO "fieldmetainfo" VALUES (DEFAULT,'_description','DESCRIPTION','"Description"','java.lang.String','com.gridnode.pdip.base.userprocedure.model.UserProcedure','userProcedure.description',15,0,0,0,1,'0',1,'displayable=true'||chr(13)||chr(10)||'editable=true'||chr(13)||chr(10)||'mandatory=true'||chr(13)||chr(10),'type=text'||chr(13)||chr(10)||'text.length.max=80'||chr(13)||chr(10));
INSERT INTO "fieldmetainfo" VALUES (DEFAULT,'_isSynchronous','IS_SYNCHRONOUS','"IsSynchronous"','java.lang.Boolean','com.gridnode.pdip.base.userprocedure.model.UserProcedure','userProcedure.isSynchronous',0,0,0,0,0,'0',999,'displayable=true'||chr(13)||chr(10)||'editable=true'||chr(13)||chr(10)||'mandatory=false'||chr(13)||chr(10),'type=enum'||chr(13)||chr(10)||'userProcedure.isSynchronous.yes=true'||chr(13)||chr(10)||'userProcedure.isSynchronous.no=false'||chr(13)||chr(10));
INSERT INTO "fieldmetainfo" VALUES (DEFAULT,'_procType','PROC_TYPE','"ProcType"','java.lang.Integer','com.gridnode.pdip.base.userprocedure.model.UserProcedure','userProcedure.procType',15,0,0,0,1,'0',1,'displayable=true'||chr(13)||chr(10)||'editable=true'||chr(13)||chr(10)||'mandatory=true'||chr(13)||chr(10),'type=enum'||chr(13)||chr(10)||'userProcedure.procType.executable=1'||chr(13)||chr(10)||'userProcedure.procType.java=2'||chr(13)||chr(10)||'userProcedure.procType.soap=3'||chr(13)||chr(10));
INSERT INTO "fieldmetainfo" VALUES (DEFAULT,'_procDefFile','PROC_DEF_FILE','"ProcDefFile"','com.gridnode.pdip.base.userprocedure.model.ProcedureDefFile','com.gridnode.pdip.base.userprocedure.model.UserProcedure','userProcedure.procDefFile',0,0,0,0,0,'0',999,'displayable=true'||chr(13)||chr(10)||'editable=true'||chr(13)||chr(10)||'mandatory=true'||chr(13)||chr(10),'type=foreign'||chr(13)||chr(10)||'foreign.key=procedureDefFile.uid'||chr(13)||chr(10)||'foreign.display=procedureDefFile.name'||chr(13)||chr(10)||'foreign.cached=true'||chr(13)||chr(10));
INSERT INTO "fieldmetainfo" VALUES (DEFAULT,'_procDef','PROC_DEF','"ProcDef"','com.gridnode.pdip.base.userprocedure.model.ProcedureDef','com.gridnode.pdip.base.userprocedure.model.UserProcedure','userProcedure.procDef',0,0,0,0,0,'0',999,'displayable=true'||chr(13)||chr(10)||'editable=true'||chr(13)||chr(10)||'mandatory=true'||chr(13)||chr(10),'type=dynamic'||chr(13)||chr(10)||'dynamic.types=shellExecutable;javaProcedure;soapProcedure'||chr(13)||chr(10));
INSERT INTO "fieldmetainfo" VALUES (DEFAULT,'_procParamList','PROC_PARAM_LIST','"ProcParamList"','java.util.Vector','com.gridnode.pdip.base.userprocedure.model.UserProcedure','userProcedure.procParamList',0,0,0,0,0,'0',999,'displayable=true'||chr(13)||chr(10)||'editable=true'||chr(13)||chr(10)||'mandatory=false'||chr(13)||chr(10),'type=embedded'||chr(13)||chr(10)||'embedded.type=paramDef'||chr(13)||chr(10)||'collection=true'||chr(13)||chr(10));
INSERT INTO "fieldmetainfo" VALUES (DEFAULT,'_returnDataType','RETURN_DATA_TYPE','"ReturnDataType"','java.lang.Integer','com.gridnode.pdip.base.userprocedure.model.UserProcedure','userProcedure.returnDataType',15,0,0,0,1,'0',1,'displayable=true'||chr(13)||chr(10)||'editable=true'||chr(13)||chr(10)||'mandatory=false'||chr(13)||chr(10),'type=enum'||chr(13)||chr(10)||'userProcedure.returnDataType.string=1'||chr(13)||chr(10)||'userProcedure.returnDataType.integer=2'||chr(13)||chr(10)||'userProcedure.returnDataType.long=3'||chr(13)||chr(10)||'userProcedure.returnDataType.double=4'||chr(13)||chr(10)||'userProcedure.returnDataType.boolean=5'||chr(13)||chr(10)||'userProcedure.returnDataType.date=6'||chr(13)||chr(10)||'userProcedure.returnDataType.object=7'||chr(13)||chr(10));
INSERT INTO "fieldmetainfo" VALUES (DEFAULT,'_defAction','DEF_ACTION','"DefAction"','java.lang.Integer','com.gridnode.pdip.base.userprocedure.model.UserProcedure','userProcedure.defAction',15,0,0,0,1,'0',1,'displayable=true'||chr(13)||chr(10)||'editable=true'||chr(13)||chr(10)||'mandatory=false'||chr(13)||chr(10),'type=enum'||chr(13)||chr(10)||'userProcedure.defAction.continue=1'||chr(13)||chr(10)||'userProcedure.defAction.abort=2'||chr(13)||chr(10));
INSERT INTO "fieldmetainfo" VALUES (DEFAULT,'_defAlert','DEF_ALERT','"DefAlert"','java.lang.Long','com.gridnode.pdip.base.userprocedure.model.UserProcedure','userProcedure.defAlert',15,0,0,0,1,'0',1,'displayable=true'||chr(13)||chr(10)||'editable=true'||chr(13)||chr(10)||'mandatory=false'||chr(13)||chr(10),'type=foreign'||chr(13)||chr(10)||'foreign.key=alert.uid'||chr(13)||chr(10)||'foreign.display=alert.description'||chr(13)||chr(10));
INSERT INTO "fieldmetainfo" VALUES (DEFAULT,'_procReturnList','PROC_RETURN_LIST','"ProcReturnList"','java.util.Vector','com.gridnode.pdip.base.userprocedure.model.UserProcedure','userProcedure.procReturnList',0,0,0,0,0,'0',999,'displayable=true'||chr(13)||chr(10)||'editable=true'||chr(13)||chr(10)||'mandatory=false'||chr(13)||chr(10),'type=embedded'||chr(13)||chr(10)||'embedded.type=returnDef'||chr(13)||chr(10)||'collection=true'||chr(13)||chr(10));
INSERT INTO "fieldmetainfo" VALUES (DEFAULT,'_gridDocField','GRID_DOC_FIELD','"GridDocField"','java.lang.Integer','com.gridnode.pdip.base.userprocedure.model.UserProcedure','userProcedure.gridDocField',0,0,0,0,0,'0',999,'displayable=true'||chr(13)||chr(10)||'editable=true'||chr(13)||chr(10)||'mandatory=false'||chr(13)||chr(10),'type=enum'||chr(13)||chr(10)||'gridDocument.custom1=77'||chr(13)||chr(10)||'gridDocument.custom2=78'||chr(13)||chr(10)||'gridDocument.custom3=79'||chr(13)||chr(10)||'gridDocument.custom4=80'||chr(13)||chr(10)||'gridDocument.custom5=81'||chr(13)||chr(10)||'gridDocument.custom6=82'||chr(13)||chr(10)||'gridDocument.custom7=83'||chr(13)||chr(10)||'gridDocument.custom8=84'||chr(13)||chr(10)||'gridDocument.custom9=85'||chr(13)||chr(10)||'gridDocument.custom10=86'||chr(13)||chr(10));

--------- ProcedureDefinition File.
DELETE FROM "fieldmetainfo" WHERE "EntityObjectName" LIKE '%ProcedureDefFile';
INSERT INTO "fieldmetainfo" VALUES (DEFAULT,'_uId','UID','"UID"','java.lang.Long','com.gridnode.pdip.base.userprocedure.model.ProcedureDefFile','procedureDefFile.uid',20,0,0,0,0,'0',999,'displayable=false'||chr(13)||chr(10)||'editable=false','type=uid');
INSERT INTO "fieldmetainfo" VALUES(DEFAULT,'_version','VERSION','"Version"','java.lang.Double','com.gridnode.pdip.base.userprocedure.model.ProcedureDefFile','',0,0,0,0,0,'',999,'displayable=false'||chr(13)||chr(10)||'mandatory=false'||chr(13)||chr(10)||'editable=false','type=range');
INSERT INTO "fieldmetainfo" VALUES(DEFAULT,'_canDelete','CAN_DELETE','"CanDelete"','java.lang.Boolean','com.gridnode.pdip.base.userprocedure.model.ProcedureDefFile','',0,0,0,0,0,'',999,'displayable=false'||chr(13)||chr(10)||'mandatory=false'||chr(13)||chr(10)||'editable=false','type=enum'||chr(13)||chr(10)||'candelete.enabled=true'||chr(13)||chr(10)||'candelete.disabled=false');
INSERT INTO "fieldmetainfo" VALUES (DEFAULT,'_name','NAME','"Name"','java.lang.String','com.gridnode.pdip.base.userprocedure.model.ProcedureDefFile','procedureDefFile.name',15,0,0,0,1,'0',1,'displayable=true'||chr(13)||chr(10)||'editable.create=true'||chr(13)||chr(10)||'mandatory=true'||chr(13)||chr(10),'type=text'||chr(13)||chr(10)||'text.length.max=30');
INSERT INTO "fieldmetainfo" VALUES (DEFAULT,'_description','DESCRIPTION','"Description"','java.lang.String','com.gridnode.pdip.base.userprocedure.model.ProcedureDefFile','procedureDefFile.description',15,0,0,0,1,'0',1,'displayable=true'||chr(13)||chr(10)||'editable=true'||chr(13)||chr(10)||'mandatory=true'||chr(13)||chr(10),'type=text'||chr(13)||chr(10)||'text.length.max=80');
INSERT INTO "fieldmetainfo" VALUES (DEFAULT,'_type','TYPE','"Type"','java.lang.Integer','com.gridnode.pdip.base.userprocedure.model.ProcedureDefFile','procedureDefFile.type',15,0,0,0,1,'0',1,'displayable=true'||chr(13)||chr(10)||'editable=true'||chr(13)||chr(10)||'mandatory=true'||chr(13)||chr(10),'type=enum'||chr(13)||chr(10)||'procedureDefFile.type.executable=1'||chr(13)||chr(10)||'procedureDefFile.type.java=2'||chr(13)||chr(10)||'procedureDefFile.type.soap=3'||chr(13)||chr(10));
INSERT INTO "fieldmetainfo" VALUES (DEFAULT,'_fileName','FILE_NAME','"FileName"','java.lang.String','com.gridnode.pdip.base.userprocedure.model.ProcedureDefFile','procedureDefFile.fileName',30,0,0,0,1,'0',1,'displayable=true'||chr(13)||chr(10)||'editable=true'||chr(13)||chr(10)||'mandatory=true'||chr(13)||chr(10),'type=file'||chr(13)||chr(10)||'file.downloadable=false'||chr(13)||chr(10)||'file.pathKey=procedureDefFile.filePath'||chr(13)||chr(10));
INSERT INTO "fieldmetainfo" VALUES (DEFAULT,'_filePath','FILE_PATH','"FilePath"','java.lang.String','com.gridnode.pdip.base.userprocedure.model.ProcedureDefFile','procedureDefFile.filePath',30,0,0,0,1,'0',1,'displayable=false'||chr(13)||chr(10)||'editable=false'||chr(13)||chr(10)||'mandatory=false'||chr(13)||chr(10),'type=file'||chr(13)||chr(10));

--------- JavaProcedure -- Note SQLName is empty as this are entities are not persistant. To Confirm regd cmdLineExpr (CMDLineExpression).
DELETE FROM "fieldmetainfo" WHERE "EntityObjectName" LIKE '%JavaProcedure';
INSERT INTO "fieldmetainfo" VALUES (DEFAULT,'_classPath','CLASS_PATH','','java.lang.String','com.gridnode.pdip.base.userprocedure.model.JavaProcedure','javaProcedure.classPath',80,0,0,0,1,'0',1,'displayable=true'||chr(13)||chr(10)||'editable=true'||chr(13)||chr(10)||'mandatory=true'||chr(13)||chr(10),'type=text'||chr(13)||chr(10)||'text.length.max=80');
INSERT INTO "fieldmetainfo" VALUES (DEFAULT,'_className','CLASS_NAME','','java.lang.String','com.gridnode.pdip.base.userprocedure.model.JavaProcedure','javaProcedure.className',80,0,0,0,1,'0',1,'displayable=true'||chr(13)||chr(10)||'editable=true'||chr(13)||chr(10)||'mandatory=true'||chr(13)||chr(10),'type=text'||chr(13)||chr(10)||'text.length.max=80');
INSERT INTO "fieldmetainfo" VALUES (DEFAULT,'_methodName','METHOD_NAME','','java.lang.String','com.gridnode.pdip.base.userprocedure.model.JavaProcedure','javaProcedure.methodName',80,0,0,0,1,'0',1,'displayable=true'||chr(13)||chr(10)||'editable=true'||chr(13)||chr(10)||'mandatory=true'||chr(13)||chr(10),'type=text'||chr(13)||chr(10)||'text.length.max=80');
INSERT INTO "fieldmetainfo" VALUES (DEFAULT,'_jvmOptions','JVM_OPTIONS','','java.lang.String','com.gridnode.pdip.base.userprocedure.model.JavaProcedure','javaProcedure.jvmOptions',30,0,0,0,1,'0',1,'displayable=true'||chr(13)||chr(10)||'editable=true'||chr(13)||chr(10)||'mandatory=false'||chr(13)||chr(10),'type=text'||chr(13)||chr(10)||'text.length.max=30');
INSERT INTO "fieldmetainfo" VALUES (DEFAULT,'_arguments','ARGUMENTS','','java.lang.String','com.gridnode.pdip.base.userprocedure.model.JavaProcedure','javaProcedure.arguments',30,0,0,0,1,'0',1,'displayable=true'||chr(13)||chr(10)||'editable=true'||chr(13)||chr(10)||'mandatory=false'||chr(13)||chr(10),'type=text'||chr(13)||chr(10)||'text.length.max=150');
INSERT INTO "fieldmetainfo" VALUES (DEFAULT,'_isLocal','IS_LOCAL','','java.lang.Boolean','com.gridnode.pdip.base.userprocedure.model.JavaProcedure','javaProcedure.isLocal',0,0,0,0,0,'',999,'displayable=true'||chr(13)||chr(10)||'editable=true','type=enum'||chr(13)||chr(10)||'javaProcedure.isLocal.true=true'||chr(13)||chr(10)||'javaProcedure.isLocal.false=false');
INSERT INTO "fieldmetainfo" VALUES (DEFAULT,'_type','TYPE','','java.lang.Integer','com.gridnode.pdip.base.userprocedure.model.JavaProcedure','javaProcedure.type',0,0,0,0,0,'',999,'displayable=false'||chr(13)||chr(10)||'editable=false','type=enum'||chr(13)||chr(10)||'javaProcedure.type.executable=1'||chr(13)||chr(10)||'javaProcedure.type.java=2'||chr(13)||chr(10)||'javaProcedure.type.soap=3'||chr(13)||chr(10));

--------- ShellExecutable -- Note SQLName is empty as this are entities are not persistant
DELETE FROM "fieldmetainfo" WHERE "EntityObjectName" LIKE '%ShellExecutable';
INSERT INTO "fieldmetainfo" VALUES (DEFAULT,'_arguments','ARGUMENTS','','java.lang.String','com.gridnode.pdip.base.userprocedure.model.ShellExecutable','shellExecutable.arguments',30,0,0,0,1,'0',1,'displayable=true'||chr(13)||chr(10)||'editable=true'||chr(13)||chr(10)||'mandatory=false'||chr(13)||chr(10),'type=text'||chr(13)||chr(10)||'text.length.max=150');
INSERT INTO "fieldmetainfo" VALUES (DEFAULT,'_type','TYPE','','java.lang.Integer','com.gridnode.pdip.base.userprocedure.model.ShellExecutable','shellExecutable.type',0,0,0,0,0,'',999,'displayable=false'||chr(13)||chr(10)||'editable=false','type=enum'||chr(13)||chr(10)||'shellExecutable.type.executable=1'||chr(13)||chr(10)||'shellExecutable.type.java=2'||chr(13)||chr(10)||'shellExecutable.type.soap=3'||chr(13)||chr(10));

--------- SoapProcedure -- Note SQLName is empty as this are entities are not persistant. To Confirm regd cmdLineExpr (CMDLineExpression).
DELETE FROM "fieldmetainfo" WHERE "EntityObjectName" LIKE '%SoapProcedure';
INSERT INTO "fieldmetainfo" VALUES (DEFAULT,'_methodName','METHOD_NAME','','java.lang.String','com.gridnode.pdip.base.userprocedure.model.SoapProcedure','soapProcedure.methodName',80,0,0,0,1,'0',1,'displayable=true'||chr(13)||chr(10)||'editable=true'||chr(13)||chr(10)||'mandatory=true'||chr(13)||chr(10),'type=text'||chr(13)||chr(10)||'text.length.max=80');
INSERT INTO "fieldmetainfo" VALUES (DEFAULT,'_type','TYPE','','java.lang.Integer','com.gridnode.pdip.base.userprocedure.model.SoapProcedure','soapProcedure.type',0,0,0,0,0,'','999','displayable=false'||chr(13)||chr(10)||'editable=false','type=enum'||chr(13)||chr(10)||'soapProcedure.type.executable=1'||chr(13)||chr(10)||'soapProcedure.type.java=2'||chr(13)||chr(10)||'soapProcedure.type.soap=3'||chr(13)||chr(10));
INSERT INTO "fieldmetainfo" VALUES (DEFAULT,'_username','USER_NAME','','java.lang.String','com.gridnode.pdip.base.userprocedure.model.SoapProcedure','soapProcedure.userName',80,0,0,0,1,'0',1,'displayable=true'||chr(13)||chr(10)||'editable=true'||chr(13)||chr(10)||'mandatory=false'||chr(13)||chr(10),'type=text'||chr(13)||chr(10)||'text.length.max=80');
INSERT INTO "fieldmetainfo" VALUES (DEFAULT,'_password','PASSWORD','','java.lang.String','com.gridnode.pdip.base.userprocedure.model.SoapProcedure','soapProcedure.password',80,0,0,0,1,'0',1,'displayable=true'||chr(13)||chr(10)||'editable=true'||chr(13)||chr(10)||'mandatory=false'||chr(13)||chr(10),'type=text'||chr(13)||chr(10)||'text.length.max=80');
--------- ParamDef -- Note SQLName is empty as this are entities are not persistant
DELETE FROM "fieldmetainfo" WHERE "EntityObjectName" LIKE '%ParamDef';
INSERT INTO "fieldmetainfo" VALUES (DEFAULT,'_name','NAME','','java.lang.String','com.gridnode.pdip.base.userprocedure.model.ParamDef','paramDef.name',30,0,0,0,1,'0',1,'displayable=true'||chr(13)||chr(10)||'editable=true'||chr(13)||chr(10)||'mandatory=true'||chr(13)||chr(10),'type=text'||chr(13)||chr(10)||'text.length.max=30');
INSERT INTO "fieldmetainfo" VALUES (DEFAULT,'_description','DESCRIPTION','','java.lang.String','com.gridnode.pdip.base.userprocedure.model.ParamDef','paramDef.description',30,0,0,0,1,'0',1,'displayable=true'||chr(13)||chr(10)||'editable=true'||chr(13)||chr(10)||'mandatory=true'||chr(13)||chr(10),'type=text'||chr(13)||chr(10)||'text.length.max=80');
INSERT INTO "fieldmetainfo" VALUES (DEFAULT,'_source','SOURCE','','java.lang.Integer','com.gridnode.pdip.base.userprocedure.model.ParamDef','paramDef.source',30,0,0,0,1,'0',1,'displayable=true'||chr(13)||chr(10)||'editable=true'||chr(13)||chr(10)||'mandatory=true'||chr(13)||chr(10),'type=enum'||chr(13)||chr(10)||'paramDef.source.userDefined=1'||chr(13)||chr(10)||'paramDef.source.gdoc=2'||chr(13)||chr(10)||'paramDef.source.udoc=3'||chr(13)||chr(10)||'paramDef.source.attachments=4'||chr(13)||chr(10));
INSERT INTO "fieldmetainfo" VALUES (DEFAULT,'_type','TYPE','','java.lang.Integer','com.gridnode.pdip.base.userprocedure.model.ParamDef','paramDef.type',30,0,0,0,1,'0',1,'displayable=true'||chr(13)||chr(10)||'editable=true'||chr(13)||chr(10)||'mandatory=true'||chr(13)||chr(10),'type=enum'||chr(13)||chr(10)||'paramDef.type.string=1'||chr(13)||chr(10)||'paramDef.type.integer=2'||chr(13)||chr(10)||'paramDef.type.long=3'||chr(13)||chr(10)||'paramDef.type.double=4'||chr(13)||chr(10)||'paramDef.type.boolean=5'||chr(13)||chr(10)||'paramDef.type.date=6'||chr(13)||chr(10)||'paramDef.type.object=7'||chr(13)||chr(10)||'paramDef.type.datahandler=8'||chr(13)||chr(10)||'paramDef.type.datahandlerArray=9'||chr(13)||chr(10)||'paramDef.type.stringArray=10'||chr(13)||chr(10)||'paramDef.type.byteArray=11'||chr(13)||chr(10)||'paramDef.type.byteArrayArray=12'||chr(13)||chr(10));
INSERT INTO "fieldmetainfo" VALUES (DEFAULT,'_value','VALUE','','java.lang.Object','com.gridnode.pdip.base.userprocedure.model.ParamDef','paramDef.value',30,0,0,0,1,'0',1,'displayable=true'||chr(13)||chr(10)||'editable=true'||chr(13)||chr(10)||'mandatory=true'||chr(13)||chr(10),'type=text'||chr(13)||chr(10)||'text.length.max=255');
INSERT INTO "fieldmetainfo" VALUES (DEFAULT,'_dateFormat','DATE_FORMAT','','java.lang.String','com.gridnode.pdip.base.userprocedure.model.ParamDef','paramDef.dateFormat',30,0,0,0,1,'0',1,'displayable=true'||chr(13)||chr(10)||'editable=true'||chr(13)||chr(10)||'mandatory=true'||chr(13)||chr(10),'type=enum'||chr(13)||chr(10)||'paramDef.dateFormat.format1=ddMMyyHHmmss'||chr(13)||chr(10)||'paramDef.dateFormat.format2=ddMMyyyyHHmmss'||chr(13)||chr(10)||'paramDef.dateFormat.format3=MMddyyHHmmss'||chr(13)||chr(10)||'paramDef.dateFormat.format4=MMddyyyyHHmmss'||chr(13)||chr(10)||'paramDef.dateFormat.format5=yyMMddHHmmss'||chr(13)||chr(10)||'paramDef.dateFormat.format6=yyyyMMddHHmmss'||chr(13)||chr(10)||'paramDef.dateFormat.format7=yyyyMMdd'||chr(13)||chr(10)||'paramDef.dateFormat.format8=yyMMdd'||chr(13)||chr(10)||'paramDef.dateFormat.format9=ddMMyyyy'||chr(13)||chr(10)||'paramDef.dateFormat.format10=ddMMyy'||chr(13)||chr(10)||'paramDef.dateFormat.format11=MMddyyyy'||chr(13)||chr(10)||'paramDef.dateFormat.format12=MMddyy');

--------- ReturnDef -- Note SQLName is empty as this are entities are not persistant
DELETE FROM "fieldmetainfo" WHERE "EntityObjectName" LIKE '%ReturnDef';
INSERT INTO "fieldmetainfo" VALUES (DEFAULT,'_operator','OPERATOR','','java.lang.Integer','com.gridnode.pdip.base.userprocedure.model.ReturnDef','returnDef.operator',30,0,0,0,1,'0',1,'displayable=true'||chr(13)||chr(10)||'editable=true'||chr(13)||chr(10)||'mandatory=true'||chr(13)||chr(10),'type=enum'||chr(13)||chr(10)||'returnDef.operator.equal=0'||chr(13)||chr(10)||'returnDef.operator.less=1'||chr(13)||chr(10)||'returnDef.operator.lessOrEqual=2'||chr(13)||chr(10)||'returnDef.operator.greaterOrEqual=3'||chr(13)||chr(10)||'returnDef.operator.greater=4'||chr(13)||chr(10)||'returnDef.operator.notEqual=5'||chr(13)||chr(10)||'returnDef.operator.between=6'||chr(13)||chr(10));
INSERT INTO "fieldmetainfo" VALUES (DEFAULT,'_value','VALUE','','java.lang.Object','com.gridnode.pdip.base.userprocedure.model.ReturnDef','returnDef.value',30,0,0,0,1,'0',1,'displayable=true'||chr(13)||chr(10)||'editable=true'||chr(13)||chr(10)||'mandatory=true'||chr(13)||chr(10),'type=text'||chr(13)||chr(10)||'text.length.max=30');
INSERT INTO "fieldmetainfo" VALUES (DEFAULT,'_action','ACTION','','java.lang.Integer','com.gridnode.pdip.base.userprocedure.model.ReturnDef','returnDef.actionType',30,0,0,0,1,'0',1,'displayable=true'||chr(13)||chr(10)||'editable=true'||chr(13)||chr(10)||'mandatory=true'||chr(13)||chr(10),'type=enum'||chr(13)||chr(10)||'returnDef.actionType.continue=1'||chr(13)||chr(10)||'returnDef.actionType.abort=2'||chr(13)||chr(10));
INSERT INTO "fieldmetainfo" VALUES (DEFAULT,'_alert','ALERT','','java.lang.Long','com.gridnode.pdip.base.userprocedure.model.ReturnDef','returnDef.alert',30,0,0,0,1,'0',1,'displayable=true'||chr(13)||chr(10)||'editable=true'||chr(13)||chr(10)||'mandatory=false'||chr(13)||chr(10),'type=foreign'||chr(13)||chr(10)||'foreign.key=alert.uid'||chr(13)||chr(10)||'foreign.display=alert.description'||chr(13)||chr(10));

--------- iCalAlarm
DELETE FROM "fieldmetainfo" WHERE "EntityObjectName" LIKE '%iCalAlarm';
INSERT INTO "fieldmetainfo" VALUES (DEFAULT,'_uId','UID','"UID"','java.lang.Long','com.gridnode.pdip.base.time.entities.model.iCalAlarm','',0,0,0,1,1,'',999,'displayable=false','');
INSERT INTO "fieldmetainfo" VALUES (DEFAULT,'_startDuration','START_DURATION','"StartDuration"','java.lang.Long','com.gridnode.pdip.base.time.entities.model.iCalAlarm','',0,0,0,1,1,'',999,'displayable=false','');
INSERT INTO "fieldmetainfo" VALUES (DEFAULT,'_startDt','START_DT','"StartDt"','java.util.Date','com.gridnode.pdip.base.time.entities.model.iCalAlarm','',0,0,0,1,1,'',999,'displayable=false','');
INSERT INTO "fieldmetainfo" VALUES (DEFAULT,'_related','RELATED','"Related"','java.lang.Integer','com.gridnode.pdip.base.time.entities.model.iCalAlarm','',0,0,0,1,1,'',999,'displayable=false','');
INSERT INTO "fieldmetainfo" VALUES (DEFAULT,'_delayPeriod','DELAY_PERIOD','"DelayPeriod"','java.lang.Long','com.gridnode.pdip.base.time.entities.model.iCalAlarm','',0,0,0,1,1,'',999,'displayable=false','');
INSERT INTO "fieldmetainfo" VALUES (DEFAULT,'_repeat','REPEAT','"RepeatCount"','java.lang.Integer','com.gridnode.pdip.base.time.entities.model.iCalAlarm','',0,0,0,1,1,'',999,'displayable=false','');
INSERT INTO "fieldmetainfo" VALUES (DEFAULT,'_category','CATEGORY','"Category"','java.lang.String','com.gridnode.pdip.base.time.entities.model.iCalAlarm','',50,0,0,1,1,'',999,'displayable=false','');
INSERT INTO "fieldmetainfo" VALUES (DEFAULT,'_senderKey','SENDER_KEY','"SenderKey"','java.lang.String','com.gridnode.pdip.base.time.entities.model.iCalAlarm','',50,0,0,1,1,'',999,'displayable=false','');
INSERT INTO "fieldmetainfo" VALUES (DEFAULT,'_receiverKey','RECEIVER_KEY','"ReceiverKey"','java.lang.String','com.gridnode.pdip.base.time.entities.model.iCalAlarm','',50,0,0,1,1,'',999,'displayable=false','');
INSERT INTO "fieldmetainfo" VALUES (DEFAULT,'_disabled','DISABLED','"Disabled"','java.lang.Boolean','com.gridnode.pdip.base.time.entities.model.iCalAlarm','',0,0,0,1,1,'',999,'displayable=false','');
INSERT INTO "fieldmetainfo" VALUES (DEFAULT,'_nextDueTime','NEXT_DUE_TIME','"NextDueTime"','java.util.Date','com.gridnode.pdip.base.time.entities.model.iCalAlarm','',0,0,0,1,1,'',999,'displayable=false','');
INSERT INTO "fieldmetainfo" VALUES (DEFAULT,'_count','COUNT','"Count"','java.lang.Integer','com.gridnode.pdip.base.time.entities.model.iCalAlarm','',0,0,0,1,1,'',999,'displayable=false','');
INSERT INTO "fieldmetainfo" VALUES (DEFAULT,'_parentUid','PARENT_UID','"ParentUid"','java.lang.Long','com.gridnode.pdip.base.time.entities.model.iCalAlarm', 'ical_alarm.parentUid', 0,0,0,1,1,'', 999,'displayable=false','');
INSERT INTO "fieldmetainfo" VALUES (DEFAULT,'_parentKind','PARENT_KIND','"ParentKind"','Short','com.gridnode.pdip.base.time.entities.model.iCalAlarm', 'ical_alarm.parentKind', 0,0,0,1,1,'', 999,'displayable=false','');
INSERT INTO "fieldmetainfo" VALUES (DEFAULT,'_recurListStr','RECUR_LIST_STR','"RecurListStr"','java.lang.String','com.gridnode.pdip.base.time.entities.model.iCalAlarm', 'ical_alarm.recurListStr', 50,0,0,1,1,'', 999,'displayable=false','');
INSERT INTO "fieldmetainfo" VALUES (DEFAULT,'_isRecurComplete','IS_RECUR_COMPLETE','"IsRecurComplete"','java.lang.Boolean','com.gridnode.pdip.base.time.entities.model.iCalAlarm', 'ical_alarm.isRecurComplete', 0,0,0,1,1,'', 999,'displayable=false','');
INSERT INTO "fieldmetainfo" VALUES (DEFAULT,'_curRecur','CUR_RECUR','"CurRecur"','java.lang.String','com.gridnode.pdip.base.time.entities.model.iCalAlarm', 'ical_alarm.curRecur', 50,0,0,1,1,'', 999,'displayable=false','');
INSERT INTO "fieldmetainfo" VALUES (DEFAULT,'_isPseudoParent','IS_PSEUDO_PARENT','"IsPseudoParent"','java.lang.Boolean','com.gridnode.pdip.base.time.entities.model.iCalAlarm', 'ical_alarm.isPseudoParent', 0,0,0,1,1,'', 999,'displayable=false','');
INSERT INTO "fieldmetainfo" VALUES (DEFAULT,'_includeParentStartTime','INCLUDE_PARENT_START_TIME','"IncludeParentStartTime"','java.lang.Boolean','com.gridnode.pdip.base.time.entities.model.iCalAlarm', 'ical_alarm.includeParentStartTime', 0,0,0,1,1,'', 999,'displayable=false','');
INSERT INTO "fieldmetainfo" VALUES (DEFAULT,'_taskId','TASK_ID','"TaskId"','java.lang.String','com.gridnode.pdip.base.time.entities.model.iCalAlarm','ical_alarm.taskId',120,0,0,1,1,'',999,'displayable=false','type=text'||chr(13)||chr(10)||'text.length.max=120'||chr(13)||chr(10));

--------- iCalEvent
DELETE FROM "fieldmetainfo" WHERE "EntityObjectName" LIKE '%iCalEvent';
INSERT INTO "fieldmetainfo" VALUES (DEFAULT,'_uId','UID','"UID"','java.lang.Long','com.gridnode.pdip.base.time.entities.model.iCalEvent','',0,0,0,1,1,'',999,'displayable=false','');
INSERT INTO "fieldmetainfo" VALUES (DEFAULT,'_kind','KIND','"Kind"','java.lang.Short','com.gridnode.pdip.base.time.entities.model.iCalEvent','',0,0,0,1,1,'',999,'displayable=false','');
INSERT INTO "fieldmetainfo" VALUES (DEFAULT,'_ownerId','OWNER_ID','"OwnerId"','java.lang.Integer','com.gridnode.pdip.base.time.entities.model.iCalEvent','',0,0,0,1,1,'',999,'displayable=false','');
INSERT INTO "fieldmetainfo" VALUES (DEFAULT,'_classification','CLASSIFICATION','"Classification"','java.lang.String','com.gridnode.pdip.base.time.entities.model.iCalEvent','',50,0,0,1,1,'',999,'displayable=false','');
INSERT INTO "fieldmetainfo" VALUES (DEFAULT,'_createDt','CREATE_DT','"CreateDt"','java.util.Date','com.gridnode.pdip.base.time.entities.model.iCalEvent','',0,0,0,1,1,'',999,'displayable=false','');
INSERT INTO "fieldmetainfo" VALUES (DEFAULT,'_lastModifyDt','LAST_MODIFY_DT','"LastModifyDt"','java.util.Date','com.gridnode.pdip.base.time.entities.model.iCalEvent','',0,0,0,1,1,'',999,'displayable=false','');
INSERT INTO "fieldmetainfo" VALUES (DEFAULT,'_isDateType','IS_DATE_TYPE','"IsDateType"','java.lang.Boolean','com.gridnode.pdip.base.time.entities.model.iCalEvent','',0,0,0,1,1,'',999,'displayable=false','');
INSERT INTO "fieldmetainfo" VALUES (DEFAULT,'_isDtFloat','IS_DT_FLOAT','"IsDtFloat"','java.lang.Boolean','com.gridnode.pdip.base.time.entities.model.iCalEvent','',0,0,0,1,1,'',999,'displayable=false','');
INSERT INTO "fieldmetainfo" VALUES (DEFAULT,'_startDt','START_DT','"StartDt"','java.util.Date','com.gridnode.pdip.base.time.entities.model.iCalEvent','',0,0,0,1,1,'',999,'displayable=false','');
INSERT INTO "fieldmetainfo" VALUES (DEFAULT,'_priority','PRIORITY','"Priority"','java.lang.Integer','com.gridnode.pdip.base.time.entities.model.iCalEvent','',0,0,0,1,1,'',999,'displayable=false','');
INSERT INTO "fieldmetainfo" VALUES (DEFAULT,'_sequenceNum','SEQUENCE_NUM','"SequenceNum"','java.lang.Integer','com.gridnode.pdip.base.time.entities.model.iCalEvent','',0,0,0,1,1,'',999,'displayable=false','');
INSERT INTO "fieldmetainfo" VALUES (DEFAULT,'_status','STATUS','"Status"','java.lang.Integer','com.gridnode.pdip.base.time.entities.model.iCalEvent','',0,0,0,1,1,'',999,'displayable=false','');
INSERT INTO "fieldmetainfo" VALUES (DEFAULT,'_iCalUid','iCal_UID','"iCalUid"','java.lang.String','com.gridnode.pdip.base.time.entities.model.iCalEvent','',50,0,0,1,1,'',999,'displayable=false','');
INSERT INTO "fieldmetainfo" VALUES (DEFAULT,'_endDt','END_DT','"EndDt"','java.util.Date','com.gridnode.pdip.base.time.entities.model.iCalEvent','',0,0,0,1,1,'',999,'displayable=false','');
INSERT INTO "fieldmetainfo" VALUES (DEFAULT,'_duration','DURATION','"Duration"','java.lang.Long','com.gridnode.pdip.base.time.entities.model.iCalEvent','',0,0,0,1,1,'',999,'displayable=false','');
INSERT INTO "fieldmetainfo" VALUES (DEFAULT,'_propertiesStr','PROPERTIES_STR','"PropertiesStr"','java.lang.String','com.gridnode.pdip.base.time.entities.model.iCalEvent', 'ical_event.PropertiesStr', 50,0,0,1,1,'', 999,'displayable=false','');
INSERT INTO "fieldmetainfo" VALUES (DEFAULT,'_timeTransparency','TIME_TRANSPARENCY','"TimeTransparency"','java.lang.Integer','com.gridnode.pdip.base.time.entities.model.iCalEvent','',0,0,0,1,1,'',999,'displayable=false','');

--------- iCalTodo
DELETE FROM "fieldmetainfo" WHERE "EntityObjectName" LIKE '%iCalTodo';
INSERT INTO "fieldmetainfo" VALUES (DEFAULT,'_uId','UID','"UID"','java.lang.Long','com.gridnode.pdip.base.time.entities.model.iCalTodo','',0,0,0,1,1,'',999,'displayable=false','');
INSERT INTO "fieldmetainfo" VALUES (DEFAULT,'_kind','KIND','"Kind"','java.lang.Short','com.gridnode.pdip.base.time.entities.model.iCalTodo','',0,0,0,1,1,'',999,'displayable=false','');
INSERT INTO "fieldmetainfo" VALUES (DEFAULT,'_ownerId','OWNER_ID','"OwnerId"','java.lang.Integer','com.gridnode.pdip.base.time.entities.model.iCalTodo','',0,0,0,1,1,'',999,'displayable=false','');
INSERT INTO "fieldmetainfo" VALUES (DEFAULT,'_classification','CLASSIFICATION','"Classification"','java.lang.String','com.gridnode.pdip.base.time.entities.model.iCalTodo','',50,0,0,1,1,'',999,'displayable=false','');
INSERT INTO "fieldmetainfo" VALUES (DEFAULT,'_createDt','CREATE_DT','"CreateDt"','java.util.Date','com.gridnode.pdip.base.time.entities.model.iCalTodo','',0,0,0,1,1,'',999,'displayable=false','');
INSERT INTO "fieldmetainfo" VALUES (DEFAULT,'_lastModifyDt','LAST_MODIFY_DT','"LastModifyDt"','java.util.Date','com.gridnode.pdip.base.time.entities.model.iCalTodo','',0,0,0,1,1,'',999,'displayable=false','');
INSERT INTO "fieldmetainfo" VALUES (DEFAULT,'_isDateType','IS_DATE_TYPE','"IsDateType"','java.lang.Boolean','com.gridnode.pdip.base.time.entities.model.iCalTodo','',0,0,0,1,1,'',999,'displayable=false','');
INSERT INTO "fieldmetainfo" VALUES (DEFAULT,'_isDtFloat','IS_DT_FLOAT','"IsDtFloat"','java.lang.Boolean','com.gridnode.pdip.base.time.entities.model.iCalTodo','',0,0,0,1,1,'',999,'displayable=false','');
INSERT INTO "fieldmetainfo" VALUES (DEFAULT,'_startDt','START_DT','"StartDt"','java.util.Date','com.gridnode.pdip.base.time.entities.model.iCalTodo','',0,0,0,1,1,'',999,'displayable=false','');
INSERT INTO "fieldmetainfo" VALUES (DEFAULT,'_priority','PRIORITY','"Priority"','java.lang.Integer','com.gridnode.pdip.base.time.entities.model.iCalTodo','',0,0,0,1,1,'',999,'displayable=false','');
INSERT INTO "fieldmetainfo" VALUES (DEFAULT,'_sequenceNum','SEQUENCE_NUM','"SequenceNum"','java.lang.Integer','com.gridnode.pdip.base.time.entities.model.iCalTodo','',0,0,0,1,1,'',999,'displayable=false','');
INSERT INTO "fieldmetainfo" VALUES (DEFAULT,'_status','STATUS','"Status"','java.lang.Integer','com.gridnode.pdip.base.time.entities.model.iCalTodo','',0,0,0,1,1,'',999,'displayable=false','');
INSERT INTO "fieldmetainfo" VALUES (DEFAULT,'_iCalUid','iCal_UID','"iCalUid"','java.lang.String','com.gridnode.pdip.base.time.entities.model.iCalTodo','',50,0,0,1,1,'',999,'displayable=false','');
INSERT INTO "fieldmetainfo" VALUES (DEFAULT,'_endDt','END_DT','"EndDt"','java.util.Date','com.gridnode.pdip.base.time.entities.model.iCalTodo','',0,0,0,1,1,'',999,'displayable=false','');
INSERT INTO "fieldmetainfo" VALUES (DEFAULT,'_duration','DURATION','"Duration"','java.lang.Long','com.gridnode.pdip.base.time.entities.model.iCalTodo','',0,0,0,0,1,'1',999,'displayable=false','');
INSERT INTO "fieldmetainfo" VALUES (DEFAULT,'_completeDt','COMPLETE_DT','"CompleteDt"','java.util.Date','com.gridnode.pdip.base.time.entities.model.iCalTodo','',0,0,0,1,1,'',999,'displayable=false','');
INSERT INTO "fieldmetainfo" VALUES (DEFAULT,'_propertiesStr','PROPERTIES_STR','"PropertiesStr"','java.lang.String','com.gridnode.pdip.base.time.entities.model.iCalTodo', 'ical_todo.PropertiesStr', 50,0,0,1,1,'', 999,'displayable=false','');
INSERT INTO "fieldmetainfo" VALUES (DEFAULT,'_percentCompleted','PERCENT_COMPLETED','"PercentCompleted"','java.lang.Integer','com.gridnode.pdip.base.time.entities.model.iCalTodo','',0,0,0,1,1,'',999,'displayable=false','');

--------- iCalProperty
DELETE FROM "fieldmetainfo" WHERE "EntityObjectName" LIKE '%iCalProperty';
INSERT INTO "fieldmetainfo" VALUES (DEFAULT,'_uId','UID','"UID"','java.lang.Long','com.gridnode.pdip.base.time.entities.model.iCalProperty','',0,0,0,1,1,'',999,'displayable=false','');
INSERT INTO "fieldmetainfo" VALUES (DEFAULT,'_kind','KIND','"Kind"','java.lang.Short','com.gridnode.pdip.base.time.entities.model.iCalProperty','',0,0,0,1,1,'',999,'displayable=false','');
INSERT INTO "fieldmetainfo" VALUES (DEFAULT,'_compKind','COMP_KIND','"CompKind"','java.lang.Short','com.gridnode.pdip.base.time.entities.model.iCalProperty','',0,0,0,1,1,'',999,'displayable=false','');
INSERT INTO "fieldmetainfo" VALUES (DEFAULT,'_iCalCompId','iCal_COMP_ID','"iCalCompId"','java.lang.Long','com.gridnode.pdip.base.time.entities.model.iCalProperty','',0,0,0,1,1,'',999,'displayable=false','');

--------- iCalInt
DELETE FROM "fieldmetainfo" WHERE "EntityObjectName" LIKE '%iCalInt';
INSERT INTO "fieldmetainfo" VALUES (DEFAULT,'_uId','UID','"UID"','java.lang.Long','com.gridnode.pdip.base.time.entities.model.iCalInt','',0,0,0,1,1,'',999,'displayable=false','');
INSERT INTO "fieldmetainfo" VALUES (DEFAULT,'_compKind','COMP_KIND','"CompKind"','java.lang.Short','com.gridnode.pdip.base.time.entities.model.iCalInt','',0,0,0,1,1,'',999,'displayable=false','');
INSERT INTO "fieldmetainfo" VALUES (DEFAULT,'_propKind','PROP_KIND','"PropKind"','java.lang.Short','com.gridnode.pdip.base.time.entities.model.iCalInt','',0,0,0,1,1,'',999,'displayable=false','');
INSERT INTO "fieldmetainfo" VALUES (DEFAULT,'_paramKind','PARAM_KIND','"ParamKind"','java.lang.Short','com.gridnode.pdip.base.time.entities.model.iCalInt','',0,0,0,1,1,'',999,'displayable=false','');
INSERT INTO "fieldmetainfo" VALUES (DEFAULT,'_refKind','REF_KIND','"RefKind"','java.lang.Short','com.gridnode.pdip.base.time.entities.model.iCalInt','',0,0,0,1,1,'',999,'displayable=false','');
INSERT INTO "fieldmetainfo" VALUES (DEFAULT,'_valueKind','VALUE_KIND','"ValueKind"','java.lang.Short','com.gridnode.pdip.base.time.entities.model.iCalInt','',0,0,0,1,1,'',999,'displayable=false','');
INSERT INTO "fieldmetainfo" VALUES (DEFAULT,'_iCalCompId','iCal_COMP_ID','"iCalCompId"','java.lang.Long','com.gridnode.pdip.base.time.entities.model.iCalInt','',0,0,0,1,1,'',999,'displayable=false','');
INSERT INTO "fieldmetainfo" VALUES (DEFAULT,'_refId','REF_ID','"RefId"','java.lang.Long','com.gridnode.pdip.base.time.entities.model.iCalInt','',0,0,0,1,1,'',999,'displayable=false','');
INSERT INTO "fieldmetainfo" VALUES (DEFAULT,'_intValue','INT_VALUE','"IntValue"','java.lang.Integer','com.gridnode.pdip.base.time.entities.model.iCalInt','',0,0,0,1,1,'',999,'displayable=false','');

--------- iCalDate
DELETE FROM "fieldmetainfo" WHERE "EntityObjectName" LIKE '%iCalDate';
INSERT INTO "fieldmetainfo" VALUES (DEFAULT,'_uId','UID','"UID"','java.lang.Long','com.gridnode.pdip.base.time.entities.model.iCalDate','',0,0,0,1,1,'',999,'displayable=false','');
INSERT INTO "fieldmetainfo" VALUES (DEFAULT,'_compKind','COMP_KIND','"CompKind"','java.lang.Short','com.gridnode.pdip.base.time.entities.model.iCalDate','',0,0,0,1,1,'',999,'displayable=false','');
INSERT INTO "fieldmetainfo" VALUES (DEFAULT,'_propKind','PROP_KIND','"PropKind"','java.lang.Short','com.gridnode.pdip.base.time.entities.model.iCalDate','',0,0,0,1,1,'',999,'displayable=false','');
INSERT INTO "fieldmetainfo" VALUES (DEFAULT,'_paramKind','PARAM_KIND','"ParamKind"','java.lang.Short','com.gridnode.pdip.base.time.entities.model.iCalDate','',0,0,0,1,1,'',999,'displayable=false','');
INSERT INTO "fieldmetainfo" VALUES (DEFAULT,'_refKind','REF_KIND','"RefKind"','java.lang.Short','com.gridnode.pdip.base.time.entities.model.iCalDate','',0,0,0,1,1,'',999,'displayable=false','');
INSERT INTO "fieldmetainfo" VALUES (DEFAULT,'_valueKind','VALUE_KIND','"ValueKind"','java.lang.Short','com.gridnode.pdip.base.time.entities.model.iCalDate','',0,0,0,1,1,'',999,'displayable=false','');
INSERT INTO "fieldmetainfo" VALUES (DEFAULT,'_iCalCompId','iCal_COMP_ID','"iCalCompId"','java.lang.Long','com.gridnode.pdip.base.time.entities.model.iCalDate','',0,0,0,1,1,'',999,'displayable=false','');
INSERT INTO "fieldmetainfo" VALUES (DEFAULT,'_refId','REF_ID','"RefId"','java.lang.Long','com.gridnode.pdip.base.time.entities.model.iCalDate','',0,0,0,1,1,'',999,'displayable=false','');
INSERT INTO "fieldmetainfo" VALUES (DEFAULT,'_dateValue','DATE_VALUE','"DateValue"','java.util.Date','com.gridnode.pdip.base.time.entities.model.iCalDate','',0,0,0,1,1,'',999,'displayable=false','');

--------- iCalString
DELETE FROM "fieldmetainfo" WHERE "EntityObjectName" LIKE '%iCalString';
INSERT INTO "fieldmetainfo" VALUES (DEFAULT,'_uId','UID','"UID"','java.lang.Long','com.gridnode.pdip.base.time.entities.model.iCalString','',0,0,0,1,1,'',999,'displayable=false','');
INSERT INTO "fieldmetainfo" VALUES (DEFAULT,'_compKind','COMP_KIND','"CompKind"','java.lang.Short','com.gridnode.pdip.base.time.entities.model.iCalString','',0,0,0,1,1,'',999,'displayable=false','');
INSERT INTO "fieldmetainfo" VALUES (DEFAULT,'_propKind','PROP_KIND','"PropKind"','java.lang.Short','com.gridnode.pdip.base.time.entities.model.iCalString','',0,0,0,1,1,'',999,'displayable=false','');
INSERT INTO "fieldmetainfo" VALUES (DEFAULT,'_paramKind','PARAM_KIND','"ParamKind"','java.lang.Short','com.gridnode.pdip.base.time.entities.model.iCalString','',0,0,0,1,1,'',999,'displayable=false','');
INSERT INTO "fieldmetainfo" VALUES (DEFAULT,'_refKind','REF_KIND','"RefKind"','java.lang.Short','com.gridnode.pdip.base.time.entities.model.iCalString','',0,0,0,1,1,'',999,'displayable=false','');
INSERT INTO "fieldmetainfo" VALUES (DEFAULT,'_valueKind','VALUE_KIND','"ValueKind"','java.lang.Short','com.gridnode.pdip.base.time.entities.model.iCalString','',0,0,0,1,1,'',999,'displayable=false','');
INSERT INTO "fieldmetainfo" VALUES (DEFAULT,'_iCalCompId','iCal_COMP_ID','"iCalCompId"','java.lang.Long','com.gridnode.pdip.base.time.entities.model.iCalString','',0,0,0,1,1,'',999,'displayable=false','');
INSERT INTO "fieldmetainfo" VALUES (DEFAULT,'_refId','REF_ID','"RefId"','java.lang.Long','com.gridnode.pdip.base.time.entities.model.iCalString','',0,0,0,1,1,'',999,'displayable=false','');
INSERT INTO "fieldmetainfo" VALUES (DEFAULT,'_strValue','STR_VALUE','"StrValue"','java.lang.String','com.gridnode.pdip.base.time.entities.model.iCalString','',50,0,0,1,1,'',999,'displayable=false','');

--------- iCalText
DELETE FROM "fieldmetainfo" WHERE "EntityObjectName" LIKE '%iCalText';
INSERT INTO "fieldmetainfo" VALUES (DEFAULT,'_uId','UID','"UID"','java.lang.Long','com.gridnode.pdip.base.time.entities.model.iCalText','',0,0,0,1,1,'',999,'displayable=false','');
INSERT INTO "fieldmetainfo" VALUES (DEFAULT,'_compKind','COMP_KIND','"CompKind"','java.lang.Short','com.gridnode.pdip.base.time.entities.model.iCalText','',0,0,0,1,1,'',999,'displayable=false','');
INSERT INTO "fieldmetainfo" VALUES (DEFAULT,'_propKind','PROP_KIND','"PropKind"','java.lang.Short','com.gridnode.pdip.base.time.entities.model.iCalText','',0,0,0,1,1,'',999,'displayable=false','');
INSERT INTO "fieldmetainfo" VALUES (DEFAULT,'_paramKind','PARAM_KIND','"ParamKind"','java.lang.Short','com.gridnode.pdip.base.time.entities.model.iCalText','',0,0,0,1,1,'',999,'displayable=false','');
INSERT INTO "fieldmetainfo" VALUES (DEFAULT,'_refKind','REF_KIND','"RefKind"','java.lang.Short','com.gridnode.pdip.base.time.entities.model.iCalText','',0,0,0,1,1,'',999,'displayable=false','');
INSERT INTO "fieldmetainfo" VALUES (DEFAULT,'_valueKind','VALUE_KIND','"ValueKind"','java.lang.Short','com.gridnode.pdip.base.time.entities.model.iCalText','',0,0,0,1,1,'',999,'displayable=false','');
INSERT INTO "fieldmetainfo" VALUES (DEFAULT,'_iCalCompId','iCal_COMP_ID','"iCalCompId"','java.lang.Long','com.gridnode.pdip.base.time.entities.model.iCalText','',0,0,0,1,1,'',999,'displayable=false','');
INSERT INTO "fieldmetainfo" VALUES (DEFAULT,'_refId','REF_ID','"RefId"','java.lang.Long','com.gridnode.pdip.base.time.entities.model.iCalText','',0,0,0,1,1,'',999,'displayable=false','');
INSERT INTO "fieldmetainfo" VALUES (DEFAULT,'_textValue','TEXT_VALUE','"TextValue"','java.lang.String','com.gridnode.pdip.base.time.entities.model.iCalText','',50,0,0,1,1,'',999,'displayable=false','');

--------- SessionAudit
DELETE FROM "fieldmetainfo" WHERE "EntityObjectName" LIKE '%SessionAudit';
INSERT INTO "fieldmetainfo" VALUES(DEFAULT,'_uId','UID','"UID"','java.lang.Long','com.gridnode.pdip.base.session.model.SessionAudit','session.uid',0,0,0,0,0,'0',999,'','');
INSERT INTO "fieldmetainfo" VALUES(DEFAULT,'_sessionId','SESSION_ID','"SessionId"','java.lang.String','com.gridnode.pdip.base.session.model.SessionAudit','session.sessionId',0,0,0,0,1,'0',1,'','');
INSERT INTO "fieldmetainfo" VALUES(DEFAULT,'_sessionName','SESSION_NAME','"SessionName"','java.lang.String','com.gridnode.pdip.base.session.model.SessionAudit','session.sessionName',0,0,0,1,1,'0',2,'','');
INSERT INTO "fieldmetainfo" VALUES(DEFAULT,'_state','STATE','"State"','java.lang.Short','com.gridnode.pdip.base.session.model.SessionAudit','session.state',0,0,0,1,1,'0',3,'','');
INSERT INTO "fieldmetainfo" VALUES(DEFAULT,'_sessionData','SESSION_DATA','"SessionData"','byte[]','com.gridnode.pdip.base.session.model.SessionAudit','session.sessionData',0,0,0,1,0,'0',4,'','');
INSERT INTO "fieldmetainfo" VALUES(DEFAULT,'_openTime','OPEN_TIME','"OpenTime"','java.util.Date','com.gridnode.pdip.base.session.model.SessionAudit','session.openTime',0,0,0,0,1,'0',5,'','');
INSERT INTO "fieldmetainfo" VALUES(DEFAULT,'_lastActiveTime','LAST_ACTIVE_TIME','"LastActiveTime"','java.util.Date','com.gridnode.pdip.base.session.model.SessionAudit','session.lastActiveTime',0,0,0,0,1,'0',6,'','');
INSERT INTO "fieldmetainfo" VALUES(DEFAULT,'_destroyTime','DESTROY_TIME','"DestroyTime"','java.util.Date','com.gridnode.pdip.base.session.model.SessionAudit','session.destroyTime',0,0,0,0,1,'0',7,'','');

--------- CountryCode
DELETE from "fieldmetainfo" WHERE "EntityObjectName" LIKE '%CountryCode';
INSERT INTO "fieldmetainfo" VALUES (DEFAULT,'_name','NAME','"Name"','java.lang.String','com.gridnode.pdip.base.locale.model.CountryCode','countryCode.name',0,0,0,1,1,NULL,999,'displayable=true'||chr(13)||chr(10)||'editable=false'||chr(13)||chr(10)||'mandatory=false'||chr(13)||chr(10),'type=text');
INSERT INTO "fieldmetainfo" VALUES (DEFAULT,'_numericalCode','NUMERICAL_CODE','"NumericalCode"','java.lang.Integer','com.gridnode.pdip.base.locale.model.CountryCode','countryCode.numericalCode',0,0,0,1,1,NULL,999,'displayable=false'||chr(13)||chr(10)||'editable=false'||chr(13)||chr(10)||'mandatory=false'||chr(13)||chr(10),'type=uid');
INSERT INTO "fieldmetainfo" VALUES (DEFAULT,'_alpha2Code','ALPHA_2_CODE','"Alpha2Code"','java.lang.String','com.gridnode.pdip.base.locale.model.CountryCode','countryCode.alpha2Code',0,0,0,1,1,NULL,999,'displayable=false'||chr(13)||chr(10)||'editable=false'||chr(13)||chr(10)||'mandatory=false'||chr(13)||chr(10),'type=text');
INSERT INTO "fieldmetainfo" VALUES (DEFAULT,'_alpha3Code','ALPHA_3_CODE','"Alpha3Code"','java.lang.String','com.gridnode.pdip.base.locale.model.CountryCode','countryCode.alpha3Code',0,0,0,1,1,NULL,999,'displayable=false'||chr(13)||chr(10)||'editable=false'||chr(13)||chr(10)||'mandatory=false'||chr(13)||chr(10),'type=text');

--------- LanguageCode
DELETE from "fieldmetainfo" WHERE "EntityObjectName" LIKE '%LanguageCode';
INSERT INTO "fieldmetainfo" VALUES (DEFAULT,'_name','NAME','"Name"','java.lang.String','com.gridnode.pdip.base.locale.model.LanguageCode','languageCode.name',0,0,0,1,1,NULL,999,'displayable=true'||chr(13)||chr(10)||'editable=false'||chr(13)||chr(10)||'mandatory=false'||chr(13)||chr(10),'type=text');
INSERT INTO "fieldmetainfo" VALUES (DEFAULT,'_bAlpha3Code','B_ALPHA_3_CODE','"BAlpha3Code"','java.lang.String','com.gridnode.pdip.base.locale.model.LanguageCode','languageCode.balpha3Code',0,0,0,1,1,NULL,999,'displayable=false'||chr(13)||chr(10)||'editable=false'||chr(13)||chr(10)||'mandatory=false'||chr(13)||chr(10),'type=text');
INSERT INTO "fieldmetainfo" VALUES (DEFAULT,'_alpha2Code','ALPHA_2_CODE','"Alpha2Code"','java.lang.String','com.gridnode.pdip.base.locale.model.LanguageCode','languageCode.alpha2Code',0,0,0,1,1,NULL,999,'displayable=false'||chr(13)||chr(10)||'editable=false'||chr(13)||chr(10)||'mandatory=false'||chr(13)||chr(10),'type=uid');
INSERT INTO "fieldmetainfo" VALUES (DEFAULT,'_tAlpha3Code','T_ALPHA_3_CODE','"TAlpha3Code"','java.lang.String','com.gridnode.pdip.base.locale.model.LanguageCode','languageCode.talpha3Code',0,0,0,1,1,NULL,999,'displayable=false'||chr(13)||chr(10)||'editable=false'||chr(13)||chr(10)||'mandatory=false'||chr(13)||chr(10),'type=text');

--------- BpssReqBusinessActivity
DELETE FROM "fieldmetainfo" WHERE "EntityObjectName" LIKE '%BpssReqBusinessActivity';
INSERT INTO "fieldmetainfo" VALUES (DEFAULT,'_uId','UID','"UID"','java.lang.Long','com.gridnode.pdip.base.gwfbase.bpss.model.BpssReqBusinessActivity','',0,0,0,1,1,'',999,'displayable=false','');
INSERT INTO "fieldmetainfo" VALUES (DEFAULT,'_businessActionName','NAME','"Name"','java.lang.String','com.gridnode.pdip.base.gwfbase.bpss.model.BpssReqBusinessActivity','',50,0,0,1,1,'',999,'displayable=false','');
INSERT INTO "fieldmetainfo" VALUES (DEFAULT,'_isIntelligibleChkReq','ISINTELLIGIBLE_REQUIRED','"IsIntelligibleChkReq"','java.lang.Boolean','com.gridnode.pdip.base.gwfbase.bpss.model.BpssReqBusinessActivity','',0,0,0,1,1,'',999,'displayable=false','');
INSERT INTO "fieldmetainfo" VALUES (DEFAULT,'_isAuthReq','ISAUTH_REQUIRED','"IsAuthReq"','java.lang.Boolean','com.gridnode.pdip.base.gwfbase.bpss.model.BpssReqBusinessActivity','',0,0,0,1,1,'',999,'displayable=false','');
INSERT INTO "fieldmetainfo" VALUES (DEFAULT,'_timeToAckReceipt','TIMETO_ACK_RECEIPT','"TimeToAckReceipt"','java.lang.String','com.gridnode.pdip.base.gwfbase.bpss.model.BpssReqBusinessActivity','',0,0,0,1,1,'',999,'displayable=false','');
INSERT INTO "fieldmetainfo" VALUES (DEFAULT,'_isNonRepudiationReq','ISNONREPUDIATION_REQUIRED','"IsNonRepudiationReq"','java.lang.Boolean','com.gridnode.pdip.base.gwfbase.bpss.model.BpssReqBusinessActivity','',0,0,0,1,1,'',999,'displayable=false','');
INSERT INTO "fieldmetainfo" VALUES (DEFAULT,'_isNonRepudiationOfReceiptReq','ISNONREPUDIATION_RECEIPT_REQUIRED','"IsNonRepudiationOfReceiptReq"','java.lang.Boolean','com.gridnode.pdip.base.gwfbase.bpss.model.BpssReqBusinessActivity','',0,0,0,1,1,'',999,'displayable=false','');
INSERT INTO "fieldmetainfo" VALUES (DEFAULT,'_timeToAckAccept','TIMETO_ACK_ACCEPT','"TimeToAckAccept"','java.lang.String','com.gridnode.pdip.base.gwfbase.bpss.model.BpssReqBusinessActivity','',0,0,0,1,1,'',999,'displayable=false','');

--------- BpssResBusinessActivity
DELETE FROM "fieldmetainfo" WHERE "EntityObjectName" LIKE '%BpssResBusinessActivity';
INSERT INTO "fieldmetainfo" VALUES (DEFAULT,'_uId','UID','"UID"','java.lang.Long','com.gridnode.pdip.base.gwfbase.bpss.model.BpssResBusinessActivity','',0,0,0,1,1,'',999,'displayable=false','');
INSERT INTO "fieldmetainfo" VALUES (DEFAULT,'_businessActionName','NAME','"Name"','java.lang.String','com.gridnode.pdip.base.gwfbase.bpss.model.BpssResBusinessActivity','',50,0,0,1,1,'',999,'displayable=false','');
INSERT INTO "fieldmetainfo" VALUES (DEFAULT,'_isIntelligibleChkReq','ISINTELLIGIBLE_REQUIRED','"IsIntelligibleChkReq"','java.lang.Boolean','com.gridnode.pdip.base.gwfbase.bpss.model.BpssResBusinessActivity','',0,0,0,1,1,'',999,'displayable=false','');
INSERT INTO "fieldmetainfo" VALUES (DEFAULT,'_isAuthReq','ISAUTH_REQUIRED','"IsAuthReq"','java.lang.Boolean','com.gridnode.pdip.base.gwfbase.bpss.model.BpssResBusinessActivity','',0,0,0,1,1,'',999,'displayable=false','');
INSERT INTO "fieldmetainfo" VALUES (DEFAULT,'_timeToAckReceipt','TIMETO_ACK_RECEIPT','"TimeToAckReceipt"','java.lang.String','com.gridnode.pdip.base.gwfbase.bpss.model.BpssResBusinessActivity','',0,0,0,1,1,'',999,'displayable=false','');
INSERT INTO "fieldmetainfo" VALUES (DEFAULT,'_isNonRepudiationReq','ISNONREPUDIATION_REQUIRED','"IsNonRepudiationReq"','java.lang.Boolean','com.gridnode.pdip.base.gwfbase.bpss.model.BpssResBusinessActivity','',0,0,0,1,1,'',999,'displayable=false','');
INSERT INTO "fieldmetainfo" VALUES (DEFAULT,'_isNonRepudiationOfReceiptReq','ISNONREPUDIATION_RECEIPT_REQUIRED','"IsNonRepudiationOfReceiptReq"','java.lang.Boolean','com.gridnode.pdip.base.gwfbase.bpss.model.BpssResBusinessActivity','',0,0,0,1,1,'',999,'displayable=false','');
 
--------- BpssBusinessTransActivity
DELETE FROM "fieldmetainfo" WHERE "EntityObjectName" LIKE '%BpssBusinessTransActivity';
INSERT INTO "fieldmetainfo" VALUES (DEFAULT,'_uId','UID','"UID"','java.lang.Long','com.gridnode.pdip.base.gwfbase.bpss.model.BpssBusinessTransActivity','',0,0,0,1,1,'',999,'displayable=false','');
INSERT INTO "fieldmetainfo" VALUES (DEFAULT,'_activityName','NAME','"Name"','java.lang.String','com.gridnode.pdip.base.gwfbase.bpss.model.BpssBusinessTransActivity','',50,0,0,1,1,'',999,'displayable=false','');
INSERT INTO "fieldmetainfo" VALUES (DEFAULT,'_timeToPerform','TIMETO_PERFORM','"TimeToPerform"','java.lang.String','com.gridnode.pdip.base.gwfbase.bpss.model.BpssBusinessTransActivity','',0,0,0,1,1,'',999,'displayable=false','');
INSERT INTO "fieldmetainfo" VALUES (DEFAULT,'_isConcurrent','ISCONCURRENT','"IsConcurrent"','java.lang.Boolean','com.gridnode.pdip.base.gwfbase.bpss.model.BpssBusinessTransActivity','',0,0,0,1,1,'',999,'displayable=false','');
INSERT INTO "fieldmetainfo" VALUES (DEFAULT,'_isLegallyBinding','ISLEGALLY_BINDING','"IsLegallyBinding"','java.lang.Boolean','com.gridnode.pdip.base.gwfbase.bpss.model.BpssBusinessTransActivity','',0,0,0,1,1,'',999,'displayable=false','');
INSERT INTO "fieldmetainfo" VALUES (DEFAULT,'_businessTransUId','BUSINESS_TRANS_UID','"BusinessTransUId"','java.lang.Long','com.gridnode.pdip.base.gwfbase.bpss.model.BpssBusinessTransActivity','',0,0,0,1,1,'',999,'displayable=false','');
INSERT INTO "fieldmetainfo" VALUES (DEFAULT,'_fromAuthorizedRole','FROM_AUTHORIZED_ROLE','"FromAuthorizedRole"','java.lang.String','com.gridnode.pdip.base.gwfbase.bpss.model.BpssBusinessTransActivity','',50,0,0,1,1,'',999,'displayable=false','');
INSERT INTO "fieldmetainfo" VALUES (DEFAULT,'_toAuthorizedRole','TO_AUTHORIZED_ROLE','"ToAuthorizedRole"','java.lang.String','com.gridnode.pdip.base.gwfbase.bpss.model.BpssBusinessTransActivity','',50,0,0,1,1,'',999,'displayable=false','');

--------- BpssCollaborationActivity
DELETE FROM "fieldmetainfo" WHERE "EntityObjectName" LIKE '%BpssCollaborationActivity';
INSERT INTO "fieldmetainfo" VALUES (DEFAULT,'_uId','UID','"UID"','java.lang.Long','com.gridnode.pdip.base.gwfbase.bpss.model.BpssCollaborationActivity','',0,0,0,1,1,'',999,'displayable=false','');
INSERT INTO "fieldmetainfo" VALUES (DEFAULT,'_activityName','NAME','"Name"','java.lang.String','com.gridnode.pdip.base.gwfbase.bpss.model.BpssCollaborationActivity','',50,0,0,1,1,'',999,'displayable=false','');
INSERT INTO "fieldmetainfo" VALUES (DEFAULT,'_binaryCollaborationUId','BINARY_COLLABORATION_UID','"BinCollProcessUId"','java.lang.Long','com.gridnode.pdip.base.gwfbase.bpss.model.BpssCollaborationActivity','',0,0,0,1,1,'',999,'displayable=false','');

--------- BpssBusinessTrans
DELETE FROM "fieldmetainfo" WHERE "EntityObjectName" LIKE '%BpssBusinessTrans';
INSERT INTO "fieldmetainfo" VALUES (DEFAULT,'_uId','UID','"UID"','java.lang.Long','com.gridnode.pdip.base.gwfbase.bpss.model.BpssBusinessTrans','',0,0,0,1,1,'',999,'displayable=false','');
INSERT INTO "fieldmetainfo" VALUES (DEFAULT,'_businessTransName','NAME','"Name"','java.lang.String','com.gridnode.pdip.base.gwfbase.bpss.model.BpssBusinessTrans','',50,0,0,1,1,'',999,'displayable=false','');
INSERT INTO "fieldmetainfo" VALUES (DEFAULT,'_pattern','PATTERN','"Pattern"','java.lang.String','com.gridnode.pdip.base.gwfbase.bpss.model.BpssBusinessTrans','',50,0,0,1,1,'',999,'displayable=false','');
INSERT INTO "fieldmetainfo" VALUES (DEFAULT,'_preCondition','PRE_CONDITION','"PreCondition"','java.lang.String','com.gridnode.pdip.base.gwfbase.bpss.model.BpssBusinessTrans','',50,0,0,1,1,'',999,'displayable=false','');
INSERT INTO "fieldmetainfo" VALUES (DEFAULT,'_postCondition','POST_CONDITION','"PostCondition"','java.lang.String','com.gridnode.pdip.base.gwfbase.bpss.model.BpssBusinessTrans','',50,0,0,1,1,'',999,'displayable=false','');
INSERT INTO "fieldmetainfo" VALUES (DEFAULT,'_isGuaranteedDeliveryRequired','ISGUARANTEED_DELIVERY_REQUIRED','"IsDeliveryReq"','java.lang.Boolean','com.gridnode.pdip.base.gwfbase.bpss.model.BpssBusinessTrans','',0,0,0,1,1,'',999,'displayable=false','');
INSERT INTO "fieldmetainfo" VALUES (DEFAULT,'_beginsWhen','BEGINS_WHEN','"BeginsWhen"','java.lang.String','com.gridnode.pdip.base.gwfbase.bpss.model.BpssBusinessTrans','',50,0,0,1,1,'',999,'displayable=false','');
INSERT INTO "fieldmetainfo" VALUES (DEFAULT,'_endsWhen','ENDS_WHEN','"EndsWhen"','java.lang.String','com.gridnode.pdip.base.gwfbase.bpss.model.BpssBusinessTrans','',50,0,0,1,1,'',999,'displayable=false','');
INSERT INTO "fieldmetainfo" VALUES (DEFAULT,'_bpssReqBusinessActivity','BPSS_REQ_ACTIVITY','"ReqUId"','java.lang.Long','com.gridnode.pdip.base.gwfbase.bpss.model.BpssBusinessTrans','',0,0,0,1,1,'',999,'displayable=false','');
INSERT INTO "fieldmetainfo" VALUES (DEFAULT,'_bpssResBusinessActivity','BPSS_RES_ACTIVITY','"ResUId"','java.lang.Long','com.gridnode.pdip.base.gwfbase.bpss.model.BpssBusinessTrans','',0,0,0,1,1,'',999,'displayable=false','');

--------- BpssBinaryCollaboration
DELETE FROM "fieldmetainfo" WHERE "EntityObjectName" LIKE '%BpssBinaryCollaboration';
INSERT INTO "fieldmetainfo" VALUES (DEFAULT,'_uId','UID','"UID"','java.lang.Long','com.gridnode.pdip.base.gwfbase.bpss.model.BpssBinaryCollaboration','',0,0,0,1,1,'',999,'displayable=false','');
INSERT INTO "fieldmetainfo" VALUES (DEFAULT,'_binaryCollaborationName','NAME','"Name"','java.lang.String','com.gridnode.pdip.base.gwfbase.bpss.model.BpssBinaryCollaboration','',50,0,0,1,1,'',999,'displayable=false','');
INSERT INTO "fieldmetainfo" VALUES (DEFAULT,'_pattern','PATTERN','"Pattern"','java.lang.String','com.gridnode.pdip.base.gwfbase.bpss.model.BpssBinaryCollaboration','',50,0,0,1,1,'',999,'displayable=false','');
INSERT INTO "fieldmetainfo" VALUES (DEFAULT,'_timeToPerform','TIMETO_PERFORM','"TimeToPerform"','java.lang.String','com.gridnode.pdip.base.gwfbase.bpss.model.BpssBinaryCollaboration','',0,0,0,1,1,'',999,'displayable=false','');
INSERT INTO "fieldmetainfo" VALUES (DEFAULT,'_preCondition','PRE_CONDITION','"PreCondition"','java.lang.String','com.gridnode.pdip.base.gwfbase.bpss.model.BpssBinaryCollaboration','',50,0,0,1,1,'',999,'displayable=false','');
INSERT INTO "fieldmetainfo" VALUES (DEFAULT,'_postCondition','POST_CONDITION','"PostCondition"','java.lang.String','com.gridnode.pdip.base.gwfbase.bpss.model.BpssBinaryCollaboration','',50,0,0,1,1,'',999,'displayable=false','');
INSERT INTO "fieldmetainfo" VALUES (DEFAULT,'_beginsWhen','BEGINS_WHEN','"BeginsWhen"','java.lang.String','com.gridnode.pdip.base.gwfbase.bpss.model.BpssBinaryCollaboration','',50,0,0,1,1,'',999,'displayable=false','');
INSERT INTO "fieldmetainfo" VALUES (DEFAULT,'_endsWhen','ENDS_WHEN','"EndsWhen"','java.lang.String','com.gridnode.pdip.base.gwfbase.bpss.model.BpssBinaryCollaboration','',50,0,0,1,1,'',999,'displayable=false','');

--------- BpssMultiPartyCollaboration
DELETE FROM "fieldmetainfo" WHERE "EntityObjectName" LIKE '%BpssMultiPartyCollaboration';
INSERT INTO "fieldmetainfo" VALUES (DEFAULT,'_uId','UID','"UID"','java.lang.Long','com.gridnode.pdip.base.gwfbase.bpss.model.BpssMultiPartyCollaboration','',0,0,0,1,1,'',999,'displayable=false','');
INSERT INTO "fieldmetainfo" VALUES (DEFAULT,'_binaryCollaborationName','NAME','"Name"','java.lang.String','com.gridnode.pdip.base.gwfbase.bpss.model.BpssMultiPartyCollaboration','',50,0,0,1,1,'',999,'displayable=false','');

--------- BpssTransition
DELETE FROM "fieldmetainfo" WHERE "EntityObjectName" LIKE '%BpssTransition';
INSERT INTO "fieldmetainfo" VALUES (DEFAULT,'_uId','UID','"UID"','java.lang.Long','com.gridnode.pdip.base.gwfbase.bpss.model.BpssTransition','',0,0,0,1,1,'',999,'displayable=false','');
INSERT INTO "fieldmetainfo" VALUES (DEFAULT,'_processUId','PROCESS_UID','"ProcessUId"','java.lang.Long','com.gridnode.pdip.base.gwfbase.bpss.model.BpssTransition','',0,0,0,1,1,'',999,'displayable=false','');
INSERT INTO "fieldmetainfo" VALUES (DEFAULT,'_onInitiation','ON_INITIATION','"OnInitiation"','java.lang.Boolean','com.gridnode.pdip.base.gwfbase.bpss.model.BpssTransition','',0,0,0,1,1,'',999,'displayable=false','');
INSERT INTO "fieldmetainfo" VALUES (DEFAULT,'_conditionGuard','CONDITION_GUARD','"ConditionGuard"','java.lang.String','com.gridnode.pdip.base.gwfbase.bpss.model.BpssTransition','',50,0,0,1,1,'',999,'displayable=false','');
INSERT INTO "fieldmetainfo" VALUES (DEFAULT,'_fromBusinessStateKey','FROM_BUSINESS_STATE_KEY','"FromBusinessStateKey"','java.lang.String','com.gridnode.pdip.base.gwfbase.bpss.model.BpssTransition','',50,0,0,1,1,'',999,'displayable=false','');
INSERT INTO "fieldmetainfo" VALUES (DEFAULT,'_toBusinessStateKey','TO_BUSINESS_STATE_KEY','"ToBusinessStateKey"','java.lang.String','com.gridnode.pdip.base.gwfbase.bpss.model.BpssTransition','',50,0,0,1,1,'',999,'displayable=false','');
INSERT INTO "fieldmetainfo" VALUES (DEFAULT,'_processType','PROCESS_TYPE','"ProcessType"','java.lang.String','com.gridnode.pdip.base.gwfbase.bpss.model.BpssTransition','',50,0,0,1,1,'',999,'displayable=false','');
INSERT INTO "fieldmetainfo" VALUES (DEFAULT,'_conditionExpression','CONDITION_EXPRESSION','"ConditionExpression"','java.lang.String','com.gridnode.pdip.base.gwfbase.bpss.model.BpssTransition','',50,0,0,1,1,'',999,'displayable=false','');
INSERT INTO "fieldmetainfo" VALUES (DEFAULT,'_expressionLanguage','EXPRESSION_LANGUAGE','"ExpressionLanguage"','java.lang.String','com.gridnode.pdip.base.gwfbase.bpss.model.BpssTransition','',50,0,0,1,1,'',999,'displayable=false','');

--------- BpssCompletionState
DELETE FROM "fieldmetainfo" WHERE "EntityObjectName" LIKE '%BpssCompletionState';
INSERT INTO "fieldmetainfo" VALUES (DEFAULT,'_uId','UID','"UID"','java.lang.Long','com.gridnode.pdip.base.gwfbase.bpss.model.BpssCompletionState','',0,0,0,1,1,'',999,'displayable=false','');
INSERT INTO "fieldmetainfo" VALUES (DEFAULT,'_processUId','PROCESS_UID','"ProcessUId"','java.lang.Long','com.gridnode.pdip.base.gwfbase.bpss.model.BpssCompletionState','',0,0,0,1,1,'',999,'displayable=false','');
INSERT INTO "fieldmetainfo" VALUES (DEFAULT,'_processType','PROCESS_TYPE','"ProcessType"','java.lang.String','com.gridnode.pdip.base.gwfbase.bpss.model.BpssCompletionState','',50,0,0,1,1,'',999,'displayable=false','');
INSERT INTO "fieldmetainfo" VALUES (DEFAULT,'_mpcUId','MPC_UID','"MpcUId"','java.lang.Long','com.gridnode.pdip.base.gwfbase.bpss.model.BpssCompletionState','',0,0,0,1,1,'',999,'displayable=false','');
INSERT INTO "fieldmetainfo" VALUES (DEFAULT,'_conditionGuard','CONDITION_GUARD','"ConditionGuard"','java.lang.String','com.gridnode.pdip.base.gwfbase.bpss.model.BpssCompletionState','',50,0,0,1,1,'',999,'displayable=false','');
INSERT INTO "fieldmetainfo" VALUES (DEFAULT,'_fromBusinessStateKey','FROM_BUSINESS_STATE_KEY','"FromBusinessStateKey"','java.lang.String','com.gridnode.pdip.base.gwfbase.bpss.model.BpssCompletionState','',50,0,0,1,1,'',999,'displayable=false','');
INSERT INTO "fieldmetainfo" VALUES (DEFAULT,'_conditionExpression','CONDITION_EXPRESSION','"ConditionExpression"','java.lang.String','com.gridnode.pdip.base.gwfbase.bpss.model.BpssCompletionState','',50,0,0,1,1,'',999,'displayable=false','');
INSERT INTO "fieldmetainfo" VALUES (DEFAULT,'_expressionLanguage','EXPRESSION_LANGUAGE','"ExpressionLanguage"','java.lang.String','com.gridnode.pdip.base.gwfbase.bpss.model.BpssCompletionState','',50,0,0,1,1,'',999,'displayable=false','');
INSERT INTO "fieldmetainfo" VALUES (DEFAULT,'_completionType','COMPLETION_TYPE','"CompletionType"','java.lang.String','com.gridnode.pdip.base.gwfbase.bpss.model.BpssCompletionState','',50,0,0,1,1,'',999,'displayable=false','');

--------- BpssFork
DELETE FROM "fieldmetainfo" WHERE "EntityObjectName" LIKE '%BpssFork';
INSERT INTO "fieldmetainfo" VALUES (DEFAULT,'_uId','UID','"UID"','java.lang.Long','com.gridnode.pdip.base.gwfbase.bpss.model.BpssFork','',0,0,0,1,1,'',999,'displayable=false','');
INSERT INTO "fieldmetainfo" VALUES (DEFAULT,'_name','NAME','"Name"','java.lang.String','com.gridnode.pdip.base.gwfbase.bpss.model.BpssFork','',50,0,0,1,1,'',999,'displayable=false','');

--------- BpssJoin
DELETE FROM "fieldmetainfo" WHERE "EntityObjectName" LIKE '%BpssJoin';
INSERT INTO "fieldmetainfo" VALUES (DEFAULT,'_uId','UID','"UID"','java.lang.Long','com.gridnode.pdip.base.gwfbase.bpss.model.BpssJoin','',0,0,0,1,1,'',999,'displayable=false','');
INSERT INTO "fieldmetainfo" VALUES (DEFAULT,'_name','NAME','"Name"','java.lang.String','com.gridnode.pdip.base.gwfbase.bpss.model.BpssJoin','',50,0,0,1,1,'',999,'displayable=false','');
INSERT INTO "fieldmetainfo" VALUES (DEFAULT,'_waitForAll','WAITFOR_ALL','"WaitForAll"','java.lang.Boolean','com.gridnode.pdip.base.gwfbase.bpss.model.BpssJoin','',0,0,0,1,1,'',999,'displayable=false','');

--------- MaxConcurrency
DELETE FROM "fieldmetainfo" WHERE "EntityObjectName" LIKE '%MaxConcurrency';
INSERT INTO "fieldmetainfo" VALUES (DEFAULT,'_maxConcurrency','MAX_CONCURRENCY','"MaxConcurrency"','java.lang.Integer','com.gridnode.pdip.base.gwfbase.bpss.model.BpssMultiPartyCollaboration','',0,0,0,1,1,'',999,'displayable=false','');
INSERT INTO "fieldmetainfo" VALUES (DEFAULT,'_maxConcurrency','MAX_CONCURRENCY','"MaxConcurrency"','java.lang.Integer','com.gridnode.pdip.base.gwfbase.bpss.model.BpssBinaryCollaboration','',0,0,0,1,1,'',999,'displayable=false','');
INSERT INTO "fieldmetainfo" VALUES (DEFAULT,'_maxConcurrency','MAX_CONCURRENCY','"MaxConcurrency"','java.lang.Integer','com.gridnode.pdip.base.gwfbase.bpss.model.BpssBusinessTrans','',0,0,0,1,1,'',999,'displayable=false','');

--------- BpssProcessSpecEntry
DELETE FROM "fieldmetainfo" WHERE "EntityObjectName" LIKE '%BpssProcessSpecEntry';
INSERT INTO "fieldmetainfo" VALUES (DEFAULT,'_uId','UID','"UID"','java.lang.Long','com.gridnode.pdip.base.gwfbase.bpss.model.BpssProcessSpecEntry','',0,0,0,1,1,'',999,'displayable=false','');
INSERT INTO "fieldmetainfo" VALUES (DEFAULT,'_specUId','SPEC_UID','"SpecUId"','java.lang.Long','com.gridnode.pdip.base.gwfbase.bpss.model.BpssProcessSpecEntry','',0,0,0,1,1,'',999,'displayable=false','');
INSERT INTO "fieldmetainfo" VALUES (DEFAULT,'_entryUId','ENTRY_UID','"EntryUId"','java.lang.Long','com.gridnode.pdip.base.gwfbase.bpss.model.BpssProcessSpecEntry','',0,0,0,1,1,'',999,'displayable=false','');
INSERT INTO "fieldmetainfo" VALUES (DEFAULT,'_entryName','ENTRY_NAME','"Name"','java.lang.String','com.gridnode.pdip.base.gwfbase.bpss.model.BpssProcessSpecEntry','',0,0,0,1,1,'',999,'displayable=false','');
INSERT INTO "fieldmetainfo" VALUES (DEFAULT,'_entryType','ENTRY_TYPE','"Type"','java.lang.String','com.gridnode.pdip.base.gwfbase.bpss.model.BpssProcessSpecEntry','',0,0,0,1,1,'',999,'displayable=false','');
INSERT INTO "fieldmetainfo" VALUES (DEFAULT,'_parentEntryUId','PARENT_ENTRY_UID','"ParentEntryUId"','java.lang.Long','com.gridnode.pdip.base.gwfbase.bpss.model.BpssProcessSpecEntry','',0,0,0,1,1,'',999,'displayable=false','');

--------- BpssProcessSpecification
DELETE FROM "fieldmetainfo" WHERE "EntityObjectName" LIKE '%BpssProcessSpecification';
INSERT INTO "fieldmetainfo" VALUES (DEFAULT,'_uId','UID','"UID"','java.lang.Long','com.gridnode.pdip.base.gwfbase.bpss.model.BpssProcessSpecification','',0,0,0,1,1,'',999,'displayable=false','');
INSERT INTO "fieldmetainfo" VALUES (DEFAULT,'_specVersion','VERSION','"Version"','java.lang.String','com.gridnode.pdip.base.gwfbase.bpss.model.BpssProcessSpecification','',0,0,0,1,1,'',999,'displayable=false','');
INSERT INTO "fieldmetainfo" VALUES (DEFAULT,'_specUUId','UUID','"UUId"','java.lang.String','com.gridnode.pdip.base.gwfbase.bpss.model.BpssProcessSpecification','',0,0,0,1,1,'',999,'displayable=false','');
INSERT INTO "fieldmetainfo" VALUES (DEFAULT,'_specName','NAME','"Name"','java.lang.String','com.gridnode.pdip.base.gwfbase.bpss.model.BpssProcessSpecification','',0,0,0,1,1,'',999,'displayable=false','');
INSERT INTO "fieldmetainfo" VALUES (DEFAULT,'_specTimestamp','TIMESTAMP','"Timestamp"','java.lang.String','com.gridnode.pdip.base.gwfbase.bpss.model.BpssProcessSpecification','',0,0,0,1,1,'',999,'displayable=false','');

--------- BpssStart
DELETE FROM "fieldmetainfo" WHERE "EntityObjectName" LIKE '%BpssStart';
INSERT INTO "fieldmetainfo" VALUES (DEFAULT,'_uId','UID','"UID"','java.lang.Long','com.gridnode.pdip.base.gwfbase.bpss.model.BpssStart','',0,0,0,1,1,'',999,'displayable=false','');
INSERT INTO "fieldmetainfo" VALUES (DEFAULT,'_processUId','PROCESS_UID','"ProcessUId"','java.lang.Long','com.gridnode.pdip.base.gwfbase.bpss.model.BpssStart','',0,0,0,1,1,'',999,'displayable=false','');
INSERT INTO "fieldmetainfo" VALUES (DEFAULT,'_isDownLink','ISDOWNLINK','"IsDownLink"','java.lang.Boolean','com.gridnode.pdip.base.gwfbase.bpss.model.BpssStart','',0,0,0,1,1,'',999,'displayable=false','');
INSERT INTO "fieldmetainfo" VALUES (DEFAULT,'_toBusinessStateKey','TO_BUSINESS_STATE_KEY','"ToBusinessStateKey"','java.lang.String','com.gridnode.pdip.base.gwfbase.bpss.model.BpssStart','',50,0,0,1,1,'',999,'displayable=false','');

--------- BpssBinaryCollaborationActivity
DELETE FROM "fieldmetainfo" WHERE "EntityObjectName" LIKE '%BpssBinaryCollaborationActivity';
INSERT INTO "fieldmetainfo" VALUES (DEFAULT,'_uId','UID','"UID"','java.lang.Long','com.gridnode.pdip.base.gwfbase.bpss.model.BpssBinaryCollaborationActivity','',0,0,0,1,1,'',999,'displayable=false','');
INSERT INTO "fieldmetainfo" VALUES (DEFAULT,'_activityName','NAME','"Name"','java.lang.String','com.gridnode.pdip.base.gwfbase.bpss.model.BpssBinaryCollaborationActivity','',50,0,0,1,1,'',999,'displayable=false','');
INSERT INTO "fieldmetainfo" VALUES (DEFAULT,'_binaryCollaborationUId','BINARY_COLLABORATION_UID','"BinCollProcessUId"','java.lang.Long','com.gridnode.pdip.base.gwfbase.bpss.model.BpssBinaryCollaborationActivity','',0,0,0,1,1,'',999,'displayable=false','');
INSERT INTO "fieldmetainfo" VALUES (DEFAULT,'_downLinkUId','DOWNLINK_UID','"DownLinkUId"','java.lang.Long','com.gridnode.pdip.base.gwfbase.bpss.model.BpssBinaryCollaborationActivity','',0,0,0,1,1,'',999,'displayable=false','');

--------- BpssBusinessPartnerRole
DELETE FROM "fieldmetainfo" WHERE "EntityObjectName" LIKE '%BpssBusinessPartnerRole';
INSERT INTO "fieldmetainfo" VALUES (DEFAULT,'_uId','UID','"UID"','java.lang.Long','com.gridnode.pdip.base.gwfbase.bpss.model.BpssBusinessPartnerRole','',0,0,0,1,1,'',999,'displayable=false','');
INSERT INTO "fieldmetainfo" VALUES (DEFAULT,'_roleName','NAME','"Name"','java.lang.String','com.gridnode.pdip.base.gwfbase.bpss.model.BpssBusinessPartnerRole','',50,0,0,1,1,'',999,'displayable=false','');

--------- BpssBusinessDocument
DELETE FROM "fieldmetainfo" WHERE "EntityObjectName" LIKE '%BpssBusinessDocument';
INSERT INTO "fieldmetainfo" VALUES(DEFAULT,'_uId','UID','"UID"','java.lang.Long','com.gridnode.pdip.base.gwfbase.bpss.model.BpssBusinessDocument','',0,0,0,0,0,'',999,'displayable=false','');
INSERT INTO "fieldmetainfo" VALUES(DEFAULT,'_name','NAME','"Name"','java.lang.String','com.gridnode.pdip.base.gwfbase.bpss.model.BpssBusinessDocument','NAME',50,0,1,1,1,'',1,'displayable=false','');
INSERT INTO "fieldmetainfo" VALUES(DEFAULT,'_expressionLanguage','EXPRESSION_LANGUAGE','"ExpressionLanguage"','java.lang.String','com.gridnode.pdip.base.gwfbase.bpss.model.BpssBusinessDocument','EXPRESSION_LANGUAGE',50,0,0,1,1,'',999,'displayable=false','');
INSERT INTO "fieldmetainfo" VALUES(DEFAULT,'_conditionExpr','CONDITION_EXPR','"ConditionExpr"','java.lang.String','com.gridnode.pdip.base.gwfbase.bpss.model.BpssBusinessDocument','CONDITION_EXPR',50,0,1,1,1,'',2,'displayable=false','');
INSERT INTO "fieldmetainfo" VALUES(DEFAULT,'_specElement','SPEC_ELEMENT','"SpecElement"','java.lang.String','com.gridnode.pdip.base.gwfbase.bpss.model.BpssBusinessDocument','SPEC_ELEMENT',50,0,1,1,1,'',3,'displayable=false','');
INSERT INTO "fieldmetainfo" VALUES(DEFAULT,'_specLocation','SPEC_LOCATION','"SpecLocation"','java.lang.String','com.gridnode.pdip.base.gwfbase.bpss.model.BpssBusinessDocument','SPEC_LOCATION',50,0,1,1,1,'',4,'displayable=false','');

--------- BpssDocumentation
DELETE FROM "fieldmetainfo" WHERE "EntityObjectName" LIKE '%BpssDocumentation';
INSERT INTO "fieldmetainfo" VALUES(DEFAULT,'_uId','UID','"UID"','java.lang.Long','com.gridnode.pdip.base.gwfbase.bpss.model.BpssDocumentation','',0,0,0,0,0,'',999,'displayable=false','');
INSERT INTO "fieldmetainfo" VALUES(DEFAULT,'_uri','URI','"Uri"','java.lang.String','com.gridnode.pdip.base.gwfbase.bpss.model.BpssDocumentation','URI',50,0,1,1,1,'',1,'displayable=false','');
INSERT INTO "fieldmetainfo" VALUES(DEFAULT,'_documentation','DOCUMENTATION','"Documentation"','java.lang.String','com.gridnode.pdip.base.gwfbase.bpss.model.BpssDocumentation','NAME',80,0,1,1,1,'',1,'displayable=false','');

--------- BpssDocumentEnvelope
DELETE FROM "fieldmetainfo" WHERE "EntityObjectName" LIKE '%BpssDocumentEnvelope';
INSERT INTO "fieldmetainfo" VALUES(DEFAULT,'_uId','UID','"UID"','java.lang.Long','com.gridnode.pdip.base.gwfbase.bpss.model.BpssDocumentEnvelope','',0,0,0,0,0,'',999,'displayable=false','');
INSERT INTO "fieldmetainfo" VALUES(DEFAULT,'_businessDocumentName','BUSINESS_DOCUMENT_NAME','"BusinessDocumentName"','java.lang.String','com.gridnode.pdip.base.gwfbase.bpss.model.BpssDocumentEnvelope','BUSINESS_DOCUMENT_NAME',50,0,1,1,1,'',1,'displayable=false','');
INSERT INTO "fieldmetainfo" VALUES(DEFAULT,'_businessDocumentIDRef','BUSINESS_DOCUMENTID_REF','"BusinessDocumentIDRef"','java.lang.String','com.gridnode.pdip.base.gwfbase.bpss.model.BpssDocumentEnvelope','BUSINESS_DOCUMENTID_REF',50,0,1,1,1,'',1,'displayable=false','');
INSERT INTO "fieldmetainfo" VALUES (DEFAULT,'_isPositiveResponse','ISPOSITIVE_RESPONSE','"IsPositiveResponse"','java.lang.Boolean','com.gridnode.pdip.base.gwfbase.bpss.model.BpssDocumentEnvelope','',0,0,0,1,1,'',999,'displayable=false','');
INSERT INTO "fieldmetainfo" VALUES (DEFAULT,'_isAuthenticated','ISAUTHENTICATED','"IsAuthenticated"','java.lang.Boolean','com.gridnode.pdip.base.gwfbase.bpss.model.BpssDocumentEnvelope','',0,0,0,1,1,'',999,'displayable=false','');
INSERT INTO "fieldmetainfo" VALUES (DEFAULT,'_isConfidential','ISCONFIDENTIAL','"IsConfidential"','java.lang.Boolean','com.gridnode.pdip.base.gwfbase.bpss.model.BpssDocumentEnvelope','',0,0,0,1,1,'',999,'displayable=false','');
INSERT INTO "fieldmetainfo" VALUES (DEFAULT,'_isTamperProof','ISTAMPERPROOF','"IsTamperProof"','java.lang.Boolean','com.gridnode.pdip.base.gwfbase.bpss.model.BpssDocumentEnvelope','',0,0,0,1,1,'',999,'displayable=false','');

--------- XpdlActivity
DELETE FROM "fieldmetainfo" WHERE "EntityObjectName" LIKE '%XpdlActivity';
INSERT INTO "fieldmetainfo" VALUES(DEFAULT,'_uId','UID','"UID"','java.lang.Long','com.gridnode.pdip.base.gwfbase.xpdl.model.XpdlActivity','',0,0,0,0,0,'',999,'displayable=false','');
INSERT INTO "fieldmetainfo" VALUES(DEFAULT,'_activityId','ACTIVITY_ID','"ActivityId"','java.lang.String','com.gridnode.pdip.base.gwfbase.xpdl.model.XpdlActivity','ACTIVITY_ID',50,0,1,1,1,'',1,'displayable=false','');
INSERT INTO "fieldmetainfo" VALUES(DEFAULT,'_activityName','ACTIVITY_NAME','"ActivityName"','java.lang.String','com.gridnode.pdip.base.gwfbase.xpdl.model.XpdlActivity','ACTIVITY_NAME',50,0,1,1,1,'',2,'displayable=false','');
INSERT INTO "fieldmetainfo" VALUES(DEFAULT,'_activityDescription','ACTIVITY_DESCRIPTION','"ActivityDescription"','java.lang.String','com.gridnode.pdip.base.gwfbase.xpdl.model.XpdlActivity','ACTIVITY_DESCRIPTION',50,0,1,1,1,'',3,'displayable=false','');
INSERT INTO "fieldmetainfo" VALUES(DEFAULT,'_extendedAttributes','EXTENDED_ATTRIBUTES','"ExtendedAttributes"','java.lang.String','com.gridnode.pdip.base.gwfbase.xpdl.model.XpdlActivity','EXTENDED_ATTRIBUTES',50,0,1,1,1,'',5,'displayable=false','');
INSERT INTO "fieldmetainfo" VALUES(DEFAULT,'_activityLimit','ACTIVITY_LIMIT','"ActivityLimit"','java.lang.Double','com.gridnode.pdip.base.gwfbase.xpdl.model.XpdlActivity','ACTIVITY_LIMIT',0,0,0,0,0,'',5,'displayable=false','');
INSERT INTO "fieldmetainfo" VALUES(DEFAULT,'_isRoute','IS_ROUTE','"IsRoute"','java.lang.Boolean','com.gridnode.pdip.base.gwfbase.xpdl.model.XpdlActivity','IS_ROUTE',0,0,0,1,1,'',6,'displayable=false','');
INSERT INTO "fieldmetainfo" VALUES(DEFAULT,'_implementationType','IMPLEMENTATION_TYPE','"ImplementationType"','java.lang.String','com.gridnode.pdip.base.gwfbase.xpdl.model.XpdlActivity','IMPLEMENTATION_TYPE',50,0,1,1,1,'',7,'displayable=false','');
INSERT INTO "fieldmetainfo" VALUES(DEFAULT,'_performerId','PERFORMER_ID','"PerformerId"','java.lang.String','com.gridnode.pdip.base.gwfbase.xpdl.model.XpdlActivity','PERFORMER_ID',50,0,1,1,1,'',8,'displayable=false','');
INSERT INTO "fieldmetainfo" VALUES(DEFAULT,'_startMode','START_MODE','"StartMode"','java.lang.String','com.gridnode.pdip.base.gwfbase.xpdl.model.XpdlActivity','START_MODE',50,0,1,1,1,'',9,'displayable=false','');
INSERT INTO "fieldmetainfo" VALUES(DEFAULT,'_finishMode','FINISH_MODE','"FinishMode"','java.lang.String','com.gridnode.pdip.base.gwfbase.xpdl.model.XpdlActivity','FINISH_MODE',50,0,1,1,1,'',10,'displayable=false','');
INSERT INTO "fieldmetainfo" VALUES(DEFAULT,'_priority','PRIORITY','"Priority"','java.lang.Long','com.gridnode.pdip.base.gwfbase.xpdl.model.XpdlActivity','PRIORITY',0,0,0,0,0,'',11,'displayable=false','');
INSERT INTO "fieldmetainfo" VALUES(DEFAULT,'_instantiation','INSTANTIATION','"Instantiation"','java.lang.String','com.gridnode.pdip.base.gwfbase.xpdl.model.XpdlActivity','INSTANTIATION',50,0,1,1,1,'',12,'displayable=false','');
INSERT INTO "fieldmetainfo" VALUES(DEFAULT,'_cost','COST','"Cost"','java.lang.Double','com.gridnode.pdip.base.gwfbase.xpdl.model.XpdlActivity','COST',0,0,0,0,0,'',13,'displayable=false','');
INSERT INTO "fieldmetainfo" VALUES(DEFAULT,'_waitingTime','WAITING_TIME','"WaitingTime"','java.lang.Double','com.gridnode.pdip.base.gwfbase.xpdl.model.XpdlActivity','WAITING_TIME',0,0,0,0,0,'',14,'displayable=false','');
INSERT INTO "fieldmetainfo" VALUES(DEFAULT,'_duration','DURATION','"Duration"','java.lang.Double','com.gridnode.pdip.base.gwfbase.xpdl.model.XpdlActivity','DURATION',0,0,0,0,0,'',15,'displayable=false','');
INSERT INTO "fieldmetainfo" VALUES(DEFAULT,'_iconUrl','ICON_URL','"IconUrl"','java.lang.String','com.gridnode.pdip.base.gwfbase.xpdl.model.XpdlActivity','ICON_URL',50,0,1,1,1,'',16,'displayable=false','');
INSERT INTO "fieldmetainfo" VALUES(DEFAULT,'_documentationUrl','DOCUMENTATION_URL','"DocumentationUrl"','java.lang.String','com.gridnode.pdip.base.gwfbase.xpdl.model.XpdlActivity','DOCUMENTATION_URL',50,0,1,1,1,'',17,'displayable=false','');
INSERT INTO "fieldmetainfo" VALUES(DEFAULT,'_transitionRestrictionListUId','TRANSITION_RESTRICTION_LIST_UID','"TransitionRestrictionListUId"','java.lang.Long','com.gridnode.pdip.base.gwfbase.xpdl.model.XpdlActivity','TRANSITION_RESTRICTION_LIST_UID',0,0,0,0,0,'',18,'displayable=false','');
INSERT INTO "fieldmetainfo" VALUES(DEFAULT,'_workingTime','WORKING_TIME','"WorkingTime"','java.lang.Double','com.gridnode.pdip.base.gwfbase.xpdl.model.XpdlActivity','WORKING_TIME',0,0,0,0,0,'',19,'displayable=false','');
INSERT INTO "fieldmetainfo" VALUES(DEFAULT,'_processId','PROCESS_ID','"ProcessId"','java.lang.String','com.gridnode.pdip.base.gwfbase.xpdl.model.XpdlActivity','PROCESS_ID',50,0,1,1,1,'',20,'displayable=false','');
INSERT INTO "fieldmetainfo" VALUES(DEFAULT,'_packageId','PACKAGE_ID','"PackageId"','java.lang.String','com.gridnode.pdip.base.gwfbase.xpdl.model.XpdlActivity','PACKAGE_ID',50,0,1,1,1,'',21,'displayable=false','');
INSERT INTO "fieldmetainfo" VALUES(DEFAULT,'_pkgVersionId','PKG_VERSION_ID','"PkgVersionId"','java.lang.String','com.gridnode.pdip.base.gwfbase.xpdl.model.XpdlActivity','PKG_VERSION_ID',50,0,1,1,1,'',22,'displayable=false','');

--------- XpdlApplication
DELETE FROM "fieldmetainfo" WHERE "EntityObjectName" LIKE '%XpdlApplication';
INSERT INTO "fieldmetainfo" VALUES(DEFAULT,'_uId','UID','"UID"','java.lang.Long','com.gridnode.pdip.base.gwfbase.xpdl.model.XpdlApplication','',0,0,0,0,0,'',999,'displayable=false','');
INSERT INTO "fieldmetainfo" VALUES(DEFAULT,'_applicationId','APPLICATION_ID','"ApplicationId"','java.lang.String','com.gridnode.pdip.base.gwfbase.xpdl.model.XpdlApplication','APPLICATION_ID',50,0,1,1,1,'',1,'displayable=false','');
INSERT INTO "fieldmetainfo" VALUES(DEFAULT,'_applicationName','APPLICATION_NAME','"ApplicationName"','java.lang.String','com.gridnode.pdip.base.gwfbase.xpdl.model.XpdlApplication','APPLICATION_NAME',50,0,1,1,1,'',2,'displayable=false','');
INSERT INTO "fieldmetainfo" VALUES(DEFAULT,'_applicationDescription','APPLICATION_DESCRIPTION','"ApplicationDescription"','java.lang.String','com.gridnode.pdip.base.gwfbase.xpdl.model.XpdlApplication','APPLICATION_DESCRIPTION',50,0,1,1,1,'',3,'displayable=false','');
INSERT INTO "fieldmetainfo" VALUES(DEFAULT,'_extendedAttributes','EXTENDED_ATTRIBUTES','"ExtendedAttributes"','java.lang.String','com.gridnode.pdip.base.gwfbase.xpdl.model.XpdlApplication','EXTENDED_ATTRIBUTES',50,0,1,1,1,'',5,'displayable=false','');
INSERT INTO "fieldmetainfo" VALUES(DEFAULT,'_processId','PROCESS_ID','"ProcessId"','java.lang.String','com.gridnode.pdip.base.gwfbase.xpdl.model.XpdlApplication','PROCESS_ID',50,0,1,1,1,'',5,'displayable=false','');
INSERT INTO "fieldmetainfo" VALUES(DEFAULT,'_packageId','PACKAGE_ID','"PackageId"','java.lang.String','com.gridnode.pdip.base.gwfbase.xpdl.model.XpdlApplication','PACKAGE_ID',50,0,1,1,1,'',6,'displayable=false','');
INSERT INTO "fieldmetainfo" VALUES(DEFAULT,'_pkgVersionId','PKG_VERSION_ID','"PkgVersionId"','java.lang.String','com.gridnode.pdip.base.gwfbase.xpdl.model.XpdlApplication','PKG_VERSION_ID',50,0,1,1,1,'',7,'displayable=false','');

--------- XpdlDataField
DELETE FROM "fieldmetainfo" WHERE "EntityObjectName" LIKE '%XpdlDataField';
INSERT INTO "fieldmetainfo" VALUES(DEFAULT,'_uId','UID','"UID"','java.lang.Long','com.gridnode.pdip.base.gwfbase.xpdl.model.XpdlDataField','',0,0,0,0,0,'',999,'displayable=false','');
INSERT INTO "fieldmetainfo" VALUES(DEFAULT,'_dataFieldId','DATA_FIELD_ID','"DataFieldId"','java.lang.String','com.gridnode.pdip.base.gwfbase.xpdl.model.XpdlDataField','DATA_FIELD_ID',50,0,1,1,1,'',1,'displayable=false','');
INSERT INTO "fieldmetainfo" VALUES(DEFAULT,'_dataFieldName','DATA_FIELD_NAME','"DataFieldName"','java.lang.String','com.gridnode.pdip.base.gwfbase.xpdl.model.XpdlDataField','DATA_FIELD_NAME',50,0,1,1,1,'',2,'displayable=false','');
INSERT INTO "fieldmetainfo" VALUES(DEFAULT,'_dataFieldDescription','DATA_FIELD_DESCRIPTION','"DataFieldDescription"','java.lang.String','com.gridnode.pdip.base.gwfbase.xpdl.model.XpdlDataField','DATA_FIELD_DESCRIPTION',50,0,1,1,1,'',3,'displayable=false','');
INSERT INTO "fieldmetainfo" VALUES(DEFAULT,'_extendedAttributes','EXTENDED_ATTRIBUTES','"ExtendedAttributes"','java.lang.String','com.gridnode.pdip.base.gwfbase.xpdl.model.XpdlDataField','EXTENDED_ATTRIBUTE_LIST_UID',0,0,0,0,0,'',4,'displayable=false','');
INSERT INTO "fieldmetainfo" VALUES(DEFAULT,'_isArray','IS_ARRAY','"IsArray"','java.lang.Boolean','com.gridnode.pdip.base.gwfbase.xpdl.model.XpdlDataField','IS_ARRAY',0,0,0,1,1,'',5,'displayable=false','');
INSERT INTO "fieldmetainfo" VALUES(DEFAULT,'_initialValue','INITIAL_VALUE','"InitialValue"','java.lang.String','com.gridnode.pdip.base.gwfbase.xpdl.model.XpdlDataField','INITIAL_VALUE',50,0,1,1,1,'',6,'displayable=false','');
INSERT INTO "fieldmetainfo" VALUES(DEFAULT,'_lengthBytes','LENGTH_BYTES','"LengthBytes"','java.lang.Long','com.gridnode.pdip.base.gwfbase.xpdl.model.XpdlDataField','LENGTH_BYTES',0,0,0,0,0,'',7,'displayable=false','');
INSERT INTO "fieldmetainfo" VALUES(DEFAULT,'_processId','PROCESS_ID','"ProcessId"','java.lang.String','com.gridnode.pdip.base.gwfbase.xpdl.model.XpdlDataField','PROCESS_ID',50,0,1,1,1,'',8,'displayable=false','');
INSERT INTO "fieldmetainfo" VALUES(DEFAULT,'packageId','PACKAGE_ID','"PackageId"','java.lang.String','com.gridnode.pdip.base.gwfbase.xpdl.model.XpdlDataField','PACKAGE_ID',50,0,1,1,1,'',9,'displayable=false','');
INSERT INTO "fieldmetainfo" VALUES(DEFAULT,'_pkgVersionId','PKG_VERSION_ID','"PkgVersionId"','java.lang.String','com.gridnode.pdip.base.gwfbase.xpdl.model.XpdlDataField','PKG_VERSION_ID',50,0,1,1,1,'',10,'displayable=false','');
INSERT INTO "fieldmetainfo" VALUES(DEFAULT,'_dataTypeName','DATATYPE_NAME','"DataTypeName"','java.lang.String','com.gridnode.pdip.base.gwfbase.xpdl.model.XpdlDataField','DATATYPE_NAME',50,0,1,1,1,'',1,'displayable=false','');
INSERT INTO "fieldmetainfo" VALUES(DEFAULT,'_complexDataTypeUId','COMPLEX_DATATYPE_UID','"ComplexDataTypeUId"','java.lang.Long','com.gridnode.pdip.base.gwfbase.xpdl.model.XpdlDataField','COMPLEX_DATATYPE_UID',0,0,0,0,0,'',4,'displayable=false','');

--------- XpdlExternalPackage
DELETE FROM "fieldmetainfo" WHERE "EntityObjectName" LIKE '%XpdlExternalPackage';
INSERT INTO "fieldmetainfo" VALUES(DEFAULT,'_uId','UID','"UID"','java.lang.Long','com.gridnode.pdip.base.gwfbase.xpdl.model.XpdlExternalPackage','',0,0,0,0,0,'',999,'displayable=false','');
INSERT INTO "fieldmetainfo" VALUES(DEFAULT,'_href','HREF','"Href"','java.lang.String','com.gridnode.pdip.base.gwfbase.xpdl.model.XpdlExternalPackage','HREF',50,0,1,1,1,'',1,'displayable=false','');
INSERT INTO "fieldmetainfo" VALUES(DEFAULT,'_extendedAttributes','EXTENDED_ATTRIBUTES','"ExtendedAttributes"','java.lang.String','com.gridnode.pdip.base.gwfbase.xpdl.model.XpdlExternalPackage','EXTENDED_ATTRIBUTE_LIST_UID',0,0,0,0,0,'',2,'displayable=false','');
INSERT INTO "fieldmetainfo" VALUES(DEFAULT,'_packageId','PACKAGE_ID','"PackageId"','java.lang.String','com.gridnode.pdip.base.gwfbase.xpdl.model.XpdlExternalPackage','PACKAGE_ID',50,0,1,1,1,'',3,'displayable=false','');
INSERT INTO "fieldmetainfo" VALUES(DEFAULT,'_pkgVersionId','PKG_VERSION_ID','"PkgVersionId"','java.lang.String','com.gridnode.pdip.base.gwfbase.xpdl.model.XpdlExternalPackage','PKG_VERSION_ID',50,0,1,1,1,'',4,'displayable=false','');

--------- XpdlFormalParam
DELETE FROM "fieldmetainfo" WHERE "EntityObjectName" LIKE '%XpdlFormalParam';
INSERT INTO "fieldmetainfo" VALUES(DEFAULT,'_uId','UID','"UID"','java.lang.Long','com.gridnode.pdip.base.gwfbase.xpdl.model.XpdlFormalParam','',0,0,0,0,0,'',999,'displayable=false','');
INSERT INTO "fieldmetainfo" VALUES(DEFAULT,'_formalParamId','FORMAL_PARAM_ID','"FormalParamId"','java.lang.String','com.gridnode.pdip.base.gwfbase.xpdl.model.XpdlFormalParam','FORMAL_PARAM_ID',50,0,1,1,1,'',1,'displayable=false','');
INSERT INTO "fieldmetainfo" VALUES(DEFAULT,'_mode','MODE','"Mode"','java.lang.String','com.gridnode.pdip.base.gwfbase.xpdl.model.XpdlFormalParam','MODE',50,0,1,1,1,'',2,'displayable=false','');
INSERT INTO "fieldmetainfo" VALUES(DEFAULT,'_indexNumber','INDEX_NUMBER','"IndexNumber"','java.lang.Integer','com.gridnode.pdip.base.gwfbase.xpdl.model.XpdlFormalParam','INDEX_NUMBER',0,0,0,0,0,'',3,'displayable=false','');
INSERT INTO "fieldmetainfo" VALUES(DEFAULT,'_formalParamDescription','FORMAL_PARAM_DESCRIPTION','"FormalParamDescription"','java.lang.String','com.gridnode.pdip.base.gwfbase.xpdl.model.XpdlFormalParam','FORMAL_PARAM_DESCRIPTION',50,0,1,1,1,'',4,'displayable=false','');
INSERT INTO "fieldmetainfo" VALUES(DEFAULT,'_applicationId','APPLICATION_ID','"ApplicationId"','java.lang.String','com.gridnode.pdip.base.gwfbase.xpdl.model.XpdlFormalParam','APPLICATION_ID',50,0,1,1,1,'',5,'displayable=false','');
INSERT INTO "fieldmetainfo" VALUES(DEFAULT,'_processId','PROCESS_ID','"ProcessId"','java.lang.String','com.gridnode.pdip.base.gwfbase.xpdl.model.XpdlFormalParam','PROCESS_ID',50,0,1,1,1,'',6,'displayable=false','');
INSERT INTO "fieldmetainfo" VALUES(DEFAULT,'_packageId','PACKAGE_ID','"PackageId"','java.lang.String','com.gridnode.pdip.base.gwfbase.xpdl.model.XpdlFormalParam','PACKAGE_ID',50,0,1,1,1,'',7,'displayable=false','');
INSERT INTO "fieldmetainfo" VALUES(DEFAULT,'_pkgVersionId','PKG_VERSION_ID','"PkgVersionId"','java.lang.String','com.gridnode.pdip.base.gwfbase.xpdl.model.XpdlFormalParam','PKG_VERSION_ID',50,0,1,1,1,'',8,'displayable=false','');
INSERT INTO "fieldmetainfo" VALUES(DEFAULT,'_dataTypeName','DATATYPE_NAME','"DataTypeName"','java.lang.String','com.gridnode.pdip.base.gwfbase.xpdl.model.XpdlFormalParam','DATATYPE_NAME',50,0,1,1,1,'',1,'displayable=false','');
INSERT INTO "fieldmetainfo" VALUES(DEFAULT,'_complexDataTypeUId','COMPLEX_DATATYPE_UID','"ComplexDataTypeUId"','java.lang.Long','com.gridnode.pdip.base.gwfbase.xpdl.model.XpdlFormalParam','COMPLEX_DATATYPE_UID',0,0,0,0,0,'',4,'displayable=false','');

--------- XpdlPackage
DELETE FROM "fieldmetainfo" WHERE "EntityObjectName" LIKE '%XpdlPackage';
INSERT INTO "fieldmetainfo" VALUES(DEFAULT,'_uId','UID','"UID"','java.lang.Long','com.gridnode.pdip.base.gwfbase.xpdl.model.XpdlPackage','',0,0,0,0,0,'',999,'displayable=false','');
INSERT INTO "fieldmetainfo" VALUES(DEFAULT,'_packageId','PACKAGE_ID','"PackageId"','java.lang.String','com.gridnode.pdip.base.gwfbase.xpdl.model.XpdlPackage','PACKAGE_ID',50,0,1,1,1,'',1,'displayable=false','');
INSERT INTO "fieldmetainfo" VALUES(DEFAULT,'_packageName','PACKAGE_NAME','"PackageName"','java.lang.String','com.gridnode.pdip.base.gwfbase.xpdl.model.XpdlPackage','PACKAGE_NAME',50,0,1,1,1,'',2,'displayable=false','');
INSERT INTO "fieldmetainfo" VALUES(DEFAULT,'_packageDescription','PACKAGE_DESCRIPTION','"PackageDescription"','java.lang.String','com.gridnode.pdip.base.gwfbase.xpdl.model.XpdlPackage','PACKAGE_DESCRIPTION',50,0,1,1,1,'',3,'displayable=false','');
INSERT INTO "fieldmetainfo" VALUES(DEFAULT,'_extendedAttributes','EXTENDED_ATTRIBUTES','"ExtendedAttributes"','java.lang.String','com.gridnode.pdip.base.gwfbase.xpdl.model.XpdlPackage','EXTENDED_ATTRIBUTE_LIST_UID',0,0,0,0,0,'',4,'displayable=false','');
INSERT INTO "fieldmetainfo" VALUES(DEFAULT,'_specificationId','SPECIFICATION_ID','"SpecificationId"','java.lang.String','com.gridnode.pdip.base.gwfbase.xpdl.model.XpdlPackage','SPECIFICATION_ID',50,0,1,1,1,'',5,'displayable=false','');
INSERT INTO "fieldmetainfo" VALUES(DEFAULT,'_specificationVersion','SPECIFICATION_VERSION','"SpecificationVersion"','java.lang.String','com.gridnode.pdip.base.gwfbase.xpdl.model.XpdlPackage','SPECIFICATION_VERSION',50,0,1,1,1,'',6,'displayable=false','');
INSERT INTO "fieldmetainfo" VALUES(DEFAULT,'_sourceVendorInfo','SOURCE_VENDOR_INFO','"SourceVendorInfo"','java.lang.String','com.gridnode.pdip.base.gwfbase.xpdl.model.XpdlPackage','SOURCE_VENDOR_INFO',50,0,1,1,1,'',7,'displayable=false','');
INSERT INTO "fieldmetainfo" VALUES(DEFAULT,'_creationDateTime','CREATION_DATE_TIME','"CreationDateTime"','java.util.Date','com.gridnode.pdip.base.gwfbase.xpdl.model.XpdlPackage','CREATION_DATE_TIME',0,0,0,0,1,'',8,'displayable=false','');
INSERT INTO "fieldmetainfo" VALUES(DEFAULT,'_documentationUrl','DOCUMENTATION_URL','"DocumentationUrl"','java.lang.String','com.gridnode.pdip.base.gwfbase.xpdl.model.XpdlPackage','DOCUMENTATION_URL',50,0,1,1,1,'',9,'displayable=false','');
INSERT INTO "fieldmetainfo" VALUES(DEFAULT,'_priorityUnit','PRIORITY_UNIT','"PriorityUnit"','java.lang.String','com.gridnode.pdip.base.gwfbase.xpdl.model.XpdlPackage','PRIORITY_UNIT',50,0,1,1,1,'',10,'displayable=false','');
INSERT INTO "fieldmetainfo" VALUES(DEFAULT,'_costUnit','COST_UNIT','"CostUnit"','java.lang.String','com.gridnode.pdip.base.gwfbase.xpdl.model.XpdlPackage','COST_UNIT',50,0,1,1,1,'',11,'displayable=false','');
INSERT INTO "fieldmetainfo" VALUES(DEFAULT,'_author','AUTHOR','"Author"','java.lang.String','com.gridnode.pdip.base.gwfbase.xpdl.model.XpdlPackage','AUTHOR',50,0,1,1,1,'',12,'displayable=false','');
INSERT INTO "fieldmetainfo" VALUES(DEFAULT,'_versionId','VERSION_ID','"VersionId"','java.lang.String','com.gridnode.pdip.base.gwfbase.xpdl.model.XpdlPackage','VERSION_ID',50,0,1,1,1,'',13,'displayable=false','');
INSERT INTO "fieldmetainfo" VALUES(DEFAULT,'_codepage','CODEPAGE','"Codepage"','java.lang.String','com.gridnode.pdip.base.gwfbase.xpdl.model.XpdlPackage','CODEPAGE',50,0,1,1,1,'',14,'displayable=false','');
INSERT INTO "fieldmetainfo" VALUES(DEFAULT,'_countrykey','COUNTRYKEY','"Countrykey"','java.lang.String','com.gridnode.pdip.base.gwfbase.xpdl.model.XpdlPackage','COUNTRYKEY',50,0,1,1,1,'',15,'displayable=false','');
INSERT INTO "fieldmetainfo" VALUES(DEFAULT,'_publicationStatus','PUBLICATION_STATUS','"PublicationStatus"','java.lang.String','com.gridnode.pdip.base.gwfbase.xpdl.model.XpdlPackage','PUBLICATION_STATUS',50,0,1,1,1,'',16,'displayable=false','');
INSERT INTO "fieldmetainfo" VALUES(DEFAULT,'_responsibleListUId','RESPONSIBLE_LIST_UID','"ResponsibleListUId"','java.lang.Long','com.gridnode.pdip.base.gwfbase.xpdl.model.XpdlPackage','RESPONSIBLE_LIST_UID',0,0,0,0,0,'',17,'displayable=false','');
INSERT INTO "fieldmetainfo" VALUES(DEFAULT,'_graphConformance','GRAPH_CONFORMANCE','"GraphConformance"','java.lang.String','com.gridnode.pdip.base.gwfbase.xpdl.model.XpdlPackage','GRAPH_CONFORMANCE',50,0,1,1,1,'',18,'displayable=false','');
INSERT INTO "fieldmetainfo" VALUES(DEFAULT,'_state','STATE','"State"','java.lang.Short','com.gridnode.pdip.base.gwfbase.xpdl.model.XpdlPackage','XpdlPackage.state',1,0,1,1,1,'',999,'displayable=false','');

--------- XpdlParticipant
DELETE FROM "fieldmetainfo" WHERE "EntityObjectName" LIKE '%XpdlParticipant';
INSERT INTO "fieldmetainfo" VALUES(DEFAULT,'_uId','UID','"UID"','java.lang.Long','com.gridnode.pdip.base.gwfbase.xpdl.model.XpdlParticipant','',0,0,0,0,0,'',999,'displayable=false','');
INSERT INTO "fieldmetainfo" VALUES(DEFAULT,'_participantId','PARTICIPANT_ID','"ParticipantId"','java.lang.String','com.gridnode.pdip.base.gwfbase.xpdl.model.XpdlParticipant','PARTICIPANT_ID',50,0,1,1,1,'',1,'displayable=false','');
INSERT INTO "fieldmetainfo" VALUES(DEFAULT,'_participantName','PARTICIPANT_NAME','"ParticipantName"','java.lang.String','com.gridnode.pdip.base.gwfbase.xpdl.model.XpdlParticipant','PARTICIPANT_NAME',50,0,1,1,1,'',2,'displayable=false','');
INSERT INTO "fieldmetainfo" VALUES(DEFAULT,'_participantDescription','PARTICIPANT_DESCRIPTION','"ParticipantDescription"','java.lang.String','com.gridnode.pdip.base.gwfbase.xpdl.model.XpdlParticipant','PARTICIPANT_DESCRIPTION',50,0,1,1,1,'',3,'displayable=false','');
INSERT INTO "fieldmetainfo" VALUES(DEFAULT,'_extendedAttributes','EXTENDED_ATTRIBUTES','"ExtendedAttributes"','java.lang.String','com.gridnode.pdip.base.gwfbase.xpdl.model.XpdlParticipant','EXTENDED_ATTRIBUTE_LIST_UID',0,0,0,0,0,'',4,'displayable=false','');
INSERT INTO "fieldmetainfo" VALUES(DEFAULT,'_participantTypeId','PARTICIPANT_TYPE_ID','"ParticipantTypeId"','java.lang.String','com.gridnode.pdip.base.gwfbase.xpdl.model.XpdlParticipant','PARTICIPANT_TYPE_ID',50,0,1,1,1,'',5,'displayable=false','');
INSERT INTO "fieldmetainfo" VALUES(DEFAULT,'_processId','PROCESS_ID','"ProcessId"','java.lang.String','com.gridnode.pdip.base.gwfbase.xpdl.model.XpdlParticipant','PROCESS_ID',50,0,1,1,1,'',5,'displayable=false','');
INSERT INTO "fieldmetainfo" VALUES(DEFAULT,'_packageId','PACKAGE_ID','"PackageId"','java.lang.String','com.gridnode.pdip.base.gwfbase.xpdl.model.XpdlParticipant','PACKAGE_ID',50,0,1,1,1,'',6,'displayable=false','');
INSERT INTO "fieldmetainfo" VALUES(DEFAULT,'_pkgVersionId','PKG_VERSION_ID','"PkgVersionId"','java.lang.String','com.gridnode.pdip.base.gwfbase.xpdl.model.XpdlParticipant','PKG_VERSION_ID',50,0,1,1,1,'',7,'displayable=false','');

--------- XpdlParticipantList
DELETE FROM "fieldmetainfo" WHERE "EntityObjectName" LIKE '%XpdlParticipantList';
INSERT INTO "fieldmetainfo" VALUES(DEFAULT,'_uId','UID','"UID"','java.lang.Long','com.gridnode.pdip.base.gwfbase.xpdl.model.XpdlParticipantList','',0,0,0,0,0,'',999,'displayable=false','');
INSERT INTO "fieldmetainfo" VALUES(DEFAULT,'_participantId','PARTICIPANT_ID','"ParticipantId"','java.lang.String','com.gridnode.pdip.base.gwfbase.xpdl.model.XpdlParticipantList','PARTICIPANT_ID',50,0,1,1,1,'',1,'displayable=false','');
INSERT INTO "fieldmetainfo" VALUES(DEFAULT,'_participantIndex','PARTICIPANT_INDEX','"ParticipantIndex"','java.lang.Long','com.gridnode.pdip.base.gwfbase.xpdl.model.XpdlParticipantList','PARTICIPANT_INDEX',0,0,0,0,0,'',2,'displayable=false','');
INSERT INTO "fieldmetainfo" VALUES(DEFAULT,'_listUId','LIST_UID','"ListUId"','java.lang.Long','com.gridnode.pdip.base.gwfbase.xpdl.model.XpdlParticipantList','LIST_UID',0,0,0,0,0,'',3,'displayable=false','');

--------- XpdlProcess
DELETE FROM "fieldmetainfo" WHERE "EntityObjectName" LIKE '%XpdlProcess';
INSERT INTO "fieldmetainfo" VALUES(DEFAULT,'_uId','UID','"UID"','java.lang.Long','com.gridnode.pdip.base.gwfbase.xpdl.model.XpdlProcess','',0,0,0,0,0,'',999,'displayable=false','');
INSERT INTO "fieldmetainfo" VALUES(DEFAULT,'_processId','PROCESS_ID','"ProcessId"','java.lang.String','com.gridnode.pdip.base.gwfbase.xpdl.model.XpdlProcess','PROCESS_ID',50,0,1,1,1,'',1,'displayable=false','');
INSERT INTO "fieldmetainfo" VALUES(DEFAULT,'_processName','PROCESS_NAME','"ProcessName"','java.lang.String','com.gridnode.pdip.base.gwfbase.xpdl.model.XpdlProcess','PROCESS_NAME',50,0,1,1,1,'',2,'displayable=false','');
INSERT INTO "fieldmetainfo" VALUES(DEFAULT,'_processDescription','PROCESS_DESCRIPTION','"ProcessDescription"','java.lang.String','com.gridnode.pdip.base.gwfbase.xpdl.model.XpdlProcess','PROCESS_DESCRIPTION',50,0,1,1,1,'',3,'displayable=false','');
INSERT INTO "fieldmetainfo" VALUES(DEFAULT,'_extendedAttributes','EXTENDED_ATTRIBUTES','"ExtendedAttributes"','java.lang.String','com.gridnode.pdip.base.gwfbase.xpdl.model.XpdlProcess','EXTENDED_ATTRIBUTE_LIST_UID',0,0,0,0,0,'',4,'displayable=false','');
INSERT INTO "fieldmetainfo" VALUES(DEFAULT,'_durationUnit','DURATION_UNIT','"DurationUnit"','java.lang.String','com.gridnode.pdip.base.gwfbase.xpdl.model.XpdlProcess','DURATION_UNIT',50,0,1,1,1,'',5,'displayable=false','');
INSERT INTO "fieldmetainfo" VALUES(DEFAULT,'_creationDateTime','CREATION_DATE_TIME','"CreationDateTime"','java.util.Date','com.gridnode.pdip.base.gwfbase.xpdl.model.XpdlProcess','CREATION_DATE_TIME',0,0,0,0,1,'',6,'displayable=false','');
INSERT INTO "fieldmetainfo" VALUES(DEFAULT,'_headerDescription','HEADER_DESCRIPTION','"HeaderDescription"','java.lang.String','com.gridnode.pdip.base.gwfbase.xpdl.model.XpdlProcess','HEADER_DESCRIPTION',50,0,1,1,1,'',7,'displayable=false','');
INSERT INTO "fieldmetainfo" VALUES(DEFAULT,'_priority','PRIORITY','"Priority"','java.lang.Long','com.gridnode.pdip.base.gwfbase.xpdl.model.XpdlProcess','PRIORITY',0,0,0,0,0,'',8,'displayable=false','');
INSERT INTO "fieldmetainfo" VALUES(DEFAULT,'_processLimit','PROCESS_LIMIT','"ProcessLimit"','java.lang.Double','com.gridnode.pdip.base.gwfbase.xpdl.model.XpdlProcess','PROCESS_LIMIT',0,0,0,0,0,'',9,'displayable=false','');
INSERT INTO "fieldmetainfo" VALUES(DEFAULT,'_validFromDate','VALID_FROM_DATE','"ValidFromDate"','java.util.Date','com.gridnode.pdip.base.gwfbase.xpdl.model.XpdlProcess','VALID_FROM_DATE',0,0,0,0,1,'',10,'displayable=false','');
INSERT INTO "fieldmetainfo" VALUES(DEFAULT,'_validToDate','VALID_TO_DATE','"ValidToDate"','java.util.Date','com.gridnode.pdip.base.gwfbase.xpdl.model.XpdlProcess','VALID_TO_DATE',0,0,0,0,1,'',11,'displayable=false','');
INSERT INTO "fieldmetainfo" VALUES(DEFAULT,'_waitingTime','WAITING_TIME','"WaitingTime"','java.lang.Double','com.gridnode.pdip.base.gwfbase.xpdl.model.XpdlProcess','WAITING_TIME',0,0,0,0,0,'',12,'displayable=false','');
INSERT INTO "fieldmetainfo" VALUES(DEFAULT,'_workingTime','WORKING_TIME','"WorkingTime"','java.lang.Double','com.gridnode.pdip.base.gwfbase.xpdl.model.XpdlProcess','WORKING_TIME',0,0,0,0,0,'',13,'displayable=false','');
INSERT INTO "fieldmetainfo" VALUES(DEFAULT,'_duration','DURATION','"Duration"','java.lang.Double','com.gridnode.pdip.base.gwfbase.xpdl.model.XpdlProcess','DURATION',0,0,0,0,0,'',14,'displayable=false','');
INSERT INTO "fieldmetainfo" VALUES(DEFAULT,'_author','AUTHOR','"Author"','java.lang.String','com.gridnode.pdip.base.gwfbase.xpdl.model.XpdlProcess','AUTHOR',50,0,1,1,1,'',15,'displayable=false','');
INSERT INTO "fieldmetainfo" VALUES(DEFAULT,'_versionId','VERSION_ID','"VersionId"','java.lang.String','com.gridnode.pdip.base.gwfbase.xpdl.model.XpdlProcess','VERSION_ID',50,0,1,1,1,'',16,'displayable=false','');
INSERT INTO "fieldmetainfo" VALUES(DEFAULT,'_codepage','CODEPAGE','"Codepage"','java.lang.String','com.gridnode.pdip.base.gwfbase.xpdl.model.XpdlProcess','CODEPAGE',50,0,1,1,1,'',17,'displayable=false','');
INSERT INTO "fieldmetainfo" VALUES(DEFAULT,'_countrykey','COUNTRYKEY','"Countrykey"','java.lang.String','com.gridnode.pdip.base.gwfbase.xpdl.model.XpdlProcess','COUNTRYKEY',50,0,1,1,1,'',18,'displayable=false','');
INSERT INTO "fieldmetainfo" VALUES(DEFAULT,'_publicationStatus','PUBLICATION_STATUS','"PublicationStatus"','java.lang.String','com.gridnode.pdip.base.gwfbase.xpdl.model.XpdlProcess','PUBLICATION_STATUS',50,0,1,1,1,'',19,'displayable=false','');
INSERT INTO "fieldmetainfo" VALUES(DEFAULT,'_responsibleListUId','RESPONSIBLE_LIST_UID','"ResponsibleListUId"','java.lang.Long','com.gridnode.pdip.base.gwfbase.xpdl.model.XpdlProcess','RESPONSIBLE_LIST_UID',0,0,0,0,0,'',20,'displayable=false','');
INSERT INTO "fieldmetainfo" VALUES(DEFAULT,'_packageId','PACKAGE_ID','"PackageId"','java.lang.String','com.gridnode.pdip.base.gwfbase.xpdl.model.XpdlProcess','PACKAGE_ID',50,0,1,1,1,'',22,'displayable=false','');
INSERT INTO "fieldmetainfo" VALUES(DEFAULT,'_defaultStartActivityId','DEFAULT_START_ACTIVITY_ID','"DefaultStartActivityId"','java.lang.String','com.gridnode.pdip.base.gwfbase.xpdl.model.XpdlProcess','DEFAULT_START_ACTIVITY_ID',50,0,1,1,1,'',23,'displayable=false','');
INSERT INTO "fieldmetainfo" VALUES(DEFAULT,'_pkgVersionId','PKG_VERSION_ID','"PkgVersionId"','java.lang.String','com.gridnode.pdip.base.gwfbase.xpdl.model.XpdlProcess','PKG_VERSION_ID',50,0,1,1,1,'',24,'displayable=false','');

--------- XpdlSubFlow
DELETE FROM "fieldmetainfo" WHERE "EntityObjectName" LIKE '%XpdlSubFlow';
INSERT INTO "fieldmetainfo" VALUES(DEFAULT,'_uId','UID','"UID"','java.lang.Long','com.gridnode.pdip.base.gwfbase.xpdl.model.XpdlSubFlow','',0,0,0,0,0,'',999,'displayable=false','');
INSERT INTO "fieldmetainfo" VALUES(DEFAULT,'_subFlowId','SUB_FLOW_ID','"SubFlowId"','java.lang.String','com.gridnode.pdip.base.gwfbase.xpdl.model.XpdlSubFlow','SUB_FLOW_ID',50,0,1,1,1,'',1,'displayable=false','');
INSERT INTO "fieldmetainfo" VALUES(DEFAULT,'_subFlowType','SUB_FLOW_TYPE','"SubFlowType"','java.lang.String','com.gridnode.pdip.base.gwfbase.xpdl.model.XpdlSubFlow','SUB_FLOW_TYPE',50,0,1,1,1,'',2,'displayable=false','');
INSERT INTO "fieldmetainfo" VALUES(DEFAULT,'_actualParameters','ACTUAL_PARAMETERS','"ActualParameters"','java.lang.String','com.gridnode.pdip.base.gwfbase.xpdl.model.XpdlSubFlow','ACTUAL_PARAMETERS',50,0,1,1,1,'',3,'displayable=false','');
INSERT INTO "fieldmetainfo" VALUES(DEFAULT,'_activityId','ACTIVITY_ID','"ActivityId"','java.lang.String','com.gridnode.pdip.base.gwfbase.xpdl.model.XpdlSubFlow','ACTIVITY_ID',50,0,1,1,1,'',4,'displayable=false','');
INSERT INTO "fieldmetainfo" VALUES(DEFAULT,'_processId','PROCESS_ID','"ProcessId"','java.lang.String','com.gridnode.pdip.base.gwfbase.xpdl.model.XpdlSubFlow','PROCESS_ID',50,0,1,1,1,'',5,'displayable=false','');
INSERT INTO "fieldmetainfo" VALUES(DEFAULT,'_packageId','PACKAGE_ID','"PackageId"','java.lang.String','com.gridnode.pdip.base.gwfbase.xpdl.model.XpdlSubFlow','PACKAGE_ID',50,0,1,1,1,'',6,'displayable=false','');
INSERT INTO "fieldmetainfo" VALUES(DEFAULT,'_pkgVersionId','PKG_VERSION_ID','"PkgVersionId"','java.lang.String','com.gridnode.pdip.base.gwfbase.xpdl.model.XpdlSubFlow','PKG_VERSION_ID',50,0,1,1,1,'',7,'displayable=false','');

--------- XpdlTool
DELETE FROM "fieldmetainfo" WHERE "EntityObjectName" LIKE '%XpdlTool';
INSERT INTO "fieldmetainfo" VALUES(DEFAULT,'_uId','UID','"UID"','java.lang.Long','com.gridnode.pdip.base.gwfbase.xpdl.model.XpdlTool','',0,0,0,0,0,'',999,'displayable=false','');
INSERT INTO "fieldmetainfo" VALUES(DEFAULT,'_toolId','TOOL_ID','"ToolId"','java.lang.String','com.gridnode.pdip.base.gwfbase.xpdl.model.XpdlTool','TOOL_ID',50,0,1,1,1,'',1,'displayable=false','');
INSERT INTO "fieldmetainfo" VALUES(DEFAULT,'_toolType','TOOL_TYPE','"ToolType"','java.lang.String','com.gridnode.pdip.base.gwfbase.xpdl.model.XpdlTool','TOOL_TYPE',50,0,1,1,1,'',2,'displayable=false','');
INSERT INTO "fieldmetainfo" VALUES(DEFAULT,'_toolDescription','TOOL_DESCRIPTION','"ToolDescription"','java.lang.String','com.gridnode.pdip.base.gwfbase.xpdl.model.XpdlTool','TOOL_DESCRIPTION',50,0,1,1,1,'',3,'displayable=false','');
INSERT INTO "fieldmetainfo" VALUES(DEFAULT,'_actualParameters','ACTUAL_PARAMETERS','"ActualParameters"','java.lang.String','com.gridnode.pdip.base.gwfbase.xpdl.model.XpdlTool','ACTUAL_PARAMETERS',50,0,1,1,1,'',4,'displayable=false','');
INSERT INTO "fieldmetainfo" VALUES(DEFAULT,'_extendedAttributes','EXTENDED_ATTRIBUTES','"ExtendedAttributes"','java.lang.String','com.gridnode.pdip.base.gwfbase.xpdl.model.XpdlTool','EXTENDED_ATTRIBUTES',50,0,1,1,1,'',5,'displayable=false','');
INSERT INTO "fieldmetainfo" VALUES(DEFAULT,'_loopKind','LOOP_KIND','"LoopKind"','java.lang.String','com.gridnode.pdip.base.gwfbase.xpdl.model.XpdlTool','LOOP_KIND',50,0,1,1,1,'',6,'displayable=false','');
INSERT INTO "fieldmetainfo" VALUES(DEFAULT,'_conditionExpr','CONDITION_EXPR','"ConditionExpr"','java.lang.String','com.gridnode.pdip.base.gwfbase.xpdl.model.XpdlTool','CONDITION_EXPR',50,0,1,1,1,'',7,'displayable=false','');
INSERT INTO "fieldmetainfo" VALUES(DEFAULT,'_activityId','ACTIVITY_ID','"ActivityId"','java.lang.String','com.gridnode.pdip.base.gwfbase.xpdl.model.XpdlTool','ACTIVITY_ID',50,0,1,1,1,'',8,'displayable=false','');
INSERT INTO "fieldmetainfo" VALUES(DEFAULT,'_processId','PROCESS_ID','"ProcessId"','java.lang.String','com.gridnode.pdip.base.gwfbase.xpdl.model.XpdlTool','PROCESS_ID',50,0,1,1,1,'',9,'displayable=false','');
INSERT INTO "fieldmetainfo" VALUES(DEFAULT,'_packageId','PACKAGE_ID','"PackageId"','java.lang.String','com.gridnode.pdip.base.gwfbase.xpdl.model.XpdlTool','PACKAGE_ID',50,0,1,1,1,'',10,'displayable=false','');
INSERT INTO "fieldmetainfo" VALUES(DEFAULT,'_pkgVersionId','PKG_VERSION_ID','"PkgVersionId"','java.lang.String','com.gridnode.pdip.base.gwfbase.xpdl.model.XpdlTool','PKG_VERSION_ID',50,0,1,1,1,'',11,'displayable=false','');

--------- XpdlTransition
DELETE FROM "fieldmetainfo" WHERE "EntityObjectName" LIKE '%XpdlTransition';
INSERT INTO "fieldmetainfo" VALUES(DEFAULT,'_uId','UID','"UID"','java.lang.Long','com.gridnode.pdip.base.gwfbase.xpdl.model.XpdlTransition','',0,0,0,0,0,'',999,'displayable=false','');
INSERT INTO "fieldmetainfo" VALUES(DEFAULT,'_transitionId','TRANSITION_ID','"TransitionId"','java.lang.String','com.gridnode.pdip.base.gwfbase.xpdl.model.XpdlTransition','TRANSITION_ID',50,0,1,1,1,'',1,'displayable=false','');
INSERT INTO "fieldmetainfo" VALUES(DEFAULT,'_transitionName','TRANSITION_NAME','"TransitionName"','java.lang.String','com.gridnode.pdip.base.gwfbase.xpdl.model.XpdlTransition','TRANSITION_NAME',50,0,1,1,1,'',2,'displayable=false','');
INSERT INTO "fieldmetainfo" VALUES(DEFAULT,'_transitionDescription','TRANSITION_DESCRIPTION','"TransitionDescription"','java.lang.String','com.gridnode.pdip.base.gwfbase.xpdl.model.XpdlTransition','TRANSITION_DESCRIPTION',50,0,1,1,1,'',3,'displayable=false','');
INSERT INTO "fieldmetainfo" VALUES(DEFAULT,'_extendedAttributes','EXTENDED_ATTRIBUTES','"ExtendedAttributes"','java.lang.String','com.gridnode.pdip.base.gwfbase.xpdl.model.XpdlTransition','EXTENDED_ATTRIBUTE_LIST_UID',0,0,0,0,0,'',4,'displayable=false','');
INSERT INTO "fieldmetainfo" VALUES(DEFAULT,'_fromActivityId','FROM_ACTIVITY_ID','"FromActivityId"','java.lang.String','com.gridnode.pdip.base.gwfbase.xpdl.model.XpdlTransition','FROM_ACTIVITY_ID',50,0,1,1,1,'',5,'displayable=false','');
INSERT INTO "fieldmetainfo" VALUES(DEFAULT,'_toActivityId','TO_ACTIVITY_ID','"ToActivityId"','java.lang.String','com.gridnode.pdip.base.gwfbase.xpdl.model.XpdlTransition','TO_ACTIVITY_ID',50,0,1,1,1,'',6,'displayable=false','');
INSERT INTO "fieldmetainfo" VALUES(DEFAULT,'_loopType','LOOP_TYPE','"LoopType"','java.lang.String','com.gridnode.pdip.base.gwfbase.xpdl.model.XpdlTransition','LOOP_TYPE',50,0,1,1,1,'',7,'displayable=false','');
INSERT INTO "fieldmetainfo" VALUES(DEFAULT,'_conditionType','CONDITION_TYPE','"ConditionType"','java.lang.String','com.gridnode.pdip.base.gwfbase.xpdl.model.XpdlTransition','CONDITION_TYPE',50,0,1,1,1,'',8,'displayable=false','');
INSERT INTO "fieldmetainfo" VALUES(DEFAULT,'_conditionExpr','CONDITION_EXPR','"ConditionExpr"','java.lang.String','com.gridnode.pdip.base.gwfbase.xpdl.model.XpdlTransition','CONDITION_EXPR',50,0,1,1,1,'',9,'displayable=false','');
INSERT INTO "fieldmetainfo" VALUES(DEFAULT,'_processId','PROCESS_ID','"ProcessId"','java.lang.String','com.gridnode.pdip.base.gwfbase.xpdl.model.XpdlTransition','PROCESS_ID',50,0,1,1,1,'',10,'displayable=false','');
INSERT INTO "fieldmetainfo" VALUES(DEFAULT,'_packageId','PACKAGE_ID','"PackageId"','java.lang.String','com.gridnode.pdip.base.gwfbase.xpdl.model.XpdlTransition','PACKAGE_ID',50,0,1,1,1,'',11,'displayable=false','');
INSERT INTO "fieldmetainfo" VALUES(DEFAULT,'_pkgVersionId','PKG_VERSION_ID','"PkgVersionId"','java.lang.String','com.gridnode.pdip.base.gwfbase.xpdl.model.XpdlTransition','PKG_VERSION_ID',50,0,1,1,1,'',12,'displayable=false','');

--------- XpdlTransitionRef
DELETE FROM "fieldmetainfo" WHERE "EntityObjectName" LIKE '%XpdlTransitionRef';
INSERT INTO "fieldmetainfo" VALUES(DEFAULT,'_uId','UID','"UID"','java.lang.Long','com.gridnode.pdip.base.gwfbase.xpdl.model.XpdlTransitionRef','',0,0,0,0,0,'',999,'displayable=false','');
INSERT INTO "fieldmetainfo" VALUES(DEFAULT,'_transitionId','TRANSITION_ID','"TransitionId"','java.lang.String','com.gridnode.pdip.base.gwfbase.xpdl.model.XpdlTransitionRef','TRANSITION_ID',50,0,1,1,1,'',1,'displayable=false','');
INSERT INTO "fieldmetainfo" VALUES(DEFAULT,'_listUId','LIST_UID','"ListUId"','java.lang.Long','com.gridnode.pdip.base.gwfbase.xpdl.model.XpdlTransitionRef','LIST_UID',0,0,0,0,0,'',2,'displayable=false','');

--------- XpdlTransitionRestriction
DELETE FROM "fieldmetainfo" WHERE "EntityObjectName" LIKE '%XpdlTransitionRestriction';
INSERT INTO "fieldmetainfo" VALUES(DEFAULT,'_uId','UID','"UID"','java.lang.Long','com.gridnode.pdip.base.gwfbase.xpdl.model.XpdlTransitionRestriction','',0,0,0,0,0,'',999,'displayable=false','');
INSERT INTO "fieldmetainfo" VALUES(DEFAULT,'_isInlineBlock','IS_INLINE_BLOCK','"IsInlineBlock"','java.lang.Boolean','com.gridnode.pdip.base.gwfbase.xpdl.model.XpdlTransitionRestriction','IS_INLINE_BLOCK',0,0,0,1,1,'',1,'displayable=false','');
INSERT INTO "fieldmetainfo" VALUES(DEFAULT,'_blockName','BLOCK_NAME','"BlockName"','java.lang.String','com.gridnode.pdip.base.gwfbase.xpdl.model.XpdlTransitionRestriction','BLOCK_NAME',50,0,1,1,1,'',2,'displayable=false','');
INSERT INTO "fieldmetainfo" VALUES(DEFAULT,'_blockDescription','BLOCK_DESCRIPTION','"BlockDescription"','java.lang.String','com.gridnode.pdip.base.gwfbase.xpdl.model.XpdlTransitionRestriction','BLOCK_DESCRIPTION',50,0,1,1,1,'',3,'displayable=false','');
INSERT INTO "fieldmetainfo" VALUES(DEFAULT,'_blockIconUrl','BLOCK_ICON_URL','"BlockIconUrl"','java.lang.String','com.gridnode.pdip.base.gwfbase.xpdl.model.XpdlTransitionRestriction','BLOCK_ICON_URL',50,0,1,1,1,'',4,'displayable=false','');
INSERT INTO "fieldmetainfo" VALUES(DEFAULT,'_blockDocumentationUrl','BLOCK_DOCUMENTATION_URL','"BlockDocumentationUrl"','java.lang.String','com.gridnode.pdip.base.gwfbase.xpdl.model.XpdlTransitionRestriction','BLOCK_DOCUMENTATION_URL',50,0,1,1,1,'',5,'displayable=false','');
INSERT INTO "fieldmetainfo" VALUES(DEFAULT,'_blockBeginActivityId','BLOCK_BEGIN_ACTIVITY_ID','"BlockBeginActivityId"','java.lang.String','com.gridnode.pdip.base.gwfbase.xpdl.model.XpdlTransitionRestriction','BLOCK_BEGIN_ACTIVITY_ID',50,0,1,1,1,'',6,'displayable=false','');
INSERT INTO "fieldmetainfo" VALUES(DEFAULT,'_blockEndActivityId','BLOCK_END_ACTIVITY_ID','"BlockEndActivityId"','java.lang.String','com.gridnode.pdip.base.gwfbase.xpdl.model.XpdlTransitionRestriction','BLOCK_END_ACTIVITY_ID',50,0,1,1,1,'',7,'displayable=false','');
INSERT INTO "fieldmetainfo" VALUES(DEFAULT,'_joinType','JOIN_TYPE','"JoinType"','java.lang.String','com.gridnode.pdip.base.gwfbase.xpdl.model.XpdlTransitionRestriction','JOIN_TYPE',50,0,1,1,1,'',8,'displayable=false','');
INSERT INTO "fieldmetainfo" VALUES(DEFAULT,'_splitType','SPLIT_TYPE','"SplitType"','java.lang.String','com.gridnode.pdip.base.gwfbase.xpdl.model.XpdlTransitionRestriction','SPLIT_TYPE',50,0,1,1,1,'',9,'displayable=false','');
INSERT INTO "fieldmetainfo" VALUES(DEFAULT,'_transitionRefListUId','TRANSITION_REF_LIST_UID','"TransitionRefListUId"','java.lang.Long','com.gridnode.pdip.base.gwfbase.xpdl.model.XpdlTransitionRestriction','TRANSITION_REF_LIST_UID',0,0,0,0,0,'',10,'displayable=false','');
INSERT INTO "fieldmetainfo" VALUES(DEFAULT,'_listUId','LIST_UID','"ListUId"','java.lang.Long','com.gridnode.pdip.base.gwfbase.xpdl.model.XpdlTransitionRestriction','LIST_UID',0,0,0,0,0,'',11,'displayable=false','');

--------- XpdlTypeDeclaration
DELETE FROM "fieldmetainfo" WHERE "EntityObjectName" LIKE '%XpdlTypeDeclaration';
INSERT INTO "fieldmetainfo" VALUES(DEFAULT,'_uId','UID','"UID"','java.lang.Long','com.gridnode.pdip.base.gwfbase.xpdl.model.XpdlTypeDeclaration','',0,0,0,0,0,'',999,'displayable=false','');
INSERT INTO "fieldmetainfo" VALUES(DEFAULT,'_typeId','TYPE_ID','"TypeId"','java.lang.String','com.gridnode.pdip.base.gwfbase.xpdl.model.XpdlTypeDeclaration','TYPE_ID',50,0,1,1,1,'',1,'displayable=false','');
INSERT INTO "fieldmetainfo" VALUES(DEFAULT,'_typeName','TYPE_NAME','"TypeName"','java.lang.String','com.gridnode.pdip.base.gwfbase.xpdl.model.XpdlTypeDeclaration','TYPE_NAME',50,0,1,1,1,'',2,'displayable=false','');
INSERT INTO "fieldmetainfo" VALUES(DEFAULT,'_typeDescription','TYPE_DESCRIPTION','"TypeDescription"','java.lang.String','com.gridnode.pdip.base.gwfbase.xpdl.model.XpdlTypeDeclaration','TYPE_DESCRIPTION',50,0,1,1,1,'',3,'displayable=false','');
INSERT INTO "fieldmetainfo" VALUES(DEFAULT,'_extendedAttributes','EXTENDED_ATTRIBUTES','"ExtendedAttributes"','java.lang.String','com.gridnode.pdip.base.gwfbase.xpdl.model.XpdlTypeDeclaration','EXTENDED_ATTRIBUTE_LIST_UID',0,0,0,0,0,'',4,'displayable=false','');
INSERT INTO "fieldmetainfo" VALUES(DEFAULT,'_packageId','PACKAGE_ID','"PackageId"','java.lang.String','com.gridnode.pdip.base.gwfbase.xpdl.model.XpdlTypeDeclaration','PACKAGE_ID',50,0,1,1,1,'',5,'displayable=false','');
INSERT INTO "fieldmetainfo" VALUES(DEFAULT,'_pkgVersionId','PKG_VERSION_ID','"PkgVersionId"','java.lang.String','com.gridnode.pdip.base.gwfbase.xpdl.model.XpdlTypeDeclaration','PKG_VERSION_ID',50,0,1,1,1,'',6,'displayable=false','');
INSERT INTO "fieldmetainfo" VALUES(DEFAULT,'_dataTypeName','DATATYPE_NAME','"DataTypeName"','java.lang.String','com.gridnode.pdip.base.gwfbase.xpdl.model.XpdlTypeDeclaration','DATATYPE_NAME',50,0,1,1,1,'',1,'displayable=false','');
INSERT INTO "fieldmetainfo" VALUES(DEFAULT,'_complexDataTypeUId','COMPLEX_DATATYPE_UID','"ComplexDataTypeUId"','java.lang.Long','com.gridnode.pdip.base.gwfbase.xpdl.model.XpdlTypeDeclaration','COMPLEX_DATATYPE_UID',0,0,0,0,0,'',4,'displayable=false','');

--------- XpdlComplexDataType
DELETE FROM "fieldmetainfo" WHERE "EntityObjectName" LIKE '%XpdlComplexDataType';
INSERT INTO "fieldmetainfo" VALUES(DEFAULT,'_uId','UID','"UID"','java.lang.Long','com.gridnode.pdip.base.gwfbase.xpdl.model.XpdlComplexDataType','',0,0,0,0,0,'',999,'displayable=false','');
INSERT INTO "fieldmetainfo" VALUES(DEFAULT,'_dataTypeName','DATATYPE_NAME','"DataTypeName"','java.lang.String','com.gridnode.pdip.base.gwfbase.xpdl.model.XpdlComplexDataType','DATATYPE_NAME',50,0,1,1,1,'',1,'displayable=false','');
INSERT INTO "fieldmetainfo" VALUES(DEFAULT,'_complexDataTypeUId','COMPLEX_DATATYPE_UID','"ComplexDataTypeUId"','java.lang.Long','com.gridnode.pdip.base.gwfbase.xpdl.model.XpdlComplexDataType','COMPLEX_DATATYPE_UID',0,0,0,0,0,'',4,'displayable=false','');
INSERT INTO "fieldmetainfo" VALUES(DEFAULT,'_subTypeUId','SUBTYPE_UID','"SubTypeUId"','java.lang.Long','com.gridnode.pdip.base.gwfbase.xpdl.model.XpdlComplexDataType','SUBTYPE_UID',0,0,0,0,0,'',4,'displayable=false','');
INSERT INTO "fieldmetainfo" VALUES(DEFAULT,'_arrayLowerIndex','ARRAY_LOWERINDEX','"ArrayLowerIndex"','java.lang.Integer','com.gridnode.pdip.base.gwfbase.xpdl.model.XpdlComplexDataType','ARRAY_LOWERINDEX',0,0,0,0,0,'',4,'displayable=false','');
INSERT INTO "fieldmetainfo" VALUES(DEFAULT,'_arrayUpperIndex','ARRAY_UPPERINDEX','"ArrayUpperIndex"','java.lang.Integer','com.gridnode.pdip.base.gwfbase.xpdl.model.XpdlComplexDataType','ARRAY_UPPERINDEX',0,0,0,0,0,'',4,'displayable=false','');

--------- StringData
DELETE FROM "fieldmetainfo" WHERE "EntityObjectName" LIKE '%StringData';
INSERT INTO "fieldmetainfo" VALUES(DEFAULT,'_uId','UID','"UID"','java.lang.Long','com.gridnode.pdip.base.contextdata.entities.model.StringData','',0,0,0,0,0,'',999,'displayable=false','');
INSERT INTO "fieldmetainfo" VALUES(DEFAULT,'_data','DATA','"Data"','java.lang.String','com.gridnode.pdip.base.contextdata.entities.model.StringData','DATA',50,0,1,1,1,'',1,'displayable=false','');
INSERT INTO "fieldmetainfo" VALUES(DEFAULT,'_dataType','DATA_TYPE','"DataType"','java.lang.String','com.gridnode.pdip.base.contextdata.entities.model.StringData','DATA_TYPE',50,0,1,1,1,'',2,'displayable=false','');

--------- ByteData
DELETE FROM "fieldmetainfo" WHERE "EntityObjectName" LIKE '%ByteData';
INSERT INTO "fieldmetainfo" VALUES(DEFAULT,'_uId','UID','"UID"','java.lang.Long','com.gridnode.pdip.base.contextdata.entities.model.ByteData','',0,0,0,0,0,'',999,'displayable=false','');
INSERT INTO "fieldmetainfo" VALUES(DEFAULT,'_data','DATA','"Data"','byte[]','com.gridnode.pdip.base.contextdata.entities.model.ByteData','DATA',50,0,1,1,1,'',1,'displayable=false','');
INSERT INTO "fieldmetainfo" VALUES(DEFAULT,'_dataType','DATA_TYPE','"DataType"','java.lang.String','com.gridnode.pdip.base.contextdata.entities.model.ByteData','DATA_TYPE',50,0,1,1,1,'',2,'displayable=false','');

--------- ContextData
DELETE FROM "fieldmetainfo" WHERE "EntityObjectName" LIKE '%ContextData';
INSERT INTO "fieldmetainfo" VALUES(DEFAULT,'_uId','UID','"UID"','java.lang.Long','com.gridnode.pdip.base.contextdata.entities.model.ContextData','',0,0,0,0,0,'',999,'displayable=false','');
INSERT INTO "fieldmetainfo" VALUES(DEFAULT,'_contextUId','CONTEXT_UID','"ContextUId"','java.lang.Long','com.gridnode.pdip.base.contextdata.entities.model.ContextData','',0,0,0,0,0,'',999,'displayable=false','');
INSERT INTO "fieldmetainfo" VALUES(DEFAULT,'_contextData','CONTEXT_DATA','"ContextData"','byte[]','com.gridnode.pdip.base.contextdata.entities.model.ContextData','',0,0,0,0,0,'',999,'displayable=false','');

--------- Certificate
DELETE FROM "fieldmetainfo" WHERE "EntityObjectName" LIKE '%Certificate';
INSERT INTO "fieldmetainfo" VALUES (DEFAULT,'_uId','UID','"UID"','java.lang.Long','com.gridnode.pdip.base.certificate.model.Certificate','certificate.uid',20,0,0,0,0,'0',999,'displayable=false'||chr(13)||chr(10)||'editable=false'||chr(13)||chr(10)||'mandatory=false'||chr(13)||chr(10),'type=uid'||chr(13)||chr(10));
INSERT INTO "fieldmetainfo" VALUES (DEFAULT,'_privateKey','PRIVATEKEY','"PrivateKey"','java.lang.String','com.gridnode.pdip.base.certificate.model.Certificate','certificate.privateKey',0,0,0,0,0,'0',999,'displayable=false'||chr(13)||chr(10)||'editable=false'||chr(13)||chr(10)||'mandatory=false'||chr(13)||chr(10),'type=text'||chr(13)||chr(10));
INSERT INTO "fieldmetainfo" VALUES (DEFAULT,'_publicKey','PUBLICKEY','"PublicKey"','java.lang.String','com.gridnode.pdip.base.certificate.model.Certificate','certificate.publicKey',0,0,0,0,0,'0',999,'displayable=false'||chr(13)||chr(10)||'editable=false'||chr(13)||chr(10)||'mandatory=false'||chr(13)||chr(10),'type=text'||chr(13)||chr(10));
INSERT INTO "fieldmetainfo" VALUES (DEFAULT,'_certificate','CERTIFICATE','"Certificate"','java.lang.String','com.gridnode.pdip.base.certificate.model.Certificate','certificate.certificate',0,0,0,0,0,'0',999,'displayable=false'||chr(13)||chr(10)||'editable=false'||chr(13)||chr(10)||'mandatory=false'||chr(13)||chr(10),'type=text'||chr(13)||chr(10));
INSERT INTO "fieldmetainfo" VALUES (DEFAULT,'_issuerName','ISSUERNAME','"IssuerName"','java.lang.String','com.gridnode.pdip.base.certificate.model.Certificate','certificate.issuerName',120,0,0,0,0,'0',999,'displayable=false'||chr(13)||chr(10)||'editable=false'||chr(13)||chr(10)||'mandatory=false'||chr(13)||chr(10),'type=text'||chr(13)||chr(10));
INSERT INTO "fieldmetainfo" VALUES (DEFAULT,'_serialNum','SERIALNUM','"SerialNum"','java.lang.String','com.gridnode.pdip.base.certificate.model.Certificate','certificate.serialNum',30,0,0,0,0,'0',999,'displayable=true'||chr(13)||chr(10)||'editable=false'||chr(13)||chr(10)||'mandatory=false'||chr(13)||chr(10),'type=text'||chr(13)||chr(10));
INSERT INTO "fieldmetainfo" VALUES (DEFAULT,'_name','NAME','"Name"','java.lang.String','com.gridnode.pdip.base.certificate.model.Certificate','certificate.name',50,0,0,0,0,'0',999,'displayable=true'||chr(13)||chr(10)||'editable=true'||chr(13)||chr(10)||'mandatory=true'||chr(13)||chr(10),'type=text'||chr(13)||chr(10)||'text.length.max=50'||chr(13)||chr(10));
INSERT INTO "fieldmetainfo" VALUES (DEFAULT,'_id','ID','"ID"','java.lang.Integer','com.gridnode.pdip.base.certificate.model.Certificate','certificate.id',20,0,0,0,0,'0',999,'displayable=true'||chr(13)||chr(10)||'editable=false'||chr(13)||chr(10)||'mandatory=false'||chr(13)||chr(10),'type=range'||chr(13)||chr(10));
INSERT INTO "fieldmetainfo" VALUES (DEFAULT,'_revokeID','REVOKEID','"RevokeID"','java.lang.Integer','com.gridnode.pdip.base.certificate.model.Certificate','certificate.revokeId',11,0,0,0,0,'0',999,'displayable=false'||chr(13)||chr(10)||'editable=false'||chr(13)||chr(10)||'mandatory=false'||chr(13)||chr(10),'type=range'||chr(13)||chr(10));
INSERT INTO "fieldmetainfo" VALUES (DEFAULT,'_isMaster','IS_MASTER','"isMaster"','java.lang.Boolean','com.gridnode.pdip.base.certificate.model.Certificate','certificate.isMaster',0,0,0,0,0,'0',999,'displayable=false'||chr(13)||chr(10)||'mandatory=false'||chr(13)||chr(10)||'editable=false'||chr(13)||chr(10),'type=enum'||chr(13)||chr(10)||'generic.yes=true'||chr(13)||chr(10)||'generic.no=false'||chr(13)||chr(10));
INSERT INTO "fieldmetainfo" VALUES (DEFAULT,'_isPartner','IS_PARTNER','"isPartner"','java.lang.Boolean','com.gridnode.pdip.base.certificate.model.Certificate','certificate.isPartner',0,0,0,0,0,'0',999,'displayable=true'||chr(13)||chr(10)||'mandatory=true'||chr(13)||chr(10)||'editable=false'||chr(13)||chr(10)||'editable.create=true','type=enum'||chr(13)||chr(10)||'generic.yes=true'||chr(13)||chr(10)||'generic.no=false'||chr(13)||chr(10));
INSERT INTO "fieldmetainfo" VALUES(DEFAULT,'_category','CATEGORY','"Category"','java.lang.Short','com.gridnode.pdip.base.certificate.model.Certificate','certificate.category',80,0,0,1,1,'0',999,'displayable=true'||chr(13)||chr(10)||'editable=false'||chr(13)||chr(10)||'mandatory=false'||chr(13)||chr(10)||'displayable.create=false'||chr(13)||chr(10),'type=enum');
INSERT INTO "fieldmetainfo" VALUES (DEFAULT,'_isInKeyStore','IS_IN_KS','"iSINKS"','java.lang.Boolean','com.gridnode.pdip.base.certificate.model.Certificate','certificate.inKeyStore',0,0,0,0,0,'0',999,'displayable=true'||chr(13)||chr(10)||'displayable.create=false'||chr(13)||chr(10)||'mandatory=true'||chr(13)||chr(10)||'editable=false'||chr(13)||chr(10),'type=enum'||chr(13)||chr(10)||'generic.yes=true'||chr(13)||chr(10)||'generic.no=false'||chr(13)||chr(10));
INSERT INTO "fieldmetainfo" VALUES (DEFAULT,'_isInTrustStore','IS_IN_TS','"iSINTS"','java.lang.Boolean','com.gridnode.pdip.base.certificate.model.Certificate','certificate.inTrustStore',0,0,0,0,0,'0',999,'displayable=true'||chr(13)||chr(10)||'displayable.create=false'||chr(13)||chr(10)||'mandatory=true'||chr(13)||chr(10)||'editable=false'||chr(13)||chr(10),'type=enum'||chr(13)||chr(10)||'generic.yes=true'||chr(13)||chr(10)||'generic.no=false'||chr(13)||chr(10));
INSERT INTO "fieldmetainfo" VALUES (DEFAULT,'_relatedCertUid','RELATED_CERT_UID','"relatedCertUid"','java.lang.Long','com.gridnode.pdip.base.certificate.model.Certificate','certificate.relatedCertUid',0,0,0,1,1,'0',999,'displayable=true'||chr(13)||chr(10)||'editable=true'||chr(13)||chr(10)||'mandatory=false'||chr(13)||chr(10),'type=foreign'||chr(13)||chr(10)||'foreign.key=certificate.uid'||chr(13)||chr(10)||'foreign.display=certificate.name'||chr(13)||chr(10)||'foreign.cached=false');
INSERT INTO "fieldmetainfo" VALUES(DEFAULT,'_startDate','START_DATE','"StartDate"','java.util.Date','com.gridnode.pdip.base.certificate.model.Certificate','certificate.startDate',0,0,0,0,1,'',999,'displayable=true'||chr(13)||chr(10)||'mandatory=false'||chr(13)||chr(10)||'editable=false','type=datetime'||chr(13)||chr(10)||'datetime.time=true'||chr(13)||chr(10)||'datetime.date=true'||chr(13)||chr(10)||'datetime.adjustment=gts');
INSERT INTO "fieldmetainfo" VALUES(DEFAULT,'_endDate','END_DATE','"EndDate"','java.util.Date','com.gridnode.pdip.base.certificate.model.Certificate','certificate.endDate',0,0,0,0,1,'',999,'displayable=true'||chr(13)||chr(10)||'mandatory=false'||chr(13)||chr(10)||'editable=false','type=datetime'||chr(13)||chr(10)||'datetime.time=true'||chr(13)||chr(10)||'datetime.date=true'||chr(13)||chr(10)||'datetime.adjustment=gts');
INSERT INTO "fieldmetainfo" VALUES(DEFAULT,'_isCA','IS_CA','"IsCA"','java.lang.Boolean','com.gridnode.pdip.base.certificate.model.Certificate','certificate.isCA',0,0,0,0,0,'0',999,'displayable=true'||chr(13)||chr(10)||'mandatory=false'||chr(13)||chr(10)||'editable=true'||chr(13)||chr(10),'type=enum'||chr(13)||chr(10)||'generic.yes=true'||chr(13)||chr(10)||'generic.no=false'||chr(13)||chr(10));
INSERT INTO "fieldmetainfo" VALUES(DEFAULT,'_replacementCertUid','REPLACEMENT_CERT_UID','"ReplacementCertUid"','java.lang.Long','com.gridnode.pdip.base.certificate.model.Certificate','certificate.replacementCertUid',0,0,0,1,1,'0',999,'displayable=true'||chr(13)||chr(10)||'editable=false'||chr(13)||chr(10)||'mandatory=false'||chr(13)||chr(10),'type=foreign'||chr(13)||chr(10)||'foreign.key=certificate.uid'||chr(13)||chr(10)||'foreign.display=certificate.name'||chr(13)||chr(10)||'foreign.cached=false');

--------- Role
DELETE FROM "fieldmetainfo" WHERE "EntityObjectName" LIKE '%Role';
INSERT INTO "fieldmetainfo" VALUES(DEFAULT,'_uId','UID','"UID"','java.lang.Long','com.gridnode.pdip.base.acl.model.Role','role.uid',20,0,0,0,0,'0',999,'displayable=false'||chr(13)||chr(10)||'editable=false'||chr(13)||chr(10)||'mandatory=false'||chr(13)||chr(10),'type=uid'||chr(13)||chr(10));
INSERT INTO "fieldmetainfo" VALUES(DEFAULT,'_role','ROLE','"Role"','java.lang.String','com.gridnode.pdip.base.acl.model.Role','role.role',30,0,1,1,1,'0',1,'displayable=true'||chr(13)||chr(10)||'editable=false'||chr(13)||chr(10)||'editable.create=true'||chr(13)||chr(10)||'mandatory=true'||chr(13)||chr(10),'type=text'||chr(13)||chr(10)||'text.length.min=2'||chr(13)||chr(10)||'text.length.max=30'||chr(13)||chr(10));
INSERT INTO "fieldmetainfo" VALUES(DEFAULT,'_description','DESCRIPTION','"Description"','java.lang.String','com.gridnode.pdip.base.acl.model.Role','role.description',255,0,1,1,1,'0',1,'displayable=true'||chr(13)||chr(10)||'editable=true'||chr(13)||chr(10)||'mandatory=true'||chr(13)||chr(10),'type=text'||chr(13)||chr(10)||'text.length.min=1'||chr(13)||chr(10)||'text.length.max=255'||chr(13)||chr(10));
INSERT INTO "fieldmetainfo" VALUES(DEFAULT,'_canDelete','CAN_DELETE','"CanDelete"','java.lang.Boolean','com.gridnode.pdip.base.acl.model.Role','role.canDelete',0,0,1,1,1,'0',999,'displayable=true'||chr(13)||chr(10)||'editable=false'||chr(13)||chr(10)||'mandatory=false'||chr(13)||chr(10),'type=enum'||chr(13)||chr(10)||'generic.yes=true'||chr(13)||chr(10)||'generic.no=false'||chr(13)||chr(10));
INSERT INTO "fieldmetainfo" VALUES(DEFAULT,'_version','VERSION','"Version"','java.lang.Double','com.gridnode.pdip.base.acl.model.Role','role.version',0,0,1,1,1,'0',999,'displayable=false'||chr(13)||chr(10)||'editable=false'||chr(13)||chr(10)||'mandatory=false'||chr(13)||chr(10),'type=range'||chr(13)||chr(10));

--------- Subject Role, NOTE: must be after Role.
DELETE FROM "fieldmetainfo" WHERE "EntityObjectName" LIKE '%SubjectRole';
INSERT INTO "fieldmetainfo" VALUES(DEFAULT,'_version','VERSION','"Version"','java.lang.Double','com.gridnode.pdip.base.acl.model.SubjectRole','',0,0,1,1,1,'0',999,'','');
INSERT INTO "fieldmetainfo" VALUES(DEFAULT,'_subjectType','SUBJECT_TYPE','"SubjectType"','java.lang.String','com.gridnode.pdip.base.acl.model.SubjectRole','',30,0,1,1,1,'0',1,'','');
INSERT INTO "fieldmetainfo" VALUES(DEFAULT,'_role','ROLE','"Role"','java.lang.Long','com.gridnode.pdip.base.acl.model.SubjectRole','',0,0,1,1,1,'0',999,'','');
INSERT INTO "fieldmetainfo" VALUES(DEFAULT,'_subject','SUBJECT','"Subject"','java.lang.Long','com.gridnode.pdip.base.acl.model.SubjectRole','',0,0,1,1,1,'0',999,'','');
INSERT INTO "fieldmetainfo" VALUES(DEFAULT,'_uId','UID','"UID"','java.lang.Long','com.gridnode.pdip.base.acl.model.SubjectRole','',0,0,0,1,0,'0',999,'','');

--------- ACCESS RIGHT
DELETE FROM "fieldmetainfo" WHERE "EntityObjectName" LIKE '%AccessRight';
INSERT INTO "fieldmetainfo" VALUES(DEFAULT,'_uId','UID','"UID"','java.lang.Long','com.gridnode.pdip.base.acl.model.AccessRight','accessRight.uid',20,0,0,0,0,'0',999,'displayable=false'||chr(13)||chr(10)||'editable=false'||chr(13)||chr(10)||'mandatory=false'||chr(13)||chr(10),'type=uid'||chr(13)||chr(10));
INSERT INTO "fieldmetainfo" VALUES(DEFAULT,'_roleUID','ROLE','"RoleUID"','java.lang.Long','com.gridnode.pdip.base.acl.model.AccessRight','accessRight.role',20,0,1,1,1,'0',2,'displayable=true'||chr(13)||chr(10)||'editable=false'||chr(13)||chr(10)||'editable.create=true'||chr(13)||chr(10)||'mandatory=true'||chr(13)||chr(10),'type=foreign'||chr(13)||chr(10)||'foreign.key=role.uid'||chr(13)||chr(10)||'foreign.display=role.description'||chr(13)||chr(10)||'foreign.cached=false'||chr(13)||chr(10));
INSERT INTO "fieldmetainfo" VALUES(DEFAULT,'_feature','FEATURE','"Feature"','java.lang.String','com.gridnode.pdip.base.acl.model.AccessRight','accessRight.feature',0,0,1,1,1,'0',3,'displayable=true'||chr(13)||chr(10)||'editable=true'||chr(13)||chr(10)||'mandatory=true'||chr(13)||chr(10),'type=foreign'||chr(13)||chr(10)||'foreign.key=feature.feature'||chr(13)||chr(10)||'foreign.display=feature.description'||chr(13)||chr(10)||'foreign.cached=false'||chr(13)||chr(10));
INSERT INTO "fieldmetainfo" VALUES(DEFAULT,'_descr','DESCRIPTION','"Description"','java.lang.String','com.gridnode.pdip.base.acl.model.AccessRight','accessRight.description',80,0,1,1,1,'0',1,'displayable=true'||chr(13)||chr(10)||'editable=true'||chr(13)||chr(10)||'mandatory=true'||chr(13)||chr(10),'type=text'||chr(13)||chr(10)||'text.length.max=80'||chr(13)||chr(10)||'text.length.min=1'||chr(13)||chr(10));
INSERT INTO "fieldmetainfo" VALUES(DEFAULT,'_action','ACTION','"Action"','java.lang.String','com.gridnode.pdip.base.acl.model.AccessRight','accessRight.actionName',30,0,1,1,1,'0',4,'displayable=true'||chr(13)||chr(10)||'editable=true'||chr(13)||chr(10)||'mandatory=true'||chr(13)||chr(10),'type=text'||chr(13)||chr(10));
INSERT INTO "fieldmetainfo" VALUES(DEFAULT,'_dataType','DATA_TYPE','"DataType"','java.lang.String','com.gridnode.pdip.base.acl.model.AccessRight','accessRight.dataType',30,0,0,1,1,'0',5,'displayable=true'||chr(13)||chr(10)||'editable=true'||chr(13)||chr(10)||'mandatory=false'||chr(13)||chr(10),'type=text'||chr(13)||chr(10));
INSERT INTO "fieldmetainfo" VALUES(DEFAULT,'_criteria','CRITERIA','"Criteria"','com.gridnode.pdip.framework.db.filter.IDataFilter','com.gridnode.pdip.base.acl.model.AccessRight','accessRight.criteria',0,0,0,1,1,'0',6,'displayable=true'||chr(13)||chr(10)||'editable=true'||chr(13)||chr(10)||'mandatory=false'||chr(13)||chr(10),'type=other'||chr(13)||chr(10));
INSERT INTO "fieldmetainfo" VALUES(DEFAULT,'_canDelete','CAN_DELETE','"CanDelete"','java.lang.Boolean','com.gridnode.pdip.base.acl.model.AccessRight','accessRight.canDelete',0,0,0,0,0,'0',999,'displayable=true'||chr(13)||chr(10)||'mandatory=false'||chr(13)||chr(10)||'editable=false'||chr(13)||chr(10),'type=enum'||chr(13)||chr(10)||'generic.yes=true'||chr(13)||chr(10)||'generic.no=false'||chr(13)||chr(10));
INSERT INTO "fieldmetainfo" VALUES(DEFAULT,'_version','VERSION','"Version"','java.lang.Double','com.gridnode.pdip.base.acl.model.AccessRight','accessRight.version',0,0,0,0,0,'0',999,'displayable=false'||chr(13)||chr(10)||'editable=false'||chr(13)||chr(10)||'mandatory=false'||chr(13)||chr(10),'type=range'||chr(13)||chr(10));

--------- Feature
DELETE FROM "fieldmetainfo" WHERE "EntityObjectName" LIKE '%Feature';
INSERT INTO "fieldmetainfo" VALUES(DEFAULT,'_version','VERSION','"Version"','java.lang.Double','com.gridnode.pdip.base.acl.model.Feature','feature.version',0,0,1,1,1,'0',999,'displayable=false'||chr(13)||chr(10)||'mandatory=false'||chr(13)||chr(10)||'editable=false'||chr(13)||chr(10),'type=range'||chr(13)||chr(10));
INSERT INTO "fieldmetainfo" VALUES(DEFAULT,'_uId','UID','"UID"','java.lang.Long','com.gridnode.pdip.base.acl.model.Feature','feature.uid',20,0,0,0,0,'0',999,'displayable=false'||chr(13)||chr(10)||'mandatory=false'||chr(13)||chr(10)||'editable=false'||chr(13)||chr(10),'type=uid'||chr(13)||chr(10));
INSERT INTO "fieldmetainfo" VALUES(DEFAULT,'_feature','FEATURE','"Feature"','java.lang.String','com.gridnode.pdip.base.acl.model.Feature','feature.feature',30,0,0,1,1,'0',999,'displayable=true'||chr(13)||chr(10)||'editable=false'||chr(13)||chr(10)||'mandatory=false'||chr(13)||chr(10),'type=text'||chr(13)||chr(10));
INSERT INTO "fieldmetainfo" VALUES(DEFAULT,'_description','DESCRIPTION','"Description"','java.lang.String','com.gridnode.pdip.base.acl.model.Feature','feature.description',80,0,0,1,1,'0',999,'displayable=true'||chr(13)||chr(10)||'editable=false'||chr(13)||chr(10)||'mandatory=false'||chr(13)||chr(10),'type=text'||chr(13)||chr(10));
INSERT INTO "fieldmetainfo" VALUES(DEFAULT,'_actions','ACTIONS','"Actions"','java.util.List','com.gridnode.pdip.base.acl.model.Feature','feature.actions',1024,0,0,1,1,'0',999,'displayable=true'||chr(13)||chr(10)||'editable=false'||chr(13)||chr(10)||'mandatory=false'||chr(13)||chr(10),'type=text'||chr(13)||chr(10)||'collection=true'||chr(13)||chr(10)||'collection.element=java.lang.String'||chr(13)||chr(10));
INSERT INTO "fieldmetainfo" VALUES(DEFAULT,'_dataTypes','DATA_TYPES','"DataTypes"','java.util.List','com.gridnode.pdip.base.acl.model.Feature','feature.dataTypes',1024,0,0,1,1,'0',999,'displayable=true'||chr(13)||chr(10)||'editable=false'||chr(13)||chr(10)||'mandatory=false'||chr(13)||chr(10),'type=text'||chr(13)||chr(10)||'collection=true'||chr(13)||chr(10)||'collection.element=java.lang.String'||chr(13)||chr(10));
COMMIT;

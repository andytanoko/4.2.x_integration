-- -----------------------------------------------------------------
-- This script Creates default data for some of the tables in APPDB
-- -----------------------------------------------------------------


CONNECT APPDB/gridnode;

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

DELETE FROM "entitymetainfo" WHERE "EntityName" IN ('CertificateSwapping');
INSERT INTO "entitymetainfo" VALUES ('com.gridnode.pdip.base.certificate.model.CertificateSwapping','CertificateSwapping','');

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
INSERT INTO "fieldmetainfo" VALUES (NULL,'_uId','UID','"UID"','java.lang.Long','com.gridnode.pdip.base.worklist.entities.model.GWFWorkListValueEntity','',0,0,0,1,1,'',999,'displayable=false','');
INSERT INTO "fieldmetainfo" VALUES (NULL,'iCal_wi_description','ICAL_WI_DESCRIPTION','"wi_description"','java.lang.String','com.gridnode.pdip.base.worklist.entities.model.GWFWorkListValueEntity','Description',30,0,1,1,1,'desc',2,'displayable=false','');
INSERT INTO "fieldmetainfo" VALUES (NULL,'iCal_comments','ICAL_COMMENTS','"wi_comments"','java.lang.String','com.gridnode.pdip.base.worklist.entities.model.GWFWorkListValueEntity','Document_type',30,0,1,0,1,'dcoType',1,'displayable=false','');
INSERT INTO "fieldmetainfo" VALUES (NULL,'iCal_reqst_status','ICAL_REQST_STATUS','"wi_status"','java.lang.Boolean','com.gridnode.pdip.base.worklist.entities.model.GWFWorkListValueEntity','30',0,0,0,0,0,'uId',999,'displayable=false','');
INSERT INTO "fieldmetainfo" VALUES (NULL,'iCal_Creation_DT','ICAL_CREATION_DT','"wi_cdate"','java.util.Date','com.gridnode.pdip.base.worklist.entities.model.GWFWorkListValueEntity','30',0,0,0,0,0,'canDelete',999,'displayable=false','');
INSERT INTO "fieldmetainfo" VALUES (NULL,'user_id','USER_ID','"user_id"','java.lang.String','com.gridnode.pdip.base.worklist.entities.model.GWFWorkListValueEntity','30',10,0,0,0,0,'',999,'displayable=false','');
INSERT INTO "fieldmetainfo" VALUES (NULL,'unassigned','UNASSIGNED','"unassigned"','java.lang.String','com.gridnode.pdip.base.worklist.entities.model.GWFWorkListValueEntity','30',10,0,0,0,0,'desc',999,'displayable=false','');
INSERT INTO "fieldmetainfo" VALUES (NULL,'processDefKey','PROCESSDEF_KEY','"processDefKey"','java.lang.String','com.gridnode.pdip.base.worklist.entities.model.GWFWorkListValueEntity','80',10,0,0,0,0,'',999,'displayable=false','');
INSERT INTO "fieldmetainfo" VALUES (NULL,'activityId','ACTIVITY_ID','"activityId"','java.lang.String','com.gridnode.pdip.base.worklist.entities.model.GWFWorkListValueEntity','30',10,0,0,0,0,'',999,'displayable=false','');
INSERT INTO "fieldmetainfo" VALUES (NULL,'performer','PERFORMER','"performer"','java.lang.String','com.gridnode.pdip.base.worklist.entities.model.GWFWorkListValueEntity','30',10,0,0,0,0,'',999,'displayable=false','');
INSERT INTO "fieldmetainfo" VALUES (NULL,'rtActivityUId','RTACTIVITY_UID','"rtActivityUId"','java.lang.Long','com.gridnode.pdip.base.worklist.entities.model.GWFWorkListValueEntity','',0,0,0,1,1,'',999,'displayable=false','');
INSERT INTO "fieldmetainfo" VALUES (NULL,'contextUId','CONTEXT_UID','"contextUId"','java.lang.Long','com.gridnode.pdip.base.worklist.entities.model.GWFWorkListValueEntity','',0,0,0,1,1,'',999,'displayable=false','');

--------- GWFWorkListUserEntity
DELETE FROM "fieldmetainfo" WHERE "EntityObjectName" LIKE '%GWFWorkListUserEntity';
INSERT INTO "fieldmetainfo" VALUES (NULL,'_uId','UID','"UID"','java.lang.Long','com.gridnode.pdip.base.worklist.entities.model.GWFWorkListUserEntity',NULL,0,0,0,1,1,'desc',999,'displayable=false','');
INSERT INTO "fieldmetainfo" VALUES (NULL,'workitem_id','WORKITEM_ID','"workitem_id"','java.lang.Integer','com.gridnode.pdip.base.worklist.entities.model.GWFWorkListUserEntity','Document_type',30,0,1,0,1,'dcoType',1,'displayable=false','');
INSERT INTO "fieldmetainfo" VALUES (NULL,'user_id','USER_ID','"user_id"','java.lang.String','com.gridnode.pdip.base.worklist.entities.model.GWFWorkListUserEntity','Document_type',30,0,1,0,1,'dcoType',1,'displayable=false','');

--------- UserProcedure.
DELETE FROM "fieldmetainfo" WHERE "EntityObjectName" LIKE '%UserProcedure';
INSERT INTO "fieldmetainfo" VALUES (NULL,'_uId','UID','"UID"','java.lang.Long','com.gridnode.pdip.base.userprocedure.model.UserProcedure','userProcedure.uid',20,0,0,0,0,'0',999,'displayable=false'||chr(13)||chr(10)||'mandatory=false'||chr(13)||chr(10)||'editable=false'||chr(13)||chr(10),'type=uid'||chr(13)||chr(10));
INSERT INTO "fieldmetainfo" VALUES(NULL,'_version','VERSION','"Version"','java.lang.Double','com.gridnode.pdip.base.userprocedure.model.UserProcedure','',0,0,0,0,0,'',999,'displayable=false'||chr(13)||chr(10)||'mandatory=false'||chr(13)||chr(10)||'editable=false','type=range');
INSERT INTO "fieldmetainfo" VALUES(NULL,'_canDelete','CAN_DELETE','"CanDelete"','java.lang.Boolean','com.gridnode.pdip.base.userprocedure.model.UserProcedure','',0,0,0,0,0,'',999,'displayable=false'||chr(13)||chr(10)||'mandatory=false'||chr(13)||chr(10)||'editable=false','type=enum'||chr(13)||chr(10)||'candelete.enabled=true'||chr(13)||chr(10)||'candelete.disabled=false');
INSERT INTO "fieldmetainfo" VALUES (NULL,'_name','NAME','"Name"','java.lang.String','com.gridnode.pdip.base.userprocedure.model.UserProcedure','userProcedure.name',15,0,0,0,1,'0',1,'displayable=true'||chr(13)||chr(10)||'editable.create=true'||chr(13)||chr(10)||'mandatory=true'||chr(13)||chr(10),'type=text'||chr(13)||chr(10)||'text.length.max=30'||chr(13)||chr(10));
INSERT INTO "fieldmetainfo" VALUES (NULL,'_description','DESCRIPTION','"Description"','java.lang.String','com.gridnode.pdip.base.userprocedure.model.UserProcedure','userProcedure.description',15,0,0,0,1,'0',1,'displayable=true'||chr(13)||chr(10)||'editable=true'||chr(13)||chr(10)||'mandatory=true'||chr(13)||chr(10),'type=text'||chr(13)||chr(10)||'text.length.max=80'||chr(13)||chr(10));
INSERT INTO "fieldmetainfo" VALUES (NULL,'_isSynchronous','IS_SYNCHRONOUS','"IsSynchronous"','java.lang.Boolean','com.gridnode.pdip.base.userprocedure.model.UserProcedure','userProcedure.isSynchronous',0,0,0,0,0,'0',999,'displayable=true'||chr(13)||chr(10)||'editable=true'||chr(13)||chr(10)||'mandatory=false'||chr(13)||chr(10),'type=enum'||chr(13)||chr(10)||'userProcedure.isSynchronous.yes=true'||chr(13)||chr(10)||'userProcedure.isSynchronous.no=false'||chr(13)||chr(10));
INSERT INTO "fieldmetainfo" VALUES (NULL,'_procType','PROC_TYPE','"ProcType"','java.lang.Integer','com.gridnode.pdip.base.userprocedure.model.UserProcedure','userProcedure.procType',15,0,0,0,1,'0',1,'displayable=true'||chr(13)||chr(10)||'editable=true'||chr(13)||chr(10)||'mandatory=true'||chr(13)||chr(10),'type=enum'||chr(13)||chr(10)||'userProcedure.procType.executable=1'||chr(13)||chr(10)||'userProcedure.procType.java=2'||chr(13)||chr(10)||'userProcedure.procType.soap=3'||chr(13)||chr(10));
INSERT INTO "fieldmetainfo" VALUES (NULL,'_procDefFile','PROC_DEF_FILE','"ProcDefFile"','com.gridnode.pdip.base.userprocedure.model.ProcedureDefFile','com.gridnode.pdip.base.userprocedure.model.UserProcedure','userProcedure.procDefFile',0,0,0,0,0,'0',999,'displayable=true'||chr(13)||chr(10)||'editable=true'||chr(13)||chr(10)||'mandatory=true'||chr(13)||chr(10),'type=foreign'||chr(13)||chr(10)||'foreign.key=procedureDefFile.uid'||chr(13)||chr(10)||'foreign.display=procedureDefFile.name'||chr(13)||chr(10)||'foreign.cached=true'||chr(13)||chr(10));
INSERT INTO "fieldmetainfo" VALUES (NULL,'_procDef','PROC_DEF','"ProcDef"','com.gridnode.pdip.base.userprocedure.model.ProcedureDef','com.gridnode.pdip.base.userprocedure.model.UserProcedure','userProcedure.procDef',0,0,0,0,0,'0',999,'displayable=true'||chr(13)||chr(10)||'editable=true'||chr(13)||chr(10)||'mandatory=true'||chr(13)||chr(10),'type=dynamic'||chr(13)||chr(10)||'dynamic.types=shellExecutable;javaProcedure;soapProcedure'||chr(13)||chr(10));
INSERT INTO "fieldmetainfo" VALUES (NULL,'_procParamList','PROC_PARAM_LIST','"ProcParamList"','java.util.Vector','com.gridnode.pdip.base.userprocedure.model.UserProcedure','userProcedure.procParamList',0,0,0,0,0,'0',999,'displayable=true'||chr(13)||chr(10)||'editable=true'||chr(13)||chr(10)||'mandatory=false'||chr(13)||chr(10),'type=embedded'||chr(13)||chr(10)||'embedded.type=paramDef'||chr(13)||chr(10)||'collection=true'||chr(13)||chr(10));
INSERT INTO "fieldmetainfo" VALUES (NULL,'_returnDataType','RETURN_DATA_TYPE','"ReturnDataType"','java.lang.Integer','com.gridnode.pdip.base.userprocedure.model.UserProcedure','userProcedure.returnDataType',15,0,0,0,1,'0',1,'displayable=true'||chr(13)||chr(10)||'editable=true'||chr(13)||chr(10)||'mandatory=false'||chr(13)||chr(10),'type=enum'||chr(13)||chr(10)||'userProcedure.returnDataType.string=1'||chr(13)||chr(10)||'userProcedure.returnDataType.integer=2'||chr(13)||chr(10)||'userProcedure.returnDataType.long=3'||chr(13)||chr(10)||'userProcedure.returnDataType.double=4'||chr(13)||chr(10)||'userProcedure.returnDataType.boolean=5'||chr(13)||chr(10)||'userProcedure.returnDataType.date=6'||chr(13)||chr(10)||'userProcedure.returnDataType.object=7'||chr(13)||chr(10));
INSERT INTO "fieldmetainfo" VALUES (NULL,'_defAction','DEF_ACTION','"DefAction"','java.lang.Integer','com.gridnode.pdip.base.userprocedure.model.UserProcedure','userProcedure.defAction',15,0,0,0,1,'0',1,'displayable=true'||chr(13)||chr(10)||'editable=true'||chr(13)||chr(10)||'mandatory=false'||chr(13)||chr(10),'type=enum'||chr(13)||chr(10)||'userProcedure.defAction.continue=1'||chr(13)||chr(10)||'userProcedure.defAction.abort=2'||chr(13)||chr(10));
INSERT INTO "fieldmetainfo" VALUES (NULL,'_defAlert','DEF_ALERT','"DefAlert"','java.lang.Long','com.gridnode.pdip.base.userprocedure.model.UserProcedure','userProcedure.defAlert',15,0,0,0,1,'0',1,'displayable=true'||chr(13)||chr(10)||'editable=true'||chr(13)||chr(10)||'mandatory=false'||chr(13)||chr(10),'type=foreign'||chr(13)||chr(10)||'foreign.key=alert.uid'||chr(13)||chr(10)||'foreign.display=alert.description'||chr(13)||chr(10));
INSERT INTO "fieldmetainfo" VALUES (NULL,'_procReturnList','PROC_RETURN_LIST','"ProcReturnList"','java.util.Vector','com.gridnode.pdip.base.userprocedure.model.UserProcedure','userProcedure.procReturnList',0,0,0,0,0,'0',999,'displayable=true'||chr(13)||chr(10)||'editable=true'||chr(13)||chr(10)||'mandatory=false'||chr(13)||chr(10),'type=embedded'||chr(13)||chr(10)||'embedded.type=returnDef'||chr(13)||chr(10)||'collection=true'||chr(13)||chr(10));
INSERT INTO "fieldmetainfo" VALUES (NULL,'_gridDocField','GRID_DOC_FIELD','"GridDocField"','java.lang.Integer','com.gridnode.pdip.base.userprocedure.model.UserProcedure','userProcedure.gridDocField',0,0,0,0,0,'0',999,'displayable=true'||chr(13)||chr(10)||'editable=true'||chr(13)||chr(10)||'mandatory=false'||chr(13)||chr(10),'type=enum'||chr(13)||chr(10)||'gridDocument.custom1=77'||chr(13)||chr(10)||'gridDocument.custom2=78'||chr(13)||chr(10)||'gridDocument.custom3=79'||chr(13)||chr(10)||'gridDocument.custom4=80'||chr(13)||chr(10)||'gridDocument.custom5=81'||chr(13)||chr(10)||'gridDocument.custom6=82'||chr(13)||chr(10)||'gridDocument.custom7=83'||chr(13)||chr(10)||'gridDocument.custom8=84'||chr(13)||chr(10)||'gridDocument.custom9=85'||chr(13)||chr(10)||'gridDocument.custom10=86'||chr(13)||chr(10));

--------- ProcedureDefinition File.
DELETE FROM "fieldmetainfo" WHERE "EntityObjectName" LIKE '%ProcedureDefFile';
INSERT INTO "fieldmetainfo" VALUES (NULL,'_uId','UID','"UID"','java.lang.Long','com.gridnode.pdip.base.userprocedure.model.ProcedureDefFile','procedureDefFile.uid',20,0,0,0,0,'0',999,'displayable=false'||chr(13)||chr(10)||'editable=false','type=uid');
INSERT INTO "fieldmetainfo" VALUES(NULL,'_version','VERSION','"Version"','java.lang.Double','com.gridnode.pdip.base.userprocedure.model.ProcedureDefFile','',0,0,0,0,0,'',999,'displayable=false'||chr(13)||chr(10)||'mandatory=false'||chr(13)||chr(10)||'editable=false','type=range');
INSERT INTO "fieldmetainfo" VALUES(NULL,'_canDelete','CAN_DELETE','"CanDelete"','java.lang.Boolean','com.gridnode.pdip.base.userprocedure.model.ProcedureDefFile','',0,0,0,0,0,'',999,'displayable=false'||chr(13)||chr(10)||'mandatory=false'||chr(13)||chr(10)||'editable=false','type=enum'||chr(13)||chr(10)||'candelete.enabled=true'||chr(13)||chr(10)||'candelete.disabled=false');
INSERT INTO "fieldmetainfo" VALUES (NULL,'_name','NAME','"Name"','java.lang.String','com.gridnode.pdip.base.userprocedure.model.ProcedureDefFile','procedureDefFile.name',15,0,0,0,1,'0',1,'displayable=true'||chr(13)||chr(10)||'editable.create=true'||chr(13)||chr(10)||'mandatory=true'||chr(13)||chr(10),'type=text'||chr(13)||chr(10)||'text.length.max=30');
INSERT INTO "fieldmetainfo" VALUES (NULL,'_description','DESCRIPTION','"Description"','java.lang.String','com.gridnode.pdip.base.userprocedure.model.ProcedureDefFile','procedureDefFile.description',15,0,0,0,1,'0',1,'displayable=true'||chr(13)||chr(10)||'editable=true'||chr(13)||chr(10)||'mandatory=true'||chr(13)||chr(10),'type=text'||chr(13)||chr(10)||'text.length.max=80');
INSERT INTO "fieldmetainfo" VALUES (NULL,'_type','TYPE','"Type"','java.lang.Integer','com.gridnode.pdip.base.userprocedure.model.ProcedureDefFile','procedureDefFile.type',15,0,0,0,1,'0',1,'displayable=true'||chr(13)||chr(10)||'editable=true'||chr(13)||chr(10)||'mandatory=true'||chr(13)||chr(10),'type=enum'||chr(13)||chr(10)||'procedureDefFile.type.executable=1'||chr(13)||chr(10)||'procedureDefFile.type.java=2'||chr(13)||chr(10)||'procedureDefFile.type.soap=3'||chr(13)||chr(10));
INSERT INTO "fieldmetainfo" VALUES (NULL,'_fileName','FILE_NAME','"FileName"','java.lang.String','com.gridnode.pdip.base.userprocedure.model.ProcedureDefFile','procedureDefFile.fileName',30,0,0,0,1,'0',1,'displayable=true'||chr(13)||chr(10)||'editable=true'||chr(13)||chr(10)||'mandatory=true'||chr(13)||chr(10),'type=file'||chr(13)||chr(10)||'file.downloadable=false'||chr(13)||chr(10)||'file.pathKey=procedureDefFile.filePath'||chr(13)||chr(10));
INSERT INTO "fieldmetainfo" VALUES (NULL,'_filePath','FILE_PATH','"FilePath"','java.lang.String','com.gridnode.pdip.base.userprocedure.model.ProcedureDefFile','procedureDefFile.filePath',30,0,0,0,1,'0',1,'displayable=false'||chr(13)||chr(10)||'editable=false'||chr(13)||chr(10)||'mandatory=false'||chr(13)||chr(10),'type=file'||chr(13)||chr(10));

--------- JavaProcedure -- Note SQLName is empty as this are entities are not persistant. To Confirm regd cmdLineExpr (CMDLineExpression).
DELETE FROM "fieldmetainfo" WHERE "EntityObjectName" LIKE '%JavaProcedure';
INSERT INTO "fieldmetainfo" VALUES (NULL,'_classPath','CLASS_PATH','','java.lang.String','com.gridnode.pdip.base.userprocedure.model.JavaProcedure','javaProcedure.classPath',80,0,0,0,1,'0',1,'displayable=true'||chr(13)||chr(10)||'editable=true'||chr(13)||chr(10)||'mandatory=true'||chr(13)||chr(10),'type=text'||chr(13)||chr(10)||'text.length.max=80');
INSERT INTO "fieldmetainfo" VALUES (NULL,'_className','CLASS_NAME','','java.lang.String','com.gridnode.pdip.base.userprocedure.model.JavaProcedure','javaProcedure.className',80,0,0,0,1,'0',1,'displayable=true'||chr(13)||chr(10)||'editable=true'||chr(13)||chr(10)||'mandatory=true'||chr(13)||chr(10),'type=text'||chr(13)||chr(10)||'text.length.max=80');
INSERT INTO "fieldmetainfo" VALUES (NULL,'_methodName','METHOD_NAME','','java.lang.String','com.gridnode.pdip.base.userprocedure.model.JavaProcedure','javaProcedure.methodName',80,0,0,0,1,'0',1,'displayable=true'||chr(13)||chr(10)||'editable=true'||chr(13)||chr(10)||'mandatory=true'||chr(13)||chr(10),'type=text'||chr(13)||chr(10)||'text.length.max=80');
INSERT INTO "fieldmetainfo" VALUES (NULL,'_jvmOptions','JVM_OPTIONS','','java.lang.String','com.gridnode.pdip.base.userprocedure.model.JavaProcedure','javaProcedure.jvmOptions',30,0,0,0,1,'0',1,'displayable=true'||chr(13)||chr(10)||'editable=true'||chr(13)||chr(10)||'mandatory=false'||chr(13)||chr(10),'type=text'||chr(13)||chr(10)||'text.length.max=30');
INSERT INTO "fieldmetainfo" VALUES (NULL,'_arguments','ARGUMENTS','','java.lang.String','com.gridnode.pdip.base.userprocedure.model.JavaProcedure','javaProcedure.arguments',30,0,0,0,1,'0',1,'displayable=true'||chr(13)||chr(10)||'editable=true'||chr(13)||chr(10)||'mandatory=false'||chr(13)||chr(10),'type=text'||chr(13)||chr(10)||'text.length.max=150');
INSERT INTO "fieldmetainfo" VALUES (NULL,'_isLocal','IS_LOCAL','','java.lang.Boolean','com.gridnode.pdip.base.userprocedure.model.JavaProcedure','javaProcedure.isLocal',0,0,0,0,0,'',999,'displayable=true'||chr(13)||chr(10)||'editable=true','type=enum'||chr(13)||chr(10)||'javaProcedure.isLocal.true=true'||chr(13)||chr(10)||'javaProcedure.isLocal.false=false');
INSERT INTO "fieldmetainfo" VALUES (NULL,'_type','TYPE','','java.lang.Integer','com.gridnode.pdip.base.userprocedure.model.JavaProcedure','javaProcedure.type',0,0,0,0,0,'',999,'displayable=false'||chr(13)||chr(10)||'editable=false','type=enum'||chr(13)||chr(10)||'javaProcedure.type.executable=1'||chr(13)||chr(10)||'javaProcedure.type.java=2'||chr(13)||chr(10)||'javaProcedure.type.soap=3'||chr(13)||chr(10));

--------- ShellExecutable -- Note SQLName is empty as this are entities are not persistant
DELETE FROM "fieldmetainfo" WHERE "EntityObjectName" LIKE '%ShellExecutable';
INSERT INTO "fieldmetainfo" VALUES (NULL,'_arguments','ARGUMENTS','','java.lang.String','com.gridnode.pdip.base.userprocedure.model.ShellExecutable','shellExecutable.arguments',30,0,0,0,1,'0',1,'displayable=true'||chr(13)||chr(10)||'editable=true'||chr(13)||chr(10)||'mandatory=false'||chr(13)||chr(10),'type=text'||chr(13)||chr(10)||'text.length.max=150');
INSERT INTO "fieldmetainfo" VALUES (NULL,'_type','TYPE','','java.lang.Integer','com.gridnode.pdip.base.userprocedure.model.ShellExecutable','shellExecutable.type',0,0,0,0,0,'',999,'displayable=false'||chr(13)||chr(10)||'editable=false','type=enum'||chr(13)||chr(10)||'shellExecutable.type.executable=1'||chr(13)||chr(10)||'shellExecutable.type.java=2'||chr(13)||chr(10)||'shellExecutable.type.soap=3'||chr(13)||chr(10));

--------- SoapProcedure -- Note SQLName is empty as this are entities are not persistant. To Confirm regd cmdLineExpr (CMDLineExpression).
DELETE FROM "fieldmetainfo" WHERE "EntityObjectName" LIKE '%SoapProcedure';
INSERT INTO "fieldmetainfo" VALUES (NULL,'_methodName','METHOD_NAME','','java.lang.String','com.gridnode.pdip.base.userprocedure.model.SoapProcedure','soapProcedure.methodName',80,0,0,0,1,'0',1,'displayable=true'||chr(13)||chr(10)||'editable=true'||chr(13)||chr(10)||'mandatory=true'||chr(13)||chr(10),'type=text'||chr(13)||chr(10)||'text.length.max=80');
INSERT INTO "fieldmetainfo" VALUES (NULL,'_type','TYPE','','java.lang.Integer','com.gridnode.pdip.base.userprocedure.model.SoapProcedure','soapProcedure.type',0,0,0,0,0,'','999','displayable=false'||chr(13)||chr(10)||'editable=false','type=enum'||chr(13)||chr(10)||'soapProcedure.type.executable=1'||chr(13)||chr(10)||'soapProcedure.type.java=2'||chr(13)||chr(10)||'soapProcedure.type.soap=3'||chr(13)||chr(10));
INSERT INTO "fieldmetainfo" VALUES (NULL,'_username','USER_NAME','','java.lang.String','com.gridnode.pdip.base.userprocedure.model.SoapProcedure','soapProcedure.userName',80,0,0,0,1,'0',1,'displayable=true'||chr(13)||chr(10)||'editable=true'||chr(13)||chr(10)||'mandatory=false'||chr(13)||chr(10),'type=text'||chr(13)||chr(10)||'text.length.max=80');
INSERT INTO "fieldmetainfo" VALUES (NULL,'_password','PASSWORD','','java.lang.String','com.gridnode.pdip.base.userprocedure.model.SoapProcedure','soapProcedure.password',80,0,0,0,1,'0',1,'displayable=true'||chr(13)||chr(10)||'editable=true'||chr(13)||chr(10)||'mandatory=false'||chr(13)||chr(10),'type=text'||chr(13)||chr(10)||'text.length.max=80');
--------- ParamDef -- Note SQLName is empty as this are entities are not persistant
DELETE FROM "fieldmetainfo" WHERE "EntityObjectName" LIKE '%ParamDef';
INSERT INTO "fieldmetainfo" VALUES (NULL,'_name','NAME','','java.lang.String','com.gridnode.pdip.base.userprocedure.model.ParamDef','paramDef.name',30,0,0,0,1,'0',1,'displayable=true'||chr(13)||chr(10)||'editable=true'||chr(13)||chr(10)||'mandatory=true'||chr(13)||chr(10),'type=text'||chr(13)||chr(10)||'text.length.max=30');
INSERT INTO "fieldmetainfo" VALUES (NULL,'_description','DESCRIPTION','','java.lang.String','com.gridnode.pdip.base.userprocedure.model.ParamDef','paramDef.description',30,0,0,0,1,'0',1,'displayable=true'||chr(13)||chr(10)||'editable=true'||chr(13)||chr(10)||'mandatory=true'||chr(13)||chr(10),'type=text'||chr(13)||chr(10)||'text.length.max=80');
INSERT INTO "fieldmetainfo" VALUES (NULL,'_source','SOURCE','','java.lang.Integer','com.gridnode.pdip.base.userprocedure.model.ParamDef','paramDef.source',30,0,0,0,1,'0',1,'displayable=true'||chr(13)||chr(10)||'editable=true'||chr(13)||chr(10)||'mandatory=true'||chr(13)||chr(10),'type=enum'||chr(13)||chr(10)||'paramDef.source.userDefined=1'||chr(13)||chr(10)||'paramDef.source.gdoc=2'||chr(13)||chr(10)||'paramDef.source.udoc=3'||chr(13)||chr(10)||'paramDef.source.attachments=4'||chr(13)||chr(10));
INSERT INTO "fieldmetainfo" VALUES (NULL,'_type','TYPE','','java.lang.Integer','com.gridnode.pdip.base.userprocedure.model.ParamDef','paramDef.type',30,0,0,0,1,'0',1,'displayable=true'||chr(13)||chr(10)||'editable=true'||chr(13)||chr(10)||'mandatory=true'||chr(13)||chr(10),'type=enum'||chr(13)||chr(10)||'paramDef.type.string=1'||chr(13)||chr(10)||'paramDef.type.integer=2'||chr(13)||chr(10)||'paramDef.type.long=3'||chr(13)||chr(10)||'paramDef.type.double=4'||chr(13)||chr(10)||'paramDef.type.boolean=5'||chr(13)||chr(10)||'paramDef.type.date=6'||chr(13)||chr(10)||'paramDef.type.object=7'||chr(13)||chr(10)||'paramDef.type.datahandler=8'||chr(13)||chr(10)||'paramDef.type.datahandlerArray=9'||chr(13)||chr(10)||'paramDef.type.stringArray=10'||chr(13)||chr(10)||'paramDef.type.byteArray=11'||chr(13)||chr(10)||'paramDef.type.byteArrayArray=12'||chr(13)||chr(10));
INSERT INTO "fieldmetainfo" VALUES (NULL,'_value','VALUE','','java.lang.Object','com.gridnode.pdip.base.userprocedure.model.ParamDef','paramDef.value',30,0,0,0,1,'0',1,'displayable=true'||chr(13)||chr(10)||'editable=true'||chr(13)||chr(10)||'mandatory=true'||chr(13)||chr(10),'type=text'||chr(13)||chr(10)||'text.length.max=255');
INSERT INTO "fieldmetainfo" VALUES (NULL,'_dateFormat','DATE_FORMAT','','java.lang.String','com.gridnode.pdip.base.userprocedure.model.ParamDef','paramDef.dateFormat',30,0,0,0,1,'0',1,'displayable=true'||chr(13)||chr(10)||'editable=true'||chr(13)||chr(10)||'mandatory=true'||chr(13)||chr(10),'type=enum'||chr(13)||chr(10)||'paramDef.dateFormat.format1=ddMMyyHHmmss'||chr(13)||chr(10)||'paramDef.dateFormat.format2=ddMMyyyyHHmmss'||chr(13)||chr(10)||'paramDef.dateFormat.format3=MMddyyHHmmss'||chr(13)||chr(10)||'paramDef.dateFormat.format4=MMddyyyyHHmmss'||chr(13)||chr(10)||'paramDef.dateFormat.format5=yyMMddHHmmss'||chr(13)||chr(10)||'paramDef.dateFormat.format6=yyyyMMddHHmmss'||chr(13)||chr(10)||'paramDef.dateFormat.format7=yyyyMMdd'||chr(13)||chr(10)||'paramDef.dateFormat.format8=yyMMdd'||chr(13)||chr(10)||'paramDef.dateFormat.format9=ddMMyyyy'||chr(13)||chr(10)||'paramDef.dateFormat.format10=ddMMyy'||chr(13)||chr(10)||'paramDef.dateFormat.format11=MMddyyyy'||chr(13)||chr(10)||'paramDef.dateFormat.format12=MMddyy');

--------- ReturnDef -- Note SQLName is empty as this are entities are not persistant
DELETE FROM "fieldmetainfo" WHERE "EntityObjectName" LIKE '%ReturnDef';
INSERT INTO "fieldmetainfo" VALUES (NULL,'_operator','OPERATOR','','java.lang.Integer','com.gridnode.pdip.base.userprocedure.model.ReturnDef','returnDef.operator',30,0,0,0,1,'0',1,'displayable=true'||chr(13)||chr(10)||'editable=true'||chr(13)||chr(10)||'mandatory=true'||chr(13)||chr(10),'type=enum'||chr(13)||chr(10)||'returnDef.operator.equal=0'||chr(13)||chr(10)||'returnDef.operator.less=1'||chr(13)||chr(10)||'returnDef.operator.lessOrEqual=2'||chr(13)||chr(10)||'returnDef.operator.greaterOrEqual=3'||chr(13)||chr(10)||'returnDef.operator.greater=4'||chr(13)||chr(10)||'returnDef.operator.notEqual=5'||chr(13)||chr(10)||'returnDef.operator.between=6'||chr(13)||chr(10));
INSERT INTO "fieldmetainfo" VALUES (NULL,'_value','VALUE','','java.lang.Object','com.gridnode.pdip.base.userprocedure.model.ReturnDef','returnDef.value',30,0,0,0,1,'0',1,'displayable=true'||chr(13)||chr(10)||'editable=true'||chr(13)||chr(10)||'mandatory=true'||chr(13)||chr(10),'type=text'||chr(13)||chr(10)||'text.length.max=30');
INSERT INTO "fieldmetainfo" VALUES (NULL,'_action','ACTION','','java.lang.Integer','com.gridnode.pdip.base.userprocedure.model.ReturnDef','returnDef.actionType',30,0,0,0,1,'0',1,'displayable=true'||chr(13)||chr(10)||'editable=true'||chr(13)||chr(10)||'mandatory=true'||chr(13)||chr(10),'type=enum'||chr(13)||chr(10)||'returnDef.actionType.continue=1'||chr(13)||chr(10)||'returnDef.actionType.abort=2'||chr(13)||chr(10));
INSERT INTO "fieldmetainfo" VALUES (NULL,'_alert','ALERT','','java.lang.Long','com.gridnode.pdip.base.userprocedure.model.ReturnDef','returnDef.alert',30,0,0,0,1,'0',1,'displayable=true'||chr(13)||chr(10)||'editable=true'||chr(13)||chr(10)||'mandatory=false'||chr(13)||chr(10),'type=foreign'||chr(13)||chr(10)||'foreign.key=alert.uid'||chr(13)||chr(10)||'foreign.display=alert.description'||chr(13)||chr(10));

--------- iCalAlarm
DELETE FROM "fieldmetainfo" WHERE "EntityObjectName" LIKE '%iCalAlarm';
INSERT INTO "fieldmetainfo" VALUES (NULL,'_uId','UID','"UID"','java.lang.Long','com.gridnode.pdip.base.time.entities.model.iCalAlarm','',0,0,0,1,1,'',999,'displayable=false','');
INSERT INTO "fieldmetainfo" VALUES (NULL,'_startDuration','START_DURATION','"StartDuration"','java.lang.Long','com.gridnode.pdip.base.time.entities.model.iCalAlarm','',0,0,0,1,1,'',999,'displayable=false','');
INSERT INTO "fieldmetainfo" VALUES (NULL,'_startDt','START_DT','"StartDt"','java.util.Date','com.gridnode.pdip.base.time.entities.model.iCalAlarm','',0,0,0,1,1,'',999,'displayable=false','');
INSERT INTO "fieldmetainfo" VALUES (NULL,'_related','RELATED','"Related"','java.lang.Integer','com.gridnode.pdip.base.time.entities.model.iCalAlarm','',0,0,0,1,1,'',999,'displayable=false','');
INSERT INTO "fieldmetainfo" VALUES (NULL,'_delayPeriod','DELAY_PERIOD','"DelayPeriod"','java.lang.Long','com.gridnode.pdip.base.time.entities.model.iCalAlarm','',0,0,0,1,1,'',999,'displayable=false','');
INSERT INTO "fieldmetainfo" VALUES (NULL,'_repeat','REPEAT','"RepeatCount"','java.lang.Integer','com.gridnode.pdip.base.time.entities.model.iCalAlarm','',0,0,0,1,1,'',999,'displayable=false','');
INSERT INTO "fieldmetainfo" VALUES (NULL,'_category','CATEGORY','"Category"','java.lang.String','com.gridnode.pdip.base.time.entities.model.iCalAlarm','',50,0,0,1,1,'',999,'displayable=false','');
INSERT INTO "fieldmetainfo" VALUES (NULL,'_senderKey','SENDER_KEY','"SenderKey"','java.lang.String','com.gridnode.pdip.base.time.entities.model.iCalAlarm','',50,0,0,1,1,'',999,'displayable=false','');
INSERT INTO "fieldmetainfo" VALUES (NULL,'_receiverKey','RECEIVER_KEY','"ReceiverKey"','java.lang.String','com.gridnode.pdip.base.time.entities.model.iCalAlarm','',50,0,0,1,1,'',999,'displayable=false','');
INSERT INTO "fieldmetainfo" VALUES (NULL,'_disabled','DISABLED','"Disabled"','java.lang.Boolean','com.gridnode.pdip.base.time.entities.model.iCalAlarm','',0,0,0,1,1,'',999,'displayable=false','');
INSERT INTO "fieldmetainfo" VALUES (NULL,'_nextDueTime','NEXT_DUE_TIME','"NextDueTime"','java.util.Date','com.gridnode.pdip.base.time.entities.model.iCalAlarm','',0,0,0,1,1,'',999,'displayable=false','');
INSERT INTO "fieldmetainfo" VALUES (NULL,'_count','COUNT','"Count"','java.lang.Integer','com.gridnode.pdip.base.time.entities.model.iCalAlarm','',0,0,0,1,1,'',999,'displayable=false','');
INSERT INTO "fieldmetainfo" VALUES (NULL,'_parentUid','PARENT_UID','"ParentUid"','java.lang.Long','com.gridnode.pdip.base.time.entities.model.iCalAlarm', 'ical_alarm.parentUid', 0,0,0,1,1,'', 999,'displayable=false','');
INSERT INTO "fieldmetainfo" VALUES (NULL,'_parentKind','PARENT_KIND','"ParentKind"','Short','com.gridnode.pdip.base.time.entities.model.iCalAlarm', 'ical_alarm.parentKind', 0,0,0,1,1,'', 999,'displayable=false','');
INSERT INTO "fieldmetainfo" VALUES (NULL,'_recurListStr','RECUR_LIST_STR','"RecurListStr"','java.lang.String','com.gridnode.pdip.base.time.entities.model.iCalAlarm', 'ical_alarm.recurListStr', 50,0,0,1,1,'', 999,'displayable=false','');
INSERT INTO "fieldmetainfo" VALUES (NULL,'_isRecurComplete','IS_RECUR_COMPLETE','"IsRecurComplete"','java.lang.Boolean','com.gridnode.pdip.base.time.entities.model.iCalAlarm', 'ical_alarm.isRecurComplete', 0,0,0,1,1,'', 999,'displayable=false','');
INSERT INTO "fieldmetainfo" VALUES (NULL,'_curRecur','CUR_RECUR','"CurRecur"','java.lang.String','com.gridnode.pdip.base.time.entities.model.iCalAlarm', 'ical_alarm.curRecur', 50,0,0,1,1,'', 999,'displayable=false','');
INSERT INTO "fieldmetainfo" VALUES (NULL,'_isPseudoParent','IS_PSEUDO_PARENT','"IsPseudoParent"','java.lang.Boolean','com.gridnode.pdip.base.time.entities.model.iCalAlarm', 'ical_alarm.isPseudoParent', 0,0,0,1,1,'', 999,'displayable=false','');
INSERT INTO "fieldmetainfo" VALUES (NULL,'_includeParentStartTime','INCLUDE_PARENT_START_TIME','"IncludeParentStartTime"','java.lang.Boolean','com.gridnode.pdip.base.time.entities.model.iCalAlarm', 'ical_alarm.includeParentStartTime', 0,0,0,1,1,'', 999,'displayable=false','');
INSERT INTO "fieldmetainfo" VALUES (NULL,'_taskId','TASK_ID','"TaskId"','java.lang.String','com.gridnode.pdip.base.time.entities.model.iCalAlarm','ical_alarm.taskId',120,0,0,1,1,'',999,'displayable=false','type=text'||chr(13)||chr(10)||'text.length.max=120'||chr(13)||chr(10));

--------- iCalEvent
DELETE FROM "fieldmetainfo" WHERE "EntityObjectName" LIKE '%iCalEvent';
INSERT INTO "fieldmetainfo" VALUES (NULL,'_uId','UID','"UID"','java.lang.Long','com.gridnode.pdip.base.time.entities.model.iCalEvent','',0,0,0,1,1,'',999,'displayable=false','');
INSERT INTO "fieldmetainfo" VALUES (NULL,'_kind','KIND','"Kind"','java.lang.Short','com.gridnode.pdip.base.time.entities.model.iCalEvent','',0,0,0,1,1,'',999,'displayable=false','');
INSERT INTO "fieldmetainfo" VALUES (NULL,'_ownerId','OWNER_ID','"OwnerId"','java.lang.Integer','com.gridnode.pdip.base.time.entities.model.iCalEvent','',0,0,0,1,1,'',999,'displayable=false','');
INSERT INTO "fieldmetainfo" VALUES (NULL,'_classification','CLASSIFICATION','"Classification"','java.lang.String','com.gridnode.pdip.base.time.entities.model.iCalEvent','',50,0,0,1,1,'',999,'displayable=false','');
INSERT INTO "fieldmetainfo" VALUES (NULL,'_createDt','CREATE_DT','"CreateDt"','java.util.Date','com.gridnode.pdip.base.time.entities.model.iCalEvent','',0,0,0,1,1,'',999,'displayable=false','');
INSERT INTO "fieldmetainfo" VALUES (NULL,'_lastModifyDt','LAST_MODIFY_DT','"LastModifyDt"','java.util.Date','com.gridnode.pdip.base.time.entities.model.iCalEvent','',0,0,0,1,1,'',999,'displayable=false','');
INSERT INTO "fieldmetainfo" VALUES (NULL,'_isDateType','IS_DATE_TYPE','"IsDateType"','java.lang.Boolean','com.gridnode.pdip.base.time.entities.model.iCalEvent','',0,0,0,1,1,'',999,'displayable=false','');
INSERT INTO "fieldmetainfo" VALUES (NULL,'_isDtFloat','IS_DT_FLOAT','"IsDtFloat"','java.lang.Boolean','com.gridnode.pdip.base.time.entities.model.iCalEvent','',0,0,0,1,1,'',999,'displayable=false','');
INSERT INTO "fieldmetainfo" VALUES (NULL,'_startDt','START_DT','"StartDt"','java.util.Date','com.gridnode.pdip.base.time.entities.model.iCalEvent','',0,0,0,1,1,'',999,'displayable=false','');
INSERT INTO "fieldmetainfo" VALUES (NULL,'_priority','PRIORITY','"Priority"','java.lang.Integer','com.gridnode.pdip.base.time.entities.model.iCalEvent','',0,0,0,1,1,'',999,'displayable=false','');
INSERT INTO "fieldmetainfo" VALUES (NULL,'_sequenceNum','SEQUENCE_NUM','"SequenceNum"','java.lang.Integer','com.gridnode.pdip.base.time.entities.model.iCalEvent','',0,0,0,1,1,'',999,'displayable=false','');
INSERT INTO "fieldmetainfo" VALUES (NULL,'_status','STATUS','"Status"','java.lang.Integer','com.gridnode.pdip.base.time.entities.model.iCalEvent','',0,0,0,1,1,'',999,'displayable=false','');
INSERT INTO "fieldmetainfo" VALUES (NULL,'_iCalUid','iCal_UID','"iCalUid"','java.lang.String','com.gridnode.pdip.base.time.entities.model.iCalEvent','',50,0,0,1,1,'',999,'displayable=false','');
INSERT INTO "fieldmetainfo" VALUES (NULL,'_endDt','END_DT','"EndDt"','java.util.Date','com.gridnode.pdip.base.time.entities.model.iCalEvent','',0,0,0,1,1,'',999,'displayable=false','');
INSERT INTO "fieldmetainfo" VALUES (NULL,'_duration','DURATION','"Duration"','java.lang.Long','com.gridnode.pdip.base.time.entities.model.iCalEvent','',0,0,0,1,1,'',999,'displayable=false','');
INSERT INTO "fieldmetainfo" VALUES (NULL,'_propertiesStr','PROPERTIES_STR','"PropertiesStr"','java.lang.String','com.gridnode.pdip.base.time.entities.model.iCalEvent', 'ical_event.PropertiesStr', 50,0,0,1,1,'', 999,'displayable=false','');
INSERT INTO "fieldmetainfo" VALUES (NULL,'_timeTransparency','TIME_TRANSPARENCY','"TimeTransparency"','java.lang.Integer','com.gridnode.pdip.base.time.entities.model.iCalEvent','',0,0,0,1,1,'',999,'displayable=false','');

--------- iCalTodo
DELETE FROM "fieldmetainfo" WHERE "EntityObjectName" LIKE '%iCalTodo';
INSERT INTO "fieldmetainfo" VALUES (NULL,'_uId','UID','"UID"','java.lang.Long','com.gridnode.pdip.base.time.entities.model.iCalTodo','',0,0,0,1,1,'',999,'displayable=false','');
INSERT INTO "fieldmetainfo" VALUES (NULL,'_kind','KIND','"Kind"','java.lang.Short','com.gridnode.pdip.base.time.entities.model.iCalTodo','',0,0,0,1,1,'',999,'displayable=false','');
INSERT INTO "fieldmetainfo" VALUES (NULL,'_ownerId','OWNER_ID','"OwnerId"','java.lang.Integer','com.gridnode.pdip.base.time.entities.model.iCalTodo','',0,0,0,1,1,'',999,'displayable=false','');
INSERT INTO "fieldmetainfo" VALUES (NULL,'_classification','CLASSIFICATION','"Classification"','java.lang.String','com.gridnode.pdip.base.time.entities.model.iCalTodo','',50,0,0,1,1,'',999,'displayable=false','');
INSERT INTO "fieldmetainfo" VALUES (NULL,'_createDt','CREATE_DT','"CreateDt"','java.util.Date','com.gridnode.pdip.base.time.entities.model.iCalTodo','',0,0,0,1,1,'',999,'displayable=false','');
INSERT INTO "fieldmetainfo" VALUES (NULL,'_lastModifyDt','LAST_MODIFY_DT','"LastModifyDt"','java.util.Date','com.gridnode.pdip.base.time.entities.model.iCalTodo','',0,0,0,1,1,'',999,'displayable=false','');
INSERT INTO "fieldmetainfo" VALUES (NULL,'_isDateType','IS_DATE_TYPE','"IsDateType"','java.lang.Boolean','com.gridnode.pdip.base.time.entities.model.iCalTodo','',0,0,0,1,1,'',999,'displayable=false','');
INSERT INTO "fieldmetainfo" VALUES (NULL,'_isDtFloat','IS_DT_FLOAT','"IsDtFloat"','java.lang.Boolean','com.gridnode.pdip.base.time.entities.model.iCalTodo','',0,0,0,1,1,'',999,'displayable=false','');
INSERT INTO "fieldmetainfo" VALUES (NULL,'_startDt','START_DT','"StartDt"','java.util.Date','com.gridnode.pdip.base.time.entities.model.iCalTodo','',0,0,0,1,1,'',999,'displayable=false','');
INSERT INTO "fieldmetainfo" VALUES (NULL,'_priority','PRIORITY','"Priority"','java.lang.Integer','com.gridnode.pdip.base.time.entities.model.iCalTodo','',0,0,0,1,1,'',999,'displayable=false','');
INSERT INTO "fieldmetainfo" VALUES (NULL,'_sequenceNum','SEQUENCE_NUM','"SequenceNum"','java.lang.Integer','com.gridnode.pdip.base.time.entities.model.iCalTodo','',0,0,0,1,1,'',999,'displayable=false','');
INSERT INTO "fieldmetainfo" VALUES (NULL,'_status','STATUS','"Status"','java.lang.Integer','com.gridnode.pdip.base.time.entities.model.iCalTodo','',0,0,0,1,1,'',999,'displayable=false','');
INSERT INTO "fieldmetainfo" VALUES (NULL,'_iCalUid','iCal_UID','"iCalUid"','java.lang.String','com.gridnode.pdip.base.time.entities.model.iCalTodo','',50,0,0,1,1,'',999,'displayable=false','');
INSERT INTO "fieldmetainfo" VALUES (NULL,'_endDt','END_DT','"EndDt"','java.util.Date','com.gridnode.pdip.base.time.entities.model.iCalTodo','',0,0,0,1,1,'',999,'displayable=false','');
INSERT INTO "fieldmetainfo" VALUES (NULL,'_duration','DURATION','"Duration"','java.lang.Long','com.gridnode.pdip.base.time.entities.model.iCalTodo','',0,0,0,0,1,'1','','displayable=false','');
INSERT INTO "fieldmetainfo" VALUES (NULL,'_completeDt','COMPLETE_DT','"CompleteDt"','java.util.Date','com.gridnode.pdip.base.time.entities.model.iCalTodo','',0,0,0,1,1,'',999,'displayable=false','');
INSERT INTO "fieldmetainfo" VALUES (NULL,'_propertiesStr','PROPERTIES_STR','"PropertiesStr"','java.lang.String','com.gridnode.pdip.base.time.entities.model.iCalTodo', 'ical_todo.PropertiesStr', 50,0,0,1,1,'', 999,'displayable=false','');
INSERT INTO "fieldmetainfo" VALUES (NULL,'_percentCompleted','PERCENT_COMPLETED','"PercentCompleted"','java.lang.Integer','com.gridnode.pdip.base.time.entities.model.iCalTodo','',0,0,0,1,1,'',999,'displayable=false','');

--------- iCalProperty
DELETE FROM "fieldmetainfo" WHERE "EntityObjectName" LIKE '%iCalProperty';
INSERT INTO "fieldmetainfo" VALUES (NULL,'_uId','UID','"UID"','java.lang.Long','com.gridnode.pdip.base.time.entities.model.iCalProperty','',0,0,0,1,1,'',999,'displayable=false','');
INSERT INTO "fieldmetainfo" VALUES (NULL,'_kind','KIND','"Kind"','java.lang.Short','com.gridnode.pdip.base.time.entities.model.iCalProperty','',0,0,0,1,1,'',999,'displayable=false','');
INSERT INTO "fieldmetainfo" VALUES (NULL,'_compKind','COMP_KIND','"CompKind"','java.lang.Short','com.gridnode.pdip.base.time.entities.model.iCalProperty','',0,0,0,1,1,'',999,'displayable=false','');
INSERT INTO "fieldmetainfo" VALUES (NULL,'_iCalCompId','iCal_COMP_ID','"iCalCompId"','java.lang.Long','com.gridnode.pdip.base.time.entities.model.iCalProperty','',0,0,0,1,1,'',999,'displayable=false','');

--------- iCalInt
DELETE FROM "fieldmetainfo" WHERE "EntityObjectName" LIKE '%iCalInt';
INSERT INTO "fieldmetainfo" VALUES (NULL,'_uId','UID','"UID"','java.lang.Long','com.gridnode.pdip.base.time.entities.model.iCalInt','',0,0,0,1,1,'',999,'displayable=false','');
INSERT INTO "fieldmetainfo" VALUES (NULL,'_compKind','COMP_KIND','"CompKind"','java.lang.Short','com.gridnode.pdip.base.time.entities.model.iCalInt','',0,0,0,1,1,'',999,'displayable=false','');
INSERT INTO "fieldmetainfo" VALUES (NULL,'_propKind','PROP_KIND','"PropKind"','java.lang.Short','com.gridnode.pdip.base.time.entities.model.iCalInt','',0,0,0,1,1,'',999,'displayable=false','');
INSERT INTO "fieldmetainfo" VALUES (NULL,'_paramKind','PARAM_KIND','"ParamKind"','java.lang.Short','com.gridnode.pdip.base.time.entities.model.iCalInt','',0,0,0,1,1,'',999,'displayable=false','');
INSERT INTO "fieldmetainfo" VALUES (NULL,'_refKind','REF_KIND','"RefKind"','java.lang.Short','com.gridnode.pdip.base.time.entities.model.iCalInt','',0,0,0,1,1,'',999,'displayable=false','');
INSERT INTO "fieldmetainfo" VALUES (NULL,'_valueKind','VALUE_KIND','"ValueKind"','java.lang.Short','com.gridnode.pdip.base.time.entities.model.iCalInt','',0,0,0,1,1,'',999,'displayable=false','');
INSERT INTO "fieldmetainfo" VALUES (NULL,'_iCalCompId','iCal_COMP_ID','"iCalCompId"','java.lang.Long','com.gridnode.pdip.base.time.entities.model.iCalInt','',0,0,0,1,1,'',999,'displayable=false','');
INSERT INTO "fieldmetainfo" VALUES (NULL,'_refId','REF_ID','"RefId"','java.lang.Long','com.gridnode.pdip.base.time.entities.model.iCalInt','',0,0,0,1,1,'',999,'displayable=false','');
INSERT INTO "fieldmetainfo" VALUES (NULL,'_intValue','INT_VALUE','"IntValue"','java.lang.Integer','com.gridnode.pdip.base.time.entities.model.iCalInt','',0,0,0,1,1,'',999,'displayable=false','');

--------- iCalDate
DELETE FROM "fieldmetainfo" WHERE "EntityObjectName" LIKE '%iCalDate';
INSERT INTO "fieldmetainfo" VALUES (NULL,'_uId','UID','"UID"','java.lang.Long','com.gridnode.pdip.base.time.entities.model.iCalDate','',0,0,0,1,1,'',999,'displayable=false','');
INSERT INTO "fieldmetainfo" VALUES (NULL,'_compKind','COMP_KIND','"CompKind"','java.lang.Short','com.gridnode.pdip.base.time.entities.model.iCalDate','',0,0,0,1,1,'',999,'displayable=false','');
INSERT INTO "fieldmetainfo" VALUES (NULL,'_propKind','PROP_KIND','"PropKind"','java.lang.Short','com.gridnode.pdip.base.time.entities.model.iCalDate','',0,0,0,1,1,'',999,'displayable=false','');
INSERT INTO "fieldmetainfo" VALUES (NULL,'_paramKind','PARAM_KIND','"ParamKind"','java.lang.Short','com.gridnode.pdip.base.time.entities.model.iCalDate','',0,0,0,1,1,'',999,'displayable=false','');
INSERT INTO "fieldmetainfo" VALUES (NULL,'_refKind','REF_KIND','"RefKind"','java.lang.Short','com.gridnode.pdip.base.time.entities.model.iCalDate','',0,0,0,1,1,'',999,'displayable=false','');
INSERT INTO "fieldmetainfo" VALUES (NULL,'_valueKind','VALUE_KIND','"ValueKind"','java.lang.Short','com.gridnode.pdip.base.time.entities.model.iCalDate','',0,0,0,1,1,'',999,'displayable=false','');
INSERT INTO "fieldmetainfo" VALUES (NULL,'_iCalCompId','iCal_COMP_ID','"iCalCompId"','java.lang.Long','com.gridnode.pdip.base.time.entities.model.iCalDate','',0,0,0,1,1,'',999,'displayable=false','');
INSERT INTO "fieldmetainfo" VALUES (NULL,'_refId','REF_ID','"RefId"','java.lang.Long','com.gridnode.pdip.base.time.entities.model.iCalDate','',0,0,0,1,1,'',999,'displayable=false','');
INSERT INTO "fieldmetainfo" VALUES (NULL,'_dateValue','DATE_VALUE','"DateValue"','java.util.Date','com.gridnode.pdip.base.time.entities.model.iCalDate','',0,0,0,1,1,'',999,'displayable=false','');

--------- iCalString
DELETE FROM "fieldmetainfo" WHERE "EntityObjectName" LIKE '%iCalString';
INSERT INTO "fieldmetainfo" VALUES (NULL,'_uId','UID','"UID"','java.lang.Long','com.gridnode.pdip.base.time.entities.model.iCalString','',0,0,0,1,1,'',999,'displayable=false','');
INSERT INTO "fieldmetainfo" VALUES (NULL,'_compKind','COMP_KIND','"CompKind"','java.lang.Short','com.gridnode.pdip.base.time.entities.model.iCalString','',0,0,0,1,1,'',999,'displayable=false','');
INSERT INTO "fieldmetainfo" VALUES (NULL,'_propKind','PROP_KIND','"PropKind"','java.lang.Short','com.gridnode.pdip.base.time.entities.model.iCalString','',0,0,0,1,1,'',999,'displayable=false','');
INSERT INTO "fieldmetainfo" VALUES (NULL,'_paramKind','PARAM_KIND','"ParamKind"','java.lang.Short','com.gridnode.pdip.base.time.entities.model.iCalString','',0,0,0,1,1,'',999,'displayable=false','');
INSERT INTO "fieldmetainfo" VALUES (NULL,'_refKind','REF_KIND','"RefKind"','java.lang.Short','com.gridnode.pdip.base.time.entities.model.iCalString','',0,0,0,1,1,'',999,'displayable=false','');
INSERT INTO "fieldmetainfo" VALUES (NULL,'_valueKind','VALUE_KIND','"ValueKind"','java.lang.Short','com.gridnode.pdip.base.time.entities.model.iCalString','',0,0,0,1,1,'',999,'displayable=false','');
INSERT INTO "fieldmetainfo" VALUES (NULL,'_iCalCompId','iCal_COMP_ID','"iCalCompId"','java.lang.Long','com.gridnode.pdip.base.time.entities.model.iCalString','',0,0,0,1,1,'',999,'displayable=false','');
INSERT INTO "fieldmetainfo" VALUES (NULL,'_refId','REF_ID','"RefId"','java.lang.Long','com.gridnode.pdip.base.time.entities.model.iCalString','',0,0,0,1,1,'',999,'displayable=false','');
INSERT INTO "fieldmetainfo" VALUES (NULL,'_strValue','STR_VALUE','"StrValue"','java.lang.String','com.gridnode.pdip.base.time.entities.model.iCalString','',50,0,0,1,1,'',999,'displayable=false','');

--------- iCalText
DELETE FROM "fieldmetainfo" WHERE "EntityObjectName" LIKE '%iCalText';
INSERT INTO "fieldmetainfo" VALUES (NULL,'_uId','UID','"UID"','java.lang.Long','com.gridnode.pdip.base.time.entities.model.iCalText','',0,0,0,1,1,'',999,'displayable=false','');
INSERT INTO "fieldmetainfo" VALUES (NULL,'_compKind','COMP_KIND','"CompKind"','java.lang.Short','com.gridnode.pdip.base.time.entities.model.iCalText','',0,0,0,1,1,'',999,'displayable=false','');
INSERT INTO "fieldmetainfo" VALUES (NULL,'_propKind','PROP_KIND','"PropKind"','java.lang.Short','com.gridnode.pdip.base.time.entities.model.iCalText','',0,0,0,1,1,'',999,'displayable=false','');
INSERT INTO "fieldmetainfo" VALUES (NULL,'_paramKind','PARAM_KIND','"ParamKind"','java.lang.Short','com.gridnode.pdip.base.time.entities.model.iCalText','',0,0,0,1,1,'',999,'displayable=false','');
INSERT INTO "fieldmetainfo" VALUES (NULL,'_refKind','REF_KIND','"RefKind"','java.lang.Short','com.gridnode.pdip.base.time.entities.model.iCalText','',0,0,0,1,1,'',999,'displayable=false','');
INSERT INTO "fieldmetainfo" VALUES (NULL,'_valueKind','VALUE_KIND','"ValueKind"','java.lang.Short','com.gridnode.pdip.base.time.entities.model.iCalText','',0,0,0,1,1,'',999,'displayable=false','');
INSERT INTO "fieldmetainfo" VALUES (NULL,'_iCalCompId','iCal_COMP_ID','"iCalCompId"','java.lang.Long','com.gridnode.pdip.base.time.entities.model.iCalText','',0,0,0,1,1,'',999,'displayable=false','');
INSERT INTO "fieldmetainfo" VALUES (NULL,'_refId','REF_ID','"RefId"','java.lang.Long','com.gridnode.pdip.base.time.entities.model.iCalText','',0,0,0,1,1,'',999,'displayable=false','');
INSERT INTO "fieldmetainfo" VALUES (NULL,'_textValue','TEXT_VALUE','"TextValue"','java.lang.String','com.gridnode.pdip.base.time.entities.model.iCalText','',50,0,0,1,1,'',999,'displayable=false','');

--------- SessionAudit
DELETE FROM "fieldmetainfo" WHERE "EntityObjectName" LIKE '%SessionAudit';
INSERT INTO "fieldmetainfo" VALUES(NULL,'_uId','UID','"UID"','java.lang.Long','com.gridnode.pdip.base.session.model.SessionAudit','session.uid',0,0,0,0,0,'0',999,'','');
INSERT INTO "fieldmetainfo" VALUES(NULL,'_sessionId','SESSION_ID','"SessionId"','java.lang.String','com.gridnode.pdip.base.session.model.SessionAudit','session.sessionId',0,0,0,0,1,'0',1,'','');
INSERT INTO "fieldmetainfo" VALUES(NULL,'_sessionName','SESSION_NAME','"SessionName"','java.lang.String','com.gridnode.pdip.base.session.model.SessionAudit','session.sessionName',0,0,0,1,1,'0',2,'','');
INSERT INTO "fieldmetainfo" VALUES(NULL,'_state','STATE','"State"','java.lang.Short','com.gridnode.pdip.base.session.model.SessionAudit','session.state',0,0,0,1,1,'0',3,'','');
INSERT INTO "fieldmetainfo" VALUES(NULL,'_sessionData','SESSION_DATA','"SessionData"','byte[]','com.gridnode.pdip.base.session.model.SessionAudit','session.sessionData',0,0,0,1,0,'0',4,'','');
INSERT INTO "fieldmetainfo" VALUES(NULL,'_openTime','OPEN_TIME','"OpenTime"','java.util.Date','com.gridnode.pdip.base.session.model.SessionAudit','session.openTime',0,0,0,0,1,'0',5,'','');
INSERT INTO "fieldmetainfo" VALUES(NULL,'_lastActiveTime','LAST_ACTIVE_TIME','"LastActiveTime"','java.util.Date','com.gridnode.pdip.base.session.model.SessionAudit','session.lastActiveTime',0,0,0,0,1,'0',6,'','');
INSERT INTO "fieldmetainfo" VALUES(NULL,'_destroyTime','DESTROY_TIME','"DestroyTime"','java.util.Date','com.gridnode.pdip.base.session.model.SessionAudit','session.destroyTime',0,0,0,0,1,'0',7,'','');

--------- CountryCode
DELETE from "fieldmetainfo" WHERE "EntityObjectName" LIKE '%CountryCode';
INSERT INTO "fieldmetainfo" VALUES (NULL,'_name','NAME','"Name"','java.lang.String','com.gridnode.pdip.base.locale.model.CountryCode','countryCode.name',0,0,0,1,1,NULL,999,'displayable=true'||chr(13)||chr(10)||'editable=false'||chr(13)||chr(10)||'mandatory=false'||chr(13)||chr(10),'type=text');
INSERT INTO "fieldmetainfo" VALUES (NULL,'_numericalCode','NUMERICAL_CODE','"NumericalCode"','java.lang.Integer','com.gridnode.pdip.base.locale.model.CountryCode','countryCode.numericalCode',0,0,0,1,1,NULL,999,'displayable=false'||chr(13)||chr(10)||'editable=false'||chr(13)||chr(10)||'mandatory=false'||chr(13)||chr(10),'type=uid');
INSERT INTO "fieldmetainfo" VALUES (NULL,'_alpha2Code','ALPHA_2_CODE','"Alpha2Code"','java.lang.String','com.gridnode.pdip.base.locale.model.CountryCode','countryCode.alpha2Code',0,0,0,1,1,NULL,999,'displayable=false'||chr(13)||chr(10)||'editable=false'||chr(13)||chr(10)||'mandatory=false'||chr(13)||chr(10),'type=text');
INSERT INTO "fieldmetainfo" VALUES (NULL,'_alpha3Code','ALPHA_3_CODE','"Alpha3Code"','java.lang.String','com.gridnode.pdip.base.locale.model.CountryCode','countryCode.alpha3Code',0,0,0,1,1,NULL,999,'displayable=false'||chr(13)||chr(10)||'editable=false'||chr(13)||chr(10)||'mandatory=false'||chr(13)||chr(10),'type=text');

--------- LanguageCode
DELETE from "fieldmetainfo" WHERE "EntityObjectName" LIKE '%LanguageCode';
INSERT INTO "fieldmetainfo" VALUES (NULL,'_name','NAME','"Name"','java.lang.String','com.gridnode.pdip.base.locale.model.LanguageCode','languageCode.name',0,0,0,1,1,NULL,999,'displayable=true'||chr(13)||chr(10)||'editable=false'||chr(13)||chr(10)||'mandatory=false'||chr(13)||chr(10),'type=text');
INSERT INTO "fieldmetainfo" VALUES (NULL,'_bAlpha3Code','B_ALPHA_3_CODE','"BAlpha3Code"','java.lang.String','com.gridnode.pdip.base.locale.model.LanguageCode','languageCode.balpha3Code',0,0,0,1,1,NULL,999,'displayable=false'||chr(13)||chr(10)||'editable=false'||chr(13)||chr(10)||'mandatory=false'||chr(13)||chr(10),'type=text');
INSERT INTO "fieldmetainfo" VALUES (NULL,'_alpha2Code','ALPHA_2_CODE','"Alpha2Code"','java.lang.String','com.gridnode.pdip.base.locale.model.LanguageCode','languageCode.alpha2Code',0,0,0,1,1,NULL,999,'displayable=false'||chr(13)||chr(10)||'editable=false'||chr(13)||chr(10)||'mandatory=false'||chr(13)||chr(10),'type=uid');
INSERT INTO "fieldmetainfo" VALUES (NULL,'_tAlpha3Code','T_ALPHA_3_CODE','"TAlpha3Code"','java.lang.String','com.gridnode.pdip.base.locale.model.LanguageCode','languageCode.talpha3Code',0,0,0,1,1,NULL,999,'displayable=false'||chr(13)||chr(10)||'editable=false'||chr(13)||chr(10)||'mandatory=false'||chr(13)||chr(10),'type=text');

--------- BpssReqBusinessActivity
DELETE FROM "fieldmetainfo" WHERE "EntityObjectName" LIKE '%BpssReqBusinessActivity';
INSERT INTO "fieldmetainfo" VALUES (NULL,'_uId','UID','"UID"','java.lang.Long','com.gridnode.pdip.base.gwfbase.bpss.model.BpssReqBusinessActivity','',0,0,0,1,1,'',999,'displayable=false','');
INSERT INTO "fieldmetainfo" VALUES (NULL,'_businessActionName','NAME','"Name"','java.lang.String','com.gridnode.pdip.base.gwfbase.bpss.model.BpssReqBusinessActivity','',50,0,0,1,1,'',999,'displayable=false','');
INSERT INTO "fieldmetainfo" VALUES (NULL,'_isIntelligibleChkReq','ISINTELLIGIBLE_REQUIRED','"IsIntelligibleChkReq"','java.lang.Boolean','com.gridnode.pdip.base.gwfbase.bpss.model.BpssReqBusinessActivity','',0,0,0,1,1,'',999,'displayable=false','');
INSERT INTO "fieldmetainfo" VALUES (NULL,'_isAuthReq','ISAUTH_REQUIRED','"IsAuthReq"','java.lang.Boolean','com.gridnode.pdip.base.gwfbase.bpss.model.BpssReqBusinessActivity','',0,0,0,1,1,'',999,'displayable=false','');
INSERT INTO "fieldmetainfo" VALUES (NULL,'_timeToAckReceipt','TIMETO_ACK_RECEIPT','"TimeToAckReceipt"','java.lang.String','com.gridnode.pdip.base.gwfbase.bpss.model.BpssReqBusinessActivity','',0,0,0,1,1,'',999,'displayable=false','');
INSERT INTO "fieldmetainfo" VALUES (NULL,'_isNonRepudiationReq','ISNONREPUDIATION_REQUIRED','"IsNonRepudiationReq"','java.lang.Boolean','com.gridnode.pdip.base.gwfbase.bpss.model.BpssReqBusinessActivity','',0,0,0,1,1,'',999,'displayable=false','');
INSERT INTO "fieldmetainfo" VALUES (NULL,'_isNonRepudiationOfReceiptReq','ISNONREPUDIATION_RECEIPT_REQUIRED','"IsNonRepudiationOfReceiptReq"','java.lang.Boolean','com.gridnode.pdip.base.gwfbase.bpss.model.BpssReqBusinessActivity','',0,0,0,1,1,'',999,'displayable=false','');
INSERT INTO "fieldmetainfo" VALUES (NULL,'_timeToAckAccept','TIMETO_ACK_ACCEPT','"TimeToAckAccept"','java.lang.String','com.gridnode.pdip.base.gwfbase.bpss.model.BpssReqBusinessActivity','',0,0,0,1,1,'',999,'displayable=false','');

--------- BpssResBusinessActivity
DELETE FROM "fieldmetainfo" WHERE "EntityObjectName" LIKE '%BpssResBusinessActivity';
INSERT INTO "fieldmetainfo" VALUES (NULL,'_uId','UID','"UID"','java.lang.Long','com.gridnode.pdip.base.gwfbase.bpss.model.BpssResBusinessActivity','',0,0,0,1,1,'',999,'displayable=false','');
INSERT INTO "fieldmetainfo" VALUES (NULL,'_businessActionName','NAME','"Name"','java.lang.String','com.gridnode.pdip.base.gwfbase.bpss.model.BpssResBusinessActivity','',50,0,0,1,1,'',999,'displayable=false','');
INSERT INTO "fieldmetainfo" VALUES (NULL,'_isIntelligibleChkReq','ISINTELLIGIBLE_REQUIRED','"IsIntelligibleChkReq"','java.lang.Boolean','com.gridnode.pdip.base.gwfbase.bpss.model.BpssResBusinessActivity','',0,0,0,1,1,'',999,'displayable=false','');
INSERT INTO "fieldmetainfo" VALUES (NULL,'_isAuthReq','ISAUTH_REQUIRED','"IsAuthReq"','java.lang.Boolean','com.gridnode.pdip.base.gwfbase.bpss.model.BpssResBusinessActivity','',0,0,0,1,1,'',999,'displayable=false','');
INSERT INTO "fieldmetainfo" VALUES (NULL,'_timeToAckReceipt','TIMETO_ACK_RECEIPT','"TimeToAckReceipt"','java.lang.String','com.gridnode.pdip.base.gwfbase.bpss.model.BpssResBusinessActivity','',0,0,0,1,1,'',999,'displayable=false','');
INSERT INTO "fieldmetainfo" VALUES (NULL,'_isNonRepudiationReq','ISNONREPUDIATION_REQUIRED','"IsNonRepudiationReq"','java.lang.Boolean','com.gridnode.pdip.base.gwfbase.bpss.model.BpssResBusinessActivity','',0,0,0,1,1,'',999,'displayable=false','');
INSERT INTO "fieldmetainfo" VALUES (NULL,'_isNonRepudiationOfReceiptReq','ISNONREPUDIATION_RECEIPT_REQUIRED','"IsNonRepudiationOfReceiptReq"','java.lang.Boolean','com.gridnode.pdip.base.gwfbase.bpss.model.BpssResBusinessActivity','',0,0,0,1,1,'',999,'displayable=false','');
 
--------- BpssBusinessTransActivity
DELETE FROM "fieldmetainfo" WHERE "EntityObjectName" LIKE '%BpssBusinessTransActivity';
INSERT INTO "fieldmetainfo" VALUES (NULL,'_uId','UID','"UID"','java.lang.Long','com.gridnode.pdip.base.gwfbase.bpss.model.BpssBusinessTransActivity','',0,0,0,1,1,'',999,'displayable=false','');
INSERT INTO "fieldmetainfo" VALUES (NULL,'_activityName','NAME','"Name"','java.lang.String','com.gridnode.pdip.base.gwfbase.bpss.model.BpssBusinessTransActivity','',50,0,0,1,1,'',999,'displayable=false','');
INSERT INTO "fieldmetainfo" VALUES (NULL,'_timeToPerform','TIMETO_PERFORM','"TimeToPerform"','java.lang.String','com.gridnode.pdip.base.gwfbase.bpss.model.BpssBusinessTransActivity','',0,0,0,1,1,'',999,'displayable=false','');
INSERT INTO "fieldmetainfo" VALUES (NULL,'_isConcurrent','ISCONCURRENT','"IsConcurrent"','java.lang.Boolean','com.gridnode.pdip.base.gwfbase.bpss.model.BpssBusinessTransActivity','',0,0,0,1,1,'',999,'displayable=false','');
INSERT INTO "fieldmetainfo" VALUES (NULL,'_isLegallyBinding','ISLEGALLY_BINDING','"IsLegallyBinding"','java.lang.Boolean','com.gridnode.pdip.base.gwfbase.bpss.model.BpssBusinessTransActivity','',0,0,0,1,1,'',999,'displayable=false','');
INSERT INTO "fieldmetainfo" VALUES (NULL,'_businessTransUId','BUSINESS_TRANS_UID','"BusinessTransUId"','java.lang.Long','com.gridnode.pdip.base.gwfbase.bpss.model.BpssBusinessTransActivity','',0,0,0,1,1,'',999,'displayable=false','');
INSERT INTO "fieldmetainfo" VALUES (NULL,'_fromAuthorizedRole','FROM_AUTHORIZED_ROLE','"FromAuthorizedRole"','java.lang.String','com.gridnode.pdip.base.gwfbase.bpss.model.BpssBusinessTransActivity','',50,0,0,1,1,'',999,'displayable=false','');
INSERT INTO "fieldmetainfo" VALUES (NULL,'_toAuthorizedRole','TO_AUTHORIZED_ROLE','"ToAuthorizedRole"','java.lang.String','com.gridnode.pdip.base.gwfbase.bpss.model.BpssBusinessTransActivity','',50,0,0,1,1,'',999,'displayable=false','');

--------- BpssCollaborationActivity
DELETE FROM "fieldmetainfo" WHERE "EntityObjectName" LIKE '%BpssCollaborationActivity';
INSERT INTO "fieldmetainfo" VALUES (NULL,'_uId','UID','"UID"','java.lang.Long','com.gridnode.pdip.base.gwfbase.bpss.model.BpssCollaborationActivity','',0,0,0,1,1,'',999,'displayable=false','');
INSERT INTO "fieldmetainfo" VALUES (NULL,'_activityName','NAME','"Name"','java.lang.String','com.gridnode.pdip.base.gwfbase.bpss.model.BpssCollaborationActivity','',50,0,0,1,1,'',999,'displayable=false','');
INSERT INTO "fieldmetainfo" VALUES (NULL,'_binaryCollaborationUId','BINARY_COLLABORATION_UID','"BinCollProcessUId"','java.lang.Long','com.gridnode.pdip.base.gwfbase.bpss.model.BpssCollaborationActivity','',0,0,0,1,1,'',999,'displayable=false','');

--------- BpssBusinessTrans
DELETE FROM "fieldmetainfo" WHERE "EntityObjectName" LIKE '%BpssBusinessTrans';
INSERT INTO "fieldmetainfo" VALUES (NULL,'_uId','UID','"UID"','java.lang.Long','com.gridnode.pdip.base.gwfbase.bpss.model.BpssBusinessTrans','',0,0,0,1,1,'',999,'displayable=false','');
INSERT INTO "fieldmetainfo" VALUES (NULL,'_businessTransName','NAME','"Name"','java.lang.String','com.gridnode.pdip.base.gwfbase.bpss.model.BpssBusinessTrans','',50,0,0,1,1,'',999,'displayable=false','');
INSERT INTO "fieldmetainfo" VALUES (NULL,'_pattern','PATTERN','"Pattern"','java.lang.String','com.gridnode.pdip.base.gwfbase.bpss.model.BpssBusinessTrans','',50,0,0,1,1,'',999,'displayable=false','');
INSERT INTO "fieldmetainfo" VALUES (NULL,'_preCondition','PRE_CONDITION','"PreCondition"','java.lang.String','com.gridnode.pdip.base.gwfbase.bpss.model.BpssBusinessTrans','',50,0,0,1,1,'',999,'displayable=false','');
INSERT INTO "fieldmetainfo" VALUES (NULL,'_postCondition','POST_CONDITION','"PostCondition"','java.lang.String','com.gridnode.pdip.base.gwfbase.bpss.model.BpssBusinessTrans','',50,0,0,1,1,'',999,'displayable=false','');
INSERT INTO "fieldmetainfo" VALUES (NULL,'_isGuaranteedDeliveryRequired','ISGUARANTEED_DELIVERY_REQUIRED','"IsDeliveryReq"','java.lang.Boolean','com.gridnode.pdip.base.gwfbase.bpss.model.BpssBusinessTrans','',0,0,0,1,1,'',999,'displayable=false','');
INSERT INTO "fieldmetainfo" VALUES (NULL,'_beginsWhen','BEGINS_WHEN','"BeginsWhen"','java.lang.String','com.gridnode.pdip.base.gwfbase.bpss.model.BpssBusinessTrans','',50,0,0,1,1,'',999,'displayable=false','');
INSERT INTO "fieldmetainfo" VALUES (NULL,'_endsWhen','ENDS_WHEN','"EndsWhen"','java.lang.String','com.gridnode.pdip.base.gwfbase.bpss.model.BpssBusinessTrans','',50,0,0,1,1,'',999,'displayable=false','');
INSERT INTO "fieldmetainfo" VALUES (NULL,'_bpssReqBusinessActivity','BPSS_REQ_ACTIVITY','"ReqUId"','java.lang.Long','com.gridnode.pdip.base.gwfbase.bpss.model.BpssBusinessTrans','',0,0,0,1,1,'',999,'displayable=false','');
INSERT INTO "fieldmetainfo" VALUES (NULL,'_bpssResBusinessActivity','BPSS_RES_ACTIVITY','"ResUId"','java.lang.Long','com.gridnode.pdip.base.gwfbase.bpss.model.BpssBusinessTrans','',0,0,0,1,1,'',999,'displayable=false','');

--------- BpssBinaryCollaboration
DELETE FROM "fieldmetainfo" WHERE "EntityObjectName" LIKE '%BpssBinaryCollaboration';
INSERT INTO "fieldmetainfo" VALUES (NULL,'_uId','UID','"UID"','java.lang.Long','com.gridnode.pdip.base.gwfbase.bpss.model.BpssBinaryCollaboration','',0,0,0,1,1,'',999,'displayable=false','');
INSERT INTO "fieldmetainfo" VALUES (NULL,'_binaryCollaborationName','NAME','"Name"','java.lang.String','com.gridnode.pdip.base.gwfbase.bpss.model.BpssBinaryCollaboration','',50,0,0,1,1,'',999,'displayable=false','');
INSERT INTO "fieldmetainfo" VALUES (NULL,'_pattern','PATTERN','"Pattern"','java.lang.String','com.gridnode.pdip.base.gwfbase.bpss.model.BpssBinaryCollaboration','',50,0,0,1,1,'',999,'displayable=false','');
INSERT INTO "fieldmetainfo" VALUES (NULL,'_timeToPerform','TIMETO_PERFORM','"TimeToPerform"','java.lang.String','com.gridnode.pdip.base.gwfbase.bpss.model.BpssBinaryCollaboration','',0,0,0,1,1,'',999,'displayable=false','');
INSERT INTO "fieldmetainfo" VALUES (NULL,'_preCondition','PRE_CONDITION','"PreCondition"','java.lang.String','com.gridnode.pdip.base.gwfbase.bpss.model.BpssBinaryCollaboration','',50,0,0,1,1,'',999,'displayable=false','');
INSERT INTO "fieldmetainfo" VALUES (NULL,'_postCondition','POST_CONDITION','"PostCondition"','java.lang.String','com.gridnode.pdip.base.gwfbase.bpss.model.BpssBinaryCollaboration','',50,0,0,1,1,'',999,'displayable=false','');
INSERT INTO "fieldmetainfo" VALUES (NULL,'_beginsWhen','BEGINS_WHEN','"BeginsWhen"','java.lang.String','com.gridnode.pdip.base.gwfbase.bpss.model.BpssBinaryCollaboration','',50,0,0,1,1,'',999,'displayable=false','');
INSERT INTO "fieldmetainfo" VALUES (NULL,'_endsWhen','ENDS_WHEN','"EndsWhen"','java.lang.String','com.gridnode.pdip.base.gwfbase.bpss.model.BpssBinaryCollaboration','',50,0,0,1,1,'',999,'displayable=false','');

--------- BpssMultiPartyCollaboration
DELETE FROM "fieldmetainfo" WHERE "EntityObjectName" LIKE '%BpssMultiPartyCollaboration';
INSERT INTO "fieldmetainfo" VALUES (NULL,'_uId','UID','"UID"','java.lang.Long','com.gridnode.pdip.base.gwfbase.bpss.model.BpssMultiPartyCollaboration','',0,0,0,1,1,'',999,'displayable=false','');
INSERT INTO "fieldmetainfo" VALUES (NULL,'_binaryCollaborationName','NAME','"Name"','java.lang.String','com.gridnode.pdip.base.gwfbase.bpss.model.BpssMultiPartyCollaboration','',50,0,0,1,1,'',999,'displayable=false','');

--------- BpssTransition
DELETE FROM "fieldmetainfo" WHERE "EntityObjectName" LIKE '%BpssTransition';
INSERT INTO "fieldmetainfo" VALUES (NULL,'_uId','UID','"UID"','java.lang.Long','com.gridnode.pdip.base.gwfbase.bpss.model.BpssTransition','',0,0,0,1,1,'',999,'displayable=false','');
INSERT INTO "fieldmetainfo" VALUES (NULL,'_processUId','PROCESS_UID','"ProcessUId"','java.lang.Long','com.gridnode.pdip.base.gwfbase.bpss.model.BpssTransition','',0,0,0,1,1,'',999,'displayable=false','');
INSERT INTO "fieldmetainfo" VALUES (NULL,'_onInitiation','ON_INITIATION','"OnInitiation"','java.lang.Boolean','com.gridnode.pdip.base.gwfbase.bpss.model.BpssTransition','',0,0,0,1,1,'',999,'displayable=false','');
INSERT INTO "fieldmetainfo" VALUES (NULL,'_conditionGuard','CONDITION_GUARD','"ConditionGuard"','java.lang.String','com.gridnode.pdip.base.gwfbase.bpss.model.BpssTransition','',50,0,0,1,1,'',999,'displayable=false','');
INSERT INTO "fieldmetainfo" VALUES (NULL,'_fromBusinessStateKey','FROM_BUSINESS_STATE_KEY','"FromBusinessStateKey"','java.lang.String','com.gridnode.pdip.base.gwfbase.bpss.model.BpssTransition','',50,0,0,1,1,'',999,'displayable=false','');
INSERT INTO "fieldmetainfo" VALUES (NULL,'_toBusinessStateKey','TO_BUSINESS_STATE_KEY','"ToBusinessStateKey"','java.lang.String','com.gridnode.pdip.base.gwfbase.bpss.model.BpssTransition','',50,0,0,1,1,'',999,'displayable=false','');
INSERT INTO "fieldmetainfo" VALUES (NULL,'_processType','PROCESS_TYPE','"ProcessType"','java.lang.String','com.gridnode.pdip.base.gwfbase.bpss.model.BpssTransition','',50,0,0,1,1,'',999,'displayable=false','');
INSERT INTO "fieldmetainfo" VALUES (NULL,'_conditionExpression','CONDITION_EXPRESSION','"ConditionExpression"','java.lang.String','com.gridnode.pdip.base.gwfbase.bpss.model.BpssTransition','',50,0,0,1,1,'',999,'displayable=false','');
INSERT INTO "fieldmetainfo" VALUES (NULL,'_expressionLanguage','EXPRESSION_LANGUAGE','"ExpressionLanguage"','java.lang.String','com.gridnode.pdip.base.gwfbase.bpss.model.BpssTransition','',50,0,0,1,1,'',999,'displayable=false','');

--------- BpssCompletionState
DELETE FROM "fieldmetainfo" WHERE "EntityObjectName" LIKE '%BpssCompletionState';
INSERT INTO "fieldmetainfo" VALUES (NULL,'_uId','UID','"UID"','java.lang.Long','com.gridnode.pdip.base.gwfbase.bpss.model.BpssCompletionState','',0,0,0,1,1,'',999,'displayable=false','');
INSERT INTO "fieldmetainfo" VALUES (NULL,'_processUId','PROCESS_UID','"ProcessUId"','java.lang.Long','com.gridnode.pdip.base.gwfbase.bpss.model.BpssCompletionState','',0,0,0,1,1,'',999,'displayable=false','');
INSERT INTO "fieldmetainfo" VALUES (NULL,'_processType','PROCESS_TYPE','"ProcessType"','java.lang.String','com.gridnode.pdip.base.gwfbase.bpss.model.BpssCompletionState','',50,0,0,1,1,'',999,'displayable=false','');
INSERT INTO "fieldmetainfo" VALUES (NULL,'_mpcUId','MPC_UID','"MpcUId"','java.lang.Long','com.gridnode.pdip.base.gwfbase.bpss.model.BpssCompletionState','',0,0,0,1,1,'',999,'displayable=false','');
INSERT INTO "fieldmetainfo" VALUES (NULL,'_conditionGuard','CONDITION_GUARD','"ConditionGuard"','java.lang.String','com.gridnode.pdip.base.gwfbase.bpss.model.BpssCompletionState','',50,0,0,1,1,'',999,'displayable=false','');
INSERT INTO "fieldmetainfo" VALUES (NULL,'_fromBusinessStateKey','FROM_BUSINESS_STATE_KEY','"FromBusinessStateKey"','java.lang.String','com.gridnode.pdip.base.gwfbase.bpss.model.BpssCompletionState','',50,0,0,1,1,'',999,'displayable=false','');
INSERT INTO "fieldmetainfo" VALUES (NULL,'_conditionExpression','CONDITION_EXPRESSION','"ConditionExpression"','java.lang.String','com.gridnode.pdip.base.gwfbase.bpss.model.BpssCompletionState','',50,0,0,1,1,'',999,'displayable=false','');
INSERT INTO "fieldmetainfo" VALUES (NULL,'_expressionLanguage','EXPRESSION_LANGUAGE','"ExpressionLanguage"','java.lang.String','com.gridnode.pdip.base.gwfbase.bpss.model.BpssCompletionState','',50,0,0,1,1,'',999,'displayable=false','');
INSERT INTO "fieldmetainfo" VALUES (NULL,'_completionType','COMPLETION_TYPE','"CompletionType"','java.lang.String','com.gridnode.pdip.base.gwfbase.bpss.model.BpssCompletionState','',50,0,0,1,1,'',999,'displayable=false','');

--------- BpssFork
DELETE FROM "fieldmetainfo" WHERE "EntityObjectName" LIKE '%BpssFork';
INSERT INTO "fieldmetainfo" VALUES (NULL,'_uId','UID','"UID"','java.lang.Long','com.gridnode.pdip.base.gwfbase.bpss.model.BpssFork','',0,0,0,1,1,'',999,'displayable=false','');
INSERT INTO "fieldmetainfo" VALUES (NULL,'_name','NAME','"Name"','java.lang.String','com.gridnode.pdip.base.gwfbase.bpss.model.BpssFork','',50,0,0,1,1,'',999,'displayable=false','');

--------- BpssJoin
DELETE FROM "fieldmetainfo" WHERE "EntityObjectName" LIKE '%BpssJoin';
INSERT INTO "fieldmetainfo" VALUES (NULL,'_uId','UID','"UID"','java.lang.Long','com.gridnode.pdip.base.gwfbase.bpss.model.BpssJoin','',0,0,0,1,1,'',999,'displayable=false','');
INSERT INTO "fieldmetainfo" VALUES (NULL,'_name','NAME','"Name"','java.lang.String','com.gridnode.pdip.base.gwfbase.bpss.model.BpssJoin','',50,0,0,1,1,'',999,'displayable=false','');
INSERT INTO "fieldmetainfo" VALUES (NULL,'_waitForAll','WAITFOR_ALL','"WaitForAll"','java.lang.Boolean','com.gridnode.pdip.base.gwfbase.bpss.model.BpssJoin','',0,0,0,1,1,'',999,'displayable=false','');

--------- MaxConcurrency
DELETE FROM "fieldmetainfo" WHERE "EntityObjectName" LIKE '%MaxConcurrency';
INSERT INTO "fieldmetainfo" VALUES (NULL,'_maxConcurrency','MAX_CONCURRENCY','"MaxConcurrency"','java.lang.Integer','com.gridnode.pdip.base.gwfbase.bpss.model.BpssMultiPartyCollaboration','',0,0,0,1,1,'',999,'displayable=false','');
INSERT INTO "fieldmetainfo" VALUES (NULL,'_maxConcurrency','MAX_CONCURRENCY','"MaxConcurrency"','java.lang.Integer','com.gridnode.pdip.base.gwfbase.bpss.model.BpssBinaryCollaboration','',0,0,0,1,1,'',999,'displayable=false','');
INSERT INTO "fieldmetainfo" VALUES (NULL,'_maxConcurrency','MAX_CONCURRENCY','"MaxConcurrency"','java.lang.Integer','com.gridnode.pdip.base.gwfbase.bpss.model.BpssBusinessTrans','',0,0,0,1,1,'',999,'displayable=false','');

--------- BpssProcessSpecEntry
DELETE FROM "fieldmetainfo" WHERE "EntityObjectName" LIKE '%BpssProcessSpecEntry';
INSERT INTO "fieldmetainfo" VALUES (NULL,'_uId','UID','"UID"','java.lang.Long','com.gridnode.pdip.base.gwfbase.bpss.model.BpssProcessSpecEntry','',0,0,0,1,1,'',999,'displayable=false','');
INSERT INTO "fieldmetainfo" VALUES (NULL,'_specUId','SPEC_UID','"SpecUId"','java.lang.Long','com.gridnode.pdip.base.gwfbase.bpss.model.BpssProcessSpecEntry','',0,0,0,1,1,'',999,'displayable=false','');
INSERT INTO "fieldmetainfo" VALUES (NULL,'_entryUId','ENTRY_UID','"EntryUId"','java.lang.Long','com.gridnode.pdip.base.gwfbase.bpss.model.BpssProcessSpecEntry','',0,0,0,1,1,'',999,'displayable=false','');
INSERT INTO "fieldmetainfo" VALUES (NULL,'_entryName','ENTRY_NAME','"Name"','java.lang.String','com.gridnode.pdip.base.gwfbase.bpss.model.BpssProcessSpecEntry','',0,0,0,1,1,'',999,'displayable=false','');
INSERT INTO "fieldmetainfo" VALUES (NULL,'_entryType','ENTRY_TYPE','"Type"','java.lang.String','com.gridnode.pdip.base.gwfbase.bpss.model.BpssProcessSpecEntry','',0,0,0,1,1,'',999,'displayable=false','');
INSERT INTO "fieldmetainfo" VALUES (NULL,'_parentEntryUId','PARENT_ENTRY_UID','"ParentEntryUId"','java.lang.Long','com.gridnode.pdip.base.gwfbase.bpss.model.BpssProcessSpecEntry','',0,0,0,1,1,'',999,'displayable=false','');

--------- BpssProcessSpecification
DELETE FROM "fieldmetainfo" WHERE "EntityObjectName" LIKE '%BpssProcessSpecification';
INSERT INTO "fieldmetainfo" VALUES (NULL,'_uId','UID','"UID"','java.lang.Long','com.gridnode.pdip.base.gwfbase.bpss.model.BpssProcessSpecification','',0,0,0,1,1,'',999,'displayable=false','');
INSERT INTO "fieldmetainfo" VALUES (NULL,'_specVersion','VERSION','"Version"','java.lang.String','com.gridnode.pdip.base.gwfbase.bpss.model.BpssProcessSpecification','',0,0,0,1,1,'',999,'displayable=false','');
INSERT INTO "fieldmetainfo" VALUES (NULL,'_specUUId','UUID','"UUId"','java.lang.String','com.gridnode.pdip.base.gwfbase.bpss.model.BpssProcessSpecification','',0,0,0,1,1,'',999,'displayable=false','');
INSERT INTO "fieldmetainfo" VALUES (NULL,'_specName','NAME','"Name"','java.lang.String','com.gridnode.pdip.base.gwfbase.bpss.model.BpssProcessSpecification','',0,0,0,1,1,'',999,'displayable=false','');
INSERT INTO "fieldmetainfo" VALUES (NULL,'_specTimestamp','TIMESTAMP','"Timestamp"','java.lang.String','com.gridnode.pdip.base.gwfbase.bpss.model.BpssProcessSpecification','',0,0,0,1,1,'',999,'displayable=false','');

--------- BpssStart
DELETE FROM "fieldmetainfo" WHERE "EntityObjectName" LIKE '%BpssStart';
INSERT INTO "fieldmetainfo" VALUES (NULL,'_uId','UID','"UID"','java.lang.Long','com.gridnode.pdip.base.gwfbase.bpss.model.BpssStart','',0,0,0,1,1,'',999,'displayable=false','');
INSERT INTO "fieldmetainfo" VALUES (NULL,'_processUId','PROCESS_UID','"ProcessUId"','java.lang.Long','com.gridnode.pdip.base.gwfbase.bpss.model.BpssStart','',0,0,0,1,1,'',999,'displayable=false','');
INSERT INTO "fieldmetainfo" VALUES (NULL,'_isDownLink','ISDOWNLINK','"IsDownLink"','java.lang.Boolean','com.gridnode.pdip.base.gwfbase.bpss.model.BpssStart','',0,0,0,1,1,'',999,'displayable=false','');
INSERT INTO "fieldmetainfo" VALUES (NULL,'_toBusinessStateKey','TO_BUSINESS_STATE_KEY','"ToBusinessStateKey"','java.lang.String','com.gridnode.pdip.base.gwfbase.bpss.model.BpssStart','',50,0,0,1,1,'',999,'displayable=false','');

--------- BpssBinaryCollaborationActivity
DELETE FROM "fieldmetainfo" WHERE "EntityObjectName" LIKE '%BpssBinaryCollaborationActivity';
INSERT INTO "fieldmetainfo" VALUES (NULL,'_uId','UID','"UID"','java.lang.Long','com.gridnode.pdip.base.gwfbase.bpss.model.BpssBinaryCollaborationActivity','',0,0,0,1,1,'',999,'displayable=false','');
INSERT INTO "fieldmetainfo" VALUES (NULL,'_activityName','NAME','"Name"','java.lang.String','com.gridnode.pdip.base.gwfbase.bpss.model.BpssBinaryCollaborationActivity','',50,0,0,1,1,'',999,'displayable=false','');
INSERT INTO "fieldmetainfo" VALUES (NULL,'_binaryCollaborationUId','BINARY_COLLABORATION_UID','"BinCollProcessUId"','java.lang.Long','com.gridnode.pdip.base.gwfbase.bpss.model.BpssBinaryCollaborationActivity','',0,0,0,1,1,'',999,'displayable=false','');
INSERT INTO "fieldmetainfo" VALUES (NULL,'_downLinkUId','DOWNLINK_UID','"DownLinkUId"','java.lang.Long','com.gridnode.pdip.base.gwfbase.bpss.model.BpssBinaryCollaborationActivity','',0,0,0,1,1,'',999,'displayable=false','');

--------- BpssBusinessPartnerRole
DELETE FROM "fieldmetainfo" WHERE "EntityObjectName" LIKE '%BpssBusinessPartnerRole';
INSERT INTO "fieldmetainfo" VALUES (NULL,'_uId','UID','"UID"','java.lang.Long','com.gridnode.pdip.base.gwfbase.bpss.model.BpssBusinessPartnerRole','',0,0,0,1,1,'',999,'displayable=false','');
INSERT INTO "fieldmetainfo" VALUES (NULL,'_roleName','NAME','"Name"','java.lang.String','com.gridnode.pdip.base.gwfbase.bpss.model.BpssBusinessPartnerRole','',50,0,0,1,1,'',999,'displayable=false','');

--------- BpssBusinessDocument
DELETE FROM "fieldmetainfo" WHERE "EntityObjectName" LIKE '%BpssBusinessDocument';
INSERT INTO "fieldmetainfo" VALUES(NULL,'_uId','UID','"UID"','java.lang.Long','com.gridnode.pdip.base.gwfbase.bpss.model.BpssBusinessDocument','',0,0,0,0,0,'',999,'displayable=false','');
INSERT INTO "fieldmetainfo" VALUES(NULL,'_name','NAME','"Name"','java.lang.String','com.gridnode.pdip.base.gwfbase.bpss.model.BpssBusinessDocument','NAME',50,0,1,1,1,'',1,'displayable=false','');
INSERT INTO "fieldmetainfo" VALUES(NULL,'_expressionLanguage','EXPRESSION_LANGUAGE','"ExpressionLanguage"','java.lang.String','com.gridnode.pdip.base.gwfbase.bpss.model.BpssBusinessDocument','EXPRESSION_LANGUAGE',50,0,0,1,1,'',999,'displayable=false','');
INSERT INTO "fieldmetainfo" VALUES(NULL,'_conditionExpr','CONDITION_EXPR','"ConditionExpr"','java.lang.String','com.gridnode.pdip.base.gwfbase.bpss.model.BpssBusinessDocument','CONDITION_EXPR',50,0,1,1,1,'',2,'displayable=false','');
INSERT INTO "fieldmetainfo" VALUES(NULL,'_specElement','SPEC_ELEMENT','"SpecElement"','java.lang.String','com.gridnode.pdip.base.gwfbase.bpss.model.BpssBusinessDocument','SPEC_ELEMENT',50,0,1,1,1,'',3,'displayable=false','');
INSERT INTO "fieldmetainfo" VALUES(NULL,'_specLocation','SPEC_LOCATION','"SpecLocation"','java.lang.String','com.gridnode.pdip.base.gwfbase.bpss.model.BpssBusinessDocument','SPEC_LOCATION',50,0,1,1,1,'',4,'displayable=false','');

--------- BpssDocumentation
DELETE FROM "fieldmetainfo" WHERE "EntityObjectName" LIKE '%BpssDocumentation';
INSERT INTO "fieldmetainfo" VALUES(NULL,'_uId','UID','"UID"','java.lang.Long','com.gridnode.pdip.base.gwfbase.bpss.model.BpssDocumentation','',0,0,0,0,0,'',999,'displayable=false','');
INSERT INTO "fieldmetainfo" VALUES(NULL,'_uri','URI','"Uri"','java.lang.String','com.gridnode.pdip.base.gwfbase.bpss.model.BpssDocumentation','URI',50,0,1,1,1,'',1,'displayable=false','');
INSERT INTO "fieldmetainfo" VALUES(NULL,'_documentation','DOCUMENTATION','"Documentation"','java.lang.String','com.gridnode.pdip.base.gwfbase.bpss.model.BpssDocumentation','NAME',80,0,1,1,1,'',1,'displayable=false','');

--------- BpssDocumentEnvelope
DELETE FROM "fieldmetainfo" WHERE "EntityObjectName" LIKE '%BpssDocumentEnvelope';
INSERT INTO "fieldmetainfo" VALUES(NULL,'_uId','UID','"UID"','java.lang.Long','com.gridnode.pdip.base.gwfbase.bpss.model.BpssDocumentEnvelope','',0,0,0,0,0,'',999,'displayable=false','');
INSERT INTO "fieldmetainfo" VALUES(NULL,'_businessDocumentName','BUSINESS_DOCUMENT_NAME','"BusinessDocumentName"','java.lang.String','com.gridnode.pdip.base.gwfbase.bpss.model.BpssDocumentEnvelope','BUSINESS_DOCUMENT_NAME',50,0,1,1,1,'',1,'displayable=false','');
INSERT INTO "fieldmetainfo" VALUES(NULL,'_businessDocumentIDRef','BUSINESS_DOCUMENTID_REF','"BusinessDocumentIDRef"','java.lang.String','com.gridnode.pdip.base.gwfbase.bpss.model.BpssDocumentEnvelope','BUSINESS_DOCUMENTID_REF',50,0,1,1,1,'',1,'displayable=false','');
INSERT INTO "fieldmetainfo" VALUES (NULL,'_isPositiveResponse','ISPOSITIVE_RESPONSE','"IsPositiveResponse"','java.lang.Boolean','com.gridnode.pdip.base.gwfbase.bpss.model.BpssDocumentEnvelope','',0,0,0,1,1,'',999,'displayable=false','');
INSERT INTO "fieldmetainfo" VALUES (NULL,'_isAuthenticated','ISAUTHENTICATED','"IsAuthenticated"','java.lang.Boolean','com.gridnode.pdip.base.gwfbase.bpss.model.BpssDocumentEnvelope','',0,0,0,1,1,'',999,'displayable=false','');
INSERT INTO "fieldmetainfo" VALUES (NULL,'_isConfidential','ISCONFIDENTIAL','"IsConfidential"','java.lang.Boolean','com.gridnode.pdip.base.gwfbase.bpss.model.BpssDocumentEnvelope','',0,0,0,1,1,'',999,'displayable=false','');
INSERT INTO "fieldmetainfo" VALUES (NULL,'_isTamperProof','ISTAMPERPROOF','"IsTamperProof"','java.lang.Boolean','com.gridnode.pdip.base.gwfbase.bpss.model.BpssDocumentEnvelope','',0,0,0,1,1,'',999,'displayable=false','');

--------- XpdlActivity
DELETE FROM "fieldmetainfo" WHERE "EntityObjectName" LIKE '%XpdlActivity';
INSERT INTO "fieldmetainfo" VALUES(NULL,'_uId','UID','"UID"','java.lang.Long','com.gridnode.pdip.base.gwfbase.xpdl.model.XpdlActivity','',0,0,0,0,0,'',999,'displayable=false','');
INSERT INTO "fieldmetainfo" VALUES(NULL,'_activityId','ACTIVITY_ID','"ActivityId"','java.lang.String','com.gridnode.pdip.base.gwfbase.xpdl.model.XpdlActivity','ACTIVITY_ID',50,0,1,1,1,'',1,'displayable=false','');
INSERT INTO "fieldmetainfo" VALUES(NULL,'_activityName','ACTIVITY_NAME','"ActivityName"','java.lang.String','com.gridnode.pdip.base.gwfbase.xpdl.model.XpdlActivity','ACTIVITY_NAME',50,0,1,1,1,'',2,'displayable=false','');
INSERT INTO "fieldmetainfo" VALUES(NULL,'_activityDescription','ACTIVITY_DESCRIPTION','"ActivityDescription"','java.lang.String','com.gridnode.pdip.base.gwfbase.xpdl.model.XpdlActivity','ACTIVITY_DESCRIPTION',50,0,1,1,1,'',3,'displayable=false','');
INSERT INTO "fieldmetainfo" VALUES(NULL,'_extendedAttributes','EXTENDED_ATTRIBUTES','"ExtendedAttributes"','java.lang.String','com.gridnode.pdip.base.gwfbase.xpdl.model.XpdlActivity','EXTENDED_ATTRIBUTES',50,0,1,1,1,'',5,'displayable=false','');
INSERT INTO "fieldmetainfo" VALUES(NULL,'_activityLimit','ACTIVITY_LIMIT','"ActivityLimit"','java.lang.Double','com.gridnode.pdip.base.gwfbase.xpdl.model.XpdlActivity','ACTIVITY_LIMIT',0,0,0,0,0,'',5,'displayable=false','');
INSERT INTO "fieldmetainfo" VALUES(NULL,'_isRoute','IS_ROUTE','"IsRoute"','java.lang.Boolean','com.gridnode.pdip.base.gwfbase.xpdl.model.XpdlActivity','IS_ROUTE',0,0,0,1,1,'',6,'displayable=false','');
INSERT INTO "fieldmetainfo" VALUES(NULL,'_implementationType','IMPLEMENTATION_TYPE','"ImplementationType"','java.lang.String','com.gridnode.pdip.base.gwfbase.xpdl.model.XpdlActivity','IMPLEMENTATION_TYPE',50,0,1,1,1,'',7,'displayable=false','');
INSERT INTO "fieldmetainfo" VALUES(NULL,'_performerId','PERFORMER_ID','"PerformerId"','java.lang.String','com.gridnode.pdip.base.gwfbase.xpdl.model.XpdlActivity','PERFORMER_ID',50,0,1,1,1,'',8,'displayable=false','');
INSERT INTO "fieldmetainfo" VALUES(NULL,'_startMode','START_MODE','"StartMode"','java.lang.String','com.gridnode.pdip.base.gwfbase.xpdl.model.XpdlActivity','START_MODE',50,0,1,1,1,'',9,'displayable=false','');
INSERT INTO "fieldmetainfo" VALUES(NULL,'_finishMode','FINISH_MODE','"FinishMode"','java.lang.String','com.gridnode.pdip.base.gwfbase.xpdl.model.XpdlActivity','FINISH_MODE',50,0,1,1,1,'',10,'displayable=false','');
INSERT INTO "fieldmetainfo" VALUES(NULL,'_priority','PRIORITY','"Priority"','java.lang.Long','com.gridnode.pdip.base.gwfbase.xpdl.model.XpdlActivity','PRIORITY',0,0,0,0,0,'',11,'displayable=false','');
INSERT INTO "fieldmetainfo" VALUES(NULL,'_instantiation','INSTANTIATION','"Instantiation"','java.lang.String','com.gridnode.pdip.base.gwfbase.xpdl.model.XpdlActivity','INSTANTIATION',50,0,1,1,1,'',12,'displayable=false','');
INSERT INTO "fieldmetainfo" VALUES(NULL,'_cost','COST','"Cost"','java.lang.Double','com.gridnode.pdip.base.gwfbase.xpdl.model.XpdlActivity','COST',0,0,0,0,0,'',13,'displayable=false','');
INSERT INTO "fieldmetainfo" VALUES(NULL,'_waitingTime','WAITING_TIME','"WaitingTime"','java.lang.Double','com.gridnode.pdip.base.gwfbase.xpdl.model.XpdlActivity','WAITING_TIME',0,0,0,0,0,'',14,'displayable=false','');
INSERT INTO "fieldmetainfo" VALUES(NULL,'_duration','DURATION','"Duration"','java.lang.Double','com.gridnode.pdip.base.gwfbase.xpdl.model.XpdlActivity','DURATION',0,0,0,0,0,'',15,'displayable=false','');
INSERT INTO "fieldmetainfo" VALUES(NULL,'_iconUrl','ICON_URL','"IconUrl"','java.lang.String','com.gridnode.pdip.base.gwfbase.xpdl.model.XpdlActivity','ICON_URL',50,0,1,1,1,'',16,'displayable=false','');
INSERT INTO "fieldmetainfo" VALUES(NULL,'_documentationUrl','DOCUMENTATION_URL','"DocumentationUrl"','java.lang.String','com.gridnode.pdip.base.gwfbase.xpdl.model.XpdlActivity','DOCUMENTATION_URL',50,0,1,1,1,'',17,'displayable=false','');
INSERT INTO "fieldmetainfo" VALUES(NULL,'_transitionRestrictionListUId','TRANSITION_RESTRICTION_LIST_UID','"TransitionRestrictionListUId"','java.lang.Long','com.gridnode.pdip.base.gwfbase.xpdl.model.XpdlActivity','TRANSITION_RESTRICTION_LIST_UID',0,0,0,0,0,'',18,'displayable=false','');
INSERT INTO "fieldmetainfo" VALUES(NULL,'_workingTime','WORKING_TIME','"WorkingTime"','java.lang.Double','com.gridnode.pdip.base.gwfbase.xpdl.model.XpdlActivity','WORKING_TIME',0,0,0,0,0,'',19,'displayable=false','');
INSERT INTO "fieldmetainfo" VALUES(NULL,'_processId','PROCESS_ID','"ProcessId"','java.lang.String','com.gridnode.pdip.base.gwfbase.xpdl.model.XpdlActivity','PROCESS_ID',50,0,1,1,1,'',20,'displayable=false','');
INSERT INTO "fieldmetainfo" VALUES(NULL,'_packageId','PACKAGE_ID','"PackageId"','java.lang.String','com.gridnode.pdip.base.gwfbase.xpdl.model.XpdlActivity','PACKAGE_ID',50,0,1,1,1,'',21,'displayable=false','');
INSERT INTO "fieldmetainfo" VALUES(NULL,'_pkgVersionId','PKG_VERSION_ID','"PkgVersionId"','java.lang.String','com.gridnode.pdip.base.gwfbase.xpdl.model.XpdlActivity','PKG_VERSION_ID',50,0,1,1,1,'',22,'displayable=false','');

--------- XpdlApplication
DELETE FROM "fieldmetainfo" WHERE "EntityObjectName" LIKE '%XpdlApplication';
INSERT INTO "fieldmetainfo" VALUES(NULL,'_uId','UID','"UID"','java.lang.Long','com.gridnode.pdip.base.gwfbase.xpdl.model.XpdlApplication','',0,0,0,0,0,'',999,'displayable=false','');
INSERT INTO "fieldmetainfo" VALUES(NULL,'_applicationId','APPLICATION_ID','"ApplicationId"','java.lang.String','com.gridnode.pdip.base.gwfbase.xpdl.model.XpdlApplication','APPLICATION_ID',50,0,1,1,1,'',1,'displayable=false','');
INSERT INTO "fieldmetainfo" VALUES(NULL,'_applicationName','APPLICATION_NAME','"ApplicationName"','java.lang.String','com.gridnode.pdip.base.gwfbase.xpdl.model.XpdlApplication','APPLICATION_NAME',50,0,1,1,1,'',2,'displayable=false','');
INSERT INTO "fieldmetainfo" VALUES(NULL,'_applicationDescription','APPLICATION_DESCRIPTION','"ApplicationDescription"','java.lang.String','com.gridnode.pdip.base.gwfbase.xpdl.model.XpdlApplication','APPLICATION_DESCRIPTION',50,0,1,1,1,'',3,'displayable=false','');
INSERT INTO "fieldmetainfo" VALUES(NULL,'_extendedAttributes','EXTENDED_ATTRIBUTES','"ExtendedAttributes"','java.lang.String','com.gridnode.pdip.base.gwfbase.xpdl.model.XpdlApplication','EXTENDED_ATTRIBUTES',50,0,1,1,1,'',5,'displayable=false','');
INSERT INTO "fieldmetainfo" VALUES(NULL,'_processId','PROCESS_ID','"ProcessId"','java.lang.String','com.gridnode.pdip.base.gwfbase.xpdl.model.XpdlApplication','PROCESS_ID',50,0,1,1,1,'',5,'displayable=false','');
INSERT INTO "fieldmetainfo" VALUES(NULL,'_packageId','PACKAGE_ID','"PackageId"','java.lang.String','com.gridnode.pdip.base.gwfbase.xpdl.model.XpdlApplication','PACKAGE_ID',50,0,1,1,1,'',6,'displayable=false','');
INSERT INTO "fieldmetainfo" VALUES(NULL,'_pkgVersionId','PKG_VERSION_ID','"PkgVersionId"','java.lang.String','com.gridnode.pdip.base.gwfbase.xpdl.model.XpdlApplication','PKG_VERSION_ID',50,0,1,1,1,'',7,'displayable=false','');

--------- XpdlDataField
DELETE FROM "fieldmetainfo" WHERE "EntityObjectName" LIKE '%XpdlDataField';
INSERT INTO "fieldmetainfo" VALUES(NULL,'_uId','UID','"UID"','java.lang.Long','com.gridnode.pdip.base.gwfbase.xpdl.model.XpdlDataField','',0,0,0,0,0,'',999,'displayable=false','');
INSERT INTO "fieldmetainfo" VALUES(NULL,'_dataFieldId','DATA_FIELD_ID','"DataFieldId"','java.lang.String','com.gridnode.pdip.base.gwfbase.xpdl.model.XpdlDataField','DATA_FIELD_ID',50,0,1,1,1,'',1,'displayable=false','');
INSERT INTO "fieldmetainfo" VALUES(NULL,'_dataFieldName','DATA_FIELD_NAME','"DataFieldName"','java.lang.String','com.gridnode.pdip.base.gwfbase.xpdl.model.XpdlDataField','DATA_FIELD_NAME',50,0,1,1,1,'',2,'displayable=false','');
INSERT INTO "fieldmetainfo" VALUES(NULL,'_dataFieldDescription','DATA_FIELD_DESCRIPTION','"DataFieldDescription"','java.lang.String','com.gridnode.pdip.base.gwfbase.xpdl.model.XpdlDataField','DATA_FIELD_DESCRIPTION',50,0,1,1,1,'',3,'displayable=false','');
INSERT INTO "fieldmetainfo" VALUES(NULL,'_extendedAttributes','EXTENDED_ATTRIBUTES','"ExtendedAttributes"','java.lang.String','com.gridnode.pdip.base.gwfbase.xpdl.model.XpdlDataField','EXTENDED_ATTRIBUTE_LIST_UID',0,0,0,0,0,'',4,'displayable=false','');
INSERT INTO "fieldmetainfo" VALUES(NULL,'_isArray','IS_ARRAY','"IsArray"','java.lang.Boolean','com.gridnode.pdip.base.gwfbase.xpdl.model.XpdlDataField','IS_ARRAY',0,0,0,1,1,'',5,'displayable=false','');
INSERT INTO "fieldmetainfo" VALUES(NULL,'_initialValue','INITIAL_VALUE','"InitialValue"','java.lang.String','com.gridnode.pdip.base.gwfbase.xpdl.model.XpdlDataField','INITIAL_VALUE',50,0,1,1,1,'',6,'displayable=false','');
INSERT INTO "fieldmetainfo" VALUES(NULL,'_lengthBytes','LENGTH_BYTES','"LengthBytes"','java.lang.Long','com.gridnode.pdip.base.gwfbase.xpdl.model.XpdlDataField','LENGTH_BYTES',0,0,0,0,0,'',7,'displayable=false','');
INSERT INTO "fieldmetainfo" VALUES(NULL,'_processId','PROCESS_ID','"ProcessId"','java.lang.String','com.gridnode.pdip.base.gwfbase.xpdl.model.XpdlDataField','PROCESS_ID',50,0,1,1,1,'',8,'displayable=false','');
INSERT INTO "fieldmetainfo" VALUES(NULL,'packageId','PACKAGE_ID','"PackageId"','java.lang.String','com.gridnode.pdip.base.gwfbase.xpdl.model.XpdlDataField','PACKAGE_ID',50,0,1,1,1,'',9,'displayable=false','');
INSERT INTO "fieldmetainfo" VALUES(NULL,'_pkgVersionId','PKG_VERSION_ID','"PkgVersionId"','java.lang.String','com.gridnode.pdip.base.gwfbase.xpdl.model.XpdlDataField','PKG_VERSION_ID',50,0,1,1,1,'',10,'displayable=false','');
INSERT INTO "fieldmetainfo" VALUES(NULL,'_dataTypeName','DATATYPE_NAME','"DataTypeName"','java.lang.String','com.gridnode.pdip.base.gwfbase.xpdl.model.XpdlDataField','DATATYPE_NAME',50,0,1,1,1,'',1,'displayable=false','');
INSERT INTO "fieldmetainfo" VALUES(NULL,'_complexDataTypeUId','COMPLEX_DATATYPE_UID','"ComplexDataTypeUId"','java.lang.Long','com.gridnode.pdip.base.gwfbase.xpdl.model.XpdlDataField','COMPLEX_DATATYPE_UID',0,0,0,0,0,'',4,'displayable=false','');

--------- XpdlExternalPackage
DELETE FROM "fieldmetainfo" WHERE "EntityObjectName" LIKE '%XpdlExternalPackage';
INSERT INTO "fieldmetainfo" VALUES(NULL,'_uId','UID','"UID"','java.lang.Long','com.gridnode.pdip.base.gwfbase.xpdl.model.XpdlExternalPackage','',0,0,0,0,0,'',999,'displayable=false','');
INSERT INTO "fieldmetainfo" VALUES(NULL,'_href','HREF','"Href"','java.lang.String','com.gridnode.pdip.base.gwfbase.xpdl.model.XpdlExternalPackage','HREF',50,0,1,1,1,'',1,'displayable=false','');
INSERT INTO "fieldmetainfo" VALUES(NULL,'_extendedAttributes','EXTENDED_ATTRIBUTES','"ExtendedAttributes"','java.lang.String','com.gridnode.pdip.base.gwfbase.xpdl.model.XpdlExternalPackage','EXTENDED_ATTRIBUTE_LIST_UID',0,0,0,0,0,'',2,'displayable=false','');
INSERT INTO "fieldmetainfo" VALUES(NULL,'_packageId','PACKAGE_ID','"PackageId"','java.lang.String','com.gridnode.pdip.base.gwfbase.xpdl.model.XpdlExternalPackage','PACKAGE_ID',50,0,1,1,1,'',3,'displayable=false','');
INSERT INTO "fieldmetainfo" VALUES(NULL,'_pkgVersionId','PKG_VERSION_ID','"PkgVersionId"','java.lang.String','com.gridnode.pdip.base.gwfbase.xpdl.model.XpdlExternalPackage','PKG_VERSION_ID',50,0,1,1,1,'',4,'displayable=false','');

--------- XpdlFormalParam
DELETE FROM "fieldmetainfo" WHERE "EntityObjectName" LIKE '%XpdlFormalParam';
INSERT INTO "fieldmetainfo" VALUES(NULL,'_uId','UID','"UID"','java.lang.Long','com.gridnode.pdip.base.gwfbase.xpdl.model.XpdlFormalParam','',0,0,0,0,0,'',999,'displayable=false','');
INSERT INTO "fieldmetainfo" VALUES(NULL,'_formalParamId','FORMAL_PARAM_ID','"FormalParamId"','java.lang.String','com.gridnode.pdip.base.gwfbase.xpdl.model.XpdlFormalParam','FORMAL_PARAM_ID',50,0,1,1,1,'',1,'displayable=false','');
INSERT INTO "fieldmetainfo" VALUES(NULL,'_mode','MODE','"Mode"','java.lang.String','com.gridnode.pdip.base.gwfbase.xpdl.model.XpdlFormalParam','MODE',50,0,1,1,1,'',2,'displayable=false','');
INSERT INTO "fieldmetainfo" VALUES(NULL,'_indexNumber','INDEX_NUMBER','"IndexNumber"','java.lang.Integer','com.gridnode.pdip.base.gwfbase.xpdl.model.XpdlFormalParam','INDEX_NUMBER',0,0,0,0,0,'',3,'displayable=false','');
INSERT INTO "fieldmetainfo" VALUES(NULL,'_formalParamDescription','FORMAL_PARAM_DESCRIPTION','"FormalParamDescription"','java.lang.String','com.gridnode.pdip.base.gwfbase.xpdl.model.XpdlFormalParam','FORMAL_PARAM_DESCRIPTION',50,0,1,1,1,'',4,'displayable=false','');
INSERT INTO "fieldmetainfo" VALUES(NULL,'_applicationId','APPLICATION_ID','"ApplicationId"','java.lang.String','com.gridnode.pdip.base.gwfbase.xpdl.model.XpdlFormalParam','APPLICATION_ID',50,0,1,1,1,'',5,'displayable=false','');
INSERT INTO "fieldmetainfo" VALUES(NULL,'_processId','PROCESS_ID','"ProcessId"','java.lang.String','com.gridnode.pdip.base.gwfbase.xpdl.model.XpdlFormalParam','PROCESS_ID',50,0,1,1,1,'',6,'displayable=false','');
INSERT INTO "fieldmetainfo" VALUES(NULL,'_packageId','PACKAGE_ID','"PackageId"','java.lang.String','com.gridnode.pdip.base.gwfbase.xpdl.model.XpdlFormalParam','PACKAGE_ID',50,0,1,1,1,'',7,'displayable=false','');
INSERT INTO "fieldmetainfo" VALUES(NULL,'_pkgVersionId','PKG_VERSION_ID','"PkgVersionId"','java.lang.String','com.gridnode.pdip.base.gwfbase.xpdl.model.XpdlFormalParam','PKG_VERSION_ID',50,0,1,1,1,'',8,'displayable=false','');
INSERT INTO "fieldmetainfo" VALUES(NULL,'_dataTypeName','DATATYPE_NAME','"DataTypeName"','java.lang.String','com.gridnode.pdip.base.gwfbase.xpdl.model.XpdlFormalParam','DATATYPE_NAME',50,0,1,1,1,'',1,'displayable=false','');
INSERT INTO "fieldmetainfo" VALUES(NULL,'_complexDataTypeUId','COMPLEX_DATATYPE_UID','"ComplexDataTypeUId"','java.lang.Long','com.gridnode.pdip.base.gwfbase.xpdl.model.XpdlFormalParam','COMPLEX_DATATYPE_UID',0,0,0,0,0,'',4,'displayable=false','');

--------- XpdlPackage
DELETE FROM "fieldmetainfo" WHERE "EntityObjectName" LIKE '%XpdlPackage';
INSERT INTO "fieldmetainfo" VALUES(NULL,'_uId','UID','"UID"','java.lang.Long','com.gridnode.pdip.base.gwfbase.xpdl.model.XpdlPackage','',0,0,0,0,0,'',999,'displayable=false','');
INSERT INTO "fieldmetainfo" VALUES(NULL,'_packageId','PACKAGE_ID','"PackageId"','java.lang.String','com.gridnode.pdip.base.gwfbase.xpdl.model.XpdlPackage','PACKAGE_ID',50,0,1,1,1,'',1,'displayable=false','');
INSERT INTO "fieldmetainfo" VALUES(NULL,'_packageName','PACKAGE_NAME','"PackageName"','java.lang.String','com.gridnode.pdip.base.gwfbase.xpdl.model.XpdlPackage','PACKAGE_NAME',50,0,1,1,1,'',2,'displayable=false','');
INSERT INTO "fieldmetainfo" VALUES(NULL,'_packageDescription','PACKAGE_DESCRIPTION','"PackageDescription"','java.lang.String','com.gridnode.pdip.base.gwfbase.xpdl.model.XpdlPackage','PACKAGE_DESCRIPTION',50,0,1,1,1,'',3,'displayable=false','');
INSERT INTO "fieldmetainfo" VALUES(NULL,'_extendedAttributes','EXTENDED_ATTRIBUTES','"ExtendedAttributes"','java.lang.String','com.gridnode.pdip.base.gwfbase.xpdl.model.XpdlPackage','EXTENDED_ATTRIBUTE_LIST_UID',0,0,0,0,0,'',4,'displayable=false','');
INSERT INTO "fieldmetainfo" VALUES(NULL,'_specificationId','SPECIFICATION_ID','"SpecificationId"','java.lang.String','com.gridnode.pdip.base.gwfbase.xpdl.model.XpdlPackage','SPECIFICATION_ID',50,0,1,1,1,'',5,'displayable=false','');
INSERT INTO "fieldmetainfo" VALUES(NULL,'_specificationVersion','SPECIFICATION_VERSION','"SpecificationVersion"','java.lang.String','com.gridnode.pdip.base.gwfbase.xpdl.model.XpdlPackage','SPECIFICATION_VERSION',50,0,1,1,1,'',6,'displayable=false','');
INSERT INTO "fieldmetainfo" VALUES(NULL,'_sourceVendorInfo','SOURCE_VENDOR_INFO','"SourceVendorInfo"','java.lang.String','com.gridnode.pdip.base.gwfbase.xpdl.model.XpdlPackage','SOURCE_VENDOR_INFO',50,0,1,1,1,'',7,'displayable=false','');
INSERT INTO "fieldmetainfo" VALUES(NULL,'_creationDateTime','CREATION_DATE_TIME','"CreationDateTime"','java.util.Date','com.gridnode.pdip.base.gwfbase.xpdl.model.XpdlPackage','CREATION_DATE_TIME',0,0,0,0,1,'',8,'displayable=false','');
INSERT INTO "fieldmetainfo" VALUES(NULL,'_documentationUrl','DOCUMENTATION_URL','"DocumentationUrl"','java.lang.String','com.gridnode.pdip.base.gwfbase.xpdl.model.XpdlPackage','DOCUMENTATION_URL',50,0,1,1,1,'',9,'displayable=false','');
INSERT INTO "fieldmetainfo" VALUES(NULL,'_priorityUnit','PRIORITY_UNIT','"PriorityUnit"','java.lang.String','com.gridnode.pdip.base.gwfbase.xpdl.model.XpdlPackage','PRIORITY_UNIT',50,0,1,1,1,'',10,'displayable=false','');
INSERT INTO "fieldmetainfo" VALUES(NULL,'_costUnit','COST_UNIT','"CostUnit"','java.lang.String','com.gridnode.pdip.base.gwfbase.xpdl.model.XpdlPackage','COST_UNIT',50,0,1,1,1,'',11,'displayable=false','');
INSERT INTO "fieldmetainfo" VALUES(NULL,'_author','AUTHOR','"Author"','java.lang.String','com.gridnode.pdip.base.gwfbase.xpdl.model.XpdlPackage','AUTHOR',50,0,1,1,1,'',12,'displayable=false','');
INSERT INTO "fieldmetainfo" VALUES(NULL,'_versionId','VERSION_ID','"VersionId"','java.lang.String','com.gridnode.pdip.base.gwfbase.xpdl.model.XpdlPackage','VERSION_ID',50,0,1,1,1,'',13,'displayable=false','');
INSERT INTO "fieldmetainfo" VALUES(NULL,'_codepage','CODEPAGE','"Codepage"','java.lang.String','com.gridnode.pdip.base.gwfbase.xpdl.model.XpdlPackage','CODEPAGE',50,0,1,1,1,'',14,'displayable=false','');
INSERT INTO "fieldmetainfo" VALUES(NULL,'_countrykey','COUNTRYKEY','"Countrykey"','java.lang.String','com.gridnode.pdip.base.gwfbase.xpdl.model.XpdlPackage','COUNTRYKEY',50,0,1,1,1,'',15,'displayable=false','');
INSERT INTO "fieldmetainfo" VALUES(NULL,'_publicationStatus','PUBLICATION_STATUS','"PublicationStatus"','java.lang.String','com.gridnode.pdip.base.gwfbase.xpdl.model.XpdlPackage','PUBLICATION_STATUS',50,0,1,1,1,'',16,'displayable=false','');
INSERT INTO "fieldmetainfo" VALUES(NULL,'_responsibleListUId','RESPONSIBLE_LIST_UID','"ResponsibleListUId"','java.lang.Long','com.gridnode.pdip.base.gwfbase.xpdl.model.XpdlPackage','RESPONSIBLE_LIST_UID',0,0,0,0,0,'',17,'displayable=false','');
INSERT INTO "fieldmetainfo" VALUES(NULL,'_graphConformance','GRAPH_CONFORMANCE','"GraphConformance"','java.lang.String','com.gridnode.pdip.base.gwfbase.xpdl.model.XpdlPackage','GRAPH_CONFORMANCE',50,0,1,1,1,'',18,'displayable=false','');
INSERT INTO "fieldmetainfo" VALUES(NULL,'_state','STATE','"State"','java.lang.Short','com.gridnode.pdip.base.gwfbase.xpdl.model.XpdlPackage','XpdlPackage.state',1,0,1,1,1,'',999,'displayable=false','');

--------- XpdlParticipant
DELETE FROM "fieldmetainfo" WHERE "EntityObjectName" LIKE '%XpdlParticipant';
INSERT INTO "fieldmetainfo" VALUES(NULL,'_uId','UID','"UID"','java.lang.Long','com.gridnode.pdip.base.gwfbase.xpdl.model.XpdlParticipant','',0,0,0,0,0,'',999,'displayable=false','');
INSERT INTO "fieldmetainfo" VALUES(NULL,'_participantId','PARTICIPANT_ID','"ParticipantId"','java.lang.String','com.gridnode.pdip.base.gwfbase.xpdl.model.XpdlParticipant','PARTICIPANT_ID',50,0,1,1,1,'',1,'displayable=false','');
INSERT INTO "fieldmetainfo" VALUES(NULL,'_participantName','PARTICIPANT_NAME','"ParticipantName"','java.lang.String','com.gridnode.pdip.base.gwfbase.xpdl.model.XpdlParticipant','PARTICIPANT_NAME',50,0,1,1,1,'',2,'displayable=false','');
INSERT INTO "fieldmetainfo" VALUES(NULL,'_participantDescription','PARTICIPANT_DESCRIPTION','"ParticipantDescription"','java.lang.String','com.gridnode.pdip.base.gwfbase.xpdl.model.XpdlParticipant','PARTICIPANT_DESCRIPTION',50,0,1,1,1,'',3,'displayable=false','');
INSERT INTO "fieldmetainfo" VALUES(NULL,'_extendedAttributes','EXTENDED_ATTRIBUTES','"ExtendedAttributes"','java.lang.String','com.gridnode.pdip.base.gwfbase.xpdl.model.XpdlParticipant','EXTENDED_ATTRIBUTE_LIST_UID',0,0,0,0,0,'',4,'displayable=false','');
INSERT INTO "fieldmetainfo" VALUES(NULL,'_participantTypeId','PARTICIPANT_TYPE_ID','"ParticipantTypeId"','java.lang.String','com.gridnode.pdip.base.gwfbase.xpdl.model.XpdlParticipant','PARTICIPANT_TYPE_ID',50,0,1,1,1,'',5,'displayable=false','');
INSERT INTO "fieldmetainfo" VALUES(NULL,'_processId','PROCESS_ID','"ProcessId"','java.lang.String','com.gridnode.pdip.base.gwfbase.xpdl.model.XpdlParticipant','PROCESS_ID',50,0,1,1,1,'',5,'displayable=false','');
INSERT INTO "fieldmetainfo" VALUES(NULL,'_packageId','PACKAGE_ID','"PackageId"','java.lang.String','com.gridnode.pdip.base.gwfbase.xpdl.model.XpdlParticipant','PACKAGE_ID',50,0,1,1,1,'',6,'displayable=false','');
INSERT INTO "fieldmetainfo" VALUES(NULL,'_pkgVersionId','PKG_VERSION_ID','"PkgVersionId"','java.lang.String','com.gridnode.pdip.base.gwfbase.xpdl.model.XpdlParticipant','PKG_VERSION_ID',50,0,1,1,1,'',7,'displayable=false','');

--------- XpdlParticipantList
DELETE FROM "fieldmetainfo" WHERE "EntityObjectName" LIKE '%XpdlParticipantList';
INSERT INTO "fieldmetainfo" VALUES(NULL,'_uId','UID','"UID"','java.lang.Long','com.gridnode.pdip.base.gwfbase.xpdl.model.XpdlParticipantList','',0,0,0,0,0,'',999,'displayable=false','');
INSERT INTO "fieldmetainfo" VALUES(NULL,'_participantId','PARTICIPANT_ID','"ParticipantId"','java.lang.String','com.gridnode.pdip.base.gwfbase.xpdl.model.XpdlParticipantList','PARTICIPANT_ID',50,0,1,1,1,'',1,'displayable=false','');
INSERT INTO "fieldmetainfo" VALUES(NULL,'_participantIndex','PARTICIPANT_INDEX','"ParticipantIndex"','java.lang.Long','com.gridnode.pdip.base.gwfbase.xpdl.model.XpdlParticipantList','PARTICIPANT_INDEX',0,0,0,0,0,'',2,'displayable=false','');
INSERT INTO "fieldmetainfo" VALUES(NULL,'_listUId','LIST_UID','"ListUId"','java.lang.Long','com.gridnode.pdip.base.gwfbase.xpdl.model.XpdlParticipantList','LIST_UID',0,0,0,0,0,'',3,'displayable=false','');

--------- XpdlProcess
DELETE FROM "fieldmetainfo" WHERE "EntityObjectName" LIKE '%XpdlProcess';
INSERT INTO "fieldmetainfo" VALUES(NULL,'_uId','UID','"UID"','java.lang.Long','com.gridnode.pdip.base.gwfbase.xpdl.model.XpdlProcess','',0,0,0,0,0,'',999,'displayable=false','');
INSERT INTO "fieldmetainfo" VALUES(NULL,'_processId','PROCESS_ID','"ProcessId"','java.lang.String','com.gridnode.pdip.base.gwfbase.xpdl.model.XpdlProcess','PROCESS_ID',50,0,1,1,1,'',1,'displayable=false','');
INSERT INTO "fieldmetainfo" VALUES(NULL,'_processName','PROCESS_NAME','"ProcessName"','java.lang.String','com.gridnode.pdip.base.gwfbase.xpdl.model.XpdlProcess','PROCESS_NAME',50,0,1,1,1,'',2,'displayable=false','');
INSERT INTO "fieldmetainfo" VALUES(NULL,'_processDescription','PROCESS_DESCRIPTION','"ProcessDescription"','java.lang.String','com.gridnode.pdip.base.gwfbase.xpdl.model.XpdlProcess','PROCESS_DESCRIPTION',50,0,1,1,1,'',3,'displayable=false','');
INSERT INTO "fieldmetainfo" VALUES(NULL,'_extendedAttributes','EXTENDED_ATTRIBUTES','"ExtendedAttributes"','java.lang.String','com.gridnode.pdip.base.gwfbase.xpdl.model.XpdlProcess','EXTENDED_ATTRIBUTE_LIST_UID',0,0,0,0,0,'',4,'displayable=false','');
INSERT INTO "fieldmetainfo" VALUES(NULL,'_durationUnit','DURATION_UNIT','"DurationUnit"','java.lang.String','com.gridnode.pdip.base.gwfbase.xpdl.model.XpdlProcess','DURATION_UNIT',50,0,1,1,1,'',5,'displayable=false','');
INSERT INTO "fieldmetainfo" VALUES(NULL,'_creationDateTime','CREATION_DATE_TIME','"CreationDateTime"','java.util.Date','com.gridnode.pdip.base.gwfbase.xpdl.model.XpdlProcess','CREATION_DATE_TIME',0,0,0,0,1,'',6,'displayable=false','');
INSERT INTO "fieldmetainfo" VALUES(NULL,'_headerDescription','HEADER_DESCRIPTION','"HeaderDescription"','java.lang.String','com.gridnode.pdip.base.gwfbase.xpdl.model.XpdlProcess','HEADER_DESCRIPTION',50,0,1,1,1,'',7,'displayable=false','');
INSERT INTO "fieldmetainfo" VALUES(NULL,'_priority','PRIORITY','"Priority"','java.lang.Long','com.gridnode.pdip.base.gwfbase.xpdl.model.XpdlProcess','PRIORITY',0,0,0,0,0,'',8,'displayable=false','');
INSERT INTO "fieldmetainfo" VALUES(NULL,'_processLimit','PROCESS_LIMIT','"ProcessLimit"','java.lang.Double','com.gridnode.pdip.base.gwfbase.xpdl.model.XpdlProcess','PROCESS_LIMIT',0,0,0,0,0,'',9,'displayable=false','');
INSERT INTO "fieldmetainfo" VALUES(NULL,'_validFromDate','VALID_FROM_DATE','"ValidFromDate"','java.util.Date','com.gridnode.pdip.base.gwfbase.xpdl.model.XpdlProcess','VALID_FROM_DATE',0,0,0,0,1,'',10,'displayable=false','');
INSERT INTO "fieldmetainfo" VALUES(NULL,'_validToDate','VALID_TO_DATE','"ValidToDate"','java.util.Date','com.gridnode.pdip.base.gwfbase.xpdl.model.XpdlProcess','VALID_TO_DATE',0,0,0,0,1,'',11,'displayable=false','');
INSERT INTO "fieldmetainfo" VALUES(NULL,'_waitingTime','WAITING_TIME','"WaitingTime"','java.lang.Double','com.gridnode.pdip.base.gwfbase.xpdl.model.XpdlProcess','WAITING_TIME',0,0,0,0,0,'',12,'displayable=false','');
INSERT INTO "fieldmetainfo" VALUES(NULL,'_workingTime','WORKING_TIME','"WorkingTime"','java.lang.Double','com.gridnode.pdip.base.gwfbase.xpdl.model.XpdlProcess','WORKING_TIME',0,0,0,0,0,'',13,'displayable=false','');
INSERT INTO "fieldmetainfo" VALUES(NULL,'_duration','DURATION','"Duration"','java.lang.Double','com.gridnode.pdip.base.gwfbase.xpdl.model.XpdlProcess','DURATION',0,0,0,0,0,'',14,'displayable=false','');
INSERT INTO "fieldmetainfo" VALUES(NULL,'_author','AUTHOR','"Author"','java.lang.String','com.gridnode.pdip.base.gwfbase.xpdl.model.XpdlProcess','AUTHOR',50,0,1,1,1,'',15,'displayable=false','');
INSERT INTO "fieldmetainfo" VALUES(NULL,'_versionId','VERSION_ID','"VersionId"','java.lang.String','com.gridnode.pdip.base.gwfbase.xpdl.model.XpdlProcess','VERSION_ID',50,0,1,1,1,'',16,'displayable=false','');
INSERT INTO "fieldmetainfo" VALUES(NULL,'_codepage','CODEPAGE','"Codepage"','java.lang.String','com.gridnode.pdip.base.gwfbase.xpdl.model.XpdlProcess','CODEPAGE',50,0,1,1,1,'',17,'displayable=false','');
INSERT INTO "fieldmetainfo" VALUES(NULL,'_countrykey','COUNTRYKEY','"Countrykey"','java.lang.String','com.gridnode.pdip.base.gwfbase.xpdl.model.XpdlProcess','COUNTRYKEY',50,0,1,1,1,'',18,'displayable=false','');
INSERT INTO "fieldmetainfo" VALUES(NULL,'_publicationStatus','PUBLICATION_STATUS','"PublicationStatus"','java.lang.String','com.gridnode.pdip.base.gwfbase.xpdl.model.XpdlProcess','PUBLICATION_STATUS',50,0,1,1,1,'',19,'displayable=false','');
INSERT INTO "fieldmetainfo" VALUES(NULL,'_responsibleListUId','RESPONSIBLE_LIST_UID','"ResponsibleListUId"','java.lang.Long','com.gridnode.pdip.base.gwfbase.xpdl.model.XpdlProcess','RESPONSIBLE_LIST_UID',0,0,0,0,0,'',20,'displayable=false','');
INSERT INTO "fieldmetainfo" VALUES(NULL,'_packageId','PACKAGE_ID','"PackageId"','java.lang.String','com.gridnode.pdip.base.gwfbase.xpdl.model.XpdlProcess','PACKAGE_ID',50,0,1,1,1,'',22,'displayable=false','');
INSERT INTO "fieldmetainfo" VALUES(NULL,'_defaultStartActivityId','DEFAULT_START_ACTIVITY_ID','"DefaultStartActivityId"','java.lang.String','com.gridnode.pdip.base.gwfbase.xpdl.model.XpdlProcess','DEFAULT_START_ACTIVITY_ID',50,0,1,1,1,'',23,'displayable=false','');
INSERT INTO "fieldmetainfo" VALUES(NULL,'_pkgVersionId','PKG_VERSION_ID','"PkgVersionId"','java.lang.String','com.gridnode.pdip.base.gwfbase.xpdl.model.XpdlProcess','PKG_VERSION_ID',50,0,1,1,1,'',24,'displayable=false','');

--------- XpdlSubFlow
DELETE FROM "fieldmetainfo" WHERE "EntityObjectName" LIKE '%XpdlSubFlow';
INSERT INTO "fieldmetainfo" VALUES(NULL,'_uId','UID','"UID"','java.lang.Long','com.gridnode.pdip.base.gwfbase.xpdl.model.XpdlSubFlow','',0,0,0,0,0,'',999,'displayable=false','');
INSERT INTO "fieldmetainfo" VALUES(NULL,'_subFlowId','SUB_FLOW_ID','"SubFlowId"','java.lang.String','com.gridnode.pdip.base.gwfbase.xpdl.model.XpdlSubFlow','SUB_FLOW_ID',50,0,1,1,1,'',1,'displayable=false','');
INSERT INTO "fieldmetainfo" VALUES(NULL,'_subFlowType','SUB_FLOW_TYPE','"SubFlowType"','java.lang.String','com.gridnode.pdip.base.gwfbase.xpdl.model.XpdlSubFlow','SUB_FLOW_TYPE',50,0,1,1,1,'',2,'displayable=false','');
INSERT INTO "fieldmetainfo" VALUES(NULL,'_actualParameters','ACTUAL_PARAMETERS','"ActualParameters"','java.lang.String','com.gridnode.pdip.base.gwfbase.xpdl.model.XpdlSubFlow','ACTUAL_PARAMETERS',50,0,1,1,1,'',3,'displayable=false','');
INSERT INTO "fieldmetainfo" VALUES(NULL,'_activityId','ACTIVITY_ID','"ActivityId"','java.lang.String','com.gridnode.pdip.base.gwfbase.xpdl.model.XpdlSubFlow','ACTIVITY_ID',50,0,1,1,1,'',4,'displayable=false','');
INSERT INTO "fieldmetainfo" VALUES(NULL,'_processId','PROCESS_ID','"ProcessId"','java.lang.String','com.gridnode.pdip.base.gwfbase.xpdl.model.XpdlSubFlow','PROCESS_ID',50,0,1,1,1,'',5,'displayable=false','');
INSERT INTO "fieldmetainfo" VALUES(NULL,'_packageId','PACKAGE_ID','"PackageId"','java.lang.String','com.gridnode.pdip.base.gwfbase.xpdl.model.XpdlSubFlow','PACKAGE_ID',50,0,1,1,1,'',6,'displayable=false','');
INSERT INTO "fieldmetainfo" VALUES(NULL,'_pkgVersionId','PKG_VERSION_ID','"PkgVersionId"','java.lang.String','com.gridnode.pdip.base.gwfbase.xpdl.model.XpdlSubFlow','PKG_VERSION_ID',50,0,1,1,1,'',7,'displayable=false','');

--------- XpdlTool
DELETE FROM "fieldmetainfo" WHERE "EntityObjectName" LIKE '%XpdlTool';
INSERT INTO "fieldmetainfo" VALUES(NULL,'_uId','UID','"UID"','java.lang.Long','com.gridnode.pdip.base.gwfbase.xpdl.model.XpdlTool','',0,0,0,0,0,'',999,'displayable=false','');
INSERT INTO "fieldmetainfo" VALUES(NULL,'_toolId','TOOL_ID','"ToolId"','java.lang.String','com.gridnode.pdip.base.gwfbase.xpdl.model.XpdlTool','TOOL_ID',50,0,1,1,1,'',1,'displayable=false','');
INSERT INTO "fieldmetainfo" VALUES(NULL,'_toolType','TOOL_TYPE','"ToolType"','java.lang.String','com.gridnode.pdip.base.gwfbase.xpdl.model.XpdlTool','TOOL_TYPE',50,0,1,1,1,'',2,'displayable=false','');
INSERT INTO "fieldmetainfo" VALUES(NULL,'_toolDescription','TOOL_DESCRIPTION','"ToolDescription"','java.lang.String','com.gridnode.pdip.base.gwfbase.xpdl.model.XpdlTool','TOOL_DESCRIPTION',50,0,1,1,1,'',3,'displayable=false','');
INSERT INTO "fieldmetainfo" VALUES(NULL,'_actualParameters','ACTUAL_PARAMETERS','"ActualParameters"','java.lang.String','com.gridnode.pdip.base.gwfbase.xpdl.model.XpdlTool','ACTUAL_PARAMETERS',50,0,1,1,1,'',4,'displayable=false','');
INSERT INTO "fieldmetainfo" VALUES(NULL,'_extendedAttributes','EXTENDED_ATTRIBUTES','"ExtendedAttributes"','java.lang.String','com.gridnode.pdip.base.gwfbase.xpdl.model.XpdlTool','EXTENDED_ATTRIBUTES',50,0,1,1,1,'',5,'displayable=false','');
INSERT INTO "fieldmetainfo" VALUES(NULL,'_loopKind','LOOP_KIND','"LoopKind"','java.lang.String','com.gridnode.pdip.base.gwfbase.xpdl.model.XpdlTool','LOOP_KIND',50,0,1,1,1,'',6,'displayable=false','');
INSERT INTO "fieldmetainfo" VALUES(NULL,'_conditionExpr','CONDITION_EXPR','"ConditionExpr"','java.lang.String','com.gridnode.pdip.base.gwfbase.xpdl.model.XpdlTool','CONDITION_EXPR',50,0,1,1,1,'',7,'displayable=false','');
INSERT INTO "fieldmetainfo" VALUES(NULL,'_activityId','ACTIVITY_ID','"ActivityId"','java.lang.String','com.gridnode.pdip.base.gwfbase.xpdl.model.XpdlTool','ACTIVITY_ID',50,0,1,1,1,'',8,'displayable=false','');
INSERT INTO "fieldmetainfo" VALUES(NULL,'_processId','PROCESS_ID','"ProcessId"','java.lang.String','com.gridnode.pdip.base.gwfbase.xpdl.model.XpdlTool','PROCESS_ID',50,0,1,1,1,'',9,'displayable=false','');
INSERT INTO "fieldmetainfo" VALUES(NULL,'_packageId','PACKAGE_ID','"PackageId"','java.lang.String','com.gridnode.pdip.base.gwfbase.xpdl.model.XpdlTool','PACKAGE_ID',50,0,1,1,1,'',10,'displayable=false','');
INSERT INTO "fieldmetainfo" VALUES(NULL,'_pkgVersionId','PKG_VERSION_ID','"PkgVersionId"','java.lang.String','com.gridnode.pdip.base.gwfbase.xpdl.model.XpdlTool','PKG_VERSION_ID',50,0,1,1,1,'',11,'displayable=false','');

--------- XpdlTransition
DELETE FROM "fieldmetainfo" WHERE "EntityObjectName" LIKE '%XpdlTransition';
INSERT INTO "fieldmetainfo" VALUES(NULL,'_uId','UID','"UID"','java.lang.Long','com.gridnode.pdip.base.gwfbase.xpdl.model.XpdlTransition','',0,0,0,0,0,'',999,'displayable=false','');
INSERT INTO "fieldmetainfo" VALUES(NULL,'_transitionId','TRANSITION_ID','"TransitionId"','java.lang.String','com.gridnode.pdip.base.gwfbase.xpdl.model.XpdlTransition','TRANSITION_ID',50,0,1,1,1,'',1,'displayable=false','');
INSERT INTO "fieldmetainfo" VALUES(NULL,'_transitionName','TRANSITION_NAME','"TransitionName"','java.lang.String','com.gridnode.pdip.base.gwfbase.xpdl.model.XpdlTransition','TRANSITION_NAME',50,0,1,1,1,'',2,'displayable=false','');
INSERT INTO "fieldmetainfo" VALUES(NULL,'_transitionDescription','TRANSITION_DESCRIPTION','"TransitionDescription"','java.lang.String','com.gridnode.pdip.base.gwfbase.xpdl.model.XpdlTransition','TRANSITION_DESCRIPTION',50,0,1,1,1,'',3,'displayable=false','');
INSERT INTO "fieldmetainfo" VALUES(NULL,'_extendedAttributes','EXTENDED_ATTRIBUTES','"ExtendedAttributes"','java.lang.String','com.gridnode.pdip.base.gwfbase.xpdl.model.XpdlTransition','EXTENDED_ATTRIBUTE_LIST_UID',0,0,0,0,0,'',4,'displayable=false','');
INSERT INTO "fieldmetainfo" VALUES(NULL,'_fromActivityId','FROM_ACTIVITY_ID','"FromActivityId"','java.lang.String','com.gridnode.pdip.base.gwfbase.xpdl.model.XpdlTransition','FROM_ACTIVITY_ID',50,0,1,1,1,'',5,'displayable=false','');
INSERT INTO "fieldmetainfo" VALUES(NULL,'_toActivityId','TO_ACTIVITY_ID','"ToActivityId"','java.lang.String','com.gridnode.pdip.base.gwfbase.xpdl.model.XpdlTransition','TO_ACTIVITY_ID',50,0,1,1,1,'',6,'displayable=false','');
INSERT INTO "fieldmetainfo" VALUES(NULL,'_loopType','LOOP_TYPE','"LoopType"','java.lang.String','com.gridnode.pdip.base.gwfbase.xpdl.model.XpdlTransition','LOOP_TYPE',50,0,1,1,1,'',7,'displayable=false','');
INSERT INTO "fieldmetainfo" VALUES(NULL,'_conditionType','CONDITION_TYPE','"ConditionType"','java.lang.String','com.gridnode.pdip.base.gwfbase.xpdl.model.XpdlTransition','CONDITION_TYPE',50,0,1,1,1,'',8,'displayable=false','');
INSERT INTO "fieldmetainfo" VALUES(NULL,'_conditionExpr','CONDITION_EXPR','"ConditionExpr"','java.lang.String','com.gridnode.pdip.base.gwfbase.xpdl.model.XpdlTransition','CONDITION_EXPR',50,0,1,1,1,'',9,'displayable=false','');
INSERT INTO "fieldmetainfo" VALUES(NULL,'_processId','PROCESS_ID','"ProcessId"','java.lang.String','com.gridnode.pdip.base.gwfbase.xpdl.model.XpdlTransition','PROCESS_ID',50,0,1,1,1,'',10,'displayable=false','');
INSERT INTO "fieldmetainfo" VALUES(NULL,'_packageId','PACKAGE_ID','"PackageId"','java.lang.String','com.gridnode.pdip.base.gwfbase.xpdl.model.XpdlTransition','PACKAGE_ID',50,0,1,1,1,'',11,'displayable=false','');
INSERT INTO "fieldmetainfo" VALUES(NULL,'_pkgVersionId','PKG_VERSION_ID','"PkgVersionId"','java.lang.String','com.gridnode.pdip.base.gwfbase.xpdl.model.XpdlTransition','PKG_VERSION_ID',50,0,1,1,1,'',12,'displayable=false','');

--------- XpdlTransitionRef
DELETE FROM "fieldmetainfo" WHERE "EntityObjectName" LIKE '%XpdlTransitionRef';
INSERT INTO "fieldmetainfo" VALUES(NULL,'_uId','UID','"UID"','java.lang.Long','com.gridnode.pdip.base.gwfbase.xpdl.model.XpdlTransitionRef','',0,0,0,0,0,'',999,'displayable=false','');
INSERT INTO "fieldmetainfo" VALUES(NULL,'_transitionId','TRANSITION_ID','"TransitionId"','java.lang.String','com.gridnode.pdip.base.gwfbase.xpdl.model.XpdlTransitionRef','TRANSITION_ID',50,0,1,1,1,'',1,'displayable=false','');
INSERT INTO "fieldmetainfo" VALUES(NULL,'_listUId','LIST_UID','"ListUId"','java.lang.Long','com.gridnode.pdip.base.gwfbase.xpdl.model.XpdlTransitionRef','LIST_UID',0,0,0,0,0,'',2,'displayable=false','');

--------- XpdlTransitionRestriction
DELETE FROM "fieldmetainfo" WHERE "EntityObjectName" LIKE '%XpdlTransitionRestriction';
INSERT INTO "fieldmetainfo" VALUES(NULL,'_uId','UID','"UID"','java.lang.Long','com.gridnode.pdip.base.gwfbase.xpdl.model.XpdlTransitionRestriction','',0,0,0,0,0,'',999,'displayable=false','');
INSERT INTO "fieldmetainfo" VALUES(NULL,'_isInlineBlock','IS_INLINE_BLOCK','"IsInlineBlock"','java.lang.Boolean','com.gridnode.pdip.base.gwfbase.xpdl.model.XpdlTransitionRestriction','IS_INLINE_BLOCK',0,0,0,1,1,'',1,'displayable=false','');
INSERT INTO "fieldmetainfo" VALUES(NULL,'_blockName','BLOCK_NAME','"BlockName"','java.lang.String','com.gridnode.pdip.base.gwfbase.xpdl.model.XpdlTransitionRestriction','BLOCK_NAME',50,0,1,1,1,'',2,'displayable=false','');
INSERT INTO "fieldmetainfo" VALUES(NULL,'_blockDescription','BLOCK_DESCRIPTION','"BlockDescription"','java.lang.String','com.gridnode.pdip.base.gwfbase.xpdl.model.XpdlTransitionRestriction','BLOCK_DESCRIPTION',50,0,1,1,1,'',3,'displayable=false','');
INSERT INTO "fieldmetainfo" VALUES(NULL,'_blockIconUrl','BLOCK_ICON_URL','"BlockIconUrl"','java.lang.String','com.gridnode.pdip.base.gwfbase.xpdl.model.XpdlTransitionRestriction','BLOCK_ICON_URL',50,0,1,1,1,'',4,'displayable=false','');
INSERT INTO "fieldmetainfo" VALUES(NULL,'_blockDocumentationUrl','BLOCK_DOCUMENTATION_URL','"BlockDocumentationUrl"','java.lang.String','com.gridnode.pdip.base.gwfbase.xpdl.model.XpdlTransitionRestriction','BLOCK_DOCUMENTATION_URL',50,0,1,1,1,'',5,'displayable=false','');
INSERT INTO "fieldmetainfo" VALUES(NULL,'_blockBeginActivityId','BLOCK_BEGIN_ACTIVITY_ID','"BlockBeginActivityId"','java.lang.String','com.gridnode.pdip.base.gwfbase.xpdl.model.XpdlTransitionRestriction','BLOCK_BEGIN_ACTIVITY_ID',50,0,1,1,1,'',6,'displayable=false','');
INSERT INTO "fieldmetainfo" VALUES(NULL,'_blockEndActivityId','BLOCK_END_ACTIVITY_ID','"BlockEndActivityId"','java.lang.String','com.gridnode.pdip.base.gwfbase.xpdl.model.XpdlTransitionRestriction','BLOCK_END_ACTIVITY_ID',50,0,1,1,1,'',7,'displayable=false','');
INSERT INTO "fieldmetainfo" VALUES(NULL,'_joinType','JOIN_TYPE','"JoinType"','java.lang.String','com.gridnode.pdip.base.gwfbase.xpdl.model.XpdlTransitionRestriction','JOIN_TYPE',50,0,1,1,1,'',8,'displayable=false','');
INSERT INTO "fieldmetainfo" VALUES(NULL,'_splitType','SPLIT_TYPE','"SplitType"','java.lang.String','com.gridnode.pdip.base.gwfbase.xpdl.model.XpdlTransitionRestriction','SPLIT_TYPE',50,0,1,1,1,'',9,'displayable=false','');
INSERT INTO "fieldmetainfo" VALUES(NULL,'_transitionRefListUId','TRANSITION_REF_LIST_UID','"TransitionRefListUId"','java.lang.Long','com.gridnode.pdip.base.gwfbase.xpdl.model.XpdlTransitionRestriction','TRANSITION_REF_LIST_UID',0,0,0,0,0,'',10,'displayable=false','');
INSERT INTO "fieldmetainfo" VALUES(NULL,'_listUId','LIST_UID','"ListUId"','java.lang.Long','com.gridnode.pdip.base.gwfbase.xpdl.model.XpdlTransitionRestriction','LIST_UID',0,0,0,0,0,'',11,'displayable=false','');

--------- XpdlTypeDeclaration
DELETE FROM "fieldmetainfo" WHERE "EntityObjectName" LIKE '%XpdlTypeDeclaration';
INSERT INTO "fieldmetainfo" VALUES(NULL,'_uId','UID','"UID"','java.lang.Long','com.gridnode.pdip.base.gwfbase.xpdl.model.XpdlTypeDeclaration','',0,0,0,0,0,'',999,'displayable=false','');
INSERT INTO "fieldmetainfo" VALUES(NULL,'_typeId','TYPE_ID','"TypeId"','java.lang.String','com.gridnode.pdip.base.gwfbase.xpdl.model.XpdlTypeDeclaration','TYPE_ID',50,0,1,1,1,'',1,'displayable=false','');
INSERT INTO "fieldmetainfo" VALUES(NULL,'_typeName','TYPE_NAME','"TypeName"','java.lang.String','com.gridnode.pdip.base.gwfbase.xpdl.model.XpdlTypeDeclaration','TYPE_NAME',50,0,1,1,1,'',2,'displayable=false','');
INSERT INTO "fieldmetainfo" VALUES(NULL,'_typeDescription','TYPE_DESCRIPTION','"TypeDescription"','java.lang.String','com.gridnode.pdip.base.gwfbase.xpdl.model.XpdlTypeDeclaration','TYPE_DESCRIPTION',50,0,1,1,1,'',3,'displayable=false','');
INSERT INTO "fieldmetainfo" VALUES(NULL,'_extendedAttributes','EXTENDED_ATTRIBUTES','"ExtendedAttributes"','java.lang.String','com.gridnode.pdip.base.gwfbase.xpdl.model.XpdlTypeDeclaration','EXTENDED_ATTRIBUTE_LIST_UID',0,0,0,0,0,'',4,'displayable=false','');
INSERT INTO "fieldmetainfo" VALUES(NULL,'_packageId','PACKAGE_ID','"PackageId"','java.lang.String','com.gridnode.pdip.base.gwfbase.xpdl.model.XpdlTypeDeclaration','PACKAGE_ID',50,0,1,1,1,'',5,'displayable=false','');
INSERT INTO "fieldmetainfo" VALUES(NULL,'_pkgVersionId','PKG_VERSION_ID','"PkgVersionId"','java.lang.String','com.gridnode.pdip.base.gwfbase.xpdl.model.XpdlTypeDeclaration','PKG_VERSION_ID',50,0,1,1,1,'',6,'displayable=false','');
INSERT INTO "fieldmetainfo" VALUES(NULL,'_dataTypeName','DATATYPE_NAME','"DataTypeName"','java.lang.String','com.gridnode.pdip.base.gwfbase.xpdl.model.XpdlTypeDeclaration','DATATYPE_NAME',50,0,1,1,1,'',1,'displayable=false','');
INSERT INTO "fieldmetainfo" VALUES(NULL,'_complexDataTypeUId','COMPLEX_DATATYPE_UID','"ComplexDataTypeUId"','java.lang.Long','com.gridnode.pdip.base.gwfbase.xpdl.model.XpdlTypeDeclaration','COMPLEX_DATATYPE_UID',0,0,0,0,0,'',4,'displayable=false','');

--------- XpdlComplexDataType
DELETE FROM "fieldmetainfo" WHERE "EntityObjectName" LIKE '%XpdlComplexDataType';
INSERT INTO "fieldmetainfo" VALUES(NULL,'_uId','UID','"UID"','java.lang.Long','com.gridnode.pdip.base.gwfbase.xpdl.model.XpdlComplexDataType','',0,0,0,0,0,'',999,'displayable=false','');
INSERT INTO "fieldmetainfo" VALUES(NULL,'_dataTypeName','DATATYPE_NAME','"DataTypeName"','java.lang.String','com.gridnode.pdip.base.gwfbase.xpdl.model.XpdlComplexDataType','DATATYPE_NAME',50,0,1,1,1,'',1,'displayable=false','');
INSERT INTO "fieldmetainfo" VALUES(NULL,'_complexDataTypeUId','COMPLEX_DATATYPE_UID','"ComplexDataTypeUId"','java.lang.Long','com.gridnode.pdip.base.gwfbase.xpdl.model.XpdlComplexDataType','COMPLEX_DATATYPE_UID',0,0,0,0,0,'',4,'displayable=false','');
INSERT INTO "fieldmetainfo" VALUES(NULL,'_subTypeUId','SUBTYPE_UID','"SubTypeUId"','java.lang.Long','com.gridnode.pdip.base.gwfbase.xpdl.model.XpdlComplexDataType','SUBTYPE_UID',0,0,0,0,0,'',4,'displayable=false','');
INSERT INTO "fieldmetainfo" VALUES(NULL,'_arrayLowerIndex','ARRAY_LOWERINDEX','"ArrayLowerIndex"','java.lang.Integer','com.gridnode.pdip.base.gwfbase.xpdl.model.XpdlComplexDataType','ARRAY_LOWERINDEX',0,0,0,0,0,'',4,'displayable=false','');
INSERT INTO "fieldmetainfo" VALUES(NULL,'_arrayUpperIndex','ARRAY_UPPERINDEX','"ArrayUpperIndex"','java.lang.Integer','com.gridnode.pdip.base.gwfbase.xpdl.model.XpdlComplexDataType','ARRAY_UPPERINDEX',0,0,0,0,0,'',4,'displayable=false','');

--------- StringData
DELETE FROM "fieldmetainfo" WHERE "EntityObjectName" LIKE '%StringData';
INSERT INTO "fieldmetainfo" VALUES(NULL,'_uId','UID','"UID"','java.lang.Long','com.gridnode.pdip.base.contextdata.entities.model.StringData','',0,0,0,0,0,'',999,'displayable=false','');
INSERT INTO "fieldmetainfo" VALUES(NULL,'_data','DATA','"Data"','java.lang.String','com.gridnode.pdip.base.contextdata.entities.model.StringData','DATA',50,0,1,1,1,'',1,'displayable=false','');
INSERT INTO "fieldmetainfo" VALUES(NULL,'_dataType','DATA_TYPE','"DataType"','java.lang.String','com.gridnode.pdip.base.contextdata.entities.model.StringData','DATA_TYPE',50,0,1,1,1,'',2,'displayable=false','');

--------- ByteData
DELETE FROM "fieldmetainfo" WHERE "EntityObjectName" LIKE '%ByteData';
INSERT INTO "fieldmetainfo" VALUES(NULL,'_uId','UID','"UID"','java.lang.Long','com.gridnode.pdip.base.contextdata.entities.model.ByteData','',0,0,0,0,0,'',999,'displayable=false','');
INSERT INTO "fieldmetainfo" VALUES(NULL,'_data','DATA','"Data"','byte[]','com.gridnode.pdip.base.contextdata.entities.model.ByteData','DATA',50,0,1,1,1,'',1,'displayable=false','');
INSERT INTO "fieldmetainfo" VALUES(NULL,'_dataType','DATA_TYPE','"DataType"','java.lang.String','com.gridnode.pdip.base.contextdata.entities.model.ByteData','DATA_TYPE',50,0,1,1,1,'',2,'displayable=false','');

--------- ContextData
DELETE FROM "fieldmetainfo" WHERE "EntityObjectName" LIKE '%ContextData';
INSERT INTO "fieldmetainfo" VALUES(NULL,'_uId','UID','"UID"','java.lang.Long','com.gridnode.pdip.base.contextdata.entities.model.ContextData','',0,0,0,0,0,'',999,'displayable=false','');
INSERT INTO "fieldmetainfo" VALUES(NULL,'_contextUId','CONTEXT_UID','"ContextUId"','java.lang.Long','com.gridnode.pdip.base.contextdata.entities.model.ContextData','',0,0,0,0,0,'',999,'displayable=false','');
INSERT INTO "fieldmetainfo" VALUES(NULL,'_contextData','CONTEXT_DATA','"ContextData"','byte[]','com.gridnode.pdip.base.contextdata.entities.model.ContextData','',0,0,0,0,0,'',999,'displayable=false','');

--------- Certificate
DELETE FROM "fieldmetainfo" WHERE "EntityObjectName" LIKE '%Certificate';
INSERT INTO "fieldmetainfo" VALUES (NULL,'_uId','UID','"UID"','java.lang.Long','com.gridnode.pdip.base.certificate.model.Certificate','certificate.uid',20,0,0,0,0,'0',999,'displayable=false'||chr(13)||chr(10)||'editable=false'||chr(13)||chr(10)||'mandatory=false'||chr(13)||chr(10),'type=uid'||chr(13)||chr(10));
INSERT INTO "fieldmetainfo" VALUES (NULL,'_privateKey','PRIVATEKEY','"PrivateKey"','java.lang.String','com.gridnode.pdip.base.certificate.model.Certificate','certificate.privateKey',0,0,0,0,0,'0',999,'displayable=false'||chr(13)||chr(10)||'editable=false'||chr(13)||chr(10)||'mandatory=false'||chr(13)||chr(10),'type=text'||chr(13)||chr(10));
INSERT INTO "fieldmetainfo" VALUES (NULL,'_publicKey','PUBLICKEY','"PublicKey"','java.lang.String','com.gridnode.pdip.base.certificate.model.Certificate','certificate.publicKey',0,0,0,0,0,'0',999,'displayable=false'||chr(13)||chr(10)||'editable=false'||chr(13)||chr(10)||'mandatory=false'||chr(13)||chr(10),'type=text'||chr(13)||chr(10));
INSERT INTO "fieldmetainfo" VALUES (NULL,'_certificate','CERTIFICATE','"Certificate"','java.lang.String','com.gridnode.pdip.base.certificate.model.Certificate','certificate.certificate',0,0,0,0,0,'0',999,'displayable=false'||chr(13)||chr(10)||'editable=false'||chr(13)||chr(10)||'mandatory=false'||chr(13)||chr(10),'type=text'||chr(13)||chr(10));
INSERT INTO "fieldmetainfo" VALUES (NULL,'_issuerName','ISSUERNAME','"IssuerName"','java.lang.String','com.gridnode.pdip.base.certificate.model.Certificate','certificate.issuerName',120,0,0,0,0,'0',999,'displayable=false'||chr(13)||chr(10)||'editable=false'||chr(13)||chr(10)||'mandatory=false'||chr(13)||chr(10),'type=text'||chr(13)||chr(10));
INSERT INTO "fieldmetainfo" VALUES (NULL,'_serialNum','SERIALNUM','"SerialNum"','java.lang.String','com.gridnode.pdip.base.certificate.model.Certificate','certificate.serialNum',30,0,0,0,0,'0',999,'displayable=true'||chr(13)||chr(10)||'editable=false'||chr(13)||chr(10)||'mandatory=false'||chr(13)||chr(10),'type=text'||chr(13)||chr(10));
INSERT INTO "fieldmetainfo" VALUES (NULL,'_name','NAME','"Name"','java.lang.String','com.gridnode.pdip.base.certificate.model.Certificate','certificate.name',50,0,0,0,0,'0',999,'displayable=true'||chr(13)||chr(10)||'editable=true'||chr(13)||chr(10)||'mandatory=true'||chr(13)||chr(10),'type=text'||chr(13)||chr(10)||'text.length.max=50'||chr(13)||chr(10));
INSERT INTO "fieldmetainfo" VALUES (NULL,'_id','ID','"ID"','java.lang.Integer','com.gridnode.pdip.base.certificate.model.Certificate','certificate.id',20,0,0,0,0,'0',999,'displayable=true'||chr(13)||chr(10)||'editable=false'||chr(13)||chr(10)||'mandatory=false'||chr(13)||chr(10),'type=range'||chr(13)||chr(10));
INSERT INTO "fieldmetainfo" VALUES (NULL,'_revokeID','REVOKEID','"RevokeID"','java.lang.Integer','com.gridnode.pdip.base.certificate.model.Certificate','certificate.revokeId',11,0,0,0,0,'0',999,'displayable=false'||chr(13)||chr(10)||'editable=false'||chr(13)||chr(10)||'mandatory=false'||chr(13)||chr(10),'type=range'||chr(13)||chr(10));
INSERT INTO "fieldmetainfo" VALUES (NULL,'_isMaster','IS_MASTER','"isMaster"','java.lang.Boolean','com.gridnode.pdip.base.certificate.model.Certificate','certificate.isMaster',0,0,0,0,0,'0',999,'displayable=false'||chr(13)||chr(10)||'mandatory=false'||chr(13)||chr(10)||'editable=false'||chr(13)||chr(10),'type=enum'||chr(13)||chr(10)||'generic.yes=true'||chr(13)||chr(10)||'generic.no=false'||chr(13)||chr(10));
INSERT INTO "fieldmetainfo" VALUES (NULL,'_isPartner','IS_PARTNER','"isPartner"','java.lang.Boolean','com.gridnode.pdip.base.certificate.model.Certificate','certificate.isPartner',0,0,0,0,0,'0',999,'displayable=true'||chr(13)||chr(10)||'mandatory=true'||chr(13)||chr(10)||'editable=false'||chr(13)||chr(10)||'editable.create=true','type=enum'||chr(13)||chr(10)||'generic.yes=true'||chr(13)||chr(10)||'generic.no=false'||chr(13)||chr(10));
INSERT INTO "fieldmetainfo" VALUES(NULL,'_category','CATEGORY','"Category"','java.lang.Short','com.gridnode.pdip.base.certificate.model.Certificate','certificate.category',80,0,0,1,1,'0',999,'displayable=true'||chr(13)||chr(10)||'editable=false'||chr(13)||chr(10)||'mandatory=false'||chr(13)||chr(10)||'displayable.create=false'||chr(13)||chr(10),'type=enum');
INSERT INTO "fieldmetainfo" VALUES (NULL,'_isInKeyStore','IS_IN_KS','"iSINKS"','java.lang.Boolean','com.gridnode.pdip.base.certificate.model.Certificate','certificate.inKeyStore',0,0,0,0,0,'0',999,'displayable=true'||chr(13)||chr(10)||'displayable.create=false'||chr(13)||chr(10)||'mandatory=true'||chr(13)||chr(10)||'editable=false'||chr(13)||chr(10),'type=enum'||chr(13)||chr(10)||'generic.yes=true'||chr(13)||chr(10)||'generic.no=false'||chr(13)||chr(10));
INSERT INTO "fieldmetainfo" VALUES (NULL,'_isInTrustStore','IS_IN_TS','"iSINTS"','java.lang.Boolean','com.gridnode.pdip.base.certificate.model.Certificate','certificate.inTrustStore',0,0,0,0,0,'0',999,'displayable=true'||chr(13)||chr(10)||'displayable.create=false'||chr(13)||chr(10)||'mandatory=true'||chr(13)||chr(10)||'editable=false'||chr(13)||chr(10),'type=enum'||chr(13)||chr(10)||'generic.yes=true'||chr(13)||chr(10)||'generic.no=false'||chr(13)||chr(10));
INSERT INTO "fieldmetainfo" VALUES (NULL,'_relatedCertUid','RELATED_CERT_UID','"relatedCertUid"','java.lang.Long','com.gridnode.pdip.base.certificate.model.Certificate','certificate.relatedCertUid',0,0,0,1,1,'0',999,'displayable=true'||chr(13)||chr(10)||'editable=true'||chr(13)||chr(10)||'mandatory=false'||chr(13)||chr(10),'type=foreign'||chr(13)||chr(10)||'foreign.key=certificate.uid'||chr(13)||chr(10)||'foreign.display=certificate.name'||chr(13)||chr(10)||'foreign.cached=false');
INSERT INTO "fieldmetainfo" VALUES(NULL,'_startDate','START_DATE','"StartDate"','java.util.Date','com.gridnode.pdip.base.certificate.model.Certificate','certificate.startDate',0,0,0,0,1,'',999,'displayable=true'||chr(13)||chr(10)||'mandatory=false'||chr(13)||chr(10)||'editable=false','type=datetime'||chr(13)||chr(10)||'datetime.time=true'||chr(13)||chr(10)||'datetime.date=true'||chr(13)||chr(10)||'datetime.adjustment=gts');
INSERT INTO "fieldmetainfo" VALUES(NULL,'_endDate','END_DATE','"EndDate"','java.util.Date','com.gridnode.pdip.base.certificate.model.Certificate','certificate.endDate',0,0,0,0,1,'',999,'displayable=true'||chr(13)||chr(10)||'mandatory=false'||chr(13)||chr(10)||'editable=false','type=datetime'||chr(13)||chr(10)||'datetime.time=true'||chr(13)||chr(10)||'datetime.date=true'||chr(13)||chr(10)||'datetime.adjustment=gts');
INSERT INTO "fieldmetainfo" VALUES(NULL,'_isCA','IS_CA','"IsCA"','java.lang.Boolean','com.gridnode.pdip.base.certificate.model.Certificate','certificate.isCA',0,0,0,0,0,'0',999,'displayable=true'||chr(13)||chr(10)||'mandatory=false'||chr(13)||chr(10)||'editable=true'||chr(13)||chr(10),'type=enum'||chr(13)||chr(10)||'generic.yes=true'||chr(13)||chr(10)||'generic.no=false'||chr(13)||chr(10));
INSERT INTO "fieldmetainfo" VALUES(NULL,'_replacementCertUid','REPLACEMENT_CERT_UID','"ReplacementCertUid"','java.lang.Long','com.gridnode.pdip.base.certificate.model.Certificate','certificate.replacementCertUid',0,0,0,1,1,'0',999,'displayable=true'||chr(13)||chr(10)||'editable=false'||chr(13)||chr(10)||'mandatory=false'||chr(13)||chr(10),'type=foreign'||chr(13)||chr(10)||'foreign.key=certificate.uid'||chr(13)||chr(10)||'foreign.display=certificate.name'||chr(13)||chr(10)||'foreign.cached=false');
INSERT INTO "fieldmetainfo" VALUES(NULL,'_certificateSwapping','CERTIFICATE_SWAPPING','','com.gridnode.pdip.base.certificate.model.CertificateSwapping','com.gridnode.pdip.base.certificate.model.Certificate','certificate.certificateSwapping',0,1,0,1,0,'0',999,'','type=embedded'||chr(13)||chr(10)||'embedded.type=certificateSwapping'||chr(13)||chr(10));

--------- CertificateSwapping
DELETE FROM "fieldmetainfo" WHERE "EntityObjectName" LIKE '%CertificateSwapping';
INSERT INTO "fieldmetainfo" VALUES (NULL,'_uId','UID','','java.lang.Long','com.gridnode.pdip.base.certificate.model.CertificateSwapping','certificateSwapping.uid',20,0,0,0,0,'0',999,'displayable=false'||chr(13)||chr(10)||'editable=false'||chr(13)||chr(10)||'mandatory=false'||chr(13)||chr(10),'type=uid'||chr(13)||chr(10));
INSERT INTO "fieldmetainfo" VALUES(NULL,'_swapDate','SWAP_DATE','','java.util.Date','com.gridnode.pdip.base.certificate.model.CertificateSwapping','certificateSwapping.swapDate',0,0,1,1,1,'',999,'displayable=true'||chr(13)||chr(10)||'mandatory=true'||chr(13)||chr(10)||'editable=true','type=datetime'||chr(13)||chr(10)||'datetime.time=false'||chr(13)||chr(10)||'datetime.date=true'||chr(13)||chr(10)||'datetime.pattern=yyyy-MM-dd'||chr(13)||chr(10)||'text.length.max=10');
INSERT INTO "fieldmetainfo" VALUES(NULL,'_swapTime','SWAP_TIME','','java.lang.String','com.gridnode.pdip.base.certificate.model.CertificateSwapping','certificateSwapping.swapTime',5,0,1,1,1,'',999,'displayable=true'||chr(13)||chr(10)||'mandatory=true'||chr(13)||chr(10)||'editable=true','type=text'||chr(13)||chr(10)||'text.length.max=5');
INSERT INTO "fieldmetainfo" VALUES (NULL,'_alarmUID','ALARM_UID','','java.lang.Long','com.gridnode.pdip.base.certificate.model.CertificateSwapping','certificateSwapping.alarmUid',20,0,0,0,0,'0',999,'displayable=false'||chr(13)||chr(10)||'editable=false'||chr(13)||chr(10)||'mandatory=false'||chr(13)||chr(10),'type=uid'||chr(13)||chr(10));

--------- Role
DELETE FROM "fieldmetainfo" WHERE "EntityObjectName" LIKE '%Role';
INSERT INTO "fieldmetainfo" VALUES(NULL,'_uId','UID','"UID"','java.lang.Long','com.gridnode.pdip.base.acl.model.Role','role.uid',20,0,0,0,0,'0',999,'displayable=false'||chr(13)||chr(10)||'editable=false'||chr(13)||chr(10)||'mandatory=false'||chr(13)||chr(10),'type=uid'||chr(13)||chr(10));
INSERT INTO "fieldmetainfo" VALUES(NULL,'_role','ROLE','"Role"','java.lang.String','com.gridnode.pdip.base.acl.model.Role','role.role',30,0,1,1,1,'0',1,'displayable=true'||chr(13)||chr(10)||'editable=false'||chr(13)||chr(10)||'editable.create=true'||chr(13)||chr(10)||'mandatory=true'||chr(13)||chr(10),'type=text'||chr(13)||chr(10)||'text.length.min=2'||chr(13)||chr(10)||'text.length.max=30'||chr(13)||chr(10));
INSERT INTO "fieldmetainfo" VALUES(NULL,'_description','DESCRIPTION','"Description"','java.lang.String','com.gridnode.pdip.base.acl.model.Role','role.description',255,0,1,1,1,'0',1,'displayable=true'||chr(13)||chr(10)||'editable=true'||chr(13)||chr(10)||'mandatory=true'||chr(13)||chr(10),'type=text'||chr(13)||chr(10)||'text.length.min=1'||chr(13)||chr(10)||'text.length.max=255'||chr(13)||chr(10));
INSERT INTO "fieldmetainfo" VALUES(NULL,'_canDelete','CAN_DELETE','"CanDelete"','java.lang.Boolean','com.gridnode.pdip.base.acl.model.Role','role.canDelete',0,0,1,1,1,'0',999,'displayable=true'||chr(13)||chr(10)||'editable=false'||chr(13)||chr(10)||'mandatory=false'||chr(13)||chr(10),'type=enum'||chr(13)||chr(10)||'generic.yes=true'||chr(13)||chr(10)||'generic.no=false'||chr(13)||chr(10));
INSERT INTO "fieldmetainfo" VALUES(NULL,'_version','VERSION','"Version"','java.lang.Double','com.gridnode.pdip.base.acl.model.Role','role.version',0,0,1,1,1,'0',999,'displayable=false'||chr(13)||chr(10)||'editable=false'||chr(13)||chr(10)||'mandatory=false'||chr(13)||chr(10),'type=range'||chr(13)||chr(10));

--------- Subject Role, NOTE: must be after Role.
DELETE FROM "fieldmetainfo" WHERE "EntityObjectName" LIKE '%SubjectRole';
INSERT INTO "fieldmetainfo" VALUES(NULL,'_version','VERSION','"Version"','java.lang.Double','com.gridnode.pdip.base.acl.model.SubjectRole','',0,0,1,1,1,'0',999,'','');
INSERT INTO "fieldmetainfo" VALUES(NULL,'_subjectType','SUBJECT_TYPE','"SubjectType"','java.lang.String','com.gridnode.pdip.base.acl.model.SubjectRole','',30,0,1,1,1,'0',1,'','');
INSERT INTO "fieldmetainfo" VALUES(NULL,'_role','ROLE','"Role"','java.lang.Long','com.gridnode.pdip.base.acl.model.SubjectRole','',0,0,1,1,1,'0',999,'','');
INSERT INTO "fieldmetainfo" VALUES(NULL,'_subject','SUBJECT','"Subject"','java.lang.Long','com.gridnode.pdip.base.acl.model.SubjectRole','',0,0,1,1,1,'0',999,'','');
INSERT INTO "fieldmetainfo" VALUES(NULL,'_uId','UID','"UID"','java.lang.Long','com.gridnode.pdip.base.acl.model.SubjectRole','',0,0,0,1,0,'0',999,'','');

--------- ACCESS RIGHT
DELETE FROM "fieldmetainfo" WHERE "EntityObjectName" LIKE '%AccessRight';
INSERT INTO "fieldmetainfo" VALUES(NULL,'_uId','UID','"UID"','java.lang.Long','com.gridnode.pdip.base.acl.model.AccessRight','accessRight.uid',20,0,0,0,0,'0',999,'displayable=false'||chr(13)||chr(10)||'editable=false'||chr(13)||chr(10)||'mandatory=false'||chr(13)||chr(10),'type=uid'||chr(13)||chr(10));
INSERT INTO "fieldmetainfo" VALUES(NULL,'_roleUID','ROLE','"RoleUID"','java.lang.Long','com.gridnode.pdip.base.acl.model.AccessRight','accessRight.role',20,0,1,1,1,'0',2,'displayable=true'||chr(13)||chr(10)||'editable=false'||chr(13)||chr(10)||'editable.create=true'||chr(13)||chr(10)||'mandatory=true'||chr(13)||chr(10),'type=foreign'||chr(13)||chr(10)||'foreign.key=role.uid'||chr(13)||chr(10)||'foreign.display=role.description'||chr(13)||chr(10)||'foreign.cached=false'||chr(13)||chr(10));
INSERT INTO "fieldmetainfo" VALUES(NULL,'_feature','FEATURE','"Feature"','java.lang.String','com.gridnode.pdip.base.acl.model.AccessRight','accessRight.feature',0,0,1,1,1,'0',3,'displayable=true'||chr(13)||chr(10)||'editable=true'||chr(13)||chr(10)||'mandatory=true'||chr(13)||chr(10),'type=foreign'||chr(13)||chr(10)||'foreign.key=feature.feature'||chr(13)||chr(10)||'foreign.display=feature.description'||chr(13)||chr(10)||'foreign.cached=false'||chr(13)||chr(10));
INSERT INTO "fieldmetainfo" VALUES(NULL,'_descr','DESCRIPTION','"Description"','java.lang.String','com.gridnode.pdip.base.acl.model.AccessRight','accessRight.description',80,0,1,1,1,'0',1,'displayable=true'||chr(13)||chr(10)||'editable=true'||chr(13)||chr(10)||'mandatory=true'||chr(13)||chr(10),'type=text'||chr(13)||chr(10)||'text.length.max=80'||chr(13)||chr(10)||'text.length.min=1'||chr(13)||chr(10));
INSERT INTO "fieldmetainfo" VALUES(NULL,'_action','ACTION','"Action"','java.lang.String','com.gridnode.pdip.base.acl.model.AccessRight','accessRight.actionName',30,0,1,1,1,'0',4,'displayable=true'||chr(13)||chr(10)||'editable=true'||chr(13)||chr(10)||'mandatory=true'||chr(13)||chr(10),'type=text'||chr(13)||chr(10));
INSERT INTO "fieldmetainfo" VALUES(NULL,'_dataType','DATA_TYPE','"DataType"','java.lang.String','com.gridnode.pdip.base.acl.model.AccessRight','accessRight.dataType',30,0,0,1,1,'0',5,'displayable=true'||chr(13)||chr(10)||'editable=true'||chr(13)||chr(10)||'mandatory=false'||chr(13)||chr(10),'type=text'||chr(13)||chr(10));
INSERT INTO "fieldmetainfo" VALUES(NULL,'_criteria','CRITERIA','"Criteria"','com.gridnode.pdip.framework.db.filter.IDataFilter','com.gridnode.pdip.base.acl.model.AccessRight','accessRight.criteria',0,0,0,1,1,'0',6,'displayable=true'||chr(13)||chr(10)||'editable=true'||chr(13)||chr(10)||'mandatory=false'||chr(13)||chr(10),'type=other'||chr(13)||chr(10));
INSERT INTO "fieldmetainfo" VALUES(NULL,'_canDelete','CAN_DELETE','"CanDelete"','java.lang.Boolean','com.gridnode.pdip.base.acl.model.AccessRight','accessRight.canDelete',0,0,0,0,0,'0',999,'displayable=true'||chr(13)||chr(10)||'mandatory=false'||chr(13)||chr(10)||'editable=false'||chr(13)||chr(10),'type=enum'||chr(13)||chr(10)||'generic.yes=true'||chr(13)||chr(10)||'generic.no=false'||chr(13)||chr(10));
INSERT INTO "fieldmetainfo" VALUES(NULL,'_version','VERSION','"Version"','java.lang.Double','com.gridnode.pdip.base.acl.model.AccessRight','accessRight.version',0,0,0,0,0,'0',999,'displayable=false'||chr(13)||chr(10)||'editable=false'||chr(13)||chr(10)||'mandatory=false'||chr(13)||chr(10),'type=range'||chr(13)||chr(10));

--------- Feature
DELETE FROM "fieldmetainfo" WHERE "EntityObjectName" LIKE '%Feature';
INSERT INTO "fieldmetainfo" VALUES(NULL,'_version','VERSION','"Version"','java.lang.Double','com.gridnode.pdip.base.acl.model.Feature','feature.version',0,0,1,1,1,'0',999,'displayable=false'||chr(13)||chr(10)||'mandatory=false'||chr(13)||chr(10)||'editable=false'||chr(13)||chr(10),'type=range'||chr(13)||chr(10));
INSERT INTO "fieldmetainfo" VALUES(NULL,'_uId','UID','"UID"','java.lang.Long','com.gridnode.pdip.base.acl.model.Feature','feature.uid',20,0,0,0,0,'0',999,'displayable=false'||chr(13)||chr(10)||'mandatory=false'||chr(13)||chr(10)||'editable=false'||chr(13)||chr(10),'type=uid'||chr(13)||chr(10));
INSERT INTO "fieldmetainfo" VALUES(NULL,'_feature','FEATURE','"Feature"','java.lang.String','com.gridnode.pdip.base.acl.model.Feature','feature.feature',30,0,0,1,1,'0',999,'displayable=true'||chr(13)||chr(10)||'editable=false'||chr(13)||chr(10)||'mandatory=false'||chr(13)||chr(10),'type=text'||chr(13)||chr(10));
INSERT INTO "fieldmetainfo" VALUES(NULL,'_description','DESCRIPTION','"Description"','java.lang.String','com.gridnode.pdip.base.acl.model.Feature','feature.description',80,0,0,1,1,'0',999,'displayable=true'||chr(13)||chr(10)||'editable=false'||chr(13)||chr(10)||'mandatory=false'||chr(13)||chr(10),'type=text'||chr(13)||chr(10));
INSERT INTO "fieldmetainfo" VALUES(NULL,'_actions','ACTIONS','"Actions"','java.util.List','com.gridnode.pdip.base.acl.model.Feature','feature.actions',1024,0,0,1,1,'0',999,'displayable=true'||chr(13)||chr(10)||'editable=false'||chr(13)||chr(10)||'mandatory=false'||chr(13)||chr(10),'type=text'||chr(13)||chr(10)||'collection=true'||chr(13)||chr(10)||'collection.element=java.lang.String'||chr(13)||chr(10));
INSERT INTO "fieldmetainfo" VALUES(NULL,'_dataTypes','DATA_TYPES','"DataTypes"','java.util.List','com.gridnode.pdip.base.acl.model.Feature','feature.dataTypes',1024,0,0,1,1,'0',999,'displayable=true'||chr(13)||chr(10)||'editable=false'||chr(13)||chr(10)||'mandatory=false'||chr(13)||chr(10),'type=text'||chr(13)||chr(10)||'collection=true'||chr(13)||chr(10)||'collection.element=java.lang.String'||chr(13)||chr(10));
COMMIT;

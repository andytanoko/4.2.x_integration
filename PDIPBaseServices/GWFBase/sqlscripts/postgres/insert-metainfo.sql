SET client_encoding = 'UTF8';
SET check_function_bodies = false;
SET client_min_messages = warning;
SET search_path = appdb;


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

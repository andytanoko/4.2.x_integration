# MySQL-Front Dump 2.1
#
# Host: 127.0.0.1   Database: appdb
#--------------------------------------------------------
# Server version 4.0.0-alpha

use appdb;


#
# Dumping data for table 'entitymetainfo'
#

INSERT INTO `entitymetainfo` VALUES("com.gridnode.pdip.base.gwfbase.bpss.model.BpssReqBusinessActivity","BpssReqBusinessActivity","bpss_req_biz_activity");
INSERT INTO `entitymetainfo` VALUES("com.gridnode.pdip.base.gwfbase.bpss.model.BpssResBusinessActivity","BpssResBusinessActivity","bpss_res_biz_activity");
INSERT INTO `entitymetainfo` VALUES("com.gridnode.pdip.base.gwfbase.bpss.model.BpssBusinessTransActivity","BpssBusinessTransActivity","bpss_biz_tran_activity");
INSERT INTO `entitymetainfo` VALUES("com.gridnode.pdip.base.gwfbase.bpss.model.BpssCollaborationActivity","BpssCollaborationActivity","bpss_coll_activity");
INSERT INTO `entitymetainfo` VALUES("com.gridnode.pdip.base.gwfbase.bpss.model.BpssBusinessTrans","BpssBusinessTrans","bpss_biz_tran");
INSERT INTO `entitymetainfo` VALUES("com.gridnode.pdip.base.gwfbase.bpss.model.BpssBinaryCollaboration","BpssBinaryCollaboration","bpss_bin_coll");
INSERT INTO `entitymetainfo` VALUES("com.gridnode.pdip.base.gwfbase.bpss.model.BpssBinaryCollaborationActivity","BpssBinaryCollaborationActivity","bpss_bin_coll_act");
INSERT INTO `entitymetainfo` VALUES("com.gridnode.pdip.base.gwfbase.bpss.model.BpssMultiPartyCollaboration","BpssMultiPartyCollaboration","bpss_multiparty_coll");
INSERT INTO `entitymetainfo` VALUES("com.gridnode.pdip.base.gwfbase.bpss.model.BpssTransition","BpssTransition","bpss_transition");
INSERT INTO `entitymetainfo` VALUES("com.gridnode.pdip.base.gwfbase.bpss.model.BpssCompletionState","BpssCompletionState","bpss_completion_state");
INSERT INTO `entitymetainfo` VALUES("com.gridnode.pdip.base.gwfbase.bpss.model.BpssFork","BpssFork","bpss_fork");
INSERT INTO `entitymetainfo` VALUES("com.gridnode.pdip.base.gwfbase.bpss.model.BpssJoin","BpssJoin","bpss_join");
INSERT INTO `entitymetainfo` VALUES("com.gridnode.pdip.base.gwfbase.bpss.model.BpssProcessSpecification","BpssProcessSpecification","bpss_proc_spec");
INSERT INTO `entitymetainfo` VALUES("com.gridnode.pdip.base.gwfbase.bpss.model.BpssProcessSpecEntry","BpssProcessSpecEntry","bpss_proc_spec_entry");
INSERT INTO `entitymetainfo` VALUES("com.gridnode.pdip.base.gwfbase.bpss.model.BpssStart","BpssStart","bpss_start");
INSERT INTO `entitymetainfo` VALUES("com.gridnode.pdip.base.gwfbase.bpss.model.BpssBusinessPartnerRole","BpssBusinessPartnerRole","bpss_biz_partner_role");
INSERT INTO `entitymetainfo` VALUES("com.gridnode.pdip.base.gwfbase.bpss.model.BpssBusinessDocument","BpssBusinessDocument","bpss_businessdocument");
INSERT INTO `entitymetainfo` VALUES("com.gridnode.pdip.base.gwfbase.bpss.model.BpssDocumentation","BpssDocumentation","bpss_documentation");
INSERT INTO `entitymetainfo` VALUES("com.gridnode.pdip.base.gwfbase.bpss.model.BpssDocumentEnvelope","BpssDocumentEnvelope","bpss_documentenvelope");

INSERT INTO entitymetainfo VALUES("com.gridnode.pdip.base.gwfbase.xpdl.model.XpdlActivity","XpdlActivity","xpdl_activity");
INSERT INTO entitymetainfo VALUES("com.gridnode.pdip.base.gwfbase.xpdl.model.XpdlApplication","XpdlApplication","xpdl_application");
INSERT INTO entitymetainfo VALUES("com.gridnode.pdip.base.gwfbase.xpdl.model.XpdlDataField","XpdlDataField","xpdl_datafield");
INSERT INTO entitymetainfo VALUES("com.gridnode.pdip.base.gwfbase.xpdl.model.XpdlExternalPackage","XpdlExternalPackage","xpdl_externalpackage");
INSERT INTO entitymetainfo VALUES("com.gridnode.pdip.base.gwfbase.xpdl.model.XpdlFormalParam","XpdlFormalParam","xpdl_formalparam");
INSERT INTO entitymetainfo VALUES("com.gridnode.pdip.base.gwfbase.xpdl.model.XpdlPackage","XpdlPackage","xpdl_package");
INSERT INTO entitymetainfo VALUES("com.gridnode.pdip.base.gwfbase.xpdl.model.XpdlParticipant","XpdlParticipant","xpdl_participant");
INSERT INTO entitymetainfo VALUES("com.gridnode.pdip.base.gwfbase.xpdl.model.XpdlParticipantList","XpdlParticipantList","xpdl_participantlist");
INSERT INTO entitymetainfo VALUES("com.gridnode.pdip.base.gwfbase.xpdl.model.XpdlProcess","XpdlProcess","xpdl_process");
INSERT INTO entitymetainfo VALUES("com.gridnode.pdip.base.gwfbase.xpdl.model.XpdlSubFlow","XpdlSubFlow","xpdl_subflow");
INSERT INTO entitymetainfo VALUES("com.gridnode.pdip.base.gwfbase.xpdl.model.XpdlTool","XpdlTool","xpdl_tool");
INSERT INTO entitymetainfo VALUES("com.gridnode.pdip.base.gwfbase.xpdl.model.XpdlTransition","XpdlTransition","xpdl_transition");
INSERT INTO entitymetainfo VALUES("com.gridnode.pdip.base.gwfbase.xpdl.model.XpdlTransitionRef","XpdlTransitionRef","xpdl_transitionref");
INSERT INTO entitymetainfo VALUES("com.gridnode.pdip.base.gwfbase.xpdl.model.XpdlTransitionRestriction","XpdlTransitionRestriction","xpdl_transitionrestriction");
INSERT INTO entitymetainfo VALUES("com.gridnode.pdip.base.gwfbase.xpdl.model.XpdlTypeDeclaration","XpdlTypeDeclaration","xpdl_typedeclaration");
INSERT INTO entitymetainfo VALUES("com.gridnode.pdip.base.gwfbase.xpdl.model.XpdlComplexDataType","XpdlComplexDataType","xpdl_complexdatatype");


#
# Dumping data for table 'fieldmetainfo'
#
INSERT INTO fieldmetainfo VALUES (NULL,"_uId","UID","UID","java.lang.Long","com.gridnode.pdip.base.gwfbase.bpss.model.BpssReqBusinessActivity","",0,"0","0","1","1","",999,'displayable=false','');
INSERT INTO fieldmetainfo VALUES (NULL,"_businessActionName","NAME","Name","java.lang.String","com.gridnode.pdip.base.gwfbase.bpss.model.BpssReqBusinessActivity","",50,"0","0","1","1","",999,'displayable=false','');
INSERT INTO fieldmetainfo VALUES (NULL,"_isIntelligibleChkReq","ISINTELLIGIBLE_REQUIRED","IsIntelligibleChkReq","java.lang.Boolean","com.gridnode.pdip.base.gwfbase.bpss.model.BpssReqBusinessActivity","",0,"0","0","1","1","",999,'displayable=false','');
INSERT INTO fieldmetainfo VALUES (NULL,"_isAuthReq","ISAUTH_REQUIRED","IsAuthReq","java.lang.Boolean","com.gridnode.pdip.base.gwfbase.bpss.model.BpssReqBusinessActivity","",0,"0","0","1","1","",999,'displayable=false','');
INSERT INTO fieldmetainfo VALUES (NULL,"_timeToAckReceipt","TIMETO_ACK_RECEIPT","TimeToAckReceipt","java.lang.String","com.gridnode.pdip.base.gwfbase.bpss.model.BpssReqBusinessActivity","",0,"0","0","1","1","",999,'displayable=false','');
INSERT INTO fieldmetainfo VALUES (NULL,"_isNonRepudiationReq","ISNONREPUDIATION_REQUIRED","IsNonRepudiationReq","java.lang.Boolean","com.gridnode.pdip.base.gwfbase.bpss.model.BpssReqBusinessActivity","",0,"0","0","1","1","",999,'displayable=false','');
INSERT INTO fieldmetainfo VALUES (NULL,"_isNonRepudiationOfReceiptReq","ISNONREPUDIATION_RECEIPT_REQUIRED","IsNonRepudiationOfReceiptReq","java.lang.Boolean","com.gridnode.pdip.base.gwfbase.bpss.model.BpssReqBusinessActivity","",0,"0","0","1","1","",999,'displayable=false','');
INSERT INTO fieldmetainfo VALUES (NULL,"_timeToAckAccept","TIMETO_ACK_ACCEPT","TimeToAckAccept","java.lang.String","com.gridnode.pdip.base.gwfbase.bpss.model.BpssReqBusinessActivity","",0,"0","0","1","1","",999,'displayable=false','');


INSERT INTO fieldmetainfo VALUES (NULL,"_uId","UID","UID","java.lang.Long","com.gridnode.pdip.base.gwfbase.bpss.model.BpssResBusinessActivity","",0,"0","0","1","1","",999,'displayable=false','');
INSERT INTO fieldmetainfo VALUES (NULL,"_businessActionName","NAME","Name","java.lang.String","com.gridnode.pdip.base.gwfbase.bpss.model.BpssResBusinessActivity","",50,"0","0","1","1","",999,'displayable=false','');
INSERT INTO fieldmetainfo VALUES (NULL,"_isIntelligibleChkReq","ISINTELLIGIBLE_REQUIRED","IsIntelligibleChkReq","java.lang.Boolean","com.gridnode.pdip.base.gwfbase.bpss.model.BpssResBusinessActivity","",0,"0","0","1","1","",999,'displayable=false','');
INSERT INTO fieldmetainfo VALUES (NULL,"_isAuthReq","ISAUTH_REQUIRED","IsAuthReq","java.lang.Boolean","com.gridnode.pdip.base.gwfbase.bpss.model.BpssResBusinessActivity","",0,"0","0","1","1","",999,'displayable=false','');
INSERT INTO fieldmetainfo VALUES (NULL,"_timeToAckReceipt","TIMETO_ACK_RECEIPT","TimeToAckReceipt","java.lang.String","com.gridnode.pdip.base.gwfbase.bpss.model.BpssResBusinessActivity","",0,"0","0","1","1","",999,'displayable=false','');
INSERT INTO fieldmetainfo VALUES (NULL,"_isNonRepudiationReq","ISNONREPUDIATION_REQUIRED","IsNonRepudiationReq","java.lang.Boolean","com.gridnode.pdip.base.gwfbase.bpss.model.BpssResBusinessActivity","",0,"0","0","1","1","",999,'displayable=false','');
INSERT INTO fieldmetainfo VALUES (NULL,"_isNonRepudiationOfReceiptReq","ISNONREPUDIATION_RECEIPT_REQUIRED","IsNonRepudiationOfReceiptReq","java.lang.Boolean","com.gridnode.pdip.base.gwfbase.bpss.model.BpssResBusinessActivity","",0,"0","0","1","1","",999,'displayable=false','');

INSERT INTO fieldmetainfo VALUES (NULL,"_uId","UID","UID","java.lang.Long","com.gridnode.pdip.base.gwfbase.bpss.model.BpssBusinessTransActivity","",0,"0","0","1","1","",999,'displayable=false','');
INSERT INTO fieldmetainfo VALUES (NULL,"_activityName","NAME","Name","java.lang.String","com.gridnode.pdip.base.gwfbase.bpss.model.BpssBusinessTransActivity","",50,"0","0","1","1","",999,'displayable=false','');
INSERT INTO fieldmetainfo VALUES (NULL,"_timeToPerform","TIMETO_PERFORM","TimeToPerform","java.lang.String","com.gridnode.pdip.base.gwfbase.bpss.model.BpssBusinessTransActivity","",0,"0","0","1","1","",999,'displayable=false','');
INSERT INTO fieldmetainfo VALUES (NULL,"_isConcurrent","ISCONCURRENT","IsConcurrent","java.lang.Boolean","com.gridnode.pdip.base.gwfbase.bpss.model.BpssBusinessTransActivity","",0,"0","0","1","1","",999,'displayable=false','');
INSERT INTO fieldmetainfo VALUES (NULL,"_isLegallyBinding","ISLEGALLY_BINDING","IsLegallyBinding","java.lang.Boolean","com.gridnode.pdip.base.gwfbase.bpss.model.BpssBusinessTransActivity","",0,"0","0","1","1","",999,'displayable=false','');
INSERT INTO fieldmetainfo VALUES (NULL,"_businessTransUId","BUSINESS_TRANS_UID","BusinessTransUId","java.lang.Long","com.gridnode.pdip.base.gwfbase.bpss.model.BpssBusinessTransActivity","",0,"0","0","1","1","",999,'displayable=false','');
INSERT INTO fieldmetainfo VALUES (NULL,"_fromAuthorizedRole","FROM_AUTHORIZED_ROLE","FromAuthorizedRole","java.lang.String","com.gridnode.pdip.base.gwfbase.bpss.model.BpssBusinessTransActivity","",50,"0","0","1","1","",999,'displayable=false','');
INSERT INTO fieldmetainfo VALUES (NULL,"_toAuthorizedRole","TO_AUTHORIZED_ROLE","ToAuthorizedRole","java.lang.String","com.gridnode.pdip.base.gwfbase.bpss.model.BpssBusinessTransActivity","",50,"0","0","1","1","",999,'displayable=false','');

INSERT INTO fieldmetainfo VALUES (NULL,"_uId","UID","UID","java.lang.Long","com.gridnode.pdip.base.gwfbase.bpss.model.BpssCollaborationActivity","",0,"0","0","1","1","",999,'displayable=false','');
INSERT INTO fieldmetainfo VALUES (NULL,"_activityName","NAME","Name","java.lang.String","com.gridnode.pdip.base.gwfbase.bpss.model.BpssCollaborationActivity","",50,"0","0","1","1","",999,'displayable=false','');
INSERT INTO fieldmetainfo VALUES (NULL,"_binaryCollaborationUId","BINARY_COLLABORATION_UID","BinCollProcessUId","java.lang.Long","com.gridnode.pdip.base.gwfbase.bpss.model.BpssCollaborationActivity","",0,"0","0","1","1","",999,'displayable=false','');

INSERT INTO fieldmetainfo VALUES (NULL,"_uId","UID","UID","java.lang.Long","com.gridnode.pdip.base.gwfbase.bpss.model.BpssBusinessTrans","",0,"0","0","1","1","",999,'displayable=false','');
INSERT INTO fieldmetainfo VALUES (NULL,"_businessTransName","NAME","Name","java.lang.String","com.gridnode.pdip.base.gwfbase.bpss.model.BpssBusinessTrans","",50,"0","0","1","1","",999,'displayable=false','');
INSERT INTO fieldmetainfo VALUES (NULL,"_pattern","PATTERN","Pattern","java.lang.String","com.gridnode.pdip.base.gwfbase.bpss.model.BpssBusinessTrans","",50,"0","0","1","1","",999,'displayable=false','');
INSERT INTO fieldmetainfo VALUES (NULL,"_preCondition","PRE_CONDITION","PreCondition","java.lang.String","com.gridnode.pdip.base.gwfbase.bpss.model.BpssBusinessTrans","",50,"0","0","1","1","",999,'displayable=false','');
INSERT INTO fieldmetainfo VALUES (NULL,"_postCondition","POST_CONDITION","PostCondition","java.lang.String","com.gridnode.pdip.base.gwfbase.bpss.model.BpssBusinessTrans","",50,"0","0","1","1","",999,'displayable=false','');
INSERT INTO fieldmetainfo VALUES (NULL,"_isGuaranteedDeliveryRequired","ISGUARANTEED_DELIVERY_REQUIRED","IsDeliveryReq","java.lang.Boolean","com.gridnode.pdip.base.gwfbase.bpss.model.BpssBusinessTrans","",0,"0","0","1","1","",999,'displayable=false','');
INSERT INTO fieldmetainfo VALUES (NULL,"_beginsWhen","BEGINS_WHEN","BeginsWhen","java.lang.String","com.gridnode.pdip.base.gwfbase.bpss.model.BpssBusinessTrans","",50,"0","0","1","1","",999,'displayable=false','');
INSERT INTO fieldmetainfo VALUES (NULL,"_endsWhen","ENDS_WHEN","EndsWhen","java.lang.String","com.gridnode.pdip.base.gwfbase.bpss.model.BpssBusinessTrans","",50,"0","0","1","1","",999,'displayable=false','');
INSERT INTO fieldmetainfo VALUES (NULL,"_bpssReqBusinessActivity","BPSS_REQ_ACTIVITY","ReqUId","java.lang.Long","com.gridnode.pdip.base.gwfbase.bpss.model.BpssBusinessTrans","",0,"0","0","1","1","",999,'displayable=false','');
INSERT INTO fieldmetainfo VALUES (NULL,"_bpssResBusinessActivity","BPSS_RES_ACTIVITY","ResUId","java.lang.Long","com.gridnode.pdip.base.gwfbase.bpss.model.BpssBusinessTrans","",0,"0","0","1","1","",999,'displayable=false','');


INSERT INTO fieldmetainfo VALUES (NULL,"_uId","UID","UID","java.lang.Long","com.gridnode.pdip.base.gwfbase.bpss.model.BpssBinaryCollaboration","",0,"0","0","1","1","",999,'displayable=false','');
INSERT INTO fieldmetainfo VALUES (NULL,"_binaryCollaborationName","NAME","Name","java.lang.String","com.gridnode.pdip.base.gwfbase.bpss.model.BpssBinaryCollaboration","",50,"0","0","1","1","",999,'displayable=false','');
INSERT INTO fieldmetainfo VALUES (NULL,"_pattern","PATTERN","Pattern","java.lang.String","com.gridnode.pdip.base.gwfbase.bpss.model.BpssBinaryCollaboration","",50,"0","0","1","1","",999,'displayable=false','');
INSERT INTO fieldmetainfo VALUES (NULL,"_timeToPerform","TIMETO_PERFORM","TimeToPerform","java.lang.String","com.gridnode.pdip.base.gwfbase.bpss.model.BpssBinaryCollaboration","",0,"0","0","1","1","",999,'displayable=false','');
INSERT INTO fieldmetainfo VALUES (NULL,"_preCondition","PRE_CONDITION","PreCondition","java.lang.String","com.gridnode.pdip.base.gwfbase.bpss.model.BpssBinaryCollaboration","",50,"0","0","1","1","",999,'displayable=false','');
INSERT INTO fieldmetainfo VALUES (NULL,"_postCondition","POST_CONDITION","PostCondition","java.lang.String","com.gridnode.pdip.base.gwfbase.bpss.model.BpssBinaryCollaboration","",50,"0","0","1","1","",999,'displayable=false','');
INSERT INTO fieldmetainfo VALUES (NULL,"_beginsWhen","BEGINS_WHEN","BeginsWhen","java.lang.String","com.gridnode.pdip.base.gwfbase.bpss.model.BpssBinaryCollaboration","",50,"0","0","1","1","",999,'displayable=false','');
INSERT INTO fieldmetainfo VALUES (NULL,"_endsWhen","ENDS_WHEN","EndsWhen","java.lang.String","com.gridnode.pdip.base.gwfbase.bpss.model.BpssBinaryCollaboration","",50,"0","0","1","1","",999,'displayable=false','');

INSERT INTO fieldmetainfo VALUES (NULL,"_uId","UID","UID","java.lang.Long","com.gridnode.pdip.base.gwfbase.bpss.model.BpssMultiPartyCollaboration","",0,"0","0","1","1","",999,'displayable=false','');
INSERT INTO fieldmetainfo VALUES (NULL,"_binaryCollaborationName","NAME","Name","java.lang.String","com.gridnode.pdip.base.gwfbase.bpss.model.BpssMultiPartyCollaboration","",50,"0","0","1","1","",999,'displayable=false','');

INSERT INTO fieldmetainfo VALUES (NULL,"_uId","UID","UID","java.lang.Long","com.gridnode.pdip.base.gwfbase.bpss.model.BpssTransition","",0,"0","0","1","1","",999,'displayable=false','');
INSERT INTO fieldmetainfo VALUES (NULL,"_processUId","PROCESS_UID","ProcessUId","java.lang.Long","com.gridnode.pdip.base.gwfbase.bpss.model.BpssTransition","",0,"0","0","1","1","",999,'displayable=false','');
INSERT INTO fieldmetainfo VALUES (NULL,"_onInitiation","ON_INITIATION","OnInitiation","java.lang.Boolean","com.gridnode.pdip.base.gwfbase.bpss.model.BpssTransition","",0,"0","0","1","1","",999,'displayable=false','');
INSERT INTO fieldmetainfo VALUES (NULL,"_conditionGuard","CONDITION_GUARD","ConditionGuard","java.lang.String","com.gridnode.pdip.base.gwfbase.bpss.model.BpssTransition","",50,"0","0","1","1","",999,'displayable=false','');
INSERT INTO fieldmetainfo VALUES (NULL,"_fromBusinessStateKey","FROM_BUSINESS_STATE_KEY","FromBusinessStateKey","java.lang.String","com.gridnode.pdip.base.gwfbase.bpss.model.BpssTransition","",50,"0","0","1","1","",999,'displayable=false','');
INSERT INTO fieldmetainfo VALUES (NULL,"_toBusinessStateKey","TO_BUSINESS_STATE_KEY","ToBusinessStateKey","java.lang.String","com.gridnode.pdip.base.gwfbase.bpss.model.BpssTransition","",50,"0","0","1","1","",999,'displayable=false','');
INSERT INTO fieldmetainfo VALUES (NULL,"_processType","PROCESS_TYPE","ProcessType","java.lang.String","com.gridnode.pdip.base.gwfbase.bpss.model.BpssTransition","",50,"0","0","1","1","",999,'displayable=false','');
INSERT INTO fieldmetainfo VALUES (NULL,"_conditionExpression","CONDITION_EXPRESSION","ConditionExpression","java.lang.String","com.gridnode.pdip.base.gwfbase.bpss.model.BpssTransition","",50,"0","0","1","1","",999,'displayable=false','');
INSERT INTO fieldmetainfo VALUES (NULL,"_expressionLanguage","EXPRESSION_LANGUAGE","ExpressionLanguage","java.lang.String","com.gridnode.pdip.base.gwfbase.bpss.model.BpssTransition","",50,"0","0","1","1","",999,'displayable=false','');


INSERT INTO fieldmetainfo VALUES (NULL,"_uId","UID","UID","java.lang.Long","com.gridnode.pdip.base.gwfbase.bpss.model.BpssCompletionState","",0,"0","0","1","1","",999,'displayable=false','');
INSERT INTO fieldmetainfo VALUES (NULL,"_processUId","PROCESS_UID","ProcessUId","java.lang.Long","com.gridnode.pdip.base.gwfbase.bpss.model.BpssCompletionState","",0,"0","0","1","1","",999,'displayable=false','');
INSERT INTO fieldmetainfo VALUES (NULL,"_processType","PROCESS_TYPE","ProcessType","java.lang.String","com.gridnode.pdip.base.gwfbase.bpss.model.BpssCompletionState","",50,"0","0","1","1","",999,'displayable=false','');
INSERT INTO fieldmetainfo VALUES (NULL,"_mpcUId","MPC_UID","MpcUId","java.lang.Long","com.gridnode.pdip.base.gwfbase.bpss.model.BpssCompletionState","",0,"0","0","1","1","",999,'displayable=false','');
INSERT INTO fieldmetainfo VALUES (NULL,"_conditionGuard","CONDITION_GUARD","ConditionGuard","java.lang.String","com.gridnode.pdip.base.gwfbase.bpss.model.BpssCompletionState","",50,"0","0","1","1","",999,'displayable=false','');
INSERT INTO fieldmetainfo VALUES (NULL,"_fromBusinessStateKey","FROM_BUSINESS_STATE_KEY","FromBusinessStateKey","java.lang.String","com.gridnode.pdip.base.gwfbase.bpss.model.BpssCompletionState","",50,"0","0","1","1","",999,'displayable=false','');
INSERT INTO fieldmetainfo VALUES (NULL,"_conditionExpression","CONDITION_EXPRESSION","ConditionExpression","java.lang.String","com.gridnode.pdip.base.gwfbase.bpss.model.BpssCompletionState","",50,"0","0","1","1","",999,'displayable=false','');
INSERT INTO fieldmetainfo VALUES (NULL,"_expressionLanguage","EXPRESSION_LANGUAGE","ExpressionLanguage","java.lang.String","com.gridnode.pdip.base.gwfbase.bpss.model.BpssCompletionState","",50,"0","0","1","1","",999,'displayable=false','');
INSERT INTO fieldmetainfo VALUES (NULL,"_completionType","COMPLETION_TYPE","CompletionType","java.lang.String","com.gridnode.pdip.base.gwfbase.bpss.model.BpssCompletionState","",50,"0","0","1","1","",999,'displayable=false','');

INSERT INTO fieldmetainfo VALUES (NULL,"_uId","UID","UID","java.lang.Long","com.gridnode.pdip.base.gwfbase.bpss.model.BpssFork","",0,"0","0","1","1","",999,'displayable=false','');
INSERT INTO fieldmetainfo VALUES (NULL,"_name","NAME","Name","java.lang.String","com.gridnode.pdip.base.gwfbase.bpss.model.BpssFork","",50,"0","0","1","1","",999,'displayable=false','');

INSERT INTO fieldmetainfo VALUES (NULL,"_uId","UID","UID","java.lang.Long","com.gridnode.pdip.base.gwfbase.bpss.model.BpssJoin","",0,"0","0","1","1","",999,'displayable=false','');
INSERT INTO fieldmetainfo VALUES (NULL,"_name","NAME","Name","java.lang.String","com.gridnode.pdip.base.gwfbase.bpss.model.BpssJoin","",50,"0","0","1","1","",999,'displayable=false','');
INSERT INTO fieldmetainfo VALUES (NULL,"_waitForAll","WAITFOR_ALL","WaitForAll","java.lang.Boolean","com.gridnode.pdip.base.gwfbase.bpss.model.BpssJoin","",0,"0","0","1","1","",999,'displayable=false','');

# added _maxConcurrency for all process entities

INSERT INTO fieldmetainfo VALUES (NULL,"_maxConcurrency","MAX_CONCURRENCY","MaxConcurrency","java.lang.Integer","com.gridnode.pdip.base.gwfbase.bpss.model.BpssMultiPartyCollaboration","",0,"0","0","1","1","",999,'displayable=false','');
INSERT INTO fieldmetainfo VALUES (NULL,"_maxConcurrency","MAX_CONCURRENCY","MaxConcurrency","java.lang.Integer","com.gridnode.pdip.base.gwfbase.bpss.model.BpssBinaryCollaboration","",0,"0","0","1","1","",999,'displayable=false','');
INSERT INTO fieldmetainfo VALUES (NULL,"_maxConcurrency","MAX_CONCURRENCY","MaxConcurrency","java.lang.Integer","com.gridnode.pdip.base.gwfbase.bpss.model.BpssBusinessTrans","",0,"0","0","1","1","",999,'displayable=false','');

INSERT INTO fieldmetainfo VALUES (NULL,"_uId","UID","UID","java.lang.Long","com.gridnode.pdip.base.gwfbase.bpss.model.BpssProcessSpecEntry","",0,"0","0","1","1","",999,'displayable=false','');
INSERT INTO fieldmetainfo VALUES (NULL,"_specUId","SPEC_UID","SpecUId","java.lang.Long","com.gridnode.pdip.base.gwfbase.bpss.model.BpssProcessSpecEntry","",0,"0","0","1","1","",999,'displayable=false','');
INSERT INTO fieldmetainfo VALUES (NULL,"_entryUId","ENTRY_UID","EntryUId","java.lang.Long","com.gridnode.pdip.base.gwfbase.bpss.model.BpssProcessSpecEntry","",0,"0","0","1","1","",999,'displayable=false','');
INSERT INTO fieldmetainfo VALUES (NULL,"_entryName","ENTRY_NAME","Name","java.lang.String","com.gridnode.pdip.base.gwfbase.bpss.model.BpssProcessSpecEntry","",0,"0","0","1","1","",999,'displayable=false','');
INSERT INTO fieldmetainfo VALUES (NULL,"_entryType","ENTRY_TYPE","Type","java.lang.String","com.gridnode.pdip.base.gwfbase.bpss.model.BpssProcessSpecEntry","",0,"0","0","1","1","",999,'displayable=false','');
INSERT INTO fieldmetainfo VALUES (NULL,"_parentEntryUId","PARENT_ENTRY_UID","ParentEntryUId","java.lang.Long","com.gridnode.pdip.base.gwfbase.bpss.model.BpssProcessSpecEntry","",0,"0","0","1","1","",999,'displayable=false','');

INSERT INTO fieldmetainfo VALUES (NULL,"_uId","UID","UID","java.lang.Long","com.gridnode.pdip.base.gwfbase.bpss.model.BpssProcessSpecification","",0,"0","0","1","1","",999,'displayable=false','');
INSERT INTO fieldmetainfo VALUES (NULL,"_specVersion","VERSION","Version","java.lang.String","com.gridnode.pdip.base.gwfbase.bpss.model.BpssProcessSpecification","",0,"0","0","1","1","",999,'displayable=false','');
INSERT INTO fieldmetainfo VALUES (NULL,"_specUUId","UUID","UUId","java.lang.String","com.gridnode.pdip.base.gwfbase.bpss.model.BpssProcessSpecification","",0,"0","0","1","1","",999,'displayable=false','');
INSERT INTO fieldmetainfo VALUES (NULL,"_specName","NAME","Name","java.lang.String","com.gridnode.pdip.base.gwfbase.bpss.model.BpssProcessSpecification","",0,"0","0","1","1","",999,'displayable=false','');
INSERT INTO fieldmetainfo VALUES (NULL,"_specTimestamp","TIMESTAMP","Timestamp","java.lang.String","com.gridnode.pdip.base.gwfbase.bpss.model.BpssProcessSpecification","",0,"0","0","1","1","",999,'displayable=false','');

INSERT INTO fieldmetainfo VALUES (NULL,"_uId","UID","UID","java.lang.Long","com.gridnode.pdip.base.gwfbase.bpss.model.BpssStart","",0,"0","0","1","1","",999,'displayable=false','');
INSERT INTO fieldmetainfo VALUES (NULL,"_processUId","PROCESS_UID","ProcessUId","java.lang.Long","com.gridnode.pdip.base.gwfbase.bpss.model.BpssStart","",0,"0","0","1","1","",999,'displayable=false','');
INSERT INTO fieldmetainfo VALUES (NULL,"_isDownLink","ISDOWNLINK","IsDownLink","java.lang.Boolean","com.gridnode.pdip.base.gwfbase.bpss.model.BpssStart","",0,"0","0","1","1","",999,'displayable=false','');
INSERT INTO fieldmetainfo VALUES (NULL,"_toBusinessStateKey","TO_BUSINESS_STATE_KEY","ToBusinessStateKey","java.lang.String","com.gridnode.pdip.base.gwfbase.bpss.model.BpssStart","",50,"0","0","1","1","",999,'displayable=false','');

INSERT INTO fieldmetainfo VALUES (NULL,"_uId","UID","UID","java.lang.Long","com.gridnode.pdip.base.gwfbase.bpss.model.BpssBinaryCollaborationActivity","",0,"0","0","1","1","",999,'displayable=false','');
INSERT INTO fieldmetainfo VALUES (NULL,"_activityName","NAME","Name","java.lang.String","com.gridnode.pdip.base.gwfbase.bpss.model.BpssBinaryCollaborationActivity","",50,"0","0","1","1","",999,'displayable=false','');
INSERT INTO fieldmetainfo VALUES (NULL,"_binaryCollaborationUId","BINARY_COLLABORATION_UID","BinCollProcessUId","java.lang.Long","com.gridnode.pdip.base.gwfbase.bpss.model.BpssBinaryCollaborationActivity","",0,"0","0","1","1","",999,'displayable=false','');
INSERT INTO fieldmetainfo VALUES (NULL,"_downLinkUId","DOWNLINK_UID","DownLinkUId","java.lang.Long","com.gridnode.pdip.base.gwfbase.bpss.model.BpssBinaryCollaborationActivity","",0,"0","0","1","1","",999,'displayable=false','');

# added BusinessPartnerRole
INSERT INTO fieldmetainfo VALUES (NULL,"_uId","UID","UID","java.lang.Long","com.gridnode.pdip.base.gwfbase.bpss.model.BpssBusinessPartnerRole","",0,"0","0","1","1","",999,'displayable=false','');
INSERT INTO fieldmetainfo VALUES (NULL,"_roleName","NAME","Name","java.lang.String","com.gridnode.pdip.base.gwfbase.bpss.model.BpssBusinessPartnerRole","",50,"0","0","1","1","",999,'displayable=false','');

INSERT INTO fieldmetainfo VALUES(NULL,"_uId","UID","UID","java.lang.Long","com.gridnode.pdip.base.gwfbase.bpss.model.BpssBusinessDocument","","0","0","0","0","0","","999",'displayable=false','');
INSERT INTO fieldmetainfo VALUES(NULL,"_name","NAME","Name","java.lang.String","com.gridnode.pdip.base.gwfbase.bpss.model.BpssBusinessDocument","NAME","50","0","1","1","1","","1",'displayable=false','');
INSERT INTO fieldmetainfo VALUES(NULL,"_expressionLanguage","EXPRESSION_LANGUAGE","ExpressionLanguage","java.lang.String","com.gridnode.pdip.base.gwfbase.bpss.model.BpssBusinessDocument","EXPRESSION_LANGUAGE",50,"0","0","1","1","",999,'displayable=false','');
INSERT INTO fieldmetainfo VALUES(NULL,"_conditionExpr","CONDITION_EXPR","ConditionExpr","java.lang.String","com.gridnode.pdip.base.gwfbase.bpss.model.BpssBusinessDocument","CONDITION_EXPR","50","0","1","1","1","","2",'displayable=false','');
INSERT INTO fieldmetainfo VALUES(NULL,"_specElement","SPEC_ELEMENT","SpecElement","java.lang.String","com.gridnode.pdip.base.gwfbase.bpss.model.BpssBusinessDocument","SPEC_ELEMENT","50","0","1","1","1","","3",'displayable=false','');
INSERT INTO fieldmetainfo VALUES(NULL,"_specLocation","SPEC_LOCATION","SpecLocation","java.lang.String","com.gridnode.pdip.base.gwfbase.bpss.model.BpssBusinessDocument","SPEC_LOCATION","50","0","1","1","1","","4",'displayable=false','');

INSERT INTO fieldmetainfo VALUES(NULL,"_uId","UID","UID","java.lang.Long","com.gridnode.pdip.base.gwfbase.bpss.model.BpssDocumentation","","0","0","0","0","0","","999",'displayable=false','');
INSERT INTO fieldmetainfo VALUES(NULL,"_uri","URI","Uri","java.lang.String","com.gridnode.pdip.base.gwfbase.bpss.model.BpssDocumentation","URI","50","0","1","1","1","","1",'displayable=false','');
INSERT INTO fieldmetainfo VALUES(NULL,"_documentation","DOCUMENTATION","Documentation","java.lang.String","com.gridnode.pdip.base.gwfbase.bpss.model.BpssDocumentation","NAME","80","0","1","1","1","","1",'displayable=false','');

INSERT INTO fieldmetainfo VALUES(NULL,"_uId","UID","UID","java.lang.Long","com.gridnode.pdip.base.gwfbase.bpss.model.BpssDocumentEnvelope","","0","0","0","0","0","","999",'displayable=false','');
INSERT INTO fieldmetainfo VALUES(NULL,"_businessDocumentName","BUSINESS_DOCUMENT_NAME","BusinessDocumentName","java.lang.String","com.gridnode.pdip.base.gwfbase.bpss.model.BpssDocumentEnvelope","BUSINESS_DOCUMENT_NAME","50","0","1","1","1","","1",'displayable=false','');
INSERT INTO fieldmetainfo VALUES(NULL,"_businessDocumentIDRef","BUSINESS_DOCUMENTID_REF","BusinessDocumentIDRef","java.lang.String","com.gridnode.pdip.base.gwfbase.bpss.model.BpssDocumentEnvelope","BUSINESS_DOCUMENTID_REF","50","0","1","1","1","","1",'displayable=false','');
INSERT INTO fieldmetainfo VALUES (NULL,"_isPositiveResponse","ISPOSITIVE_RESPONSE","IsPositiveResponse","java.lang.Boolean","com.gridnode.pdip.base.gwfbase.bpss.model.BpssDocumentEnvelope","",0,"0","0","1","1","",999,'displayable=false','');
INSERT INTO fieldmetainfo VALUES (NULL,"_isAuthenticated","ISAUTHENTICATED","IsAuthenticated","java.lang.Boolean","com.gridnode.pdip.base.gwfbase.bpss.model.BpssDocumentEnvelope","",0,"0","0","1","1","",999,'displayable=false','');
INSERT INTO fieldmetainfo VALUES (NULL,"_isConfidential","ISCONFIDENTIAL","IsConfidential","java.lang.Boolean","com.gridnode.pdip.base.gwfbase.bpss.model.BpssDocumentEnvelope","",0,"0","0","1","1","",999,'displayable=false','');
INSERT INTO fieldmetainfo VALUES (NULL,"_isTamperProof","ISTAMPERPROOF","IsTamperProof","java.lang.Boolean","com.gridnode.pdip.base.gwfbase.bpss.model.BpssDocumentEnvelope","",0,"0","0","1","1","",999,'displayable=false','');


INSERT INTO fieldmetainfo VALUES(NULL,"_uId","UID","UID","java.lang.Long","com.gridnode.pdip.base.gwfbase.xpdl.model.XpdlActivity","","0","0","0","0","0","","999",'displayable=false','');
INSERT INTO fieldmetainfo VALUES(NULL,"_activityId","ACTIVITY_ID","ActivityId","java.lang.String","com.gridnode.pdip.base.gwfbase.xpdl.model.XpdlActivity","ACTIVITY_ID","50","0","1","1","1","","1",'displayable=false','');
INSERT INTO fieldmetainfo VALUES(NULL,"_activityName","ACTIVITY_NAME","ActivityName","java.lang.String","com.gridnode.pdip.base.gwfbase.xpdl.model.XpdlActivity","ACTIVITY_NAME","50","0","1","1","1","","2",'displayable=false','');
INSERT INTO fieldmetainfo VALUES(NULL,"_activityDescription","ACTIVITY_DESCRIPTION","ActivityDescription","java.lang.String","com.gridnode.pdip.base.gwfbase.xpdl.model.XpdlActivity","ACTIVITY_DESCRIPTION","50","0","1","1","1","","3",'displayable=false','');
INSERT INTO fieldmetainfo VALUES(NULL,"_extendedAttributes","EXTENDED_ATTRIBUTES","ExtendedAttributes","java.lang.String","com.gridnode.pdip.base.gwfbase.xpdl.model.XpdlActivity","EXTENDED_ATTRIBUTES","50","0","1","1","1","","5",'displayable=false','');
INSERT INTO fieldmetainfo VALUES(NULL,"_activityLimit","ACTIVITY_LIMIT","ActivityLimit","java.lang.Double","com.gridnode.pdip.base.gwfbase.xpdl.model.XpdlActivity","ACTIVITY_LIMIT","0","0","0","0","0","","5",'displayable=false','');
INSERT INTO fieldmetainfo VALUES(NULL,"_isRoute","IS_ROUTE","IsRoute","java.lang.Boolean","com.gridnode.pdip.base.gwfbase.xpdl.model.XpdlActivity","IS_ROUTE","0","0","0","1","1","","6",'displayable=false','');
INSERT INTO fieldmetainfo VALUES(NULL,"_implementationType","IMPLEMENTATION_TYPE","ImplementationType","java.lang.String","com.gridnode.pdip.base.gwfbase.xpdl.model.XpdlActivity","IMPLEMENTATION_TYPE","50","0","1","1","1","","7",'displayable=false','');
INSERT INTO fieldmetainfo VALUES(NULL,"_performerId","PERFORMER_ID","PerformerId","java.lang.String","com.gridnode.pdip.base.gwfbase.xpdl.model.XpdlActivity","PERFORMER_ID","50","0","1","1","1","","8",'displayable=false','');
INSERT INTO fieldmetainfo VALUES(NULL,"_startMode","START_MODE","StartMode","java.lang.String","com.gridnode.pdip.base.gwfbase.xpdl.model.XpdlActivity","START_MODE","50","0","1","1","1","","9",'displayable=false','');
INSERT INTO fieldmetainfo VALUES(NULL,"_finishMode","FINISH_MODE","FinishMode","java.lang.String","com.gridnode.pdip.base.gwfbase.xpdl.model.XpdlActivity","FINISH_MODE","50","0","1","1","1","","10",'displayable=false','');
INSERT INTO fieldmetainfo VALUES(NULL,"_priority","PRIORITY","Priority","java.lang.Long","com.gridnode.pdip.base.gwfbase.xpdl.model.XpdlActivity","PRIORITY","0","0","0","0","0","","11",'displayable=false','');
INSERT INTO fieldmetainfo VALUES(NULL,"_instantiation","INSTANTIATION","Instantiation","java.lang.String","com.gridnode.pdip.base.gwfbase.xpdl.model.XpdlActivity","INSTANTIATION","50","0","1","1","1","","12",'displayable=false','');
INSERT INTO fieldmetainfo VALUES(NULL,"_cost","COST","Cost","java.lang.Double","com.gridnode.pdip.base.gwfbase.xpdl.model.XpdlActivity","COST","0","0","0","0","0","","13",'displayable=false','');
INSERT INTO fieldmetainfo VALUES(NULL,"_waitingTime","WAITING_TIME","WaitingTime","java.lang.Double","com.gridnode.pdip.base.gwfbase.xpdl.model.XpdlActivity","WAITING_TIME","0","0","0","0","0","","14",'displayable=false','');
INSERT INTO fieldmetainfo VALUES(NULL,"_duration","DURATION","Duration","java.lang.Double","com.gridnode.pdip.base.gwfbase.xpdl.model.XpdlActivity","DURATION","0","0","0","0","0","","15",'displayable=false','');
INSERT INTO fieldmetainfo VALUES(NULL,"_iconUrl","ICON_URL","IconUrl","java.lang.String","com.gridnode.pdip.base.gwfbase.xpdl.model.XpdlActivity","ICON_URL","50","0","1","1","1","","16",'displayable=false','');
INSERT INTO fieldmetainfo VALUES(NULL,"_documentationUrl","DOCUMENTATION_URL","DocumentationUrl","java.lang.String","com.gridnode.pdip.base.gwfbase.xpdl.model.XpdlActivity","DOCUMENTATION_URL","50","0","1","1","1","","17",'displayable=false','');
INSERT INTO fieldmetainfo VALUES(NULL,"_transitionRestrictionListUId","TRANSITION_RESTRICTION_LIST_UID","TransitionRestrictionListUId","java.lang.Long","com.gridnode.pdip.base.gwfbase.xpdl.model.XpdlActivity","TRANSITION_RESTRICTION_LIST_UID","0","0","0","0","0","","18",'displayable=false','');
INSERT INTO fieldmetainfo VALUES(NULL,"_workingTime","WORKING_TIME","WorkingTime","java.lang.Double","com.gridnode.pdip.base.gwfbase.xpdl.model.XpdlActivity","WORKING_TIME","0","0","0","0","0","","19",'displayable=false','');
INSERT INTO fieldmetainfo VALUES(NULL,"_processId","PROCESS_ID","ProcessId","java.lang.String","com.gridnode.pdip.base.gwfbase.xpdl.model.XpdlActivity","PROCESS_ID","50","0","1","1","1","","20",'displayable=false','');
INSERT INTO fieldmetainfo VALUES(NULL,"_packageId","PACKAGE_ID","PackageId","java.lang.String","com.gridnode.pdip.base.gwfbase.xpdl.model.XpdlActivity","PACKAGE_ID","50","0","1","1","1","","21",'displayable=false','');
INSERT INTO fieldmetainfo VALUES(NULL,"_pkgVersionId","PKG_VERSION_ID","PkgVersionId","java.lang.String","com.gridnode.pdip.base.gwfbase.xpdl.model.XpdlActivity","PKG_VERSION_ID","50","0","1","1","1","","22",'displayable=false','');


INSERT INTO fieldmetainfo VALUES(NULL,"_uId","UID","UID","java.lang.Long","com.gridnode.pdip.base.gwfbase.xpdl.model.XpdlApplication","","0","0","0","0","0","","999",'displayable=false','');
INSERT INTO fieldmetainfo VALUES(NULL,"_applicationId","APPLICATION_ID","ApplicationId","java.lang.String","com.gridnode.pdip.base.gwfbase.xpdl.model.XpdlApplication","APPLICATION_ID","50","0","1","1","1","","1",'displayable=false','');
INSERT INTO fieldmetainfo VALUES(NULL,"_applicationName","APPLICATION_NAME","ApplicationName","java.lang.String","com.gridnode.pdip.base.gwfbase.xpdl.model.XpdlApplication","APPLICATION_NAME","50","0","1","1","1","","2",'displayable=false','');
INSERT INTO fieldmetainfo VALUES(NULL,"_applicationDescription","APPLICATION_DESCRIPTION","ApplicationDescription","java.lang.String","com.gridnode.pdip.base.gwfbase.xpdl.model.XpdlApplication","APPLICATION_DESCRIPTION","50","0","1","1","1","","3",'displayable=false','');
INSERT INTO fieldmetainfo VALUES(NULL,"_extendedAttributes","EXTENDED_ATTRIBUTES","ExtendedAttributes","java.lang.String","com.gridnode.pdip.base.gwfbase.xpdl.model.XpdlApplication","EXTENDED_ATTRIBUTES","50","0","1","1","1","","5",'displayable=false','');
INSERT INTO fieldmetainfo VALUES(NULL,"_processId","PROCESS_ID","ProcessId","java.lang.String","com.gridnode.pdip.base.gwfbase.xpdl.model.XpdlApplication","PROCESS_ID","50","0","1","1","1","","5",'displayable=false','');
INSERT INTO fieldmetainfo VALUES(NULL,"_packageId","PACKAGE_ID","PackageId","java.lang.String","com.gridnode.pdip.base.gwfbase.xpdl.model.XpdlApplication","PACKAGE_ID","50","0","1","1","1","","6",'displayable=false','');
INSERT INTO fieldmetainfo VALUES(NULL,"_pkgVersionId","PKG_VERSION_ID","PkgVersionId","java.lang.String","com.gridnode.pdip.base.gwfbase.xpdl.model.XpdlApplication","PKG_VERSION_ID","50","0","1","1","1","","7",'displayable=false','');


INSERT INTO fieldmetainfo VALUES(NULL,"_uId","UID","UID","java.lang.Long","com.gridnode.pdip.base.gwfbase.xpdl.model.XpdlDataField","","0","0","0","0","0","","999",'displayable=false','');
INSERT INTO fieldmetainfo VALUES(NULL,"_dataFieldId","DATA_FIELD_ID","DataFieldId","java.lang.String","com.gridnode.pdip.base.gwfbase.xpdl.model.XpdlDataField","DATA_FIELD_ID","50","0","1","1","1","","1",'displayable=false','');
INSERT INTO fieldmetainfo VALUES(NULL,"_dataFieldName","DATA_FIELD_NAME","DataFieldName","java.lang.String","com.gridnode.pdip.base.gwfbase.xpdl.model.XpdlDataField","DATA_FIELD_NAME","50","0","1","1","1","","2",'displayable=false','');
INSERT INTO fieldmetainfo VALUES(NULL,"_dataFieldDescription","DATA_FIELD_DESCRIPTION","DataFieldDescription","java.lang.String","com.gridnode.pdip.base.gwfbase.xpdl.model.XpdlDataField","DATA_FIELD_DESCRIPTION","50","0","1","1","1","","3",'displayable=false','');
INSERT INTO fieldmetainfo VALUES(NULL,"_extendedAttributes","EXTENDED_ATTRIBUTES","ExtendedAttributes","java.lang.String","com.gridnode.pdip.base.gwfbase.xpdl.model.XpdlDataField","EXTENDED_ATTRIBUTE_LIST_UID","0","0","0","0","0","","4",'displayable=false','');
INSERT INTO fieldmetainfo VALUES(NULL,"_isArray","IS_ARRAY","IsArray","java.lang.Boolean","com.gridnode.pdip.base.gwfbase.xpdl.model.XpdlDataField","IS_ARRAY","0","0","0","1","1","","5",'displayable=false','');
INSERT INTO fieldmetainfo VALUES(NULL,"_initialValue","INITIAL_VALUE","InitialValue","java.lang.String","com.gridnode.pdip.base.gwfbase.xpdl.model.XpdlDataField","INITIAL_VALUE","50","0","1","1","1","","6",'displayable=false','');
INSERT INTO fieldmetainfo VALUES(NULL,"_lengthBytes","LENGTH_BYTES","LengthBytes","java.lang.Long","com.gridnode.pdip.base.gwfbase.xpdl.model.XpdlDataField","LENGTH_BYTES","0","0","0","0","0","","7",'displayable=false','');
INSERT INTO fieldmetainfo VALUES(NULL,"_processId","PROCESS_ID","ProcessId","java.lang.String","com.gridnode.pdip.base.gwfbase.xpdl.model.XpdlDataField","PROCESS_ID","50","0","1","1","1","","8",'displayable=false','');
INSERT INTO fieldmetainfo VALUES(NULL,"_packageId","PACKAGE_ID","PackageId","java.lang.String","com.gridnode.pdip.base.gwfbase.xpdl.model.XpdlDataField","PACKAGE_ID","50","0","1","1","1","","9",'displayable=false','');
INSERT INTO fieldmetainfo VALUES(NULL,"_pkgVersionId","PKG_VERSION_ID","PkgVersionId","java.lang.String","com.gridnode.pdip.base.gwfbase.xpdl.model.XpdlDataField","PKG_VERSION_ID","50","0","1","1","1","","10",'displayable=false','');
INSERT INTO fieldmetainfo VALUES(NULL,"_dataTypeName","DATATYPE_NAME","DataTypeName","java.lang.String","com.gridnode.pdip.base.gwfbase.xpdl.model.XpdlDataField","DATATYPE_NAME","50","0","1","1","1","","1",'displayable=false','');
INSERT INTO fieldmetainfo VALUES(NULL,"_complexDataTypeUId","COMPLEX_DATATYPE_UID","ComplexDataTypeUId","java.lang.Long","com.gridnode.pdip.base.gwfbase.xpdl.model.XpdlDataField","COMPLEX_DATATYPE_UID","0","0","0","0","0","","4",'displayable=false','');


INSERT INTO fieldmetainfo VALUES(NULL,"_uId","UID","UID","java.lang.Long","com.gridnode.pdip.base.gwfbase.xpdl.model.XpdlExternalPackage","","0","0","0","0","0","","999",'displayable=false','');
INSERT INTO fieldmetainfo VALUES(NULL,"_href","HREF","Href","java.lang.String","com.gridnode.pdip.base.gwfbase.xpdl.model.XpdlExternalPackage","HREF","50","0","1","1","1","","1",'displayable=false','');
INSERT INTO fieldmetainfo VALUES(NULL,"_extendedAttributes","EXTENDED_ATTRIBUTES","ExtendedAttributes","java.lang.String","com.gridnode.pdip.base.gwfbase.xpdl.model.XpdlExternalPackage","EXTENDED_ATTRIBUTE_LIST_UID","0","0","0","0","0","","2",'displayable=false','');
INSERT INTO fieldmetainfo VALUES(NULL,"_packageId","PACKAGE_ID","PackageId","java.lang.String","com.gridnode.pdip.base.gwfbase.xpdl.model.XpdlExternalPackage","PACKAGE_ID","50","0","1","1","1","","3",'displayable=false','');
INSERT INTO fieldmetainfo VALUES(NULL,"_pkgVersionId","PKG_VERSION_ID","PkgVersionId","java.lang.String","com.gridnode.pdip.base.gwfbase.xpdl.model.XpdlExternalPackage","PKG_VERSION_ID","50","0","1","1","1","","4",'displayable=false','');


INSERT INTO fieldmetainfo VALUES(NULL,"_uId","UID","UID","java.lang.Long","com.gridnode.pdip.base.gwfbase.xpdl.model.XpdlFormalParam","","0","0","0","0","0","","999",'displayable=false','');
INSERT INTO fieldmetainfo VALUES(NULL,"_formalParamId","FORMAL_PARAM_ID","FormalParamId","java.lang.String","com.gridnode.pdip.base.gwfbase.xpdl.model.XpdlFormalParam","FORMAL_PARAM_ID","50","0","1","1","1","","1",'displayable=false','');
INSERT INTO fieldmetainfo VALUES(NULL,"_mode","MODE","Mode","java.lang.String","com.gridnode.pdip.base.gwfbase.xpdl.model.XpdlFormalParam","MODE","50","0","1","1","1","","2",'displayable=false','');
INSERT INTO fieldmetainfo VALUES(NULL,"_indexNumber","INDEX_NUMBER","IndexNumber","java.lang.Integer","com.gridnode.pdip.base.gwfbase.xpdl.model.XpdlFormalParam","INDEX_NUMBER","0","0","0","0","0","","3",'displayable=false','');
INSERT INTO fieldmetainfo VALUES(NULL,"_formalParamDescription","FORMAL_PARAM_DESCRIPTION","FormalParamDescription","java.lang.String","com.gridnode.pdip.base.gwfbase.xpdl.model.XpdlFormalParam","FORMAL_PARAM_DESCRIPTION","50","0","1","1","1","","4",'displayable=false','');
INSERT INTO fieldmetainfo VALUES(NULL,"_applicationId","APPLICATION_ID","ApplicationId","java.lang.String","com.gridnode.pdip.base.gwfbase.xpdl.model.XpdlFormalParam","APPLICATION_ID","50","0","1","1","1","","5",'displayable=false','');
INSERT INTO fieldmetainfo VALUES(NULL,"_processId","PROCESS_ID","ProcessId","java.lang.String","com.gridnode.pdip.base.gwfbase.xpdl.model.XpdlFormalParam","PROCESS_ID","50","0","1","1","1","","6",'displayable=false','');
INSERT INTO fieldmetainfo VALUES(NULL,"_packageId","PACKAGE_ID","PackageId","java.lang.String","com.gridnode.pdip.base.gwfbase.xpdl.model.XpdlFormalParam","PACKAGE_ID","50","0","1","1","1","","7",'displayable=false','');
INSERT INTO fieldmetainfo VALUES(NULL,"_pkgVersionId","PKG_VERSION_ID","PkgVersionId","java.lang.String","com.gridnode.pdip.base.gwfbase.xpdl.model.XpdlFormalParam","PKG_VERSION_ID","50","0","1","1","1","","8",'displayable=false','');
INSERT INTO fieldmetainfo VALUES(NULL,"_dataTypeName","DATATYPE_NAME","DataTypeName","java.lang.String","com.gridnode.pdip.base.gwfbase.xpdl.model.XpdlFormalParam","DATATYPE_NAME","50","0","1","1","1","","1",'displayable=false','');
INSERT INTO fieldmetainfo VALUES(NULL,"_complexDataTypeUId","COMPLEX_DATATYPE_UID","ComplexDataTypeUId","java.lang.Long","com.gridnode.pdip.base.gwfbase.xpdl.model.XpdlFormalParam","COMPLEX_DATATYPE_UID","0","0","0","0","0","","4",'displayable=false','');


INSERT INTO fieldmetainfo VALUES(NULL,"_uId","UID","UID","java.lang.Long","com.gridnode.pdip.base.gwfbase.xpdl.model.XpdlPackage","","0","0","0","0","0","","999",'displayable=false','');
INSERT INTO fieldmetainfo VALUES(NULL,"_packageId","PACKAGE_ID","PackageId","java.lang.String","com.gridnode.pdip.base.gwfbase.xpdl.model.XpdlPackage","PACKAGE_ID","50","0","1","1","1","","1",'displayable=false','');
INSERT INTO fieldmetainfo VALUES(NULL,"_packageName","PACKAGE_NAME","PackageName","java.lang.String","com.gridnode.pdip.base.gwfbase.xpdl.model.XpdlPackage","PACKAGE_NAME","50","0","1","1","1","","2",'displayable=false','');
INSERT INTO fieldmetainfo VALUES(NULL,"_packageDescription","PACKAGE_DESCRIPTION","PackageDescription","java.lang.String","com.gridnode.pdip.base.gwfbase.xpdl.model.XpdlPackage","PACKAGE_DESCRIPTION","50","0","1","1","1","","3",'displayable=false','');
INSERT INTO fieldmetainfo VALUES(NULL,"_extendedAttributes","EXTENDED_ATTRIBUTES","ExtendedAttributes","java.lang.String","com.gridnode.pdip.base.gwfbase.xpdl.model.XpdlPackage","EXTENDED_ATTRIBUTE_LIST_UID","0","0","0","0","0","","4",'displayable=false','');
INSERT INTO fieldmetainfo VALUES(NULL,"_specificationId","SPECIFICATION_ID","SpecificationId","java.lang.String","com.gridnode.pdip.base.gwfbase.xpdl.model.XpdlPackage","SPECIFICATION_ID","50","0","1","1","1","","5",'displayable=false','');
INSERT INTO fieldmetainfo VALUES(NULL,"_specificationVersion","SPECIFICATION_VERSION","SpecificationVersion","java.lang.String","com.gridnode.pdip.base.gwfbase.xpdl.model.XpdlPackage","SPECIFICATION_VERSION","50","0","1","1","1","","6",'displayable=false','');
INSERT INTO fieldmetainfo VALUES(NULL,"_sourceVendorInfo","SOURCE_VENDOR_INFO","SourceVendorInfo","java.lang.String","com.gridnode.pdip.base.gwfbase.xpdl.model.XpdlPackage","SOURCE_VENDOR_INFO","50","0","1","1","1","","7",'displayable=false','');
INSERT INTO fieldmetainfo VALUES(NULL,"_creationDateTime","CREATION_DATE_TIME","CreationDateTime","java.util.Date","com.gridnode.pdip.base.gwfbase.xpdl.model.XpdlPackage","CREATION_DATE_TIME","0","0","0","0","1","","8",'displayable=false','');
INSERT INTO fieldmetainfo VALUES(NULL,"_documentationUrl","DOCUMENTATION_URL","DocumentationUrl","java.lang.String","com.gridnode.pdip.base.gwfbase.xpdl.model.XpdlPackage","DOCUMENTATION_URL","50","0","1","1","1","","9",'displayable=false','');
INSERT INTO fieldmetainfo VALUES(NULL,"_priorityUnit","PRIORITY_UNIT","PriorityUnit","java.lang.String","com.gridnode.pdip.base.gwfbase.xpdl.model.XpdlPackage","PRIORITY_UNIT","50","0","1","1","1","","10",'displayable=false','');
INSERT INTO fieldmetainfo VALUES(NULL,"_costUnit","COST_UNIT","CostUnit","java.lang.String","com.gridnode.pdip.base.gwfbase.xpdl.model.XpdlPackage","COST_UNIT","50","0","1","1","1","","11",'displayable=false','');
INSERT INTO fieldmetainfo VALUES(NULL,"_author","AUTHOR","Author","java.lang.String","com.gridnode.pdip.base.gwfbase.xpdl.model.XpdlPackage","AUTHOR","50","0","1","1","1","","12",'displayable=false','');
INSERT INTO fieldmetainfo VALUES(NULL,"_versionId","VERSION_ID","VersionId","java.lang.String","com.gridnode.pdip.base.gwfbase.xpdl.model.XpdlPackage","VERSION_ID","50","0","1","1","1","","13",'displayable=false','');
INSERT INTO fieldmetainfo VALUES(NULL,"_codepage","CODEPAGE","Codepage","java.lang.String","com.gridnode.pdip.base.gwfbase.xpdl.model.XpdlPackage","CODEPAGE","50","0","1","1","1","","14",'displayable=false','');
INSERT INTO fieldmetainfo VALUES(NULL,"_countrykey","COUNTRYKEY","Countrykey","java.lang.String","com.gridnode.pdip.base.gwfbase.xpdl.model.XpdlPackage","COUNTRYKEY","50","0","1","1","1","","15",'displayable=false','');
INSERT INTO fieldmetainfo VALUES(NULL,"_publicationStatus","PUBLICATION_STATUS","PublicationStatus","java.lang.String","com.gridnode.pdip.base.gwfbase.xpdl.model.XpdlPackage","PUBLICATION_STATUS","50","0","1","1","1","","16",'displayable=false','');
INSERT INTO fieldmetainfo VALUES(NULL,"_responsibleListUId","RESPONSIBLE_LIST_UID","ResponsibleListUId","java.lang.Long","com.gridnode.pdip.base.gwfbase.xpdl.model.XpdlPackage","RESPONSIBLE_LIST_UID","0","0","0","0","0","","17",'displayable=false','');
INSERT INTO fieldmetainfo VALUES(NULL,"_graphConformance","GRAPH_CONFORMANCE","GraphConformance","java.lang.String","com.gridnode.pdip.base.gwfbase.xpdl.model.XpdlPackage","GRAPH_CONFORMANCE","50","0","1","1","1","","18",'displayable=false','');
INSERT INTO fieldmetainfo VALUES(NULL,"_state","STATE","State","java.lang.Short","com.gridnode.pdip.base.gwfbase.xpdl.model.XpdlPackage","XpdlPackage.state","1","0","1","1","1","","999","displayable=false","");

INSERT INTO fieldmetainfo VALUES(NULL,"_uId","UID","UID","java.lang.Long","com.gridnode.pdip.base.gwfbase.xpdl.model.XpdlParticipant","","0","0","0","0","0","","999",'displayable=false','');
INSERT INTO fieldmetainfo VALUES(NULL,"_participantId","PARTICIPANT_ID","ParticipantId","java.lang.String","com.gridnode.pdip.base.gwfbase.xpdl.model.XpdlParticipant","PARTICIPANT_ID","50","0","1","1","1","","1",'displayable=false','');
INSERT INTO fieldmetainfo VALUES(NULL,"_participantName","PARTICIPANT_NAME","ParticipantName","java.lang.String","com.gridnode.pdip.base.gwfbase.xpdl.model.XpdlParticipant","PARTICIPANT_NAME","50","0","1","1","1","","2",'displayable=false','');
INSERT INTO fieldmetainfo VALUES(NULL,"_participantDescription","PARTICIPANT_DESCRIPTION","ParticipantDescription","java.lang.String","com.gridnode.pdip.base.gwfbase.xpdl.model.XpdlParticipant","PARTICIPANT_DESCRIPTION","50","0","1","1","1","","3",'displayable=false','');
INSERT INTO fieldmetainfo VALUES(NULL,"_extendedAttributes","EXTENDED_ATTRIBUTES","ExtendedAttributes","java.lang.String","com.gridnode.pdip.base.gwfbase.xpdl.model.XpdlParticipant","EXTENDED_ATTRIBUTE_LIST_UID","0","0","0","0","0","","4",'displayable=false','');
INSERT INTO fieldmetainfo VALUES(NULL,"_participantTypeId","PARTICIPANT_TYPE_ID","ParticipantTypeId","java.lang.String","com.gridnode.pdip.base.gwfbase.xpdl.model.XpdlParticipant","PARTICIPANT_TYPE_ID","50","0","1","1","1","","5",'displayable=false','');
INSERT INTO fieldmetainfo VALUES(NULL,"_processId","PROCESS_ID","ProcessId","java.lang.String","com.gridnode.pdip.base.gwfbase.xpdl.model.XpdlParticipant","PROCESS_ID","50","0","1","1","1","","5",'displayable=false','');
INSERT INTO fieldmetainfo VALUES(NULL,"_packageId","PACKAGE_ID","PackageId","java.lang.String","com.gridnode.pdip.base.gwfbase.xpdl.model.XpdlParticipant","PACKAGE_ID","50","0","1","1","1","","6",'displayable=false','');
INSERT INTO fieldmetainfo VALUES(NULL,"_pkgVersionId","PKG_VERSION_ID","PkgVersionId","java.lang.String","com.gridnode.pdip.base.gwfbase.xpdl.model.XpdlParticipant","PKG_VERSION_ID","50","0","1","1","1","","7",'displayable=false','');


INSERT INTO fieldmetainfo VALUES(NULL,"_uId","UID","UID","java.lang.Long","com.gridnode.pdip.base.gwfbase.xpdl.model.XpdlParticipantList","","0","0","0","0","0","","999",'displayable=false','');
INSERT INTO fieldmetainfo VALUES(NULL,"_participantId","PARTICIPANT_ID","ParticipantId","java.lang.String","com.gridnode.pdip.base.gwfbase.xpdl.model.XpdlParticipantList","PARTICIPANT_ID","50","0","1","1","1","","1",'displayable=false','');
INSERT INTO fieldmetainfo VALUES(NULL,"_participantIndex","PARTICIPANT_INDEX","ParticipantIndex","java.lang.Long","com.gridnode.pdip.base.gwfbase.xpdl.model.XpdlParticipantList","PARTICIPANT_INDEX","0","0","0","0","0","","2",'displayable=false','');
INSERT INTO fieldmetainfo VALUES(NULL,"_listUId","LIST_UID","ListUId","java.lang.Long","com.gridnode.pdip.base.gwfbase.xpdl.model.XpdlParticipantList","LIST_UID","0","0","0","0","0","","3",'displayable=false','');


INSERT INTO fieldmetainfo VALUES(NULL,"_uId","UID","UID","java.lang.Long","com.gridnode.pdip.base.gwfbase.xpdl.model.XpdlProcess","","0","0","0","0","0","","999",'displayable=false','');
INSERT INTO fieldmetainfo VALUES(NULL,"_processId","PROCESS_ID","ProcessId","java.lang.String","com.gridnode.pdip.base.gwfbase.xpdl.model.XpdlProcess","PROCESS_ID","50","0","1","1","1","","1",'displayable=false','');
INSERT INTO fieldmetainfo VALUES(NULL,"_processName","PROCESS_NAME","ProcessName","java.lang.String","com.gridnode.pdip.base.gwfbase.xpdl.model.XpdlProcess","PROCESS_NAME","50","0","1","1","1","","2",'displayable=false','');
INSERT INTO fieldmetainfo VALUES(NULL,"_processDescription","PROCESS_DESCRIPTION","ProcessDescription","java.lang.String","com.gridnode.pdip.base.gwfbase.xpdl.model.XpdlProcess","PROCESS_DESCRIPTION","50","0","1","1","1","","3",'displayable=false','');
INSERT INTO fieldmetainfo VALUES(NULL,"_extendedAttributes","EXTENDED_ATTRIBUTES","ExtendedAttributes","java.lang.String","com.gridnode.pdip.base.gwfbase.xpdl.model.XpdlProcess","EXTENDED_ATTRIBUTE_LIST_UID","0","0","0","0","0","","4",'displayable=false','');
INSERT INTO fieldmetainfo VALUES(NULL,"_durationUnit","DURATION_UNIT","DurationUnit","java.lang.String","com.gridnode.pdip.base.gwfbase.xpdl.model.XpdlProcess","DURATION_UNIT","50","0","1","1","1","","5",'displayable=false','');
INSERT INTO fieldmetainfo VALUES(NULL,"_creationDateTime","CREATION_DATE_TIME","CreationDateTime","java.util.Date","com.gridnode.pdip.base.gwfbase.xpdl.model.XpdlProcess","CREATION_DATE_TIME","0","0","0","0","1","","6",'displayable=false','');
INSERT INTO fieldmetainfo VALUES(NULL,"_headerDescription","HEADER_DESCRIPTION","HeaderDescription","java.lang.String","com.gridnode.pdip.base.gwfbase.xpdl.model.XpdlProcess","HEADER_DESCRIPTION","50","0","1","1","1","","7",'displayable=false','');
INSERT INTO fieldmetainfo VALUES(NULL,"_priority","PRIORITY","Priority","java.lang.Long","com.gridnode.pdip.base.gwfbase.xpdl.model.XpdlProcess","PRIORITY","0","0","0","0","0","","8",'displayable=false','');
INSERT INTO fieldmetainfo VALUES(NULL,"_processLimit","PROCESS_LIMIT","ProcessLimit","java.lang.Double","com.gridnode.pdip.base.gwfbase.xpdl.model.XpdlProcess","PROCESS_LIMIT","0","0","0","0","0","","9",'displayable=false','');
INSERT INTO fieldmetainfo VALUES(NULL,"_validFromDate","VALID_FROM_DATE","ValidFromDate","java.util.Date","com.gridnode.pdip.base.gwfbase.xpdl.model.XpdlProcess","VALID_FROM_DATE","0","0","0","0","1","","10",'displayable=false','');
INSERT INTO fieldmetainfo VALUES(NULL,"_validToDate","VALID_TO_DATE","ValidToDate","java.util.Date","com.gridnode.pdip.base.gwfbase.xpdl.model.XpdlProcess","VALID_TO_DATE","0","0","0","0","1","","11",'displayable=false','');
INSERT INTO fieldmetainfo VALUES(NULL,"_waitingTime","WAITING_TIME","WaitingTime","java.lang.Double","com.gridnode.pdip.base.gwfbase.xpdl.model.XpdlProcess","WAITING_TIME","0","0","0","0","0","","12",'displayable=false','');
INSERT INTO fieldmetainfo VALUES(NULL,"_workingTime","WORKING_TIME","WorkingTime","java.lang.Double","com.gridnode.pdip.base.gwfbase.xpdl.model.XpdlProcess","WORKING_TIME","0","0","0","0","0","","13",'displayable=false','');
INSERT INTO fieldmetainfo VALUES(NULL,"_duration","DURATION","Duration","java.lang.Double","com.gridnode.pdip.base.gwfbase.xpdl.model.XpdlProcess","DURATION","0","0","0","0","0","","14",'displayable=false','');
INSERT INTO fieldmetainfo VALUES(NULL,"_author","AUTHOR","Author","java.lang.String","com.gridnode.pdip.base.gwfbase.xpdl.model.XpdlProcess","AUTHOR","50","0","1","1","1","","15",'displayable=false','');
INSERT INTO fieldmetainfo VALUES(NULL,"_versionId","VERSION_ID","VersionId","java.lang.String","com.gridnode.pdip.base.gwfbase.xpdl.model.XpdlProcess","VERSION_ID","50","0","1","1","1","","16",'displayable=false','');
INSERT INTO fieldmetainfo VALUES(NULL,"_codepage","CODEPAGE","Codepage","java.lang.String","com.gridnode.pdip.base.gwfbase.xpdl.model.XpdlProcess","CODEPAGE","50","0","1","1","1","","17",'displayable=false','');
INSERT INTO fieldmetainfo VALUES(NULL,"_countrykey","COUNTRYKEY","Countrykey","java.lang.String","com.gridnode.pdip.base.gwfbase.xpdl.model.XpdlProcess","COUNTRYKEY","50","0","1","1","1","","18",'displayable=false','');
INSERT INTO fieldmetainfo VALUES(NULL,"_publicationStatus","PUBLICATION_STATUS","PublicationStatus","java.lang.String","com.gridnode.pdip.base.gwfbase.xpdl.model.XpdlProcess","PUBLICATION_STATUS","50","0","1","1","1","","19",'displayable=false','');
INSERT INTO fieldmetainfo VALUES(NULL,"_responsibleListUId","RESPONSIBLE_LIST_UID","ResponsibleListUId","java.lang.Long","com.gridnode.pdip.base.gwfbase.xpdl.model.XpdlProcess","RESPONSIBLE_LIST_UID","0","0","0","0","0","","20",'displayable=false','');
INSERT INTO fieldmetainfo VALUES(NULL,"_packageId","PACKAGE_ID","PackageId","java.lang.String","com.gridnode.pdip.base.gwfbase.xpdl.model.XpdlProcess","PACKAGE_ID","50","0","1","1","1","","22",'displayable=false','');
INSERT INTO fieldmetainfo VALUES(NULL,"_defaultStartActivityId","DEFAULT_START_ACTIVITY_ID","DefaultStartActivityId","java.lang.String","com.gridnode.pdip.base.gwfbase.xpdl.model.XpdlProcess","DEFAULT_START_ACTIVITY_ID","50","0","1","1","1","","23",'displayable=false','');
INSERT INTO fieldmetainfo VALUES(NULL,"_pkgVersionId","PKG_VERSION_ID","PkgVersionId","java.lang.String","com.gridnode.pdip.base.gwfbase.xpdl.model.XpdlProcess","PKG_VERSION_ID","50","0","1","1","1","","24",'displayable=false','');


INSERT INTO fieldmetainfo VALUES(NULL,"_uId","UID","UID","java.lang.Long","com.gridnode.pdip.base.gwfbase.xpdl.model.XpdlSubFlow","","0","0","0","0","0","","999",'displayable=false','');
INSERT INTO fieldmetainfo VALUES(NULL,"_subFlowId","SUB_FLOW_ID","SubFlowId","java.lang.String","com.gridnode.pdip.base.gwfbase.xpdl.model.XpdlSubFlow","SUB_FLOW_ID","50","0","1","1","1","","1",'displayable=false','');
INSERT INTO fieldmetainfo VALUES(NULL,"_subFlowType","SUB_FLOW_TYPE","SubFlowType","java.lang.String","com.gridnode.pdip.base.gwfbase.xpdl.model.XpdlSubFlow","SUB_FLOW_TYPE","50","0","1","1","1","","2",'displayable=false','');
INSERT INTO fieldmetainfo VALUES(NULL,"_actualParameters","ACTUAL_PARAMETERS","ActualParameters","java.lang.String","com.gridnode.pdip.base.gwfbase.xpdl.model.XpdlSubFlow","ACTUAL_PARAMETERS","50","0","1","1","1","","3",'displayable=false','');
INSERT INTO fieldmetainfo VALUES(NULL,"_activityId","ACTIVITY_ID","ActivityId","java.lang.String","com.gridnode.pdip.base.gwfbase.xpdl.model.XpdlSubFlow","ACTIVITY_ID","50","0","1","1","1","","4",'displayable=false','');
INSERT INTO fieldmetainfo VALUES(NULL,"_processId","PROCESS_ID","ProcessId","java.lang.String","com.gridnode.pdip.base.gwfbase.xpdl.model.XpdlSubFlow","PROCESS_ID","50","0","1","1","1","","5",'displayable=false','');
INSERT INTO fieldmetainfo VALUES(NULL,"_packageId","PACKAGE_ID","PackageId","java.lang.String","com.gridnode.pdip.base.gwfbase.xpdl.model.XpdlSubFlow","PACKAGE_ID","50","0","1","1","1","","6",'displayable=false','');
INSERT INTO fieldmetainfo VALUES(NULL,"_pkgVersionId","PKG_VERSION_ID","PkgVersionId","java.lang.String","com.gridnode.pdip.base.gwfbase.xpdl.model.XpdlSubFlow","PKG_VERSION_ID","50","0","1","1","1","","7",'displayable=false','');


INSERT INTO fieldmetainfo VALUES(NULL,"_uId","UID","UID","java.lang.Long","com.gridnode.pdip.base.gwfbase.xpdl.model.XpdlTool","","0","0","0","0","0","","999",'displayable=false','');
INSERT INTO fieldmetainfo VALUES(NULL,"_toolId","TOOL_ID","ToolId","java.lang.String","com.gridnode.pdip.base.gwfbase.xpdl.model.XpdlTool","TOOL_ID","50","0","1","1","1","","1",'displayable=false','');
INSERT INTO fieldmetainfo VALUES(NULL,"_toolType","TOOL_TYPE","ToolType","java.lang.String","com.gridnode.pdip.base.gwfbase.xpdl.model.XpdlTool","TOOL_TYPE","50","0","1","1","1","","2",'displayable=false','');
INSERT INTO fieldmetainfo VALUES(NULL,"_toolDescription","TOOL_DESCRIPTION","ToolDescription","java.lang.String","com.gridnode.pdip.base.gwfbase.xpdl.model.XpdlTool","TOOL_DESCRIPTION","50","0","1","1","1","","3",'displayable=false','');
INSERT INTO fieldmetainfo VALUES(NULL,"_actualParameters","ACTUAL_PARAMETERS","ActualParameters","java.lang.String","com.gridnode.pdip.base.gwfbase.xpdl.model.XpdlTool","ACTUAL_PARAMETERS","50","0","1","1","1","","4",'displayable=false','');
INSERT INTO fieldmetainfo VALUES(NULL,"_extendedAttributes","EXTENDED_ATTRIBUTES","ExtendedAttributes","java.lang.String","com.gridnode.pdip.base.gwfbase.xpdl.model.XpdlTool","EXTENDED_ATTRIBUTES","50","0","1","1","1","","5",'displayable=false','');
INSERT INTO fieldmetainfo VALUES(NULL,"_loopKind","LOOP_KIND","LoopKind","java.lang.String","com.gridnode.pdip.base.gwfbase.xpdl.model.XpdlTool","LOOP_KIND","50","0","1","1","1","","6",'displayable=false','');
INSERT INTO fieldmetainfo VALUES(NULL,"_conditionExpr","CONDITION_EXPR","ConditionExpr","java.lang.String","com.gridnode.pdip.base.gwfbase.xpdl.model.XpdlTool","CONDITION_EXPR","50","0","1","1","1","","7",'displayable=false','');
INSERT INTO fieldmetainfo VALUES(NULL,"_activityId","ACTIVITY_ID","ActivityId","java.lang.String","com.gridnode.pdip.base.gwfbase.xpdl.model.XpdlTool","ACTIVITY_ID","50","0","1","1","1","","8",'displayable=false','');
INSERT INTO fieldmetainfo VALUES(NULL,"_processId","PROCESS_ID","ProcessId","java.lang.String","com.gridnode.pdip.base.gwfbase.xpdl.model.XpdlTool","PROCESS_ID","50","0","1","1","1","","9",'displayable=false','');
INSERT INTO fieldmetainfo VALUES(NULL,"_packageId","PACKAGE_ID","PackageId","java.lang.String","com.gridnode.pdip.base.gwfbase.xpdl.model.XpdlTool","PACKAGE_ID","50","0","1","1","1","","10",'displayable=false','');
INSERT INTO fieldmetainfo VALUES(NULL,"_pkgVersionId","PKG_VERSION_ID","PkgVersionId","java.lang.String","com.gridnode.pdip.base.gwfbase.xpdl.model.XpdlTool","PKG_VERSION_ID","50","0","1","1","1","","11",'displayable=false','');


INSERT INTO fieldmetainfo VALUES(NULL,"_uId","UID","UID","java.lang.Long","com.gridnode.pdip.base.gwfbase.xpdl.model.XpdlTransition","","0","0","0","0","0","","999",'displayable=false','');
INSERT INTO fieldmetainfo VALUES(NULL,"_transitionId","TRANSITION_ID","TransitionId","java.lang.String","com.gridnode.pdip.base.gwfbase.xpdl.model.XpdlTransition","TRANSITION_ID","50","0","1","1","1","","1",'displayable=false','');
INSERT INTO fieldmetainfo VALUES(NULL,"_transitionName","TRANSITION_NAME","TransitionName","java.lang.String","com.gridnode.pdip.base.gwfbase.xpdl.model.XpdlTransition","TRANSITION_NAME","50","0","1","1","1","","2",'displayable=false','');
INSERT INTO fieldmetainfo VALUES(NULL,"_transitionDescription","TRANSITION_DESCRIPTION","TransitionDescription","java.lang.String","com.gridnode.pdip.base.gwfbase.xpdl.model.XpdlTransition","TRANSITION_DESCRIPTION","50","0","1","1","1","","3",'displayable=false','');
INSERT INTO fieldmetainfo VALUES(NULL,"_extendedAttributes","EXTENDED_ATTRIBUTES","ExtendedAttributes","java.lang.String","com.gridnode.pdip.base.gwfbase.xpdl.model.XpdlTransition","EXTENDED_ATTRIBUTE_LIST_UID","0","0","0","0","0","","4",'displayable=false','');
INSERT INTO fieldmetainfo VALUES(NULL,"_fromActivityId","FROM_ACTIVITY_ID","FromActivityId","java.lang.String","com.gridnode.pdip.base.gwfbase.xpdl.model.XpdlTransition","FROM_ACTIVITY_ID","50","0","1","1","1","","5",'displayable=false','');
INSERT INTO fieldmetainfo VALUES(NULL,"_toActivityId","TO_ACTIVITY_ID","ToActivityId","java.lang.String","com.gridnode.pdip.base.gwfbase.xpdl.model.XpdlTransition","TO_ACTIVITY_ID","50","0","1","1","1","","6",'displayable=false','');
INSERT INTO fieldmetainfo VALUES(NULL,"_loopType","LOOP_TYPE","LoopType","java.lang.String","com.gridnode.pdip.base.gwfbase.xpdl.model.XpdlTransition","LOOP_TYPE","50","0","1","1","1","","7",'displayable=false','');
INSERT INTO fieldmetainfo VALUES(NULL,"_conditionType","CONDITION_TYPE","ConditionType","java.lang.String","com.gridnode.pdip.base.gwfbase.xpdl.model.XpdlTransition","CONDITION_TYPE","50","0","1","1","1","","8",'displayable=false','');
INSERT INTO fieldmetainfo VALUES(NULL,"_conditionExpr","CONDITION_EXPR","ConditionExpr","java.lang.String","com.gridnode.pdip.base.gwfbase.xpdl.model.XpdlTransition","CONDITION_EXPR","50","0","1","1","1","","9",'displayable=false','');
INSERT INTO fieldmetainfo VALUES(NULL,"_processId","PROCESS_ID","ProcessId","java.lang.String","com.gridnode.pdip.base.gwfbase.xpdl.model.XpdlTransition","PROCESS_ID","50","0","1","1","1","","10",'displayable=false','');
INSERT INTO fieldmetainfo VALUES(NULL,"_packageId","PACKAGE_ID","PackageId","java.lang.String","com.gridnode.pdip.base.gwfbase.xpdl.model.XpdlTransition","PACKAGE_ID","50","0","1","1","1","","11",'displayable=false','');
INSERT INTO fieldmetainfo VALUES(NULL,"_pkgVersionId","PKG_VERSION_ID","PkgVersionId","java.lang.String","com.gridnode.pdip.base.gwfbase.xpdl.model.XpdlTransition","PKG_VERSION_ID","50","0","1","1","1","","12",'displayable=false','');


INSERT INTO fieldmetainfo VALUES(NULL,"_uId","UID","UID","java.lang.Long","com.gridnode.pdip.base.gwfbase.xpdl.model.XpdlTransitionRef","","0","0","0","0","0","","999",'displayable=false','');
INSERT INTO fieldmetainfo VALUES(NULL,"_transitionId","TRANSITION_ID","TransitionId","java.lang.String","com.gridnode.pdip.base.gwfbase.xpdl.model.XpdlTransitionRef","TRANSITION_ID","50","0","1","1","1","","1",'displayable=false','');
INSERT INTO fieldmetainfo VALUES(NULL,"_listUId","LIST_UID","ListUId","java.lang.Long","com.gridnode.pdip.base.gwfbase.xpdl.model.XpdlTransitionRef","LIST_UID","0","0","0","0","0","","2",'displayable=false','');


INSERT INTO fieldmetainfo VALUES(NULL,"_uId","UID","UID","java.lang.Long","com.gridnode.pdip.base.gwfbase.xpdl.model.XpdlTransitionRestriction","","0","0","0","0","0","","999",'displayable=false','');
INSERT INTO fieldmetainfo VALUES(NULL,"_isInlineBlock","IS_INLINE_BLOCK","IsInlineBlock","java.lang.Boolean","com.gridnode.pdip.base.gwfbase.xpdl.model.XpdlTransitionRestriction","IS_INLINE_BLOCK","0","0","0","1","1","","1",'displayable=false','');
INSERT INTO fieldmetainfo VALUES(NULL,"_blockName","BLOCK_NAME","BlockName","java.lang.String","com.gridnode.pdip.base.gwfbase.xpdl.model.XpdlTransitionRestriction","BLOCK_NAME","50","0","1","1","1","","2",'displayable=false','');
INSERT INTO fieldmetainfo VALUES(NULL,"_blockDescription","BLOCK_DESCRIPTION","BlockDescription","java.lang.String","com.gridnode.pdip.base.gwfbase.xpdl.model.XpdlTransitionRestriction","BLOCK_DESCRIPTION","50","0","1","1","1","","3",'displayable=false','');
INSERT INTO fieldmetainfo VALUES(NULL,"_blockIconUrl","BLOCK_ICON_URL","BlockIconUrl","java.lang.String","com.gridnode.pdip.base.gwfbase.xpdl.model.XpdlTransitionRestriction","BLOCK_ICON_URL","50","0","1","1","1","","4",'displayable=false','');
INSERT INTO fieldmetainfo VALUES(NULL,"_blockDocumentationUrl","BLOCK_DOCUMENTATION_URL","BlockDocumentationUrl","java.lang.String","com.gridnode.pdip.base.gwfbase.xpdl.model.XpdlTransitionRestriction","BLOCK_DOCUMENTATION_URL","50","0","1","1","1","","5",'displayable=false','');
INSERT INTO fieldmetainfo VALUES(NULL,"_blockBeginActivityId","BLOCK_BEGIN_ACTIVITY_ID","BlockBeginActivityId","java.lang.String","com.gridnode.pdip.base.gwfbase.xpdl.model.XpdlTransitionRestriction","BLOCK_BEGIN_ACTIVITY_ID","50","0","1","1","1","","6",'displayable=false','');
INSERT INTO fieldmetainfo VALUES(NULL,"_blockEndActivityId","BLOCK_END_ACTIVITY_ID","BlockEndActivityId","java.lang.String","com.gridnode.pdip.base.gwfbase.xpdl.model.XpdlTransitionRestriction","BLOCK_END_ACTIVITY_ID","50","0","1","1","1","","7",'displayable=false','');
INSERT INTO fieldmetainfo VALUES(NULL,"_joinType","JOIN_TYPE","JoinType","java.lang.String","com.gridnode.pdip.base.gwfbase.xpdl.model.XpdlTransitionRestriction","JOIN_TYPE","50","0","1","1","1","","8",'displayable=false','');
INSERT INTO fieldmetainfo VALUES(NULL,"_splitType","SPLIT_TYPE","SplitType","java.lang.String","com.gridnode.pdip.base.gwfbase.xpdl.model.XpdlTransitionRestriction","SPLIT_TYPE","50","0","1","1","1","","9",'displayable=false','');
INSERT INTO fieldmetainfo VALUES(NULL,"_transitionRefListUId","TRANSITION_REF_LIST_UID","TransitionRefListUId","java.lang.Long","com.gridnode.pdip.base.gwfbase.xpdl.model.XpdlTransitionRestriction","TRANSITION_REF_LIST_UID","0","0","0","0","0","","10",'displayable=false','');
INSERT INTO fieldmetainfo VALUES(NULL,"_listUId","LIST_UID","ListUId","java.lang.Long","com.gridnode.pdip.base.gwfbase.xpdl.model.XpdlTransitionRestriction","LIST_UID","0","0","0","0","0","","11",'displayable=false','');

INSERT INTO fieldmetainfo VALUES(NULL,"_uId","UID","UID","java.lang.Long","com.gridnode.pdip.base.gwfbase.xpdl.model.XpdlTypeDeclaration","","0","0","0","0","0","","999",'displayable=false','');
INSERT INTO fieldmetainfo VALUES(NULL,"_typeId","TYPE_ID","TypeId","java.lang.String","com.gridnode.pdip.base.gwfbase.xpdl.model.XpdlTypeDeclaration","TYPE_ID","50","0","1","1","1","","1",'displayable=false','');
INSERT INTO fieldmetainfo VALUES(NULL,"_typeName","TYPE_NAME","TypeName","java.lang.String","com.gridnode.pdip.base.gwfbase.xpdl.model.XpdlTypeDeclaration","TYPE_NAME","50","0","1","1","1","","2",'displayable=false','');
INSERT INTO fieldmetainfo VALUES(NULL,"_typeDescription","TYPE_DESCRIPTION","TypeDescription","java.lang.String","com.gridnode.pdip.base.gwfbase.xpdl.model.XpdlTypeDeclaration","TYPE_DESCRIPTION","50","0","1","1","1","","3",'displayable=false','');
INSERT INTO fieldmetainfo VALUES(NULL,"_extendedAttributes","EXTENDED_ATTRIBUTES","ExtendedAttributes","java.lang.String","com.gridnode.pdip.base.gwfbase.xpdl.model.XpdlTypeDeclaration","EXTENDED_ATTRIBUTE_LIST_UID","0","0","0","0","0","","4",'displayable=false','');
INSERT INTO fieldmetainfo VALUES(NULL,"_packageId","PACKAGE_ID","PackageId","java.lang.String","com.gridnode.pdip.base.gwfbase.xpdl.model.XpdlTypeDeclaration","PACKAGE_ID","50","0","1","1","1","","5",'displayable=false','');
INSERT INTO fieldmetainfo VALUES(NULL,"_pkgVersionId","PKG_VERSION_ID","PkgVersionId","java.lang.String","com.gridnode.pdip.base.gwfbase.xpdl.model.XpdlTypeDeclaration","PKG_VERSION_ID","50","0","1","1","1","","6",'displayable=false','');
INSERT INTO fieldmetainfo VALUES(NULL,"_dataTypeName","DATATYPE_NAME","DataTypeName","java.lang.String","com.gridnode.pdip.base.gwfbase.xpdl.model.XpdlTypeDeclaration","DATATYPE_NAME","50","0","1","1","1","","1",'displayable=false','');
INSERT INTO fieldmetainfo VALUES(NULL,"_complexDataTypeUId","COMPLEX_DATATYPE_UID","ComplexDataTypeUId","java.lang.Long","com.gridnode.pdip.base.gwfbase.xpdl.model.XpdlTypeDeclaration","COMPLEX_DATATYPE_UID","0","0","0","0","0","","4",'displayable=false','');

INSERT INTO fieldmetainfo VALUES(NULL,"_uId","UID","UID","java.lang.Long","com.gridnode.pdip.base.gwfbase.xpdl.model.XpdlComplexDataType","","0","0","0","0","0","","999",'displayable=false','');
INSERT INTO fieldmetainfo VALUES(NULL,"_dataTypeName","DATATYPE_NAME","DataTypeName","java.lang.String","com.gridnode.pdip.base.gwfbase.xpdl.model.XpdlComplexDataType","DATATYPE_NAME","50","0","1","1","1","","1",'displayable=false','');
INSERT INTO fieldmetainfo VALUES(NULL,"_complexDataTypeUId","COMPLEX_DATATYPE_UID","ComplexDataTypeUId","java.lang.Long","com.gridnode.pdip.base.gwfbase.xpdl.model.XpdlComplexDataType","COMPLEX_DATATYPE_UID","0","0","0","0","0","","4",'displayable=false','');
INSERT INTO fieldmetainfo VALUES(NULL,"_subTypeUId","SUBTYPE_UID","SubTypeUId","java.lang.Long","com.gridnode.pdip.base.gwfbase.xpdl.model.XpdlComplexDataType","SUBTYPE_UID","0","0","0","0","0","","4",'displayable=false','');
INSERT INTO fieldmetainfo VALUES(NULL,"_arrayLowerIndex","ARRAY_LOWERINDEX","ArrayLowerIndex","java.lang.Integer","com.gridnode.pdip.base.gwfbase.xpdl.model.XpdlComplexDataType","ARRAY_LOWERINDEX","0","0","0","0","0","","4",'displayable=false','');
INSERT INTO fieldmetainfo VALUES(NULL,"_arrayUpperIndex","ARRAY_UPPERINDEX","ArrayUpperIndex","java.lang.Integer","com.gridnode.pdip.base.gwfbase.xpdl.model.XpdlComplexDataType","ARRAY_UPPERINDEX","0","0","0","0","0","","4",'displayable=false','');

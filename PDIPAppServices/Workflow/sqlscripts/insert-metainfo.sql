# MySQL-Front Dump 2.1
#
# Host: localhost   Database: appdb
#--------------------------------------------------------
# Server version 4.0.0-alpha

USE appdb;

# Dumping data for table 'entitymetainfo'
INSERT INTO `entitymetainfo` VALUES("com.gridnode.pdip.app.workflow.runtime.model.GWFRtProcess","GWFRtProcess","rtprocess");
INSERT INTO `entitymetainfo` VALUES("com.gridnode.pdip.app.workflow.runtime.model.GWFRtActivity","GWFRtActivity","rtactivity");
INSERT INTO `entitymetainfo` VALUES("com.gridnode.pdip.app.workflow.runtime.model.GWFRtRestriction","GWFRtRestriction","rtrestriction");
INSERT INTO `entitymetainfo` VALUES("com.gridnode.pdip.app.workflow.runtime.model.GWFTransActivationState","GWFTransActivationState","trans_activation_state");
INSERT INTO `entitymetainfo` VALUES("com.gridnode.pdip.app.workflow.runtime.model.GWFRtProcessDoc","GWFRtProcessDoc","rtprocessdoc");
INSERT INTO `entitymetainfo` VALUES("com.gridnode.pdip.app.workflow.runtime.model.GWFArchiveProcess","GWFArchiveProcess","archive_process_view");

# Dumping data for table 'fieldmetainfo'

INSERT INTO fieldmetainfo VALUES (NULL,"_uId","UID","UID","java.lang.Long","com.gridnode.pdip.app.workflow.runtime.model.GWFRtActivity","",0,"0","0","1","1","",999,'displayable=false','');
INSERT INTO fieldmetainfo VALUES (NULL,"_activityUId","ACTIVITY_UID","ActivityUId","java.lang.Long","com.gridnode.pdip.app.workflow.runtime.model.GWFRtActivity","",0,"0","0","1","1","",999,'displayable=false','');
INSERT INTO fieldmetainfo VALUES (NULL,"_state","STATE","State","java.lang.Integer","com.gridnode.pdip.app.workflow.runtime.model.GWFRtActivity","",0,"0","0","1","1","",999,'displayable=false','');
INSERT INTO fieldmetainfo VALUES (NULL,"_priority","PRIORITY","Priority","java.lang.Integer","com.gridnode.pdip.app.workflow.runtime.model.GWFRtActivity","",0,"0","0","1","1","",999,'displayable=false','');
INSERT INTO fieldmetainfo VALUES (NULL,"_rtProcessUId","RT_PROCESS_UID","RtProcessUId","java.lang.Long","com.gridnode.pdip.app.workflow.runtime.model.GWFRtActivity","",0,"0","0","1","1","",999,'displayable=false','');
INSERT INTO fieldmetainfo VALUES (NULL,"_activityType","ACTIVITY_TYPE","ActivityType","java.lang.String","com.gridnode.pdip.app.workflow.runtime.model.GWFRtActivity","",50,"0","0","1","1","",999,'displayable=false','');
INSERT INTO fieldmetainfo VALUES (NULL,'_finishInterval','FINISH_INTERVAL','FinishInterval','java.lang.Long','com.gridnode.pdip.app.workflow.runtime.model.GWFRtActivity','',0,'0','0','1','1','',999,'displayable=false','');
INSERT INTO fieldmetainfo VALUES (NULL,'_startTime','START_TIME','StartTime','java.util.Date','com.gridnode.pdip.app.workflow.runtime.model.GWFRtActivity','',0,'0','0','1','1','',999,'displayable=false','');
INSERT INTO fieldmetainfo VALUES (NULL,'_endTime','END_TIME','EndTime','java.util.Date','com.gridnode.pdip.app.workflow.runtime.model.GWFRtActivity','',0,'0','0','1','1','',999,'displayable=false','');
INSERT INTO fieldmetainfo VALUES (NULL,"_contextUId","CONTEXT_UID","ContextUId","java.lang.Long","com.gridnode.pdip.app.workflow.runtime.model.GWFRtActivity","",0,"0","0","1","1","",999,'displayable=false','');
INSERT INTO fieldmetainfo VALUES (NULL,"_engineType","ENGINE_TYPE","EngineType","java.lang.String","com.gridnode.pdip.app.workflow.runtime.model.GWFRtActivity","",30,"0","0","1","1","",999,'displayable=false','');
INSERT INTO fieldmetainfo VALUES (NULL,"_branchName","BRANCH_NAME","BranchName","java.lang.String","com.gridnode.pdip.app.workflow.runtime.model.GWFRtActivity","",50,"0","0","1","1","",999,'displayable=false','');
INSERT INTO fieldmetainfo VALUES (NULL,"_processDefKey","PROCESSDEF_KEY","ProcessDefKey","java.lang.String","com.gridnode.pdip.app.workflow.runtime.model.GWFRtActivity","",50,"0","0","1","1","",999,'displayable=false','');

INSERT INTO fieldmetainfo VALUES (NULL,"_uId","UID","UID","java.lang.Long","com.gridnode.pdip.app.workflow.runtime.model.GWFRtProcess","",0,"0","0","1","1","",999,'displayable=false','');
INSERT INTO fieldmetainfo VALUES (NULL,"_processUId","PROCESS_UID","ProcessUId","java.lang.Long","com.gridnode.pdip.app.workflow.runtime.model.GWFRtProcess","",0,"0","0","1","1","",999,'displayable=false','');
INSERT INTO fieldmetainfo VALUES (NULL,"_state","STATE","State","java.lang.Integer","com.gridnode.pdip.app.workflow.runtime.model.GWFRtProcess","",0,"0","0","1","1","",999,'displayable=false','');
INSERT INTO fieldmetainfo VALUES (NULL,"_processType","PROCESS_TYPE","ProcessType","java.lang.String","com.gridnode.pdip.app.workflow.runtime.model.GWFRtProcess","",50,"0","0","1","1","",999,'displayable=false','');
INSERT INTO fieldmetainfo VALUES (NULL,"_parentRtActivityUId","PARENT_RTACTIVITY_UID","ParentRtActivityUId","java.lang.Long","com.gridnode.pdip.app.workflow.runtime.model.GWFRtProcess","",0,"0","0","1","1","",999,'displayable=false','');
INSERT INTO fieldmetainfo VALUES (NULL,"_maxConcurrency","MAX_CONCURRENCY","MaxConcurrency","java.lang.Integer","com.gridnode.pdip.app.workflow.runtime.model.GWFRtProcess","",0,"0","0","1","1","",999,'displayable=false','');
INSERT INTO fieldmetainfo VALUES (NULL,'_finishInterval','FINISH_INTERVAL','FinishInterval','java.lang.Long','com.gridnode.pdip.app.workflow.runtime.model.GWFRtProcess','',0,'0','0','1','1','',999,'displayable=false','');
INSERT INTO fieldmetainfo VALUES (NULL,'_startTime','START_TIME','StartTime','java.util.Date','com.gridnode.pdip.app.workflow.runtime.model.GWFRtProcess','',0,'0','0','1','1','',999,'displayable=false','');
INSERT INTO fieldmetainfo VALUES (NULL,'_endTime','END_TIME','EndTime','java.util.Date','com.gridnode.pdip.app.workflow.runtime.model.GWFRtProcess','',0,'0','0','1','1','',999,'displayable=false','');
INSERT INTO fieldmetainfo VALUES (NULL,"_contextUId","CONTEXT_UID","ContextUId","java.lang.Long","com.gridnode.pdip.app.workflow.runtime.model.GWFRtProcess","",0,"0","0","1","1","",999,'displayable=false','');
INSERT INTO fieldmetainfo VALUES (NULL,"_engineType","ENGINE_TYPE","EngineType","java.lang.String","com.gridnode.pdip.app.workflow.runtime.model.GWFRtProcess","",30,"0","0","1","1","",999,'displayable=false','');
INSERT INTO fieldmetainfo VALUES (NULL,"_processDefKey","PROCESSDEF_KEY","ProcessDefKey","java.lang.String","com.gridnode.pdip.app.workflow.runtime.model.GWFRtProcess","",50,"0","0","1","1","",999,'displayable=false','');

INSERT INTO fieldmetainfo VALUES (NULL,"_uId","UID","UID","java.lang.Long","com.gridnode.pdip.app.workflow.runtime.model.GWFRtRestriction","",0,"0","0","1","1","",999,'displayable=false','');
INSERT INTO fieldmetainfo VALUES (NULL,"_restrictionUId","RESTRICTION_UID","RestrictionUId","java.lang.Long","com.gridnode.pdip.app.workflow.runtime.model.GWFRtRestriction","",0,"0","0","1","1","",999,'displayable=false','');
INSERT INTO fieldmetainfo VALUES (NULL,"_restrictionType","RESTRICTION_TYPE","RestrictionType","java.lang.String","com.gridnode.pdip.app.workflow.runtime.model.GWFRtRestriction","",50,"0","0","1","1","",999,'displayable=false','');
INSERT INTO fieldmetainfo VALUES (NULL,"_subRestrictionType","SUB_RESTRICTION_TYPE","SubRestrictionType","java.lang.String","com.gridnode.pdip.app.workflow.runtime.model.GWFRtRestriction","",50,"0","0","1","1","",999,'displayable=false','');
INSERT INTO fieldmetainfo VALUES (NULL,"_rtProcessUId","RT_PROCESS_UID","RtProcessUId","java.lang.Long","com.gridnode.pdip.app.workflow.runtime.model.GWFRtRestriction","",0,"0","0","1","1","",999,'displayable=false','');
INSERT INTO fieldmetainfo VALUES (NULL,"_transActivationStateListUId","TRANS_ACTIVATION_STATE_LIST_UID","TransActivationStateListUId","java.lang.Long","com.gridnode.pdip.app.workflow.runtime.model.GWFRtRestriction","",0,"0","0","1","1","",999,'displayable=false','');
INSERT INTO fieldmetainfo VALUES (NULL,"_processDefKey","PROCESSDEF_KEY","ProcessDefKey","java.lang.String","com.gridnode.pdip.app.workflow.runtime.model.GWFRtRestriction","",50,"0","0","1","1","",999,'displayable=false','');

INSERT INTO fieldmetainfo VALUES (NULL,"_uId","UID","UID","java.lang.Long","com.gridnode.pdip.app.workflow.runtime.model.GWFTransActivationState","",0,"0","0","1","1","",999,'displayable=false','');
INSERT INTO fieldmetainfo VALUES (NULL,"_transitionUId","TRANSITION_UID","TransitionUId","java.lang.Long","com.gridnode.pdip.app.workflow.runtime.model.GWFTransActivationState","",0,"0","0","1","1","",999,'displayable=false','');
INSERT INTO fieldmetainfo VALUES (NULL,"_rtRestrictionUId","RT_RESTRICTION_UID","RtRestrictionUId","java.lang.Long","com.gridnode.pdip.app.workflow.runtime.model.GWFTransActivationState","",0,"0","0","1","1","",999,'displayable=false','');
INSERT INTO fieldmetainfo VALUES (NULL,"_rtRestrictionType","RT_RESTRICTION_TYPE","RtRestrictionType","java.lang.String","com.gridnode.pdip.app.workflow.runtime.model.GWFTransActivationState","",50,"0","0","1","1","",999,'displayable=false','');
INSERT INTO fieldmetainfo VALUES (NULL,"_state","STATE","State","java.lang.Long","com.gridnode.pdip.app.workflow.runtime.model.GWFTransActivationState","",0,"0","0","1","1","",999,'displayable=false','');
INSERT INTO fieldmetainfo VALUES (NULL,"_listUId","LIST_UID","ListUId","java.lang.Long","com.gridnode.pdip.app.workflow.runtime.model.GWFTransActivationState","LIST_UID","0","0","0","0","0","","11",'displayable=false','');

# for processdoc
INSERT INTO fieldmetainfo VALUES(NULL,"_uId","UID","UID","java.lang.Long","com.gridnode.pdip.app.workflow.runtime.model.GWFRtProcessDoc","","0","0","0","0","0","","999",'displayable=false','');
INSERT INTO fieldmetainfo VALUES(NULL,"_documentId","DOCUMENT_ID","DocumentId","java.lang.String","com.gridnode.pdip.app.workflow.runtime.model.GWFRtProcessDoc","DOCUMENT_ID","50","0","1","1","1","","1",'displayable=false','');
INSERT INTO fieldmetainfo VALUES(NULL,"_documentType","DOCUMENT_TYPE","DocumentType","java.lang.String","com.gridnode.pdip.app.workflow.runtime.model.GWFRtProcessDoc","DOCUMENT_TYPE","50","0","1","1","1","","2",'displayable=false','');
INSERT INTO fieldmetainfo VALUES(NULL,"_documentName","DOCUMENT_NAME","DocumentName","java.lang.String","com.gridnode.pdip.app.workflow.runtime.model.GWFRtProcessDoc","DOCUMENT_NAME","50","0","1","1","1","","3",'displayable=false','');
INSERT INTO fieldmetainfo VALUES(NULL,"_businessTransActivityId","BUSINESS_TRANSACTIVITY_ID","BusinessTransActivityId","java.lang.String","com.gridnode.pdip.app.workflow.runtime.model.GWFRtProcessDoc","BUSINESS_TRANSACTIVITY_ID","50","0","1","1","1","","3",'displayable=false','');
INSERT INTO fieldmetainfo VALUES(NULL,"_binaryCollaborationUId","BINARY_COLLABORATION_UID","BinaryCollaborationUId","java.lang.Long","com.gridnode.pdip.app.workflow.runtime.model.GWFRtProcessDoc","BINARY_COLLABORATION_UID","0","0","0","0","0","","4",'displayable=false','');
INSERT INTO fieldmetainfo VALUES(NULL,"_rtBinaryCollaborationUId","RT_BINARY_COLLABORATION_UID","RtBinaryCollaborationUId","java.lang.Long","com.gridnode.pdip.app.workflow.runtime.model.GWFRtProcessDoc","RT_BINARY_COLLABORATION_UID","0","0","0","0","0","","5",'displayable=false','');
INSERT INTO fieldmetainfo VALUES(NULL,"_rtBusinessTransactionUId","RT_BUSINESS_TRANSACTION_UID","RtBusinessTransactionUId","java.lang.Long","com.gridnode.pdip.app.workflow.runtime.model.GWFRtProcessDoc","RT_BUSINESS_TRANSACTION_UID","0","0","0","0","0","","6",'displayable=false','');
INSERT INTO fieldmetainfo VALUES(NULL,"_isPositiveResponse","IS_POSITIVE_RESPONSE","IsPositiveResponse","java.lang.Boolean","com.gridnode.pdip.app.workflow.runtime.model.GWFRtProcessDoc","IS_POSITIVE_RESPONSE","0","0","0","0","0","","6",'displayable=false','');
INSERT INTO fieldmetainfo VALUES(NULL,"_docProcessedFlag","DOC_PROCESSED_FLAG","DocProcessedFlag","java.lang.Boolean","com.gridnode.pdip.app.workflow.runtime.model.GWFRtProcessDoc","DOC_PROCESSED_FLAG","0","0","0","0","0","","6",'displayable=false','');
INSERT INTO fieldmetainfo VALUES(NULL,"_ackReceiptSignalFlag","ACK_RECEIPTSIGNAL_FLAG","AckReceiptSignalFlag","java.lang.Boolean","com.gridnode.pdip.app.workflow.runtime.model.GWFRtProcessDoc","ACK_RECEIPTSIGNAL_FLAG","0","0","0","0","0","","6",'displayable=false','');
INSERT INTO fieldmetainfo VALUES(NULL,"_ackAcceptSignalFlag","ACK_ACCEPTSIGNAL_FLAG","AckAcceptSignalFlag","java.lang.Boolean","com.gridnode.pdip.app.workflow.runtime.model.GWFRtProcessDoc","ACK_ACCEPTSIGNAL_FLAG","0","0","0","0","0","","6",'displayable=false','');
INSERT INTO fieldmetainfo VALUES(NULL,"_exceptionSignalType","EXCEPTIONSIGNAL_TYPE","ExceptionSignalType","java.lang.String","com.gridnode.pdip.app.workflow.runtime.model.GWFRtProcessDoc","EXCEPTIONSIGNAL_TYPE","0","0","0","0","0","","6",'displayable=false','');
INSERT INTO fieldmetainfo VALUES(NULL,"_roleType","ROLE_TYPE","RoleType","java.lang.String","com.gridnode.pdip.app.workflow.runtime.model.GWFRtProcessDoc","ROLE_TYPE","50","0","1","1","1","","3",'displayable=false','');
INSERT INTO fieldmetainfo VALUES(NULL,"_retryCount","RETRY_COUNT","RetryCount","java.lang.Integer","com.gridnode.pdip.app.workflow.runtime.model.GWFRtProcessDoc","RETRY_COUNT","0","0","0","0","0","","6",'displayable=false','');
INSERT INTO fieldmetainfo VALUES(NULL,"_requestDocType","REQUESTDOC_TYPE","RequestDocType","java.lang.String","com.gridnode.pdip.app.workflow.runtime.model.GWFRtProcessDoc","REQUESTDOC_TYPE","50","0","1","1","1","","3",'displayable=false','');
INSERT INTO fieldmetainfo VALUES(NULL,"_responseDocTypes","RESPONSEDOC_TYPES","ResponseDocTypes","java.lang.String","com.gridnode.pdip.app.workflow.runtime.model.GWFRtProcessDoc","RESPONSEDOC_TYPES","50","0","1","1","1","","3",'displayable=false','');
INSERT INTO fieldmetainfo VALUES(NULL,"_partnerKey","PARTNER_KEY","PartnerKey","java.lang.String","com.gridnode.pdip.app.workflow.runtime.model.GWFRtProcessDoc","PARTNER_KEY","50","0","1","1","1","","3",'displayable=false','');
INSERT INTO fieldmetainfo VALUES(NULL,"_status","STATUS","Status","java.lang.Integer","com.gridnode.pdip.app.workflow.runtime.model.GWFRtProcessDoc","STATUS","0","0","0","0","0","","6",'displayable=false','');
INSERT INTO fieldmetainfo VALUES(NULL,"_reason","REASON","Reason","ObjectSER","com.gridnode.pdip.app.workflow.runtime.model.GWFRtProcessDoc","REASON","50","0","1","1","1","","3",'displayable=false','');
INSERT INTO fieldmetainfo VALUES(NULL,"_customerBEID","CUSTOMER_BE_ID","CustomerBEId","java.lang.String","com.gridnode.pdip.app.workflow.runtime.model.GWFRtProcessDoc","CUSTOMER_BE_ID","4","0","0","0","0","","0",'displayable=false','');


# for archiveProcess
INSERT INTO fieldmetainfo VALUES (NULL,"_uId","RT_PROCESS_UID","RTProcessUID","java.lang.Long","com.gridnode.pdip.app.workflow.runtime.model.GWFArchiveProcess","",0,"0","0","1","1","",999,'displayable=false','');
INSERT INTO fieldmetainfo VALUES (NULL,"_engineType","ENGINE_TYPE","EngineType","java.lang.String","com.gridnode.pdip.app.workflow.runtime.model.GWFArchiveProcess","",30,"0","0","1","1","",999,'displayable=false','');
INSERT INTO fieldmetainfo VALUES (NULL,"_processType","PROCESS_TYPE","ProcessType","java.lang.String","com.gridnode.pdip.app.workflow.runtime.model.GWFArchiveProcess","",50,"0","0","1","1","",999,'displayable=false','');
INSERT INTO fieldmetainfo VALUES (NULL,'_processStartTime','PROCESS_START_TIME','StartTime','java.util.Date','com.gridnode.pdip.app.workflow.runtime.model.GWFArchiveProcess','',0,'0','0','1','1','',999,'displayable=false','');
INSERT INTO fieldmetainfo VALUES (NULL,'_processEndTime','PROCESS_END_TIME','EndTime','java.util.Date','com.gridnode.pdip.app.workflow.runtime.model.GWFArchiveProcess','',0,'0','0','1','1','',999,'displayable=false','');
INSERT INTO fieldmetainfo VALUES (NULL,"_processUID","PROCESS_UID","ProcessUId","java.lang.Long","com.gridnode.pdip.app.workflow.runtime.model.GWFArchiveProcess","",0,"0","0","1","1","",999,'displayable=false','');
INSERT INTO fieldmetainfo VALUES (NULL,"_rtprocessDocUID","RT_PROCESS_DOC_UID","RTProcessDocUID","java.lang.Long","com.gridnode.pdip.app.workflow.runtime.model.GWFArchiveProcess","","0","0","0","0","0","","999",'displayable=false','');
INSERT INTO fieldmetainfo VALUES (NULL,"_customerBEId","CUSTOMER_BE_ID","CustomerBEId","java.lang.String","com.gridnode.pdip.app.workflow.runtime.model.GWFArchiveProcess","CUSTOMER_BE_ID","4","0","0","0","0","","0",'displayable=false','');
INSERT INTO fieldmetainfo VALUES (NULL,"_partnerKey","PARTNER_KEY","PartnerKey","java.lang.String","com.gridnode.pdip.app.workflow.runtime.model.GWFArchiveProcess","PARTNER_KEY","50","0","1","1","1","","3",'displayable=false','');
INSERT INTO fieldmetainfo VALUES (NULL,"_processStatus","PROCESS_STATUS","ProcessStatus","java.lang.Integer","com.gridnode.pdip.app.workflow.runtime.model.GWFArchiveProcess","STATUS","0","0","0","0","0","","6",'displayable=false','');
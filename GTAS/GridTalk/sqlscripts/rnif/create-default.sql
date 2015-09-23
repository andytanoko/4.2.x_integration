# Change Log
# 16 Jul 2003 GT 2.2 I1 [Neo Sok Lay] New CanDelete column value default to "false" for default ProcessDefs.
# 06 Jan 2006 GT 4.0 I1 [Neo Sok Lay] Add '_' in front of document type
# 20 Jan 2006 GT 4.0 I1 [Neo Sok Lay] Set user tracking identifier xpath for failure notification

USE userdb;


#
# Dumping data for table 'bpss_bin_coll'
#
INSERT INTO bpss_bin_coll VALUES("-1","Distribute Notification of Failure",null,null,null,null,null,null,"1");
INSERT INTO bpss_bin_coll VALUES("-2","Distribute Notification of Failure",null,null,null,null,null,null,"1");


#
# Dumping data for table 'bpss_bin_coll_act'
#


#
# Dumping data for table 'bpss_biz_partner_role'
#


#
# Dumping data for table 'bpss_biz_tran'
#
INSERT INTO bpss_biz_tran VALUES("-1","Distribute Notification of Failure",null,null,null,"0",null,null,"-1","-1","1");
INSERT INTO bpss_biz_tran VALUES("-2","Distribute Notification of Failure",null,null,null,"0",null,null,"-2","-2","1");


#
# Dumping data for table 'bpss_biz_tran_activity'
#
INSERT INTO bpss_biz_tran_activity VALUES("-1","Distribute Notification of Failure",null,"1","1","-1","PIP Failure Notifier","Failure Report Administrator");
INSERT INTO bpss_biz_tran_activity VALUES("-2","Distribute Notification of Failure",null,"1","1","-2","PIP Failure Notifier","Failure Report Administrator");


#
# Dumping data for table 'bpss_businessdocument'
#
INSERT INTO bpss_businessdocument VALUES("-1","REQUESTDOC",null,null,null,null);
INSERT INTO bpss_businessdocument VALUES("-2","REQUESTDOC",null,null,null,null);


#
# Dumping data for table 'bpss_coll_activity'
#


#
# Dumping data for table 'bpss_completion_state'
#
INSERT INTO bpss_completion_state VALUES("-1","-1","BpssBinaryCollaboration",null,"BusinessFailure",null,null,"http://wfActivity/-1/BpssBusinessTransActivity","Failure");
INSERT INTO bpss_completion_state VALUES("-2","-1","BpssBinaryCollaboration",null,"TechnicalFailure",null,null,"http://wfActivity/-1/BpssBusinessTransActivity","Failure");
INSERT INTO bpss_completion_state VALUES("-3","-1","BpssBinaryCollaboration",null,"Success",null,null,"http://wfActivity/-1/BpssBusinessTransActivity","Success");
INSERT INTO bpss_completion_state VALUES("-4","-2","BpssBinaryCollaboration",null,"BusinessFailure",null,null,"http://wfActivity/-2/BpssBusinessTransActivity","Failure");
INSERT INTO bpss_completion_state VALUES("-5","-2","BpssBinaryCollaboration",null,"TechnicalFailure",null,null,"http://wfActivity/-2/BpssBusinessTransActivity","Failure");
INSERT INTO bpss_completion_state VALUES("-6","-2","BpssBinaryCollaboration",null,"Success",null,null,"http://wfActivity/-2/BpssBusinessTransActivity","Success");


#
# Dumping data for table 'bpss_documentation'
#
INSERT INTO bpss_documentation VALUES("-1",null,"ebXML BPSS Description generated from processdef 0A1FailureNotification1.1");
INSERT INTO bpss_documentation VALUES("-2",null,"ebXML BPSS Description generated from processdef 0A1FailureNotification");


#
# Dumping data for table 'bpss_documentenvelope'
#
INSERT INTO bpss_documentenvelope VALUES("-1","REQUESTDOC","_0A1FailureNotification1.1_Request","1","0","0","0");
INSERT INTO bpss_documentenvelope VALUES("-2","REQUESTDOC","_0A1FailureNotification_Request","1","0","0","0");


#
# Dumping data for table 'bpss_fork'
#


#
# Dumping data for table 'bpss_join'
#


#
# Dumping data for table 'bpss_multiparty_coll'
#


#
# Dumping data for table 'bpss_proc_spec'
#
INSERT INTO bpss_proc_spec VALUES("-1","0A1FailureNotification1.1","1.0","_0A1FailureNotification1.1","10:52:51");
INSERT INTO bpss_proc_spec VALUES("-2","0A1FailureNotification","V02.00","_0A1FailureNotification","10:56:52");


#
# Dumping data for table 'bpss_proc_spec_entry'
#
INSERT INTO bpss_proc_spec_entry VALUES("-1","-1","-1","Failure::Distribute Notification of Failure::1","BpssCompletionState","-6");
INSERT INTO bpss_proc_spec_entry VALUES("-2","-1","-2","Failure::Distribute Notification of Failure::2","BpssCompletionState","-6");
INSERT INTO bpss_proc_spec_entry VALUES("-3","-1","-1","Distribute Notification of Failure","BpssBusinessTransActivity","-6");
INSERT INTO bpss_proc_spec_entry VALUES("-4","-1","-1","Start::Distribute Notification of Failure::0","BpssStart","-6");
INSERT INTO bpss_proc_spec_entry VALUES("-5","-1","-3","Success::Distribute Notification of Failure::3","BpssCompletionState","-6");
INSERT INTO bpss_proc_spec_entry VALUES("-6","-1","-1","Distribute Notification of Failure","BpssBinaryCollaboration","0");
INSERT INTO bpss_proc_spec_entry VALUES("-7","-1","-1","REQUESTDOC","BpssBusinessDocument","0");
INSERT INTO bpss_proc_spec_entry VALUES("-8","-1","-1","REQUESTDOC","BpssDocumentEnvelope","-9");
INSERT INTO bpss_proc_spec_entry VALUES("-9","-1","-1","Failure Notification","BpssReqBusinessActivity","-11");
INSERT INTO bpss_proc_spec_entry VALUES("-10","-1","-1",null,"BpssResBusinessActivity","-11");
INSERT INTO bpss_proc_spec_entry VALUES("-11","-1","-1","Distribute Notification of Failure","BpssBusinessTrans","0");
INSERT INTO bpss_proc_spec_entry VALUES("-12","-1","-1","1","BpssDocumentation","0");
INSERT INTO bpss_proc_spec_entry VALUES("-13","-2","-4","Failure::Distribute Notification of Failure::4","BpssCompletionState","-18");
INSERT INTO bpss_proc_spec_entry VALUES("-14","-2","-5","Failure::Distribute Notification of Failure::5","BpssCompletionState","-18");
INSERT INTO bpss_proc_spec_entry VALUES("-15","-2","-2","Distribute Notification of Failure","BpssBusinessTransActivity","-18");
INSERT INTO bpss_proc_spec_entry VALUES("-16","-2","-2","Start::Distribute Notification of Failure::0","BpssStart","-18");
INSERT INTO bpss_proc_spec_entry VALUES("-17","-2","-6","Success::Distribute Notification of Failure::6","BpssCompletionState","-18");
INSERT INTO bpss_proc_spec_entry VALUES("-18","-2","-2","Distribute Notification of Failure","BpssBinaryCollaboration","0");
INSERT INTO bpss_proc_spec_entry VALUES("-19","-2","-2","REQUESTDOC","BpssBusinessDocument","0");
INSERT INTO bpss_proc_spec_entry VALUES("-20","-2","-2","REQUESTDOC","BpssDocumentEnvelope","-21");
INSERT INTO bpss_proc_spec_entry VALUES("-21","-2","-2","Failure Notification","BpssReqBusinessActivity","-23");
INSERT INTO bpss_proc_spec_entry VALUES("-22","-2","-2",null,"BpssResBusinessActivity","-23");
INSERT INTO bpss_proc_spec_entry VALUES("-23","-2","-2","Distribute Notification of Failure","BpssBusinessTrans","0");
INSERT INTO bpss_proc_spec_entry VALUES("-24","-2","-2","2","BpssDocumentation","0");


#
# Dumping data for table 'bpss_req_biz_activity'
#
INSERT INTO bpss_req_biz_activity VALUES("-1","Failure Notification","0","0","PT2H","0","0",null);
INSERT INTO bpss_req_biz_activity VALUES("-2","Failure Notification","0","0","PT2H","0","0",null);


#
# Dumping data for table 'bpss_res_biz_activity'
#
INSERT INTO bpss_res_biz_activity VALUES("-1",null,"0","0",null,"0","0");
INSERT INTO bpss_res_biz_activity VALUES("-2",null,"0","0",null,"0","0");


#
# Dumping data for table 'bpss_start'
#
INSERT INTO bpss_start VALUES("-1","-1","0","http://wfActivity/-1/BpssBusinessTransActivity");
INSERT INTO bpss_start VALUES("-2","-2","0","http://wfActivity/-2/BpssBusinessTransActivity");


#
# Dumping data for table 'bpss_transition'
#


#
# Dumping data for table 'process_act'
#
INSERT INTO process_act VALUES("-1","-1","1",-1,"3","7200","0","0","0","Distribute Notification of Failure","Failure Notification Action",-3,null,null,"0","0","1","1","0","0","MD5","3DES_EDE/CBC/PKCS5Padding","168", null);
INSERT INTO process_act VALUES("-2","-2","1",-2,"3","7200","0","0","0","Distribute Notification of Failure","Failure Notification Action",-4,null,null,"0","0","1","1","0","0","MD5","3DES_EDE/CBC/PKCS5Padding","168", null);


#
# Dumping data for table 'process_def'
#
INSERT INTO process_def VALUES("-1","0A1FailureNotification1.1",null,"SingleActionProcess","RNIF1.1","PIP Failure Notifier","PIP Failure Notifier Service","Distributor","Failure Report Administrator","Failure Report Administrator Service","End User","0A1","1.0","Test","//Pip0A1FailureNotification/thisDocumentIdentifier/ProprietaryDocumentIdentifier",null,null,"//Pip0A1FailureNotification/ProcessIdentity/InstanceIdentifier","0","0");
INSERT INTO process_def VALUES("-2","0A1FailureNotification",null,"SingleActionProcess","RNIF2.0","PIP Failure Notifier","PIP Failure Notifier Service","Distributor","Failure Report Administrator","Failure Report Administrator Service","End User","0A1","V02.00","Test","//Pip0A1FailureNotification/thisDocumentIdentifier/ProprietaryDocumentIdentifier",null,null,"//Pip0A1FailureNotification/ProcessIdentity/InstanceIdentifier","0","0");





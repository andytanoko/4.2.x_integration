# 28 Feb 2003 I8 v2.0.16 [Neo Sok Lay] Add default message templates and alerts for RNException, Mapping and user procedure failures.
# 

use userdb;

INSERT INTO alert_type VALUES("5", "DOC_PROCESS_FAILURE", "Document Processing Failure");

INSERT INTO action VALUES("-1","MAPPING_FAILURE","Mapping Failure Action","-1");
INSERT INTO action VALUES("-2","USER_PROCEDURE_FAILURE","User Procedure Failure Action","-2");
INSERT INTO action VALUES("-3","PS_RECEIVE_EXCEPTION","Receive RosettaNet Exception Action","-3");

INSERT INTO alert VALUES("-1","MAPPING_FAILURE","5","","","Mapping Failure Alert");
INSERT INTO alert VALUES("-2","USER_PROCEDURE_FAILURE","5","","","User Procedure Failure Alert");
INSERT INTO alert VALUES("-3","PS_RECEIVE_EXCEPTION","5","","","Receive RosettaNet Exception Alert");

INSERT INTO alert_action VALUES("-1","-1","-1");
INSERT INTO alert_action VALUES("-2","-2","-2");
INSERT INTO alert_action VALUES("-3","-3","-3");

INSERT INTO message_template VALUES("-1","MAPPING_FAILURE","Text","EMail","<#USER=admin#>","<#USER=admin#>","","Mapping failure on document <%GridDocument.U_DOC_FILENAME%>","An error has occurred while applying mapping rule on document <%GridDocument.U_DOC_FILENAME%>.\r\n\r\nDocument Details:\r\n-----------------\r\nDocument Type    : <%GridDocument.U_DOC_DOC_TYPE%>\r\nFile Type        : <%GridDocument.U_DOC_FILE_TYPE%>\r\nGridDoc ID       : <%GridDocument.G_DOC_ID%>\r\nFolder           : <%GridDocument.FOLDER%>\r\nPartner Function : <%GridDocument.PARTNER_FUNCTION%>\r\n\r\nMapping Rule Details:\r\n---------------------\r\nMapping Rule     : <%Mapping.NAME%>\r\nMapping Type     : <%Mapping.TYPE%> - [0] Convert [1] Transform [2] Split\r\nMapping Filename : <%Mapping.MAPPING_FILE_NAME%>\r\n\r\nException : <%Exception.MESSAGE%>\r\nTrace:\r\n------\r\n<%Exception.STACK_TRACE%>\r\n","","");
INSERT INTO message_template VALUES("-2","USER_PROCEDURE_FAILURE","Text","EMail","<#USER=admin#>","<#USER=admin#>","","User Procedure execution failure on document <%GridDocument.U_DOC_FILENAME%>","An error has occurred while executing user procedure on document <%GridDocument.U_DOC_FILENAME%>.\r\n\r\nDocument Details:\r\n-----------------\r\nDocument Type    : <%GridDocument.U_DOC_DOC_TYPE%>\r\nFile Type        : <%GridDocument.U_DOC_FILE_TYPE%>\r\nGridDoc ID       : <%GridDocument.G_DOC_ID%>\r\nFolder           : <%GridDocument.FOLDER%>\r\nPartner Function : <%GridDocument.PARTNER_FUNCTION%>\r\n\r\nUser Procedure Details:\r\n-----------------------\r\nProcedure Name : <%UserProcedure.NAME%>\r\nDescription    : <%UserProcedure.DESCRIPTION%>\r\nProcedure Type : <%UserProcedure.PROC_TYPE%> - [1] Shell Executable [2] Java Procedure\r\n\r\nException : <%Exception.MESSAGE%>\r\nTrace:\r\n------\r\n<%Exception.STACK_TRACE%>\r\n","","");
INSERT INTO message_template VALUES("-3","PS_RECEIVE_EXCEPTION","Text","EMail","<#USER=admin#>","<%UserDocument.EMAIL_CODE_RECIPIENTS%>","","Process failed for document <%GridDocument.U_DOC_FILENAME%>","Process has failed for outbound document <%GridDocument.U_DOC_FILENAME%>\r\n\r\nReason: \r\nA RosettaNet Exception Message has been received in the Inbound folder for the process.\r\n\r\nProcess Details:\r\n----------------\r\nDefinition Name : <%Process.PROCESS_DEF_NAME%>\r\nInstance ID     : <%Process.PROCESS_INSTANCE_ID%>\r\nOriginator ID   : <%Process.PROCESS_ORIGINATOR_ID%>\r\n\r\nDocument Details:\r\n-----------------\r\nDocument Type : <%GridDocument.U_DOC_DOC_TYPE%>\r\nGridDoc ID    : <%GridDocument.G_DOC_ID%>\r\n","","");


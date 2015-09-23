# 06 Apr 2006     [Tam Wei Xiang]       Modified current archive alert to ARCHIVE_STARTUP_ALERT.
#                                       Added alert ARCHIVE_COMPLETE_ALERT, ARCHIVE_FAIL_ALERT
# 28 Nov 2006     [Regina Zeng]		Deleted process instance count from 'message_template'
# 05 Dec 2006 GT 4.0 [Neo Sok Lay]      Increase document type field length to 30 chars.
# 20 Dec 2006 GT 4.0 [Neo Sok Lay]      Change the archive message templates


USE userdb;

#Alert
DELETE FROM alert WHERE Name='ARCHIVE_ALERT';
INSERT INTO alert VALUES("-17","ARCHIVE_START_ALERT","17",NULL,NULL,"This alert will be triggered when archive process has been started","1","0");
INSERT INTO alert VALUES("-20","ARCHIVE_COMPLETE_ALERT","17",NULL,NULL,"This alert will be triggered when archive process has been completed.","1","0");
INSERT INTO alert VALUES("-21","ARCHIVE_FAIL_ALERT","17",NULL,NULL,"This alert will be triggered when archive process has been failed","1","0");


#Action
DELETE FROM action WHERE Name='ARCHIVE_ALERT_ACTION';
INSERT INTO action VALUES("-23","ARCHIVE_START_ALERT_EMAIL","Archive Start Email Action","-23","1","0");
INSERT INTO action VALUES("-27","ARCHIVE_COMPLETE_ALERT_EMAIL","Archive Complete Email Action","-27","1","0");
INSERT INTO action VALUES("-28","ARCHIVE_FAIL_ALERT_EMAIL","Archive Fail Email Action","-28","1","0");


#Alert-Action
INSERT INTO alert_action VALUES("-28","-20","-27");
INSERT INTO alert_action VALUES("-29","-21","-28");


#Msg-Template

DELETE FROM message_template WHERE NAME='GTAS ARCHIVE ALERT';
INSERT INTO message_template (UID, Name, ContentType, MessageType, FromAddr, ToAddr, CcAddr, Subject, Message, Location, Append, Version, CanDelete) VALUES (-23,'ARCHIVE_START_EMAIL','Text','EMail','<#USER=admin#>','<#USER=admin#>','','[GRIDTALK] Archive Started','Dear User,\r\n\r\nArchive by [<%ARCHIVE.ARCHIVE_BY%>] has started at: <%ARCHIVE.START_TIME_UTC%> [<%ARCHIVE.START_TIME%>]\r\n\r\nArchive [<%ARCHIVE.ARCHIVE_BY%>] Criteria\r\n-----------------------------------------------------\r\nIs Enable Restore Archive  : <%ARCHIVE.IS_ENABLE_RESTORE_ARCHIVE%>\r\nIs Enable Search Archive   : <%ARCHIVE.IS_ENABLE_SEARCH_ARCHIVE%>\r\nFrom Date/Time             : <%ARCHIVE.FROM_DATE_TIME_UTC%> [<%ARCHIVE.FROM_DATE_TIME%>]\r\nTo Date Time               : <%ARCHIVE.TO_DATE_TIME_UTC%> [<%ARCHIVE.TO_DATE_TIME%>]\r\n<%?NProcess Definition         N?ARCHIVE.IS_DOC_TYPE_NULL#Folder                     %>: <%?NN?ARCHIVE.PROCESS_DEF_LIST%><%?NN?ARCHIVE.FOLDER_LIST%>\r\n<%?NInclude Incomplete         N?ARCHIVE.IS_DOC_TYPE_NULL#Document Type              %>: <%?NN?ARCHIVE.INCLUDE_INCOMPLETE%><%?NN?ARCHIVE.DOC_TYPE_LIST%>\r\nPartners To Archive        : <%ARCHIVE.PARTNER_FOR_ARCHIVE%>\r\n\r\n\r\nRegards,\r\nGridtalk Server',NULL,NULL,1,0);

INSERT INTO message_template (UID, Name, ContentType, MessageType, FromAddr, ToAddr, CcAddr, Subject, Message, Location, Append, Version, CanDelete) 
VALUES (-27,'ARCHIVE_COMPLETE_EMAIL','Text','EMail','<#USER=admin#>','<#USER=admin#>','','[GRIDTALK] Archive Completed','Dear User,\r\n\r\nArchive by [<%ARCHIVE.ARCHIVE_BY%>] has completed at: <%ARCHIVE.START_TIME_UTC%> [<%ARCHIVE.START_TIME%>]\r\n\r\nArchive [<%ARCHIVE.ARCHIVE_BY%>] Criteria\r\n-----------------------------------------------------\r\nIs Enable Restore Archive  : <%ARCHIVE.IS_ENABLE_RESTORE_ARCHIVE%>\r\nIs Enable Search Archive   : <%ARCHIVE.IS_ENABLE_SEARCH_ARCHIVE%>\r\nFrom Date/Time             : <%ARCHIVE.FROM_DATE_TIME_UTC%> [<%ARCHIVE.FROM_DATE_TIME%>]\r\nTo Date Time               : <%ARCHIVE.TO_DATE_TIME_UTC%> [<%ARCHIVE.TO_DATE_TIME%>]\r\n<%?NProcess Definition         N?ARCHIVE.IS_DOC_TYPE_NULL#Folder                     %>: <%?NN?ARCHIVE.PROCESS_DEF_LIST%><%?NN?ARCHIVE.FOLDER_LIST%>\r\n<%?NInclude Incomplete         N?ARCHIVE.IS_DOC_TYPE_NULL#Document Type              %>: <%?NN?ARCHIVE.INCLUDE_INCOMPLETE%><%?NN?ARCHIVE.DOC_TYPE_LIST%>\r\nPartners To Archive        : <%ARCHIVE.PARTNER_FOR_ARCHIVE%>\r\n\r\nNumber of [<%ARCHIVE.ARCHIVE_BY%>] archived : <%ARCHIVE.NUM_ARCHIVED%>\r\n\r\nRegards,\r\nGridtalk Server',NULL,NULL,1,0);

INSERT INTO message_template (UID, Name, ContentType, MessageType, FromAddr, ToAddr, CcAddr, Subject, Message, Location, Append, Version, CanDelete) 
VALUES (-28,'ARCHIVE_FAIL_EMAIL','Text','EMail','<#USER=admin#>','<#USER=admin#>','','[GRIDTALK] Archive Failed','Dear User,\r\n\r\nArchive by [<%ARCHIVE.ARCHIVE_BY%>] has failed at: <%ARCHIVE.START_TIME_UTC%> [<%ARCHIVE.START_TIME%>]\r\n\r\nArchive [<%ARCHIVE.ARCHIVE_BY%>] Criteria\r\n-----------------------------------------------------\r\nIs Enable Restore Archive  : <%ARCHIVE.IS_ENABLE_RESTORE_ARCHIVE%>\r\nIs Enable Search Archive   : <%ARCHIVE.IS_ENABLE_SEARCH_ARCHIVE%>\r\nFrom Date/Time             : <%ARCHIVE.FROM_DATE_TIME_UTC%> [<%ARCHIVE.FROM_DATE_TIME%>]\r\nTo Date Time               : <%ARCHIVE.TO_DATE_TIME_UTC%> [<%ARCHIVE.TO_DATE_TIME%>]\r\n<%?NProcess Definition         N?ARCHIVE.IS_DOC_TYPE_NULL#Folder                     %>: <%?NN?ARCHIVE.PROCESS_DEF_LIST%><%?NN?ARCHIVE.FOLDER_LIST%>\r\n<%?NInclude Incomplete         N?ARCHIVE.IS_DOC_TYPE_NULL#Document Type              %>: <%?NN?ARCHIVE.INCLUDE_INCOMPLETE%><%?NN?ARCHIVE.DOC_TYPE_LIST%>\r\nPartners To Archive        : <%ARCHIVE.PARTNER_FOR_ARCHIVE%>\r\n\r\nPlease verify the problem and restart the operation. (Note: Some documents/processes may have already been archived. It is safe to run the archival again.)\r\n\r\nException : <%Exception.MESSAGE%>\r\nTrace:\r\n------\r\n<%Exception.STACK_TRACE%>\r\n\r\n\r\nRegards,\r\nGridTalk Server','','',1,0);

#20Dec2006

DELETE FROM message_template WHERE NAME='GTAS RESTORE ALERT';
INSERT INTO message_template (UID, Name, ContentType, MessageType, FromAddr, ToAddr, CcAddr, Subject, Message, Location, Append, Version, CanDelete) 
VALUES (-24,'RESTORE_EMAIL','Text','EMail','<#USER=admin#>','<#USER=admin#>',NULL,'[GRIDTALK] Restore Complete','Dear User,\r\n\r\nRestore of the archive is completed with status: <%ARCHIVE.STATUS%>\r\nThe Archival Summary file is attached to this mail.\r\n\r\n\r\nRegards,\r\nGridTalk Server',NULL,0,1,0);

UPDATE action
SET Name='RESTORE_ALERT_EMAIL', Description='Restore Alert Email Action'
WHERE Name='RESTORE_ALERT_ACTION';

#05Dec2006

ALTER TABLE alert_trigger 
MODIFY DocType varchar(30) DEFAULT NULL;


USE appdb;

UPDATE fieldmetainfo
SET Length=30
WHERE EntityObjectName='com.gridnode.gtas.server.alert.model.AlertTrigger'
AND FieldName='DOC_TYPE';



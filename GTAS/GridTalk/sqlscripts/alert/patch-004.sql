# 23 Apr 2003 I1 v2.1  [Neo Sok Lay] Add AlertTrigger entity
# 09 May 2003 I1 v2.1  [Neo Sok Lay] Refine alert types and alert templates. Provide a more sophisticated default list.


USE userdb;

# table definition for AlertTrigger
DROP TABLE IF EXISTS alert_trigger;
CREATE TABLE IF NOT EXISTS alert_trigger (
  UID bigint(20) NOT NULL DEFAULT '0' ,
  Level smallint(2) NOT NULL DEFAULT '' ,
  AlertType varchar(35) NOT NULL DEFAULT '' ,
  AlertUID bigint(20) NOT NULL DEFAULT '0' ,
  DocType varchar(12) DEFAULT NULL ,
  PartnerType varchar(3) DEFAULT NULL ,
  PartnerGroup varchar(3) DEFAULT NULL ,
  PartnerId varchar(20) DEFAULT NULL ,
  Recipients text DEFAULT NULL ,
  Enabled tinyint(1) NOT NULL DEFAULT '1' ,
  AttachDoc tinyint(1) NOT NULL DEFAULT '0' ,
  CanDelete tinyint(1) NOT NULL DEFAULT '1' ,
  Version double NOT NULL DEFAULT '1' ,
  PRIMARY KEY (UID),
  UNIQUE KEY ID (Level,AlertType,DocType,PartnerType,PartnerGroup,PartnerId)
);

# default alert types
DELETE FROM alert_type;

INSERT INTO alert_type (UID, Name, Description, Version, CanDelete) VALUES (1,'DOCUMENT_RECEIVED','Document Received',1,0);
INSERT INTO alert_type (UID, Name, Description, Version, CanDelete) VALUES (2,'DOCUMENT_RESPONSE_RECEIVED','Document Response Received',1,0);
INSERT INTO alert_type (UID, Name, Description, Version, CanDelete) VALUES (3,'DOCUMENT_REMINDER','Document Reminder',1,0);
INSERT INTO alert_type (UID, Name, Description, Version, CanDelete) VALUES (4,'DOCUMENT_RECEIVED_BY_PARTNER','Document Received by Partner',1,0);
INSERT INTO alert_type (UID, Name, Description, Version, CanDelete) VALUES (5,'PROCESS_INSTANCE_FAILURE','Process Instance Failure',1,0);
INSERT INTO alert_type (UID, Name, Description, Version, CanDelete) VALUES (6,'LICENSE_EXPIRED','GridTalk License Expired',1,0);
INSERT INTO alert_type (UID, Name, Description, Version, CanDelete) VALUES (7,'GRIDMASTER_CONNECTION_ACTIVITY','Connection to GridMaster',1,0);
INSERT INTO alert_type (UID, Name, Description, Version, CanDelete) VALUES (8,'GRIDMASTER_CONNECTION_LOST','Connection Lost to GridMaster',1,0);
INSERT INTO alert_type (UID, Name, Description, Version, CanDelete) VALUES (9,'GRIDMASTER_RECONNECTION','Reconnection to GridMaster',1,0);
INSERT INTO alert_type (UID, Name, Description, Version, CanDelete) VALUES (10,'PARTNER_UNCONTACTABLE','Connection Lost to Partner',1,0);
INSERT INTO alert_type (UID, Name, Description, Version, CanDelete) VALUES (11,'PARTNER_ACTIVATION','Partner Activation',1,0);
INSERT INTO alert_type (UID, Name, Description, Version, CanDelete) VALUES (12,'LICENSE_EXPIRE_SOON','GridTalk License Expiring Soon',1,0);
INSERT INTO alert_type (UID, Name, Description, Version, CanDelete) VALUES (13,'DOCUMENT_EXPORTED','Document Exported',1,0);
INSERT INTO alert_type (UID, Name, Description, Version, CanDelete) VALUES (14,'PARTNER_FUNCTION_FAILURE','Partner Function Failure',1,0);
INSERT INTO alert_type (UID, Name, Description, Version, CanDelete) VALUES (15,'DOCUMENT_MAPPING_FAILURE','Document Mapping Failure',1,0);
INSERT INTO alert_type (UID, Name, Description, Version, CanDelete) VALUES (16,'USER_PROCEDURE_FAILURE','User Procedure Failure',1,0);
INSERT INTO alert_type (UID, Name, Description, Version, CanDelete) VALUES (17,'USER_DEFINED','User Defined',1,0);
INSERT INTO alert_type (UID, Name, Description, Version, CanDelete) VALUES (18,'APPLICATION_STATE','Application State',1,0);

# default message templates
DELETE from message_template WHERE UID < 0;

INSERT INTO message_template (UID, Name, ContentType, MessageType, FromAddr, ToAddr, CcAddr, Subject, Message, Location, Append, Version, CanDelete) VALUES (-1,'MAPPING_FAILURE_EMAIL','Text','EMail','<#USER=admin#>','<#USER=admin#>','','Mapping Rule <%Mapping.NAME%> has failed for document <%GridDocument.U_DOC_FILENAME%>','An error has occurred while applying mapping rule on document <%GridDocument.U_DOC_FILENAME%> on <%System.CURRENT_DATETIME%>.\r\n\r\nDocument Details:\r\n-----------------\r\nDocument Type    : <%GridDocument.U_DOC_DOC_TYPE%>\r\nFile Type        : <%GridDocument.U_DOC_FILE_TYPE%>\r\nGridDoc ID       : <%GridDocument.G_DOC_ID%>\r\nFolder           : <%GridDocument.FOLDER%>\r\nPartner Function : <%GridDocument.PARTNER_FUNCTION%>\r\n\r\nMapping Rule Details:\r\n---------------------\r\nMapping Rule     : <%Mapping.NAME%>\r\nMapping Type     : <%Mapping.TYPE%> - [0] Convert [1] Transform [2] Split\r\nMapping Filename : <%Mapping.MAPPING_FILE_NAME%>\r\n\r\nException : <%Exception.MESSAGE%>\r\nTrace:\r\n------\r\n<%Exception.STACK_TRACE%>\r\n','',0,1,0);
INSERT INTO message_template (UID, Name, ContentType, MessageType, FromAddr, ToAddr, CcAddr, Subject, Message, Location, Append, Version, CanDelete) VALUES (-2,'MAPPING_FAILURE_LOG','','Log',NULL,NULL,NULL,NULL,'[<%System.CURRENT_DATETIME#yyyy-MM-dd HH:mm:ss.SSS%>] Mapping Rule <%Mapping.NAME%> has failed for document <%GridDocument.U_DOC_FILENAME%>, Message: <%Exception.MESSAGE%>.','process-errors.log',1,1,0);
INSERT INTO message_template (UID, Name, ContentType, MessageType, FromAddr, ToAddr, CcAddr, Subject, Message, Location, Append, Version, CanDelete) VALUES (-3,'DOCUMENT_EXPORTED_EMAIL','Text','EMail','<#USER=admin#>','<#USER=admin#>',NULL,'Document <%GridDocument.U_DOC_FILENAME%> has been successfully exported via Port <%Port.NAME%>','Document <%GridDocument.U_DOC_FILENAME%> has been successfully exported via Port <%Port.NAME%> on <%System.CURRENT_DATETIME%>.\r\n\r\nDocument Details:\r\n-----------------\r\nDocument Type    : <%GridDocument.U_DOC_DOC_TYPE%>\r\nFile Type        : <%GridDocument.U_DOC_FILE_TYPE%>\r\nGridDoc ID       : <%GridDocument.G_DOC_ID%>\r\nFolder           : <%GridDocument.FOLDER%>\r\nPartner Function : <%GridDocument.PARTNER_FUNCTION%>\r\n\r\nPort Details:\r\n-------------\r\nPort        : <%Port.DESCRIPTION%>\r\nUse RFC     : <%Port.IS_RFC%>\r\nDest. Dir   : <%Port.HOST_DIR%>\r\n\r\nRFC Details:\r\n------------\r\nRFC         : <%Rfc.NAME%>\r\nDescription : <%Rfc.DESCRIPTION%>\r\nHost        : <%Rfc.HOST%>\r\nPort Number : <%Rfc.PORT_NUMBER%>\r\n',NULL,0,1,0);
INSERT INTO message_template (UID, Name, ContentType, MessageType, FromAddr, ToAddr, CcAddr, Subject, Message, Location, Append, Version, CanDelete) VALUES (-4,'USER_PROCEDURE_FAILURE_LOG',NULL,'Log',NULL,NULL,NULL,NULL,'[<%System.CURRENT_DATETIME#yyyy-MM-dd HH:mm:ss.SSS%>] User procedure <%UserProcedure.NAME%> has failed for document <%GridDocument.U_DOC_FILENAME%>, Reason: <%ProcessingError.REASON%>, Message: <%ProcessingError.MESSAGE%>.','process-errors.log',1,1,0);
INSERT INTO message_template (UID, Name, ContentType, MessageType, FromAddr, ToAddr, CcAddr, Subject, Message, Location, Append, Version, CanDelete) VALUES (-5,'USER_PROCEDURE_FAILURE_EMAIL','Text','EMail','<#USER=admin#>','<#USER=admin#>','','User Procedure <%UserProcedure.NAME%> has failed for document <%GridDocument.U_DOC_FILENAME%>','User procedure <%UserProcedure.NAME%> has failed for document <%GridDocument.U_DOC_FILENAME%> on <%System.CURRENT_DATETIME%>.\r\n\r\nDocument Details:\r\n-----------------\r\nDocument Type    : <%GridDocument.U_DOC_DOC_TYPE%>\r\nFile Type        : <%GridDocument.U_DOC_FILE_TYPE%>\r\nGridDoc ID       : <%GridDocument.G_DOC_ID%>\r\nFolder           : <%GridDocument.FOLDER%>\r\nPartner Function : <%GridDocument.PARTNER_FUNCTION%>\r\n\r\nUser Procedure Details:\r\n-----------------------\r\nProcedure Name : <%UserProcedure.NAME%>\r\nDescription    : <%UserProcedure.DESCRIPTION%>\r\nProcedure Type : <%UserProcedure.PROC_TYPE%> - [1] Shell Executable [2] Java Procedure\r\n\r\nError Details:\r\n--------------\r\nReason    : <%ProcessingError.REASON%>\r\n            [1] User Procedure Aborted\r\n            [2] User Procedure Execution Failure\r\n\r\nException : <%ProcessingError.MESSAGE%>\r\nTrace:\r\n------\r\n<%ProcessingError.STACK_TRACE%>\r\n','',0,1,0);
INSERT INTO message_template (UID, Name, ContentType, MessageType, FromAddr, ToAddr, CcAddr, Subject, Message, Location, Append, Version, CanDelete) VALUES (-6,'PROCESS_INSTANCE_FAILURE_EMAIL','Text','EMail','<#USER=admin#>','<#USER=admin#>','','Process Instance (<%Process.PROCESS_DEF_NAME%>/<%Process.PROCESS_ORIGINATOR_ID%>/<%Process.PROCESS_INSTANCE_ID%>) has failed for document <%GridDocument.U_DOC_FILENAME%>','Process Instance has failed for document <%GridDocument.U_DOC_FILENAME%> on <%System.CURRENT_DATETIME%>.\r\n\r\nProcess Details:\r\n----------------\r\nDefinition Name : <%Process.PROCESS_DEF_NAME%>\r\nOriginator ID   : <%Process.PROCESS_ORIGINATOR_ID%>\r\nInstance ID     : <%Process.PROCESS_INSTANCE_ID%>\r\n\r\nDocument Details:\r\n-----------------\r\nDocument Type : <%GridDocument.U_DOC_DOC_TYPE%>\r\nGridDoc ID    : <%GridDocument.G_DOC_ID%>\r\nFolder        : <%GridDocument.FOLDER%>\r\n\r\nError Details:\r\n--------------\r\nType     : <%ProcessingError.TYPE%>\r\n           [1] Maximum retries has reached but no acknowledge received\r\n           [2] Time to perform has expired but no response received\r\n           [3] Document validation has failed (See Reason and Exception)\r\n           [4] Failure Notification PIP has failed due to [1]\r\n           [5] RosettaNet Exception Message is received in Inbound folder\r\n\r\nReason   : <%ProcessingError.REASON%>\r\n\r\nException: <%ProcessingError.MESSAGE%>\r\nTrace:\r\n------\r\n<%ProcessingError.STACK_TRACE%>\r\n','',0,1,0);
INSERT INTO message_template (UID, Name, ContentType, MessageType, FromAddr, ToAddr, CcAddr, Subject, Message, Location, Append, Version, CanDelete) VALUES (-7,'PROCESS_INSTANCE_FAILURE_LOG',NULL,'Log',NULL,NULL,NULL,NULL,'[<%System.CURRENT_DATETIME#yyyy-MM-dd HH:mm:ss.SSS%>] Process Instance (<%Process.PROCESS_DEF_NAME%>/<%Process.PROCESS_ORIGINATOR_ID%>/<%Process.PROCESS_INSTANCE_ID%>) has failed for document <%GridDocument.U_DOC_FILENAME%>.','process-errors.log',1,1,0);
INSERT INTO message_template (UID, Name, ContentType, MessageType, FromAddr, ToAddr, CcAddr, Subject, Message, Location, Append, Version, CanDelete) VALUES (-8,'PARTNER_FUNCTION_FAILURE_EMAIL','Text','EMail','<#USER=admin#>','<#USER=admin#>',NULL,'Partner Function <%GridDocument.PARTNER_FUNCTION%> has failed for document <%GridDocument.U_DOC_FILENAME%>','Partner Function <%GridDocument.PARTNER_FUNCTION%> has failed for document <%GridDocument.U_DOC_FILENAME%> on <%System.CURRENT_DATETIME%>.\r\n\r\nDocument Details:\r\n-----------------\r\nDocument Type    : <%GridDocument.U_DOC_DOC_TYPE%>\r\nFile Type        : <%GridDocument.U_DOC_FILE_TYPE%>\r\nGridDoc ID       : <%GridDocument.G_DOC_ID%>\r\nFolder           : <%GridDocument.FOLDER%>\r\n\r\nError Details:\r\n--------------\r\nType      : <%ProcessingError.TYPE%>\r\n            [1] Import document failure\r\n            [2] Export document failure\r\n            [3] Send document failure\r\n            [4] Receive document failure\r\n\r\nReason    : <%ProcessingError.REASON%>\r\n            [1] Fail to connect to Port\r\n            [2] Unknown Port\r\n            [3] Filesize exceeds limit\r\n            [4] Unknown Partner\r\n            [5] Unknown Destination\r\n            [6] Partner is not enabled for transaction\r\n            [7] No valid GridTalk license for transaction\r\n            [8] File processing errors\r\n            [9] General errors\r\n\r\nException : <%ProcessingError.MESSAGE%>\r\n\r\nTrace:\r\n------\r\n<%ProcessingError.STACK_TRACE%>\r\n',NULL,0,1,0);
INSERT INTO message_template (UID, Name, ContentType, MessageType, FromAddr, ToAddr, CcAddr, Subject, Message, Location, Append, Version, CanDelete) VALUES (-9,'PARTNER_FUNCTION_FAILURE_LOG',NULL,'Log',NULL,NULL,NULL,NULL,'[<%System.CURRENT_DATETIME#yyyy-MM-dd HH:mm:ss.SSS%>] Partner Function <%GridDocument.PARTNER_FUNCTION%> has failed for document <%GridDocument.U_DOC_FILENAME%>, Error Type: <%ProcessingError.TYPE%>, Reason: <%ProcessingError.REASON%>, Message: <%ProcessingError.MESSAGE%>.','process-errors.log',1,1,0);
INSERT INTO message_template (UID, Name, ContentType, MessageType, FromAddr, ToAddr, CcAddr, Subject, Message, Location, Append, Version, CanDelete) VALUES (-10,'DOCUMENT_RECEIVED_BY_PARTNER_EMAIL','Text','EMail','<#USER=admin#>','<#USER=admin#>',NULL,'Document <%GridDocument.U_DOC_FILENAME%> has been received by Partner <%GridDocument.R_PARTNER_ID%>','Document <%GridDocument.U_DOC_FILENAME%> has been received by Partner <%GridDocument.R_PARTNER_ID%> on <%GridDocument.DT_TRANSACTION_COMPLETE%>.\r\n\r\nDocument Details:\r\n-----------------\r\nDocument Type    : <%GridDocument.U_DOC_DOC_TYPE%>\r\nFile Type        : <%GridDocument.U_DOC_FILE_TYPE%>\r\nGridDoc ID       : <%GridDocument.G_DOC_ID%>\r\nPartner Name     : <%GridDocument.R_PARTNER_NAME%>\r\nChannel          : <%GridDocument.R_CHANNEL_NAME%>\r\nRoute            : <%GridDocument.ROUTE%>\r\nRecpt GridDoc ID : <%GridDocument.R_G_DOC_ID%>\r\n',NULL,0,1,0);
INSERT INTO message_template (UID, Name, ContentType, MessageType, FromAddr, ToAddr, CcAddr, Subject, Message, Location, Append, Version, CanDelete) VALUES (-11,'DOCUMENT_RECEIVED_EMAIL','Text','EMail','<#USER=admin#>','<#USER=admin#>',NULL,'Received document <%GridDocument.U_DOC_FILENAME%> from Partner <%GridDocument.S_PARTNER_ID%>','You have received document <%GridDocument.U_DOC_FILENAME%> from Partner <%GridDocument.S_PARTNER_ID%> on <%System.CURRENT_DATETIME%>.\r\n\r\nDocument Details:\r\n-----------------\r\nDocument Type     : <%GridDocument.U_DOC_DOC_TYPE%>\r\nFile Type         : <%GridDocument.U_DOC_FILE_TYPE%>\r\nGridDoc ID        : <%GridDocument.G_DOC_ID%>\r\nPartner Name      : <%GridDocument.S_PARTNER_NAME%>\r\nChannel           : <%GridDocument.R_CHANNEL_NAME%>\r\nRoute             : <%GridDocument.ROUTE%>\r\nSender GridDoc ID : <%GridDocument.S_G_DOC_ID%>\r\n',NULL,0,1,0);
INSERT INTO message_template (UID, Name, ContentType, MessageType, FromAddr, ToAddr, CcAddr, Subject, Message, Location, Append, Version, CanDelete) VALUES (-12,'FAILURE_NOF_PIP_RECEIVED_EMAIL','Text','EMail','<#USER=admin#>','<#USER=admin#>',NULL,'Failure Notification PIP received from Partner <%GridDocument.S_PARTNER_ID%>','You have received a Failure Notification PIP in Inbound folder from Partner <%GridDocument.S_PARTNER_ID%> on <%GridDocument.DT_RECEIVE_END%>.\r\nPlease check the document immediately and take corresponding action.\r\n\r\nDocument Details:\r\n-----------------\r\nDocument Type : <%GridDocument.U_DOC_DOC_TYPE%>\r\nGridDoc ID    : <%GridDocument.G_DOC_ID%>\r\nPartner Name  : <%GridDocument.S_PARTNER_NAME%>\r\n\r\nProcess Details:\r\n----------------\r\nDefinition Name : <%Process.PROCESS_DEF_NAME%>\r\nOriginator ID   : <%Process.PROCESS_ORIGINATOR_ID%>\r\nInstance ID     : <%Process.PROCESS_INSTANCE_ID%>\r\n',NULL,0,1,0);
INSERT INTO message_template (UID, Name, ContentType, MessageType, FromAddr, ToAddr, CcAddr, Subject, Message, Location, Append, Version, CanDelete) VALUES (-13,'LICENSE_EXPIRED_EMAIL','Text','EMail','<#USER=admin#>','<#USER=admin#>',NULL,'GridTalk License has expired for GridNode <%GridNode.ID%>','GridTalk License has expired for GridNode <%GridNode.ID%> - <%GridNode.NAME%> on <%License.END_DATE#yyyy-MM-dd%>.\r\nPlease renew the license immediately to continue the usage of the product.\r\n\r\nLicense Details:\r\n----------------\r\nProduct Key        : <%License.PRODUCT_KEY%>\r\nProduct Name       : <%License.PRODUCT_NAME%>\r\nProduct Version    : <%License.PRODUCT_VERSION%>\r\nLicense Start Date : <%License.START_DATE#yyyy-MM-dd%>\r\nLicense End Date   : <%License.END_DATE#yyyy-MM-dd%>\r\n',NULL,0,1,0);
INSERT INTO message_template (UID, Name, ContentType, MessageType, FromAddr, ToAddr, CcAddr, Subject, Message, Location, Append, Version, CanDelete) VALUES (-14,'LICENSE_EXPIRED_LOG',NULL,'Log',NULL,NULL,NULL,NULL,'[<%System.CURRENT_DATETIME%>] GridTalk License has expired for GridNode <%GridNode.ID%> - <%GridNode.NAME%> on <%License.END_DATE#yyyy-MM-dd%>.','license.log',1,1,0);
INSERT INTO message_template (UID, Name, ContentType, MessageType, FromAddr, ToAddr, CcAddr, Subject, Message, Location, Append, Version, CanDelete) VALUES (-15,'LICENSE_EXPIRING_EMAIL','Text','EMail','<#USER=admin#>','<#USER=admin#>',NULL,'GridTalk License is expiring soon for GridNode <%GridNode.ID%>','GridTalk License is expiring soon for GridNode <%GridNode.ID%> - <%GridNode.NAME%> on <%License.END_DATE#yyyy-MM-dd%>.\r\nPlease renew the license in due time to continue the usage of the product.\r\n\r\nLicense Details:\r\n----------------\r\nProduct Key        : <%License.PRODUCT_KEY%>\r\nProduct Name       : <%License.PRODUCT_NAME%>\r\nProduct Version    : <%License.PRODUCT_VERSION%>\r\nLicense Start Date : <%License.START_DATE#yyyy-MM-dd%>\r\nLicense End Date   : <%License.END_DATE#yyyy-MM-dd%>\r\n',NULL,0,1,0);
INSERT INTO message_template (UID, Name, ContentType, MessageType, FromAddr, ToAddr, CcAddr, Subject, Message, Location, Append, Version, CanDelete) VALUES (-16,'PARTNER_ACTIVATION_EMAIL','Text','EMail','<#USER=admin#>','<#USER=admin#>',NULL,'Received Activation message (Type <%Activation.TYPE%>) from GridNode <%Activation.PARTNER_GN_ID%>','You have received an Activation message (Type <%Activation.TYPE%>) from GridNode <%Activation.PARTNER_GN_ID%> - <%Activation.PARTNER_GN_NAME%> on <%Activation.DT_RECEIVED%>.\r\nPlease review and take appropriate action on the message if necessary.\r\n\r\nTypes\r\n-----\r\n[1] Activation Request\r\n    Purpose: The mentioned GridNode has requested your GridNode for business connection.\r\n    Actions: You can approve or reject the request.\r\n\r\n[2] Deactivation Request\r\n    Purpose: The mentioned GridNode has deactivated the business connection with your GridNode.\r\n    Actions: No action required. The business connection is now deactivated and no new transactions can be performed between the two parties.\r\n\r\n[3] Activation Request Cancellation\r\n    Purpose: The mentioned GridNode has cancelled the previous Activation Request to your GridNode.\r\n    Actions: No action required. The previous incoming Activation Request from the GridNode is now void.\r\n\r\n[4] Activation Request Approval\r\n    Purpose: The mentioned GridNode has approved your Activation Request submitted earlier on.\r\n    Actions: The business connection is now activated and you can proceed to configure processes to perform transactions with the GridNode.\r\n\r\n[5] Activation Request Rejection\r\n    Purpose: The mentioned GridNode has rejected your Activation Request submitted earlier on.\r\n    Actions: No action required. The previous outgoing Activation Request to the GridNode is now void.\r\n',NULL,0,1,0);
INSERT INTO message_template (UID, Name, ContentType, MessageType, FromAddr, ToAddr, CcAddr, Subject, Message, Location, Append, Version, CanDelete) VALUES (-17,'GRIDMASTER_CONNECTION_LOG','','Log',NULL,NULL,NULL,NULL,'[<%Connection.DT_ACTIVITY_START#yyyy-MM-dd HH:mm:ss.SSS%>-<%Connection.DT_ACTIVITY_END#yyyy-MM-dd HH:mm:ss.SSS%>] Activity: <%Connection.ACTIVITY%>, GridMaster: <%Connection.NODE_ID%>-<%Connection.NODE_NAME%>, Status: <%Connection.STATUS%>.','connection.log',1,1,0);
INSERT INTO message_template (UID, Name, ContentType, MessageType, FromAddr, ToAddr, CcAddr, Subject, Message, Location, Append, Version, CanDelete) VALUES (-18,'GRIDMASTER_CONNECTION_LOST_EMAIL','Text','EMail','<#USER=admin#>','<#USER=admin#>',NULL,'Connection Lost from GridMaster <%Connection.NODE_ID%>','GridTalk has lost connection from GridMaster <%Connection.NODE_ID%>-<%Connection.NODE_NAME%> on <%Connection.DT_OCCUR%>.\r\nThe system is attempting to re-establish the connection and will notify you of the result.\r\n','',0,1,0);
INSERT INTO message_template (UID, Name, ContentType, MessageType, FromAddr, ToAddr, CcAddr, Subject, Message, Location, Append, Version, CanDelete) VALUES (-19,'GRIDMASTER_CONNECTION_LOST_LOG',NULL,'Log',NULL,NULL,NULL,NULL,'[<%Connection.DT_OCCUR#yyyy-MM-dd HH:mm:ss.SSS%>] GridTalk has lost connection from GridMaster <%Connection.NODE_ID%>-<%Connection.NODE_NAME%>.','connection.log',1,1,0);
INSERT INTO message_template (UID, Name, ContentType, MessageType, FromAddr, ToAddr, CcAddr, Subject, Message, Location, Append, Version, CanDelete) VALUES (-20,'GRIDMASTER_RECONNECTION_EMAIL','Text','EMail','<#USER=admin#>','<#USER=admin#>',NULL,'Reconnection to GridMaster <%Connection.NODE_ID%>-<%Connection.NODE_NAME%>','GridTalk has attempted reconnection to GridMaster <%Connection.NODE_ID%>-<%Connection.NODE_NAME%> from <%Connection.DT_ACTIVITY_START%> to <%Connection.DT_ACTIVITY_START%>.\r\nResult: <%Connection.STATUS%> \r\n        [0] Reconnected \r\n        [1] Failed. Please check your network connection.\r\n',NULL,0,1,0);
INSERT INTO message_template (UID, Name, ContentType, MessageType, FromAddr, ToAddr, CcAddr, Subject, Message, Location, Append, Version, CanDelete) VALUES (-21,'PARTNER_UNCONTACTABLE_EMAIL','Text','EMail','<#USER=admin#>','<#USER=admin#>',NULL,'Partner GridNode <%Connection.NODE_ID%> has lost connection','Partner GridNode <%Connection.NODE_ID%>-<%Connection.NODE_NAME%> has lost connection from GridMaster on <%Connection.DT_OCCUR%>.\r\nThe system will be unable to contact the partner until the partner has re-established the connection.\r\n',NULL,0,1,0);

# default actions
DELETE FROM action WHERE UID < 0;

INSERT INTO action (UID, Name, Description, MsgUid, Version, CanDelete) VALUES (-1,'MAPPING_FAILURE_EMAIL','Mapping Failure Email Action',-1,1,0);
INSERT INTO action (UID, Name, Description, MsgUid, Version, CanDelete) VALUES (-2,'MAPPING_FAILURE_LOG','Mapping Failure Log Action',-2,1,0);
INSERT INTO action (UID, Name, Description, MsgUid, Version, CanDelete) VALUES (-3,'DOCUMENT_EXPORTED_EMAIL','Document Exported Email Action',-3,1,0);
INSERT INTO action (UID, Name, Description, MsgUid, Version, CanDelete) VALUES (-4,'USER_PROCEDURE_FAILURE_LOG','User Procedure Failure Log Action',-4,1,0);
INSERT INTO action (UID, Name, Description, MsgUid, Version, CanDelete) VALUES (-5,'USER_PROCEDURE_FAILURE_EMAIL','User Procedure Failure Email Action',-5,1,0);
INSERT INTO action (UID, Name, Description, MsgUid, Version, CanDelete) VALUES (-6,'PROCESS_INSTANCE_FAILURE_EMAIL','Process Instance Failure Email Action',-6,1,0);
INSERT INTO action (UID, Name, Description, MsgUid, Version, CanDelete) VALUES (-7,'PROCESS_INSTANCE_FAILURE_LOG','Process Instance Failure Log Action',-7,1,0);
INSERT INTO action (UID, Name, Description, MsgUid, Version, CanDelete) VALUES (-8,'PARTNER_FUNCTION_FAILURE_EMAIL','Partner Function Failure Email Action',-8,1,0);
INSERT INTO action (UID, Name, Description, MsgUid, Version, CanDelete) VALUES (-9,'PARTNER_FUNCTION_FAILURE_LOG','Partner Function Failure Log Action',-9,1,0);
INSERT INTO action (UID, Name, Description, MsgUid, Version, CanDelete) VALUES (-10,'DOCUMENT_RECEIVED_BY_PARTNER_EMAIL','Document Received By Partner Email Action',-10,1,0);
INSERT INTO action (UID, Name, Description, MsgUid, Version, CanDelete) VALUES (-11,'DOCUMENT_RECEIVED_EMAIL','Document Received Email Action',-11,1,0);
INSERT INTO action (UID, Name, Description, MsgUid, Version, CanDelete) VALUES (-12,'FAILURE_NOF_PIP_RECEIVED_EMAIL','Failure Notification PIP Received Email Action',-12,1,0);
INSERT INTO action (UID, Name, Description, MsgUid, Version, CanDelete) VALUES (-13,'LICENSE_EXPIRED_EMAIL','License Expired Email Action',-13,1,0);
INSERT INTO action (UID, Name, Description, MsgUid, Version, CanDelete) VALUES (-14,'LICENSE_EXPIRED_LOG','License Expired Log Action',-14,1,0);
INSERT INTO action (UID, Name, Description, MsgUid, Version, CanDelete) VALUES (-15,'LICENSE_EXPIRING_EMAIL','License Expiring Email Action',-15,1,0);
INSERT INTO action (UID, Name, Description, MsgUid, Version, CanDelete) VALUES (-16,'PARTNER_ACTIVATION_EMAIL','Partner Activation Email Action',-16,1,0);
INSERT INTO action (UID, Name, Description, MsgUid, Version, CanDelete) VALUES (-17,'GRIDMASTER_CONNECTION_LOG','GridMaster Connection Log Action',-17,1,0);
INSERT INTO action (UID, Name, Description, MsgUid, Version, CanDelete) VALUES (-18,'GRIDMASTER_CONNECTION_LOST_EMAIL','GridMaster Connection Lost Email Action',-18,1,0);
INSERT INTO action (UID, Name, Description, MsgUid, Version, CanDelete) VALUES (-19,'GRIDMASTER_CONNECTION_LOST_LOG','GridMaster Connection Lost Log Action',-19,1,0);
INSERT INTO action (UID, Name, Description, MsgUid, Version, CanDelete) VALUES (-20,'GRIDMASTER_RECONNECTION_EMAIL','GridMaster Reconnection Email Action',-20,1,0);
INSERT INTO action (UID, Name, Description, MsgUid, Version, CanDelete) VALUES (-21,'PARTNER_UNCONTACTABLE_EMAIL','Partner Uncontactable Email Action',-21,1,0);

# default alerts
DELETE FROM alert WHERE UID < 0;

INSERT INTO alert (UID, Name, AlertType, Category, Trigger, Description, Version, CanDelete) VALUES (-1,'MAPPING_FAILURE',15,0,'','Default Mapping Failure Alert',1,0);
INSERT INTO alert (UID, Name, AlertType, Category, Trigger, Description, Version, CanDelete) VALUES (-2,'DOCUMENT_EXPORTED',13,0,NULL,'Default Document Exported Alert',1,0);
INSERT INTO alert (UID, Name, AlertType, Category, Trigger, Description, Version, CanDelete) VALUES (-3,'USER_PROCEDURE_FAILURE',16,0,'','Default User Procedure Failure Alert',1,0);
INSERT INTO alert (UID, Name, AlertType, Category, Trigger, Description, Version, CanDelete) VALUES (-4,'PROCESS_INSTANCE_FAILURE',5,0,'','Default Process Instance Failure Alert',1,0);
INSERT INTO alert (UID, Name, AlertType, Category, Trigger, Description, Version, CanDelete) VALUES (-5,'PARTNER_FUNCTION_FAILURE',14,0,NULL,'Default Partner Function Failure Alert',1,0);
INSERT INTO alert (UID, Name, AlertType, Category, Trigger, Description, Version, CanDelete) VALUES (-6,'DOCUMENT_RECEIVED_BY_PARTNER',4,0,NULL,'Default Document Received By Partner Alert',1,0);
INSERT INTO alert (UID, Name, AlertType, Category, Trigger, Description, Version, CanDelete) VALUES (-7,'DOCUMENT_RECEIVED',1,0,NULL,'Default Document Received Alert',1,0);
INSERT INTO alert (UID, Name, AlertType, Category, Trigger, Description, Version, CanDelete) VALUES (-8,'FAILURE_NOF_PIP_RECEIVED',1,0,NULL,'Default Failure Notification PIP Received Alert',1,0);
INSERT INTO alert (UID, Name, AlertType, Category, Trigger, Description, Version, CanDelete) VALUES (-9,'LICENSE_EXPIRED',6,0,NULL,'Default License Expired Alert',1,0);
INSERT INTO alert (UID, Name, AlertType, Category, Trigger, Description, Version, CanDelete) VALUES (-10,'LICENSE_EXPIRING_SOON',12,0,NULL,'Default License Expiring Soon Alert',1,0);
INSERT INTO alert (UID, Name, AlertType, Category, Trigger, Description, Version, CanDelete) VALUES (-11,'PARTNER_ACTIVATION',11,0,NULL,'Default Partner Activation Alert',1,0);
INSERT INTO alert (UID, Name, AlertType, Category, Trigger, Description, Version, CanDelete) VALUES (-12,'GRIDMASTER_CONNECTION',7,0,NULL,'Default GridMaster Connection Alert',1,0);
INSERT INTO alert (UID, Name, AlertType, Category, Trigger, Description, Version, CanDelete) VALUES (-13,'GRIDMASTER_CONNECTION_LOST',8,0,NULL,'Default GridMaster Connection Lost Alert',1,0);
INSERT INTO alert (UID, Name, AlertType, Category, Trigger, Description, Version, CanDelete) VALUES (-14,'GRIDMASTER_RECONNECTION',9,0,NULL,'Default GridMaster Reconnection Alert',1,0);
INSERT INTO alert (UID, Name, AlertType, Category, Trigger, Description, Version, CanDelete) VALUES (-15,'PARTNER_UNCONTACTABLE',10,0,NULL,'Default Partner Uncontactable Alert',1,0);

# default alert actions
DELETE FROM alert_action WHERE UID < 0;

INSERT INTO alert_action (UID, AlertUid, ActionUid) VALUES (-1,-1,-1);
INSERT INTO alert_action (UID, AlertUid, ActionUid) VALUES (-2,-1,-2);
INSERT INTO alert_action (UID, AlertUid, ActionUid) VALUES (-3,-2,-3);
INSERT INTO alert_action (UID, AlertUid, ActionUid) VALUES (-4,-3,-4);
INSERT INTO alert_action (UID, AlertUid, ActionUid) VALUES (-5,-3,-5);
INSERT INTO alert_action (UID, AlertUid, ActionUid) VALUES (-6,-4,6);
INSERT INTO alert_action (UID, AlertUid, ActionUid) VALUES (-7,-4,-7);
INSERT INTO alert_action (UID, AlertUid, ActionUid) VALUES (-8,-5,-8);
INSERT INTO alert_action (UID, AlertUid, ActionUid) VALUES (-9,-5,-9);
INSERT INTO alert_action (UID, AlertUid, ActionUid) VALUES (-10,-6,-10);
INSERT INTO alert_action (UID, AlertUid, ActionUid) VALUES (-11,-7,-11);
INSERT INTO alert_action (UID, AlertUid, ActionUid) VALUES (-12,-8,-12);
INSERT INTO alert_action (UID, AlertUid, ActionUid) VALUES (-13,-9,-13);
INSERT INTO alert_action (UID, AlertUid, ActionUid) VALUES (-14,-9,-14);
INSERT INTO alert_action (UID, AlertUid, ActionUid) VALUES (-15,-10,-15);
INSERT INTO alert_action (UID, AlertUid, ActionUid) VALUES (-16,-11,-16);
INSERT INTO alert_action (UID, AlertUid, ActionUid) VALUES (-17,-12,-17);
INSERT INTO alert_action (UID, AlertUid, ActionUid) VALUES (-18,-13,-18);
INSERT INTO alert_action (UID, AlertUid, ActionUid) VALUES (-19,-13,-19);
INSERT INTO alert_action (UID, AlertUid, ActionUid) VALUES (-20,-14,-20);
INSERT INTO alert_action (UID, AlertUid, ActionUid) VALUES (-21,-14,-17);
INSERT INTO alert_action (UID, AlertUid, ActionUid) VALUES (-22,-15,-21);

# default alert triggers
INSERT INTO alert_trigger (UID, Level, AlertType, AlertUID, DocType, PartnerType, PartnerGroup, PartnerId, Recipients, Enabled, AttachDoc, CanDelete, Version) VALUES (-1,0,'DOCUMENT_MAPPING_FAILURE',-1,NULL,NULL,NULL,NULL,NULL,1,0,0,1);
INSERT INTO alert_trigger (UID, Level, AlertType, AlertUID, DocType, PartnerType, PartnerGroup, PartnerId, Recipients, Enabled, AttachDoc, CanDelete, Version) VALUES (-2,0,'DOCUMENT_EXPORTED',-2,NULL,NULL,NULL,NULL,NULL,1,0,0,1);
INSERT INTO alert_trigger (UID, Level, AlertType, AlertUID, DocType, PartnerType, PartnerGroup, PartnerId, Recipients, Enabled, AttachDoc, CanDelete, Version) VALUES (-3,0,'USER_PROCEDURE_FAILURE',-3,NULL,NULL,NULL,NULL,NULL,1,0,0,1);
INSERT INTO alert_trigger (UID, Level, AlertType, AlertUID, DocType, PartnerType, PartnerGroup, PartnerId, Recipients, Enabled, AttachDoc, CanDelete, Version) VALUES (-4,0,'PROCESS_INSTANCE_FAILURE',-4,NULL,NULL,NULL,NULL,NULL,1,0,0,1);
INSERT INTO alert_trigger (UID, Level, AlertType, AlertUID, DocType, PartnerType, PartnerGroup, PartnerId, Recipients, Enabled, AttachDoc, CanDelete, Version) VALUES (-5,0,'PARTNER_FUNCTION_FAILURE',-5,NULL,NULL,NULL,NULL,NULL,1,0,0,1);
INSERT INTO alert_trigger (UID, Level, AlertType, AlertUID, DocType, PartnerType, PartnerGroup, PartnerId, Recipients, Enabled, AttachDoc, CanDelete, Version) VALUES (-6,0,'DOCUMENT_RECEIVED_BY_PARTNER',-6,NULL,NULL,NULL,NULL,NULL,1,0,0,1);
INSERT INTO alert_trigger (UID, Level, AlertType, AlertUID, DocType, PartnerType, PartnerGroup, PartnerId, Recipients, Enabled, AttachDoc, CanDelete, Version) VALUES (-7,0,'DOCUMENT_RECEIVED',-7,NULL,NULL,NULL,NULL,NULL,1,0,0,1);
INSERT INTO alert_trigger (UID, Level, AlertType, AlertUID, DocType, PartnerType, PartnerGroup, PartnerId, Recipients, Enabled, AttachDoc, CanDelete, Version) VALUES (-8,1,'DOCUMENT_RECEIVED',-8,'RN_FAILNOTF1',NULL,NULL,NULL,NULL,1,0,0,1);
INSERT INTO alert_trigger (UID, Level, AlertType, AlertUID, DocType, PartnerType, PartnerGroup, PartnerId, Recipients, Enabled, AttachDoc, CanDelete, Version) VALUES (-9,1,'DOCUMENT_RECEIVED',-8,'RN_FAILNOTF2',NULL,NULL,NULL,NULL,1,0,0,1);
INSERT INTO alert_trigger (UID, Level, AlertType, AlertUID, DocType, PartnerType, PartnerGroup, PartnerId, Recipients, Enabled, AttachDoc, CanDelete, Version) VALUES (-10,0,'LICENSE_EXPIRED',-9,NULL,NULL,NULL,NULL,NULL,1,0,0,1);
INSERT INTO alert_trigger (UID, Level, AlertType, AlertUID, DocType, PartnerType, PartnerGroup, PartnerId, Recipients, Enabled, AttachDoc, CanDelete, Version) VALUES (-11,0,'LICENSE_EXPIRE_SOON',-10,NULL,NULL,NULL,NULL,NULL,1,0,0,1);
INSERT INTO alert_trigger (UID, Level, AlertType, AlertUID, DocType, PartnerType, PartnerGroup, PartnerId, Recipients, Enabled, AttachDoc, CanDelete, Version) VALUES (-12,0,'PARTNER_ACTIVATION',-11,NULL,NULL,NULL,NULL,NULL,1,0,0,1);
INSERT INTO alert_trigger (UID, Level, AlertType, AlertUID, DocType, PartnerType, PartnerGroup, PartnerId, Recipients, Enabled, AttachDoc, CanDelete, Version) VALUES (-13,0,'GRIDMASTER_CONNECTION_ACTIVITY',-12,NULL,NULL,NULL,NULL,NULL,1,0,0,1);
INSERT INTO alert_trigger (UID, Level, AlertType, AlertUID, DocType, PartnerType, PartnerGroup, PartnerId, Recipients, Enabled, AttachDoc, CanDelete, Version) VALUES (-14,0,'GRIDMASTER_CONNECTION_LOST',-13,NULL,NULL,NULL,NULL,NULL,1,0,0,1);
INSERT INTO alert_trigger (UID, Level, AlertType, AlertUID, DocType, PartnerType, PartnerGroup, PartnerId, Recipients, Enabled, AttachDoc, CanDelete, Version) VALUES (-15,0,'GRIDMASTER_RECONNECTION',-14,NULL,NULL,NULL,NULL,NULL,1,0,0,1);


USE appdb;

# meta info definition for AlertTrigger
DELETE FROM entitymetainfo
WHERE ObjectName
IN ('com.gridnode.gtas.server.alert.model.AlertTrigger');
INSERT INTO entitymetainfo VALUES ("com.gridnode.gtas.server.alert.model.AlertTrigger","AlertTrigger","alert_trigger");

DELETE FROM fieldmetainfo WHERE EntityObjectName LIKE '%.AlertTrigger';
INSERT INTO fieldmetainfo VALUES(
NULL,"_uId","UID","UID","java.lang.Long",
"com.gridnode.gtas.server.alert.model.AlertTrigger","alertTrigger.uid",
"0","0","0","0","0","","999",
"displayable=false\r\nmandatory=false\r\neditable=false",
"type=uid"
);
INSERT INTO fieldmetainfo VALUES(
NULL,"_version","VERSION","Version","java.lang.Double",
"com.gridnode.gtas.server.alert.model.AlertTrigger","alertTrigger.version",
"0","0","0","0","0","","999",
"displayable=false\r\nmandatory=false\r\neditable=false",
"type=range"
);
INSERT INTO fieldmetainfo VALUES(
NULL,"_canDelete","CAN_DELETE","CanDelete","java.lang.Boolean",
"com.gridnode.gtas.server.alert.model.AlertTrigger","alertTrigger.canDelete",
"0","0","0","0","0","","999",
"displayable=false\r\nmandatory=false\r\neditable=false",
"type=enum\r\ncandelete.enabled=true\r\ncandelete.disabled=false"
);
INSERT INTO fieldmetainfo VALUES(
NULL,"_level","LEVEL","Level","java.lang.Integer",
"com.gridnode.gtas.server.alert.model.AlertTrigger","alertTrigger.level",
"0","0","1","1","1","","999",
"displayable=true\r\nmandatory=true\r\neditable=false\r\neditable.create=true",
"type=enum\r\nalertTrigger.level.zero=0\r\nalertTrigger.level.one=1\r\nalertTrigger.level.two=2\r\nalertTrigger.level.three=3\r\nalertTrigger.level.four=4"
);
INSERT INTO fieldmetainfo VALUES(
NULL,"_alertUID","ALERT_UID","AlertUID","java.lang.Long",
"com.gridnode.gtas.server.alert.model.AlertTrigger","alertTrigger.alertUid",
"0","0","1","1","1","","999",
"displayable=true\r\nmandatory=true\r\neditable=true",
"type=foreign\r\nforeign.key=alert.uid\r\nforeign.display=alert.description"
);
INSERT INTO fieldmetainfo VALUES(
NULL,"_recipients","RECIPIENTS","Recipients","java.util.List",
"com.gridnode.gtas.server.alert.model.AlertTrigger","alertTrigger.recipients",
"0","0","0","1","1","","999",
"displayable=true\r\nmandatory=false\r\neditable=true",
"type=text\r\ncollection=true\r\ncollection.element=java.lang.String\r\n"
);
INSERT INTO fieldmetainfo VALUES(
NULL,"_docType","DOC_TYPE","DocType","java.lang.String",
"com.gridnode.gtas.server.alert.model.AlertTrigger","alertTrigger.docType",
"12","0","0","1","1","","999",
"displayable=true\r\nmandatory=true\r\neditable=true",
"type=foreign\r\nforeign.key=documentType.docType\r\nforeign.display=documentType.description"
);
INSERT INTO fieldmetainfo VALUES(
NULL,"_partnerType","PARTNER_TYPE","PartnerType","java.lang.String",
"com.gridnode.gtas.server.alert.model.AlertTrigger","alertTrigger.partnerType",
"3","0","0","1","1","","999",
"displayable=true\r\nmandatory=true\r\neditable=true",
"type=foreign\r\nforeign.key=partnerType.partnerType\r\nforeign.display=partnerType.description"
);
INSERT INTO fieldmetainfo VALUES(
NULL,"_partnerGroup","PARTNER_GROUP","PartnerGroup","java.lang.String",
"com.gridnode.gtas.server.alert.model.AlertTrigger","alertTrigger.partnerGroup",
"3","0","0","1","1","","999",
"displayable=true\r\nmandatory=true\r\neditable=true",
"type=foreign\r\nforeign.key=partnerGroup.name\r\nforeign.display=partnerGroup.description"
);
INSERT INTO fieldmetainfo VALUES(
NULL,"_partnerId","PARTNER_ID","PartnerId","java.lang.String",
"com.gridnode.gtas.server.alert.model.AlertTrigger","alertTrigger.partnerId",
"20","0","0","1","1","","999",
"displayable=true\r\nmandatory=false\r\neditable=true",
"type=foreign\r\nforeign.key=partner.partnerId\r\nforeign.display=partner.name"
);
INSERT INTO fieldmetainfo VALUES(
NULL,"_alertType","ALERT_TYPE","AlertType","java.lang.String",
"com.gridnode.gtas.server.alert.model.AlertTrigger","alertTrigger.alertType",
"35","0","1","1","1","","999",
"displayable=true\r\nmandatory=true\r\neditable=false\r\neditable.create=true",
"type=foreign\r\nforeign.key=alertType.name\r\nforeign.display=alertType.description\r\nforeign.cached=false"
);
INSERT INTO fieldmetainfo VALUES(
NULL,"_enabled","IS_ENABLED","Enabled","java.lang.Boolean",
"com.gridnode.gtas.server.alert.model.AlertTrigger","alertTrigger.isEnabled",
"0","0","0","0","0","","999",
"displayable=true\r\nmandatory=true\r\neditable=true",
"type=enum\r\ngeneric.yes=true\r\ngeneric.no=false"
);

INSERT INTO fieldmetainfo VALUES(
NULL,"_attachDoc","IS_ATTACH_DOC","AttachDoc","java.lang.Boolean",
"com.gridnode.gtas.server.alert.model.AlertTrigger","alertTrigger.isAttachDoc",
"0","0","0","0","0","","999",
"displayable=true\r\nmandatory=false\r\neditable=true",
"type=enum\r\ngeneric.yes=true\r\ngeneric.no=false"
);



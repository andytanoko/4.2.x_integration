# 03 Mar 2003 I8 v2.0.18 [Neo Sok Lay] Add FMI for Version and CanDelete columns to various Alert entities except AlertAction.
# 05 Mar 2003 I8 v2.0.18 [Neo Sok Lay] Wrong data type for TRIGGER field in Alert. Should be String instead of Long.
# 02 May 2003 I1 v2.1    [Neo Sok Lay] Location & Append field now displayable
# 19 May 2003 I1 v2.1    [Neo Sok Lay] Display Alert type description instead of name in Alert
# 01 Jul 2004 2.4        [Mahesh]      Added metainfo for EmailConfig
# 06 Oct 2005 I1 v4.0    [Neo Sok Lay] Change alert.Trigger field to alert.TriggerCond
# 28 Dec 2005 I1 v4.0    [Tam Wei Xiang] i)  Added FMI & EMI for JMSDestination , MessageProperty and JmsMessageRecord
#                                        ii) Added in two field 1)JMS_Destination 2) Message_Properties
#                                            into MessageTemplate.
#                                        iii)Modified field MessageType of MessageTemplate.
# 17 Feb 2006 I1 v4.0    [Neo Sok Lay] Change JmsMessageRecord.JMS_DESTINATION_NAME to JMS_DESTINATION_UID

USE appdb;


#
# Dumping data for table 'entitymetainfo'
#
DELETE FROM entitymetainfo WHERE EntityName IN
("Alert","AlertType","Action","MessageTemplate","AlertAction","AlertCategory","AlertList","EmailConfig","JmsDestination","MessageProperty","JmsMessageRecord");
INSERT INTO entitymetainfo VALUES("com.gridnode.pdip.app.alert.model.Alert","Alert","alert");
INSERT INTO entitymetainfo VALUES("com.gridnode.pdip.app.alert.model.AlertType","AlertType","alert_type");
INSERT INTO entitymetainfo VALUES("com.gridnode.pdip.app.alert.model.Action","Action","action");
INSERT INTO entitymetainfo VALUES("com.gridnode.pdip.app.alert.model.MessageTemplate","MessageTemplate","message_template");
INSERT INTO entitymetainfo VALUES("com.gridnode.pdip.app.alert.model.AlertAction","AlertAction","alert_action");
INSERT INTO entitymetainfo VALUES("com.gridnode.pdip.app.alert.model.AlertCategory","AlertCategory","alert_category");
INSERT INTO entitymetainfo VALUES("com.gridnode.pdip.app.alert.model.AlertList","AlertList","alert_list");
INSERT INTO entitymetainfo VALUES("com.gridnode.pdip.app.alert.model.EmailConfig","EmailConfig",NULL);
INSERT INTO entitymetainfo VALUES("com.gridnode.pdip.app.alert.model.JmsDestination","JmsDestination","jms_destination");
INSERT INTO entitymetainfo VALUES("com.gridnode.pdip.app.alert.model.MessageProperty","MessageProperty","");
INSERT INTO entitymetainfo VALUES("com.gridnode.pdip.app.alert.model.JmsMessageRecord","JmsMessageRecord","jms_msg_record");

#
# Dumping data for table 'fieldmetainfo'
#

# FieldMetaInfo for Alert
DELETE FROM fieldmetainfo WHERE EntityObjectName LIKE "%.Alert";
INSERT INTO fieldmetainfo VALUES(
NULL,"_trigger","TRIGGER","TriggerCond","java.lang.String",
"com.gridnode.pdip.app.alert.model.Alert","alert.trigger",
"2000","0","0","1","1","0","999",
"displayable=true\r\neditable=true\r\nmandatory=false\r\n",
"type=text\r\ntext.length.max=2000");

INSERT INTO fieldmetainfo VALUES(
NULL,"_categoryUid","CATEGORY_UID","Category","java.lang.Long",
"com.gridnode.pdip.app.alert.model.Alert","alert.category",
"20","0","0","1","1","0","999",
"displayable=true\r\neditable=true\r\nmandatory=false\r\n",
"type=foreign\r\nforeign.key=alertCategory.uid\r\nforeign.cached=false\r\nforeign.display=alertCategory.code");

INSERT INTO fieldmetainfo VALUES(
NULL,"_name","NAME","Name","java.lang.String",
"com.gridnode.pdip.app.alert.model.Alert","alert.name",
"80","0","1","1","1","0","999",
"displayable=true\r\nmandatory=true\r\neditable=false\r\neditable.create=true",
"type=text\r\ntext.length.max=80");

INSERT INTO fieldmetainfo VALUES(
NULL,"_alertTypeUid","ALERT_TYPE_UID","AlertType","java.lang.Long",
"com.gridnode.pdip.app.alert.model.Alert","alert.type",
"20","0","0","1","1","0","999",
"displayable=true\r\neditable=true\r\nmandatory=true",
"type=foreign\r\nforeign.key=alertType.uid\r\nforeign.display=alertType.description\r\nforeign.cached=false");

INSERT INTO fieldmetainfo VALUES(
NULL,"_uId","UID","UID","java.lang.Long",
"com.gridnode.pdip.app.alert.model.Alert","alert.uid",
"20","0","0","0","0","0","999",
"displayable=false\r\nmandatory=false\r\neditable=false",
"type=uid");

INSERT INTO fieldmetainfo VALUES(
NULL,"_description","DESCRIPTION","Description","java.lang.String",
"com.gridnode.pdip.app.alert.model.Alert","alert.description",
"255","0","0","1","1","0","999",
"displayable=true\r\neditable=true\r\nmandatory=true",
"type=text\r\ntext.length.max=255");

INSERT INTO fieldmetainfo VALUES(
NULL,"_bindActions","BIND_ACTIONS_UIDS","","java.util.Collection",
"com.gridnode.pdip.app.alert.model.Alert","alert.actions",
"0","0","0","1","1","0","999",
"displayable=true\r\neditable=true\r\nmandatory=false",
"collection=true\r\ncollection.element=java.lang.Long\r\ntype=foreign\r\nforeign.key=action.uid\r\nforeign.display=action.name\r\nforeign.cached=false");

INSERT INTO fieldmetainfo VALUES(
NULL,"_version","VERSION","Version","java.lang.Double",
"com.gridnode.pdip.app.alert.model.Alert","",
"0","0","0","0","0","","999",
"displayable=false\r\nmandatory=false\r\neditable=false",
"type=range"
);

INSERT INTO fieldmetainfo VALUES(
NULL,"_canDelete","CAN_DELETE","CanDelete","java.lang.Boolean",
"com.gridnode.pdip.app.alert.model.Alert","",
"0","0","0","0","0","","999",
"displayable=false\r\nmandatory=false\r\neditable=false",
"type=enum\r\ngeneric.yes=true\r\ngeneric.no=false"
);

# FieldMetaInfo for Action
DELETE FROM fieldmetainfo WHERE EntityObjectName LIKE "%.Action";
INSERT INTO fieldmetainfo VALUES(
NULL,"_description","DESCRIPTION","Description","java.lang.String",
"com.gridnode.pdip.app.alert.model.Action","action.description",
"255","0","0","1","1","0","999",
"displayable=true\r\neditable=true\r\nmandatory=true",
"type=text\r\ntext.length.max=255");

INSERT INTO fieldmetainfo VALUES(
NULL,"_msgUid","MSG_UID","MsgUid","java.lang.Long",
"com.gridnode.pdip.app.alert.model.Action","action.msgId",
"20","0","0","1","1","0","999",
"displayable=true\r\neditable=true\r\nmandatory=true",
"type=foreign\r\nforeign.key=messageTemplate.uid\r\nforeign.display=messageTemplate.name\r\nforeign.cached=false");

INSERT INTO fieldmetainfo VALUES(
NULL,"_uId","UID","UID","java.lang.Long",
"com.gridnode.pdip.app.alert.model.Action","action.uid",
"20","0","0","0","0","0","999",
"displayable=false\r\nmandatory=false\r\neditable=false",
"type=uid");

INSERT INTO fieldmetainfo VALUES(
NULL,"_name","NAME","Name","java.lang.String",
"com.gridnode.pdip.app.alert.model.Action","action.name",
"80","0","1","1","1","0","999",
"displayable=true\r\nmandatory=true\r\neditable=false\r\neditable.create=true",
"type=text\r\ntext.length.max=80");

INSERT INTO fieldmetainfo VALUES(
NULL,"_version","VERSION","Version","java.lang.Double",
"com.gridnode.pdip.app.alert.model.Action","",
"0","0","0","0","0","","999",
"displayable=false\r\nmandatory=false\r\neditable=false",
"type=range"
);

INSERT INTO fieldmetainfo VALUES(
NULL,"_canDelete","CAN_DELETE","CanDelete","java.lang.Boolean",
"com.gridnode.pdip.app.alert.model.Action","",
"0","0","0","0","0","","999",
"displayable=false\r\nmandatory=false\r\neditable=false",
"type=enum\r\ngeneric.yes=true\r\ngeneric.no=false"
);

# FieldMetaInfo for MessageTemplate
DELETE FROM fieldmetainfo WHERE EntityObjectName LIKE "%.MessageTemplate";
INSERT INTO fieldmetainfo VALUES(
NULL,"_toaddr","TOADDR","ToAddr","java.lang.String",
"com.gridnode.pdip.app.alert.model.MessageTemplate","messageTemplate.toAddr",
"255","0","0","1","1","0","999",
"displayable=true\r\neditable=true\r\nmandatory=false",
"type=text\r\ntext.length.max=255");

INSERT INTO fieldmetainfo VALUES(
NULL,"_fromaddr","FROMADDR","FromAddr","java.lang.String",
"com.gridnode.pdip.app.alert.model.MessageTemplate","messageTemplate.fromAddr",
"255","0","0","1","1","0","999",
"displayable=true\r\neditable=true\r\nmandatory=false",
"type=text\r\ntext.max.length=255");

INSERT INTO fieldmetainfo VALUES(
NULL,"_messageType","MESSAGETYPE","MessageType","java.lang.String",
"com.gridnode.pdip.app.alert.model.MessageTemplate","messageTemplate.messageType",
"30","0","0","1","1","0","999",
"displayable=true\r\neditable=true\r\nmandatory=true",
"type=enum\r\nmessageTemplate.messageType.email=EMail\r\nmessageTemplate.messageType.log=Log\r\nmessageTemplate.messageType.alertList=AlertList\r\nmessageTemplate.messageType.JMS=JMS");

INSERT INTO fieldmetainfo VALUES(
NULL,"_contentType","CONTENTTYPE","ContentType","java.lang.String",
"com.gridnode.pdip.app.alert.model.MessageTemplate","messageTemplate.contentType",
"30","0","0","1","1","0","999",
"displayable=true\r\neditable=true\r\nmandatory=true",
"type=enum\r\nmessageTemplate.contentType.text=Text\r\nmessageTemplate.contentType.html=HTML");

INSERT INTO fieldmetainfo VALUES(
NULL,"_uId","UID","UID","java.lang.Long",
"com.gridnode.pdip.app.alert.model.MessageTemplate","messageTemplate.uid",
"20","0","0","0","0","0","999",
"displayable=false\r\nmandatory=false\r\neditable=false",
"type=uid");

INSERT INTO fieldmetainfo VALUES(
NULL,"_name","NAME","Name","java.lang.String",
"com.gridnode.pdip.app.alert.model.MessageTemplate","messageTemplate.name",
"80","0","1","1","1","0","999",
"displayable=true\r\nmandatory=true\r\neditable=false\r\neditable.create=true",
"type=text\r\ntext.length.max=80");

INSERT INTO fieldmetainfo VALUES(
NULL,"_ccAddr","CCADDR","CcAddr","java.lang.String",
"com.gridnode.pdip.app.alert.model.MessageTemplate","messageTemplate.ccAddr",
"255","0","0","1","1","0","999",
"displayable=true\r\neditable=true\r\nmandatory=false",
"type=text\r\ntext.length.max=255");

INSERT INTO fieldmetainfo VALUES(
NULL,"_subject","SUBJECT","Subject","java.lang.String",
"com.gridnode.pdip.app.alert.model.MessageTemplate","messageTemplate.subject",
"255","0","0","1","1","0","999",
"displayable=true\r\neditable=true\r\nmandatory=true",
"type=text\r\ntext.length.max=255");

INSERT INTO fieldmetainfo VALUES(
NULL,"_message","MESSAGE","Message","java.lang.String",
"com.gridnode.pdip.app.alert.model.MessageTemplate","messageTemplate.message",
"2000","0","0","1","1","0","999",
"displayable=true\r\neditable=true\r\nmandatory=true",
"type=text\r\ntext.length.max=2000");

INSERT INTO fieldmetainfo VALUES(
NULL,"_location","LOCATION","Location","java.lang.String",
"com.gridnode.pdip.app.alert.model.MessageTemplate","messageTemplate.location",
"255","0","1","1","1","0","999",
"displayable=true\r\nmandatory=true\r\neditable=true",
"type=text");

INSERT INTO fieldmetainfo VALUES(
NULL,"_append","APPEND","Append","java.lang.Boolean",
"com.gridnode.pdip.app.alert.model.MessageTemplate","messageTemplate.append",
"1","0","0","1","1","0","999",
"displayable=true\r\neditable=true\r\nmandatory=false",
"type=enum\r\ngeneric.yes=true\r\ngeneric.no=false");

INSERT INTO fieldmetainfo VALUES(
NULL,"_version","VERSION","Version","java.lang.Double",
"com.gridnode.pdip.app.alert.model.MessageTemplate","",
"0","0","0","0","0","","999",
"displayable=false\r\nmandatory=false\r\neditable=false",
"type=range"
);

INSERT INTO fieldmetainfo VALUES(
NULL,"_canDelete","CAN_DELETE","CanDelete","java.lang.Boolean",
"com.gridnode.pdip.app.alert.model.MessageTemplate","",
"0","0","0","0","0","","999",
"displayable=false\r\nmandatory=false\r\neditable=false",
"type=enum\r\ngeneric.yes=true\r\ngeneric.no=false"
);

INSERT INTO fieldmetainfo VALUES(
NULL,"_jmsDestination","JMS_DESTINATION","JmsDestination","com.gridnode.pdip.app.alert.model.JmsDestination",
"com.gridnode.pdip.app.alert.model.MessageTemplate","messageTemplate.jmsDestination",
"20","0","1","1","1","0","999",
"displayable=true\r\neditable=true\r\nmandatory=true",
"type=foreign\r\nforeign.key=jmsDestination.uid\r\nforeign.display=jmsDestination.name\r\nforeign.cached=true");

INSERT INTO fieldmetainfo VALUES(
NULL,"_messageProperties","MESSAGE_PROPERTIES","MessageProperties","DataObjectList",
"com.gridnode.pdip.app.alert.model.MessageTemplate","messageTemplate.messageProperties",
"0","0","0","1","1","0","999",
"displayable=true\r\neditable=true\r\nmandatory=false",
"type=embedded\r\nembedded.type=messageProperty\r\ncollection=true");


# FieldMetaInfo for AlertAction
DELETE FROM fieldmetainfo WHERE EntityObjectName LIKE "%.AlertAction";
INSERT INTO fieldmetainfo VALUES(
NULL,"_actionUid","ACTION_UID","ActionUid","java.lang.Long",
"com.gridnode.pdip.app.alert.model.AlertAction","alertAction.actionUid",
"20","0","0","1","1","0","999",
"displayable=true\r\neditable=true\r\nmandatory=true",
"type=foreign\r\nforeign.key=action.uid\r\nforeign.display=action.name\r\nforeign.cached=false");

INSERT INTO fieldmetainfo VALUES(
NULL,"_uId","UID","UID","java.lang.Long",
"com.gridnode.pdip.app.alert.model.AlertAction","alertAction.uid",
"20","0","0","0","0","0","999",
"displayable=false\r\nmandatory=false\r\neditable=false",
"type=uid");

INSERT INTO fieldmetainfo VALUES(
NULL,"_alertUid","ALERT_UID","AlertUid","java.lang.Long",
"com.gridnode.pdip.app.alert.model.AlertAction","alertAction.alertUid",
"20","0","1","1","1","0","999",
"displayable=true\r\nmandatory=true\r\neditable=true",
"type=foreign\r\nforeign.key=alert.uid\r\nforeign.display=alert.name\r\nforeign.cached=false");

# FieldMetaInfo for AlertCategory
DELETE FROM fieldmetainfo WHERE EntityObjectName LIKE "%.AlertCategory";
INSERT INTO fieldmetainfo VALUES(
NULL,"_uId","UID","UID","java.lang.Long",
"com.gridnode.pdip.app.alert.model.AlertCategory","alertCategory.uid",
"20","0","0","0","0","0","999",
"displayable=false\r\nmandatory=false\r\neditable=false",
"type=uid");

INSERT INTO fieldmetainfo VALUES(
NULL,"_code","CODE","Code","java.lang.String",
"com.gridnode.pdip.app.alert.model.AlertCategory","alertCategory.code",
"20","0","1","1","1","0","999",
"displayable=true\r\nmandatory=true\r\neditable=false\r\neditable.create=true",
"type=text\r\ntext.length.max=20");

INSERT INTO fieldmetainfo VALUES(
NULL,"_name","NAME","Name","java.lang.String",
"com.gridnode.pdip.app.alert.model.AlertCategory","alertCategory.name",
"80","0","1","1","1","0","999",
"displayable=true\r\nmandatory=true\r\neditable=true",
"type=text\r\ntext.length.max=80");

INSERT INTO fieldmetainfo VALUES(
NULL,"_description","DESCRIPTION","Description","java.lang.String",
"com.gridnode.pdip.app.alert.model.AlertCategory","alertCategory.description",
"255","0","0","1","1","0","999",
"displayable=true\r\neditable=true\r\nmandatory=false",
"type=text\r\ntext.length.max=255");

INSERT INTO fieldmetainfo VALUES(
NULL,"_version","VERSION","Version","java.lang.Double",
"com.gridnode.pdip.app.alert.model.AlertCategory","",
"0","0","0","0","0","","999",
"displayable=false\r\nmandatory=false\r\neditable=false",
"type=range"
);

INSERT INTO fieldmetainfo VALUES(
NULL,"_canDelete","CAN_DELETE","CanDelete","java.lang.Boolean",
"com.gridnode.pdip.app.alert.model.AlertCategory","",
"0","0","0","0","0","","999",
"displayable=false\r\nmandatory=false\r\neditable=false",
"type=enum\r\ngeneric.yes=true\r\ngeneric.no=false"
);

# FieldMetaInfo for AlertList
# temporary not used in 2.0 I7
DELETE FROM fieldmetainfo WHERE EntityObjectName LIKE "%.AlertList";
INSERT INTO fieldmetainfo VALUES(
NULL,"_uId","UID","UID","java.lang.Long",
"com.gridnode.pdip.app.alert.model.AlertList","alertList.uid",
"20","0","0","0","0","0","999",
"displayable=false\r\nmandatory=false\r\neditable=false",
"type=uid");

INSERT INTO fieldmetainfo VALUES(
NULL,"_userUid","USER_UID","UserUid","java.lang.Long",
"com.gridnode.pdip.app.alert.model.AlertList","alertList.userUid",
"20","0","0","1","1","0","999",
"displayable=true\r\neditable=true\r\nmandatory=false",
"type=text");

INSERT INTO fieldmetainfo VALUES(
NULL,"_from_uid","FROM_UID","FromUid","java.lang.Long",
"com.gridnode.pdip.app.alert.model.AlertList","alertList.fromUid",
"20","0","0","1","1","0","999",
"displayable=true\r\neditable=false\r\nmandatory=false",
"type=text");

INSERT INTO fieldmetainfo VALUES(
NULL,"_title","TITLE","Title","java.lang.String",
"com.gridnode.pdip.app.alert.model.AlertList","alertList.title",
"35","0","0","1","1","0","999",
"displayable=true\r\neditable=false\r\nmandatory=false",
"type=text");

INSERT INTO fieldmetainfo VALUES(
NULL,"_message","MESSAGE","Message","java.lang.String",
"com.gridnode.pdip.app.alert.model.AlertList","alertList.message",
"1024","0","0","1","1","0","999",
"displayable=true\r\neditable=false\r\nmandatory=false",
"type=text");

INSERT INTO fieldmetainfo VALUES(
NULL,"_readstatus","READSTATUS","ReadStatus","java.lang.Boolean",
"com.gridnode.pdip.app.alert.model.AlertList","alertList.readStatus",
"1","0","0","1","1","0","999",
"displayable=true\r\neditable=false\r\nmandatory=false",
"type=enum\r\ngeneric.yes=true\r\ngeneric.no=false");

INSERT INTO fieldmetainfo VALUES(
NULL,"_date","DATE","Date","java.util.Date",
"com.gridnode.pdip.app.alert.model.AlertList","alertList.date",
"20","0","0","1","1","0","999",
"displayable=true\r\neditable=false\r\nmandatory=false",
"type=text");

INSERT INTO fieldmetainfo VALUES(
NULL,"_version","VERSION","Version","java.lang.Double",
"com.gridnode.pdip.app.alert.model.AlertList","",
"0","0","0","0","0","","999",
"displayable=false\r\nmandatory=false\r\neditable=false",
"type=range"
);

INSERT INTO fieldmetainfo VALUES(
NULL,"_canDelete","CAN_DELETE","CanDelete","java.lang.Boolean",
"com.gridnode.pdip.app.alert.model.AlertList","",
"0","0","0","0","0","","999",
"displayable=false\r\nmandatory=false\r\neditable=false",
"type=enum\r\ngeneric.yes=true\r\ngeneric.no=false"
);

# FieldMetaInfo for AlertType
DELETE FROM fieldmetainfo WHERE EntityObjectName LIKE "%.AlertType";
INSERT INTO fieldmetainfo VALUES(
NULL,"_uId","UID","UID","java.lang.Long",
"com.gridnode.pdip.app.alert.model.AlertType","alertType.uid",
"20","0","0","0","0","0","999",
"displayable=false\r\nmandatory=false\r\neditable=false",
"type=uid");

INSERT INTO fieldmetainfo VALUES(
NULL,"_name","NAME","Name","java.lang.String",
"com.gridnode.pdip.app.alert.model.AlertType","alertType.name",
"80","0","1","1","1","0","999",
"displayable=true\r\nmandatory=true\r\neditable=false\r\neditable.create=true",
"type=text\r\ntext.length.max=80");

INSERT INTO fieldmetainfo VALUES(
NULL,"_description","DESCRIPTION","Description","java.lang.String",
"com.gridnode.pdip.app.alert.model.AlertType","alertType.description",
"255","0","0","1","1","0","999",
"displayable=true\r\neditable=true\r\nmandatory=true",
"type=text\r\ntext.length.max=255");

INSERT INTO fieldmetainfo VALUES(
NULL,"_version","VERSION","Version","java.lang.Double",
"com.gridnode.pdip.app.alert.model.AlertType","",
"0","0","0","0","0","","999",
"displayable=false\r\nmandatory=false\r\neditable=false",
"type=range"
);

INSERT INTO fieldmetainfo VALUES(
NULL,"_canDelete","CAN_DELETE","CanDelete","java.lang.Boolean",
"com.gridnode.pdip.app.alert.model.AlertType","",
"0","0","0","0","0","","999",
"displayable=false\r\nmandatory=false\r\neditable=false",
"type=enum\r\ngeneric.yes=true\r\ngeneric.no=false"
);


# FieldMetaInfo for EmailConfig
DELETE FROM fieldmetainfo WHERE EntityObjectName LIKE "%EmailConfig";

INSERT INTO fieldmetainfo VALUES
(NULL,"_smtpServerHost","SMTP_SERVER_HOST",NULL,"java.lang.String","com.gridnode.pdip.app.alert.model.EmailConfig","emailConfig.smtpServerHost","80","0","1","1","1","0","999","displayable=true\r\nmandatory=true\r\neditable=true","type=text\r\ntext.length.max=80");

INSERT INTO fieldmetainfo VALUES
(NULL,"_smtpServerPort","SMTP_SERVER_PORT",NULL,"java.lang.Long","com.gridnode.pdip.app.alert.model.EmailConfig","emailConfig.smtpServerPort","0","0","0","1","1",NULL,"999","displayable=true\r\nmandatory=true\r\neditable=true","type=range");

INSERT INTO fieldmetainfo VALUES
(NULL,"_authUser","AUTH_USER",NULL,"java.lang.String","com.gridnode.pdip.app.alert.model.EmailConfig","emailConfig.authUser","80","0","1","1","1","0","999","displayable=true\r\nmandatory=false\r\neditable=true","type=text\r\ntext.length.max=80");

INSERT INTO fieldmetainfo VALUES
(NULL,"_authPassword","AUTH_PASSWORD",NULL,"java.lang.String","com.gridnode.pdip.app.alert.model.EmailConfig","emailConfig.authPassword","80","0","1","1","1","0","999","displayable=true\r\nmandatory=false\r\neditable=true","type=text\r\ntext.length.max=80");

INSERT INTO fieldmetainfo VALUES
(NULL,"_retryInterval","RETRY_INTERVAL",NULL,"java.lang.Long","com.gridnode.pdip.app.alert.model.EmailConfig","emailConfig.retryInterval","0","0","0","1","1",NULL,"999","displayable=true\r\nmandatory=true\r\neditable=true","type=range\r\nrange.min=180");

INSERT INTO fieldmetainfo VALUES
(NULL,"_maxRetries","MAX_RETRIES",NULL,"java.lang.Integer","com.gridnode.pdip.app.alert.model.EmailConfig","emailConfig.maxRetries","0","0","0","1","1",NULL,"999","displayable=true\r\nmandatory=true\r\neditable=true","type=range");


INSERT INTO fieldmetainfo VALUES
(NULL, "_saveFailedEmails", "SAVE_FAILED_EMAILS", NULL, "java.lang.Boolean", "com.gridnode.pdip.app.alert.model.EmailConfig", "emailConfig.saveFailedEmails", "0", "0", "0", "1", "1", NULL, 999, "displayable=true\r\nmandatory=true\r\n\editable=true", "type=enum\r\ngeneric.yes=true\r\ngeneric.no=false");

#*************FieldMetaInfo for jms_destination
DELETE FROM fieldmetainfo WHERE EntityObjectName LIKE '%JmsDestination';

INSERT INTO fieldmetainfo VALUES(
NULL,"_uId","UID","UID","java.lang.Long",
"com.gridnode.pdip.app.alert.model.JmsDestination","jmsDestination.uid",
"0","0","0","0","0","","999",
"displayable=false\r\nmandatory=false\r\neditable=false",
"type=uid"
);

INSERT INTO fieldmetainfo VALUES(
NULL,"_version","VERSION","Version","java.lang.Double",
"com.gridnode.pdip.app.alert.model.JmsDestination","",
"0","0","0","0","0","","999",
"displayable=false\r\nmandatory=false\r\neditable=false",
"type=range"
);


INSERT INTO fieldmetainfo VALUES(
NULL,"_canDelete","CAN_DELETE","CanDelete","java.lang.Boolean",
"com.gridnode.pdip.app.alert.model.JmsDestination","",
"0","0","1","0","0","","999",
"displayable=false\r\nmandatory=false\r\neditable=false",
"type=enum\r\ncandelete.enabled=true\r\ncandelete.disabled=false"
);


INSERT INTO fieldmetainfo VALUES(
NULL,"_name","NAME","Name","java.lang.String",
"com.gridnode.pdip.app.alert.model.JmsDestination","jmsDestination.name",
"30","0","1","1","1","","999",
"displayable=true\r\nmandatory=true\r\neditable=true",
"type=text\r\ntext.length.max=30"
);

INSERT INTO fieldmetainfo VALUES(
NULL,"_type","TYPE","Type","java.lang.Integer",
"com.gridnode.pdip.app.alert.model.JmsDestination","jmsDestination.type",
"1","0","1","0","1","","999",
"displayable=true\r\nmandatory=true\r\neditable=true",
"type=enum\r\njmsDestination.type.queue=1\r\njmsDestination.type.topic=2");


INSERT INTO fieldmetainfo VALUES(
NULL,"_jndiName","JNDI_NAME","JndiName","java.lang.String",
"com.gridnode.pdip.app.alert.model.JmsDestination","jmsDestination.jndiName",
"255","0","1","1","1","","999",
"displayable=true\r\nmandatory=true\r\neditable=true",
"type=text\r\ntext.length.max=255"
);

INSERT INTO fieldmetainfo VALUES(
NULL,"_deliveryMode","DELIVERY_MODE","DeliveryMode","java.lang.Integer",
"com.gridnode.pdip.app.alert.model.JmsDestination","jmsDestination.deliveryMode",
"2","0","1","0","1","","999",
"displayable=true\r\nmandatory=true\r\neditable=true",
"type=enum\r\njmsDestination.deliveryMode.Default=0\r\njmsDestination.deliveryMode.Persistent=1\r\njmsDestination.deliveryMode.Non-Persistent=2");

INSERT INTO fieldmetainfo VALUES(
NULL,"_priority","PRIORITY","Priority","java.lang.Integer",
"com.gridnode.pdip.app.alert.model.JmsDestination","jmsDestination.priority",
"2","0","1","0","1","","999",
"displayable=true\r\nmandatory=true\r\neditable=true",
"type=enum\r\njmsDestination.priority.default=-1\r\njmsDestination.priority.0=0\r\njmsDestination.priority.1=1\r\njmsDestination.priority.2=2\r\njmsDestination.priority.3=3\r\njmsDestination.priority.4=4\r\njmsDestination.priority.5=5\r\njmsDestination.priority.6=6\r\njmsDestination.priority.7=7\r\njmsDestination.priority.8=8\r\njmsDestination.priority.9=9");

INSERT INTO fieldmetainfo VALUES(
NULL,"_connectionFactoryJndi","CONNECTION_FACTORY_JNDI","ConnectionFactoryJndi","java.lang.String",
"com.gridnode.pdip.app.alert.model.JmsDestination","jmsDestination.connectionFactoryJndi",
"255","0","1","1","1","","999",
"displayable=true\r\nmandatory=true\r\neditable=true",
"type=text\r\ntext.length.max=255"
);

INSERT INTO fieldmetainfo VALUES(
NULL,"_connectionUser","CONNECTION_USER","ConnectionUser","java.lang.String",
"com.gridnode.pdip.app.alert.model.JmsDestination","jmsDestination.connectionUser",
"30","0","1","1","1","","999",
"displayable=true\r\nmandatory=false\r\neditable=true",
"type=text\r\ntext.length.max=30"
);

INSERT INTO fieldmetainfo VALUES(
NULL,"_connectionPassword","CONNECTION_PASSWORD","ConnectionPassword","java.lang.String",
"com.gridnode.pdip.app.alert.model.JmsDestination","jmsDestination.connectionPassword",
"30","0","1","1","1","","999",
"displayable=true\r\nmandatory=false\r\neditable=true",
"type=text\r\ntext.length.max=30"
);

INSERT INTO fieldmetainfo VALUES(
NULL,"_lookupProperties","LOOKUP_PROPERTIES","LookupProperties","java.util.Properties",
"com.gridnode.pdip.app.alert.model.JmsDestination","jmsDestination.lookupProperties",
"0","0","1","1","1","","999",
"displayable=true\r\nmandatory=false\r\neditable=true",
"type=other"
);

INSERT INTO fieldmetainfo VALUES(
NULL,"_retryInterval","RETRY_INTERVAL","RetryInterval","java.lang.Integer",
"com.gridnode.pdip.app.alert.model.JmsDestination","jmsDestination.retryInterval",
"11","0","1","0","1","","999",
"displayable=true\r\nmandatory=true\r\neditable=true",
"type=range\r\nrange.min=1\r\nrange.max=65535");

INSERT INTO fieldmetainfo VALUES(
NULL,"_maximumRetries","MAXIMUM_RETRIES","MaximumRetries","java.lang.Integer",
"com.gridnode.pdip.app.alert.model.JmsDestination","jmsDestination.maximumRetries",
"11","0","1","0","1","","999",
"displayable=true\r\nmandatory=true\r\neditable=true",
"type=range\r\nrange.min=-1\r\nrange.max=65535");


#************FieldMetaInfo for entity MessageProperty
DELETE FROM fieldmetainfo WHERE EntityObjectName ='com.gridnode.pdip.app.alert.model.MessageProperty';

INSERT INTO fieldmetainfo VALUES(
NULL,"_key","KEY","","java.lang.String",
"com.gridnode.pdip.app.alert.model.MessageProperty","messageProperty.key",
"50","0","1","1","1","","999",
"displayable=true\r\nmandatory=true\r\neditable=true",
"type=text\r\ntext.length.max=50"
);

INSERT INTO fieldmetainfo VALUES(
NULL,"_type","TYPE","","java.lang.Integer",
"com.gridnode.pdip.app.alert.model.MessageProperty","messageProperty.type",
"1","0","1","0","1","","999",
"displayable=true\r\nmandatory=true\r\neditable=true",
"type=enum\r\nmessage.property.Boolean=1\r\nmessage.property.Byte=2\r\nmessage.property.Double=3\r\nmessage.property.Float=4\r\nmessage.property.Int=5\r\nmessage.property.Long=6\r\nmessage.property.Short=7\r\nmessage.property.String=8");

INSERT INTO fieldmetainfo VALUES(
NULL,"_value","VALUE","","java.lang.String",
"com.gridnode.pdip.app.alert.model.MessageProperty","messageProperty.value",
"255","0","1","1","1","","999",
"displayable=true\r\nmandatory=true\r\neditable=true",
"type=text\r\ntext.length.max=255"
);

#***********FieldMetaInfo for entity JmsMessageRecord
DELETE FROM fieldmetainfo WHERE EntityObjectName ='com.gridnode.pdip.app.alert.model.JmsMessageRecord';

INSERT INTO fieldmetainfo VALUES(
NULL,"_uId","UID","UID","java.lang.Long",
"com.gridnode.pdip.app.alert.model.JmsMessageRecord","",
"0","0","0","0","0","","999",
"displayable=false\r\nmandatory=false\r\neditable=false",
"type=uid"
);

INSERT INTO fieldmetainfo VALUES(
NULL,"_version","VERSION","Version","java.lang.Double",
"com.gridnode.pdip.app.alert.model.JmsMessageRecord","",
"0","0","0","0","0","","999",
"displayable=false\r\nmandatory=false\r\neditable=false",
"type=range"
);


INSERT INTO fieldmetainfo VALUES(
NULL,"_canDelete","CAN_DELETE","CanDelete","java.lang.Boolean",
"com.gridnode.pdip.app.alert.model.JmsMessageRecord","",
"0","0","1","0","0","","999",
"displayable=false\r\nmandatory=false\r\neditable=false",
"type=enum\r\ncandelete.enabled=true\r\ncandelete.disabled=false"
);

INSERT INTO fieldmetainfo VALUES(
NULL,"_jmsDestinationUid","JMS_DESTINATION_UID","JmsDestUid","java.lang.Long",
"com.gridnode.pdip.app.alert.model.JmsMessageRecord","",
"0","0","1","1","1","","999",
"displayable=false\r\nmandatory=false\r\neditable=false",
"type=foreign\r\nforeign.key=jmsDestination.uid\r\nforeign.display=jmsDestination.name\r\nforeign.cached=false"
);

INSERT INTO fieldmetainfo VALUES(
NULL,"_alertTimestamp","ALERT_TIMESTAMP","AlertTimestamp","java.util.Date",
"com.gridnode.pdip.app.alert.model.JmsMessageRecord","",
"0","0","0","0","1","","999",
"displayable=true\r\nmandatory=false\r\neditable=false",
"type=datetime\r\ndatetime.time=true\r\ndatetime.date=true\r\ndatetime.adjustment=gts"
);

INSERT INTO fieldmetainfo VALUES(
NULL,"_msgData","MESSAGE_DATA","MsgData","com.gridnode.pdip.app.alert.jms.MessageData",
"com.gridnode.pdip.app.alert.model.JmsMessageRecord","",
"0","0","0","0","1","","999",
"displayable=true\r\nmandatory=false\r\neditable=false",
"type=text\r\ntext.length.max=999999"
);

INSERT INTO fieldmetainfo VALUES(
NULL,"_permanentFailed","PERMANENT_FAILED","PermanentFailed","java.lang.Boolean",
"com.gridnode.pdip.app.alert.model.JmsMessageRecord","",
"0","0","1","0","0","","999",
"displayable=false\r\nmandatory=false\r\neditable=false",
"type=text\r\ntext.length.max=9"
);

INSERT INTO fieldmetainfo VALUES(
NULL,"_alertTimeInLong","ALERT_TIME_IN_LONG","AlertTimeInLong","java.lang.Long",
"com.gridnode.pdip.app.alert.model.JmsMessageRecord","",
"0","0","1","0","0","","999",
"displayable=false\r\nmandatory=false\r\neditable=false",
"type=text\r\ntext.length.max=9"
);

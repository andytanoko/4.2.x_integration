#
# 30 Apr 2003 v2.1 I1 [Neo Sok Lay] Add AttachRespDoc field
# 05 Dec 2006 GT 4.0  [Neo Sok Lay] Increase SentDocType, ResponseDocType field length to 30 chars.
#

use appdb;

#
# Dumping data for table 'entitymetainfo'
#
# Field 1 ObjectName: full qualified class name of the entity
# Field 2 EntityName: short class name of the entity
# Field 3 SqlName: table name of the entity

DELETE FROM entitymetainfo WHERE EntityName IN
('ResponseTrackRecord','ReminderAlert','ActiveTrackRecord');
# entitymetainfo for response_track_record
INSERT INTO entitymetainfo VALUES("com.gridnode.gtas.server.docalert.model.ResponseTrackRecord","ResponseTrackRecord","response_track_record");
# entitymetainfo for reminder_alert
INSERT INTO entitymetainfo VALUES("com.gridnode.gtas.server.docalert.model.ReminderAlert","ReminderAlert","reminder_alert");
# entitymetainfo for active_track_record
INSERT INTO entitymetainfo VALUES("com.gridnode.gtas.server.docalert.model.ActiveTrackRecord","ActiveTrackRecord","active_track_record");


#
# Dumping data for table 'fieldmetainfo'
#
# UID bigint(20) NOT NULL auto_increment,
# ObjectName: field name in Entity class ,
# FieldName: field ID in Entity Interface class ,
# SqlName: Field column name in Table,
# ValueClass: field data type,
# EntityObjectName: full qualified class name of the entity
# Label: field display label
# Length: valid field length ,
# Proxy: '1' if proxy, '0' otherwise,,
# Mandatory: '1' if mandatory, '0' otherwise,
# Editable: '1' if editable, '0' otherwise
# Displayable: '1' if displayable, '0' otherwise
# OqlName: ,
# Sequence: default '999' ,
# Presentation: properties for presentation of the field
# Constraints: constraint imposed on the values of the field
#

# fieldmetainfo for response_track_record
DELETE FROM fieldmetainfo WHERE EntityObjectName LIKE '%ResponseTrackRecord';
INSERT INTO fieldmetainfo VALUES(
NULL,"_uId","UID","UID","java.lang.Long",
"com.gridnode.gtas.server.docalert.model.ResponseTrackRecord","responseTrackRecord.uid",
"0","0","0","0","0","","999",
"displayable=false\r\nmandatory=false\r\neditable=false",
"type=uid"
);
INSERT INTO fieldmetainfo VALUES(
NULL,"_sentDocType","SENT_DOC_TYPE","SentDocType","java.lang.String",
"com.gridnode.gtas.server.docalert.model.ResponseTrackRecord","responseTrackRecord.sentDocType",
"30","0","1","0","1","","1",
"displayable=true\r\nmandatory=true\r\neditable=true",
"type=foreign\r\nforeign.key=documentType.docType\r\nforeign.cached=false\r\nforeign.display=documentType.description"
);
INSERT INTO fieldmetainfo VALUES(
NULL,"_sentDocIdXpath","SENT_DOCID_XPATH","SentDocIdXpath","java.lang.String",
"com.gridnode.gtas.server.docalert.model.ResponseTrackRecord","responseTrackRecord.sentDocIdXpath",
"255","0","1","1","1","","2",
"displayable=true\r\nmandatory=false\r\neditable=true",
"type=text\r\ntext.length.max=255"
);
INSERT INTO fieldmetainfo VALUES(
NULL,"_startTrackDateXpath","START_TRACK_DATE_XPATH","StartTrackDateXpath","java.lang.String",
"com.gridnode.gtas.server.docalert.model.ResponseTrackRecord","responseTrackRecord.startTrackDateXpath",
"255","0","1","1","1","","2",
"displayable=true\r\nmandatory=false\r\neditable=true",
"type=text\r\ntext.length.max=255"
);
INSERT INTO fieldmetainfo VALUES(
NULL,"_responseDocType","RESPONSE_DOC_TYPE","ResponseDocType","java.lang.String",
"com.gridnode.gtas.server.docalert.model.ResponseTrackRecord","responseTrackRecord.responseDocType",
"30","0","1","0","1","","1",
"displayable=true\r\nmandatory=true\r\neditable=true",
"type=foreign\r\nforeign.key=documentType.docType\r\nforeign.cached=false\r\nforeign.display=documentType.description"
);
INSERT INTO fieldmetainfo VALUES(
NULL,"_responseDocIdXpath","RESPONSE_DOCID_XPATH","ResponseDocIdXpath","java.lang.String",
"com.gridnode.gtas.server.docalert.model.ResponseTrackRecord","responseTrackRecord.responseDocIdXpath",
"255","0","1","1","1","","2",
"displayable=true\r\nmandatory=false\r\neditable=true",
"type=text\r\ntext.length.max=255"
);
INSERT INTO fieldmetainfo VALUES(
NULL,"_receiveResponseAlert","RECEIVE_RESPONSE_ALERT","ReceiveResponseAlert","java.lang.String",
"com.gridnode.gtas.server.docalert.model.ResponseTrackRecord","responseTrackRecord.receiveResponseAlert",
"20","0","1","0","1","","1",
"displayable=true\r\nmandatory=false\r\neditable=true",
"type=foreign\r\nforeign.key=alert.name\r\nforeign.cached=false\r\nforeign.display=alert.description"
);
INSERT INTO fieldmetainfo VALUES(
NULL,"_alertRecipientXpath","ALERT_RECPT_XPATH","AlertRecipientXpath","java.lang.String",
"com.gridnode.gtas.server.docalert.model.ResponseTrackRecord","responseTrackRecord.alertRecipientXpath",
"255","0","1","1","1","","2",
"displayable=true\r\nmandatory=false\r\neditable=true",
"type=text\r\ntext.length.max=255"
);
INSERT INTO fieldmetainfo VALUES(
NULL,"_reminderAlerts","REMINDER_ALERTS","","java.util.Collection",
"com.gridnode.gtas.server.docalert.model.ResponseTrackRecord","responseTrackRecord.reminderAlerts",
"0","0","1","1","1","","2",
"displayable=true\r\nmandatory=false\r\neditable=true",
"collection=true\r\ncollection.element=java.util.HashMap\r\ntype=embedded\r\nembedded.type=reminderAlert"
);
INSERT INTO fieldmetainfo VALUES(
NULL,"_version","VERSION","Version","java.lang.Double",
"com.gridnode.gtas.server.docalert.model.ResponseTrackRecord","",
"0","0","0","0","0","","999",
"displayable=false\r\nmandatory=false\r\neditable=false",
"type=range"
);
INSERT INTO fieldmetainfo VALUES(
NULL,"_attachRespDoc","IS_ATTACH_RESPONSE_DOC","AttachRespDoc","java.lang.Boolean",
"com.gridnode.gtas.server.docalert.model.ResponseTrackRecord","responseTrackRecord.isAttachResponseDoc",
"0","0","0","0","0","","999",
"displayable=true\r\nmandatory=false\r\neditable=true",
"type=enum\r\ngeneric.yes=true\r\ngeneric.no=false"
);


# fieldmetainfo for reminder_alert
DELETE FROM fieldmetainfo WHERE EntityObjectName LIKE '%ReminderAlert';
INSERT INTO fieldmetainfo VALUES(
NULL,"_uId","UID","UID","java.lang.Long",
"com.gridnode.gtas.server.docalert.model.ReminderAlert","reminderAlert.uid",
"0","0","0","0","0","","999",
"displayable=false\r\nmandatory=false\r\neditable=false",
"type=uid"
);
INSERT INTO fieldmetainfo VALUES(
NULL,"_trackRecordUID","TRACK_RECORD_UID","TrackRecordUID","java.lang.Long",
"com.gridnode.gtas.server.docalert.model.ReminderAlert","reminderAlert.trackRecordUid",
"20","0","1","0","1","","999",
"displayable=false\r\nmandatory=false\r\neditable=false\r\n",
"type=foreign\r\nforeign.cached=false\r\nforeign.key=responseTrackRecord.uid\r\nforeign.display=responseTrackRecord.sentDocType"
);
INSERT INTO fieldmetainfo VALUES(
NULL,"_daysToReminder","DAYS_TO_REMINDER","DaysToReminder","java.lang.Integer",
"com.gridnode.gtas.server.docalert.model.ReminderAlert","reminderAlert.daysToReminder",
"3","0","1","1","1","","999",
"displayable=true\r\nmandatory=true\r\neditable=false\r\neditable.create=true",
"type=range\r\nrange.min=1\r\nrange.max=100"
);
INSERT INTO fieldmetainfo VALUES(
NULL,"_alertToRaise","ALERT_TO_RAISE","AlertToRaise","java.lang.String",
"com.gridnode.gtas.server.docalert.model.ReminderAlert","reminderAlert.alertToRaise",
"20","0","1","1","1","","999",
"displayable=true\r\nmandatory=true\r\neditable=true",
"type=foreign\r\nforeign.cached=false\r\nforeign.key=alert.name\r\nforeign.display=alert.description"
);
INSERT INTO fieldmetainfo VALUES(
NULL,"_docRecipientXpath","DOC_RECPT_XPATH","DocRecipientXpath","java.lang.String",
"com.gridnode.gtas.server.docalert.model.ReminderAlert","reminderAlert.docRecipientXpath",
"255","0","0","1","1","","999",
"displayable=true\r\nmandatory=false\r\neditable=true",
"type=text\r\ntext.length.max=255"
);
INSERT INTO fieldmetainfo VALUES(
NULL,"_docSenderXpath","DOC_SENDER_XPATH","DocSenderXpath","java.lang.String",
"com.gridnode.gtas.server.docalert.model.ReminderAlert","reminderAlert.docSenderXpath",
"255","0","0","1","1","","999",
"displayable=true\r\nmandatory=false\r\neditable=true",
"type=text\r\ntext.length.max=255"
);
INSERT INTO fieldmetainfo VALUES(
NULL,"_version","VERSION","Version","java.lang.Double",
"com.gridnode.gtas.server.docalert.model.ReminderAlert","",
"0","0","0","0","0","","999",
"displayable=false\r\nmandatory=false\r\neditable=false",
"type=range"
);

# fieldmetainfo for active_track_record
DELETE FROM fieldmetainfo WHERE EntityObjectName LIKE '%ActiveTrackRecord';
INSERT INTO fieldmetainfo VALUES(
NULL,"_uId","UID","UID","java.lang.Long",
"com.gridnode.gtas.server.docalert.model.ActiveTrackRecord","",
"0","0","0","0","0","","999",
"",
""
);
INSERT INTO fieldmetainfo VALUES(
NULL,"_sentGridDocUID","SENT_GRIDDOC_UID","SentGridDocUID","java.lang.Long",
"com.gridnode.gtas.server.docalert.model.ActiveTrackRecord","",
"20","0","1","0","1","","999",
"",
""
);
INSERT INTO fieldmetainfo VALUES(
NULL,"_trackRecordUID","TRACK_RECORD_UID","TrackRecordUID","java.lang.Long",
"com.gridnode.gtas.server.docalert.model.ActiveTrackRecord","",
"20","0","0","0","1","","999",
"",
""
);
INSERT INTO fieldmetainfo VALUES(
NULL,"_daysToReminder","DAYS_TO_REMINDER","DaysToReminder","java.lang.Integer",
"com.gridnode.gtas.server.docalert.model.ActiveTrackRecord","",
"3","0","0","0","1","","999",
"",
""
);
INSERT INTO fieldmetainfo VALUES(
NULL,"_alarmUID","ALARM_UID","AlarmUID","java.lang.Long",
"com.gridnode.gtas.server.docalert.model.ActiveTrackRecord","",
"20","0","0","0","1","","999",
"",
""
);

SET client_encoding = 'UTF8';
SET check_function_bodies = false;
SET client_min_messages = warning;
SET search_path = appdb;

--
-- Dumping data for table 'entitymetainfo'
--
-- Field 1 ObjectName: full qualified class name of the entity
-- Field 2 EntityName: short class name of the entity
-- Field 3 SqlName: table name of the entity

DELETE FROM "entitymetainfo" WHERE "EntityName" IN ('ResponseTrackRecord','ReminderAlert','ActiveTrackRecord');
INSERT INTO "entitymetainfo" VALUES('com.gridnode.gtas.server.docalert.model.ResponseTrackRecord','ResponseTrackRecord','"response_track_record"');
INSERT INTO "entitymetainfo" VALUES('com.gridnode.gtas.server.docalert.model.ReminderAlert','ReminderAlert','"reminder_alert"');
INSERT INTO "entitymetainfo" VALUES('com.gridnode.gtas.server.docalert.model.ActiveTrackRecord','ActiveTrackRecord','"active_track_record"');


--
-- Dumping data for table 'fieldmetainfo'
--
-- UID bigint(20) NOT NULL auto_increment,
-- ObjectName: field name in Entity class ,
-- FieldName: field ID in Entity Interface class ,
-- SqlName: Field column name in Table,
-- ValueClass: field data type,
-- EntityObjectName: full qualified class name of the entity
-- Label: field display label
-- Length: valid field length ,
-- Proxy: '1' if proxy, '0' otherwise,,
-- Mandatory: '1' if mandatory, '0' otherwise,
-- Editable: '1' if editable, '0' otherwise
-- Displayable: '1' if displayable, '0' otherwise
-- OqlName: ,
-- Sequence: default '999' ,
-- Presentation: properties for presentation of the field
-- Constraints: constraint imposed on the values of the field

---------- response_track_record
DELETE FROM "fieldmetainfo" WHERE "EntityObjectName" LIKE '%ResponseTrackRecord';
INSERT INTO "fieldmetainfo" VALUES(DEFAULT,'_uId','UID','"UID"','java.lang.Long','com.gridnode.gtas.server.docalert.model.ResponseTrackRecord','responseTrackRecord.uid',0,0,0,0,0,'',999,'displayable=false'||chr(13)||chr(10)||'mandatory=false'||chr(13)||chr(10)||'editable=false','type=uid');
INSERT INTO "fieldmetainfo" VALUES(DEFAULT,'_sentDocType','SENT_DOC_TYPE','"SentDocType"','java.lang.String','com.gridnode.gtas.server.docalert.model.ResponseTrackRecord','responseTrackRecord.sentDocType',30,0,1,0,1,'',1,'displayable=true'||chr(13)||chr(10)||'mandatory=true'||chr(13)||chr(10)||'editable=true','type=foreign'||chr(13)||chr(10)||'foreign.key=documentType.docType'||chr(13)||chr(10)||'foreign.cached=false'||chr(13)||chr(10)||'foreign.display=documentType.description');
INSERT INTO "fieldmetainfo" VALUES(DEFAULT,'_sentDocIdXpath','SENT_DOCID_XPATH','"SentDocIdXpath"','java.lang.String','com.gridnode.gtas.server.docalert.model.ResponseTrackRecord','responseTrackRecord.sentDocIdXpath',255,0,1,1,1,'',2,'displayable=true'||chr(13)||chr(10)||'mandatory=false'||chr(13)||chr(10)||'editable=true','type=text'||chr(13)||chr(10)||'text.length.max=255');
INSERT INTO "fieldmetainfo" VALUES(DEFAULT,'_startTrackDateXpath','START_TRACK_DATE_XPATH','"StartTrackDateXpath"','java.lang.String','com.gridnode.gtas.server.docalert.model.ResponseTrackRecord','responseTrackRecord.startTrackDateXpath',255,0,1,1,1,'',2,'displayable=true'||chr(13)||chr(10)||'mandatory=false'||chr(13)||chr(10)||'editable=true','type=text'||chr(13)||chr(10)||'text.length.max=255');
INSERT INTO "fieldmetainfo" VALUES(DEFAULT,'_responseDocType','RESPONSE_DOC_TYPE','"ResponseDocType"','java.lang.String','com.gridnode.gtas.server.docalert.model.ResponseTrackRecord','responseTrackRecord.responseDocType',30,0,1,0,1,'',1,'displayable=true'||chr(13)||chr(10)||'mandatory=true'||chr(13)||chr(10)||'editable=true','type=foreign'||chr(13)||chr(10)||'foreign.key=documentType.docType'||chr(13)||chr(10)||'foreign.cached=false'||chr(13)||chr(10)||'foreign.display=documentType.description');
INSERT INTO "fieldmetainfo" VALUES(DEFAULT,'_responseDocIdXpath','RESPONSE_DOCID_XPATH','"ResponseDocIdXpath"','java.lang.String','com.gridnode.gtas.server.docalert.model.ResponseTrackRecord','responseTrackRecord.responseDocIdXpath',255,0,1,1,1,'',2,'displayable=true'||chr(13)||chr(10)||'mandatory=false'||chr(13)||chr(10)||'editable=true','type=text'||chr(13)||chr(10)||'text.length.max=255');
INSERT INTO "fieldmetainfo" VALUES(DEFAULT,'_receiveResponseAlert','RECEIVE_RESPONSE_ALERT','"ReceiveResponseAlert"','java.lang.String','com.gridnode.gtas.server.docalert.model.ResponseTrackRecord','responseTrackRecord.receiveResponseAlert',20,0,1,0,1,'',1,'displayable=true'||chr(13)||chr(10)||'mandatory=false'||chr(13)||chr(10)||'editable=true','type=foreign'||chr(13)||chr(10)||'foreign.key=alert.name'||chr(13)||chr(10)||'foreign.cached=false'||chr(13)||chr(10)||'foreign.display=alert.description');
INSERT INTO "fieldmetainfo" VALUES(DEFAULT,'_alertRecipientXpath','ALERT_RECPT_XPATH','"AlertRecipientXpath"','java.lang.String','com.gridnode.gtas.server.docalert.model.ResponseTrackRecord','responseTrackRecord.alertRecipientXpath',255,0,1,1,1,'',2,'displayable=true'||chr(13)||chr(10)||'mandatory=false'||chr(13)||chr(10)||'editable=true','type=text'||chr(13)||chr(10)||'text.length.max=255');
INSERT INTO "fieldmetainfo" VALUES(DEFAULT,'_reminderAlerts','REMINDER_ALERTS','','java.util.Collection','com.gridnode.gtas.server.docalert.model.ResponseTrackRecord','responseTrackRecord.reminderAlerts',0,0,1,1,1,'',2,'displayable=true'||chr(13)||chr(10)||'mandatory=false'||chr(13)||chr(10)||'editable=true','collection=true'||chr(13)||chr(10)||'collection.element=java.util.HashMap'||chr(13)||chr(10)||'type=embedded'||chr(13)||chr(10)||'embedded.type=reminderAlert');
INSERT INTO "fieldmetainfo" VALUES(DEFAULT,'_version','VERSION','"Version"','java.lang.Double','com.gridnode.gtas.server.docalert.model.ResponseTrackRecord','',0,0,0,0,0,'',999,'displayable=false'||chr(13)||chr(10)||'mandatory=false'||chr(13)||chr(10)||'editable=false','type=range');
INSERT INTO "fieldmetainfo" VALUES(DEFAULT,'_attachRespDoc','IS_ATTACH_RESPONSE_DOC','"AttachRespDoc"','java.lang.Boolean','com.gridnode.gtas.server.docalert.model.ResponseTrackRecord','responseTrackRecord.isAttachResponseDoc',0,0,0,0,0,'',999,'displayable=true'||chr(13)||chr(10)||'mandatory=false'||chr(13)||chr(10)||'editable=true','type=enum'||chr(13)||chr(10)||'generic.yes=true'||chr(13)||chr(10)||'generic.no=false');

---------- reminder_alert
DELETE FROM "fieldmetainfo" WHERE "EntityObjectName" LIKE '%ReminderAlert';
INSERT INTO "fieldmetainfo" VALUES(DEFAULT,'_uId','UID','"UID"','java.lang.Long','com.gridnode.gtas.server.docalert.model.ReminderAlert','reminderAlert.uid',0,0,0,0,0,'',999,'displayable=false'||chr(13)||chr(10)||'mandatory=false'||chr(13)||chr(10)||'editable=false','type=uid');
INSERT INTO "fieldmetainfo" VALUES(DEFAULT,'_trackRecordUID','TRACK_RECORD_UID','"TrackRecordUID"','java.lang.Long','com.gridnode.gtas.server.docalert.model.ReminderAlert','reminderAlert.trackRecordUid',20,0,1,0,1,'',999,'displayable=false'||chr(13)||chr(10)||'mandatory=false'||chr(13)||chr(10)||'editable=false'||chr(13)||chr(10),'type=foreign'||chr(13)||chr(10)||'foreign.cached=false'||chr(13)||chr(10)||'foreign.key=responseTrackRecord.uid'||chr(13)||chr(10)||'foreign.display=responseTrackRecord.sentDocType');
INSERT INTO "fieldmetainfo" VALUES(DEFAULT,'_daysToReminder','DAYS_TO_REMINDER','"DaysToReminder"','java.lang.Integer','com.gridnode.gtas.server.docalert.model.ReminderAlert','reminderAlert.daysToReminder',3,0,1,1,1,'',999,'displayable=true'||chr(13)||chr(10)||'mandatory=true'||chr(13)||chr(10)||'editable=false'||chr(13)||chr(10)||'editable.create=true','type=range'||chr(13)||chr(10)||'range.min=1'||chr(13)||chr(10)||'range.max=100');
INSERT INTO "fieldmetainfo" VALUES(DEFAULT,'_alertToRaise','ALERT_TO_RAISE','"AlertToRaise"','java.lang.String','com.gridnode.gtas.server.docalert.model.ReminderAlert','reminderAlert.alertToRaise',20,0,1,1,1,'',999,'displayable=true'||chr(13)||chr(10)||'mandatory=true'||chr(13)||chr(10)||'editable=true','type=foreign'||chr(13)||chr(10)||'foreign.cached=false'||chr(13)||chr(10)||'foreign.key=alert.name'||chr(13)||chr(10)||'foreign.display=alert.description');
INSERT INTO "fieldmetainfo" VALUES(DEFAULT,'_docRecipientXpath','DOC_RECPT_XPATH','"DocRecipientXpath"','java.lang.String','com.gridnode.gtas.server.docalert.model.ReminderAlert','reminderAlert.docRecipientXpath',255,0,0,1,1,'',999,'displayable=true'||chr(13)||chr(10)||'mandatory=false'||chr(13)||chr(10)||'editable=true','type=text'||chr(13)||chr(10)||'text.length.max=255');
INSERT INTO "fieldmetainfo" VALUES(DEFAULT,'_docSenderXpath','DOC_SENDER_XPATH','"DocSenderXpath"','java.lang.String','com.gridnode.gtas.server.docalert.model.ReminderAlert','reminderAlert.docSenderXpath',255,0,0,1,1,'',999,'displayable=true'||chr(13)||chr(10)||'mandatory=false'||chr(13)||chr(10)||'editable=true','type=text'||chr(13)||chr(10)||'text.length.max=255');
INSERT INTO "fieldmetainfo" VALUES(DEFAULT,'_version','VERSION','"Version"','java.lang.Double','com.gridnode.gtas.server.docalert.model.ReminderAlert','',0,0,0,0,0,'',999,'displayable=false'||chr(13)||chr(10)||'mandatory=false'||chr(13)||chr(10)||'editable=false','type=range');

---------- active_track_record
DELETE FROM "fieldmetainfo" WHERE "EntityObjectName" LIKE '%ActiveTrackRecord';
INSERT INTO "fieldmetainfo" VALUES(DEFAULT,'_uId','UID','"UID"','java.lang.Long','com.gridnode.gtas.server.docalert.model.ActiveTrackRecord','',0,0,0,0,0,'',999,'','');
INSERT INTO "fieldmetainfo" VALUES(DEFAULT,'_sentGridDocUID','SENT_GRIDDOC_UID','"SentGridDocUID"','java.lang.Long','com.gridnode.gtas.server.docalert.model.ActiveTrackRecord','',20,0,1,0,1,'',999,'','');
INSERT INTO "fieldmetainfo" VALUES(DEFAULT,'_trackRecordUID','TRACK_RECORD_UID','"TrackRecordUID"','java.lang.Long','com.gridnode.gtas.server.docalert.model.ActiveTrackRecord','',20,0,0,0,1,'',999,'','');
INSERT INTO "fieldmetainfo" VALUES(DEFAULT,'_daysToReminder','DAYS_TO_REMINDER','"DaysToReminder"','java.lang.Integer','com.gridnode.gtas.server.docalert.model.ActiveTrackRecord','',3,0,0,0,1,'',999,'','');
INSERT INTO "fieldmetainfo" VALUES(DEFAULT,'_alarmUID','ALARM_UID','"AlarmUID"','java.lang.Long','com.gridnode.gtas.server.docalert.model.ActiveTrackRecord','',20,0,0,0,1,'',999,'','');


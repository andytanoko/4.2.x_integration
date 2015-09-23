#04 Jan 2006  I1 v4.0    [Tam Wei Xiang]   Support JMS Alert Notification: 
#                                          Add JmsDestination, MessageProperty and JmsMessageRecord
#                                          Add link to JmsDestination and MessageProperty in MessageTemplate  
# 17 Feb 2006 I1 v4.0    [Neo Sok Lay]     Link JmsMessageRecord to JmsDestination.uid instead of Name                             

USE appdb;

# Entity metainfo
DELETE FROM entitymetainfo WHERE EntityName IN
("JmsDestination","MessageProperty", "JmsMessageRecord");
INSERT INTO entitymetainfo VALUES("com.gridnode.pdip.app.alert.model.JmsDestination","JmsDestination","jms_destination");
INSERT INTO entitymetainfo VALUES("com.gridnode.pdip.app.alert.model.MessageProperty","MessageProperty","");
INSERT INTO entitymetainfo VALUES("com.gridnode.pdip.app.alert.model.JmsMessageRecord","JmsMessageRecord","jms_msg_record");


# metainfo for JmsDestination
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


# MessageTemplate: add in JMS_DESTINATION and MESSAGE_PROPERTIES
DELETE FROM fieldmetainfo 
WHERE FieldName in ('JMS_DESTINATION','MESSAGE_PROPERTIES')
AND EntityObjectName='com.gridnode.pdip.app.alert.model.MessageTemplate';

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

# to add in new message template type
UPDATE fieldmetainfo SET Constraints = "type=enum\r\nmessageTemplate.messageType.email=EMail\r\nmessageTemplate.messageType.log=Log\r\nmessageTemplate.messageType.alertList=AlertList\r\nmessageTemplate.messageType.JMS=JMS"
WHERE ObjectName='_messageType' AND EntityObjectName='com.gridnode.pdip.app.alert.model.MessageTemplate';


USE userdb;
ALTER TABLE message_template
ADD COLUMN JmsDestination bigint(20) default NULL,
ADD COLUMN MessageProperties mediumtext collate utf8_unicode_ci;

DROP TABLE  IF EXISTS jms_destination;
CREATE TABLE IF NOT EXISTS jms_destination (
  `UID` bigint(20) NOT NULL,
  `Version` double NOT NULL default '1',
  `CanDelete` tinyint(1) NOT NULL default '1',
  `Name` varchar(30) collate utf8_unicode_ci default NULL,
  `Type` int(1) default '1',
  `JndiName` varchar(255) collate utf8_unicode_ci default NULL,
  `DeliveryMode` int(1) default '0',
  `Priority` int(2) default '-1',
  `ConnectionFactoryJndi` varchar(255) collate utf8_unicode_ci default NULL,
  `ConnectionUser` varchar(30) collate utf8_unicode_ci default NULL,
  `ConnectionPassword` varchar(30) collate utf8_unicode_ci default NULL,
  `LookupProperties` mediumtext collate utf8_unicode_ci,
  `RetryInterval` int(11) default NULL,
  `MaximumRetries` int(11) default NULL,
   UNIQUE KEY `Name` (`Name`),
   PRIMARY KEY  (`UID`)
);

DROP TABLE  IF EXISTS jms_msg_record;
CREATE TABLE `jms_msg_record` (
  `UID` bigint(20) NOT NULL,
  `Version` double NOT NULL default '1',
  `CanDelete` tinyint(1) NOT NULL default '1',
  `JmsDestUid` bigint(20) NOT NULL,
  `AlertTimestamp` datetime default NULL,
  `MsgData` mediumtext collate utf8_unicode_ci,
  `PermanentFailed` tinyint(1) default '0',
  `AlertTimeInLong` bigint(20) default NULL,
  PRIMARY KEY  (`UID`)
);

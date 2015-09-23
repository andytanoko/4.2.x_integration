# 040628 GT2.4.1 [Neo Sok Lay] Remove AlertList message type, HTML content type
# 060106 GT4.0   [Tam Wei Xiang] Added new messageType 'JMS'

USE appdb;

UPDATE fieldmetainfo
SET Constraints="type=enum\r\nmessageTemplate.messageType.email=EMail\r\nmessageTemplate.messageType.log=Log\r\nmessageTemplate.messageType.JMS=JMS"
WHERE FieldName="MESSAGETYPE"
AND EntityObjectName="com.gridnode.pdip.app.alert.model.MessageTemplate";

UPDATE fieldmetainfo
SET Constraints="type=enum\r\nmessageTemplate.contentType.text=Text"
WHERE FieldName="CONTENTTYPE"
AND EntityObjectName="com.gridnode.pdip.app.alert.model.MessageTemplate";

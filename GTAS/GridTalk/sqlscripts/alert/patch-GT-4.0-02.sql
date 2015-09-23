# 060106 GT4.0   [Tam Wei Xiang] Added new messageType 'JMS'
#                                Change default message template for PROCESS_INSTANCE_FAILURE_EMAIL to include new error types
# 08 Feb 2006 GT4.0 [Neo Sok Lay] Add ErrorType 8 for PROCESS_INSTANCE_FAILURE_EMAIL
USE appdb;

UPDATE fieldmetainfo
SET Constraints="type=enum\r\nmessageTemplate.messageType.email=EMail\r\nmessageTemplate.messageType.log=Log\r\nmessageTemplate.messageType.JMS=JMS"
WHERE FieldName="MESSAGETYPE"
AND EntityObjectName="com.gridnode.pdip.app.alert.model.MessageTemplate";


USE userdb;

UPDATE message_template
SET Message=REPLACE(Message,'[5] RosettaNet Exception Message is received in Inbound folder\r\n\r\n','[5] RosettaNet Exception Message is received in Inbound folder\r\n           [6] Failure Notification PIP is received from Partner\r\n           [7] Failure Notification PIP is initiated (See Reason)\r\n           [8] User has cancelled the process\r\n\r\n')
WHERE UID=-6;



# 20 Jun 2003 I1 v2.1  [Neo Sok Lay] Add DOCUMENT_STATUS_UPDATE alert type and default alert.
#                                    Add default alert trigger for PARTNER_CONTACTABLE


USE userdb;

INSERT INTO alert_trigger (UID, Level, AlertType, AlertUID, DocType, PartnerType, PartnerGroup, PartnerId, Recipients, Enabled, AttachDoc, CanDelete, Version) VALUES (-16,0,'PARTNER_UNCONTACTABLE',-15,NULL,NULL,NULL,NULL,';',1,0,0,1);

INSERT INTO alert_type (UID, Name, Description, Version, CanDelete) VALUES (19,'DOCUMENT_STATUS_UPDATE','Document Status Update',1,0);
INSERT INTO alert_trigger (UID, Level, AlertType, AlertUID, DocType, PartnerType, PartnerGroup, PartnerId, Recipients, Enabled, AttachDoc, CanDelete, Version) VALUES (-17,0,'DOCUMENT_STATUS_UPDATE',-16,NULL,NULL,NULL,NULL,';',0,0,0,1);
INSERT INTO action (UID, Name, Description, MsgUid, Version, CanDelete) VALUES (-22,'DOC_STATUS_UPDATE_LOG','Document Status Update Log Action',-22,1,0);
INSERT INTO alert (UID, Name, AlertType, Category, Trigger, Description, Version, CanDelete) VALUES (-16,'DOC_STATUS_UPDATE',19,NULL,NULL,'Default Document Status Update Alert',1,0);
INSERT INTO alert_action (UID, AlertUid, ActionUid) VALUES (-23,-16,-22);
INSERT INTO message_template (UID, Name, ContentType, MessageType, FromAddr, ToAddr, CcAddr, Subject, Message, Location, Append, Version, CanDelete) VALUES (-22,'DOC_STATUS_UPDATE_LOG',NULL,'Log',NULL,NULL,NULL,NULL,'<?xml version=\"1.0\" encoding=\"UTF-8\"?>\r\n<GridDocument>\r\n    <GdocId><%GridDocument.G_DOC_ID%></GdocId>\r\n    <UniqueDocIdentifier><%GridDocument.UNIQUE_DOC_IDENTIFIER%></UniqueDocIdentifier>\r\n    <isViewed><%GridDocument.IS_VIEWED%></isViewed>\r\n    <isSent><%GridDocument.IS_SENT%></isSent>\r\n    <isExpired><%GridDocument.IS_EXPIRED%></isExpired>\r\n    <isRejected><%GridDocument.IS_REJECTED%></isRejected><%?NN?GridDocument.DT_SEND_END#\'\r\n    <DateTimeSendEnd>\'yyyy-MM-dd\'T\'HH:mm:ss.SSS\'</DateTimeSendEnd>\'%><%?NN?GridDocument.DT_SEND_START#\'\r\n    <DateTimeSendStart>\'yyyy-MM-dd\'T\'HH:mm:ss.SSS\'</DateTimeSendStart>\'%><%?NN?GridDocument.DT_TRANSACTION_COMPLETE#\'\r\n    <DateTimeTransComplete>\'yyyy-MM-dd\'T\'HH:mm:ss.SSS\'</DateTimeTransComplete>\'%>\r\n</GridDocument>','docStatus/<%GridDocument.FOLDER%>-<%GridDocument.G_DOC_ID%>-<%GridDocument.UNIQUE_DOC_IDENTIFIER%>.xml',0,1,0);





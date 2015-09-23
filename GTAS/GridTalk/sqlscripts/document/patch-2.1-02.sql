# 22 May 2003 I1 v2.1 [Koh Han Sing] Add in new fields for Maxtor Project.
# 

use appdb;

UPDATE fieldmetainfo SET Presentation= 'displayable=false\r\nmandatory=false\r\neditable=false' WHERE LABEL='gridDocument.isViewAckReq';
UPDATE fieldmetainfo SET Presentation= 'displayable=false\r\nmandatory=false\r\neditable=false' WHERE LABEL='gridDocument.isExportAckReq';
UPDATE fieldmetainfo SET Presentation= 'displayable=false\r\nmandatory=false\r\neditable=false' WHERE LABEL='gridDocument.isReceiveAckReq';
UPDATE fieldmetainfo SET Presentation= 'displayable=false\r\nmandatory=false\r\neditable=false' WHERE LABEL='gridDocument.isLocalPending';
UPDATE fieldmetainfo SET Presentation= 'displayable=false\r\nmandatory=false\r\neditable=false' WHERE LABEL='gridDocument.isExpired';
UPDATE fieldmetainfo SET Presentation= 'displayable=false\r\nmandatory=false\r\neditable=false' WHERE LABEL='gridDocument.isRecAckProcessed';
UPDATE fieldmetainfo SET Presentation= 'displayable=false\r\nmandatory=false\r\neditable=false' WHERE LABEL='gridDocument.createBy';
UPDATE fieldmetainfo SET Presentation= 'displayable=false\r\nmandatory=false\r\neditable=false' WHERE LABEL='gridDocument.recipientPartnerFunction';
UPDATE fieldmetainfo SET Presentation= 'displayable=false\r\nmandatory=false\r\neditable=false' WHERE LABEL='gridDocument.dateTimeCreate';
UPDATE fieldmetainfo SET Presentation= 'displayable=false\r\nmandatory=false\r\neditable=false' WHERE LABEL='gridDocument.dateTimeReceiveStart';
UPDATE fieldmetainfo SET Presentation= 'displayable=false\r\nmandatory=false\r\neditable=false' WHERE LABEL='gridDocument.dateTimeSendStart';
UPDATE fieldmetainfo SET Presentation= 'displayable=false\r\nmandatory=false\r\neditable=false' WHERE LABEL='gridDocument.dateTimeRecipientView';
UPDATE fieldmetainfo SET Presentation= 'displayable=false\r\nmandatory=false\r\neditable=false' WHERE LABEL='gridDocument.dateTimeRecipientExport';
UPDATE fieldmetainfo SET Presentation= 'displayable=false\r\nmandatory=false\r\neditable=false' WHERE LABEL='gridDocument.portUid';
UPDATE fieldmetainfo SET Presentation= 'displayable=false\r\nmandatory=false\r\neditable=false' WHERE LABEL='gridDocument.actionCode';
UPDATE fieldmetainfo SET Presentation= 'displayable=false\r\nmandatory=false\r\neditable=false' WHERE LABEL='gridDocument.notifyUserEmail';
UPDATE fieldmetainfo SET Presentation= 'displayable=false\r\nmandatory=false\r\neditable=false' WHERE LABEL='gridDocument.recipientChannelUid';
UPDATE fieldmetainfo SET Presentation= 'displayable=false\r\nmandatory=false\r\neditable=false' WHERE LABEL='gridDocument.recipientChannelProtocol';
UPDATE fieldmetainfo SET Presentation= 'displayable=false\r\nmandatory=false\r\neditable=false' WHERE LABEL='gridDocument.rnProfileUid';
UPDATE fieldmetainfo SET Presentation= 'displayable=false\r\nmandatory=false\r\neditable=false' WHERE LABEL='gridDocument.isRequest';
UPDATE fieldmetainfo SET Presentation= 'displayable=false\r\nmandatory=false\r\neditable=false' WHERE LABEL='gridDocument.isAttachmentLinkUpdated';
UPDATE fieldmetainfo SET Presentation= 'displayable=false\r\nmandatory=false\r\neditable=false' WHERE LABEL='gridDocument.attachments';
UPDATE fieldmetainfo SET Presentation= 'displayable=false\r\nmandatory=false\r\neditable=false' WHERE LABEL='gridDocument.triggerType';

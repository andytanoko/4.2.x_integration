# 08 Feb 2006 GT 4.0 [Neo Sok Lay] Increase PartnerFunction id column size to 30

USE userdb;

ALTER TABLE grid_document
MODIFY RecipientPartnerFunction varchar(30);

USE appdb;

UPDATE fieldmetainfo
SET Length=30,
    Constraints='type=text\r\ntext.length.max=30'
WHERE Label IN ('gridDocument.partnerFunction','gridDocument.senderPartnerFunction','gridDocument.recipientPartnerFunction')
;

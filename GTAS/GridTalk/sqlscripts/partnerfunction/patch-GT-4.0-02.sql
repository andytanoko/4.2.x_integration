# 08 Feb 2006 GT4.0   [Neo Sok Lay] Increase PartnerFunctionId column to 30 chars.

USE userdb;

ALTER TABLE partner_function
MODIFY PartnerFunctionId varchar(30) NOT NULL DEFAULT '';

USE appdb;
UPDATE fieldmetainfo
SET Length=30,
    Constraints='type=text\r\ntext.length.max=30'
WHERE Label='partnerFunction.id'
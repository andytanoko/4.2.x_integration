# 08 Feb 2006 GT 4.0 [Neo Sok Lay] Increase PartnerFunctionId column size to 30 chars.

USE userdb;

ALTER TABLE pf_trigger
MODIFY PartnerFunctionId varchar(30) NOT NULL DEFAULT '';


USE appdb;

UPDATE fieldmetainfo
SET Length=30
WHERE Label='trigger.partnerFunctionId'
;
# 05 Dec 2003 GT 2.2 [Neo Sok Lay] Limit GridNode Name input length to 64 char, upper limit for OrganizationName by RDNAttributes

USE appdb;

UPDATE fieldmetainfo
SET Constraints='type=text\r\ntext.length.max=64\r\n'
WHERE Label='registrationInfo.gridnodeName'
;

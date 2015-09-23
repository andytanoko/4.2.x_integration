# 04 Dec 2003 GT 2.2 [Neo Sok Lay] Display certificate.id in addition to certificate.name in SecurityInfo to avoid confusion

USE appdb;

UPDATE fieldmetainfo
SET Constraints='type=foreign\r\nforeign.key=certificate.uid\r\nforeign.display=certificate.name\r\nforeign.cached=false\r\nforeign.additionalDisplay=id'
WHERE FieldName IN ("SIGNATURE_ENCRYPTION_CERTIFICATE_ID","ENCRYPTION_CERTIFICATE_ID")
AND EntityObjectName='com.gridnode.pdip.app.channel.model.SecurityInfo'
;


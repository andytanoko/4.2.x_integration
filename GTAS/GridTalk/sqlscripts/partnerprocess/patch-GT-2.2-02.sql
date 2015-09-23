# 04 Dec 2003 GT 2.2 [Neo Sok Lay] Display certificate.id in addition to certificate.name in BizCertMapping to avoid confusion

USE appdb;

UPDATE fieldmetainfo 
SET Constraints="type=foreign\r\nforeign.key=certificate.uid\r\nforeign.cached=true\r\nforeign.display=certificate.name\r\nforeign.additionalDisplay=id"
WHERE FieldName IN ("PARTNER_CERT","OWN_CERT")
AND EntityObjectName="com.gridnode.gtas.server.partnerprocess.model.BizCertMapping"
;


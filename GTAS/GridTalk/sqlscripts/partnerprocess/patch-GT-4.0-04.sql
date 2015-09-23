# 05 Dec 2006 GT 4.0 [Neo Sok Lay] Increase document type field length to 30 chars.

USE userdb;

ALTER TABLE pf_trigger 
MODIFY DocType varchar(30) DEFAULT NULL;

ALTER TABLE process_mapping
MODIFY DocType varchar(30) DEFAULT NULL;


USE appdb;

UPDATE fieldmetainfo
SET Length=30
WHERE EntityObjectName='com.gridnode.gtas.server.partnerprocess.model.Trigger'
AND FieldName='DOC_TYPE';

UPDATE fieldmetainfo
SET Length=30
WHERE EntityObjectName='com.gridnode.gtas.server.partnerprocess.model.ProcessMapping'
AND FieldName='DOC_TYPE';


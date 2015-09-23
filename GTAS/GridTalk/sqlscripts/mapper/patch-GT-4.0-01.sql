# 05 Dec 2006 GT 4.0  [Neo Sok Lay] Increase SourceDocType, TargetDocType field length to 30 chars.

USE userdb;

ALTER TABLE gridtalk_mapping_rule
MODIFY SourceDocType varchar(30) NOT NULL DEFAULT '',
MODIFY TargetDocType varchar(30) NOT NULL DEFAULT '';



USE appdb;

UPDATE fieldmetainfo
SET Length=30
WHERE EntityObjectName='com.gridnode.gtas.server.mapper.model.GridTalkMappingRule'
AND FieldName IN ('SOURCE_DOC_TYPE','TARGET_DOC_TYPE');


# 05 Dec 2006 GT 4.0  [Neo Sok Lay] Increase SentDocType, ResponseDocType field length to 30 chars.


USE userdb;

ALTER TABLE response_track_record
MODIFY SentDocType varchar(30) NOT NULL DEFAULT '',
MODIFY ResponseDocType varchar(30) NOT NULL DEFAULT '';



USE appdb;

UPDATE fieldmetainfo
SET Length=30
WHERE EntityObjectName='com.gridnode.gtas.server.docalert.model.ResponseTrackRecord'
AND FieldName IN ('SENT_DOC_TYPE','RESPONSE_DOC_TYPE');


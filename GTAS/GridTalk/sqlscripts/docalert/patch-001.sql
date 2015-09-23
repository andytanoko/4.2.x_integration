# 30 Apr 2003 v2.1 I1 [Neo Sok Lay] Add AttachRespDoc field
# 

USE appdb;

INSERT INTO fieldmetainfo VALUES(
NULL,"_attachRespDoc","IS_ATTACH_RESPONSE_DOC","AttachRespDoc","java.lang.Boolean",
"com.gridnode.gtas.server.docalert.model.ResponseTrackRecord","responseTrackRecord.isAttachResponseDoc",
"0","0","0","0","0","","999",
"displayable=true\r\nmandatory=false\r\neditable=true",
"type=enum\r\ngeneric.yes=true\r\ngeneric.no=false"
);


USE userdb;

ALTER TABLE response_track_record
ADD COLUMN AttachRespDoc tinyint(1) NOT NULL DEFAULT '1';


# 20 Jun 2003 I1 v2.1 [Koh Han Sing] Add in new field IsRejected.
# 24 Jun 2003 I1 v2.1 [Neo Sok Lay] Change to displayable true for IS_EXPIRED, IS_REC_ACK_PROCESSED and DT_SEND_START

use appdb;

INSERT INTO fieldmetainfo VALUES(
NULL,"_isRejected","IS_REJECTED","Rejected","java.lang.Boolean",
"com.gridnode.gtas.server.document.model.GridDocument","gridDocument.isRejected",
"0","0","0","0","1","","999",
"displayable=true\r\nmandatory=false\r\neditable=false",
"type=enum\r\ngridDocument.isRejected.enabled=true\r\ngridDocument.isRejected.disabled=false"
);

UPDATE fieldmetainfo
SET Presentation='displayable=true\r\nmandatory=false\r\neditable=false'
WHERE FieldName IN ('IS_EXPIRED','IS_REC_ACK_PROCESSED','DT_SEND_START')
AND EntityObjectName='com.gridnode.gtas.server.document.model.GridDocument';

use userdb;

alter table grid_document add column Rejected tinyint(1) DEFAULT '0';

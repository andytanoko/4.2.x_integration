# Oct 23 2003 GT2.3 [Guo Jianyu] Add hasDocTransFailed field in GridDocument.

use appdb;

INSERT INTO fieldmetainfo VALUES(
NULL,"_hasDocTransFailed","HAS_DOC_TRANS_FAILED","HasDocTransFailed","java.lang.Boolean",
"com.gridnode.gtas.server.document.model.GridDocument","gridDocument.hasDocTransFailed",
"0","0","0","0","1","","999",
"displayable=true\r\nmandatory=false\r\neditable=false",
"type=enum\r\ngridDocument.hasDocTransFailed.enabled=true\r\ngridDocument.hasDocTransFailed.disabled=false"
);

use userdb;

alter table grid_document add column HasDocTransFailed tinyint(1) default '0';

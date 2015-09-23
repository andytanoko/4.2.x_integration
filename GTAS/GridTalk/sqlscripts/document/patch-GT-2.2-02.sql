# 09 Jul 2003 I1 v2.2 [Koh Han Sing] Add in 10 custom fields.

use appdb;

INSERT INTO fieldmetainfo VALUES(
NULL,"_custom1","CUSTOM1","Custom1","java.lang.String",
"com.gridnode.gtas.server.document.model.GridDocument","gridDocument.custom1",
"255","0","0","1","1","","999",
"displayable=true\r\nmandatory=false\r\neditable=true",
"type=text\r\ntext.length.max=255"
);
INSERT INTO fieldmetainfo VALUES(
NULL,"_custom2","CUSTOM2","Custom2","java.lang.String",
"com.gridnode.gtas.server.document.model.GridDocument","gridDocument.custom2",
"255","0","0","1","1","","999",
"displayable=true\r\nmandatory=false\r\neditable=true",
"type=text\r\ntext.length.max=255"
);
INSERT INTO fieldmetainfo VALUES(
NULL,"_custom3","CUSTOM3","Custom3","java.lang.String",
"com.gridnode.gtas.server.document.model.GridDocument","gridDocument.custom3",
"255","0","0","1","1","","999",
"displayable=true\r\nmandatory=false\r\neditable=true",
"type=text\r\ntext.length.max=255"
);
INSERT INTO fieldmetainfo VALUES(
NULL,"_custom4","CUSTOM4","Custom4","java.lang.String",
"com.gridnode.gtas.server.document.model.GridDocument","gridDocument.custom4",
"255","0","0","1","1","","999",
"displayable=true\r\nmandatory=false\r\neditable=true",
"type=text\r\ntext.length.max=255"
);
INSERT INTO fieldmetainfo VALUES(
NULL,"_custom5","CUSTOM5","Custom5","java.lang.String",
"com.gridnode.gtas.server.document.model.GridDocument","gridDocument.custom5",
"255","0","0","1","1","","999",
"displayable=true\r\nmandatory=false\r\neditable=true",
"type=text\r\ntext.length.max=255"
);
INSERT INTO fieldmetainfo VALUES(
NULL,"_custom6","CUSTOM6","Custom6","java.lang.String",
"com.gridnode.gtas.server.document.model.GridDocument","gridDocument.custom6",
"255","0","0","1","1","","999",
"displayable=true\r\nmandatory=false\r\neditable=true",
"type=text\r\ntext.length.max=255"
);
INSERT INTO fieldmetainfo VALUES(
NULL,"_custom7","CUSTOM7","Custom7","java.lang.String",
"com.gridnode.gtas.server.document.model.GridDocument","gridDocument.custom7",
"255","0","0","1","1","","999",
"displayable=true\r\nmandatory=false\r\neditable=true",
"type=text\r\ntext.length.max=255"
);
INSERT INTO fieldmetainfo VALUES(
NULL,"_custom8","CUSTOM8","Custom8","java.lang.String",
"com.gridnode.gtas.server.document.model.GridDocument","gridDocument.custom8",
"255","0","0","1","1","","999",
"displayable=true\r\nmandatory=false\r\neditable=true",
"type=text\r\ntext.length.max=255"
);
INSERT INTO fieldmetainfo VALUES(
NULL,"_custom9","CUSTOM9","Custom9","java.lang.String",
"com.gridnode.gtas.server.document.model.GridDocument","gridDocument.custom9",
"255","0","0","1","1","","999",
"displayable=true\r\nmandatory=false\r\neditable=true",
"type=text\r\ntext.length.max=255"
);
INSERT INTO fieldmetainfo VALUES(
NULL,"_custom10","CUSTOM10","Custom10","java.lang.String",
"com.gridnode.gtas.server.document.model.GridDocument","gridDocument.custom10",
"255","0","0","1","1","","999",
"displayable=true\r\nmandatory=false\r\neditable=true",
"type=text\r\ntext.length.max=255"
);


use userdb;

alter table grid_document add column Custom1 varchar(255);
alter table grid_document add column Custom2 varchar(255);
alter table grid_document add column Custom3 varchar(255);
alter table grid_document add column Custom4 varchar(255);
alter table grid_document add column Custom5 varchar(255);
alter table grid_document add column Custom6 varchar(255);
alter table grid_document add column Custom7 varchar(255);
alter table grid_document add column Custom8 varchar(255);
alter table grid_document add column Custom9 varchar(255);
alter table grid_document add column Custom10 varchar(255);

# 22 May 2003 I1 v2.1 [Koh Han Sing] Add in new fields for Maxtor Project.
# 

use appdb;

INSERT INTO fieldmetainfo VALUES(
NULL,"_uniqueDocIdentifier","UNIQUE_DOC_IDENTIFIER","UniqueDocIdentifier","java.lang.String",
"com.gridnode.gtas.server.document.model.GridDocument","gridDocument.uniqueDocIdentifier",
"80","0","0","0","1","","999",
"displayable=true\r\nmandatory=false\r\neditable=false",
"type=text\r\ntext.length.max=80"
);
INSERT INTO fieldmetainfo VALUES(
NULL,"_udocFullPath","U_DOC_FULL_PATH","UdocFullPath","java.lang.String",
"com.gridnode.gtas.server.document.model.GridDocument","gridDocument.udocFullPath",
"255","0","0","0","1","","999",
"displayable=true\r\nmandatory=true\r\neditable=false",
"type=text\r\ntext.length.max=255"
);
INSERT INTO fieldmetainfo VALUES(
NULL,"_exportedUdocFullPath","EXPORTED_U_DOC_FULL_PATH","ExportedUdocFullPath","java.lang.String",
"com.gridnode.gtas.server.document.model.GridDocument","gridDocument.exportedUdocFullPath",
"255","0","0","0","1","","999",
"displayable=true\r\nmandatory=false\r\neditable=false",
"type=text\r\ntext.length.max=255"
);


use userdb;

alter table grid_document add column UniqueDocIdentifier varchar(80);
alter table grid_document add column UdocFullPath varchar(255) NOT NULL DEFAULT '';
alter table grid_document add column   ExportedUdocFullPath varchar(255);
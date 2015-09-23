# 18 DEC 2008    GT4.0.2.3    Wong Yee Wah   add table and metainfo for as2DocTypeMapping
#

USE userdb;
DROP TABLE IF EXISTS as2_doc_type_mapping; 
CREATE TABLE as2_doc_type_mapping (
	UID bigint(20) NOT NULL DEFAULT '0' ,
	AS2DocType varchar(30) NOT NULL DEFAULT '' ,
	DocType varchar(30) NOT NULL DEFAULT '' ,
    PartnerId varchar(15) NOT NULL DEFAULT '' ,
	CanDelete tinyint(1) DEFAULT '1' ,
	Version double DEFAULT '1' ,
	PRIMARY KEY (UID) ,
	UNIQUE KEY ID (AS2DocType, PartnerId)
);


# entitymetainfo for as2DocTypeMapping
DELETE FROM entitymetainfo WHERE EntityName = 'AS2DocTypeMapping';

INSERT INTO entitymetainfo VALUES ("com.gridnode.gtas.server.document.model.AS2DocTypeMapping","AS2DocTypeMapping","as2_doc_type_mapping");


# fieldmetainfo for as2DocTypeMapping
DELETE FROM fieldmetainfo WHERE EntityObjectName LIKE '%AS2DocTypeMapping';

INSERT INTO fieldmetainfo VALUES(
NULL,"_uId","UID","UID","java.lang.Long",
"com.gridnode.gtas.server.document.model.AS2DocTypeMapping","as2DocTypeMapping.uid",
"0","0","0","0","0","","999",
"displayable=false\r\nmandatory=false\r\neditable=false",
"type=uid"
);
INSERT INTO fieldmetainfo VALUES(
NULL,"_as2DocType","AS2_DOC_TYPE","AS2DocType","java.lang.String",
"com.gridnode.gtas.server.document.model.AS2DocTypeMapping","as2DocTypeMapping.as2DocType",
"30","0","0","1","1","","1",
"displayable=true\r\nmandatory=false\r\neditable=true\r\neditable.create=true",
"type=foreign\r\nforeign.key=documentType.docType\r\nforeign.display=document_type.docType\r\nforeign.cached=false"
);
INSERT INTO fieldmetainfo VALUES(
NULL,"_docType","DOC_TYPE","DocType","java.lang.String",
"com.gridnode.gtas.server.document.model.AS2DocTypeMapping","as2DocTypeMapping.docType",
"30","0","0","1","1","","1",
"displayable=true\r\nmandatory=false\r\neditable=true\r\neditable.create=true",
"type=foreign\r\nforeign.key=documentType.docType\r\nforeign.display=document_type.docType\r\nforeign.cached=false"
);
INSERT INTO fieldmetainfo VALUES(
NULL,"_partnerID","PARTNER_ID","PartnerId","java.lang.String",
"com.gridnode.gtas.server.document.model.AS2DocTypeMapping","as2DocTypeMapping.partnerId",
"20","0","0","1","1","","999",
"displayable=true\r\nmandatory=false\r\neditable=true\r\neditable.create=true",
"type=foreign\r\nforeign.key=partner.partnerId\r\nforeign.display=partner.name\r\nforeign.cached=false"
);
INSERT INTO fieldmetainfo VALUES(
NULL,"_canDelete","CAN_DELETE","CanDelete","java.lang.Boolean",
"com.gridnode.gtas.server.document.model.AS2DocTypeMapping","",
"0","0","1","0","0","","999",
"displayable=false\r\nmandatory=false\r\neditable=false",
"type=enum\r\ncandelete.enabled=true\r\ncandelete.disabled=false"
);
INSERT INTO fieldmetainfo VALUES(
NULL,"_version","VERSION","Version","java.lang.Double",
"com.gridnode.gtas.server.document.model.AS2DocTypeMapping","",
"0","0","0","0","0","","999",
"displayable=false\r\nmandatory=false\r\neditable=false",
"type=range"
);
SET client_encoding = 'UTF8';
SET check_function_bodies = false;
SET client_min_messages = warning;
SET search_path = userdb;

DROP TABLE IF EXISTS "as2_doc_type_mapping"; 
CREATE TABLE "as2_doc_type_mapping" (
	"UID" BIGINT DEFAULT 0 NOT NULL ,
	"AS2DocType" varchar(30) NOT NULL DEFAULT '' ,
	"DocType" varchar(30) NOT NULL DEFAULT '' ,
    "PartnerId" varchar(15) NOT NULL DEFAULT '' ,
    "CanDelete" DECIMAL(1) DEFAULT 1 NOT NULL ,
    "Version" DECIMAL(7,5) DEFAULT 1 NOT NULL,
	PRIMARY KEY ("UID") ,
	CONSTRAINT "AS2_DOC_TYPE_MAPPING_CON" UNIQUE ("AS2DocType", "PartnerId")
);
ALTER TABLE "as2_doc_type_mapping" OWNER TO userdb;

ALTER TABLE "grid_document" ALTER COLUMN "RecipientPartnerId" TYPE varchar(20);
ALTER TABLE "grid_document" ALTER COLUMN "SenderPartnerId" TYPE varchar(20);

DROP index IF EXISTS "GRID_DOCUMENT_IDX1";
DROP index IF EXISTS "GRID_DOCUMENT_IDX2";
DROP index IF EXISTS "GRID_DOCUMENT_IDX3";

CREATE INDEX "GRID_DOCUMENT_IDX1" ON "grid_document" ("DateTimeCreate", "Folder", "UdocDocType"); 
CREATE INDEX "GRID_DOCUMENT_IDX2" ON "grid_document" ("DateTimeCreate", "Folder", "UdocDocType", "SenderPartnerId", "RecipientPartnerId");
CREATE INDEX "GRID_DOCUMENT_IDX3" ON "grid_document" ("DateTimeCreate", "Folder", "UdocDocType", "SenderBizEntityId", "RecipientBizEntityId");


SET search_path = appdb;

-- entitymetainfo for as2DocTypeMapping
DELETE FROM "entitymetainfo" WHERE "EntityName" = 'AS2DocTypeMapping';
INSERT INTO "entitymetainfo" VALUES ('com.gridnode.gtas.server.document.model.AS2DocTypeMapping','AS2DocTypeMapping','as2_doc_type_mapping');


-- fieldmetainfo for as2DocTypeMapping
DELETE FROM "fieldmetainfo" WHERE "EntityObjectName" LIKE '%AS2DocTypeMapping';

INSERT INTO "fieldmetainfo" VALUES(
DEFAULT,'_uId','UID','"UID"','java.lang.Long',
'com.gridnode.gtas.server.document.model.AS2DocTypeMapping','as2DocTypeMapping.uid',
'0','0','0','0','0','','999',
'displayable=false'||chr(13)||chr(10)||'mandatory=false'||chr(13)||chr(10)||'editable=false',
'type=uid'
);

INSERT INTO "fieldmetainfo" VALUES(
DEFAULT,'_as2DocType','AS2_DOC_TYPE','"AS2DocType"','java.lang.String',
'com.gridnode.gtas.server.document.model.AS2DocTypeMapping','as2DocTypeMapping.as2DocType',
'30','0','0','1','1','','1',
'displayable=true'||chr(13)||chr(10)||'mandatory=false'||chr(13)||chr(10)||'editable=true'||chr(13)||chr(10)||'editable.create=true',
'type=foreign'||chr(13)||chr(10)||'foreign.key=documentType.docType'||chr(13)||chr(10)||'foreign.display=document_type.docType'||chr(13)||chr(10)||'foreign.cached=false'
);

INSERT INTO "fieldmetainfo" VALUES(
DEFAULT,'_docType','DOC_TYPE','"DocType"','java.lang.String',
'com.gridnode.gtas.server.document.model.AS2DocTypeMapping','as2DocTypeMapping.docType',
'30','0','0','1','1','','1',
'displayable=true'||chr(13)||chr(10)||'mandatory=false'||chr(13)||chr(10)||'editable=true'||chr(13)||chr(10)||'editable.create=true',
'type=foreign'||chr(13)||chr(10)||'foreign.key=documentType.docType'||chr(13)||chr(10)||'foreign.display=document_type.docType'||chr(13)||chr(10)||'foreign.cached=false'
);

INSERT INTO "fieldmetainfo" VALUES(
DEFAULT,'_partnerID','PARTNER_ID','"PartnerId"','java.lang.String',
'com.gridnode.gtas.server.document.model.AS2DocTypeMapping','as2DocTypeMapping.partnerId',
'20','0','0','1','1','','999',
'displayable=true'||chr(13)||chr(10)||'mandatory=false'||chr(13)||chr(10)||'editable=true'||chr(13)||chr(10)||'editable.create=true',
'type=foreign'||chr(13)||chr(10)||'foreign.key=partner.partnerId'||chr(13)||chr(10)||'foreign.display=partner.name'||chr(13)||chr(10)||'foreign.cached=false'
);

INSERT INTO "fieldmetainfo" VALUES(
DEFAULT,'_canDelete','CAN_DELETE','"CanDelete"','java.lang.Boolean',
'com.gridnode.gtas.server.document.model.AS2DocTypeMapping','',
'0','0','1','0','0','','999',
'displayable=false'||chr(13)||chr(10)||'mandatory=false'||chr(13)||chr(10)||'editable=false',
'type=enum'||chr(13)||chr(10)||'candelete.enabled=true'||chr(13)||chr(10)||'candelete.disabled=false'
);
INSERT INTO "fieldmetainfo" VALUES(
DEFAULT,'_version','VERSION','"Version"','java.lang.Double',
'com.gridnode.gtas.server.document.model.AS2DocTypeMapping','',
'0','0','0','0','0','','999',
'displayable=false'||chr(13)||chr(10)||'mandatory=false'||chr(13)||chr(10)||'editable=false',
'type=range'
);



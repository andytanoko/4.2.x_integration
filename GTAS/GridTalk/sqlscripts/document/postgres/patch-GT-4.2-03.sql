--[07 OCT 2010]   Tam Wei Xiang        MCI: retrieve files

SET client_encoding = 'UTF8';
SET check_function_bodies = false;
SET client_min_messages = warning;
SET search_path = appdb;

DELETE FROM "fieldmetainfo" WHERE "EntityObjectName"='com.gridnode.gtas.server.document.model.GridDocument' AND "FieldName"='IS_READ';
INSERT INTO "fieldmetainfo" VALUES(DEFAULT,'_isRead','IS_READ','"IsRead"','java.lang.Boolean','com.gridnode.gtas.server.document.model.GridDocument','gridDocument.isRead',0,0,0,0,1,'',999,'displayable=true'||chr(13)||chr(10)||'mandatory=false'||chr(13)||chr(10)||'editable=false','type=enum'||chr(13)||chr(10)||'gridDocument.isRead.enabled=true'||chr(13)||chr(10)||'gridDocument.isRead.disabled=false');

SET search_path = userdb;
ALTER TABLE "grid_document" ADD COLUMN "IsRead" boolean default FALSE;

DROP index IF EXISTS "GRID_DOCUMENT_IDX4";
CREATE INDEX "GRID_DOCUMENT_IDX4" ON "grid_document" ("Folder", "RecipientPartnerId", "IsRead");

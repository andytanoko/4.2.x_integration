SET client_encoding = 'UTF8';
SET check_function_bodies = false;
SET client_min_messages = warning;
SET search_path = userdb;

ALTER TABLE "search_query" ADD COLUMN "IsPublic" BOOLEAN DEFAULT FALSE;

SET client_encoding = 'UTF8';
SET check_function_bodies = false;
SET client_min_messages = warning;
SET search_path = appdb;
DELETE FROM "fieldmetainfo" WHERE "EntityObjectName"='com.gridnode.pdip.app.searchquery.model.SearchQuery' AND "ObjectName"='_isPublic';
INSERT INTO "fieldmetainfo" VALUES (DEFAULT,'_isPublic','IS_PUBLIC','"IsPublic"','java.lang.Boolean','com.gridnode.pdip.app.searchquery.model.SearchQuery', 'searchQuery.isPublic', 0,0,0,1,1,'', 999, 'displayable=true'||chr(13)||chr(10)||'editable=true'||chr(13)||chr(10)||'mandatory=false'||chr(13)||chr(10),'type=enum'||chr(13)||chr(10)||'searchQuery.isPublic.true=true'||chr(13)||chr(10)||'searchQuery.isPublic.false=false');

SET client_encoding = 'UTF8';
SET check_function_bodies = false;
SET client_min_messages = warning;
SET search_path = appdb;

DELETE from "entitymetainfo" WHERE "EntityName" IN ('SearchQuery', 'Condition');
INSERT INTO "entitymetainfo" VALUES('com.gridnode.pdip.app.searchquery.model.SearchQuery','SearchQuery','"search_query"');
INSERT INTO "entitymetainfo" VALUES('com.gridnode.pdip.app.searchquery.model.Condition','Condition','');

--------- SearchQuery
DELETE from "fieldmetainfo" WHERE "EntityObjectName" LIKE '%SearchQuery';
INSERT INTO "fieldmetainfo" VALUES(DEFAULT,'_uId','UID','"UID"','java.lang.Long','com.gridnode.pdip.app.searchquery.model.SearchQuery','searchQuery.uid',0,0,0,0,0,'',999,'displayable=false'||chr(13)||chr(10)||'mandatory=false'||chr(13)||chr(10)||'editable=false','type=uid');
INSERT INTO "fieldmetainfo" VALUES(DEFAULT,'_version','VERSION','"Version"','java.lang.Double','com.gridnode.pdip.app.searchquery.model.SearchQuery','',0,0,0,0,0,'',999,'displayable=false'||chr(13)||chr(10)||'mandatory=false'||chr(13)||chr(10)||'editable=false','type=range');
INSERT INTO "fieldmetainfo" VALUES(DEFAULT,'_canDelete','CAN_DELETE','"CanDelete"','java.lang.Boolean','com.gridnode.pdip.app.searchquery.model.SearchQuery','',0,0,0,0,0,'',999,'displayable=false'||chr(13)||chr(10)||'mandatory=false'||chr(13)||chr(10)||'editable=false','type=enum'||chr(13)||chr(10)||'candelete.enabled=true'||chr(13)||chr(10)||'candelete.disabled=false');
INSERT INTO "fieldmetainfo" VALUES(DEFAULT,'_name','NAME','"Name"','java.lang.String','com.gridnode.pdip.app.searchquery.model.SearchQuery','searchQuery.name',30,0,1,0,1,'',999,'displayable=true'||chr(13)||chr(10)||'mandatory=true'||chr(13)||chr(10)||'editable=false'||chr(13)||chr(10)||'editable.create=true','type=text'||chr(13)||chr(10)||'text.length.max=30');
INSERT INTO "fieldmetainfo" VALUES(DEFAULT,'_description','DESCRIPTION','"Description"','java.lang.String','com.gridnode.pdip.app.searchquery.model.SearchQuery','searchQuery.description',80,0,1,1,1,'',999,'displayable=true'||chr(13)||chr(10)||'mandatory=true'||chr(13)||chr(10)||'editable=true','type=text'||chr(13)||chr(10)||'text.length.max=80');
INSERT INTO "fieldmetainfo" VALUES(DEFAULT,'_createdBy','CREATED_BY','"CreatedBy"','java.lang.String','com.gridnode.pdip.app.searchquery.model.SearchQuery','searchQuery.createdBy',15,0,1,1,1,'',999,'displayable=true'||chr(13)||chr(10)||'mandatory=true'||chr(13)||chr(10)||'editable=false','type=text'||chr(13)||chr(10)||'text.length.max=15');
INSERT INTO "fieldmetainfo" VALUES(DEFAULT,'_conditions','CONDITIONS','"Conditions"','java.util.ArrayList','com.gridnode.pdip.app.searchquery.model.SearchQuery','searchQuery.conditions',0,0,1,1,1,'',999,'displayable=true'||chr(13)||chr(10)||'mandatory=true'||chr(13)||chr(10)||'editable=true','type=embedded'||chr(13)||chr(10)||'embedded.type=condition'||chr(13)||chr(10)||'collection=true');
INSERT INTO "fieldmetainfo" VALUES (DEFAULT,'_isPublic','IS_PUBLIC','"IsPublic"','java.lang.Boolean','com.gridnode.pdip.app.searchquery.model.SearchQuery', 'searchQuery.isPublic', 0,0,0,1,1,'', 999, 'displayable=true'||chr(13)||chr(10)||'editable=true'||chr(13)||chr(10)||'mandatory=false'||chr(13)||chr(10),'type=enum'||chr(13)||chr(10)||'searchQuery.isPublic.true=true'||chr(13)||chr(10)||'searchQuery.isPublic.false=false');

--------- condition
DELETE from "fieldmetainfo" WHERE "EntityObjectName" LIKE '%Condition';
INSERT INTO "fieldmetainfo" VALUES(DEFAULT,'_field','FIELD','','java.lang.Number','com.gridnode.pdip.app.searchquery.model.Condition','condition.field',0,0,1,0,1,'',999,'displayable=true'||chr(13)||chr(10)||'mandatory=true'||chr(13)||chr(10)||'editable=true','type=range');
INSERT INTO "fieldmetainfo" VALUES(DEFAULT,'_xpath','XPATH','','java.lang.String','com.gridnode.pdip.app.searchquery.model.Condition','condition.xpath',0,0,1,0,1,'',999,'displayable=true'||chr(13)||chr(10)||'mandatory=true'||chr(13)||chr(10)||'editable=true','type=text'||chr(13)||chr(10)||'text.length.max=255');
INSERT INTO "fieldmetainfo" VALUES(DEFAULT,'_operator','OPERATOR','','java.lang.Short','com.gridnode.pdip.app.searchquery.model.Condition','condition.operator',0,0,1,1,1,'',999,'displayable=true'||chr(13)||chr(10)||'mandatory=true'||chr(13)||chr(10)||'editable=true','type=enum'||chr(13)||chr(10)||'condition.operator.equal=0'||chr(13)||chr(10)||'condition.operator.notEqual=1'||chr(13)||chr(10)||'condition.operator.less=2'||chr(13)||chr(10)||'condition.operator.lessOrEqual=3'||chr(13)||chr(10)||'condition.operator.greater=4'||chr(13)||chr(10)||'condition.operator.greaterOrEqual=5'||chr(13)||chr(10)||'condition.operator.in=6'||chr(13)||chr(10)||'condition.operator.between=7'||chr(13)||chr(10)||'condition.operator.like=9'||chr(13)||chr(10));
INSERT INTO "fieldmetainfo" VALUES(DEFAULT,'_values','VALUES','','java.util.ArrayList','com.gridnode.pdip.app.searchquery.model.Condition','condition.values',15,0,1,1,1,'',999,'displayable=true'||chr(13)||chr(10)||'mandatory=true'||chr(13)||chr(10)||'editable=true','type=text'||chr(13)||chr(10)||'collection=true'||chr(13)||chr(10)||'collection.element=java.lang.Object');
INSERT INTO "fieldmetainfo" VALUES(DEFAULT,'_type','TYPE','','java.lang.Short','com.gridnode.pdip.app.searchquery.model.Condition','condition.type',0,0,1,1,1,'',999,'displayable=true'||chr(13)||chr(10)||'mandatory=true'||chr(13)||chr(10)||'editable=true','type=enum'||chr(13)||chr(10)||'condition.type.gdoc=1'||chr(13)||chr(10)||'condition.type.udoc=2');



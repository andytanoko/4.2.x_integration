set search_path = appdb;
SET client_encoding = 'UTF8';
SET check_function_bodies = false;
SET client_min_messages = warning;

delete from "fieldmetainfo" where
"FieldName" = 'TYPE' and "EntityObjectName" = 'com.gridnode.pdip.app.mapper.model.MappingFile';

INSERT INTO "fieldmetainfo" VALUES(DEFAULT,'_type','TYPE','"Type"','java.lang.Short','com.gridnode.pdip.app.mapper.model.MappingFile','mappingFile.type',1,0,1,1,1,'',999,'displayable=true'||chr(13)||chr(10)||'mandatory=true'||chr(13)||chr(10)||'editable=true','type=enum'||chr(13)||chr(10)||'mappingFile.type.xsl=0'||chr(13)||chr(10)||'mappingFile.type.conversionRule=1'||chr(13)||chr(10)||'mappingFile.type.referenceDoc=2'||chr(13)||chr(10)||'mappingFile.type.dtd=3'||chr(13)||chr(10)||'mappingFile.type.schema=4'||chr(13)||chr(10)||'mappingFile.type.dict=5'||chr(13)||chr(10)||'mappingFile.type.xpath=6'||chr(13)||chr(10)||'mappingFile.type.javaBinary=7');

delete from "fieldmetainfo" where "FieldName" = 'MAPPING_CLASS' and "EntityObjectName" = 'com.gridnode.pdip.app.mapper.model.MappingRule';
	
INSERT INTO "fieldmetainfo" VALUES(
DEFAULT,'_mappingClass','MAPPING_CLASS','"MappingClass"','java.lang.String',
'com.gridnode.pdip.app.mapper.model.MappingRule','mappingRule.mappingClass',
'255','0','1','1','1','','999',
'displayable=true',
'type=text'
);

set search_path = userdb;

ALTER TABLE "mapping_rule" ADD COLUMN "MappingClass" VARCHAR(255);



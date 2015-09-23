SET client_encoding = 'UTF8';
SET check_function_bodies = false;
SET client_min_messages = warning;
SET search_path = appdb;

--
-- Dumping data for table 'entitymetainfo'
--
-- Field 1 ObjectName: full qualified class name of the entity
-- Field 2 EntityName: short class name of the entity
-- Field 3 SqlName: table name of the entity

DELETE from "entitymetainfo" WHERE "EntityName" IN ('GridTalkMappingRule');
INSERT INTO "entitymetainfo" VALUES('com.gridnode.gtas.server.mapper.model.GridTalkMappingRule','GridTalkMappingRule','"gridtalk_mapping_rule"');

--
-- Dumping data for table 'fieldmetainfo'
--
-- UID bigint(20) NOT NULL auto_increment,
-- ObjectName: field name in Entity class ,
-- FieldName: field ID in Entity Interface class ,
-- SqlName: Field column name in Table,
-- ValueClass: field data type,
-- EntityObjectName: full qualified class name of the entity
-- Label: field display label
-- Length: valid field length ,
-- Proxy: '1' if proxy, '0' otherwise,,
-- Mandatory: '1' if mandatory, '0' otherwise,
-- Editable: '1' if editable, '0' otherwise
-- Displayable: '1' if displayable, '0' otherwise
-- OqlName: ,
-- Sequence: default '999' ,
-- Presentation: properties for presentation of the field
-- Constraints: constraint imposed on the values of the field
--

---------- "fieldmetainfo" for gridtalk_mapping_file
DELETE from "fieldmetainfo" WHERE "EntityObjectName" LIKE '%GridTalkMappingRule';
INSERT INTO "fieldmetainfo" VALUES(DEFAULT,'_uId','UID','"UID"','java.lang.Long','com.gridnode.gtas.server.mapper.model.GridTalkMappingRule','gridTalkMappingRule.uid',0,0,0,0,0,'',999,'displayable=false'||chr(13)||chr(10)||'mandatory=false'||chr(13)||chr(10)||'editable=false','type=uid');
INSERT INTO "fieldmetainfo" VALUES(DEFAULT,'_version','VERSION','"Version"','java.lang.Double','com.gridnode.gtas.server.mapper.model.GridTalkMappingRule','',0,0,0,0,0,'',999,'displayable=false'||chr(13)||chr(10)||'mandatory=false'||chr(13)||chr(10)||'editable=false','type=range');
INSERT INTO "fieldmetainfo" VALUES(DEFAULT,'_canDelete','CAN_DELETE','"CanDelete"','java.lang.Boolean','com.gridnode.gtas.server.mapper.model.GridTalkMappingRule','',0,0,0,0,0,'',999,'displayable=false'||chr(13)||chr(10)||'mandatory=false'||chr(13)||chr(10)||'editable=false','type=enum'||chr(13)||chr(10)||'candelete.enabled=true'||chr(13)||chr(10)||'candelete.disabled=false');
INSERT INTO "fieldmetainfo" VALUES(DEFAULT,'_name','NAME','"Name"','java.lang.String','com.gridnode.gtas.server.mapper.model.GridTalkMappingRule','gridTalkMappingRule.name',30,0,1,0,1,'',999,'displayable=true'||chr(13)||chr(10)||'mandatory=true'||chr(13)||chr(10)||'editable=false'||chr(13)||chr(10)||'editable.create=true','type=text'||chr(13)||chr(10)||'text.length.max=30');
INSERT INTO "fieldmetainfo" VALUES(DEFAULT,'_description','DESCRIPTION','"Description"','java.lang.String','com.gridnode.gtas.server.mapper.model.GridTalkMappingRule','gridTalkMappingRule.description',80,0,1,1,1,'',999,'displayable=true'||chr(13)||chr(10)||'mandatory=true'||chr(13)||chr(10)||'editable=true','type=text'||chr(13)||chr(10)||'text.length.max=80');
INSERT INTO "fieldmetainfo" VALUES(DEFAULT,'_sourceDocType','SOURCE_DOC_TYPE','"SourceDocType"','java.lang.String','com.gridnode.gtas.server.mapper.model.GridTalkMappingRule','gridTalkMappingRule.sourceDocType',30,0,1,1,1,'',999,'displayable=true'||chr(13)||chr(10)||'mandatory=false'||chr(13)||chr(10)||'editable=true','type=foreign'||chr(13)||chr(10)||'foreign.key=documentType.docType'||chr(13)||chr(10)||'foreign.display=documentType.docType'||chr(13)||chr(10)||'foreign.cached=false');
INSERT INTO "fieldmetainfo" VALUES(DEFAULT,'_targetDocType','TARGET_DOC_TYPE','"TargetDocType"','java.lang.String','com.gridnode.gtas.server.mapper.model.GridTalkMappingRule','gridTalkMappingRule.targetDocType',30,0,1,1,1,'',999,'displayable=true'||chr(13)||chr(10)||'mandatory=false'||chr(13)||chr(10)||'editable=true','type=foreign'||chr(13)||chr(10)||'foreign.key=documentType.docType'||chr(13)||chr(10)||'foreign.display=documentType.docType'||chr(13)||chr(10)||'foreign.cached=false');
INSERT INTO "fieldmetainfo" VALUES(DEFAULT,'_sourceDocFileType','SOURCE_DOC_FILE_TYPE','"SourceDocFileType"','java.lang.String','com.gridnode.gtas.server.mapper.model.GridTalkMappingRule','gridTalkMappingRule.sourceDocFileType',12,0,1,1,1,'',999,'displayable=true'||chr(13)||chr(10)||'mandatory=false'||chr(13)||chr(10)||'editable=true','type=foreign'||chr(13)||chr(10)||'foreign.key=fileType.fileType'||chr(13)||chr(10)||'foreign.display=fileType.fileType'||chr(13)||chr(10)||'foreign.cached=false');
INSERT INTO "fieldmetainfo" VALUES(DEFAULT,'_targetDocFileType','TARGET_DOC_FILE_TYPE','"TargetDocFileType"','java.lang.String','com.gridnode.gtas.server.mapper.model.GridTalkMappingRule','gridTalkMappingRule.targetDocFileType',12,0,1,1,1,'',999,'displayable=true'||chr(13)||chr(10)||'mandatory=false'||chr(13)||chr(10)||'editable=true','type=foreign'||chr(13)||chr(10)||'foreign.key=fileType.fileType'||chr(13)||chr(10)||'foreign.display=fileType.fileType'||chr(13)||chr(10)||'foreign.cached=false');
INSERT INTO "fieldmetainfo" VALUES(DEFAULT,'_headerTransformation','HEADER_TRANSFORMATION','"HeaderTransformation"','java.lang.Boolean','com.gridnode.gtas.server.mapper.model.GridTalkMappingRule','gridTalkMappingRule.headerTransformation',0,0,1,1,1,'',999,'displayable=true'||chr(13)||chr(10)||'mandatory=true'||chr(13)||chr(10)||'editable=true','type=enum'||chr(13)||chr(10)||'gridTalkMappingRule.headerTransformation.enabled=true'||chr(13)||chr(10)||'gridTalkMappingRule.headerTransformation.disabled=false');
INSERT INTO "fieldmetainfo" VALUES(DEFAULT,'_transformWithHeader','TRANSFORM_WITH_HEADER','"TransformWithHeader"','java.lang.Boolean','com.gridnode.gtas.server.mapper.model.GridTalkMappingRule','gridTalkMappingRule.transformWithHeader',0,0,1,1,1,'',999,'displayable=true'||chr(13)||chr(10)||'mandatory=true'||chr(13)||chr(10)||'editable=true','type=enum'||chr(13)||chr(10)||'gridTalkMappingRule.transformWithHeader.enabled=true'||chr(13)||chr(10)||'gridTalkMappingRule.transformWithHeader.disabled=false');
INSERT INTO "fieldmetainfo" VALUES(DEFAULT,'_transformWithSource','TRANSFORM_WITH_SOURCE','"TransformWithSource"','java.lang.Boolean','com.gridnode.gtas.server.mapper.model.GridTalkMappingRule','gridTalkMappingRule.transformWithSource',0,0,1,1,1,'',999,'displayable=true'||chr(13)||chr(10)||'mandatory=true'||chr(13)||chr(10)||'editable=true','type=enum'||chr(13)||chr(10)||'gridTalkMappingRule.transformWithSource.enabled=true'||chr(13)||chr(10)||'gridTalkMappingRule.transformWithSource.disabled=false');
INSERT INTO "fieldmetainfo" VALUES(DEFAULT,'_mappingRule','MAPPING_RULE','"MappingRuleUID"','com.gridnode.pdip.app.mapper.model.MappingRule','com.gridnode.gtas.server.mapper.model.GridTalkMappingRule','gridTalkMappingRule.mappingRule',0,0,1,1,1,'',999,'displayable=true'||chr(13)||chr(10)||'mandatory=true'||chr(13)||chr(10)||'editable=true','type=embedded'||chr(13)||chr(10)||'embedded.type=mappingRule');



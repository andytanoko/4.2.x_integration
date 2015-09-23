# Change Log
# 07 Mar 2006 GT 4.0 [Neo Sok Lay] Add metainfo for non-persistent entity SchemaMapping

use appdb;

#
# Dumping data for table 'entitymetainfo'
#
# Field 1 ObjectName: full qualified class name of the entity
# Field 2 EntityName: short class name of the entity
# Field 3 SqlName: table name of the entity

# entitymetainfo for mapping_file, mapping_rule
DELETE from entitymetainfo WHERE EntityName IN ('MappingFile','MappingRule','XpathMapping');
INSERT INTO entitymetainfo VALUES("com.gridnode.pdip.app.mapper.model.MappingFile","MappingFile","mapping_file");
INSERT INTO entitymetainfo VALUES("com.gridnode.pdip.app.mapper.model.MappingRule","MappingRule","mapping_rule");
INSERT INTO entitymetainfo VALUES("com.gridnode.pdip.app.mapper.model.XpathMapping","XpathMapping","xpath_mapping");

#
# Dumping data for table 'fieldmetainfo'
#
# UID bigint(20) NOT NULL auto_increment,
# ObjectName: field name in Entity class ,
# FieldName: field ID in Entity Interface class ,
# SqlName: Field column name in Table,
# ValueClass: field data type,
# EntityObjectName: full qualified class name of the entity
# Label: field display label
# Length: valid field length ,
# Proxy: '1' if proxy, '0' otherwise,,
# Mandatory: '1' if mandatory, '0' otherwise,
# Editable: '1' if editable, '0' otherwise
# Displayable: '1' if displayable, '0' otherwise
# OqlName: ,
# Sequence: default '999' ,
# Presentation: properties for presentation of the field
# Constraints: constraint imposed on the values of the field
#

# fieldmetainfo for mapping_file
DELETE from fieldmetainfo WHERE EntityObjectName LIKE '%MappingFile';

INSERT INTO fieldmetainfo VALUES(
NULL,"_uId","UID","UID","java.lang.Long",
"com.gridnode.pdip.app.mapper.model.MappingFile","mappingFile.uid",
"0","0","0","0","0","","999",
"displayable=false\r\nmandatory=false\r\neditable=false",
"type=uid"
);
INSERT INTO fieldmetainfo VALUES(
NULL,"_version","VERSION","Version","java.lang.Double",
"com.gridnode.pdip.app.mapper.model.MappingFile","",
"0","0","0","0","0","","999",
"displayable=false\r\nmandatory=false\r\neditable=false",
"type=range"
);
INSERT INTO fieldmetainfo VALUES(
NULL,"_canDelete","CAN_DELETE","CanDelete","java.lang.Boolean",
"com.gridnode.pdip.app.mapper.model.MappingFile","",
"0","0","0","0","0","","999",
"displayable=false\r\nmandatory=false\r\neditable=false",
"type=enum\r\ncandelete.enabled=true\r\ncandelete.disabled=false"
);
INSERT INTO fieldmetainfo VALUES(
NULL,"_name","NAME","Name","java.lang.String",
"com.gridnode.pdip.app.mapper.model.MappingFile","mappingFile.name",
"30","0","1","0","1","","999",
"displayable=true\r\nmandatory=true\r\neditable=false\r\neditable.create=true",
"type=text\r\ntext.length.max=30"
);
INSERT INTO fieldmetainfo VALUES(
NULL,"_description","DESCRIPTION","Description","java.lang.String",
"com.gridnode.pdip.app.mapper.model.MappingFile","mappingFile.description",
"80","0","1","1","1","","999",
"displayable=true\r\nmandatory=true\r\neditable=true",
"type=text\r\ntext.length.max=80"
);
INSERT INTO fieldmetainfo VALUES(
NULL,"_filename","FILENAME","Filename","java.lang.String",
"com.gridnode.pdip.app.mapper.model.MappingFile","mappingFile.filename",
"80","0","1","1","1","","999",
"displayable=true\r\nmandatory=true\r\neditable=true",
"type=file\r\nfile.downloadable=true\r\nfile.pathKey=mappingFile.path\r\nfile.subPath=subPath"
);
INSERT INTO fieldmetainfo VALUES(
NULL,"_path","PATH","Path","java.lang.String",
"com.gridnode.pdip.app.mapper.model.MappingFile","mappingFile.path",
"80","0","1","1","1","","999",
"displayable=false\r\nmandatory=false\r\neditable=false",
"type=file"
);
INSERT INTO fieldmetainfo VALUES(
NULL,"_subPath","SUB_PATH","SubPath","java.lang.String",
"com.gridnode.pdip.app.mapper.model.MappingFile","mappingFile.subPath",
"80","0","1","1","1","","999",
"displayable=true\r\nmandatory=false\r\neditable=true",
"type=text\r\ntext.length.max=80"
);
INSERT INTO fieldmetainfo VALUES(
NULL,"_type","TYPE","Type","java.lang.Short",
"com.gridnode.pdip.app.mapper.model.MappingFile","mappingFile.type",
"1","0","1","1","1","","999",
"displayable=true\r\nmandatory=true\r\neditable=true",
"type=enum\r\nmappingFile.type.xsl=0\r\nmappingFile.type.conversionRule=1\r\nmappingFile.type.referenceDoc=2\r\nmappingFile.type.dtd=3\r\nmappingFile.type.schema=4\r\nmappingFile.type.dict=5\r\nmappingFile.type.xpath=6"
);


# fieldmetainfo for mapping_rule
DELETE from fieldmetainfo WHERE EntityObjectName LIKE '%MappingRule';

INSERT INTO fieldmetainfo VALUES(
NULL,"_uId","UID","UID","java.lang.Long",
"com.gridnode.pdip.app.mapper.model.MappingRule","mappingRule.uid",
"0","0","0","0","0","","999",
"displayable=false\r\nmandatory=false\r\neditable=false",
"type=uid"
);
INSERT INTO fieldmetainfo VALUES(
NULL,"_version","VERSION","Version","java.lang.Double",
"com.gridnode.pdip.app.mapper.model.MappingRule","",
"0","0","0","0","0","","999",
"displayable=false\r\nmandatory=false\r\neditable=false",
"type=range"
);
INSERT INTO fieldmetainfo VALUES(
NULL,"_canDelete","CAN_DELETE","CanDelete","java.lang.Boolean",
"com.gridnode.pdip.app.mapper.model.MappingRule","",
"0","0","0","0","0","","999",
"displayable=false\r\nmandatory=false\r\neditable=false",
"type=enum\r\ncandelete.enabled=true\r\ncandelete.disabled=false"
);
INSERT INTO fieldmetainfo VALUES(
NULL,"_name","NAME","Name","java.lang.String",
"com.gridnode.pdip.app.mapper.model.MappingRule","MappingRule.name",
"30","0","1","0","1","","999",
"displayable=true\r\nmandatory=true\r\neditable=false\r\neditable.create=true",
"type=text\r\ntext.length.max=30"
);
INSERT INTO fieldmetainfo VALUES(
NULL,"_description","DESCRIPTION","Description","java.lang.String",
"com.gridnode.pdip.app.mapper.model.MappingRule","mappingRule.description",
"80","0","1","1","1","","999",
"displayable=true\r\nmandatory=true\r\neditable=true",
"type=text\r\ntext.length.max=80"
);
INSERT INTO fieldmetainfo VALUES(
NULL,"_type","TYPE","Type","java.lang.Short",
"com.gridnode.pdip.app.mapper.model.MappingRule","mappingRule.type",
"1","0","1","1","1","","999",
"displayable=true\r\nmandatory=true\r\neditable=true",
"type=enum\r\nmappingRule.type.convert=0\r\nmappingRule.type.transform=1\r\nmappingRule.type.split=2"
);
INSERT INTO fieldmetainfo VALUES(
NULL,"_mappingFile","MAPPING_FILE","MappingFileUID","com.gridnode.pdip.app.mapper.model.MappingFile",
"com.gridnode.pdip.app.mapper.model.MappingRule","mappingRule.mappingFile",
"0","0","0","1","1","","999",
"displayable=true\r\nmandatory=true\r\neditable=true",
"type=foreign\r\nforeign.key=mappingFile.uid\r\nforeign.display=mappingFile.name\r\nforeign.cached=true"
);
INSERT INTO fieldmetainfo VALUES(
NULL,"_transformRefDoc","TRANSFORM_REF_DOC","TransformRefDoc","java.lang.Boolean",
"com.gridnode.pdip.app.mapper.model.MappingRule","mappingRule.transformRefDoc",
"0","0","0","1","1","","999",
"displayable=true\r\nmandatory=true\r\neditable=true",
"type=enum\r\nmappingRule.transformRefDoc.enabled=true\r\nmappingRule.transformRefDoc.disabled=false"
);
INSERT INTO fieldmetainfo VALUES(
NULL,"_refDocUID","REFERENCE_DOC_UID","ReferenceDocUID","java.lang.Long",
"com.gridnode.pdip.app.mapper.model.MappingRule","mappingRule.refDocUid",
"0","0","1","1","1","","999",
"displayable=true\r\nmandatory=true\r\neditable=true",
"type=foreign\r\nforeign.key=mappingFile.uid\r\nforeign.display=mappingFile.name\r\nforeign.cached=false"
);
INSERT INTO fieldmetainfo VALUES(
NULL,"_xpath","XPATH","XPath","java.lang.String",
"com.gridnode.pdip.app.mapper.model.MappingRule","mappingRule.xpath",
"1024","0","1","1","1","","999",
"displayable=true\r\nmandatory=true\r\neditable=true",
"type=text\r\ntext.length.max=1024"
);
INSERT INTO fieldmetainfo VALUES(
NULL,"_paramName","PARAM_NAME","ParamName","java.lang.String",
"com.gridnode.pdip.app.mapper.model.MappingRule","mappingRule.paramName",
"40","0","1","1","1","","999",
"displayable=true\r\nmandatory=true\r\neditable=true",
"type=text\r\ntext.length.max=40"
);
INSERT INTO fieldmetainfo VALUES(
NULL,"_keepOriginal","KEEP_ORIGINAL","KeepOriginal","java.lang.Boolean",
"com.gridnode.pdip.app.mapper.model.MappingRule","mappingRule.keepOriginal",
"0","0","0","1","1","","999",
"displayable=true\r\nmandatory=true\r\neditable=true",
"type=enum\r\nmappingRule.keepOriginal.enabled=true\r\nmappingRule.keepOriginal.disabled=false"
);

# fieldmetainfo for xpath_mapping
DELETE from fieldmetainfo WHERE EntityObjectName LIKE '%XpathMapping';
INSERT INTO fieldmetainfo VALUES(
NULL,"_uId","UID","UID","java.lang.Long",
"com.gridnode.pdip.app.mapper.model.XpathMapping","xpathMapping.uid",
"0","0","0","0","0","","999",
"displayable=false\r\nmandatory=false\r\neditable=false",
"type=uid"
);
INSERT INTO fieldmetainfo VALUES(
NULL,"_version","VERSION","Version","java.lang.Double",
"com.gridnode.pdip.app.mapper.model.XpathMapping","",
"0","0","0","0","0","","999",
"displayable=false\r\nmandatory=false\r\neditable=false",
"type=range"
);
INSERT INTO fieldmetainfo VALUES(
NULL,"_canDelete","CAN_DELETE","CanDelete","java.lang.Boolean",
"com.gridnode.pdip.app.mapper.model.XpathMapping","",
"0","0","0","0","0","","999",
"displayable=false\r\nmandatory=false\r\neditable=false",
"type=enum\r\ncandelete.enabled=true\r\ncandelete.disabled=false"
);
INSERT INTO fieldmetainfo VALUES(
NULL,"_rootElement","ROOT_ELEMENT","RootElement","java.lang.String",
"com.gridnode.pdip.app.mapper.model.XpathMapping","xpathMapping.rootElement",
"120","0","1","0","1","","999",
"displayable=true\r\nmandatory=true\r\neditable=false\r\neditable.create=true",
"type=text\r\ntext.length.max=120"
);
INSERT INTO fieldmetainfo VALUES(
NULL,"_xpathUid","XPATH_UID","XpathUid","java.lang.Long",
"com.gridnode.pdip.app.mapper.model.XpathMapping","xpathMapping.xpathUid",
"0","0","0","0","0","","999",
"displayable=false\r\nmandatory=false\r\neditable=false",
"type=foreign\r\nforeign.key=mappingFile.uid\r\nforeign.cached=false"
);



# NSL20060307: metainfo for SchemaMapping

DELETE FROM entitymetainfo WHERE EntityName IN ('SchemaMapping');
INSERT INTO entitymetainfo VALUES("com.gridnode.pdip.app.mapper.model.SchemaMapping","SchemaMapping","");

DELETE FROM fieldmetainfo WHERE EntityObjectName = 'com.gridnode.pdip.app.mapper.model.SchemaMapping';

INSERT INTO fieldmetainfo VALUES(
NULL,"_mappingName","MAPPING_NAME","MappingName","java.lang.String",
"com.gridnode.pdip.app.mapper.model.SchemaMapping","schemaMapping.mappingName",
"30","0","1","0","1","","999",
"displayable=true\r\nmandatory=true\r\neditable=true\r\neditable.create=true",
"type=text\r\ntext.length.max=30"
);
INSERT INTO fieldmetainfo VALUES(
NULL,"_description","DESCRIPTION","Description","java.lang.String",
"com.gridnode.pdip.app.mapper.model.SchemaMapping","schemaMapping.description",
"80","0","1","1","1","","999",
"displayable=true\r\nmandatory=true\r\neditable=true",
"type=text\r\ntext.length.max=80"
);
INSERT INTO fieldmetainfo VALUES(
NULL,"_fileName","FILENAME","FileName","java.lang.String",
"com.gridnode.pdip.app.mapper.model.SchemaMapping","schemaMapping.fileName",
"80","0","1","1","1","","999",
"displayable=true\r\nmandatory=true\r\neditable=true",
"type=other"
);
INSERT INTO fieldmetainfo VALUES(
NULL,"_path","PATH","Path","java.lang.String",
"com.gridnode.pdip.app.mapper.model.SchemaMapping","schemaMapping.path",
"80","0","1","1","1","","999",
"displayable=true\r\nmandatory=true\r\neditable=true",
"type=text\r\ntext.length.max=80"
);
INSERT INTO fieldmetainfo VALUES(
NULL,"_zipEntryName","ZIP_ENTRY_NAME","ZipEntryName","java.lang.String",
"com.gridnode.pdip.app.mapper.model.SchemaMapping","schemaMapping.zipEntryName",
"255","0","1","1","1","","999",
"displayable=true\r\nmandatory=true\r\neditable=true",
"type=text\r\ntext.length.max=255"
);

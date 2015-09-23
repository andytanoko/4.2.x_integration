# 07 Mar 2006 GT 4.0   [Neo Sok Lay]   Add metainfo for non-persistent entity SchemaMapping

USE appdb;

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
INSERT INTO fieldmetainfo VALUES(
NULL,"_mappingClass","MAPPING_CLASS","MappingClass","java.lang.String",
"com.gridnode.pdip.app.mapper.model.SchemaMapping","schemaMapping.mappingClass",
"255","0","1","1","1","","999",
"displayable=true\r\nmandatory=true\r\neditable=true",
"type=text\r\ntext.length.max=255"
);
# 05 Dec 2006 GT 4.0  [Neo Sok Lay] Increase SourceDocType, TargetDocType field length to 30 chars.


use appdb;

#
# Dumping data for table 'entitymetainfo'
#
# Field 1 ObjectName: full qualified class name of the entity
# Field 2 EntityName: short class name of the entity
# Field 3 SqlName: table name of the entity

# entitymetainfo for gridtalk_mapping_rule
DELETE from entitymetainfo WHERE EntityName IN ('GridTalkMappingRule');
INSERT INTO entitymetainfo VALUES("com.gridnode.gtas.server.mapper.model.GridTalkMappingRule","GridTalkMappingRule","gridtalk_mapping_rule");

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

# fieldmetainfo for gridtalk_mapping_file
DELETE from fieldmetainfo WHERE EntityObjectName LIKE '%GridTalkMappingRule';
INSERT INTO fieldmetainfo VALUES(
NULL,"_uId","UID","UID","java.lang.Long",
"com.gridnode.gtas.server.mapper.model.GridTalkMappingRule","gridTalkMappingRule.uid",
"0","0","0","0","0","","999",
"displayable=false\r\nmandatory=false\r\neditable=false",
"type=uid"
);
INSERT INTO fieldmetainfo VALUES(
NULL,"_version","VERSION","Version","java.lang.Double",
"com.gridnode.gtas.server.mapper.model.GridTalkMappingRule","",
"0","0","0","0","0","","999",
"displayable=false\r\nmandatory=false\r\neditable=false",
"type=range"
);
INSERT INTO fieldmetainfo VALUES(
NULL,"_canDelete","CAN_DELETE","CanDelete","java.lang.Boolean",
"com.gridnode.gtas.server.mapper.model.GridTalkMappingRule","",
"0","0","0","0","0","","999",
"displayable=false\r\nmandatory=false\r\neditable=false",
"type=enum\r\ncandelete.enabled=true\r\ncandelete.disabled=false"
);
INSERT INTO fieldmetainfo VALUES(
NULL,"_name","NAME","Name","java.lang.String",
"com.gridnode.gtas.server.mapper.model.GridTalkMappingRule","gridTalkMappingRule.name",
"30","0","1","0","1","","999",
"displayable=true\r\nmandatory=true\r\neditable=false\r\neditable.create=true",
"type=text\r\ntext.length.max=30"
);
INSERT INTO fieldmetainfo VALUES(
NULL,"_description","DESCRIPTION","Description",
"java.lang.String","com.gridnode.gtas.server.mapper.model.GridTalkMappingRule","gridTalkMappingRule.description",
"80","0","1","1","1","","999",
"displayable=true\r\nmandatory=true\r\neditable=true",
"type=text\r\ntext.length.max=80"
);
INSERT INTO fieldmetainfo VALUES(
NULL,"_sourceDocType","SOURCE_DOC_TYPE","SourceDocType","java.lang.String",
"com.gridnode.gtas.server.mapper.model.GridTalkMappingRule","gridTalkMappingRule.sourceDocType",
"30","0","1","1","1","","999",
"displayable=true\r\nmandatory=false\r\neditable=true",
"type=foreign\r\nforeign.key=documentType.docType\r\nforeign.display=documentType.docType\r\nforeign.cached=false"
);
INSERT INTO fieldmetainfo VALUES(
NULL,"_targetDocType","TARGET_DOC_TYPE","TargetDocType","java.lang.String",
"com.gridnode.gtas.server.mapper.model.GridTalkMappingRule","gridTalkMappingRule.targetDocType",
"30","0","1","1","1","","999",
"displayable=true\r\nmandatory=false\r\neditable=true",
"type=foreign\r\nforeign.key=documentType.docType\r\nforeign.display=documentType.docType\r\nforeign.cached=false"
);
INSERT INTO fieldmetainfo VALUES(
NULL,"_sourceDocFileType","SOURCE_DOC_FILE_TYPE","SourceDocFileType","java.lang.String",
"com.gridnode.gtas.server.mapper.model.GridTalkMappingRule","gridTalkMappingRule.sourceDocFileType",
"12","0","1","1","1","","999",
"displayable=true\r\nmandatory=false\r\neditable=true",
"type=foreign\r\nforeign.key=fileType.fileType\r\nforeign.display=fileType.fileType\r\nforeign.cached=false"
);
INSERT INTO fieldmetainfo VALUES(
NULL,"_targetDocFileType","TARGET_DOC_FILE_TYPE","TargetDocFileType","java.lang.String",
"com.gridnode.gtas.server.mapper.model.GridTalkMappingRule","gridTalkMappingRule.targetDocFileType",
"12","0","1","1","1","","999",
"displayable=true\r\nmandatory=false\r\neditable=true",
"type=foreign\r\nforeign.key=fileType.fileType\r\nforeign.display=fileType.fileType\r\nforeign.cached=false"
);
INSERT INTO fieldmetainfo VALUES(
NULL,"_headerTransformation","HEADER_TRANSFORMATION","HeaderTransformation","java.lang.Boolean",
"com.gridnode.gtas.server.mapper.model.GridTalkMappingRule","gridTalkMappingRule.headerTransformation",
"0","0","1","1","1","","999",
"displayable=true\r\nmandatory=true\r\neditable=true",
"type=enum\r\ngridTalkMappingRule.headerTransformation.enabled=true\r\ngridTalkMappingRule.headerTransformation.disabled=false"
);
INSERT INTO fieldmetainfo VALUES(
NULL,"_transformWithHeader","TRANSFORM_WITH_HEADER","TransformWithHeader","java.lang.Boolean",
"com.gridnode.gtas.server.mapper.model.GridTalkMappingRule","gridTalkMappingRule.transformWithHeader",
"0","0","1","1","1","","999",
"displayable=true\r\nmandatory=true\r\neditable=true",
"type=enum\r\ngridTalkMappingRule.transformWithHeader.enabled=true\r\ngridTalkMappingRule.transformWithHeader.disabled=false"
);
INSERT INTO fieldmetainfo VALUES(
NULL,"_transformWithSource","TRANSFORM_WITH_SOURCE","TransformWithSource","java.lang.Boolean",
"com.gridnode.gtas.server.mapper.model.GridTalkMappingRule","gridTalkMappingRule.transformWithSource",
"0","0","1","1","1","","999",
"displayable=true\r\nmandatory=true\r\neditable=true",
"type=enum\r\ngridTalkMappingRule.transformWithSource.enabled=true\r\ngridTalkMappingRule.transformWithSource.disabled=false"
);
INSERT INTO fieldmetainfo VALUES(
NULL,"_mappingRule","MAPPING_RULE","MappingRuleUID","com.gridnode.pdip.app.mapper.model.MappingRule",
"com.gridnode.gtas.server.mapper.model.GridTalkMappingRule","gridTalkMappingRule.mappingRule",
"0","0","1","1","1","","999",
"displayable=true\r\nmandatory=true\r\neditable=true",
"type=embedded\r\nembedded.type=mappingRule"
);


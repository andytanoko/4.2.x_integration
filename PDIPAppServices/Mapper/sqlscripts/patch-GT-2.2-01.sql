# 09 Mar 04 GT 2.2.10 [Daniel D'Cotta] Add SubPath to Mapping File

USE appdb;

UPDATE fieldmetainfo SET Constraints="type=foreign\r\nforeign.key=mappingFile.uid\r\nforeign.cached=false" WHERE LABEL="xpathMapping.xpathUid";

UPDATE fieldmetainfo SET Constraints="type=file\r\nfile.downloadable=true\r\nfile.pathKey=mappingFile.path\r\nfile.subPath=subPath" WHERE Label = "mappingFile.filename";

INSERT INTO fieldmetainfo VALUES(
NULL,"_subPath","SUB_PATH","SubPath","java.lang.String",
"com.gridnode.pdip.app.mapper.model.MappingFile","mappingFile.subPath",
"80","0","1","1","1","","999",
"displayable=true\r\nmandatory=false\r\neditable=true",
"type=text\r\ntext.length.max=80"
);

USE userdb;

ALTER TABLE mapping_file ADD COLUMN SubPath varchar(80) NOT NULL DEFAULT '' AFTER Path;
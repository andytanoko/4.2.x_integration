# 03 March 2006       [Tam Wei Xiang]     Update new field 'FileGrouping' in table 'Port'
#                                         New fieldMetaInfo for 'FileGrouping'

USE userdb;
ALTER TABLE port ADD FileGrouping Integer(1) DEFAULT '2';

USE appdb;
INSERT INTO fieldmetainfo VALUES(
NULL,"_fileGrouping","FILE_GROUPING","FileGrouping","java.lang.Integer",
"com.gridnode.gtas.server.backend.model.Port","port.fileGrouping",
"0","0","0","1","1","","999",
"displayable=true\r\nmandatory=true\r\neditable=true",
"type=enum\r\nport.fileGrouping.flat=1\r\nport.fileGrouping.gdocAttachment=2\r\nport.fileGrouping.all=3"
);
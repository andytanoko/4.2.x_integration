# 29 FEB 2008    GT4.0.2.3    Tam Wei Xiang   Expand ProcessInstanceID
# 20 OCT 2006    GT4.0_VAN        [Tam Wei Xiang]     Added in TracingID into GridDocument table

#                                                     Added FMI for TracingID
# 29 FEB 2008    GT4.0.2.3        [Tam Wei Xiang]     Expand ProcessInstanceID

USE userdb;
ALTER TABLE grid_document ADD COLUMN TracingID varchar(36) default NULL;
ALTER TABLE grid_document MODIFY ProcessInstanceID varchar(255) DEFAULT NULL;


USE appdb;
INSERT INTO fieldmetainfo VALUES(
NULL,"_tracingID","TRACING_ID","TracingID","java.lang.String",
"com.gridnode.gtas.server.document.model.GridDocument","gridDocument.tracingID",
"36","0","0","0","1","","999",
"displayable=false\r\nmandatory=false\r\neditable=false",
"type=text\r\ntext.length.max=36"
);

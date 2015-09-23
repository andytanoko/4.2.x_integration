# 21 Aug 2003 [Guo Jianyu] add "ProcessInstanceUid","ProcessInstanceID" and "UserTrackingID" in grid_document table

USE userdb;

ALTER TABLE grid_document ADD COLUMN ProcessInstanceUid bigint(20) AFTER Custom10;
ALTER TABLE grid_document ADD COLUMN ProcessInstanceID varchar(80) default NULL AFTER ProcessInstanceUid;
ALTER TABLE grid_document ADD COLUMN UserTrackingID varchar(80) default NULL AFTER ProcessInstanceID;

USE appdb;

INSERT INTO fieldmetainfo VALUES(
NULL,"_processInstanceUid", "PROCESS_INSTANCE_UID","ProcessInstanceUid","java.lang.Long",
"com.gridnode.gtas.server.document.model.GridDocument", "gridDocument.processInstanceUid",
"0","0","0","0","0","","999",
"displayable=true\r\nmandatory=false\r\neditable=false",
'type=foreign\r\nforeign.key=processInstance.uid\r\nforeign.display=gridDocument.processInstanceId\r\nforeign.cached=false\r\n'
);
INSERT INTO fieldmetainfo VALUES(
NULL,"_processInstanceID","PROCESS_INSTANCE_ID","ProcessInstanceID","java.lang.String",
"com.gridnode.gtas.server.document.model.GridDocument", "gridDocument.processInstanceId", 
80,"0","0","1","1","", 999, 
"displayable=true\r\neditable=false\nmandatory=false\r\n",
'type=foreign\r\nforeign.key=processInstance.processInstanceId\r\nforeign.display=processInstance.processInstanceId\r\nforeign.cached=false\r\n'
);
INSERT INTO fieldmetainfo VALUES(
NULL,"_userTrackingID","USER_TRACKING_ID","UserTrackingID","java.lang.String",
"com.gridnode.gtas.server.document.model.GridDocument", "gridDocument.userTrackingId", 
80,"0","0","1","1","", 999, 
"displayable=true\r\neditable=false\nmandatory=false\r\n",
"type=text\r\ntext.length.max=80\r\n"
);	
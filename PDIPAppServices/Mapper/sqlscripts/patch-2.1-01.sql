# 26 May 2003 I8 v2.1 [Koh Han Sing] Move XpathMapping from GTAS to App.
# 

use appdb;

UPDATE entitymetainfo SET ObjectName= 'com.gridnode.pdip.app.mapper.model.XpathMapping' WHERE EntityName='XpathMapping';

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


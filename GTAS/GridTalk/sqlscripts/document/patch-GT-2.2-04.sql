# 03 Sep 2003 I1 v2.2 [Koh Han Sing] Make Attachment fields selectable when defining UserProcedure

use appdb;

UPDATE fieldmetainfo
SET Presentation='displayable=true\r\nmandatory=true\r\neditable=false'
WHERE FieldName IN ('FILENAME','ORIGINAL_FILENAME')
AND EntityObjectName='com.gridnode.gtas.server.document.model.Attachment';

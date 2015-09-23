# 31 May 2004 GT 2.3 [Daniel] Changed FlowControl's isZip & isSplit to mandatory

USE appdb;

UPDATE fieldmetainfo
SET Presentation="displayable=true\r\neditable=true\r\nmandatory=true\r\n\r\n"
WHERE FieldName="IS_ZIP"
AND EntityObjectName="com.gridnode.pdip.app.channel.model.FlowControlInfo";

UPDATE fieldmetainfo
SET Presentation="displayable=true\r\neditable=true\r\nmandatory=true\r\n\r\n"
WHERE FieldName="IS_SPLIT"
AND EntityObjectName="com.gridnode.pdip.app.channel.model.FlowControlInfo";

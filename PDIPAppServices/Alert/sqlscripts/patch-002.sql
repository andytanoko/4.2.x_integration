# 02 May 2003 v2.1 I1 [Neo Sok Lay] Location & Append field now displayable
# 19 May 2003 v2.1 I1 [Neo Sok Lay] Display Alert type description instead of name in Alert


USE appdb;

UPDATE fieldmetainfo
SET Presentation="displayable=true\r\nmandatory=true\r\neditable=true"
WHERE FieldName="LOCATION"
AND EntityObjectName="com.gridnode.pdip.app.alert.model.MessageTemplate";

UPDATE fieldmetainfo
SET Presentation="displayable=true\r\neditable=true\r\nmandatory=false"
WHERE FieldName="APPEND"
AND EntityObjectName="com.gridnode.pdip.app.alert.model.MessageTemplate";

UPDATE fieldmetainfo
SET Constraints="type=foreign\r\nforeign.key=alertType.uid\r\nforeign.display=alertType.description\r\nforeign.cached=false"
WHERE FieldName="ALERT_TYPE_UID"
AND EntityObjectName="com.gridnode.pdip.app.alert.model.Alert";

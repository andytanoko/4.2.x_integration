# 25 Feb 2004 GT 2.1 [Neo Sok Lay] Display Feature description in AccessRight.
USE appdb;

UPDATE fieldmetainfo
SET Constraints="type=foreign\r\nforeign.key=feature.feature\r\nforeign.display=feature.description\r\nforeign.cached=false\r\n"
WHERE FieldName="FEATURE"
AND EntityObjectName="com.gridnode.pdip.base.acl.model.AccessRight"
;

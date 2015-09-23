# 05 Dec 2006 GT 4.0 [Neo Sok Lay] Increase document type field length to 30 chars.

use appdb;

#
# Dumping data for table 'entitymetainfo'
#
# Field 1 ObjectName: full qualified class name of the entity
# Field 2 EntityName: short class name of the entity
# Field 3 SqlName: table name of the entity

DELETE FROM entitymetainfo
WHERE ObjectName
IN ('com.gridnode.gtas.server.alert.model.AlertTrigger');

INSERT INTO entitymetainfo VALUES ("com.gridnode.gtas.server.alert.model.AlertTrigger","AlertTrigger","alert_trigger");

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

# fieldmetainfo for alert_trigger
DELETE FROM fieldmetainfo WHERE EntityObjectName LIKE '%.AlertTrigger';
INSERT INTO fieldmetainfo VALUES(
NULL,"_uId","UID","UID","java.lang.Long",
"com.gridnode.gtas.server.alert.model.AlertTrigger","alertTrigger.uid",
"0","0","0","0","0","","999",
"displayable=false\r\nmandatory=false\r\neditable=false",
"type=uid"
);
INSERT INTO fieldmetainfo VALUES(
NULL,"_version","VERSION","Version","java.lang.Double",
"com.gridnode.gtas.server.alert.model.AlertTrigger","alertTrigger.version",
"0","0","0","0","0","","999",
"displayable=false\r\nmandatory=false\r\neditable=false",
"type=range"
);
INSERT INTO fieldmetainfo VALUES(
NULL,"_canDelete","CAN_DELETE","CanDelete","java.lang.Boolean",
"com.gridnode.gtas.server.alert.model.AlertTrigger","alertTrigger.canDelete",
"0","0","0","0","0","","999",
"displayable=false\r\nmandatory=false\r\neditable=false",
"type=enum\r\ncandelete.enabled=true\r\ncandelete.disabled=false"
);
INSERT INTO fieldmetainfo VALUES(
NULL,"_level","LEVEL","Level","java.lang.Integer",
"com.gridnode.gtas.server.alert.model.AlertTrigger","alertTrigger.level",
"0","0","1","1","1","","999",
"displayable=true\r\nmandatory=true\r\neditable=false\r\neditable.create=true",
"type=enum\r\nalertTrigger.level.zero=0\r\nalertTrigger.level.one=1\r\nalertTrigger.level.two=2\r\nalertTrigger.level.three=3\r\nalertTrigger.level.four=4"
);
INSERT INTO fieldmetainfo VALUES(
NULL,"_alertUID","ALERT_UID","AlertUID","java.lang.Long",
"com.gridnode.gtas.server.alert.model.AlertTrigger","alertTrigger.alertUid",
"0","0","1","1","1","","999",
"displayable=true\r\nmandatory=true\r\neditable=true",
"type=foreign\r\nforeign.key=alert.uid\r\nforeign.display=alert.description"
);
INSERT INTO fieldmetainfo VALUES(
NULL,"_recipients","RECIPIENTS","Recipients","java.util.List",
"com.gridnode.gtas.server.alert.model.AlertTrigger","alertTrigger.recipients",
"0","0","0","1","1","","999",
"displayable=true\r\nmandatory=false\r\neditable=true",
"type=text\r\ncollection=true\r\ncollection.element=java.lang.String\r\n"
);
INSERT INTO fieldmetainfo VALUES(
NULL,"_docType","DOC_TYPE","DocType","java.lang.String",
"com.gridnode.gtas.server.alert.model.AlertTrigger","alertTrigger.docType",
"30","0","0","1","1","","999",
"displayable=true\r\nmandatory=true\r\neditable=true",
"type=foreign\r\nforeign.key=documentType.docType\r\nforeign.display=documentType.description"
);
INSERT INTO fieldmetainfo VALUES(
NULL,"_partnerType","PARTNER_TYPE","PartnerType","java.lang.String",
"com.gridnode.gtas.server.alert.model.AlertTrigger","alertTrigger.partnerType",
"3","0","0","1","1","","999",
"displayable=true\r\nmandatory=true\r\neditable=true",
"type=foreign\r\nforeign.key=partnerType.partnerType\r\nforeign.display=partnerType.description"
);
INSERT INTO fieldmetainfo VALUES(
NULL,"_partnerGroup","PARTNER_GROUP","PartnerGroup","java.lang.String",
"com.gridnode.gtas.server.alert.model.AlertTrigger","alertTrigger.partnerGroup",
"3","0","0","1","1","","999",
"displayable=true\r\nmandatory=true\r\neditable=true",
"type=foreign\r\nforeign.key=partnerGroup.name\r\nforeign.display=partnerGroup.description"
);
INSERT INTO fieldmetainfo VALUES(
NULL,"_partnerId","PARTNER_ID","PartnerId","java.lang.String",
"com.gridnode.gtas.server.alert.model.AlertTrigger","alertTrigger.partnerId",
"20","0","0","1","1","","999",
"displayable=true\r\nmandatory=false\r\neditable=true",
"type=foreign\r\nforeign.key=partner.partnerId\r\nforeign.display=partner.name"
);
INSERT INTO fieldmetainfo VALUES(
NULL,"_alertType","ALERT_TYPE","AlertType","java.lang.String",
"com.gridnode.gtas.server.alert.model.AlertTrigger","alertTrigger.alertType",
"35","0","1","1","1","","999",
"displayable=true\r\nmandatory=true\r\neditable=false\r\neditable.create=true",
"type=foreign\r\nforeign.key=alertType.name\r\nforeign.display=alertType.description\r\nforeign.cached=false"
);
INSERT INTO fieldmetainfo VALUES(
NULL,"_enabled","IS_ENABLED","Enabled","java.lang.Boolean",
"com.gridnode.gtas.server.alert.model.AlertTrigger","alertTrigger.isEnabled",
"0","0","0","0","0","","999",
"displayable=true\r\nmandatory=true\r\neditable=true",
"type=enum\r\ngeneric.yes=true\r\ngeneric.no=false"
);

INSERT INTO fieldmetainfo VALUES(
NULL,"_attachDoc","IS_ATTACH_DOC","AttachDoc","java.lang.Boolean",
"com.gridnode.gtas.server.alert.model.AlertTrigger","alertTrigger.isAttachDoc",
"0","0","0","0","0","","999",
"displayable=true\r\nmandatory=false\r\neditable=true",
"type=enum\r\ngeneric.yes=true\r\ngeneric.no=false"
);


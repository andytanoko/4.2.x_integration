# 03 Mar 2003 I8 v2.0.18 [Neo Sok Lay] Add Version and CanDelete columns to various Alert entities except AlertAction.
# 05 Mar 2003 I8 v2.0.18 [Neo Sok Lay] Wrong data type for TRIGGER field in Alert. Should be String instead of Long.
#
USE appdb;

INSERT INTO fieldmetainfo VALUES(NULL,"_version","VERSION","Version","java.lang.Double","com.gridnode.pdip.app.alert.model.Alert","",
"0","0","0","0","0","","999","displayable=false\r\nmandatory=false\r\neditable=false","type=range");
INSERT INTO fieldmetainfo VALUES(NULL,"_canDelete","CAN_DELETE","CanDelete","java.lang.Boolean","com.gridnode.pdip.app.alert.model.Alert","",
"0","0","0","0","0","","999","displayable=false\r\nmandatory=false\r\neditable=false","type=enum\r\ngeneric.yes=true\r\ngeneric.no=false");

INSERT INTO fieldmetainfo VALUES(NULL,"_version","VERSION","Version","java.lang.Double","com.gridnode.pdip.app.alert.model.Action","",
"0","0","0","0","0","","999","displayable=false\r\nmandatory=false\r\neditable=false","type=range");
INSERT INTO fieldmetainfo VALUES(NULL,"_canDelete","CAN_DELETE","CanDelete","java.lang.Boolean","com.gridnode.pdip.app.alert.model.Action","",
"0","0","0","0","0","","999","displayable=false\r\nmandatory=false\r\neditable=false","type=enum\r\ngeneric.yes=true\r\ngeneric.no=false");

INSERT INTO fieldmetainfo VALUES(NULL,"_version","VERSION","Version","java.lang.Double","com.gridnode.pdip.app.alert.model.MessageTemplate","",
"0","0","0","0","0","","999","displayable=false\r\nmandatory=false\r\neditable=false","type=range");
INSERT INTO fieldmetainfo VALUES(NULL,"_canDelete","CAN_DELETE","CanDelete","java.lang.Boolean","com.gridnode.pdip.app.alert.model.MessageTemplate","",
"0","0","0","0","0","","999","displayable=false\r\nmandatory=false\r\neditable=false","type=enum\r\ngeneric.yes=true\r\ngeneric.no=false");

INSERT INTO fieldmetainfo VALUES(NULL,"_version","VERSION","Version","java.lang.Double","com.gridnode.pdip.app.alert.model.AlertCategory","",
"0","0","0","0","0","","999","displayable=false\r\nmandatory=false\r\neditable=false","type=range");
INSERT INTO fieldmetainfo VALUES(NULL,"_canDelete","CAN_DELETE","CanDelete","java.lang.Boolean","com.gridnode.pdip.app.alert.model.AlertCategory","",
"0","0","0","0","0","","999","displayable=false\r\nmandatory=false\r\neditable=false","type=enum\r\ngeneric.yes=true\r\ngeneric.no=false");

INSERT INTO fieldmetainfo VALUES(NULL,"_version","VERSION","Version","java.lang.Double","com.gridnode.pdip.app.alert.model.AlertList","",
"0","0","0","0","0","","999","displayable=false\r\nmandatory=false\r\neditable=false","type=range");
INSERT INTO fieldmetainfo VALUES(NULL,"_canDelete","CAN_DELETE","CanDelete","java.lang.Boolean","com.gridnode.pdip.app.alert.model.AlertList","",
"0","0","0","0","0","","999","displayable=false\r\nmandatory=false\r\neditable=false","type=enum\r\ngeneric.yes=true\r\ngeneric.no=false");

INSERT INTO fieldmetainfo VALUES(NULL,"_version","VERSION","Version","java.lang.Double","com.gridnode.pdip.app.alert.model.AlertType","",
"0","0","0","0","0","","999","displayable=false\r\nmandatory=false\r\neditable=false","type=range");
INSERT INTO fieldmetainfo VALUES(NULL,"_canDelete","CAN_DELETE","CanDelete","java.lang.Boolean","com.gridnode.pdip.app.alert.model.AlertType","",
"0","0","0","0","0","","999","displayable=false\r\nmandatory=false\r\neditable=false","type=enum\r\ngeneric.yes=true\r\ngeneric.no=false");

UPDATE fieldmetainfo 
SET ValueClass="java.lang.String"
WHERE FieldName="TRIGGER"
AND EntityObjectName LIKE "%.Alert";


USE userdb;

ALTER table action
ADD COLUMN Version double NOT NULL DEFAULT '1',
ADD COLUMN CanDelete tinyint(1) NOT NULL DEFAULT '1';

ALTER table alert
ADD COLUMN Version double NOT NULL DEFAULT '1',
ADD COLUMN CanDelete tinyint(1) NOT NULL DEFAULT '1';

ALTER table alert_list
ADD COLUMN Version double NOT NULL DEFAULT '1',
ADD COLUMN CanDelete tinyint(1) NOT NULL DEFAULT '1';

ALTER table alert_category
ADD COLUMN Version double NOT NULL DEFAULT '1',
ADD COLUMN CanDelete tinyint(1) NOT NULL DEFAULT '1';

ALTER table message_template
ADD COLUMN Version double NOT NULL DEFAULT '1',
ADD COLUMN CanDelete tinyint(1) NOT NULL DEFAULT '1';

ALTER table alert_type
ADD COLUMN Version double NOT NULL DEFAULT '1',
ADD COLUMN CanDelete tinyint(1) NOT NULL DEFAULT '1';

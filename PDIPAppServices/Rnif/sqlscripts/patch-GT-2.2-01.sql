# 16 Jul 2003 GT 2.2 I1 [Neo Sok Lay] Add CanDelete column in process_def.
# 16 Jul 2003 GT 2.2 I1 [Neo Sok Lay] Add fieldmetainfo for new column CanDelete in process_def table.

USE userdb;

ALTER TABLE process_def
ADD COLUMN CanDelete tinyint(1) NOT NULL DEFAULT '1';

USE appdb;

INSERT INTO fieldmetainfo VALUES (NULL,'_canDelete','CAN_DELETE','CanDelete','java.lang.Boolean','com.gridnode.pdip.app.rnif.model.ProcessDef', 'processDef.isSynchronous', 0,'0','0','1','1','', 999, 'displayable=false\r\neditable=false\r\nmandatory=false\r\n','type=enum\r\ngeneric.yes=true\r\ngeneric.false=false');

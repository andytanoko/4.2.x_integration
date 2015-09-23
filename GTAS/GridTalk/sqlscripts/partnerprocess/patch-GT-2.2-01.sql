# 08 Aug 2003 GT 2.2 I1 [Koh Han Sing] Add isLocalPending field in Trigger.

use appdb;

INSERT INTO fieldmetainfo VALUES(
NULL,"_isLocalPending","IS_LOCAL_PENDING","IsLocalPending","java.lang.Boolean",
"com.gridnode.gtas.server.partnerprocess.model.Trigger","trigger.isLocalPending",
"0","0","0","0","0","","999",
"displayable=true\r\nmandatory=false\r\neditable=true",
"type=enum\r\ntrigger.isLocalPending.true=true\r\ntrigger.isLocalPending.false=false"
);

use userdb;

alter table trigger add column IsLocalPending tinyint(1) NOT NULL DEFAULT '1';


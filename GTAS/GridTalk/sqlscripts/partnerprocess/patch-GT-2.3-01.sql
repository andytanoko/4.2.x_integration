# Oct20 2003 GT2.3 [Guo Jianyu] Add numOfRetries, retryInterval and channelUID fields in Trigger.

use appdb;

INSERT INTO fieldmetainfo VALUES(
NULL,"_numOfRetries","NUM_OF_RETRIES","NumOfRetries","java.lang.Integer",
"com.gridnode.gtas.server.partnerprocess.model.Trigger","trigger.numOfRetries",
"0","0","0","0","0","","999",
"displayable=true\r\nmandatory=false\r\neditable=true",
"type=text"
);
INSERT INTO fieldmetainfo VALUES(
NULL,"_retryInterval","RETRY_INTERVAL","RetryInterval","java.lang.Integer",
"com.gridnode.gtas.server.partnerprocess.model.Trigger","trigger.retryInterval",
"0","0","0","0","0","","999",
"displayable=true\r\nmandatory=false\r\neditable=true",
"type=text"
);
INSERT INTO fieldmetainfo VALUES(
NULL,"_channelUID","CHANNEL_UID","ChannelUID","java.lang.Long",
"com.gridnode.gtas.server.partnerprocess.model.Trigger","trigger.channelUid",
"0","0","0","0","0","","999",
"displayable=true\r\nmandatory=false\r\neditable=true",
"type=foreign\r\nforeign.key=channelInfo.uid\r\nforeign.display=channelInfo.description\r\nforeign.cached=false"
);

use userdb;

alter table trigger add column NumOfRetries smallint DEFAULT '0';
alter table trigger add column RetryInterval int UNSIGNED DEFAULT '0';
alter table trigger add column ChannelUID bigint DEFAULT '-99999999';

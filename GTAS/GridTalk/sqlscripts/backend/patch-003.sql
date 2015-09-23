# 29 May 2003 I8 v2.0.30 [Jagadeesh] 
# 1. Script for Addition of new columns to Port Entity [userdb]
# 2. Addition of metainfo for new fields to FieldMetaInfo [appdb].
# 3. Modification of  FiledMetaInfo for fileExtType to include a new
#    sequential running no type [appdb].
#


USE userdb;

alter table port add column StartNum int(10) DEFAULT NULL;
alter table port add column RolloverNum int(10) DEFAULT NULL;
alter table port add column NextNum int(10)  DEFAULT NULL;
alter table port add column IsPadded tinyint(1) DEFAULT '1';
alter table port add column FixNumLength int(10) DEFAULT NULL;

USE appdb;

INSERT INTO fieldmetainfo VALUES(
NULL,"_startNum","START_NUM","StartNum","java.lang.Integer",
"com.gridnode.gtas.server.backend.model.Port","port.startNum",
"0","0","0","1","1","","999",
"displayable=true\r\nmandatory=true\r\neditable=true",
"type=range"
);

INSERT INTO fieldmetainfo VALUES(
NULL,"_rolloverNum","ROLLOVER_NUM","RolloverNum","java.lang.Integer",
"com.gridnode.gtas.server.backend.model.Port","port.rolloverNum",
"0","0","0","1","1","","999",
"displayable=true\r\nmandatory=true\r\neditable=true",
"type=range"
);

INSERT INTO fieldmetainfo VALUES(
NULL,"_nextNum","NEXT_NUM","NextNum","java.lang.Integer",
"com.gridnode.gtas.server.backend.model.Port","port.nextNum",
"0","0","0","1","1","","999",
"displayable=true\r\nmandatory=true\r\neditable=true",
"type=range"
);

INSERT INTO fieldmetainfo VALUES(
NULL,"_isPadded","IS_PADDED","IsPadded","java.lang.Boolean",
"com.gridnode.gtas.server.backend.model.Port","port.isPadded",
"0","0","1","1","1","","999",
"displayable=true\r\nmandatory=false\r\neditable=true",
"type=enum\r\nport.isPadded.true=true\r\nport.isPadded.false=false"
);


INSERT INTO fieldmetainfo VALUES(
NULL,"_fixNumLength","FIX_NUM_LENGTH","FixNumLength","java.lang.Integer",
"com.gridnode.gtas.server.backend.model.Port","port.fixNumLength",
"0","0","0","1","1","","999",
"displayable=true\r\nmandatory=true\r\neditable=true",
"type=range"
);

#update script for fileExtType to include new FileExtension Type [Sequential Running No].

update fieldmetainfo
set constraints='type=enum\r\nport.fileExtType.datetime=1\r\nport.fileExtType.gdoc=2\r\nport.fileExtType.sequence=3'
where objectname = '_fileExtType'
and entityobjectname = 'com.gridnode.gtas.server.backend.model.Port';


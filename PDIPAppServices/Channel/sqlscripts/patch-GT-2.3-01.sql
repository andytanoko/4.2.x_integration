#@Date:18/DEC/2003 @Author Jagadeesh @Comments: To include FlowControl Profile at entitymetainfo and fieldmetainfo.
#@Date:31/DEC/2003 @Author Neo Sok Lay @Comments: Add FlowControl fmi in ChannelInfo, Remove zip&split fmis from PackagingInfo

use userdb;
ALTER TABLE `channel_info` ADD `FlowControlProfile` BLOB;


# Fieldmetainfo for FlowControl Profile
use appdb;
INSERT INTO entitymetainfo VALUES ('com.gridnode.pdip.app.channel.model.FlowControlInfo','FlowControlInfo','');

INSERT INTO fieldmetainfo VALUES (NULL,'_flowControlInfo','FLOWCONTROL_PROFILE','FlowControlProfile','com.gridnode.pdip.app.channel.model.FlowControlInfo','com.gridnode.pdip.app.channel.model.ChannelInfo','channelInfo.flowControlProfile',0,'0','0','0','0','0',999,'displayable=true\r\neditable=true\r\nmandatory=true\r\n','type=embedded\r\nembedded.type=flowControlInfo');

INSERT INTO fieldmetainfo VALUES (NULL,'_isZip','IS_ZIP','','java.lang.Boolean','com.gridnode.pdip.app.channel.model.FlowControlInfo','flowControlInfo.isZip',0,'0','0','0','0','',999,'displayable=true\r\neditable=true\r\nmandatory=false\r\n\r\n','type=enum\r\ngeneric.yes=true\r\ngeneric.no=false\r\n');
INSERT INTO fieldmetainfo VALUES (NULL,'_zipThreshold','ZIP_THRESHOLD','','java.lang.Integer','com.gridnode.pdip.app.channel.model.FlowControlInfo','flowControlInfo.zipThreshold',0,'0','1','1','1','0',999,'displayable=true\r\neditable=true\r\nmandatory=true\r\n\r\n','type=range');
INSERT INTO fieldmetainfo VALUES (NULL,'_isSplit','IS_SPLIT','','java.lang.Boolean','com.gridnode.pdip.app.channel.model.FlowControlInfo','flowControlInfo.isSplit',0,'0','0','0','0','',999,'displayable=true\r\neditable=true\r\nmandatory=false\r\n\r\n','type=enum\r\ngeneric.yes=true\r\ngeneric.no=false\r\n');
INSERT INTO fieldmetainfo VALUES (NULL,'_splitThreshold','SPLIT_THRESHOLD','','java.lang.Integer','com.gridnode.pdip.app.channel.model.FlowControlInfo','flowControlInfo.splitThreshold',0,'0','1','1','1','0',999,'displayable=true\r\neditable=true\r\nmandatory=true\r\n\r\n','type=range');
INSERT INTO fieldmetainfo VALUES (NULL,'_splitSize','SPLIT_SIZE','','java.lang.Integer','com.gridnode.pdip.app.channel.model.FlowControlInfo','flowControlInfo.splitSize',0,'0','1','1','1','0',999,'displayable=true\r\neditable=true\r\nmandatory=true\r\n\r\n','type=range');

# Remove Zip & split metainfos from PackagingInfo
DELETE FROM fieldmetainfo
WHERE FieldName IN ('ZIPTHRESHOLD','ZIP','SPLIT','SPLIT_SIZE','SPLIT_THRESHOLD')
AND EntityObjectName='com.gridnode.pdip.app.channel.model.PackagingInfo'
;
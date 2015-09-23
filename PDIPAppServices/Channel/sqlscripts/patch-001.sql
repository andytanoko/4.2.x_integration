# 03 Apr 2003 I8 v2.0.26 [Goh Kan Mun] Support file splitting
# 

use appdb;

INSERT INTO fieldmetainfo VALUES (NULL,'_split','SPLIT','Split','java.lang.Boolean','com.gridnode.pdip.app.channel.model.PackagingInfo','packagingInfo.zip',0,'0','0','0','0','',999,'displayable=true\r\neditable=true\r\nmandatory=false\r\n\r\n','type=enum\r\ngeneric.yes=true\r\ngeneric.no=false\r\n');
INSERT INTO fieldmetainfo VALUES (NULL,'_splitSize','SPLIT_SIZE','SplitSize','java.lang.Integer','com.gridnode.pdip.app.channel.model.PackagingInfo','packagingInfo.zipThreshold',0,'0','1','1','1','0',999,'displayable=true\r\neditable=true\r\nmandatory=true\r\n\r\n','type=range');
INSERT INTO fieldmetainfo VALUES (NULL,'_splitThreshold','SPLIT_THRESHOLD','SplitThreshold','java.lang.Integer','com.gridnode.pdip.app.channel.model.PackagingInfo','packagingInfo.zipThreshold',0,'0','1','1','1','0',999,'displayable=true\r\neditable=true\r\nmandatory=true\r\n\r\n','type=range');

use userdb;

ALTER TABLE `packaging_profile`
  MODIFY COLUMN `Zip` tinyint(1) unsigned NOT NULL DEFAULT '1',
  ADD COLUMN `Split` tinyint(1) unsigned NOT NULL DEFAULT '0',
  ADD COLUMN `SplitThreshold` int(11) default '5000',
  ADD COLUMN `SplitSize` int(11) default '5000';


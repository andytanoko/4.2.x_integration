# 19 Aug 2003 GT 2.2 I1 [Guo Jianyu] add "userTrackingID" in process_def table.

USE userdb;

ALTER TABLE process_def
ADD COLUMN UserTrackingIdentifier varchar(150) default NULL AFTER ResponseDocRequestDocIdentifier;

USE appdb;
INSERT INTO fieldmetainfo VALUES (NULL,'_userTrackingIdentifier','USER_TRACKING_IDENTIFIER','UserTrackingIdentifier','java.lang.String','com.gridnode.pdip.app.rnif.model.ProcessDef', 'processDef.userTrackingIdentifier', 150,'0','0','1','1','', 999, 'displayable=true\r\neditable=true\nmandatory=false\r\n','type=text\r\ntext.length.max=150\r\n');
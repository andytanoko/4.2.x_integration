# 21 Aug 2003 [Guo Jianyu] add "UserTrackingID" in rn_profile table

USE userdb;

ALTER TABLE rn_profile ADD COLUMN UserTrackingID varchar(80) default NULL AFTER InResponseToActionID;

USE appdb;
INSERT INTO fieldmetainfo VALUES (NULL,'_userTrackingID','USER_TRACKING_ID','UserTrackingID','java.lang.String','com.gridnode.gtas.server.rnif.model.RNProfile', 'RNProfile.userTrackingId', 80,'0','0','1','1','', 999, 'displayable=true\r\neditable=false\nmandatory=false\r\n','type=text\r\ntext.length.max=80\r\n');
INSERT INTO fieldmetainfo VALUES (NULL,'_userTrackingID','USER_TRACKING_ID','','java.lang.String','com.gridnode.gtas.server.rnif.model.ProcessInstance', 'processInstance.userTrackingId', 80,'0','0','1','1','', 999, 'displayable=true\r\neditable=false\nmandatory=false\r\n','type=text\r\ntext.length.max=80\r\n');	

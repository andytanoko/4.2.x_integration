# 21 July 2003  Jianyu 
USE userdb;

ALTER TABLE rn_profile
ADD COLUMN InResponseToActionID varchar(80) default NULL AFTER MsgDigest,
ADD COLUMN RNIFVersion varchar(80) default NULL AFTER MsgDigest;

USE appdb;

INSERT INTO fieldmetainfo VALUES (NULL,'_RNIFVersion','RNIF_VERSION','RNIFVersion','java.lang.String','com.gridnode.gtas.server.rnif.model.RNProfile', 'RNProfile.RNIFVersion', 80,'0','0','1','1','', 999, NULL, NULL);
INSERT INTO fieldmetainfo VALUES (NULL,'_inResponseToActionID','IN_RESPONSE_TO_ACTION_ID','InResponseToActionID','java.lang.String','com.gridnode.gtas.server.rnif.model.RNProfile', 'RNProfile.inResponseToActionID', 80,'0','0','1','1','', 999, NULL, NULL);
	

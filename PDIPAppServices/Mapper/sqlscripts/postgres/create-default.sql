SET client_encoding = 'UTF8';
SET check_function_bodies = false;
SET client_min_messages = warning;
SET search_path = userdb;

--- mapping_file - default mapping configurations
INSERT INTO "mapping_file" VALUES(-4,'RN_FAILNOTF2_DICT','RosettaNet 2.0 FailureNotification Dictionary','0A1_MS_V02_00_FailureNotification.xml','mapper.path.dict','',5,1,1.0);
INSERT INTO "mapping_file" VALUES(-3,'RN_FAILNOTF1_DICT','RosettaNet 1.1 FailureNotification Dictionary','0A1FailureNotificationMessageGuideline.xml','mapper.path.dict','',5,1,1.0);
INSERT INTO "mapping_file" VALUES(-2,'RN_FAILNOTF2_DTD','RosettaNet 2.0 FailureNotification DTD','0A1_MS_V02_00_FailureNotification.dtd','mapper.path.dtd','',3,1,1.0);
INSERT INTO "mapping_file" VALUES(-1,'RN_FAILNOTF1_DTD','RosettaNet 1.1 FailureNotification DTD','0A1FailureNotificationMessageGuideline.dtd','mapper.path.dtd','',3,1,1.0);
INSERT INTO "mapping_file" VALUES(0,'GT_GD_INBOUND2IMPORT','GridTalk''s default header transformation for Inbound to Import','GD_IB_IP.xsl','path.xsl','',0,0,1.0);
INSERT INTO "mapping_file" VALUES(1,'GT_GD_INBOUND2EXPORT','GridTalk''s default header transformation for Inbound to Export','GD_IB_EP.xsl','path.xsl','',0,0,1.0);
INSERT INTO "mapping_file" VALUES(2,'GT_GD_OUTBOUND2INBOUND','GridTalk''s default header transformation for Outbound to Inbound','GD_OB_IB.xsl', 'path.xsl','',0,0,1.0);
INSERT INTO "mapping_file" VALUES(3,'GT_GD_IMPORT2OUTBOUND','GridTalk''s default header transformation for Import to Outbound','GD_IP_OB.xsl','path.xsl','',0,0,1.0);
INSERT INTO "mapping_file" VALUES(4,'GT_GD_IMPORT2EXPORT','GridTalk''s default header transformation for Import to Export','GD_IP_EP.xsl','path.xsl','',0,0,1.0);
INSERT INTO "mapping_file" VALUES(-5,'GT_GD_INBOUND2OUTBOUND','GridTalk''s default header transformation for Inbound to Outbound','GD_IB_OB.xsl','path.xsl','',0,0,1.0);


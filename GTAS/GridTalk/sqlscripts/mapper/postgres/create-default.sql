SET client_encoding = 'UTF8';
SET check_function_bodies = false;
SET client_min_messages = warning;
SET search_path = userdb;

--- gridtalk_mapping_rule
INSERT INTO "gridtalk_mapping_rule" VALUES(0,'GT_GD_INBOUND2IMPORT_MR','GridTalk''s default header transformation for Inbound to Import','','','','',1,0,0,0,0,1.0);
INSERT INTO "gridtalk_mapping_rule" VALUES(1,'GT_GD_INBOUND2EXPORT_MR','GridTalk''s default header transformation for Inbound to Export','','','','',1,0,0,1,0,1.0);
INSERT INTO "gridtalk_mapping_rule" VALUES(2,'GT_GD_OUTBOUND2INBOUND_MR','GridTalk''s default header transformation for Outbound to Inbound','','','','',1,0,0,2,0,1.0);
INSERT INTO "gridtalk_mapping_rule" VALUES(3,'GT_GD_IMPORT2OUTBOUND_MR','GridTalk''s default header transformation for Import to Outbound','','','','',1,0,0,3,0,1.0);
INSERT INTO "gridtalk_mapping_rule" VALUES(4,'GT_GD_IMPORT2EXPORT_MR','GridTalk''s default header transformation for Import to Export','','','','',1,0,0,4,0,1.0);
INSERT INTO "gridtalk_mapping_rule" VALUES(-1,'GT_GD_INBOUND2OUTBOUND_MR','GridTalk''s default header transformation for Inbound to Outbound','','','','',1,0,0,-1,0,1.0);

--- mapping_rule
INSERT INTO "mapping_rule" VALUES(0,'GT_GD_INBOUND2IMPORT_MR','GridTalk''s default header transformation for Inbound to Import',1,0,0,NULL,'','',0,0,1.0);
INSERT INTO "mapping_rule" VALUES(1,'GT_GD_INBOUND2EXPORT_MR','GridTalk''s default header transformation for Inbound to Export',1,1,0,NULL,'','',0,0,1.0);
INSERT INTO "mapping_rule" VALUES(2,'GT_GD_OUTBOUND2INBOUND_MR','GridTalk''s default header transformation for Outbound to Inbound',1,2,0,NULL,'','',0,0,1.0);
INSERT INTO "mapping_rule" VALUES(3,'GT_GD_IMPORT2OUTBOUND_MR','GridTalk''s default header transformation for Import to Outbound',1,3,0,NULL,'','',0,0,1.0);
INSERT INTO "mapping_rule" VALUES(4,'GT_GD_IMPORT2EXPORT_MR','GridTalk''s default header transformation for Import to Export',1,4,0,NULL,'','',0,0,1.0);
INSERT INTO "mapping_rule" VALUES(-1,'GT_GD_INBOUND2OUTBOUND_MR','GridTalk''s default header transformation for Inbound to Outbound',1,-5,0,NULL,'','',0,0,1.0);


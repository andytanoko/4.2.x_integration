SET client_encoding = 'UTF8';
SET check_function_bodies = false;
SET client_min_messages = warning;
SET search_path = userdb;

--- document_type
INSERT INTO "document_type" VALUES(-4,'RN_FAILNOTF2','RosettaNet 2.0 Failure Notification',0,1);
INSERT INTO "document_type" VALUES(-3,'RN_FAILNOTF1','RosettaNet 1.1 Failure Notification',0,1);
INSERT INTO "document_type" VALUES(-2,'RN_EXCEPTION','RosettaNet Exception',0,1);
INSERT INTO "document_type" VALUES(-1,'RN_ACK','RosettaNet Acknowledgement',0,1);
INSERT INTO "document_type" VALUES(0,'UC','Unclassified',0,1.0);

--- file_type
INSERT INTO "file_type" VALUES(0,'doc','Microsoft Word Document','','','','',0,1.0);
INSERT INTO "file_type" VALUES(1,'txt','Text Document','','','','',0,1.0);
INSERT INTO "file_type" VALUES(2,'xml','XML Document','','','','',0,1.0);
INSERT INTO "file_type" VALUES(3,'csv','CSV Document','','','','',0,1.0);
INSERT INTO "file_type" VALUES(4,'xls','Microsoft Excel Document','','','','',0,1.0);
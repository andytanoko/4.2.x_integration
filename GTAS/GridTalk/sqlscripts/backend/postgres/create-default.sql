SET client_encoding = 'UTF8';
SET check_function_bodies = false;
SET client_min_messages = warning;
SET search_path = userdb;

DELETE FROM "port" WHERE "UID" IN (-1);
INSERT INTO "port" ("UID","PortName","Description","IsRfc","RfcUid","HostDir","IsDiffFileName","IsOverwrite","FileName","IsAddFileExt","FileExtType","FileExtValue","IsExportGdoc","CanDelete","Version","StartNum","RolloverNum","NextNum","IsPadded","FixNumLength","FileGrouping") 
VALUES (-1,'DEF','Default Port',0,NULL,'port/DEF',0,1,'',0,0,'',0,0,1,0,0,0,0,0,2);

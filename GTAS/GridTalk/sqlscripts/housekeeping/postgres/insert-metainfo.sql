SET client_encoding = 'UTF8';
SET check_function_bodies = false;
SET client_min_messages = warning;
SET search_path = appdb;

DELETE FROM "entitymetainfo" WHERE "EntityName" IN ('HousekeepingInfo');
INSERT INTO "entitymetainfo" VALUES ('com.gridnode.gtas.server.housekeeping.model.HousekeepingInfo','HousekeepingInfo',NULL);

---------- HousekeepingInfo
DELETE FROM "fieldmetainfo" WHERE "EntityObjectName" LIKE '%HousekeepingInfo';
INSERT INTO "fieldmetainfo" VALUES(DEFAULT,'_tempFilesDaysToKeep','TEMP_FILES_DAYS_TO_KEEP',NULL,'java.lang.Integer','com.gridnode.gtas.server.housekeeping.model.HousekeepingInfo','houseKeeping.tempFilesDaysToKeep',0,0,0,1,1,NULL,999,'displayable=true'||chr(13)||chr(10)||'mandatory=true'||chr(13)||chr(10)||'editable=true','type=range'||chr(13)||chr(10)||'range.min=0');
INSERT INTO "fieldmetainfo" VALUES(DEFAULT,'_logFilesDaysToKeep','LOG_FILES_DAYS_TO_KEEP',NULL,'java.lang.Integer','com.gridnode.gtas.server.housekeeping.model.HousekeepingInfo','houseKeeping.logFilesDaysToKeep',0,0,0,1,1,NULL,999,'displayable=true'||chr(13)||chr(10)||'mandatory=true'||chr(13)||chr(10)||'editable=true','type=range'||chr(13)||chr(10)||'range.min=0');
INSERT INTO "fieldmetainfo" VALUES(DEFAULT,'_wfRecordsDaysToKeep','WF_RECORDS_DAYS_TO_KEEP',NULL,'java.lang.Integer','com.gridnode.gtas.server.housekeeping.model.HousekeepingInfo','houseKeeping.wfRecordsDaysToKeep',0,0,0,1,1,NULL,999,'displayable=true'||chr(13)||chr(10)||'mandatory=true'||chr(13)||chr(10)||'editable=true','type=range'||chr(13)||chr(10)||'range.min=0');

SET client_encoding = 'UTF8';
SET check_function_bodies = false;
SET client_min_messages = warning;
SET search_path = appdb;

DELETE FROM "fieldmetainfo" WHERE "EntityObjectName" LIKE '%.Schedule' AND "ObjectName"='_taskType';
INSERT INTO "fieldmetainfo" VALUES(DEFAULT,'_taskType','TASK_TYPE','','java.lang.String','com.gridnode.gtas.server.scheduler.model.Schedule','scheduledTask.taskType',100,0,1,1,1,'',999,'displayable=true'||chr(13)||chr(10)||'mandatory=true'||chr(13)||chr(10)||'editable=false'||chr(13)||chr(10)||'editable.create=true','type=enum'||chr(13)||chr(10)||'scheduledTask.taskType.userProcedure=UserProcedure'||chr(13)||chr(10)||'scheduledTask.taskType.checkLicense=CheckLicense'||chr(13)||chr(10)||'scheduledTask.taskType.housekeepingInfo=HousekeepingInfo'||chr(13)||chr(10)||'scheduledTask.taskType.dbArchive=DBArchive');
# 20 Sep 2004 v2.4 [Mahesh] added fieldmetainfo for housekeeping
# 

USE appdb;

DELETE FROM fieldmetainfo WHERE EntityObjectName LIKE '%.Schedule' and ObjectName = '_taskType';

INSERT INTO fieldmetainfo VALUES(
NULL,"_taskType","TASK_TYPE","","java.lang.String",
"com.gridnode.gtas.server.scheduler.model.Schedule","scheduledTask.taskType",
"100","0","1","1","1","","999",
"displayable=true\r\nmandatory=true\r\neditable=false\r\neditable.create=true",
"type=enum\r\nscheduledTask.taskType.userProcedure=UserProcedure\r\nscheduledTask.taskType.checkLicense=CheckLicense\r\nscheduledTask.taskType.housekeepingInfo=HousekeepingInfo"
);

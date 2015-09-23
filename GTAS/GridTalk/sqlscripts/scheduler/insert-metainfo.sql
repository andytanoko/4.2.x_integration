# 11 Feb 2004 v2.2 I5 [Koh Han Sing] Created
#

use appdb;

#
# Dumping data for table 'entitymetainfo'
#
# Field 1 ObjectName: full qualified class name of the entity
# Field 2 EntityName: short class name of the entity
# Field 3 SqlName: table name of the entity

# entitymetainfo for schedule
DELETE FROM entitymetainfo WHERE EntityName = 'Schedule';
INSERT INTO entitymetainfo VALUES("com.gridnode.gtas.server.scheduler.model.Schedule","Schedule","");

#
# Dumping data for table 'fieldmetainfo'
#
# UID bigint(20) NOT NULL auto_increment,
# ObjectName: field name in Entity class ,
# FieldName: field ID in Entity Interface class ,
# SqlName: Field column name in Table,
# ValueClass: field data type,
# EntityObjectName: full qualified class name of the entity
# Label: field display label
# Length: valid field length ,
# Proxy: '1' if proxy, '0' otherwise,,
# Mandatory: '1' if mandatory, '0' otherwise,
# Editable: '1' if editable, '0' otherwise
# Displayable: '1' if displayable, '0' otherwise
# OqlName: ,
# Sequence: default '999' ,
# Presentation: properties for presentation of the field
# Constraints: constraint imposed on the values of the field
#


# fieldmetainfo for workflow_activity
DELETE FROM fieldmetainfo WHERE EntityObjectName LIKE '%.Schedule';
INSERT INTO fieldmetainfo VALUES(
NULL,"_uId","UID","","java.lang.Long",
"com.gridnode.gtas.server.scheduler.model.Schedule","scheduledTask.uid",
"0","0","0","0","0","","999",
"displayable=false\r\nmandatory=false\r\neditable=false",
"type=uid"
);
INSERT INTO fieldmetainfo VALUES(
NULL,"_taskType","TASK_TYPE","","java.lang.String",
"com.gridnode.gtas.server.scheduler.model.Schedule","scheduledTask.taskType",
"100","0","1","1","1","","999",
"displayable=true\r\nmandatory=true\r\neditable=false\r\neditable.create=true",
"type=enum\r\nscheduledTask.taskType.userProcedure=UserProcedure\r\nscheduledTask.taskType.checkLicense=CheckLicense\r\nscheduledTask.taskType.housekeepingInfo=HousekeepingInfo"
);
INSERT INTO fieldmetainfo VALUES(
NULL,"_taskId","TASK_ID","","java.lang.String",
"com.gridnode.gtas.server.scheduler.model.Schedule","scheduledTask.taskId",
"120","0","1","1","1","","999",
"displayable=true\r\nmandatory=true\r\neditable=true",
"type=text\r\ntext.length.max=120"
);
INSERT INTO fieldmetainfo VALUES(
NULL,"_disabled","DISABLED","","java.lang.Boolean",
"com.gridnode.gtas.server.scheduler.model.Schedule","scheduledTask.isDisabled",
"0","0","0","0","0","","999",
"displayable=true\r\nmandatory=true\r\neditable=true",
"type=enum\r\ngeneric.yes=true\r\ngeneric.no=false"
);
INSERT INTO fieldmetainfo VALUES(
NULL,"_startDate","START_DATE","","java.util.Date",
"com.gridnode.gtas.server.scheduler.model.Schedule","scheduledTask.startDate",
"0","0","1","1","1","","999",
"displayable=true\r\nmandatory=true\r\neditable=true",
"type=datetime\r\ndatetime.time=false\r\ndatetime.date=true\r\ndatetime.pattern=yyyy-MM-dd"
);
INSERT INTO fieldmetainfo VALUES(
NULL,"_startTime","START_TIME","","java.lang.String",
"com.gridnode.gtas.server.scheduler.model.Schedule","scheduledTask.runTime",
"5","0","1","1","1","","999",
"displayable=true\r\nmandatory=true\r\neditable=true",
"type=text\r\ntext.length.max=5"
);
INSERT INTO fieldmetainfo VALUES(
NULL,"_timesToRun","TIMES_TO_RUN","","java.lang.Integer",
"com.gridnode.gtas.server.scheduler.model.Schedule","scheduledTask.count",
"0","0","1","1","1","","999",
"displayable=true\r\nmandatory=false\r\neditable=true",
"type=range"
);
INSERT INTO fieldmetainfo VALUES(
NULL,"_delayPeriod","DELAY_PERIOD","","java.lang.Integer",
"com.gridnode.gtas.server.scheduler.model.Schedule","scheduledTask.interval",
"0","0","1","1","1","","999",
"displayable=true\r\nmandatory=true\r\neditable=true",
"type=range"
);
INSERT INTO fieldmetainfo VALUES(
NULL,"_frequency","FREQUENCY","","java.lang.Integer",
"com.gridnode.gtas.server.scheduler.model.Schedule","scheduledTask.frequency",
"0","0","1","1","1","","999",
"displayable=true\r\nmandatory=true\r\neditable=true",
"type=enum\r\nscheduledTask.frequency.once=-1\r\nscheduledTask.frequency.minutely=2\r\nscheduledTask.frequency.hourly=3\r\nscheduledTask.frequency.daily=4\r\nscheduledTask.frequency.weekly=5\r\nscheduledTask.frequency.monthly=6"
);
INSERT INTO fieldmetainfo VALUES(
NULL,"_daysOfWeek","DAYS_OF_WEEK","","java.util.ArrayList",
"com.gridnode.gtas.server.scheduler.model.Schedule","scheduledTask.daysOfWeek",
"0","0","1","0","1","","999",
"displayable=true\r\nmandatory=true\r\neditable=true",
"type=enum\r\ncollection=true\r\ncollection.element=java.lang.Integer\r\nscheduledTask.daysOfWeek.monday=0\r\nscheduledTask.daysOfWeek.tuesday=1\r\nscheduledTask.daysOfWeek.wednesday=2\r\nscheduledTask.daysOfWeek.thursday=3\r\nscheduledTask.daysOfWeek.friday=4\r\nscheduledTask.daysOfWeek.saturday=5\r\nscheduledTask.daysOfWeek.sunday=6"
);
INSERT INTO fieldmetainfo VALUES(
NULL,"_weekOfMonth","WEEK_OF_MONTH","","java.lang.Integer",
"com.gridnode.gtas.server.scheduler.model.Schedule","scheduledTask.weekOfMonth",
"0","0","1","0","1","","999",
"displayable=true\r\nmandatory=true\r\neditable=true",
"type=enum\r\nscheduledTask.weekOfMonth.first=1\r\nscheduledTask.weekOfMonth.second=2\r\nscheduledTask.weekOfMonth.third=3\r\nscheduledTask.weekOfMonth.fourth=4\r\nscheduledTask.weekOfMonth.last=-1"
);

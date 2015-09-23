# 09 Feb 2004 I5 v2.2 [Koh Han Sing] Add in field TaskId in ical_alarm
# 

use appdb;

INSERT INTO fieldmetainfo VALUES 
(NULL,'_taskId','TASK_ID','TaskId','java.lang.String',
'com.gridnode.pdip.base.time.entities.model.iCalAlarm','ical_alarm.taskId',
120,'0','0','1','1','',999,
'displayable=false\r\nmandatory=false\r\neditable=false\r\n',
'type=text\r\ntext.length.max=120\r\n'
);

INSERT INTO fieldmetainfo VALUES 
(NULL,'_includeParentStartTime','INCLUDE_PARENT_START_TIME','IncludeParentStartTime','java.lang.Boolean',
'com.gridnode.pdip.base.time.entities.model.iCalAlarm', 'ical_alarm.includeParentStartTime', 
0,'0','0','1','1','', 999, 'displayable=false','');

INSERT INTO fieldmetainfo VALUES 
(NULL,'_propertiesStr','PROPERTIES_STR','PropertiesStr','java.lang.String',
'com.gridnode.pdip.base.time.entities.model.iCalEvent', 'ical_event.PropertiesStr', 
50,'0','0','1','1','', 999, 'displayable=false','');


INSERT INTO fieldmetainfo VALUES (NULL,'_propertiesStr','PROPERTIES_STR','PropertiesStr','java.lang.String',
'com.gridnode.pdip.base.time.entities.model.iCalTodo', 'ical_todo.PropertiesStr', 
50,'0','0','1','1','', 999, 'displayable=false','');

use userdb;

alter table ical_alarm add column TaskId varchar(120) default NULL;
alter table ical_alarm add column IncludeParentStartTime tinyint(1) default NULL;
alter table ical_event add column `PropertiesStr` text default NULL;
alter table ical_todo  add column  `PropertiesStr` text default NULL;

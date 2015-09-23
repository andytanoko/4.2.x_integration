# 05 Feb 2007 [Neo Sok Lay] GT 4.1   Add a schedule for loading time tasks

use jbossdb;

insert into schedule(target, method_name, method_signature, start_date, period, repetitions)
values('base.time:service=TimerTasksLoaderService', 'loadTasks', 'DATE, NEXT_DATE', 'NOW', 50000, -1);

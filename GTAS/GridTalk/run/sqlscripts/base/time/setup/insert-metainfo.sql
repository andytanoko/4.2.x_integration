SET client_encoding = 'UTF8';
SET check_function_bodies = false;
SET client_min_messages = warning;
SET search_path = appdb;

DELETE FROM "entitymetainfo" WHERE "EntityName" LIKE '%iCalAlarm';
DELETE FROM "entitymetainfo" WHERE "EntityName" LIKE '%iCalEvent';
DELETE FROM "entitymetainfo" WHERE "EntityName" LIKE '%iCalTodo';
DELETE FROM "entitymetainfo" WHERE "EntityName" LIKE '%iCalProperty';
DELETE FROM "entitymetainfo" WHERE "EntityName" LIKE '%iCalInt';
DELETE FROM "entitymetainfo" WHERE "EntityName" LIKE '%iCalDate';
DELETE FROM "entitymetainfo" WHERE "EntityName" LIKE '%iCalString';
DELETE FROM "entitymetainfo" WHERE "EntityName" LIKE '%iCalText';
INSERT INTO "entitymetainfo" VALUES ('com.gridnode.pdip.base.time.entities.model.iCalAlarm','com.gridnode.pdip.base.time.entities.model.iCalAlarm','"ical_alarm"');
INSERT INTO "entitymetainfo" VALUES ('com.gridnode.pdip.base.time.entities.model.iCalEvent','com.gridnode.pdip.base.time.entities.model.iCalEvent','"ical_event"');
INSERT INTO "entitymetainfo" VALUES ('com.gridnode.pdip.base.time.entities.model.iCalTodo','com.gridnode.pdip.base.time.entities.model.iCalTodo','"ical_todo"');
INSERT INTO "entitymetainfo" VALUES ('com.gridnode.pdip.base.time.entities.model.iCalProperty','com.gridnode.pdip.base.time.entities.model.iCalProperty','"ical_property"');
INSERT INTO "entitymetainfo" VALUES ('com.gridnode.pdip.base.time.entities.model.iCalInt','com.gridnode.pdip.base.time.entities.model.iCalInt','"ical_int"');
INSERT INTO "entitymetainfo" VALUES ('com.gridnode.pdip.base.time.entities.model.iCalDate','com.gridnode.pdip.base.time.entities.model.iCalDate','"ical_date"');
INSERT INTO "entitymetainfo" VALUES ('com.gridnode.pdip.base.time.entities.model.iCalString','com.gridnode.pdip.base.time.entities.model.iCalString','"ical_string"');
INSERT INTO "entitymetainfo" VALUES ('com.gridnode.pdip.base.time.entities.model.iCalText','com.gridnode.pdip.base.time.entities.model.iCalText','"ical_text"');


--------- iCalAlarm
DELETE FROM "fieldmetainfo" WHERE "EntityObjectName" LIKE '%iCalAlarm';
INSERT INTO "fieldmetainfo" VALUES (DEFAULT,'_uId','UID','"UID"','java.lang.Long','com.gridnode.pdip.base.time.entities.model.iCalAlarm','',0,0,0,1,1,'',999,'displayable=false','');
INSERT INTO "fieldmetainfo" VALUES (DEFAULT,'_startDuration','START_DURATION','"StartDuration"','java.lang.Long','com.gridnode.pdip.base.time.entities.model.iCalAlarm','',0,0,0,1,1,'',999,'displayable=false','');
INSERT INTO "fieldmetainfo" VALUES (DEFAULT,'_startDt','START_DT','"StartDt"','java.util.Date','com.gridnode.pdip.base.time.entities.model.iCalAlarm','',0,0,0,1,1,'',999,'displayable=false','');
INSERT INTO "fieldmetainfo" VALUES (DEFAULT,'_related','RELATED','"Related"','java.lang.Integer','com.gridnode.pdip.base.time.entities.model.iCalAlarm','',0,0,0,1,1,'',999,'displayable=false','');
INSERT INTO "fieldmetainfo" VALUES (DEFAULT,'_delayPeriod','DELAY_PERIOD','"DelayPeriod"','java.lang.Long','com.gridnode.pdip.base.time.entities.model.iCalAlarm','',0,0,0,1,1,'',999,'displayable=false','');
INSERT INTO "fieldmetainfo" VALUES (DEFAULT,'_repeat','REPEAT','"RepeatCount"','java.lang.Integer','com.gridnode.pdip.base.time.entities.model.iCalAlarm','',0,0,0,1,1,'',999,'displayable=false','');
INSERT INTO "fieldmetainfo" VALUES (DEFAULT,'_category','CATEGORY','"Category"','java.lang.String','com.gridnode.pdip.base.time.entities.model.iCalAlarm','',50,0,0,1,1,'',999,'displayable=false','');
INSERT INTO "fieldmetainfo" VALUES (DEFAULT,'_senderKey','SENDER_KEY','"SenderKey"','java.lang.String','com.gridnode.pdip.base.time.entities.model.iCalAlarm','',50,0,0,1,1,'',999,'displayable=false','');
INSERT INTO "fieldmetainfo" VALUES (DEFAULT,'_receiverKey','RECEIVER_KEY','"ReceiverKey"','java.lang.String','com.gridnode.pdip.base.time.entities.model.iCalAlarm','',50,0,0,1,1,'',999,'displayable=false','');
INSERT INTO "fieldmetainfo" VALUES (DEFAULT,'_disabled','DISABLED','"Disabled"','java.lang.Boolean','com.gridnode.pdip.base.time.entities.model.iCalAlarm','',0,0,0,1,1,'',999,'displayable=false','');
INSERT INTO "fieldmetainfo" VALUES (DEFAULT,'_nextDueTime','NEXT_DUE_TIME','"NextDueTime"','java.util.Date','com.gridnode.pdip.base.time.entities.model.iCalAlarm','',0,0,0,1,1,'',999,'displayable=false','');
INSERT INTO "fieldmetainfo" VALUES (DEFAULT,'_count','COUNT','"Count"','java.lang.Integer','com.gridnode.pdip.base.time.entities.model.iCalAlarm','',0,0,0,1,1,'',999,'displayable=false','');
INSERT INTO "fieldmetainfo" VALUES (DEFAULT,'_parentUid','PARENT_UID','"ParentUid"','java.lang.Long','com.gridnode.pdip.base.time.entities.model.iCalAlarm', 'ical_alarm.parentUid', 0,0,0,1,1,'', 999,'displayable=false','');
INSERT INTO "fieldmetainfo" VALUES (DEFAULT,'_parentKind','PARENT_KIND','"ParentKind"','Short','com.gridnode.pdip.base.time.entities.model.iCalAlarm', 'ical_alarm.parentKind', 0,0,0,1,1,'', 999,'displayable=false','');
INSERT INTO "fieldmetainfo" VALUES (DEFAULT,'_recurListStr','RECUR_LIST_STR','"RecurListStr"','java.lang.String','com.gridnode.pdip.base.time.entities.model.iCalAlarm', 'ical_alarm.recurListStr', 50,0,0,1,1,'', 999,'displayable=false','');
INSERT INTO "fieldmetainfo" VALUES (DEFAULT,'_isRecurComplete','IS_RECUR_COMPLETE','"IsRecurComplete"','java.lang.Boolean','com.gridnode.pdip.base.time.entities.model.iCalAlarm', 'ical_alarm.isRecurComplete', 0,0,0,1,1,'', 999,'displayable=false','');
INSERT INTO "fieldmetainfo" VALUES (DEFAULT,'_curRecur','CUR_RECUR','"CurRecur"','java.lang.String','com.gridnode.pdip.base.time.entities.model.iCalAlarm', 'ical_alarm.curRecur', 50,0,0,1,1,'', 999,'displayable=false','');
INSERT INTO "fieldmetainfo" VALUES (DEFAULT,'_isPseudoParent','IS_PSEUDO_PARENT','"IsPseudoParent"','java.lang.Boolean','com.gridnode.pdip.base.time.entities.model.iCalAlarm', 'ical_alarm.isPseudoParent', 0,0,0,1,1,'', 999,'displayable=false','');
INSERT INTO "fieldmetainfo" VALUES (DEFAULT,'_includeParentStartTime','INCLUDE_PARENT_START_TIME','"IncludeParentStartTime"','java.lang.Boolean','com.gridnode.pdip.base.time.entities.model.iCalAlarm', 'ical_alarm.includeParentStartTime', 0,0,0,1,1,'', 999,'displayable=false','');
INSERT INTO "fieldmetainfo" VALUES (DEFAULT,'_taskId','TASK_ID','"TaskId"','java.lang.String','com.gridnode.pdip.base.time.entities.model.iCalAlarm','ical_alarm.taskId',120,0,0,1,1,'',999,'displayable=false','type=text'||chr(13)||chr(10)||'text.length.max=120'||chr(13)||chr(10));


--------- iCalEvent
DELETE FROM "fieldmetainfo" WHERE "EntityObjectName" LIKE '%iCalEvent';
INSERT INTO "fieldmetainfo" VALUES (DEFAULT,'_uId','UID','"UID"','java.lang.Long','com.gridnode.pdip.base.time.entities.model.iCalEvent','',0,0,0,1,1,'',999,'displayable=false','');
INSERT INTO "fieldmetainfo" VALUES (DEFAULT,'_kind','KIND','"Kind"','java.lang.Short','com.gridnode.pdip.base.time.entities.model.iCalEvent','',0,0,0,1,1,'',999,'displayable=false','');
INSERT INTO "fieldmetainfo" VALUES (DEFAULT,'_ownerId','OWNER_ID','"OwnerId"','java.lang.Integer','com.gridnode.pdip.base.time.entities.model.iCalEvent','',0,0,0,1,1,'',999,'displayable=false','');
INSERT INTO "fieldmetainfo" VALUES (DEFAULT,'_classification','CLASSIFICATION','"Classification"','java.lang.String','com.gridnode.pdip.base.time.entities.model.iCalEvent','',50,0,0,1,1,'',999,'displayable=false','');
INSERT INTO "fieldmetainfo" VALUES (DEFAULT,'_createDt','CREATE_DT','"CreateDt"','java.util.Date','com.gridnode.pdip.base.time.entities.model.iCalEvent','',0,0,0,1,1,'',999,'displayable=false','');
INSERT INTO "fieldmetainfo" VALUES (DEFAULT,'_lastModifyDt','LAST_MODIFY_DT','"LastModifyDt"','java.util.Date','com.gridnode.pdip.base.time.entities.model.iCalEvent','',0,0,0,1,1,'',999,'displayable=false','');
INSERT INTO "fieldmetainfo" VALUES (DEFAULT,'_isDateType','IS_DATE_TYPE','"IsDateType"','java.lang.Boolean','com.gridnode.pdip.base.time.entities.model.iCalEvent','',0,0,0,1,1,'',999,'displayable=false','');
INSERT INTO "fieldmetainfo" VALUES (DEFAULT,'_isDtFloat','IS_DT_FLOAT','"IsDtFloat"','java.lang.Boolean','com.gridnode.pdip.base.time.entities.model.iCalEvent','',0,0,0,1,1,'',999,'displayable=false','');
INSERT INTO "fieldmetainfo" VALUES (DEFAULT,'_startDt','START_DT','"StartDt"','java.util.Date','com.gridnode.pdip.base.time.entities.model.iCalEvent','',0,0,0,1,1,'',999,'displayable=false','');
INSERT INTO "fieldmetainfo" VALUES (DEFAULT,'_priority','PRIORITY','"Priority"','java.lang.Integer','com.gridnode.pdip.base.time.entities.model.iCalEvent','',0,0,0,1,1,'',999,'displayable=false','');
INSERT INTO "fieldmetainfo" VALUES (DEFAULT,'_sequenceNum','SEQUENCE_NUM','"SequenceNum"','java.lang.Integer','com.gridnode.pdip.base.time.entities.model.iCalEvent','',0,0,0,1,1,'',999,'displayable=false','');
INSERT INTO "fieldmetainfo" VALUES (DEFAULT,'_status','STATUS','"Status"','java.lang.Integer','com.gridnode.pdip.base.time.entities.model.iCalEvent','',0,0,0,1,1,'',999,'displayable=false','');
INSERT INTO "fieldmetainfo" VALUES (DEFAULT,'_iCalUid','iCal_UID','"iCalUid"','java.lang.String','com.gridnode.pdip.base.time.entities.model.iCalEvent','',50,0,0,1,1,'',999,'displayable=false','');
INSERT INTO "fieldmetainfo" VALUES (DEFAULT,'_endDt','END_DT','"EndDt"','java.util.Date','com.gridnode.pdip.base.time.entities.model.iCalEvent','',0,0,0,1,1,'',999,'displayable=false','');
INSERT INTO "fieldmetainfo" VALUES (DEFAULT,'_duration','DURATION','"Duration"','java.lang.Long','com.gridnode.pdip.base.time.entities.model.iCalEvent','',0,0,0,1,1,'',999,'displayable=false','');
INSERT INTO "fieldmetainfo" VALUES (DEFAULT,'_propertiesStr','PROPERTIES_STR','"PropertiesStr"','java.lang.String','com.gridnode.pdip.base.time.entities.model.iCalEvent', 'ical_event.PropertiesStr', 50,0,0,1,1,'', 999,'displayable=false','');
INSERT INTO "fieldmetainfo" VALUES (DEFAULT,'_timeTransparency','TIME_TRANSPARENCY','"TimeTransparency"','java.lang.Integer','com.gridnode.pdip.base.time.entities.model.iCalEvent','',0,0,0,1,1,'',999,'displayable=false','');

--------- iCalTodo
DELETE FROM "fieldmetainfo" WHERE "EntityObjectName" LIKE '%iCalTodo';
INSERT INTO "fieldmetainfo" VALUES (DEFAULT,'_uId','UID','"UID"','java.lang.Long','com.gridnode.pdip.base.time.entities.model.iCalTodo','',0,0,0,1,1,'',999,'displayable=false','');
INSERT INTO "fieldmetainfo" VALUES (DEFAULT,'_kind','KIND','"Kind"','java.lang.Short','com.gridnode.pdip.base.time.entities.model.iCalTodo','',0,0,0,1,1,'',999,'displayable=false','');
INSERT INTO "fieldmetainfo" VALUES (DEFAULT,'_ownerId','OWNER_ID','"OwnerId"','java.lang.Integer','com.gridnode.pdip.base.time.entities.model.iCalTodo','',0,0,0,1,1,'',999,'displayable=false','');
INSERT INTO "fieldmetainfo" VALUES (DEFAULT,'_classification','CLASSIFICATION','"Classification"','java.lang.String','com.gridnode.pdip.base.time.entities.model.iCalTodo','',50,0,0,1,1,'',999,'displayable=false','');
INSERT INTO "fieldmetainfo" VALUES (DEFAULT,'_createDt','CREATE_DT','"CreateDt"','java.util.Date','com.gridnode.pdip.base.time.entities.model.iCalTodo','',0,0,0,1,1,'',999,'displayable=false','');
INSERT INTO "fieldmetainfo" VALUES (DEFAULT,'_lastModifyDt','LAST_MODIFY_DT','"LastModifyDt"','java.util.Date','com.gridnode.pdip.base.time.entities.model.iCalTodo','',0,0,0,1,1,'',999,'displayable=false','');
INSERT INTO "fieldmetainfo" VALUES (DEFAULT,'_isDateType','IS_DATE_TYPE','"IsDateType"','java.lang.Boolean','com.gridnode.pdip.base.time.entities.model.iCalTodo','',0,0,0,1,1,'',999,'displayable=false','');
INSERT INTO "fieldmetainfo" VALUES (DEFAULT,'_isDtFloat','IS_DT_FLOAT','"IsDtFloat"','java.lang.Boolean','com.gridnode.pdip.base.time.entities.model.iCalTodo','',0,0,0,1,1,'',999,'displayable=false','');
INSERT INTO "fieldmetainfo" VALUES (DEFAULT,'_startDt','START_DT','"StartDt"','java.util.Date','com.gridnode.pdip.base.time.entities.model.iCalTodo','',0,0,0,1,1,'',999,'displayable=false','');
INSERT INTO "fieldmetainfo" VALUES (DEFAULT,'_priority','PRIORITY','"Priority"','java.lang.Integer','com.gridnode.pdip.base.time.entities.model.iCalTodo','',0,0,0,1,1,'',999,'displayable=false','');
INSERT INTO "fieldmetainfo" VALUES (DEFAULT,'_sequenceNum','SEQUENCE_NUM','"SequenceNum"','java.lang.Integer','com.gridnode.pdip.base.time.entities.model.iCalTodo','',0,0,0,1,1,'',999,'displayable=false','');
INSERT INTO "fieldmetainfo" VALUES (DEFAULT,'_status','STATUS','"Status"','java.lang.Integer','com.gridnode.pdip.base.time.entities.model.iCalTodo','',0,0,0,1,1,'',999,'displayable=false','');
INSERT INTO "fieldmetainfo" VALUES (DEFAULT,'_iCalUid','iCal_UID','"iCalUid"','java.lang.String','com.gridnode.pdip.base.time.entities.model.iCalTodo','',50,0,0,1,1,'',999,'displayable=false','');
INSERT INTO "fieldmetainfo" VALUES (DEFAULT,'_endDt','END_DT','"EndDt"','java.util.Date','com.gridnode.pdip.base.time.entities.model.iCalTodo','',0,0,0,1,1,'',999,'displayable=false','');
INSERT INTO "fieldmetainfo" VALUES (DEFAULT,'_duration','DURATION','"Duration"','java.lang.Long','com.gridnode.pdip.base.time.entities.model.iCalTodo','',0,0,0,0,1,'1',999,'displayable=false','');
INSERT INTO "fieldmetainfo" VALUES (DEFAULT,'_completeDt','COMPLETE_DT','"CompleteDt"','java.util.Date','com.gridnode.pdip.base.time.entities.model.iCalTodo','',0,0,0,1,1,'',999,'displayable=false','');
INSERT INTO "fieldmetainfo" VALUES (DEFAULT,'_propertiesStr','PROPERTIES_STR','"PropertiesStr"','java.lang.String','com.gridnode.pdip.base.time.entities.model.iCalTodo', 'ical_todo.PropertiesStr', 50,0,0,1,1,'', 999,'displayable=false','');
INSERT INTO "fieldmetainfo" VALUES (DEFAULT,'_percentCompleted','PERCENT_COMPLETED','"PercentCompleted"','java.lang.Integer','com.gridnode.pdip.base.time.entities.model.iCalTodo','',0,0,0,1,1,'',999,'displayable=false','');

--------- iCalProperty
DELETE FROM "fieldmetainfo" WHERE "EntityObjectName" LIKE '%iCalProperty';
INSERT INTO "fieldmetainfo" VALUES (DEFAULT,'_uId','UID','"UID"','java.lang.Long','com.gridnode.pdip.base.time.entities.model.iCalProperty','',0,0,0,1,1,'',999,'displayable=false','');
INSERT INTO "fieldmetainfo" VALUES (DEFAULT,'_kind','KIND','"Kind"','java.lang.Short','com.gridnode.pdip.base.time.entities.model.iCalProperty','',0,0,0,1,1,'',999,'displayable=false','');
INSERT INTO "fieldmetainfo" VALUES (DEFAULT,'_compKind','COMP_KIND','"CompKind"','java.lang.Short','com.gridnode.pdip.base.time.entities.model.iCalProperty','',0,0,0,1,1,'',999,'displayable=false','');
INSERT INTO "fieldmetainfo" VALUES (DEFAULT,'_iCalCompId','iCal_COMP_ID','"iCalCompId"','java.lang.Long','com.gridnode.pdip.base.time.entities.model.iCalProperty','',0,0,0,1,1,'',999,'displayable=false','');

--------- iCalInt
DELETE FROM "fieldmetainfo" WHERE "EntityObjectName" LIKE '%iCalInt';
INSERT INTO "fieldmetainfo" VALUES (DEFAULT,'_uId','UID','"UID"','java.lang.Long','com.gridnode.pdip.base.time.entities.model.iCalInt','',0,0,0,1,1,'',999,'displayable=false','');
INSERT INTO "fieldmetainfo" VALUES (DEFAULT,'_compKind','COMP_KIND','"CompKind"','java.lang.Short','com.gridnode.pdip.base.time.entities.model.iCalInt','',0,0,0,1,1,'',999,'displayable=false','');
INSERT INTO "fieldmetainfo" VALUES (DEFAULT,'_propKind','PROP_KIND','"PropKind"','java.lang.Short','com.gridnode.pdip.base.time.entities.model.iCalInt','',0,0,0,1,1,'',999,'displayable=false','');
INSERT INTO "fieldmetainfo" VALUES (DEFAULT,'_paramKind','PARAM_KIND','"ParamKind"','java.lang.Short','com.gridnode.pdip.base.time.entities.model.iCalInt','',0,0,0,1,1,'',999,'displayable=false','');
INSERT INTO "fieldmetainfo" VALUES (DEFAULT,'_refKind','REF_KIND','"RefKind"','java.lang.Short','com.gridnode.pdip.base.time.entities.model.iCalInt','',0,0,0,1,1,'',999,'displayable=false','');
INSERT INTO "fieldmetainfo" VALUES (DEFAULT,'_valueKind','VALUE_KIND','"ValueKind"','java.lang.Short','com.gridnode.pdip.base.time.entities.model.iCalInt','',0,0,0,1,1,'',999,'displayable=false','');
INSERT INTO "fieldmetainfo" VALUES (DEFAULT,'_iCalCompId','iCal_COMP_ID','"iCalCompId"','java.lang.Long','com.gridnode.pdip.base.time.entities.model.iCalInt','',0,0,0,1,1,'',999,'displayable=false','');
INSERT INTO "fieldmetainfo" VALUES (DEFAULT,'_refId','REF_ID','"RefId"','java.lang.Long','com.gridnode.pdip.base.time.entities.model.iCalInt','',0,0,0,1,1,'',999,'displayable=false','');
INSERT INTO "fieldmetainfo" VALUES (DEFAULT,'_intValue','INT_VALUE','"IntValue"','java.lang.Integer','com.gridnode.pdip.base.time.entities.model.iCalInt','',0,0,0,1,1,'',999,'displayable=false','');


--------- iCalDate
DELETE FROM "fieldmetainfo" WHERE "EntityObjectName" LIKE '%iCalDate';
INSERT INTO "fieldmetainfo" VALUES (DEFAULT,'_uId','UID','"UID"','java.lang.Long','com.gridnode.pdip.base.time.entities.model.iCalDate','',0,0,0,1,1,'',999,'displayable=false','');
INSERT INTO "fieldmetainfo" VALUES (DEFAULT,'_compKind','COMP_KIND','"CompKind"','java.lang.Short','com.gridnode.pdip.base.time.entities.model.iCalDate','',0,0,0,1,1,'',999,'displayable=false','');
INSERT INTO "fieldmetainfo" VALUES (DEFAULT,'_propKind','PROP_KIND','"PropKind"','java.lang.Short','com.gridnode.pdip.base.time.entities.model.iCalDate','',0,0,0,1,1,'',999,'displayable=false','');
INSERT INTO "fieldmetainfo" VALUES (DEFAULT,'_paramKind','PARAM_KIND','"ParamKind"','java.lang.Short','com.gridnode.pdip.base.time.entities.model.iCalDate','',0,0,0,1,1,'',999,'displayable=false','');
INSERT INTO "fieldmetainfo" VALUES (DEFAULT,'_refKind','REF_KIND','"RefKind"','java.lang.Short','com.gridnode.pdip.base.time.entities.model.iCalDate','',0,0,0,1,1,'',999,'displayable=false','');
INSERT INTO "fieldmetainfo" VALUES (DEFAULT,'_valueKind','VALUE_KIND','"ValueKind"','java.lang.Short','com.gridnode.pdip.base.time.entities.model.iCalDate','',0,0,0,1,1,'',999,'displayable=false','');
INSERT INTO "fieldmetainfo" VALUES (DEFAULT,'_iCalCompId','iCal_COMP_ID','"iCalCompId"','java.lang.Long','com.gridnode.pdip.base.time.entities.model.iCalDate','',0,0,0,1,1,'',999,'displayable=false','');
INSERT INTO "fieldmetainfo" VALUES (DEFAULT,'_refId','REF_ID','"RefId"','java.lang.Long','com.gridnode.pdip.base.time.entities.model.iCalDate','',0,0,0,1,1,'',999,'displayable=false','');
INSERT INTO "fieldmetainfo" VALUES (DEFAULT,'_dateValue','DATE_VALUE','"DateValue"','java.util.Date','com.gridnode.pdip.base.time.entities.model.iCalDate','',0,0,0,1,1,'',999,'displayable=false','');

--------- iCalString
DELETE FROM "fieldmetainfo" WHERE "EntityObjectName" LIKE '%iCalString';
INSERT INTO "fieldmetainfo" VALUES (DEFAULT,'_uId','UID','"UID"','java.lang.Long','com.gridnode.pdip.base.time.entities.model.iCalString','',0,0,0,1,1,'',999,'displayable=false','');
INSERT INTO "fieldmetainfo" VALUES (DEFAULT,'_compKind','COMP_KIND','"CompKind"','java.lang.Short','com.gridnode.pdip.base.time.entities.model.iCalString','',0,0,0,1,1,'',999,'displayable=false','');
INSERT INTO "fieldmetainfo" VALUES (DEFAULT,'_propKind','PROP_KIND','"PropKind"','java.lang.Short','com.gridnode.pdip.base.time.entities.model.iCalString','',0,0,0,1,1,'',999,'displayable=false','');
INSERT INTO "fieldmetainfo" VALUES (DEFAULT,'_paramKind','PARAM_KIND','"ParamKind"','java.lang.Short','com.gridnode.pdip.base.time.entities.model.iCalString','',0,0,0,1,1,'',999,'displayable=false','');
INSERT INTO "fieldmetainfo" VALUES (DEFAULT,'_refKind','REF_KIND','"RefKind"','java.lang.Short','com.gridnode.pdip.base.time.entities.model.iCalString','',0,0,0,1,1,'',999,'displayable=false','');
INSERT INTO "fieldmetainfo" VALUES (DEFAULT,'_valueKind','VALUE_KIND','"ValueKind"','java.lang.Short','com.gridnode.pdip.base.time.entities.model.iCalString','',0,0,0,1,1,'',999,'displayable=false','');
INSERT INTO "fieldmetainfo" VALUES (DEFAULT,'_iCalCompId','iCal_COMP_ID','"iCalCompId"','java.lang.Long','com.gridnode.pdip.base.time.entities.model.iCalString','',0,0,0,1,1,'',999,'displayable=false','');
INSERT INTO "fieldmetainfo" VALUES (DEFAULT,'_refId','REF_ID','"RefId"','java.lang.Long','com.gridnode.pdip.base.time.entities.model.iCalString','',0,0,0,1,1,'',999,'displayable=false','');
INSERT INTO "fieldmetainfo" VALUES (DEFAULT,'_strValue','STR_VALUE','"StrValue"','java.lang.String','com.gridnode.pdip.base.time.entities.model.iCalString','',50,0,0,1,1,'',999,'displayable=false','');

--------- iCalText
DELETE FROM "fieldmetainfo" WHERE "EntityObjectName" LIKE '%iCalText';
INSERT INTO "fieldmetainfo" VALUES (DEFAULT,'_uId','UID','"UID"','java.lang.Long','com.gridnode.pdip.base.time.entities.model.iCalText','',0,0,0,1,1,'',999,'displayable=false','');
INSERT INTO "fieldmetainfo" VALUES (DEFAULT,'_compKind','COMP_KIND','"CompKind"','java.lang.Short','com.gridnode.pdip.base.time.entities.model.iCalText','',0,0,0,1,1,'',999,'displayable=false','');
INSERT INTO "fieldmetainfo" VALUES (DEFAULT,'_propKind','PROP_KIND','"PropKind"','java.lang.Short','com.gridnode.pdip.base.time.entities.model.iCalText','',0,0,0,1,1,'',999,'displayable=false','');
INSERT INTO "fieldmetainfo" VALUES (DEFAULT,'_paramKind','PARAM_KIND','"ParamKind"','java.lang.Short','com.gridnode.pdip.base.time.entities.model.iCalText','',0,0,0,1,1,'',999,'displayable=false','');
INSERT INTO "fieldmetainfo" VALUES (DEFAULT,'_refKind','REF_KIND','"RefKind"','java.lang.Short','com.gridnode.pdip.base.time.entities.model.iCalText','',0,0,0,1,1,'',999,'displayable=false','');
INSERT INTO "fieldmetainfo" VALUES (DEFAULT,'_valueKind','VALUE_KIND','"ValueKind"','java.lang.Short','com.gridnode.pdip.base.time.entities.model.iCalText','',0,0,0,1,1,'',999,'displayable=false','');
INSERT INTO "fieldmetainfo" VALUES (DEFAULT,'_iCalCompId','iCal_COMP_ID','"iCalCompId"','java.lang.Long','com.gridnode.pdip.base.time.entities.model.iCalText','',0,0,0,1,1,'',999,'displayable=false','');
INSERT INTO "fieldmetainfo" VALUES (DEFAULT,'_refId','REF_ID','"RefId"','java.lang.Long','com.gridnode.pdip.base.time.entities.model.iCalText','',0,0,0,1,1,'',999,'displayable=false','');
INSERT INTO "fieldmetainfo" VALUES (DEFAULT,'_textValue','TEXT_VALUE','"TextValue"','java.lang.String','com.gridnode.pdip.base.time.entities.model.iCalText','',50,0,0,1,1,'',999,'displayable=false','');

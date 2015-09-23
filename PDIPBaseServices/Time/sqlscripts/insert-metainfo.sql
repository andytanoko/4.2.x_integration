# 09 Feb 2004 I5 v2.2 [Koh Han Sing] Add in field TaskId in ical_alarm
# 06 Oct 2005 I1 v4.0 [Neo Sok Lay]  Change ical_alarm.Repeat field name to RepeatCount

USE appdb;

#
# Dumping data for table 'entitymetainfo'
#
INSERT INTO entitymetainfo VALUES ('com.gridnode.pdip.base.time.entities.model.iCalAlarm','com.gridnode.pdip.base.time.entities.model.iCalAlarm','ical_alarm');
INSERT INTO entitymetainfo VALUES ('com.gridnode.pdip.base.time.entities.model.iCalEvent','com.gridnode.pdip.base.time.entities.model.iCalEvent','ical_event');
INSERT INTO entitymetainfo VALUES ('com.gridnode.pdip.base.time.entities.model.iCalTodo','com.gridnode.pdip.base.time.entities.model.iCalTodo','ical_todo');
INSERT INTO entitymetainfo VALUES ('com.gridnode.pdip.base.time.entities.model.iCalProperty','com.gridnode.pdip.base.time.entities.model.iCalProperty','ical_property');
INSERT INTO entitymetainfo VALUES ('com.gridnode.pdip.base.time.entities.model.iCalInt','com.gridnode.pdip.base.time.entities.model.iCalInt','ical_int');
INSERT INTO entitymetainfo VALUES ('com.gridnode.pdip.base.time.entities.model.iCalDate','com.gridnode.pdip.base.time.entities.model.iCalDate','ical_date');
INSERT INTO entitymetainfo VALUES ('com.gridnode.pdip.base.time.entities.model.iCalString','com.gridnode.pdip.base.time.entities.model.iCalString','ical_string');
INSERT INTO entitymetainfo VALUES ('com.gridnode.pdip.base.time.entities.model.iCalText','com.gridnode.pdip.base.time.entities.model.iCalText','ical_text');


#
# Dumping data for table 'fieldmetainfo'
#

INSERT INTO fieldmetainfo VALUES (NULL,'_uId','UID','UID','java.lang.Long','com.gridnode.pdip.base.time.entities.model.iCalAlarm','',0,'0','0','1','1','',999,'displayable=false','');
INSERT INTO fieldmetainfo VALUES (NULL,'_startDuration','START_DURATION','StartDuration','java.lang.Long','com.gridnode.pdip.base.time.entities.model.iCalAlarm','',0,'0','0','1','1','',999,'displayable=false','');
INSERT INTO fieldmetainfo VALUES (NULL,'_startDt','START_DT','StartDt','java.util.Date','com.gridnode.pdip.base.time.entities.model.iCalAlarm','',0,'0','0','1','1','',999,'displayable=false','');
INSERT INTO fieldmetainfo VALUES (NULL,'_related','RELATED','Related','java.lang.Integer','com.gridnode.pdip.base.time.entities.model.iCalAlarm','',0,'0','0','1','1','',999,'displayable=false','');
INSERT INTO fieldmetainfo VALUES (NULL,'_delayPeriod','DELAY_PERIOD','DelayPeriod','java.lang.Long','com.gridnode.pdip.base.time.entities.model.iCalAlarm','',0,'0','0','1','1','',999,'displayable=false','');
INSERT INTO fieldmetainfo VALUES (NULL,'_repeat','REPEAT','RepeatCount','java.lang.Integer','com.gridnode.pdip.base.time.entities.model.iCalAlarm','',0,'0','0','1','1','',999,'displayable=false','');
INSERT INTO fieldmetainfo VALUES (NULL,'_category','CATEGORY','Category','java.lang.String','com.gridnode.pdip.base.time.entities.model.iCalAlarm','',50,'0','0','1','1','',999,'displayable=false','');
INSERT INTO fieldmetainfo VALUES (NULL,'_senderKey','SENDER_KEY','SenderKey','java.lang.String','com.gridnode.pdip.base.time.entities.model.iCalAlarm','',50,'0','0','1','1','',999,'displayable=false','');
INSERT INTO fieldmetainfo VALUES (NULL,'_receiverKey','RECEIVER_KEY','ReceiverKey','java.lang.String','com.gridnode.pdip.base.time.entities.model.iCalAlarm','',50,'0','0','1','1','',999,'displayable=false','');
INSERT INTO fieldmetainfo VALUES (NULL,'_disabled','DISABLED','Disabled','java.lang.Boolean','com.gridnode.pdip.base.time.entities.model.iCalAlarm','',0,'0','0','1','1','',999,'displayable=false','');
INSERT INTO fieldmetainfo VALUES (NULL,'_nextDueTime','NEXT_DUE_TIME','NextDueTime','java.util.Date','com.gridnode.pdip.base.time.entities.model.iCalAlarm','',0,'0','0','1','1','',999,'displayable=false','');
INSERT INTO fieldmetainfo VALUES (NULL,'_count','COUNT','Count','java.lang.Integer','com.gridnode.pdip.base.time.entities.model.iCalAlarm','',0,'0','0','1','1','',999,'displayable=false','');
INSERT INTO fieldmetainfo VALUES (NULL,'_parentUid','PARENT_UID','ParentUid','java.lang.Long','com.gridnode.pdip.base.time.entities.model.iCalAlarm', 'ical_alarm.parentUid', 0,'0','0','1','1','', 999, 'displayable=false','');
INSERT INTO fieldmetainfo VALUES (NULL,'_parentKind','PARENT_KIND','ParentKind','Short','com.gridnode.pdip.base.time.entities.model.iCalAlarm', 'ical_alarm.parentKind', 0,'0','0','1','1','', 999, 'displayable=false','');
INSERT INTO fieldmetainfo VALUES (NULL,'_recurListStr','RECUR_LIST_STR','RecurListStr','java.lang.String','com.gridnode.pdip.base.time.entities.model.iCalAlarm', 'ical_alarm.recurListStr', 50,'0','0','1','1','', 999, 'displayable=false','');
INSERT INTO fieldmetainfo VALUES (NULL,'_isRecurComplete','IS_RECUR_COMPLETE','IsRecurComplete','java.lang.Boolean','com.gridnode.pdip.base.time.entities.model.iCalAlarm', 'ical_alarm.isRecurComplete', 0,'0','0','1','1','', 999, 'displayable=false','');
INSERT INTO fieldmetainfo VALUES (NULL,'_curRecur','CUR_RECUR','CurRecur','java.lang.String','com.gridnode.pdip.base.time.entities.model.iCalAlarm', 'ical_alarm.curRecur', 50,'0','0','1','1','', 999, 'displayable=false','');
INSERT INTO fieldmetainfo VALUES (NULL,'_isPseudoParent','IS_PSEUDO_PARENT','IsPseudoParent','java.lang.Boolean','com.gridnode.pdip.base.time.entities.model.iCalAlarm', 'ical_alarm.isPseudoParent', 0,'0','0','1','1','', 999, 'displayable=false','');
INSERT INTO fieldmetainfo VALUES (NULL,'_includeParentStartTime','INCLUDE_PARENT_START_TIME','IncludeParentStartTime','java.lang.Boolean','com.gridnode.pdip.base.time.entities.model.iCalAlarm', 'ical_alarm.includeParentStartTime', 0,'0','0','1','1','', 999, 'displayable=false','');
INSERT INTO fieldmetainfo VALUES (NULL,'_taskId','TASK_ID','TaskId','java.lang.String','com.gridnode.pdip.base.time.entities.model.iCalAlarm','ical_alarm.taskId',120,'0','0','1','1','',999,'displayable=false','type=text\r\ntext.length.max=120\r\n');




INSERT INTO fieldmetainfo VALUES (NULL,'_uId','UID','UID','java.lang.Long','com.gridnode.pdip.base.time.entities.model.iCalEvent','',0,'0','0','1','1','',999,'displayable=false','');
INSERT INTO fieldmetainfo VALUES (NULL,'_kind','KIND','Kind','java.lang.Short','com.gridnode.pdip.base.time.entities.model.iCalEvent','',0,'0','0','1','1','',999,'displayable=false','');
INSERT INTO fieldmetainfo VALUES (NULL,'_ownerId','OWNER_ID','OwnerId','java.lang.Integer','com.gridnode.pdip.base.time.entities.model.iCalEvent','',0,'0','0','1','1','',999,'displayable=false','');
INSERT INTO fieldmetainfo VALUES (NULL,'_classification','CLASSIFICATION','Classification','java.lang.String','com.gridnode.pdip.base.time.entities.model.iCalEvent','',50,'0','0','1','1','',999,'displayable=false','');
INSERT INTO fieldmetainfo VALUES (NULL,'_createDt','CREATE_DT','CreateDt','java.util.Date','com.gridnode.pdip.base.time.entities.model.iCalEvent','',0,'0','0','1','1','',999,'displayable=false','');
INSERT INTO fieldmetainfo VALUES (NULL,'_lastModifyDt','LAST_MODIFY_DT','LastModifyDt','java.util.Date','com.gridnode.pdip.base.time.entities.model.iCalEvent','',0,'0','0','1','1','',999,'displayable=false','');
INSERT INTO fieldmetainfo VALUES (NULL,'_isDateType','IS_DATE_TYPE','IsDateType','java.lang.Boolean','com.gridnode.pdip.base.time.entities.model.iCalEvent','',0,'0','0','1','1','',999,'displayable=false','');
INSERT INTO fieldmetainfo VALUES (NULL,'_isDtFloat','IS_DT_FLOAT','IsDtFloat','java.lang.Boolean','com.gridnode.pdip.base.time.entities.model.iCalEvent','',0,'0','0','1','1','',999,'displayable=false','');
INSERT INTO fieldmetainfo VALUES (NULL,'_startDt','START_DT','StartDt','java.util.Date','com.gridnode.pdip.base.time.entities.model.iCalEvent','',0,'0','0','1','1','',999,'displayable=false','');
INSERT INTO fieldmetainfo VALUES (NULL,'_priority','PRIORITY','Priority','java.lang.Integer','com.gridnode.pdip.base.time.entities.model.iCalEvent','',0,'0','0','1','1','',999,'displayable=false','');
INSERT INTO fieldmetainfo VALUES (NULL,'_sequenceNum','SEQUENCE_NUM','SequenceNum','java.lang.Integer','com.gridnode.pdip.base.time.entities.model.iCalEvent','',0,'0','0','1','1','',999,'displayable=false','');
INSERT INTO fieldmetainfo VALUES (NULL,'_status','STATUS','Status','java.lang.Integer','com.gridnode.pdip.base.time.entities.model.iCalEvent','',0,'0','0','1','1','',999,'displayable=false','');
INSERT INTO fieldmetainfo VALUES (NULL,'_iCalUid','iCal_UID','iCalUid','java.lang.String','com.gridnode.pdip.base.time.entities.model.iCalEvent','',50,'0','0','1','1','',999,'displayable=false','');
INSERT INTO fieldmetainfo VALUES (NULL,'_endDt','END_DT','EndDt','java.util.Date','com.gridnode.pdip.base.time.entities.model.iCalEvent','',0,'0','0','1','1','',999,'displayable=false','');
INSERT INTO fieldmetainfo VALUES (NULL,'_duration','DURATION','Duration','java.lang.Long','com.gridnode.pdip.base.time.entities.model.iCalEvent','',0,'0','0','1','1','',999,'displayable=false','');
INSERT INTO fieldmetainfo VALUES (NULL,'_propertiesStr','PROPERTIES_STR','PropertiesStr','java.lang.String','com.gridnode.pdip.base.time.entities.model.iCalEvent', 'ical_event.PropertiesStr', 50,'0','0','1','1','', 999, 'displayable=false','');
INSERT INTO fieldmetainfo VALUES (NULL,'_timeTransparency','TIME_TRANSPARENCY','TimeTransparency','java.lang.Integer','com.gridnode.pdip.base.time.entities.model.iCalEvent','',0,'0','0','1','1','',999,'displayable=false','');


INSERT INTO fieldmetainfo VALUES (NULL,'_uId','UID','UID','java.lang.Long','com.gridnode.pdip.base.time.entities.model.iCalTodo','',0,'0','0','1','1','',999,'displayable=false','');
INSERT INTO fieldmetainfo VALUES (NULL,'_kind','KIND','Kind','java.lang.Short','com.gridnode.pdip.base.time.entities.model.iCalTodo','',0,'0','0','1','1','',999,'displayable=false','');
INSERT INTO fieldmetainfo VALUES (NULL,'_ownerId','OWNER_ID','OwnerId','java.lang.Integer','com.gridnode.pdip.base.time.entities.model.iCalTodo','',0,'0','0','1','1','',999,'displayable=false','');
INSERT INTO fieldmetainfo VALUES (NULL,'_classification','CLASSIFICATION','Classification','java.lang.String','com.gridnode.pdip.base.time.entities.model.iCalTodo','',50,'0','0','1','1','',999,'displayable=false','');
INSERT INTO fieldmetainfo VALUES (NULL,'_createDt','CREATE_DT','CreateDt','java.util.Date','com.gridnode.pdip.base.time.entities.model.iCalTodo','',0,'0','0','1','1','',999,'displayable=false','');
INSERT INTO fieldmetainfo VALUES (NULL,'_lastModifyDt','LAST_MODIFY_DT','LastModifyDt','java.util.Date','com.gridnode.pdip.base.time.entities.model.iCalTodo','',0,'0','0','1','1','',999,'displayable=false','');
INSERT INTO fieldmetainfo VALUES (NULL,'_isDateType','IS_DATE_TYPE','IsDateType','java.lang.Boolean','com.gridnode.pdip.base.time.entities.model.iCalTodo','',0,'0','0','1','1','',999,'displayable=false','');
INSERT INTO fieldmetainfo VALUES (NULL,'_isDtFloat','IS_DT_FLOAT','IsDtFloat','java.lang.Boolean','com.gridnode.pdip.base.time.entities.model.iCalTodo','',0,'0','0','1','1','',999,'displayable=false','');
INSERT INTO fieldmetainfo VALUES (NULL,'_startDt','START_DT','StartDt','java.util.Date','com.gridnode.pdip.base.time.entities.model.iCalTodo','',0,'0','0','1','1','',999,'displayable=false','');
INSERT INTO fieldmetainfo VALUES (NULL,'_priority','PRIORITY','Priority','java.lang.Integer','com.gridnode.pdip.base.time.entities.model.iCalTodo','',0,'0','0','1','1','',999,'displayable=false','');
INSERT INTO fieldmetainfo VALUES (NULL,'_sequenceNum','SEQUENCE_NUM','SequenceNum','java.lang.Integer','com.gridnode.pdip.base.time.entities.model.iCalTodo','',0,'0','0','1','1','',999,'displayable=false','');
INSERT INTO fieldmetainfo VALUES (NULL,'_status','STATUS','Status','java.lang.Integer','com.gridnode.pdip.base.time.entities.model.iCalTodo','',0,'0','0','1','1','',999,'displayable=false','');
INSERT INTO fieldmetainfo VALUES (NULL,'_iCalUid','iCal_UID','iCalUid','java.lang.String','com.gridnode.pdip.base.time.entities.model.iCalTodo','',50,'0','0','1','1','',999,'displayable=false','');
INSERT INTO fieldmetainfo VALUES (NULL,'_endDt','END_DT','EndDt','java.util.Date','com.gridnode.pdip.base.time.entities.model.iCalTodo','',0,'0','0','1','1','',999,'displayable=false','');
INSERT INTO fieldmetainfo VALUES (NULL,'_duration','DURATION','Duration','java.lang.Long','com.gridnode.pdip.base.time.entities.model.iCalTodo','',0,'0','0','0','1','1','','displayable=false','');
INSERT INTO fieldmetainfo VALUES (NULL,'_completeDt','COMPLETE_DT','CompleteDt','java.util.Date','com.gridnode.pdip.base.time.entities.model.iCalTodo','',0,'0','0','1','1','',999,'displayable=false','');
INSERT INTO fieldmetainfo VALUES (NULL,'_propertiesStr','PROPERTIES_STR','PropertiesStr','java.lang.String','com.gridnode.pdip.base.time.entities.model.iCalTodo', 'ical_todo.PropertiesStr', 50,'0','0','1','1','', 999, 'displayable=false','');
INSERT INTO fieldmetainfo VALUES (NULL,'_percentCompleted','PERCENT_COMPLETED','PercentCompleted','java.lang.Integer','com.gridnode.pdip.base.time.entities.model.iCalTodo','',0,'0','0','1','1','',999,'displayable=false','');


INSERT INTO fieldmetainfo VALUES (NULL,'_uId','UID','UID','java.lang.Long','com.gridnode.pdip.base.time.entities.model.iCalProperty','',0,'0','0','1','1','',999,'displayable=false','');
INSERT INTO fieldmetainfo VALUES (NULL,'_kind','KIND','Kind','java.lang.Short','com.gridnode.pdip.base.time.entities.model.iCalProperty','',0,'0','0','1','1','',999,'displayable=false','');
INSERT INTO fieldmetainfo VALUES (NULL,'_compKind','COMP_KIND','CompKind','java.lang.Short','com.gridnode.pdip.base.time.entities.model.iCalProperty','',0,'0','0','1','1','',999,'displayable=false','');
INSERT INTO fieldmetainfo VALUES (NULL,'_iCalCompId','iCal_COMP_ID','iCalCompId','java.lang.Long','com.gridnode.pdip.base.time.entities.model.iCalProperty','',0,'0','0','1','1','',999,'displayable=false','');


INSERT INTO fieldmetainfo VALUES (NULL,'_uId','UID','UID','java.lang.Long','com.gridnode.pdip.base.time.entities.model.iCalInt','',0,'0','0','1','1','',999,'displayable=false','');
INSERT INTO fieldmetainfo VALUES (NULL,'_compKind','COMP_KIND','CompKind','java.lang.Short','com.gridnode.pdip.base.time.entities.model.iCalInt','',0,'0','0','1','1','',999,'displayable=false','');
INSERT INTO fieldmetainfo VALUES (NULL,'_propKind','PROP_KIND','PropKind','java.lang.Short','com.gridnode.pdip.base.time.entities.model.iCalInt','',0,'0','0','1','1','',999,'displayable=false','');
INSERT INTO fieldmetainfo VALUES (NULL,'_paramKind','PARAM_KIND','ParamKind','java.lang.Short','com.gridnode.pdip.base.time.entities.model.iCalInt','',0,'0','0','1','1','',999,'displayable=false','');
INSERT INTO fieldmetainfo VALUES (NULL,'_refKind','REF_KIND','RefKind','java.lang.Short','com.gridnode.pdip.base.time.entities.model.iCalInt','',0,'0','0','1','1','',999,'displayable=false','');
INSERT INTO fieldmetainfo VALUES (NULL,'_valueKind','VALUE_KIND','ValueKind','java.lang.Short','com.gridnode.pdip.base.time.entities.model.iCalInt','',0,'0','0','1','1','',999,'displayable=false','');
INSERT INTO fieldmetainfo VALUES (NULL,'_iCalCompId','iCal_COMP_ID','iCalCompId','java.lang.Long','com.gridnode.pdip.base.time.entities.model.iCalInt','',0,'0','0','1','1','',999,'displayable=false','');
INSERT INTO fieldmetainfo VALUES (NULL,'_refId','REF_ID','RefId','java.lang.Long','com.gridnode.pdip.base.time.entities.model.iCalInt','',0,'0','0','1','1','',999,'displayable=false','');
INSERT INTO fieldmetainfo VALUES (NULL,'_intValue','INT_VALUE','IntValue','java.lang.Integer','com.gridnode.pdip.base.time.entities.model.iCalInt','',0,'0','0','1','1','',999,'displayable=false','');


INSERT INTO fieldmetainfo VALUES (NULL,'_uId','UID','UID','java.lang.Long','com.gridnode.pdip.base.time.entities.model.iCalDate','',0,'0','0','1','1','',999,'displayable=false','');
INSERT INTO fieldmetainfo VALUES (NULL,'_compKind','COMP_KIND','CompKind','java.lang.Short','com.gridnode.pdip.base.time.entities.model.iCalDate','',0,'0','0','1','1','',999,'displayable=false','');
INSERT INTO fieldmetainfo VALUES (NULL,'_propKind','PROP_KIND','PropKind','java.lang.Short','com.gridnode.pdip.base.time.entities.model.iCalDate','',0,'0','0','1','1','',999,'displayable=false','');
INSERT INTO fieldmetainfo VALUES (NULL,'_paramKind','PARAM_KIND','ParamKind','java.lang.Short','com.gridnode.pdip.base.time.entities.model.iCalDate','',0,'0','0','1','1','',999,'displayable=false','');
INSERT INTO fieldmetainfo VALUES (NULL,'_refKind','REF_KIND','RefKind','java.lang.Short','com.gridnode.pdip.base.time.entities.model.iCalDate','',0,'0','0','1','1','',999,'displayable=false','');
INSERT INTO fieldmetainfo VALUES (NULL,'_valueKind','VALUE_KIND','ValueKind','java.lang.Short','com.gridnode.pdip.base.time.entities.model.iCalDate','',0,'0','0','1','1','',999,'displayable=false','');
INSERT INTO fieldmetainfo VALUES (NULL,'_iCalCompId','iCal_COMP_ID','iCalCompId','java.lang.Long','com.gridnode.pdip.base.time.entities.model.iCalDate','',0,'0','0','1','1','',999,'displayable=false','');
INSERT INTO fieldmetainfo VALUES (NULL,'_refId','REF_ID','RefId','java.lang.Long','com.gridnode.pdip.base.time.entities.model.iCalDate','',0,'0','0','1','1','',999,'displayable=false','');
INSERT INTO fieldmetainfo VALUES (NULL,'_dateValue','DATE_VALUE','DateValue','java.util.Date','com.gridnode.pdip.base.time.entities.model.iCalDate','',0,'0','0','1','1','',999,'displayable=false','');


INSERT INTO fieldmetainfo VALUES (NULL,'_uId','UID','UID','java.lang.Long','com.gridnode.pdip.base.time.entities.model.iCalString','',0,'0','0','1','1','',999,'displayable=false','');
INSERT INTO fieldmetainfo VALUES (NULL,'_compKind','COMP_KIND','CompKind','java.lang.Short','com.gridnode.pdip.base.time.entities.model.iCalString','',0,'0','0','1','1','',999,'displayable=false','');
INSERT INTO fieldmetainfo VALUES (NULL,'_propKind','PROP_KIND','PropKind','java.lang.Short','com.gridnode.pdip.base.time.entities.model.iCalString','',0,'0','0','1','1','',999,'displayable=false','');
INSERT INTO fieldmetainfo VALUES (NULL,'_paramKind','PARAM_KIND','ParamKind','java.lang.Short','com.gridnode.pdip.base.time.entities.model.iCalString','',0,'0','0','1','1','',999,'displayable=false','');
INSERT INTO fieldmetainfo VALUES (NULL,'_refKind','REF_KIND','RefKind','java.lang.Short','com.gridnode.pdip.base.time.entities.model.iCalString','',0,'0','0','1','1','',999,'displayable=false','');
INSERT INTO fieldmetainfo VALUES (NULL,'_valueKind','VALUE_KIND','ValueKind','java.lang.Short','com.gridnode.pdip.base.time.entities.model.iCalString','',0,'0','0','1','1','',999,'displayable=false','');
INSERT INTO fieldmetainfo VALUES (NULL,'_iCalCompId','iCal_COMP_ID','iCalCompId','java.lang.Long','com.gridnode.pdip.base.time.entities.model.iCalString','',0,'0','0','1','1','',999,'displayable=false','');
INSERT INTO fieldmetainfo VALUES (NULL,'_refId','REF_ID','RefId','java.lang.Long','com.gridnode.pdip.base.time.entities.model.iCalString','',0,'0','0','1','1','',999,'displayable=false','');
INSERT INTO fieldmetainfo VALUES (NULL,'_strValue','STR_VALUE','StrValue','java.lang.String','com.gridnode.pdip.base.time.entities.model.iCalString','',50,'0','0','1','1','',999,'displayable=false','');


INSERT INTO fieldmetainfo VALUES (NULL,'_uId','UID','UID','java.lang.Long','com.gridnode.pdip.base.time.entities.model.iCalText','',0,'0','0','1','1','',999,'displayable=false','');
INSERT INTO fieldmetainfo VALUES (NULL,'_compKind','COMP_KIND','CompKind','java.lang.Short','com.gridnode.pdip.base.time.entities.model.iCalText','',0,'0','0','1','1','',999,'displayable=false','');
INSERT INTO fieldmetainfo VALUES (NULL,'_propKind','PROP_KIND','PropKind','java.lang.Short','com.gridnode.pdip.base.time.entities.model.iCalText','',0,'0','0','1','1','',999,'displayable=false','');
INSERT INTO fieldmetainfo VALUES (NULL,'_paramKind','PARAM_KIND','ParamKind','java.lang.Short','com.gridnode.pdip.base.time.entities.model.iCalText','',0,'0','0','1','1','',999,'displayable=false','');
INSERT INTO fieldmetainfo VALUES (NULL,'_refKind','REF_KIND','RefKind','java.lang.Short','com.gridnode.pdip.base.time.entities.model.iCalText','',0,'0','0','1','1','',999,'displayable=false','');
INSERT INTO fieldmetainfo VALUES (NULL,'_valueKind','VALUE_KIND','ValueKind','java.lang.Short','com.gridnode.pdip.base.time.entities.model.iCalText','',0,'0','0','1','1','',999,'displayable=false','');
INSERT INTO fieldmetainfo VALUES (NULL,'_iCalCompId','iCal_COMP_ID','iCalCompId','java.lang.Long','com.gridnode.pdip.base.time.entities.model.iCalText','',0,'0','0','1','1','',999,'displayable=false','');
INSERT INTO fieldmetainfo VALUES (NULL,'_refId','REF_ID','RefId','java.lang.Long','com.gridnode.pdip.base.time.entities.model.iCalText','',0,'0','0','1','1','',999,'displayable=false','');
INSERT INTO fieldmetainfo VALUES (NULL,'_textValue','TEXT_VALUE','TextValue','java.lang.String','com.gridnode.pdip.base.time.entities.model.iCalText','',50,'0','0','1','1','',999,'displayable=false','');

SET client_encoding = 'UTF8';
SET check_function_bodies = false;
SET client_min_messages = warning;
SET search_path = appdb;

DELETE FROM "entitymetainfo" WHERE "EntityName" IN ('GWFWorkListValueEntity','GWFWorkListUserEntity');
INSERT INTO "entitymetainfo" VALUES ('com.gridnode.pdip.base.worklist.entities.model.GWFWorkListValueEntity','GWFWorkListValueEntity','"worklistvalue"');
INSERT INTO "entitymetainfo" VALUES ('com.gridnode.pdip.base.worklist.entities.model.GWFWorkListUserEntity','GWFWorkListUserEntity','"worklistuser"');

---
--- data for table 'fieldmetainfo'
---
--------- GWFWorkListValueEntity
DELETE FROM "fieldmetainfo" WHERE "EntityObjectName" LIKE '%GWFWorkListValueEntity';
INSERT INTO "fieldmetainfo" VALUES (DEFAULT,'_uId','UID','"UID"','java.lang.Long','com.gridnode.pdip.base.worklist.entities.model.GWFWorkListValueEntity','',0,0,0,1,1,'',999,'displayable=false','');
INSERT INTO "fieldmetainfo" VALUES (DEFAULT,'iCal_wi_description','ICAL_WI_DESCRIPTION','"wi_description"','java.lang.String','com.gridnode.pdip.base.worklist.entities.model.GWFWorkListValueEntity','Description',30,0,1,1,1,'desc',2,'displayable=false','');
INSERT INTO "fieldmetainfo" VALUES (DEFAULT,'iCal_comments','ICAL_COMMENTS','"wi_comments"','java.lang.String','com.gridnode.pdip.base.worklist.entities.model.GWFWorkListValueEntity','Document_type',30,0,1,0,1,'dcoType',1,'displayable=false','');
INSERT INTO "fieldmetainfo" VALUES (DEFAULT,'iCal_reqst_status','ICAL_REQST_STATUS','"wi_status"','java.lang.Boolean','com.gridnode.pdip.base.worklist.entities.model.GWFWorkListValueEntity','30',0,0,0,0,0,'uId',999,'displayable=false','');
INSERT INTO "fieldmetainfo" VALUES (DEFAULT,'iCal_Creation_DT','ICAL_CREATION_DT','"wi_cdate"','java.util.Date','com.gridnode.pdip.base.worklist.entities.model.GWFWorkListValueEntity','30',0,0,0,0,0,'canDelete',999,'displayable=false','');
INSERT INTO "fieldmetainfo" VALUES (DEFAULT,'user_id','USER_ID','"user_id"','java.lang.String','com.gridnode.pdip.base.worklist.entities.model.GWFWorkListValueEntity','30',10,0,0,0,0,'',999,'displayable=false','');
INSERT INTO "fieldmetainfo" VALUES (DEFAULT,'unassigned','UNASSIGNED','"unassigned"','java.lang.String','com.gridnode.pdip.base.worklist.entities.model.GWFWorkListValueEntity','30',10,0,0,0,0,'desc',999,'displayable=false','');
INSERT INTO "fieldmetainfo" VALUES (DEFAULT,'processDefKey','PROCESSDEF_KEY','"processDefKey"','java.lang.String','com.gridnode.pdip.base.worklist.entities.model.GWFWorkListValueEntity','80',10,0,0,0,0,'',999,'displayable=false','');
INSERT INTO "fieldmetainfo" VALUES (DEFAULT,'activityId','ACTIVITY_ID','"activityId"','java.lang.String','com.gridnode.pdip.base.worklist.entities.model.GWFWorkListValueEntity','30',10,0,0,0,0,'',999,'displayable=false','');
INSERT INTO "fieldmetainfo" VALUES (DEFAULT,'performer','PERFORMER','"performer"','java.lang.String','com.gridnode.pdip.base.worklist.entities.model.GWFWorkListValueEntity','30',10,0,0,0,0,'',999,'displayable=false','');
INSERT INTO "fieldmetainfo" VALUES (DEFAULT,'rtActivityUId','RTACTIVITY_UID','"rtActivityUId"','java.lang.Long','com.gridnode.pdip.base.worklist.entities.model.GWFWorkListValueEntity','',0,0,0,1,1,'',999,'displayable=false','');
INSERT INTO "fieldmetainfo" VALUES (DEFAULT,'contextUId','CONTEXT_UID','"contextUId"','java.lang.Long','com.gridnode.pdip.base.worklist.entities.model.GWFWorkListValueEntity','',0,0,0,1,1,'',999,'displayable=false','');

--------- GWFWorkListUserEntity
DELETE FROM "fieldmetainfo" WHERE "EntityObjectName" LIKE '%GWFWorkListUserEntity';
INSERT INTO "fieldmetainfo" VALUES (DEFAULT,'_uId','UID','"UID"','java.lang.Long','com.gridnode.pdip.base.worklist.entities.model.GWFWorkListUserEntity',NULL,0,0,0,1,1,'desc',999,'displayable=false','');
INSERT INTO "fieldmetainfo" VALUES (DEFAULT,'workitem_id','WORKITEM_ID','"workitem_id"','java.lang.Integer','com.gridnode.pdip.base.worklist.entities.model.GWFWorkListUserEntity','Document_type',30,0,1,0,1,'dcoType',1,'displayable=false','');
INSERT INTO "fieldmetainfo" VALUES (DEFAULT,'user_id','USER_ID','"user_id"','java.lang.String','com.gridnode.pdip.base.worklist.entities.model.GWFWorkListUserEntity','Document_type',30,0,1,0,1,'dcoType',1,'displayable=false','');

# MySQL-Front Dump 2.1
#
# Host: 127.0.0.1   Database: appdb
#--------------------------------------------------------
# Server version 4.0.0-alpha

use appdb;

#
# Dumping data for table 'entitymetainfo'
#

INSERT INTO entitymetainfo VALUES ('com.gridnode.pdip.base.worklist.entities.model.GWFWorkListValueEntity','GWFWorkListValueEntity','worklistvalue');
INSERT INTO entitymetainfo VALUES ('com.gridnode.pdip.base.worklist.entities.model.GWFWorkListUserEntity','GWFWorkListUserEntity','worklistuser');

#
# Dumping data for table 'fieldmetainfo'
#

INSERT INTO fieldmetainfo VALUES (NULL,'_uId','UID','UID','java.lang.Long','com.gridnode.pdip.base.worklist.entities.model.GWFWorkListValueEntity','',0,'0','0','1','1','',999,'displayable=false','');
INSERT INTO fieldmetainfo VALUES (NULL,'iCal_wi_description','ICAL_WI_DESCRIPTION','wi_description','java.lang.String','com.gridnode.pdip.base.worklist.entities.model.GWFWorkListValueEntity','Description',30,'0','1','1','1','desc',2,'displayable=false','');
INSERT INTO fieldmetainfo VALUES (NULL,'iCal_comments','ICAL_COMMENTS','wi_comments','java.lang.String','com.gridnode.pdip.base.worklist.entities.model.GWFWorkListValueEntity','Document_type',30,'0','1','0','1','dcoType',1,'displayable=false','');
INSERT INTO fieldmetainfo VALUES (NULL,'iCal_reqst_status','ICAL_REQST_STATUS','wi_status','java.lang.Boolean','com.gridnode.pdip.base.worklist.entities.model.GWFWorkListValueEntity','30',0,'0','0','0','0','uId',999,'displayable=false','');
INSERT INTO fieldmetainfo VALUES (NULL,'iCal_Creation_DT','ICAL_CREATION_DT','wi_cdate','java.util.Date','com.gridnode.pdip.base.worklist.entities.model.GWFWorkListValueEntity','30',0,'0','0','0','0','canDelete',999,'displayable=false','');
INSERT INTO fieldmetainfo VALUES (NULL,'user_id','USER_ID','user_id','java.lang.String','com.gridnode.pdip.base.worklist.entities.model.GWFWorkListValueEntity','30',10,'0','0','0','0','',999,'displayable=false','');
INSERT INTO fieldmetainfo VALUES (NULL,'unassigned','UNASSIGNED','unassigned','java.lang.String','com.gridnode.pdip.base.worklist.entities.model.GWFWorkListValueEntity','30',10,'0','0','0','0','desc',999,'displayable=false','');
INSERT INTO fieldmetainfo VALUES (NULL,'processDefKey','PROCESSDEF_KEY','processDefKey','java.lang.String','com.gridnode.pdip.base.worklist.entities.model.GWFWorkListValueEntity','80',10,'0','0','0','0','',999,'displayable=false','');
INSERT INTO fieldmetainfo VALUES (NULL,'activityId','ACTIVITY_ID','activityId','java.lang.String','com.gridnode.pdip.base.worklist.entities.model.GWFWorkListValueEntity','30',10,'0','0','0','0','',999,'displayable=false','');
INSERT INTO fieldmetainfo VALUES (NULL,'performer','PERFORMER','performer','java.lang.String','com.gridnode.pdip.base.worklist.entities.model.GWFWorkListValueEntity','30',10,'0','0','0','0','',999,'displayable=false','');
INSERT INTO fieldmetainfo VALUES (NULL,'rtActivityUId','RTACTIVITY_UID','rtActivityUId','java.lang.Long','com.gridnode.pdip.base.worklist.entities.model.GWFWorkListValueEntity','',0,'0','0','1','1','',999,'displayable=false','');
INSERT INTO fieldmetainfo VALUES (NULL,'contextUId','CONTEXT_UID','contextUId','java.lang.Long','com.gridnode.pdip.base.worklist.entities.model.GWFWorkListValueEntity','',0,'0','0','1','1','',999,'displayable=false','');


INSERT INTO fieldmetainfo VALUES (NULL,'_uId','UID','UID','java.lang.Long','com.gridnode.pdip.base.worklist.entities.model.GWFWorkListUserEntity',NULL,0,'0','0','1','1','desc',999,'displayable=false','');
INSERT INTO fieldmetainfo VALUES (NULL,'workitem_id','WORKITEM_ID','workitem_id','java.lang.Integer','com.gridnode.pdip.base.worklist.entities.model.GWFWorkListUserEntity','Document_type',30,'0','1','0','1','dcoType',1,'displayable=false','');
INSERT INTO fieldmetainfo VALUES (NULL,'user_id','USER_ID','user_id','java.lang.String','com.gridnode.pdip.base.worklist.entities.model.GWFWorkListUserEntity','Document_type',30,'0','1','0','1','dcoType',1,'displayable=false','');


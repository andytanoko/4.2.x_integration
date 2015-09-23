SET client_encoding = 'UTF8';
SET check_function_bodies = false;
SET client_min_messages = warning;
SET search_path = appdb;

DELETE FROM "entitymetainfo" WHERE "EntityName" = 'SessionAudit';
INSERT INTO "entitymetainfo" VALUES('com.gridnode.pdip.base.session.model.SessionAudit','SessionAudit','"session_audit"');


--------- SessionAudit
DELETE FROM "fieldmetainfo" WHERE "EntityObjectName" LIKE '%SessionAudit';
INSERT INTO "fieldmetainfo" VALUES(DEFAULT,'_uId','UID','"UID"','java.lang.Long','com.gridnode.pdip.base.session.model.SessionAudit','session.uid',0,0,0,0,0,'0',999,'','');
INSERT INTO "fieldmetainfo" VALUES(DEFAULT,'_sessionId','SESSION_ID','"SessionId"','java.lang.String','com.gridnode.pdip.base.session.model.SessionAudit','session.sessionId',0,0,0,0,1,'0',1,'','');
INSERT INTO "fieldmetainfo" VALUES(DEFAULT,'_sessionName','SESSION_NAME','"SessionName"','java.lang.String','com.gridnode.pdip.base.session.model.SessionAudit','session.sessionName',0,0,0,1,1,'0',2,'','');
INSERT INTO "fieldmetainfo" VALUES(DEFAULT,'_state','STATE','"State"','java.lang.Short','com.gridnode.pdip.base.session.model.SessionAudit','session.state',0,0,0,1,1,'0',3,'','');
INSERT INTO "fieldmetainfo" VALUES(DEFAULT,'_sessionData','SESSION_DATA','"SessionData"','byte[]','com.gridnode.pdip.base.session.model.SessionAudit','session.sessionData',0,0,0,1,0,'0',4,'','');
INSERT INTO "fieldmetainfo" VALUES(DEFAULT,'_openTime','OPEN_TIME','"OpenTime"','java.util.Date','com.gridnode.pdip.base.session.model.SessionAudit','session.openTime',0,0,0,0,1,'0',5,'','');
INSERT INTO "fieldmetainfo" VALUES(DEFAULT,'_lastActiveTime','LAST_ACTIVE_TIME','"LastActiveTime"','java.util.Date','com.gridnode.pdip.base.session.model.SessionAudit','session.lastActiveTime',0,0,0,0,1,'0',6,'','');
INSERT INTO "fieldmetainfo" VALUES(DEFAULT,'_destroyTime','DESTROY_TIME','"DestroyTime"','java.util.Date','com.gridnode.pdip.base.session.model.SessionAudit','session.destroyTime',0,0,0,0,1,'0',7,'','');

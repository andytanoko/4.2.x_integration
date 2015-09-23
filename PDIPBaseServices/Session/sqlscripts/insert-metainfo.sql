# MySQL-Front DUMP 2.1
#
# HOST: localhost   DATABASE: appdb
#--------------------------------------------------------
# Server version 4.0.0-alpha

USE appdb;

# Dumping data FOR TABLE 'entitymetainfo'
INSERT INTO entitymetainfo VALUES("com.gridnode.pdip.base.session.model.SessionAudit","SessionAudit","session_audit");

# Dumping data FOR TABLE 'fieldmetainfo'


INSERT INTO fieldmetainfo VALUES(NULL,"_uId","UID","UID","java.lang.Long","com.gridnode.pdip.base.session.model.SessionAudit","session.uid","0","0","0","0","0","0","999","","");
INSERT INTO fieldmetainfo VALUES(NULL,"_sessionId","SESSION_ID","SessionId","java.lang.String","com.gridnode.pdip.base.session.model.SessionAudit","session.sessionId","0","0","0","0","1","0","1","","");
INSERT INTO fieldmetainfo VALUES(NULL,"_sessionName","SESSION_NAME","SessionName","java.lang.String","com.gridnode.pdip.base.session.model.SessionAudit","session.sessionName","0","0","0","1","1","0","2","","");
INSERT INTO fieldmetainfo VALUES(NULL,"_state","STATE","State","java.lang.Short","com.gridnode.pdip.base.session.model.SessionAudit","session.state","0","0","0","1","1","0","3","","");
INSERT INTO fieldmetainfo VALUES(NULL,"_sessionData","SESSION_DATA","SessionData","byte[]","com.gridnode.pdip.base.session.model.SessionAudit","session.sessionData","0","0","0","1","0","0","4","","");
INSERT INTO fieldmetainfo VALUES(NULL,"_openTime","OPEN_TIME","OpenTime","java.util.Date","com.gridnode.pdip.base.session.model.SessionAudit","session.openTime","0","0","0","0","1","0","5","","");
INSERT INTO fieldmetainfo VALUES(NULL,"_lastActiveTime","LAST_ACTIVE_TIME","LastActiveTime","java.util.Date","com.gridnode.pdip.base.session.model.SessionAudit","session.lastActiveTime","0","0","0","0","1","0","6","","");
INSERT INTO fieldmetainfo VALUES(NULL,"_destroyTime","DESTROY_TIME","DestroyTime","java.util.Date","com.gridnode.pdip.base.session.model.SessionAudit","session.destroyTime","0","0","0","0","1","0","7","","");

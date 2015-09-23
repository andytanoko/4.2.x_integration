USE appdb;

#
# Dumping data FOR TABLE 'entitymetainfo'
#
DELETE FROM entitymetainfo WHERE EntityName IN ("UserAccount","UserAccountState");
INSERT INTO entitymetainfo VALUES("com.gridnode.pdip.app.user.model.UserAccount","UserAccount","user_account");
INSERT INTO entitymetainfo VALUES("com.gridnode.pdip.app.user.model.UserAccountState","UserAccountState","user_account_state");


#
# Dumping data FOR TABLE 'fieldmetainfo'
#

### UserAccount
DELETE FROM fieldmetainfo WHERE EntityObjectName LIKE "%UserAccount";
INSERT INTO fieldmetainfo VALUES(NULL,"_uId","UID","UID","java.lang.Long","com.gridnode.pdip.app.user.model.UserAccount","user.uid","20","0","0","0","0","0","999","displayable=false\r\nmandatory=false\r\neditable=false\r\n","type=uid");
INSERT INTO fieldmetainfo VALUES(NULL,"_id","ID","ID","java.lang.String","com.gridnode.pdip.app.user.model.UserAccount","user.userId","15","0","1","1","1","0","1","displayable=true\r\nmandatory=true\r\neditable=false\r\neditable.create=true\r\n","type=text\r\ntext.length.min=2\r\ntext.length.max=15");
INSERT INTO fieldmetainfo VALUES(NULL,"_name","NAME","Name","java.lang.String","com.gridnode.pdip.app.user.model.UserAccount","user.userName","50","0","1","1","1","0","2","displayable=true\r\nmandatory=true\r\neditable=true\r\n","type=text\r\ntext.length.min=2\r\ntext.length.max=50\r\n");
INSERT INTO fieldmetainfo VALUES(NULL,"_password","PASSWORD","Password","java.lang.String","com.gridnode.pdip.app.user.model.UserAccount","user.password","12","1","1","1","0","0","999","displayable=true\r\neditable=true\r\nmandatory=true\r\n","type=text\r\ntext.length.min=6\r\ntext.length.max=12\r\n");
INSERT INTO fieldmetainfo VALUES(NULL,"_phone","PHONE","Phone","java.lang.String","com.gridnode.pdip.app.user.model.UserAccount","user.phone","16","0","0","1","1","0","3","displayable=true\r\neditable=true\r\nmandatory=false\r\n","type=text\r\ntext.length.max=16\r\n");
INSERT INTO fieldmetainfo VALUES(NULL,"_email","EMAIL","Email","java.lang.String","com.gridnode.pdip.app.user.model.UserAccount","user.email","255","0","0","1","1","0","4","displayable=true\r\neditable=true\r\nmandatory=false\r\n","type=text\r\ntext.length.max=255\r\n");
INSERT INTO fieldmetainfo VALUES(NULL,"_property","PROPERTY","Property","java.lang.String","com.gridnode.pdip.app.user.model.UserAccount","user.property","255","0","0","1","1","0","5","displayable=true\r\neditable=true\r\nmandatory=false\r\n","type=text\r\ntext.length.max=255\r\n");
INSERT INTO fieldmetainfo VALUES(NULL,"_accountState","ACCOUNT_STATE","","com.gridnode.pdip.app.user.model.UserAccountState","com.gridnode.pdip.app.user.model.UserAccount","user.accountState","0","1","0","1","0","0","999","","type=embedded\r\nembedded.type=accountState\r\n");
INSERT INTO fieldmetainfo VALUES(NULL,"_version","VERSION","Version","java.lang.Double","com.gridnode.pdip.app.user.model.UserAccount","user.version","0","0","0","0","0","0","999","displayable=false\r\neditable=false\r\nmandatory=false\r\n","type=range\r\n");

### UserAccountState
DELETE FROM fieldmetainfo WHERE EntityObjectName LIKE "%UserAccountState";
INSERT INTO fieldmetainfo VALUES(NULL,"_uId","UID","UID","java.lang.Long","com.gridnode.pdip.app.user.model.UserAccountState","accountState.uid","20","0","0","0","0","0","999","displayable=false\r\neditable=false\r\nmandatory=false\r\n","type=uid\r\n");
INSERT INTO fieldmetainfo VALUES(NULL,"_userID","USER_ID","UserID","java.lang.String","com.gridnode.pdip.app.user.model.UserAccountState","accountState.userId","15","0","0","0","1","0","1","displayable=true\r\neditable=false\r\nmandatory=false\r\n\r\n","type=foreign\r\nforeign.key=user.userId\r\nforeign.display=user.userName\r\nforeign.cached=false\r\n\r\n");
INSERT INTO fieldmetainfo VALUES(NULL,"_state","STATE","State","java.lang.Short","com.gridnode.pdip.app.user.model.UserAccountState","accountState.state","1","0","1","1","1","0","2","displayable=true\r\neditable=true\r\nmandatory=true\r\n\r\n","type=enum\r\naccountState.state.disabled=0\r\naccountState.state.enabled=1\r\n");
INSERT INTO fieldmetainfo VALUES(NULL,"_numLoginTries","NUM_LOGIN_TRIES","NumLoginTries","java.lang.Short","com.gridnode.pdip.app.user.model.UserAccountState","accountState.loginAttempts","2","0","0","1","1","0","3","displayable=true\r\neditable=false\r\nmandatory=false\r\n\r\n","type=range\r\nrange.min=0\r\n");
INSERT INTO fieldmetainfo VALUES(NULL,"_isFreeze","IS_FREEZE","IsFreeze","java.lang.Boolean","com.gridnode.pdip.app.user.model.UserAccountState","accountState.frozen","0","0","0","1","1","0","4","displayable=true\r\neditable=true\r\nmandatory=true\r\n\r\n","type=enum\r\naccountState.frozen.freeze=true\r\naccountState.frozen.unfreeze=false\r\n");
INSERT INTO fieldmetainfo VALUES(NULL,"_freezeTime","FREEZE_TIME","FreezeTime","java.util.Date","com.gridnode.pdip.app.user.model.UserAccountState","accountState.freezeTime","30","0","0","1","1","0","5","displayable=true\r\neditable=false\r\nmandatory=false\r\n","type=datetime\r\ndatetime.date=true\r\ndatetime.time=true\r\n");
INSERT INTO fieldmetainfo VALUES(NULL,"_lastLoginTime","LAST_LOGIN_TIME","LastLoginTime","java.util.Date","com.gridnode.pdip.app.user.model.UserAccountState","accountState.lastLoginTime","30","0","0","0","1","0","6","displayable=true\r\neditable=false\r\nmandatory=false\r\n","type=datetime\r\ndatetime.date=true\r\ndatetime.time=true\r\n");
INSERT INTO fieldmetainfo VALUES(NULL,"_lastLogoutTime","LAST_LOGOUT_TIME","LastLogoutTime","java.util.Date","com.gridnode.pdip.app.user.model.UserAccountState","accountState.lastLogoutTime","30","0","0","0","1","0","7","displayable=true\r\neditable=false\r\nmandatory=false\r\n","type=datetime\r\ndatetime.date=true\r\ndatetime.time=true\r\n");
INSERT INTO fieldmetainfo VALUES(NULL,"_createTime","CREATE_TIME","CreateTime","java.util.Date","com.gridnode.pdip.app.user.model.UserAccountState","accountState.created","30","0","0","0","1","0","8","displayable=true\r\neditable=false\r\nmandatory=false\r\n","type=datetime\r\ndatetime.date=true\r\ndatetime.time=true\r\n");
INSERT INTO fieldmetainfo VALUES(NULL,"_createBy","CREATE_BY","CreateBy","java.lang.String","com.gridnode.pdip.app.user.model.UserAccountState","accountState.createdBy","15","0","0","0","1","0","9","displayable=true\r\neditable=false\r\nmandatory=false\r\n","type=text\r\n");
INSERT INTO fieldmetainfo VALUES(NULL,"_canDelete","CAN_DELETE","CanDelete","java.lang.Boolean","com.gridnode.pdip.app.user.model.UserAccountState","accountState.canDelete","0","0","0","0","0","0","999","displayable=true\r\neditable=false\r\nmandatory=false\r\n","type=enum\r\ngeneric.yes=true\r\ngeneric.no=false\r\n");

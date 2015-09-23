# 13 Jul 2004 v2.4  [Mahesh] Add EmailConfig entity


USE appdb;

DELETE FROM entitymetainfo WHERE EntityName IN
("EmailConfig");

INSERT INTO entitymetainfo VALUES("com.gridnode.pdip.app.alert.model.EmailConfig","EmailConfig",NULL);


# FieldMetaInfo for EmailConfig
DELETE FROM fieldmetainfo WHERE EntityObjectName LIKE "%EmailConfig";

INSERT INTO fieldmetainfo VALUES
(NULL,"_smtpServerHost","SMTP_SERVER_HOST",NULL,"java.lang.String","com.gridnode.pdip.app.alert.model.EmailConfig","emailConfig.smtpServerHost","80","0","1","1","1","0","999","displayable=true\r\nmandatory=true\r\neditable=true","type=text\r\ntext.length.max=80");

INSERT INTO fieldmetainfo VALUES
(NULL,"_smtpServerPort","SMTP_SERVER_PORT",NULL,"java.lang.Long","com.gridnode.pdip.app.alert.model.EmailConfig","emailConfig.smtpServerPort","0","0","0","1","1",NULL,"999","displayable=true\r\nmandatory=true\r\neditable=true","type=range");

INSERT INTO fieldmetainfo VALUES
(NULL,"_authUser","AUTH_USER",NULL,"java.lang.String","com.gridnode.pdip.app.alert.model.EmailConfig","emailConfig.authUser","80","0","1","1","1","0","999","displayable=true\r\nmandatory=false\r\neditable=true","type=text\r\ntext.length.max=80");

INSERT INTO fieldmetainfo VALUES
(NULL,"_authPassword","AUTH_PASSWORD",NULL,"java.lang.String","com.gridnode.pdip.app.alert.model.EmailConfig","emailConfig.authPassword","80","0","1","1","1","0","999","displayable=true\r\nmandatory=false\r\neditable=true","type=text\r\ntext.length.max=80");

INSERT INTO fieldmetainfo VALUES
(NULL,"_retryInterval","RETRY_INTERVAL",NULL,"java.lang.Long","com.gridnode.pdip.app.alert.model.EmailConfig","emailConfig.retryInterval","0","0","0","1","1",NULL,"999","displayable=true\r\nmandatory=true\r\neditable=true","type=range\r\nrange.min=180");

INSERT INTO fieldmetainfo VALUES
(NULL,"_maxRetries","MAX_RETRIES",NULL,"java.lang.Integer","com.gridnode.pdip.app.alert.model.EmailConfig","emailConfig.maxRetries","0","0","0","1","1",NULL,"999","displayable=true\r\nmandatory=true\r\neditable=true","type=range");


INSERT INTO fieldmetainfo VALUES
(NULL, "_saveFailedEmails", "SAVE_FAILED_EMAILS", NULL, "java.lang.Boolean", "com.gridnode.pdip.app.alert.model.EmailConfig", "emailConfig.saveFailedEmails", "0", "0", "0", "1", "1", NULL, 999, "displayable=true\r\nmandatory=true\r\n\editable=true", "type=enum\r\ngeneric.yes=true\r\ngeneric.no=false");



USE userdb;

# ical_alarm to retry failed emails
INSERT INTO ical_alarm (UID,StartDuration,StartDt,Related,DelayPeriod,Repeat,Category,SenderKey,ReceiverKey,Disabled,NextDueTime,Count,ParentUid,ParentKind,RecurListStr,IsRecurComplete,CurRecur,IsPseudoParent,IncludeParentStartTime,TaskId) VALUES
(-10,"","2004-07-14 15:20:13","","300",NULL,"EmailConfig","EmailConfig","EmailConfig","","","","","","","","","","","");
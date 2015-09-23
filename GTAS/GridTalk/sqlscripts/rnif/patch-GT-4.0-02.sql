# 20 Jan 2006 GT 4.0 I1 [Neo Sok Lay] Set user tracking identifier xpath for failure notification

USE userdb;

update process_def
set UserTrackingIdentifier='//Pip0A1FailureNotification/ProcessIdentity/InstanceIdentifier'
where DefName in ('0A1FailureNotification1.1','0A1FailureNotification');

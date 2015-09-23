# Change Log
# 22 Jul 2003 GT 2.2 I1 [Neo Sok Lay] Set CanDelete to false for default users.
# 17 Oct 2005 GT 4.0 I1 [Neo Sok Lay] Set FreezeTime default to NULL.

USE userdb;

#
# Default User Accounts
#
DELETE FROM user_account WHERE UID IN (-1,-2) OR ID IN ("admin","guest");
INSERT INTO user_account
(UID,Name,ID,Password,Phone,Email,Property,Version) VALUES
("-1","Administrator","admin","1N3BRqvwF7dN","12345678","admin@gridnode.com","This account belongs to admin user","1"),
("-2","Guest User","guest","1IMDd9CLvmxP","12345678","","This account can be used by any guest of GridTalk","1")
;

#
# Default User Account States
#
DELETE FROM user_account_state WHERE UID IN (-1,-2) OR UserID IN ("admin","guest");
INSERT INTO user_account_state
(UID,UserID,NumLoginTries,IsFreeze,FreezeTime,LastLoginTime,LastLogoutTime,State,CanDelete,CreateTime,CreateBy) VALUES
("-1","admin","0","0",NULL,NULL,NULL,"1","0",NULL,"SYSTEM"),
("-2","guest","0","0",NULL,NULL,NULL,"1","0",NULL,"SYSTEM")
;

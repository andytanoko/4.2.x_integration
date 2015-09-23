SET client_encoding = 'UTF8';
SET check_function_bodies = false;
SET client_min_messages = warning;
SET search_path = userdb;

--- user_account
DELETE FROM "user_account" WHERE "UID" IN (-1,-2) OR "ID" IN ('admin','guest');
INSERT INTO "user_account" ("UID","Name","ID","Password","Phone","Email","Property","Version") VALUES (-1,'Administrator','admin','1N3BRqvwF7dN','12345678','admin@gridnode.com','This account belongs to admin user',1);
INSERT INTO "user_account" ("UID","Name","ID","Password","Phone","Email","Property","Version") VALUES (-2,'Guest User','guest','1IMDd9CLvmxP','12345678','','This account can be used by any guest of GridTalk',1);


--- user_account_state
DELETE FROM "user_account_state" WHERE "UID" IN (-1,-2) OR "UserID" IN ('admin','guest');
INSERT INTO "user_account_state" ("UID","UserID","NumLoginTries","IsFreeze","FreezeTime","LastLoginTime","LastLogoutTime","State","CanDelete","CreateTime","CreateBy") VALUES (-1,'admin',0,0,NULL,NULL,NULL,1,0,NULL,'SYSTEM');
INSERT INTO "user_account_state" ("UID","UserID","NumLoginTries","IsFreeze","FreezeTime","LastLoginTime","LastLogoutTime","State","CanDelete","CreateTime","CreateBy") VALUES (-2,'guest',0,0,NULL,NULL,NULL,1,0,NULL,'SYSTEM');


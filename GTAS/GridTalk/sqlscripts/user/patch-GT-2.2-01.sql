# 22 Jul 2003 GT 2.2 I1 [Neo Sok Lay] Set CanDelete to false for default users.

USE userdb;

UPDATE user_account_state
SET CanDelete='0'
WHERE UserID IN ("admin","guest");


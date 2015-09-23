# Oct 17 2005 GT 4.0 [Neo Sok Lay] Set the freezetime to null to prevent '0000-00-00 00:00' timestamp error.
USE userdb;

UPDATE user_account_state
SET FreezeTime=NULL
WHERE IsFreeze=0;

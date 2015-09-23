# 26 Jun 2003 I1 v2.1    [Neo Sok Lay] Update alert.Category and alert.Trigger fields to NULL.
#

USE userdb;

UPDATE alert
SET Category=NULL,Trigger=NULL;

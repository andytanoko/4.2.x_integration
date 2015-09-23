# 19 May 2003 GT 2.1 I1  [Neo Sok Lay] Expand xpdl_subflow.ActualParameters column size to 255 
#

USE userdb;

ALTER TABLE xpdl_subflow
MODIFY COLUMN ActualParameters varchar(255);


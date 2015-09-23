# 08 Sep 2003  Jianyu 
USE userdb;

ALTER TABLE rn_profile
ADD COLUMN InResponseToActionID varchar(80) default NULL;
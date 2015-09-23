# 29 FEB 2008 GT 4.0.2.3   [Tam Wei Xiang] Expand ProcessInstanceId of rn_profile to 255 chars
#

USE userdb;
ALTER TABLE rn_profile MODIFY ProcessInstanceId varchar(255);
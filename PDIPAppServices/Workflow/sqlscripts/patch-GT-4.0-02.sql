# 29 FEB 2008 GT 4.0.2.3   [Tam Wei Xiang] Expand ProcessInstanceId of rtprocessdoc to 255 chars
#

USE userdb;
ALTER TABLE rtprocessdoc MODIFY DocumentId varchar(300);
# 11 Jan 2006      [Tam Wei Xiang]      Remove the field flag 'unsigned' if the field type is int or smallint

USE userdb;
ALTER TABLE pf_trigger MODIFY NumOfRetries smallint DEFAULT '0';
ALTER TABLE pf_trigger MODIFY RetryInterval int DEFAULT '0';
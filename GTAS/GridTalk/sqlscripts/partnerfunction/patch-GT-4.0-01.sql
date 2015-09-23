# 11 Jan 2006      [Tam Wei Xiang]      Remove the field flag 'unsigned' if the field type is int or smallint

USE userdb;
ALTER TABLE partner_function MODIFY TriggerOn smallint(2) NOT NULL DEFAULT '0';
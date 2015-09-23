# 11 Jan 2006      [Tam Wei Xiang]      Remove the field flag 'unsigned' if the field type is int or smallint

USE userdb;
ALTER TABLE business_entity MODIFY State smallint(2) NOT NULL DEFAULT '0';
ALTER TABLE registry_object_map MODIFY State smallint(2) NOT NULL DEFAULT '0';
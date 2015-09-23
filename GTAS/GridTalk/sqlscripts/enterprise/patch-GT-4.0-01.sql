# 11 Jan 2006      [Tam Wei Xiang]      Remove the field flag 'unsigned' if the field type is int or smallint

use userdb;
ALTER TABLE resource_link MODIFY Priority smallint(5) NOT NULL DEFAULT '0';
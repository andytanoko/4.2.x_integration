# 11 Jan 2006      [Tam Wei Xiang]      Remove the field flag 'unsigned' if the field type is int or smallint

use userdb;
alter table grid_document modify TriggerType smallint(2);
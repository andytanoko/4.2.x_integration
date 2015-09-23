# 26 Aug 2005 I8 v2.0.26 [Tam Wei Xiang] 
# 1) Remove the field 'attachmentDir' from table 'Port'
# 2) Delete record 'attDir' from table 'fieldmetainfo'

use userdb;
alter table port drop column AttachmentDir;

use appdb;
delete from fieldmetainfo where ObjectName='_attachmentDir' and EntityObjectName='com.gridnode.gtas.server.backend.model.Port';
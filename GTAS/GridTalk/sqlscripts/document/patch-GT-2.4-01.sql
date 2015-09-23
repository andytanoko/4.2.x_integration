USE userdb;

update grid_document set DateTimeCreate = DateTimeImport where Folder = 'Import' and (isnull(DateTimeCreate) or DateTimeCreate='');
update grid_document set DateTimeCreate = DateTimeExport where Folder = 'Export' and (isnull(DateTimeCreate) or DateTimeCreate='');
update grid_document set DateTimeCreate = DateTimeReceiveStart where Folder = 'Inbound' and (isnull(DateTimeCreate) or DateTimeCreate='');
update grid_document set DateTimeCreate = DateTimeSendStart where Folder = 'Outbound' and (isnull(DateTimeCreate) or DateTimeCreate='');

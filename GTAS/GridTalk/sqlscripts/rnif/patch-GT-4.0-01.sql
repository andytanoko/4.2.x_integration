# 06 Jan 2006 GT 4.0 I1 [Neo Sok Lay] Add '_' in front of document type

USE userdb;

update bpss_documentenvelope 
set BusinessDocumentIDRef=concat('_',BusinessDocumentIDRef)
where BusinessDocumentIDRef not like ('\_%');

update bpss_proc_spec
set Name=concat('_',Name)
where Name not like ('\_%');

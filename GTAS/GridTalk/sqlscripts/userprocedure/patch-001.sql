# 04 Jun 2003 v2.1 [Jagadeesh] Add default procedure definition for FTP.

use userdb;

INSERT INTO procedure_definition_file (UID,Name,Description,Type,FileName,FilePath,CanDelete,Version) values (-1,'PROCEDURE_DEF_FILE_FTP','Procedure File to perform FTP',2,'gn-ftpclient-ext.jar','userproc.path.jars',0,1);
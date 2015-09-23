# 17 Jul 2003 GT 2.2 I1 [Neo Sok Lay] Set CanDelete to false for default document_type and file_type records.

USE userdb;

UPDATE document_type
SET CanDelete='0'
WHERE DocType IN ("RN_FAILNOTF2","RN_FAILNOTF1","RN_EXCEPTION","RN_ACK","UC");

UPDATE file_type
SET CanDelete='0'
WHERE FileType IN ("doc","txt","xml","csv","xls");

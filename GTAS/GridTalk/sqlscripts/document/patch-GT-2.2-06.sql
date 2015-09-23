# 05 Nov 2003 GT 2.2 I1 [Daniel D'Cotta] Fix fields in grid_document table: 

USE appdb;

UPDATE fieldmetainfo
SET Constraints = 'type=text\r\ntext.length.max=80'
WHERE FieldName = 'PROCESS_INSTANCE_ID'
AND EntityObjectName = 'com.gridnode.gtas.server.document.model.GridDocument';
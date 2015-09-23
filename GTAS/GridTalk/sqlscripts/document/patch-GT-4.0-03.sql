# 21 Sep 2006 [GT4]  [Tam Wei Xiang]            Change the OwnCert, TpCert to SenderCert, ReceiverCert
# 05 Dec 2006 GT 4.0 [Neo Sok Lay] Increase document type field length to 30 chars.
# 06 Dec 2007 [GT4]  [Tam Wei Xiang]            Add in the script to include the SenderCert, ReceiverCert,
#                                               DocDateGen, OriginalDoc.
#                                               Include the FMI for the above 4 fields as well.

USE userdb;

ALTER TABLE document_type 
MODIFY DocType varchar(30) NOT NULL DEFAULT '';

ALTER TABLE grid_document
MODIFY UdocDocType varchar(30);

ALTER TABLE `grid_document` ADD COLUMN SenderCert BIGINT(20) default NULL;
ALTER TABLE `grid_document` ADD COLUMN ReceiverCert BIGINT(20) default NULL;
ALTER TABLE `grid_document` ADD COLUMN DocDateGen varchar(25) default NULL;
ALTER TABLE `grid_document` ADD COLUMN OriginalDoc tinyint(1) default '0';

USE appdb;

UPDATE fieldmetainfo
SET Length=30, Constraints='type=text\r\ntext.length.max=30'
WHERE EntityObjectName='com.gridnode.gtas.server.document.model.DocumentType'
AND FieldName='DOC_TYPE';

UPDATE fieldmetainfo
SET Length=30
WHERE EntityObjectName='com.gridnode.gtas.server.document.model.GridDocument'
AND FieldName='U_DOC_DOC_TYPE';

DELETE FROM fieldmetainfo WHERE EntityObjectName="com.gridnode.gtas.server.document.model.GridDocument" AND FieldName="SENDER_CERT";
INSERT INTO fieldmetainfo VALUES(
NULL,"_senderCert","SENDER_CERT","SenderCert","java.lang.Long",
"com.gridnode.gtas.server.document.model.GridDocument","gridDocument.senderCert",
"0","0","0","0","0","","999",
"displayable=true\r\nmandatory=false\r\neditable=false",
"type=uid"
);

DELETE FROM fieldmetainfo WHERE EntityObjectName="com.gridnode.gtas.server.document.model.GridDocument" AND FieldName="RECEIVER_CERT";
INSERT INTO fieldmetainfo VALUES(
NULL,"_receiverCert","RECEIVER_CERT","ReceiverCert","java.lang.Long",
"com.gridnode.gtas.server.document.model.GridDocument","gridDocument.receiverCert",
"0","0","0","0","0","","999",
"displayable=true\r\nmandatory=false\r\neditable=false",
"type=uid"
);

DELETE FROM fieldmetainfo WHERE EntityObjectName="com.gridnode.gtas.server.document.model.GridDocument" AND FieldName="DOC_DATE_GEN";
INSERT INTO fieldmetainfo VALUES(
NULL,"_docDateGen","DOC_DATE_GEN","DocDateGen","java.lang.String",
"com.gridnode.gtas.server.document.model.GridDocument","gridDocument.docDateGen",
"25","0","0","0","1","","999",
"displayable=true\r\nmandatory=false\r\neditable=false",
"type=text\r\ntext.length.max=25"
);

DELETE FROM fieldmetainfo WHERE EntityObjectName="com.gridnode.gtas.server.document.model.GridDocument" AND FieldName="ORIGINAL_DOC";
INSERT INTO fieldmetainfo VALUES(
NULL,"_originalDoc","ORIGINAL_DOC","OriginalDoc","java.lang.Boolean",
"com.gridnode.gtas.server.document.model.GridDocument","",
"0","0","1","0","0","","999",
"displayable=false\r\nmandatory=false\r\neditable=false",
"type=enum\r\ncandelete.enabled=true\r\ncandelete.disabled=false"
);
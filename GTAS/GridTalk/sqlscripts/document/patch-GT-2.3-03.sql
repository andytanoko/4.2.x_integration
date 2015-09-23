# 27/02/2004 Guo Jianyu -- enlarge the length of UserTrackingID in grid_document 
# to accomodate the length of Message-ID in AS2 documents.
# 17/05/2004 Neo Sok Lay -- fields for AS2 message handling

use userdb;

ALTER TABLE grid_document 
CHANGE UserTrackingID UserTrackingID VARCHAR(255),
DROP COLUMN HasDocTransFailed,
ADD COLUMN DocTransStatus varchar(255),
ADD COLUMN MessageDigest  varchar(80),
ADD COLUMN AuditFileName  varchar(200),
ADD COLUMN ReceiptAuditFileName varchar(200)
;

USE appdb;

DELETE FROM fieldmetainfo
WHERE FieldName='HAS_DOC_TRANS_FAILED'
AND EntityObjectName='com.gridnode.gtas.server.document.model.GridDocument';

INSERT INTO fieldmetainfo VALUES(
NULL,"_docTransStatus","DOC_TRANS_STATUS","DocTransStatus","java.lang.String",
"com.gridnode.gtas.server.document.model.GridDocument","gridDocument.docTransStatus",
255,"0","0","0","1","","999",
"displayable=true\r\nmandatory=false\r\neditable=false",
"type=text\r\ntext.length.max=255\r\n"
);
INSERT INTO fieldmetainfo VALUES(
NULL,"_messageDigest","MESSAGE_DIGEST","MessageDigest","java.lang.String",
"com.gridnode.gtas.server.document.model.GridDocument","gridDocument.messageDigest",
80,"0","0","0","1","","999",
"displayable=true\r\nmandatory=false\r\neditable=false",
"type=text\r\ntext.length.max=80\r\n"
);
INSERT INTO fieldmetainfo VALUES(
NULL,"_auditFileName","AUDIT_FILE_NAME","AuditFileName","java.lang.String",
"com.gridnode.gtas.server.document.model.GridDocument","gridDocument.auditFileName",
"200","0","0","0","1","","999",
"displayable=true\r\nmandatory=true\r\neditable=false",
"type=file\r\nfile.downloadable=true\r\nfile.fixedKey=common.path.audit"
);
INSERT INTO fieldmetainfo VALUES(
NULL,"_receiptAuditFileName","RECEIPT_AUDIT_FILE_NAME","ReceiptAuditFileName","java.lang.String",
"com.gridnode.gtas.server.document.model.GridDocument","gridDocument.receiptAuditFileName",
"200","0","0","0","1","","999",
"displayable=true\r\nmandatory=true\r\neditable=false",
"type=file\r\nfile.downloadable=true\r\nfile.fixedKey=common.path.audit"
);
# Change log
# 01 Oct 2003 GT 2.2 I1  [Neo Sok Lay] Add fieldmetainfos for GridDocument fields: SenderBizEntityUuid,SenderRegistryQueryUrl,RecipientBizEntityUuid,RecipientRegistryQueryUrl
# 20 Oct 2003 GT 2.3 I1  [Neo Sok Lay] Add fieldmetainfos for GridDocument fields: OBPayloadFile
# 24 Nov 2005 GT 2.4     [Tam Wei Xiang] Add in DocDateGen, OwnCert, TPCert, OriginalDoc fields to Gdoc's fieldmetainfo
# 08 Feb 2006 GT 4.0     [Neo Sok Lay] Increase column size to 30 for R_PARTNER_FUNCTION, S_PARTNER_FUNCTION, and PARTNER_FUNCTION
# 20 Oct 2006 GT 4.0(VAN)[Tam Wei Xiang] Added tracingID into GDOC's FMI
# 20 Sep 2006 GT 4.0    [Tam Wei Xiang] Update the griddocument's OwnCert, TPCert to SenderCert, ReceiverCert
# 05 Dec 2006 GT 4.0    [Neo Sok Lay] Increase column size to 30 for DOC_TYPE, UDOC_DOC_TYPE

use appdb;

#
# Dumping data for table 'entitymetainfo'
#
# Field 1 ObjectName: full qualified class name of the entity
# Field 2 EntityName: short class name of the entity
# Field 3 SqlName: table name of the entity

# entitymetainfo for document_type
DELETE FROM entitymetainfo WHERE EntityName = 'DocumentType';
DELETE FROM entitymetainfo WHERE EntityName = 'FileType';
DELETE FROM entitymetainfo WHERE EntityName = 'GridDocument';
DELETE FROM entitymetainfo WHERE EntityName = 'Attachment';
DELETE FROM entitymetainfo WHERE EntityName = 'AttachmentRegistry';
DELETE FROM entitymetainfo WHERE EntityName = 'AS2DocTypeMapping';
INSERT INTO entitymetainfo VALUES("com.gridnode.gtas.server.document.model.DocumentType","DocumentType","document_type");
# entitymetainfo for file_type
INSERT INTO entitymetainfo VALUES("com.gridnode.gtas.server.document.model.FileType","FileType","file_type");
# entitymetainfo for grid_document
INSERT INTO entitymetainfo VALUES("com.gridnode.gtas.server.document.model.GridDocument","GridDocument","grid_document");
# entitymetainfo for attachment
INSERT INTO entitymetainfo VALUES("com.gridnode.gtas.server.document.model.Attachment","Attachment","attachment");
# entitymetainfo for attachmentRegistry
INSERT INTO entitymetainfo VALUES("com.gridnode.gtas.server.document.model.AttachmentRegistry","AttachmentRegistry","attachmentRegistry");
# entitymetainfo for as2DocTypeMapping
INSERT INTO entitymetainfo VALUES ("com.gridnode.gtas.server.document.model.AS2DocTypeMapping","AS2DocTypeMapping","as2_doc_type_mapping");

#
# Dumping data for table 'fieldmetainfo'
#
# UID bigint(20) NOT NULL auto_increment,
# ObjectName: field name in Entity class ,
# FieldName: field ID in Entity Interface class ,
# SqlName: Field column name in Table,
# ValueClass: field data type,
# EntityObjectName: full qualified class name of the entity
# Label: field display label
# Length: valid field length ,
# Proxy: '1' if proxy, '0' otherwise,,
# Mandatory: '1' if mandatory, '0' otherwise,
# Editable: '1' if editable, '0' otherwise
# Displayable: '1' if displayable, '0' otherwise
# OqlName: ,
# Sequence: default '999' ,
# Presentation: properties for presentation of the field
# Constraints: constraint imposed on the values of the field
#

# fieldmetainfo for document_type
DELETE FROM fieldmetainfo WHERE EntityObjectName LIKE '%DocumentType';
INSERT INTO fieldmetainfo VALUES(
NULL,"_uId","UID","UID","java.lang.Long",
"com.gridnode.gtas.server.document.model.DocumentType","documentType.uid",
"0","0","0","0","0","","999",
"displayable=false\r\nmandatory=false\r\neditable=false",
"type=uid"
);
INSERT INTO fieldmetainfo VALUES(
NULL,"_docType","DOC_TYPE","DocType","java.lang.String",
"com.gridnode.gtas.server.document.model.DocumentType","documentType.docType",
"30","0","1","0","1","","1",
"displayable=true\r\nmandatory=true\r\neditable=false\r\neditable.create=true",
"type=text\r\ntext.length.max=30"
);
INSERT INTO fieldmetainfo VALUES(
NULL,"_desc","DESCRIPTION","Description","java.lang.String",
"com.gridnode.gtas.server.document.model.DocumentType","documentType.description",
"80","0","1","1","1","","2",
"displayable=true\r\nmandatory=true\r\neditable=true",
"type=text\r\ntext.length.max=80"
);
INSERT INTO fieldmetainfo VALUES(
NULL,"_canDelete","CAN_DELETE","CanDelete","java.lang.Boolean",
"com.gridnode.gtas.server.document.model.DocumentType","",
"0","0","1","0","0","","999",
"displayable=false\r\nmandatory=false\r\neditable=false",
"type=enum\r\ncandelete.enabled=true\r\ncandelete.disabled=false"
);
INSERT INTO fieldmetainfo VALUES(
NULL,"_version","VERSION","Version","java.lang.Double",
"com.gridnode.gtas.server.document.model.DocumentType","",
"0","0","0","0","0","","999",
"displayable=false\r\nmandatory=false\r\neditable=false",
"type=range"
);


# fieldmetainfo for file_type
DELETE FROM fieldmetainfo WHERE EntityObjectName LIKE '%FileType';
INSERT INTO fieldmetainfo VALUES(
NULL,"_uId","UID","UID","java.lang.Long",
"com.gridnode.gtas.server.document.model.FileType","fileType.uid",
"0","0","0","0","0","","999",
"displayable=false\r\nmandatory=false\r\neditable=false",
"type=uid"
);
INSERT INTO fieldmetainfo VALUES(
NULL,"_fileType","FILE_TYPE","FileType","java.lang.String",
"com.gridnode.gtas.server.document.model.FileType","fileType.fileType",
"10","0","1","0","1","","999",
"displayable=true\r\nmandatory=true\r\neditable=false\r\neditable.create=true",
"type=text\r\ntext.length.max=10"
);
INSERT INTO fieldmetainfo VALUES(
NULL,"_description","DESCRIPTION","Description","java.lang.String",
"com.gridnode.gtas.server.document.model.FileType","fileType.description",
"80","0","1","1","1","","999",
"displayable=true\r\nmandatory=true\r\neditable=true",
"type=text\r\ntext.length.max=80"
);
INSERT INTO fieldmetainfo VALUES(
NULL,"_programName","PROGRAM_NAME","ProgramName","java.lang.String",
"com.gridnode.gtas.server.document.model.FileType","fileType.programName",
"120","0","1","1","1","","999",
"displayable=false\r\nmandatory=false\r\neditable=true",
"type=text\r\ntext.length.max=120"
);
INSERT INTO fieldmetainfo VALUES(
NULL,"_programPath","PROGRAM_PATH","ProgramPath","java.lang.String",
"com.gridnode.gtas.server.document.model.FileType","fileType.programPath",
"120","0","0","1","1","","999",
"displayable=false\r\nmandatory=false\r\neditable=true",
"type=text\r\ntext.length.max=120"
);
INSERT INTO fieldmetainfo VALUES(
NULL,"_parameters","PARAMETERS","Parameters","java.lang.String",
"com.gridnode.gtas.server.document.model.FileType","fileType.parameters",
"120","0","0","1","1","","999",
"displayable=false\r\nmandatory=false\r\neditable=true",
"type=text\r\ntext.length.max=120"
);
INSERT INTO fieldmetainfo VALUES(
NULL,"_workingDirectory","WORKING_DIR","WorkingDirectory","java.lang.String",
"com.gridnode.gtas.server.document.model.FileType","fileType.workingDirectory",
"120","0","0","1","1","","999",
"displayable=false\r\nmandatory=false\r\neditable=true",
"type=text\r\ntext.length.max=120"
);
INSERT INTO fieldmetainfo VALUES(
NULL,"_canDelete","CAN_DELETE","CanDelete","java.lang.Boolean",
"com.gridnode.gtas.server.document.model.FileType","",
"0","0","1","0","0","","999",
"displayable=false\r\nmandatory=false\r\neditable=false",
"type=enum\r\ncandelete.enabled=true\r\ncandelete.disabled=false"
);
INSERT INTO fieldmetainfo VALUES(
NULL,"_version","VERSION","Version","java.lang.Double",
"com.gridnode.gtas.server.document.model.FileType","",
"0","0","0","0","0","","999",
"displayable=false\r\nmandatory=false\r\neditable=false",
"type=range"
);

# fieldmetainfo for grid_document
DELETE FROM fieldmetainfo WHERE EntityObjectName LIKE '%GridDocument';
INSERT INTO fieldmetainfo VALUES(
NULL,"_uId","UID","UID","java.lang.Long",
"com.gridnode.gtas.server.document.model.GridDocument","gridDocument.uid",
"0","0","0","0","0","","999",
"displayable=false\r\nmandatory=false\r\neditable=false",
"type=uid"
);
INSERT INTO fieldmetainfo VALUES(
NULL,"_canDelete","CAN_DELETE","CanDelete","java.lang.Boolean",
"com.gridnode.gtas.server.document.model.GridDocument","",
"0","0","1","0","0","","999",
"displayable=false\r\nmandatory=false\r\neditable=false",
"type=enum\r\ncandelete.enabled=true\r\ncandelete.disabled=false"
);
INSERT INTO fieldmetainfo VALUES(
NULL,"_version","VERSION","Version","java.lang.Double",
"com.gridnode.gtas.server.document.model.GridDocument","",
"0","0","0","0","0","","999",
"displayable=false\r\nmandatory=false\r\neditable=false",
"type=range"
);
INSERT INTO fieldmetainfo VALUES(
NULL,"_gdocId","G_DOC_ID","GdocId","java.lang.Integer",
"com.gridnode.gtas.server.document.model.GridDocument","gridDocument.gdocId",
"0","0","1","0","1","","999",
"displayable=true\r\nmandatory=true\r\neditable=false",
"type=range"
);
INSERT INTO fieldmetainfo VALUES(
NULL,"_refGdocId","REF_G_DOC_ID","RefGdocId","java.lang.Integer",
"com.gridnode.gtas.server.document.model.GridDocument","gridDocument.refGdocId",
"0","0","0","0","1","","999",
"displayable=true\r\nmandatory=false\r\neditable=false",
"type=range"
);
INSERT INTO fieldmetainfo VALUES(
NULL,"_gdocFilename","G_DOC_FILENAME","GdocFilename","java.lang.String",
"com.gridnode.gtas.server.document.model.GridDocument","gridDocument.gdocFilename",
"80","0","0","0","1","","999",
"displayable=true\r\nmandatory=false\r\neditable=false",
"type=file\r\nfile.downloadable=true\r\nfile.fixedKey=document.path.gdoc"
);
INSERT INTO fieldmetainfo VALUES(
NULL,"_udocNum","U_DOC_NUM","UdocNum","java.lang.String",
"com.gridnode.gtas.server.document.model.GridDocument","gridDocument.udocNum",
"20","0","0","0","1","","999",
"displayable=true\r\nmandatory=false\r\neditable=false",
"type=text\r\ntext.length.max=20"
);
INSERT INTO fieldmetainfo VALUES(
NULL,"_refUdocNum","REF_U_DOC_NUM","RefUdocNum","java.lang.String",
"com.gridnode.gtas.server.document.model.GridDocument","gridDocument.refUdocNum",
"20","0","0","0","1","","999",
"displayable=true\r\nmandatory=false\r\neditable=false",
"type=text\r\ntext.length.max=20"
);
INSERT INTO fieldmetainfo VALUES(
NULL,"_udocFilename","U_DOC_FILENAME","UdocFilename","java.lang.String",
"com.gridnode.gtas.server.document.model.GridDocument","gridDocument.udocFilename",
"80","0","0","0","1","","999",
"displayable=true\r\nmandatory=true\r\neditable=true",
"type=file\r\nfile.downloadable=true\r\nfile.fixedKey=document.path.udoc"
);
INSERT INTO fieldmetainfo VALUES(
NULL,"_refUdocFilename","REF_U_DOC_FILENAME","RefUdocFilename","java.lang.String",
"com.gridnode.gtas.server.document.model.GridDocument","gridDocument.refUdocFilename",
"80","0","0","0","1","","999",
"displayable=true\r\nmandatory=false\r\neditable=false",
"type=text\r\ntext.length.max=80"
);
INSERT INTO fieldmetainfo VALUES(
NULL,"_udocVersion","U_DOC_VERSION","UdocVersion","java.lang.Integer",
"com.gridnode.gtas.server.document.model.GridDocument","gridDocument.udocVersion",
"10","0","0","0","1","","999",
"displayable=true\r\nmandatory=false\r\neditable=false",
"type=range\r\range.min=1"
);
INSERT INTO fieldmetainfo VALUES(
NULL,"_udocDocType","U_DOC_DOC_TYPE","UdocDocType","java.lang.String",
"com.gridnode.gtas.server.document.model.GridDocument","gridDocument.udocDocType",
"30","0","0","0","1","","999",
"displayable=true\r\nmandatory=false\r\neditable=false",
"type=foreign\r\nforeign.key=documentType.docType\r\nforeign.display=documentType.docType\r\nforeign.cached=false\r\n"
);
INSERT INTO fieldmetainfo VALUES(
NULL,"_udocFileSize","U_DOC_FILESIZE","","java.lang.Long",
"com.gridnode.gtas.server.document.model.GridDocument","gridDocument.udocFileSize",
"0","0","0","0","1","","999",
"displayable=true\r\nmandatory=false\r\neditable=false",
"type=range\r\nrange.min=0"
);
INSERT INTO fieldmetainfo VALUES(
NULL,"_udocFileType","U_DOC_FILE_TYPE","UdocFileType","java.lang.String",
"com.gridnode.gtas.server.document.model.GridDocument","gridDocument.udocFileType",
"10","0","0","0","1","","999",
"displayable=true\r\nmandatory=false\r\neditable=false",
"type=text\r\ntext.length.max=10"
);
INSERT INTO fieldmetainfo VALUES(
NULL,"_isExported","IS_EXPORTED","Exported","java.lang.Boolean",
"com.gridnode.gtas.server.document.model.GridDocument","gridDocument.isExported",
"0","0","0","0","1","","999",
"displayable=true\r\nmandatory=false\r\neditable=false",
"type=enum\r\ngridDocument.isExported.enabled=true\r\ngridDocument.isExported.disabled=false"
);
INSERT INTO fieldmetainfo VALUES(
NULL,"_isViewAckReq","IS_VIEW_ACK_REQ","ViewAckReq","java.lang.Boolean",
"com.gridnode.gtas.server.document.model.GridDocument","gridDocument.isViewAckReq",
"0","0","0","0","1","","999",
"displayable=false\r\nmandatory=false\r\neditable=false",
"type=enum\r\ngridDocument.isViewAckReq.enabled=true\r\ngridDocument.isViewAckReq.disabled=false"
);
INSERT INTO fieldmetainfo VALUES(
NULL,"_isExportAckReq","IS_EXPORT_ACK_REQ","ExportAckReq","java.lang.Boolean",
"com.gridnode.gtas.server.document.model.GridDocument","gridDocument.isExportAckReq",
"0","0","0","0","1","","999",
"displayable=false\r\nmandatory=false\r\neditable=false",
"type=enum\r\ngridDocument.isExportAckReq.enabled=true\r\ngridDocument.isExportAckReq.disabled=false"
);
INSERT INTO fieldmetainfo VALUES(
NULL,"_isReceiveAckReq","IS_RECEIVE_ACK_REQ","ReceiveAckReq","java.lang.Boolean",
"com.gridnode.gtas.server.document.model.GridDocument","gridDocument.isReceiveAckReq",
"0","0","0","0","1","","999",
"displayable=false\r\nmandatory=false\r\neditable=false",
"type=enum\r\ngridDocument.isReceiveAckReq.enabled=true\r\ngridDocument.isReceiveAckReq.disabled=false"
);
INSERT INTO fieldmetainfo VALUES(
NULL,"_isViewed","IS_VIEWED","Viewed","java.lang.Boolean",
"com.gridnode.gtas.server.document.model.GridDocument","gridDocument.isViewed",
"0","0","0","0","1","","999",
"displayable=true\r\nmandatory=false\r\neditable=false",
"type=enum\r\ngridDocument.isViewed.enabled=true\r\ngridDocument.isViewed.disabled=false"
);
INSERT INTO fieldmetainfo VALUES(
NULL,"_isSent","IS_SENT","Sent","java.lang.Boolean",
"com.gridnode.gtas.server.document.model.GridDocument","gridDocument.isSent",
"0","0","0","0","1","","999",
"displayable=true\r\nmandatory=false\r\neditable=false",
"type=enum\r\ngridDocument.isSent.enabled=true\r\ngridDocument.isSent.disabled=false"
);
INSERT INTO fieldmetainfo VALUES(
NULL,"_isLocalPending","IS_LOCAL_PENDING","LocalPending","java.lang.Boolean",
"com.gridnode.gtas.server.document.model.GridDocument","gridDocument.isLocalPending",
"0","0","0","0","1","","999",
"displayable=false\r\nmandatory=false\r\neditable=false",
"type=enum\r\ngridDocument.isLocalPending.enabled=true\r\ngridDocument.isLocalPending.disabled=false"
);
INSERT INTO fieldmetainfo VALUES(
NULL,"_isExpired","IS_EXPIRED","Expired","java.lang.Boolean",
"com.gridnode.gtas.server.document.model.GridDocument","gridDocument.isExpired",
"0","0","0","0","1","","999",
"displayable=true\r\nmandatory=false\r\neditable=false",
"type=enum\r\ngridDocument.isExpired.enabled=true\r\ngridDocument.isExpired.disabled=false"
);
INSERT INTO fieldmetainfo VALUES(
NULL,"_isRecAckProcessed","IS_REC_ACK_PROCESSED","RecAckProcessed","java.lang.Boolean",
"com.gridnode.gtas.server.document.model.GridDocument","gridDocument.isRecAckProcessed",
"0","0","0","0","1","","999",
"displayable=true\r\nmandatory=false\r\neditable=false",
"type=enum\r\ngridDocument.isRecAckProcessed.enabled=true\r\ngridDocument.isRecAckProcessed.disabled=false"
);
INSERT INTO fieldmetainfo VALUES(
NULL,"_encryptionLevel","ENCRYPTION_LEVEL","EncryptionLevel","java.lang.Integer",
"com.gridnode.gtas.server.document.model.GridDocument","gridDocument.encryptionLevel",
"0","0","0","0","1","","999",
"displayable=true\r\nmandatory=false\r\neditable=false",
"type=range\r\nrange.min=64\r\nrange.max=1024"
);
INSERT INTO fieldmetainfo VALUES(
NULL,"_folder","FOLDER","Folder","java.lang.String",
"com.gridnode.gtas.server.document.model.GridDocument","gridDocument.folder",
"10","0","0","0","1","","999",
"displayable=true\r\nmandatory=false\r\neditable=false",
"type=text\r\ntext.length.max=10"
);
INSERT INTO fieldmetainfo VALUES(
NULL,"_createBy","CREATE_BY","CreateBy","java.lang.String",
"com.gridnode.gtas.server.document.model.GridDocument","gridDocument.createBy",
"50","0","0","0","1","","999",
"displayable=false\r\nmandatory=false\r\neditable=false",
"type=text\r\ntext.length.max=50"
);
INSERT INTO fieldmetainfo VALUES(
NULL,"_recipientNodeId","R_NODE_ID","RecipientNodeId","java.lang.Integer",
"com.gridnode.gtas.server.document.model.GridDocument","gridDocument.recipientNodeId",
"0","0","0","0","1","","999",
"displayable=true\r\nmandatory=false\r\neditable=false",
"type=range"
);
INSERT INTO fieldmetainfo VALUES(
NULL,"_recipientPartnerId","R_PARTNER_ID","RecipientPartnerId","java.lang.String",
"com.gridnode.gtas.server.document.model.GridDocument","gridDocument.recipientPartnerId",
"15","0","0","0","1","","999",
"displayable=true\r\nmandatory=false\r\neditable=false",
"type=foreign\r\nforeign.key=partner.partnerId\r\nforeign.display=partner.partnerId\r\nforeign.cached=false\r\n"
);
INSERT INTO fieldmetainfo VALUES(
NULL,"_recipientPartnerType","R_PARTNER_TYPE","RecipientPartnerType","java.lang.String",
"com.gridnode.gtas.server.document.model.GridDocument","gridDocument.recipientPartnerType",
"3","0","0","0","1","","999",
"displayable=true\r\nmandatory=false\r\neditable=false",
"type=text\r\ntext.length.max=3"
);
INSERT INTO fieldmetainfo VALUES(
NULL,"_recipientPartnerGroup","R_PARTNER_GROUP","RecipientPartnerGroup","java.lang.String",
"com.gridnode.gtas.server.document.model.GridDocument","gridDocument.recipientPartnerGroup",
"3","0","0","0","1","","999",
"displayable=true\r\nmandatory=false\r\neditable=false",
"type=text\r\ntext.length.max=3"
);
INSERT INTO fieldmetainfo VALUES(
NULL,"_recipientBizEntityId","R_BIZ_ENTITY_ID","RecipientBizEntityId","java.lang.String",
"com.gridnode.gtas.server.document.model.GridDocument","gridDocument.recipientBizEntityId",
"4","0","0","0","1","","999",
"displayable=true\r\nmandatory=false\r\neditable=false",
"type=text\r\ntext.length.max=4"
);
INSERT INTO fieldmetainfo VALUES(
NULL,"_recipientPartnerFunction","R_PARTNER_FUNCTION","RecipientPartnerFunction","java.lang.String",
"com.gridnode.gtas.server.document.model.GridDocument","gridDocument.recipientPartnerFunction",
"30","0","0","0","1","","999",
"displayable=false\r\nmandatory=false\r\neditable=false",
"type=text\r\ntext.length.max=30"
);
INSERT INTO fieldmetainfo VALUES(
NULL,"_recipientGdocId","R_G_DOC_ID","RecipientGdocId","java.lang.Integer",
"com.gridnode.gtas.server.document.model.GridDocument","gridDocument.recipientGdocId",
"0","0","0","0","1","","999",
"displayable=true\r\nmandatory=false\r\neditable=false",
"type=range"
);
INSERT INTO fieldmetainfo VALUES(
NULL,"_senderNodeId","S_NODE_ID","SenderNodeId","java.lang.Integer",
"com.gridnode.gtas.server.document.model.GridDocument","gridDocument.senderNodeId",
"0","0","0","0","1","","999",
"displayable=true\r\nmandatory=false\r\neditable=false",
"type=range"
);
INSERT INTO fieldmetainfo VALUES(
NULL,"_senderGdocId","S_G_DOC_ID","SenderGdocId","java.lang.Integer",
"com.gridnode.gtas.server.document.model.GridDocument","gridDocument.senderGdocId",
"0","0","0","0","1","","999",
"displayable=true\r\nmandatory=false\r\neditable=false",
"type=range"
);
INSERT INTO fieldmetainfo VALUES(
NULL,"_senderPartnerFunction","S_PARTNER_FUNCTION","","java.lang.String",
"com.gridnode.gtas.server.document.model.GridDocument","gridDocument.senderPartnerFunction",
"30","0","0","0","1","","999",
"displayable=true\r\nmandatory=false\r\neditable=false",
"type=text\r\ntext.length.max=30"
);
INSERT INTO fieldmetainfo VALUES(
NULL,"_senderUserId","S_USER_ID","SenderUserId","java.lang.String",
"com.gridnode.gtas.server.document.model.GridDocument","gridDocument.senderUserId",
"15","0","0","0","1","","999",
"displayable=true\r\nmandatory=false\r\neditable=false",
"type=text\r\ntext.length.max=15"
);
INSERT INTO fieldmetainfo VALUES(
NULL,"_senderUserName","S_USER_NAME","SenderUserName","java.lang.String",
"com.gridnode.gtas.server.document.model.GridDocument","gridDocument.senderUserName",
"50","0","0","0","1","","999",
"displayable=true\r\nmandatory=false\r\neditable=false",
"type=text\r\ntext.length.max=50"
);
INSERT INTO fieldmetainfo VALUES(
NULL,"_senderBizEntityId","S_BIZ_ENTITY_ID","SenderBizEntityId","java.lang.String",
"com.gridnode.gtas.server.document.model.GridDocument","gridDocument.senderBizEntityId",
"4","0","0","0","1","","999",
"displayable=true\r\nmandatory=false\r\neditable=false",
"type=text\r\ntext.length.max=4"
);
INSERT INTO fieldmetainfo VALUES(
NULL,"_senderRoute","S_ROUTE","SenderRoute","java.lang.String",
"com.gridnode.gtas.server.document.model.GridDocument","gridDocument.senderRoute",
"20","0","0","0","1","","999",
"displayable=true\r\nmandatory=false\r\neditable=false",
"type=text\r\ntext.length.max=20"
);
INSERT INTO fieldmetainfo VALUES(
NULL,"_senderPartnerId","S_PARTNER_ID","SenderPartnerId","java.lang.String",
"com.gridnode.gtas.server.document.model.GridDocument","gridDocument.senderPartnerId",
"15","0","0","0","1","","999",
"displayable=true\r\nmandatory=false\r\neditable=false",
"type=foreign\r\nforeign.key=partner.partnerId\r\nforeign.display=partner.partnerId\r\nforeign.cached=false\r\n"
);
INSERT INTO fieldmetainfo VALUES(
NULL,"_senderPartnerType","S_PARTNER_TYPE","SenderPartnerType","java.lang.String",
"com.gridnode.gtas.server.document.model.GridDocument","gridDocument.senderPartnerType",
"3","0","0","0","1","","999",
"displayable=true\r\nmandatory=false\r\neditable=false",
"type=text\r\ntext.length.max=3"
);
INSERT INTO fieldmetainfo VALUES(
NULL,"_senderPartnerGroup","S_PARTNER_GROUP","SenderPartnerGroup","java.lang.String",
"com.gridnode.gtas.server.document.model.GridDocument","gridDocument.senderPartnerGroup",
"3","0","0","0","1","","999",
"displayable=true\r\nmandatory=false\r\neditable=false",
"type=text\r\ntext.length.max=3"
);
INSERT INTO fieldmetainfo VALUES(
NULL,"_dateTimeImport","DT_IMPORT","DateTimeImport","java.util.Date",
"com.gridnode.gtas.server.document.model.GridDocument","gridDocument.dateTimeImport",
"0","0","0","0","1","","999",
"displayable=true\r\nmandatory=false\r\neditable=false",
"type=datetime\r\ndatetime.time=true\r\ndatetime.date=true\r\ndatetime.adjustment=gts");
INSERT INTO fieldmetainfo VALUES(
NULL,"_dateTimeSendEnd","DT_SEND_END","DateTimeSendEnd","java.util.Date",
"com.gridnode.gtas.server.document.model.GridDocument","gridDocument.dateTimeSendEnd",
"0","0","0","0","1","","999",
"displayable=true\r\nmandatory=false\r\neditable=false",
"type=datetime\r\ndatetime.time=true\r\ndatetime.date=true\r\ndatetime.adjustment=gts");
INSERT INTO fieldmetainfo VALUES(
NULL,"_dateTimeReceiveEnd","DT_RECEIVE_END","DateTimeReceiveEnd","java.util.Date",
"com.gridnode.gtas.server.document.model.GridDocument","gridDocument.dateTimeReceiveEnd",
"0","0","0","0","1","","999",
"displayable=true\r\nmandatory=false\r\neditable=false",
"type=datetime\r\ndatetime.time=true\r\ndatetime.date=true\r\ndatetime.adjustment=gts");
INSERT INTO fieldmetainfo VALUES(
NULL,"_dateTimeExport","DT_EXPORT","DateTimeExport","java.util.Date",
"com.gridnode.gtas.server.document.model.GridDocument","gridDocument.dateTimeExport",
"0","0","0","0","1","","999",
"displayable=true\r\nmandatory=false\r\neditable=false",
"type=datetime\r\ndatetime.time=true\r\ndatetime.date=true\r\ndatetime.adjustment=gts");
INSERT INTO fieldmetainfo VALUES(
NULL,"_dateTimeCreate","DT_CREATE","DateTimeCreate","java.util.Date",
"com.gridnode.gtas.server.document.model.GridDocument","gridDocument.dateTimeCreate",
"0","0","0","0","1","","999",
"displayable=false\r\nmandatory=false\r\neditable=false",
"type=datetime\r\ndatetime.time=true\r\ndatetime.date=true\r\ndatetime.adjustment=gts");
INSERT INTO fieldmetainfo VALUES(
NULL,"_dateTimeTransComplete","DT_TRANSACTION_COMPLETE","DateTimeTransComplete","java.util.Date",
"com.gridnode.gtas.server.document.model.GridDocument","gridDocument.dateTimeTransComplete",
"0","0","0","0","1","","999",
"displayable=true\r\nmandatory=false\r\neditable=false",
"type=datetime\r\ndatetime.time=true\r\ndatetime.date=true\r\ndatetime.adjustment=gts");
INSERT INTO fieldmetainfo VALUES(
NULL,"_dateTimeReceiveStart","DT_RECEIVE_START","DateTimeReceiveStart","java.util.Date",
"com.gridnode.gtas.server.document.model.GridDocument","gridDocument.dateTimeReceiveStart",
"0","0","0","0","1","","999",
"displayable=false\r\nmandatory=false\r\neditable=false",
"type=datetime\r\ndatetime.time=true\r\ndatetime.date=true\r\ndatetime.adjustment=gts");
INSERT INTO fieldmetainfo VALUES(
NULL,"_dateTimeView","DT_VIEW","DateTimeView","java.util.Date",
"com.gridnode.gtas.server.document.model.GridDocument","gridDocument.dateTimeView",
"0","0","0","0","1","","999",
"displayable=true\r\nmandatory=false\r\neditable=false",
"type=datetime\r\ndatetime.time=true\r\ndatetime.date=true\r\ndatetime.adjustment=gts");
INSERT INTO fieldmetainfo VALUES(
NULL,"_dateTimeSendStart","DT_SEND_START","DateTimeSendStart","java.util.Date",
"com.gridnode.gtas.server.document.model.GridDocument","gridDocument.dateTimeSendStart",
"0","0","0","0","1","","999",
"displayable=true\r\nmandatory=false\r\neditable=false",
"type=datetime\r\ndatetime.time=true\r\ndatetime.date=true\r\ndatetime.adjustment=gts");
INSERT INTO fieldmetainfo VALUES(
NULL,"_dateTimeRecipientView","DT_RECIPIENT_VIEW","DateTimeRecipientView","java.util.Date",
"com.gridnode.gtas.server.document.model.GridDocument","gridDocument.dateTimeRecipientView",
"0","0","0","0","1","","999",
"displayable=false\r\nmandatory=false\r\neditable=false",
"type=datetime\r\ndatetime.time=true\r\ndatetime.date=true\r\ndatetime.adjustment=gts");
INSERT INTO fieldmetainfo VALUES(
NULL,"_dateTimeRecipientExport","DT_RECIPIENT_EXPORT","DateTimeRecipientExport","java.util.Date",
"com.gridnode.gtas.server.document.model.GridDocument","gridDocument.dateTimeRecipientExport",
"0","0","0","0","1","","999",
"displayable=false\r\nmandatory=false\r\neditable=false",
"type=datetime\r\ndatetime.time=true\r\ndatetime.date=true\r\ndatetime.adjustment=gts");
INSERT INTO fieldmetainfo VALUES(
NULL,"_partnerFunction","PARTNER_FUNCTION","","java.lang.String",
"com.gridnode.gtas.server.document.model.GridDocument","gridDocument.partnerFunction",
"30","0","0","0","1","","999",
"displayable=true\r\nmandatory=false\r\neditable=false",
"type=text\r\ntext.length.max=30"
);
INSERT INTO fieldmetainfo VALUES(
NULL,"_portUid","PORT_UID","PortUid","java.lang.Long",
"com.gridnode.gtas.server.document.model.GridDocument","gridDocument.portUid",
"0","0","0","0","1","","999",
"displayable=false\r\nmandatory=false\r\neditable=false",
"type=range"
);
INSERT INTO fieldmetainfo VALUES(
NULL,"_actionCode","ACTION_CODE","","java.lang.String",
"com.gridnode.gtas.server.document.model.GridDocument","gridDocument.actionCode",
"0","0","0","0","1","","999",
"displayable=false\r\nmandatory=false\r\neditable=false",
"type=text"
);
INSERT INTO fieldmetainfo VALUES(
NULL,"_srcFolder","SOURCE_FOLDER","SrcFolder","java.lang.String",
"com.gridnode.gtas.server.document.model.GridDocument","gridDocument.srcFolder",
"10","0","0","0","1","","999",
"displayable=true\r\nmandatory=false\r\neditable=false",
"type=text\r\ntext.length.max=10"
);
INSERT INTO fieldmetainfo VALUES(
NULL,"_notifyUserEmail","NOTIFY_USER_EMAIL","NotifyUserEmail","java.lang.String",
"com.gridnode.gtas.server.document.model.GridDocument","gridDocument.notifyUserEmail",
"50","0","0","0","1","","999",
"displayable=false\r\nmandatory=false\r\neditable=false",
"type=text\r\ntext.length.max=50"
);
INSERT INTO fieldmetainfo VALUES(
NULL,"_recipientChannelUid","R_CHANNEL_UID","","java.lang.Long",
"com.gridnode.gtas.server.document.model.GridDocument","gridDocument.recipientChannelUid",
"0","0","0","0","1","","999",
"displayable=false\r\nmandatory=false\r\neditable=false",
"type=range"
);
INSERT INTO fieldmetainfo VALUES(
NULL,"_recipientChannelName","R_CHANNEL_NAME","","java.lang.String",
"com.gridnode.gtas.server.document.model.GridDocument","gridDocument.recipientChannelName",
"30","0","0","0","1","","999",
"displayable=true\r\nmandatory=false\r\neditable=false",
"type=foreign\r\nforeign.key=channelInfo.name\r\nforeign.display=channelInfo.name\r\nforeign.cached=false\r\n"
);
INSERT INTO fieldmetainfo VALUES(
NULL,"_recipientChannelProtocol","R_CHANNEL_PROTOCOL","","java.lang.String",
"com.gridnode.gtas.server.document.model.GridDocument","gridDocument.recipientChannelProtocol",
"10","0","0","0","1","","999",
"displayable=false\r\nmandatory=false\r\neditable=false",
"type=text\r\ntext.length.max=10"
);
INSERT INTO fieldmetainfo VALUES(
NULL,"_recipientPartnerName","R_PARTNER_NAME","RecipientPartnerName","java.lang.String",
"com.gridnode.gtas.server.document.model.GridDocument","gridDocument.recipientPartnerName",
"20","0","0","0","1","","999",
"displayable=true\r\nmandatory=false\r\neditable=false",
"type=text\r\ntext.length.max=20"
);
INSERT INTO fieldmetainfo VALUES(
NULL,"_senderPartnerName","S_PARTNER_NAME","SenderPartnerName","java.lang.String",
"com.gridnode.gtas.server.document.model.GridDocument","gridDocument.senderPartnerName",
"20","0","0","0","1","","999",
"displayable=true\r\nmandatory=false\r\neditable=false",
"type=text\r\ntext.length.max=20"
);
INSERT INTO fieldmetainfo VALUES(
NULL,"_portName","PORT_NAME","PortName","java.lang.String",
"com.gridnode.gtas.server.document.model.GridDocument","gridDocument.portName",
"15","0","0","0","1","","999",
"displayable=true\r\nmandatory=false\r\neditable=false",
"type=foreign\r\nforeign.key=port.name\r\nforeign.display=port.name\r\nforeign.cached=false\r\n"
);
INSERT INTO fieldmetainfo VALUES(
NULL,"_rnProfileUid","RN_PROFILE_UID","RnProfileUid","java.lang.Long",
"com.gridnode.gtas.server.document.model.GridDocument","gridDocument.rnProfileUid",
"0","0","0","0","1","","999",
"displayable=false\r\nmandatory=false\r\neditable=false",
"type=range"
);
INSERT INTO fieldmetainfo VALUES(
NULL,"_processDefId","PROCESS_DEF_ID","ProcessDefId","java.lang.String",
"com.gridnode.gtas.server.document.model.GridDocument","gridDocument.processDefId",
"0","0","0","0","1","","999",
"displayable=true\r\nmandatory=false\r\neditable=false",
"type=text\r\ntext.length.max=80"
);
INSERT INTO fieldmetainfo VALUES(
NULL,"_isRequest","IS_REQUEST","IsRequest","java.lang.Boolean",
"com.gridnode.gtas.server.document.model.GridDocument","gridDocument.isRequest",
"0","0","0","0","1","","999",
"displayable=false\r\nmandatory=false\r\neditable=false",
"type=enum\r\ngridDocument.isRequest.enabled=true\r\ngridDocument.isRequest.disabled=false"
);
INSERT INTO fieldmetainfo VALUES(
NULL,"_hasAttachment","HAS_ATTACHMENT","HasAttachment","java.lang.Boolean",
"com.gridnode.gtas.server.document.model.GridDocument","gridDocument.hasAttachment",
"0","0","0","0","1","","999",
"displayable=true\r\nmandatory=false\r\neditable=false",
"type=enum\r\ngridDocument.hasAttachment.enabled=true\r\ngridDocument.hasAttachment.disabled=false"
);
INSERT INTO fieldmetainfo VALUES(
NULL,"_isAttachmentLinkUpdated","IS_ATTACHMENT_LINK_UPDATED","AttachmentLinkUpdated","java.lang.Boolean",
"com.gridnode.gtas.server.document.model.GridDocument","gridDocument.isAttachmentLinkUpdated",
"0","0","0","0","1","","999",
"displayable=false\r\nmandatory=false\r\neditable=false",
"type=enum\r\ngridDocument.isAttachmentLinkUpdated.enabled=true\r\ngridDocument.isAttachmentLinkUpdated.disabled=false"
);
INSERT INTO fieldmetainfo VALUES(
NULL,"_attachments","ATTACHMENTS","Attachments","java.util.ArrayList",
"com.gridnode.gtas.server.document.model.GridDocument","gridDocument.attachments",
"0","0","0","0","1","","999",
"displayable=false\r\nmandatory=false\r\neditable=false",
"type=foreign\r\nforeign.key=attachment.uid\r\nforeign.display=attachment.originalFilename\r\nforeign.cached=false\r\ncollection=true"
);
INSERT INTO fieldmetainfo VALUES(
NULL,"_triggerType","TRIGGER_TYPE","TriggerType","java.lang.Integer",
"com.gridnode.gtas.server.document.model.GridDocument","gridDocument.triggerType",
"0","0","1","1","1","","999",
"displayable=false\r\nmandatory=false\r\neditable=false",
"type=enum\r\ngridDocument.triggerType.import=0\r\ngridDocument.triggerType.receive=1\r\ngridDocument.triggerType.manualSend=2\r\ngridDocument.triggerType.manualExport=3"
);
INSERT INTO fieldmetainfo VALUES(
NULL,"_uniqueDocIdentifier","UNIQUE_DOC_IDENTIFIER","UniqueDocIdentifier","java.lang.String",
"com.gridnode.gtas.server.document.model.GridDocument","gridDocument.uniqueDocIdentifier",
"80","0","0","0","1","","999",
"displayable=true\r\nmandatory=false\r\neditable=false",
"type=text\r\ntext.length.max=80"
);
INSERT INTO fieldmetainfo VALUES(
NULL,"_udocFullPath","U_DOC_FULL_PATH","UdocFullPath","java.lang.String",
"com.gridnode.gtas.server.document.model.GridDocument","gridDocument.udocFullPath",
"255","0","0","0","1","","999",
"displayable=true\r\nmandatory=true\r\neditable=false",
"type=text\r\ntext.length.max=255"
);
INSERT INTO fieldmetainfo VALUES(
NULL,"_exportedUdocFullPath","EXPORTED_U_DOC_FULL_PATH","ExportedUdocFullPath","java.lang.String",
"com.gridnode.gtas.server.document.model.GridDocument","gridDocument.exportedUdocFullPath",
"255","0","0","0","1","","999",
"displayable=true\r\nmandatory=false\r\neditable=false",
"type=text\r\ntext.length.max=255"
);
INSERT INTO fieldmetainfo VALUES(
NULL,"_isRejected","IS_REJECTED","Rejected","java.lang.Boolean",
"com.gridnode.gtas.server.document.model.GridDocument","gridDocument.isRejected",
"0","0","0","0","1","","999",
"displayable=true\r\nmandatory=false\r\neditable=false",
"type=enum\r\ngridDocument.isRejected.enabled=true\r\ngridDocument.isRejected.disabled=false"
);
INSERT INTO fieldmetainfo VALUES(
NULL,"_custom1","CUSTOM1","Custom1","java.lang.String",
"com.gridnode.gtas.server.document.model.GridDocument","gridDocument.custom1",
"255","0","0","1","1","","999",
"displayable=true\r\nmandatory=false\r\neditable=true",
"type=text\r\ntext.length.max=255"
);
INSERT INTO fieldmetainfo VALUES(
NULL,"_custom2","CUSTOM2","Custom2","java.lang.String",
"com.gridnode.gtas.server.document.model.GridDocument","gridDocument.custom2",
"255","0","0","1","1","","999",
"displayable=true\r\nmandatory=false\r\neditable=true",
"type=text\r\ntext.length.max=255"
);
INSERT INTO fieldmetainfo VALUES(
NULL,"_custom3","CUSTOM3","Custom3","java.lang.String",
"com.gridnode.gtas.server.document.model.GridDocument","gridDocument.custom3",
"255","0","0","1","1","","999",
"displayable=true\r\nmandatory=false\r\neditable=true",
"type=text\r\ntext.length.max=255"
);
INSERT INTO fieldmetainfo VALUES(
NULL,"_custom4","CUSTOM4","Custom4","java.lang.String",
"com.gridnode.gtas.server.document.model.GridDocument","gridDocument.custom4",
"255","0","0","1","1","","999",
"displayable=true\r\nmandatory=false\r\neditable=true",
"type=text\r\ntext.length.max=255"
);
INSERT INTO fieldmetainfo VALUES(
NULL,"_custom5","CUSTOM5","Custom5","java.lang.String",
"com.gridnode.gtas.server.document.model.GridDocument","gridDocument.custom5",
"255","0","0","1","1","","999",
"displayable=true\r\nmandatory=false\r\neditable=true",
"type=text\r\ntext.length.max=255"
);
INSERT INTO fieldmetainfo VALUES(
NULL,"_custom6","CUSTOM6","Custom6","java.lang.String",
"com.gridnode.gtas.server.document.model.GridDocument","gridDocument.custom6",
"255","0","0","1","1","","999",
"displayable=true\r\nmandatory=false\r\neditable=true",
"type=text\r\ntext.length.max=255"
);
INSERT INTO fieldmetainfo VALUES(
NULL,"_custom7","CUSTOM7","Custom7","java.lang.String",
"com.gridnode.gtas.server.document.model.GridDocument","gridDocument.custom7",
"255","0","0","1","1","","999",
"displayable=true\r\nmandatory=false\r\neditable=true",
"type=text\r\ntext.length.max=255"
);
INSERT INTO fieldmetainfo VALUES(
NULL,"_custom8","CUSTOM8","Custom8","java.lang.String",
"com.gridnode.gtas.server.document.model.GridDocument","gridDocument.custom8",
"255","0","0","1","1","","999",
"displayable=true\r\nmandatory=false\r\neditable=true",
"type=text\r\ntext.length.max=255"
);
INSERT INTO fieldmetainfo VALUES(
NULL,"_custom9","CUSTOM9","Custom9","java.lang.String",
"com.gridnode.gtas.server.document.model.GridDocument","gridDocument.custom9",
"255","0","0","1","1","","999",
"displayable=true\r\nmandatory=false\r\neditable=true",
"type=text\r\ntext.length.max=255"
);
INSERT INTO fieldmetainfo VALUES(
NULL,"_custom10","CUSTOM10","Custom10","java.lang.String",
"com.gridnode.gtas.server.document.model.GridDocument","gridDocument.custom10",
"255","0","0","1","1","","999",
"displayable=true\r\nmandatory=false\r\neditable=true",
"type=text\r\ntext.length.max=255"
);
INSERT INTO fieldmetainfo VALUES(
NULL,"_processInstanceUid", "PROCESS_INSTANCE_UID","ProcessInstanceUid","java.lang.Long",
"com.gridnode.gtas.server.document.model.GridDocument", "gridDocument.processInstanceUid",
"0","0","0","0","0","","999",
"displayable=true\r\nmandatory=false\r\neditable=false",
'type=foreign\r\nforeign.key=processInstance.uid\r\nforeign.display=gridDocument.processInstanceId\r\nforeign.cached=false\r\n'
);
INSERT INTO fieldmetainfo VALUES(
NULL,"_processInstanceID","PROCESS_INSTANCE_ID","ProcessInstanceID","java.lang.String",
"com.gridnode.gtas.server.document.model.GridDocument", "gridDocument.processInstanceId",
80,"0","0","1","1","", 999,
"displayable=true\r\neditable=false\nmandatory=false\r\n",
"type=text\r\ntext.length.max=80"
);
INSERT INTO fieldmetainfo VALUES(
NULL,"_userTrackingID","USER_TRACKING_ID","UserTrackingID","java.lang.String",
"com.gridnode.gtas.server.document.model.GridDocument", "gridDocument.userTrackingId",
80,"0","0","1","1","", 999,
"displayable=true\r\neditable=false\nmandatory=false\r\n",
"type=text\r\ntext.length.max=80\r\n"
);
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
INSERT INTO fieldmetainfo VALUES(
NULL,"_senderBizEntityUuid","S_BIZ_ENTITY_UUID","SenderBizEntityUuid","java.lang.String",
"com.gridnode.gtas.server.document.model.GridDocument", "gridDocument.senderBizEntityUuid", 
50,"0","0","1","1","", 999, 
"displayable=true\r\neditable=false\nmandatory=false\r\n",
"type=text\r\ntext.length.max=50\r\n"
);	
INSERT INTO fieldmetainfo VALUES(
NULL,"_senderRegistryQueryUrl","S_REGISTRY_QUERY_URL","SenderRegistryQueryUrl","java.lang.String",
"com.gridnode.gtas.server.document.model.GridDocument", "gridDocument.senderRegistryQueryUrl", 
255,"0","0","1","1","", 999, 
"displayable=true\r\neditable=false\nmandatory=false\r\n",
"type=text\r\ntext.length.max=255\r\n"
);	
INSERT INTO fieldmetainfo VALUES(
NULL,"_recipientBizEntityUuid","R_BIZ_ENTITY_UUID","RecipientBizEntityUuid","java.lang.String",
"com.gridnode.gtas.server.document.model.GridDocument", "gridDocument.recipientBizEntityUuid", 
50,"0","0","1","1","", 999, 
"displayable=true\r\neditable=false\nmandatory=false\r\n",
"type=text\r\ntext.length.max=50\r\n"
);	
INSERT INTO fieldmetainfo VALUES(
NULL,"_recipientRegistryQueryUrl","R_REGISTRY_QUERY_URL","RecipientRegistryQueryUrl","java.lang.String",
"com.gridnode.gtas.server.document.model.GridDocument", "gridDocument.recipientRegistryQueryUrl", 
255,"0","0","1","1","", 999, 
"displayable=true\r\neditable=false\nmandatory=false\r\n",
"type=text\r\ntext.length.max=255\r\n"
);	
INSERT INTO fieldmetainfo VALUES(
NULL,"_OBPayloadFile","OB_PAYLOAD_FILE","","java.lang.String",
"com.gridnode.gtas.server.document.model.GridDocument", "gridDocument.obPayloadFile", 
255,"0","0","1","1","", 999, 
"displayable=true\r\neditable=false\nmandatory=false\r\n",
"type=text\r\ntext.length.max=255\r\n"
);	
INSERT INTO fieldmetainfo VALUES(
NULL,"_senderCert","SENDER_CERT","SenderCert","java.lang.Long",
"com.gridnode.gtas.server.document.model.GridDocument","gridDocument.senderCert",
"0","0","0","0","0","","999",
"displayable=true\r\nmandatory=false\r\neditable=false",
"type=uid"
);
INSERT INTO fieldmetainfo VALUES(
NULL,"_receiverCert","RECEIVER_CERT","ReceiverCert","java.lang.Long",
"com.gridnode.gtas.server.document.model.GridDocument","gridDocument.receiverCert",
"0","0","0","0","0","","999",
"displayable=true\r\nmandatory=false\r\neditable=false",
"type=uid"
);
INSERT INTO fieldmetainfo VALUES(
NULL,"_docDateGen","DOC_DATE_GEN","DocDateGen","java.lang.String",
"com.gridnode.gtas.server.document.model.GridDocument","gridDocument.docDateGen",
"25","0","0","0","1","","999",
"displayable=true\r\nmandatory=false\r\neditable=false",
"type=text\r\ntext.length.max=25"
);
INSERT INTO fieldmetainfo VALUES(
NULL,"_originalDoc","ORIGINAL_DOC","OriginalDoc","java.lang.Boolean",
"com.gridnode.gtas.server.document.model.GridDocument","",
"0","0","1","0","0","","999",
"displayable=false\r\nmandatory=false\r\neditable=false",
"type=enum\r\ncandelete.enabled=true\r\ncandelete.disabled=false"
);
INSERT INTO fieldmetainfo VALUES(
NULL,"_tracingID","TRACING_ID","TracingID","java.lang.String",
"com.gridnode.gtas.server.document.model.GridDocument","gridDocument.tracingID",
"36","0","0","0","1","","999",
"displayable=false\r\nmandatory=false\r\neditable=false",
"type=text\r\ntext.length.max=36"
);

# fieldmetainfo for attachment
DELETE FROM fieldmetainfo WHERE EntityObjectName LIKE '%Attachment';
INSERT INTO fieldmetainfo VALUES(
NULL,"_uId","UID","UID","java.lang.Long",
"com.gridnode.gtas.server.document.model.Attachment","attachment.uid",
"0","0","0","0","0","","999",
"displayable=false\r\nmandatory=false\r\neditable=false",
"type=uid"
);
INSERT INTO fieldmetainfo VALUES(
NULL,"_canDelete","CAN_DELETE","CanDelete","java.lang.Boolean",
"com.gridnode.gtas.server.document.model.Attachment","",
"0","0","1","0","0","","999",
"displayable=false\r\nmandatory=false\r\neditable=false",
"type=enum\r\ncandelete.enabled=true\r\ncandelete.disabled=false"
);
INSERT INTO fieldmetainfo VALUES(
NULL,"_version","VERSION","Version","java.lang.Double",
"com.gridnode.gtas.server.document.model.Attachment","",
"0","0","0","0","0","","999",
"displayable=false\r\nmandatory=false\r\neditable=false",
"type=range"
);
INSERT INTO fieldmetainfo VALUES(
NULL,"_partnerId","PARTNER_ID","PartnerId","java.lang.String",
"com.gridnode.gtas.server.document.model.Attachment","attachment.partnerId",
"20","0","0","0","0","","999",
"displayable=false\r\nmandatory=false\r\neditable=false",
"type=text\r\ntext.length.max=20"
);
INSERT INTO fieldmetainfo VALUES(
NULL,"_originalUid","ORIGINAL_UID","OriginalUid","java.lang.Long",
"com.gridnode.gtas.server.document.model.Attachment","attachment.originalUid",
"0","0","0","0","0","","999",
"displayable=false\r\nmandatory=false\r\neditable=false",
"type=range"
);
INSERT INTO fieldmetainfo VALUES(
NULL,"_filename","FILENAME","Filename","java.lang.String",
"com.gridnode.gtas.server.document.model.Attachment","attachment.filename",
"80","0","0","0","0","","999",
"displayable=true\r\nmandatory=true\r\neditable=false",
"type=file\r\nfile.downloadable=true\r\nfile.fixedKey=document.path.attachment"
);
INSERT INTO fieldmetainfo VALUES(
NULL,"_originalFilename","ORIGINAL_FILENAME","OriginalFilename","java.lang.String",
"com.gridnode.gtas.server.document.model.Attachment","attachment.originalFilename",
"80","0","0","0","0","","999",
"displayable=true\r\nmandatory=true\r\neditable=false",
"type=text\r\ntext.length.max=80"
);
INSERT INTO fieldmetainfo VALUES(
NULL,"_isOutgoing","IS_OUTGOING","IsOutgoing","java.lang.Boolean",
"com.gridnode.gtas.server.document.model.Attachment","attachment.isOutgoing",
"0","0","0","0","0","","999",
"displayable=false\r\nmandatory=false\r\neditable=false",
"type=enum\r\nattachment.isOutgoing.enabled=true\r\nattachment.isOutgoing.disabled=false"
);

# fieldmetainfo for attachmentRegistry
DELETE FROM fieldmetainfo WHERE EntityObjectName LIKE '%AttachmentRegistry';
INSERT INTO fieldmetainfo VALUES(
NULL,"_uId","UID","UID","java.lang.Long",
"com.gridnode.gtas.server.document.model.AttachmentRegistry","attachmentRegistry.uid",
"0","0","0","0","0","","999",
"displayable=false\r\nmandatory=false\r\neditable=false",
"type=uid"
);
INSERT INTO fieldmetainfo VALUES(
NULL,"_canDelete","CAN_DELETE","CanDelete","java.lang.Boolean",
"com.gridnode.gtas.server.document.model.AttachmentRegistry","",
"0","0","1","0","0","","999",
"displayable=false\r\nmandatory=false\r\neditable=false",
"type=enum\r\ncandelete.enabled=true\r\ncandelete.disabled=false"
);
INSERT INTO fieldmetainfo VALUES(
NULL,"_version","VERSION","Version","java.lang.Double",
"com.gridnode.gtas.server.document.model.AttachmentRegistry","",
"0","0","0","0","0","","999",
"displayable=false\r\nmandatory=false\r\neditable=false",
"type=range"
);
INSERT INTO fieldmetainfo VALUES(
NULL,"_partnerId","PARTNER_ID","PartnerId","java.lang.String",
"com.gridnode.gtas.server.document.model.AttachmentRegistry","attachmentRegistry.partnerId",
"20","0","0","0","0","","999",
"displayable=false\r\nmandatory=false\r\neditable=false",
"type=text\r\ntext.length.max=20"
);
INSERT INTO fieldmetainfo VALUES(
NULL,"_attachmentUid","ATTACHMENT_UID","AttachmentUid","java.lang.Long",
"com.gridnode.gtas.server.document.model.AttachmentRegistry","attachmentRegistry.attachmentUid",
"0","0","0","0","0","","999",
"displayable=false\r\nmandatory=false\r\neditable=false",
"type=range"
);
INSERT INTO fieldmetainfo VALUES(
NULL,"_dateProcessed","DATE_PROCESSED","DateProcessed","java.util.Date",
"com.gridnode.gtas.server.document.model.AttachmentRegistry","attachmentRegistry.dateProcessed",
"0","0","0","0","1","","999",
"displayable=false\r\nmandatory=false\r\neditable=false",
"type=datetime"
);

# fieldmetainfo for as2DocTypeMapping
DELETE FROM fieldmetainfo WHERE EntityObjectName LIKE '%AS2DocTypeMapping';

INSERT INTO fieldmetainfo VALUES(
NULL,"_uId","UID","UID","java.lang.Long",
"com.gridnode.gtas.server.document.model.AS2DocTypeMapping","as2DocTypeMapping.uid",
"0","0","0","0","0","","999",
"displayable=false\r\nmandatory=false\r\neditable=false",
"type=uid"
);
INSERT INTO fieldmetainfo VALUES(
NULL,"_as2DocType","AS2_DOC_TYPE","AS2DocType","java.lang.String",
"com.gridnode.gtas.server.document.model.AS2DocTypeMapping","as2DocTypeMapping.as2DocType",
"30","0","0","1","1","","1",
"displayable=true\r\nmandatory=false\r\neditable=true\r\neditable.create=true",
"type=foreign\r\nforeign.key=documentType.docType\r\nforeign.display=document_type.docType\r\nforeign.cached=false"
);
INSERT INTO fieldmetainfo VALUES(
NULL,"_docType","DOC_TYPE","DocType","java.lang.String",
"com.gridnode.gtas.server.document.model.AS2DocTypeMapping","as2DocTypeMapping.docType",
"30","0","0","1","1","","1",
"displayable=true\r\nmandatory=false\r\neditable=true\r\neditable.create=true",
"type=foreign\r\nforeign.key=documentType.docType\r\nforeign.display=document_type.docType\r\nforeign.cached=false"
);
INSERT INTO fieldmetainfo VALUES(
NULL,"_partnerID","PARTNER_ID","PartnerId","java.lang.String",
"com.gridnode.gtas.server.document.model.AS2DocTypeMapping","as2DocTypeMapping.partnerId",
"20","0","0","1","1","","999",
"displayable=true\r\nmandatory=false\r\neditable=true\r\neditable.create=true",
"type=foreign\r\nforeign.key=partner.partnerId\r\nforeign.display=partner.name\r\nforeign.cached=false"
);
INSERT INTO fieldmetainfo VALUES(
NULL,"_canDelete","CAN_DELETE","CanDelete","java.lang.Boolean",
"com.gridnode.gtas.server.document.model.AS2DocTypeMapping","",
"0","0","1","0","0","","999",
"displayable=false\r\nmandatory=false\r\neditable=false",
"type=enum\r\ncandelete.enabled=true\r\ncandelete.disabled=false"
);
INSERT INTO fieldmetainfo VALUES(
NULL,"_version","VERSION","Version","java.lang.Double",
"com.gridnode.gtas.server.document.model.AS2DocTypeMapping","",
"0","0","0","0","0","","999",
"displayable=false\r\nmandatory=false\r\neditable=false",
"type=range"
);


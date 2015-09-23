#
# 01 Oct 2003 GT 2.2 I1 [Neo Sok Lay] Add fields in grid_document table: SenderBizEntityUuid,SenderRegistryQueryUrl,RecipientBizEntityUuid,RecipientRegistryQueryUrl
# 

USE userdb;

ALTER table grid_document
ADD COLUMN RecipientRegistryQueryUrl VARCHAR(255) AFTER RecipientBizEntityId,
ADD COLUMN RecipientBizEntityUuid VARCHAR(50) AFTER RecipientBizEntityId,
ADD COLUMN SenderRegistryQueryUrl VARCHAR(255) AFTER SenderBizEntityId,
ADD COLUMN SenderBizEntityUuid VARCHAR(50) AFTER SenderBizEntityId;


USE appdb;

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
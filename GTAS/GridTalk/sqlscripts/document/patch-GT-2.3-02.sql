# 20 Oct 2003 GT 2.3 I1 [Neo Sok Lay] Add fieldmetainfos for GridDocument fields: OBPayloadFile

USE appdb;

INSERT INTO fieldmetainfo VALUES(
NULL,"_OBPayloadFile","OB_PAYLOAD_FILE","","java.lang.String",
"com.gridnode.gtas.server.document.model.GridDocument", "gridDocument.obPayloadFile", 
255,"0","0","1","1","", 999, 
"displayable=true\r\neditable=false\nmandatory=false\r\n",
"type=text\r\ntext.length.max=255\r\n"
);	

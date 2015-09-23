# 03 Sep 2003 GT 2.2 I1 [Neo Sok Lay] Add metainfo for SearchedBusinessEntity

USE appdb;

# SearchedBusinessEntity
DELETE from entitymetainfo WHERE EntityName = 'SearchedBusinessEntity';
INSERT INTO entitymetainfo VALUES ('com.gridnode.gtas.server.enterprise.model.SearchedBusinessEntity', 'SearchedBusinessEntity', '');

DELETE from fieldmetainfo WHERE EntityObjectName LIKE '%.SearchedBusinessEntity';
INSERT INTO fieldmetainfo VALUES(NULL,"_uuid","UUID","","java.lang.String","com.gridnode.gtas.server.enterprise.model.SearchedBusinessEntity","searchedBusinessEntity.uuid","0","0","0","1","1","","1","displayable=true\r\neditable=false\r\nmandatory=false\r\n","type=text\r\n");
INSERT INTO fieldmetainfo VALUES(NULL,"_enterpriseId","ENTERPRISE_ID","","java.lang.String","com.gridnode.gtas.server.enterprise.model.SearchedBusinessEntity","searchedBusinessEntity.enterpriseId","0","0","0","1","1","","1","displayable=true\r\neditable=false\r\nmandatory=false\r\n","type=text\r\n");
INSERT INTO fieldmetainfo VALUES(NULL,"_busEntId","ID","","java.lang.String","com.gridnode.gtas.server.enterprise.model.SearchedBusinessEntity","searchedBusinessEntity.id","0","0","0","1","1","","2","displayable=true\r\neditable=false\r\n","type=text\r\n");
INSERT INTO fieldmetainfo VALUES(NULL,"_desc","DESCRIPTION","","java.lang.String","com.gridnode.gtas.server.enterprise.model.SearchedBusinessEntity","searchedBusinessEntity.description","0","0","0","1","1","","3","displayable=true\r\neditable=false\r\nmandatory=false\r\n","type=text\r\n");
INSERT INTO fieldmetainfo VALUES(NULL,"_state","STATE","","java.lang.Integer","com.gridnode.gtas.server.enterprise.model.SearchedBusinessEntity","searchedBusinessEntity.beState","0","0","0","1","1","","5","displayable=true\r\neditable=false\r\nmandatory=false\r\n","type=enum\r\nsearchedBusinessEntity.state.newBe=0\r\nsearchedBusinessEntity.state.myBe=1\r\nsearchedBusinessEntity.state.partnerBe=2\r\n");
INSERT INTO fieldmetainfo VALUES(NULL,"_whitePage","WHITE_PAGE","","com.gridnode.pdip.app.bizreg.model.WhitePage","com.gridnode.gtas.server.enterprise.model.SearchedBusinessEntity","searchedBusinessEntity.whitePage","0","0","0","1","1","","999","displayable=true\r\neditable=false\r\nmandatory=false\r\n","type=embedded\r\nembedded.type=whitePage\r\n");
INSERT INTO fieldmetainfo VALUES(NULL,"_channels","CHANNELS","","java.util.Collection","com.gridnode.gtas.server.enterprise.model.SearchedBusinessEntity","searchedBusinessEntity.channels","0","0","0","1","1","","999","displayable=true\r\neditable=false\r\nmandatory=false\r\n","collection=true\r\ntype=embedded\r\nembedded.type=channelInfo\r\n");


# Change Log
#
# 03 Sep 2003 GT 2.2 I2 [Neo Sok Lay] Add metainfo for SearchedBusinessEntity
# 10 Oct 2003 GT 2.2 I2 [Neo Sok Lay] Add metainfo for SearchedChannelInfo
# 24 Nov 2003 GT 2.3 I1 [Neo Sok Lay] Add UPC tptProtocolType. 
#

USE appdb;

DELETE FROM entitymetainfo WHERE EntityName IN ('ResourceLink','SharedResource','SearchedBusinessEntity','SearchedChannelInfo');
INSERT INTO entitymetainfo (ObjectName, EntityName, SqlName) VALUES ('com.gridnode.gtas.server.enterprise.model.ResourceLink', 'ResourceLink', 'resource_link');
INSERT INTO entitymetainfo (ObjectName, EntityName, SqlName) VALUES ('com.gridnode.gtas.server.enterprise.model.SharedResource', 'SharedResource', 'shared_resource');
INSERT INTO entitymetainfo (ObjectName, EntityName, SqlName) VALUES ('com.gridnode.gtas.server.enterprise.model.SearchedBusinessEntity', 'SearchedBusinessEntity', '');
INSERT INTO entitymetainfo (ObjectName, EntityName, SqlName) VALUES ('com.gridnode.gtas.model.enterprise.ISearchedChannelInfo', 'SearchedChannelInfo', '');

# ResourceLink
DELETE FROM fieldmetainfo WHERE EntityObjectName LIKE '%ResourceLink';
INSERT INTO fieldmetainfo VALUES (NULL, '_uId', 'UID', 'UID', 'java.lang.Long', 'com.gridnode.gtas.server.enterprise.model.ResourceLink', NULL, 0, 0, 0, 1, 1, NULL, 999, NULL, NULL);
INSERT INTO fieldmetainfo VALUES (NULL, '_fromType', 'FROM_TYPE', 'FromResourceType', 'java.lang.String', 'com.gridnode.gtas.server.enterprise.model.ResourceLink', NULL, 0, 0, 0, 1, 1, NULL, 999, NULL, NULL);
INSERT INTO fieldmetainfo VALUES (NULL, '_fromUID', 'FROM_UID', 'FromResourceUID', 'java.lang.Long', 'com.gridnode.gtas.server.enterprise.model.ResourceLink', NULL, 0, 0, 0, 1, 1, NULL, 999, NULL, NULL);
INSERT INTO fieldmetainfo VALUES (NULL, '_toType', 'TO_TYPE', 'ToResourceType', 'java.lang.String', 'com.gridnode.gtas.server.enterprise.model.ResourceLink', NULL, 0, 0, 0, 1, 1, NULL, 999, NULL, NULL);
INSERT INTO fieldmetainfo VALUES (NULL, '_toUID', 'TO_UID', 'ToResourceUID', 'java.lang.Long', 'com.gridnode.gtas.server.enterprise.model.ResourceLink', NULL, 0, 0, 0, 1, 1, NULL, 999, NULL, NULL);
INSERT INTO fieldmetainfo VALUES (NULL, '_priority', 'PRIORITY', 'Priority', 'java.lang.Integer', 'com.gridnode.gtas.server.enterprise.model.ResourceLink', NULL, 0, 0, 0, 1, 1, NULL, 999, NULL, NULL);
INSERT INTO fieldmetainfo VALUES (NULL, '_canDelete', 'CAN_DELETE', 'CanDelete', 'java.lang.Boolean', 'com.gridnode.gtas.server.enterprise.model.ResourceLink', NULL, 0, 0, 0, 1, 1, NULL, 999, NULL, NULL);
INSERT INTO fieldmetainfo VALUES (NULL, '_version', 'VERSION', 'Version', 'java.lang.Double', 'com.gridnode.gtas.server.enterprise.model.ResourceLink', NULL, 0, 0, 0, 1, 1, NULL, 999, NULL, NULL);
INSERT INTO fieldmetainfo VALUES (NULL, '_nextLinks', 'NEXT_LINKS', NULL, 'java.util.Collection', 'com.gridnode.gtas.server.enterprise.model.ResourceLink', NULL, 0, 0, 0, 1, 1, NULL, 999, NULL, NULL);

# SharedResource
DELETE FROM fieldmetainfo WHERE EntityObjectName LIKE '%SharedResource';
INSERT INTO fieldmetainfo VALUES (NULL, '_uId', 'UID', 'UID', 'java.lang.Long', 'com.gridnode.gtas.server.enterprise.model.SharedResource', NULL, 0, 0, 0, 1, 1, NULL, 999, NULL, NULL);
INSERT INTO fieldmetainfo VALUES (NULL, '_resourceType', 'RESOURCE_TYPE', 'ResourceType', 'java.lang.String', 'com.gridnode.gtas.server.enterprise.model.SharedResource', NULL, 0, 0, 0, 1, 1, NULL, 999, NULL, NULL);
INSERT INTO fieldmetainfo VALUES (NULL, '_resourceUID', 'RESOURCE_UID', 'ResourceUID', 'java.lang.Long', 'com.gridnode.gtas.server.enterprise.model.SharedResource', NULL, 0, 0, 0, 1, 1, NULL, 999, NULL, NULL);
INSERT INTO fieldmetainfo VALUES (NULL, '_toEnterpriseID', 'TO_ENTERPRISE_ID', 'ToEnterpriseID', 'java.lang.String', 'com.gridnode.gtas.server.enterprise.model.SharedResource', NULL, 0, 0, 0, 1, 1, NULL, 999, NULL, NULL);
INSERT INTO fieldmetainfo VALUES (NULL, '_state', 'STATE', 'State', 'java.lang.Short', 'com.gridnode.gtas.server.enterprise.model.SharedResource', NULL, 0, 0, 0, 1, 1, NULL, 999, NULL, NULL);
INSERT INTO fieldmetainfo VALUES (NULL, '_syncChecksum', 'SYNC_CHECKSUM', 'SyncChecksum', 'java.lang.Long', 'com.gridnode.gtas.server.enterprise.model.SharedResource', NULL, 0, 0, 0, 1, 1, NULL, 999, NULL, NULL);
INSERT INTO fieldmetainfo VALUES (NULL, '_canDelete', 'CAN_DELETE', 'CanDelete', 'java.lang.Boolean', 'com.gridnode.gtas.server.enterprise.model.SharedResource', NULL, 0, 0, 0, 1, 1, NULL, 999, NULL, NULL);
INSERT INTO fieldmetainfo VALUES (NULL, '_version', 'VERSION', 'Version', 'java.lang.Double', 'com.gridnode.gtas.server.enterprise.model.SharedResource', NULL, 0, 0, 0, 1, 1, NULL, 999, NULL, NULL);

# SearchedBusinessEntity
DELETE from fieldmetainfo WHERE EntityObjectName LIKE '%.SearchedBusinessEntity';
INSERT INTO fieldmetainfo VALUES(NULL,"_uuid","UUID","","java.lang.String","com.gridnode.gtas.server.enterprise.model.SearchedBusinessEntity","searchedBusinessEntity.uuid","0","0","0","1","1","","1","displayable=true\r\neditable=false\r\nmandatory=false\r\n","type=text\r\n");
INSERT INTO fieldmetainfo VALUES(NULL,"_enterpriseId","ENTERPRISE_ID","EnterpriseID","java.lang.String","com.gridnode.gtas.server.enterprise.model.SearchedBusinessEntity","searchedBusinessEntity.enterpriseId","0","0","0","1","1","","1","displayable=true\r\neditable=false\r\nmandatory=false\r\n","type=text\r\n");
INSERT INTO fieldmetainfo VALUES(NULL,"_busEntId","ID","ID","java.lang.String","com.gridnode.gtas.server.enterprise.model.SearchedBusinessEntity","searchedBusinessEntity.id","0","0","0","1","1","","2","displayable=true\r\neditable=false\r\n","type=text\r\n");
INSERT INTO fieldmetainfo VALUES(NULL,"_desc","DESCRIPTION","Description","java.lang.String","com.gridnode.gtas.server.enterprise.model.SearchedBusinessEntity","searchedBusinessEntity.description","0","0","0","1","1","","3","displayable=true\r\neditable=false\r\nmandatory=false\r\n","type=text\r\n");
INSERT INTO fieldmetainfo VALUES(NULL,"_state","STATE","State","java.lang.Integer","com.gridnode.gtas.server.enterprise.model.SearchedBusinessEntity","searchedBusinessEntity.beState","0","0","0","1","1","","5","displayable=true\r\neditable=false\r\nmandatory=false\r\n","type=enum\r\nsearchedBusinessEntity.state.newBe=0\r\nsearchedBusinessEntity.state.myBe=1\r\nsearchedBusinessEntity.state.partnerBe=2\r\n");
INSERT INTO fieldmetainfo VALUES(NULL,"_whitePage","WHITE_PAGE","","com.gridnode.pdip.app.bizreg.model.WhitePage","com.gridnode.gtas.server.enterprise.model.SearchedBusinessEntity","searchedBusinessEntity.whitePage","0","0","0","1","1","","999","displayable=true\r\neditable=false\r\nmandatory=false\r\n","type=embedded\r\nembedded.type=whitePage\r\n");
INSERT INTO fieldmetainfo VALUES(NULL,"_channels","CHANNELS","","java.util.Collection","com.gridnode.gtas.server.enterprise.model.SearchedBusinessEntity","searchedBusinessEntity.channels","0","0","0","1","1","","999","displayable=true\r\neditable=false\r\nmandatory=false\r\n","collection=true\r\ntype=embedded\r\nembedded.type=searchedChannelInfo\r\n");

# SearchedChannelInfo
DELETE FROM fieldmetainfo WHERE EntityObjectName LIKE '%.ISearchedChannelInfo';
INSERT INTO fieldmetainfo VALUES(NULL,"_refId","REF_ID","","java.lang.String","com.gridnode.gtas.model.enterprise.ISearchedChannelInfo","searchedChannelInfo.refId","30","0","0","1","1","0","999","displayable=true\r\neditable=false\r\ndisplayable=true","type=text\r\ntext.length.max=30");
INSERT INTO fieldmetainfo VALUES(NULL,"_tptProtocolType","TPT_PROTOCOL_TYPE","","java.lang.String","com.gridnode.gtas.model.enterprise.ISearchedChannelInfo","searchedChannelInfo.tptProtocolType","10","0","1","1","1","0","999","displayable=true\r\nmandatory=true","type=enum\r\nchannelInfo.tptProtocolType.jms=JMS\r\nchannelInfo.tptProtocolType.http=HTTP\r\nchannelInfo.tptProtocolType.soap=SOAP-HTTP\r\nchannelInfo.tptProtocolType.upc=UPC");
INSERT INTO fieldmetainfo VALUES(NULL,"_description","DESCRIPTION","","java.lang.String","com.gridnode.gtas.model.enterprise.ISearchedChannelInfo","searchedChannelInfo.description","80","0","0","1","1","0","999","displayable=true\r\nmandatory=true","type=text\r\ntext.length.max=80");
INSERT INTO fieldmetainfo VALUES(NULL,"_tptCommInfo","TPT_COMM_INFO","","com.gridnode.pdip.app.channel.model.CommInfo","com.gridnode.gtas.model.enterprise.ISearchedChannelInfo","searchedChannelInfo.tptCommInfo","0","0","1","1","0","0","999","displayable=true\r\neditable=false\r\nmandatory=true","type=embedded\r\nembedded.type=commInfo");
INSERT INTO fieldmetainfo VALUES(NULL,"_packagingInfo","PACKAGING_PROFILE","","com.gridnode.pdip.app.channel.model.PackagingInfo","com.gridnode.gtas.model.enterprise.ISearchedChannelInfo","searchedChannelInfo.packagingProfile","0","0","1","1","0","0","999","displayable=true\r\neditable=false\r\nmandatory=true","type=embedded\r\nembedded.type=packagingInfo");
INSERT INTO fieldmetainfo VALUES(NULL,"_securityInfo","SECURITY_PROFILE","","com.gridnode.pdip.app.channel.model.SecurityInfo","com.gridnode.gtas.model.enterprise.ISearchedChannelInfo","searchedChannelInfo.securityProfile","0","0","1","1","0","0","999","displayable=true\r\neditable=false\r\nmandatory=true","type=embedded\r\nembedded.type=securityInfo");
INSERT INTO fieldmetainfo VALUES(NULL,"_name","NAME","","java.lang.String","com.gridnode.gtas.model.enterprise.ISearchedChannelInfo","searchedChannelInfo.name","30","0","1","1","1","","999","displayable=true\r\neditable=false\r\nmandatory=true","type=text\r\ntext.length.max=30");


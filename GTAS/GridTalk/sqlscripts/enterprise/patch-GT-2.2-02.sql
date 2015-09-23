# 10 Oct 2003 GT 2.2 I2 [Neo Sok Lay] Add metainfo for SearchedChannelInfo

USE appdb;

# SearchedChannelInfo
DELETE from entitymetainfo WHERE EntityName = 'SearchedChannelInfo';
INSERT INTO entitymetainfo (ObjectName, EntityName, SqlName) VALUES ('com.gridnode.gtas.model.enterprise.ISearchedChannelInfo', 'SearchedChannelInfo', '');

DELETE FROM fieldmetainfo WHERE EntityObjectName LIKE '%.ISearchedChannelInfo';
INSERT INTO fieldmetainfo VALUES(NULL,"_refId","REF_ID","","java.lang.String","com.gridnode.gtas.model.enterprise.ISearchedChannelInfo","searchedChannelInfo.refId","30","0","0","1","1","0","999","displayable=true\r\neditable=false\r\ndisplayable=true","type=text\r\ntext.length.max=30");
INSERT INTO fieldmetainfo VALUES(NULL,"_tptProtocolType","TPT_PROTOCOL_TYPE","","java.lang.String","com.gridnode.gtas.model.enterprise.ISearchedChannelInfo","searchedChannelInfo.tptProtocolType","10","0","1","1","1","0","999","displayable=true\r\nmandatory=true","type=enum\r\nchannelInfo.tptProtocolType.jms=JMS\r\nchannelInfo.tptProtocolType.http=HTTP\r\nnchannelInfo.tptProtocolType.soap=SOAP-HTTP");
INSERT INTO fieldmetainfo VALUES(NULL,"_description","DESCRIPTION","","java.lang.String","com.gridnode.gtas.model.enterprise.ISearchedChannelInfo","searchedChannelInfo.description","80","0","0","1","1","0","999","displayable=true\r\nmandatory=true","type=text\r\ntext.length.max=80");
INSERT INTO fieldmetainfo VALUES(NULL,"_tptCommInfo","TPT_COMM_INFO","","com.gridnode.pdip.app.channel.model.CommInfo","com.gridnode.gtas.model.enterprise.ISearchedChannelInfo","searchedChannelInfo.tptCommInfo","0","0","1","1","0","0","999","displayable=true\r\neditable=false\r\nmandatory=true","type=embedded\r\nembedded.type=commInfo");
INSERT INTO fieldmetainfo VALUES(NULL,"_packagingInfo","PACKAGING_PROFILE","","com.gridnode.pdip.app.channel.model.PackagingInfo","com.gridnode.gtas.model.enterprise.ISearchedChannelInfo","searchedChannelInfo.packagingProfile","0","0","1","1","0","0","999","displayable=true\r\neditable=false\r\nmandatory=true","type=embedded\r\nembedded.type=packagingInfo");
INSERT INTO fieldmetainfo VALUES(NULL,"_securityInfo","SECURITY_PROFILE","","com.gridnode.pdip.app.channel.model.SecurityInfo","com.gridnode.gtas.model.enterprise.ISearchedChannelInfo","searchedChannelInfo.securityProfile","0","0","1","1","0","0","999","displayable=true\r\neditable=false\r\nmandatory=true","type=embedded\r\nembedded.type=securityInfo");
INSERT INTO fieldmetainfo VALUES(NULL,"_name","NAME","","java.lang.String","com.gridnode.gtas.model.enterprise.ISearchedChannelInfo","searchedChannelInfo.name","30","0","1","1","1","","999","displayable=true\r\neditable=false\r\nmandatory=true","type=text\r\ntext.length.max=30");

UPDATE fieldmetainfo
SET Constraints="collection=true\r\ntype=embedded\r\nembedded.type=searchedChannelInfo\r\n"
WHERE FieldName="CHANNELS"
AND EntityObjectName="com.gridnode.gtas.server.enterprise.model.SearchedBusinessEntity";


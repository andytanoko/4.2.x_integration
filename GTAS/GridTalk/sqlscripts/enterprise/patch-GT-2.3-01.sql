# 24 Nov 2003 GT 2.3 I1 [Neo Sok Lay] Add UPC tptProtocolType. 

USE appdb;

UPDATE fieldmetainfo
SET Constraints=CONCAT(Constraints,'\r\nchannelInfo.tptProtocolType.upc=UPC')
WHERE FieldName='TPT_PROTOCOL_TYPE'
AND EntityObjectName='com.gridnode.gtas.model.enterprise.ISearchedChannelInfo'
;


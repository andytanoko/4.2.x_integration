# Change Log
# 21 Oct 2003 GT 2.3 I1 [Neo Sok Lay] Add UPC Protocol type
# 24 Nov 2003 GT 2.3 I1 [Neo Sok Lay] Incorrect spelling grindode -- should be gridnode

USE appdb;

# update fieldmetainfo for partner categories.
UPDATE fieldmetainfo
SET Constraints=CONCAT(Constraints,'generic.partnerCategory.others=0\r\ngeneric.partnerCategory.gridtalk=1\r\n')
WHERE FieldName='PARTNER_CAT'
AND EntityObjectName = 'com.gridnode.pdip.app.channel.model.ChannelInfo'
;

UPDATE fieldmetainfo
SET Constraints=CONCAT(Constraints,'generic.partnerCategory.others=0\r\ngeneric.partnerCategory.gridtalk=1\r\n')
WHERE FieldName='PARTNER_CAT'
AND EntityObjectName = 'com.gridnode.pdip.app.channel.model.CommInfo'
;

UPDATE fieldmetainfo
SET Constraints=CONCAT(Constraints,'generic.partnerCategory.others=0\r\ngeneric.partnerCategory.gridtalk=1\r\n')
WHERE FieldName='PARTNER_CAT'
AND EntityObjectName = 'com.gridnode.pdip.app.channel.model.PackagingInfo'
;

UPDATE fieldmetainfo
SET Constraints=CONCAT(Constraints,'generic.partnerCategory.others=0\r\ngeneric.partnerCategory.gridtalk=1\r\n')
WHERE FieldName='PARTNER_CAT'
AND EntityObjectName = 'com.gridnode.pdip.app.channel.model.SecurityInfo'
;

# 21 Oct 03
UPDATE fieldmetainfo
SET Constraints=CONCAT(Constraints,'\r\nchannelInfo.tptProtocolType.upc=UPC')
WHERE FieldName='TPT_PROTOCOL_TYPE'
AND EntityObjectName='com.gridnode.pdip.app.channel.model.ChannelInfo'
;

# 21 Oct 03
UPDATE fieldmetainfo
SET Constraints=CONCAT(Constraints,'\r\ncommInfo.protocolType.upc=UPC')
WHERE FieldName='PROTOCOL_TYPE'
AND EntityObjectName='com.gridnode.pdip.app.channel.model.CommInfo'
;







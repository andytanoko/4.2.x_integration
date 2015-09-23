USE appdb;

# update fieldmetainfo for additional States.
UPDATE fieldmetainfo
SET Constraints=CONCAT(Constraints,'businessEntity.beState.active=2\r\nbusinessEntity.beState.inactive=3\r\nbusinessEntity.beState.pending=4\r\n')
WHERE FieldName='STATE'
AND EntityObjectName = 'com.gridnode.pdip.app.bizreg.model.BusinessEntity'
;

# update fieldmetainfo for partner categories.
UPDATE fieldmetainfo
SET Constraints=CONCAT(Constraints,'generic.partnerCategory.others=0\r\generic.partnerCategory.gridtalk=1\r\n')
WHERE FieldName='PARTNER_CATEGORY'
AND EntityObjectName = 'com.gridnode.pdip.app.bizreg.model.BusinessEntity'
;

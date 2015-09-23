# 22 Dec 2005 GT 4.0 I1 [Sumedh] Add Starfish Id domain identifier type

USE appdb;

# DomainIdentifier
# SC: add starfish id type to domainIdentifier.type
UPDATE fieldmetainfo
SET Constraints=CONCAT(Constraints,'domainIdentifier.type.starfishId=StarfishId\r\n')
WHERE label = 'domainIdentifier.type'
AND EntityObjectName = 'com.gridnode.pdip.app.bizreg.model.DomainIdentifier'
;
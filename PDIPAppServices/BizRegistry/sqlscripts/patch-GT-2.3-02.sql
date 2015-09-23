# 23 Dec 2003 GT 2.3 I1 [Neo Sok Lay] Add DomainIdentifier entity.

USE userdb;
DROP TABLE IF EXISTS domain_identifier;
CREATE TABLE IF NOT EXISTS domain_identifier (
  UID bigint(20) NOT NULL DEFAULT '0' ,
  BizEntityUID bigint(20) NOT NULL,
  Domain varchar(30) NOT NULL,
  Type varchar(50) NOT NULL,
  Value varchar(255),
  CanDelete tinyint(1) unsigned NOT NULL DEFAULT '1' ,
  PRIMARY KEY (UID),
  UNIQUE KEY identifier_idx (Type,Value)
);


USE appdb;

# DomainIdentifier
INSERT INTO entitymetainfo VALUES("com.gridnode.pdip.app.bizreg.model.DomainIdentifier","DomainIdentifier","domain_identifier");
INSERT INTO fieldmetainfo VALUES(NULL,"_uId","UID","UID","java.lang.Long","com.gridnode.pdip.app.bizreg.model.DomainIdentifier","domainIdentifier.uid","0","0","0","1","1","","999","displayable=false\r\nmandatory=false\r\neditable=false\r\n","type=uid\r\n");
INSERT INTO fieldmetainfo VALUES(NULL,"_beUid","BUSINESS_ENTITY_UID","BizEntityUID","java.lang.Long","com.gridnode.pdip.app.bizreg.model.DomainIdentifier","domainIdentifier.beUid","0","0","0","1","1","","999","displayable=false\r\neditable=false\r\nmandatory=false\r\n",NULL);
INSERT INTO fieldmetainfo VALUES(NULL,"_domainName","DOMAIN_NAME","Domain","java.lang.String","com.gridnode.pdip.app.bizreg.model.DomainIdentifier","domainIdentifier.domainName","0","0","0","1","1","","999","displayable=false\r\neditable=false\r\nmandatory=false\r\n",NULL);
INSERT INTO fieldmetainfo VALUES(NULL,"_type","TYPE","Type","java.lang.String","com.gridnode.pdip.app.bizreg.model.DomainIdentifier","domainIdentifier.type","0","0","0","1","1","","999","displayable=true\r\neditable=true\r\nmandatory=true\r\n","type=enum\r\ndomainIdentifier.type.duns=DunsNumber\r\ndomainIdentifier.type.as2id=As2Identifier\r\ndomainIdentifier.type.discoveryUrl=DiscoveryUrl\r\ndomainIdentifier.type.gln=GlobalLocationNumber\r\n");
INSERT INTO fieldmetainfo VALUES(NULL,"_value","VALUE","Value","java.lang.String","com.gridnode.pdip.app.bizreg.model.DomainIdentifier","domainIdentifier.value","0","0","0","1","1","","999","displayable=true\r\neditable=true\r\nmandatory=true\r\n","type=text\r\ntext.length.max=255\r\n");
INSERT INTO fieldmetainfo VALUES(NULL,"_canDelete","CAN_DELETE","CanDelete","java.lang.Boolean","com.gridnode.pdip.app.bizreg.model.DomainIdentifier","domainIdentifier.canDelete","0","0","0","1","1","","999","displayable=true\r\neditable=false\r\nmandatory=false\r\n","type=enum\r\ngeneric.yes=true\r\ngeneric.no=false\r\n");

# BusinessEntity
INSERT INTO fieldmetainfo VALUES(NULL,"_domainIdentifiers","DOMAIN_IDENTIFIERS","","java.util.Collection","com.gridnode.pdip.app.bizreg.model.BusinessEntity","businessEntity.domainIdentifiers","0","0","0","1","1","","999","","type=embedded\r\nembedded.type=domainIdentifier\r\ncollection=true\r\n");


# 23 Apr 2003 I8 v2.0.27 [AMH] 
# Update Script for Certificate to add the columns for
# a. isINKS - is in key store
# b. isINTS - is in trust store

USE userdb;

alter table certificate add column isINKS tinyint(1) NOT NULL default '0';
alter table certificate add column isINTS tinyint(1) NOT NULL default '0';

USE appdb;

INSERT INTO fieldmetainfo VALUES (NULL,"_isInKeyStore","IS_IN_KS","isINKS","java.lang.Boolean","com.gridnode.pdip.base.certificate.model.Certificate","certificate.inKeyStore","0","0","0","0","0","0","999","displayable=true\r\ndisplayable.create=false\r\nmandatory=true\r\neditable=false\r\n","type=enum\r\ngeneric.yes=true\r\ngeneric.no=false\r\n");
INSERT INTO fieldmetainfo VALUES (NULL,"_isInTrustStore","IS_IN_TS","isINTS","java.lang.Boolean","com.gridnode.pdip.base.certificate.model.Certificate","certificate.inTrustStore","0","0","0","0","0","0","999","displayable=true\r\ndisplayable.create=false\r\nmandatory=true\r\neditable=false\r\n","type=enum\r\ngeneric.yes=true\r\ngeneric.no=false\r\n");



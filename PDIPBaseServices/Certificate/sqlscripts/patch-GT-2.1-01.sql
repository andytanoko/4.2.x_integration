# 25 Mar 2004 Guo Jianyu 
# Update Script for Certificate to add the columns for
# relatedCertUID -- the pending new cert or the related expiring cert

USE userdb;

alter table certificate add column relatedCertUid bigint(20) default NULL;

USE appdb;

INSERT INTO fieldmetainfo VALUES (NULL,"_relatedCertUid","RELATED_CERT_UID","relatedCertUid","java.lang.Long","com.gridnode.pdip.base.certificate.model.Certificate","certificate.relatedCertUid",0,"0","0","1","1","0",999,"displayable=true\r\neditable=true\r\nmandatory=false\r\n\r\n","type=foreign\r\nforeign.key=certificate.uid\r\nforeign.display=certificate.name\r\nforeign.cached=false");



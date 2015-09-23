#[Tam Wei Xiang] [GT4] 25 July 2006   New FMI for StartDate, EndDate, IsCA, ReplacementCertUid
#[Neo Sok Lay]   [GT4] 06 Dec 2006    Change label for SerialNum
#[Wong Yee Wah]	 [GT4] 01 Aug 2008    add EMI and FMI for Certificate Swapping

USE appdb;

#
# Dumping data for table 'entitymetainfo'
#
DELETE FROM entitymetainfo WHERE EntityName IN ("Certificate");
INSERT INTO entitymetainfo VALUES ('com.gridnode.pdip.base.certificate.model.Certificate','Certificate','certificate');


# Dumping data for table 'fieldmetainfo'
#
DELETE FROM fieldmetainfo WHERE EntityObjectName LIKE "%Certificate";
INSERT INTO fieldmetainfo VALUES (NULL,"_uId","UID","UID","java.lang.Long","com.gridnode.pdip.base.certificate.model.Certificate","certificate.uid","20","0","0","0","0","0","999","displayable=false\r\neditable=false\r\nmandatory=false\r\n","type=uid\r\n");
INSERT INTO fieldmetainfo VALUES (NULL,"_privateKey","PRIVATEKEY","PrivateKey","java.lang.String","com.gridnode.pdip.base.certificate.model.Certificate","certificate.privateKey"," ","0","0","0","0","0","999","displayable=false\r\neditable=false\r\nmandatory=false\r\n","type=text\r\n");
INSERT INTO fieldmetainfo VALUES (NULL,"_publicKey","PUBLICKEY","PublicKey","java.lang.String","com.gridnode.pdip.base.certificate.model.Certificate","certificate.publicKey"," ","0","0","0","0","0","999","displayable=false\r\neditable=false\r\nmandatory=false\r\n","type=text\r\n");
INSERT INTO fieldmetainfo VALUES (NULL,"_certificate","CERTIFICATE","Certificate","java.lang.String","com.gridnode.pdip.base.certificate.model.Certificate","certificate.certificate"," ","0","0","0","0","0","999","displayable=false\r\neditable=false\r\nmandatory=false\r\n","type=text\r\n");
INSERT INTO fieldmetainfo VALUES (NULL,"_issuerName","ISSUERNAME","IssuerName","java.lang.String","com.gridnode.pdip.base.certificate.model.Certificate","certificate.issuerName","120","0","0","0","0","0","999","displayable=false\r\neditable=false\r\nmandatory=false\r\n","type=text\r\n");
INSERT INTO fieldmetainfo VALUES (NULL,"_serialNum","SERIALNUM","SerialNum","java.lang.String","com.gridnode.pdip.base.certificate.model.Certificate","certificate.serialNum","30","0","0","0","0","0","999","displayable=true\r\neditable=false\r\nmandatory=false\r\n","type=text\r\n");
INSERT INTO fieldmetainfo VALUES (NULL,"_name","NAME","Name","java.lang.String","com.gridnode.pdip.base.certificate.model.Certificate","certificate.name","50","0","0","0","0","0","999","displayable=true\r\neditable=true\r\nmandatory=true\r\n","type=text\r\ntext.length.max=50\r\n");
INSERT INTO fieldmetainfo VALUES (NULL,"_id","ID","ID","java.lang.Integer","com.gridnode.pdip.base.certificate.model.Certificate","certificate.id","20","0","0","0","0","0","999","displayable=true\r\neditable=false\r\nmandatory=false\r\n","type=range\r\n");
INSERT INTO fieldmetainfo VALUES (NULL,"_revokeID","REVOKEID","RevokeID","java.lang.Integer","com.gridnode.pdip.base.certificate.model.Certificate","certificate.revokeId","11","0","0","0","0","0","999","displayable=false\r\neditable=false\r\nmandatory=false\r\n","type=range\r\n");
INSERT INTO fieldmetainfo VALUES (NULL,"_isMaster","IS_MASTER","isMaster","java.lang.Boolean","com.gridnode.pdip.base.certificate.model.Certificate","certificate.isMaster","0","0","0","0","0","0","999","displayable=false\r\nmandatory=false\r\neditable=false\r\n","type=enum\r\ngeneric.yes=true\r\ngeneric.no=false\r\n");
INSERT INTO fieldmetainfo VALUES (NULL,"_isPartner","IS_PARTNER","isPartner","java.lang.Boolean","com.gridnode.pdip.base.certificate.model.Certificate","certificate.isPartner","0","0","0","0","0","0","999","displayable=true\r\nmandatory=true\r\neditable=false\r\n\r\neditable.create=true","type=enum\r\ngeneric.yes=true\r\ngeneric.no=false\r\n");
INSERT INTO fieldmetainfo VALUES(NULL,"_category","CATEGORY","Category","java.lang.Short","com.gridnode.pdip.base.certificate.model.Certificate","certificate.category","80","0","0","1","1","0","999","displayable=true\r\neditable=false\r\nmandatory=false\r\ndisplayable.create=false\r\n","type=enum\r\n##application specific\r\n");
INSERT INTO fieldmetainfo VALUES (NULL,"_isInKeyStore","IS_IN_KS","iSINKS","java.lang.Boolean","com.gridnode.pdip.base.certificate.model.Certificate","certificate.inKeyStore","0","0","0","0","0","0","999","displayable=true\r\ndisplayable.create=false\r\nmandatory=true\r\neditable=false\r\n","type=enum\r\ngeneric.yes=true\r\ngeneric.no=false\r\n");
INSERT INTO fieldmetainfo VALUES (NULL,"_isInTrustStore","IS_IN_TS","iSINTS","java.lang.Boolean","com.gridnode.pdip.base.certificate.model.Certificate","certificate.inTrustStore","0","0","0","0","0","0","999","displayable=true\r\ndisplayable.create=false\r\nmandatory=true\r\neditable=false\r\n","type=enum\r\ngeneric.yes=true\r\ngeneric.no=false\r\n");
INSERT INTO fieldmetainfo VALUES (NULL,"_relatedCertUid","RELATED_CERT_UID","relatedCertUid","java.lang.Long","com.gridnode.pdip.base.certificate.model.Certificate","certificate.relatedCertUid",0,"0","0","1","1","0",999,"displayable=true\r\neditable=true\r\nmandatory=false\r\n\r\n","type=foreign\r\nforeign.key=certificate.uid\r\nforeign.display=certificate.name\r\nforeign.cached=false");
INSERT INTO fieldmetainfo VALUES(NULL,"_startDate","START_DATE","StartDate","java.util.Date","com.gridnode.pdip.base.certificate.model.Certificate","certificate.startDate","0","0","0","0","1","","999","displayable=true\r\nmandatory=false\r\neditable=false","type=datetime\r\ndatetime.time=true\r\ndatetime.date=true\r\ndatetime.adjustment=gts");
INSERT INTO fieldmetainfo VALUES(NULL,"_endDate","END_DATE","EndDate","java.util.Date","com.gridnode.pdip.base.certificate.model.Certificate","certificate.endDate","0","0","0","0","1","","999","displayable=true\r\nmandatory=false\r\neditable=false","type=datetime\r\ndatetime.time=true\r\ndatetime.date=true\r\ndatetime.adjustment=gts");
INSERT INTO fieldmetainfo VALUES(NULL,"_isCA","IS_CA","IsCA","java.lang.Boolean","com.gridnode.pdip.base.certificate.model.Certificate","certificate.isCA","0","0","0","0","0","0","999","displayable=true\r\nmandatory=false\r\neditable=true\r\n","type=enum\r\ngeneric.yes=true\r\ngeneric.no=false\r\n");
INSERT INTO fieldmetainfo VALUES(NULL,"_replacementCertUid","REPLACEMENT_CERT_UID","ReplacementCertUid","java.lang.Long","com.gridnode.pdip.base.certificate.model.Certificate","certificate.replacementCertUid",0,"0","0","1","1","0",999,"displayable=true\r\neditable=false\r\nmandatory=false\r\n\r\n","type=foreign\r\nforeign.key=certificate.uid\r\nforeign.display=certificate.name\r\nforeign.cached=false");
INSERT INTO fieldmetainfo VALUES(NULL,"_certificateSwapping","CERTIFICATE_SWAPPING","","com.gridnode.pdip.base.certificate.model.CertificateSwapping","com.gridnode.pdip.base.certificate.model.Certificate","certificate.certificateSwapping",0,1,0,1,0,"0",999,"","type=embedded\r\nembedded.type=certificateSwapping");

#
# Dumping data for table 'entitymetainfo'for certificate swapping
#
DELETE FROM entitymetainfo WHERE EntityName IN ("CertificateSwapping");
INSERT INTO entitymetainfo VALUES ("com.gridnode.pdip.base.certificate.model.CertificateSwapping","CertificateSwapping","");


# Dumping data for table 'fieldmetainfo' for certificate swapping
#
DELETE FROM fieldmetainfo WHERE EntityObjectName LIKE "%CertificateSwapping";

INSERT INTO fieldmetainfo VALUES (NULL,"_uId","UID","","java.lang.Long","com.gridnode.pdip.base.certificate.model.CertificateSwapping","certificateSwapping.uid",20,0,0,0,0,"0",999,"displayable=false\r\neditable=false\r\nmandatory=false","type=uid");
INSERT INTO fieldmetainfo VALUES(NULL,"_swapDate","SWAP_DATE","","java.util.Date","com.gridnode.pdip.base.certificate.model.CertificateSwapping","certificateSwapping.swapDate",0,0,1,1,1,"",999,"displayable=true\r\nmandatory=false\r\neditable=true","type=datetime\r\ndatetime.time=false\r\ndatetime.date=true\r\ndatetime.pattern=yyyy-MM-dd\r\ntext.length.max=10");
INSERT INTO fieldmetainfo VALUES(NULL,"_swapTime","SWAP_TIME","","java.lang.String","com.gridnode.pdip.base.certificate.model.CertificateSwapping","certificateSwapping.swapTime",5,0,1,1,1,"",999,"displayable=true\r\nmandatory=false\r\neditable=true","type=text\r\ntext.length.max=5");
INSERT INTO fieldmetainfo VALUES (NULL,"_alarmUID","ALARM_UID","","java.lang.Long","com.gridnode.pdip.base.certificate.model.CertificateSwapping","certificateSwapping.alarmUid",20,0,0,0,0,"0",999,"displayable=false\r\neditable=false\r\nmandatory=false","type=uid");



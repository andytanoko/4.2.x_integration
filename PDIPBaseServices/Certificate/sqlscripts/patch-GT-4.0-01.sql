#[Tam Wei Xiang] [GT4] 25 July 2006   Alter table certificate to include cert validFrom and validTo date
#[Tam Wei Xiang] [GT4]                Alter table certificate to include replacementCertUid, new FMI for replacementCertUid
#[Neo Sok Lay]   [GT4] 06 Dec 2006    Change label for SerialNumber

USE userdb;
ALTER TABLE certificate ADD COLUMN (StartDate datetime default NULL,EndDate datetime default NULL, IsCA tinyint(1) NOT NULL default '0');
ALTER TABLE certificate ADD COLUMN (ReplacementCertUid bigint(20) default NULL);

USE appdb;

DELETE FROM fieldmetainfo WHERE entityobjectname="com.gridnode.pdip.base.certificate.model.Certificate" AND FieldName="START_DATE";
DELETE FROM fieldmetainfo WHERE entityobjectname="com.gridnode.pdip.base.certificate.model.Certificate" AND FieldName="END_DATE";
DELETE FROM fieldmetainfo WHERE entityobjectname="com.gridnode.pdip.base.certificate.model.Certificate" AND FieldName="IS_CA";
DELETE FROM fieldmetainfo WHERE entityobjectname="com.gridnode.pdip.base.certificate.model.Certificate" AND FieldName="REPLACEMENT_CERT_UID";

INSERT INTO fieldmetainfo VALUES(NULL,"_startDate","START_DATE","StartDate","java.util.Date","com.gridnode.pdip.base.certificate.model.Certificate","certificate.startDate","0","0","0","0","1","","999","displayable=true\r\nmandatory=false\r\neditable=false","type=datetime\r\ndatetime.time=true\r\ndatetime.date=true\r\ndatetime.adjustment=gts");
INSERT INTO fieldmetainfo VALUES(NULL,"_endDate","END_DATE","EndDate","java.util.Date","com.gridnode.pdip.base.certificate.model.Certificate","certificate.endDate","0","0","0","0","1","","999","displayable=true\r\nmandatory=false\r\neditable=false","type=datetime\r\ndatetime.time=true\r\ndatetime.date=true\r\ndatetime.adjustment=gts");
INSERT INTO fieldmetainfo VALUES(NULL,"_isCA","IS_CA","IsCA","java.lang.Boolean","com.gridnode.pdip.base.certificate.model.Certificate","certificate.isCA","0","0","0","0","0","0","999","displayable=true\r\nmandatory=false\r\neditable=true\r\n","type=enum\r\ngeneric.yes=true\r\ngeneric.no=false\r\n");
INSERT INTO fieldmetainfo VALUES(NULL,"_replacementCertUid","REPLACEMENT_CERT_UID","ReplacementCertUid","java.lang.Long","com.gridnode.pdip.base.certificate.model.Certificate","certificate.replacementCertUid",0,"0","0","1","1","0",999,"displayable=true\r\neditable=false\r\nmandatory=false\r\n\r\n","type=foreign\r\nforeign.key=certificate.uid\r\nforeign.display=certificate.name\r\nforeign.cached=false");

UPDATE fieldmetainfo
SET Label='certificate.serialNum', Presentation='displayable=true\r\neditable=false\r\nmandatory=false\r\n'
WHERE EntityObjectName='com.gridnode.pdip.base.certificate.model.Certificate'
AND FieldName='SERIALNUM';

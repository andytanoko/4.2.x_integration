#[Wong Yee Wah] [GT4.1.3]      01 AUg 2008   add EMI and FMI for for certificate swapping
#[Tam Wei Xiang][GT4.1.3]      18 Aug 2008   Add delete statement for Certificate's _certificateSwapping

use appdb;

DELETE FROM fieldmetainfo WHERE EntityObjectName='com.gridnode.pdip.base.certificate.model.Certificate' AND ObjectName='_certificateSwapping';
INSERT INTO fieldmetainfo VALUES(NULL,"_certificateSwapping","CERTIFICATE_SWAPPING","","com.gridnode.pdip.base.certificate.model.CertificateSwapping","com.gridnode.pdip.base.certificate.model.Certificate","certificate.certificateSwapping",0,1,0,1,0,"0",999,"","type=embedded\r\nembedded.type=certificateSwapping");

DELETE FROM fieldmetainfo WHERE EntityObjectName LIKE "%CertificateSwapping";
INSERT INTO fieldmetainfo VALUES (NULL,"_uId","UID","","java.lang.Long","com.gridnode.pdip.base.certificate.model.CertificateSwapping","certificateSwapping.uid",20,0,0,0,0,"0",999,"displayable=false\r\neditable=false\r\nmandatory=false","type=uid");
INSERT INTO fieldmetainfo VALUES(NULL,"_swapDate","SWAP_DATE","","java.util.Date","com.gridnode.pdip.base.certificate.model.CertificateSwapping","certificateSwapping.swapDate",0,0,1,1,1,"",999,"displayable=true\r\nmandatory=false\r\neditable=true","type=datetime\r\ndatetime.time=false\r\ndatetime.date=true\r\ndatetime.pattern=yyyy-MM-dd\r\ntext.length.max=10");
INSERT INTO fieldmetainfo VALUES(NULL,"_swapTime","SWAP_TIME","","java.lang.String","com.gridnode.pdip.base.certificate.model.CertificateSwapping","certificateSwapping.swapTime",5,0,1,1,1,"",999,"displayable=true\r\nmandatory=false\r\neditable=true","type=text\r\ntext.length.max=5");
INSERT INTO fieldmetainfo VALUES (NULL,"_alarmUID","ALARM_UID","","java.lang.Long","com.gridnode.pdip.base.certificate.model.CertificateSwapping","certificateSwapping.alarmUid",20,0,0,0,0,"0",999,"displayable=false\r\neditable=false\r\nmandatory=false","type=uid");

DELETE FROM entitymetainfo WHERE EntityName IN ("CertificateSwapping");
INSERT INTO entitymetainfo VALUES ("com.gridnode.pdip.base.certificate.model.CertificateSwapping","CertificateSwapping","");
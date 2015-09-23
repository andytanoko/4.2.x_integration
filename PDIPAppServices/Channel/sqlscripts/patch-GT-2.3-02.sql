# 31 Oct 2003 I1 GT2.3 [Guo Jianyu] add packaging profile extension
# 

use appdb;

# 20040113 DDJ: Commented out AS2 change
#UPDATE fieldmetainfo
#SET Constraints="type=enum\r\npackagingInfo.envelope.none=NONE\r\npackagingInfo.envelope.rnif1=RNIF1\r\npackagingInfo.envelope.rnif2=RNIF2\r\npackagingInfo.envelope.as2=AS2\r\n"
#WHERE FieldName="ENVELOPE"
#AND EntityObjectName="com.gridnode.pdip.app.channel.model.PackagingInfo";

UPDATE fieldmetainfo
SET Constraints="type=enum\r\nsecurityInfo.encLevel.40=40\r\nsecurityInfo.encLevel.64=64\r\nsecurityInfo.encLevel.128=128\r\nsecurityInfo.encLevel.168=168\r\nsecurityInfo.encLevel.256=256\r\nsecurityInfo.encLevel.512=512\r\nsecurityInfo.encLevel.1024=1024"
WHERE FieldName="ENCRYPTION_LEVEL"
AND EntityObjectName="com.gridnode.pdip.app.channel.model.SecurityInfo";


INSERT INTO fieldmetainfo VALUES (NULL,'_pkgInfoExtension','PKG_INFO_EXTENSION','PackagingInfoExtension','com.gridnode.pdip.app.channel.model.PackagingInfoExtension','com.gridnode.pdip.app.channel.model.PackagingInfo','packagingInfo.packagingInfoExtension',0,'0','0','0','0','0',999,'displayable=true\r\neditable=true\r\nmandatory=true\r\n','type=dynamic\r\ndynamic.types=as2PackagingInfoExtension\r\n');

# AS2PackagingInfoExtension.
INSERT INTO entitymetainfo VALUES ('com.gridnode.pdip.app.channel.model.AS2PackagingInfoExtension','AS2PackagingInfoExtension','');
DELETE FROM fieldmetainfo WHERE EntityObjectName LIKE '%AS2PackagingInfoExtension';
INSERT INTO fieldmetainfo VALUES (NULL,'_isAckReq','IS_ACK_REQ','','java.lang.Boolean','com.gridnode.pdip.app.channel.model.AS2PackagingInfoExtension','as2PackagingInfoExtension.isAckReq','0','0','0','0','0','0',999,'displayable=true\r\nmandatory=false\r\neditable=true\r\n','type=enum\r\ngeneric.yes=true\r\ngeneric.no=false\r\n');
INSERT INTO fieldmetainfo VALUES (NULL,'_isAckSigned','IS_ACK_SIGNED','','java.lang.Boolean','com.gridnode.pdip.app.channel.model.AS2PackagingInfoExtension','as2PackagingInfoExtension.isAckSigned','0','0','0','0','0','0',999,'displayable=true\r\nmandatory=false\r\neditable=true\r\n','type=enum\r\ngeneric.yes=true\r\ngeneric.no=false\r\n');
INSERT INTO fieldmetainfo VALUES (NULL,'_isNRRReq','IS_NRR_REQ','','java.lang.Boolean','com.gridnode.pdip.app.channel.model.AS2PackagingInfoExtension','as2PackagingInfoExtension.isNrrReq','0','0','0','0','0','0',999,'displayable=true\r\nmandatory=false\r\neditable=true\r\n','type=enum\r\ngeneric.yes=true\r\ngeneric.no=false\r\n');
INSERT INTO fieldmetainfo VALUES (NULL,'_isAckSyn','IS_ACK_SYN','','java.lang.Boolean','com.gridnode.pdip.app.channel.model.AS2PackagingInfoExtension','as2PackagingInfoExtension.isAckSyn','0','0','0','0','0','0',999,'displayable=true\r\nmandatory=false\r\neditable=true\r\n','type=enum\r\ngeneric.yes=true\r\ngeneric.no=false\r\n');
INSERT INTO fieldmetainfo VALUES (NULL,'_returnURL','RETURN_URL','','java.lang.String','com.gridnode.pdip.app.channel.model.AS2PackagingInfoExtension','as2PackagingInfoExtension.returnUrl','200','0','1','1','1','',999,'displayable=true\r\nmandatory=false\r\neditable=true\r\n','type=text\r\ntext.length.max=200');

# Security Profile
INSERT INTO fieldmetainfo VALUES (NULL,'_compressionType','COMPRESSION_TYPE','CompressionType','java.lang.String','com.gridnode.pdip.app.channel.model.SecurityInfo','securityInfo.compressionType',10,'0','0','1','1','',999,'displayable=true\r\neditable=true\r\nmandatory=true\r\n\r\n','type=enum\r\nsecurityInfo.compressionType.none=None\r\nsecurityInfo.compressionType.smime=S/MIME');
INSERT INTO fieldmetainfo VALUES (NULL,'_compressionMethod','COMPRESSION_METHOD','CompressionMethod','java.lang.String','com.gridnode.pdip.app.channel.model.SecurityInfo','securityInfo.compressionMethod',0,'0','1','1','1','0',999,'displayable=true\r\neditable=true\r\nmandatory=true\r\n\r\n','type=enum\r\nsecurityInfo.compressionMethod.zlib=ZLIB');
INSERT INTO fieldmetainfo VALUES (NULL,'_compressionLevel','COMPRESSION_LEVEL','CompressionLevel','java.lang.Integer','com.gridnode.pdip.app.channel.model.SecurityInfo','securityInfo.compressionLevel',0,'0','0','0','0','',999,'displayable=true\r\neditable=true\r\nmandatory=true\r\n\r\n','type=enum\r\nsecurityInfo.compressionLevel.0=0\r\nsecurityInfo.compressionLevel.1=1\r\nsecurityInfo.compressionLevel.2=2\r\nsecurityInfo.compressionLevel.3=3');
INSERT INTO fieldmetainfo VALUES (NULL,'_sequence','SEQUENCE','Sequence','java.lang.String','com.gridnode.pdip.app.channel.model.SecurityInfo','securityInfo.sequence',10,'0','0','1','1','',999,'displayable=true\r\neditable=true\r\nmandatory=true\r\n\r\n','type=enum\r\nsecurityInfo.sequence.sign_compress=S;C\r\nsecurityInfo.sequence.compress_sign=C;S\r\nsecurityInfo.sequence.sign_encrypt=S;E\r\nsecurityInfo.sequence.encrypt_sign=E;S\r\nsecurityInfo.sequence.compress_encrypt=C;E\r\nsecurityInfo.sequence.sign_compress_encrypt=S;C;E\r\nsecurityInfo.sequence.compress_sign_encrypt=C;S;E\r\nsecurityInfo.sequence.compress_encrypt_sign=C;E;S');
INSERT INTO fieldmetainfo VALUES (NULL,'_encryptionAlgorithm','ENCRYPTION_ALGORITHM','EncryptionAlgorithm','java.lang.String','com.gridnode.pdip.app.channel.model.SecurityInfo', 'securityInfo.encryptionAlgorithm', 80,'0','0','1','1','', 999, 'displayable=true\r\neditable=true\nmandatory=false\r\n','type=enum\r\nsecurityInfo.encryptionAlgorithm.TDES_ALG=3DES_EDE/CBC/PKCS5Padding\r\nsecurityInfo.encryptionAlgorithm.RC2_ALG=RC2/CBC/PKCS5Padding');

use userdb;

ALTER TABLE `packaging_profile`
ADD COLUMN `PackagingInfoExtension` blob;

ALTER TABLE `security_profile` ADD COLUMN `CompressionType` varchar(30) default NULL;
ALTER TABLE `security_profile` ADD COLUMN `CompressionMethod` varchar(30) default NULL;
ALTER TABLE `security_profile` ADD COLUMN `CompressionLevel` int(10) default NULL;
ALTER TABLE `security_profile` ADD COLUMN `Sequence` varchar(30) default NULL;
ALTER TABLE `security_profile` ADD COLUMN `EncryptionAlgorithm` varchar(30) default NULL;


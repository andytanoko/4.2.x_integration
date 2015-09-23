# Tam Wei Xiang        7 Nov 2007      GT2.4.8       Add the new column "isCompressRequired"

USE userdb;
ALTER TABLE `process_act` ADD COLUMN IsCompressRequired TINYINT(1) DEFAULT NULL;

USE appdb;
DELETE FROM fieldmetainfo WHERE EntityObjectName='com.gridnode.pdip.app.rnif.model.ProcessAct' AND FieldName='IS_COMPRESS_REQUIRED';
INSERT INTO fieldmetainfo VALUES (NULL,'_isCompressRequired','IS_COMPRESS_REQUIRED','IsCompressRequired','java.lang.Boolean','com.gridnode.pdip.app.rnif.model.ProcessAct', 'processAct.isCompressRequired', 0,'0','0','1','1','', 999, 'displayable=true\r\neditable=true\r\nmandatory=false\r\n','type=enum\r\nprocessAct.isCompressRequired.true=true\r\nprocessAct.isCompressRequired.false=false');

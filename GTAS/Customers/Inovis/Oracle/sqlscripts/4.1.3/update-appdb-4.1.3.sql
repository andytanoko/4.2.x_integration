-- Apply following script to "APPDB" schema

DELETE FROM "entitymetainfo" WHERE "EntityName" IN ('CertificateSwapping');
INSERT INTO "entitymetainfo" VALUES ('com.gridnode.pdip.base.certificate.model.CertificateSwapping','CertificateSwapping','');

--------- CertificateSwapping
DELETE FROM "fieldmetainfo" WHERE "EntityObjectName" LIKE '%CertificateSwapping';
INSERT INTO "fieldmetainfo" VALUES (NULL,'_uId','UID','','java.lang.Long','com.gridnode.pdip.base.certificate.model.CertificateSwapping','certificateSwapping.uid',20,0,0,0,0,'0',999,'displayable=false'||chr(13)||chr(10)||'editable=false'||chr(13)||chr(10)||'mandatory=false'||chr(13)||chr(10),'type=uid'||chr(13)||chr(10));

INSERT INTO "fieldmetainfo" VALUES(NULL,'_swapDate','SWAP_DATE','','java.util.Date','com.gridnode.pdip.base.certificate.model.CertificateSwapping','certificateSwapping.swapDate',0,0,1,1,1,'',999,'displayable=true'||chr(13)||chr(10)||'mandatory=false'||chr(13)||chr(10)||'editable=true','type=datetime'||chr(13)||chr(10)||'datetime.time=false'||chr(13)||chr(10)||'datetime.date=true'||chr(13)||chr(10)||'datetime.pattern=yyyy-MM-dd'||chr(13)||chr(10)||'text.length.max=10');

INSERT INTO "fieldmetainfo" VALUES(NULL,'_swapTime','SWAP_TIME','','java.lang.String','com.gridnode.pdip.base.certificate.model.CertificateSwapping','certificateSwapping.swapTime',5,0,1,1,1,'',999,'displayable=true'||chr(13)||chr(10)||'mandatory=false'||chr(13)||chr(10)||'editable=true','type=text'||chr(13)||chr(10)||'text.length.max=5');

INSERT INTO "fieldmetainfo" VALUES (NULL,'_alarmUID','ALARM_UID','','java.lang.Long','com.gridnode.pdip.base.certificate.model.CertificateSwapping','certificateSwapping.alarmUid',20,0,0,0,0,'0',999,'displayable=false'||chr(13)||chr(10)||'editable=false'||chr(13)||chr(10)||'mandatory=false'||chr(13)||chr(10),'type=uid'||chr(13)||chr(10));

-------------Certificate
DELETE FROM "fieldmetainfo" WHERE "EntityObjectName"='com.gridnode.pdip.base.certificate.model.Certificate' AND "ObjectName"='_certificateSwapping';
INSERT INTO "fieldmetainfo" VALUES(NULL,'_certificateSwapping','CERTIFICATE_SWAPPING','','com.gridnode.pdip.base.certificate.model.CertificateSwapping','com.gridnode.pdip.base.certificate.model.Certificate','certificate.certificateSwapping',0,1,0,1,0,'0',999,'','type=embedded'||chr(13)||chr(10)||'embedded.type=certificateSwapping'||chr(13)||chr(10));
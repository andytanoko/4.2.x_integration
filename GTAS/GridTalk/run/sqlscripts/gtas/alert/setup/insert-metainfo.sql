SET client_encoding = 'UTF8';
SET check_function_bodies = false;
SET client_min_messages = warning;
SET search_path = appdb;

-- Dumping data for table 'entitymetainfo'
--
--Field 1 ObjectName: full qualified class name of the entity
--# Field 2 EntityName: short class name of the entity
--# Field 3 SqlName: table name of the entity

DELETE FROM "entitymetainfo" WHERE "ObjectName" IN ('com.gridnode.gtas.server.alert.model.AlertTrigger');
INSERT INTO "entitymetainfo" VALUES ('com.gridnode.gtas.server.alert.model.AlertTrigger','AlertTrigger','"alert_trigger"');

--
-- Dumping data for table 'fieldmetainfo'
--
-- UID bigint(20) NOT NULL auto_increment,
-- ObjectName: field name in Entity class ,
-- FieldName: field ID in Entity Interface class ,
-- SqlName: Field column name in Table,
-- ValueClass: field data type,
-- EntityObjectName: full qualified class name of the entity
-- Label: field display label
-- Length: valid field length ,
-- Proxy: '1' if proxy, '0' otherwise,,
-- Mandatory: '1' if mandatory, '0' otherwise,
-- Editable: '1' if editable, '0' otherwise
-- Displayable: '1' if displayable, '0' otherwise
-- OqlName: ,
-- Sequence: default '999' ,
-- Presentation: properties for presentation of the field
-- Constraints: constraint imposed on the values of the field
--

---------- "fieldmetainfo" for alert_trigger
DELETE FROM "fieldmetainfo" WHERE "EntityObjectName" LIKE '%.AlertTrigger';
INSERT INTO "fieldmetainfo" VALUES(DEFAULT,'_uId','UID','"UID"','java.lang.Long','com.gridnode.gtas.server.alert.model.AlertTrigger','alertTrigger.uid',0,0,0,0,0,'',999,'displayable=false'||chr(13)||chr(10)||'mandatory=false'||chr(13)||chr(10)||'editable=false','type=uid');
INSERT INTO "fieldmetainfo" VALUES(DEFAULT,'_version','VERSION','"Version"','java.lang.Double','com.gridnode.gtas.server.alert.model.AlertTrigger','alertTrigger.version',0,0,0,0,0,'',999,'displayable=false'||chr(13)||chr(10)||'mandatory=false'||chr(13)||chr(10)||'editable=false','type=range');
INSERT INTO "fieldmetainfo" VALUES(DEFAULT,'_canDelete','CAN_DELETE','"CanDelete"','java.lang.Boolean','com.gridnode.gtas.server.alert.model.AlertTrigger','alertTrigger.canDelete',0,0,0,0,0,'',999,'displayable=false'||chr(13)||chr(10)||'mandatory=false'||chr(13)||chr(10)||'editable=false','type=enum'||chr(13)||chr(10)||'candelete.enabled=true'||chr(13)||chr(10)||'candelete.disabled=false');
INSERT INTO "fieldmetainfo" VALUES(DEFAULT,'_level','LEVEL','"Level"','java.lang.Integer','com.gridnode.gtas.server.alert.model.AlertTrigger','alertTrigger.level',0,0,1,1,1,'',999,'displayable=true'||chr(13)||chr(10)||'mandatory=true'||chr(13)||chr(10)||'editable=false'||chr(13)||chr(10)||'editable.create=true','type=enum'||chr(13)||chr(10)||'alertTrigger.level.zero=0'||chr(13)||chr(10)||'alertTrigger.level.one=1'||chr(13)||chr(10)||'alertTrigger.level.two=2'||chr(13)||chr(10)||'alertTrigger.level.three=3'||chr(13)||chr(10)||'alertTrigger.level.four=4');
INSERT INTO "fieldmetainfo" VALUES(DEFAULT,'_alertUID','ALERT_UID','"AlertUID"','java.lang.Long','com.gridnode.gtas.server.alert.model.AlertTrigger','alertTrigger.alertUid',0,0,1,1,1,'',999,'displayable=true'||chr(13)||chr(10)||'mandatory=true'||chr(13)||chr(10)||'editable=true','type=foreign'||chr(13)||chr(10)||'foreign.key=alert.uid'||chr(13)||chr(10)||'foreign.display=alert.description');
INSERT INTO "fieldmetainfo" VALUES(DEFAULT,'_recipients','RECIPIENTS','"Recipients"','java.util.List','com.gridnode.gtas.server.alert.model.AlertTrigger','alertTrigger.recipients',0,0,0,1,1,'',999,'displayable=true'||chr(13)||chr(10)||'mandatory=false'||chr(13)||chr(10)||'editable=true','type=text'||chr(13)||chr(10)||'collection=true'||chr(13)||chr(10)||'collection.element=java.lang.String'||chr(13)||chr(10));
INSERT INTO "fieldmetainfo" VALUES(DEFAULT,'_docType','DOC_TYPE','"DocType"','java.lang.String','com.gridnode.gtas.server.alert.model.AlertTrigger','alertTrigger.docType',30,0,0,1,1,'',999,'displayable=true'||chr(13)||chr(10)||'mandatory=true'||chr(13)||chr(10)||'editable=true','type=foreign'||chr(13)||chr(10)||'foreign.key=documentType.docType'||chr(13)||chr(10)||'foreign.display=documentType.description');
INSERT INTO "fieldmetainfo" VALUES(DEFAULT,'_partnerType','PARTNER_TYPE','"PartnerType"','java.lang.String','com.gridnode.gtas.server.alert.model.AlertTrigger','alertTrigger.partnerType',3,0,0,1,1,'',999,'displayable=true'||chr(13)||chr(10)||'mandatory=true'||chr(13)||chr(10)||'editable=true','type=foreign'||chr(13)||chr(10)||'foreign.key=partnerType.partnerType'||chr(13)||chr(10)||'foreign.display=partnerType.description');
INSERT INTO "fieldmetainfo" VALUES(DEFAULT,'_partnerGroup','PARTNER_GROUP','"PartnerGroup"','java.lang.String','com.gridnode.gtas.server.alert.model.AlertTrigger','alertTrigger.partnerGroup',3,0,0,1,1,'',999,'displayable=true'||chr(13)||chr(10)||'mandatory=true'||chr(13)||chr(10)||'editable=true','type=foreign'||chr(13)||chr(10)||'foreign.key=partnerGroup.name'||chr(13)||chr(10)||'foreign.display=partnerGroup.description');
INSERT INTO "fieldmetainfo" VALUES(DEFAULT,'_partnerId','PARTNER_ID','"PartnerId"','java.lang.String','com.gridnode.gtas.server.alert.model.AlertTrigger','alertTrigger.partnerId',20,0,0,1,1,'',999,'displayable=true'||chr(13)||chr(10)||'mandatory=false'||chr(13)||chr(10)||'editable=true','type=foreign'||chr(13)||chr(10)||'foreign.key=partner.partnerId'||chr(13)||chr(10)||'foreign.display=partner.name');
INSERT INTO "fieldmetainfo" VALUES(DEFAULT,'_alertType','ALERT_TYPE','"AlertType"','java.lang.String','com.gridnode.gtas.server.alert.model.AlertTrigger','alertTrigger.alertType',35,0,1,1,1,'',999,'displayable=true'||chr(13)||chr(10)||'mandatory=true'||chr(13)||chr(10)||'editable=false'||chr(13)||chr(10)||'editable.create=true','type=foreign'||chr(13)||chr(10)||'foreign.key=alertType.name'||chr(13)||chr(10)||'foreign.display=alertType.description'||chr(13)||chr(10)||'foreign.cached=false');
INSERT INTO "fieldmetainfo" VALUES(DEFAULT,'_enabled','IS_ENABLED','"Enabled"','java.lang.Boolean','com.gridnode.gtas.server.alert.model.AlertTrigger','alertTrigger.isEnabled',0,0,0,0,0,'',999,'displayable=true'||chr(13)||chr(10)||'mandatory=true'||chr(13)||chr(10)||'editable=true','type=enum'||chr(13)||chr(10)||'generic.yes=true'||chr(13)||chr(10)||'generic.no=false');
INSERT INTO "fieldmetainfo" VALUES(DEFAULT,'_attachDoc','IS_ATTACH_DOC','"AttachDoc"','java.lang.Boolean','com.gridnode.gtas.server.alert.model.AlertTrigger','alertTrigger.isAttachDoc',0,0,0,0,0,'',999,'displayable=true'||chr(13)||chr(10)||'mandatory=false'||chr(13)||chr(10)||'editable=true','type=enum'||chr(13)||chr(10)||'generic.yes=true'||chr(13)||chr(10)||'generic.no=false');



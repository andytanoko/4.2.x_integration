# Change Log
# 04 Dec 2003 GT 2.2 [Neo Sok Lay] Display certificate.id in addition to certificate.name in BizCertMapping to avoid confusion
# 06 Oct 2005 GT 4.0 I1  [Neo Sok Lay] Change 'trigger' table name to 'pf_trigger' -- clash with Trigger introduced in MySQL5
# 08 Feb 2006 GT 4.0 I1  [Neo Sok Lay] Increase PartnerFunctionId column size to 30chars
# 05 Dec 2006 GT 4.0 I3  [Neo Sok Lay] Increase DocType field length to 30 chars.

use appdb;

#
# Dumping data for table 'entitymetainfo'
#
# Field 1 ObjectName: full qualified class name of the entity
# Field 2 EntityName: short class name of the entity
# Field 3 SqlName: table name of the entity

# entitymetainfo for gridtalk_mapping_rule
DELETE FROM entitymetainfo
WHERE ObjectName
IN ('com.gridnode.gtas.server.partnerprocess.model.Trigger',
    'com.gridnode.gtas.server.partnerprocess.model.ProcessMapping',
    'com.gridnode.gtas.server.partnerprocess.model.BizCertMapping');

INSERT INTO entitymetainfo VALUES ("com.gridnode.gtas.server.partnerprocess.model.Trigger","Trigger","pf_trigger");
INSERT INTO entitymetainfo VALUES ("com.gridnode.gtas.server.partnerprocess.model.ProcessMapping","ProcessMapping","process_mapping");
INSERT INTO entitymetainfo VALUES ("com.gridnode.gtas.server.partnerprocess.model.BizCertMapping","BizCertMapping","biz_cert_mapping");

#
# Dumping data for table 'fieldmetainfo'
#
# UID bigint(20) NOT NULL auto_increment,
# ObjectName: field name in Entity class ,
# FieldName: field ID in Entity Interface class ,
# SqlName: Field column name in Table,
# ValueClass: field data type,
# EntityObjectName: full qualified class name of the entity
# Label: field display label
# Length: valid field length ,
# Proxy: '1' if proxy, '0' otherwise,,
# Mandatory: '1' if mandatory, '0' otherwise,
# Editable: '1' if editable, '0' otherwise
# Displayable: '1' if displayable, '0' otherwise
# OqlName: ,
# Sequence: default '999' ,
# Presentation: properties for presentation of the field
# Constraints: constraint imposed on the values of the field
#

# fieldmetainfo for trigger
DELETE FROM fieldmetainfo WHERE EntityObjectName='com.gridnode.gtas.server.partnerprocess.model.Trigger';
INSERT INTO fieldmetainfo VALUES(
NULL,"_uId","UID","UID","java.lang.Long",
"com.gridnode.gtas.server.partnerprocess.model.Trigger","trigger.uid",
"0","0","0","0","0","","999",
"displayable=false\r\nmandatory=false\r\neditable=false",
"type=uid"
);
INSERT INTO fieldmetainfo VALUES(
NULL,"_version","VERSION","Version","java.lang.Double",
"com.gridnode.gtas.server.partnerprocess.model.Trigger","",
"0","0","0","0","0","","999",
"displayable=false\r\nmandatory=false\r\neditable=false",
"type=range"
);
INSERT INTO fieldmetainfo VALUES(
NULL,"_canDelete","CAN_DELETE","CanDelete","java.lang.Boolean",
"com.gridnode.gtas.server.partnerprocess.model.Trigger","",
"0","0","0","0","0","","999",
"displayable=false\r\nmandatory=false\r\neditable=false",
"type=enum\r\ncandelete.enabled=true\r\ncandelete.disabled=false"
);
INSERT INTO fieldmetainfo VALUES(
NULL,"_triggerLevel","TRIGGER_LEVEL","TriggerLevel","java.lang.Integer",
"com.gridnode.gtas.server.partnerprocess.model.Trigger","trigger.triggerLevel",
"0","0","1","1","1","","999",
"displayable=true\r\nmandatory=true\r\neditable=true",
"type=enum\r\ntrigger.triggerLevel.zero=0\r\ntrigger.triggerLevel.one=1\r\ntrigger.triggerLevel.two=2\r\ntrigger.triggerLevel.three=3\r\ntrigger.triggerLevel.four=4"
);
INSERT INTO fieldmetainfo VALUES(
NULL,"_partnerFunctionId","PARTNER_FUNCTION_ID","PartnerFunctionId","java.lang.String",
"com.gridnode.gtas.server.partnerprocess.model.Trigger","trigger.partnerFunctionId",
"30","0","1","1","1","","999",
"displayable=true\r\nmandatory=true\r\neditable=true",
"type=foreign\r\nforeign.key=partnerFunction.id\r\nforeign.display=partnerFunction.id"
);
INSERT INTO fieldmetainfo VALUES(
NULL,"_processId","PROCESS_ID","ProcessId","java.lang.String",
"com.gridnode.gtas.server.partnerprocess.model.Trigger","trigger.processId",
"80","0","0","1","1","","999",
"displayable=true\r\nmandatory=false\r\neditable=true",
"type=foreign\r\nforeign.key=processDef.defName\r\nforeign.display=processDef.defName"
);
INSERT INTO fieldmetainfo VALUES(
NULL,"_docType","DOC_TYPE","DocType","java.lang.String",
"com.gridnode.gtas.server.partnerprocess.model.Trigger","trigger.docType",
"30","0","0","1","1","","999",
"displayable=true\r\nmandatory=true\r\neditable=true",
"type=foreign\r\nforeign.key=documentType.docType\r\nforeign.display=documentType.docType"
);
INSERT INTO fieldmetainfo VALUES(
NULL,"_partnerType","PARTNER_TYPE","PartnerType","java.lang.String",
"com.gridnode.gtas.server.partnerprocess.model.Trigger","trigger.partnerType",
"3","0","0","1","1","","999",
"displayable=true\r\nmandatory=true\r\neditable=true",
"type=foreign\r\nforeign.key=partnerType.partnerType\r\nforeign.display=partnerType.partnerType"
);
INSERT INTO fieldmetainfo VALUES(
NULL,"_partnerGroup","PARTNER_GROUP","PartnerGroup","java.lang.String",
"com.gridnode.gtas.server.partnerprocess.model.Trigger","trigger.partnerGroup",
"3","0","0","1","1","","999",
"displayable=true\r\nmandatory=false\r\neditable=true",
"type=foreign\r\nforeign.key=partnerGroup.name\r\nforeign.display=partnerGroup.name"
);
INSERT INTO fieldmetainfo VALUES(
NULL,"_partnerId","PARTNER_ID","PartnerId","java.lang.String",
"com.gridnode.gtas.server.partnerprocess.model.Trigger","trigger.partnerId",
"20","0","0","1","1","","999",
"displayable=true\r\nmandatory=true\r\neditable=true",
"type=foreign\r\nforeign.key=partner.partnerId\r\nforeign.display=partner.partnerId"
);
INSERT INTO fieldmetainfo VALUES(
NULL,"_triggerType","TRIGGER_TYPE","TriggerType","java.lang.Integer",
"com.gridnode.gtas.server.partnerprocess.model.Trigger","trigger.triggerType",
"0","0","1","1","1","","999",
"displayable=true\r\nmandatory=true\r\neditable=true",
"type=enum\r\ntrigger.triggerType.import=0\r\ntrigger.triggerType.receive=1\r\ntrigger.triggerType.manualSend=2\r\ntrigger.triggerType.manualExport=3"
);
INSERT INTO fieldmetainfo VALUES(
NULL,"_isRequest","IS_REQUEST","IsRequest","java.lang.Boolean",
"com.gridnode.gtas.server.partnerprocess.model.Trigger","trigger.isRequest",
"0","0","0","0","0","","999",
"displayable=true\r\nmandatory=false\r\neditable=true",
"type=enum\r\ntrigger.isRequest.true=true\r\ntrigger.isRequest.false=false"
);
INSERT INTO fieldmetainfo VALUES(
NULL,"_isLocalPending","IS_LOCAL_PENDING","IsLocalPending","java.lang.Boolean",
"com.gridnode.gtas.server.partnerprocess.model.Trigger","trigger.isLocalPending",
"0","0","0","0","0","","999",
"displayable=true\r\nmandatory=false\r\neditable=true",
"type=enum\r\ntrigger.isLocalPending.true=true\r\ntrigger.isLocalPending.false=false"
);
INSERT INTO fieldmetainfo VALUES(
NULL,"_numOfRetries","NUM_OF_RETRIES","NumOfRetries","java.lang.Integer",
"com.gridnode.gtas.server.partnerprocess.model.Trigger","trigger.numOfRetries",
"0","0","0","0","0","","999",
"displayable=true\r\nmandatory=false\r\neditable=true",
"type=text"
);
INSERT INTO fieldmetainfo VALUES(
NULL,"_retryInterval","RETRY_INTERVAL","RetryInterval","java.lang.Integer",
"com.gridnode.gtas.server.partnerprocess.model.Trigger","trigger.retryInterval",
"0","0","0","0","0","","999",
"displayable=true\r\nmandatory=false\r\neditable=true",
"type=text"
);
INSERT INTO fieldmetainfo VALUES(
NULL,"_channelUID","CHANNEL_UID","ChannelUID","java.lang.Long",
"com.gridnode.gtas.server.partnerprocess.model.Trigger","trigger.channelUid",
"0","0","0","0","0","","999",
"displayable=true\r\nmandatory=false\r\neditable=true",
"type=foreign\r\nforeign.key=channelInfo.uid\r\nforeign.display=channelInfo.description\r\nforeign.cached=false"
);

# fieldmetainfo for process_mapping
DELETE FROM fieldmetainfo WHERE EntityObjectName='com.gridnode.gtas.server.partnerprocess.model.ProcessMapping';
INSERT INTO fieldmetainfo VALUES(
NULL,"_uId","UID","UID","java.lang.Long",
"com.gridnode.gtas.server.partnerprocess.model.ProcessMapping","processMapping.uid",
"0","0","0","0","0","","999",
"displayable=false\r\nmandatory=false\r\neditable=false",
"type=uid"
);
INSERT INTO fieldmetainfo VALUES(
NULL,"_version","VERSION","Version","java.lang.Double",
"com.gridnode.gtas.server.partnerprocess.model.ProcessMapping","processMapping.version",
"0","0","0","0","0","","999",
"displayable=false\r\nmandatory=false\r\neditable=false",
"type=range"
);
INSERT INTO fieldmetainfo VALUES(
NULL,"_canDelete","CAN_DELETE","CanDelete","java.lang.Boolean",
"com.gridnode.gtas.server.partnerprocess.model.ProcessMapping","processMapping.canDelete",
"0","0","0","0","0","","999",
"displayable=false\r\nmandatory=false\r\neditable=false",
"type=enum\r\ncandelete.enabled=true\r\ncandelete.disabled=false"
);
INSERT INTO fieldmetainfo VALUES(
NULL,"_partnerRoleMapping","PARTNER_ROLE_MAPPING","PartnerRoleMapping","java.lang.Long",
"com.gridnode.gtas.server.partnerprocess.model.ProcessMapping","processMapping.partnerRoleMapping",
"0","0","1","1","1","","999",
"displayable=false\r\nmandatory=false\r\neditable=false",
"type=other"
);
INSERT INTO fieldmetainfo VALUES(
NULL,"_processVersionID","PROCESS_VERSION_ID","ProcessVersionID","java.lang.String",
"com.gridnode.gtas.server.partnerprocess.model.ProcessMapping","processMapping.processVersionId",
"0","0","1","1","1","","999",
"displayable=false\r\nmandatory=false\r\neditable=false",
"type=text\r\n"
);
INSERT INTO fieldmetainfo VALUES(
NULL,"_processIndicatorCode","PROCESS_INDICATOR_CODE","ProcessIndicatorCode","java.lang.String",
"com.gridnode.gtas.server.partnerprocess.model.ProcessMapping","processMapping.processIndicatorCode",
"80","0","0","1","1","","999",
"displayable=false\r\nmandatory=false\r\neditable=false",
"type=text\r\n"
);
INSERT INTO fieldmetainfo VALUES(
NULL,"_docType","DOC_TYPE","DocType","java.lang.String",
"com.gridnode.gtas.server.partnerprocess.model.ProcessMapping","processMapping.docType",
"30","0","0","1","1","","999",
"displayable=true\r\nmandatory=true\r\neditable=true",
"type=foreign\r\nforeign.key=documentType.docType\r\nforeign.display=documentType.description\r\nforeign.cached=false\r\n"
);
INSERT INTO fieldmetainfo VALUES(
NULL,"_sendChannelUID","SEND_CHANNEL_UID","SendChannelUID","java.lang.Long",
"com.gridnode.gtas.server.partnerprocess.model.ProcessMapping","processMapping.sendChannelUid",
"0","0","0","1","1","","999",
"displayable=true\r\nmandatory=true\r\neditable=true",
"type=foreign\r\nforeign.key=channelInfo.uid\r\nforeign.display=channelInfo.description\r\nforeign.cached=false"
);
INSERT INTO fieldmetainfo VALUES(
NULL,"_isInitiatingRole","IS_INITIATING_ROLE","IsInitiatingRole","java.lang.Boolean",
"com.gridnode.gtas.server.partnerprocess.model.ProcessMapping","processMapping.isInitiatingRole",
"0","0","0","1","1","","999",
"displayable=true\r\nmandatory=true\r\neditable=true",
"type=enum\r\nprocessMapping.isInitiatingRole.initiating=true\r\nprocessMapping.isInitiatingRole.responding=false\r\n"
);
INSERT INTO fieldmetainfo VALUES(
NULL,"_partnerID","PARTNER_ID","PartnerID","java.lang.String",
"com.gridnode.gtas.server.partnerprocess.model.ProcessMapping","processMapping.partnerId",
"20","0","0","1","1","","999",
"displayable=true\r\nmandatory=true\r\neditable=true",
"type=foreign\r\nforeign.key=partner.partnerId\r\nforeign.display=partner.name\r\nforeign.cached=false"
);
INSERT INTO fieldmetainfo VALUES(
NULL,"_processDef","PROCESS_DEF","ProcessDef","java.lang.String",
"com.gridnode.gtas.server.partnerprocess.model.ProcessMapping","processMapping.processDef",
"80","0","0","1","1","","999",
"displayable=true\r\nmandatory=true\r\neditable=true",
"type=foreign\r\nforeign.key=processDef.defName\r\nforeign.display=processDef.defName\r\nforeign.cached=false"
);

# fieldmetainfo for biz_cert_mapping
DELETE FROM fieldmetainfo WHERE EntityObjectName='com.gridnode.gtas.server.partnerprocess.model.BizCertMapping';
INSERT INTO fieldmetainfo VALUES(
NULL,"_uId","UID","UID","java.lang.Long",
"com.gridnode.gtas.server.partnerprocess.model.BizCertMapping","bizCertMapping.uid",
"0","0","0","0","0","","999",
"displayable=false\r\nmandatory=false\r\neditable=false",
"type=uid"
);
INSERT INTO fieldmetainfo VALUES(
NULL,"_version","VERSION","Version","java.lang.Double",
"com.gridnode.gtas.server.partnerprocess.model.BizCertMapping","bizCertMapping.version",
"0","0","0","0","0","","999",
"displayable=false\r\nmandatory=false\r\neditable=false",
"type=range"
);
INSERT INTO fieldmetainfo VALUES(
NULL,"_canDelete","CAN_DELETE","CanDelete","java.lang.Boolean",
"com.gridnode.gtas.server.partnerprocess.model.BizCertMapping","bizCertMapping.canDelete",
"0","0","0","0","0","","999",
"displayable=false\r\nmandatory=false\r\neditable=false",
"type=enum\r\ncandelete.enabled=true\r\ncandelete.disabled=false"
);
INSERT INTO fieldmetainfo VALUES(
NULL,"_partnerCert","PARTNER_CERT","PartnerCertUID","com.gridnode.pdip.base.certificate.model.Certificate",
"com.gridnode.gtas.server.partnerprocess.model.BizCertMapping","bizCertMapping.partnerCert",
"0","0","1","1","1","","999",
"displayable=true\r\nmandatory=true\r\neditable=true",
"type=foreign\r\nforeign.key=certificate.uid\r\nforeign.cached=true\r\nforeign.display=certificate.name\r\nforeign.additionalDisplay=id"
);
INSERT INTO fieldmetainfo VALUES(
NULL,"_ownCert","OWN_CERT","OwnCertUID","com.gridnode.pdip.base.certificate.model.Certificate",
"com.gridnode.gtas.server.partnerprocess.model.BizCertMapping","bizCertMapping.ownCert",
"0","0","0","1","1","","999",
"displayable=true\r\nmandatory=true\r\neditable=true",
"type=foreign\r\nforeign.key=certificate.uid\r\nforeign.display=certificate.name\r\nforeign.cached=true\r\nforeign.additionalDisplay=id"
);
INSERT INTO fieldmetainfo VALUES(
NULL,"_partnerID","PARTNER_ID","PartnerID","java.lang.String",
"com.gridnode.gtas.server.partnerprocess.model.BizCertMapping","bizCertMapping.partnerId",
"20","0","0","1","1","","999",
"displayable=true\r\nmandatory=true\r\neditable=false\r\neditable.create=true",
"type=foreign\r\nforeign.key=partner.partnerId\r\nforeign.display=partner.name\r\nforeign.cached=false"
);

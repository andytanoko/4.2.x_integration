SET client_encoding = 'UTF8';
SET check_function_bodies = false;
SET client_min_messages = warning;
SET search_path = appdb;

DELETE FROM "entitymetainfo" WHERE "EntityName" IN ('ProcessDef','ProcessAct');
INSERT INTO "entitymetainfo" VALUES ('com.gridnode.pdip.app.rnif.model.ProcessDef', 'ProcessDef','"process_def"');
INSERT INTO "entitymetainfo" VALUES ('com.gridnode.pdip.app.rnif.model.ProcessAct', 'ProcessAct','"process_act"');

--------- ProcessDef
DELETE FROM "fieldmetainfo" WHERE "EntityObjectName" LIKE '%ProcessDef';
INSERT INTO "fieldmetainfo" VALUES (DEFAULT,'_uId','UID','"UID"','java.lang.Long','com.gridnode.pdip.app.rnif.model.ProcessDef' ,'processDef.uid', 0, 0, 0 , 1, 1, NULL, 999, 'displayable=false'||chr(13)||chr(10)||'editable=false'||chr(13)||chr(10)||'mandatory=false'||chr(13)||chr(10),'type=uid'||chr(13)||chr(10));
INSERT INTO "fieldmetainfo" VALUES (DEFAULT,'_requestAct','REQUEST_ACT','','com.gridnode.pdip.app.rnif.model.ProcessAct','com.gridnode.pdip.app.rnif.model.ProcessDef','processDef.requestAct',0,0,0,1,1,'',999,'displayable=true'||chr(13)||chr(10)||'editable=true'||chr(13)||chr(10)||'mandatory=true'||chr(13)||chr(10),'type=embedded'||chr(13)||chr(10)||'embedded.type=processAct');
INSERT INTO "fieldmetainfo" VALUES (DEFAULT,'_responseAct','RESPONSE_ACT','','com.gridnode.pdip.app.rnif.model.ProcessAct','com.gridnode.pdip.app.rnif.model.ProcessDef','processDef.responseAct',0,0,0,1,1,'',999,'displayable=true'||chr(13)||chr(10)||'editable=true'||chr(13)||chr(10)||'mandatory=true'||chr(13)||chr(10),'type=dynamic'||chr(13)||chr(10)||'dynamic.types=processAct');
INSERT INTO "fieldmetainfo" VALUES (DEFAULT,'_defName','DEF_NAME','"DefName"','java.lang.String','com.gridnode.pdip.app.rnif.model.ProcessDef', 'processDef.defName', 50,0,0,1,1,'', 999, 'displayable=true'||chr(13)||chr(10)||'editable=false'||chr(13)||chr(10)||'mandatory=true'||chr(13)||chr(10)||'editable.create=true'||chr(13)||chr(10),'type=text'||chr(13)||chr(10)||'text.length.min=3'||chr(13)||chr(10)||'text.length.max=50');
INSERT INTO "fieldmetainfo" VALUES (DEFAULT,'_actionTimeOut','ACTION_TIME_OUT','"ActionTimeOut"','java.lang.Integer','com.gridnode.pdip.app.rnif.model.ProcessDef', 'processDef.actionTimeOut', 0,0,0,1,1,'', 999, 'displayable=true'||chr(13)||chr(10)||'editable=true'||chr(13)||chr(10)||'mandatory=false'||chr(13)||chr(10),'type=text'||chr(13)||chr(10)||'text.length.max=10'||chr(13)||chr(10));
INSERT INTO "fieldmetainfo" VALUES (DEFAULT,'_processType','PROCESS_TYPE','"ProcessType"','java.lang.String','com.gridnode.pdip.app.rnif.model.ProcessDef', 'processDef.processType', 50,0,0,1,1,'', 999, 'displayable=true'||chr(13)||chr(10)||'editable=false'||chr(13)||chr(10)||'editable.create=true'||chr(13)||chr(10)||'mandatory=true'||chr(13)||chr(10),'type=enum'||chr(13)||chr(10)||'processDef.processType.single=SingleActionProcess'||chr(13)||chr(10)||'processDef.processType.double=TwoActionProcess');
INSERT INTO "fieldmetainfo" VALUES (DEFAULT,'_RNIFVersion','RNIF_VERSION','"RNIFVersion"','java.lang.String','com.gridnode.pdip.app.rnif.model.ProcessDef', 'processDef.rnifVersion', 50,0,0,1,1,'', 999, 'displayable=true'||chr(13)||chr(10)||'editable=true'||chr(13)||chr(10)||'mandatory=true'||chr(13)||chr(10),'type=enum'||chr(13)||chr(10)||'processDef.rnifVersion.rnif_1_1=RNIF1.1'||chr(13)||chr(10)||'processDef.rnifVersion.rnif_2_0=RNIF2.0');
INSERT INTO "fieldmetainfo" VALUES (DEFAULT,'_fromPartnerRoleClassCode','FROM_PARTNER_ROLE_CLASS_CODE','"FromPartnerRoleClassCode"','java.lang.String','com.gridnode.pdip.app.rnif.model.ProcessDef', 'processDef.fromPartnerRoleClassCode', 50,0,0,1,1,'', 999, 'displayable=true'||chr(13)||chr(10)||'editable=true'||chr(13)||chr(10)||'mandatory=true'||chr(13)||chr(10),'type=text'||chr(13)||chr(10)||'text.length.max=50'||chr(13)||chr(10));
INSERT INTO "fieldmetainfo" VALUES (DEFAULT,'_fromBizServiceCode','FROM_BIZ_SERVICE_CODE','"FromBizServiceCode"','java.lang.String','com.gridnode.pdip.app.rnif.model.ProcessDef', 'processDef.fromBizServiceCode', 50,0,0,1,1,'', 999, 'displayable=true'||chr(13)||chr(10)||'editable=true'||chr(13)||chr(10)||'mandatory=true'||chr(13)||chr(10),'type=text'||chr(13)||chr(10)||'text.length.max=50'||chr(13)||chr(10));
INSERT INTO "fieldmetainfo" VALUES (DEFAULT,'_fromPartnerClassCode','FROM_PARTNER_CLASS_CODE','"FromPartnerClassCode"','java.lang.String','com.gridnode.pdip.app.rnif.model.ProcessDef', 'processDef.fromPartnerClassCode', 50,0,0,1,1,'', 999, 'displayable=true'||chr(13)||chr(10)||'editable=true'||chr(13)||chr(10)||'mandatory=true'||chr(13)||chr(10),'type=text'||chr(13)||chr(10)||'text.length.max=50'||chr(13)||chr(10));
INSERT INTO "fieldmetainfo" VALUES (DEFAULT,'_gToPartnerRoleClassCode','G_TO_PARTNER_ROLE_CLASS_CODE','"GToPartnerRoleClassCode"','java.lang.String','com.gridnode.pdip.app.rnif.model.ProcessDef', 'processDef.toPartnerRoleClassCode', 50,0,0,1,1,'', 999, 'displayable=true'||chr(13)||chr(10)||'editable=true'||chr(13)||chr(10)||'mandatory=true'||chr(13)||chr(10),'type=text'||chr(13)||chr(10)||'text.length.max=50'||chr(13)||chr(10));
INSERT INTO "fieldmetainfo" VALUES (DEFAULT,'_gToBizServiceCode','G_TO_BIZ_SERVICE_CODE','"GToBizServiceCode"','java.lang.String','com.gridnode.pdip.app.rnif.model.ProcessDef', 'processDef.toBizServiceCode', 50,0,0,1,1,'', 999, 'displayable=true'||chr(13)||chr(10)||'editable=true'||chr(13)||chr(10)||'mandatory=true'||chr(13)||chr(10),'type=text'||chr(13)||chr(10)||'text.length.max=50'||chr(13)||chr(10));
INSERT INTO "fieldmetainfo" VALUES (DEFAULT,'_gToPartnerClassCode','G_TO_PARTNER_CLASS_CODE','"GToPartnerClassCode"','java.lang.String','com.gridnode.pdip.app.rnif.model.ProcessDef', 'processDef.toPartnerClassCode', 50,0,0,1,1,'', 999, 'displayable=true'||chr(13)||chr(10)||'editable=true'||chr(13)||chr(10)||'mandatory=true'||chr(13)||chr(10),'type=text'||chr(13)||chr(10)||'text.length.max=50'||chr(13)||chr(10));
INSERT INTO "fieldmetainfo" VALUES (DEFAULT,'_gProcessIndicatorCode','G_PROCESS_INDICATOR_CODE','"GProcessIndicatorCode"','java.lang.String','com.gridnode.pdip.app.rnif.model.ProcessDef', 'processDef.processIndicatorCode', 50,0,0,1,1,'', 999, 'displayable=true'||chr(13)||chr(10)||'editable=true'||chr(13)||chr(10)||'mandatory=true'||chr(13)||chr(10),'type=text'||chr(13)||chr(10)||'text.length.max=50'||chr(13)||chr(10));
INSERT INTO "fieldmetainfo" VALUES (DEFAULT,'_versionIdentifier','VERSION_IDENTIFIER','"VersionIdentifier"','java.lang.String','com.gridnode.pdip.app.rnif.model.ProcessDef', 'processDef.versionIdentifier', 50,0,0,1,1,'', 999, 'displayable=true'||chr(13)||chr(10)||'editable=true'||chr(13)||chr(10)||'mandatory=true'||chr(13)||chr(10),'type=text'||chr(13)||chr(10)||'text.length.max=50'||chr(13)||chr(10));
INSERT INTO "fieldmetainfo" VALUES (DEFAULT,'_gUsageCode','G_USAGE_CODE','"GUsageCode"','java.lang.String','com.gridnode.pdip.app.rnif.model.ProcessDef', 'processDef.usageCode', 50,0,0,1,1,'', 999, 'displayable=true'||chr(13)||chr(10)||'editable=true'||chr(13)||chr(10)||'mandatory=true'||chr(13)||chr(10),'type=text'||chr(13)||chr(10)||'text.length.max=50'||chr(13)||chr(10));
INSERT INTO "fieldmetainfo" VALUES (DEFAULT,'_requestDocThisDocIdentifier','REQUEST_DOC_THIS_DOC_IDENTIFIER','"RequestDocThisDocIdentifier"','java.lang.String','com.gridnode.pdip.app.rnif.model.ProcessDef', 'processDef.requestDocThisDocIdentifier', 150,0,0,1,1,'', 999, 'displayable=true'||chr(13)||chr(10)||'editable=true'||chr(13)||chr(10)||'mandatory=false'||chr(13)||chr(10),'type=text'||chr(13)||chr(10)||'text.length.max=150'||chr(13)||chr(10));
INSERT INTO "fieldmetainfo" VALUES (DEFAULT,'_responseDocThisDocIdentifier','RESPONSE_DOC_THIS_DOC_IDENTIFIER','"ResponseDocThisDocIdentifier"','java.lang.String','com.gridnode.pdip.app.rnif.model.ProcessDef', 'processDef.responseDocThisDocIdentifier', 150,0,0,1,1,'', 999, 'displayable=true'||chr(13)||chr(10)||'editable=true'||chr(13)||chr(10)||'mandatory=false'||chr(13)||chr(10),'type=text'||chr(13)||chr(10)||'text.length.max=150'||chr(13)||chr(10));
INSERT INTO "fieldmetainfo" VALUES (DEFAULT,'_responseDocRequestDocIdentifier','RESPONSE_DOC_REQUEST_DOC_IDENTIFIER','"RespDocReqDocIdentifier"','java.lang.String','com.gridnode.pdip.app.rnif.model.ProcessDef', 'processDef.responseDocRequestDocIdentifier', 150,0,0,1,1,'', 999, 'displayable=true'||chr(13)||chr(10)||'editable=true'||chr(13)||chr(10)||'mandatory=false'||chr(13)||chr(10),'type=text'||chr(13)||chr(10)||'text.length.max=150'||chr(13)||chr(10));
INSERT INTO "fieldmetainfo" VALUES (DEFAULT,'_userTrackingIdentifier','USER_TRACKING_IDENTIFIER','"UserTrackingIdentifier"','java.lang.String','com.gridnode.pdip.app.rnif.model.ProcessDef', 'processDef.userTrackingIdentifier', 150,0,0,1,1,'', 999, 'displayable=true'||chr(13)||chr(10)||'editable=true'||chr(13)||chr(10)||'mandatory=false'||chr(13)||chr(10),'type=text'||chr(13)||chr(10)||'text.length.max=150'||chr(13)||chr(10));
INSERT INTO "fieldmetainfo" VALUES (DEFAULT,'_isSynchronous','IS_SYNCHRONOUS','"IsSynchronous"','java.lang.Boolean','com.gridnode.pdip.app.rnif.model.ProcessDef', 'processDef.isSynchronous', 0,0,0,1,1,'', 999, 'displayable=true'||chr(13)||chr(10)||'editable=true'||chr(13)||chr(10)||'mandatory=false'||chr(13)||chr(10),'type=enum'||chr(13)||chr(10)||'processDef.isSynchronous.true=true'||chr(13)||chr(10)||'processDef.isSynchronous.false=false');
INSERT INTO "fieldmetainfo" VALUES (DEFAULT,'_canDelete','CAN_DELETE','"CanDelete"','java.lang.Boolean','com.gridnode.pdip.app.rnif.model.ProcessDef', 'processDef.isSynchronous', 0,0,0,1,1,'', 999, 'displayable=false'||chr(13)||chr(10)||'editable=false'||chr(13)||chr(10)||'mandatory=false'||chr(13)||chr(10),'type=enum'||chr(13)||chr(10)||'generic.yes=true'||chr(13)||chr(10)||'generic.false=false');

--------- ProcessAct
DELETE FROM "fieldmetainfo" WHERE "EntityObjectName" LIKE '%ProcessAct';
INSERT INTO "fieldmetainfo" VALUES (DEFAULT,'_uId','UID','"UID"','java.lang.Long','com.gridnode.pdip.app.rnif.model.ProcessAct' ,'processAct.uid', 0, 0, 0 , 1, 1, '', 999,'displayable=false'||chr(13)||chr(10)||'editable=false'||chr(13)||chr(10)||'mandatory=false'||chr(13)||chr(10),'type=uid'||chr(13)||chr(10));
INSERT INTO "fieldmetainfo" VALUES (DEFAULT,'_processDefUid','PROCESS_DEF_UID','"ProcessDefUid"','java.lang.Integer','com.gridnode.pdip.app.rnif.model.ProcessAct', 'processAct.processDefUid', 0,0,0,1,1,'', 999,'displayable=false'||chr(13)||chr(10)||'editable=false'||chr(13)||chr(10)||'mandatory=false'||chr(13)||chr(10), '');
INSERT INTO "fieldmetainfo" VALUES (DEFAULT,'_processDefAct','PROCESS_DEF_ACT','"ProcessDefAct"','java.lang.Integer','com.gridnode.pdip.app.rnif.model.ProcessAct', 'processAct.processDefAct', 0,0,0,1,1,'', 999,'displayable=false'||chr(13)||chr(10)||'editable=false'||chr(13)||chr(10)||'mandatory=false'||chr(13)||chr(10), 'type=enum'||chr(13)||chr(10)||'processAct.processDefAct.request=1'||chr(13)||chr(10)||'processAct.processDefAct.response=2');
INSERT INTO "fieldmetainfo" VALUES (DEFAULT,'_msgType','MSG_TYPE','"MsgType"','java.lang.Long','com.gridnode.pdip.app.rnif.model.ProcessAct', 'processAct.msgType', 0,0,0,1,1,'', 999,'displayable=true'||chr(13)||chr(10)||'editable=true'||chr(13)||chr(10)||'mandatory=true'||chr(13)||chr(10),'type=foreign'||chr(13)||chr(10)||'foreign.key=mappingFile.uid'||chr(13)||chr(10)||'foreign.display=mappingFile.name'||chr(13)||chr(10));
INSERT INTO "fieldmetainfo" VALUES (DEFAULT,'_retries','RETRIES','"Retries"','java.lang.Integer','com.gridnode.pdip.app.rnif.model.ProcessAct', 'processAct.retries', 0,0,0,1,1,'', 999,'displayable=true'||chr(13)||chr(10)||'editable=true'||chr(13)||chr(10)||'mandatory=false'||chr(13)||chr(10),'type=range'||chr(13)||chr(10)||'range.max=65535'||chr(13)||chr(10));
INSERT INTO "fieldmetainfo" VALUES (DEFAULT,'_timeToAcknowledge','TIME_TO_ACKNOWLEDGE','"TimeToAcknowledge"','java.lang.Integer','com.gridnode.pdip.app.rnif.model.ProcessAct', 'processAct.timeToAcknowledge', 0,0,0,1,1,'', 999,'displayable=true'||chr(13)||chr(10)||'editable=true'||chr(13)||chr(10)||'mandatory=false'||chr(13)||chr(10),'type=text'||chr(13)||chr(10)||'text.length.max=10'||chr(13)||chr(10));
INSERT INTO "fieldmetainfo" VALUES (DEFAULT,'_isAuthorizationRequired','IS_AUTHORIZATION_REQUIRED','"IsAuthorizationRequired"','java.lang.Boolean','com.gridnode.pdip.app.rnif.model.ProcessAct', 'processAct.isAuthorizationRequired', 0,0,0,1,1,'', 999,'displayable=true'||chr(13)||chr(10)||'editable=true'||chr(13)||chr(10)||'mandatory=false'||chr(13)||chr(10),'type=enum'||chr(13)||chr(10)||'processAct.isAuthorizationRequired.true=true'||chr(13)||chr(10)||'processAct.isAuthorizationRequired.false=false');
INSERT INTO "fieldmetainfo" VALUES (DEFAULT,'_isNonRepudiationRequired','IS_NON_REPUDIATION_REQUIRED','"IsNonRepudiationRequired"','java.lang.Boolean','com.gridnode.pdip.app.rnif.model.ProcessAct', 'processAct.isNonRepudiationRequired', 0,0,0,1,1,'', 999,'displayable=true'||chr(13)||chr(10)||'editable=true'||chr(13)||chr(10)||'mandatory=false'||chr(13)||chr(10),'type=enum'||chr(13)||chr(10)||'processAct.isNonRepudiationRequired.true=true'||chr(13)||chr(10)||'processAct.isNonRepudiationRequired.false=false');
INSERT INTO "fieldmetainfo" VALUES (DEFAULT,'_isSecureTransportRequired','IS_SECURE_TRANSPORT_REQUIRED','"IsSecureTransportRequired"','java.lang.Boolean','com.gridnode.pdip.app.rnif.model.ProcessAct', 'processAct.isSecureTransportRequired', 0,0,0,1,1,'', 999,'displayable=true'||chr(13)||chr(10)||'editable=true'||chr(13)||chr(10)||'mandatory=false'||chr(13)||chr(10),'type=enum'||chr(13)||chr(10)||'processAct.isSecureTransportRequired.true=true'||chr(13)||chr(10)||'processAct.isSecureTransportRequired.false=false');
INSERT INTO "fieldmetainfo" VALUES (DEFAULT,'_bizActivityIdentifier','BIZ_ACTIVITY_IDENTIFIER','"BizActivityIdentifier"','java.lang.String','com.gridnode.pdip.app.rnif.model.ProcessAct', 'processAct.bizActivityIdentifier', 50,0,0,1,1,'', 999,'displayable=true'||chr(13)||chr(10)||'editable=true'||chr(13)||chr(10)||'mandatory=true'||chr(13)||chr(10),'type=text'||chr(13)||chr(10)||'text.length.max=50'||chr(13)||chr(10));
INSERT INTO "fieldmetainfo" VALUES (DEFAULT,'_gBizActionCode','G_BIZ_ACTION_CODE','"GBizActionCode"','java.lang.String','com.gridnode.pdip.app.rnif.model.ProcessAct', 'processAct.bizActionCode', 50,0,0,1,1,'', 999,'displayable=true'||chr(13)||chr(10)||'editable=true'||chr(13)||chr(10)||'mandatory=true'||chr(13)||chr(10),'type=text'||chr(13)||chr(10)||'text.length.max=50'||chr(13)||chr(10));
INSERT INTO "fieldmetainfo" VALUES (DEFAULT,'_dictFile','DICT_FILE','"DictFile"','java.lang.Long','com.gridnode.pdip.app.rnif.model.ProcessAct', 'processAct.dictFile', 0,0,0,1,1,'', 999,'displayable=true'||chr(13)||chr(10)||'editable=true'||chr(13)||chr(10)||'mandatory=false'||chr(13)||chr(10),'type=foreign'||chr(13)||chr(10)||'foreign.key=mappingFile.uid'||chr(13)||chr(10)||'foreign.display=mappingFile.name'||chr(13)||chr(10));
INSERT INTO "fieldmetainfo" VALUES (DEFAULT,'_XMLSchema','XML_SCHEMA','"XMLSchema"','java.lang.Long','com.gridnode.pdip.app.rnif.model.ProcessAct', 'processAct.xmlSchema', 0,0,0,1,1,'', 999,'displayable=true'||chr(13)||chr(10)||'editable=true','type=foreign'||chr(13)||chr(10)||'foreign.key=mappingFile.uid'||chr(13)||chr(10)||'foreign.display=mappingFile.name'||chr(13)||chr(10));
INSERT INTO "fieldmetainfo" VALUES (DEFAULT,'_disableDTD','DISABLE_DTD','"DisableDTD"','java.lang.Boolean','com.gridnode.pdip.app.rnif.model.ProcessAct', 'processAct.disableDtd', 0,0,0,1,1,'', 999,'displayable=true'||chr(13)||chr(10)||'editable=true'||chr(13)||chr(10)||'mandatory=false'||chr(13)||chr(10),'type=enum'||chr(13)||chr(10)||'processAct.disableDtd.true=true'||chr(13)||chr(10)||'processAct.disableDtd.false=false');
INSERT INTO "fieldmetainfo" VALUES (DEFAULT,'_disableSchema','DISABLE_SCHEMA','"DisableSchema"','java.lang.Boolean','com.gridnode.pdip.app.rnif.model.ProcessAct', 'processAct.disableSchema', 0,0,0,1,1,'', 999,'displayable=true'||chr(13)||chr(10)||'editable=true'||chr(13)||chr(10)||'mandatory=false'||chr(13)||chr(10),'type=enum'||chr(13)||chr(10)||'processAct.disableSchema.true=true'||chr(13)||chr(10)||'processAct.disableSchema.false=false');
INSERT INTO "fieldmetainfo" VALUES (DEFAULT,'_disableEncryption','DISABLE_ENCRYPTION','"DisableEncryption"','java.lang.Boolean','com.gridnode.pdip.app.rnif.model.ProcessAct', 'processAct.disableEncryption', 0,0,0,1,1,'', 999,'displayable=true'||chr(13)||chr(10)||'editable=true'||chr(13)||chr(10)||'mandatory=false'||chr(13)||chr(10),'type=enum'||chr(13)||chr(10)||'processAct.disableEncryption.true=true'||chr(13)||chr(10)||'processAct.disableEncryption.false=false');
INSERT INTO "fieldmetainfo" VALUES (DEFAULT,'_disableSignature','DISABLE_SIGNATURE','"DisableSignature"','java.lang.Boolean','com.gridnode.pdip.app.rnif.model.ProcessAct', 'processAct.disableSignature', 0,0,0,1,1,'', 999,'displayable=true'||chr(13)||chr(10)||'editable=true'||chr(13)||chr(10)||'mandatory=false'||chr(13)||chr(10),'type=enum'||chr(13)||chr(10)||'processAct.disableSignature.true=true'||chr(13)||chr(10)||'processAct.disableSignature.false=false');
INSERT INTO "fieldmetainfo" VALUES (DEFAULT,'_validateAtSender','VALIDATE_AT_SENDER','"ValidateAtSender"','java.lang.Boolean','com.gridnode.pdip.app.rnif.model.ProcessAct', 'processAct.validateAtSender', 0,0,0,1,1,'', 999,'displayable=true'||chr(13)||chr(10)||'editable=true'||chr(13)||chr(10)||'mandatory=false'||chr(13)||chr(10),'type=enum'||chr(13)||chr(10)||'processAct.validateAtSender.true=true'||chr(13)||chr(10)||'processAct.validateAtSender.false=false');
INSERT INTO "fieldmetainfo" VALUES (DEFAULT,'_onlyEncryptPayload','ONLY_ENCRYPT_PAYLOAD','"OnlyEncryptPayload"','java.lang.Boolean','com.gridnode.pdip.app.rnif.model.ProcessAct', 'processAct.onlyEncryptPayload', 0,0,0,1,1,'', 999,'displayable=true'||chr(13)||chr(10)||'editable=true'||chr(13)||chr(10)||'mandatory=false'||chr(13)||chr(10),'type=enum'||chr(13)||chr(10)||'processAct.onlyEncryptPayload.true=true'||chr(13)||chr(10)||'processAct.onlyEncryptPayload.false=false');
INSERT INTO "fieldmetainfo" VALUES (DEFAULT,'_digestAlgorithm','DIGEST_ALGORITHM','"DigestAlgorithm"','java.lang.String','com.gridnode.pdip.app.rnif.model.ProcessAct', 'processAct.digestAlgorithm', 80,0,0,1,1,'', 999,'displayable=true'||chr(13)||chr(10)||'editable=true'||chr(13)||chr(10)||'mandatory=false'||chr(13)||chr(10),'type=enum'||chr(13)||chr(10)||'processAct.digestAlgorithm.sha=1.3.14.3.2.26'||chr(13)||chr(10)||'processAct.digestAlgorithm.md5=1.2.840.113549.2.5');
INSERT INTO "fieldmetainfo" VALUES (DEFAULT,'_encryptionAlgorithm','ENCRYPTION_ALGORITHM','"EncryptionAlgorithm"','java.lang.String','com.gridnode.pdip.app.rnif.model.ProcessAct', 'processAct.encryptionAlgorithm', 80,0,0,1,1,'', 999,'displayable=true'||chr(13)||chr(10)||'editable=true'||chr(13)||chr(10)||'mandatory=false'||chr(13)||chr(10),'type=enum'||chr(13)||chr(10)||'processAct.encryptionAlgorithm.TDES_ALG=1.2.840.113549.3.7'||chr(13)||chr(10)||'processAct.encryptionAlgorithm.RC2_ALG=1.2.840.113549.3.2');
INSERT INTO "fieldmetainfo" VALUES (DEFAULT,'_encryptionAlgorithmLength','ENCRYPTION_ALGORITHM_LENGTH','"EncryptionAlgorithmLength"','java.lang.Integer','com.gridnode.pdip.app.rnif.model.ProcessAct', 'processAct.encryptionAlgorithmLength', 0,0,0,1,1,'', 999,'displayable=true'||chr(13)||chr(10)||'editable=true'||chr(13)||chr(10)||'mandatory=false'||chr(13)||chr(10),'type=range'||chr(13)||chr(10)||'range.max=65535'||chr(13)||chr(10));
INSERT INTO "fieldmetainfo" VALUES (DEFAULT,'_isCompressRequired','IS_COMPRESS_REQUIRED','"IsCompressRequired"','java.lang.Boolean','com.gridnode.pdip.app.rnif.model.ProcessAct', 'processAct.isCompressRequired', 0,'0','0','1','1','', 999, 'displayable=true'||chr(13)||chr(10)||'editable=true'||chr(13)||chr(10)||'mandatory=false'||chr(13)||chr(10),'type=enum'||chr(13)||chr(10)||'processAct.isCompressRequired.true=true'||chr(13)||chr(10)||'processAct.isCompressRequired.false=false'||chr(13)||chr(10));



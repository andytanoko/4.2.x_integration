--- TWX 19012008  convert from mysql to Postgres

SET search_path=userdb;

----------- BEGIN: 15 Feb 06 [SC] : remove additional data for GT 4.0 (part 2)
-- delete all domain identifiers of marked as deleted partner BE.
LOCK TABLE "domain_identifier";
LOCK TABLE "business_entity" IN ACCESS SHARE MODE;
DELETE FROM "domain_identifier" WHERE "UID" IN (
  SELECT "domain_identifier"."UID" FROM "domain_identifier", "business_entity"
  WHERE "domain_identifier"."BizEntityUID" = "business_entity"."UID"
  AND "business_entity"."IsPartner" = '1'
  AND "business_entity"."State" = '1'
);

-- delete marked as deleted partner BE
LOCK TABLE "business_entity";
DELETE FROM "business_entity" WHERE "State" = '1' AND "IsPartner" = '1';

-- delete marked as deleted partner
LOCK TABLE "partner";
DELETE FROM "partner" WHERE "partner"."State" = '2';
----------- END: 15 Feb 06 [SC] : remove additional data for GT 4.0 (part 2)


----------- BEGIN: 24 Jan 06 [SC] : remove additional data for GT 4.0
-- delete all domain identifiers linked to own business entities
LOCK TABLE "domain_identifier";
LOCK TABLE "business_entity" IN ACCESS SHARE MODE;
DELETE FROM "domain_identifier" WHERE "UID" IN (
   SELECT "domain_identifier"."UID" FROM "domain_identifier", "business_entity"
   WHERE "domain_identifier"."BizEntityUID" = "business_entity"."UID"
   AND "business_entity"."IsPartner"='0'
);


-- The following script trash some database tables.

-- delete all grid_document 
LOCK TABLE "grid_document";
DELETE FROM "grid_document";

-- trash all table starts with 'rt'
LOCK TABLE "rtactivity";
DELETE FROM "rtactivity";

LOCK TABLE "rtprocess";
DELETE FROM "rtprocess";

LOCK TABLE "rtprocessdoc";
DELETE FROM "rtprocessdoc";

LOCK TABLE "rtrestriction";
DELETE FROM "rtrestriction";

-- Trash all table starts with 'data_'
LOCK TABLE "data_bytedata";
DELETE FROM "data_bytedata";

LOCK TABLE "data_contextdata";
DELETE FROM "data_contextdata";

LOCK TABLE "data_stringdata";
DELETE FROM "data_stringdata";

-- delete all session_audit 
LOCK TABLE "session_audit";
DELETE FROM "session_audit";

-- delete all jms_router 
LOCK TABLE "jms_router";
DELETE FROM "jms_router";

-- delete all shared_resource 
LOCK TABLE "shared_resource";
DELETE FROM "shared_resource";

-- delete all trans_activation_state 
LOCK TABLE "trans_activation_state";
DELETE FROM "trans_activation_state";

-- delete all activation_record 
LOCK TABLE "activation_record";
DELETE FROM "activation_record";

-- delete all registry_object_map 
LOCK TABLE "registry_object_map";
DELETE FROM "registry_object_map";

-- delete all attachment 
LOCK TABLE "attachment";
DELETE FROM "attachment";

-- delete all attachmentregistry 
LOCK TABLE "attachmentRegistry";
DELETE FROM "attachmentRegistry";

-- delete all active_track_record 
LOCK TABLE "active_track_record";
DELETE FROM "active_track_record";

--------- Revert to 2 users: admin + guest

-- Retstore User Accounts
DELETE FROM "user_account";
INSERT INTO "user_account"
("UID","Name","ID","Password","Phone","Email","Property","Version") VALUES
('-1','Administrator','admin','1N3BRqvwF7dN','12345678','admin@gridnode.com','This account belongs to admin user','1'),
('-2','Guest User','guest','1IMDd9CLvmxP','12345678','','This account can be used by any guest of GridTalk','1')
;

-- Restore User Account States

DELETE FROM "user_account_state";
INSERT INTO "user_account_state"
("UID","UserID","NumLoginTries","IsFreeze","FreezeTime","LastLoginTime","LastLogoutTime","State","CanDelete","CreateTime","CreateBy") VALUES
('-1','admin','0','0',NULL,NULL,NULL,'1','0',NULL,'SYSTEM'),
('-2','guest','0','0',NULL,NULL,NULL,'1','0',NULL,'SYSTEM');

-- Restore User Roles
DELETE FROM "subject_role";
INSERT INTO "subject_role"
("UID","Subject","Role","SubjectType","Version") VALUES
('-1','-1','-1','UserAccount','0'),
('-2','-2','-2','UserAccount','0')
;

----------- END  : 24 Jan 06 [SC] : remove additional data for GT 4.0

-- delete license 
LOCK TABLE "license";
DELETE FROM "license";

LOCK TABLE "gridtalk_license";
DELETE FROM "gridtalk_license";

-- delete all gridnodes 
LOCK TABLE "gridnode";
DELETE FROM "gridnode";

-- delete company profile
LOCK TABLE "coy_profile";
DELETE FROM "coy_profile";

-- delete connection status
LOCK TABLE "connection_status";
DELETE FROM "connection_status";

-- delete all whitepages linked to own business entities
LOCK TABLE "whitepage";
LOCK TABLE "business_entity" IN ACCESS SHARE MODE;
DELETE FROM "whitepage" WHERE "UID" IN (
  SELECT "whitepage"."UID" FROM "whitepage", "business_entity"
  WHERE "whitepage"."BizEntityUID" = "business_entity"."UID"
  AND "business_entity"."IsPartner"='0'
);

-- delete all resource links to own business entities
LOCK TABLE "resource_link";
LOCK "business_entity" IN ACCESS SHARE MODE;
DELETE FROM "resource_link" WHERE "UID" IN (
   SELECT "resource_link"."UID" FROM "resource_link", "business_entity"
   WHERE "resource_link"."FromResourceType" = 'BusinessEntity' 
   AND "resource_link"."FromResourceUID" = "business_entity"."UID"
   AND "business_entity"."IsPartner"='0'
);

DELETE FROM "resource_link" WHERE "FromResourceType"='User' AND "ToResourceType"='BusinessEntity';

-- delete all own business entities
LOCK TABLE "business_entity";
DELETE FROM "business_entity" WHERE "IsPartner" = '0';

-- delete all own certifcates
LOCK TABLE "certificate";
DELETE FROM "certificate" WHERE "isPartner"='0';

-- delete all business certificates mapping
LOCK TABLE "biz_cert_mapping";
DELETE FROM "biz_cert_mapping";

-- delete all non-partner channels and profiles (including own, GM & Connection Setup)
LOCK TABLE "channel_info";
DELETE FROM "channel_info" WHERE "isPartner"='0';

LOCK TABLE "comm_info";
DELETE FROM "comm_info" WHERE "isPartner"='0';

LOCK TABLE "packaging_profile";
DELETE FROM "packaging_profile" WHERE "isPartner"='0';

LOCK TABLE "security_profile";
DELETE FROM "security_profile" WHERE "isPartner"='0';
UPDATE "security_profile" SET "SignatureEncryptionCertificate"=NULL;
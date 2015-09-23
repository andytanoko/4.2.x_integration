USE userdb; 

----------- BEGIN: 15 Feb 06 [SC] : remove additional data for GT 4.0 (part 2)
-- delete all domain identifiers of marked as deleted partner BE.
LOCK TABLES domain_identifier WRITE, business_entity READ;
DELETE domain_identifier
FROM domain_identifier, business_entity
WHERE
domain_identifier.BizEntityUID = business_entity.UID
AND business_entity.IsPartner = '1'
AND business_entity.State = '1';
UNLOCK TABLES;

-- delete marked as deleted partner BE
LOCK TABLES business_entity WRITE;
DELETE FROM business_entity
WHERE 
state = '1' AND isPartner = '1';
UNLOCK TABLES;

-- delete marked as deleted partner
LOCK TABLES partner WRITE;
DELETE partner
FROM partner
WHERE
partner.state = '2';
UNLOCK TABLES;
----------- END: 15 Feb 06 [SC] : remove additional data for GT 4.0 (part 2)


----------- BEGIN: 24 Jan 06 [SC] : remove additional data for GT 4.0
-- delete all domain identifiers linked to own business entities
LOCK TABLES domain_identifier WRITE, business_entity READ;
DELETE domain_identifier FROM domain_identifier, business_entity
WHERE domain_identifier.BizEntityUID = business_entity.UID
AND business_entity.isPartner='0';
UNLOCK TABLES;

-- The following script trash some database tables.

-- delete all grid_document 
LOCK TABLES grid_document WRITE;
DELETE FROM grid_document;
UNLOCK TABLES;

-- trash all table starts with 'rt'
LOCK TABLES rtactivity WRITE;
DELETE FROM rtactivity;
UNLOCK TABLES;

LOCK TABLES rtprocess WRITE;
DELETE FROM rtprocess;
UNLOCK TABLES;

LOCK TABLES rtprocessdoc WRITE;
DELETE FROM rtprocessdoc;
UNLOCK TABLES;

LOCK TABLES rtrestriction WRITE;
DELETE FROM rtrestriction;
UNLOCK TABLES;

-- Trash all table starts with 'data_'
LOCK TABLES data_bytedata WRITE;
DELETE FROM data_bytedata;
UNLOCK TABLES;

LOCK TABLES data_contextdata WRITE;
DELETE FROM data_contextdata;
UNLOCK TABLES;

LOCK TABLES data_stringdata WRITE;
DELETE FROM data_stringdata;
UNLOCK TABLES;

-- delete all session_audit 
LOCK TABLES session_audit WRITE;
DELETE FROM session_audit;
UNLOCK TABLES;

-- delete all jms_router 
LOCK TABLES jms_router WRITE;
DELETE FROM jms_router;
UNLOCK TABLES;

-- delete all shared_resource 
LOCK TABLES shared_resource WRITE;
DELETE FROM shared_resource;
UNLOCK TABLES;

-- delete all trans_activation_state 
LOCK TABLES trans_activation_state WRITE;
DELETE FROM trans_activation_state;
UNLOCK TABLES;

-- delete all activation_record 
LOCK TABLES activation_record WRITE;
DELETE FROM activation_record;
UNLOCK TABLES;

-- delete all registry_object_map 
LOCK TABLES registry_object_map WRITE;
DELETE FROM registry_object_map;
UNLOCK TABLES;

-- delete all attachment 
LOCK TABLES attachment WRITE;
DELETE FROM attachment;
UNLOCK TABLES;

-- delete all attachmentregistry 
LOCK TABLES attachmentregistry WRITE;
DELETE FROM attachmentregistry;
UNLOCK TABLES;

-- delete all active_track_record 
LOCK TABLES active_track_record WRITE;
DELETE FROM active_track_record;
UNLOCK TABLES;

--------- Revert to 2 users: admin + guest

-- Retstore User Accounts
DELETE FROM user_account;
INSERT INTO user_account
(UID,Name,ID,Password,Phone,Email,Property,Version) VALUES
("-1","Administrator","admin","1N3BRqvwF7dN","12345678","admin@gridnode.com","This account belongs to admin user","1"),
("-2","Guest User","guest","1IMDd9CLvmxP","12345678","","This account can be used by any guest of GridTalk","1")
;

-- Restore User Account States

DELETE FROM user_account_state;
INSERT INTO user_account_state
(UID,UserID,NumLoginTries,IsFreeze,FreezeTime,LastLoginTime,LastLogoutTime,State,CanDelete,CreateTime,CreateBy) VALUES
("-1","admin","0","0",NULL,NULL,NULL,"1","0",NULL,"SYSTEM"),
("-2","guest","0","0",NULL,NULL,NULL,"1","0",NULL,"SYSTEM")
;

-- Restore User Roles
DELETE FROM subject_role;
INSERT INTO subject_role
(UID,Subject,Role,SubjectType,Version) VALUES
("-1","-1","-1","UserAccount","0"),
("-2","-2","-2","UserAccount","0")
;

----------- END  : 24 Jan 06 [SC] : remove additional data for GT 4.0

-- delete license 
LOCK TABLES license WRITE;
DELETE FROM license;
UNLOCK TABLES;

LOCK TABLES gridtalk_license WRITE;
DELETE FROM gridtalk_license;
UNLOCK TABLES;

-- delete all gridnodes 
LOCK TABLES gridnode WRITE;
DELETE FROM gridnode;
UNLOCK TABLES;

-- delete company profile
LOCK TABLES coy_profile WRITE;
DELETE FROM coy_profile;
UNLOCK TABLES;

-- delete connection status
LOCK TABLES connection_status WRITE;
DELETE FROM connection_status;
UNLOCK TABLES;

-- delete all whitepages linked to own business entities
LOCK TABLES whitepage WRITE, business_entity READ;
DELETE whitepage FROM whitepage, business_entity
WHERE whitepage.BizEntityUID = business_entity.UID
AND business_entity.isPartner='0';
UNLOCK TABLES;

-- delete all resource links to own business entities
LOCK TABLES resource_link WRITE,business_entity READ;
DELETE resource_link FROM resource_link, business_entity
WHERE resource_link.FromResourceType = 'BusinessEntity' 
AND resource_link.FromResourceUID = business_entity.UID
AND business_entity.isPartner='0';

DELETE FROM resource_link WHERE FromResourceType='User' AND ToResourceType='BusinessEntity';
UNLOCK TABLES;

-- delete all own business entities
LOCK TABLES business_entity WRITE;
DELETE FROM business_entity WHERE IsPartner = '0';
UNLOCK TABLES;

-- delete all own certifcates
LOCK TABLES certificate WRITE;
DELETE FROM certificate WHERE isPartner='0';
UNLOCK TABLES;

-- delete all business certificates mapping
LOCK TABLES biz_cert_mapping WRITE;
DELETE FROM biz_cert_mapping;
UNLOCK TABLES;

-- delete all non-partner channels and profiles (including own, GM & Connection Setup)
LOCK TABLES channel_info WRITE;
DELETE FROM channel_info WHERE isPartner='0';
UNLOCK TABLES;

LOCK TABLES comm_info WRITE;
DELETE FROM comm_info WHERE isPartner='0';
UNLOCK TABLES;

LOCK TABLES packaging_profile WRITE;
DELETE FROM packaging_profile WHERE isPartner='0';
UNLOCK TABLES;

LOCK TABLES security_profile WRITE;
DELETE FROM security_profile WHERE isPartner='0';
UPDATE security_profile SET SignatureEncryptionCertificate=NULL;
UNLOCK TABLES;
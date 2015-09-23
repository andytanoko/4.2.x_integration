SET client_encoding = 'UTF8';
SET check_function_bodies = false;
SET client_min_messages = warning;
SET search_path = userdb;


---
--- Table structure for table 'business_entity'
---
DROP TABLE IF EXISTS "business_entity";
CREATE TABLE "business_entity" (
  "UID" BIGINT DEFAULT 0 NOT NULL  ,
  "ID" VARCHAR(4)  NOT NULL ,
  "EnterpriseID" VARCHAR(20) ,
  "Description" VARCHAR(80)  NOT NULL ,
  "IsPartner" DECIMAL(1)  DEFAULT 0 NOT NULL  ,
  "PartnerCat" DECIMAL(1) ,
  "IsPublishable" DECIMAL(1) DEFAULT 1 NOT NULL  ,
  "IsSyncToServer" DECIMAL(1) DEFAULT 1 NOT NULL  ,
  "State" DECIMAL(2) DEFAULT 0 NOT NULL  ,
  "CanDelete" DECIMAL(1) DEFAULT 1 NOT NULL  ,
  "Version" DECIMAL(7,5) DEFAULT 1 NOT NULL  ,
  PRIMARY KEY ("UID"),
  CONSTRAINT "BUSS_ENT_CON" UNIQUE ("EnterpriseID","ID") 
);
ALTER TABLE "business_entity" OWNER TO userdb;


---
--- Table structure for table 'whitepage'
---
DROP TABLE IF EXISTS "whitepage";
CREATE TABLE "whitepage" (
  "UID" BIGINT DEFAULT 0 NOT NULL  ,
  "BizEntityUID" BIGINT DEFAULT 0 NOT NULL  ,
  "BusinessDesc" VARCHAR(80) ,
  "DUNS" VARCHAR(20) ,
  "GlobalSCCode" VARCHAR(255) ,
  "ContactPerson" VARCHAR(80) ,
  "Email" VARCHAR(255) ,
  "Tel" VARCHAR(16) ,
  "Fax" VARCHAR(16) ,
  "Website" VARCHAR(255) ,
  "Address" VARCHAR(255) ,
  "City" VARCHAR(50) ,
  "State" VARCHAR(6) ,
  "ZipCode" VARCHAR(10) ,
  "POBox" VARCHAR(10) ,
  "Country" VARCHAR(3) ,
  "Language" VARCHAR(3) ,
  PRIMARY KEY ("UID"),
  CONSTRAINT "WHITEPAGE_CON" UNIQUE ("BizEntityUID") 
);
ALTER TABLE "whitepage" OWNER TO userdb;


---
--- Table structure for table 'registry_object_map'
---
DROP TABLE IF EXISTS "registry_object_map";
CREATE TABLE "registry_object_map" (
  "UID" BIGINT DEFAULT 0 NOT NULL  ,
  "RegistryObjectKey" VARCHAR(50) NOT NULL,
  "RegistryObjectType" VARCHAR(30)NOT NULL,
  "RegistryQueryUrl" VARCHAR(255) NOT NULL,
  "RegistryPublishUrl" VARCHAR(255) NOT NULL,
  "ProprietaryObjectKey" VARCHAR(50) NOT NULL,
  "ProprietaryObjectType" VARCHAR(30) NOT NULL,
  "IsPublishedObject" DECIMAL(1) DEFAULT 1 NOT NULL  ,
  "State" DECIMAL(2,0) DEFAULT 0 NOT NULL  ,
  PRIMARY KEY ("UID"),
  CONSTRAINT "REG_OBJ_CON" UNIQUE ("RegistryObjectKey","RegistryObjectType","RegistryQueryUrl") 
);
ALTER TABLE "registry_object_map" OWNER TO userdb;

---
--- Table structure for table 'registry_connect_info'
---
DROP TABLE IF EXISTS "registry_connect_info";
CREATE TABLE "registry_connect_info" (
  "UID" BIGINT DEFAULT 0 NOT NULL  ,
  "Name" VARCHAR(50) NOT NULL,
  "QueryUrl" VARCHAR(255) NOT NULL,
  "PublishUrl" VARCHAR(255),
  "PublishUser" VARCHAR(20),
  "PublishPassword" VARCHAR(20),
  "CanDelete" DECIMAL(1) DEFAULT 1 NOT NULL  ,
  "Version" DECIMAL(7,5) DEFAULT 1 NOT NULL  ,
  PRIMARY KEY ("UID"),
  CONSTRAINT "REG_CONNECT_CON" UNIQUE ("Name") 
);
ALTER TABLE "registry_connect_info" OWNER TO userdb;


---
--- Table structure for table 'domain_identifier'
---
DROP TABLE IF EXISTS "domain_identifier";
CREATE TABLE "domain_identifier" (
  "UID" BIGINT DEFAULT 0 NOT NULL  ,
  "BizEntityUID" BIGINT NOT NULL,
  "Domain" VARCHAR(30) NOT NULL,
  "Type" VARCHAR(50) NOT NULL,
  "Value" VARCHAR(255),
  "CanDelete" DECIMAL(1) DEFAULT 1 NOT NULL  ,
  PRIMARY KEY ("UID"),
  CONSTRAINT "DOMAIN_ID_CON" UNIQUE ("Type","Value") 
);
ALTER TABLE "domain_identifier" OWNER TO userdb;


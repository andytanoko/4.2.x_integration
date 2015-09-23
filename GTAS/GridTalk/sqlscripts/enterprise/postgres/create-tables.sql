SET client_encoding = 'UTF8';
SET check_function_bodies = false;
SET client_min_messages = warning;
SET search_path = userdb;


--- #### enterprise ####
---
--- Table structure for table 'resource_link'
---
DROP TABLE IF EXISTS "resource_link";
CREATE TABLE "resource_link" (
  "UID" BIGINT DEFAULT 0 NOT NULL ,
  "FromResourceType" VARCHAR(50)  NOT NULL ,
  "FromResourceUID" BIGINT DEFAULT 0 NOT NULL,
  "ToResourceType" VARCHAR(50)  NOT NULL ,
  "ToResourceUID" BIGINT DEFAULT 0 NOT NULL ,
  "Priority" DECIMAL(5)  DEFAULT 0 NOT NULL ,
  "CanDelete" DECIMAL(1) DEFAULT 1 NOT NULL,
  "Version" DECIMAL(7,5) DEFAULT 1 NOT NULL,
  PRIMARY KEY ("UID"),
  CONSTRAINT "RESOURCE_LINK_CON" UNIQUE ("FromResourceType","FromResourceUID","ToResourceType","ToResourceUID")
);
ALTER TABLE "resource_link" OWNER TO userdb;


---
--- Table structure for table 'shared_resource'
---
DROP TABLE IF EXISTS "shared_resource";
CREATE TABLE "shared_resource" (
  "UID" BIGINT DEFAULT 0 NOT NULL ,
  "ToEnterpriseID" VARCHAR(20)  NOT NULL ,
  "ResourceUID" BIGINT DEFAULT 0 NOT NULL ,
  "ResourceType" VARCHAR(50)  NOT NULL ,
  "State" DECIMAL(1) DEFAULT 0 NOT NULL ,
  "SyncChecksum" BIGINT ,
  "CanDelete" DECIMAL(1) DEFAULT 1 NOT NULL,
  "Version" DECIMAL(7,5) DEFAULT 1 NOT NULL,
  PRIMARY KEY ("UID"),
  CONSTRAINT "SHARED_RESOURCE_CON" UNIQUE ("ToEnterpriseID","ResourceType","ResourceUID")
);
ALTER TABLE "shared_resource" OWNER TO userdb;




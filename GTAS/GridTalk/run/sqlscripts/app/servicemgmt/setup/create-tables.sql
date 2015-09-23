SET client_encoding = 'UTF8';
SET check_function_bodies = false;
SET client_min_messages = warning;
SET search_path = userdb;


---
--- Table structure for table 'webservice'
---
DROP TABLE IF EXISTS "webservice";
CREATE TABLE "webservice" (
  "UID" BIGINT DEFAULT 0 NOT NULL  ,
  "WsdlUrl" VARCHAR(255)  NOT NULL  ,
  "EndPoint" VARCHAR(255)  NOT NULL  ,
  "ServiceName" VARCHAR(20) NOT NULL ,
  "ServiceGroup" VARCHAR(20) NOT NULL ,
  "CanDelete" DECIMAL(1) DEFAULT 1 NOT NULL  ,
  "Version" DECIMAL(7,5) DEFAULT 1 NOT NULL  ,
  PRIMARY KEY ("UID"),
  CONSTRAINT "WEBSERVICE_CON" UNIQUE ("ServiceName","ServiceGroup") 
);
ALTER TABLE "webservice" OWNER TO userdb;

---
--- Table structure for table 'service_assignment'
---
DROP TABLE IF EXISTS "service_assignment";
CREATE TABLE "service_assignment" (
  "UID" BIGINT DEFAULT 0 NOT NULL  ,
  "UserName" VARCHAR(20) NOT NULL ,
  "Password" VARCHAR(80) NOT NULL ,
  "UserType" VARCHAR(20) NOT NULL ,
  "WebServiceUIds" VARCHAR(500) ,
  "CanDelete" DECIMAL(1) DEFAULT 1 NOT NULL  ,
  "Version" DECIMAL(7,5) DEFAULT 1 NOT NULL  ,
  PRIMARY KEY ("UID"),
  CONSTRAINT "SERVICE_ASSIGNMENT_CON" UNIQUE ("UserName","UserType") 
);  
ALTER TABLE "service_assignment" OWNER TO userdb;
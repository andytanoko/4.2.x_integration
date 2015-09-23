SET client_encoding = 'UTF8';
SET check_function_bodies = false;
SET client_min_messages = warning;
SET search_path = userdb;

--- 
--- Table structure for table 'role' 
--- 
DROP TABLE IF EXISTS "role";
CREATE TABLE "role" (
  "UID" BIGINT DEFAULT 0 NOT NULL, 
  "Role" VARCHAR(30)  NOT NULL, 
  "Description" VARCHAR(255), 
  "CanDelete" DECIMAL(1) DEFAULT 1 NOT NULL, 
  "Version" DECIMAL(7,5) DEFAULT 0 NOT NULL, 
  PRIMARY KEY ("UID"), 
  CONSTRAINT "ROLE_CON" UNIQUE ("Role")
);
ALTER TABLE "role" OWNER TO userdb;
 
--- 
--- Table structure for table 'subject_role' 
--- 
DROP TABLE IF EXISTS "subject_role";
CREATE TABLE "subject_role" (
  "UID" BIGINT DEFAULT 0 NOT NULL,
  "Subject" BIGINT DEFAULT 0 NOT NULL,
  "Role" BIGINT DEFAULT 0 NOT NULL,
  "SubjectType" VARCHAR(30)  NOT NULL,
  "Version" DECIMAL(7,5) DEFAULT 0 NOT NULL,
  PRIMARY KEY ("UID"),
  CONSTRAINT "SUBJECT_ROLE_CON" UNIQUE ("Role","Subject","SubjectType")
);
CREATE INDEX  "SUBJECT_ROLE_IDX1" ON  "subject_role" ("Subject","SubjectType");
CREATE INDEX  "SUBJECT_ROLE_IDX2" ON  "subject_role" ("Role","SubjectType");

ALTER TABLE "subject_role" OWNER TO userdb;



---
--- Table structure for table 'access_right'
---
DROP TABLE IF EXISTS "access_right";
CREATE TABLE "access_right" (
  "UID" BIGINT DEFAULT 0 NOT NULL, 
  "RoleUID" BIGINT DEFAULT 0 NOT NULL, 
  "Feature" VARCHAR(50)  NOT NULL, 
  "Description" VARCHAR(80)  NOT NULL, 
  "Action" VARCHAR(30)  NOT NULL, 
  "DataType" VARCHAR(30), 
  "Criteria" TEXT, 
  "CanDelete" DECIMAL(1) DEFAULT 0 NOT NULL, 
  "Version" DECIMAL(7,5) DEFAULT 0 NOT NULL, 
  PRIMARY KEY ("UID"),
  CONSTRAINT "ACCESS_RIGHT_CON" UNIQUE ("RoleUID","Feature","Action","DataType")
);

CREATE INDEX  "ACCESS_RIGHT_IDX1" ON  "access_right" ("RoleUID");
CREATE INDEX  "ACCESS_RIGHT_IDX2" ON  "access_right" ("RoleUID", "Feature");

ALTER TABLE "access_right" OWNER TO userdb;

---
--- Table structure for table 'feature'
---
DROP TABLE IF EXISTS "feature";
CREATE TABLE "feature"(
  "UID" BIGINT DEFAULT 0 NOT NULL,
  "Feature" VARCHAR(30)  NOT NULL,
  "Description" VARCHAR(80)  NOT NULL,
  "Actions" TEXT  NOT NULL,
  "DataTypes" TEXT  NOT NULL,
  "Version" DECIMAL(7,5) DEFAULT 0 NOT NULL,
  PRIMARY KEY ("UID"),
  CONSTRAINT "FEATURE_CON" UNIQUE ("Feature")
);
ALTER TABLE "feature" OWNER TO userdb;


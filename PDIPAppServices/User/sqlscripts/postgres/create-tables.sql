SET client_encoding = 'UTF8';
SET check_function_bodies = false;
SET client_min_messages = warning;
SET search_path = userdb;

---
--- Table structure for table 'user_account'
---
DROP TABLE IF EXISTS "user_account";
CREATE TABLE "user_account" (
  "UID" BIGINT DEFAULT 0 NOT NULL  ,
  "Name" VARCHAR(50)  NOT NULL  ,
  "ID" VARCHAR(15)  NOT NULL  ,
  "Password" VARCHAR(12)  NOT NULL  ,
  "Phone" VARCHAR(16) ,
  "Email" VARCHAR(255) ,
  "Property" VARCHAR(255) ,
  "Version" DECIMAL(7,5) DEFAULT 1 NOT NULL ,
  PRIMARY KEY ("UID"),
  CONSTRAINT "USER_ACCOUNT_CON" UNIQUE ("ID") 
);

CREATE INDEX  "USER_ACCOUNT_IDX" ON  "user_account" ("Name");
ALTER TABLE "user_account" OWNER TO userdb;


---
--- Table structure for table 'user_account_state'
---
DROP TABLE IF EXISTS "user_account_state";
CREATE TABLE "user_account_state" (
  "UID" BIGINT DEFAULT 0 NOT NULL  ,
  "UserID" VARCHAR(15)  NOT NULL  ,
  "NumLoginTries" DECIMAL(5) DEFAULT 0 NOT NULL  ,
  "IsFreeze" DECIMAL(1) DEFAULT 0 NOT NULL  ,
  "FreezeTime" TIMESTAMP ,
  "LastLoginTime" TIMESTAMP ,
  "LastLogoutTime" TIMESTAMP ,
  "State" DECIMAL(1) DEFAULT 1 NOT NULL  ,
  "CanDelete" DECIMAL(1) DEFAULT 1 NOT NULL  ,
  "CreateTime" TIMESTAMP ,
  "CreateBy" VARCHAR(15) ,
  PRIMARY KEY ("UID"),
  CONSTRAINT "USER_ACCOUNT_STATE_CON" UNIQUE ("UserID") 
);
ALTER TABLE "user_account_state" OWNER TO userdb;



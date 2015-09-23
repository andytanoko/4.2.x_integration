SET client_encoding = 'UTF8';
SET check_function_bodies = false;
SET client_min_messages = warning;
SET search_path = userdb;
---
--- Table structure for table 'jms_router'
---
DROP TABLE IF EXISTS "jms_router";
CREATE TABLE "jms_router" (
  "UID" BIGINT DEFAULT 0 NOT NULL,
  "Name" VARCHAR(50) NOT NULL,
  "IpAddress" VARCHAR(50) NOT NULL,
  PRIMARY KEY ("UID")
);
ALTER TABLE "jms_router" OWNER TO userdb;



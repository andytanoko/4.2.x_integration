SET client_encoding = 'UTF8';
SET check_function_bodies = false;
SET client_min_messages = warning;
SET search_path = userdb;


---
--- Table structure for table 'activation_record'
---

DROP TABLE IF EXISTS "activation_record";
CREATE TABLE "activation_record" (
  "UID" BIGINT DEFAULT 0 NOT NULL,
  "CurrentType" DECIMAL(1) DEFAULT 0 NOT NULL,
  "ActDirection" DECIMAL(1) DEFAULT NULL,
  "DeactDirection" DECIMAL(1) DEFAULT NULL,
  "GridNodeID" DECIMAL(10) NOT NULL,
  "GridNodeName" VARCHAR(80),
  "DTRequested" TIMESTAMP DEFAULT NULL,
  "DTApproved" TIMESTAMP DEFAULT NULL,
  "DTAborted" TIMESTAMP DEFAULT NULL,
  "DTDeactivated" TIMESTAMP DEFAULT NULL,
  "DTDenied" TIMESTAMP DEFAULT NULL,
  "IsLatest" DECIMAL(1) DEFAULT 1 NOT NULL,
  "TransCompleted" DECIMAL(1) DEFAULT 0 NOT NULL,
  "TransFailReason" VARCHAR(255),
  "ActivationDetails" TEXT,
  PRIMARY KEY ("UID")
);
ALTER TABLE "activation_record" OWNER TO userdb;


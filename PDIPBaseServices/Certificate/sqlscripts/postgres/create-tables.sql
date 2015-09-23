SET client_encoding = 'UTF8';
SET check_function_bodies = false;
SET client_min_messages = warning;
SET search_path = userdb;

--- 
--- Table structure for table 'certificate' 
--- 
DROP TABLE IF EXISTS "certificate";
CREATE TABLE "certificate" (
  "UID" BIGINT DEFAULT 0 NOT NULL,
  "ID" BIGINT DEFAULT 0 NOT NULL,
  "Name" VARCHAR(50) ,
  "SerialNum" VARCHAR(64) ,
  "IssuerName" VARCHAR(1000) ,
  "Certificate" TEXT NULL,
  "PublicKey" TEXT NULL,
  "PrivateKey" TEXT NULL,
  "RevokeID" DECIMAL(10) DEFAULT 0,
  "isMaster" DECIMAL(1) DEFAULT 0 NOT NULL,
  "isPartner" DECIMAL(1) DEFAULT 0 NOT NULL,
  "Category" DECIMAL(1),
  "iSINKS" DECIMAL(1) DEFAULT 0 NOT NULL,
  "iSINTS" DECIMAL(1) DEFAULT 0 NOT NULL,
  "relatedCertUid" BIGINT,
  "StartDate" TIMESTAMP NULL,
  "EndDate" TIMESTAMP NULL,
  "IsCA" DECIMAL(1) DEFAULT 0 NOT NULL,
  "ReplacementCertUid" BIGINT NULL
);
ALTER TABLE "certificate" OWNER TO userdb;
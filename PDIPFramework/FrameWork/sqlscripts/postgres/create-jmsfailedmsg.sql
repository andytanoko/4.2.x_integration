SET client_encoding = 'UTF8';
SET check_function_bodies = false;
SET client_min_messages = warning;
SET search_path = userdb;

-- To store the JMS msg that failed to be delivered to the destination, from GT4.1.2
CREATE TABLE "jms_failed_msg" (
   "UID" BIGSERIAL NOT NULL,
   "DestinationType" VARCHAR(20),
   "ConfigName" VARCHAR(20),
   "DestName" VARCHAR(100) NOT NULL,
   "MsgObj" BYTEA NOT NULL,
   "MsgProps" BYTEA,
   "CreatedDate" TIMESTAMP(3) NOT NULL,
   "RetryCount" DECIMAL(10) DEFAULT 0 NOT NULL,
   "JmsConfigProps" BYTEA,
   PRIMARY KEY("UID")
);

ALTER TABLE "jms_failed_msg" OWNER TO userdb;
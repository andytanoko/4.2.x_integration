
-- To store the JMS msg that failed to be delivered to the destination

CREATE TABLE "jms_failed_msg" (
   "UID" NUMBER(19) NOT NULL ENABLE,
   "DestinationType" VARCHAR(20),
   "ConfigName" VARCHAR(20),
   "DestName" VARCHAR(100) NOT NULL ENABLE,
   "MsgObj" BLOB NOT NULL ENABLE,
   "MsgProps" BLOB,
   "CreatedDate" TIMESTAMP(3) NOT NULL ENABLE,
   "RetryCount" NUMBER(10) DEFAULT 0 NOT NULL ENABLE,
   "JmsConfigProps" BLOB,
   PRIMARY KEY("UID")
);
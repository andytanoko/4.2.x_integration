
CONNECT GTVAN/gridnode;
ALTER SESSION SET NLS_DATE_FORMAT = 'YYYY-MM-DD HH24:MI:SS';

-- The JMS retry configuration
INSERT INTO "config_props" ("category", "property_key", "value") VALUES ('failed.jms', 'max.retry', '10');
INSERT INTO "config_props" ("category", "property_key", "value") VALUES ('failed.jms', 'retry.interval', '10000');
INSERT INTO "config_props" ("category", "property_key", "value") VALUES ('failed.jms', 'is.send.via.def', 'false');

-- The switch to revert back the JMS listener handling of redelivered msg
INSERT INTO "config_props" ("category", "property_key", "value") VALUES ('jms.handle.mode','jms.redelivered','false');

-- Table for handling JMS msg that failed to be delivered to their destination
CREATE TABLE "jms_failed_msg" (
   "uid" VARCHAR2(36) NOT NULL, 
   "version" NUMBER(10), 
   "dest_name" VARCHAR(100),
   "msg_obj" BLOB NOT NULL ENABLE,
   "msg_props" BLOB,
   "send_props" BLOB,
   "created_date" TIMESTAMP(3) WITH TIME ZONE NOT NULL ENABLE, 
   "retry_count" NUMBER(10) DEFAULT 0 NOT NULL ENABLE,
   "category" VARCHAR2(20) NOT NULL ENABLE,
   PRIMARY KEY("uid")
);



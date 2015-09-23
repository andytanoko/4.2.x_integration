SET client_encoding = 'UTF8';
SET check_function_bodies = false;
SET client_min_messages = warning;
SET search_path = gtvan;

---
--- Table structure for table 'httpbc_config_props'
---
DROP TABLE IF EXISTS "config_props";
CREATE TABLE "config_props"
(
  "uid" BIGSERIAL NOT NULL, -- UID of the property
  "category" VARCHAR(80) NOT NULL, -- Category of the Configuration property
  "property_key" VARCHAR(80) NOT NULL, -- The property key value
  "value" VARCHAR(1000), -- The value of the configuration property
  PRIMARY KEY ("uid"),
  CONSTRAINT "config_props_cat_prop_key" UNIQUE ("category", "property_key")
); 
ALTER TABLE "config_props" OWNER TO gtvan;

COMMENT ON TABLE "config_props" IS 'Table to store configuration properties';
COMMENT ON COLUMN "config_props"."category" IS 'Category of the Configuration property';
COMMENT ON COLUMN "config_props"."property_key" IS 'The property key value';
COMMENT ON COLUMN "config_props"."value" IS 'The value of the configuration property';

---
--- Table structure for table 'schedule'
---
DROP TABLE IF EXISTS "schedule";
CREATE TABLE "schedule"
(
  "schedule_uid" BIGSERIAL NOT NULL,
  "target" VARCHAR(100), -- Target MBean service name
  "method_name" VARCHAR(100), -- Method name to call on MBean
  "method_signature" VARCHAR(100), -- Method signature
  "start_date" VARCHAR(20), -- Start date of of call
  "period" DECIMAL(19), -- Number of milliseconds between calls
  "repetitions" DECIMAL(10), -- Number of times to repeat calls
  "date_format" VARCHAR(20), -- The date format of the date specified in start_date
  PRIMARY KEY ("schedule_uid")
);
ALTER TABLE "schedule" OWNER TO gtvan;


DROP TABLE IF EXISTS "httpbc_txrec";
CREATE TABLE "httpbc_txrec"
(
  "uid" VARCHAR(36) NOT NULL, -- Unique identity of record
  "receive_ts" timestamp with time zone NOT NULL,
  "direction" VARCHAR(10) NOT NULL, -- The direction whereby the transaction is to be delivered.
  "tracing_id" VARCHAR(36) NOT NULL, -- The identification of a transaction that comes from a source.
  "tx_doc" bytea NOT NULL, -- Serialized content of the transaction document to be delivered.
  "attempt_count" DECIMAL(10) DEFAULT 0 NOT NULL, -- The number of attempts to deliver the transaction with vain
  "version" DECIMAL(10), -- Version of record
  PRIMARY KEY ("uid")
);
ALTER TABLE "httpbc_txrec" OWNER TO gtvan;

COMMENT ON TABLE "httpbc_txrec" IS 'Table to temporary store transactions pending to be delivered to the target destination';
COMMENT ON COLUMN "httpbc_txrec"."uid" IS 'Unique identity of record';
COMMENT ON COLUMN "httpbc_txrec"."direction" IS 'The direction whereby the transaction is to be delivered.';
COMMENT ON COLUMN "httpbc_txrec"."tracing_id" IS 'The identification of a transaction that comes from a source.';
COMMENT ON COLUMN "httpbc_txrec"."tx_doc" IS 'Serialized content of the transaction document to be delivered.';
COMMENT ON COLUMN "httpbc_txrec"."attempt_count" IS 'The number of attempts to deliver the transaction with vain';
COMMENT ON COLUMN "httpbc_txrec"."version" IS 'Version of record';


DROP TABLE IF EXISTS "isat_business_document" CASCADE;
CREATE TABLE "isat_business_document" (
  "uid" VARCHAR(36),
  "files" BYTEA NOT NULL,
  "is_required_unpack" boolean DEFAULT false NOT NULL,
  "is_required_unzip" boolean DEFAULT false NOT NULL,
  "group_name" VARCHAR(50),
  "version" DECIMAL(10),
  PRIMARY KEY ("uid")
);
ALTER TABLE "isat_business_document" OWNER TO gtvan;


DROP TABLE IF EXISTS "isat_trace_event_info";
CREATE TABLE "isat_trace_event_info" (
  "uid" VARCHAR(36),
  "event_name" VARCHAR(50) NOT NULL,
  "event_occur_time" TIMESTAMP WITH TIME ZONE NOT NULL,
  "message_id" VARCHAR(25),
  "msg_type" VARCHAR(10),
  "status" VARCHAR(20) NOT NULL,
  "tracing_id" VARCHAR(36) NOT NULL,
  "biz_document_uid" VARCHAR(36),
  "error_reason" TEXT,
  "event_remark" VARCHAR(200),
  "reprocess_linkage_uid" DECIMAL(19),
  "group_name" VARCHAR(50),
  "version" DECIMAL(10),
  PRIMARY KEY("uid"),
  CONSTRAINT ISAT_TRACE_EVENT_INFO_FK FOREIGN KEY ("biz_document_uid")
    REFERENCES "isat_business_document"("uid")
    ON DELETE CASCADE
);
ALTER TABLE "isat_trace_event_info" OWNER TO gtvan;

CREATE INDEX trace_event_info_idx1 ON "isat_trace_event_info" ("tracing_id","event_name");
CREATE INDEX trace_event_info_idx2 ON "isat_trace_event_info" ("tracing_id","event_name","message_id");
CREATE INDEX trace_event_info_idx3 ON "isat_trace_event_info" ("tracing_id","event_name","event_occur_time");
CREATE INDEX trace_event_info_idx4 ON "isat_trace_event_info" ("event_occur_time","event_name","status","message_id","msg_type");

DROP TABLE IF EXISTS "isat_document_transaction" CASCADE;
CREATE TABLE "isat_document_transaction" (
  "uid" VARCHAR(36),
  "document_type" VARCHAR(30) NOT NULL,
  "doc_no" VARCHAR(80),
  "direction" VARCHAR(10) NOT NULL,
  "message_id" VARCHAR(25) NOT NULL,
  "doc_time_sent" TIMESTAMP WITH TIME ZONE,
  "doc_time_received" TIMESTAMP WITH TIME ZONE,
  "biz_document_uid" VARCHAR(36),
  "tracing_id" VARCHAR(36) NOT NULL,
  "process_instance_uid" DECIMAL(19) NOT NULL,
  "document_size" DECIMAL(10) NOT NULL,
  "user_tracking_id" VARCHAR(255),
  "is_duplicate" boolean DEFAULT false NOT NULL,
  "is_retry" boolean DEFAULT false NOT NULL,
  "is_signal" boolean DEFAULT false NOT NULL,
  "customer_name" VARCHAR(80),
  "partner_name" VARCHAR(80),
  "group_name" VARCHAR(50) NOT NULL,
  "version" DECIMAL(10),
  PRIMARY KEY("uid"),
  CONSTRAINT ISAT_DOC_TRANSACTION_FK FOREIGN KEY ("biz_document_uid")
    REFERENCES "isat_business_document"("uid")
    ON DELETE CASCADE
);
ALTER TABLE "isat_document_transaction" OWNER TO gtvan;

CREATE INDEX document_transaction_idx1 ON "isat_document_transaction" ("tracing_id","direction","document_type","process_instance_uid");
CREATE INDEX document_transaction_idx2 ON "isat_document_transaction" ("tracing_id","message_id");
CREATE INDEX document_transaction_idx3 ON "isat_document_transaction" ("process_instance_uid");
CREATE INDEX document_transaction_idx4 ON "isat_document_transaction" ("tracing_id","process_instance_uid");


DROP TABLE IF EXISTS "isat_process_transaction";
CREATE TABLE "isat_process_transaction" (
  "uid" VARCHAR(36),
  "pip_name" VARCHAR(80),
  "pip_version" VARCHAR(80),
  "process_id" VARCHAR(335),
  "process_start_time" TIMESTAMP WITH TIME ZONE,
  "process_end_time" TIMESTAMP WITH TIME ZONE,
  "partner_name" VARCHAR(80),
  "partner_duns" VARCHAR(80),
  "customer_name" VARCHAR(80),
  "customer_duns" VARCHAR(80),
  "request_doc_no" VARCHAR(80),
  "response_doc_no" VARCHAR(80),
  "process_status" VARCHAR(50),
  "is_process_success" boolean DEFAULT false,
  "is_initiator" boolean DEFAULT false,
  "error_type" VARCHAR(40),
  "error_reason" TEXT,
  "user_tracking_id" VARCHAR(255),
  "process_instance_uid" DECIMAL(19),
  "group_name" VARCHAR(50),
  "version" DECIMAL(10),
  PRIMARY KEY("uid"),
  CONSTRAINT ISAT_PROC_TRANSACTION_CON UNIQUE("process_instance_uid")
);
ALTER TABLE "isat_process_transaction" OWNER TO gtvan;

-- Tune for searching for "process_instance_id" + "date_range", or any combination that 
CREATE INDEX PROCESS_TRANSACTION_IDX5 ON "isat_process_transaction" (
  "process_start_time",   
  "group_name",
  "customer_name",
  "pip_name",     
  "process_status",
  "request_doc_no",
  "response_doc_no",
  "process_id",
  "partner_name"
);
CREATE INDEX PROCESS_TRANSACTION_IDX7 ON "isat_process_transaction" ("process_start_time","group_name");

DROP TABLE IF EXISTS "isat_trace_event_header";
CREATE TABLE "isat_trace_event_header" (
  "uid" VARCHAR(36),
  "last_event_status" VARCHAR(20) NOT NULL,
  "error_count" SMALLINT DEFAULT 0 NOT NULL,
  "last_event_occur_time" TIMESTAMP WITH TIME ZONE NOT NULL,
  "tracing_id" VARCHAR(36) NOT NULL,
  "msg_type" VARCHAR(10),
  "group_name" VARCHAR(50),
  "version" DECIMAL(10),
  PRIMARY KEY("uid"),
  CONSTRAINT ISAT_TRACE_EVENT_HDR_CON UNIQUE("tracing_id")
);
ALTER TABLE "isat_trace_event_header" OWNER TO gtvan;

DROP SEQUENCE IF EXISTS isat_resource_sequence CASCADE;
CREATE SEQUENCE isat_resource_sequence;
ALTER SEQUENCE isat_resource_sequence OWNER to gtvan;

DROP TABLE IF EXISTS "isat_resource";
CREATE TABLE "isat_resource" (
  "uid" VARCHAR(36) DEFAULT nextval('isat_resource_sequence'),
  --"uid" bigserial,
  "type" VARCHAR(255) NOT NULL,
  "code" VARCHAR(255) NOT NULL,
  "value" VARCHAR(255),
  "group_name" VARCHAR(50),
  "version" DECIMAL(10),
  PRIMARY KEY("uid"),
  CONSTRAINT ISAT_RESOURCE_CON UNIQUE("group_name","type","code")
);
ALTER TABLE "isat_resource" OWNER TO gtvan;

DROP TABLE IF EXISTS "isat_biz_entity_group_mapping";
CREATE TABLE "isat_biz_entity_group_mapping" (
  "uid" VARCHAR(36),
  "be_id" VARCHAR(20) NOT NULL,
  "group_name" VARCHAR(50) NOT NULL,
  "version" DECIMAL(10),
  PRIMARY KEY("uid"),
  CONSTRAINT ISAT_BIZ_ENTITY_CON1 UNIQUE("be_id"),
  CONSTRAINT ISAT_BIZ_ENTITY_CON2 UNIQUE("group_name")
);
ALTER TABLE "isat_biz_entity_group_mapping" OWNER TO gtvan;


DROP TABLE IF EXISTS "isat_audit_trail_data";
CREATE TABLE "isat_audit_trail_data" (
  "uid" VARCHAR(36) NOT NULL, 
  "object" BYTEA NOT NULL, 
  "version" DECIMAL(10), 
  "attempt_count" DECIMAL(10) DEFAULT 0 NOT NULL, 
  "failed_reason" TEXT, 
  "last_modified_date" TIMESTAMP WITH TIME ZONE,
  PRIMARY KEY("uid")
);
ALTER TABLE "isat_audit_trail_data" OWNER TO gtvan;

CREATE INDEX audit_trail_data_idx1 ON "isat_audit_trail_data" ("last_modified_date","attempt_count");


DROP TABLE IF EXISTS "isat_archive_scheduler";
CREATE TABLE "isat_archive_scheduler" (
  "uid" VARCHAR(36) NOT NULL, 
  "effective_start_time" TIMESTAMP WITH TIME ZONE NOT NULL, 
  "frequency" VARCHAR(15) NOT NULL, 
  "is_active" boolean DEFAULT true NOT NULL, 
  "last_run_time" TIMESTAMP WITH TIME ZONE, 
  "next_run_time" TIMESTAMP WITH TIME ZONE, 
  "archive_record_older" DECIMAL(5), 
  "customer_list" VARCHAR(2000), 
  "archive_every" DECIMAL(5), 
  "is_success_invoke" boolean DEFAULT true NOT NULL, 
  "archive_start_date" TIMESTAMP(3) WITH TIME ZONE, 
  "archive_end_date" TIMESTAMP(3) WITH TIME ZONE, 
  "timezone_id" VARCHAR(50),
  "is_archive_orphan_record" boolean DEFAULT false NOT NULL,
  "version" DECIMAL(10), 
  PRIMARY KEY("uid")
);
ALTER TABLE "isat_archive_scheduler" OWNER TO gtvan;

CREATE INDEX archive_scheduler_idx1 ON "isat_archive_scheduler" ("next_run_time","is_active");


---
--- View that combine DocumentTransaction & ProcessTransaction
---
CREATE OR REPLACE VIEW "isat_process_doc_tran_view"
AS SELECT "isat_process_transaction"."uid" AS "process_trans_uid", "isat_process_transaction"."pip_name", "isat_process_transaction"."pip_version",
    "isat_process_transaction"."process_id", "isat_process_transaction"."process_start_time", "isat_process_transaction"."process_end_time",
    "isat_process_transaction"."partner_name", "isat_process_transaction"."partner_duns", "isat_process_transaction"."customer_name", 
    "isat_process_transaction"."customer_duns", "isat_process_transaction"."request_doc_no", "isat_process_transaction"."response_doc_no", 
    "isat_process_transaction"."process_status", "isat_process_transaction"."error_type", "isat_process_transaction"."error_reason", 
    "isat_process_transaction"."process_instance_uid", "isat_process_transaction"."group_name", "isat_document_transaction"."uid" AS "doc_trans_uid", 
    "isat_document_transaction"."document_type", "isat_document_transaction"."doc_no", "isat_document_transaction"."direction", "isat_document_transaction"."message_id",
    "isat_document_transaction"."doc_time_sent", "isat_document_transaction"."doc_time_received", "isat_document_transaction"."biz_document_uid",
    "isat_document_transaction"."tracing_id", "isat_document_transaction"."is_duplicate", "isat_document_transaction"."is_retry",
    "isat_document_transaction"."document_size", "isat_document_transaction"."user_tracking_id","isat_document_transaction"."is_signal"
FROM "isat_process_transaction", "isat_document_transaction"
WHERE ("isat_document_transaction"."process_instance_uid"=  "isat_process_transaction"."process_instance_uid");

ALTER TABLE "isat_process_doc_tran_view" OWNER TO gtvan;

---
--- Table structure for table 'rpt_schedule'
---
DROP TABLE IF EXISTS "rpt_schedule";
CREATE TABLE "rpt_schedule" (
    "uid" VARCHAR(36) NOT NULL,
    "version" DECIMAL(10),
    "schedule_id" BIGSERIAL NOT NULL,
    "effective_start_date" DATE NOT NULL,
    "email_list" VARCHAR(1000),
    "customer_list" VARCHAR(1000),
    "report_location" VARCHAR(100),
    "group_name" VARCHAR(50) NOT NULL,
    "report_type" VARCHAR(80) NOT NULL,
    "run_time" VARCHAR(17) NOT NULL,
    "next_run_date_time" TIMESTAMP(3) WITH TIME ZONE,
    "frequency" VARCHAR(7) NOT NULL,
    "report_content" BYTEA,
    "next_start_date_time" TIMESTAMP(3) WITH TIME ZONE,
    "next_end_date_time" TIMESTAMP(3) WITH TIME ZONE,
    "report_format" VARCHAR(5) NOT NULL,
    "username" VARCHAR(80),
    "created_date_time" TIMESTAMP(3) WITH TIME ZONE,
    "modified_date_time" TIMESTAMP(3) WITH TIME ZONE,
    PRIMARY KEY ("uid")
);
ALTER TABLE "rpt_schedule" OWNER TO gtvan;

---
--- Table structure for table 'rpt_report_type'
---
DROP TABLE IF EXISTS "rpt_report_type";
CREATE TABLE "rpt_report_type" (
    "uid" VARCHAR(36) NOT NULL,
    "version" DECIMAL(10) NOT NULL,
    "report_type" VARCHAR(80) NOT NULL,
    "template_name" VARCHAR(100),
    "datasource_type" VARCHAR(4),
    PRIMARY KEY ("uid")
);
ALTER TABLE "rpt_report_type" OWNER TO gtvan;

---------------------------------------------------------------------------

---
--- Table structure for table 'rpt_report'
---
DROP TABLE IF EXISTS "rpt_report";
CREATE TABLE "rpt_report" (
    "uid" VARCHAR(36) NOT NULL,
    "version" DECIMAL(10) NOT NULL,
    "report_type" VARCHAR(80) NOT NULL,
    "frequency" VARCHAR(7) NOT NULL,
    "status" VARCHAR(7) NOT NULL,
    "customer_list" VARCHAR(1000),
    "start_date_time" TIMESTAMP WITH TIME ZONE NOT NULL,
    "end_date_time" TIMESTAMP(3) WITH TIME ZONE NOT NULL,
    "report_content" BYTEA,
    "archive_duration" DECIMAL(5) DEFAULT 30 NOT NULL,
    "email_list" VARCHAR(1000),
    "schedule_doc_id" VARCHAR(36),
    "report_location" VARCHAR(100),
    "group_name" VARCHAR(50) NOT NULL,
    "last_run_date_time" TIMESTAMP WITH TIME ZONE,
    "report_format" VARCHAR(5) NOT NULL,
    "username" VARCHAR(80) NOT NULL,
    "created_date_time" TIMESTAMP WITH TIME ZONE,
    PRIMARY KEY ("uid")
);
ALTER TABLE "rpt_report" OWNER TO gtvan;


DROP TABLE IF EXISTS "adm_groups";
CREATE TABLE "adm_groups" (
    "group_name" VARCHAR(50) NOT NULL,
    "groupid" BIGSERIAL NOT NULL,
    "be_id" VARCHAR(10),
  PRIMARY KEY("groupid")
);
ALTER TABLE "adm_groups" OWNER TO gtvan;


DROP TABLE IF EXISTS "adm_setting";
CREATE TABLE "adm_setting" (
    "set_id" DECIMAL(10) NOT NULL,
    "idle_setting" VARCHAR(30) NOT NULL,
  PRIMARY KEY("set_id")
);
ALTER TABLE "adm_setting" OWNER TO gtvan;


DROP TABLE IF EXISTS "adm_user_level";
CREATE TABLE "adm_user_level" (
    "level_name" VARCHAR(25) NOT NULL,
    "level_id" DECIMAL(10) NOT NULL,
  PRIMARY KEY("level_id")
);
ALTER TABLE "adm_user_level" OWNER TO gtvan;


DROP TABLE IF EXISTS "adm_users";
CREATE TABLE "adm_users" (
    "first_name" VARCHAR(25),
    "last_name" VARCHAR(25),
    "email_address" VARCHAR(50),
    "username" VARCHAR(25),
    "password" VARCHAR(100),
    "info" VARCHAR(500),
    "signup_date" TIMESTAMP WITH TIME ZONE,
    "user_right" VARCHAR(30),
    "user_group" VARCHAR(30),
    "last_login" TIMESTAMP WITH TIME ZONE,
    "last_logout" TIMESTAMP WITH TIME ZONE,
    "last_active" DECIMAL(10),
    "status" VARCHAR(8),
    "activated" VARCHAR(10),
    "userid" DECIMAL(19) NOT NULL,
  PRIMARY KEY("userid")
); 
ALTER TABLE "adm_users" OWNER TO gtvan;


DROP TABLE IF EXISTS "adm_log";
CREATE TABLE "adm_log" (
    "logid" serial NOT NULL,
    "date_time" DATE NOT NULL,
    "group" VARCHAR(20),
    "username" VARCHAR(50),
    "log_type" VARCHAR(20),
    "log_content" VARCHAR(100),
    "user_level" VARCHAR(20),
  PRIMARY KEY("logid")
); 
ALTER TABLE "adm_log" OWNER TO gtvan;


DROP TABLE IF EXISTS "jms_failed_msg";
CREATE TABLE "jms_failed_msg" (
   "uid" VARCHAR(36) NOT NULL, 
   "version" DECIMAL(10), 
   "dest_name" VARCHAR(100),
   "msg_obj" BYTEA NOT NULL,
   "msg_props" BYTEA,
   "send_props" BYTEA,
   "created_date" TIMESTAMP WITH TIME ZONE NOT NULL, 
   "retry_count" DECIMAL(10) DEFAULT 0 NOT NULL,
   "category" VARCHAR(20) NOT NULL,
   PRIMARY KEY("uid")
);
ALTER TABLE "jms_failed_msg" OWNER TO gtvan;


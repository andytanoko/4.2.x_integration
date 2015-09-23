-- 26 June 2007    [Tam Wei Xiang]     GTVAN     Added field "is_archive_orphan_record" into table "archive_scheduler"
--                                               to support archive by customer.

--- --------------------------------------------------------------------
--- This script includes all the CREATE queries for all tables in GTVAN
--- --------------------------------------------------------------------

CONNECT GTVAN/gridnode;

ALTER SESSION SET NLS_DATE_FORMAT = 'YYYY-MM-DD HH24:MI:SS';

---------------------------------------------------------------------------

---
--- Table structure for table 'httpbc_config_props'
---
CREATE TABLE "config_props"
(
  "uid" NUMBER(10) NOT NULL ENABLE, -- UID of the property
  "category" VARCHAR2(80) NOT NULL ENABLE, -- Category of the Configuration property
  "property_key" VARCHAR2(80) NOT NULL ENABLE, -- The property key value
  "value" VARCHAR2(1000), -- The value of the configuration property
  PRIMARY KEY ("uid"),
  CONSTRAINT "config_props_cat_prop_key" UNIQUE ("category", "property_key") ENABLE
); 

COMMENT ON TABLE "config_props" IS 'Table to store configuration properties';
COMMENT ON COLUMN "config_props"."category" IS 'Category of the Configuration property';
COMMENT ON COLUMN "config_props"."property_key" IS 'The property key value';
COMMENT ON COLUMN "config_props"."value" IS 'The value of the configuration property';


CREATE SEQUENCE CONFIG_PROPS_SEQ 
START WITH 1 
INCREMENT BY 1 
NOMAXVALUE; 

CREATE TRIGGER CONFIG_PROPS_TRIGGER 
BEFORE INSERT ON "config_props"
REFERENCING NEW AS NEW OLD AS OLD 
FOR EACH ROW
BEGIN
     SELECT CONFIG_PROPS_SEQ.NEXTVAL INTO :NEW."uid" FROM DUAL;
END;
/


---------------------------------------------------------------------------

---
--- Table structure for table 'schedule'
---
CREATE TABLE "schedule"
(
  "schedule_uid" NUMBER(19),
  "target" VARCHAR2(100),
  "method_name" VARCHAR2(100),
  "method_signature" VARCHAR2(100),
  "start_date" VARCHAR2(20),
  "period" NUMBER(19),
  "repetitions" NUMBER(10),
  "date_format" VARCHAR2(20),
  PRIMARY KEY ("schedule_uid")
);

CREATE SEQUENCE GTVAN_SCHEDULE_SEQ 
START WITH 1 
INCREMENT BY 1 
NOMAXVALUE; 

CREATE TRIGGER GTVAN_SCHEDULE_TRIGGER 
BEFORE INSERT ON "schedule"
REFERENCING NEW AS NEW OLD AS OLD 
FOR EACH ROW
BEGIN
     SELECT GTVAN_SCHEDULE_SEQ.NEXTVAL INTO :NEW."schedule_uid" FROM DUAL;
END;
/

---------------------------------------------------------------------------

---
--- Table structure for table 'httpbc_txrec'
---
CREATE TABLE "httpbc_txrec"
(
  "uid" VARCHAR2(36) NOT NULL ENABLE, -- Unique identity of record
  "receive_ts" TIMESTAMP(3) WITH TIME ZONE DEFAULT CURRENT_DATE NOT NULL ENABLE,
  "direction" VARCHAR2(10) NOT NULL ENABLE, -- The direction whereby the transaction is to be delivered.
  "tracing_id" VARCHAR2(36) NOT NULL ENABLE, -- The identification of a transaction that comes from a source.
  "tx_doc" BLOB NOT NULL, -- Serialized content of the transaction document to be delivered.
  "attempt_count" NUMBER(10) DEFAULT 0 NOT NULL ENABLE, -- The number of attempts to deliver the transaction with vain
  "version" NUMBER(10), -- Version of record
  PRIMARY KEY ("uid")
);

COMMENT ON TABLE "httpbc_txrec" IS 'Table to temporary store transactions pending to be delivered to the target destination';
COMMENT ON COLUMN "httpbc_txrec"."uid" IS 'Unique identity of record';
COMMENT ON COLUMN "httpbc_txrec"."direction" IS 'The direction whereby the transaction is to be delivered.';
COMMENT ON COLUMN "httpbc_txrec"."tracing_id" IS 'The identification of a transaction that comes from a source.';
COMMENT ON COLUMN "httpbc_txrec"."tx_doc" IS 'Serialized content of the transaction document to be delivered.';
COMMENT ON COLUMN "httpbc_txrec"."attempt_count" IS 'The number of attempts to deliver the transaction with vain';
COMMENT ON COLUMN "httpbc_txrec"."version" IS 'Version of record';

---------------------------------------------------------------------------

--- 20070524v4.1.1: Add "is_required_unzip" column
---
--- Table structure for table 'isat_business_document'
---
CREATE TABLE "isat_business_document" (
  "uid" VARCHAR2(36),
  "files" BLOB NOT NULL ENABLE,
  "is_required_unpack" NUMBER(1) DEFAULT 0 NOT NULL ENABLE,
  "is_required_unzip" NUMBER(1) DEFAULT 0 NOT NULL ENABLE,
  "group_name" VARCHAR2(50),
  "version" NUMBER(10),
  PRIMARY KEY ("uid")
);

---------------------------------------------------------------------------

--- 20070523v4.1.1: Add "msg_type" column
---
--- Table structure for table 'isat_trace_event_info'
---
CREATE TABLE "isat_trace_event_info" (
  "uid" VARCHAR2(36),
  "event_name" VARCHAR2(50) NOT NULL ENABLE,
  "event_occur_time" TIMESTAMP(3) WITH TIME ZONE NOT NULL ENABLE,
  "message_id" VARCHAR2(25),
  "msg_type" VARCHAR2(10),
  "status" VARCHAR2(20) NOT NULL ENABLE,
  "tracing_id" VARCHAR2(36) NOT NULL ENABLE,
  "biz_document_uid" VARCHAR2(36),
  "error_reason" CLOB,
  "event_remark" VARCHAR2(200),
  "reprocess_linkage_uid" NUMBER(19),
  "group_name" VARCHAR2(50),
  "version" NUMBER(10),
  PRIMARY KEY("uid"),
  CONSTRAINT ISAT_TRACE_EVENT_INFO_FK FOREIGN KEY ("biz_document_uid")
    REFERENCES "isat_business_document"("uid")
    ON DELETE CASCADE
);

CREATE INDEX trace_event_info_idx1 ON "isat_trace_event_info" ("tracing_id","event_name");
CREATE INDEX trace_event_info_idx2 ON "isat_trace_event_info" ("tracing_id","event_name","message_id");
CREATE INDEX trace_event_info_idx3 ON "isat_trace_event_info" ("tracing_id","event_name","event_occur_time");

---------------------------------------------------------------------------

---
--- Table structure for table 'isat_document_transaction'
---
CREATE TABLE "isat_document_transaction" (
  "uid" VARCHAR2(36),
  "document_type" VARCHAR2(30) NOT NULL ENABLE,
  "doc_no" VARCHAR2(80),
  "direction" VARCHAR2(10) NOT NULL ENABLE,
  "message_id" VARCHAR2(25) NOT NULL ENABLE,
  "doc_time_sent" TIMESTAMP(3) WITH TIME ZONE,
  "doc_time_received" TIMESTAMP(3) WITH TIME ZONE,
  "biz_document_uid" VARCHAR2(36),
  "tracing_id" VARCHAR2(36) NOT NULL ENABLE,
  "process_instance_uid" NUMBER(19) NOT NULL ENABLE,
  "document_size" NUMBER(10) NOT NULL ENABLE,
  "user_tracking_id" VARCHAR2(255),
  "is_duplicate" NUMBER(1) DEFAULT 0 NOT NULL ENABLE,
  "is_retry" NUMBER(1) DEFAULT 0 NOT NULL ENABLE,
  "is_signal" NUMBER(1) DEFAULT 0 NOT NULL ENABLE,
  "customer_name" VARCHAR2(80),
  "partner_name" VARCHAR2(80),
  "group_name" VARCHAR2(50) NOT NULL ENABLE,
  "version" NUMBER(10),
  PRIMARY KEY("uid"),
  CONSTRAINT ISAT_DOC_TRANSACTION_FK FOREIGN KEY ("biz_document_uid")
    REFERENCES "isat_business_document"("uid")
    ON DELETE CASCADE
);

CREATE INDEX document_transaction_idx1 ON "isat_document_transaction" ("tracing_id","direction","document_type","process_instance_uid");
CREATE INDEX document_transaction_idx2 ON "isat_document_transaction" ("tracing_id","message_id");
CREATE INDEX document_transaction_idx3 ON "isat_document_transaction" ("process_instance_uid");
CREATE INDEX document_transaction_idx4 ON "isat_document_transaction" ("tracing_id","process_instance_uid");


---------------------------------------------------------------------------

---
--- Table structure for table 'isat_process_transaction'
---
CREATE TABLE "isat_process_transaction" (
  "uid" VARCHAR2(36),
  "pip_name" VARCHAR2(80),
  "pip_version" VARCHAR2(80),
  "process_id" VARCHAR2(335),
  "process_start_time" TIMESTAMP(3) WITH TIME ZONE,
  "process_end_time" TIMESTAMP(3) WITH TIME ZONE,
  "partner_name" VARCHAR2(80),
  "partner_duns" VARCHAR2(80),
  "customer_name" VARCHAR2(80),
  "customer_duns" VARCHAR2(80),
  "request_doc_no" VARCHAR2(80),
  "response_doc_no" VARCHAR2(80),
  "process_status" VARCHAR2(50),
  "is_process_success" NUMBER(1) DEFAULT 0,
  "is_initiator" NUMBER(1) DEFAULT 0,
  "error_type" VARCHAR2(40),
  "error_reason" CLOB,
  "user_tracking_id" VARCHAR2(255),
  "process_instance_uid" NUMBER(19),
  "group_name" VARCHAR2(50),
  "version" NUMBER(10),
  PRIMARY KEY("uid"),
  CONSTRAINT ISAT_PROC_TRANSACTION_CON UNIQUE("process_instance_uid")
);

CREATE INDEX process_transaction_idx1 ON "isat_process_transaction" ("process_start_time");

---------------------------------------------------------------------------

---
--- Table structure for table 'isat_trace_event_header'
---
CREATE TABLE "isat_trace_event_header" (
  "uid" VARCHAR2(36),
  "last_event_status" VARCHAR2(20) NOT NULL ENABLE,
  "error_count" SMALLINT DEFAULT 0 NOT NULL ENABLE,
  "last_event_occur_time" TIMESTAMP(3) WITH TIME ZONE NOT NULL ENABLE,
  "tracing_id" VARCHAR2(36) NOT NULL ENABLE,
  "msg_type" VARCHAR2(10),
  "group_name" VARCHAR2(50),
  "version" NUMBER(10),
  PRIMARY KEY("uid"),
  CONSTRAINT ISAT_TRACE_EVENT_HDR_CON UNIQUE("tracing_id")
);

--- 20070523v4.1.1: Performance Tuning
--- ---------------------------------------------------------------------------
--- Create Procedure to UPDATE "isat_trace_event_header"
---   - SELECT the existing records to do some conditions checking for updates
---   - UPDATE record
--- ---------------------------------------------------------------------------
CREATE OR REPLACE PROCEDURE update_isat_trace_event_header
(
  n_last_event_status "isat_trace_event_header"."last_event_status"%type,
  n_occur_time "isat_trace_event_header"."last_event_occur_time"%type,
  n_tracing_id "isat_trace_event_header"."tracing_id"%type,
  n_msg_type "isat_trace_event_header"."msg_type"%type,
  n_group_name "isat_trace_event_header"."group_name"%type
)
AS
  s_error_count "isat_trace_event_header"."error_count"%type;
  s_occur_time "isat_trace_event_header"."last_event_occur_time"%type;
  s_group_name "isat_trace_event_header"."group_name"%type;
  s_msg_type "isat_trace_event_header"."msg_type"%type;
BEGIN
  
  SELECT "last_event_occur_time", "error_count", "group_name", "msg_type" INTO s_occur_time, s_error_count, s_group_name, s_msg_type 
    FROM "isat_trace_event_header" WHERE "tracing_id" = n_tracing_id
    FOR UPDATE;
  
  IF n_last_event_status = 'FAILED' THEN
    s_error_count := s_error_count + 1;
  END IF; 
  
  IF n_group_name IS NOT NULL THEN
      s_group_name := n_group_name;
  END IF;
  IF n_msg_type IS NOT NULL THEN
      s_msg_type := n_msg_type;
  END IF;

  IF n_occur_time > s_occur_time THEN
    UPDATE "isat_trace_event_header" SET "last_event_status"=n_last_event_status, 
                                         "error_count"=s_error_count, 
					 "last_event_occur_time"=n_occur_time,
					 "group_name"=s_group_name,
					 "msg_type"=s_msg_type
      WHERE "tracing_id"=n_tracing_id; 
  ELSIF n_last_event_status = 'FAILED' THEN
    UPDATE "isat_trace_event_header" SET "error_count"=s_error_count ,"group_name" = s_group_name, "msg_type" = s_msg_type
      WHERE "tracing_id"=n_tracing_id; 
  ELSIF s_group_name IS NOT NULL OR s_msg_type IS NOT NULL THEN
       UPDATE "isat_trace_event_header" SET "group_name" = s_group_name, "msg_type" = s_msg_type
       WHERE "tracing_id"=n_tracing_id; 
  END IF;
EXCEPTION 
  WHEN OTHERS THEN
    DBMS_OUTPUT.PUT_LINE('Failed to update isat_trace_event_header, tracing_id:'||n_tracing_id);
END update_isat_trace_event_header;
/

--- ---------------------------------------------------------------------
--- Create Procedure to insert new record in "isat_trace_event_header"
---   - INSERT new record
---   - If EXCEPTION caught for Duplicate value found on INDEX, 
---     call Procedure - update_isat_trace_event_header(...)
--- ---------------------------------------------------------------------
CREATE OR REPLACE PROCEDURE insert_isat_trace_event_header
(
  n_msg_type "isat_trace_event_header"."msg_type"%type,
  n_uid "isat_trace_event_header"."uid"%type,
  n_event_occur_time "isat_trace_event_info"."event_occur_time"%type,
  n_status "isat_trace_event_info"."status"%type,
  n_tracing_id "isat_trace_event_info"."tracing_id"%type,
  n_group_name "isat_trace_event_info"."group_name"%type
)
AS
  s_error_count "isat_trace_event_header"."error_count"%type;
BEGIN
  
  IF n_status = 'FAILED' THEN
      s_error_count := 1;
    ELSE
      s_error_count := 0;
    END IF;
    INSERT INTO "isat_trace_event_header"("uid","last_event_status","error_count","last_event_occur_time","tracing_id","msg_type","group_name","version") 
      VALUES (n_uid,n_status,s_error_count,n_event_occur_time,n_tracing_id,n_msg_type,n_group_name,0);

EXCEPTION
  WHEN DUP_VAL_ON_INDEX THEN
    DBMS_OUTPUT.PUT_LINE('Duplicate data found, tracing_id='||n_tracing_id);
    update_isat_trace_event_header(n_status,n_event_occur_time,n_tracing_id,n_msg_type,n_group_name);

END insert_isat_trace_event_header;
/
--- ---------------------------------------------------------------------
--- Create Procedure to check the existance of "isat_trace_event_header"
---   If exist, call Procedure - update_isat_trace_event_header(...)
---   Else call Procedure - insert_isat_trace_event_header(...)
--- ---------------------------------------------------------------------
CREATE OR REPLACE PROCEDURE check_isat_trace_event_header
(
  n_msg_type "isat_trace_event_header"."msg_type"%type,
  n_uid "isat_trace_event_header"."uid"%type,
  n_event_occur_time "isat_trace_event_info"."event_occur_time"%type,
  n_status "isat_trace_event_info"."status"%type,
  n_tracing_id "isat_trace_event_info"."tracing_id"%type,
  n_group_name "isat_trace_event_info"."group_name"%type
) 
AS
  s_error_count "isat_trace_event_header"."error_count"%type;
  s_occur_time "isat_trace_event_header"."last_event_occur_time"%type;
BEGIN 

  SELECT "last_event_occur_time", "error_count" INTO s_occur_time, s_error_count 
    FROM "isat_trace_event_header" WHERE "tracing_id" = n_tracing_id;
  
  IF s_occur_time IS NOT NULL THEN    
    update_isat_trace_event_header(n_status,n_event_occur_time,n_tracing_id,n_msg_type,n_group_name);
  END IF;

EXCEPTION
  WHEN NO_DATA_FOUND THEN
    DBMS_OUTPUT.PUT_LINE('No Data found, tracing_id='||n_tracing_id);
    insert_isat_trace_event_header(n_msg_type,n_uid,n_event_occur_time,n_status,n_tracing_id,n_group_name);
END check_isat_trace_event_header;
/

--- ----------------------------------------------------------------------------------------------------------
--- Create Trigger to INSERT or UPDATE "isat_trace_event_header" before any INSERT on "isat_trace_event_info"
--- ----------------------------------------------------------------------------------------------------------
CREATE OR REPLACE TRIGGER event_header_change
  BEFORE INSERT ON "isat_trace_event_info"
  FOR EACH ROW
  WHEN (new."tracing_id" IS NOT NULL)
  CALL check_isat_trace_event_header(:new."msg_type",:new."uid",:new."event_occur_time",:new."status",:new."tracing_id",:new."group_name")
/

--- 20070523v4.1.1

---------------------------------------------------------------------------

---
--- Table structure for table 'isat_resource'
---
CREATE TABLE "isat_resource" (
  "uid" VARCHAR2(36),
  "type" VARCHAR2(255) NOT NULL ENABLE,
  "code" VARCHAR2(255) NOT NULL ENABLE,
  "value" VARCHAR2(255),
  "group_name" VARCHAR2(50),
  "version" NUMBER(10),
  PRIMARY KEY("uid"),
  CONSTRAINT ISAT_RESOURCE_CON UNIQUE("group_name","type","code")
);

--- 20070523v4.1.1: Performance Tuning
--- ---------------------------------------------------------
--- Create new sequence for generating UID for isat_resource
--- ---------------------------------------------------------
CREATE SEQUENCE ISAT_RESOURCE_SEQ 
START WITH 1 
INCREMENT BY 1 
NOMAXVALUE; 

CREATE TRIGGER ISAT_RESOURCE_TRIGGER 
BEFORE INSERT ON "isat_resource"
REFERENCING NEW AS NEW OLD AS OLD 
FOR EACH ROW
BEGIN
     SELECT ISAT_RESOURCE_SEQ.NEXTVAL INTO :NEW."uid" FROM DUAL;
END;
/

--- -----------------------------------------
--- Create Procedure INSERT "isat_resource"
--- -----------------------------------------

CREATE OR REPLACE PROCEDURE isat_resource_insert
(
  n_type "isat_resource"."type"%type,
  n_code "isat_resource"."code"%type,
  n_value "isat_resource"."value"%type,
  n_group_name "isat_resource"."group_name"%type
)
IS
BEGIN
  INSERT INTO "isat_resource"("type", "code", "value", "group_name") VALUES (n_type, n_code, n_value, n_group_name);
EXCEPTION 
  WHEN DUP_VAL_ON_INDEX THEN
    DBMS_OUTPUT.PUT_LINE(n_type||' alredy inserted !');
END isat_resource_insert;
/

--- -------------------------------------------------------------------------------------
--- Create Trigger to INSERT "isat_resource" after INSERT "isat_document_transaction"
--- -------------------------------------------------------------------------------------

CREATE OR REPLACE PROCEDURE isat_resource_doc_check
(
  n_group_name "isat_resource"."group_name"%type,
  n_document_type "isat_resource"."code"%type,
  n_customer_name "isat_resource"."code"%type,
  n_partner_name "isat_resource"."code"%type
) 
AS
   resource_type_doc VARCHAR2(10) := 'Doc Type';
   resource_type_customer VARCHAR2(15) := 'Customer Name';
   resource_type_partner VARCHAR2(15) := 'Partner Name';
   resource_count  NUMBER(10);
BEGIN 
  SELECT COUNT(*) INTO resource_count FROM "isat_resource" t WHERE t."type"=resource_type_doc AND t."code"=n_document_type AND t."group_name"=n_group_name;
  
  IF resource_count = 0 THEN
    isat_resource_insert(resource_type_doc, n_document_type, n_document_type, n_group_name);
  ELSE
    DBMS_OUTPUT.PUT_LINE('resource is existed');
  END IF;

  SELECT COUNT(*) INTO resource_count FROM "isat_resource" t WHERE t."type"=resource_type_customer AND t."code"=n_customer_name AND t."group_name"=n_group_name;
  
  IF resource_count = 0 THEN
    isat_resource_insert(resource_type_customer, n_customer_name, n_customer_name, n_group_name);
  ELSE
    DBMS_OUTPUT.PUT_LINE('resource is existed');
  END IF;

  SELECT COUNT(*) INTO resource_count FROM "isat_resource" t WHERE t."type"=resource_type_partner AND t."code"=n_partner_name AND t."group_name"=n_group_name;
  
  IF resource_count = 0 THEN
    isat_resource_insert(resource_type_partner, n_partner_name, n_partner_name, n_group_name);
  ELSE
    DBMS_OUTPUT.PUT_LINE('resource is existed');
  END IF;
    
END isat_resource_doc_check;
/

CREATE OR REPLACE TRIGGER doc_trans_after_insert_CR
  AFTER INSERT ON "isat_document_transaction"
  FOR EACH ROW
  WHEN (new."group_name" IS NOT NULL)
  CALL isat_resource_doc_check(:new."group_name",:new."document_type",:new."customer_name",:new."partner_name")
/

--- ------------------------------------------------------------------------------------
--- Create Trigger to INSERT "isat_resource" after INSERT "isat_process_transaction"
--- ------------------------------------------------------------------------------------

CREATE OR REPLACE PROCEDURE isat_resource_proc_check
(
  n_group_name "isat_resource"."group_name"%type,
  n_pip_name "isat_resource"."code"%type
) 
AS
   resource_type VARCHAR2(10) := 'PIP Name';
   resource_count  NUMBER(10);
BEGIN 
  SELECT COUNT(*) INTO resource_count FROM "isat_resource" t WHERE t."type"=resource_type AND t."code"=n_pip_name AND t."group_name"=n_group_name;
  
  IF resource_count = 0 THEN
  isat_resource_insert(resource_type,n_pip_name,n_pip_name,n_group_name);  
  ELSE
    DBMS_OUTPUT.PUT_LINE('resource is existed');
  END IF;
    
END isat_resource_proc_check;
/


CREATE OR REPLACE TRIGGER proc_trans_after_insert
  AFTER INSERT OR UPDATE ON "isat_process_transaction"
  FOR EACH ROW
  WHEN (new."group_name" IS NOT NULL)
  CALL isat_resource_proc_check(:new."group_name",:new."pip_name")
/

--- -----------------------------------------------------------------------------
---  Update isat_document_transaction.doc_time_received after insert new record
---  in isat_document_transaction
--- -----------------------------------------------------------------------------
CREATE OR REPLACE PROCEDURE doc_recv_date_after_idt
(
  curr_rowid ROWID,
  n_tracing_id "isat_document_transaction"."tracing_id"%type
) AS
  doc_recv_time "isat_document_transaction"."doc_time_received"%type;
  event_time "isat_document_transaction"."doc_time_received"%type;
  CURSOR curs IS 
    SELECT "doc_time_received" FROM "isat_document_transaction" 
      WHERE "tracing_id" = n_tracing_id AND "direction" = 'Inbound' AND ROWID = curr_rowid
    FOR UPDATE;
  CURSOR curs_sel IS
    SELECT "event_occur_time" FROM "isat_trace_event_info" 
      WHERE "tracing_id" = n_tracing_id AND "event_name" = 'Document Received';
BEGIN 
  OPEN curs;
  LOOP
    FETCH curs INTO doc_recv_time;
    EXIT WHEN curs%NOTFOUND;
    
    OPEN curs_sel;
      FETCH curs_sel INTO event_time;
      EXIT WHEN curs_sel%NOTFOUND;
      IF event_time IS NOT NULL THEN
        UPDATE "isat_document_transaction" SET "doc_time_received" = event_time 
          WHERE CURRENT OF curs;
      END IF;
  END LOOP;
END doc_recv_date_after_idt;
/

--- -----------------------------------------------------------------------------
---  Update isat_document_transaction.doc_time_sent after insert new record
---  in isat_document_transaction
--- -----------------------------------------------------------------------------
CREATE OR REPLACE PROCEDURE doc_sent_date_after_idt
(
  curr_rowid ROWID,
  n_tracing_id "isat_document_transaction"."tracing_id"%type,
  n_msg_id "isat_document_transaction"."message_id"%type
) AS
  doc_sent_time "isat_document_transaction"."doc_time_received"%type;
  event_time "isat_document_transaction"."doc_time_sent"%type;
  CURSOR curs IS 
    SELECT "doc_time_sent" FROM "isat_document_transaction" 
      WHERE "tracing_id" = n_tracing_id AND "message_id" = n_msg_id AND ROWID = curr_rowid
    FOR UPDATE;
  CURSOR curs_sel IS
    SELECT "event_occur_time" FROM "isat_trace_event_info" 
      WHERE "tracing_id" = n_tracing_id AND "message_id" = n_msg_id 
        AND "event_name" = 'Document Delivery' AND "status" = 'OK';
BEGIN 
  OPEN curs;
  LOOP
    FETCH curs INTO doc_sent_time;
    EXIT WHEN curs%NOTFOUND;
    
    OPEN curs_sel;
      FETCH curs_sel INTO event_time;
      EXIT WHEN curs_sel%NOTFOUND;
      IF event_time IS NOT NULL THEN
        UPDATE "isat_document_transaction" SET "doc_time_sent" = event_time 
          WHERE CURRENT OF curs;
      END IF;
  END LOOP;
END doc_sent_date_after_idt;
/

--- -----------------------------------------------------------------------------
---  Creates 2 arrays, one for storing ROWID, the other keep empty for reseting 
---  purposes
--- -----------------------------------------------------------------------------
CREATE OR REPLACE PACKAGE state_pkg
AS
  TYPE ridArray IS TABLE OF ROWID INDEX BY BINARY_INTEGER;
  newRows ridArray;
  empty   ridArray;
END;
/

--- -----------------------------------------------------------------------------
---  Reset the ROWID array to ensure that the package state is cleared
--- -----------------------------------------------------------------------------
CREATE OR REPLACE TRIGGER doc_trans_before_insert
  BEFORE INSERT ON "isat_document_transaction" 
BEGIN
  state_pkg.newRows := state_pkg.empty;
END doc_trans_before_insert;
/

--- -----------------------------------------------------------------------------
---  Captures the ROWID of the affected row and saves it in the newRows array
--- -----------------------------------------------------------------------------
CREATE OR REPLACE TRIGGER doc_trans_after_insert_fer
  AFTER INSERT ON "isat_document_transaction" 
  REFERENCING NEW AS NEW OLD AS OLD
  FOR EACH ROW
BEGIN
  state_pkg.newRows( state_pkg.newRows.count+1 ) := :new.rowid;
END doc_trans_after_insert_fer;
/


--- -----------------------------------------------------------------------------
---  Trigger procedure to update doc_date AFTER insert new record in
---  isat_document_trasaction
--- -----------------------------------------------------------------------------
CREATE OR REPLACE TRIGGER doc_trans_after_insert
  AFTER INSERT ON "isat_document_transaction" 
DECLARE
  vMsg VARCHAR2(100) := 'Statement Level Trigger Fired';
  doc_dir "isat_document_transaction"."direction"%TYPE;
  trac_id "isat_document_transaction"."tracing_id"%TYPE;
  msg_id "isat_document_transaction"."message_id"%TYPE;
  time_lock "isat_resource"."value"%type;
BEGIN
  FOR i in 1 .. state_pkg.newRows.count
  LOOP
    SELECT "direction","tracing_id","message_id" INTO doc_dir,trac_id,msg_id 
      FROM "isat_document_transaction" WHERE ROWID = state_pkg.newRows(i);

    IF doc_dir = 'Inbound' THEN
      SELECT "value" INTO time_lock 
        FROM "isat_resource" WHERE "type"='lock' AND "code"='timelock'
        FOR UPDATE;
      doc_recv_date_after_idt(state_pkg.newRows(i),trac_id);
    ELSIF doc_dir = 'Outbound' THEN
      SELECT "value" INTO time_lock 
        FROM "isat_resource" WHERE "type"='lock' AND "code"='timelock'
        FOR UPDATE;
      doc_sent_date_after_idt(state_pkg.newRows(i),trac_id,msg_id);
    ELSE 
      dbms_output.put_line(vMsg || ' Invalid document direction');
    END IF;
  END LOOP;
--EXCEPTION
  --WHEN OTHERS THEN
  --  DBMS_OUTPUT.PUT_LINE('Error while INSERT isat_document_transaction, direction='||doc_dir);
  --  ROLLBACK;
END doc_trans_after_insert;
/

--- -----------------------------------------------------------------------------
---  Update isat_document_transaction.doc_time_received after insert new record
---  in isat_trace_event_info
--- -----------------------------------------------------------------------------
CREATE OR REPLACE PROCEDURE doc_recv_date_after_itei
(
  n_tracing_id "isat_trace_event_info"."tracing_id"%type,
  n_event_time "isat_trace_event_info"."event_occur_time"%type
) AS
  doc_recv_time "isat_document_transaction"."doc_time_received"%type;
  CURSOR curs IS 
    SELECT "doc_time_received" FROM "isat_document_transaction" 
      WHERE "tracing_id" = n_tracing_id AND "direction" = 'Inbound'
    FOR UPDATE;
BEGIN 
  OPEN curs;
  LOOP
    FETCH curs INTO doc_recv_time;
    EXIT WHEN curs%NOTFOUND;
    IF n_event_time IS NOT NULL THEN
      UPDATE "isat_document_transaction" SET "doc_time_received" = n_event_time WHERE CURRENT OF curs;
    END IF;
  END LOOP;
END doc_recv_date_after_itei;
/

--- -----------------------------------------------------------------------------
---  Update isat_document_transaction.doc_time_sent after insert new record
---  in isat_trace_event_info
--- -----------------------------------------------------------------------------
CREATE OR REPLACE PROCEDURE doc_sent_date_after_itei
(
  n_tracing_id "isat_trace_event_info"."tracing_id"%type,
  n_msg_id "isat_trace_event_info"."message_id"%type,
  n_event_time "isat_trace_event_info"."event_occur_time"%type
) AS
  doc_sent_time "isat_document_transaction"."doc_time_received"%type;
  CURSOR curs IS 
    SELECT "doc_time_sent" FROM "isat_document_transaction" 
      WHERE "tracing_id" = n_tracing_id AND "message_id" = n_msg_id
    FOR UPDATE;
BEGIN 
  OPEN curs;
  LOOP
    FETCH curs INTO doc_sent_time;    
    EXIT WHEN curs%NOTFOUND;
    IF n_event_time IS NOT NULL THEN
      UPDATE "isat_document_transaction" SET "doc_time_sent" = n_event_time WHERE CURRENT OF curs;
    END IF;
  END LOOP;
END doc_sent_date_after_itei;
/

--- -----------------------------------------------------------------------------
---  Trigger procedure to update doc_date AFTER insert new record in
---  isat_trace_event_info
--- -----------------------------------------------------------------------------
CREATE OR REPLACE TRIGGER trace_event_info_after_insert
  AFTER INSERT ON "isat_trace_event_info" 
  REFERENCING NEW AS NEW OLD AS OLD
  FOR EACH ROW
DECLARE
  vMsg VARCHAR2(100) := 'Statement Level Trigger Fired';
  time_lock "isat_resource"."value"%type;
BEGIN
  IF :new."event_name" = 'Document Received' THEN
    SELECT "value" INTO time_lock 
      FROM "isat_resource" WHERE "type"='lock' AND "code"='timelock'
      FOR UPDATE;
    doc_recv_date_after_itei(:new."tracing_id",:new."event_occur_time");
  ELSIF :new."event_name" = 'Document Delivery' AND :new."status" = 'OK' THEN
    SELECT "value" INTO time_lock
      FROM "isat_resource" WHERE "type"='lock' AND "code"='timelock'
      FOR UPDATE;
    doc_sent_date_after_itei(:new."tracing_id",:new."message_id",:new."event_occur_time");
  ELSIF :new."event_name" IS NULL THEN
    dbms_output.put_line(vMsg || ' Null event_name');
  END IF;
--EXCEPTION
--  WHEN OTHERS THEN
--    DBMS_OUTPUT.PUT_LINE('Error while INSERT isat_trace_event_info, event_name='||:new."event_name");
--    ROLLBACK;
END trace_event_info_after_insert;
/
--- 20070523v4.1.1

---------------------------------------------------------------------------

---
--- Table structure for table 'isat_biz_entity_group_mapping'
---
CREATE TABLE "isat_biz_entity_group_mapping" (
  "uid" VARCHAR2(36),
  "be_id" VARCHAR2(20) NOT NULL ENABLE,
  "group_name" VARCHAR2(50) NOT NULL ENABLE,
  "version" NUMBER(10),
  PRIMARY KEY("uid"),
  CONSTRAINT ISAT_BIZ_ENTITY_CON1 UNIQUE("be_id"),
  CONSTRAINT ISAT_BIZ_ENTITY_CON2 UNIQUE("group_name")
);


---------------------------------------------------------------------------

---
--- Table structure for table 'isat_audit_trail_data'
---
CREATE TABLE "isat_audit_trail_data" (
  "uid" VARCHAR2(36) NOT NULL, 
  "object" BLOB NOT NULL, 
  "version" NUMBER(10), 
  "attempt_count" NUMBER(10) DEFAULT 0 NOT NULL, 
  "failed_reason" CLOB, 
  "last_modified_date" TIMESTAMP(3) WITH TIME ZONE,
  PRIMARY KEY("uid")
);

CREATE INDEX audit_trail_data_idx1 ON "isat_audit_trail_data" ("last_modified_date","attempt_count");

---------------------------------------------------------------------------

---
--- Table structure for table 'isat_archive_scheduler'
---
CREATE TABLE "isat_archive_scheduler" (
  "uid" VARCHAR(36) NOT NULL, 
  "effective_start_time" TIMESTAMP(3) WITH TIME ZONE NOT NULL ENABLE, 
  "frequency" VARCHAR(15) NOT NULL, 
  "is_active" NUMBER(1) DEFAULT 1 NOT NULL ENABLE, 
  "last_run_time" TIMESTAMP(3) WITH TIME ZONE, 
  "next_run_time" TIMESTAMP(3) WITH TIME ZONE, 
  "archive_record_older" NUMBER(5), 
  "customer_list" VARCHAR(2000), 
  "archive_every" NUMBER(5), 
  "is_success_invoke" NUMBER(1) DEFAULT 1 NOT NULL ENABLE, 
  "archive_start_date" TIMESTAMP(3) WITH TIME ZONE, 
  "archive_end_date" TIMESTAMP(3) WITH TIME ZONE, 
  "timezone_id" VARCHAR(50),
  "is_archive_orphan_record" NUMBER(1) DEFAULT 0 NOT NULL ENABLE,
  "version" NUMBER(10), 
  PRIMARY KEY("uid")
);

CREATE INDEX archive_scheduler_idx1 ON "isat_archive_scheduler" ("next_run_time","is_active");

---------------------------------------------------------------------------

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


---------------------------------------------------------------------------

---
--- Table structure for table 'rpt_schedule'
---
CREATE TABLE "rpt_schedule" (
    "uid" VARCHAR2(36) NOT NULL ENABLE,
    "version" NUMBER(10),
    "schedule_id" NUMBER(10) NOT NULL ENABLE,
    "effective_start_date" DATE NOT NULL ENABLE,
    "email_list" VARCHAR2(1000),
    "customer_list" VARCHAR2(1000),
    "report_location" VARCHAR2(100),
    "group_name" VARCHAR2(50) NOT NULL ENABLE,
    "report_type" VARCHAR2(80) NOT NULL ENABLE,
    "run_time" VARCHAR2(17) NOT NULL ENABLE,
    "next_run_date_time" TIMESTAMP(3) WITH TIME ZONE,
    "frequency" VARCHAR2(7) NOT NULL ENABLE,
    "report_content" BLOB,
    "next_start_date_time" TIMESTAMP(3) WITH TIME ZONE,
    "next_end_date_time" TIMESTAMP(3) WITH TIME ZONE,
    "report_format" VARCHAR2(5) NOT NULL ENABLE,
    "username" VARCHAR2(80),
    "created_date_time" TIMESTAMP(3) WITH TIME ZONE,
    "modified_date_time" TIMESTAMP(3) WITH TIME ZONE,
    PRIMARY KEY ("uid")
);

CREATE SEQUENCE RPT_SCHEDULE_SEQ 
START WITH 1 
INCREMENT BY 1 
NOMAXVALUE; 

CREATE TRIGGER RPT_SCHEDULE_TRIGGER 
BEFORE INSERT ON "rpt_schedule"
REFERENCING NEW AS NEW OLD AS OLD 
FOR EACH ROW
BEGIN
     SELECT RPT_SCHEDULE_SEQ.NEXTVAL INTO :NEW."schedule_id" FROM DUAL;
END;
/

---------------------------------------------------------------------------

---
--- Table structure for table 'rpt_report_type'
---
CREATE TABLE "rpt_report_type" (
    "uid" VARCHAR2(36) NOT NULL ENABLE,
    "version" NUMBER(10) NOT NULL ENABLE,
    "report_type" VARCHAR2(80) NOT NULL ENABLE,
    "template_name" VARCHAR2(100),
    "datasource_type" VARCHAR2(4),
    PRIMARY KEY ("uid")
);


---------------------------------------------------------------------------

---
--- Table structure for table 'rpt_report'
---
CREATE TABLE "rpt_report" (
    "uid" VARCHAR2(36) NOT NULL ENABLE,
    "version" NUMBER(10) NOT NULL ENABLE,
    "report_type" VARCHAR2(80) NOT NULL ENABLE,
    "frequency" VARCHAR2(7) NOT NULL ENABLE,
    "status" VARCHAR2(7) NOT NULL ENABLE,
    "customer_list" VARCHAR2(1000),
    "start_date_time" TIMESTAMP(3) WITH TIME ZONE NOT NULL ENABLE,
    "end_date_time" TIMESTAMP(3) WITH TIME ZONE NOT NULL ENABLE,
    "report_content" BLOB,
    "archive_duration" NUMBER(5) DEFAULT 30 NOT NULL ENABLE,
    "email_list" VARCHAR2(1000),
    "schedule_doc_id" VARCHAR2(36),
    "report_location" VARCHAR2(100),
    "group_name" VARCHAR2(50) NOT NULL ENABLE,
    "last_run_date_time" TIMESTAMP(3) WITH TIME ZONE,
    "report_format" VARCHAR2(5) NOT NULL ENABLE,
    "username" VARCHAR2(80) NOT NULL ENABLE,
    "created_date_time" TIMESTAMP(3) WITH TIME ZONE,
    PRIMARY KEY ("uid")
);


---------------------------------------------------------------------------

---
--- Table structure for table 'adm_groups'
---
CREATE TABLE "adm_groups" (
    "group_name" VARCHAR2(50) NOT NULL ENABLE,
    "groupid" NUMBER(19) NOT NULL ENABLE,
    "be_id" VARCHAR2(10),
  PRIMARY KEY("groupid")
);

CREATE SEQUENCE ADM_GROUPS_SEQ 
START WITH 1 
INCREMENT BY 1 
NOMAXVALUE; 

CREATE TRIGGER ADM_GROUPS_TRIGGER 
BEFORE INSERT ON "adm_groups"
REFERENCING NEW AS NEW OLD AS OLD 
FOR EACH ROW
BEGIN
     SELECT ADM_GROUPS_SEQ.NEXTVAL INTO :NEW."groupid" FROM DUAL;
END;
/

---------------------------------------------------------------------------

---
--- Table structure for table 'adm_setting'
---
CREATE TABLE "adm_setting" (
    "set_id" NUMBER(10) NOT NULL ENABLE,
    "idle_setting" VARCHAR2(30) NOT NULL ENABLE,
  PRIMARY KEY("set_id")
);

---------------------------------------------------------------------------

---
--- Table structure for table 'adm_user_level'
---
CREATE TABLE "adm_user_level" (
    "level_name" VARCHAR2(25) NOT NULL ENABLE,
    "level_id" NUMBER(10) NOT NULL ENABLE,
  PRIMARY KEY("level_id")
);

---------------------------------------------------------------------------

---
--- Table structure for table 'adm_users'
---
CREATE TABLE "adm_users" (
    "first_name" VARCHAR2(25),
    "last_name" VARCHAR2(25),
    "email_address" VARCHAR2(50),
    "username" VARCHAR2(25),
    "password" VARCHAR2(100),
    "info" VARCHAR2(500),
    "signup_date" TIMESTAMP(3) WITH TIME ZONE,
    "user_right" VARCHAR2(30),
    "user_group" VARCHAR2(30),
    "last_login" TIMESTAMP(3) WITH TIME ZONE,
    "last_logout" TIMESTAMP(3) WITH TIME ZONE,
    "last_active" NUMBER(10),
    "status" VARCHAR2(8),
    "activated" VARCHAR2(10),
    "userid" NUMBER(19) NOT NULL ENABLE,
  PRIMARY KEY("userid")
); 

---------------------------------------------------------------------------

---
--- Table structure for table 'adm_log'
---
CREATE TABLE "adm_log" (
    "logid" NUMBER(5) NOT NULL ENABLE,
    "date_time" DATE NOT NULL ENABLE,
    "group" VARCHAR2(20),
    "username" VARCHAR2(50),
    "log_type" VARCHAR2(20),
    "log_content" VARCHAR2(100),
    "user_level" VARCHAR2(20),
  PRIMARY KEY("logid")
); 

CREATE SEQUENCE ADM_LOG_SEQ 
START WITH 1 
INCREMENT BY 1 
NOMAXVALUE; 

CREATE TRIGGER ADM_LOG_TRIGGER 
BEFORE INSERT ON "adm_log"
REFERENCING NEW AS NEW OLD AS OLD 
FOR EACH ROW
BEGIN
     SELECT ADM_LOG_SEQ.NEXTVAL INTO :NEW."logid" FROM DUAL;
END;
/

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

COMMIT;

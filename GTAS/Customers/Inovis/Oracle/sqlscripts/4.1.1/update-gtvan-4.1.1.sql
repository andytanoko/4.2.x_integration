connect GTVAN/gridnode;

-- NSL20070412: Not required anymore. ISHC directly invoke save via EJB
DELETE FROM "config_props" WHERE "category"='ishb.tx.out';

-- NSL20070419: GNDB00028303
DELETE FROM "config_props" WHERE "property_key"='Content-type';
INSERT INTO "config_props" ("category", "property_key", "value") VALUES ('http.doc.headers.default', 'Content-type', 'application/xml');

-- NSL20070419: GNDB00028302
-- HTTPBC: These properties are required to enable outbound HTTPS connection
DELETE FROM "config_props" WHERE "category"='http.proxy.auth';
INSERT INTO "config_props" ("category", "property_key", "value") VALUES ('http.proxy.auth', 'auth.server.enabled','true');
INSERT INTO "config_props" ("category", "property_key", "value") VALUES ('http.proxy.auth', 'cert.truststore.loc','../../data/GNapps/gtas/data/keystore/cacerts');
INSERT INTO "config_props" ("category", "property_key", "value") VALUES ('http.proxy.auth', 'cert.truststore.pass','changeit');
INSERT INTO "config_props" ("category", "property_key", "value") VALUES ('http.proxy.auth', 'cert.keystore.loc','../../data/GNapps/gtas/data/keystore/keystore');
INSERT INTO "config_props" ("category", "property_key", "value") VALUES ('http.proxy.auth', 'cert.keystore.pass','changeit');


-- TWX20070514: Performance Tuning

ALTER TABLE "isat_business_document" ADD "is_required_unzip" NUMBER(1) DEFAULT 0 NOT NULL ENABLE;

--- ---------------------------------------------------------
--- Create new sequence for generating UID for isat_resource
--- ---------------------------------------------------------

CREATE INDEX resource_idx1 ON "isat_resource" ("group_name","type","code");

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

--- -------------------------------------------------------
--- Add new column "msg_type" to "isat_trace_event_header"
--- -------------------------------------------------------
ALTER TABLE "isat_trace_event_info" ADD ("msg_type" VARCHAR2(10));

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



-- TXW20060514: No need scheduler to process audit_trail_info anymore
DELETE FROM "schedule" WHERE "target"='gtvan.isat:service=TrailInfoService';

-- TWX20070514
DELETE FROM "isat_resource" WHERE "type"='lock';
-- This time lock is used in stored procedures that perform update on document transaction's doc_time_sent and doc_time_received
INSERT INTO "isat_resource"("type", "code", "value", "group_name") VALUES('lock','timelock','timelock','');
-- The process lock is used to avoid the concorrent creation of the same process transaction record
INSERT INTO "isat_resource"("type", "code", "value", "group_name") VALUES('lock','processlock','processlock','');


-- TWX20070601 For archive by customer

ALTER TABLE "isat_archive_scheduler" 
ADD "is_archive_orphan_record" NUMBER(1) DEFAULT 0 NOT NULL ENABLE;

DELETE FROM "isat_resource" WHERE "code"='archiveSummLock';
INSERT INTO "isat_resource"("type", "code", "value", "group_name") VALUES('lock','archiveSummLock', 'archiveSummLock', '');

-- update archive zip
UPDATE "config_props" set "value"=100 where "category"='ISAT' and "property_key"='audit.archive.total.process.in.zip';

DELETE FROM "config_props" WHERE "category"='isat.archive.singleton';
-- The MBean name that responsible to delegate the archive request to backend
INSERT INTO "config_props" ("category", "property_key", "value") VALUES ('isat.archive.singleton', 'mbean.name', 'gtvan.isat:service=ArchiveSingleton');

-- RMI adaptor for looking up archive singleton mbean
INSERT INTO "config_props" ("category", "property_key", "value") VALUES ('isat.archive.singleton', 'rmi.adaptor', 'jmx/invoker/SingletonRMIAdaptor');

-- HA JNDI Lookup
INSERT INTO "config_props" ("category", "property_key", "value") VALUES ('isat.jndi.lookup', 'java.naming.factory.initial', 'org.jnp.interfaces.NamingContextFactory');
INSERT INTO "config_props" ("category", "property_key", "value") VALUES ('isat.jndi.lookup', 'java.naming.provider.url', 'localhost:1100');
INSERT INTO "config_props" ("category", "property_key", "value") VALUES ('isat.jndi.lookup', 'java.naming.factory.url.pkgs', 'org.jboss.naming:org.jnp.interfaces');

-- Archive scheduler for keep track the archive status
DELETE FROM "schedule" WHERE "target"='gtvan.isat:service=ArchiveService';
INSERT INTO "schedule"("target", "method_name", "method_signature", "start_date", "period", "repetitions") VALUES('gtvan.isat:service=ArchiveService', 'checkArchiveStatus', '', 'NOW', 60000, -1);

-- ISAT GENERAL ARCHIVE STARTUP ALERT FOR GT & TM
DELETE FROM "config_props" WHERE "category"='isat.gttm.archive.started';
INSERT INTO "config_props" ("category", "property_key", "value") VALUES ('isat.gttm.archive.started', 'recipients', 'default');
INSERT INTO "config_props" ("category", "property_key", "value") VALUES ('isat.gttm.archive.started', 'delivery.mode', '1');
INSERT INTO "config_props" ("category", "property_key", "value") VALUES ('isat.gttm.archive.started', 'subject', 'Archive Started');
INSERT INTO "config_props" ("category", "property_key", "value") VALUES ('isat.gttm.archive.started', 'message', 'Dear user,'||chr(13)||chr(10)||chr(13)||chr(10)||'Archive Process Transaction has started at {0}.'||chr(13)||chr(10)||chr(13)||chr(10)||'Archive Criteria'||chr(13)||chr(10)||'-------------------------------------------------'||chr(13)||chr(10)||'From Date/Time:   {1}'||chr(13)||chr(10)||'To Date/Time:     {2}'||chr(13)||chr(10)||'Is Orphan Record: {3}'||chr(13)||chr(10)||'Customer List:    {4}'||chr(13)||chr(10)||chr(13)||chr(10)||'Archive ID: {5}'||chr(13)||chr(10)||chr(13)||chr(10)||'Regards,'||chr(13)||chr(10)||'GTVAN System');

DELETE FROM "config_props" WHERE "category"='isat.gttm.archive.completed';
INSERT INTO "config_props" ("category", "property_key", "value") VALUES ('isat.gttm.archive.completed', 'recipients', 'default');
INSERT INTO "config_props" ("category", "property_key", "value") VALUES ('isat.gttm.archive.completed', 'delivery.mode', '1');
INSERT INTO "config_props" ("category", "property_key", "value") VALUES ('isat.gttm.archive.completed', 'subject', 'Archive Completed');
INSERT INTO "config_props" ("category", "property_key", "value") VALUES ('isat.gttm.archive.completed', 'message', 'Dear user,'||chr(13)||chr(10)||chr(13)||chr(10)||'Archive has completed at {0}.'||chr(13)||chr(10)||chr(13)||chr(10)||'Archive Criteria'||chr(13)||chr(10)||'-------------------------------------------------'||chr(13)||chr(10)||'From Date/Time:   {1}'||chr(13)||chr(10)||'To Date/Time:     {2}'||chr(13)||chr(10)||'Is Orphan Record: {3}'||chr(13)||chr(10)||'Customer List:    {4}'||chr(13)||chr(10)||chr(13)||chr(10)||'Archive ID:                                   {5}'||chr(13)||chr(10)||'Archive Summary File:                         {6}'||chr(13)||chr(10)||chr(13)||chr(10)||'Regards,'||chr(13)||chr(10)||'GTVAN System');

DELETE FROM "config_props" WHERE "category"='isat.gttm.archive.failed';
INSERT INTO "config_props" ("category", "property_key", "value") VALUES ('isat.gttm.archive.failed', 'recipients', 'default');
INSERT INTO "config_props" ("category", "property_key", "value") VALUES ('isat.gttm.archive.failed', 'delivery.mode', '1');
INSERT INTO "config_props" ("category", "property_key", "value") VALUES ('isat.gttm.archive.failed', 'subject', 'Archive Failed');
INSERT INTO "config_props" ("category", "property_key", "value") VALUES ('isat.gttm.archive.failed', 'message', 'Dear user,'||chr(13)||chr(10)||chr(13)||chr(10)||'Archive has failed at {0}.'||chr(13)||chr(10)||chr(13)||chr(10)||'Archive Criteria'||chr(13)||chr(10)||'-------------------------------------------------'||chr(13)||chr(10)||'From Date/Time:   {1}'||chr(13)||chr(10)||'To Date/Time:     {2}'||chr(13)||chr(10)||'Is Orphan Record: {3}'||chr(13)||chr(10)||'Customer List:    {4}'||chr(13)||chr(10)||chr(13)||chr(10)||'Archive ID:                                   {5}'||chr(13)||chr(10)||'Archive Summary File:                         {6}'||chr(13)||chr(10)||chr(13)||chr(10)||'Please verify the problem and restart the operation. (Note: some process transaction may have already been archived. It is safe to run the archival again.)'||chr(13)||chr(10)||chr(13)||chr(10)||'Exception: {7}'||chr(13)||chr(10)||'Trace:'||chr(13)||chr(10)||'-------'||chr(13)||chr(10)||'{8}'||chr(13)||chr(10)||chr(13)||chr(10)||'Regards,'||chr(13)||chr(10)||'GTVAN System');

-- UPDATE for archive startup alert within the node
DELETE FROM "config_props" WHERE "category"='isat.archive.started' AND "property_key"='message';
INSERT INTO "config_props"("category", "property_key", "value") VALUES ('isat.archive.started', 'message', 'Dear user,'||chr(13)||chr(10)||chr(13)||chr(10)||'Archive Process Transaction has started at {0}.'||chr(13)||chr(10)||chr(13)||chr(10)||'Archive Criteria'||chr(13)||chr(10)||'-------------------------------------------------'||chr(13)||chr(10)||'From Date/Time:   {1}'||chr(13)||chr(10)||'To Date/Time:     {2}'||chr(13)||chr(10)||'Is Orphan Record: {3}'||chr(13)||chr(10)||'Customer List:    {4}'||chr(13)||chr(10)||chr(13)||chr(10)||'Archive ID:                                   {5}'||chr(13)||chr(10)||chr(13)||chr(10)||'Regards,'||chr(13)||chr(10)||'GTVAN System');

DELETE FROM "config_props" WHERE "category"='isat.archive.completed' AND "property_key"='message';
INSERT INTO "config_props" ("category", "property_key", "value") VALUES ('isat.archive.completed', 'message', 'Dear user,'||chr(13)||chr(10)||chr(13)||chr(10)||'Archive Process Transaction has completed at {0}.'||chr(13)||chr(10)||chr(13)||chr(10)||'Archive Criteria'||chr(13)||chr(10)||'-------------------------------------------------'||chr(13)||chr(10)||'From Date/Time:   {1}'||chr(13)||chr(10)||'To Date/Time:     {2}'||chr(13)||chr(10)||'Is Orphan Record: {3}'||chr(13)||chr(10)||'Customer List:    {4}'||chr(13)||chr(10)||chr(13)||chr(10)||'Number of Process Transaction archived:       {5}'||chr(13)||chr(10)||'Number of Incomplete Process Trans archived:  {6}'||chr(13)||chr(10)||'Number of Incomplete Document archived:       {7}'||chr(13)||chr(10)||chr(13)||chr(10)||'Archive ID:                                   {8}'||chr(13)||chr(10)||chr(13)||chr(10)||'Archive Summary File:                         {9}'||chr(13)||chr(10)||chr(13)||chr(10)||'Regards,'||chr(13)||chr(10)||'GTVAN System');

DELETE FROM "config_props" WHERE "category"='isat.archive.failed' AND "property_key"='message';
INSERT INTO "config_props" ("category", "property_key", "value") VALUES ('isat.archive.failed', 'message', 'Dear user,'||chr(13)||chr(10)||chr(13)||chr(10)||'Archive Process Transaction has failed at {0}.'||chr(13)||chr(10)||chr(13)||chr(10)||'Archive Criteria'||chr(13)||chr(10)||'-------------------------------------------------'||chr(13)||chr(10)||'From Date/Time:   {1}'||chr(13)||chr(10)||'To Date/Time:     {2}'||chr(13)||chr(10)||'Is Orphan Record: {3}'||chr(13)||chr(10)||'Customer List:    {4}'||chr(13)||chr(10)||chr(13)||chr(10)||'Number of Process Transaction archived:       {5}'||chr(13)||chr(10)||'Number of Incomplete Process Trans archived:  {6}'||chr(13)||chr(10)||'Number of Incomplete Document archived:       {7}'||chr(13)||chr(10)||chr(13)||chr(10)||'Archive ID:                                   {8}'||chr(13)||chr(10)||chr(13)||chr(10)||'Archive Summary File:                         {9}'||chr(13)||chr(10)||chr(13)||chr(10)||'Please verify the problem and restart the operation. (Note: some process transaction may have already been archived. It is safe to run the archival again.)'||chr(13)||chr(10)||chr(13)||chr(10)||'Exception: {10}'||chr(13)||chr(10)||'Trace:'||chr(13)||chr(10)||'-------'||chr(13)||chr(10)||'{11}'||chr(13)||chr(10)||chr(13)||chr(10)||'Regards,'||chr(13)||chr(10)||'GTVAN System');


-- NSL20070620: Fix Certificate Status Report
UPDATE "rpt_report_type" SET "datasource_type"=2 WHERE "report_type"='Certificate Status Report';


COMMIT;
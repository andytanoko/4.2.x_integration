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


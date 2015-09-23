SET client_encoding = 'UTF8';
SET check_function_bodies = false;
SET client_min_messages = warning;
set search_path='gtvan';

-- create new procedural language 
DROP LANGUAGE IF EXISTS plpgsql CASCADE;
CREATE LANGUAGE plpgsql;

CREATE OR REPLACE FUNCTION update_isat_trace_event_header
(
  n_last_event_status "isat_trace_event_header"."last_event_status"%type,
  n_occur_time "isat_trace_event_header"."last_event_occur_time"%type,
  n_tracing_id "isat_trace_event_header"."tracing_id"%type,
  n_msg_type "isat_trace_event_header"."msg_type"%type,
  n_group_name "isat_trace_event_header"."group_name"%type
  
) RETURNS void AS $$

DECLARE
  s_error_count smallint;
  s_occur_time timestamp with time zone;
  s_group_name character varying;
  s_msg_type character varying;

BEGIN

  SELECT "last_event_occur_time", "error_count", "group_name", "msg_type" INTO s_occur_time, s_error_count, s_group_name, s_msg_type 
    FROM gtvan."isat_trace_event_header" WHERE "tracing_id" = n_tracing_id
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
       UPDATE gtvan."isat_trace_event_header" SET "last_event_status"=n_last_event_status, 
                                         "error_count"=s_error_count, 
					 "last_event_occur_time"=n_occur_time,
					 "group_name"=s_group_name,
					 "msg_type"=s_msg_type
       WHERE "tracing_id"=n_tracing_id; 
  ELSIF n_last_event_status = 'FAILED' THEN
       UPDATE gtvan."isat_trace_event_header" SET "error_count"=s_error_count ,"group_name" = s_group_name, "msg_type" = s_msg_type
       WHERE "tracing_id"=n_tracing_id; 
  ELSIF s_group_name IS NOT NULL OR s_msg_type IS NOT NULL THEN
       UPDATE gtvan."isat_trace_event_header" SET "group_name" = s_group_name, "msg_type" = s_msg_type
       WHERE "tracing_id"=n_tracing_id; 
  END IF;
  
--EXCEPTION 
--  WHEN OTHERS THEN
--    RAISE DEBUG 'Failed to update isat_trace_event_header, tracing_id:%',n_tracing_id;
    
END;
$$ LANGUAGE plpgsql;
ALTER FUNCTION update_isat_trace_event_header(character varying, timestamp with time zone, character varying, character varying, character varying) OWNER TO gtvan;


--- ---------------------------------------------------------------------
--- Create FUNCTION to insert new record in "isat_trace_event_header"
---   - INSERT new record
---   - If EXCEPTION caught for Duplicate value found on INDEX, 
---     call Procedure - update_isat_trace_event_header(...)
--- ---------------------------------------------------------------------
CREATE OR REPLACE FUNCTION insert_isat_trace_event_header
(
  n_msg_type "isat_trace_event_header"."msg_type"%type,
  n_uid "isat_trace_event_header"."uid"%type,
  n_event_occur_time "isat_trace_event_info"."event_occur_time"%type,
  n_status "isat_trace_event_info"."status"%type,
  n_tracing_id "isat_trace_event_info"."tracing_id"%type,
  n_group_name "isat_trace_event_info"."group_name"%type
) RETURNS VOID AS $$
DECLARE
  s_error_count smallint;
BEGIN
  
  IF n_status = 'FAILED' THEN
      s_error_count := 1;
    ELSE
      s_error_count := 0;
    END IF;
    INSERT INTO gtvan."isat_trace_event_header"("uid","last_event_status","error_count","last_event_occur_time","tracing_id","msg_type","group_name","version") 
      VALUES (n_uid,n_status,s_error_count,n_event_occur_time,n_tracing_id,n_msg_type,n_group_name,0);

EXCEPTION WHEN unique_violation THEN
      RAISE DEBUG 'Duplicate data found, tracing_id=%',n_tracing_id;
      PERFORM gtvan.update_isat_trace_event_header(n_status,n_event_occur_time,n_tracing_id,n_msg_type,n_group_name);
END;
$$ LANGUAGE plpgsql;
ALTER FUNCTION insert_isat_trace_event_header(character varying, character varying, timestamp with time zone, character varying, character varying, character varying) OWNER TO gtvan;


--s_error_count "isat_trace_event_header"."error_count"%TYPE;

CREATE OR REPLACE FUNCTION check_isat_trace_event_header
(
  n_msg_type "isat_trace_event_header"."msg_type"%TYPE,
  n_uid "isat_trace_event_header"."uid"%TYPE,
  n_event_occur_time "isat_trace_event_info"."event_occur_time"%TYPE,
  n_status "isat_trace_event_info"."status"%TYPE,
  n_tracing_id "isat_trace_event_info"."tracing_id"%TYPE,
  n_group_name "isat_trace_event_info"."group_name"%TYPE
) RETURNS void AS $$
DECLARE
  s_error_count smallint;
  s_occur_time timestamp with time zone;
BEGIN 

  SELECT "last_event_occur_time", "error_count" INTO s_occur_time, s_error_count 
    FROM gtvan."isat_trace_event_header" WHERE "tracing_id" = n_tracing_id;

  IF NOT FOUND THEN
    RAISE DEBUG 'No Data found, tracing_id=%', n_tracing_id;
    PERFORM gtvan.insert_isat_trace_event_header(n_msg_type,n_uid,n_event_occur_time,n_status,n_tracing_id,n_group_name);
    RETURN;
  END IF;
  
  IF s_occur_time IS NOT NULL THEN    
    PERFORM gtvan.update_isat_trace_event_header(n_status,n_event_occur_time,n_tracing_id,n_msg_type,n_group_name);
	RETURN;
  END IF;

END;
$$ LANGUAGE plpgsql;


---- TRIGGER declaration  for handling event_header_change
CREATE OR REPLACE FUNCTION invoke_check_isat_trace_event_header() RETURNS trigger AS $event_header_change$
    BEGIN
        -- Check that empname and salary are given
        IF NEW."tracing_id" IS NOT NULL THEN
            RAISE DEBUG 'Creating isat trace event header';
			PERFORM gtvan.check_isat_trace_event_header(NEW."msg_type", NEW."uid", NEW."event_occur_time", NEW."status", NEW."tracing_id", NEW."group_name");
			RETURN NEW;
        END IF;
		
		RETURN NULL;
    END;
$event_header_change$ LANGUAGE plpgsql;

DROP TRIGGER IF EXISTS event_header_change on "isat_trace_event_info";
CREATE TRIGGER event_header_change BEFORE INSERT ON "isat_trace_event_info"
    FOR EACH ROW EXECUTE PROCEDURE invoke_check_isat_trace_event_header();

---- End Trigger declaration for handling event_header_change


set search_path='gtvan';


------ 11 Aug ------

--- -----------------------------------------
--- Create Procedure INSERT "isat_resource"
--- -----------------------------------------
CREATE OR REPLACE FUNCTION isat_resource_insert
(
  --n_uid "isat_resource"."uid"%type,
  n_type "isat_resource"."type"%type,
  n_code "isat_resource"."code"%type,
  n_value "isat_resource"."value"%type,
  n_group_name "isat_resource"."group_name"%type
) RETURNS VOID AS $$
BEGIN
  INSERT INTO gtvan."isat_resource"("type", "code", "value", "group_name") VALUES (n_type, n_code, n_value, n_group_name);
EXCEPTION WHEN unique_violation THEN
    RAISE DEBUG '% alredy inserted !',n_type;
END;
$$ LANGUAGE plpgsql;


--- -------------------------------------------------------------------------------------
--- Create Trigger to INSERT "isat_resource" after INSERT "isat_document_transaction"
--- -------------------------------------------------------------------------------------

CREATE OR REPLACE FUNCTION isat_resource_doc_check
(
  --n_uid "isat_resource"."uid"%type,
  n_group_name "isat_resource"."group_name"%type,
  n_document_type "isat_resource"."code"%type,
  n_customer_name "isat_resource"."code"%type,
  n_partner_name "isat_resource"."code"%type
) RETURNS VOID AS $$
DECLARE
   resource_type_doc VARCHAR := 'Doc Type';
   resource_type_customer VARCHAR := 'Customer Name';
   resource_type_partner VARCHAR := 'Partner Name';
   resource_count  DECIMAL;
BEGIN 
  SELECT COUNT(*) INTO resource_count FROM gtvan."isat_resource" t WHERE t."type"=resource_type_doc AND t."code"=n_document_type AND t."group_name"=n_group_name;
  
  IF resource_count = 0 THEN
    PERFORM gtvan.isat_resource_insert(resource_type_doc, n_document_type, n_document_type, n_group_name);
  ELSE
    RAISE DEBUG 'resource is existed';
  END IF;

  SELECT COUNT(*) INTO resource_count FROM gtvan."isat_resource" t WHERE t."type"=resource_type_customer AND t."code"=n_customer_name AND t."group_name"=n_group_name;
  
  IF resource_count = 0 THEN
    PERFORM gtvan.isat_resource_insert(resource_type_customer, n_customer_name, n_customer_name, n_group_name);
  ELSE
    RAISE DEBUG 'resource is existed';
  END IF;

  SELECT COUNT(*) INTO resource_count FROM gtvan."isat_resource" t WHERE t."type"=resource_type_partner AND t."code"=n_partner_name AND t."group_name"=n_group_name;
  
  IF resource_count = 0 THEN
    PERFORM gtvan.isat_resource_insert(resource_type_partner, n_partner_name, n_partner_name, n_group_name);
  ELSE
    RAISE DEBUG 'resource is existed';
  END IF;
END;
$$ LANGUAGE plpgsql;


---- TRIGGER for handling the creation of the isat_resource for document transaction
CREATE OR REPLACE FUNCTION invoke_create_resource_after_insert_doc_trans() RETURNS trigger AS $create_resource_after_insert_doc_trans$
    BEGIN
        IF NEW."group_name" IS NOT NULL THEN
            RAISE DEBUG 'Creating isat resource for doc trans';
			PERFORM gtvan.isat_resource_doc_check(NEW."group_name", NEW."document_type", NEW."customer_name", NEW."partner_name");
			RETURN NEW;
        END IF;
		
		RETURN NULL;
    END;
$create_resource_after_insert_doc_trans$ LANGUAGE plpgsql;

DROP TRIGGER IF EXISTS create_resource_after_insert_doc_trans on "isat_document_transaction";
CREATE TRIGGER create_resource_after_insert_doc_trans AFTER INSERT ON "isat_document_transaction"
    FOR EACH ROW EXECUTE PROCEDURE gtvan.invoke_create_resource_after_insert_doc_trans();



--- ------------------------------------------------------------------------------------
--- Create Trigger to INSERT "isat_resource" after INSERT "isat_process_transaction"
--- ------------------------------------------------------------------------------------

CREATE OR REPLACE FUNCTION isat_resource_proc_check
(
  n_group_name "isat_resource"."group_name"%type,
  n_pip_name "isat_resource"."code"%type
) RETURNS VOID AS $$
DECLARE
   resource_type VARCHAR := 'PIP Name';
   resource_count DECIMAL;
BEGIN 
  SELECT COUNT(*) INTO resource_count FROM gtvan."isat_resource" t WHERE t."type"=resource_type AND t."code"=n_pip_name AND t."group_name"=n_group_name;
  
  IF resource_count = 0 THEN
     PERFORM gtvan.isat_resource_insert(resource_type,n_pip_name,n_pip_name,n_group_name);  
  ELSE
     RAISE DEBUG 'resource is existed';
  END IF;
    
END;
$$ LANGUAGE plpgsql;

---- TRIGGER for handling the creation of the isat_resource for Process Transaction
CREATE OR REPLACE FUNCTION invoke_create_resource_after_insert_process_trans() RETURNS trigger AS $create_resource_after_insert_process_trans$
    BEGIN
        IF NEW."group_name" IS NOT NULL THEN
            RAISE DEBUG 'Creating isat resource for process trans';
			PERFORM gtvan.isat_resource_proc_check(NEW."group_name", NEW."pip_name");
			RETURN NEW;
        END IF;
		
		RETURN NEW;
    END;
$create_resource_after_insert_process_trans$ LANGUAGE plpgsql;

DROP TRIGGER IF EXISTS create_resource_after_insert_process_trans on "isat_process_transaction";
CREATE TRIGGER create_resource_after_insert_process_trans AFTER INSERT OR UPDATE ON "isat_process_transaction"
    FOR EACH ROW EXECUTE PROCEDURE invoke_create_resource_after_insert_process_trans();


---- test completed ----
	
--- -----------------------------------------------------------------------------
---  Update isat_document_transaction.doc_time_received after insert new record
---  in isat_document_transaction
--- -----------------------------------------------------------------------------
CREATE OR REPLACE FUNCTION doc_recv_date_after_idt
(
  --curr_rowid ROWID,
  n_tracing_id "isat_document_transaction"."tracing_id"%type
) RETURNS VOID AS $$
DECLARE
  doc_recv_time gtvan."isat_document_transaction"."doc_time_received"%type;
  event_time gtvan."isat_document_transaction"."doc_time_received"%type;
  curs CURSOR FOR 
    SELECT "doc_time_received" FROM gtvan."isat_document_transaction" 
      WHERE "tracing_id" = n_tracing_id AND "direction" = 'Inbound'
    FOR UPDATE;
  curs_sel CURSOR FOR
    SELECT "event_occur_time" FROM gtvan."isat_trace_event_info" 
      WHERE "tracing_id" = n_tracing_id AND "event_name" = 'Document Received';
BEGIN 
  OPEN curs;
  LOOP
    FETCH curs INTO doc_recv_time;
    IF NOT FOUND THEN
	    RETURN;
	END IF;
    
    OPEN curs_sel;
      FETCH curs_sel INTO event_time;
      IF NOT FOUND THEN
	    RETURN;
	  END IF;
	  
      IF event_time IS NOT NULL THEN
        UPDATE gtvan."isat_document_transaction" SET "doc_time_received" = event_time 
          WHERE CURRENT OF curs;
		RETURN;
      END IF;
  END LOOP;
END;
$$ LANGUAGE plpgsql;

--- -----------------------------------------------------------------------------
---  Update isat_document_transaction.doc_time_sent after insert new record
---  in isat_document_transaction
--- -----------------------------------------------------------------------------
CREATE OR REPLACE FUNCTION doc_sent_date_after_idt
(
  --curr_rowid ROWID,
  n_tracing_id "isat_document_transaction"."tracing_id"%type,
  n_msg_id "isat_document_transaction"."message_id"%type
) RETURNS VOID AS $$

DECLARE
  doc_sent_time gtvan."isat_document_transaction"."doc_time_received"%type;
  event_time gtvan."isat_document_transaction"."doc_time_sent"%type;
  curs CURSOR FOR 
    SELECT "doc_time_sent" FROM gtvan."isat_document_transaction" 
      WHERE "tracing_id" = n_tracing_id AND "message_id" = n_msg_id
    FOR UPDATE;
  curs_sel CURSOR FOR
    SELECT "event_occur_time" FROM gtvan."isat_trace_event_info" 
      WHERE "tracing_id" = n_tracing_id AND "message_id" = n_msg_id 
        AND "event_name" = 'Document Delivery' AND "status" = 'OK';
BEGIN 
  OPEN curs;
  LOOP
    FETCH curs INTO doc_sent_time;
	
	IF NOT FOUND THEN
	    RAISE DEBUG 'doc_sent_date_after_idt isat doc trans with msg id %, tracing id % not found', n_msg_id, n_tracing_id;
	    RETURN;
	END IF;
    
    OPEN curs_sel;
      FETCH curs_sel INTO event_time;
      IF NOT FOUND THEN
	    RAISE DEBUG 'doc_sent_date_after_idt trace event with tracing id % not found',n_tracing_id;
	    RETURN;
	  END IF;
	  
      IF event_time IS NOT NULL THEN
        UPDATE gtvan."isat_document_transaction" SET "doc_time_sent" = event_time 
          WHERE CURRENT OF curs;
		RETURN;
      END IF;
	  
  END LOOP;
END;
$$ LANGUAGE plpgsql;

---- TRIGGER for handling the creation of the isat_resource for document transaction
CREATE OR REPLACE FUNCTION invoke_update_doc_date_after_insert() RETURNS trigger AS $update_doc_date_after_insert$
DECLARE
  vMsg VARCHAR := 'Statement Level Trigger Fired';
  doc_dir varchar;
  trac_id varchar;
  msg_id varchar;
  time_lock varchar;
BEGIN

    SELECT "direction","tracing_id","message_id" INTO doc_dir,trac_id,msg_id 
      FROM gtvan."isat_document_transaction" WHERE "uid" = NEW."uid";

    IF doc_dir = 'Inbound' THEN
      SELECT "value" INTO time_lock 
        FROM gtvan."isat_resource" WHERE "type"='lock' AND "code"='timelock'
        FOR UPDATE;
      PERFORM gtvan.doc_recv_date_after_idt(trac_id);
      RETURN NEW;
    ELSIF doc_dir = 'Outbound' THEN
	  RAISE DEBUG 'invoke_update_doc_date_after_insert, updating sent date for msgId %', msg_id;
      SELECT "value" INTO time_lock 
        FROM gtvan."isat_resource" WHERE "type"='lock' AND "code"='timelock'
        FOR UPDATE;
      PERFORM gtvan.doc_sent_date_after_idt(trac_id,msg_id);
      RETURN NEW;
    ELSE 
      RAISE DEBUG '% Invalid document direction',vMsg;
      RETURN NEW;
    END IF;
--EXCEPTION
  --WHEN OTHERS THEN
  --  DBMS_OUTPUT.PUT_LINE('Error while INSERT isat_document_transaction, direction='||doc_dir);
  --  ROLLBACK;
END;
$update_doc_date_after_insert$ LANGUAGE plpgsql;

--- -----------------------------------------------------------------------------
---  Trigger procedure to update doc_date AFTER insert new record in
---  isat_document_trasaction
--- -----------------------------------------------------------------------------
DROP TRIGGER IF EXISTS update_doc_date_after_insert on "isat_document_transaction";
CREATE TRIGGER update_doc_date_after_insert AFTER INSERT ON "isat_document_transaction"
    FOR EACH ROW EXECUTE PROCEDURE invoke_update_doc_date_after_insert();

-- test completed!

--- -----------------------------------------------------------------------------
---  Update isat_document_transaction.doc_time_received after insert new record
---  in isat_trace_event_info
--- -----------------------------------------------------------------------------
CREATE OR REPLACE FUNCTION doc_recv_date_after_itei
(
  n_tracing_id "isat_trace_event_info"."tracing_id"%type,
  n_event_time "isat_trace_event_info"."event_occur_time"%type
) RETURNS VOID AS $$
DECLARE
  doc_recv_time timestamp with time zone;
  curs CURSOR FOR
    SELECT "doc_time_received" FROM gtvan."isat_document_transaction" 
      WHERE "tracing_id" = n_tracing_id AND "direction" = 'Inbound'
    FOR UPDATE;
BEGIN 
  OPEN curs;
  LOOP
    FETCH curs INTO doc_recv_time;
    IF NOT FOUND THEN
	    RETURN;
	END IF;
	
    IF n_event_time IS NOT NULL THEN
      UPDATE gtvan."isat_document_transaction" SET "doc_time_received" = n_event_time WHERE CURRENT OF curs;
	  RETURN;
    END IF;
  END LOOP;
END;
$$ LANGUAGE plpgsql;

--- -----------------------------------------------------------------------------
---  Update isat_document_transaction.doc_time_sent after insert new record
---  in isat_trace_event_info
--- -----------------------------------------------------------------------------
CREATE OR REPLACE FUNCTION doc_sent_date_after_itei
(
  n_tracing_id "isat_trace_event_info"."tracing_id"%type,
  n_msg_id "isat_trace_event_info"."message_id"%type,
  n_event_time "isat_trace_event_info"."event_occur_time"%type
) RETURNS VOID AS $$
DECLARE
  doc_sent_time timestamp with time zone;
  curs CURSOR FOR
    SELECT "doc_time_sent" FROM gtvan."isat_document_transaction" 
      WHERE "tracing_id" = n_tracing_id AND "message_id" = n_msg_id
    FOR UPDATE;
BEGIN 
  OPEN curs;
  LOOP
    FETCH curs INTO doc_sent_time;    
    IF NOT FOUND THEN
	    RETURN;
	END IF;
	
    IF n_event_time IS NOT NULL THEN
      UPDATE gtvan."isat_document_transaction" SET "doc_time_sent" = n_event_time WHERE CURRENT OF curs;
	  RETURN;
    END IF;
  END LOOP;
END;
$$ LANGUAGE plpgsql;


CREATE OR REPLACE FUNCTION invoke_trace_event_info_after_insert() RETURNS trigger AS $trace_event_info_after_insert$
DECLARE
  vMsg VARCHAR := 'Statement Level Trigger Fired';
  time_lock character varying;
BEGIN
  IF NEW."event_name" = 'Document Received' THEN
    SELECT "value" INTO time_lock 
      FROM gtvan."isat_resource" WHERE "type"='lock' AND "code"='timelock'
      FOR UPDATE;
    PERFORM gtvan.doc_recv_date_after_itei(NEW."tracing_id",NEW."event_occur_time");
  ELSIF NEW."event_name" = 'Document Delivery' AND NEW."status" = 'OK' THEN
    SELECT "value" INTO time_lock
      FROM gtvan."isat_resource" WHERE "type"='lock' AND "code"='timelock'
      FOR UPDATE;
    PERFORM gtvan.doc_sent_date_after_itei(NEW."tracing_id",NEW."message_id",NEW."event_occur_time");
  ELSIF NEW."event_name" IS NULL THEN
    RAISE DEBUG '% Null event_name',vMsg;
  END IF;
  
  RETURN NEW;
--EXCEPTION
--  WHEN OTHERS THEN
--    DBMS_OUTPUT.PUT_LINE('Error while INSERT isat_trace_event_info, event_name='||:new."event_name");
--    ROLLBACK;
END;
$trace_event_info_after_insert$ LANGUAGE plpgsql;

--- -----------------------------------------------------------------------------
---  Trigger procedure to update doc_date AFTER insert new record in
---  isat_trace_event_info
--- -----------------------------------------------------------------------------
DROP TRIGGER IF EXISTS trace_event_info_after_insert on "isat_trace_event_info";
CREATE TRIGGER trace_event_info_after_insert AFTER INSERT ON "isat_trace_event_info"
    FOR EACH ROW EXECUTE PROCEDURE invoke_trace_event_info_after_insert();


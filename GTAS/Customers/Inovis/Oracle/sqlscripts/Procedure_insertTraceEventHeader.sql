--- -----------------------------------------------------------------------------------
--- This script create Stored Procedure for INSERT or UPDATE "isat_trace_event_header"
--- -----------------------------------------------------------------------------------

--CONNECT GTVAN/gridnode;

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

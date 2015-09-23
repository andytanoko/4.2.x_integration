-- Apply following script to "GTVAN" schema

-- DROP index Process_transaction_idx1
DROP INDEX Process_transaction_idx1;

-- New index to speed up TXMR UI

-- speed up "transaction_detail" page
create index TRACE_EVENT_INFO_IDX4 on "isat_trace_event_info"
(
  NLSSORT ("tracing_id", 'NLS_SORT=BINARY_CI'),
  NLSSORT ("event_name", 'NLS_SORT=BINARY_CI')  
); 

CREATE INDEX ISAT_TRACE_EVENT_HDR_IDX1 ON "isat_trace_event_header" 
(
      NLSSORT("tracing_id", 'NLS_SORT=BINARY_CI')
);

-- speed up the fetching for document_transaction record in "process_detail" page
CREATE INDEX DOCUMENT_TRANSACTION_IDX5 ON "isat_document_transaction" 
(
      NLSSORT("tracing_id", 'NLS_SORT=BINARY_CI'),
      NLSSORT("process_instance_uid",'NLS_SORT=BINARY_CI')
);


-- Speed up the trandoc_detail_event_detail
create index TRACE_EVENT_INFO_IDX5 on "isat_trace_event_info"
(
    
    "event_occur_time",
    NLSSORT("event_name",'NLS_SORT=BINARY_CI'),  
    NLSSORT("status",'NLS_SORT=BINARY_CI'),
    NLSSORT("message_id",'NLS_SORT=BINARY_CI'),
    NLSSORT("msg_type",'NLS_SORT=BINARY_CI')
);


-- Tune for searching for "process_instance_id" + "date_range", or any combination that 
CREATE INDEX PROCESS_TRANSACTION_IDX5 ON "isat_process_transaction" (
  "process_start_time",   
  NLSSORT("group_name",'nls_sort=''BINARY_CI'''),
  NLSSORT("customer_name",'nls_sort=''BINARY_CI'''),
  NLSSORT("pip_name",'nls_sort=''BINARY_CI'''),     
  NLSSORT("process_status",'nls_sort=''BINARY_CI'''),
  NLSSORT("request_doc_no",'nls_sort=''BINARY_CI'''),
  NLSSORT("response_doc_no",'nls_sort=''BINARY_CI'''),
  NLSSORT("process_id",'nls_sort=''BINARY_CI'''),
  NLSSORT("partner_name",'nls_sort=''BINARY_CI''')

) ;

CREATE INDEX PROCESS_TRANSACTION_IDX6 ON "isat_process_transaction" (
  "process_start_time",   
  "group_name",
  "customer_name",
  "pip_name",     
  "process_status",
  "request_doc_no",
  "response_doc_no",
  "process_id",
  "partner_name"
) ;


CREATE INDEX PROCESS_TRANSACTION_IDX7 ON "isat_process_transaction" (
  "process_start_time",   
  "group_name"
);

commit;




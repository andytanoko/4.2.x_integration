CONNECT USERDB/gridnode;

--- ----------------------------------------------------
---  To alter tables with the extended UID field length
--- ----------------------------------------------------

--- -------------
---  userdb-app
--- -------------

ALTER TABLE "process_act" MODIFY (
  "ProcessDefUid" NUMBER(19),
  "ProcessDefAct" NUMBER(19)
);


ALTER TABLE "rtprocess" MODIFY (
  "UID" NUMBER(19),
  "ProcessUId" NUMBER(19),
  "ParentRtActivityUId" NUMBER(19),
  "ContextUId" NUMBER(19)
);

ALTER TABLE "rtactivity" MODIFY (
  "UID" NUMBER(19),
  "ActivityUId" NUMBER(19),
  "RtProcessUId" NUMBER(19),
  "ContextUId" NUMBER(19)
);


ALTER TABLE "rtrestriction" MODIFY (
  "UID" NUMBER(19),
  "RestrictionUId" NUMBER(19),
  "RtProcessUId" NUMBER(19),
  "TransActivationStateListUId" NUMBER(19)
);

ALTER TABLE "trans_activation_state" MODIFY (
  "UID" NUMBER(19),
  "TransitionUId" NUMBER(19),
  "RtRestrictionUId" NUMBER(19)
);

ALTER TABLE "rtprocessdoc" MODIFY (
  "UID" NUMBER(19),
  "BinaryCollaborationUId" NUMBER(19),
  "RtBinaryCollaborationUId" NUMBER(19),
  "RtBusinessTransactionUId" NUMBER(19)
);


--- -------------
---  userdb-base
--- -------------

ALTER TABLE "bpss_bin_coll" MODIFY ("UID" NUMBER(19));

ALTER TABLE "bpss_biz_partner_role" MODIFY ("UID" NUMBER(19));

ALTER TABLE "bpss_bin_coll_act" MODIFY (
  "UID" NUMBER(19),
  "BinCollProcessUId" NUMBER(19),
  "DownLinkUId" NUMBER(19)
);

ALTER TABLE "bpss_biz_tran" MODIFY (
  "UID" NUMBER(19),
  "ReqUId" NUMBER(19),
  "ResUId" NUMBER(19)
);

ALTER TABLE "bpss_biz_tran_activity" MODIFY (
  "UID" NUMBER(19),
  "BusinessTransUId" NUMBER(19)
);

ALTER TABLE "bpss_coll_activity" MODIFY (
  "UID" NUMBER(19),
  "BinCollProcessUId" NUMBER(19)
);

ALTER TABLE "bpss_completion_state" MODIFY (
  "UID" NUMBER(19),
  "ProcessUId" NUMBER(19),
  "MpcUId" NUMBER(19)
);

ALTER TABLE "bpss_fork" MODIFY ("UID" NUMBER(19));

ALTER TABLE "bpss_multiparty_coll" MODIFY ("UID" NUMBER(19));

ALTER TABLE "bpss_req_biz_activity" MODIFY ("UID" NUMBER(19));

ALTER TABLE "bpss_res_biz_activity" MODIFY ("UID" NUMBER(19));

ALTER TABLE "bpss_start" MODIFY (
  "UID" NUMBER(19),
  "ProcessUId" NUMBER(19)
);

ALTER TABLE "bpss_transition" MODIFY (
  "UID" NUMBER(19),
  "ProcessUId" NUMBER(19)
);

ALTER TABLE "bpss_businessdocument" MODIFY ("UID" NUMBER(19));

ALTER TABLE "bpss_documentation" MODIFY ("UID" NUMBER(19));

ALTER TABLE "bpss_documentenvelope" MODIFY ("UID" NUMBER(19));

ALTER TABLE "bpss_proc_spec" MODIFY ("UID" NUMBER(19));

ALTER TABLE "bpss_proc_spec_entry" MODIFY (
  "UID" NUMBER(19),
  "SpecUId" NUMBER(19),
  "EntryUId" NUMBER(19),
  "ParentEntryUId" NUMBER(19)
);

ALTER TABLE "worklistvalue" MODIFY (
  "UID" NUMBER(19),
  "rtActivityUId" NUMBER(19),
  "contextUId" NUMBER(19)
);

ALTER TABLE "worklistuser" MODIFY ("workitem_id" NUMBER(19));


-- TWX20070617 Update for archive by customer
-- rtprocess uid is set to wrong field.  Added Process Status
CREATE OR REPLACE VIEW "USERDB"."archive_process_view" ("RTProcessUID","EngineType","ProcessType","StartTime","EndTime","ProcessUId","RTProcessDocUID","CustomerBEId","PartnerKey","ProcessStatus") AS SELECT "rtprocess"."UID" AS "RTProcessUID", "rtprocess"."EngineType" AS "EngineType", "rtprocess"."ProcessType" AS "ProcessType","rtprocess"."StartTime" AS "StartTime", "rtprocess"."EndTime" AS "EndTime", "rtprocess"."ProcessUId" AS "ProcessUId","rtprocessdoc"."UID" AS "RTProcessDocUID", "rtprocessdoc"."CustomerBEId" AS "CustomerBEId", "rtprocessdoc"."PartnerKey" AS "PartnerKey", "rtprocessdoc"."Status" AS "ProcessStatus" FROM "rtprocess" JOIN "rtprocessdoc" ON "rtprocess"."UID" = "rtprocessdoc"."RtBinaryCollaborationUId"; 

DELETE FROM "message_template" WHERE "UID"=-23 AND "Name"='ARCHIVE_START_EMAIL';
INSERT INTO "message_template" ("UID", "Name", "ContentType", "MessageType", "FromAddr", "ToAddr", "CcAddr", "Subject", "Message", "Location", "Append", "Version", "CanDelete") 
VALUES (-23,'ARCHIVE_START_EMAIL','Text','EMail','<#USER=admin#>','<#USER=admin#>','','[GRIDTALK] Archive Started','Dear User,'||chr(13)||chr(10)||chr(13)||chr(10)||'Archive by [<%ARCHIVE.ARCHIVE_BY%>] has started at: <%ARCHIVE.START_TIME_UTC%> [<%ARCHIVE.START_TIME%>]'||chr(13)||chr(10)||chr(13)||chr(10)||'Archive [<%ARCHIVE.ARCHIVE_BY%>] Criteria'||chr(13)||chr(10)||'-----------------------------------------------------'||chr(13)||chr(10)||'Is Enable Restore Archive  : <%ARCHIVE.IS_ENABLE_RESTORE_ARCHIVE%>'||chr(13)||chr(10)||'Is Enable Search Archive   : <%ARCHIVE.IS_ENABLE_SEARCH_ARCHIVE%>'||chr(13)||chr(10)||'From Date/Time             : <%ARCHIVE.FROM_DATE_TIME_UTC%> [<%ARCHIVE.FROM_DATE_TIME%>]'||chr(13)||chr(10)||'To Date Time               : <%ARCHIVE.TO_DATE_TIME_UTC%> [<%ARCHIVE.TO_DATE_TIME%>]'||chr(13)||chr(10)||'<%?NProcess Definition         N?ARCHIVE.IS_DOC_TYPE_NULL#Folder                     %>: <%?NN?ARCHIVE.PROCESS_DEF_LIST%><%?NN?ARCHIVE.FOLDER_LIST%>'||chr(13)||chr(10)||'<%?NInclude Incomplete         N?ARCHIVE.IS_DOC_TYPE_NULL#Document Type              %>: <%?NN?ARCHIVE.INCLUDE_INCOMPLETE%><%?NN?ARCHIVE.DOC_TYPE_LIST%>'||chr(13)||chr(10)||'Partners To Archive        : <%ARCHIVE.PARTNER_FOR_ARCHIVE%>'||chr(13)||chr(10)||'Biz Entities To Archive    : <%ARCHIVE.BE_ID_FOR_ARCHIVE%>'||chr(13)||chr(10)||'Is Archive Orphan Record   : <%ARCHIVE.IS_ARCHIVE_ORPHAN_RECORD%>'||chr(13)||chr(10)||chr(13)||chr(10)||'Archive ID                 : <%ARCHIVE.ARCHIVE_ID%>'||chr(13)||chr(10)||chr(13)||chr(10)||chr(13)||chr(10)||'Regards,'||chr(13)||chr(10)||'Gridtalk Server',NULL,NULL,1,0);

DELETE FROM "message_template" WHERE "UID"=-27 AND "Name"='ARCHIVE_COMPLETE_EMAIL';
INSERT INTO "message_template" ("UID", "Name", "ContentType", "MessageType", "FromAddr", "ToAddr", "CcAddr", "Subject", "Message", "Location", "Append", "Version", "CanDelete") 
VALUES (-27,'ARCHIVE_COMPLETE_EMAIL','Text','EMail','<#USER=admin#>','<#USER=admin#>','','[GRIDTALK] Archive Completed','Dear User,'||chr(13)||chr(10)||chr(13)||chr(10)||'Archive by [<%ARCHIVE.ARCHIVE_BY%>] has completed at: <%ARCHIVE.START_TIME_UTC%> [<%ARCHIVE.START_TIME%>]'||chr(13)||chr(10)||chr(13)||chr(10)||'Archive [<%ARCHIVE.ARCHIVE_BY%>] Criteria'||chr(13)||chr(10)||'-----------------------------------------------------'||chr(13)||chr(10)||'Is Enable Restore Archive  : <%ARCHIVE.IS_ENABLE_RESTORE_ARCHIVE%>'||chr(13)||chr(10)||'Is Enable Search Archive   : <%ARCHIVE.IS_ENABLE_SEARCH_ARCHIVE%>'||chr(13)||chr(10)||'From Date/Time             : <%ARCHIVE.FROM_DATE_TIME_UTC%> [<%ARCHIVE.FROM_DATE_TIME%>]'||chr(13)||chr(10)||'To Date Time               : <%ARCHIVE.TO_DATE_TIME_UTC%> [<%ARCHIVE.TO_DATE_TIME%>]'||chr(13)||chr(10)||'<%?NProcess Definition         N?ARCHIVE.IS_DOC_TYPE_NULL#Folder                     %>: <%?NN?ARCHIVE.PROCESS_DEF_LIST%><%?NN?ARCHIVE.FOLDER_LIST%>'||chr(13)||chr(10)||'<%?NInclude Incomplete         N?ARCHIVE.IS_DOC_TYPE_NULL#Document Type              %>: <%?NN?ARCHIVE.INCLUDE_INCOMPLETE%><%?NN?ARCHIVE.DOC_TYPE_LIST%>'||chr(13)||chr(10)||'Partners To Archive        : <%ARCHIVE.PARTNER_FOR_ARCHIVE%>'||chr(13)||chr(10)||'Biz Entities To Archive    : <%ARCHIVE.BE_ID_FOR_ARCHIVE%>'||chr(13)||chr(10)||'Is Archive Orphan Record   : <%ARCHIVE.IS_ARCHIVE_ORPHAN_RECORD%>'||chr(13)||chr(10)||chr(13)||chr(10)||'Archive ID                 : <%ARCHIVE.ARCHIVE_ID%>'||chr(13)||chr(10)||chr(13)||chr(10)||'Number of [<%ARCHIVE.ARCHIVE_BY%>] archived : <%ARCHIVE.NUM_ARCHIVED%>'||chr(13)||chr(10)||chr(13)||chr(10)||'Regards,'||chr(13)||chr(10)||'Gridtalk Server',NULL,NULL,1,0);

DELETE FROM "message_template" WHERE "UID"=-28 AND "Name"='ARCHIVE_FAIL_EMAIL';
INSERT INTO "message_template" ("UID", "Name", "ContentType", "MessageType", "FromAddr", "ToAddr", "CcAddr", "Subject", "Message", "Location", "Append", "Version", "CanDelete") 
VALUES (-28,'ARCHIVE_FAIL_EMAIL','Text','EMail','<#USER=admin#>','<#USER=admin#>','','[GRIDTALK] Archive Failed','Dear User,'||chr(13)||chr(10)||chr(13)||chr(10)||'Archive by [<%ARCHIVE.ARCHIVE_BY%>] has failed at: <%ARCHIVE.START_TIME_UTC%> [<%ARCHIVE.START_TIME%>]'||chr(13)||chr(10)||chr(13)||chr(10)||'Archive [<%ARCHIVE.ARCHIVE_BY%>] Criteria'||chr(13)||chr(10)||'-----------------------------------------------------'||chr(13)||chr(10)||'Is Enable Restore Archive  : <%ARCHIVE.IS_ENABLE_RESTORE_ARCHIVE%>'||chr(13)||chr(10)||'Is Enable Search Archive   : <%ARCHIVE.IS_ENABLE_SEARCH_ARCHIVE%>'||chr(13)||chr(10)||'From Date/Time             : <%ARCHIVE.FROM_DATE_TIME_UTC%> [<%ARCHIVE.FROM_DATE_TIME%>]'||chr(13)||chr(10)||'To Date Time               : <%ARCHIVE.TO_DATE_TIME_UTC%> [<%ARCHIVE.TO_DATE_TIME%>]'||chr(13)||chr(10)||'<%?NProcess Definition         N?ARCHIVE.IS_DOC_TYPE_NULL#Folder                     %>: <%?NN?ARCHIVE.PROCESS_DEF_LIST%><%?NN?ARCHIVE.FOLDER_LIST%>'||chr(13)||chr(10)||'<%?NInclude Incomplete         N?ARCHIVE.IS_DOC_TYPE_NULL#Document Type              %>: <%?NN?ARCHIVE.INCLUDE_INCOMPLETE%><%?NN?ARCHIVE.DOC_TYPE_LIST%>'||chr(13)||chr(10)||'Partners To Archive        : <%ARCHIVE.PARTNER_FOR_ARCHIVE%>'||chr(13)||chr(10)||'Biz Entities To Archive    : <%ARCHIVE.BE_ID_FOR_ARCHIVE%>'||chr(13)||chr(10)||'Is Archive Orphan Record   : <%ARCHIVE.IS_ARCHIVE_ORPHAN_RECORD%>'||chr(13)||chr(10)||chr(13)||chr(10)||'Archive ID                 : <%ARCHIVE.ARCHIVE_ID%>'||chr(13)||chr(10)||chr(13)||chr(10)||'Please verify the problem and restart the operation. (Note: Some documents/processes may have already been archived. It is safe to run the archival again.)'||chr(13)||chr(10)||chr(13)||chr(10)||'Exception : <%Exception.MESSAGE%>'||chr(13)||chr(10)||'Trace:'||chr(13)||chr(10)||'------'||chr(13)||chr(10)||'<%Exception.STACK_TRACE%>'||chr(13)||chr(10)||chr(13)||chr(10)||chr(13)||chr(10)||'Regards,'||chr(13)||chr(10)||'GridTalk Server','','',1,0);


COMMIT;


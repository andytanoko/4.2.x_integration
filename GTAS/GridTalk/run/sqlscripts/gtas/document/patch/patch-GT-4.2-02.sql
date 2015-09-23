--[29 Jun 2010]   Tam Wei Xiang        Enable the UID, tracingId to be part of the substitution list

SET client_encoding = 'UTF8';
SET check_function_bodies = false;
SET client_min_messages = warning;
SET search_path = appdb;

update "fieldmetainfo" set "Presentation"='displayable=true' where "EntityObjectName"='com.gridnode.gtas.server.document.model.GridDocument' and "FieldName"='UID';
update "fieldmetainfo" set "Presentation"='displayable=true' where "EntityObjectName"='com.gridnode.gtas.server.document.model.GridDocument' and "FieldName"='TRACING_ID';

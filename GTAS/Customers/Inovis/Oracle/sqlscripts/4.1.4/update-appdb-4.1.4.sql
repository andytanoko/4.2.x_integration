-- Apply following script to "APPDB" schema
-- This is needed for the ATX connectivity configuration

update "fieldmetainfo" set "Presentation"='displayable=true'||chr(13)||chr(10)||'mandatory=false'||chr(13)||chr(10)||'editable=false' where "EntityObjectName"='com.gridnode.gtas.server.document.model.GridDocument' AND "ObjectName"='_tracingID'; 
commit;

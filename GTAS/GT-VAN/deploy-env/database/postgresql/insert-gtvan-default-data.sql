-- -----------------------------------------------------------------
-- This script Creates default data for some of the tables in GTVAN
-- -----------------------------------------------------------------
SET client_encoding = 'UTF8';
SET check_function_bodies = false;
SET client_min_messages = warning;
SET search_path = gtvan;

--
-- Default configuration properties
--

-- EMAIL: The following properties are required for Email alert notifications
-- 'mail.recipients' : Define the email recipient groups. The property_key specifies the email group name, and value specifies the
--                     list of email address (separated by comma) for that group. 
-- Recommend to always specify one for 'default'.
--INSERT INTO "config_props" ("category", property_key, value) VALUES ('mail.recipients', 'default', 'pdtest@gridnode.com');

-- 'smtp.server.default' : Define the default SMTP server to use for emailing.
--   'mail.smtp.auth'    : Specify 'true' if server authentication is required to send email, 'false' otherwise.
--   'mail.smtp.user'    : Specify the authentication user to use, if authentication is required
--   'mail.smtp.auth.pwd': Specify the authentication password to use, if authentication is required
--   'mail.smtp.host'    : Specify the SMTP Server host IP or name
--   'mail.smtp.port'    : Specify the SMTP server port
--   'mail.from'         : Specify the From address for emails sent from the application.
--INSERT INTO "config_props" ("category", "property_key", "value") VALUES ('smtp.server.default', 'mail.smtp.auth', 'true');
--INSERT INTO "config_props" ("category", "property_key", "value") VALUES ('smtp.server.default', 'mail.smtp.user', 'pdtest@gridnode.com');
--INSERT INTO "config_props" ("category", "property_key", "value") VALUES ('smtp.server.default', 'mail.smtp.auth.pwd', 'gridnode');
INSERT INTO "config_props" ("category", "property_key", "value") VALUES ('smtp.server.default', 'mail.smtp.host', 'rca-relay.itlogon.com');
INSERT INTO "config_props" ("category", "property_key", "value") VALUES ('smtp.server.default', 'mail.smtp.port', '25');
INSERT INTO "config_props" ("category", "property_key", "value") VALUES ('smtp.server.default', 'mail.from', 'gtvan@gridnode.com');



-- HTTPBC: The following properties are required for HTTPBC

-- NSL20070412: Not required anymore. ISHC directly invoke save via EJB
-- HTTPBC: These properties are required for receiving transactions from customer HTTP backend. Change should not be required.
--INSERT INTO "config_props" ("category", "property_key", "value") VALUES ('ishb.tx.out', 'destination.jndi', 'queue/gtvan/ishb/TxOutQueue');
--INSERT INTO "config_props" ("category", "property_key", "value") VALUES ('ishb.tx.out', 'connection.factory.jndi', 'ConnectionFactory');
--INSERT INTO "config_props" ("category", "property_key", "value") VALUES ('ishb.tx.out', 'java.naming.factory.initial', 'org.jnp.interfaces.NamingContextFactory');
--INSERT INTO "config_props" ("category", "property_key", "value") VALUES ('ishb.tx.out', 'java.naming.provider.url', 'localhost:1100');
--INSERT INTO "config_props" ("category", "property_key", "value") VALUES ('ishb.tx.out', 'java.naming.factory.url.pkgs', 'org.jboss.naming:org.jnp.interfaces');

-- HTTPBC: These properties are required for default http headers to set for a document when sending to customer HTTP backend. 
-- Additional headers may be added by specify a new property_key and value to the same category.
-- Specific headers may be added for specific document types by just specifying 'http.doc.headers.<documenttype>' for the category,
-- the property_key is abiturary while, however, substitution keys (${doc.type}, ${partner.id},etc) are limited to those specified for default. 
INSERT INTO "config_props" ("category", "property_key", "value") VALUES ('http.doc.headers.default', 'DocType', '${doc.type}');
INSERT INTO "config_props" ("category", "property_key", "value") VALUES ('http.doc.headers.default', 'SenderId', '${partner.id}');
INSERT INTO "config_props" ("category", "property_key", "value") VALUES ('http.doc.headers.default', 'RecipientId', '${bizent.id}');
INSERT INTO "config_props" ("category", "property_key", "value") VALUES ('http.doc.headers.default', 'DocFilename', '${doc.filename}');

-- NSL20070419: GNDB00028303
INSERT INTO "config_props" ("category", "property_key", "value") VALUES ('http.doc.headers.default', 'Content-type', 'application/xml');

-- Must be specified each document type to receive from Customer HTTP Backend.
-- '<document_type>' : Replace with the actual document type received from Customer HTTP backend.
-- '<file_ext>' : Replace with the actual file extension, e.g. 'xml'
--INSERT INTO "config_props" ("category", "property_key", "value") VALUES ('doctype.fext.map', '<document_type>', '<file_ext>');

-- Must be specified, one for each customer HTTP backend.
-- Replace <cust_beid> with the Customer (business) entity.
-- Set the actual HTTP URL for the customer HTTP backend.
--INSERT INTO "config_props" ("category", "property_key", "value") VALUES ('http.target.conn.<cust_beid>', 'target.http.url', 'http://cust_url');

-- NSL20070419: GNDB00028302
-- Disable Hostname verification during outbound HTTPS connection using SSL
--INSERT INTO "config_props" ("category", "property_key", "value") VALUES ('http.target.conn.<cust_beid>','auth.verifyhost.enabled','false');


-- HTTPBC: These properties are required for delivering document to GridTalk
-- 'gtas.login.default' : Default for login to GridTalk to import document
--    'login.user'      : User name
--    'client.root.dir' : Root directory for the login client (no change required).
-- If different authentication is necessary, add in new set of above properties with category 'gtas.login.<customer_beid>'
-- where <customer_beid> is the Business entity ID of the customer.
-- 'gtas.jndi'          : for locating GridTalk Document Manager to handle limport. No change required.
INSERT INTO "config_props" ("category", "property_key", "value") VALUES ('gtas.login.default', 'login.user', 'admin');
--INSERT INTO "config_props" ("category", "property_key", "value") VALUES ('gtas.login.default', 'login.pwd', 'admin');
INSERT INTO "config_props" ("category", "property_key", "value") VALUES ('gtas.login.default', 'client.root.dir', 'gtas/data/temp');
INSERT INTO "config_props" ("category", "property_key", "value") VALUES ('gtas.jndi', 'gtas.docmgr.jndi', 'com.gridnode.gtas.server.document.facade.ejb.IDocumentManagerHome');
INSERT INTO "config_props" ("category", "property_key", "value") VALUES ('gtas.jndi', 'java.naming.factory.initial', 'org.jnp.interfaces.NamingContextFactory');
INSERT INTO "config_props" ("category", "property_key", "value") VALUES ('gtas.jndi', 'java.naming.provider.url', 'localhost:31099');
INSERT INTO "config_props" ("category", "property_key", "value") VALUES ('gtas.jndi', 'java.naming.factory.url.pkgs', 'org.jboss.naming:org.jnp.interfaces');

-- NSL20070419: GNDB00028302
-- HTTPBC: These properties are required to enable outbound HTTPS connection
INSERT INTO "config_props" ("category", "property_key", "value") VALUES ('http.proxy.auth', 'auth.server.enabled','true');
INSERT INTO "config_props" ("category", "property_key", "value") VALUES ('http.proxy.auth', 'cert.truststore.loc','../../data/GNapps/gtas/data/keystore/cacerts');
INSERT INTO "config_props" ("category", "property_key", "value") VALUES ('http.proxy.auth', 'cert.keystore.loc','../../data/GNapps/gtas/data/keystore/keystore');
INSERT INTO "config_props" ("category", "property_key", "value") VALUES ('http.proxy.auth', 'cert.keystore.pass','changeit');
INSERT INTO "config_props" ("category", "property_key", "value") VALUES ('http.proxy.auth', 'cert.truststore.pass','changeit');


-- HTTPBC: These properties are for event notification to ISAT
INSERT INTO "config_props" ("category", "property_key", "value") VALUES ('event.notfn', 'java.naming.factory.initial', 'org.jnp.interfaces.NamingContextFactory');
INSERT INTO "config_props" ("category", "property_key", "value") VALUES ('event.notfn', 'java.naming.provider.url', 'localhost:1100');
INSERT INTO "config_props" ("category", "property_key", "value") VALUES ('event.notfn', 'java.naming.factory.url.pkgs', 'org.jboss.naming:org.jnp.interfaces');
INSERT INTO "config_props" ("category", "property_key", "value") VALUES ('event.notfn', 'connection.factory.jndi', 'java:/JmsXA');
INSERT INTO "config_props" ("category", "property_key", "value") VALUES ('event.notfn', 'destination.jndi', 'queue/gtvan/isat/LocalEventQueue');
INSERT INTO "config_props" ("category", "property_key", "value") VALUES ('event.notfn', 'primitive.msg.mode', 'stream');
INSERT INTO "config_props" ("category", "property_key", "value") VALUES ('event.notfn.doc.received', 'event.name', 'Document Received from {0}');
INSERT INTO "config_props" ("category", "property_key", "value") VALUES ('event.notfn.doc.received', 'event.status', 'OK');
INSERT INTO "config_props" ("category", "property_key", "value") VALUES ('event.notfn.doc.received', 'event.root', 'AuditTrailData');
INSERT INTO "config_props" ("category", "property_key", "value") VALUES ('event.notfn.doc.received', 'event.type', 'Trans');
INSERT INTO "config_props" ("category", "property_key", "value") VALUES ('event.notfn.doc.delivered', 'event.name', 'Document Delivery to {0}');
INSERT INTO "config_props" ("category", "property_key", "value") VALUES ('event.notfn.doc.delivered', 'event.type', 'Trans');
INSERT INTO "config_props" ("category", "property_key", "value") VALUES ('event.notfn.doc.delivered', 'event.status', 'OK');
INSERT INTO "config_props" ("category", "property_key", "value") VALUES ('event.notfn.doc.delivered', 'event.root', 'AuditTrailData');
INSERT INTO "config_props" ("category", "property_key", "value") VALUES ('event.notfn.doc.delivery.failed', 'event.name', 'Document Delivery to {0}');
INSERT INTO "config_props" ("category", "property_key", "value") VALUES ('event.notfn.doc.delivery.failed', 'event.type', 'Trans');
INSERT INTO "config_props" ("category", "property_key", "value") VALUES ('event.notfn.doc.delivery.failed', 'event.status', 'FAILED');
INSERT INTO "config_props" ("category", "property_key", "value") VALUES ('event.notfn.doc.delivery.failed', 'event.root', 'AuditTrailData');

-- HTTPBC: These properties are required for email alert for exception cases. The values may be customized.
-- 'recipients' : the email recipient group to receive the alert. Multiple groups are separated by comma. Each email group must be specified with 'mail.recipients' category.
-- 'delivery.mode' : the type of delivery for the alert. '1' - Email, '2' - SMS (not implemented yet), '3' - Log 
-- 'subject': the subject of the alert. 
-- 'message': the alert message 
-- substituion variables: 
--    {0} - Module raising the alert
--    {1} - Component of Module raising the alert
--    {2} - Function in component that encountered the error
--    {3} - Short error description
--    {4} - Exception stack trace
INSERT INTO "config_props" ("category", "property_key", "value") VALUES ('unexpected.system.error', 'recipients', 'default');
INSERT INTO "config_props" ("category", "property_key", "value") VALUES ('unexpected.system.error', 'delivery.mode', '1');
INSERT INTO "config_props" ("category", "property_key", "value") VALUES ('unexpected.system.error', 'subject', '[{0}.{1}] Unexpected System Error Encountered');
INSERT INTO "config_props" ("category", "property_key", "value") 
VALUES ('unexpected.system.error', 'message', 
'{0}.{1} encountered an error during {2}.'||chr(13)||chr(10)||chr(13)||chr(10)||'Error Message: {3}'||chr(13)||chr(10)||chr(13)||chr(10)||'Error Details: {4}'||chr(13)||chr(10)||chr(13)||chr(10)||'Please contact the system support to rectify the problem.'||chr(13)||chr(10)||chr(13)||chr(10)||'GtVan System');

-- HTTPBC: These properties are required for email alert for too many failed attempts for a transaction delivery. The values may be customized.
-- 'recipients' : the email recipient group to receive the alert
-- 'delivery.mode' : the type of delivery for the alert. '1' - Email, '2' - SMS (not implemented yet), '3' - Log 
-- 'subject': the subject of the alert. 
-- 'message': the alert message 
-- substituion variables: 
--    {0} - Internal tracing identifier for the document
--    {1} - Document Direction
--    {2} - Customer (Business) Entity
--    {3} - Trading Partner
--    {4} - Document filename
--    {5} - Message ID of the document
--    {6} - Total number of failed attempts to deliver the document
INSERT INTO "config_props" ("category", "property_key", "value") VALUES ('too.many.failed.attempts', 'delivery.mode', '1');
INSERT INTO "config_props" ("category", "property_key", "value") VALUES ('too.many.failed.attempts', 'recipients', 'default');
INSERT INTO "config_props" ("category", "property_key", "value") VALUES ('too.many.failed.attempts', 'subject', '{6} failed attempts in total to deliver document: {4}');
INSERT INTO "config_props" ("category", "property_key", "value")
VALUES ('too.many.failed.attempts', 'message', 
'{6} attempts are in vain to deliver the following document:'||chr(13)||chr(10)||chr(13)||chr(10)||'Document  : {4}'||chr(13)||chr(10)||'Direction : {1}'||chr(13)||chr(10)||'Customer  : {2}'||chr(13)||chr(10)||'Partner   : {3}'||chr(13)||chr(10)||'Tracing ID: {0}'||chr(13)||chr(10)||'Message ID: {5}'||chr(13)||chr(10)||chr(13)||chr(10)||'Please contact the system support to identify and rectify the problem.'||chr(13)||chr(10)||'Meanwhile, the system would continue to retry the delivery of this document until the problem is resolved.'||chr(13)||chr(10)||'Note that, any other documents received after this document would not be delivered until this document is delivered.'||chr(13)||chr(10)||chr(13)||chr(10)||'GtVan System');

-- For GTAT RemoteEvent Q JMS properties
INSERT INTO "config_props" ("category", "property_key", "value") VALUES('gtat.jms.remote', 'destination.jndi', 'queue/gtvan/isat/RemoteEventQueue');
INSERT INTO "config_props" ("category", "property_key", "value") VALUES('gtat.jms.remote', 'connection.factory.jndi', 'java:/JmsXA');
INSERT INTO "config_props" ("category", "property_key", "value") VALUES('gtat.jms.remote', 'java.naming.factory.initial', 'org.jnp.interfaces.NamingContextFactory');
INSERT INTO "config_props" ("category", "property_key", "value") VALUES('gtat.jms.remote', 'java.naming.provider.url', 'localhost:1100');
INSERT INTO "config_props" ("category", "property_key", "value") VALUES('gtat.jms.remote', 'java.naming.factory.url.pkgs', 'org.jboss.naming:org.jnp.interfaces');

-- For ISAT ArchiveTrailData Q JMS properties
INSERT INTO "config_props" ("category", "property_key", "value") VALUES('isat.jms.archive', 'destination.jndi', 'queue/gtvan/isat/archiveTrailDataQueue');
INSERT INTO "config_props" ("category", "property_key", "value") VALUES('isat.jms.archive', 'connection.factory.jndi', 'java:/JmsXA');
INSERT INTO "config_props" ("category", "property_key", "value") VALUES('isat.jms.archive', 'java.naming.factory.initial', 'org.jnp.interfaces.NamingContextFactory');
INSERT INTO "config_props" ("category", "property_key", "value") VALUES('isat.jms.archive', 'java.naming.provider.url', 'localhost:1100');
INSERT INTO "config_props" ("category", "property_key", "value") VALUES('isat.jms.archive', 'java.naming.factory.url.pkgs', 'org.jboss.naming:org.jnp.interfaces');

-- For ISAT Reprocess Q Jms properties
INSERT INTO "config_props" ("category", "property_key", "value") VALUES('isat.jms.reprocess', 'destination.jndi', 'queue/gtvan/gtat/reprocessQueue');
INSERT INTO "config_props" ("category", "property_key", "value") VALUES('isat.jms.reprocess', 'connection.factory.jndi', 'java:/JmsXA');
INSERT INTO "config_props" ("category", "property_key", "value") VALUES('isat.jms.reprocess', 'java.naming.factory.initial', 'org.jnp.interfaces.NamingContextFactory');
INSERT INTO "config_props" ("category", "property_key", "value") VALUES('isat.jms.reprocess', 'java.naming.provider.url', 'localhost:1100');
INSERT INTO "config_props" ("category", "property_key", "value") VALUES('isat.jms.reprocess', 'java.naming.factory.url.pkgs', 'org.jboss.naming:org.jnp.interfaces');

-- For ISAT scheduler 
INSERT INTO "config_props"("category", "property_key", "value")VALUES('ISAT', 'audit.scheduler.date.pattern', 'yyyy-MM-dd HH:mm');
INSERT INTO "config_props"("category", "property_key", "value")VALUES('ISAT', 'audit.scheduler.time.interval', '60');
INSERT INTO "config_props"("category", "property_key", "value")VALUES('ISAT', 'archive.scheduler.description', 'Archival started by TM at ');
INSERT INTO "config_props"("category", "property_key", "value")VALUES('ISAT', 'archive.scheduler.name.prefix', 'Archive_Auto_Generated');

-- For ISAT Archive Q (From ISAT's archive request to OTC-plug in)
INSERT INTO "config_props" ("category", "property_key", "value") VALUES('isat.jms.gt.archive', 'destination.jndi', 'queue/gtvan/gtat/archiveQueue');
INSERT INTO "config_props" ("category", "property_key", "value") VALUES('isat.jms.gt.archive', 'connection.factory.jndi', 'java:/JmsXA');
INSERT INTO "config_props" ("category", "property_key", "value") VALUES('isat.jms.gt.archive', 'java.naming.factory.initial', 'org.jnp.interfaces.NamingContextFactory');
INSERT INTO "config_props" ("category", "property_key", "value") VALUES('isat.jms.gt.archive', 'java.naming.provider.url', 'localhost:1100');
INSERT INTO "config_props" ("category", "property_key", "value") VALUES('isat.jms.gt.archive', 'java.naming.factory.url.pkgs', 'org.jboss.naming:org.jnp.interfaces');

INSERT INTO "config_props" ("category", "property_key", "value") VALUES('isat.jms.local.event', 'destination.jndi', 'queue/gtvan/isat/LocalEventQueue');
INSERT INTO "config_props" ("category", "property_key", "value") VALUES('isat.jms.local.event', 'connection.factory.jndi', 'java:/JmsXA');
INSERT INTO "config_props" ("category", "property_key", "value") VALUES('isat.jms.local.event', 'java.naming.factory.initial', 'org.jnp.interfaces.NamingContextFactory');
INSERT INTO "config_props" ("category", "property_key", "value") VALUES('isat.jms.local.event', 'java.naming.provider.url', 'localhost:1100');
INSERT INTO "config_props" ("category", "property_key", "value") VALUES('isat.jms.local.event', 'java.naming.factory.url.pkgs', 'org.jboss.naming:org.jnp.interfaces');

-- ISAT ARCHIVE status
INSERT INTO "config_props"("category", "property_key", "value")VALUES('ISAT', 'archive.status.forward.url', 'http://localhost:8080/txmr/confirmation/archive_status.jsp');

-- 26 June 2007 TWX ISAT GENERAL ARCHIVE STARTUP ALERT FOR GT & TM
INSERT INTO "config_props" ("category", "property_key", "value") VALUES ('isat.gttm.archive.started', 'recipients', 'default');
INSERT INTO "config_props" ("category", "property_key", "value") VALUES ('isat.gttm.archive.started', 'delivery.mode', '1');
INSERT INTO "config_props" ("category", "property_key", "value") VALUES ('isat.gttm.archive.started', 'subject', 'Archive Started');
INSERT INTO "config_props" ("category", "property_key", "value") VALUES ('isat.gttm.archive.started', 'message', 'Dear user,'||chr(13)||chr(10)||chr(13)||chr(10)||'Archive Process Transaction has started at {0}.'||chr(13)||chr(10)||chr(13)||chr(10)||'Archive Criteria'||chr(13)||chr(10)||'-------------------------------------------------'||chr(13)||chr(10)||'From Date/Time:   {1}'||chr(13)||chr(10)||'To Date/Time:     {2}'||chr(13)||chr(10)||'Is Orphan Record: {3}'||chr(13)||chr(10)||'Customer List:    {4}'||chr(13)||chr(10)||chr(13)||chr(10)||'Archive ID: {5}'||chr(13)||chr(10)||chr(13)||chr(10)||'Regards,'||chr(13)||chr(10)||'GTVAN System');

INSERT INTO "config_props" ("category", "property_key", "value") VALUES ('isat.gttm.archive.completed', 'recipients', 'default');
INSERT INTO "config_props" ("category", "property_key", "value") VALUES ('isat.gttm.archive.completed', 'delivery.mode', '1');
INSERT INTO "config_props" ("category", "property_key", "value") VALUES ('isat.gttm.archive.completed', 'subject', 'Archive Completed');
INSERT INTO "config_props" ("category", "property_key", "value") VALUES ('isat.gttm.archive.completed', 'message', 'Dear user,'||chr(13)||chr(10)||chr(13)||chr(10)||'Archive has completed at {0}.'||chr(13)||chr(10)||chr(13)||chr(10)||'Archive Criteria'||chr(13)||chr(10)||'-------------------------------------------------'||chr(13)||chr(10)||'From Date/Time:   {1}'||chr(13)||chr(10)||'To Date/Time:     {2}'||chr(13)||chr(10)||'Is Orphan Record: {3}'||chr(13)||chr(10)||'Customer List:    {4}'||chr(13)||chr(10)||chr(13)||chr(10)||'Archive ID:                                   {5}'||chr(13)||chr(10)||'Archive Summary File:                         {6}'||chr(13)||chr(10)||chr(13)||chr(10)||'Regards,'||chr(13)||chr(10)||'GTVAN System');


INSERT INTO "config_props" ("category", "property_key", "value") VALUES ('isat.gttm.archive.failed', 'recipients', 'default');
INSERT INTO "config_props" ("category", "property_key", "value") VALUES ('isat.gttm.archive.failed', 'delivery.mode', '1');
INSERT INTO "config_props" ("category", "property_key", "value") VALUES ('isat.gttm.archive.failed', 'subject', 'Archive Failed');
INSERT INTO "config_props" ("category", "property_key", "value") VALUES ('isat.gttm.archive.failed', 'message', 'Dear user,'||chr(13)||chr(10)||chr(13)||chr(10)||'Archive has failed at {0}.'||chr(13)||chr(10)||chr(13)||chr(10)||'Archive Criteria'||chr(13)||chr(10)||'-------------------------------------------------'||chr(13)||chr(10)||'From Date/Time:   {1}'||chr(13)||chr(10)||'To Date/Time:     {2}'||chr(13)||chr(10)||'Is Orphan Record: {3}'||chr(13)||chr(10)||'Customer List:    {4}'||chr(13)||chr(10)||chr(13)||chr(10)||'Archive ID:                                   {5}'||chr(13)||chr(10)||'Archive Summary File:                         {6}'||chr(13)||chr(10)||chr(13)||chr(10)||'Please verify the problem and restart the operation. (Note: some process transaction may have already been archived. It is safe to run the archival again.)'||chr(13)||chr(10)||chr(13)||chr(10)||'Exception: {7}'||chr(13)||chr(10)||'Trace:'||chr(13)||chr(10)||'-------'||chr(13)||chr(10)||'{8}'||chr(13)||chr(10)||chr(13)||chr(10)||'Regards,'||chr(13)||chr(10)||'GTVAN System');


-- ISAT ARCHIVE Start
INSERT INTO "config_props"("category", "property_key", "value") VALUES ('isat.archive.started', 'recipients', 'default');
INSERT INTO "config_props"("category", "property_key", "value") VALUES ('isat.archive.started', 'delivery.mode', '1');
INSERT INTO "config_props"("category", "property_key", "value") VALUES ('isat.archive.started', 'subject', '[GTVAN] Archive Started');
INSERT INTO "config_props"("category", "property_key", "value") VALUES ('isat.archive.started', 'message', 'Dear user,'||chr(13)||chr(10)||chr(13)||chr(10)||'Archive Process Transaction has started at {0}.'||chr(13)||chr(10)||chr(13)||chr(10)||'Archive Criteria'||chr(13)||chr(10)||'-------------------------------------------------'||chr(13)||chr(10)||'From Date/Time:   {1}'||chr(13)||chr(10)||'To Date/Time:     {2}'||chr(13)||chr(10)||'Is Orphan Record: {3}'||chr(13)||chr(10)||'Customer List:    {4}'||chr(13)||chr(10)||chr(13)||chr(10)||'Archive ID:                                   {5}'||chr(13)||chr(10)||chr(13)||chr(10)||'Regards,'||chr(13)||chr(10)||'GTVAN System');

-- ISAT ARCHIVE Complete/Fail alert
INSERT INTO "config_props" ("category", "property_key", "value") VALUES ('isat.archive.completed', 'recipients', 'default');
INSERT INTO "config_props" ("category", "property_key", "value") VALUES ('isat.archive.completed', 'delivery.mode', '1');
INSERT INTO "config_props" ("category", "property_key", "value") VALUES ('isat.archive.completed', 'subject', '[GTVAN] Archive Completed');
INSERT INTO "config_props" ("category", "property_key", "value") VALUES ('isat.archive.completed', 'message', 'Dear user,'||chr(13)||chr(10)||chr(13)||chr(10)||'Archive Process Transaction has completed at {0}.'||chr(13)||chr(10)||chr(13)||chr(10)||'Archive Criteria'||chr(13)||chr(10)||'-------------------------------------------------'||chr(13)||chr(10)||'From Date/Time:   {1}'||chr(13)||chr(10)||'To Date/Time:     {2}'||chr(13)||chr(10)||'Is Orphan Record: {3}'||chr(13)||chr(10)||'Customer List:    {4}'||chr(13)||chr(10)||chr(13)||chr(10)||'Number of Process Transaction archived:       {5}'||chr(13)||chr(10)||'Number of Incomplete Process Trans archived:  {6}'||chr(13)||chr(10)||'Number of Incomplete Document archived:       {7}'||chr(13)||chr(10)||chr(13)||chr(10)||'Archive ID:                                   {8}'||chr(13)||chr(10)||chr(13)||chr(10)||'Archive Summary File:                         {9}'||chr(13)||chr(10)||chr(13)||chr(10)||'Regards,'||chr(13)||chr(10)||'GTVAN System');


INSERT INTO "config_props" ("category", "property_key", "value") VALUES ('isat.archive.failed', 'recipients', 'default');
INSERT INTO "config_props" ("category", "property_key", "value") VALUES ('isat.archive.failed', 'delivery.mode', '1');
INSERT INTO "config_props" ("category", "property_key", "value") VALUES ('isat.archive.failed', 'subject', '[GTVAN] Archive Failed');
INSERT INTO "config_props" ("category", "property_key", "value") VALUES ('isat.archive.failed', 'message', 'Dear user,'||chr(13)||chr(10)||chr(13)||chr(10)||'Archive Process Transaction has failed at {0}.'||chr(13)||chr(10)||chr(13)||chr(10)||'Archive Criteria'||chr(13)||chr(10)||'-------------------------------------------------'||chr(13)||chr(10)||'From Date/Time:   {1}'||chr(13)||chr(10)||'To Date/Time:     {2}'||chr(13)||chr(10)||'Is Orphan Record: {3}'||chr(13)||chr(10)||'Customer List:    {4}'||chr(13)||chr(10)||chr(13)||chr(10)||'Number of Process Transaction archived:       {5}'||chr(13)||chr(10)||'Number of Incomplete Process Trans archived:  {6}'||chr(13)||chr(10)||'Number of Incomplete Document archived:       {7}'||chr(13)||chr(10)||chr(13)||chr(10)||'Archive ID:                                   {8}'||chr(13)||chr(10)||chr(13)||chr(10)||'Archive Summary File:                         {9}'||chr(13)||chr(10)||chr(13)||chr(10)||'Please verify the problem and restart the operation. (Note: some process transaction may have already been archived. It is safe to run the archival again.)'||chr(13)||chr(10)||chr(13)||chr(10)||'Exception: {10}'||chr(13)||chr(10)||'Trace:'||chr(13)||chr(10)||'-------'||chr(13)||chr(10)||'{11}'||chr(13)||chr(10)||chr(13)||chr(10)||'Regards,'||chr(13)||chr(10)||'GTVAN System');


-- ISAT RESTORE Complete/ Failed
INSERT INTO "config_props" ("category", "property_key", "value") VALUES ('isat.restore.completed', 'recipients', 'default');
INSERT INTO "config_props" ("category", "property_key", "value") VALUES ('isat.restore.completed', 'delivery.mode', '1');
INSERT INTO "config_props" ("category", "property_key", "value") VALUES ('isat.restore.completed', 'subject', '[GTVAN] Restore Completed');
INSERT INTO "config_props" ("category", "property_key", "value") 
VALUES ('isat.restore.completed', 'message', 
'Dear user,'||chr(13)||chr(10)||chr(13)||chr(10)||'Restore of the archive has completed at {0}.'||chr(13)||chr(10)||chr(13)||chr(10)||'Archive Summary File: {1}'||chr(13)||chr(10)||chr(13)||chr(10)||'Regards,'||chr(13)||chr(10)||'GTVAN System');


INSERT INTO "config_props" ("category", "property_key", "value") VALUES ('isat.restore.failed', 'recipients', 'default');
INSERT INTO "config_props" ("category", "property_key", "value") VALUES ('isat.restore.failed', 'delivery.mode', '1');
INSERT INTO "config_props" ("category", "property_key", "value") VALUES ('isat.restore.failed', 'subject', '[GTVAN] Restore Failed');
INSERT INTO "config_props" ("category", "property_key", "value") 
VALUES ('isat.restore.failed', 'message', 
'Dear user,'||chr(13)||chr(10)||chr(13)||chr(10)||'Restore of the archive has failed at {0}.'||chr(13)||chr(10)||chr(13)||chr(10)||'Archive Summary File: {1}'||chr(13)||chr(10)||chr(13)||chr(10)||'Exception: {2}'||chr(13)||chr(10)||'Trace:'||chr(13)||chr(10)||'-------'||chr(13)||chr(10)||'{3}'||chr(13)||chr(10)||chr(13)||chr(10)||'Regards,'||chr(13)||chr(10)||'GTVAN System');

-- For ISAT archive notify queue
INSERT INTO "config_props" ("category", "property_key", "value") VALUES('isat.jms.archive.notify', 'destination.jndi', 'queue/gtvan/isat/archiveNotifyQueue');
INSERT INTO "config_props" ("category", "property_key", "value") VALUES('isat.jms.archive.notify', 'connection.factory.jndi', 'java:/JmsXA');
INSERT INTO "config_props" ("category", "property_key", "value") VALUES('isat.jms.archive.notify', 'java.naming.factory.initial', 'org.jnp.interfaces.NamingContextFactory');
INSERT INTO "config_props" ("category", "property_key", "value") VALUES('isat.jms.archive.notify', 'java.naming.provider.url', 'localhost:1100');
INSERT INTO "config_props" ("category", "property_key", "value") VALUES('isat.jms.archive.notify', 'java.naming.factory.url.pkgs', 'org.jboss.naming:org.jnp.interfaces');

-- For ISAT
INSERT INTO "config_props" ("category", "property_key", "value") VALUES('ISAT', 'reprocess.response.url', 'http://gtstage.inovis.com/txmr/confirmation/reprocess_status.jsp');
INSERT INTO "config_props" ("category", "property_key", "value") VALUES('ISAT', 'audit.archive.total.traceinfo.in.zip', '3000');
INSERT INTO "config_props" ("category", "property_key", "value") VALUES('ISAT', 'audit.archive.folder', 'gtvan/data/archive/');
INSERT INTO "config_props" ("category", "property_key", "value") VALUES('ISAT', 'audit.archive.total.process.in.zip', '100');
INSERT INTO "config_props" ("category", "property_key", "value") VALUES('ISAT', 'audit.email.alert.date.pattern', 'yyyy-MM-dd HH:mm:ss.SSS Z');

-- GENREPORT: The following properties are required for GENREPORT
INSERT INTO "config_props" ("category", "property_key", "value") VALUES('genreport', 'reportservice.url', 'http://gtstage.inovis.com/txmr/mainpage.jsp?included=');
INSERT INTO "config_props" ("category", "property_key", "value") VALUES('housekeep.report', 'frequency', 'Once');
INSERT INTO "config_props" ("category", "property_key", "value") VALUES('housekeep.report', 'next.delete.date', '2007-1-1');
INSERT INTO "config_props" ("category", "property_key", "value") VALUES('housekeep.report', 'archive.duration', '30');

INSERT INTO "config_props" ("category", "property_key", "value") VALUES('genreport', 'imageTitle', 'inovis.jpg');
INSERT INTO "config_props" ("category", "property_key", "value") VALUES('genreport', 'imageSmallLogo', 'smallLogo.jpg');
INSERT INTO "config_props" ("category", "property_key", "value") VALUES('genreport', 'template.datasource.1', 'DefaultDAO');
INSERT INTO "config_props" ("category", "property_key", "value") VALUES('genreport', 'template.datasource.2', 'UserDAO');
INSERT INTO "config_props" ("category", "property_key", "value") VALUES('genreport', 'email.body', 'Regards, GTVAN System');

-- 26 June 2007 TWX to support archive by customer
-- The MBean name that responsible to delegate the archive request to backend
INSERT INTO "config_props" ("category", "property_key", "value") VALUES ('isat.archive.singleton', 'mbean.name', 'gtvan.isat:service=ArchiveSingleton');

-- RMI adaptor for looking up archive singleton mbean
INSERT INTO "config_props" ("category", "property_key", "value") VALUES ('isat.archive.singleton', 'rmi.adaptor', 'jmx/invoker/SingletonRMIAdaptor');

-- HA JNDI Lookup
INSERT INTO "config_props" ("category", "property_key", "value") VALUES ('isat.jndi.lookup', 'java.naming.factory.initial', 'org.jnp.interfaces.NamingContextFactory');
INSERT INTO "config_props" ("category", "property_key", "value") VALUES ('isat.jndi.lookup', 'java.naming.provider.url', 'localhost:1100');
INSERT INTO "config_props" ("category", "property_key", "value") VALUES ('isat.jndi.lookup', 'java.naming.factory.url.pkgs', 'org.jboss.naming:org.jnp.interfaces');



---------------------------------------------------------------------------
---
--- Default data for 'isat_resource'
---
INSERT INTO "isat_resource"("type", "code", "value", "group_name") VALUES('Process Status','Running','Running','');
INSERT INTO "isat_resource"("type", "code", "value", "group_name") VALUES('Process Status','Complete','Complete','');
INSERT INTO "isat_resource"("type", "code", "value", "group_name") VALUES('Process Status','Abnormally Aborted','Abnormally Aborted','');
INSERT INTO "isat_resource"("type", "code", "value", "group_name") VALUES('Process Status','Abnormally Completed','Abnormally Completed','');
INSERT INTO "isat_resource"("type", "code", "value", "group_name") VALUES('Process Status','Abnormally Terminated','Abnormally Terminated','');

-- Process/ Doc Occuring Common
INSERT INTO "isat_resource"("type", "code", "value", "group_name") VALUES('Process Status','Time','0','Today');
INSERT INTO "isat_resource"("type", "code", "value", "group_name") VALUES('Process Status','Time','-24','Yesterday');
INSERT INTO "isat_resource"("type", "code", "value", "group_name") VALUES('Process Status','Time','-7','Last Week');
INSERT INTO "isat_resource"("type", "code", "value", "group_name") VALUES('Process Status','Time','-30','Last Month');
INSERT INTO "isat_resource"("type", "code", "value", "group_name") VALUES('Process Status','Time','-1','Last 1 Hour');
INSERT INTO "isat_resource"("type", "code", "value", "group_name") VALUES('Process Status','Time','-2','Last 2 Hours');
INSERT INTO "isat_resource"("type", "code", "value", "group_name") VALUES('Process Status','Time','30','This Month');
INSERT INTO "isat_resource"("type", "code", "value", "group_name") VALUES('Process Status','Time','7','This Week');

-- Event type shared by all customer
INSERT INTO "isat_resource"("type", "code", "value", "group_name") VALUES('Event Type', 'Document Received', 'Document Received','');
INSERT INTO "isat_resource"("type", "code", "value", "group_name") VALUES('Event Type', 'Unpack Payload', 'Unpack Payload','');
INSERT INTO "isat_resource"("type", "code", "value", "group_name") VALUES('Event Type', 'Document Delivery', 'Document Delivery','');
INSERT INTO "isat_resource"("type", "code", "value", "group_name") VALUES('Event Type', 'Pack Payload', 'Pack Payload','');
INSERT INTO "isat_resource"("type", "code", "value", "group_name") VALUES('Event Type', 'Document Acknowledged', 'Document Acknowledged','');
INSERT INTO "isat_resource"("type", "code", "value", "group_name") VALUES('Event Type', 'Document Import', 'Document Import','');
INSERT INTO "isat_resource"("type", "code", "value", "group_name") VALUES('Event Type', 'Outbound Processing Start', 'Outbound Processing Start','');
INSERT INTO "isat_resource"("type", "code", "value", "group_name") VALUES('Event Type', 'Outbound Processing End', 'Outbound Processing End','');
INSERT INTO "isat_resource"("type", "code", "value", "group_name") VALUES('Event Type', 'Export Processing Start', 'Export Processing Start','');
INSERT INTO "isat_resource"("type", "code", "value", "group_name") VALUES('Event Type', 'Export Processing End', 'Export Processing End','');
INSERT INTO "isat_resource"("type", "code", "value", "group_name") VALUES('Event Type', 'Mapping Rule', 'Mapping Rule','');
INSERT INTO "isat_resource"("type", "code", "value", "group_name") VALUES('Event Type', 'Document Export', 'Document Export','');
INSERT INTO "isat_resource"("type", "code", "value", "group_name") VALUES('Event Type', 'Procedure', 'Procedure','');
INSERT INTO "isat_resource"("type", "code", "value", "group_name") VALUES('Event Type', 'Channel Connectivity', 'Channel Connectivity','');
INSERT INTO "isat_resource"("type", "code", "value", "group_name") VALUES('Event Type', 'Reprocess Doc', 'Reprocess Doc','');
INSERT INTO "isat_resource"("type", "code", "value", "group_name") VALUES('Event Type', 'Process Injection', 'Process Injection','');
INSERT INTO "isat_resource"("type", "code", "value", "group_name") VALUES('Event Type', 'Document Received from Backend', 'Document Received from Backend','');
INSERT INTO "isat_resource"("type", "code", "value", "group_name") VALUES('Event Type', 'Document Received from Gateway', 'Document Received from Gateway','');
INSERT INTO "isat_resource"("type", "code", "value", "group_name") VALUES('Event Type', 'Document Delivery to Backend', 'Document Delivery to Backend','');
INSERT INTO "isat_resource"("type", "code", "value", "group_name") VALUES('Event Type', 'Document Delivery to Gateway', 'Document Delivery to Gateway','');

-- TWX20070514
-- This time lock is used in stored procedures that perform update on document transaction's doc_time_sent and doc_time_received
INSERT INTO "isat_resource"("type", "code", "value", "group_name") VALUES('lock','timelock','timelock','');

-- The process lock is used to avoid the concorrent creation of the same process transaction record
INSERT INTO "isat_resource"("type", "code", "value", "group_name") VALUES('lock','processlock','processlock','');

-- The archive summary lock is used to avoid concurrent access to the same summary file from multiple nodes
INSERT INTO "isat_resource"("type", "code", "value", "group_name") VALUES('lock','archiveSummLock', 'archiveSummLock', '');

---------------------------------------------------------------------------
---
--- Default user for 'adm_users'
---
INSERT INTO "adm_users" VALUES ('ADMIN','ADMIN','','admin','9v3/5IyQjesPTDvTbAMucg==','admin',CURRENT_TIMESTAMP(2),'Web Admin','GridNode',NULL,NULL,NULL,'offline','Yes',1);

--- user level
INSERT INTO "adm_user_level" ("level_name","level_id") VALUES ('Web Admin',1);
INSERT INTO "adm_user_level" ("level_name","level_id") VALUES ('Group Admin',2);
INSERT INTO "adm_user_level" ("level_name","level_id") VALUES ('Group User',3);

-- idle setting
INSERT INTO "adm_setting" VALUES (1, '1800');

-- default groups
INSERT INTO "adm_groups" VALUES ('CL etrade',1,'etrade');

INSERT INTO "isat_biz_entity_group_mapping" ("uid","be_id","group_name") VALUES ('1','ets','etrade');

---------------------------------------------------------------------------
---
--- Default data for 'schedule'
---
INSERT INTO "schedule" ("target", "method_name", "method_signature", "start_date", "period", "repetitions") VALUES('gtvan.httpbc:service=TxDeliveryService', 'processInTx', 'DATE', 'NOW', 10000, -1);
INSERT INTO "schedule" ("target", "method_name", "method_signature", "start_date", "period", "repetitions") VALUES('gtvan.httpbc:service=TxDeliveryService', 'processOutTx', 'DATE', 'NOW', 10000, -1);
-- The following schedule is for ISAT module
INSERT INTO "schedule" ("target", "method_name", "method_signature", "start_date", "period", "repetitions") VALUES('gtvan.isat:service=ArchiveSchedulerService', 'invokeArchiveScheduler', 'DATE', 'NOW', 60000, -1);
-- TWX20070514 Not required to have this anymore
--INSERT INTO "schedule" ("target", "method_name", "method_signature", "start_date", "period", "repetitions") VALUES('gtvan.isat:service=TrailInfoService', 'processAuditTrailData', '', 'NOW', 10000, -1);

-- Archive scheduler for keep track the archive status
INSERT INTO "schedule"("target", "method_name", "method_signature", "start_date", "period", "repetitions") VALUES('gtvan.isat:service=ArchiveService', 'checkArchiveStatus', '', 'NOW', 60000, -1);

-- The following schedule is for GENREPORT module
INSERT INTO "schedule" ("target", "method_name", "method_signature", "start_date", "period", "repetitions") VALUES('gtvan.genreport:service=ScheduleReportService', 'generateReport', 'DATE', 'NOW', 60000, -1);
INSERT INTO "schedule" ("target", "method_name", "method_signature", "start_date", "period", "repetitions") VALUES('gtvan.genreport:service=ScheduleReportService', 'deleteReport', '', 'NOW', 60000, -1);

---------------------------------------------------------------------------
---
--- Default data for 'rpt_report_type'
---
INSERT INTO "rpt_report_type" ("uid", "version", "report_type", "template_name", "datasource_type") VALUES ('fd563136-d907-4cb3-9544-d405a2dd6764', 0, 'Transaction Status Report', 'TransactionStatusTemplate', 1);
INSERT INTO "rpt_report_type" ("uid", "version", "report_type", "template_name", "datasource_type") VALUES ('7adacf4a-31be-4760-8c99-020e26809182', 0, 'Certificate Status Report', 'CertificateStatusTemplate', 2);


-- The JMS retry configuration
INSERT INTO "config_props" ("category", "property_key", "value") VALUES ('failed.jms', 'max.retry', '10');
INSERT INTO "config_props" ("category", "property_key", "value") VALUES ('failed.jms', 'retry.interval', '10000');
INSERT INTO "config_props" ("category", "property_key", "value") VALUES ('failed.jms', 'is.send.via.def', 'false');

-- The switch to revert back the JMS listener handling of redelivered msg (Some MD Bean logic is not able to handle duplicate JMS msg)
INSERT INTO "config_props" ("category", "property_key", "value") VALUES ('jms.handle.mode','jms.redelivered','false');

-- For HTTPBC processBackTxQueue JMS properties
INSERT INTO "config_props" ("category", "property_key", "value") VALUES('tx.delivery.jms', 'destination.jndi', 'queue/gtvan/ishb/processBackTxQueue');
INSERT INTO "config_props" ("category", "property_key", "value") VALUES('tx.delivery.jms', 'connection.factory.jndi', 'java:/JmsXA');
INSERT INTO "config_props" ("category", "property_key", "value") VALUES('tx.delivery.jms', 'java.naming.factory.initial', 'org.jnp.interfaces.NamingContextFactory');
INSERT INTO "config_props" ("category", "property_key", "value") VALUES('tx.delivery.jms', 'java.naming.provider.url', 'localhost:1100');
INSERT INTO "config_props" ("category", "property_key", "value") VALUES('tx.delivery.jms', 'java.naming.factory.url.pkgs', 'org.jboss.naming:org.jnp.interfaces');

-- For HTTPBC Tx Delivery Info Props
INSERT INTO "config_props" ("category", "property_key", "value") VALUES('tx.delivery.info', 'max.process.count.percall', '50');
INSERT INTO "config_props" ("category", "property_key", "value") VALUES('tx.delivery.info', 'max.failed.attempts.pertx', '10');
INSERT INTO "config_props" ("category", "property_key", "value") VALUES('tx.delivery.info', 'failed.attempt.alert.threshold', '5');
INSERT INTO "config_props" ("category", "property_key", "value") VALUES('tx.delivery.info', 'tx.delivery.mgr.jndi', 'ISHB_TransactionHandlerBean');
INSERT INTO "config_props" ("category", "property_key", "value") VALUES('tx.delivery.info', 'java.naming.factory.initial', 'org.jnp.interfaces.NamingContextFactory');
INSERT INTO "config_props" ("category", "property_key", "value") VALUES('tx.delivery.info', 'java.naming.provider.url', 'localhost:1100');
INSERT INTO "config_props" ("category", "property_key", "value") VALUES('tx.delivery.info', 'java.naming.factory.url.pkgs', 'org.jboss.naming:org.jnp.interfaces');

-- For HTTP BC global locking
INSERT INTO "config_props" ("category", "property_key", "value") VALUES('outgoingTransLock', 'outgoingTransLock', 'outgoingTransLock');
INSERT INTO "config_props" ("category", "property_key", "value") VALUES('incomingTransLock', 'incomingTransLock', 'incomingTransLock');


COMMIT;

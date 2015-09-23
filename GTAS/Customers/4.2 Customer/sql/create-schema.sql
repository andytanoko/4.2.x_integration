--- appdb Schema
CREATE ROLE appdb LOGIN ENCRYPTED PASSWORD 'gridnode';
CREATE SCHEMA appdb AUTHORIZATION appdb;

--- userdb schema
CREATE ROLE userdb LOGIN ENCRYPTED PASSWORD 'gridnode';
CREATE SCHEMA userdb AUTHORIZATION userdb;

--- jbossdb schema
CREATE ROLE jbossdb LOGIN ENCRYPTED PASSWORD 'gridnode';
CREATE SCHEMA jbossdb AUTHORIZATION jbossdb;

--- archivedb schema
CREATE ROLE archivedb LOGIN ENCRYPTED PASSWORD 'gridnode';
CREATE SCHEMA archivedb AUTHORIZATION archivedb;
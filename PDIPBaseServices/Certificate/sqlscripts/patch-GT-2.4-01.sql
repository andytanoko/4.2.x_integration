# 16 Jun 2004 
# Update Script for Certificate to modify the column attributes for
# a. SerialNum     - From: varchar(64)  to varchar(64) BINARY.
# b. IssuerName  - From : varchar(255) to varchar(255) BINARY.

USE userdb;

alter table certificate change SerialNum SerialNum varchar(64) BINARY default NULL;
alter table certificate change IssuerName IssuerName varchar(255) BINARY default NULL;


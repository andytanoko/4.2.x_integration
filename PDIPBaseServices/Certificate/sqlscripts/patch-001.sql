# 27 Feb 2003 I8 v2.0.16 [Jagadeesh] Update Script for Certificate to modify the column width for
#                                    a. SerialNum     - From: varchar(30)  to varchar(64).
#                                    b. IssuerName  - From : varchar(120) to varchar(255).

USE userdb;

alter table certificate modify column SerialNum varchar(64);

alter table certificate modify column IssuerName varchar(255);



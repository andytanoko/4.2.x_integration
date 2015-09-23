# 26 Jun 2003 I1 V2.1    [Neo Sok Lay] Change alert.Category and alert.Trigger to DEFAULT NULL.
#

USE userdb;

ALTER TABLE alert
MODIFY COLUMN Category bigint(20) DEFAULT NULL,
MODIFY COLUMN Trigger longtext DEFAULT NULL;

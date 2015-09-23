# 05 Mar 2004 GT 2.2 I5 [Neo Sok Lay] Increase Attachment filename length to 255.

USE userdb;

ALTER TABLE attachment
MODIFY Filename VARCHAR(255) NOT NULL,
MODIFY OriginalFilename VARCHAR(255) NOT NULL
;
 
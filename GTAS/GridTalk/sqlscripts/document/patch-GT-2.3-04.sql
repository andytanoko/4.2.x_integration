# 01 Jul 2004 GT 2.3.2a [Neo Sok Lay] Expand udocfilename fields to 255 chars, udocfullpath fields to text.

USE userdb;

ALTER TABLE grid_document
MODIFY UdocFilename VARCHAR(255),
MODIFY RefUdocFilename VARCHAR(255),
MODIFY UdocFullPath TEXT NOT NULL DEFAULT '',
MODIFY ExportedUdocFullPath TEXT
;

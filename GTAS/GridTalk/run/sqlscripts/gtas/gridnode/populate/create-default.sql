SET client_encoding = 'UTF8';
SET check_function_bodies = false;
SET client_min_messages = warning;
SET search_path = appdb;

---
--- data for table 'gncategory'
---
INSERT INTO "gncategory" ("Code", "Name", "NodeUsage") VALUES ('GNC', 'GridNode Commercial', 'Live');
INSERT INTO "gncategory" ("Code", "Name", "NodeUsage") VALUES ('GNS', 'GridNode Support', 'Live');
INSERT INTO "gncategory" ("Code", "Name", "NodeUsage") VALUES ('GNL', 'GridNode Lab', 'Test');
INSERT INTO "gncategory" ("Code", "Name", "NodeUsage") VALUES ('GNT', 'GridNode Trial', 'Trial');
INSERT INTO "gncategory" ("Code", "Name", "NodeUsage") VALUES ('GNO', 'GridNode Other', 'Live');
INSERT INTO "gncategory" ("Code", "Name", "NodeUsage") VALUES ('GNM', 'GridNode Master', 'Server');

---20090818    Tam Wei Xiang      [GT4.2.1]    Added db index from GT4.1.4
---

SET client_encoding = 'UTF8';
SET check_function_bodies = false;
SET client_min_messages = warning;
SET search_path = userdb;


DROP index IF EXISTS "RNPROFILE_IDX2";
DROP index IF EXISTS "RNPROFILE_IDX3";
DROP index IF EXISTS "RNPROFILE_IDX4";

CREATE INDEX "RNPROFILE_IDX2" ON "rn_profile" ("ProcessDefName", "ProcessInstanceId", "ProcessOriginatorId", "IsRequestMsg");
CREATE INDEX "RNPROFILE_IDX3" ON "rn_profile" ("ProcessDefName", "UniqueValue", "ProcessOriginatorId", "IsRequestMsg");
CREATE INDEX "RNPROFILE_IDX4" ON "rn_profile" ("PIPInstanceIdentifier", "IsRequestMsg");


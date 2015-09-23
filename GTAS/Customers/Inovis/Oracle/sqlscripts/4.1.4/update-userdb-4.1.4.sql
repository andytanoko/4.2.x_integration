-- Apply following script to "USERDB" schema

--- index for "rtprocess"
CREATE INDEX "RTPROCESS_IDX2" ON "rtprocess" ("EngineType", "ProcessType", "StartTime"); 
CREATE INDEX "RTPROCESS_IDX3" ON "rtprocess" ("EngineType", "ProcessType");
CREATE INDEX "RTPROCESS_IDX4" ON "rtprocess" ("ProcessUId", "ProcessType", "UID");
CREATE INDEX "RTPROCESS_IDX5" ON "rtprocess" ("StartTime", "ProcessUId", "UID");
CREATE INDEX "RTPROCESS_IDX6" ON "rtprocess" ("EndTime", "ProcessUId", "UID");

--- Index for "rn_profile"  
CREATE INDEX "RNPROFILE_IDX2" ON "rn_profile" ("ProcessDefName", "ProcessInstanceId", "ProcessOriginatorId", "IsRequestMsg");
CREATE INDEX "RNPROFILE_IDX3" ON "rn_profile" ("ProcessDefName", "UniqueValue", "ProcessOriginatorId", "IsRequestMsg");
CREATE INDEX "RNPROFILE_IDX4" ON "rn_profile" ("PIPInstanceIdentifier", "IsRequestMsg");

--- Index for "grid_document"
CREATE INDEX "GRID_DOCUMENT_IDX1" ON "grid_document" ("DateTimeCreate", "Folder", "UdocDocType"); 
CREATE INDEX "GRID_DOCUMENT_IDX2" ON "grid_document" ("DateTimeCreate", "Folder", "UdocDocType", "SenderPartnerId", "RecipientPartnerId");
CREATE INDEX "GRID_DOCUMENT_IDX3" ON "grid_document" ("DateTimeCreate", "Folder", "UdocDocType", "SenderBizEntityId", "RecipientBizEntityId");

commit;
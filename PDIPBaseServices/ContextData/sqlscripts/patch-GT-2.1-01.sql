# 25 Mar 2004 GT 2.1.20 [Neo Sok Lay] Index data_contextdata table on ContextUId for better performance

USE userdb;

ALTER TABLE data_contextdata 
ADD UNIQUE data_context_idx (ContextUId);

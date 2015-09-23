# 25 Mar 2004 GT 2.1.20 [Neo Sok Lay] Index rtprocess, rtprocessdoc, rtrestriction, and rtactivity tables for 
#                                     better performance.

USE userdb;

ALTER TABLE rtactivity 
ADD INDEX rtactivity_processuid_i (RtProcessUId);

ALTER TABLE rtprocessdoc 
ADD INDEX documentid_i (DocumentId),
ADD INDEX rtbinarycollaborationuid_i (RtBinaryCollaborationUId),
ADD INDEX rtbusinesstransactionuid_i (RtBusinessTransactionUId);

ALTER TABLE rtprocess 
ADD INDEX parentrtactivityuid_i (ParentRtActivityUId);

ALTER TABLE rtrestriction 
ADD INDEX rtrestriction_processuid_i (RtProcessUId);

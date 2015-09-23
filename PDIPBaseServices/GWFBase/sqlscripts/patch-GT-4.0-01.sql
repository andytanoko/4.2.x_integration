# 06 Oct 2005 I1 v4.0 [Neo Sok Lay]  Set default values for bpss_proc_spec_entry various columns.

USE userdb;

ALTER TABLE bpss_proc_spec_entry
  ALTER SpecUId SET DEFAULT '0',
  ALTER EntryUId SET DEFAULT '0',
  ALTER ParentEntryUId SET DEFAULT '0';


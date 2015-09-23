USE userdb;

#
# Dumping data for table 'port'
# Modified by Wei Xiang 24 Aug 2005
# We no longer need the field 'AttachmentDir varchar(80) DEFAULT NULL'
# since GDOC will be stored in the same folder as UDOC
#
DELETE FROM port WHERE UID IN (-1);
INSERT INTO port 
(UID, PortName, Description, IsRfc,
RfcUid, HostDir, IsDiffFileName, IsOverwrite,
FileName, IsAddFileExt, FileExtType, FileExtValue, 
IsExportGdoc, CanDelete, Version, StartNum,
RolloverNum, NextNum, IsPadded, FixNumLength,
FileGrouping) 
VALUES 
(-1,'DEF','Default Port',0,
NULL,'port/DEF',0,1,
'',0,0,'',
0,0,1,0,
0,0,0,0,
2);


# 26 May 2003 I8 v2.1 [Koh Han Sing] Make correction to xpathMapping.xpathUid constraints field.
# 

use appdb;

UPDATE fieldmetainfo SET Constraints= 'type=foreign\r\nforeign.key=mappingFile.uid\r\nforeign.cached=false' WHERE LABEL='xpathMapping.xpathUid'
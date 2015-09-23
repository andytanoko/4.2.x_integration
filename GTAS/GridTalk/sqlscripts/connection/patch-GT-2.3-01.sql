# 04 May 2003 I1 v2.3  [Daniel D'Cotta] Change KEEP_ALIVE_INTERVAL to editable

USE appdb;

UPDATE fieldmetainfo
SET Presentation='displayable=true\r\neditable=true\r\nmandatory=true\r\n'
WHERE LABEL='networkSetting.keepAliveInterval';
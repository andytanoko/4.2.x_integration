# 26 Nov 2003 I4 v2.2 [Koh Han Sing] Add in new data type byte[] and byte[][].
# 01 Dec 2003 I4 v2.2 [Koh Han Sing] Add in username and password in SoapProcedure.
# 30 Dec 2003 I4 v2.2 [Neo Sok Lay] Incorrect objectname for DateFormat field in ParamDef fmi.

use appdb;

UPDATE fieldmetainfo 
set Constraints= 'type=enum\r\nparamDef.type.string=1\r\nparamDef.type.integer=2\r\nparamDef.type.long=3\r\nparamDef.type.double=4\r\nparamDef.type.boolean=5\r\nparamDef.type.date=6\r\nparamDef.type.object=7\r\nparamDef.type.datahandler=8\r\nparamDef.type.datahandlerArray=9\r\nparamDef.type.stringArray=10\r\nparamDef.type.byteArray=11\r\nparamDef.type.byteArrayArray=12\r\n' 
where objectname = '_type'
and entityobjectname = 'com.gridnode.pdip.base.userprocedure.model.ParamDef';

UPDATE fieldmetainfo 
SET Constraints= 'type=text\r\ntext.length.max=255' 
WHERE objectname = '_value'
and entityobjectname = 'com.gridnode.pdip.base.userprocedure.model.ParamDef';

INSERT INTO fieldmetainfo VALUES (NULL,'_username','USER_NAME','','java.lang.String','com.gridnode.pdip.base.userprocedure.model.SoapProcedure','soapProcedure.userName','80','0','0','0','1','0',1,'displayable=true\r\neditable=true\r\nmandatory=false\r\n\r\n','type=text\r\ntext.length.max=80');
INSERT INTO fieldmetainfo VALUES (NULL,'_password','PASSWORD','','java.lang.String','com.gridnode.pdip.base.userprocedure.model.SoapProcedure','soapProcedure.password','80','0','0','0','1','0',1,'displayable=true\r\neditable=true\r\nmandatory=false\r\n\r\n','type=text\r\ntext.length.max=80');

UPDATE fieldmetainfo
SET ObjectName='_dateFormat'
WHERE ObjectName='_dateFormate'
AND EntityObjectName='com.gridnode.pdip.base.userprocedure.model.ParamDef';


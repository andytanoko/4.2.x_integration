# 09 Jul 2003 I1 v2.2 [Koh Han Sing] Add in field GridDocField in UserProcedure.
# 

use appdb;

INSERT INTO fieldmetainfo VALUES 
(NULL,'_gridDocField','GRID_DOC_FIELD','GridDocField','java.lang.Integer',
'com.gridnode.pdip.base.userprocedure.model.UserProcedure','userProcedure.gridDocField',
0,'0','0','0','0','0',999,
'displayable=true\r\neditable=true\r\nmandatory=false\r\n',
'type=enum\r\ngridDocument.custom1=77\r\ngridDocument.custom2=78\r\ngridDocument.custom3=79\r\ngridDocument.custom4=80\r\ngridDocument.custom5=81\r\ngridDocument.custom6=82\r\ngridDocument.custom7=83\r\ngridDocument.custom8=84\r\ngridDocument.custom9=85\r\ngridDocument.custom10=86\r\n'
);

INSERT INTO entitymetainfo VALUES ('com.gridnode.pdip.base.userprocedure.model.SoapProcedure','SoapProcedure','');

INSERT INTO fieldmetainfo VALUES 
(NULL,'_methodName','METHOD_NAME','','java.lang.String',
'com.gridnode.pdip.base.userprocedure.model.SoapProcedure','soapProcedure.methodName',
80,'0','0','0','1','0',1,
'displayable=true\r\neditable=true\r\nmandatory=true\r\n',
'type=text\r\ntext.length.max=80');

INSERT INTO fieldmetainfo VALUES 
(NULL,'_type','TYPE','','java.lang.Integer',
'com.gridnode.pdip.base.userprocedure.model.SoapProcedure','soapProcedure.type',
0,'0','0','0','0','','999',
"displayable=false\r\neditable=false",
"type=enum\r\nsoapProcedure.type.executable=1\r\nsoapProcedure.type.java=2\r\nsoapProcedure.type.soap=3\r\n");

UPDATE fieldmetainfo 
set Constraints= 'type=enum\r\nuserProcedure.procType.executable=1\r\nuserProcedure.procType.java=2\r\nuserProcedure.procType.soap=3\r\n' 
where objectname = '_procType'
and entityobjectname = 'com.gridnode.pdip.base.userprocedure.model.UserProcedure';

UPDATE fieldmetainfo 
set Constraints= 'type=dynamic\r\ndynamic.types=shellExecutable;javaProcedure;soapProcedure\r\n' 
where objectname = '_procDef'
and entityobjectname = 'com.gridnode.pdip.base.userprocedure.model.UserProcedure';

UPDATE fieldmetainfo 
set Constraints= 'type=enum\r\nprocedureDefFile.type.executable=1\r\nprocedureDefFile.type.java=2\r\nprocedureDefFile.type.soap=3\r\n' 
where objectname = '_type'
and entityobjectname = 'com.gridnode.pdip.base.userprocedure.model.ProcedureDefFile';

update fieldmetainfo
set presentation='displayable=true\r\neditable=true\r\nmandatory=true\r\n'
where objectname = '_methodName'
and entityobjectname = 'com.gridnode.pdip.base.userprocedure.model.JavaProcedure';

UPDATE fieldmetainfo 
set Constraints= 'type=enum\r\njavaProcedure.type.executable=1\r\njavaProcedure.type.java=2\r\njavaProcedure.type.soap=3\r\n' 
where objectname = '_type'
and entityobjectname = 'com.gridnode.pdip.base.userprocedure.model.JavaProcedure';

UPDATE fieldmetainfo 
set Constraints= 'type=enum\r\nshellExecutable.type.executable=1\r\nshellExecutable.type.java=2\r\nshellExecutable.type.soap=3\r\n' 
where objectname = '_type'
and entityobjectname = 'com.gridnode.pdip.base.userprocedure.model.ShellExecutable';


use userdb;

alter table user_procedure add column GridDocField int(3) DEFAULT NULL;

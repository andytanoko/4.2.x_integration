# 13 June 2003 2.1.5 [Jagadeesh] 
# 1. Modified value,description field for ParamDef - increase the display length.
# 2. Modified Alert Field presentation 'mandatory=false', as alert is not Mandatory.
# 3. Add Table constarints for Procedure_Definition_File and User_Procedure.
# 


use appdb;


update fieldmetainfo set constraints = 'type=text\r\ntext.length.max=120'
where objectname = '_value' 
and entityobjectname = 'com.gridnode.pdip.base.userprocedure.model.ParamDef';

update fieldmetainfo 
set constraints = 'type=text\r\ntext.length.max=80'
where objectname = '_description' 
and entityobjectname = 'com.gridnode.pdip.base.userprocedure.model.ParamDef';


update fieldmetainfo
set presentation='displayable=true\r\neditable=true\r\nmandatory=false\r\n\r\n'
where objectname = '_alert'
and entityobjectname = 'com.gridnode.pdip.base.userprocedure.model.ReturnDef';


use userdb;

ALTER TABLE `procedure_definition_file` ADD PRIMARY KEY (UID);
ALTER TABLE `procedure_definition_file` ADD UNIQUE name_idx (Name);

ALTER TABLE `user_procedure` ADD PRIMARY KEY (UID);
ALTER TABLE `user_procedure` ADD UNIQUE name_idx (Name);
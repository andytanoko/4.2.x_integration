# 09 Sep 2003 I1 v2.2 [Daniel D'Cotta] Change ParamDef's DataValue to mandatory (will manually set it to non-mandatory based on required logic)
# 

use appdb;

UPDATE fieldmetainfo 
set Presentation= 'displayable=true\r\neditable=true\r\nmandatory=true\r\n'
where objectname = '_value'
and entityobjectname = 'com.gridnode.pdip.base.userprocedure.model.ParamDef';


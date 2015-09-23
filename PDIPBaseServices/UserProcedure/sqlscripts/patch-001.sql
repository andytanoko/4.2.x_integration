# 04 June 2003 2.1 [Jagadeesh] 
# 1. Modified Alert Filed to display Alert Description insted of Text Box.
#    

use appdb;

update fieldmetainfo
set constraints='type=foreign\r\nforeign.key=alert.uid\r\nforeign.display=alert.description\r\n'
where objectname = '_alert'
and entityobjectname = 'com.gridnode.pdip.base.userprocedure.model.ReturnDef';


update fieldmetainfo
set constraints='type=foreign\r\nforeign.key=alert.uid\r\nforeign.display=alert.description\r\n'
where objectname = '_defAlert'
and entityobjectname = 'com.gridnode.pdip.base.userprocedure.model.UserProcedure';


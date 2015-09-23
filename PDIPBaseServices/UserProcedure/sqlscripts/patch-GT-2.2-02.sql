# 02 Sep 2003 I1 v2.2 [Koh Han Sing] Add in new data type DataHandler, DataHandler[] and String[]. Add in new source type Attachments.
# 

use appdb;

UPDATE fieldmetainfo 
set Constraints= 'type=enum\r\nparamDef.source.userDefined=1\r\nparamDef.source.gdoc=2\r\nparamDef.source.udoc=3\r\nparamDef.source.attachments=4\r\n' 
where objectname = '_source'
and entityobjectname = 'com.gridnode.pdip.base.userprocedure.model.ParamDef';

UPDATE fieldmetainfo 
set Constraints= 'type=enum\r\nparamDef.type.string=1\r\nparamDef.type.integer=2\r\nparamDef.type.long=3\r\nparamDef.type.double=4\r\nparamDef.type.boolean=5\r\nparamDef.type.date=6\r\nparamDef.type.object=7\r\nparamDef.type.datahandler=8\r\nparamDef.type.datahandlerArray=9\r\nparamDef.type.stringArray=10\r\n' 
where objectname = '_type'
and entityobjectname = 'com.gridnode.pdip.base.userprocedure.model.ParamDef';


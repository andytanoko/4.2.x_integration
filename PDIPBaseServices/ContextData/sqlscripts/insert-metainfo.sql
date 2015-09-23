# MySQL-Front Dump 2.1
#
# Host: 127.0.0.1   Database: appdb
#--------------------------------------------------------
# Server version 4.0.0-alpha

use appdb;

#
# Dumping data for table 'entitymetainfo'
#

INSERT INTO entitymetainfo VALUES("com.gridnode.pdip.base.contextdata.entities.model.StringData","StringData","data_stringdata");
INSERT INTO entitymetainfo VALUES("com.gridnode.pdip.base.contextdata.entities.model.ByteData","ByteData","data_bytedata");
INSERT INTO entitymetainfo VALUES("com.gridnode.pdip.base.contextdata.entities.model.ContextData","ContextData","data_contextdata");


#
# Dumping data for table 'fieldmetainfo'
#



INSERT INTO fieldmetainfo VALUES(NULL,"_uId","UID","UID","java.lang.Long","com.gridnode.pdip.base.contextdata.entities.model.StringData","","0","0","0","0","0","","999",'displayable=false','');
INSERT INTO fieldmetainfo VALUES(NULL,"_data","DATA","Data","java.lang.String","com.gridnode.pdip.base.contextdata.entities.model.StringData","DATA","50","0","1","1","1","","1",'displayable=false','');
INSERT INTO fieldmetainfo VALUES(NULL,"_dataType","DATA_TYPE","DataType","java.lang.String","com.gridnode.pdip.base.contextdata.entities.model.StringData","DATA_TYPE","50","0","1","1","1","","2",'displayable=false','');

INSERT INTO fieldmetainfo VALUES(NULL,"_uId","UID","UID","java.lang.Long","com.gridnode.pdip.base.contextdata.entities.model.ByteData","","0","0","0","0","0","","999",'displayable=false','');
INSERT INTO fieldmetainfo VALUES(NULL,"_data","DATA","Data","byte[]","com.gridnode.pdip.base.contextdata.entities.model.ByteData","DATA","50","0","1","1","1","","1",'displayable=false','');
INSERT INTO fieldmetainfo VALUES(NULL,"_dataType","DATA_TYPE","DataType","java.lang.String","com.gridnode.pdip.base.contextdata.entities.model.ByteData","DATA_TYPE","50","0","1","1","1","","2",'displayable=false','');

INSERT INTO fieldmetainfo VALUES(NULL,"_uId","UID","UID","java.lang.Long","com.gridnode.pdip.base.contextdata.entities.model.ContextData","","0","0","0","0","0","","999",'displayable=false','');
INSERT INTO fieldmetainfo VALUES(NULL,"_contextUId","CONTEXT_UID","ContextUId","java.lang.Long","com.gridnode.pdip.base.contextdata.entities.model.ContextData","","0","0","0","0","0","","999",'displayable=false','');
INSERT INTO fieldmetainfo VALUES(NULL,"_contextData","CONTEXT_DATA","ContextData","byte[]","com.gridnode.pdip.base.contextdata.entities.model.ContextData","","0","0","0","0","0","","999",'displayable=false','');

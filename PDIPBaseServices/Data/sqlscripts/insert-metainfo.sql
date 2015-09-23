# MySQL-Front Dump 2.1
#
# Host: 127.0.0.1   Database: appdb
#--------------------------------------------------------
# Server version 4.0.0-alpha

use appdb;

#
# Dumping data for table 'entitymetainfo'
#
INSERT INTO entitymetainfo VALUES("com.gridnode.pdip.base.data.entities.model.BusinessDocument","BusinessDocument","data_businessdocument");
INSERT INTO entitymetainfo VALUES("com.gridnode.pdip.base.data.entities.model.DataItem","DataItem","data_dataitem");
INSERT INTO entitymetainfo VALUES("com.gridnode.pdip.base.data.entities.model.DataItemLocationMap","DataItemLocationMap","data_dataitemspecmap");
INSERT INTO entitymetainfo VALUES("com.gridnode.pdip.base.data.entities.model.StringData","StringData","data_stringdata");
INSERT INTO entitymetainfo VALUES("com.gridnode.pdip.base.data.entities.model.ByteData","ByteData","data_bytedata");
INSERT INTO entitymetainfo VALUES("com.gridnode.pdip.base.data.entities.model.ControlDocument","ControlDocument","data_controldocument");


#
# Dumping data for table 'fieldmetainfo'
#
INSERT INTO fieldmetainfo VALUES(NULL,"_uId","UID","UID","java.lang.Long","com.gridnode.pdip.base.data.entities.model.BusinessDocument","","0","0","0","0","0","","999",'displayable=false','');
INSERT INTO fieldmetainfo VALUES(NULL,"_name","NAME","Name","java.lang.String","com.gridnode.pdip.base.data.entities.model.BusinessDocument","NAME","50","0","1","1","1","","1",'displayable=false','');
INSERT INTO fieldmetainfo VALUES(NULL,"_conditionExpr","CONDITION_EXPR","ConditionExpr","java.lang.String","com.gridnode.pdip.base.data.entities.model.BusinessDocument","CONDITION_EXPR","50","0","1","1","1","","2",'displayable=false','');
INSERT INTO fieldmetainfo VALUES(NULL,"_specElement","SPEC_ELEMENT","SpecElement","java.lang.String","com.gridnode.pdip.base.data.entities.model.BusinessDocument","SPEC_ELEMENT","50","0","1","1","1","","3",'displayable=false','');
INSERT INTO fieldmetainfo VALUES(NULL,"_specLocation","SPEC_LOCATION","SpecLocation","java.lang.String","com.gridnode.pdip.base.data.entities.model.BusinessDocument","SPEC_LOCATION","50","0","1","1","1","","4",'displayable=false','');


INSERT INTO fieldmetainfo VALUES(NULL,"_uId","UID","UID","java.lang.Long","com.gridnode.pdip.base.data.entities.model.DataItem","","0","0","0","0","0","","999",'displayable=false','');
INSERT INTO fieldmetainfo VALUES(NULL,"_dataContextType","DATA_CONTEXT_TYPE","DataContextType","java.lang.String","com.gridnode.pdip.base.data.entities.model.DataItem","DATA_CONTEXT_TYPE","50","0","1","1","1","","1",'displayable=false','');
INSERT INTO fieldmetainfo VALUES(NULL,"_contextUId","CONTEXT_UID","ContextUId","java.lang.Long","com.gridnode.pdip.base.data.entities.model.DataItem","CONTEXT_UID","0","0","0","0","0","","2",'displayable=false','');
INSERT INTO fieldmetainfo VALUES(NULL,"_dataDefName","DATA_DEF_NAME","DataDefName","java.lang.String","com.gridnode.pdip.base.data.entities.model.DataItem","DATA_DEF_NAME","50","0","1","1","1","","3",'displayable=false','');
INSERT INTO fieldmetainfo VALUES(NULL,"_dataDefType","DATA_DEF_TYPE","DataDefType","java.lang.String","com.gridnode.pdip.base.data.entities.model.DataItem","DATA_DEF_TYPE","50","0","1","1","1","","3",'displayable=false','');
INSERT INTO fieldmetainfo VALUES(NULL,"_dataDefKey","DATA_DEF_KEY","DataDefKey","java.lang.String","com.gridnode.pdip.base.data.entities.model.DataItem","DATA_DEF_KEY","50","0","1","1","1","","3",'displayable=false','');
INSERT INTO fieldmetainfo VALUES(NULL,"_isCopy","IS_COPY","IsCopy","java.lang.Boolean","com.gridnode.pdip.base.data.entities.model.DataItem","IS_COPY","0","0","0","1","1","","4",'displayable=false','');


INSERT INTO fieldmetainfo VALUES(NULL,"_uId","UID","UID","java.lang.Long","com.gridnode.pdip.base.data.entities.model.DataItemLocationMap","","0","0","0","0","0","","999",'displayable=false','');
INSERT INTO fieldmetainfo VALUES(NULL,"_dataItemUId","DATA_ITEM_UID","DataItemUId","java.lang.Long","com.gridnode.pdip.base.data.entities.model.DataItemLocationMap","DATA_ITEM_UID","0","0","0","0","0","","1",'displayable=false','');
INSERT INTO fieldmetainfo VALUES(NULL,"_dataFieldName","DATA_FIELD_NAME","DataFieldName","java.lang.String","com.gridnode.pdip.base.data.entities.model.DataItemLocationMap","DATA_FIELD_NAME","50","0","1","1","1","","2",'displayable=false','');
INSERT INTO fieldmetainfo VALUES(NULL,"_dataLocation","DATA_LOCATION","DataLocation","java.lang.String","com.gridnode.pdip.base.data.entities.model.DataItemLocationMap","DATA_LOCATION","50","0","1","1","1","","2",'displayable=false','');


INSERT INTO fieldmetainfo VALUES(NULL,"_uId","UID","UID","java.lang.Long","com.gridnode.pdip.base.data.entities.model.StringData","","0","0","0","0","0","","999",'displayable=false','');
INSERT INTO fieldmetainfo VALUES(NULL,"_data","DATA","Data","java.lang.String","com.gridnode.pdip.base.data.entities.model.StringData","DATA","50","0","1","1","1","","1",'displayable=false','');
INSERT INTO fieldmetainfo VALUES(NULL,"_dataType","DATA_TYPE","DataType","java.lang.String","com.gridnode.pdip.base.data.entities.model.StringData","DATA_TYPE","50","0","1","1","1","","2",'displayable=false','');

INSERT INTO fieldmetainfo VALUES(NULL,"_uId","UID","UID","java.lang.Long","com.gridnode.pdip.base.data.entities.model.ByteData","","0","0","0","0","0","","999",'displayable=false','');
INSERT INTO fieldmetainfo VALUES(NULL,"_data","DATA","Data","byte[]","com.gridnode.pdip.base.data.entities.model.ByteData","DATA","50","0","1","1","1","","1",'displayable=false','');
INSERT INTO fieldmetainfo VALUES(NULL,"_dataType","DATA_TYPE","DataType","java.lang.String","com.gridnode.pdip.base.data.entities.model.ByteData","DATA_TYPE","50","0","1","1","1","","2",'displayable=false','');

INSERT INTO fieldmetainfo VALUES(NULL,"_uId","UID","UID","java.lang.Long","com.gridnode.pdip.base.data.entities.model.ControlDocument","","0","0","0","0","0","","999",'displayable=false','');
INSERT INTO fieldmetainfo VALUES(NULL,"_name","NAME","Name","java.lang.String","com.gridnode.pdip.base.data.entities.model.ControlDocument","NAME","50","0","1","1","1","","1",'displayable=false','');
INSERT INTO fieldmetainfo VALUES(NULL,"_valueLocation","VALUE_LOCATION","ValueLocation","java.lang.String","com.gridnode.pdip.base.data.entities.model.ControlDocument","VALUE_LOCATION","50","0","1","1","1","","2",'displayable=false','');

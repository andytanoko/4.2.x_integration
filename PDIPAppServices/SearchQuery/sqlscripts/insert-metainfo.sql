# MySQL-Front Dump 2.1
#
# Host: 127.0.0.1   Database: appdb
#--------------------------------------------------------
# Server version 4.0.0-alpha

use appdb;

#
# Dumping data for table 'entitymetainfo'
#
# Field 1 ObjectName: full qualified class name of the entity
# Field 2 EntityName: short class name of the entity
# Field 3 SqlName: table name of the entity

# entitymetainfo for search_query
DELETE from entitymetainfo WHERE EntityName IN ('SearchQuery', 'Condition');
INSERT INTO entitymetainfo VALUES("com.gridnode.pdip.app.searchquery.model.SearchQuery","SearchQuery","search_query");
INSERT INTO entitymetainfo VALUES("com.gridnode.pdip.app.searchquery.model.Condition","Condition","");

#
# Dumping data for table 'fieldmetainfo'
#
# UID bigint(20) NOT NULL auto_increment,
# ObjectName: field name in Entity class ,
# FieldName: field ID in Entity Interface class ,
# SqlName: Field column name in Table,
# ValueClass: field data type,
# EntityObjectName: full qualified class name of the entity
# Label: field display label
# Length: valid field length ,
# Proxy: '1' if proxy, '0' otherwise,,
# Mandatory: '1' if mandatory, '0' otherwise,
# Editable: '1' if editable, '0' otherwise
# Displayable: '1' if displayable, '0' otherwise
# OqlName: ,
# Sequence: default '999' ,
# Presentation: properties for presentation of the field
# Constraints: constraint imposed on the values of the field
#

# fieldmetainfo for mapping_file
DELETE from fieldmetainfo WHERE EntityObjectName LIKE '%SearchQuery';

INSERT INTO fieldmetainfo VALUES(
NULL,"_uId","UID","UID","java.lang.Long",
"com.gridnode.pdip.app.searchquery.model.SearchQuery","searchQuery.uid",
"0","0","0","0","0","","999",
"displayable=false\r\nmandatory=false\r\neditable=false",
"type=uid"
);
INSERT INTO fieldmetainfo VALUES(
NULL,"_version","VERSION","Version","java.lang.Double",
"com.gridnode.pdip.app.searchquery.model.SearchQuery","",
"0","0","0","0","0","","999",
"displayable=false\r\nmandatory=false\r\neditable=false",
"type=range"
);
INSERT INTO fieldmetainfo VALUES(
NULL,"_canDelete","CAN_DELETE","CanDelete","java.lang.Boolean",
"com.gridnode.pdip.app.searchquery.model.SearchQuery","",
"0","0","0","0","0","","999",
"displayable=false\r\nmandatory=false\r\neditable=false",
"type=enum\r\ncandelete.enabled=true\r\ncandelete.disabled=false"
);
INSERT INTO fieldmetainfo VALUES(
NULL,"_name","NAME","Name","java.lang.String",
"com.gridnode.pdip.app.searchquery.model.SearchQuery","searchQuery.name",
"30","0","1","0","1","","999",
"displayable=true\r\nmandatory=true\r\neditable=false\r\neditable.create=true",
"type=text\r\ntext.length.max=30"
);
INSERT INTO fieldmetainfo VALUES(
NULL,"_description","DESCRIPTION","Description","java.lang.String",
"com.gridnode.pdip.app.searchquery.model.SearchQuery","searchQuery.description",
"80","0","1","1","1","","999",
"displayable=true\r\nmandatory=true\r\neditable=true",
"type=text\r\ntext.length.max=80"
);
INSERT INTO fieldmetainfo VALUES(
NULL,"_createdBy","CREATED_BY","CreatedBy","java.lang.String",
"com.gridnode.pdip.app.searchquery.model.SearchQuery","searchQuery.createdBy",
"15","0","1","1","1","","999",
"displayable=true\r\nmandatory=true\r\neditable=false",
"type=text\r\ntext.length.max=15"
);
INSERT INTO fieldmetainfo VALUES(
NULL,"_conditions","CONDITIONS","Conditions","java.util.ArrayList",
"com.gridnode.pdip.app.searchquery.model.SearchQuery","searchQuery.conditions",
"0","0","1","1","1","","999",
"displayable=true\r\nmandatory=true\r\neditable=true",
"type=embedded\r\nembedded.type=condition\r\ncollection=true"
);


# fieldmetainfo for condition
DELETE from fieldmetainfo WHERE EntityObjectName LIKE '%Condition';

INSERT INTO fieldmetainfo VALUES(
NULL,"_field","FIELD","","java.lang.Number",
"com.gridnode.pdip.app.searchquery.model.Condition","condition.field",
"0","0","1","0","1","","999",
"displayable=true\r\nmandatory=true\r\neditable=true",
"type=range"
);
INSERT INTO fieldmetainfo VALUES(
NULL,"_xpath","XPATH","","java.lang.String",
"com.gridnode.pdip.app.searchquery.model.Condition","condition.xpath",
"0","0","1","0","1","","999",
"displayable=true\r\nmandatory=true\r\neditable=true",
"type=text\r\ntext.length.max=255"
);
INSERT INTO fieldmetainfo VALUES(
NULL,"_operator","OPERATOR","","java.lang.Short",
"com.gridnode.pdip.app.searchquery.model.Condition","condition.operator",
"0","0","1","1","1","","999",
"displayable=true\r\nmandatory=true\r\neditable=true",
"type=enum\r\ncondition.operator.equal=0\r\ncondition.operator.notEqual=1\r\ncondition.operator.less=2\r\ncondition.operator.lessOrEqual=3\r\ncondition.operator.greater=4\r\ncondition.operator.greaterOrEqual=5\r\ncondition.operator.in=6\r\ncondition.operator.between=7\r\ncondition.operator.like=9\r\n"
);
INSERT INTO fieldmetainfo VALUES(
NULL,"_values","VALUES","","java.util.ArrayList",
"com.gridnode.pdip.app.searchquery.model.Condition","condition.values",
"15","0","1","1","1","","999",
"displayable=true\r\nmandatory=true\r\neditable=true",
"type=text\r\ncollection=true\r\ncollection.element=java.lang.Object"
);
INSERT INTO fieldmetainfo VALUES(
NULL,"_type","TYPE","","java.lang.Short",
"com.gridnode.pdip.app.searchquery.model.Condition","condition.type",
"0","0","1","1","1","","999",
"displayable=true\r\nmandatory=true\r\neditable=true",
"type=enum\r\ncondition.type.gdoc=1\r\ncondition.type.udoc=2"
);


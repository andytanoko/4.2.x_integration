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

# entitymetainfo for partner, partner_type, partner_group
INSERT INTO entitymetainfo VALUES("com.gridnode.pdip.app.partner.model.PartnerType","PartnerType","partner_type");
INSERT INTO entitymetainfo VALUES("com.gridnode.pdip.app.partner.model.PartnerGroup","PartnerGroup","partner_group");
INSERT INTO entitymetainfo VALUES("com.gridnode.pdip.app.partner.model.Partner","Partner","partner");

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
#

# fieldmetainfo for partner_type
INSERT INTO fieldmetainfo VALUES(NULL,"_uId","UID","UID","java.lang.Long","com.gridnode.pdip.app.partner.model.PartnerType","partnerType.uid","0","0","0","0","0","","999","displayable=false\r\neditable=false\r\nmandatory=false","type=uid");
INSERT INTO fieldmetainfo VALUES(NULL,"_version","VERSION","Version","java.lang.Double","com.gridnode.pdip.app.partner.model.PartnerType","","0","0","0","0","0","","999","displayable=false\r\neditable=false\r\nmandatory=false","type=range");
INSERT INTO fieldmetainfo VALUES(NULL,"_canDelete","CAN_DELETE","CanDelete","java.lang.Boolean","com.gridnode.pdip.app.partner.model.PartnerType","partnerType.canDelete","0","0","0","0","0","","999","displayable=false\r\neditable=false\r\nmandatory=false","type=enum\r\ncandelete.enabled=true\r\ncandelete.disabled=false");
INSERT INTO fieldmetainfo VALUES(NULL,"_name","NAME","Name","java.lang.String","com.gridnode.pdip.app.partner.model.PartnerType","partnerType.partnerType","3","0","1","0","1","","999","displayable=true\r\neditable=false\r\nmandatory=true\r\neditable.create=true","type=text\r\ntext.length.max=3");
INSERT INTO fieldmetainfo VALUES(NULL,"_description","DESCRIPTION","Description","java.lang.String","com.gridnode.pdip.app.partner.model.PartnerType","partnerType.description","80","0","1","1","1","","999","displayable=true\r\neditable=true\r\nmandatory=true\r\n","type=text\r\ntext.length.max=80");

# fieldmetainfo for partner_goup
INSERT INTO fieldmetainfo VALUES(NULL,"_uId","UID","UID","java.lang.Long","com.gridnode.pdip.app.partner.model.PartnerGroup","partnerGroup.uid","0","0","0","0","0","","999","displayable=false\r\neditable=false\r\nmandatory=false","type=uid");
INSERT INTO fieldmetainfo VALUES(NULL,"_version","VERSION","Version","java.lang.Double","com.gridnode.pdip.app.partner.model.PartnerGroup","","0","0","0","0","0","","999","displayable=false\r\neditable=false\r\nmandatory=false","type=range");
INSERT INTO fieldmetainfo VALUES(NULL,"_canDelete","CAN_DELETE","CanDelete","java.lang.Boolean","com.gridnode.pdip.app.partner.model.PartnerGroup","partnerGroup.canDelete","0","0","0","0","0","","999","displayable=false\r\neditable=false\r\nmandatory=false","type=enum\r\ncandelete.enabled=true\r\ncandelete.disabled=false");
INSERT INTO fieldmetainfo VALUES(NULL,"_name","NAME","Name","java.lang.String","com.gridnode.pdip.app.partner.model.PartnerGroup","partnerGroup.name","3","0","1","1","1","","999","displayable=true\r\neditable=false\r\nmandatory=true\r\neditable.create=true","type=text\r\ntext.length.max=3");
INSERT INTO fieldmetainfo VALUES(NULL,"_description","DESCRIPTION","Description","java.lang.String","com.gridnode.pdip.app.partner.model.PartnerGroup","partnerGroup.description","80","0","1","1","1","","999","displayable=true\r\neditable=true\r\nmandatory=true\r\n","type=text\r\ntext.length.max=80");
INSERT INTO fieldmetainfo VALUES(NULL,"_partnerType","PARTNER_TYPE","PartnerTypeUID","com.gridnode.pdip.app.partner.model.PartnerType","com.gridnode.pdip.app.partner.model.PartnerGroup","partnerGroup.partnerType","0","0","1","0","1","","999","displayable=true\r\neditable=false\r\nmandatory=true\r\neditable.create=true","type=foreign\r\nforeign.key=partnerType.uid\r\nforeign.display=partnerType.partnerType\r\nforeign.cached=true\r\nforeign.isAvailableInCache=false");

# fieldmetainfo for partner
INSERT INTO fieldmetainfo VALUES(NULL,"_uId","UID","UID","java.lang.Long","com.gridnode.pdip.app.partner.model.Partner","partner.uid","0","0","0","0","0","","999","displayable=false\r\neditable=false\r\nmandatory=false","type=uid");
INSERT INTO fieldmetainfo VALUES(NULL,"_partnerID","PARTNER_ID","PartnerID","java.lang.String","com.gridnode.pdip.app.partner.model.Partner","partner.partnerId","20","0","1","1","1","","999","displayable=true\r\neditable=false\r\nmandatory=true\r\neditable.create=true","type=text\r\ntext.length.max=20");
INSERT INTO fieldmetainfo VALUES(NULL,"_name","NAME","Name","java.lang.String","com.gridnode.pdip.app.partner.model.Partner","partner.name","20","0","1","1","1","","999","displayable=true\r\neditable=false\r\nmandatory=true\r\neditable.create=true","type=text\r\ntext.length.max=20");
INSERT INTO fieldmetainfo VALUES(NULL,"_description","DESCRIPTION","Description","java.lang.String","com.gridnode.pdip.app.partner.model.Partner","partner.description","80","0","1","1","1","","999","displayable=true\r\neditable=true\r\nmandatory=true\r\n","type=text\r\ntext.length.max=80");
INSERT INTO fieldmetainfo VALUES(NULL,"_partnerType","PARTNER_TYPE","PartnerTypeUID","com.gridnode.pdip.app.partner.model.PartnerType","com.gridnode.pdip.app.partner.model.Partner","partner.partnerType","0","0","1","1","1","","999","displayable=true\r\neditable=true\r\nmandatory=true","type=foreign\r\nforeign.key=partnerType.uid\r\nforeign.display=partnerType.partnerType\r\nforeign.cached=true");
INSERT INTO fieldmetainfo VALUES(NULL,"_partnerGroup","PARTNER_GROUP","PartnerGroupUID","com.gridnode.pdip.app.partner.model.PartnerGroup","com.gridnode.pdip.app.partner.model.Partner","partner.partnerGroup","0","0","0","1","1","","999","displayable=true\r\neditable=true\r\nmandatory=false","type=foreign\r\nforeign.key=partnerGroup.uid\r\nforeign.display=partnerGroup.name\r\nforeign.cached=true");
INSERT INTO fieldmetainfo VALUES(NULL,"_createTime","CREATE_TIME","CreateTime","java.util.Date","com.gridnode.pdip.app.partner.model.Partner","partner.created","0","0","0","0","1","","999","displayable=true\r\neditable=false\r\nmandatory=false\r\ndisplayable.create=false\r\ndisplayable.list=false","type=datetime\r\n");
INSERT INTO fieldmetainfo VALUES(NULL,"_createBy","CREATE_BY","CreateBy","java.lang.String","com.gridnode.pdip.app.partner.model.Partner","partner.creator","0","0","0","0","1","","999","displayable=true\r\neditable=false\r\nmandatory=false\r\ndisplayable.create=false\r\ndisplayable.list=false","type=text");
INSERT INTO fieldmetainfo VALUES(NULL,"_state","STATE","State","java.lang.Short","com.gridnode.pdip.app.partner.model.Partner","partner.state","1","0","1","1","1","","999","displayable=true\r\neditable=true\r\nmandatory=true","type=enum\r\npartner.state.disabled=0\r\npartner.state.enabled=1\r\npartner.state.deleted=2");
INSERT INTO fieldmetainfo VALUES(NULL,"_version","VERSION","Version","java.lang.Double","com.gridnode.pdip.app.partner.model.Partner","","0","0","0","0","0","","999","displayable=false\r\neditable=false\r\nmandatory=false","type=range");
INSERT INTO fieldmetainfo VALUES(NULL,"_canDelete","CAN_DELETE","CanDelete","java.lang.Boolean","com.gridnode.pdip.app.partner.model.Partner","partner.canDelete","0","0","0","0","0","","999","displayable=false\r\neditable=false\r\nmandatory=false","type=enum\r\ncandelete.enabled=true\r\ncandelete.disabled=false");


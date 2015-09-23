USE appdb;

# MySQL-Front Dump 2.1
#
# Host: 127.0.0.1   Database: appdb
#--------------------------------------------------------
# Server version 4.0.0-alpha


#
# Dumping data FOR TABLE 'entitymetainfo'
#
DELETE FROM entitymetainfo WHERE EntityName IN ("GFDefinition","GFTemplate");
INSERT INTO entitymetainfo VALUES("com.gridnode.pdip.app.gridform.model.GFDefinition","GFDefinition","gridform_definition");
INSERT INTO entitymetainfo VALUES("com.gridnode.pdip.app.gridform.model.GFTemplate","GFTemplate","gridform_template");


#
# Dumping data FOR TABLE 'fieldmetainfo'
#

### GFDefinition
DELETE FROM fieldmetainfo WHERE EntityObjectName LIKE "%GFDefinition";
INSERT INTO fieldmetainfo VALUES(NULL,"_uId","UID","UID","java.lang.Long","com.gridnode.pdip.app.gridform.model.GFDefinition","definition.uid","0","0","0","0","0","","999","displayable=false\r\neditable=false\r\nmandatory=false","type=uid");
INSERT INTO fieldmetainfo VALUES(NULL,"_name","NAME","Name","java.lang.String","com.gridnode.pdip.app.gridform.model.GFDefinition","definition.name","50","0","1","1","1","","1","displayable=true\r\nmandatory=true\r\neditable=true","type=text\r\ntext.length.min=1\r\ntext.length.max=50");
INSERT INTO fieldmetainfo VALUES(NULL,"_filename","FILENAME","Filename","java.lang.String","com.gridnode.pdip.app.gridform.model.GFDefinition","definition.filename","80","0","1","1","1","","2","displayable=true\r\nmandatory=true\r\neditable=true","type=file\r\nfile.downloadable=true\r\nfile.pathKey=gridform.path.ldf");
INSERT INTO fieldmetainfo VALUES(NULL,"_template","TEMPLATE","TemplateUID","com.gridnode.pdip.app.gridform.model.GFTemplate","com.gridnode.pdip.app.gridform.model.GFDefinition","definition.template","0","0","1","1","1","","3","displayable=true\r\neditable=true\r\nmandatory=true","type=foreign\r\nforeign.key=template.uid\r\nforeign.display=template.name\r\nforeign.cached=true");
INSERT INTO fieldmetainfo VALUES(NULL,"_version","VERSION","Version","java.lang.Double","com.gridnode.pdip.app.gridform.model.GFDefinition","definition.version","0","0","0","0","0","","999","displayable=false\r\neditable=false\r\nmandatory=false","type=range");
INSERT INTO fieldmetainfo VALUES(NULL,"_canDelete","CAN_DELETE","CanDelete","java.lang.Boolean","com.gridnode.pdip.app.gridform.model.GFDefinition","definition.canDelete","0","0","0","0","0","","999","displayable=false\r\neditable=false\r\nmandatory=false","type=enum\r\ncandelete.enabled=true\r\ncandelete.disabled=false");



### GFTemplate
DELETE FROM fieldmetainfo WHERE EntityObjectName LIKE "%GFTemplate";
INSERT INTO fieldmetainfo VALUES(NULL,"_uId","UID","UID","java.lang.Long","com.gridnode.pdip.app.gridform.model.GFTemplate","template.uid","0","0","0","0","0","","999","displayable=false\r\neditable=false\r\nmandatory=false","type=uid");
INSERT INTO fieldmetainfo VALUES(NULL,"_name","NAME","Name","java.lang.String","com.gridnode.pdip.app.gridform.model.GFTemplate","template.name","50","0","0","0","1","","1","displayable=true\r\nmandatory=true\r\neditable=true","type=text\r\ntext.length.min=1\r\ntext.length.max=50");
INSERT INTO fieldmetainfo VALUES(NULL,"_filename","FILENAME","Filename","java.lang.String","com.gridnode.pdip.app.gridform.model.GFTemplate","template.filename","80","0","1","1","1","","2","displayable=true\r\nmandatory=true\r\neditable=true\r\n","type=file\r\nfile.downloadable=true\r\nfile.pathKey=gridform.path.ltf");
INSERT INTO fieldmetainfo VALUES(NULL,"_version","VERSION","Version","java.lang.Double","com.gridnode.pdip.app.gridform.model.GFTemplate","template.version","0","0","0","0","0","","999","displayable=false\r\neditable=false\r\nmandatory=false","type=range");
INSERT INTO fieldmetainfo VALUES(NULL,"_canDelete","CAN_DELETE","CanDelete","java.lang.Boolean","com.gridnode.pdip.app.gridform.model.GFTemplate","template.canDelete","0","0","0","0","0","","999","displayable=false\r\neditable=false\r\nmandatory=false","type=enum\r\ncandelete.enabled=true\r\ncandelete.disabled=false");

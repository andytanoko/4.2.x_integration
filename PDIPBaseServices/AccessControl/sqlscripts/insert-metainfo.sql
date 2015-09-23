# Change History
# 25 Feb 2004 GT 2.1 [Neo Sok Lay] Display Feature description in AccessRight.

USE appdb;

#
# Dumping data FOR TABLE 'entitymetainfo'
#
DELETE FROM entitymetainfo WHERE EntityName IN ("Role","SubjectRole","Feature","AccessRight");
INSERT INTO entitymetainfo VALUES("com.gridnode.pdip.base.acl.model.Role","Role","role");
INSERT INTO entitymetainfo VALUES("com.gridnode.pdip.base.acl.model.SubjectRole","SubjectRole","subject_role");
INSERT INTO entitymetainfo VALUES("com.gridnode.pdip.base.acl.model.Feature","Feature","feature");
INSERT INTO entitymetainfo VALUES("com.gridnode.pdip.base.acl.model.AccessRight","AccessRight","access_right");


#
# Dumping data FOR TABLE 'fieldmetainfo'
#

### Role
DELETE FROM fieldmetainfo WHERE EntityObjectName LIKE "%Role";
INSERT INTO fieldmetainfo VALUES(NULL,"_uId","UID","UID","java.lang.Long","com.gridnode.pdip.base.acl.model.Role","role.uid","20","0","0","0","0","0","999","displayable=false\r\neditable=false\r\nmandatory=false\r\n","type=uid\r\n");
INSERT INTO fieldmetainfo VALUES(NULL,"_role","ROLE","Role","java.lang.String","com.gridnode.pdip.base.acl.model.Role","role.role","30","0","1","1","1","0","1","displayable=true\r\neditable=false\r\neditable.create=true\r\nmandatory=true\r\n","type=text\r\ntext.length.min=2\r\ntext.length.max=30\r\n");
INSERT INTO fieldmetainfo VALUES(NULL,"_description","DESCRIPTION","Description","java.lang.String","com.gridnode.pdip.base.acl.model.Role","role.description","255","0","1","1","1","0","1","displayable=true\r\neditable=true\r\nmandatory=true\r\n","type=text\r\ntext.length.min=1\r\ntext.length.max=255\r\n");
INSERT INTO fieldmetainfo VALUES(NULL,"_canDelete","CAN_DELETE","CanDelete","java.lang.Boolean","com.gridnode.pdip.base.acl.model.Role","role.canDelete","0","0","1","1","1","0","999","displayable=true\r\neditable=false\r\nmandatory=false\r\n","type=enum\r\ngeneric.yes=true\r\ngeneric.no=false\r\n");
INSERT INTO fieldmetainfo VALUES(NULL,"_version","VERSION","Version","java.lang.Double","com.gridnode.pdip.base.acl.model.Role","role.version","0","0","1","1","1","0","999","displayable=false\r\neditable=false\r\nmandatory=false\r\n","type=range\r\n");

### Subject Role, NOTE: must be after Role.
DELETE FROM fieldmetainfo WHERE EntityObjectName LIKE "%SubjectRole";
INSERT INTO fieldmetainfo VALUES(NULL,"_version","VERSION","Version","java.lang.Double","com.gridnode.pdip.base.acl.model.SubjectRole","","0","0","1","1","1","0","999","","");
INSERT INTO fieldmetainfo VALUES(NULL,"_subjectType","SUBJECT_TYPE","SubjectType","java.lang.String","com.gridnode.pdip.base.acl.model.SubjectRole","","30","0","1","1","1","0","1","","");
INSERT INTO fieldmetainfo VALUES(NULL,"_role","ROLE","Role","java.lang.Long","com.gridnode.pdip.base.acl.model.SubjectRole","","0","0","1","1","1","0","999","","");
INSERT INTO fieldmetainfo VALUES(NULL,"_subject","SUBJECT","Subject","java.lang.Long","com.gridnode.pdip.base.acl.model.SubjectRole","","0","0","1","1","1","0","999","","");
INSERT INTO fieldmetainfo VALUES(NULL,"_uId","UID","UID","java.lang.Long","com.gridnode.pdip.base.acl.model.SubjectRole","","0","0","0","1","0","0","999","","");

### ACCESS RIGHT
DELETE FROM fieldmetainfo WHERE EntityObjectName LIKE "%AccessRight";
INSERT INTO fieldmetainfo VALUES(NULL,"_uId","UID","UID","java.lang.Long","com.gridnode.pdip.base.acl.model.AccessRight","accessRight.uid","20","0","0","0","0","0","999","displayable=false\r\neditable=false\r\nmandatory=false\r\n","type=uid\r\n");
INSERT INTO fieldmetainfo VALUES(NULL,"_roleUID","ROLE","RoleUID","java.lang.Long","com.gridnode.pdip.base.acl.model.AccessRight","accessRight.role","20","0","1","1","1","0","2","displayable=true\r\neditable=false\r\neditable.create=true\r\nmandatory=true\r\n","type=foreign\r\nforeign.key=role.uid\r\nforeign.display=role.description\r\nforeign.cached=false\r\n");
INSERT INTO fieldmetainfo VALUES(NULL,"_feature","FEATURE","Feature","java.lang.String","com.gridnode.pdip.base.acl.model.AccessRight","accessRight.feature","50","0","1","1","1","0","3","displayable=true\r\neditable=true\r\nmandatory=true\r\n","type=foreign\r\nforeign.key=feature.feature\r\nforeign.display=feature.description\r\nforeign.cached=false\r\n");
INSERT INTO fieldmetainfo VALUES(NULL,"_descr","DESCRIPTION","Description","java.lang.String","com.gridnode.pdip.base.acl.model.AccessRight","accessRight.description","80","0","1","1","1","0","1","displayable=true\r\neditable=true\r\nmandatory=true\r\n","type=text\r\ntext.length.max=80\r\ntext.length.min=1\r\n");
INSERT INTO fieldmetainfo VALUES(NULL,"_action","ACTION","Action","java.lang.String","com.gridnode.pdip.base.acl.model.AccessRight","accessRight.actionName","30","0","1","1","1","0","4","displayable=true\r\neditable=true\r\nmandatory=true\r\n","type=text\r\n");
INSERT INTO fieldmetainfo VALUES(NULL,"_dataType","DATA_TYPE","DataType","java.lang.String","com.gridnode.pdip.base.acl.model.AccessRight","accessRight.dataType","30","0","0","1","1","0","5","displayable=true\r\neditable=true\r\nmandatory=false\r\n","type=text\r\n");
INSERT INTO fieldmetainfo VALUES(NULL,"_criteria","CRITERIA","Criteria","com.gridnode.pdip.framework.db.filter.IDataFilter","com.gridnode.pdip.base.acl.model.AccessRight","accessRight.criteria","0","0","0","1","1","0","6","displayable=true\r\neditable=true\r\nmandatory=false\r\n","type=other\r\n");
INSERT INTO fieldmetainfo VALUES(NULL,"_canDelete","CAN_DELETE","CanDelete","java.lang.Boolean","com.gridnode.pdip.base.acl.model.AccessRight","accessRight.canDelete","0","0","0","0","0","0","999","displayable=true\r\nmandatory=false\r\neditable=false\r\n","type=enum\r\ngeneric.yes=true\r\ngeneric.no=false\r\n");
INSERT INTO fieldmetainfo VALUES(NULL,"_version","VERSION","Version","java.lang.Double","com.gridnode.pdip.base.acl.model.AccessRight","accessRight.version","0","0","0","0","0","0","999","displayable=false\r\neditable=false\r\nmandatory=false\r\n","type=range\r\n");

### Feature
DELETE FROM fieldmetainfo WHERE EntityObjectName LIKE "%Feature";
INSERT INTO fieldmetainfo VALUES(NULL,"_version","VERSION","Version","java.lang.Double","com.gridnode.pdip.base.acl.model.Feature","feature.version","0","0","1","1","1","0","999","displayable=false\r\nmandatory=false\r\neditable=false\r\n","type=range\r\n");
INSERT INTO fieldmetainfo VALUES(NULL,"_uId","UID","UID","java.lang.Long","com.gridnode.pdip.base.acl.model.Feature","feature.uid","20","0","0","0","0","0","999","displayable=false\r\nmandatory=false\r\neditable=false\r\n","type=uid\r\n");
INSERT INTO fieldmetainfo VALUES(NULL,"_feature","FEATURE","Feature","java.lang.String","com.gridnode.pdip.base.acl.model.Feature","feature.feature","30","0","0","1","1","0","999","displayable=true\r\neditable=false\r\nmandatory=false\r\n","type=text\r\n");
INSERT INTO fieldmetainfo VALUES(NULL,"_description","DESCRIPTION","Description","java.lang.String","com.gridnode.pdip.base.acl.model.Feature","feature.description","80","0","0","1","1","0","999","displayable=true\r\neditable=false\r\nmandatory=false\r\n","type=text\r\n");
INSERT INTO fieldmetainfo VALUES(NULL,"_actions","ACTIONS","Actions","java.util.List","com.gridnode.pdip.base.acl.model.Feature","feature.actions","1024","0","0","1","1","0","999","displayable=true\r\neditable=false\r\nmandatory=false\r\n","type=text\r\ncollection=true\r\ncollection.element=java.lang.String\r\n");
INSERT INTO fieldmetainfo VALUES(NULL,"_dataTypes","DATA_TYPES","DataTypes","java.util.List","com.gridnode.pdip.base.acl.model.Feature","feature.dataTypes","1024","0","0","1","1","0","999","displayable=true\r\neditable=false\r\nmandatory=false\r\n","type=text\r\ncollection=true\r\ncollection.element=java.lang.String\r\n");

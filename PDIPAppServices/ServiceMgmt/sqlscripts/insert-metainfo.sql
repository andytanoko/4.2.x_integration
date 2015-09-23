USE appdb;


DELETE FROM entitymetainfo WHERE EntityName LIKE '%WebService';
DELETE FROM entitymetainfo WHERE EntityName LIKE '%ServiceAssignment';
INSERT INTO entitymetainfo VALUES("com.gridnode.pdip.app.servicemgmt.model.WebService","WebService","webservice");
INSERT INTO entitymetainfo VALUES("com.gridnode.pdip.app.servicemgmt.model.ServiceAssignment","ServiceAssignment","service_assignment");

# WebService
DELETE FROM fieldmetainfo WHERE EntityObjectName LIKE '%WebService';
INSERT INTO fieldmetainfo VALUES(NULL,"_uId","UID","UID","java.lang.Long","com.gridnode.pdip.app.servicemgmt.model.WebService","webService.uid","0","0","0","1","1","","999","displayable=false\r\nmandatory=false\r\neditable=false\r\n","type=uid\r\n");
INSERT INTO fieldmetainfo VALUES(NULL,"_wsdlUrl","WSDL_URL","WsdlUrl","java.lang.String","com.gridnode.pdip.app.servicemgmt.model.WebService","webService.wsdlUrl","0","0","0","1","1","","999","displayable=true\r\neditable=true\r\nmandatory=true\r\n","type=text\r\ntext.length.max=255\r\n");
INSERT INTO fieldmetainfo VALUES(NULL,"_endPoint","END_POINT","EndPoint","java.lang.String","com.gridnode.pdip.app.servicemgmt.model.WebService","webService.endPoint","0","0","0","1","1","","999","displayable=true\r\neditable=true\r\nmandatory=true\r\n","type=text\r\ntext.length.max=255\r\n");
INSERT INTO fieldmetainfo VALUES(NULL,"_serviceName","SERVICE_NAME","ServiceName","java.lang.String","com.gridnode.pdip.app.servicemgmt.model.WebService","webService.serviceName","0","0","0","1","1","","999","displayable=true\r\neditable=false\r\neditable.create=true\r\nmandatory=true\r\n","type=text\r\ntext.length.max=20\r\n");
INSERT INTO fieldmetainfo VALUES(NULL,"_serviceGroup","SERVICE_GROUP","ServiceGroup","java.lang.String","com.gridnode.pdip.app.servicemgmt.model.WebService","webService.serviceGroup","0","0","0","1","1","","999","displayable=true\r\neditable=false\r\nmandatory=true\r\n",'');
INSERT INTO fieldmetainfo VALUES(NULL,"_canDelete","CAN_DELETE","CanDelete","java.lang.Boolean","com.gridnode.pdip.app.servicemgmt.model.WebService","webService.canDelete","0","0","0","1","1","","999","displayable=true\r\neditable=false\r\nmandatory=false\r\n","type=enum\r\ngeneric.yes=true\r\ngeneric.no=false\r\n");
INSERT INTO fieldmetainfo VALUES (NULL, '_version', 'VERSION', 'Version', 'java.lang.Double', 'com.gridnode.pdip.app.servicemgmt.model.WebService', 'webService.version', 0, 0, 0, 1, 1, NULL, 999, 'displayable=false\r\neditable=false\r\nmandatory=false\r\n', 'type=range\r\n');


# ServiceAssignment
DELETE FROM fieldmetainfo WHERE EntityObjectName LIKE '%ServiceAssignment';
INSERT INTO fieldmetainfo VALUES(NULL,"_uId","UID","UID","java.lang.Long","com.gridnode.pdip.app.servicemgmt.model.ServiceAssignment","serviceAssignment.uid","0","0","0","1","1","","999","displayable=false\r\nmandatory=false\r\neditable=false\r\n","type=uid\r\n");
INSERT INTO fieldmetainfo VALUES(NULL,"_userName","USER_NAME","UserName","java.lang.String","com.gridnode.pdip.app.servicemgmt.model.ServiceAssignment","serviceAssignment.userName","0","0","0","1","1","","999","displayable=true\r\neditable=false\r\neditable.create=true\r\nmandatory=true\r\n","type=foreign\r\nforeign.key=partner.partnerId\r\nforeign.display=partner.name\r\nforeign.cached=false");
INSERT INTO fieldmetainfo VALUES(NULL,"_password","PASSWORD","Password","java.lang.String","com.gridnode.pdip.app.servicemgmt.model.ServiceAssignment","serviceAssignment.userPassword","0","0","0","1","1","","999","displayable=true\r\neditable=true\r\nmandatory=true\r\n","type=text\r\ntext.length.min=6\r\ntext.length.max=12\r\n");
INSERT INTO fieldmetainfo VALUES(NULL,"_userType","USER_TYPE","UserType","java.lang.String","com.gridnode.pdip.app.servicemgmt.model.ServiceAssignment","serviceAssignment.userType","0","0","0","1","1","","999","displayable=true\r\neditable=false\r\nmandatory=true\r\n",'');
INSERT INTO fieldmetainfo VALUES(NULL,"_webServiceUIds","WEBSERVICE_UIDS","WebServiceUIds","java.util.Collection","com.gridnode.pdip.app.servicemgmt.model.ServiceAssignment","serviceAssignment.webServiceUids","0","0","0","1","1","","999","displayable=true\r\neditable=true\r\nmandatory=false\r\n","type=foreign\r\nforeign.key=webService.uid\r\nforeign.display=webService.serviceName\r\nforeign.additionalDisplay=serviceGroup\r\nforeign.cached=false\r\ncollection=true\r\ncollection.element=java.lang.Long\r\n");
INSERT INTO fieldmetainfo VALUES(NULL,"_canDelete","CAN_DELETE","CanDelete","java.lang.Boolean","com.gridnode.pdip.app.servicemgmt.model.ServiceAssignment","serviceAssignment.canDelete","0","0","0","1","1","","999","displayable=true\r\neditable=false\r\nmandatory=false\r\n","type=enum\r\ngeneric.yes=true\r\ngeneric.no=false\r\n");
INSERT INTO fieldmetainfo VALUES (NULL, '_version', 'VERSION', 'Version', 'java.lang.Double', 'com.gridnode.pdip.app.servicemgmt.model.ServiceAssignment', 'serviceAssignment.version', 0, 0, 0, 1, 1, NULL, 999, 'displayable=false\r\neditable=false\r\nmandatory=false\r\n', 'type=range\r\n');
